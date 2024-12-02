package com.nemuel.estoque.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nemuel.estoque.api.repository.CurrencyRepository;
import com.nemuel.estoque.api.model.CurrencyEntity;
import com.nemuel.estoque.api.service.CurrencyService;

import java.util.Map;

@RestController
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyRepository currencyRepository;

    public CurrencyController(CurrencyService currencyService, CurrencyRepository currencyRepository) {
        this.currencyService = currencyService;
        this.currencyRepository = currencyRepository;
    }

    // Endpoint para buscar diretamente da API externa
    @GetMapping("/api/currency-rates/external")
    public Map<String, Object> getExchangeRatesFromApi() {
        return currencyService.getExchangeRates();
    }

    // Endpoint para retornar o registro mais recente salvo no banco
    @GetMapping("/api/currency-rates/latest")
    public CurrencyEntity getLatestRatesFromDatabase() {
        return currencyRepository.findAll()
                .stream()
                .reduce((first, second) -> second) // Retorna o último registro
                .orElse(null);
    }
}
