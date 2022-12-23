package com.jnunes.eponto.service;

import com.jnunes.core.commons.ValidationUtils;
import com.jnunes.eponto.domain.CreditoMensal;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.support.JornadaTrabalhoUtils;
import com.jnunes.springjsf.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class RelatorioServiceImpl extends BaseServiceImpl<DiaTrabalho> implements RelatorioService {

    @Autowired
    private RelatorioRepository repository;

    @Autowired
    private CreditoMensalRepository creditoMensalRepository;

    @Override
    public void save(List<DiaTrabalho> diasTrabalho) {
        ValidationUtils.validateNonEmpty(diasTrabalho, this::saveNonEmpty);
    }

    private void saveNonEmpty(List<DiaTrabalho> diasTrabalho) {
        CreditoMensal creditoMensal = new CreditoMensal();
        creditoMensal.setDataInicioReferencia(diasTrabalho.get(0).getDia());
        creditoMensal.setDataFimReferencia(diasTrabalho.get(diasTrabalho.size() - 1).getDia());
        creditoMensal.setCredito(JornadaTrabalhoUtils.somarCreditoDaLista(diasTrabalho));
        creditoMensalRepository.save(creditoMensal);
        diasTrabalho.forEach(this::save);
    }

    @Override
    public void remove(List<DiaTrabalho> diasTrabalho) {
        ValidationUtils.validateNonEmpty(diasTrabalho, this::removeNomEmpty);
    }

    private void removeNomEmpty(List<DiaTrabalho> diasTrabalho){
        CreditoMensal creditoMensal = creditoMensalRepository.findByDataReferencia(diasTrabalho.get(0).getDia(),
                diasTrabalho.get(diasTrabalho.size() - 1).getDia());
        Optional.ofNullable(creditoMensal).ifPresent(creditoM -> creditoMensalRepository.delete(creditoM));
        diasTrabalho.forEach(diaTrabalho -> this.delete(diaTrabalho.getId()));
    }


    @Override
    public List<DiaTrabalho> findAllByMesAno(Integer mes, Integer ano) {
        return repository.findAllByMesAno(mes, ano);
    }
}
