package com.jnunes.eponto.controller;

import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.service.ConfiguracaoService;
import com.jnunes.reports.RelatorioEponto;
import com.jnunes.reports.vo.RelatorioVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @Autowired
    private ConfiguracaoService service;

    @RequestMapping(path = "/hello")
    public String helloWorld() {
        return "Hello";
    }

    @RequestMapping(path = "/pdf")
    public ResponseEntity<byte[]> getFile() {
        RelatorioVO vo = obterRelatorio();
        setInformacoesComplementares(vo);
        return RelatorioEponto.obterRelatorio(vo);
    }

    private RelatorioVO obterRelatorio() {
        Configuracao configuracao = service.obterConfiguracao();
        RelatorioVO vo = new RelatorioVO();
        vo.setEmpresa(configuracao.getOrganizacao());
        vo.setEndereco(configuracao.getEndereco());
        vo.setAtividade(configuracao.getAtividade());
        vo.setNomeFuncionario(configuracao.getFuncionario());
        vo.setLogomarca(configuracao.getLogo());
        vo.setAssinatura(configuracao.getAssinatura());
        return vo;
    }

    private void setInformacoesComplementares(RelatorioVO relatorio) {
        relatorio.setDataReferencia("Dezembro/2022");
    }


}
