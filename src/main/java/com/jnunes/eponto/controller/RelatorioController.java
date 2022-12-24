package com.jnunes.eponto.controller;

import com.jnunes.core.commons.ValidationUtils;
import com.jnunes.core.commons.utils.DateUtils;
import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.domain.CreditoMensal;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import com.jnunes.eponto.service.CreditoMensalServiceImpl;
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
import java.time.YearMonth;
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
    @Autowired
    private CreditoMensalServiceImpl creditoMensalService;

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
    @Getter
    private StreamedContent file;


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
        service.save(getForm().getDiasTrabalho(), getForm().getCreditoMensal());
        setRelatorioParaDownload();
        addInfoMessage("relatorio.salvo.sucesso");
    }

    private void setDiasTrabalho() {
        validateEmpty(getForm().getDiasTrabalho(), () -> getForm().setDiasTrabalho(criarLista()));
    }

    public void buscarRelatorio() {
        validateNonEmptyOrElse(internalBuscarDiasTrabalho(), this::setInformacoesCasoLocalizadoRegistros,
            this::setInformacoesCasoNaoLocalizadoRegistros);
        validateNonNullThen(creditoMensalService.findCreditoMesAnterior(getForm().getYearMonth()),
                creditoMensal -> getForm().setCreditoMesesAnteriores(creditoMensal.getCreditoAcumulado()));
        calcularCredito();
    }

    private void setInformacoesCasoLocalizadoRegistros(List<DiaTrabalho> diasTrabalho){
        getForm().setDiasTrabalho(diasTrabalho);
        setRelatorioParaDownload();
        disableDownload = Boolean.FALSE;
    }

    private void setInformacoesCasoNaoLocalizadoRegistros(){
        addWarningMessage("relatorio.sem.registro.para.data.informada", DateUtils.yearMonthFormat(getForm().getYearMonth()));
    }

    public void remove(){
        service.remove(getForm().getDiasTrabalho());
        getForm().setDiasTrabalho(criarLista());
        addInfoMessage("relatorio.removido.sucesso");
        disableDownload = Boolean.TRUE;
        PrimeFaces.current().ajax().update("form:toolbarPanelGroup,form:editPanelGroup");
    }

    private List<DiaTrabalho> internalBuscarDiasTrabalho(){
        return service.findAllByMesAno(getForm().getYearMonth().getMonthValue(), getForm().getYearMonth().getYear());
    }

    private List<DiaTrabalho> criarLista() {
        List<DiaTrabalho> novaLista = new ArrayList<>();
        DateUtils.daysOfMonth(getForm().getYearMonth()).forEach(date -> {
            DiaTrabalho diaTrabalho = new DiaTrabalho();
            diaTrabalho.setDia(date);
            diaTrabalho.setHoraEntrada(obterHoraConfiguradaOuZeroHora(date, configuracao.getInicioExpediente()));
            diaTrabalho.setHoraSaida(obterHoraConfiguradaOuZeroHora(date, configuracao.getFimExpediente()));
            diaTrabalho.setInicioIntervalo(obterHoraConfiguradaOuZeroHora(date, configuracao.getInicioIntervalo()));
            diaTrabalho.setFimIntervalo(obterHoraConfiguradaOuZeroHora(date, configuracao.getFimIntervalo()));
            diaTrabalho.setCredito(0.0);
            novaLista.add(diaTrabalho);
        });
        return novaLista;
    }

    private LocalTime obterHoraConfiguradaOuZeroHora(LocalDate localDate, LocalTime horaConfigurada) {
        return desabilitarAtributo(localDate) ? LocalTime.MIN : horaConfigurada;
    }

    public void onCellEditListener(DiaTrabalho diaTrabalho) {
        diaTrabalho.setCredito(JornadaTrabalhoUtils.calcularCreditoDiario(diaTrabalho));
        calcularCredito();
    }

    public boolean disableCellEdit(DiaTrabalho diaTrabalho){
        return desabilitarAtributo(diaTrabalho.getDia());
    }

    private boolean desabilitarAtributo(LocalDate localDate){
        return DateUtils.isWeekend(localDate) && !habilitarFimDeSemana;
    }

    private void setRelatorioParaDownload() {
        disableDownload = getForm().getDiasTrabalho().stream().allMatch(diaTrabalho -> diaTrabalho.getId() == null);
        file = RelatorioEponto.obterRelatorio(getForm().getDiasTrabalho());
    }

    public void calcularCredito(){
        getForm().getCreditoMensal().setCredito(JornadaTrabalhoUtils.somarCreditoDaLista(getForm().getDiasTrabalho()));
        getForm().getCreditoMensal().setCreditoAcumulado(getForm().getCreditoMesesAnteriores() + getForm().getCreditoMensal().getCredito());
    }


    @Getter
    @Setter
    public static class Form {
        private YearMonth yearMonth;
        private List<DiaTrabalho> diasTrabalho;
        private CreditoMensal creditoMensal;
        private Double creditoMesesAnteriores;


        public Form(){
            this.diasTrabalho = new ArrayList<>();
            this.creditoMensal = new CreditoMensal();
            this.yearMonth = null;
            this.creditoMesesAnteriores = 0.0;
        }
    }

}
