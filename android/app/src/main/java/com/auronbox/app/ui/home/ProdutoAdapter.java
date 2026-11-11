package com.auronbox.app.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.auronbox.app.data.model.Produto;
import com.auronbox.app.databinding.ItemProdutoBinding;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Formatador;

import java.util.ArrayList;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.Holder> {

    public interface Listener {
        void abrir(Produto produto);

        void adicionar(Produto produto);
    }

    private final List<Produto> itens = new ArrayList<>();
    private final Listener listener;

    public ProdutoAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submit(List<Produto> novos) {
        itens.clear();
        itens.addAll(novos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemProdutoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Produto p = itens.get(position);
        ItemProdutoBinding b = holder.b;
        b.nome.setText(p.nomeProduto + " — " + p.marca);
        b.preco.setText(Formatador.moeda(p.valorComDesconto));
        UiHelper.imagem(b.imagem, p.imagem);
        b.getRoot().setOnClickListener(v -> listener.abrir(p));
        b.btnAdicionar.setOnClickListener(v -> listener.adicionar(p));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ItemProdutoBinding b;

        Holder(ItemProdutoBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
