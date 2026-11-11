package com.auronbox.app.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auronbox.app.AuronApp;
import com.auronbox.app.R;
import com.auronbox.app.data.model.Pedido;
import com.auronbox.app.databinding.FragmentOrdersBinding;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Resource;
import com.auronbox.app.viewmodel.PedidoViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;
    private PedidoViewModel vm;
    private PedidoAdapter adapter;
    private Resource<?> erroMostrado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(PedidoViewModel.class);
        adapter = new PedidoAdapter(pedido -> {
            Bundle args = new Bundle();
            args.putLong(OrderDetailFragment.KEY_CODIGO, pedido.codigo);
            NavHostFragment.findNavController(this).navigate(R.id.orderDetailFragment, args);
        });
        binding.lista.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.lista.setAdapter(adapter);
        binding.swipe.setOnRefreshListener(() -> vm.carregarDoCliente());

        vm.getLista().observe(getViewLifecycleOwner(), this::renderizar);
        AuronApp.sessao().logado().observe(getViewLifecycleOwner(), logado -> {
            if (logado) {
                vm.carregarDoCliente();
            } else {
                adapter.submit(new ArrayList<>());
                UiHelper.estadoLogin(binding.state, binding.swipe, R.string.msg_login_pedidos, () -> UiHelper.irParaLogin(this));
            }
        });
    }

    private void renderizar(Resource<List<Pedido>> r) {
        if (!AuronApp.sessao().estaLogado()) return;
        binding.swipe.setRefreshing(r.status == Resource.Status.LOADING && adapter.getItemCount() > 0);
        if (r.status == Resource.Status.SUCCESS) adapter.submit(r.data);
        boolean tem = adapter.getItemCount() > 0;
        if (r.status == Resource.Status.ERROR && tem) {
            if (erroMostrado != r) {
                erroMostrado = r;
                UiHelper.erro(this, r);
            }
            UiHelper.esconderEstado(binding.state, binding.swipe);
            return;
        }
        boolean vazio = r.status == Resource.Status.SUCCESS && !tem;
        UiHelper.estado(binding.state, binding.swipe, r, tem, vazio, getString(R.string.sem_pedidos), () -> vm.carregarDoCliente());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
