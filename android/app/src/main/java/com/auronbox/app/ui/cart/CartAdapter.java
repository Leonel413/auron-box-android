package com.auronbox.app.ui.cart;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.auronbox.app.R;
import com.auronbox.app.data.model.CarrinhoItem;
import com.auronbox.app.databinding.ItemCarrinhoBinding;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Formatador;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Holder> {

    public interface Listener {
        void alterar(CarrinhoItem item, int acao);

        void remover(CarrinhoItem item);
    }

    private final List<CarrinhoItem> itens = new ArrayList<>();
    private final Listener listener;

    public CartAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submit(List<CarrinhoItem> novos) {
        itens.clear();
        if (novos != null) itens.addAll(novos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemCarrinhoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CarrinhoItem item = itens.get(position);
        ItemCarrinhoBinding b = holder.b;
        b.titulo.setText(item.modelo);
        b.descricao.setText(item.descricaoProduto);
        b.cor.setText(Formatador.ou(item.cor, ""));
        b.precoUnitario.setText(Formatador.moeda(item.precoUnitario));
        b.quantidade.setText(String.valueOf(item.quantidade));
        b.subtotal.setText(Formatador.moeda(item.precoFinal));
        UiHelper.imagem(b.imagem, item.imagem);
        b.btnMenos.setOnClickListener(v -> listener.alterar(item, 2));
        b.btnMais.setOnClickListener(v -> listener.alterar(item, 1));
        b.btnRemover.setOnClickListener(v -> listener.remover(item));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ItemCarrinhoBinding b;

        Holder(ItemCarrinhoBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
