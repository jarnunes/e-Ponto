package com.jnunes.eponto.service;

import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.springjsf.service.BaseCrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.StreamSupport;

@Component
@Transactional
public class ConfiguracaoServiceImpl extends BaseCrudServiceImpl<Configuracao> implements ConfiguracaoService {

    @Autowired
    private ConfiguracaoRepository repository;

    @Override
    public Configuracao save(Configuracao entity) {
        return super.save(entity);
    }

    @Override
    public Configuracao obterConfiguracao() {
        return StreamSupport.stream(repository.findAll().spliterator(), Boolean.FALSE)
            .findFirst().orElse(new Configuracao());
    }
}
