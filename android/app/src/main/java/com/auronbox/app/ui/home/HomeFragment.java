package com.auronbox.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.auronbox.app.AuronApp;
import com.auronbox.app.R;
import com.auronbox.app.data.model.Produto;
import com.auronbox.app.databinding.FragmentHomeBinding;
import com.auronbox.app.ui.common.SimpleWatcher;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.ui.detail.ProductDetailFragment;
import com.auronbox.app.util.Resource;
import com.auronbox.app.viewmodel.CarrinhoViewModel;
import com.auronbox.app.viewmodel.ProdutoViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ProdutoViewModel vm;
    private CarrinhoViewModel carrinhoVm;
    private ProdutoAdapter adapter;
    private String categoriaAtual = "";
    private int ordemAtual = 0;
    private Resource<?> erroMostrado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ProdutoViewModel.class);
        carrinhoVm = new ViewModelProvider(this).get(CarrinhoViewModel.class);

        adapter = new ProdutoAdapter(new ProdutoAdapter.Listener() {
            @Override
            public void abrir(Produto produto) {
                Bundle args = new Bundle();
                args.putLong(ProductDetailFragment.KEY_CODIGO, produto.codigo);
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.productDetailFragment, args);
            }

            @Override
            public void adicionar(Produto produto) {
                adicionarAoCarrinho(produto);
            }
        });
        binding.lista.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.lista.setAdapter(adapter);

        montarChips();
        montarOrdenacao();
        montarBusca();

        binding.swipe.setOnRefreshListener(() -> vm.carregar());
        vm.getLista().observe(getViewLifecycleOwner(), this::renderizar);
        if (!vm.carregado()) vm.carregar();
    }

    private void montarChips() {
        List<String> categorias = java.util.Arrays.asList(getResources().getStringArray(R.array.categorias));
        adicionarChip(getString(R.string.todos), "");
        for (String categoria : categorias) adicionarChip(categoria, categoria);
        for (int i = 0; i < binding.chips.getChildCount(); i++) {
            Chip chip = (Chip) binding.chips.getChildAt(i);
            if (categoriaAtual.equals(chip.getTag())) binding.chips.check(chip.getId());
        }
        binding.chips.setOnCheckedStateChangeListener((group, ids) -> {
            if (ids.isEmpty()) return;
            Chip escolhido = group.findViewById(ids.get(0));
            categoriaAtual = (String) escolhido.getTag();
            vm.definirCategoria(categoriaAtual);
        });
    }

    private void adicionarChip(String texto, String categoria) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip, binding.chips, false);
        chip.setId(View.generateViewId());
        chip.setText(texto);
        chip.setTag(categoria);
        binding.chips.addView(chip);
    }

    private void montarOrdenacao() {
        ArrayAdapter<CharSequence> opcoes = ArrayAdapter.createFromResource(requireContext(), R.array.ordenacao, android.R.layout.simple_spinner_item);
        opcoes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.ordenar.setAdapter(opcoes);
        binding.ordenar.setSelection(ordemAtual, false);
        binding.ordenar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == ordemAtual) return;
                ordemAtual = position;
                String ordem = position == 1 ? ProdutoViewModel.ORDEM_MENOR : position == 2 ? ProdutoViewModel.ORDEM_MAIOR : ProdutoViewModel.ORDEM_PADRAO;
                vm.definirOrdem(ordem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void montarBusca() {
        binding.busca.setText(vm.getBusca());
        binding.btnLimparBusca.setVisibility(vm.getBusca().isEmpty() ? View.GONE : View.VISIBLE);
        binding.busca.addTextChangedListener(new SimpleWatcher() {
            @Override
            public void aoMudar() {
                binding.btnLimparBusca.setVisibility(UiHelper.texto(binding.busca).isEmpty() ? View.GONE : View.VISIBLE);
            }
        });
        binding.busca.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                pesquisar();
                return true;
            }
            return false;
        });
        binding.btnBuscar.setOnClickListener(v -> pesquisar());
        binding.btnLimparBusca.setOnClickListener(v -> {
            binding.busca.setText("");
            pesquisar();
        });
    }

    private void pesquisar() {
        String termo = UiHelper.texto(binding.busca);
        UiHelper.esconderTeclado(binding.busca);
        binding.titulo.setText(termo.isEmpty() ? R.string.todos_produtos : R.string.resultados_busca);
        vm.definirBusca(termo);
    }

    private void renderizar(Resource<List<Produto>> r) {
        binding.swipe.setRefreshing(r.status == Resource.Status.LOADING && adapter.getItemCount() > 0);
        if (r.status == Resource.Status.SUCCESS && r.data != null) adapter.submit(r.data);
        boolean vazio = r.status == Resource.Status.SUCCESS && adapter.getItemCount() == 0;
        String msgVazio = getString(vm.getBusca().isEmpty() ? R.string.sem_produtos : R.string.sem_resultados);
        if (r.status == Resource.Status.ERROR && adapter.getItemCount() > 0) {
            if (erroMostrado != r) {
                erroMostrado = r;
                UiHelper.erro(this, r);
            }
            UiHelper.esconderEstado(binding.state, binding.swipe);
            return;
        }
        UiHelper.estado(binding.state, binding.swipe, r, adapter.getItemCount() > 0, vazio, msgVazio, () -> vm.carregar());
    }

    private void adicionarAoCarrinho(Produto produto) {
        if (!AuronApp.sessao().estaLogado()) {
            UiHelper.dialogoLogin(this, R.string.msg_login_carrinho);
            return;
        }
        carrinhoVm.adicionar(produto.codigo).observe(getViewLifecycleOwner(), r -> {
            if (r.status == Resource.Status.SUCCESS) {
                Snackbar.make(binding.getRoot(), R.string.item_adicionado, Snackbar.LENGTH_SHORT).show();
            } else if (r.status == Resource.Status.ERROR) {
                UiHelper.erro(this, r);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
