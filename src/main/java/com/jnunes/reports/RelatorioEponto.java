package com.jnunes.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnunes.reports.vo.DiaTrabalhoVO;
import com.jnunes.reports.vo.RelatorioVO;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.Option;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;

public class RelatorioEponto extends JasperUtils {

    private static final String TEMPLATE_EPONTO = "relatorio_eponto" ;
    private static final String TEMPLATE_TABLE = "table" ;
    public static ResponseEntity<byte[]> obterRelatorio(RelatorioVO relatorio) {
        dataSource = getCollectionDataSource(Collections.singletonList(new Object()));
        Map<String, Object> empParams = relatorioVOToMap(relatorio);
        empParams.put("logomarca", toInputStream(relatorio.getLogomarca()));
        empParams.put("assinatura", toInputStream(relatorio.getAssinatura()));
        empParams.put("ItemDataSource", new JRBeanCollectionDataSource(getDiasTrabalho()));
        return getResponseEntity(empParams, TEMPLATE_EPONTO, "relatorio");
    }

    private static List<DiaTrabalhoVO> getDiasTrabalho() {
        List<DiaTrabalhoVO> diasTrabalho = new ArrayList<>();
        IntStream.range(1, 30).forEach(i -> {
            DiaTrabalhoVO diaA = new DiaTrabalhoVO();
            diaA.setDia(i);
            diaA.setEntrada("08:00");
            diaA.setSaida("17:30");
            diaA.setInicioIntervalo("13:00");
            diaA.setFimIntervalo("14:00");
            diaA.setCredito("00:00");
            diaA.setDebito("00:00");
            diaA.setObservacao("Observação " + i);
            diasTrabalho.add(diaA);
        });

        return diasTrabalho;
    }

    private static Map<String, Object> relatorioVOToMap(RelatorioVO relatorio) {
        ObjectMapper om = new ObjectMapper();
        return om.convertValue(relatorio, Map.class);
    }

    private static InputStream toInputStream(byte[] content){
        return Optional.ofNullable(content).map(ByteArrayInputStream::new).orElse(null);
    }


}
