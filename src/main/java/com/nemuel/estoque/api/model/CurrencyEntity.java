package com.nemuel.estoque.api.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class CurrencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String baseCurrency;

    @Lob // Permite armazenar um JSON grande
    private String rates;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }

    public String getRates() { return rates; }
    public void setRates(String rates) { this.rates = rates; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
