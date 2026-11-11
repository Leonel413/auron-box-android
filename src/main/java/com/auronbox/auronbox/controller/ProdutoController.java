package com.auronbox.auronbox.controller;

import com.auronbox.auronbox.dto.request.ProdutoRequestDTO;
import com.auronbox.auronbox.dto.response.ProdutoResponseDTO;
import com.auronbox.auronbox.model.ProdutoModel;
import com.auronbox.auronbox.service.ProdutoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @Autowired
    private ModelMapper modelMapper;

    public ProdutoModel converterParaEntidade(ProdutoRequestDTO produtoDTO){
        return modelMapper.map(produtoDTO, ProdutoModel.class);
    }

    public ProdutoResponseDTO converterParaDTO(ProdutoModel produto){
        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    public List<ProdutoResponseDTO> converterListaEmDTO(List<ProdutoModel> listaProdutos){
        List<ProdutoResponseDTO> listaProdutosDTO = new ArrayList<>();
        for(ProdutoModel produto: listaProdutos) {
            listaProdutosDTO.add(modelMapper.map(produto, ProdutoResponseDTO.class));
        }
        return listaProdutosDTO;
    }


    @GetMapping
    public ResponseEntity<?> listar(){
        return new ResponseEntity<>(converterListaEmDTO(service.listarProdutos()), HttpStatus.OK);
    }

    // era /tipo/{tipo}
    @GetMapping(path = "/categoria/{categoria}")
    public ResponseEntity<?> filtrarPorCategoria(@PathVariable String categoria){
        return new ResponseEntity<>(converterListaEmDTO(service.filtrarPorCategoria(categoria)), HttpStatus.OK);
    }

    // novo
    @GetMapping(path = "/marca/{marca}")
    public ResponseEntity<?> filtrarPorMarca(@PathVariable String marca){
        return new ResponseEntity<>(converterListaEmDTO(service.filtrarPorMarca(marca)), HttpStatus.OK);
    }

    @GetMapping(path = "/promocaoDesc")
    public ResponseEntity<?> ordenarPromocaoEmOrdemDecrescente(){
        return new ResponseEntity<>(converterListaEmDTO(service.ordenarPromocaoEmOrdemDecrescente()), HttpStatus.OK);
    }

    @GetMapping(path = "/valorAsc")
    public ResponseEntity<?> ordenarValorEmOrdemCrescente(){
        return new ResponseEntity<>(converterListaEmDTO(service.ordenarValorEmOrdemCrescente()), HttpStatus.OK);
    }

    @GetMapping(path = "/valorDesc")
    public ResponseEntity<?> ordenarValorEmOrdemDecrescente(){
        return new ResponseEntity<>(converterListaEmDTO(service.ordenarValorEmOrdemDecrescente()), HttpStatus.OK);
    }

    // busca por nome ou descrição
    @GetMapping(path = "/busca/{descricao}")
    public ResponseEntity<?> listarPesquisados(@PathVariable String descricao){
        return new ResponseEntity<>(converterListaEmDTO(service.buscarPorDescricao(descricao)), HttpStatus.OK);
    }

    @GetMapping(path = "/{codigo}")
    public ResponseEntity<?> buscarPorID(@PathVariable Long codigo){
        return new ResponseEntity<>(converterParaDTO(service.buscarProdutoPorID(codigo)), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> salvar(@RequestBody ProdutoRequestDTO produtoDTO) {
        ProdutoModel produto = service.salvarProduto(converterParaEntidade(produtoDTO));
        return new ResponseEntity<>(converterParaDTO(produto), HttpStatus.CREATED);
    }

    @PostMapping(path = "/img/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> img(@PathVariable Long codigo,
                                 @RequestParam MultipartFile imagem){
        service.salvarImagem(codigo, imagem);
        return new ResponseEntity<>("Imagem salva com sucesso!", HttpStatus.OK);
    }

    @DeleteMapping(path = "/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletar(@PathVariable Long codigo){
        return new ResponseEntity<>(service.deletarProdutoPorID(codigo), HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletarTodos(){
        return new ResponseEntity<>(service.deletarTodosProdutos(), HttpStatus.OK);
    }
}