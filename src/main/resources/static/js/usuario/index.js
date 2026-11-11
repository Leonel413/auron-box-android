window.onload = () => {
    (!verificarLogin()) ? $("[name='sessao']").hide().first().show() : $("[name='sessao']").hide().last().show();
    renderizarQuantidade(localStorage.getItem('quantidadeItens'));
    listarProdutos();
}

function listarProdutos(){
    $.ajax({
        method: "GET",
        url: "/produtos",
        success: function(dados){
            dados.forEach(item => {
                if(item.quantidadeEstoque > 0) adcionaProduto(item, "section-produto");
            });
        }
    }).fail(function(err){
        tratarErro(err);
    });
}

function buscar(caminho, valor){
    if(valor.length){
        $.ajax({
            method: "GET",
            url: "/produtos/" + caminho + valor,
            success: function(dados){
                limparPesquisa();
                dados.forEach(item => {
                    if(item.quantidadeEstoque > 0) adcionaProduto(item, "section-pesquisa");
                });
                $('#titulo-pesquisa').append(
                    '<button onclick="location.reload()" class="btn-limpar-pesquisa" id="btnLimparPesquisa">Limpar pesquisa</button>'
                );
                if(!dados.length){
                    $('#section-pesquisa').append(
                        '<p class="texto-titulo-section" id="aviso">Sem resultados para sua pesquisa!!</p>'
                    );
                }
            }
        }).fail(function(err){
            tratarErro(err);
        });
    } else location.reload();
}

function adcionaProduto(dados, local){
    // imagem com fallback caso seja nulo ou vazio
    var imagem = (dados.imagem && dados.imagem !== 'nulo') ? dados.imagem : 'icon/product.png';

    $('#' + local).append(
        '<div class="produto" id="produto" name="produto">' +
            '<a type="button" class="link" onclick="pegarId(' + dados.codigo + ')" href="detalhes.html">' +
                '<div class="img-produto">' +
                    '<img src="' + imagem + '" height="90%">' +
                '</div>' +
                // era dados.descricao + dados.tamanho — agora usa nomeProduto + marca
                '<div class="desc-produto">' + dados.nomeProduto + ' — ' + dados.marca + '</div>' +
                '<div class="valor-produto">' +
                    '<p class="valor-desconto">R$ ' + dados.valorComDesconto.toFixed(2) + '</p>' +
                '</div>' +
            '</a>' +
            '<button class="adciona-carrinho" onclick="adcionaItens(' + dados.codigo + ')">Adicionar ao carrinho</button>' +
        '</div>'
    );
}

function limparPesquisa(){
    $('#tituloSection').remove();
    $('#section-produto').remove();
    $('#btnLimparPesquisa').remove();
    $('#aviso').remove();
    while($("[name='produto']").length) $('[name="produto"]').first().remove();
}

function pegarId(id){
    localStorage.setItem('codigoProduto', id);
}