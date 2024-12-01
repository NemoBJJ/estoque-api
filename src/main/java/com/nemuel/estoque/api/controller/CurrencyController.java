package com.nemuel.estoque.api.controller;

import java.util.Map;

import com.nemuel.estoque.api.service.CurrencyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/api/currency-rates")
    public Map<String, Object> getExchangeRates() {
        return currencyService.getExchangeRates();
    }
}
