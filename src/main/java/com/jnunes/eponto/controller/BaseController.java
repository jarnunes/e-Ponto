package com.jnunes.eponto.controller;

import com.jnunes.core.commons.Msg;
import com.jnunes.core.commons.Validate;
import com.jnunes.springjsf.support.utils.JSFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseController extends Validate implements Serializable {
    @Autowired
    @Lazy
    protected Msg msg;
    final LinkedHashMap<String, Object> viewParams = new LinkedHashMap();
    final Map<String, Object> params = new LinkedHashMap();

    public BaseController() {
    }

    public void setViewParams(LinkedHashMap<String, Object> params) {
        this.viewParams.putAll(params);
        this.params.putAll(params);
    }

    public void setViewParam(final String key, final Object value) {
        LinkedHashMap<String, Object> params = new LinkedHashMap();
        params.put(key, value);
        this.setViewParams(params);
    }

    protected void addErrorMessage(final String messageKey, final Object... arguments) {
        JSFUtils.addErrorMessage(messageKey, arguments);
    }

    protected  void addWarningMessage(final String messageKey, final Object... arguments) {
        JSFUtils.addWarningMessage(messageKey, arguments);
    }

    protected  void addInfoMessage(final String messageKey, final Object... arguments) {
        JSFUtils.addInfoMessage(messageKey, arguments);
    }

    protected void addFatalMessage(final String messageKey, final Object... arguments) {
        JSFUtils.addFatalMessage(messageKey, arguments);
    }


}