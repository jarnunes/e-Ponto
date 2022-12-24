package com.jnunes.eponto.service;

import com.jnunes.eponto.domain.CreditoMensal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CreditoMensalRepository extends CrudRepository<CreditoMensal, Long> {

    @Query("select max(cm) from CreditoMensal cm " +
            " where (cm.dataInicioReferencia >= :dataInicioReferencia) " +
            "   and (cm.dataFimReferencia  <= :dataFimReferencia) ")
    CreditoMensal findByDataReferencia(@Param("dataInicioReferencia") LocalDate dataInicioReferencia,
        @Param("dataFimReferencia") LocalDate dataFimReferencia);
}