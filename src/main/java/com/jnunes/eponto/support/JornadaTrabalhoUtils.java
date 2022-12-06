package com.jnunes.eponto.support;

import com.jnunes.core.commons.context.StaticContextAccessor;
import com.jnunes.core.commons.utils.DateUtils;
import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;

import java.time.Duration;
import java.util.List;

public class JornadaTrabalhoUtils {

    private static ConfiguracaoServiceImpl getConfiguracaoService() {
        return StaticContextAccessor.getBean(ConfiguracaoServiceImpl.class);
    }

    public static Duration getConfiguracaoJornadaDiaria() {
        Configuracao configuracao = getConfiguracaoService().obterConfiguracao();
        return Duration.between(configuracao.getInicioExpediente(), configuracao.getFimExpediente());
    }

    public static Duration getConfiguracaoIntervaloDiaria() {
        Configuracao configuracao = getConfiguracaoService().obterConfiguracao();
        return Duration.between(configuracao.getInicioIntervalo(), configuracao.getFimIntervalo());
    }

    public static Double calcularTotalCreditoDeDiasTrabalhados(List<DiaTrabalho> diasTrabalho) {
        return DateUtils.toHourMinute(calcularTotalTrabalhoDiario(diasTrabalho));
    }

    public static Duration calcularTotalTrabalhoDiario(List<DiaTrabalho> diasTrabalho) {
        Duration duration = Duration.ZERO;
        for (DiaTrabalho diaTrabalho : diasTrabalho) {
            duration = duration.plus(getCreditoDiario(diaTrabalho));
        }
        return duration;
    }

    public static Duration getDuracaoTrabalhoDiaria(DiaTrabalho diaTrabalho) {
        return Duration.between(diaTrabalho.getHoraEntrada(), diaTrabalho.getHoraSaida());
    }

    public static Duration getDuracaoIntevaloDiaria(DiaTrabalho diaTrabalho) {
        return Duration.between(diaTrabalho.getInicioIntervalo(), diaTrabalho.getFimIntervalo());
    }

    public static Double calcularCreditoDiario(DiaTrabalho diaTrabalho) {
        return DateUtils.toHourMinute(getCreditoDiario(diaTrabalho));
    }

    private static Duration getCreditoDiario(DiaTrabalho diaTrabalho) {
        Duration creditoDiario = getDuracaoTrabalhoDiaria(diaTrabalho).minus(getConfiguracaoJornadaDiaria());
        Duration creditoIntervalo = getConfiguracaoIntervaloDiaria().minus(getDuracaoIntevaloDiaria(diaTrabalho));
        return creditoDiario.plus(creditoIntervalo);
    }

}
