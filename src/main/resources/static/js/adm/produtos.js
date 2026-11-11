window.onload = () =>{
    if(verificarAutorizacaoAdmin()) listarProdutos();
}

function listarProdutos(){
    $.ajax({
       method: "GET",
       url: "/produtos",
       success: function(dados){
           dados.forEach(item => criaLinha(item));
       }
    }).fail(function(err){
       alert("Erro ao listar: " + err.responseJSON.message);
    });
}

function criaLinha(dados){
    $('#tabelaProdutos').append(
        '<div class="linha-tabela-produtos" id="linha" name="linha">'+
            '<div class="coluna-1">'+
                '<p class="texto-tabela-produtos">'+ dados.nomeProduto +'</p>'+
            '</div>'+
            '<div class="coluna-2">'+
                '<p class="texto-tabela-produtos">'+ dados.marca +' — '+ dados.modelo +'</p>'+
            '</div>'+
            '<div class="coluna-3">'+
                '<p class="texto-tabela-produtos">R$ '+ dados.valorComDesconto.toFixed(2) +'</p>'+
            '</div>'+
            '<div class="coluna-4">'+
                '<p class="texto-tabela-produtos">'+ dados.quantidadeEstoque +'</p>'+
            '</div>'+
            '<div class="coluna-5">'+
                '<p class="texto-tabela-produtos">'+ dados.cor +'</p>'+
            '</div>'+
            '<div class="coluna-6">'+
                '<p class="texto-tabela-produtos">'+ dados.categoria +'</p>'+
            '</div>'+
            '<div class="coluna-7">'+
                '<button class="btn-editar" onclick="editar('+ dados.codigo +')">Editar</button>'+
                '<button class="btn-excluir" onclick="excluir('+ dados.codigo +')">Excluir</button>'+
            '</div>'+
        '</div>'
    );
}

function renderizarFormProdutos(opc){
    if(opc == 1){
        $('#containerCadastroProdutos').addClass('ativo');
        travarTela();
    }
    if(opc == 2){
        $('#containerCadastroProdutos').removeClass('ativo');
        destravarTela();
    }
}

function salvar(){
    let imagem = ($('#imagemEsconder').val().length) ? $('#imagemEsconder').val() : "nulo";
    $.ajax({
        method: "POST",
        url: "/produtos",
        data: JSON.stringify({
            codigo:            $('#codigoProduto').val(),
            nomeProduto:       $('#nomeProduto').val(),
            descricao:         $('#descricaoProduto').val(),
            imagem:            imagem,
            categoria:         $('#categoriaProduto').val(),
            marca:             $('#marcaProduto').val(),
            modelo:            $('#modeloProduto').val(),
            cor:               $('#corProduto').val(),
            garantia:          $('#garantiaProduto').val(),
            especificacoes:    $('#especificacoesProduto').val(),
            promocao:          $('#promocaoProduto').val(),
            quantidadeEstoque: $('#quantidadeProduto').val(),
            valorBase:         $('#valorProduto').val()
        }),
        contentType: "application/json; charset-utf8",
        beforeSend: function(xhr){
            xhr.setRequestHeader("Authorization", 'Bearer ' + localStorage.getItem('token'));
        }
    }).done(function(dados){
        if($("#upload")[0].files.length){
            salvarImagem(dados.codigo);
        } else {
            gerarMessageBox(1, "Produto salvo com sucesso!!", "Prosseguir");
        }
    }).fail(function(err){
        tratarErro(err);
    });
}

let data = new FormData();
document.getElementById("upload").onchange = function(e){
    if(e.target.files != null && e.target.files.length != 0){
        data = new FormData();
        data.append("imagem", e.target.files[0]);
    }
}

function salvarImagem(codigo){
    $.ajax({
        type: "POST",
        url: "/produtos/img/" + codigo,
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        beforeSend: function(xhr){
            xhr.setRequestHeader("Authorization", 'Bearer ' + localStorage.getItem('token'));
        }
    }).done(function(){
        gerarMessageBox(1, "Produto salvo com sucesso!!", "Prosseguir");
    }).fail(function(err){
        tratarErro(err);
    });
}

function editar(codigo){
    $.ajax({
        method: "GET",
        url: "/produtos/" + codigo,
        beforeSend: function(xhr){
            xhr.setRequestHeader("Authorization", 'Bearer ' + localStorage.getItem('token'));
        }
    }).done(function(dados){
        renderizarFormProdutos(1);
        $('#imagemEsconder').val(dados.imagem);
        $('#codigoProduto').val(dados.codigo);
        $('#nomeProduto').val(dados.nomeProduto);
        $('#descricaoProduto').val(dados.descricao);
        $('#categoriaProduto').val(dados.categoria);
        $('#marcaProduto').val(dados.marca);
        $('#modeloProduto').val(dados.modelo);
        $('#corProduto').val(dados.cor);
        $('#garantiaProduto').val(dados.garantia);
        $('#especificacoesProduto').val(dados.especificacoes);
        $('#promocaoProduto').val(dados.promocao);
        $('#quantidadeProduto').val(dados.quantidadeEstoque);
        $('#valorProduto').val(dados.valorBase);
    }).fail(function(err){
        tratarErro(err);
    });
}

function excluir(codigo){
    $.ajax({
        method: "DELETE",
        url: "/produtos/" + codigo,
        beforeSend: function(xhr){
            xhr.setRequestHeader("Authorization", 'Bearer ' + localStorage.getItem('token'));
        }
    }).done(function(){
        gerarMessageBox(1, "Produto deletado com sucesso!!", "Prosseguir");
    }).fail(function(err){
        tratarErro(err);
    });
}

function pesquisar(){
    if(!$('#pesquisarProduto').val().length) location.reload();
    limpar();
    $.ajax({
        method: "GET",
        url: "/produtos/busca/" + $('#pesquisarProduto').val(),
        success: function(dados){
            dados.forEach(item => criaLinha(item));
        }
    }).fail(function(err){
        alert("Erro ao listar: " + err.responseJSON.message);
    });
}

function limpar(){
    while($("[name='linha']").length) $('[name="linha"]').first().remove();
}