package com.auronbox.auronbox.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarrinhoUsuarioModel {
    private Long codigo;
    private String imagem;
    private String descricaoProduto;
    private Integer quantidade;
    private String cor;
    private String marca;
    private String modelo;
    private Double precoUnitario;
    private Double precoFinal;

    public CarrinhoUsuarioModel(Integer quantidade){
        this.quantidade = quantidade;
    }
}
