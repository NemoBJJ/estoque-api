package com.nemuel.estoque.api.dto;

import java.text.DecimalFormat;

public class ProdutoDTO {
    private Long id;
    private String nome;
    private String preco; // Alterado para string formatada
    private String moeda;

    public ProdutoDTO(Long id, String nome, double preco, String moeda) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.id = id;
        this.nome = nome;
        this.preco = df.format(preco);
        this.moeda = moeda;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.preco = df.format(preco);
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }
}
