package com.auronbox.app.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auronbox.app.AuronApp;
import com.auronbox.app.R;
import com.auronbox.app.data.model.Carrinho;
import com.auronbox.app.data.model.CarrinhoItem;
import com.auronbox.app.databinding.DialogCheckoutBinding;
import com.auronbox.app.databinding.FragmentCartBinding;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Formatador;
import com.auronbox.app.util.Resource;
import com.auronbox.app.viewmodel.CarrinhoViewModel;
import com.auronbox.app.viewmodel.PedidoViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private CarrinhoViewModel vm;
    private PedidoViewModel pedidoVm;
    private CartAdapter adapter;
    private Carrinho ultimo;
    private Resource<?> erroMostrado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(CarrinhoViewModel.class);
        pedidoVm = new ViewModelProvider(this).get(PedidoViewModel.class);

        adapter = new CartAdapter(new CartAdapter.Listener() {
            @Override
            public void alterar(CarrinhoItem item, int acao) {
                vm.alterar(item.codigo, acao);
            }

            @Override
            public void remover(CarrinhoItem item) {
                vm.remover(item.codigo);
            }
        });
        binding.lista.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.lista.setAdapter(adapter);

        binding.btnLimpar.setOnClickListener(v -> UiHelper.confirmar(requireContext(), R.string.limpar_carrinho, R.string.confirmar_limpar_carrinho, () -> vm.limpar()));
        binding.btnFinalizar.setOnClickListener(v -> abrirCheckout());

        vm.getCarrinho().observe(getViewLifecycleOwner(), this::renderizar);
        AuronApp.sessao().logado().observe(getViewLifecycleOwner(), logado -> {
            if (logado) {
                vm.carregar();
            } else {
                adapter.submit(new ArrayList<>());
                UiHelper.estadoLogin(binding.state, binding.conteudo, R.string.msg_login_carrinho_tela, () -> UiHelper.irParaLogin(this));
            }
        });
    }

    private void renderizar(Resource<Carrinho> r) {
        if (!AuronApp.sessao().estaLogado()) return;
        if (r.status == Resource.Status.SUCCESS && r.data != null) {
            ultimo = r.data;
            adapter.submit(r.data.itens);
            binding.subtotal.setText(Formatador.moeda(r.data.valorTotalItens));
            binding.total.setText(Formatador.moeda(r.data.valorTotalItens));
        }
        boolean tem = adapter.getItemCount() > 0;
        if (r.status == Resource.Status.ERROR && tem) {
            if (erroMostrado != r) {
                erroMostrado = r;
                UiHelper.erro(this, r);
            }
            UiHelper.esconderEstado(binding.state, binding.conteudo);
            return;
        }
        boolean vazio = r.status == Resource.Status.SUCCESS && !tem;
        UiHelper.estado(binding.state, binding.conteudo, r, tem, vazio, getString(R.string.carrinho_vazio), () -> vm.carregar());
    }

    private void abrirCheckout() {
        if (ultimo == null || ultimo.valorTotalItens <= 0) {
            UiHelper.dialogo(requireContext(), getString(R.string.titulo_atencao), getString(R.string.carrinho_vazio), null);
            return;
        }
        double total = ultimo.valorTotalItens;
        DialogCheckoutBinding d = DialogCheckoutBinding.inflate(getLayoutInflater());
        d.endereco.setText(AuronApp.sessao().endereco());
        d.total.setText(getString(R.string.total_valor, Formatador.moeda(total)));

        ArrayAdapter<CharSequence> formas = ArrayAdapter.createFromResource(requireContext(), R.array.formas_pagamento, android.R.layout.simple_spinner_item);
        formas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        d.forma.setAdapter(formas);
        d.forma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                montarParcelas(d, total, position == 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.finalizar_pedido)
                .setView(d.getRoot())
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.confirmar, (dialog, which) -> {
                    String forma = (String) d.forma.getSelectedItem();
                    int parcelas = d.parcelas.getSelectedItemPosition() + 1;
                    finalizar(forma, parcelas);
                })
                .show();
    }

    private void montarParcelas(DialogCheckoutBinding d, double total, boolean pix) {
        int maximo;
        if (pix) maximo = 1;
        else if (total < 300) maximo = 3;
        else if (total < 600) maximo = 6;
        else if (total < 1000) maximo = 9;
        else maximo = 12;
        List<String> opcoes = new ArrayList<>();
        for (int i = 1; i <= maximo; i++) opcoes.add(getString(R.string.parcela_opcao, i, Formatador.moeda(total / i)));
        ArrayAdapter<String> adapterParcelas = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, opcoes);
        adapterParcelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        d.parcelas.setAdapter(adapterParcelas);
    }

    private void finalizar(String forma, int parcelas) {
        pedidoVm.finalizar(forma, parcelas).observe(getViewLifecycleOwner(), r -> {
            binding.btnFinalizar.setEnabled(r.status != Resource.Status.LOADING);
            if (r.status == Resource.Status.ERROR) {
                UiHelper.erro(this, r);
            } else if (r.status == Resource.Status.SUCCESS) {
                String mensagem = r.data == null
                        ? getString(R.string.pedido_nao_concluido)
                        : getString(R.string.pedido_sucesso, r.data.numero);
                String titulo = getString(r.data == null ? R.string.titulo_erro : R.string.titulo_pedido_feito);
                UiHelper.dialogo(requireContext(), titulo, mensagem, () -> {
                    if (isAdded()) vm.carregar();
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
