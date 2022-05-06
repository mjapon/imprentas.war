package org.imprentas.sys.servlets;

import net.sf.jasperreports.engine.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.dao.TParamsHome;
import org.imprentas.sys.util.DbUtil;
import org.imprentas.sys.util.JPAUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ReporteCreditoServlet")
public class ReporteCreditoServlet extends BaseJasperServlet {

    private static final Log log = LogFactory.getLog(ReporteCreditoServlet.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.clearConn();

        try {
            String codcred = request.getParameter("cred");
            String esquema = request.getParameter("sqm");


            em = JPAUtil.getEntityManagerFactoryComp().createEntityManager();
            TParamsHome paramshome = new TParamsHome(em);

            Map parametros = new HashMap();
            parametros.put("pesquema", esquema);
            parametros.put("pcredid", Integer.valueOf(codcred));

            String paramTemplate = "pathReportCred";
            String pathReporte = paramshome.getParamValue(esquema, paramTemplate);

            JasperReport jasperReport = JasperCompileManager.compileReport(pathReporte);

            conexion = DbUtil.getDbConecction();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);

            String filename = "ReporteCred_" + codcred + ".pdf";
            String contentType = "inline; filename=\"" + filename + "\"";

            response.setContentType("application/pdf; name=\"" + filename + "\"");
            response.setHeader("Content-disposition", contentType);
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);

            sos = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, sos);

            sos.flush();

        } catch (Throwable ex) {
            log.error(String.format("error al generar el servlet del reporte credito: %s", ex.getMessage()));
            System.out.println(String.format("error al generar el servlet del reporte credito: %s", ex.getMessage()));
            ex.printStackTrace();
        } finally {
            this.closeConn();
        }

    }
}
