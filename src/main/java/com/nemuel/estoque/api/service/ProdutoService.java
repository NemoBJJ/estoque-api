package com.nemuel.estoque.api.service;

import com.nemuel.estoque.api.dto.ProdutoDTO;
import com.nemuel.estoque.api.model.Produto;
import com.nemuel.estoque.api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CurrencyService currencyService;

    public ProdutoService(ProdutoRepository produtoRepository, CurrencyService currencyService) {
        this.produtoRepository = produtoRepository;
        this.currencyService = currencyService;
    }

    // Listar produtos com suporte a conversão de moeda
    public List<ProdutoDTO> getProdutosComMoeda(String currency) {
        List<Produto> produtos = produtoRepository.findAll();
        String targetCurrency = (currency == null || currency.isEmpty()) ? "BRL" : currency;

        // Taxa de conversão: usa 1.0 como taxa padrão para BRL
        final double rate;
        if ("BRL".equalsIgnoreCase(targetCurrency)) {
            rate = 1.0;
        } else {
            Map<String, Object> exchangeRates = currencyService.getExchangeRates();
            try {
                String ratesString = exchangeRates.get("rates").toString();
                rate = Double.parseDouble(ratesString.split(targetCurrency + "=")[1].split(",")[0]);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar a taxa de câmbio para a moeda: " + targetCurrency, e);
            }
        }

        // Retorna produtos com valores formatados
        return produtos.stream()
                .map(produto -> new ProdutoDTO(
                        produto.getId(),
                        produto.getNome(),
                        BigDecimal.valueOf(produto.getPreco() * rate).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                        targetCurrency
                ))
                .collect(Collectors.toList());
    }

    // Listar todos os produtos sem conversão (BRL por padrão)
    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    // Salvar produto
    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    // Buscar produto por ID
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    // Deletar produto por ID
    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }
}
