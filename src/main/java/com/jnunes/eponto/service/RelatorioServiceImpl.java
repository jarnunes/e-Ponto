package com.jnunes.eponto.service;

import com.jnunes.core.commons.ValidationUtils;
import com.jnunes.core.commons.utils.DateUtils;
import com.jnunes.eponto.domain.CreditoMensal;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.springjsf.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Transactional
public class RelatorioServiceImpl extends BaseServiceImpl<DiaTrabalho> implements RelatorioService {

    @Autowired
    private RelatorioRepository repository;

    @Autowired
    private CreditoMensalServiceImpl creditoMensalService;

    @Override
    public void save(List<DiaTrabalho> diasTrabalho, CreditoMensal creditoMensal) {
        ValidationUtils.validateNonEmpty(diasTrabalho, () -> saveNonEmpty(diasTrabalho, creditoMensal));
    }

    private void saveNonEmpty(List<DiaTrabalho> diasTrabalho, CreditoMensal creditoMensal) {
        creditoMensalService.criarCreditoMensalFromDiasTrabalhados(diasTrabalho, creditoMensal);
        diasTrabalho.forEach(this::save);
    }

    @Override
    public void remove(List<DiaTrabalho> diasTrabalho) {
        ValidationUtils.validateNonEmpty(diasTrabalho, this::removeNonEmpty);
    }

    private void removeNonEmpty(List<DiaTrabalho> diasTrabalho){
        List<LocalDate> listaDeDatas = diasTrabalho.stream().map(DiaTrabalho::getDia).toList();
        creditoMensalService.removeByDataReferencia(DateUtils.firstDayOf(listaDeDatas), DateUtils.lastDayOf(listaDeDatas));
        diasTrabalho.forEach(diaTrabalho -> this.delete(diaTrabalho.getId()));
    }

    @Override
    public List<DiaTrabalho> findAllByMesAno(Integer mes, Integer ano) {
        return repository.findAllByMesAno(mes, ano);
    }
}
