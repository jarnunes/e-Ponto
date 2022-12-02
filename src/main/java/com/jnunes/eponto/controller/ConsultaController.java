package com.jnunes.eponto.controller;

import com.jnunes.core.domain.BaseEntity;
import com.jnunes.springjsf.controller.BaseControllerJSF;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ConsultaController extends BaseControllerJSF<BaseEntity> implements Serializable {

    @Getter
    @Setter
    private String searchValue;

    @PostConstruct
    public void postConstruct() {
        searchValue = "Jaé";
    }

    @Override
    public void search() {
    }

    @Override
    public void setEditEntity() {
    }
}
