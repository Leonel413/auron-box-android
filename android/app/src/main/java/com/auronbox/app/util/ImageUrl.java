package com.auronbox.app.util;

import android.net.Uri;

import com.auronbox.app.BuildConfig;

public final class ImageUrl {

    private ImageUrl() {
    }

    public static String de(String imagem) {
        if (imagem == null || imagem.trim().isEmpty() || "nulo".equals(imagem)) return null;
        if (imagem.startsWith("http://") || imagem.startsWith("https://")) return imagem;
        Uri.Builder builder = Uri.parse(BuildConfig.BASE_URL).buildUpon();
        for (String parte : imagem.split("/")) if (!parte.isEmpty()) builder.appendPath(parte);
        return builder.build().toString();
    }
}
