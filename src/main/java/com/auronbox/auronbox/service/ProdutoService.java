package com.auronbox.auronbox.service;

import com.auronbox.auronbox.exception.RequestException;
import com.auronbox.auronbox.model.ProdutoModel;
import com.auronbox.auronbox.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    // Corrigido: caminho vem do application.properties, não hardcoded
    @Value("${upload.caminho}")
    private String caminhoUpload;


    public List<ProdutoModel> listarProdutos(){
        return produtoRepository.findAll();
    }

    // era filtrarPorTipo
    public List<ProdutoModel> filtrarPorCategoria(String categoria){
        return produtoRepository.buscarPorCategoria(categoria);
    }

    // novo
    public List<ProdutoModel> filtrarPorMarca(String marca){
        return produtoRepository.buscarPorMarca(marca);
    }

    public List<ProdutoModel> ordenarPromocaoEmOrdemDecrescente(){
        return produtoRepository.promocaoDecrescente();
    }

    public List<ProdutoModel> ordenarValorEmOrdemCrescente(){
        return produtoRepository.valorCrescente();
    }

    public List<ProdutoModel> ordenarValorEmOrdemDecrescente(){
        return produtoRepository.valorDecrescente();
    }

    // Corrigido: passa descricao duas vezes por causa dos dois "?" na query
    public List<ProdutoModel> buscarPorDescricao(String descricao){
        return produtoRepository.buscarPorDescricao(descricao, descricao);
    }

    public ProdutoModel buscarProdutoPorID(Long codigo){
        return isProductByCode(codigo);
    }

    public ProdutoModel salvarProduto(ProdutoModel produto) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy   HH:mm:ss");
        Calendar calendar = Calendar.getInstance();

        produto.setValorComDesconto(produto.getValorBase() - produto.getValorBase() * produto.getPromocao() / 100);
        produto.setDataPostagem(formatter.format(calendar.getTime()));
        return produtoRepository.save(produto);
    }

    public ProdutoModel atualizarProduto(ProdutoModel produto){
        return produtoRepository.save(produto);
    }

    public void salvarImagem(Long codigo, MultipartFile imagem){
        if(!imagem.isEmpty()){
            ProdutoModel produto = isProductByCode(codigo);
            String nomeImagem = imagem.getOriginalFilename();
            produto.setImagem("uploads/" + nomeImagem);
            produtoRepository.save(produto);

            try {
                // Corrigido: usa caminho do application.properties
                File diretorio = new File(caminhoUpload);
                if(!diretorio.exists()) diretorio.mkdirs();

                File serverFile = new File(diretorio.getAbsolutePath() + File.separator + nomeImagem);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(imagem.getBytes());
                stream.close();
            } catch (Exception e) {
                throw new RequestException("Erro ao salvar imagem: " + e.getMessage());
            }
        }
    }

    public String deletarProdutoPorID(Long codigo){
        produtoRepository.deleteById(isProductByCode(codigo).getCodigo());
        return "Produto deletado com sucesso!!";
    }

    public String deletarTodosProdutos(){
        produtoRepository.deleteAll();
        return "Todos produtos deletados com sucesso!!";
    }

    // Validações
    public ProdutoModel isProductByCode(Long codigo){
        Optional<ProdutoModel> produto = produtoRepository.buscarPorID(codigo);
        if(produto.isEmpty()) throw new RequestException("Produto inexistente");
        else return produto.get();
    }
}