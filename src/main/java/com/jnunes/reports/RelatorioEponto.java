package com.jnunes.reports;

import com.jnunes.core.commons.CommonsUtils;
import com.jnunes.core.commons.context.StaticContextAccessor;
import com.jnunes.core.commons.utils.DateUtils;
import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import com.jnunes.eponto.support.JornadaTrabalhoUtils;
import com.jnunes.reports.vo.DiaTrabalhoVO;
import com.jnunes.reports.vo.RelatorioVO;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.StreamedContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.jnunes.reports.ReportConsts.*;

public class RelatorioEponto extends JasperUtils {

    private static ConfiguracaoServiceImpl getConfiguracaoService() {
        return StaticContextAccessor.getBean(ConfiguracaoServiceImpl.class);
    }

    public static StreamedContent obterStreamedContent(RelatorioVO relatorio, List<DiaTrabalhoVO> diasTrabalho) {
        dataSource = getCollectionDataSource(Collections.singletonList(new Object()));
        Map<String, Object> empParams = getParametros(relatorio, diasTrabalho);
        return getStreamedContent(empParams, TEMPLATE_EPONTO, getFileName());
    }

    private static String getFileName() {
        return PREFIXO_NOME_ARQUIVO + DateUtils.localDateTimeNowLong();
    }

    private static Map<String, Object> getParametros(RelatorioVO relatorio, List<DiaTrabalhoVO> diasTrabalho) {
        Map<String, Object> empParams = CommonsUtils.toMap(relatorio);
        empParams.put(REL_LOGOMARCA, CommonsUtils.toInputStream(relatorio.getLogomarca()));
        empParams.put(REL_ASSINATURA, CommonsUtils.toInputStream(relatorio.getAssinatura()));
        empParams.put(DATA_SOURCE, new JRBeanCollectionDataSource(diasTrabalho));
        return empParams;
    }

    public static StreamedContent obterRelatorio(List<DiaTrabalho> diasTrabalho) {
        return validateNonEmptyThenGetValue(StreamedContent.class, diasTrabalho,
            () -> internalObterRelatorio(diasTrabalho));
    }

    private static StreamedContent internalObterRelatorio(List<DiaTrabalho> diasTrabalho) {
        RelatorioVO vo = criarEstruturaBaseRelatorio();
        setInformacoesComplementares(vo, diasTrabalho);

        List<DiaTrabalhoVO> diasTrabalhoVO = new ArrayList<>();
        validateOrElse(getConfiguracaoService().obterConfiguracao().getHabilitarFimDeSemana(),
            () -> diasTrabalhoVO.addAll(criarListaDiaTrabalhoVO(diasTrabalho)),
            () -> diasTrabalhoVO.addAll(criarListaDiaTrabalhoVOFimDeSemana(diasTrabalho)));
        return obterStreamedContent(vo, diasTrabalhoVO);
    }

    private static List<DiaTrabalhoVO> criarListaDiaTrabalhoVO(List<DiaTrabalho> diasTrabalho){
        return  diasTrabalho.stream().map(DiaTrabalhoVO::new).toList();
    }

    private static List<DiaTrabalhoVO> criarListaDiaTrabalhoVOFimDeSemana(List<DiaTrabalho> diasTrabalho){
        return  diasTrabalho.stream().map( diaTrabalho -> new DiaTrabalhoVO(diaTrabalho, true)).toList();
    }

    private static RelatorioVO criarEstruturaBaseRelatorio() {
        final Configuracao configuracao = getConfiguracaoService().obterConfiguracao();
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

    private static void setInformacoesComplementares(RelatorioVO relatorio, List<DiaTrabalho> diasTrabalho) {
        diasTrabalho.stream().findFirst().ifPresent(diaTrabalho -> {
            String dataFormatada = StringUtils.join(DateUtils.monthName(diaTrabalho.getDia()), "/",
                    diaTrabalho.getDia().getYear());
            relatorio.setDataReferencia(dataFormatada);
            relatorio.setCreditoAtual(JornadaTrabalhoUtils.calcularCreditoTotal(diasTrabalho));
            relatorio.setCreditoTotal(0.0);
        });
    }
}
