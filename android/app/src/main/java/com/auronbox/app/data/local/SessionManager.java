package com.auronbox.app.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auronbox.app.data.model.LoginResponse;
import com.auronbox.app.util.Formatador;

public class SessionManager {

    private static final String ARQUIVO = "auron_session";
    private static final String K_TOKEN = "token";
    private static final String K_CODIGO = "codigo";
    private static final String K_EMAIL = "email";
    private static final String K_NOME = "nome";
    private static final String K_ADMIN = "admin";
    private static final String K_ENDERECO = "endereco";
    private static final String K_QTD = "quantidadeItens";

    private final SharedPreferences prefs;
    private final MutableLiveData<Boolean> logado = new MutableLiveData<>();
    private final MutableLiveData<Integer> quantidadeItens = new MutableLiveData<>();

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        logado.setValue(estaLogado());
        quantidadeItens.setValue(estaLogado() ? prefs.getInt(K_QTD, 0) : 0);
    }

    public boolean estaLogado() {
        String token = prefs.getString(K_TOKEN, "");
        return token != null && !token.isEmpty();
    }

    public String token() {
        return prefs.getString(K_TOKEN, "");
    }

    public long codigo() {
        return prefs.getLong(K_CODIGO, 0);
    }

    public String email() {
        return prefs.getString(K_EMAIL, "");
    }

    public String nome() {
        return prefs.getString(K_NOME, "");
    }

    public boolean admin() {
        return prefs.getBoolean(K_ADMIN, false);
    }

    public String endereco() {
        return prefs.getString(K_ENDERECO, "");
    }

    public LiveData<Boolean> logado() {
        return logado;
    }

    public LiveData<Integer> quantidadeItens() {
        return quantidadeItens;
    }

    public void salvarLogin(LoginResponse r) {
        String endereco = Formatador.endereco(r.cep, r.cidade, r.estado, r.bairro, r.rua, r.numero, r.complemento);
        prefs.edit()
                .putString(K_TOKEN, r.token)
                .putLong(K_CODIGO, r.codigo)
                .putString(K_EMAIL, r.email)
                .putString(K_NOME, r.nome)
                .putBoolean(K_ADMIN, r.admin)
                .putString(K_ENDERECO, endereco)
                .putInt(K_QTD, r.quantidadeItens)
                .apply();
        logado.setValue(true);
        quantidadeItens.setValue(r.quantidadeItens);
    }

    public void atualizarEndereco(String endereco) {
        prefs.edit().putString(K_ENDERECO, endereco).apply();
    }

    public void definirQuantidade(int quantidade) {
        prefs.edit().putInt(K_QTD, quantidade).apply();
        quantidadeItens.setValue(quantidade);
    }

    public void logout() {
        prefs.edit().clear().apply();
        logado.setValue(false);
        quantidadeItens.setValue(0);
    }
}
