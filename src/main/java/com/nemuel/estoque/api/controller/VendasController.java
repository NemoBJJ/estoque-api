package com.nemuel.estoque.api.controller;

import com.nemuel.estoque.api.model.Produto;
import com.nemuel.estoque.api.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
        BigDecimal frete = produto.getPreco().compareTo(new BigDecimal("1000")) < 0
                ? new BigDecimal("50")
                : produto.getPreco().multiply(new BigDecimal("0.05")).setScale(2, BigDecimal.ROUND_HALF_UP);

        return Map.of(
                "produto", produto.getNome(),
                "frete", frete.toString(),
                "cep", cep
        );
    }

    // Simular Pagamento
    @GetMapping("/simular-pagamento/{id}")
    public Map<String, Object> simularPagamento(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        BigDecimal valorFinal = produto.getPreco().multiply(new BigDecimal("1.10")).setScale(2, BigDecimal.ROUND_HALF_UP); // Adicionando taxa de 10%
        return Map.of(
                "produto", produto.getNome(),
                "preco_original", produto.getPreco().setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                "valor_final", valorFinal.toString()
        );
    }
}
