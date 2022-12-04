package com.jnunes.reports.vo;

import com.jnunes.eponto.domain.DiaTrabalho;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaTrabalhoVO {
    private Integer dia;
    private String entrada;
    private String saida;
    private String inicioIntervalo;
    private String fimIntervalo;
    private String credito;
    private String observacao;

    public DiaTrabalhoVO(DiaTrabalho diaTrabalho) {
        this.dia = diaTrabalho.getDia().getDayOfMonth();
        this.entrada = toString(diaTrabalho.getHoraEntrada());
        this.saida = toString(diaTrabalho.getHoraSaida());
        this.inicioIntervalo = toString(diaTrabalho.getInicioIntervalo());
        this.fimIntervalo = toString(diaTrabalho.getFimIntervalo());
        this.credito = toString(diaTrabalho.getCredito());
        this.observacao = StringUtils.trimToEmpty(diaTrabalho.getObservacao());
    }

    private String toString(Object localTime) {
        return String.valueOf(localTime);
    }
}
