package com.auronbox.app.data.repository;

import androidx.lifecycle.LiveData;

import com.auronbox.app.AuronApp;
import com.auronbox.app.data.model.Carrinho;
import com.auronbox.app.data.remote.ApiClient;
import com.auronbox.app.util.Resource;

public class CarrinhoRepository extends BaseRepository {

    private long usuario() {
        return AuronApp.sessao().codigo();
    }

    public LiveData<Resource<Carrinho>> listar() {
        return executar(ApiClient.api().carrinho(usuario()));
    }

    public LiveData<Resource<Carrinho>> adicionar(long produto) {
        return executar(ApiClient.api().adicionarItem(produto, usuario()));
    }

    public LiveData<Resource<Carrinho>> alterar(long produto, int acao) {
        return executar(ApiClient.api().alterarItem(produto, usuario(), acao));
    }

    public LiveData<Resource<Carrinho>> excluirItem(long produto) {
        return executar(ApiClient.api().excluirItem(produto, usuario()));
    }

    public LiveData<Resource<Carrinho>> excluirTodos() {
        return executar(ApiClient.api().excluirItens(usuario()));
    }
}
