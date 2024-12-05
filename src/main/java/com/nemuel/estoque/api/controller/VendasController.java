package com.nemuel.estoque.api.controller;

import com.nemuel.estoque.api.model.Produto;
import com.nemuel.estoque.api.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vendas")
public class VendasController {

    private final ProdutoService produtoService;

    public VendasController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Sincronizar Produtos
    @GetMapping("/sincronizar")
    public List<Map<String, String>> sincronizarProdutos() {
        List<Produto> produtos = produtoService.listarProdutos();
        return produtos.stream()
                .map(produto -> Map.of(
                        "produto", produto.getNome(),
                        "status", "Sincronizado com sucesso"
                ))
                .collect(Collectors.toList());
    }

    // Calcular Frete
    @GetMapping("/calcular-frete/{id}")
    public Map<String, Object> calcularFrete(@PathVariable Long id, @RequestParam String cep) {
        Produto produto = produtoService.buscarPorId(id);

        // Lógica fictícia para calcular o frete
        double frete = produto.getPreco() < 1000 ? 50 : produto.getPreco() * 0.05;

        return Map.of(
                "produto", produto.getNome(),
                "frete", String.format("%.2f", frete),
                "cep", cep
        );
    }

    // Simular Pagamento
    @GetMapping("/simular-pagamento/{id}")
    public Map<String, Object> simularPagamento(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        double valorFinal = produto.getPreco() * 1.10; // Adicionando taxa de 10%
        return Map.of(
                "produto", produto.getNome(),
                "preco_original", String.format("%.2f", produto.getPreco()),
                "valor_final", String.format("%.2f", valorFinal)
        );
    }
}
