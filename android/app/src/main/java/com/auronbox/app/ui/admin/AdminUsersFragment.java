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

import com.auronbox.app.R;
import com.auronbox.app.data.model.Usuario;
import com.auronbox.app.databinding.FragmentAdminUsersBinding;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Resource;
import com.auronbox.app.viewmodel.UsuarioViewModel;

public class AdminUsersFragment extends Fragment {

    private FragmentAdminUsersBinding binding;
    private UsuarioViewModel vm;
    private Usuario atual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(UsuarioViewModel.class);
        binding.btnBuscar.setOnClickListener(v -> buscar());
        binding.email.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                buscar();
                return true;
            }
            return false;
        });
        binding.btnPromover.setOnClickListener(v -> alterar(true));
        binding.btnRemover.setOnClickListener(v -> alterar(false));
    }

    private void buscar() {
        String email = UiHelper.texto(binding.email);
        if (email.isEmpty()) return;
        UiHelper.esconderTeclado(binding.email);
        vm.buscarUsuario(email).observe(getViewLifecycleOwner(), r -> {
            binding.progress.setVisibility(r.status == Resource.Status.LOADING ? View.VISIBLE : View.GONE);
            if (r.status == Resource.Status.SUCCESS && r.data != null) {
                exibir(r.data);
            } else if (r.status == Resource.Status.ERROR) {
                binding.card.setVisibility(View.GONE);
                if (r.sessaoExpirada() || r.semConexao) {
                    binding.erro.setVisibility(View.GONE);
                    UiHelper.erro(this, r);
                } else {
                    binding.erro.setText(R.string.usuario_nao_encontrado);
                    binding.erro.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void exibir(Usuario u) {
        atual = u;
        binding.erro.setVisibility(View.GONE);
        binding.card.setVisibility(View.VISIBLE);
        binding.nome.setText(getString(R.string.rotulo_nome, u.nome));
        binding.emailUsuario.setText(getString(R.string.rotulo_email, u.email));
        binding.codigoUsuario.setText(getString(R.string.rotulo_codigo, u.codigo));
        binding.adminUsuario.setText(getString(R.string.rotulo_admin, getString(u.admin ? R.string.sim : R.string.nao)));
        binding.btnPromover.setVisibility(u.admin ? View.GONE : View.VISIBLE);
        binding.btnRemover.setVisibility(u.admin ? View.VISIBLE : View.GONE);
    }

    private void alterar(boolean admin) {
        String senha = UiHelper.texto(binding.senhaSistema);
        if (atual == null) return;
        if (senha.isEmpty()) {
            UiHelper.dialogo(requireContext(), getString(R.string.titulo_atencao), getString(R.string.informe_senha_sistema), null);
            return;
        }
        UiHelper.esconderTeclado(binding.getRoot());
        vm.alterarAdmin(atual.codigo, admin, senha).observe(getViewLifecycleOwner(), r -> {
            binding.progress.setVisibility(r.status == Resource.Status.LOADING ? View.VISIBLE : View.GONE);
            if (r.status == Resource.Status.ERROR) {
                UiHelper.erro(this, r);
            } else if (r.status == Resource.Status.SUCCESS && r.data != null) {
                exibir(r.data);
                binding.senhaSistema.setText("");
                int msg = admin ? R.string.admin_promovido : R.string.admin_removido;
                UiHelper.dialogo(requireContext(), getString(R.string.titulo_sucesso), getString(msg), null);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
