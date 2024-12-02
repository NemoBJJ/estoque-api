package com.nemuel.estoque.api.controller;

import com.nemuel.estoque.api.model.Produto;
import com.nemuel.estoque.api.service.GeoService;
import com.nemuel.estoque.api.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vendas")
public class VendasController {

    private final ProdutoService produtoService;
    private final GeoService geoService;

    public VendasController(ProdutoService produtoService, GeoService geoService) {
        this.produtoService = produtoService;
        this.geoService = geoService;
    }

    // 1. Sincronizar Produtos
    @GetMapping("/sincronizar")
    public List<Map<String, String>> sincronizarProdutos() {
        List<Produto> produtos = produtoService.listarProdutos();
        return produtos.stream()
                .map(produto -> Map.of(
                        "produto", produto.getNome(),
                        "status", "Enviado a Plataforma com sucesso"
                ))
                .collect(Collectors.toList());
    }

    // 2. Calcular Frete com Geolocalização
    @GetMapping("/calcular-frete/{id}")
    public Map<String, Object> calcularFreteComGeolocalizacao(@PathVariable Long id, @RequestParam String cep) {
        Produto produto = produtoService.buscarPorId(id);

        // Passo 1: Buscar coordenadas do cliente
        Map<String, Double> coordenadasCliente = geoService.getCoordenadasPorCep(cep);

        // Passo 2: Coordenadas do depósito fictício
        double latitudeOrigem = -23.55052; // São Paulo
        double longitudeOrigem = -46.633308;

        // Passo 3: Calcular distância
        double distanciaKm = geoService.calcularDistancia(latitudeOrigem, longitudeOrigem,
                coordenadasCliente.get("latitude"),
                coordenadasCliente.get("longitude"));

        // Passo 4: Calcular frete
        double freteBase = produto.getPreco() < 1000 ? 50 : produto.getPreco() * 0.05;
        double freteFinal = freteBase + (distanciaKm * 2); // R$2 por km como exemplo

        return Map.of(
                "produto", produto.getNome(),
                "frete", String.format("%.2f", freteFinal),
                "distancia_km", String.format("%.2f", distanciaKm)
        );
    }

    // 3. Simular Pagamento
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
