package com.auronbox.app.ui.orders;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.auronbox.app.data.model.Pedido;
import com.auronbox.app.databinding.ItemPedidoBinding;
import com.auronbox.app.util.Formatador;
import com.auronbox.app.util.StatusPedido;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.Holder> {

    private final List<Pedido> itens = new ArrayList<>();
    private final Consumer<Pedido> aoAbrir;

    public PedidoAdapter(Consumer<Pedido> aoAbrir) {
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
        return new Holder(ItemPedidoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Pedido p = itens.get(position);
        ItemPedidoBinding b = holder.b;
        b.data.setText(p.data == null ? "" : p.data.replaceAll("\\s+", " "));
        b.numero.setText("#" + p.numero);
        b.status.setText(p.status);
        b.status.setTextColor(ContextCompat.getColor(b.getRoot().getContext(), StatusPedido.cor(p.status)));
        b.valor.setText(Formatador.moeda(p.valor));
        b.btnDetalhe.setOnClickListener(v -> aoAbrir.accept(p));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ItemPedidoBinding b;

        Holder(ItemPedidoBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
