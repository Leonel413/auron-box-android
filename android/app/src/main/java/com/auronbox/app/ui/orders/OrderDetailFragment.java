package com.auronbox.app.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auronbox.app.R;
import com.auronbox.app.data.model.Pedido;
import com.auronbox.app.databinding.DialogMotivoBinding;
import com.auronbox.app.databinding.FragmentOrderDetailBinding;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Formatador;
import com.auronbox.app.util.Resource;
import com.auronbox.app.util.StatusPedido;
import com.auronbox.app.viewmodel.PedidoViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class OrderDetailFragment extends Fragment {

    public static final String KEY_CODIGO = "codigo";
    public static final String KEY_ADMIN = "admin";
    public static final String RESULT_PEDIDO = "pedido_atualizado";

    private FragmentOrderDetailBinding binding;
    private PedidoViewModel vm;
    private OrderItemAdapter itensAdapter;
    private long codigo;
    private boolean admin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        codigo = requireArguments().getLong(KEY_CODIGO);
        admin = requireArguments().getBoolean(KEY_ADMIN, false);
        vm = new ViewModelProvider(this).get(PedidoViewModel.class);

        itensAdapter = new OrderItemAdapter();
        binding.itens.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.itens.setAdapter(itensAdapter);
        binding.btnConfirmar.setOnClickListener(v -> UiHelper.confirmar(requireContext(), R.string.confirmar_pedido, R.string.confirmar_pedido_msg, () -> alterarStatus(1, getString(R.string.motivo_confirmado))));
        binding.btnRecusar.setOnClickListener(v -> pedirMotivo());

        vm.getPedido().observe(getViewLifecycleOwner(), this::renderizar);
        if (vm.getPedido().getValue() == null) vm.carregarPedido(codigo);
    }

    private void renderizar(Resource<Pedido> r) {
        if (r.status == Resource.Status.SUCCESS && r.data != null) preencher(r.data);
        UiHelper.estado(binding.state, binding.conteudo, r, false, false, "", () -> vm.carregarPedido(codigo));
    }

    private void preencher(Pedido p) {
        binding.dados.removeAllViews();
        UiHelper.linha(binding.dados, getString(R.string.nome_cliente), p.nomeCliente);
        if (admin) UiHelper.linha(binding.dados, getString(R.string.cpf), p.cpfCliente);
        UiHelper.linha(binding.dados, getString(R.string.email), p.email);
        UiHelper.linha(binding.dados, getString(R.string.telefone), p.celular);
        UiHelper.linha(binding.dados, getString(R.string.numero_pedido), "#" + p.numero);
        UiHelper.linha(binding.dados, getString(R.string.data_pedido), p.data == null ? null : p.data.replaceAll("\\s+", " "));
        TextView status = UiHelper.linha(binding.dados, getString(R.string.status_pedido), p.status);
        status.setTextColor(ContextCompat.getColor(requireContext(), StatusPedido.cor(p.status)));
        if (StatusPedido.NEGADO.equals(p.status)) UiHelper.linha(binding.dados, getString(R.string.motivo_negacao), p.motivoRejeicao);
        UiHelper.linha(binding.dados, getString(R.string.quantidade_itens), String.valueOf(p.quantidadeItens));
        UiHelper.linha(binding.dados, getString(R.string.metodo_pagamento), p.pagamento);
        UiHelper.linha(binding.dados, getString(R.string.parcelamento), getString(R.string.parcelamento_valor, p.quantidaeParcelas, Formatador.moeda(p.valorParcela)));
        UiHelper.linha(binding.dados, getString(R.string.valor_final), Formatador.moeda(p.valor));
        UiHelper.linha(binding.dados, getString(R.string.endereco_entrega), p.endereco);
        itensAdapter.submit(p.itens);
        binding.acoes.setVisibility(admin && StatusPedido.PENDENTE.equals(p.status) ? View.VISIBLE : View.GONE);
    }

    private void pedirMotivo() {
        DialogMotivoBinding d = DialogMotivoBinding.inflate(getLayoutInflater());
        androidx.appcompat.app.AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.recusar_pedido)
                .setView(d.getRoot())
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.confirmar, null)
                .create();
        dialog.setOnShowListener(i -> dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String motivo = UiHelper.texto(d.motivo);
            if (motivo.isEmpty()) {
                d.layoutMotivo.setError(getString(R.string.erro_obrigatorio));
                return;
            }
            dialog.dismiss();
            alterarStatus(2, motivo);
        }));
        dialog.show();
    }

    private void alterarStatus(int acao, String motivo) {
        LiveData<Resource<Pedido>> chamada = vm.alterarStatus(codigo, acao, motivo);
        chamada.observe(getViewLifecycleOwner(), r -> {
            binding.btnConfirmar.setEnabled(r.status != Resource.Status.LOADING);
            binding.btnRecusar.setEnabled(r.status != Resource.Status.LOADING);
            if (r.status == Resource.Status.ERROR) {
                UiHelper.erro(this, r);
            } else if (r.status == Resource.Status.SUCCESS) {
                int msg = acao == 1 ? R.string.pedido_confirmado_sucesso : R.string.pedido_recusado_sucesso;
                getParentFragmentManager().setFragmentResult(RESULT_PEDIDO, new Bundle());
                UiHelper.dialogo(requireContext(), getString(R.string.titulo_sucesso), getString(msg), () -> {
                    if (isAdded()) NavHostFragment.findNavController(this).popBackStack();
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
