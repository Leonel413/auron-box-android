package com.auronbox.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.auronbox.app.AuronApp;
import com.auronbox.app.data.model.Pedido;
import com.auronbox.app.data.repository.PedidoRepository;
import com.auronbox.app.util.Fonte;
import com.auronbox.app.util.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PedidoViewModel extends ViewModel {

    private final PedidoRepository repo = new PedidoRepository();
    private final Fonte<List<Pedido>> lista = new Fonte<>();
    private final Fonte<Pedido> pedido = new Fonte<>();

    public LiveData<Resource<List<Pedido>>> getLista() {
        return lista.saida();
    }

    public LiveData<Resource<Pedido>> getPedido() {
        return pedido.saida();
    }

    public boolean carregado() {
        return !lista.vazia();
    }

    public void carregarDoCliente() {
        lista.trocar(repo.doCliente(), PedidoViewModel::maisRecentesPrimeiro);
    }

    public void carregarTodos() {
        lista.trocar(repo.todos(), PedidoViewModel::maisRecentesPrimeiro);
    }

    public void carregarPedido(long codigo) {
        pedido.trocar(repo.porId(codigo));
    }

    public LiveData<Resource<Pedido>> finalizar(String formaPagamento, int parcelas) {
        return Fonte.aoSucesso(repo.fazerPedido(formaPagamento, parcelas), p -> {
            if (p != null) AuronApp.sessao().definirQuantidade(0);
        });
    }

    public LiveData<Resource<Pedido>> alterarStatus(long codigo, int acao, String motivo) {
        return repo.alterarStatus(codigo, acao, motivo);
    }

    private static List<Pedido> maisRecentesPrimeiro(List<Pedido> origem) {
        List<Pedido> copia = new ArrayList<>(origem);
        Collections.reverse(copia);
        return copia;
    }
}
