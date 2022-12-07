package com.jnunes.reports.vo;

import com.jnunes.core.commons.utils.DateUtils;
import com.jnunes.eponto.domain.DiaTrabalho;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

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
        LocalDate date = diaTrabalho.getDia();
        this.dia = diaTrabalho.getDia().getDayOfMonth();
        this.entrada = toStringOrWeekDay(diaTrabalho.getHoraEntrada(), date);
        this.saida = toStringOrWeekDay(diaTrabalho.getHoraSaida(), date);
        this.inicioIntervalo = toStringOrWeekDay(diaTrabalho.getInicioIntervalo(), date);
        this.fimIntervalo = toStringOrWeekDay(diaTrabalho.getFimIntervalo(), date);
        this.credito = toString(diaTrabalho.getCredito());
        this.observacao = StringUtils.trimToEmpty(diaTrabalho.getObservacao());
    }

    private String toString(Object localTime) {
        return String.valueOf(localTime);
    }

    private String toStringOrWeekDay(LocalTime localTime, LocalDate date){
        return DateUtils.isWeekend(date) ? DateUtils.shortWeekName(date) : toString(localTime);
    }
}
