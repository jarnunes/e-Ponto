package com.jnunes.eponto.service;

import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.springjsf.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class RelatorioServiceImpl extends BaseServiceImpl<DiaTrabalho> implements RelatorioService {

    @Autowired
    private RelatorioRepository repository;

    @Override
    public void save(List<DiaTrabalho> diasTrabalho) {
        for (DiaTrabalho diaTrabalho : diasTrabalho) {
            this.save(diaTrabalho);
        }
    }

    @Override
    public void remove(List<DiaTrabalho> diasTrabalho) {
        for (DiaTrabalho diaTrabalho : diasTrabalho) {
            this.delete(diaTrabalho.getId());
        }
    }

    @Override
    public List<DiaTrabalho> findAllByMesAno(Integer mes, Integer ano) {
        return repository.findAllByMesAno(mes, ano);
    }
}
