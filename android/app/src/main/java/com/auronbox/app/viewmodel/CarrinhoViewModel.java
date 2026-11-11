package com.auronbox.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.auronbox.app.AuronApp;
import com.auronbox.app.data.model.Carrinho;
import com.auronbox.app.data.repository.CarrinhoRepository;
import com.auronbox.app.util.Fonte;
import com.auronbox.app.util.Resource;

public class CarrinhoViewModel extends ViewModel {

    private final CarrinhoRepository repo = new CarrinhoRepository();
    private final Fonte<Carrinho> carrinho = new Fonte<>();

    public LiveData<Resource<Carrinho>> getCarrinho() {
        return carrinho.saida();
    }

    public void carregar() {
        carrinho.trocar(comSessao(repo.listar()));
    }

    public LiveData<Resource<Carrinho>> adicionar(long produto) {
        return comSessao(repo.adicionar(produto));
    }

    public void alterar(long produto, int acao) {
        carrinho.trocar(comSessao(repo.alterar(produto, acao)));
    }

    public void remover(long produto) {
        carrinho.trocar(comSessao(repo.excluirItem(produto)));
    }

    public void limpar() {
        carrinho.trocar(comSessao(repo.excluirTodos()));
    }

    private LiveData<Resource<Carrinho>> comSessao(LiveData<Resource<Carrinho>> origem) {
        return Fonte.aoSucesso(origem, c -> {
            if (c != null) AuronApp.sessao().definirQuantidade(c.quantidadeItens);
        });
    }
}
