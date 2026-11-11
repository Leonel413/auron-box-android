package com.auronbox.app.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auronbox.app.AuronApp;
import com.auronbox.app.R;
import com.auronbox.app.data.model.ErroApi;
import com.auronbox.app.util.NetworkUtils;
import com.auronbox.app.util.Resource;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

abstract class BaseRepository {

    private static final Gson GSON = new Gson();

    protected <T> LiveData<Resource<T>> executar(Call<T> chamada) {
        MutableLiveData<Resource<T>> live = new MutableLiveData<>();
        live.setValue(Resource.<T>loading());
        chamada.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    live.setValue(Resource.success(response.body()));
                } else {
                    live.setValue(Resource.<T>error(mensagem(response), response.code(), false));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                live.setValue(Resource.<T>error(mensagemFalha(t), 0, t instanceof IOException));
            }
        });
        return live;
    }

    private String mensagem(Response<?> response) {
        int codigo = response.code();
        if (codigo == 403) return texto(R.string.erro_403);
        try {
            if (response.errorBody() != null) {
                ErroApi erro = GSON.fromJson(response.errorBody().string(), ErroApi.class);
                if (erro != null && erro.message != null && !erro.message.isEmpty()) return erro.message;
            }
        } catch (Exception ignorada) {
        }
        if (codigo == 404) return texto(R.string.erro_404);
        if (codigo >= 500) return AuronApp.get().getString(R.string.erro_servidor, codigo);
        return AuronApp.get().getString(R.string.erro_http, codigo);
    }

    private String mensagemFalha(Throwable t) {
        if (t instanceof IOException) {
            return NetworkUtils.temInternet(AuronApp.get()) ? texto(R.string.erro_servidor_offline) : texto(R.string.erro_sem_internet);
        }
        return AuronApp.get().getString(R.string.erro_inesperado, String.valueOf(t.getMessage()));
    }

    private String texto(int id) {
        return AuronApp.get().getString(id);
    }
}
