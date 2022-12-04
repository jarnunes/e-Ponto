package com.jnunes.eponto.support;

import com.jnunes.core.commons.context.StaticContextAccessor;
import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;

import java.time.Duration;
import java.util.List;

public class JornadaTrabalhoUtils {

    private static ConfiguracaoServiceImpl getConfiguracaoService() {
        return StaticContextAccessor.getBean(ConfiguracaoServiceImpl.class);
    }

    public static Duration jornadaDiaria() {
        Configuracao configuracao = getConfiguracaoService().obterConfiguracao();
        return Duration.between(configuracao.getInicioExpediente(), configuracao.getFimExpediente());
    }

    public static Double calcularTotalCreditoDeDiasTrabalhados(List<DiaTrabalho> diasTrabalho) {
        return toHoraMinutoDouble(calcularTotalTrabalhoDiario(diasTrabalho));
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

    public static Double calcularCreditoDiario(DiaTrabalho diaTrabalho) {
        return toHoraMinutoDouble(getCreditoDiario(diaTrabalho));
    }

    private static Duration getCreditoDiario(DiaTrabalho diaTrabalho) {
        return getDuracaoTrabalhoDiaria(diaTrabalho).minus(jornadaDiaria());
    }

    private static Double toHoraMinutoDouble(Duration duration) {
        return (double) duration.toMinutes() / Duration.ofHours(1).toMinutes();
    }
}
