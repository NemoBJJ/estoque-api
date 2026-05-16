package com.nemuel.estoque.api.service;

import com.nemuel.estoque.api.model.Venda;
import com.nemuel.estoque.api.repository.VendaRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;

    public VendaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    public Venda registrarVenda(Venda venda) {
        venda.setDataVenda(LocalDateTime.now());
        return vendaRepository.save(venda);
    }

    public List<Venda> listarTodasVendas() {
        return vendaRepository.findAllByOrderByDataVendaDesc();
    }

    public List<Venda> buscarVendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaRepository.findByDataVendaBetweenOrderByDataVendaDesc(inicio, fim);
    }

    public List<Venda> buscarVendasPorProduto(Long produtoId) {
        return vendaRepository.findByProdutoIdOrderByDataVendaDesc(produtoId);
    }

    public Map<String, Object> obterResumoVendas() {
        LocalDateTime inicioDoDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fimDoDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        LocalDateTime inicioDaSemana = LocalDateTime.now().minusDays(7);
        LocalDateTime inicioDoMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);

        Map<String, Object> resumo = new HashMap<>();
        resumo.put("totalVendas", vendaRepository.count());
        resumo.put("vendasHoje", vendaRepository.countByPeriodo(inicioDoDia, fimDoDia));
        resumo.put("vendasSemana", vendaRepository.countByPeriodo(inicioDaSemana, LocalDateTime.now()));
        resumo.put("vendasMes", vendaRepository.countByPeriodo(inicioDoMes, LocalDateTime.now()));
        resumo.put("faturamentoHoje", vendaRepository.sumTotalByPeriodo(inicioDoDia, fimDoDia));
        resumo.put("faturamentoSemana", vendaRepository.sumTotalByPeriodo(inicioDaSemana, LocalDateTime.now()));
        resumo.put("faturamentoMes", vendaRepository.sumTotalByPeriodo(inicioDoMes, LocalDateTime.now()));

        return resumo;
    }
}
