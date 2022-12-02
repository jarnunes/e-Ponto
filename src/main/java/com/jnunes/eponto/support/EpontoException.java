package com.jnunes.eponto.support;

import com.jnunes.core.commons.Utils;

public class EpontoException extends RuntimeException{

    public EpontoException(String messageKey){
        super(Utils.getMessage(messageKey));
    }
}
