package org.imprentas.sys.servlets;

import net.sf.jasperreports.engine.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.dao.TParamsHome;
import org.imprentas.sys.entity.TReporteEntity;
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

@WebServlet("/ReporteServlet")
public class ReporteServlet extends HttpServlet {

    private static final Log log = LogFactory.getLog(ReporteServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            String esquema = request.getParameter("sqm");
            String codrep = request.getParameter("codrep");
            String pdesde = request.getParameter("pdesde");
            String phasta = request.getParameter("phasta");

            EntityManager em = JPAUtil.getEntityManagerFactoryComp().createEntityManager();
            TParamsHome paramshome = new TParamsHome(em);

            TReporteEntity reporte =  paramshome.getDatosReporte(Integer.valueOf(codrep), esquema);

            String pathReporte =reporte.getRepJasper();

            Map parametros = new HashMap();
            parametros.put("pesquema", esquema);
            parametros.put("pdesde", pdesde);
            parametros.put("phasta", phasta);
            String pfechas = String.format("and asi.trn_fecreg between '%s' and '%s'", pdesde, phasta);
            parametros.put("pfechas", pfechas);

            // Compila o template
            JasperReport jasperReport = JasperCompileManager.compileReport(pathReporte);

            Connection conexion = DbUtil.getDbConecction();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);

            String filename = String.format("Reporte%s.pdf",reporte.getRepNombre());
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
            log.error(String.format("error al generar reporte: %s", ex.getMessage()));
            System.out.println(String.format("error al generarrepñorte : %s", ex.getMessage()));
            ex.printStackTrace();
        }
    }

    /*
    private String removeComillas(String cadena) {
        if (cadena.startsWith("\"")) {
            cadena = cadena.substring(1);
        }
        if (cadena.endsWith("\"")) {
            cadena = cadena.substring(0, cadena.length() - 1);
        }
        return cadena;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            String desde = request.getParameter("desde");
            String hasta = request.getParameter("hasta");
            String codigoRep = request.getParameter("codrep");
            String tipocopia = request.getParameter("tipocopia");
            String jobid = request.getParameter("jobid");
            String emp_esquema = request.getParameter("emp_esquema");

            RestClient restClient = new RestClient();

            InputStream inputStream = null;

            String plantilla = restClient.getTempJrxml(emp_esquema, Integer.valueOf(codigoRep));

            plantilla = removeComillas(plantilla);

            inputStream = new FileInputStream(plantilla);

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            //Configuarcion de parametros
            Map parametros = new HashMap();
            parametros.put("desde", Integer.valueOf(desde));
            parametros.put("hasta", Integer.valueOf(hasta));
            parametros.put("jobid", Integer.valueOf(jobid));
            parametros.put("tipo", Integer.valueOf(tipocopia));
            parametros.put("emp_esquema", emp_esquema);

            //Se crea conexion a la base de datos
            Connection conexion = DbUtil.getDbConecction();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);

            ServletOutputStream sos = response.getOutputStream();
            response.setContentType("application/pdf");

            JasperExportManager.exportReportToPdfStream(jasperPrint, sos);

            String pathSaveJob = restClient.getPathSaveDoc(emp_esquema);

            pathSaveJob = removeComillas(pathSaveJob);


            String filename = String.format("job_%s.pdf", jobid);


            String fullPathFile = String.format("%s%s%s", pathSaveJob, File.separator, filename);
            if (pathSaveJob.endsWith(File.separator)) {
                fullPathFile = String.format("%s%s", pathSaveJob, filename);
            }

            restClient.saveOrUpdateDoc(emp_esquema, Integer.valueOf(jobid), filename);

            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            OutputStream out = new FileOutputStream(fullPathFile);
            out.write(pdfBytes);
            out.close();

            response.setHeader("Content-Sisposition", String.format("inline,filename=%s", filename));

            sos.flush();
            sos.close();
            conexion.close();
        } catch (Throwable ex) {
            log.error(String.format("error al generar el servlet del reporte: %s", ex.getMessage()));
            System.out.println(String.format("error al generar el servlet del reporte: %s", ex.getMessage()));
            ex.printStackTrace();
        }
    }
     */
}
