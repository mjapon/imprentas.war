package org.imprentas.sys.servlets;

import net.sf.jasperreports.engine.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.dao.TParamsHome;
import org.imprentas.sys.util.DbUtil;
import org.imprentas.sys.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/HistoriaClinicaServlet")
public class HistoriaClinicaServlet extends HttpServlet {

    private static final Log log = LogFactory.getLog(HistoriaClinicaServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String tkid = request.getParameter("ch");

            Map parametros = new HashMap();
            parametros.put("codhist", Integer.valueOf(tkid));

            String esquema = request.getParameter("sqm");
            parametros.put("esquema", esquema);

            EntityManager em = JPAUtil.getEntityManagerFactoryComp().createEntityManager();
            TParamsHome paramshome = new TParamsHome(em);

            String rutaArchivo = paramshome.getParamValue(esquema, "pathReporteHC");

            // Compila o template
            JasperReport jasperReport = JasperCompileManager.compileReport(rutaArchivo);

            Connection conexion = DbUtil.getDbConecction();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);


            ServletOutputStream sos = response.getOutputStream();
            String filename = "inline; filename=HistoriaClinica" + tkid + ".pdf";

            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", filename);
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);

            JasperExportManager.exportReportToPdfStream(jasperPrint, sos);

            sos.flush();
            sos.close();
            conexion.close();
            em.close();

        } catch (Throwable ex) {
            log.error(String.format("error al generar la historia clínica: %s", ex.getMessage()));
            System.out.println(String.format("error al generar la historia clinica: %s", ex.getMessage()));
            ex.printStackTrace();
        }
    }


}
