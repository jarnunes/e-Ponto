package com.jnunes.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnunes.core.commons.context.StaticContextAccessor;
import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import com.jnunes.reports.vo.DiaTrabalhoVO;
import com.jnunes.reports.vo.RelatorioVO;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.StreamedContent;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

public class RelatorioEponto extends JasperUtils {

    private static final String TEMPLATE_EPONTO = "relatorio_eponto";
    private static final String PREFIXO_NOME_ARQUIVO = "relatorio_";

    private static ConfiguracaoServiceImpl getConfiguracaoService() {
        return StaticContextAccessor.getBean(ConfiguracaoServiceImpl.class);
    }

//    public static ResponseEntity<byte[]> obterRelatorio(RelatorioVO relatorio, List<DiaTrabalhoVO> diasTrabalho) {
//        dataSource = getCollectionDataSource(Collections.singletonList(new Object()));
//        Map<String, Object> empParams = getParametros(relatorio, diasTrabalho);
//        return getResponseEntity(empParams, TEMPLATE_EPONTO, getFileName());
//    }

    public static StreamedContent obterStreamedContent(RelatorioVO relatorio, List<DiaTrabalhoVO> diasTrabalho) {
        dataSource = getCollectionDataSource(Collections.singletonList(new Object()));
        Map<String, Object> empParams = getParametros(relatorio, diasTrabalho);
        return getStreamedContent(empParams, TEMPLATE_EPONTO, getFileName());
    }


    public static byte[] getByteArray(RelatorioVO relatorio, List<DiaTrabalhoVO> diasTrabalho) {
        dataSource = getCollectionDataSource(Collections.singletonList(new Object()));
        Map<String, Object> empParams = getParametros(relatorio, diasTrabalho);
        return getByteArray(empParams, TEMPLATE_EPONTO);
    }

    public static void salvarRelatorio(RelatorioVO relatorio, List<DiaTrabalhoVO> diasTrabalho) {
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
        relatorio.setDataReferencia(diaTrabalho.getDia().getMonth().name() + "/" + diaTrabalho.getDia().getYear());
    }
}
