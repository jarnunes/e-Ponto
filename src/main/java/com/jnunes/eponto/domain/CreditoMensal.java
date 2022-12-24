package com.jnunes.eponto.domain;

import com.jnunes.core.commons.CommonsUtils;
import com.jnunes.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class CreditoMensal extends BaseEntity {

    @Column(nullable = false)
    private LocalDate dataInicioReferencia;

    @Column(nullable = false)
    private LocalDate dataFimReferencia;

    @Column(nullable = false)
    private Double credito;

    @Column(nullable = false)
    private Double creditoAcumulado;

    public void setCredito(Double credito) {
        this.credito = CommonsUtils.setScale(credito);
    }

    public void setCreditoAcumulado(Double credito){
        this.creditoAcumulado = CommonsUtils.setScale(credito);
    }
}
