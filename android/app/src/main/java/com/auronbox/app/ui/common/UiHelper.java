package com.auronbox.app.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.auronbox.app.AuronApp;
import com.auronbox.app.R;
import com.auronbox.app.databinding.ItemInfoRowBinding;
import com.auronbox.app.databinding.LayoutStateBinding;
import com.auronbox.app.ui.admin.AdminActivity;
import com.auronbox.app.util.ImageUrl;
import com.auronbox.app.util.Resource;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public final class UiHelper {

    private UiHelper() {
    }

    public static String texto(TextView view) {
        CharSequence valor = view.getText();
        return valor == null ? "" : valor.toString().trim();
    }

    public static void dialogo(Context context, String titulo, String mensagem, Runnable aoFechar) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setCancelable(aoFechar == null)
                .setPositiveButton(R.string.ok, (d, w) -> {
                    if (aoFechar != null) aoFechar.run();
                })
                .show();
    }

    public static void confirmar(Context context, int titulo, int mensagem, Runnable aoConfirmar) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.confirmar, (d, w) -> aoConfirmar.run())
                .show();
    }

    public static void erro(Fragment fragment, Resource<?> r) {
        if (!fragment.isAdded()) return;
        Context context = fragment.requireContext();
        if (r.sessaoExpirada()) {
            AuronApp.sessao().logout();
            dialogo(context, context.getString(R.string.titulo_sessao), context.getString(R.string.msg_sessao_expirada), () -> {
                if (fragment.isAdded()) irParaLogin(fragment);
            });
            return;
        }
        int titulo = r.semConexao ? R.string.titulo_sem_conexao : R.string.titulo_erro;
        dialogo(context, context.getString(titulo), r.message, null);
    }

    public static void irParaLogin(Fragment fragment) {
        if (fragment.requireActivity() instanceof AdminActivity) {
            fragment.requireActivity().finish();
        } else {
            NavHostFragment.findNavController(fragment).navigate(R.id.action_global_loginFragment);
        }
    }

    public static void dialogoLogin(Fragment fragment, int mensagem) {
        new MaterialAlertDialogBuilder(fragment.requireContext())
                .setTitle(R.string.titulo_atencao)
                .setMessage(mensagem)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.entrar, (d, w) -> irParaLogin(fragment))
                .show();
    }

    public static void estado(LayoutStateBinding s, View conteudo, Resource<?> r, boolean temConteudo, boolean vazio, String msgVazio, Runnable tentar) {
        if (r.status == Resource.Status.LOADING) {
            if (temConteudo) esconderEstado(s, conteudo);
            else mostrarEstado(s, conteudo, true, null, null, null);
        } else if (r.status == Resource.Status.ERROR) {
            mostrarEstado(s, conteudo, false, r.message, s.getRoot().getContext().getString(R.string.tentar_novamente), tentar);
        } else if (vazio) {
            mostrarEstado(s, conteudo, false, msgVazio, null, null);
        } else {
            esconderEstado(s, conteudo);
        }
    }

    public static void estadoLogin(LayoutStateBinding s, View conteudo, int mensagem, Runnable aoEntrar) {
        Context context = s.getRoot().getContext();
        mostrarEstado(s, conteudo, false, context.getString(mensagem), context.getString(R.string.entrar), aoEntrar);
    }

    public static void esconderEstado(LayoutStateBinding s, View conteudo) {
        s.getRoot().setVisibility(View.GONE);
        conteudo.setVisibility(View.VISIBLE);
    }

    private static void mostrarEstado(LayoutStateBinding s, View conteudo, boolean carregando, String mensagem, String rotulo, Runnable acao) {
        conteudo.setVisibility(View.GONE);
        s.getRoot().setVisibility(View.VISIBLE);
        s.progress.setVisibility(carregando ? View.VISIBLE : View.GONE);
        s.mensagem.setVisibility(mensagem != null ? View.VISIBLE : View.GONE);
        s.mensagem.setText(mensagem);
        s.btnAcao.setVisibility(acao != null ? View.VISIBLE : View.GONE);
        if (acao != null) {
            s.btnAcao.setText(rotulo);
            s.btnAcao.setOnClickListener(v -> acao.run());
        }
    }

    public static TextView linha(LinearLayout pai, String rotulo, String valor) {
        ItemInfoRowBinding b = ItemInfoRowBinding.inflate(LayoutInflater.from(pai.getContext()), pai, false);
        b.rotulo.setText(rotulo);
        b.valor.setText(valor == null || valor.isEmpty() ? "-" : valor);
        pai.addView(b.getRoot());
        return b.valor;
    }

    public static void imagem(ImageView view, String caminho) {
        Glide.with(view)
                .load(ImageUrl.de(caminho))
                .placeholder(R.drawable.ic_product)
                .error(R.drawable.ic_product)
                .fallback(R.drawable.ic_product)
                .into(view);
    }

    public static void esconderTeclado(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
