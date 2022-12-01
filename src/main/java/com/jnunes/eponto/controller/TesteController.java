package com.jnunes.crudveiculos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @RequestMapping(path = "/hello")
    public String helloWorld() {
        return "Hello";
    }
}
