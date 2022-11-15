package com.jnunes.crudveiculos.controller;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class VeiculoController implements Serializable {


    @Getter
    @Setter
    private String searchValue;

    @PostConstruct
    public void postConstruct() {
        searchValue = "Jaé";
    }


}
