package com.jnunes.eponto.controller;

import com.jnunes.core.commons.utils.DateUtils;
import com.jnunes.eponto.service.CreditoMensalServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.YearMonth;
import java.util.Optional;

@Named
@ViewScoped
public class DashboardController extends BaseController implements Serializable {


    @Autowired
    private CreditoMensalServiceImpl creditoMensalService;

    @Getter
    @Setter
    private Form form;

    @PostConstruct
    public void postConstruct() {
        form = new Form();

        YearMonth yearMonth = YearMonth.now();
        Optional.ofNullable(creditoMensalService.findByDataReferencia(DateUtils.firstDayOf(yearMonth),
            DateUtils.lastDayOf(yearMonth)))
            .ifPresent(creditoMensal -> {
                form.setCreditoAtual(creditoMensal.getCredito());
                form.setCreditoTotal(creditoMensal.getCreditoAcumulado());
            });
    }

    @Getter
    @Setter
    public static class Form {
        private Double creditoTotal;
        private Double creditoAtual;

        public Form() {
            this.creditoAtual = 0.0;
            this.creditoTotal = 0.0;
        }
    }

}
