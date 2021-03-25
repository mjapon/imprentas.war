package org.imprentas.sys.servlets;

import net.sf.jasperreports.engine.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.dao.TParamsHome;
import org.imprentas.sys.util.DbUtil;
import org.imprentas.sys.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/RecetaEmptyServlet")
public class RecetaEmptyServlet extends HttpServlet {
    private static final Log log = LogFactory.getLog(RecetaEmptyServlet.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            String esquema = request.getParameter("sqm");

            EntityManager em = JPAUtil.getEntityManagerFactoryComp().createEntityManager();
            TParamsHome paramshome = new TParamsHome(em);

            String pathReporte = paramshome.getParamValue(esquema, "rutaRecetaEmpty");
            String pathFondo = paramshome.getParamValue(esquema, "pathFondoRec");

            Map parametros = new HashMap();
            parametros.put("pathfondo", pathFondo);

            // Compila o template
            JasperReport jasperReport = JasperCompileManager.compileReport(pathReporte);

            Connection conexion = DbUtil.getDbConecction();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);

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
            conexion.close();
            em.close();

        } catch (Throwable ex) {
            log.error(String.format("error al generar la receta vacia: %s", ex.getMessage()));
            System.out.println(String.format("error al generar la receta vacia: %s", ex.getMessage()));
            ex.printStackTrace();
        }
    }
}
