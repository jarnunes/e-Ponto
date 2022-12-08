package com.jnunes.reports;

import com.jnunes.core.commons.MediaType;
import com.jnunes.eponto.support.EpontoException;
import com.jnunes.springjsf.support.utils.PFUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.StreamedContent;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.jnunes.reports.ReportConsts.*;

public class JasperUtils {

    protected static JRBeanCollectionDataSource dataSource =
            new JRBeanCollectionDataSource(Collections.singletonList(new Object()));

    public static StreamedContent getStreamedContent(Map<String, Object> params, String templateName,
        String fileName) {
        JasperPrint jasperPrint = getJasperPrint(params, templateName);
        try {
            byte[] relatorio = JasperExportManager.exportReportToPdf(jasperPrint);
            return PFUtils.toStreamedContent(relatorio, MediaType.PDF, fileName);
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
        String resorceLocation = StringUtils.join("classpath:", BASE_REPORTS_PATH, templateName,
            MediaType.JRXML.getDotFileType());
        return JasperCompileManager.compileReport(ResourceUtils.getFile(resorceLocation).getAbsolutePath());
    }

    protected static JRBeanCollectionDataSource getCollectionDataSource(List<Object> items) {
        return new JRBeanCollectionDataSource(items);
    }

}
