package com.jnunes.eponto.service;

import com.jnunes.core.commons.ValidationUtils;
import com.jnunes.core.commons.utils.DateUtils;
import com.jnunes.eponto.model.CreditoMensal;
import com.jnunes.eponto.model.DiaTrabalho;
import com.jnunes.eponto.support.JornadaTrabalhoUtils;
import com.jnunes.springjsf.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class CreditoMensalServiceImpl extends BaseServiceImpl<CreditoMensal> implements CreditoMensalService {

    @Autowired
    private CreditoMensalRepository repository;

    @Override
    public void criarCreditoMensalFromDiasTrabalhados(List<DiaTrabalho> diasTrabalho, CreditoMensal creditoMensal) {
        ValidationUtils.validateNonEmpty(diasTrabalho, ()-> saveNonEmpty(diasTrabalho, creditoMensal));
    }

    private void saveNonEmpty(List<DiaTrabalho> diasTrabalho, CreditoMensal creditoMensal) {
        creditoMensal.setDataInicioReferencia(JornadaTrabalhoUtils.primeiraDataDaLista(diasTrabalho));
        creditoMensal.setDataFimReferencia(JornadaTrabalhoUtils.ultimaDataDaLista(diasTrabalho));
        ValidationUtils.validateNonNullOrElse(
                findByDataReferencia(creditoMensal.getDataInicioReferencia(), creditoMensal.getDataFimReferencia()),
                creditoAtual -> atualizarCreditoExistente(creditoMensal, creditoAtual),
                () -> repository.save(creditoMensal));
    }

    private void atualizarCreditoExistente(CreditoMensal novoCredito, CreditoMensal creditoAtual){
        creditoAtual.setCredito(novoCredito.getCredito());
        creditoAtual.setCreditoAcumulado(novoCredito.getCreditoAcumulado());
        repository.save(creditoAtual);
    }

    @Override
    public CreditoMensal findByDataReferencia(LocalDate inicioReferencia, LocalDate fimReferencia){
        return  repository.findByDataReferencia(inicioReferencia, fimReferencia);
    }

    @Override
    public void removeByDataReferencia(LocalDate inicioReferencia, LocalDate fimReferencia) {
        CreditoMensal creditoMensal = repository.findByDataReferencia(inicioReferencia, fimReferencia);
        Optional.ofNullable(creditoMensal).ifPresent(creditoM -> repository.delete(creditoM));
    }

    @Override
    public CreditoMensal findCreditoMesAnterior(YearMonth reference) {
        YearMonth previousYearMonth = DateUtils.previousYearMonth(reference);
        return repository.findByDataReferencia(DateUtils.firstDayOf(previousYearMonth), DateUtils.lastDayOf(previousYearMonth));
    }

}
