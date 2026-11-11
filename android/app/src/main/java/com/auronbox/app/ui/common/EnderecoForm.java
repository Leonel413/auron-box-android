package com.auronbox.app.ui.common;

import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.auronbox.app.R;
import com.auronbox.app.data.model.EnderecoRequest;
import com.auronbox.app.data.model.Usuario;
import com.auronbox.app.data.model.UsuarioRequest;
import com.auronbox.app.databinding.LayoutEnderecoFormBinding;
import com.auronbox.app.util.Resource;
import com.auronbox.app.viewmodel.UsuarioViewModel;

import java.util.Arrays;
import java.util.List;

public class EnderecoForm {

    private final Fragment fragment;
    private final LayoutEnderecoFormBinding b;
    private final UsuarioViewModel vm;
    private final List<String> estados;

    public EnderecoForm(Fragment fragment, LayoutEnderecoFormBinding binding, UsuarioViewModel vm) {
        this.fragment = fragment;
        this.b = binding;
        this.vm = vm;
        this.estados = Arrays.asList(fragment.getResources().getStringArray(R.array.estados_uf));
        b.estado.setAdapter(new ArrayAdapter<>(fragment.requireContext(), android.R.layout.simple_list_item_1, estados));
        b.btnBuscarCep.setOnClickListener(v -> buscarCep());
    }

    private void buscarCep() {
        String cep = UiHelper.texto(b.cep);
        if (cep.length() != 8) {
            b.layoutCep.setError(fragment.getString(R.string.erro_cep_completo));
            return;
        }
        b.layoutCep.setError(null);
        vm.buscarCep(cep).observe(fragment.getViewLifecycleOwner(), r -> {
            b.btnBuscarCep.setEnabled(r.status != Resource.Status.LOADING);
            if (r.status == Resource.Status.ERROR) {
                UiHelper.erro(fragment, r);
            } else if (r.status == Resource.Status.SUCCESS) {
                if (r.data == null || Boolean.TRUE.equals(r.data.erro) || r.data.uf == null) {
                    b.layoutCep.setError(fragment.getString(R.string.erro_cep_inexistente));
                } else {
                    b.estado.setText(r.data.uf, false);
                    b.cidade.setText(r.data.localidade);
                    b.bairro.setText(r.data.bairro);
                    b.rua.setText(r.data.logradouro);
                }
            }
        });
    }

    public void preencher(Usuario u) {
        b.cep.setText(u.cep);
        b.estado.setText(u.estado, false);
        b.cidade.setText(u.cidade);
        b.bairro.setText(u.bairro);
        b.rua.setText(u.rua);
        b.numero.setText(String.valueOf(u.numero));
        b.complemento.setText(u.complemento);
    }

    public boolean validar() {
        boolean ok = true;
        b.layoutCep.setError(null);
        b.layoutEstado.setError(null);
        b.layoutCidade.setError(null);
        b.layoutBairro.setError(null);
        b.layoutRua.setError(null);
        b.layoutNumero.setError(null);
        if (UiHelper.texto(b.cep).length() != 8) {
            b.layoutCep.setError(fragment.getString(R.string.erro_cep_completo));
            ok = false;
        }
        if (!estados.contains(UiHelper.texto(b.estado))) {
            b.layoutEstado.setError(fragment.getString(R.string.erro_estado));
            ok = false;
        }
        if (UiHelper.texto(b.cidade).isEmpty()) {
            b.layoutCidade.setError(fragment.getString(R.string.erro_obrigatorio));
            ok = false;
        }
        if (UiHelper.texto(b.bairro).isEmpty()) {
            b.layoutBairro.setError(fragment.getString(R.string.erro_obrigatorio));
            ok = false;
        }
        if (UiHelper.texto(b.rua).isEmpty()) {
            b.layoutRua.setError(fragment.getString(R.string.erro_obrigatorio));
            ok = false;
        }
        if (!UiHelper.texto(b.numero).matches("\\d{1,6}")) {
            b.layoutNumero.setError(fragment.getString(R.string.erro_obrigatorio));
            ok = false;
        }
        return ok;
    }

    public EnderecoRequest montar() {
        EnderecoRequest r = new EnderecoRequest();
        r.cep = UiHelper.texto(b.cep);
        r.estado = UiHelper.texto(b.estado);
        r.cidade = UiHelper.texto(b.cidade);
        r.bairro = UiHelper.texto(b.bairro);
        r.rua = UiHelper.texto(b.rua);
        r.numero = Integer.parseInt(UiHelper.texto(b.numero));
        r.complemento = UiHelper.texto(b.complemento);
        return r;
    }

    public void aplicar(UsuarioRequest r) {
        EnderecoRequest e = montar();
        r.cep = e.cep;
        r.estado = e.estado;
        r.cidade = e.cidade;
        r.bairro = e.bairro;
        r.rua = e.rua;
        r.numero = e.numero;
        r.complemento = e.complemento;
    }
}
