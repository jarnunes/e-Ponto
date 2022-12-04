package com.jnunes.eponto.service;

import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.springjsf.service.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelatorioRepository extends BaseRepository<DiaTrabalho> {

    @Query("from DiaTrabalho  dt " +
            " where (:mes is null or month (dt.dia) = :mes) " +
            "   and (:ano is null or year (dt.dia) = :ano) " +
            " order by dt.dia ")
    List<DiaTrabalho> findAllByMesAno(@Param("mes") Integer mes, @Param("ano") Integer ano);
}