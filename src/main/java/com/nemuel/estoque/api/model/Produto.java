package com.nemuel.estoque.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false, name = "estoque_minimo")
    private int estoqueMinimo;

    @Column(length = 100)
    private String categoria;

    @Column(name = "codigo_interno", unique = true, length = 50)
    private String codigoInterno;

    @Column(name = "codigo_barras", unique = true, length = 50)
    private String codigoBarras;

    // Construtor padrão
    public Produto() {
    }

    // Construtor com argumentos (sem os novos campos)
    public Produto(Long id, String nome, int quantidade, BigDecimal preco, int estoqueMinimo) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.estoqueMinimo = estoqueMinimo;
    }

    // Construtor completo com todos os campos
    public Produto(Long id, String nome, int quantidade, BigDecimal preco, int estoqueMinimo,
                   String categoria, String codigoInterno, String codigoBarras) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
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

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                ", preco=" + preco +
                ", estoqueMinimo=" + estoqueMinimo +
                ", categoria='" + categoria + '\'' +
                ", codigoInterno='" + codigoInterno + '\'' +
                ", codigoBarras='" + codigoBarras + '\'' +
                '}';
    }
}