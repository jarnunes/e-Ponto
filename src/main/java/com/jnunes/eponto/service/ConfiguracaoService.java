package com.jnunes.eponto.service;

import com.jnunes.eponto.domain.Configuracao;


public interface ConfiguracaoService {

    void save(Configuracao entity);
    Configuracao obterConfiguracao();
}