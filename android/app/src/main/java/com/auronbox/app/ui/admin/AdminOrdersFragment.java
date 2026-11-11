package com.auronbox.app.ui.admin;

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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auronbox.app.R;
import com.auronbox.app.data.model.Pedido;
import com.auronbox.app.databinding.FragmentAdminOrdersBinding;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.ui.orders.OrderDetailFragment;
import com.auronbox.app.util.Resource;
import com.auronbox.app.util.StatusPedido;
import com.auronbox.app.viewmodel.PedidoViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminOrdersFragment extends Fragment {

    private FragmentAdminOrdersBinding binding;
    private PedidoViewModel vm;
    private AdminPedidoAdapter adapter;
    private List<Pedido> todos = new ArrayList<>();
    private int filtroAtual = 0;
    private Resource<?> erroMostrado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(PedidoViewModel.class);
        adapter = new AdminPedidoAdapter(pedido -> {
            Bundle args = new Bundle();
            args.putLong(OrderDetailFragment.KEY_CODIGO, pedido.codigo);
            args.putBoolean(OrderDetailFragment.KEY_ADMIN, true);
            NavHostFragment.findNavController(this).navigate(R.id.orderDetailFragment, args);
        });
        binding.lista.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.lista.setAdapter(adapter);
        binding.swipe.setOnRefreshListener(() -> vm.carregarTodos());

        ArrayAdapter<CharSequence> opcoes = ArrayAdapter.createFromResource(requireContext(), R.array.filtro_pedidos, android.R.layout.simple_spinner_item);
        opcoes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.filtro.setAdapter(opcoes);
        binding.filtro.setSelection(filtroAtual, false);
        binding.filtro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if (position == filtroAtual) return;
                filtroAtual = position;
                aplicarFiltro();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        getParentFragmentManager().setFragmentResultListener(OrderDetailFragment.RESULT_PEDIDO, getViewLifecycleOwner(), (chave, bundle) -> vm.carregarTodos());
        vm.getLista().observe(getViewLifecycleOwner(), this::renderizar);
        if (!vm.carregado()) vm.carregarTodos();
    }

    private String statusFiltro() {
        if (filtroAtual == 1) return StatusPedido.CONFIRMADO;
        if (filtroAtual == 2) return StatusPedido.NEGADO;
        return StatusPedido.PENDENTE;
    }

    private void aplicarFiltro() {
        List<Pedido> filtrados = new ArrayList<>();
        for (Pedido p : todos) if (statusFiltro().equals(p.status)) filtrados.add(p);
        adapter.submit(filtrados);
        Resource<List<Pedido>> sucesso = Resource.success(filtrados);
        UiHelper.estado(binding.state, binding.swipe, sucesso, !filtrados.isEmpty(), filtrados.isEmpty(), getString(R.string.sem_pedidos_filtro), () -> vm.carregarTodos());
    }

    private void renderizar(Resource<List<Pedido>> r) {
        binding.swipe.setRefreshing(r.status == Resource.Status.LOADING && adapter.getItemCount() > 0);
        if (r.status == Resource.Status.SUCCESS) {
            todos = r.data == null ? new ArrayList<>() : r.data;
            aplicarFiltro();
            return;
        }
        boolean tem = adapter.getItemCount() > 0;
        if (r.status == Resource.Status.ERROR && tem) {
            if (erroMostrado != r) {
                erroMostrado = r;
                UiHelper.erro(this, r);
            }
            UiHelper.esconderEstado(binding.state, binding.swipe);
            return;
        }
        UiHelper.estado(binding.state, binding.swipe, r, tem, false, "", () -> vm.carregarTodos());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
