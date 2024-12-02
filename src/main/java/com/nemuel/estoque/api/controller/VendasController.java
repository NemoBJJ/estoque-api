package com.nemuel.estoque.api.controller;

import com.nemuel.estoque.api.model.Produto;
import com.nemuel.estoque.api.service.GeoService;
import com.nemuel.estoque.api.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/calcular-frete/{id}")
    public ResponseEntity<Map<String, Object>> calcularFreteComGeolocalizacao(@PathVariable Long id, @RequestParam String cep) {
        try {
            // Passo 1: Validar e formatar o CEP
            String cepFormatado = formatarCep(cep);

            // Passo 2: Buscar o produto
            Produto produto = produtoService.buscarPorId(id);

            // Passo 3: Buscar coordenadas do cliente
            Map<String, Double> coordenadasCliente = geoService.getCoordenadasPorCep(cepFormatado);

            // Passo 4: Coordenadas do depósito fictício
            double latitudeOrigem = -23.55052; // São Paulo
            double longitudeOrigem = -46.633308;

            // Passo 5: Calcular distância
            double distanciaKm = geoService.calcularDistancia(latitudeOrigem, longitudeOrigem,
                    coordenadasCliente.get("latitude"),
                    coordenadasCliente.get("longitude"));

            // Passo 6: Calcular frete ajustado
            double taxaPorKm = 0.10; // R$0,10 por km
            double taxaBase = 20.00; // Taxa fixa de processamento
            double freteFinal = (distanciaKm * taxaPorKm) + taxaBase;

            // Limitar o valor do frete (exemplo: máximo R$100,00)
            freteFinal = Math.min(freteFinal, 1000);

            // Retornar os resultados
            return ResponseEntity.ok(Map.of(
                    "produto", produto.getNome(),
                    "frete", String.format("%.2f", freteFinal),
                    "distancia_km", String.format("%.2f", distanciaKm)
            ));
        } catch (IllegalArgumentException e) {
            // Retorna erro para CEP inválido
            return ResponseEntity.badRequest().body(Map.of(
                    "erro", e.getMessage()
            ));
        } catch (RuntimeException e) {
            // Retorna erro para CEP não encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "erro", "Desculpe, não conseguimos encontrar informações para o CEP informado. Por favor, verifique o CEP ou tente outro."
            ));
        }
    }

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

    /**
     * Valida e formata o CEP no padrão XXXXX-XXX.
     * 
     * @param cep CEP informado pelo usuário
     * @return CEP formatado
     */
    private String formatarCep(String cep) {
        cep = cep.replaceAll("[^0-9]", ""); // Remove caracteres não numéricos
        if (cep.length() != 8) {
            throw new IllegalArgumentException("Formato de CEP inválido: " + cep);
        }
        return cep.substring(0, 5) + "-" + cep.substring(5);
    }
}
