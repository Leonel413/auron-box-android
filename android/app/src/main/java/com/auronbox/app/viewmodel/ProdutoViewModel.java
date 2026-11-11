package com.auronbox.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.auronbox.app.data.model.Produto;
import com.auronbox.app.data.repository.ProdutoRepository;
import com.auronbox.app.util.Fonte;
import com.auronbox.app.util.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProdutoViewModel extends ViewModel {

    public static final String ORDEM_PADRAO = "";
    public static final String ORDEM_MENOR = "valorAsc";
    public static final String ORDEM_MAIOR = "valorDesc";

    private final ProdutoRepository repo = new ProdutoRepository();
    private final Fonte<List<Produto>> lista = new Fonte<>();
    private final Fonte<Produto> produto = new Fonte<>();
    private String categoria = "";
    private String busca = "";
    private String ordem = ORDEM_PADRAO;

    public LiveData<Resource<List<Produto>>> getLista() {
        return lista.saida();
    }

    public LiveData<Resource<Produto>> getProduto() {
        return produto.saida();
    }

    public String getBusca() {
        return busca;
    }

    public boolean carregado() {
        return !lista.vazia();
    }

    public void definirCategoria(String nova) {
        categoria = nova;
        carregar();
    }

    public void definirBusca(String nova) {
        busca = nova;
        carregar();
    }

    public void definirOrdem(String nova) {
        ordem = nova;
        carregar();
    }

    public void carregar() {
        lista.trocar(escolherChamada(), this::processar);
    }

    public void carregarProduto(long codigo) {
        produto.trocar(repo.porId(codigo));
    }

    private LiveData<Resource<List<Produto>>> escolherChamada() {
        if (!busca.isEmpty()) return repo.buscar(busca);
        if (!categoria.isEmpty()) return repo.porCategoria(categoria);
        if (ORDEM_MENOR.equals(ordem)) return repo.valorCrescente();
        if (ORDEM_MAIOR.equals(ordem)) return repo.valorDecrescente();
        return repo.listar();
    }

    private List<Produto> processar(List<Produto> origem) {
        List<Produto> saida = new ArrayList<>();
        for (Produto p : origem) {
            if (p.quantidadeEstoque <= 0) continue;
            if (!categoria.isEmpty() && !categoria.equalsIgnoreCase(p.categoria)) continue;
            saida.add(p);
        }
        if (ORDEM_MENOR.equals(ordem)) Collections.sort(saida, (a, b) -> Double.compare(a.valorComDesconto, b.valorComDesconto));
        if (ORDEM_MAIOR.equals(ordem)) Collections.sort(saida, (a, b) -> Double.compare(b.valorComDesconto, a.valorComDesconto));
        return saida;
    }
}
