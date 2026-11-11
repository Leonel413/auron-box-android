package com.auronbox.app.viewmodel;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.auronbox.app.data.model.Produto;
import com.auronbox.app.data.model.ProdutoRequest;
import com.auronbox.app.data.repository.ProdutoRepository;
import com.auronbox.app.util.Fonte;
import com.auronbox.app.util.Resource;

import java.util.List;

public class AdminProdutoViewModel extends ViewModel {

    private final ProdutoRepository repo = new ProdutoRepository();
    private final Fonte<List<Produto>> lista = new Fonte<>();
    private final Fonte<Produto> produto = new Fonte<>();
    private String busca = "";

    public LiveData<Resource<List<Produto>>> getLista() {
        return lista.saida();
    }

    public LiveData<Resource<Produto>> getProduto() {
        return produto.saida();
    }

    public boolean carregado() {
        return !lista.vazia();
    }

    public void carregar(String termo) {
        busca = termo;
        carregar();
    }

    public void carregar() {
        lista.trocar(busca.isEmpty() ? repo.listar() : repo.buscar(busca));
    }

    public void carregarProduto(long codigo) {
        produto.trocar(repo.porId(codigo));
    }

    public LiveData<Resource<Produto>> salvar(ProdutoRequest request) {
        return repo.salvar(request);
    }

    public LiveData<Resource<String>> enviarImagem(Context context, long codigo, Uri uri) {
        return repo.enviarImagem(context, codigo, uri);
    }

    public LiveData<Resource<String>> excluir(long codigo) {
        return repo.excluir(codigo);
    }
}
