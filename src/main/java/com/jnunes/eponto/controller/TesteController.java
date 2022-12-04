package com.jnunes.eponto.controller;

import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.service.ConfiguracaoService;
import com.jnunes.reports.RelatorioEponto;
import com.jnunes.reports.vo.RelatorioVO;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

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
        getDias();
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


    private void getDias(){
        YearMonth ym = YearMonth.of(2013, Month.JANUARY);
        LocalDate firstOfMonth = ym.atDay(1);
        LocalDate firstOfFollowingMonth = ym.plusMonths(1).atDay(1);
        firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(it -> System.out.println(weekend(it)));
    }

    private String weekend(LocalDate localDate){
        return DayOfWeek.SATURDAY.equals(localDate.getDayOfWeek()) ?
                DayOfWeek.SATURDAY.name() : DayOfWeek.SUNDAY.equals(localDate.getDayOfWeek()) ?
                DayOfWeek.SUNDAY.name() : localDate.toString();
    }

}
