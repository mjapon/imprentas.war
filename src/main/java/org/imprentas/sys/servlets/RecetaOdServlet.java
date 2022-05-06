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

@WebServlet("/RecetaOdServlet")
public class RecetaOdServlet extends BaseJasperServlet {

    private static final Log log = LogFactory.getLog(RecetaServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.clearConn();
        try {
            String recid = request.getParameter("rec");
            String esquema = request.getParameter("sqm");

            this.em = JPAUtil.getEntityManagerFactoryComp().createEntityManager();
            TParamsHome paramshome = new TParamsHome(em);

            String pathFondo = paramshome.getParamValue(esquema, "pathFondoRecOd");

            Map parametros = new HashMap();
            parametros.put("pcod_receta", Integer.valueOf(recid));
            parametros.put("pathfondo", pathFondo);
            parametros.put("esquema", esquema);

            String pathTemp = paramshome.getParamValue(esquema, "rutaRecetaOd");

            // Compila o template
            JasperReport jasperReport = JasperCompileManager.compileReport(pathTemp);

            this.conexion = DbUtil.getDbConecction();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);

            String filename = "inline; filename=receta" + recid + ".pdf";
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", filename);
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);

            this.sos = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, sos);

            sos.flush();

        } catch (Throwable ex) {
            log.error(String.format("error al generar la receta: %s", ex.getMessage()));
            System.out.println(String.format("error al generar la receta: %s", ex.getMessage()));
            ex.printStackTrace();
        } finally {
            this.closeConn();
        }
    }
}
