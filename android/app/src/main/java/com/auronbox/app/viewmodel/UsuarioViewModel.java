package com.auronbox.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.auronbox.app.AuronApp;
import com.auronbox.app.data.model.EnderecoRequest;
import com.auronbox.app.data.model.LoginResponse;
import com.auronbox.app.data.model.Usuario;
import com.auronbox.app.data.model.UsuarioRequest;
import com.auronbox.app.data.model.ViaCepResponse;
import com.auronbox.app.data.repository.CepRepository;
import com.auronbox.app.data.repository.UsuarioRepository;
import com.auronbox.app.util.Fonte;
import com.auronbox.app.util.Formatador;
import com.auronbox.app.util.Resource;

public class UsuarioViewModel extends ViewModel {

    private final UsuarioRepository repo = new UsuarioRepository();
    private final CepRepository cepRepo = new CepRepository();
    private final Fonte<Usuario> perfil = new Fonte<>();

    public LiveData<Resource<Usuario>> getPerfil() {
        return perfil.saida();
    }

    public void carregarPerfil() {
        perfil.trocar(repo.buscarPorEmail(AuronApp.sessao().email()));
    }

    public LiveData<Resource<LoginResponse>> login(String email, String senha) {
        return Fonte.aoSucesso(repo.login(email, senha), r -> {
            if (r != null) AuronApp.sessao().salvarLogin(r);
        });
    }

    public LiveData<Resource<Usuario>> cadastrar(UsuarioRequest request) {
        return repo.cadastrar(request);
    }

    public LiveData<Resource<Usuario>> alterarEndereco(EnderecoRequest endereco) {
        return Fonte.aoSucesso(repo.alterarEndereco(AuronApp.sessao().codigo(), endereco), u -> {
            if (u != null) {
                AuronApp.sessao().atualizarEndereco(Formatador.endereco(u.cep, u.cidade, u.estado, u.bairro, u.rua, u.numero, u.complemento));
            }
        });
    }

    public LiveData<Resource<String>> alterarSenha(String atual, String nova) {
        return repo.alterarSenha(AuronApp.sessao().codigo(), atual, nova);
    }

    public LiveData<Resource<Usuario>> buscarUsuario(String email) {
        return repo.buscarPorEmail(email);
    }

    public LiveData<Resource<Usuario>> alterarAdmin(long codigo, boolean admin, String senhaSistema) {
        return repo.alterarAdmin(codigo, admin, senhaSistema);
    }

    public LiveData<Resource<ViaCepResponse>> buscarCep(String cep) {
        return cepRepo.buscar(cep);
    }
}
