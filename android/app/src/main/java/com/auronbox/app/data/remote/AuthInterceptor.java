package com.auronbox.app.data.remote;

import com.auronbox.app.AuronApp;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        if (publica(original)) return chain.proceed(original);
        String token = AuronApp.sessao().token();
        if (token == null || token.isEmpty()) return chain.proceed(original);
        Request autenticada = original.newBuilder().header("Authorization", "Bearer " + token).build();
        return chain.proceed(autenticada);
    }

    private boolean publica(Request request) {
        List<String> segmentos = request.url().pathSegments();
        if (segmentos.isEmpty() || !"usuarios".equals(segmentos.get(0))) return false;
        if (!"POST".equals(request.method())) return false;
        return segmentos.size() == 1 || (segmentos.size() > 1 && "email".equals(segmentos.get(1)));
    }
}
