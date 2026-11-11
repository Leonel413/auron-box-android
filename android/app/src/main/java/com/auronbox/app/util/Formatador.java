package com.auronbox.app.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class Formatador {

    private static final Locale BR = Locale.forLanguageTag("pt-BR");

    private Formatador() {
    }

    public static String moeda(double valor) {
        return NumberFormat.getCurrencyInstance(BR).format(valor);
    }

    public static String primeiroNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) return "";
        return nome.trim().split("\\s+")[0];
    }

    public static String ou(String valor, String padrao) {
        return valor == null || valor.trim().isEmpty() ? padrao : valor;
    }

    public static String endereco(String cep, String cidade, String estado, String bairro, String rua, int numero, String complemento) {
        String base = cep + ", " + cidade + " - " + estado + ", " + bairro + ", " + rua + ", " + numero;
        return complemento == null || complemento.trim().isEmpty() ? base : base + ", " + complemento;
    }
}
