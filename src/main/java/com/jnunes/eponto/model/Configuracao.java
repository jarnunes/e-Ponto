package com.jnunes.eponto.model;

import com.jnunes.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.time.LocalTime;

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

    @Column(nullable = false)
    private LocalTime inicioExpediente;

    @Column(nullable = false)
    private LocalTime fimExpediente;

    @Column(nullable = false)
    private LocalTime inicioIntervalo;

    @Column(nullable = false)
    private LocalTime fimIntervalo;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] logo;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] assinatura;

    private Boolean habilitarFimDeSemana = Boolean.FALSE;

}
