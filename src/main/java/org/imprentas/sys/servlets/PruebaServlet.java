package org.imprentas.sys.servlets;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.imprentas.sys.util.DbUtil;
import org.imprentas.sys.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/PruebaServlet")
public class PruebaServlet extends HttpServlet {

    public DynamicReport buildReport() throws ClassNotFoundException {
        FastReportBuilder fastReportBuilder = new FastReportBuilder();

        fastReportBuilder.addColumn("ID", "id", Long.class.getName(), 50)
                .addColumn("Nombre", "firstname", String.class.getName(), 200)
                .addColumn("Apellidos", "surname", String.class.getName(), 200)
                .addColumn("Fecha Incorporación", "startDate", String.class.getName(), 120)
                .addColumn("Salario", "salary", String.class.getName(), 120)
                .addColumn("Departamento", "department", String.class.getName(), 180)
                .setTitle("Mi primer Informe con DynamicJasper").setSubtitle("Generado el " + new Date())
                .setPrintBackgroundOnOddRows(true).setUseFullPageWidth(true);

        fastReportBuilder.setWhenNoData("No hay datos", null);

        return fastReportBuilder.build();
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

        try {
            EntityManager em = JPAUtil.getEntityManagerFactoryComp().createEntityManager();

            DynamicReport report = buildReport();

            Map parametros = new HashMap();
            // Compila o template
            Connection conexion = DbUtil.getDbConecction();

            JasperPrint jasperPrint = DynamicJasperHelper.generateJasperPrint(report, new ClassicLayoutManager(), conexion, parametros);

            /*
            JasperReport jr = DynamicJasperHelper.generateJasperReport(report, new ClassicLayoutManager(), null);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jr, parametros, conexion);
             */

            // Create the output byte stream where the data will be written
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // 5. Export to XLS format

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

            /*
            JRXlsExporter exporter = new JRXlsExporter();

            // Here we assign the parameters jp and baos to the exporter
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

            // Excel specific parameters
            // Check the Jasper (not DynamicJasper) docs for a description of these settings. Most are
            // self-documenting
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);

             */

            // Retrieve the exported report in XLS format
            exporter.exportReport();

            // 6. Set the response properties
            String fileName = "SalesReport.pdf";
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            // Make sure to set the correct content type
            response.setContentType("application/pdf");
            // Assign the byte stream's size
            response.setContentLength(baos.size());

            // 7. Write to response stream


            try {
                // Retrieve the output stream
                ServletOutputStream outputStream = response.getOutputStream();
                // Write to the output stream
                baos.writeTo(outputStream);
                // Flush the stream
                outputStream.flush();

            } catch (Exception e) {

            }



            /*

            //JasperPrint jasperPrint = JasperFillManager.fillReport(jr, parametros);

            String filename = "Receta_vacia.pdf";
            String contentType = "inline; filename=\"" + filename + "\"";

            response.setContentType("application/pdf; name=\"" + filename + "\"");
            response.setHeader("Content-disposition", contentType);
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);

            ServletOutputStream sos = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, sos);


            sos.flush();
            sos.close();
            */
            conexion.close();
            em.close();


        } catch (ClassNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
        }

/*
        // send HTML page to client
        out.println("<html>");
        out.println("<head><title>A Test Servlet</title></head>");
        out.println("<body>");
        out.println("<h1>Test</h1>");
        out.println("<p>Simple servlet for testing.</p>");
        out.println("</body></html>");

 */

    }
}
