package com.auronbox.app.data.remote;

import com.auronbox.app.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class ApiClient {

    private static ApiService api;
    private static ViaCepService viaCep;

    private ApiClient() {
    }

    public static synchronized ApiService api() {
        if (api == null) {
            OkHttpClient cliente = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(new AuthInterceptor())
                    .build();
            api = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(cliente)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService.class);
        }
        return api;
    }

    public static synchronized ViaCepService viaCep() {
        if (viaCep == null) {
            OkHttpClient cliente = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();
            viaCep = new Retrofit.Builder()
                    .baseUrl("https://viacep.com.br/")
                    .client(cliente)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ViaCepService.class);
        }
        return viaCep;
    }
}
