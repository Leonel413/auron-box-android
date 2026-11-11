window.onload = () => {
    alterarSessao();
    mostrarProduto();
    renderizarQuantidade(localStorage.getItem('quantidadeItens'));
}

function mostrarProduto(){
    $.ajax({
        method: "GET",
        url: "/produtos/" + localStorage.getItem('codigoProduto'),
        success: function(dados){
            // Imagem
            $('#imgCamisa').attr('src', dados.imagem);

            // Breadcrumb
            $('#breadcrumbCategoria').html(dados.categoria);
            $('#breadcrumbNome').html(dados.nomeProduto);

            // Badge de categoria
            $('#badgeCategoria').html(dados.categoria);

            // Nome — era dados.descricao + dados.tamanho
            $('#descricao').html(dados.nomeProduto);

            // Marca e modelo
            $('#marcaInfo').html(dados.marca);
            $('#modeloInfo').html(dados.modelo);

            // Descrição longa
            $('#descricaoLonga').html(dados.descricao);

            // Estoque
            if(dados.quantidadeEstoque > 0){
                $('#estoqueInfo').html('Em estoque (' + dados.quantidadeEstoque + ' unidades)');
            } else {
                $('#estoqueInfo').html('Fora de estoque');
                $('#estoqueInfo').css('color', 'var(--danger)');
                $('#adcionaItem').prop('disabled', true);
                $('#adcionaItem').css('opacity', '0.5');
            }

            // Preços
            $('#valorComDesconto').html(dados.valorComDesconto.toFixed(2));

            // Cor — era tamanhoInfo
            $('#corInfo').html(dados.cor);

            // Garantia
            $('#garantiaInfo').html(dados.garantia);

            // Especificações técnicas
            if(dados.especificacoes){
                // Quebra por vírgula e monta linhas da tabela
                let specs = dados.especificacoes.split(',');
                specs.forEach(spec => {
                    let partes = spec.split(':');
                    if(partes.length === 2){
                        $('#specsBody').append(
                            '<tr>' +
                                '<td>' + partes[0].trim() + '</td>' +
                                '<td>' + partes[1].trim() + '</td>' +
                            '</tr>'
                        );
                    }
                });
            }
        }
    }).fail(function(err){
        alert("Erro ao exibir produto: " + err.responseJSON.message);
    });
}

$('#adcionaItem').click(function(){
    adcionaItens(localStorage.getItem('codigoProduto'));
});