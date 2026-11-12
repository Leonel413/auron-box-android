package com.auronbox.auronbox.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class CarrinhoPedidoModel {
    private Long codigo;
    private String imagem;
    private String descricaoProduto;
    private Integer quantidade;
    private String cor;
    private String marca;
    private String modelo;
    private Double precoUnitario;
    private Double precoFinal;
}
