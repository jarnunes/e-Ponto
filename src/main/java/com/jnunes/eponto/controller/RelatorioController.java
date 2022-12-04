package com.jnunes.eponto.controller;

import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import com.jnunes.eponto.service.RelatorioServiceImpl;
import com.jnunes.springjsf.support.utils.JSFUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class RelatorioController implements Serializable {

    @Autowired
    private ConfiguracaoServiceImpl configuracaoService;

    @Autowired
    private RelatorioServiceImpl service;

    @Getter
    @Setter
    private Configuracao configuracao;

    @Getter
    @Setter
    private LocalDate localDate;

    @Getter
    @Setter
    private List<DiaTrabalho> diasTrabalho;

    @PostConstruct
    public void postConstruct() {
        diasTrabalho = new ArrayList<>();
    }

    public void continuar() {
        configuracao = configuracaoService.obterConfiguracao();
        setDiasTrabalho();
    }

    public void salvarRelatorio() {
        service.save(diasTrabalho);
        JSFUtils.addInfoMessage("relatorio.salvo.sucesso");
    }

    private void setDiasTrabalho() {
        final int mes = localDate.getMonthValue();
        final int ano = localDate.getYear();
        List<DiaTrabalho> resultList = service.findAllByMesAno(mes, ano);
        diasTrabalho = CollectionUtils.isNotEmpty(resultList) ? resultList : obterNovaListaDiasTrabalho(mes, ano);
    }

    private List<DiaTrabalho> obterNovaListaDiasTrabalho(final int mes, final int ano){
        List<DiaTrabalho> novaLista = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        LocalDate firstOfFollowingMonth = yearMonth.plusMonths(1).atDay(1);
        firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(data -> {
            DiaTrabalho diaTrabalho = new DiaTrabalho();
            diaTrabalho.setDia(data);
            diaTrabalho.setHoraEntrada(configuracao.getInicioExpediente());
            diaTrabalho.setHoraSaida(configuracao.getFimExpediente());
            diaTrabalho.setInicioIntervalo(configuracao.getInicioIntervalo());
            diaTrabalho.setFimIntervalo(configuracao.getFimIntervalo());
            diaTrabalho.setCredito(LocalTime.MIN);
            diaTrabalho.setDebito(LocalTime.MIN);
            novaLista.add(diaTrabalho);
        });
        return novaLista;
    }

    private String weekend(LocalDate localDate) {
        return DayOfWeek.SATURDAY.equals(localDate.getDayOfWeek()) ?
                DayOfWeek.SATURDAY.name() : DayOfWeek.SUNDAY.equals(localDate.getDayOfWeek()) ?
                DayOfWeek.SUNDAY.name() : localDate.toString();
    }

}
