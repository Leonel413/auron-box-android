package com.auronbox.app.data.model;

import java.util.List;

public class Pedido {
    public long codigo;
    public long codigoCliente;
    public long numero;
    public String data;
    public String status;
    public String motivoRejeicao;
    public String pagamento;
    public int quantidaeParcelas;
    public double valorParcela;
    public String nomeCliente;
    public String cpfCliente;
    public String celular;
    public String email;
    public String endereco;
    public List<CarrinhoItem> itens;
    public int quantidadeItens;
    public double valor;
}
