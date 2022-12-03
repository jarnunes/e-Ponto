package com.jnunes.reports.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaTrabalhoVO {
    private Integer dia;
    private String entrada;
    private String saida;
    private String inicioIntervalo;
    private String fimIntervalo;
    private String credito;
    private String debito;
    private String observacao;
}
