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


@WebServlet("/ComproAgua")
public class ComproAguaServlet extends BaseJasperServlet {

    private static final Log log = LogFactory.getLog(ComproAguaServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.clearConn();

        try {
            String trncod = request.getParameter("trn");
            String esquema = request.getParameter("sqm");

            String pexceso = request.getParameter("pexceso");
            String pvconsumo = request.getParameter("pvconsumo");
            String pvexceso = request.getParameter("pvexceso");
            String pvsubt = request.getParameter("pvsubt");
            String pvdesc = request.getParameter("pvdesc");
            String pvmulta = request.getParameter("pvmulta");
            String pvtotal = request.getParameter("pvtotal");
            String pfechamaxpago = request.getParameter("pfechamaxpago");

            this.em = JPAUtil.getEntityManagerFactoryComp().createEntityManager();
            TParamsHome paramshome = new TParamsHome(em);
            String pathFondo = paramshome.getParamValue(esquema, "pathFondoAgua");
            String paramTemplate = "pathReporteAgua";

            String pathReporte = paramshome.getParamValue(esquema, paramTemplate);

            Map parametros = new HashMap();
            parametros.put("ptrncod", Integer.valueOf(trncod));
            parametros.put("pesquema", esquema);
            parametros.put("pathfondo", pathFondo);
            parametros.put("pexceso", pexceso);
            parametros.put("pvconsumo", pvconsumo);
            parametros.put("pvexceso", pvexceso);
            parametros.put("pvsubt", pvsubt);
            parametros.put("pvdesc", pvdesc);
            parametros.put("pvmulta", pvmulta);
            parametros.put("pvtotal", pvtotal);
            parametros.put("pfechamaxpago", pfechamaxpago);

            // Compila o template
            JasperReport jasperReport = JasperCompileManager.compileReport(pathReporte);

            this.conexion = DbUtil.getDbConecction();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);

            String filename = "ComprobanteAgua_" + trncod + ".pdf";
            String contentType = "inline; filename=\"" + filename + "\"";

            response.setContentType("application/pdf; name=\"" + filename + "\"");
            response.setHeader("Content-disposition", contentType);
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);

            this.sos = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, sos);

            sos.flush();

        } catch (Throwable ex) {
            log.error(String.format("error al generar comprobante de agua: %s", ex.getMessage()));
            System.out.println(String.format("error al generar comprobante de agua: %s", ex.getMessage()));
            ex.printStackTrace();
        } finally {
            this.closeConn();
        }
    }
}
