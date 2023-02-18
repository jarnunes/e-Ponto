package com.jnunes.eponto.service;

import com.jnunes.eponto.model.Configuracao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracaoRepository extends CrudRepository<Configuracao, Long> {}