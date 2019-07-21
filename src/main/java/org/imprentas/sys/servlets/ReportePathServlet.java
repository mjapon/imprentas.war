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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ReportePathServlet")
public class ReportePathServlet extends HttpServlet {


    private static final Log log = LogFactory.getLog(ReportePathServlet.class);

    private String removeComillas(String cadena) {
        if (cadena.startsWith("\"")) {
            cadena = cadena.substring(1);
        }
        if (cadena.endsWith("\"")) {
            cadena = cadena.substring(0,cadena.length()-1);
        }
        return cadena;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.info("Inicia  doGet ReporteServlet--->");

        try {
            String codigoRep = request.getParameter("codigorep");
            String emp_esquema = request.getParameter("emp_esquema");

            Map parametros = new HashMap();
            Enumeration<String> paramsNames = request.getParameterNames();
            while (paramsNames.hasMoreElements()) {
                String key = paramsNames.nextElement();
                parametros.put(key, request.getParameterValues(key)[0]);
            }

            RestClient restClient = new RestClient();

            InputStream inputStream = null;

            String plantilla =  restClient.getTempJrxml(emp_esquema, Integer.valueOf(codigoRep));
            plantilla = removeComillas(plantilla);
            inputStream = new FileInputStream(plantilla);

            // Compila o template
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

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
