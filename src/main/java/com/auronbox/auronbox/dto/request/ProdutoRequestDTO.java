package com.auronbox.auronbox.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoRequestDTO {

    private Long codigo;
    private String nomeProduto;
    private String descricao;
    private String imagem;
    private String categoria;
    private String marca;
    private String modelo;
    private String cor;
    private String garantia;
    private String especificacoes;
    private Integer promocao;
    private Integer quantidadeEstoque;
    private String tamanho;
    private Double valorBase;
}
