package com.nemuel.estoque.api.controller;

import com.nemuel.estoque.api.dto.ProdutoDTO;
import com.nemuel.estoque.api.model.Produto;
import com.nemuel.estoque.api.model.Venda;
import com.nemuel.estoque.api.service.ProdutoService;
import com.nemuel.estoque.api.service.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    
    @Autowired
    private VendaService vendaService;

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
                        "BRL",
                        produto.getQuantidade(),
                        produto.getEstoqueMinimo(),
                        produto.getCategoria(),
                        produto.getCodigoInterno(),
                        produto.getCodigoBarras()
                ))
                .collect(Collectors.toList());
    }

    // Criar um novo produto
    @Operation(summary = "Criar produto", description = "Cria um novo produto no sistema.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Produto criarProduto(@RequestBody Produto produto) {
        // Gera código interno automaticamente se não foi informado
        if (produto.getCodigoInterno() == null || produto.getCodigoInterno().isEmpty()) {
            produto.setCodigoInterno(gerarCodigoInterno(produto.getNome()));
        }
        // Gera código de barras automaticamente se não foi informado
        if (produto.getCodigoBarras() == null || produto.getCodigoBarras().isEmpty()) {
            produto.setCodigoBarras(gerarCodigoBarras());
        }
        return produtoService.salvarProduto(produto);
    }

    // Buscar produto por ID
    @Operation(summary = "Buscar produto por ID", description = "Busca um produto específico pelo seu ID.")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Produto buscarProdutoPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    // Buscar produto por código de barras
    @Operation(summary = "Buscar produto por código de barras", description = "Busca um produto específico pelo seu código de barras.")
    @GetMapping("/buscar/codigo-barras/{codigo}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Produto> buscarPorCodigoBarras(@PathVariable String codigo) {
        Produto produto = produtoService.buscarPorCodigoBarras(codigo);
        if (produto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produto);
    }

    // Deletar produto por ID
    @Operation(summary = "Deletar produto", description = "Deleta um produto específico pelo seu ID.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
    }

    // Atualizar produto existente (incluindo vendas)
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente pelo seu ID. Se a quantidade diminuir, registra uma venda no histórico.")
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

        // Verifica se é uma venda (quantidade diminuiu)
        if (produtoAtualizado.getQuantidade() < produtoExistente.getQuantidade()) {
            int quantidadeVendida = produtoExistente.getQuantidade() - produtoAtualizado.getQuantidade();
            BigDecimal totalVenda = produtoExistente.getPreco().multiply(BigDecimal.valueOf(quantidadeVendida));
            
            Venda venda = new Venda(
                produtoExistente.getId(),
                produtoExistente.getNome(),
                quantidadeVendida,
                produtoExistente.getPreco(),
                totalVenda,
                "Sistema",
                "Venda registrada via sistema"
            );
            vendaService.registrarVenda(venda);
            System.out.println("✅ Venda registrada: " + quantidadeVendida + "x " + produtoExistente.getNome() + " - Total: R$ " + totalVenda);
        }

        // Atualiza os dados do produto
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        produtoExistente.setQuantidade(produtoAtualizado.getQuantidade());
        produtoExistente.setEstoqueMinimo(produtoAtualizado.getEstoqueMinimo());
        produtoExistente.setCategoria(produtoAtualizado.getCategoria());
        produtoExistente.setCodigoInterno(produtoAtualizado.getCodigoInterno());
        produtoExistente.setCodigoBarras(produtoAtualizado.getCodigoBarras());

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
        List<ProdutoDTO> produtosComMoeda = produtoService.getProdutosComMoeda(currency);
        
        // Adiciona os campos extras aos produtos convertidos
        return produtosComMoeda.stream()
                .map(dto -> {
                    Produto produto = produtoService.buscarPorId(dto.getId());
                    return new ProdutoDTO(
                        dto.getId(),
                        dto.getNome(),
                        new java.math.BigDecimal(dto.getPreco().replace(",", ".")),
                        dto.getMoeda(),
                        produto.getQuantidade(),
                        produto.getEstoqueMinimo(),
                        produto.getCategoria(),
                        produto.getCodigoInterno(),
                        produto.getCodigoBarras()
                    );
                })
                .collect(Collectors.toList());
    }

    // Métodos auxiliares para gerar códigos
    private String gerarCodigoInterno(String nome) {
        String prefixo = nome.toUpperCase()
                .replaceAll("[^A-Z0-9]", "")
                .substring(0, Math.min(10, nome.length()));
        return prefixo + "-" + System.currentTimeMillis();
    }

    private String gerarCodigoBarras() {
        return "789" + System.currentTimeMillis();
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