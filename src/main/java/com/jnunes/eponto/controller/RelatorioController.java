package com.jnunes.eponto.controller;

import com.jnunes.eponto.controller.vo.RelatorioPesquisaVO;
import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import com.jnunes.eponto.service.RelatorioServiceImpl;
import com.jnunes.eponto.support.JornadaTrabalhoUtils;
import com.jnunes.reports.RelatorioEponto;
import com.jnunes.springjsf.controller.BaseController;
import com.jnunes.springjsf.support.utils.JSFUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.io.*;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
@ManagedBean
public class RelatorioController extends BaseController implements Serializable {

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

    @Getter
    @Setter
    private Form form;

    @Getter
    private Boolean editMode;

    @Getter
    private Boolean disable;

    @Getter
    private Boolean disableDownload;

    public void beforeInit() {
        configuracao = configuracaoService.obterConfiguracao();
        if (configuracao.getId() == null) {
            JSFUtils.addErrorMessage("configuracao.empty");
            disable = Boolean.TRUE;
        }
    }

    @PostConstruct
    public void init() {
        localDate = LocalDate.now();
        form = new Form();
        diasTrabalho = new ArrayList<>();
    }

    public void continuar() {
        setDiasTrabalho();
        setRelatorioParaDownload();
        editMode = Boolean.TRUE;
    }

    public void cancel() {
        editMode = Boolean.FALSE;
    }

    public void salvarRelatorio(){
        service.save(diasTrabalho);
        setRelatorioParaDownload();
        JSFUtils.addInfoMessage("relatorio.salvo.sucesso");
    }

    private void setDiasTrabalho() {
        List<DiaTrabalho> resultList = service.findAllByMesAno(localDate.getMonthValue(), localDate.getYear());
        diasTrabalho = CollectionUtils.isNotEmpty(resultList) ? resultList
                : criarListaDiasTrabalho(localDate.getMonthValue(), localDate.getYear());
    }

    public void buscarRelatorio() {
        if (configuracao.getId() == null)
            JSFUtils.addErrorMessage("configuracao.empty");

        diasTrabalho = service.findAllByMesAno(localDate.getMonthValue(), localDate.getYear());
        diasTrabalho.stream().findFirst().ifPresent(diaTrabalho -> {
            RelatorioPesquisaVO relatorio = new RelatorioPesquisaVO();
            relatorio.setMes(diaTrabalho.getDia().getMonth().name());
            relatorio.setAno(diaTrabalho.getDia().getYear());
            relatorio.setDiasTrabalho(diasTrabalho);
            form.setRelatorios(Collections.singletonList(relatorio));
        });
    }

    private List<DiaTrabalho> criarListaDiasTrabalho(final int mes, final int ano) {
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
            diaTrabalho.setCredito(JornadaTrabalhoUtils.calcularCreditoDiario(diaTrabalho));
            novaLista.add(diaTrabalho);
        });
        return novaLista;
    }

    private String weekend(LocalDate localDate) {
        return DayOfWeek.SATURDAY.equals(localDate.getDayOfWeek()) ?
                DayOfWeek.SATURDAY.name() : DayOfWeek.SUNDAY.equals(localDate.getDayOfWeek()) ?
                DayOfWeek.SUNDAY.name() : localDate.toString();
    }

    public void onCellEdit(DiaTrabalho diaTrabalho) {
        diaTrabalho.setCredito(JornadaTrabalhoUtils.calcularCreditoDiario(diaTrabalho));
    }

    @Getter
    private StreamedContent file;

    private void setRelatorioParaDownload() {
        disableDownload = diasTrabalho.stream().allMatch(diaTrabalho -> diaTrabalho.getId() == null);
        if (!disableDownload) {
            file = RelatorioEponto.obterRelatorio(diasTrabalho);
        }
    }

    public Double getSomatorioCreditoDoRelatorioEmEdicao(){
        return diasTrabalho.stream().map(DiaTrabalho::getCredito).reduce(0.0, Double::sum);
    }
    @Getter
    @Setter
    public static class Form {
        List<RelatorioPesquisaVO> relatorios;
    }

}
