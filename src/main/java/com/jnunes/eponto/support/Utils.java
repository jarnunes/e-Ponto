package com.jnunes.eponto.support;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class Utils {

    public static void redirect(String to) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(to);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
