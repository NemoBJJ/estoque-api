package com.nemuel.estoque.api.repository;

import com.nemuel.estoque.api.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByDataVendaBetweenOrderByDataVendaDesc(LocalDateTime start, LocalDateTime end);

    List<Venda> findByProdutoIdOrderByDataVendaDesc(Long produtoId);

    List<Venda> findAllByOrderByDataVendaDesc();

    @Query("SELECT SUM(v.total) FROM Venda v WHERE v.dataVenda BETWEEN :inicio AND :fim")
    BigDecimal sumTotalByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT COUNT(v) FROM Venda v WHERE v.dataVenda BETWEEN :inicio AND :fim")
    Long countByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}