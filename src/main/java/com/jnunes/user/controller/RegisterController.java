package com.jnunes.user.controller;

import com.jnunes.eponto.support.Utils;
import com.jnunes.springjsf.support.utils.JSFUtils;
import com.jnunes.user.model.Role;
import com.jnunes.user.model.User;
import com.jnunes.user.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;


@Named
@ViewScoped
public class RegisterController {

    @Autowired
    private UserService service;

    @Getter
    @Setter
    private User form;

    @PostConstruct
    public void init() {
        this.form = new User();
    }

    public void save() {
        service.registerNewUser(form);
        form = new User();
    }

}
