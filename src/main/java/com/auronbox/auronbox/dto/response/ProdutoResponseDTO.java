package com.auronbox.auronbox.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoResponseDTO {

    private Long codigo;
    private String nomeProduto;
    private String imagem;
    private String descricao;
    private Integer promocao;
    private Integer quantidadeEstoque;
    private String categoria;
    private String marca;
    private String modelo;
    private String cor;
    private String garantia;
    private String especificacoes;
    private Double valorBase;
    private Double valorComDesconto;
    private String dataPostagem;
}
