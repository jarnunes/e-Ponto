package com.jnunes.reports;

import com.jnunes.core.commons.MediaType;
import com.jnunes.eponto.support.EpontoException;
import com.jnunes.springjsf.support.utils.PFUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.model.StreamedContent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JasperUtils {

    private static final String REPORTS_PATH = "reports/";
    private static final String REPORTS_TYPE = ".jrxml";

    protected static JRBeanCollectionDataSource dataSource =
            new JRBeanCollectionDataSource(Collections.singletonList(new Object()));

    public static StreamedContent getStreamedContent(Map<String, Object> params, String templateName,
                                                     String fileName) {
        JasperPrint jasperPrint = getJasperPrint(params, templateName);
        try {
            byte[] relatorio = JasperExportManager.exportReportToPdf(jasperPrint);
            return PFUtils.toStreamedContent(relatorio, com.jnunes.core.commons.MediaType.PDF, fileName);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getByteArray(Map<String, Object> params, String templateName) {
        JasperPrint jasperPrint = getJasperPrint(params, templateName);
        try {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public static void salvarArquivoEmDiretorio(Map<String, Object> params, String templateName,
                                                String fileName, String path) {
        JasperPrint jasperPrint = getJasperPrint(params, templateName);
        try {
            File file = new File(path);
            JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath() + "\\" + fileName + ".pdf");
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
