package com.auronbox.app.data.repository;

import androidx.lifecycle.LiveData;

import com.auronbox.app.AuronApp;
import com.auronbox.app.data.model.Pedido;
import com.auronbox.app.data.remote.ApiClient;
import com.auronbox.app.util.Resource;

import java.util.List;

public class PedidoRepository extends BaseRepository {

    public LiveData<Resource<List<Pedido>>> doCliente() {
        return executar(ApiClient.api().pedidosDoCliente(AuronApp.sessao().codigo()));
    }

    public LiveData<Resource<List<Pedido>>> todos() {
        return executar(ApiClient.api().listarPedidos());
    }

    public LiveData<Resource<Pedido>> porId(long codigo) {
        return executar(ApiClient.api().pedidoPorId(codigo));
    }

    public LiveData<Resource<Pedido>> fazerPedido(String formaPagamento, int parcelas) {
        return executar(ApiClient.api().fazerPedido(AuronApp.sessao().codigo(), formaPagamento, parcelas));
    }

    public LiveData<Resource<Pedido>> alterarStatus(long codigo, int acao, String motivo) {
        return executar(ApiClient.api().alterarStatusPedido(codigo, acao, motivo.replace('/', '-').replace('\\', '-')));
    }
}
