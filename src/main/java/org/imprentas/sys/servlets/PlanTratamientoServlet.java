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

@WebServlet("/PlanTratamientoServlet")
public class PlanTratamientoServlet extends BaseJasperServlet {

    private static final Log log = LogFactory.getLog(HistoriaClinicaServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.clearConn();
        try {
            this.em = JPAUtil.getEntityManagerFactoryComp().createEntityManager();
            TParamsHome paramshome = new TParamsHome(em);

            String tkid = request.getParameter("cod");
            String esquema = request.getParameter("sqm");

            String pathLogo = paramshome.getParamValue(esquema, "pathlogoplnod");

            Map parametros = new HashMap();
            parametros.put("codpt", Integer.valueOf(tkid));
            parametros.put("esquema", esquema);
            parametros.put("pathlogo", pathLogo);

            String rutaArchivo = paramshome.getParamValue(esquema, "pathPlanTrata");

            // Compila o template
            JasperReport jasperReport = JasperCompileManager.compileReport(rutaArchivo);

            this.conexion = DbUtil.getDbConecction();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);

            this.sos = response.getOutputStream();
            String filename = "inline; filename=PlanTratamiento" + tkid + ".pdf";

            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", filename);
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);

            JasperExportManager.exportReportToPdfStream(jasperPrint, sos);

            sos.flush();

        } catch (Throwable ex) {
            log.error(String.format("error al generar plan de tratamiento: %s", ex.getMessage()));
            System.out.println(String.format("error al generar plan tratamiento: %s", ex.getMessage()));
            ex.printStackTrace();
        } finally {
            this.closeConn();
        }
    }


}
