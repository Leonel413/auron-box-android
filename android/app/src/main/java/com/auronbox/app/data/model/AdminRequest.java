package com.auronbox.app.data.model;

public class AdminRequest {
    public boolean admin;
    public String senhaSistema;

    public AdminRequest(boolean admin, String senhaSistema) {
        this.admin = admin;
        this.senhaSistema = senhaSistema;
    }
}
