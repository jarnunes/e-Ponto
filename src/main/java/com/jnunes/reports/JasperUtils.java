package com.jnunes.reports;

import com.jnunes.eponto.support.EpontoException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JasperUtils {

    private static final String REPORTS_PATH = "reports/";
    private static final String REPORTS_TYPE = ".jrxml";

    protected static JRBeanCollectionDataSource dataSource =
            new JRBeanCollectionDataSource(Collections.singletonList(new Object()));

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

    public static StreamedContent getStreamedContent(Map<String, Object> params, String templateName,
                                                     String fileName) {
        JasperPrint jasperPrint = getJasperPrint(params, templateName);
        try {
            InputStream inputStream = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jasperPrint));
            StreamedContent file = DefaultStreamedContent.builder()
                    .contentType(MediaType.APPLICATION_PDF_VALUE)
                    .name(fileName + ".pdf")
                    .stream(() -> inputStream)
                    .build();
            return file;
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public static void salvarArquivoEmDiretorio(Map<String, Object> params, String templateName,
        String fileName, String path) {
        JasperPrint jasperPrint = getJasperPrint(params, templateName);
        try {
            File file = new File(path);
            JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath() +"\\" + fileName + ".pdf");
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public static JasperPrint getJasperPrint(Map<String, Object> params, String templateName) {
        try {
            JasperReport jasperReport = getJasperReport(templateName);
            return JasperFillManager.fillReport(jasperReport, params, dataSource);
        } catch (FileNotFoundException | JRException e) {
            throw new EpontoException(e);
        }
    }

    protected static JasperReport getJasperReport(final String templateName) throws FileNotFoundException, JRException {
        File template = ResourceUtils.getFile("classpath:" + REPORTS_PATH + templateName + REPORTS_TYPE);
        return JasperCompileManager.compileReport(template.getAbsolutePath());
    }

    protected static JRDataSource getDatasource() {
        return getCollectionDataSource(Collections.singletonList(new Object()));
    }

    protected static JRBeanCollectionDataSource getCollectionDataSource(List<Object> items) {
        return new JRBeanCollectionDataSource(items);
    }

}
