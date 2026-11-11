package com.auronbox.app.ui.detail;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.auronbox.app.AuronApp;
import com.auronbox.app.R;
import com.auronbox.app.data.model.Produto;
import com.auronbox.app.databinding.FragmentProductDetailBinding;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Formatador;
import com.auronbox.app.util.Resource;
import com.auronbox.app.viewmodel.CarrinhoViewModel;
import com.auronbox.app.viewmodel.ProdutoViewModel;
import com.google.android.material.snackbar.Snackbar;

public class ProductDetailFragment extends Fragment {

    public static final String KEY_CODIGO = "codigo";

    private FragmentProductDetailBinding binding;
    private ProdutoViewModel vm;
    private CarrinhoViewModel carrinhoVm;
    private long codigo;
    private Produto produtoAtual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        codigo = requireArguments().getLong(KEY_CODIGO);
        vm = new ViewModelProvider(this).get(ProdutoViewModel.class);
        carrinhoVm = new ViewModelProvider(this).get(CarrinhoViewModel.class);
        binding.btnAdicionar.setOnClickListener(v -> adicionar());
        vm.getProduto().observe(getViewLifecycleOwner(), this::renderizar);
        if (vm.getProduto().getValue() == null) vm.carregarProduto(codigo);
    }

    private void renderizar(Resource<Produto> r) {
        if (r.status == Resource.Status.SUCCESS && r.data != null) {
            produtoAtual = r.data;
            preencher(r.data);
        }
        UiHelper.estado(binding.state, binding.conteudo, r, false, false, "", () -> vm.carregarProduto(codigo));
    }

    private void preencher(Produto p) {
        binding.breadcrumb.setText(getString(R.string.breadcrumb, p.categoria, p.nomeProduto));
        UiHelper.imagem(binding.imagem, p.imagem);
        binding.badge.setText(p.categoria);
        binding.nome.setText(p.nomeProduto);
        binding.marcaModelo.setText(getString(R.string.marca_modelo, p.marca, p.modelo));
        binding.descricao.setText(p.descricao);
        binding.preco.setText(Formatador.moeda(p.valorComDesconto));
        binding.garantia.setText(getString(R.string.garantia_valor, Formatador.ou(p.garantia, "-")));
        if (p.quantidadeEstoque > 0) {
            binding.estoque.setText(getString(R.string.em_estoque, p.quantidadeEstoque));
            binding.estoque.setTextColor(ContextCompat.getColor(requireContext(), R.color.sucesso));
            binding.btnAdicionar.setEnabled(true);
        } else {
            binding.estoque.setText(R.string.fora_estoque);
            binding.estoque.setTextColor(ContextCompat.getColor(requireContext(), R.color.perigo));
            binding.btnAdicionar.setEnabled(false);
        }
        montarEspecificacoes(p.especificacoes);
    }

    private void montarEspecificacoes(String especificacoes) {
        binding.specs.removeAllViews();
        boolean algum = false;
        if (especificacoes != null) {
            for (String item : especificacoes.split(",")) {
                int separador = item.indexOf(':');
                if (separador <= 0) continue;
                String chave = item.substring(0, separador).trim();
                String valor = item.substring(separador + 1).trim();
                if (chave.isEmpty() || valor.isEmpty()) continue;
                binding.specs.addView(linhaSpec(chave, valor));
                algum = true;
            }
        }
        binding.tituloSpecs.setVisibility(algum ? View.VISIBLE : View.GONE);
        binding.specs.setVisibility(algum ? View.VISIBLE : View.GONE);
    }

    private TableRow linhaSpec(String chave, String valor) {
        TableRow linha = new TableRow(requireContext());
        int pad = (int) (8 * getResources().getDisplayMetrics().density);
        TextView c = new TextView(requireContext());
        c.setText(chave);
        c.setTextColor(ContextCompat.getColor(requireContext(), R.color.cinza_texto));
        c.setPadding(0, pad, pad * 2, pad);
        TextView v = new TextView(requireContext());
        v.setText(valor);
        v.setTextColor(ContextCompat.getColor(requireContext(), R.color.preto));
        v.setGravity(Gravity.START);
        v.setPadding(0, pad, 0, pad);
        linha.addView(c);
        linha.addView(v);
        return linha;
    }

    private void adicionar() {
        if (produtoAtual == null) return;
        if (!AuronApp.sessao().estaLogado()) {
            UiHelper.dialogoLogin(this, R.string.msg_login_carrinho);
            return;
        }
        carrinhoVm.adicionar(produtoAtual.codigo).observe(getViewLifecycleOwner(), r -> {
            binding.btnAdicionar.setEnabled(r.status != Resource.Status.LOADING && produtoAtual.quantidadeEstoque > 0);
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
