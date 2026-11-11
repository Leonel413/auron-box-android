package com.auronbox.auronbox.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "produto")
public class ProdutoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo;

    @Column(length = 50, nullable = false)
    private String nomeProduto;

    @Column
    private String imagem;

    @Column(length = 400, nullable = false)
    private String descricao;

    @Column(length = 50, nullable = false)
    private String categoria;

    @Column(length = 50, nullable = false)
    private String marca;

    @Column(length = 80, nullable = false)
    private String modelo;

    @Column(length = 30)
    private String cor;

    @Column(length = 30)
    private String garantia;

    @Column(length = 1000)
    private String especificacoes;

    @Column(nullable = false)
    private Integer promocao;

    @Column(nullable = false)
    private Integer quantidadeEstoque;

    @Column(nullable = false)
    private Double valorBase;

    @Column(nullable = false)
    private Double valorComDesconto;

    @Column(length = 50, nullable = false)
    private String dataPostagem;
}