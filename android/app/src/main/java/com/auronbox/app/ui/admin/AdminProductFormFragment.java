package com.auronbox.app.ui.admin;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.auronbox.app.R;
import com.auronbox.app.data.model.Produto;
import com.auronbox.app.data.model.ProdutoRequest;
import com.auronbox.app.databinding.FragmentAdminProductFormBinding;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Resource;
import com.auronbox.app.viewmodel.AdminProdutoViewModel;
import com.bumptech.glide.Glide;

public class AdminProductFormFragment extends Fragment {

    public static final String KEY_CODIGO = "codigo";
    public static final String RESULT_PRODUTO = "produto_atualizado";

    private FragmentAdminProductFormBinding binding;
    private AdminProdutoViewModel vm;
    private long codigo;
    private String imagemAtual = "nulo";
    private Uri imagemEscolhida;
    private boolean preenchido;

    private final ActivityResultLauncher<String> seletor = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null && binding != null) {
            imagemEscolhida = uri;
            binding.imagemNome.setText(R.string.imagem_selecionada);
            Glide.with(binding.preview).load(uri).into(binding.preview);
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminProductFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        codigo = requireArguments().getLong(KEY_CODIGO, 0);
        vm = new ViewModelProvider(this).get(AdminProdutoViewModel.class);

        configurar(binding.categoria, R.array.form_categorias);
        configurar(binding.marca, R.array.form_marcas);
        configurar(binding.cor, R.array.form_cores);
        configurar(binding.garantia, R.array.form_garantias);
        binding.garantia.setSelection(2);
        binding.btnImagem.setOnClickListener(v -> seletor.launch("image/*"));
        binding.btnSalvar.setOnClickListener(v -> {
            if (validar()) salvar();
        });

        if (codigo > 0) {
            vm.getProduto().observe(getViewLifecycleOwner(), r -> {
                if (r.status == Resource.Status.SUCCESS && r.data != null && !preenchido) {
                    preenchido = true;
                    preencher(r.data);
                }
                UiHelper.estado(binding.state, binding.conteudo, r, false, false, "", () -> vm.carregarProduto(codigo));
            });
            if (vm.getProduto().getValue() == null) vm.carregarProduto(codigo);
        } else {
            binding.state.getRoot().setVisibility(View.GONE);
        }
    }

    private void configurar(Spinner spinner, int array) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void selecionar(Spinner spinner, String valor) {
        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            if (String.valueOf(spinner.getAdapter().getItem(i)).equalsIgnoreCase(valor)) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    private void preencher(Produto p) {
        imagemAtual = p.imagem == null || p.imagem.isEmpty() ? "nulo" : p.imagem;
        binding.nome.setText(p.nomeProduto);
        binding.descricao.setText(p.descricao);
        selecionar(binding.categoria, p.categoria);
        selecionar(binding.marca, p.marca);
        binding.modelo.setText(p.modelo);
        selecionar(binding.cor, p.cor);
        selecionar(binding.garantia, p.garantia);
        binding.especificacoes.setText(p.especificacoes);
        binding.promocao.setText(String.valueOf(p.promocao));
        binding.estoque.setText(String.valueOf(p.quantidadeEstoque));
        binding.valor.setText(String.valueOf(p.valorBase));
        UiHelper.imagem(binding.preview, p.imagem);
    }

    private boolean validar() {
        boolean ok = true;
        binding.layoutNome.setError(null);
        binding.layoutDescricao.setError(null);
        binding.layoutModelo.setError(null);
        binding.layoutPromocao.setError(null);
        binding.layoutEstoque.setError(null);
        binding.layoutValor.setError(null);
        String obrigatorio = getString(R.string.erro_obrigatorio);
        if (UiHelper.texto(binding.nome).isEmpty()) {
            binding.layoutNome.setError(obrigatorio);
            ok = false;
        }
        if (UiHelper.texto(binding.descricao).isEmpty()) {
            binding.layoutDescricao.setError(obrigatorio);
            ok = false;
        }
        if (UiHelper.texto(binding.modelo).isEmpty()) {
            binding.layoutModelo.setError(obrigatorio);
            ok = false;
        }
        if (binding.categoria.getSelectedItemPosition() == 0 || binding.marca.getSelectedItemPosition() == 0) {
            UiHelper.dialogo(requireContext(), getString(R.string.titulo_atencao), getString(R.string.erro_categoria_marca), null);
            ok = false;
        }
        String promocao = UiHelper.texto(binding.promocao);
        if (!promocao.isEmpty() && (!promocao.matches("\\d{1,3}") || Integer.parseInt(promocao) > 100)) {
            binding.layoutPromocao.setError(getString(R.string.erro_desconto));
            ok = false;
        }
        if (!UiHelper.texto(binding.estoque).matches("\\d{1,6}")) {
            binding.layoutEstoque.setError(obrigatorio);
            ok = false;
        }
        if (valorBase() <= 0) {
            binding.layoutValor.setError(getString(R.string.erro_valor));
            ok = false;
        }
        return ok;
    }

    private double valorBase() {
        try {
            return Double.parseDouble(UiHelper.texto(binding.valor).replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void salvar() {
        ProdutoRequest r = new ProdutoRequest();
        r.codigo = codigo > 0 ? codigo : null;
        r.nomeProduto = UiHelper.texto(binding.nome);
        r.descricao = UiHelper.texto(binding.descricao);
        r.imagem = imagemAtual;
        r.categoria = (String) binding.categoria.getSelectedItem();
        r.marca = (String) binding.marca.getSelectedItem();
        r.modelo = UiHelper.texto(binding.modelo);
        r.cor = (String) binding.cor.getSelectedItem();
        r.garantia = (String) binding.garantia.getSelectedItem();
        r.especificacoes = UiHelper.texto(binding.especificacoes);
        String promocao = UiHelper.texto(binding.promocao);
        r.promocao = promocao.isEmpty() ? 0 : Integer.parseInt(promocao);
        r.quantidadeEstoque = Integer.parseInt(UiHelper.texto(binding.estoque));
        r.valorBase = valorBase();
        UiHelper.esconderTeclado(binding.getRoot());

        vm.salvar(r).observe(getViewLifecycleOwner(), res -> {
            aoMudarCarregamento(res.status == Resource.Status.LOADING);
            if (res.status == Resource.Status.ERROR) {
                UiHelper.erro(this, res);
            } else if (res.status == Resource.Status.SUCCESS && res.data != null) {
                if (imagemEscolhida != null) enviarImagem(res.data.codigo);
                else concluir();
            }
        });
    }

    private void enviarImagem(long codigoProduto) {
        vm.enviarImagem(requireContext(), codigoProduto, imagemEscolhida).observe(getViewLifecycleOwner(), res -> {
            aoMudarCarregamento(res.status == Resource.Status.LOADING);
            if (res.status == Resource.Status.ERROR) {
                getParentFragmentManager().setFragmentResult(RESULT_PRODUTO, new Bundle());
                UiHelper.erro(this, res);
            } else if (res.status == Resource.Status.SUCCESS) {
                concluir();
            }
        });
    }

    private void aoMudarCarregamento(boolean carregando) {
        binding.progress.setVisibility(carregando ? View.VISIBLE : View.GONE);
        binding.btnSalvar.setEnabled(!carregando);
    }

    private void concluir() {
        getParentFragmentManager().setFragmentResult(RESULT_PRODUTO, new Bundle());
        UiHelper.dialogo(requireContext(), getString(R.string.titulo_sucesso), getString(R.string.produto_salvo), () -> {
            if (isAdded()) NavHostFragment.findNavController(this).popBackStack();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
