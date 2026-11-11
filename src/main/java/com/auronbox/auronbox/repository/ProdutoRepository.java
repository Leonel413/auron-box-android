package com.auronbox.auronbox.repository;

import com.auronbox.auronbox.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository <ProdutoModel, Long> {

    @Query(value = "select * from produto where codigo = ?", nativeQuery = true)
    public Optional<ProdutoModel> buscarPorID(Long codigo);

    //Pesquisar
    @Query(value = "select * from produto where descricao like %?% or nome_produto like %?%", nativeQuery = true)
    public List<ProdutoModel> buscarPorDescricao(String descricao, String descricao2);

    //Filtrar
    @Query(value = "select * from produto where categoria = ?", nativeQuery = true)
    public List<ProdutoModel> buscarPorCategoria(String categoria);  // era buscarPorTipo

    @Query(value = "select * from produto where marca = ?", nativeQuery = true)
    public List<ProdutoModel> buscarPorMarca(String marca);  // novo

    //Ordenar
    @Query(value = "select * from produto order by promocao desc", nativeQuery = true)
    public List<ProdutoModel> promocaoDecrescente();

    @Query(value = "select * from produto order by valor_com_desconto asc", nativeQuery = true)
    public List<ProdutoModel> valorCrescente();

    @Query(value = "select * from produto order by valor_com_desconto desc", nativeQuery = true)
    public List<ProdutoModel> valorDecrescente();
}