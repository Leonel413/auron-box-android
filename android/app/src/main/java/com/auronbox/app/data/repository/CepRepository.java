package com.auronbox.app.data.repository;

import androidx.lifecycle.LiveData;

import com.auronbox.app.data.model.ViaCepResponse;
import com.auronbox.app.data.remote.ApiClient;
import com.auronbox.app.util.Resource;

public class CepRepository extends BaseRepository {

    public LiveData<Resource<ViaCepResponse>> buscar(String cep) {
        return executar(ApiClient.viaCep().buscar(cep));
    }
}
