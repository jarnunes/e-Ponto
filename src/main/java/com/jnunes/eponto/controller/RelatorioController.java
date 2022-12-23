package com.jnunes.eponto.controller;

import com.jnunes.core.commons.utils.DateUtils;
import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import com.jnunes.eponto.service.RelatorioServiceImpl;
import com.jnunes.eponto.support.JornadaTrabalhoUtils;
import com.jnunes.reports.RelatorioEponto;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Named
@ManagedBean
@ViewScoped
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

    @Getter
    private Boolean habilitarFimDeSemana;

    public void beforeInit() {
        configuracao = configuracaoService.obterConfiguracao();
        validateNull(configuracao.getId(), () -> {
            addErrorMessage("configuracao.empty");
            disabled = Boolean.TRUE;
        });
        resetValues();
    }

    @PostConstruct
    public void postConstruct() {
        this.file = null;
    }

    public void continuar() {
        setDiasTrabalho();
        setRelatorioParaDownload();
        editMode = Boolean.TRUE;
    }

    public void resetValues() {
        editMode = Boolean.FALSE;
        disableDownload = Boolean.TRUE;
        form = new Form();
        habilitarFimDeSemana = configuracao.getHabilitarFimDeSemana();
    }

    public void salvarRelatorio() {
        service.save(getForm().getDiasTrabalho());
        setRelatorioParaDownload();
        addInfoMessage("relatorio.salvo.sucesso");
    }

    private void setDiasTrabalho() {
        validateEmpty(getForm().getDiasTrabalho(), () -> getForm().setDiasTrabalho(criarLista()));
    }

    public void buscarRelatorio() {
        validateNonEmptyOrElse(internalBuscarDiasTrabalho(), this::setInformacoesCasoLocalizadoRegistros,
            this::setInformacoesCasoNaoLocalizadoRegistros);
    }

    private void setInformacoesCasoLocalizadoRegistros(List<DiaTrabalho> diasTrabalho){
        getForm().setDiasTrabalho(diasTrabalho);
        setRelatorioParaDownload();
        disableDownload = Boolean.FALSE;
    }

    private void setInformacoesCasoNaoLocalizadoRegistros(){
        addWarningMessage("relatorio.sem.registro.para.data.informada", DateUtils.dateMonthFormat(getForm().getLocalDate()));
    }

    public void remove(){
        service.remove(getForm().getDiasTrabalho());
        getForm().setDiasTrabalho(criarLista());
        addInfoMessage("relatorio.removido.sucesso");
        disableDownload = Boolean.TRUE;
        PrimeFaces.current().ajax().update("form:toolbarPanelGroup,form:editPanelGroup");
    }

    private List<DiaTrabalho> internalBuscarDiasTrabalho(){
        return service.findAllByMesAno(getForm().getLocalDate().getMonthValue(), getForm().getLocalDate().getYear());
    }

    private List<DiaTrabalho> criarLista() {
        List<DiaTrabalho> novaLista = new ArrayList<>();
        DateUtils.daysOfMonth(getForm().getLocalDate().getYear(), getForm().getLocalDate().getMonthValue()).forEach(date -> {
            DiaTrabalho diaTrabalho = new DiaTrabalho();
            diaTrabalho.setDia(date);
            diaTrabalho.setHoraEntrada(obterHoraConfiguradaOuMin(date, configuracao.getInicioExpediente()));
            diaTrabalho.setHoraSaida(obterHoraConfiguradaOuMin(date, configuracao.getFimExpediente()));
            diaTrabalho.setInicioIntervalo(obterHoraConfiguradaOuMin(date, configuracao.getInicioIntervalo()));
            diaTrabalho.setFimIntervalo(obterHoraConfiguradaOuMin(date, configuracao.getFimIntervalo()));
            diaTrabalho.setCredito(0.0);
            novaLista.add(diaTrabalho);
        });
        return novaLista;
    }

    private LocalTime obterHoraConfiguradaOuMin(LocalDate localDate, LocalTime horaConfigurada) {
        return desabilitarAtributo(localDate) ? LocalTime.MIN : horaConfigurada;
    }

    public void onCellEditListener(DiaTrabalho diaTrabalho) {
        diaTrabalho.setCredito(JornadaTrabalhoUtils.calcularCreditoDiario(diaTrabalho));
    }

    public boolean disableCellEdit(DiaTrabalho diaTrabalho){
        return desabilitarAtributo(diaTrabalho.getDia());
    }

    private boolean desabilitarAtributo(LocalDate localDate){
        return DateUtils.isWeekend(localDate) && !habilitarFimDeSemana;
    }
    @Getter
    private StreamedContent file;

    public StreamedContent obterArquivo(){
        return file;
    }
    private void setRelatorioParaDownload() {
        disableDownload = getForm().getDiasTrabalho().stream().allMatch(diaTrabalho -> diaTrabalho.getId() == null);
        file = RelatorioEponto.obterRelatorio(getForm().getDiasTrabalho());
    }

    public Double getSomatorioCreditoDoRelatorioEmEdicao() {
        return JornadaTrabalhoUtils.somarCreditoDaLista(getForm().getDiasTrabalho());
    }

    @Getter
    @Setter
    public static class Form {
        private LocalDate localDate;
        private List<DiaTrabalho> diasTrabalho;

        public Form(){
            this.diasTrabalho = new ArrayList<>();
            this.localDate = null;
        }
    }

}
