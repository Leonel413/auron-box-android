package com.auronbox.app.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auronbox.app.R;
import com.auronbox.app.data.model.Produto;
import com.auronbox.app.databinding.FragmentAdminProductsBinding;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Resource;
import com.auronbox.app.viewmodel.AdminProdutoViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AdminProductsFragment extends Fragment {

    private FragmentAdminProductsBinding binding;
    private AdminProdutoViewModel vm;
    private AdminProdutoAdapter adapter;
    private Resource<?> erroMostrado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminProductsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(AdminProdutoViewModel.class);
        adapter = new AdminProdutoAdapter(new AdminProdutoAdapter.Listener() {
            @Override
            public void editar(Produto produto) {
                abrirFormulario(produto.codigo);
            }

            @Override
            public void excluir(Produto produto) {
                UiHelper.confirmar(requireContext(), R.string.excluir, R.string.confirmar_excluir_produto, () -> excluirProduto(produto));
            }
        });
        binding.lista.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.lista.setAdapter(adapter);
        binding.btnNovo.setOnClickListener(v -> abrirFormulario(0));
        binding.btnBuscar.setOnClickListener(v -> pesquisar());
        binding.busca.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                pesquisar();
                return true;
            }
            return false;
        });

        getParentFragmentManager().setFragmentResultListener(AdminProductFormFragment.RESULT_PRODUTO, getViewLifecycleOwner(), (chave, bundle) -> vm.carregar());
        vm.getLista().observe(getViewLifecycleOwner(), this::renderizar);
        if (!vm.carregado()) vm.carregar("");
    }

    private void pesquisar() {
        UiHelper.esconderTeclado(binding.busca);
        vm.carregar(UiHelper.texto(binding.busca));
    }

    private void abrirFormulario(long codigo) {
        Bundle args = new Bundle();
        args.putLong(AdminProductFormFragment.KEY_CODIGO, codigo);
        NavHostFragment.findNavController(this).navigate(R.id.adminProductFormFragment, args);
    }

    private void excluirProduto(Produto produto) {
        vm.excluir(produto.codigo).observe(getViewLifecycleOwner(), r -> {
            if (r.status == Resource.Status.ERROR) {
                UiHelper.erro(this, r);
            } else if (r.status == Resource.Status.SUCCESS) {
                Snackbar.make(binding.getRoot(), R.string.produto_excluido, Snackbar.LENGTH_SHORT).show();
                vm.carregar();
            }
        });
    }

    private void renderizar(Resource<List<Produto>> r) {
        if (r.status == Resource.Status.SUCCESS) adapter.submit(r.data);
        boolean tem = adapter.getItemCount() > 0;
        if (r.status == Resource.Status.ERROR && tem) {
            if (erroMostrado != r) {
                erroMostrado = r;
                UiHelper.erro(this, r);
            }
            UiHelper.esconderEstado(binding.state, binding.lista);
            return;
        }
        boolean vazio = r.status == Resource.Status.SUCCESS && !tem;
        UiHelper.estado(binding.state, binding.lista, r, tem, vazio, getString(R.string.sem_produtos_admin), () -> vm.carregar());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
