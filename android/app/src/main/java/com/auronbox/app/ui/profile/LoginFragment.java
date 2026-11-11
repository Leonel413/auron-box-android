package com.auronbox.app.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.auronbox.app.R;
import com.auronbox.app.databinding.FragmentLoginBinding;
import com.auronbox.app.ui.common.SimpleWatcher;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Resource;
import com.auronbox.app.viewmodel.UsuarioViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private UsuarioViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(UsuarioViewModel.class);
        SimpleWatcher watcher = new SimpleWatcher() {
            @Override
            public void aoMudar() {
                binding.btnEntrar.setEnabled(!UiHelper.texto(binding.email).isEmpty() && !UiHelper.texto(binding.senha).isEmpty());
            }
        };
        binding.email.addTextChangedListener(watcher);
        binding.senha.addTextChangedListener(watcher);
        binding.btnEntrar.setOnClickListener(v -> entrar());
        binding.btnCriarConta.setOnClickListener(v -> {
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(R.id.registerFragment, null, new NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build());
        });
    }

    private void entrar() {
        UiHelper.esconderTeclado(binding.getRoot());
        vm.login(UiHelper.texto(binding.email), UiHelper.texto(binding.senha)).observe(getViewLifecycleOwner(), r -> {
            boolean carregando = r.status == Resource.Status.LOADING;
            binding.progress.setVisibility(carregando ? View.VISIBLE : View.GONE);
            binding.btnEntrar.setEnabled(!carregando);
            if (r.status == Resource.Status.ERROR) {
                UiHelper.erro(this, r);
            } else if (r.status == Resource.Status.SUCCESS) {
                Toast.makeText(requireContext().getApplicationContext(), R.string.login_sucesso, Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(this).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
