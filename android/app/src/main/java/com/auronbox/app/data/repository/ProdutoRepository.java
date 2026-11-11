package com.auronbox.app.data.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.auronbox.app.R;
import com.auronbox.app.data.model.Produto;
import com.auronbox.app.data.model.ProdutoRequest;
import com.auronbox.app.data.remote.ApiClient;
import com.auronbox.app.util.Resource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProdutoRepository extends BaseRepository {

    private static final ExecutorService IO = Executors.newSingleThreadExecutor();

    public LiveData<Resource<List<Produto>>> listar() {
        return executar(ApiClient.api().listarProdutos());
    }

    public LiveData<Resource<List<Produto>>> porCategoria(String categoria) {
        return executar(ApiClient.api().produtosPorCategoria(categoria));
    }

    public LiveData<Resource<List<Produto>>> valorCrescente() {
        return executar(ApiClient.api().produtosValorCrescente());
    }

    public LiveData<Resource<List<Produto>>> valorDecrescente() {
        return executar(ApiClient.api().produtosValorDecrescente());
    }

    public LiveData<Resource<List<Produto>>> buscar(String descricao) {
        return executar(ApiClient.api().buscarProdutos(descricao));
    }

    public LiveData<Resource<Produto>> porId(long codigo) {
        return executar(ApiClient.api().produtoPorId(codigo));
    }

    public LiveData<Resource<Produto>> salvar(ProdutoRequest request) {
        return executar(ApiClient.api().salvarProduto(request));
    }

    public LiveData<Resource<String>> excluir(long codigo) {
        return executar(ApiClient.api().excluirProduto(codigo));
    }

    public LiveData<Resource<String>> enviarImagem(Context context, long codigo, Uri uri) {
        MutableLiveData<Resource<String>> saida = new MutableLiveData<>();
        saida.setValue(Resource.<String>loading());
        Context app = context.getApplicationContext();
        Handler main = new Handler(Looper.getMainLooper());
        IO.execute(() -> {
            try {
                ContentResolver resolver = app.getContentResolver();
                byte[] bytes = lerBytes(resolver, uri);
                String tipo = resolver.getType(uri);
                RequestBody corpo = RequestBody.create(bytes, MediaType.parse(tipo != null ? tipo : "image/*"));
                MultipartBody.Part parte = MultipartBody.Part.createFormData("imagem", nomeArquivo(resolver, uri), corpo);
                main.post(() -> {
                    LiveData<Resource<String>> chamada = executar(ApiClient.api().enviarImagem(codigo, parte));
                    chamada.observeForever(new Observer<Resource<String>>() {
                        @Override
                        public void onChanged(Resource<String> r) {
                            saida.setValue(r);
                            if (r.status != Resource.Status.LOADING) chamada.removeObserver(this);
                        }
                    });
                });
            } catch (Exception e) {
                saida.postValue(Resource.<String>error(app.getString(R.string.erro_ler_imagem), 0, false));
            }
        });
        return saida;
    }

    private byte[] lerBytes(ContentResolver resolver, Uri uri) throws Exception {
        try (InputStream in = resolver.openInputStream(uri)) {
            if (in == null) throw new IllegalStateException();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int lido;
            while ((lido = in.read(buffer)) != -1) out.write(buffer, 0, lido);
            return out.toByteArray();
        }
    }

    private String nomeArquivo(ContentResolver resolver, Uri uri) {
        try (Cursor c = resolver.query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
            if (c != null && c.moveToFirst()) {
                String nome = c.getString(0);
                if (nome != null && !nome.isEmpty()) return nome;
            }
        }
        return "imagem_" + System.currentTimeMillis() + ".jpg";
    }
}
