package com.jnunes.eponto.domain;

import com.jnunes.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class DiaTrabalho extends BaseEntity {

    @Column(nullable = false)
    private LocalDate dia;

    @Column(nullable = false)
    private LocalTime horaEntrada;

    @Column(nullable = false)
    private LocalTime horaSaida;

    @Column(nullable = false)
    private LocalTime inicioIntervalo;

    @Column(nullable = false)
    private LocalTime fimIntervalo;

    private Double credito;

    private String observacao;

    public void setCredito(Double credito) {
        this.credito = BigDecimal.valueOf(credito).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
