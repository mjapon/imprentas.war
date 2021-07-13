package org.imprentas.sys.servlets;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.dao.TParamsHome;
import org.imprentas.sys.entity.TReporteEntity;
import org.imprentas.sys.util.DbUtil;
import org.imprentas.sys.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ReporteServlet")
public class ReporteServlet extends HttpServlet {

    private static final Log log = LogFactory.getLog(ReporteServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            String esquema = request.getParameter("sqm");
            String codrep = request.getParameter("codrep");
            String pdesde = request.getParameter("pdesde");
            String phasta = request.getParameter("phasta");
            String psecid = request.getParameter("secid");
            String pusid = request.getParameter("usid");
            String prefid = request.getParameter("refid");
            String pfmt = request.getParameter("fmt");
            String labelParams = request.getParameter("labelparams");

            EntityManager em = JPAUtil.getEntityManagerFactoryComp().createEntityManager();
            TParamsHome paramshome = new TParamsHome(em);

            TReporteEntity reporte = paramshome.getDatosReporte(Integer.valueOf(codrep), esquema);

            String pathReporte = reporte.getRepJasper();

            Map parametros = new HashMap();
            parametros.put("pesquema", esquema);
            parametros.put("pdesde", pdesde);
            parametros.put("phasta", phasta);
            parametros.put("psecid", psecid);
            parametros.put("pusid", pusid);
            parametros.put("prefid", prefid);
            String pfechas = String.format("and asi.trn_fecreg between '%s' and '%s'", pdesde, phasta);
            parametros.put("pfechas", pfechas);
            parametros.put("labelparams", labelParams);

            // Compila o template
            JasperReport jasperReport = JasperCompileManager.compileReport(pathReporte);

            Connection conexion = DbUtil.getDbConecction();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);
            String contentType = "application/pdf;";
            String inline = "inline";
            String ext = "pdf";
            if ("2".equalsIgnoreCase(pfmt)) {
                contentType = "application/vnd.ms-excel;";
                ext = "xls";
                inline = "attachment";
            } else if ("3".equalsIgnoreCase(pfmt)) {
                contentType = "text/html;";
                ext = ".html";
            }

            String fechahora = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String filename = String.format("Reporte%s_%s.%s", reporte.getRepNombre(), fechahora, ext);
            String contentDisposition = String.format("%s; filename=%s", inline, filename);

            response.setContentType(String.format("%s name=%s", contentType, filename));
            response.setHeader("Content-disposition", contentDisposition);
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);


            if ("1".equalsIgnoreCase(pfmt)) {
                ServletOutputStream sos = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, sos);

                sos.flush();
                sos.close();

            } else if ("2".equalsIgnoreCase(pfmt)) {

                JRXlsExporter exporter = new JRXlsExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint)); //The JasperPrint, filled report
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream())); //Your ByteArrayOutputStream

                SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
                configuration.setOnePagePerSheet(true);
                configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
                configuration.setRemoveEmptySpaceBetweenColumns(Boolean.TRUE);
                configuration.setCellHidden(Boolean.FALSE);
                configuration.setDetectCellType(true);
                configuration.setDetectCellType(true);
                exporter.setConfiguration(configuration);

                exporter.exportReport();

            } else if ("3".equalsIgnoreCase(pfmt)) {
                HtmlExporter exporter = new HtmlExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                PrintWriter out = response.getWriter();
                SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(out);
                output.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
                exporter.setExporterOutput(output);
                exporter.exportReport();
            }

            conexion.close();
            em.close();

        } catch (Throwable ex) {
            log.error(String.format("error al generar reporte: %s", ex.getMessage()));
            System.out.println(String.format("error al generarrepñorte : %s", ex.getMessage()));
            ex.printStackTrace();
        }
    }
}
