package com.jnunes.eponto.service;

import com.jnunes.eponto.domain.DiaTrabalho;

import java.util.List;

public interface RelatorioService {
    void save(List<DiaTrabalho> diasTrabalho);
    void remove(List<DiaTrabalho> diasTrabalho);

    List<DiaTrabalho> findAllByMesAno(Integer mes, Integer ano);
}