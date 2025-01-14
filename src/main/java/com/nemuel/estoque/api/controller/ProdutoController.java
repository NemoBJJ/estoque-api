package com.nemuel.estoque.api.controller;

import com.nemuel.estoque.api.dto.ProdutoDTO;
import com.nemuel.estoque.api.model.Produto;
import com.nemuel.estoque.api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Listar produtos com suporte a conversão de moeda
    @Operation(summary = "Listar produtos", description = "Retorna a lista de produtos. Opcionalmente, pode converter os preços para a moeda especificada.")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ProdutoDTO> listarProdutos(
            @Parameter(description = "Código da moeda para conversão (ex.: USD, EUR, etc.)", example = "USD")
            @RequestParam(required = false) String currency) {
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
    @Operation(summary = "Criar produto", description = "Cria um novo produto no sistema.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Produto criarProduto(@RequestBody Produto produto) {
        return produtoService.salvarProduto(produto);
    }

    // Buscar produto por ID
    @Operation(summary = "Buscar produto por ID", description = "Busca um produto específico pelo seu ID.")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Produto buscarProdutoPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    // Deletar produto por ID
    @Operation(summary = "Deletar produto", description = "Deleta um produto específico pelo seu ID.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
    }

    // Atualizar produto existente
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente pelo seu ID.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produto> atualizarProduto(
            @PathVariable Long id,
            @RequestBody Produto produtoAtualizado) {
        // Verifica se o produto existe
        Produto produtoExistente = produtoService.buscarPorId(id);
        if (produtoExistente == null) {
            return ResponseEntity.notFound().build();
        }

        // Atualiza os dados do produto
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setPreco(produtoAtualizado.getPreco());

        // Salva as alterações
        Produto produtoSalvo = produtoService.salvarProduto(produtoExistente);

        return ResponseEntity.ok(produtoSalvo);
    }

    // Endpoint para listar produtos com conversão de moeda
    @Operation(summary = "Listar produtos com moeda", description = "Lista produtos com preços convertidos para a moeda especificada.")
    @GetMapping("/com-moeda")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ProdutoDTO> listarProdutosComMoeda(
            @Parameter(description = "Código da moeda para conversão (ex.: USD, EUR, etc.)", example = "USD")
            @RequestParam(required = false) String currency) {
        return produtoService.getProdutosComMoeda(currency);
    }
}

  /*
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
    public Map<String, Object> calcularFrete(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        double frete = produto.getPreco() < 1000 ? 50 : produto.getPreco() * 0.05;
        return Map.of(
                "produto", produto.getNome(),
                "frete", String.format("%.2f", frete)
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
    */

