package com.jnunes.eponto.controller;

import com.jnunes.eponto.domain.Configuracao;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import com.jnunes.springjsf.controller.BaseController;
import com.jnunes.springjsf.support.utils.JSFUtils;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ConfiguracaoController extends BaseController<Configuracao> {

    @Autowired
    private ConfiguracaoServiceImpl service;

    @Getter
    @Setter
    private Configuracao form;

    @PostConstruct
    public void init() {
        setEditForm(service.obterConfiguracao());
        setSearchForm(new SearchForm());
        editOnLoad();
    }

    public void handleLogoUpload(FileUploadEvent event) {
        form.setLogo(event.getFile().getContent());
        addUploadSuccessMessage(event.getFile());
    }

    public void handleAssinaturaUpload(FileUploadEvent event) {
        form.setAssinatura(event.getFile().getContent());
        addUploadSuccessMessage(event.getFile());
    }

    private void addUploadSuccessMessage(UploadedFile file){
        JSFUtils.addInfoMessage("upload.success", file.getFileName());
    }
}
