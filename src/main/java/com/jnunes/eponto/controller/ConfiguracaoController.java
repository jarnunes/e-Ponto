package com.jnunes.eponto.controller;

import com.jnunes.eponto.model.Configuracao;
import com.jnunes.eponto.service.ConfiguracaoServiceImpl;
import com.jnunes.springjsf.controller.BaseCrudController;
import com.jnunes.springjsf.support.utils.JSFUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ConfiguracaoController extends BaseCrudController<Configuracao> {

    @Autowired
    private ConfiguracaoServiceImpl service;

    @PostConstruct
    public void init() {
        setEditForm(service.obterConfiguracao());
        editOnLoad();
    }

    public void handleLogoUpload(FileUploadEvent event) {
        getEditForm().setLogo(event.getFile().getContent());
        addUploadSuccessMessage(event.getFile());
    }

    public void handleAssinaturaUpload(FileUploadEvent event) {
        getEditForm().setAssinatura(event.getFile().getContent());
        addUploadSuccessMessage(event.getFile());
    }

    private void addUploadSuccessMessage(UploadedFile file){
        JSFUtils.addInfoMessage("upload.success", file.getFileName());
    }
}
