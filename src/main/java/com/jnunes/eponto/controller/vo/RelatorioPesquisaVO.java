package com.jnunes.eponto.controller.vo;

import com.jnunes.eponto.domain.DiaTrabalho;
import com.jnunes.eponto.support.JornadaTrabalhoUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RelatorioPesquisaVO {
    private Long id;
    private String referencia;
    private Double credito;
    private List<DiaTrabalho> diasTrabalho;

    public void setDiasTrabalho(List<DiaTrabalho> list) {
        credito = JornadaTrabalhoUtils.calcularCreditoTotal(list);
        this.diasTrabalho = list;
    }
}
