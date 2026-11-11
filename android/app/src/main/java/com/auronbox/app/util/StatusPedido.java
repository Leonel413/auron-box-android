package com.auronbox.app.util;

import com.auronbox.app.R;

public final class StatusPedido {

    public static final String PENDENTE = "Aguardando confirmação";
    public static final String CONFIRMADO = "Pedido confirmado";
    public static final String NEGADO = "Pedido negado";

    private StatusPedido() {
    }

    public static int cor(String status) {
        if (PENDENTE.equals(status)) return R.color.aviso;
        if (CONFIRMADO.equals(status)) return R.color.sucesso;
        return R.color.perigo;
    }
}
