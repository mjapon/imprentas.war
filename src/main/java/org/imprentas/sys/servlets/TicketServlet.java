package org.imprentas.sys.servlets;

import net.sf.jasperreports.engine.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.dao.TParamsHome;
import org.imprentas.sys.util.DbUtil;
import org.imprentas.sys.util.JPAUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/TicketServlet")
public class TicketServlet extends BaseJasperServlet {

    private static final Log log = LogFactory.getLog(TicketServlet.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.clearConn();
        try {
            String tkid = request.getParameter("tkid");

            this.em = JPAUtil.getEntityManagerFactoryComp().createEntityManager();
            TParamsHome paramshome = new TParamsHome(em);
            String esquema = request.getParameter("sqm");

            String pathReporteTicket = "";
            if (esquema != null && !esquema.isEmpty()) {
                pathReporteTicket = paramshome.getParamValue(esquema, "pathReporteTicket");
            }

            //String rutaArchivo = "/opt/reportes/ticket2.jrxml";
            if (pathReporteTicket.isEmpty()) {
                pathReporteTicket = "/opt/reportes/ticket2.jrxml";
            }

            Map parametros = new HashMap();
            parametros.put("ticketid", Integer.valueOf(tkid));

            System.out.println("Valor para path reporte ticket: "+ pathReporteTicket);

            // Compila o template
            JasperReport jasperReport = JasperCompileManager.compileReport(pathReporteTicket);

            this.conexion = DbUtil.getDbConecction();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);

            this.sos = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, sos);

            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=ticket.pdf");

            sos.flush();

        } catch (Throwable ex) {
            log.error(String.format("error al generar el ticket: %s", ex.getMessage()));
            System.out.println(String.format("error al generar el ticket: %s", ex.getMessage()));
            ex.printStackTrace();
        } finally {
            this.closeConn();
        }
    }


}