package com.auronbox.app.ui.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.auronbox.app.R;
import com.auronbox.app.data.model.Produto;
import com.auronbox.app.databinding.ItemAdminProdutoBinding;
import com.auronbox.app.util.Formatador;

import java.util.ArrayList;
import java.util.List;

public class AdminProdutoAdapter extends RecyclerView.Adapter<AdminProdutoAdapter.Holder> {

    public interface Listener {
        void editar(Produto produto);

        void excluir(Produto produto);
    }

    private final List<Produto> itens = new ArrayList<>();
    private final Listener listener;

    public AdminProdutoAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submit(List<Produto> novos) {
        itens.clear();
        if (novos != null) itens.addAll(novos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemAdminProdutoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Produto p = itens.get(position);
        ItemAdminProdutoBinding b = holder.b;
        b.nome.setText(p.nomeProduto);
        b.marcaModelo.setText(b.getRoot().getContext().getString(R.string.marca_modelo, p.marca, p.modelo));
        b.detalhes.setText(b.getRoot().getContext().getString(R.string.admin_produto_detalhes,
                Formatador.moeda(p.valorComDesconto), p.quantidadeEstoque, Formatador.ou(p.cor, "-"), p.categoria));
        b.btnEditar.setOnClickListener(v -> listener.editar(p));
        b.btnExcluir.setOnClickListener(v -> listener.excluir(p));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ItemAdminProdutoBinding b;

        Holder(ItemAdminProdutoBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
