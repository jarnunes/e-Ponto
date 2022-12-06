package com.jnunes.eponto.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnunes.reports.vo.RelatorioVO;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class AppUtils {

    /**
     * Usar metodo do core
     */
    @Deprecated
    public static InputStream toInputStream(byte[] content) {
        return Optional.ofNullable(content).map(ByteArrayInputStream::new).orElse(null);
    }

    /**
     * Usar metodo do core
     */
    @Deprecated
    public static Integer toDay(LocalDate localDate) {
        return Optional.ofNullable(localDate).map(LocalDate::getDayOfMonth).orElse(null);
    }

    /**
     * Usar metodo do core
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object relatorio) {
        return new ObjectMapper().convertValue(relatorio, Map.class);
    }
}
