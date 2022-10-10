package org.imprentas.sys.servlets;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.dao.ReporteContableHome;
import org.imprentas.sys.util.DbUtil;
import org.imprentas.sys.util.JPAUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceGeneralServlet extends BaseJasperServlet {

    private static final Log log = LogFactory.getLog(BalanceGeneralServlet.class);

    public DynamicReport buildReport() throws ClassNotFoundException {
        FastReportBuilder fastReportBuilder = new FastReportBuilder();

        fastReportBuilder.addColumn("Código", "ic_code", String.class.getName(), 200)
                .addColumn("Nombre", "ic_nombres", String.class.getName(), 600)
                .addColumn("Saldo", "total", String.class.getName(), 200)
                .setTitle("BALANCE GENERAL").setSubtitle("Generado el " + new Date())
                .setPrintBackgroundOnOddRows(true).setUseFullPageWidth(true);

        fastReportBuilder.setWhenNoData("No hay datos", null);


        return fastReportBuilder.build();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.clearConn();

        try {
            em = JPAUtil.getEntityManagerFactoryComp().createEntityManager();

            DynamicReport report = buildReport();

            String esquema = request.getParameter("sqm");
            String hasta = request.getParameter("hasta");
            String seccion = request.getParameter("seccion");
            String resultadoEjercicio = request.getParameter("resejer");

            Map parametros = new HashMap();
            //conexion = DbUtil.getDbConecction();
            ReporteContableHome reporteContableHome = new ReporteContableHome(em);
            List<Map<String, String>> data = reporteContableHome.build(esquema, hasta, seccion, Double.valueOf(resultadoEjercicio));

            JRDataSource ds = new JRBeanCollectionDataSource(data);

            JasperPrint jasperPrint = DynamicJasperHelper.generateJasperPrint(report, new ClassicLayoutManager(), ds);

            // Create the output byte stream where the data will be written
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // 5. Export to XLS format

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);


            // Retrieve the exported report in XLS format
            exporter.exportReport();

            // 6. Set the response properties
            String fechaActual = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String fileName = "BalanceGeneral" + fechaActual + ".pdf";
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());


            // Retrieve the output stream
            this.sos = response.getOutputStream();
            // Write to the output stream
            baos.writeTo(this.sos);
            // Flush the stream
            sos.flush();


        } catch (Throwable ex) {
            log.error(String.format("error al generar el reporte balance general: %s", ex.getMessage()));
            System.out.println(String.format("error al generar reporte balance general: %s", ex.getMessage()));
            ex.printStackTrace();
        } finally {
            this.closeConn();
        }

    }
}
