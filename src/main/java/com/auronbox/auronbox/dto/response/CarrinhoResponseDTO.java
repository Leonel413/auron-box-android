package com.auronbox.auronbox.dto.response;

import com.auronbox.auronbox.model.CarrinhoUsuarioModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CarrinhoResponseDTO {

    private List<CarrinhoUsuarioModel> itens;
    private Integer quantidadeItens;
    private Double valorTotalItens;
}
