package com.nemuel.estoque.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate;

    public CurrencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getExchangeRates() {
        String apiUrl = "https://api.exchangerate-api.com/v4/latest/USD"; // Ajuste o URL conforme necessário
        return restTemplate.getForObject(apiUrl, Map.class);
    }
}
