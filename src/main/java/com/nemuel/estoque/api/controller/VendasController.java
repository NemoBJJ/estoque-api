package com.nemuel.estoque.api.controller;

import com.nemuel.estoque.api.model.Produto;
import com.nemuel.estoque.api.service.GeoService;
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
    private final GeoService geoService;

    public VendasController(ProdutoService produtoService, GeoService geoService) {
        this.produtoService = produtoService;
        this.geoService = geoService;
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

        // Coordenadas fictícias do centro de distribuição (latitude e longitude)
        final double centroLat = -23.550520; // Exemplo: São Paulo
        final double centroLon = -46.633308;

        try {
            // Obtem as coordenadas do CEP fornecido
            Map<String, Double> coordenadas = geoService.getCoordenadasPorCep(cep);
            double destinoLat = coordenadas.get("latitude");
            double destinoLon = coordenadas.get("longitude");

            // Calcula a distância em km
            double distancia = geoService.calcularDistancia(centroLat, centroLon, destinoLat, destinoLon);

            // Lógica para calcular o frete baseado na distância
            BigDecimal taxaPorKm = new BigDecimal("0.40"); // Exemplo: R$ 2.50 por km
            BigDecimal fretePorDistancia = taxaPorKm.multiply(new BigDecimal(distancia)).setScale(2, BigDecimal.ROUND_HALF_UP);

            // Adiciona custo adicional baseado no preço do produto
            BigDecimal freteBase = produto.getPreco().compareTo(new BigDecimal("1000")) < 0
                    ? new BigDecimal("50")
                    : produto.getPreco().multiply(new BigDecimal("0.05")).setScale(2, BigDecimal.ROUND_HALF_UP);

            // Soma o frete base ao frete calculado pela distância
            BigDecimal freteFinal = freteBase.add(fretePorDistancia);

            return Map.of(
                    "produto", produto.getNome(),
                    "frete", freteFinal.toString(),
                    "distancia_km", String.format("%.2f", distancia),
                    "cep", cep
            );
        } catch (Exception e) {
            return Map.of(
                    "erro", "Não foi possível calcular o frete para o CEP fornecido.",
                    "mensagem", e.getMessage()
            );
        }
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
