package com.auronbox.app.data.remote;

import com.auronbox.app.data.model.ViaCepResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ViaCepService {

    @GET("ws/{cep}/json/")
    Call<ViaCepResponse> buscar(@Path("cep") String cep);
}
