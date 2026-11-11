package com.auronbox.app.ui.orders;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.auronbox.app.R;
import com.auronbox.app.data.model.CarrinhoItem;
import com.auronbox.app.databinding.ItemPedidoItemBinding;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Formatador;

import java.util.ArrayList;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.Holder> {

    private final List<CarrinhoItem> itens = new ArrayList<>();

    public void submit(List<CarrinhoItem> novos) {
        itens.clear();
        if (novos != null) itens.addAll(novos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemPedidoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CarrinhoItem item = itens.get(position);
        ItemPedidoItemBinding b = holder.b;
        b.titulo.setText(Formatador.ou(item.modelo, item.descricaoProduto));
        b.detalhe.setText(b.getRoot().getContext().getString(R.string.item_detalhe, item.quantidade, Formatador.moeda(item.precoUnitario)));
        b.subtotal.setText(Formatador.moeda(item.precoFinal));
        UiHelper.imagem(b.imagem, item.imagem);
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ItemPedidoItemBinding b;

        Holder(ItemPedidoItemBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
