package com.auronbox.app;

import android.app.Application;

import com.auronbox.app.data.local.SessionManager;

public class AuronApp extends Application {

    private static AuronApp instance;
    private SessionManager sessao;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sessao = new SessionManager(this);
    }

    public static AuronApp get() {
        return instance;
    }

    public static SessionManager sessao() {
        return instance.sessao;
    }
}
