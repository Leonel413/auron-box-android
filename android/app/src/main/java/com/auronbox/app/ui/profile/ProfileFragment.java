package com.auronbox.app.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.auronbox.app.AuronApp;
import com.auronbox.app.R;
import com.auronbox.app.data.model.Usuario;
import com.auronbox.app.databinding.FragmentProfileBinding;
import com.auronbox.app.ui.admin.AdminActivity;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Formatador;
import com.auronbox.app.util.Resource;
import com.auronbox.app.viewmodel.UsuarioViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private UsuarioViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(UsuarioViewModel.class);

        binding.btnEntrar.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.loginFragment));
        binding.btnCriarConta.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.registerFragment));
        binding.btnEndereco.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.addressFragment));
        binding.btnSenha.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.passwordFragment));
        binding.btnAdmin.setOnClickListener(v -> startActivity(new Intent(requireContext(), AdminActivity.class)));
        binding.btnSair.setOnClickListener(v -> UiHelper.confirmar(requireContext(), R.string.sair, R.string.confirmar_sair, () -> AuronApp.sessao().logout()));

        vm.getPerfil().observe(getViewLifecycleOwner(), this::renderizar);
        AuronApp.sessao().logado().observe(getViewLifecycleOwner(), logado -> {
            if (logado) {
                binding.deslogado.setVisibility(View.GONE);
                binding.btnAdmin.setVisibility(AuronApp.sessao().admin() ? View.VISIBLE : View.GONE);
                vm.carregarPerfil();
            } else {
                binding.logado.setVisibility(View.GONE);
                binding.state.getRoot().setVisibility(View.GONE);
                binding.deslogado.setVisibility(View.VISIBLE);
            }
        });
    }

    private void renderizar(Resource<Usuario> r) {
        if (!AuronApp.sessao().estaLogado()) return;
        if (r.status == Resource.Status.SUCCESS && r.data != null) preencher(r.data);
        UiHelper.estado(binding.state, binding.logado, r, false, false, "", () -> vm.carregarPerfil());
    }

    private void preencher(Usuario u) {
        binding.nome.setText(Formatador.primeiroNome(u.nome));
        binding.dadosPessoais.removeAllViews();
        UiHelper.linha(binding.dadosPessoais, getString(R.string.nome), u.nome);
        UiHelper.linha(binding.dadosPessoais, getString(R.string.data_nascimento), u.dataNascimento);
        UiHelper.linha(binding.dadosPessoais, getString(R.string.cpf), u.cpf);
        UiHelper.linha(binding.dadosPessoais, getString(R.string.email), u.email);
        UiHelper.linha(binding.dadosPessoais, getString(R.string.celular), u.celular);
        binding.dadosEndereco.removeAllViews();
        UiHelper.linha(binding.dadosEndereco, getString(R.string.cep), u.cep);
        UiHelper.linha(binding.dadosEndereco, getString(R.string.estado), u.estado);
        UiHelper.linha(binding.dadosEndereco, getString(R.string.cidade), u.cidade);
        UiHelper.linha(binding.dadosEndereco, getString(R.string.bairro), u.bairro);
        UiHelper.linha(binding.dadosEndereco, getString(R.string.rua), u.rua);
        UiHelper.linha(binding.dadosEndereco, getString(R.string.numero), String.valueOf(u.numero));
        UiHelper.linha(binding.dadosEndereco, getString(R.string.complemento), u.complemento);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
