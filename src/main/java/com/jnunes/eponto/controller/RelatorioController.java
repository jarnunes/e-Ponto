package com.jnunes.eponto.controller;

import com.jnunes.core.commons.Validate;
import com.jnunes.core.commons.utils.DateUtils;
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
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

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
    private Form form;
    @Getter
    private Boolean editMode;
    @Getter
    private Boolean disableDownload;
    @Getter
    private Boolean disabled;

    public void beforeInit() {
        configuracao = configuracaoService.obterConfiguracao();
        Validate.condition(Objects.isNull(configuracao.getId())).then(() -> {
            JSFUtils.addErrorMessage("configuracao.empty");
            disabled = Boolean.TRUE;
        });
        this.form = new Form();
        disableDownload = Boolean.TRUE;
    }


    public void continuar() {
        setDiasTrabalho();
        setRelatorioParaDownload();
        editMode = Boolean.TRUE;
    }

    public void cancel() {
        editMode = Boolean.FALSE;
    }

    public void salvarRelatorio() {
        service.save(getForm().getDiasTrabalho());
        setRelatorioParaDownload();
        JSFUtils.addInfoMessage("relatorio.salvo.sucesso");
    }

    private void setDiasTrabalho() {
        List<DiaTrabalho> resultList = service.findAllByMesAno(getForm().getLocalDate().getMonthValue(), getForm().getLocalDate().getYear());
        getForm().setDiasTrabalho(CollectionUtils.isNotEmpty(resultList) ? resultList
                : criarLista(getForm().getLocalDate().getMonthValue(), getForm().getLocalDate().getYear()));
    }

    public void buscarRelatorio() {
        getForm().setDiasTrabalho(service.findAllByMesAno(getForm().getLocalDate().getMonthValue(), getForm().getLocalDate().getYear()));
        disableDownload = CollectionUtils.isEmpty(getForm().getDiasTrabalho());
    }

    private List<DiaTrabalho> criarLista(final int mes, final int ano) {
        List<DiaTrabalho> novaLista = new ArrayList<>();
        DateUtils.daysOfMonth(ano, mes).forEach(date -> {
            DiaTrabalho diaTrabalho = new DiaTrabalho();
            diaTrabalho.setDia(date);
            diaTrabalho.setHoraEntrada(configuracao.getInicioExpediente());
            diaTrabalho.setHoraSaida(configuracao.getFimExpediente());
            diaTrabalho.setInicioIntervalo(configuracao.getInicioIntervalo());
            diaTrabalho.setFimIntervalo(configuracao.getFimIntervalo());
            diaTrabalho.setCredito(JornadaTrabalhoUtils.calcularCreditoDiario(diaTrabalho));
            novaLista.add(diaTrabalho);
        });
        return novaLista;
    }

    public void onCellEditListener(DiaTrabalho diaTrabalho) {
        diaTrabalho.setCredito(JornadaTrabalhoUtils.calcularCreditoDiario(diaTrabalho));
    }

    @Getter
    private StreamedContent file;

    private void setRelatorioParaDownload() {
        disableDownload = getForm().getDiasTrabalho().stream().allMatch(diaTrabalho -> diaTrabalho.getId() == null);
        Validate.condition(!disableDownload).then(() ->
            file = RelatorioEponto.obterRelatorio(getForm().getDiasTrabalho()));

    }

    public Double getSomatorioCreditoDoRelatorioEmEdicao() {
        return getForm().getDiasTrabalho().stream().map(DiaTrabalho::getCredito).reduce(0.0, Double::sum);
    }

    @Getter
    @Setter
    public static class Form {
        List<RelatorioPesquisaVO> relatorios;
        private LocalDate localDate;
        private List<DiaTrabalho> diasTrabalho;
    }

}
