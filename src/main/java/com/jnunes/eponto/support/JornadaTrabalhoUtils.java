package com.jnunes.eponto.support;

import com.jnunes.core.commons.context.StaticContextAccessor;
import com.jnunes.core.commons.utils.DateUtils;
import com.jnunes.eponto.model.Configuracao;
import com.jnunes.eponto.model.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class JornadaTrabalhoUtils {

    private static ConfiguracaoServiceImpl getConfiguracaoService() {
        return StaticContextAccessor.getBean(ConfiguracaoServiceImpl.class);
    }

    public static Duration jornadaDiariaConfigurada() {
        Configuracao configuracao = getConfiguracaoService().obterConfiguracao();
        return Duration.between(configuracao.getInicioExpediente(), configuracao.getFimExpediente());
    }

    public static Duration intervaloDiarioConfigurado() {
        Configuracao configuracao = getConfiguracaoService().obterConfiguracao();
        return Duration.between(configuracao.getInicioIntervalo(), configuracao.getFimIntervalo());
    }

    public static Double calcularCreditoTotal(List<DiaTrabalho> diasTrabalho) {
        return DateUtils.toHourMinute(calcularCreditoTotalDaLista(diasTrabalho));
    }

    public static Duration calcularCreditoTotalDaLista(List<DiaTrabalho> diasTrabalho) {
        return diasTrabalho.stream().map(JornadaTrabalhoUtils::obterCreditoDiario).reduce(Duration.ZERO, Duration::plus);
    }
    public static Double somarCreditoDaLista(List<DiaTrabalho> diasTrabalho) {
        return diasTrabalho.stream().map(DiaTrabalho::getCredito).reduce(0.0, Double::sum);
    }


    public static Duration duracaoTrabalhoDoDia(DiaTrabalho diaTrabalho) {
        return Duration.between(diaTrabalho.getHoraEntrada(), diaTrabalho.getHoraSaida());
    }

    public static Duration duracaoIntervaloDoDia(DiaTrabalho diaTrabalho) {
        return Duration.between(diaTrabalho.getInicioIntervalo(), diaTrabalho.getFimIntervalo());
    }

    public static Double calcularCreditoDiario(DiaTrabalho diaTrabalho) {
        return DateUtils.toHourMinute(obterCreditoDiario(diaTrabalho));
    }

    private static Duration obterCreditoDiario(DiaTrabalho diaTrabalho) {
        Duration creditoDiario = duracaoTrabalhoDoDia(diaTrabalho).minus(jornadaDiariaConfigurada());
        Duration creditoIntervalo = intervaloDiarioConfigurado().minus(duracaoIntervaloDoDia(diaTrabalho));
        return creditoDiario.plus(creditoIntervalo);
    }

    public static LocalDate primeiraDataDaLista(List<DiaTrabalho> diasTrablaho){
        return diasTrablaho.stream().min(Comparator.comparing(DiaTrabalho::getDia)).map(DiaTrabalho::getDia).orElse(null);
    }

    public static LocalDate ultimaDataDaLista(List<DiaTrabalho> diasTrablaho){
        return diasTrablaho.stream().max(Comparator.comparing(DiaTrabalho::getDia)).map(DiaTrabalho::getDia).orElse(null);
    }

}
