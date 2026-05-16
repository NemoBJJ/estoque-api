package com.nemuel.estoque.api.controller;

import com.nemuel.estoque.api.model.Venda;
import com.nemuel.estoque.api.service.VendaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @GetMapping("/historico")
    public ResponseEntity<List<Venda>> listarHistoricoVendas() {
        return ResponseEntity.ok(vendaService.listarTodasVendas());
    }

    @GetMapping("/historico/produto/{produtoId}")
    public ResponseEntity<List<Venda>> listarVendasPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(vendaService.buscarVendasPorProduto(produtoId));
    }

    @GetMapping("/historico/periodo")
    public ResponseEntity<List<Venda>> listarVendasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(vendaService.buscarVendasPorPeriodo(inicio, fim));
    }

    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Object>> obterResumoVendas() {
        return ResponseEntity.ok(vendaService.obterResumoVendas());
    }
}
