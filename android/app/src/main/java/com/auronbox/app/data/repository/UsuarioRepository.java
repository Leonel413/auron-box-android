package com.auronbox.app.data.repository;

import androidx.lifecycle.LiveData;

import com.auronbox.app.data.model.AdminRequest;
import com.auronbox.app.data.model.EnderecoRequest;
import com.auronbox.app.data.model.LoginResponse;
import com.auronbox.app.data.model.Usuario;
import com.auronbox.app.data.model.UsuarioRequest;
import com.auronbox.app.data.remote.ApiClient;
import com.auronbox.app.util.Resource;

public class UsuarioRepository extends BaseRepository {

    public LiveData<Resource<LoginResponse>> login(String email, String senha) {
        return executar(ApiClient.api().login(email, senha));
    }

    public LiveData<Resource<Usuario>> cadastrar(UsuarioRequest request) {
        return executar(ApiClient.api().cadastrar(request));
    }

    public LiveData<Resource<Usuario>> buscarPorEmail(String email) {
        return executar(ApiClient.api().usuarioPorEmail(email));
    }

    public LiveData<Resource<Usuario>> alterarEndereco(long codigo, EnderecoRequest endereco) {
        return executar(ApiClient.api().alterarEndereco(codigo, endereco));
    }

    public LiveData<Resource<String>> alterarSenha(long codigo, String senhaAtual, String senhaNova) {
        return executar(ApiClient.api().alterarSenha(codigo, senhaAtual, senhaNova));
    }

    public LiveData<Resource<Usuario>> alterarAdmin(long codigo, boolean admin, String senhaSistema) {
        return executar(ApiClient.api().alterarAdmin(codigo, new AdminRequest(admin, senhaSistema)));
    }
}
