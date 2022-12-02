package com.jnunes.eponto.domain;

import com.jnunes.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Getter
@Setter
@Entity
public class Configuracao extends BaseEntity {

    @Column(nullable = false)
    private String organizacao;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String atividade;

    @Column(nullable = false)
    private String funcionario;

    @Column(nullable = false)
    private String funcao;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] logo;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] assinatura;
}
