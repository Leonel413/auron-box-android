window.onload = () => {
    alterarSessao();
    renderizarQuantidade(localStorage.getItem('quantidadeItens'));
    if(verificarLogin()) listarItens();
}

function listarItens(){
    while($("[name='linha']").length) $('[name="linha"]').first().remove();
    $.ajax({
        method: "GET",
        url: "carrinhos/" + localStorage.getItem('codigo'),
        beforeSend: function(xhr){
            xhr.setRequestHeader("Authorization", 'Bearer ' + localStorage.getItem('token'));
        }
    }).done(function(dados){
        dados.itens.forEach(item => listaItens(item));
        $('#totalCarrinho').html("R$ " + dados.valorTotalItens.toFixed(2));
        $('#subtotal').html("R$ " + dados.valorTotalItens.toFixed(2));
    }).fail(function(err){
        tratarErro(err);
    });
}

function adcionaItens(id){
    if(localStorage.getItem('logado') == 'logado'){
        $.ajax({
            method: "POST",
            url: "carrinhos/produto/" + id + "/usuario/" + localStorage.getItem('codigo'),
            beforeSend: function(xhr){
                xhr.setRequestHeader("Authorization", 'Bearer ' + localStorage.getItem('token'));
            }
        }).done(function(dados){
            renderizarQuantidade(dados.quantidadeItens);
        }).fail(function(err){
            tratarErro(err);
        });
    } else {
        gerarMessageBox(2, "É necessário fazer login para adicionar um item ao carrinho!", "Ok");
    }
}

function alterar(codigo, acao){
    $.ajax({
        method: "PUT",
        url: "carrinhos/produto/" + codigo + "/usuario/" + localStorage.getItem('codigo') + "/acao/" + acao,
        beforeSend: function(xhr){
            xhr.setRequestHeader("Authorization", 'Bearer ' + localStorage.getItem('token'));
        }
    }).done(function(dados){
        if($('#quantidadeItens-' + codigo).html() == 1 && acao == 2) listarItens();
        dados.itens.forEach(item => {
            $('#quantidadeItens-' + item.codigo).html(item.quantidade);
            $('#valorFinal-' + item.codigo).html("R$ " + item.precoFinal.toFixed(2));
        });
        $('#totalCarrinho').html("R$ " + dados.valorTotalItens.toFixed(2));
        $('#subtotal').html("R$ " + dados.valorTotalItens.toFixed(2));
        renderizarQuantidade(dados.quantidadeItens);
    }).fail(function(err){
        tratarErro(err);
    });
}

function excluir(){
    $.ajax({
        method: "DELETE",
        url: "carrinhos/usuario/" + localStorage.getItem('codigo'),
        beforeSend: function(xhr){
            xhr.setRequestHeader("Authorization", 'Bearer ' + localStorage.getItem('token'));
        }
    }).done(function(dados){
        listarItens();
        renderizarQuantidade(dados.quantidadeItens);
    }).fail(function(err){
        tratarErro(err);
    });
}

function listaItens(dados){
    var imagem = (dados.imagem && dados.imagem !== 'nulo') ? dados.imagem : 'icon/product.png';
    $('#itensCarrinho').append(
        '<tr class="linha-carrinho item-row" name="linha" id="linhaCarrinho">' +
            '<td class="coluna-1">' +
                '<div style="display:flex;align-items:center;gap:12px">' +
                    '<img src="' + imagem + '" class="item-img">' +
                    '<div>' +
                        '<p class="item-nome">' + dados.descricaoProduto + '</p>' +
                        '<p class="item-sub">' + (dados.cor || '') + '</p>' +
                    '</div>' +
                '</div>' +
            '</td>' +
            '<td class="coluna-2">' +
                '<div class="item-qty">' +
                    '<button class="qty-btn" onclick="alterar(' + dados.codigo + ', 2)">-</button>' +
                    '<span class="qty-num" id="quantidadeItens-' + dados.codigo + '">' + dados.quantidade + '</span>' +
                    '<button class="qty-btn" onclick="alterar(' + dados.codigo + ', 1)">+</button>' +
                '</div>' +
            '</td>' +
            '<td class="coluna-3"><span class="item-preco">R$ ' + dados.precoUnitario.toFixed(2) + '</span></td>' +
            '<td class="coluna-4 item-preco" id="valorFinal-' + dados.codigo + '">R$ ' + dados.precoFinal.toFixed(2) + '</td>' +
        '</tr>'
    );
}

function finalizarCompra(){
    if(localStorage.getItem('logado') != 'logado'){
        gerarMessageBox(2, "É necessário fazer login para finalizar o pedido!", "Ok");
        return;
    }
    if(!localStorage.getItem('quantidadeItens') || localStorage.getItem('quantidadeItens') == 0){
        gerarMessageBox(2, "Seu carrinho está vazio!", "Ok");
        return;
    }
    $.ajax({
        method: "POST",
        url: "/pedidos/cliente/" + localStorage.getItem('codigo') + "/formaPagamento/PIX/quantidadeParcelas/1",
        beforeSend: function(xhr){
            xhr.setRequestHeader("Authorization", 'Bearer ' + localStorage.getItem('token'));
        }
    }).done(function(dados){
        renderizarQuantidade(0);
        gerarMessageBox(1, "Pedido #" + dados.numero + " realizado com sucesso!", "Ok");
        listarItens();
    }).fail(function(err){
        tratarErro(err);
    });
}