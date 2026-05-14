package com.nemuel.estoque.api.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ProdutoDTO {
    private Long id;
    private String nome;
    private String preco; // Alterado para string formatada
    private String moeda;
    private int quantidade;
    private int estoqueMinimo;
    private String categoria;
    private String codigoInterno;
    private String codigoBarras;

    // Construtor original (compatibilidade)
    public ProdutoDTO(Long id, String nome, BigDecimal preco, String moeda) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.id = id;
        this.nome = nome;
        this.preco = df.format(preco);
        this.moeda = moeda;
        this.quantidade = 0;
        this.estoqueMinimo = 0;
        this.categoria = "";
        this.codigoInterno = "";
        this.codigoBarras = "";
    }

    // Construtor com quantidade e estoqueMinimo
    public ProdutoDTO(Long id, String nome, BigDecimal preco, String moeda, int quantidade, int estoqueMinimo) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.id = id;
        this.nome = nome;
        this.preco = df.format(preco);
        this.moeda = moeda;
        this.quantidade = quantidade;
        this.estoqueMinimo = estoqueMinimo;
        this.categoria = "";
        this.codigoInterno = "";
        this.codigoBarras = "";
    }

    // Construtor completo com todos os campos
    public ProdutoDTO(Long id, String nome, BigDecimal preco, String moeda, 
                      int quantidade, int estoqueMinimo,
                      String categoria, String codigoInterno, String codigoBarras) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.id = id;
        this.nome = nome;
        this.preco = df.format(preco);
        this.moeda = moeda;
        this.quantidade = quantidade;
        this.estoqueMinimo = estoqueMinimo;
        this.categoria = categoria;
        this.codigoInterno = codigoInterno;
        this.codigoBarras = codigoBarras;
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

    public void setPreco(BigDecimal preco) {
        DecimalFormat df = new DecimalFormat("#.00");
        this.preco = df.format(preco);
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
}