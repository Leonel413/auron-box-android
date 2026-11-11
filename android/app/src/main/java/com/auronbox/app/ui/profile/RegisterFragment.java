package com.auronbox.app.ui.profile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.auronbox.app.R;
import com.auronbox.app.data.model.UsuarioRequest;
import com.auronbox.app.databinding.FragmentRegisterBinding;
import com.auronbox.app.ui.common.EnderecoForm;
import com.auronbox.app.ui.common.UiHelper;
import com.auronbox.app.util.Resource;
import com.auronbox.app.util.Validadores;
import com.auronbox.app.viewmodel.UsuarioViewModel;

import java.util.Calendar;
import java.util.Locale;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private UsuarioViewModel vm;
    private EnderecoForm endereco;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(UsuarioViewModel.class);
        endereco = new EnderecoForm(this, binding.endereco, vm);
        mostrarPasso(1);
        binding.nascimento.setOnClickListener(v -> escolherData());
        binding.btnAvancar.setOnClickListener(v -> {
            if (validarPasso1()) mostrarPasso(2);
        });
        binding.btnVoltar.setOnClickListener(v -> mostrarPasso(1));
        binding.btnCriar.setOnClickListener(v -> {
            if (endereco.validar()) cadastrar();
        });
    }

    private void mostrarPasso(int passo) {
        binding.step1.setVisibility(passo == 1 ? View.VISIBLE : View.GONE);
        binding.step2.setVisibility(passo == 2 ? View.VISIBLE : View.GONE);
        binding.passo.setText(getString(R.string.passo, passo));
    }

    private void escolherData() {
        Calendar maxima = Calendar.getInstance();
        maxima.add(Calendar.YEAR, -18);
        Calendar minima = Calendar.getInstance();
        minima.set(1940, Calendar.JANUARY, 1);
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), (picker, ano, mes, dia) ->
                binding.nascimento.setText(String.format(Locale.US, "%02d/%02d/%04d", dia, mes + 1, ano)),
                maxima.get(Calendar.YEAR), maxima.get(Calendar.MONTH), maxima.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(maxima.getTimeInMillis());
        dialog.getDatePicker().setMinDate(minima.getTimeInMillis());
        dialog.show();
    }

    private boolean validarPasso1() {
        boolean ok = true;
        binding.layoutNome.setError(null);
        binding.layoutNascimento.setError(null);
        binding.layoutCpf.setError(null);
        binding.layoutEmail.setError(null);
        binding.layoutSenha.setError(null);
        binding.layoutCelular.setError(null);

        String nome = UiHelper.texto(binding.nome);
        if (nome.isEmpty()) {
            binding.layoutNome.setError(getString(R.string.erro_obrigatorio));
            ok = false;
        } else if (!Validadores.nomeSemNumeros(nome)) {
            binding.layoutNome.setError(getString(R.string.erro_nome_numeros));
            ok = false;
        }
        if (UiHelper.texto(binding.nascimento).isEmpty()) {
            binding.layoutNascimento.setError(getString(R.string.erro_data_nascimento));
            ok = false;
        }
        String cpf = UiHelper.texto(binding.cpf);
        if (cpf.length() != 11) {
            binding.layoutCpf.setError(getString(R.string.erro_cpf_completo));
            ok = false;
        } else if (!Validadores.cpfValido(cpf)) {
            binding.layoutCpf.setError(getString(R.string.erro_cpf_invalido));
            ok = false;
        }
        if (!Validadores.emailGmail(UiHelper.texto(binding.email))) {
            binding.layoutEmail.setError(getString(R.string.erro_email_gmail));
            ok = false;
        }
        if (UiHelper.texto(binding.senha).length() < 6) {
            binding.layoutSenha.setError(getString(R.string.erro_senha_curta));
            ok = false;
        }
        if (UiHelper.texto(binding.celular).length() != 11) {
            binding.layoutCelular.setError(getString(R.string.erro_celular));
            ok = false;
        }
        return ok;
    }

    private void cadastrar() {
        UsuarioRequest request = new UsuarioRequest();
        request.nome = UiHelper.texto(binding.nome);
        request.dataNascimento = UiHelper.texto(binding.nascimento);
        request.cpf = UiHelper.texto(binding.cpf);
        request.email = UiHelper.texto(binding.email);
        request.senha = UiHelper.texto(binding.senha);
        request.celular = UiHelper.texto(binding.celular);
        endereco.aplicar(request);
        UiHelper.esconderTeclado(binding.getRoot());
        vm.cadastrar(request).observe(getViewLifecycleOwner(), r -> {
            boolean carregando = r.status == Resource.Status.LOADING;
            binding.progress.setVisibility(carregando ? View.VISIBLE : View.GONE);
            binding.btnCriar.setEnabled(!carregando);
            if (r.status == Resource.Status.ERROR) {
                UiHelper.erro(this, r);
            } else if (r.status == Resource.Status.SUCCESS) {
                Toast.makeText(requireContext().getApplicationContext(), R.string.cadastro_sucesso, Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(this).navigate(R.id.loginFragment, null,
                        new NavOptions.Builder().setPopUpTo(R.id.registerFragment, true).build());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
