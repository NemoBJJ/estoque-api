package com.nemuel.estoque.api.service;

import com.nemuel.estoque.api.model.Produto;
import com.nemuel.estoque.api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        // Log dos produtos no console para debug
        produtos.forEach(produto -> System.out.println("Produto encontrado: " + produto));
        return produtos;
    }

    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }
}
