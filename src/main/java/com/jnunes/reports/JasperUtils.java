package com.jnunes.reports;

import com.jnunes.eponto.support.EpontoException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Map;

public class JasperUtils {

    public static ResponseEntity<byte[]> getResponseEntity(Map<String, Object> params, String templateName,
        String fileName) {
        JasperPrint jasperPrint = getJasperPrint(params, templateName);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", fileName + ".pdf");
        try {
            return new ResponseEntity<byte[]>
                    (JasperExportManager.exportReportToPdf(jasperPrint), headers, HttpStatus.OK);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public static JasperPrint getJasperPrint(Map<String, Object> params, String templateName) {
        try {
            JasperReport jasperReport = getJasperReport(templateName);
            return JasperFillManager.fillReport(jasperReport, params, getDummyDatasource());
        } catch (FileNotFoundException | JRException e) {
            throw new EpontoException(e);
        }
    }

    private static JasperReport getJasperReport(final String templateName) throws FileNotFoundException, JRException {
        File template = ResourceUtils.getFile("classpath:" + templateName + ".jrxml");
        return JasperCompileManager.compileReport(template.getAbsolutePath());
    }

    private static JRDataSource getDummyDatasource() {
        return new JRBeanCollectionDataSource(Collections.singletonList(new Object()));
    }

}
