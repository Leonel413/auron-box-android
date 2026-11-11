package com.auronbox.app.data.remote;

import com.auronbox.app.data.model.AdminRequest;
import com.auronbox.app.data.model.Carrinho;
import com.auronbox.app.data.model.EnderecoRequest;
import com.auronbox.app.data.model.LoginResponse;
import com.auronbox.app.data.model.Pedido;
import com.auronbox.app.data.model.Produto;
import com.auronbox.app.data.model.ProdutoRequest;
import com.auronbox.app.data.model.Usuario;
import com.auronbox.app.data.model.UsuarioRequest;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @POST("usuarios/email/{email}/senha/{senha}")
    Call<LoginResponse> login(@Path("email") String email, @Path("senha") String senha);

    @POST("usuarios")
    Call<Usuario> cadastrar(@Body UsuarioRequest request);

    @GET("usuarios/{email}")
    Call<Usuario> usuarioPorEmail(@Path("email") String email);

    @PUT("usuarios/{codigo}")
    Call<Usuario> alterarEndereco(@Path("codigo") long codigo, @Body EnderecoRequest endereco);

    @PUT("usuarios/{codigo}/senhaAtual/{senhaAtual}/senhaNova/{senhaNova}")
    Call<String> alterarSenha(@Path("codigo") long codigo, @Path("senhaAtual") String senhaAtual, @Path("senhaNova") String senhaNova);

    @PUT("usuarios/{codigo}/admin")
    Call<Usuario> alterarAdmin(@Path("codigo") long codigo, @Body AdminRequest request);

    @GET("produtos")
    Call<List<Produto>> listarProdutos();

    @GET("produtos/categoria/{categoria}")
    Call<List<Produto>> produtosPorCategoria(@Path("categoria") String categoria);

    @GET("produtos/valorAsc")
    Call<List<Produto>> produtosValorCrescente();

    @GET("produtos/valorDesc")
    Call<List<Produto>> produtosValorDecrescente();

    @GET("produtos/busca/{descricao}")
    Call<List<Produto>> buscarProdutos(@Path("descricao") String descricao);

    @GET("produtos/{codigo}")
    Call<Produto> produtoPorId(@Path("codigo") long codigo);

    @POST("produtos")
    Call<Produto> salvarProduto(@Body ProdutoRequest request);

    @Multipart
    @POST("produtos/img/{codigo}")
    Call<String> enviarImagem(@Path("codigo") long codigo, @Part MultipartBody.Part imagem);

    @DELETE("produtos/{codigo}")
    Call<String> excluirProduto(@Path("codigo") long codigo);

    @GET("carrinhos/{codigo}")
    Call<Carrinho> carrinho(@Path("codigo") long codigoUsuario);

    @POST("carrinhos/produto/{codigo}/usuario/{codigoUser}")
    Call<Carrinho> adicionarItem(@Path("codigo") long codigoProduto, @Path("codigoUser") long codigoUsuario);

    @PUT("carrinhos/produto/{codigo}/usuario/{codigoUser}/acao/{acao}")
    Call<Carrinho> alterarItem(@Path("codigo") long codigoProduto, @Path("codigoUser") long codigoUsuario, @Path("acao") int acao);

    @DELETE("carrinhos/produto/{codigoProd}/usuario/{codigoUser}")
    Call<Carrinho> excluirItem(@Path("codigoProd") long codigoProduto, @Path("codigoUser") long codigoUsuario);

    @DELETE("carrinhos/usuario/{codigo}")
    Call<Carrinho> excluirItens(@Path("codigo") long codigoUsuario);

    @GET("pedidos")
    Call<List<Pedido>> listarPedidos();

    @GET("pedidos/cliente/{codigoCliente}")
    Call<List<Pedido>> pedidosDoCliente(@Path("codigoCliente") long codigoCliente);

    @GET("pedidos/pedido/{codigo}")
    Call<Pedido> pedidoPorId(@Path("codigo") long codigo);

    @POST("pedidos/cliente/{codigoCliente}/formaPagamento/{formaPagamento}/quantidadeParcelas/{quantidadeParcelas}")
    Call<Pedido> fazerPedido(@Path("codigoCliente") long codigoCliente, @Path("formaPagamento") String formaPagamento, @Path("quantidadeParcelas") int quantidadeParcelas);

    @PUT("pedidos/pedido/{codigo}/acao/{acao}/motivo/{motivo}")
    Call<Pedido> alterarStatusPedido(@Path("codigo") long codigo, @Path("acao") int acao, @Path("motivo") String motivo);
}
