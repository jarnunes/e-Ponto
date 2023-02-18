package com.jnunes.eponto.service;

import com.jnunes.eponto.model.CreditoMensal;
import com.jnunes.eponto.model.DiaTrabalho;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface CreditoMensalService {

    void criarCreditoMensalFromDiasTrabalhados(List<DiaTrabalho> diasTrabalho, CreditoMensal creditoMensal);

    CreditoMensal findCreditoMesAnterior(YearMonth reference);

    CreditoMensal findByDataReferencia(LocalDate inicioReferencia, LocalDate fimReferencia);

    void removeByDataReferencia(LocalDate inicioReferencia, LocalDate fimReferencia);
}