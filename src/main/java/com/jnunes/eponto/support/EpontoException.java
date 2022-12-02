package com.jnunes.eponto.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EpontoException extends RuntimeException{
    private static final Logger log = LoggerFactory.getLogger(EpontoException.class);
    public EpontoException(Exception message){
        super(message);
        log.error(String.valueOf(message));
    }
}
