package com.auronbox.app.ui.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.auronbox.app.R;
import com.auronbox.app.data.model.Pedido;
import com.auronbox.app.databinding.ItemAdminPedidoBinding;
import com.auronbox.app.util.Formatador;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AdminPedidoAdapter extends RecyclerView.Adapter<AdminPedidoAdapter.Holder> {

    private final List<Pedido> itens = new ArrayList<>();
    private final Consumer<Pedido> aoAbrir;

    public AdminPedidoAdapter(Consumer<Pedido> aoAbrir) {
        this.aoAbrir = aoAbrir;
    }

    public void submit(List<Pedido> novos) {
        itens.clear();
        if (novos != null) itens.addAll(novos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemAdminPedidoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Pedido p = itens.get(position);
        ItemAdminPedidoBinding b = holder.b;
        b.numero.setText("#" + p.numero);
        b.data.setText(p.data == null ? "" : p.data.replaceAll("\\s+", " "));
        b.resumo.setText(b.getRoot().getContext().getString(R.string.admin_pedido_resumo, p.quantidadeItens, Formatador.moeda(p.valor)));
        b.btnVer.setOnClickListener(v -> aoAbrir.accept(p));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ItemAdminPedidoBinding b;

        Holder(ItemAdminPedidoBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
