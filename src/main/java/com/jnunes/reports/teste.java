package com.jnunes.reports;

import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

public class teste {
    public static void main(String[] args) {
        mes();
    }

    private static void mes() {
        Locale locale = new Locale("pt", "BR");
//
//        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.FRENCH);
//        final String month = LocalDate.now().format(formatter);
        System.out.println(weekName(LocalDate.now()));
    }

    public static String getMonthName(LocalDate localDate){
        Locale locale = new Locale("pt", "BR");
        return Optional.ofNullable(localDate).map(LocalDate::getMonth)
                .map(Month::getValue).map(value -> Month.of(value).getDisplayName(TextStyle.NARROW, locale))
                .orElse(null);
    }

    public static String weekName(LocalDate localDate) {
        return Optional.ofNullable(localDate).map(LocalDate::getDayOfWeek)
                .map(day -> DayOfWeek.of(day.getValue()).getDisplayName(TextStyle.FULL, localePtBr()))
                .map(StringUtils::capitalize).orElse(null);
    }

    public static Locale localePtBr() {
        return new Locale("pt", "BR");
    }
}
