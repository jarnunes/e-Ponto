package com.jnunes.eponto.controller;

import com.jnunes.eponto.controller.vo.RelatorioPesquisaVO;
import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import com.jnunes.eponto.service.RelatorioServiceImpl;
import com.jnunes.eponto.support.JornadaTrabalhoUtils;
import com.jnunes.reports.RelatorioEponto;
import com.jnunes.reports.vo.DiaTrabalhoVO;
import com.jnunes.reports.vo.RelatorioVO;
import com.jnunes.springjsf.support.utils.JSFUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Getter
    @Setter
    private Form form;

    @Getter
    private Boolean editMode;

    @PostConstruct
    public void init() {
        localDate = LocalDate.now();
        form = new Form();
        diasTrabalho = new ArrayList<>();
        configuracao = configuracaoService.obterConfiguracao();
        if (configuracao.getId() == null)
            JSFUtils.addWarningMessage("configuracao.empty");
    }


    public void continuar() {
        setDiasTrabalho();
        editMode = Boolean.TRUE;
    }

    public void cancel() {
        editMode = Boolean.FALSE;
    }


    public void salvarRelatorio() {
        service.save(diasTrabalho);
        JSFUtils.addInfoMessage("entity.success.save", "Relatório");
    }

    private void setDiasTrabalho() {
        final int mes = localDate.getMonthValue();
        final int ano = localDate.getYear();
        List<DiaTrabalho> resultList = service.findAllByMesAno(mes, ano);
        diasTrabalho = CollectionUtils.isNotEmpty(resultList) ? resultList : obterNovaListaDiasTrabalho(mes, ano);
    }

    public void buscar() {
        buscarDiasTrabalho();
        diasTrabalho.stream().findFirst().ifPresent(diaTrabalho -> {
            RelatorioPesquisaVO relatorio = new RelatorioPesquisaVO();
            relatorio.setMes(diaTrabalho.getDia().getMonth().name());
            relatorio.setAno(diaTrabalho.getDia().getYear());
            relatorio.setDiasTrabalho(diasTrabalho);
            form.setRelatorios(Collections.singletonList(relatorio));
        });
    }

    public void buscarDiasTrabalho() {
        final int mes = localDate.getMonthValue();
        final int ano = localDate.getYear();
        diasTrabalho = service.findAllByMesAno(mes, ano);
    }


    private List<DiaTrabalho> obterNovaListaDiasTrabalho(final int mes, final int ano) {
        List<DiaTrabalho> novaLista = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        LocalDate firstOfFollowingMonth = yearMonth.plusMonths(1).atDay(1);
        firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(data -> {
            DiaTrabalho diaTrabalho = new DiaTrabalho();
            diaTrabalho.setDia(data);
            diaTrabalho.setHoraEntrada(configuracao.getInicioExpediente());
            diaTrabalho.setHoraSaida(data.getDayOfMonth() == 5 ?configuracao.getFimExpediente().plus(Duration.ofHours(1L)):configuracao.getFimExpediente());
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
    public void emitirRelatorio() {
        if (CollectionUtils.isNotEmpty(diasTrabalho)) {
            RelatorioVO vo = criarEstruturaBaseRelatorio();
            setInformacoesComplementares(vo, Objects.requireNonNull(diasTrabalho.stream().findFirst().orElse(null)));
            RelatorioEponto.salvarRelatorio(vo, diasTrabalho.stream().map(DiaTrabalhoVO::new).collect(Collectors.toList()));
        }
    }

    private RelatorioVO criarEstruturaBaseRelatorio() {
        RelatorioVO vo = new RelatorioVO();
        vo.setEmpresa(configuracao.getOrganizacao());
        vo.setEndereco(configuracao.getEndereco());
        vo.setAtividade(StringUtils.trimToEmpty(configuracao.getAtividade()));
        vo.setNomeFuncionario(configuracao.getFuncionario());
        vo.setLogomarca(configuracao.getLogo());
        vo.setAssinatura(configuracao.getAssinatura());
        vo.setCargo(StringUtils.trimToEmpty(configuracao.getFuncao()));
        return vo;
    }

    private void setInformacoesComplementares(RelatorioVO relatorio, DiaTrabalho diaTrabalho) {
        relatorio.setDataReferencia(diaTrabalho.getDia().getMonth().name() + "/" + diaTrabalho.getDia().getYear());
    }


    @Getter
    @Setter
    public static class Form {
        List<RelatorioPesquisaVO> relatorios;
    }
}
