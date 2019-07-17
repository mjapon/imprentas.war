package org.imprentas.sys.servlets;

import net.sf.jasperreports.engine.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.dao.TJobDocHome;
import org.imprentas.sys.dao.TParamsHome;
import org.imprentas.sys.dao.TplantillaHome;
import org.imprentas.sys.entity.TplantillaEntity;
import org.imprentas.sys.util.DbUtil;
import org.imprentas.sys.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ReporteServlet")
public class ReporteServlet extends HttpServlet {

    private static final Log log = LogFactory.getLog(ReporteServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Inicia  doGet ReporteServlet--->");
        try {

            String desde = request.getParameter("desde");
            String hasta = request.getParameter("hasta");
            String codigoRep = request.getParameter("codrep");
            String tipocopia = request.getParameter("tipocopia");
            String jobid = request.getParameter("jobid");


            EntityManagerFactory entityManagerFactory = JPAUtil.getEntityManagerFactoryComp();

            EntityManager entityManager = entityManagerFactory.createEntityManager();

            TplantillaHome tplantillaHome = new TplantillaHome(entityManager);

            TplantillaEntity tplantillaEntity = tplantillaHome.findByCod(Integer.valueOf(codigoRep));

            InputStream inputStream = null;

            if (tplantillaEntity != null) {
                //System.out.println(String.format("Tplantillaentity value es:%s", tplantillaEntity.getTempJrxml()));

                String plantilla = tplantillaEntity.getTempJrxml();
                inputStream = new ByteArrayInputStream(plantilla.getBytes(Charset.forName("UTF-8")));
            }

            // Compila o template
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            //Configuarcion de parametros
            Map parametros = new HashMap();
            parametros.put("desde", Integer.valueOf(desde));
            parametros.put("hasta", Integer.valueOf(hasta));
            parametros.put("jobid", Integer.valueOf(jobid));
            parametros.put("tipo", Integer.valueOf(tipocopia));

            //Se crea conexion a la base de datos
            Connection conexion = DbUtil.getDbConecction();
            //Connection conexion = tplantillaHome.getConnection();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ServletOutputStream sos = response.getOutputStream();
            response.setContentType("application/pdf");

            JasperExportManager.exportReportToPdfStream(jasperPrint, sos);

            TParamsHome tParamsHome = new TParamsHome(entityManager);

            String pathSaveJob = tParamsHome.getPathSaveJobs();
            String filename = String.format("job_%s.pdf", jobid);

            String fullPathFile = String.format("%s%s%s", pathSaveJob, File.separator, filename);
            if (pathSaveJob.endsWith(File.separator)) {
                fullPathFile = String.format("%s%s", pathSaveJob, filename);
            }

            TJobDocHome jobDocHome = new TJobDocHome(entityManager);
            jobDocHome.saveOrUpdate(Integer.valueOf(jobid), fullPathFile);

            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            OutputStream out = new FileOutputStream(fullPathFile);
            out.write(pdfBytes);
            out.close();

            response.setHeader("Content-Sisposition", String.format("inline,filename=%s", filename));

            sos.flush();
            sos.close();
        } catch (Throwable ex) {
            log.error(String.format("error al generar el servlet del reporte: %s", ex.getMessage()));
            System.out.println(String.format("error al generar el servlet del reporte: %s", ex.getMessage()));
            ex.printStackTrace();
        }
    }
}