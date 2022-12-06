package com.jnunes.eponto.controller;

import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.service.ConfiguracaoService;
import com.jnunes.eponto.service.RelatorioServiceImpl;
import com.jnunes.reports.RelatorioEponto;
import com.jnunes.reports.vo.DiaTrabalhoVO;
import com.jnunes.reports.vo.RelatorioVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private ConfiguracaoService service;

    @Autowired
    private RelatorioServiceImpl relatorioService;

    @RequestMapping(path = "/pdf")
//    public ResponseEntity<byte[]> getFile() {
//        List<DiaTrabalho> diasTrabalho = relatorioService.findAllByMesAno(12, 2022);
//        if (CollectionUtils.isNotEmpty(diasTrabalho)) {
//            RelatorioVO vo = criarEstruturaBaseRelatorio();
//            setInformacoesComplementares(vo, Objects.requireNonNull(diasTrabalho.stream().findFirst().orElse(null)));
//            return RelatorioEponto.obterRelatorio(vo, diasTrabalho.stream().map(DiaTrabalhoVO::new).collect(Collectors.toList()));
//        }
//        return null;
//    }

    private RelatorioVO criarEstruturaBaseRelatorio() {
        Configuracao configuracao = service.obterConfiguracao();
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

    private void setInformacoesComplementares(RelatorioVO relatorio, DiaTrabalho diaTrabalho) {
        relatorio.setDataReferencia(diaTrabalho.getDia().getMonth().name() + "/" + diaTrabalho.getDia().getYear());
    }


    private void getDias() {
        YearMonth ym = YearMonth.of(2013, Month.JANUARY);
        LocalDate firstOfMonth = ym.atDay(1);
        LocalDate firstOfFollowingMonth = ym.plusMonths(1).atDay(1);
        firstOfMonth.datesUntil(firstOfFollowingMonth).forEach(it -> System.out.println(weekend(it)));
    }

    private String weekend(LocalDate localDate) {
        return DayOfWeek.SATURDAY.equals(localDate.getDayOfWeek()) ?
                DayOfWeek.SATURDAY.name() : DayOfWeek.SUNDAY.equals(localDate.getDayOfWeek()) ?
                DayOfWeek.SUNDAY.name() : localDate.toString();
    }

}
