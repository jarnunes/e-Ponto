package com.jnunes.eponto.controller;

import com.jnunes.core.commons.MediaType;
import com.jnunes.core.commons.utils.DateUtils;
import com.jnunes.eponto.controller.vo.RelatorioPesquisaVO;
import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import com.jnunes.eponto.service.RelatorioServiceImpl;
import com.jnunes.eponto.support.JornadaTrabalhoUtils;
import com.jnunes.reports.RelatorioEponto;
import com.jnunes.springjsf.support.utils.PFUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.jnunes.reports.ReportConsts.BASE_REPORTS_PATH;

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
        this.novoArquivo = null;
    }

    public void continuar() {
        setDiasTrabalho();
        setRelatorioParaDownload();
        obterNovoArquivo();
        editMode = Boolean.TRUE;
    }

    public void resetValues() {
        editMode = Boolean.FALSE;
        disableDownload = Boolean.TRUE;
        form = new Form();
    }

    public void salvarRelatorio() {
        service.save(getForm().getDiasTrabalho());
        setRelatorioParaDownload();
        addInfoMessage("relatorio.salvo.sucesso");
    }

    public void salvarNoDisco(){
        RelatorioEponto.salvarRelatorioEmDisco(getForm().getDiasTrabalho());
    }

    private void setDiasTrabalho() {
        validateEmpty(getForm().getDiasTrabalho(), () -> getForm().setDiasTrabalho(criarLista()));
    }

    public void buscarRelatorio() {
        validateNonEmpty(internalBuscarDiasTrabalho(), diasTrabalho -> {
            getForm().setDiasTrabalho(diasTrabalho);
            setRelatorioParaDownload();
            disableDownload = Boolean.FALSE;
        });
    }

    private List<DiaTrabalho> internalBuscarDiasTrabalho(){
        return service.findAllByMesAno(getForm().getLocalDate().getMonthValue(), getForm().getLocalDate().getYear());
    }

    private List<DiaTrabalho> criarLista() {
        List<DiaTrabalho> novaLista = new ArrayList<>();
        DateUtils.daysOfMonth(getForm().getLocalDate().getYear(), getForm().getLocalDate().getMonthValue()).forEach(date -> {
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

    public StreamedContent obterArquivo(){
        return file;
    }
    private void setRelatorioParaDownload() {
        disableDownload = getForm().getDiasTrabalho().stream().allMatch(diaTrabalho -> diaTrabalho.getId() == null);
        file = RelatorioEponto.obterRelatorio(getForm().getDiasTrabalho());
    }

    public Double getSomatorioCreditoDoRelatorioEmEdicao() {
        return getForm().getDiasTrabalho().stream().map(DiaTrabalho::getCredito).reduce(0.0, Double::sum);
    }

    @Getter
    private StreamedContent novoArquivo;

    public void obterNovoArquivo() {
        try {
            String resorceLocation = StringUtils.join("classpath:", BASE_REPORTS_PATH, "example.pdf");
            InputStream stream = new FileInputStream(ResourceUtils.getFile(resorceLocation).getAbsolutePath());
            novoArquivo = PFUtils.toStreamedContent(stream, MediaType.PDF, "example");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
