package com.jnunes.eponto.service;

import com.jnunes.eponto.model.CreditoMensal;
import com.jnunes.eponto.model.DiaTrabalho;

import java.util.List;

public interface RelatorioService {
    void save(List<DiaTrabalho> diasTrabalho, CreditoMensal creditoMensal);
    void remove(List<DiaTrabalho> diasTrabalho);

    List<DiaTrabalho> findAllByMesAno(Integer mes, Integer ano);
}