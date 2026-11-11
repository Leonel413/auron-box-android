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
import com.auronbox.app.databinding.FragmentAddressBinding;
import com.auronbox.app.ui.common.EnderecoForm;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Resource;
import com.auronbox.app.viewmodel.UsuarioViewModel;

public class AddressFragment extends Fragment {

    private FragmentAddressBinding binding;
    private UsuarioViewModel vm;
    private EnderecoForm endereco;
    private boolean preenchido;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(UsuarioViewModel.class);
        endereco = new EnderecoForm(this, binding.endereco, vm);
        binding.btnSalvar.setOnClickListener(v -> {
            if (endereco.validar()) salvar();
        });
        vm.getPerfil().observe(getViewLifecycleOwner(), r -> {
            binding.progress.setVisibility(r.status == Resource.Status.LOADING ? View.VISIBLE : View.GONE);
            if (r.status == Resource.Status.ERROR) {
                UiHelper.erro(this, r);
            } else if (r.status == Resource.Status.SUCCESS && r.data != null && !preenchido) {
                preenchido = true;
                endereco.preencher(r.data);
            }
        });
        vm.carregarPerfil();
    }

    private void salvar() {
        UiHelper.esconderTeclado(binding.getRoot());
        vm.alterarEndereco(endereco.montar()).observe(getViewLifecycleOwner(), r -> {
            boolean carregando = r.status == Resource.Status.LOADING;
            binding.progress.setVisibility(carregando ? View.VISIBLE : View.GONE);
            binding.btnSalvar.setEnabled(!carregando);
            if (r.status == Resource.Status.ERROR) {
                UiHelper.erro(this, r);
            } else if (r.status == Resource.Status.SUCCESS) {
                UiHelper.dialogo(requireContext(), getString(R.string.titulo_sucesso), getString(R.string.endereco_alterado), () -> {
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
