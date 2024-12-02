package com.nemuel.estoque.api.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import com.nemuel.estoque.api.repository.CurrencyRepository;
import com.nemuel.estoque.api.model.CurrencyEntity;





@Service
public class CurrencyRateUpdater {

    private final CurrencyService currencyService;
    private final CurrencyRepository currencyRepository; // Repositório para salvar os dados

    public CurrencyRateUpdater(CurrencyService currencyService, CurrencyRepository currencyRepository) {
        this.currencyService = currencyService;
        this.currencyRepository = currencyRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // Executa à meia-noite todos os dias
    public void updateCurrencyRates() {
        try {
            Map<String, Object> exchangeRates = currencyService.getExchangeRates();

            // Processar e salvar os dados no banco
            currencyRepository.save(convertToEntity(exchangeRates));

            System.out.println("Taxas de câmbio atualizadas com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar taxas de câmbio: " + e.getMessage());
        }
    }

    private CurrencyEntity convertToEntity(Map<String, Object> exchangeRates) {
        // Lógica para converter os dados retornados pela API em uma entidade que será salva no banco
        CurrencyEntity entity = new CurrencyEntity();
        entity.setBaseCurrency(exchangeRates.get("base").toString());
        entity.setRates(exchangeRates.get("rates").toString()); // Você pode mapear como JSON ou outros formatos
        entity.setUpdatedAt(new Date()); // Certifique-se de importar java.util.Date
        return entity;
    }
}
