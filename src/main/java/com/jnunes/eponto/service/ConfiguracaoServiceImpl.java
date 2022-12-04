package com.jnunes.eponto.service;

import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.springjsf.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ConfiguracaoServiceImpl extends BaseServiceImpl<Configuracao> implements ConfiguracaoService {

    private static final Long ENTITY_ID = 1L;
    @Autowired
    private ConfiguracaoRepository repository;

    @Override
    public Configuracao save(Configuracao entity) {
        entity.setId(ENTITY_ID);
        return repository.save(entity);
    }

    @Override
    public Configuracao obterConfiguracao() {
        return repository.findById(ENTITY_ID).orElse(new Configuracao());
    }
}
