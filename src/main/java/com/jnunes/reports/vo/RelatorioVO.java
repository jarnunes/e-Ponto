package com.jnunes.reports.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioVO {
    private String empresa;
    private byte[] logomarca;
    private String dataReferencia;
    private String endereco;
    private String nomeFuncionario;
    private String atividade;
    private String cargo;
    private byte[] assinatura;

}
