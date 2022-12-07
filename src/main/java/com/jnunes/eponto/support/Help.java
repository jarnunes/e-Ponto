package com.jnunes.eponto.support;

import com.jnunes.core.commons.Utils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.LocalDate;

@ApplicationScope
@Component
public class Help {

    public Integer toDay(LocalDate localDate) {
        return Utils.toDay(localDate);
    }

}
