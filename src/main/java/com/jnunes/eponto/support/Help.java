package com.jnunes.eponto.support;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.LocalDate;
import java.util.Optional;

@ApplicationScope
@Component
public class Help {

    public Integer toDay(LocalDate localDate) {
        return Optional.ofNullable(localDate).map(LocalDate::getDayOfMonth).orElse(null);
    }
}
