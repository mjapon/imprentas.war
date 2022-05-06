package org.imprentas.sys.servlets;

import net.sf.jasperreports.engine.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.util.DbUtil;

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

            Map parametros = new HashMap();
            parametros.put("ticketid", Integer.valueOf(tkid));

            String rutaArchivo = "/opt/reportes/ticket2.jrxml";

            // Compila o template
            JasperReport jasperReport = JasperCompileManager.compileReport(rutaArchivo);

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