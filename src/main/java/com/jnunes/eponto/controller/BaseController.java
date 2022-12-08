package com.jnunes.eponto.controller;

import com.jnunes.core.commons.Msg;
import com.jnunes.eponto.support.BaseValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseController extends BaseValidate implements Serializable {
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

}