package com.jnunes.reports;

import com.jnunes.eponto.domain.Configuracao;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class RelatorioEponto {

    public static ResponseEntity<byte[]> obterRelatorio(Configuracao configuracao) {
        Map<String, Object> empParams = toMap(configuracao);
        empParams.put("organizacao", configuracao.getOrganizacao());
        return JasperUtils.getResponseEntity(empParams, "relatorio", "relatorio");
    }

    private static Map<String, Object> toMap(Configuracao configuracao) {
        Map<String, Object> empParams = new HashMap<>();
        empParams.put("organizacao", configuracao.getOrganizacao());
        return empParams;
    }
}
