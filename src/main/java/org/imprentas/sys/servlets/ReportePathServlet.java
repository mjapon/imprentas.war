package org.imprentas.sys.servlets;


import net.sf.jasperreports.engine.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.dao.TplantillaHome;
import org.imprentas.sys.entity.TplantillaEntity;
import org.imprentas.sys.util.DbUtil;
import org.imprentas.sys.util.JPAUtil;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ReportePathServlet")
public class ReportePathServlet extends HttpServlet {


    private static final Log log = LogFactory.getLog(ReportePathServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.info("Inicia  doGet ReporteServlet--->");

        try {

            String pfechaGen = new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(new Date());
            String pGeneradoPor = request.getParameter("generadopor");
            String paramdesc = request.getParameter("paramdesc");
            String codigoRep = request.getParameter("codigorep");

            EntityManagerFactory entityManagerFactory = JPAUtil.getEntityManagerFactoryComp();

            TplantillaHome tplantillaHome = new TplantillaHome(entityManagerFactory.createEntityManager());

            TplantillaEntity tplantillaEntity = tplantillaHome.findByCod(Integer.valueOf(codigoRep));

            InputStream inputStream = null;

            if (tplantillaEntity != null) {
                System.out.println(String.format("Tplantillaentity value es:%s", tplantillaEntity.getTempJrxml()));

                String plantilla = tplantillaEntity.getTempJrxml();
                inputStream = new FileInputStream(tplantillaEntity.getTempJrxml());
            }

            // Compila o template
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            //Configuarcion de parametros
            Map parametros = new HashMap();
            parametros.put("pFechaGen", pfechaGen);
            parametros.put("pGeneradoPor", pGeneradoPor);
            parametros.put("pParamDesc", paramdesc);

            Connection conexion = DbUtil.getDbConecction();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);


            ServletOutputStream sos = response.getOutputStream();
            response.setContentType("application/pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, sos);

            response.setHeader("Content-Sisposition", "inline,filename=prueba");

            sos.flush();
            sos.close();

        } catch (Throwable ex) {
            log.error(String.format("error al generar el servlet del reporte: %s", ex.getMessage()));
            System.out.println(String.format("error al generar el servlet del reporte: %s", ex.getMessage()));
            ex.printStackTrace();
        }
    }
}
