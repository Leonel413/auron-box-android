package com.auronbox.app.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.auronbox.app.R;
import com.auronbox.app.databinding.FragmentPasswordBinding;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Resource;
import com.auronbox.app.viewmodel.UsuarioViewModel;

public class PasswordFragment extends Fragment {

    private FragmentPasswordBinding binding;
    private UsuarioViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(UsuarioViewModel.class);
        binding.btnSalvar.setOnClickListener(v -> {
            if (validar()) salvar();
        });
    }

    private boolean validar() {
        boolean ok = true;
        binding.layoutAtual.setError(null);
        binding.layoutNova.setError(null);
        binding.layoutConfirmacao.setError(null);
        if (UiHelper.texto(binding.atual).isEmpty()) {
            binding.layoutAtual.setError(getString(R.string.erro_obrigatorio));
            ok = false;
        }
        if (UiHelper.texto(binding.nova).length() < 6) {
            binding.layoutNova.setError(getString(R.string.erro_senha_nova_curta));
            ok = false;
        }
        if (!UiHelper.texto(binding.nova).equals(UiHelper.texto(binding.confirmacao))) {
            binding.layoutConfirmacao.setError(getString(R.string.erro_senhas_diferentes));
            ok = false;
        }
        return ok;
    }

    private void salvar() {
        UiHelper.esconderTeclado(binding.getRoot());
        vm.alterarSenha(UiHelper.texto(binding.atual), UiHelper.texto(binding.nova)).observe(getViewLifecycleOwner(), r -> {
            boolean carregando = r.status == Resource.Status.LOADING;
            binding.progress.setVisibility(carregando ? View.VISIBLE : View.GONE);
            binding.btnSalvar.setEnabled(!carregando);
            if (r.status == Resource.Status.ERROR) {
                UiHelper.erro(this, r);
            } else if (r.status == Resource.Status.SUCCESS) {
                UiHelper.dialogo(requireContext(), getString(R.string.titulo_sucesso), getString(R.string.senha_alterada), () -> {
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
