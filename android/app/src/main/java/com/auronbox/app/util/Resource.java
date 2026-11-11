package com.auronbox.app.util;

public class Resource<T> {

    public enum Status { LOADING, SUCCESS, ERROR }

    public final Status status;
    public final T data;
    public final String message;
    public final int code;
    public final boolean semConexao;

    private Resource(Status status, T data, String message, int code, boolean semConexao) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.code = code;
        this.semConexao = semConexao;
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null, 0, false);
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null, 200, false);
    }

    public static <T> Resource<T> error(String message, int code, boolean semConexao) {
        return new Resource<>(Status.ERROR, null, message, code, semConexao);
    }

    public boolean sessaoExpirada() {
        return status == Status.ERROR && code == 403;
    }
}
