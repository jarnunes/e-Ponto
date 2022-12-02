package com.jnunes.eponto.controller;

import com.jnunes.eponto.service.ConfiguracaoService;
import com.jnunes.reports.RelatorioEponto;
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
        return RelatorioEponto.obterRelatorio(service.obterConfiguracao());
    }


}
