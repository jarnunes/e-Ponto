package com.jnunes.reports;

import com.jnunes.core.commons.context.StaticContextAccessor;
import com.jnunes.core.commons.utils.DateUtils;
import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import com.jnunes.eponto.support.AppUtils;
import com.jnunes.reports.vo.DiaTrabalhoVO;
import com.jnunes.reports.vo.RelatorioVO;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.StreamedContent;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RelatorioEponto extends JasperUtils {

    private static final String TEMPLATE_EPONTO = "relatorio_eponto";
    private static final String PREFIXO_NOME_ARQUIVO = "relatorio_";

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
        Map<String, Object> empParams = AppUtils.toMap(relatorio);
        empParams.put("logomarca", AppUtils.toInputStream(relatorio.getLogomarca()));
        empParams.put("assinatura", AppUtils.toInputStream(relatorio.getAssinatura()));
        empParams.put("ItemDataSource", new JRBeanCollectionDataSource(diasTrabalho));
        return empParams;
    }

    public static StreamedContent obterRelatorio(List<DiaTrabalho> diasTrabalho) {
        if (CollectionUtils.isNotEmpty(diasTrabalho)) {
            RelatorioVO vo = criarEstruturaBaseRelatorio();
            setInformacoesComplementares(vo, Objects.requireNonNull(diasTrabalho.stream().findFirst().orElse(null)));
            return obterStreamedContent(vo, diasTrabalho.stream().map(DiaTrabalhoVO::new).collect(Collectors.toList()));
        }
        return null;
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

    private static void setInformacoesComplementares(RelatorioVO relatorio, DiaTrabalho diaTrabalho) {
        String dataFormatada = StringUtils.join(DateUtils.monthName(diaTrabalho.getDia()), "/",
                diaTrabalho.getDia().getYear());
        relatorio.setDataReferencia(dataFormatada);
    }
}
