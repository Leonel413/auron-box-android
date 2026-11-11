package com.auronbox.app.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.function.UnaryOperator;

public class Fonte<T> {

    private final MediatorLiveData<Resource<T>> saida = new MediatorLiveData<>();
    private LiveData<Resource<T>> atual;

    public LiveData<Resource<T>> saida() {
        return saida;
    }

    public boolean vazia() {
        return saida.getValue() == null;
    }

    public void trocar(LiveData<Resource<T>> nova) {
        trocar(nova, null);
    }

    public void trocar(LiveData<Resource<T>> nova, UnaryOperator<T> transformacao) {
        if (atual != null) saida.removeSource(atual);
        atual = nova;
        saida.addSource(nova, r -> {
            if (transformacao != null && r.status == Resource.Status.SUCCESS && r.data != null) {
                saida.setValue(Resource.success(transformacao.apply(r.data)));
            } else {
                saida.setValue(r);
            }
        });
    }

    public static <T> LiveData<Resource<T>> aoSucesso(LiveData<Resource<T>> origem, java.util.function.Consumer<T> acao) {
        MediatorLiveData<Resource<T>> out = new MediatorLiveData<>();
        out.addSource(origem, r -> {
            if (r.status == Resource.Status.SUCCESS) acao.accept(r.data);
            out.setValue(r);
        });
        return out;
    }
}
