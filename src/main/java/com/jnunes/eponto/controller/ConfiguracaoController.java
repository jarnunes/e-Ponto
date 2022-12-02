package com.jnunes.eponto.controller;

import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ConfiguracaoController implements Serializable {

    @Autowired
    private ConfiguracaoServiceImpl service;

    @Getter
    @Setter
    private Configuracao form;

    @PostConstruct
    public void postConstruct() {
        form = service.obterConfiguracao();
    }

    public void handleFileUpload(FileUploadEvent event) {
        form.setLogo(event.getFile().getContent());
        FacesMessage message = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void save() {
        service.save(form);
        FacesMessage message = new FacesMessage("Successful", "Configuração salva com sucesso");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
