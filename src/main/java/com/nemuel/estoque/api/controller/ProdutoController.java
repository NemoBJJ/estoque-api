package com.nemuel.estoque.api.controller;

import com.nemuel.estoque.api.dto.ProdutoDTO;
import com.nemuel.estoque.api.model.Produto;
import com.nemuel.estoque.api.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Listar produtos com suporte a conversão de moeda
    @GetMapping
    public List<ProdutoDTO> listarProdutos(@RequestParam(required = false) String currency) {
        if (currency != null) {
            return produtoService.getProdutosComMoeda(currency);
        }
        return produtoService.listarProdutos()
                .stream()
                .map(produto -> new ProdutoDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getPreco(),
                        "BRL" // Moeda padrão
                ))
                .collect(Collectors.toList());
    }

    // Criar um novo produto
    @PostMapping
    public Produto criarProduto(@RequestBody Produto produto) {
        return produtoService.salvarProduto(produto);
    }

    // Buscar produto por ID
    @GetMapping("/{id}")
    public Produto buscarProdutoPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    // Deletar produto por ID
    @DeleteMapping("/{id}")
    public void deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
    }
}

