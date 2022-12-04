package com.jnunes.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnunes.core.commons.context.StaticContextAccessor;
import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import com.jnunes.reports.vo.DiaTrabalhoVO;
import com.jnunes.reports.vo.RelatorioVO;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.model.StreamedContent;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RelatorioEponto extends JasperUtils {

    private static final String TEMPLATE_EPONTO = "relatorio_eponto";
    private static final String PREFIXO_NOME_ARQUIVO = "relatorio_";

    private static ConfiguracaoServiceImpl getConfiguracaoService(){
        return StaticContextAccessor.getBean(ConfiguracaoServiceImpl.class);
    }

    public static ResponseEntity<byte[]> obterRelatorio(RelatorioVO relatorio, List<DiaTrabalhoVO> diasTrabalho) {
        dataSource = getCollectionDataSource(Collections.singletonList(new Object()));
        Map<String, Object> empParams = getParametros(relatorio, diasTrabalho);
        return getResponseEntity(empParams, TEMPLATE_EPONTO, getFileName());
    }

    public static StreamedContent obterRelatorioStreamedContent(RelatorioVO relatorio, List<DiaTrabalhoVO> diasTrabalho) {
        dataSource = getCollectionDataSource(Collections.singletonList(new Object()));
        Map<String, Object> empParams = getParametros(relatorio, diasTrabalho);
        return getStreamedContent(empParams, TEMPLATE_EPONTO, getFileName());
    }
    public static void salvarRelatorio (RelatorioVO relatorio, List<DiaTrabalhoVO> diasTrabalho) {
        dataSource = getCollectionDataSource(Collections.singletonList(new Object()));
        Map<String, Object> empParams = getParametros(relatorio, diasTrabalho);
        salvarArquivoEmDiretorio(empParams, TEMPLATE_EPONTO, getFileName(),
            getConfiguracaoService().obterConfiguracao().getDiretorioDownload());
    }
    private static String getFileName() {
        return PREFIXO_NOME_ARQUIVO + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    private static Map<String, Object> getParametros(RelatorioVO relatorio, List<DiaTrabalhoVO> diasTrabalho) {
        Map<String, Object> empParams = relatorioVOToMap(relatorio);
        empParams.put("logomarca", toInputStream(relatorio.getLogomarca()));
        empParams.put("assinatura", toInputStream(relatorio.getAssinatura()));
        empParams.put("ItemDataSource", new JRBeanCollectionDataSource(diasTrabalho));
        return empParams;
    }

    private static Map<String, Object> relatorioVOToMap(RelatorioVO relatorio) {
        ObjectMapper om = new ObjectMapper();
        return om.convertValue(relatorio, Map.class);
    }

    private static InputStream toInputStream(byte[] content) {
        return Optional.ofNullable(content).map(ByteArrayInputStream::new).orElse(null);
    }

}
