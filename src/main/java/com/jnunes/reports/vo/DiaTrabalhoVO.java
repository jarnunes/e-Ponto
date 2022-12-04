package com.jnunes.reports.vo;

import com.jnunes.eponto.domain.DiaTrabalho;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;

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
    private String debito;
    private String observacao;

    public DiaTrabalhoVO(DiaTrabalho diaTrabalho) {
        this.dia = diaTrabalho.getDia().getDayOfMonth();
        this.entrada = toHoraString(diaTrabalho.getHoraEntrada());
        this.saida = toHoraString(diaTrabalho.getHoraSaida());
        this.inicioIntervalo = toHoraString(diaTrabalho.getInicioIntervalo());
        this.fimIntervalo = toHoraString(diaTrabalho.getFimIntervalo());
        this.credito = toHoraString(diaTrabalho.getCredito());
        this.debito = toHoraString(diaTrabalho.getDebito());
        this.observacao = StringUtils.trimToEmpty(diaTrabalho.getObservacao());
    }

    private String toHoraString(LocalTime localTime) {
        return String.valueOf(localTime);
    }
}
