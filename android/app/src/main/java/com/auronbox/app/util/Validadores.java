package com.auronbox.app.util;

public final class Validadores {

    private Validadores() {
    }

    public static boolean cpfValido(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) return false;
        boolean iguais = true;
        for (int i = 1; i < 11; i++) if (cpf.charAt(i) != cpf.charAt(0)) iguais = false;
        if (iguais) return false;
        return digito(cpf, 10) == cpf.charAt(9) - '0' && digito(cpf, 11) == cpf.charAt(10) - '0';
    }

    private static int digito(String cpf, int pesoInicial) {
        int soma = 0;
        for (int i = 0, peso = pesoInicial; peso >= 2; i++, peso--) soma += (cpf.charAt(i) - '0') * peso;
        int resto = 11 - (soma % 11);
        return resto >= 10 ? 0 : resto;
    }

    public static boolean nomeSemNumeros(String nome) {
        for (int i = 0; i < nome.length(); i++) if (Character.isDigit(nome.charAt(i))) return false;
        return true;
    }

    public static boolean emailGmail(String email) {
        return email != null && email.toLowerCase().endsWith("@gmail.com") && email.length() > 10;
    }
}
