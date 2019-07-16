package org.imprentas.sys.servlets;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.dao.TJobDocHome;
import org.imprentas.sys.entity.TjobdocEntity;
import org.imprentas.sys.util.JPAUtil;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/DescargaReportServlet")
public class DescargaReportServlet extends HttpServlet {

    private static final Log log = LogFactory.getLog(DescargaReportServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String codJob = request.getParameter("codjob");

            EntityManagerFactory entityManagerFactory = JPAUtil.getEntityManagerFactoryComp();

            TJobDocHome jobDocHome = new TJobDocHome(entityManagerFactory.createEntityManager());

            TjobdocEntity tJobDoc = jobDocHome.findByCodJob(Integer.valueOf(codJob));
            String rutaFile =  tJobDoc.getTjdRuta();

            String filename =  rutaFile.substring(rutaFile.lastIndexOf(File.separator));
            String pdfFileName = filename;

            File pdfFile = new File(rutaFile);

            response.setContentType("application/pdf");
            //response.addHeader("Content-Disposition", "attachment; filename=" + pdfFileName);
            response.addHeader("Content-Disposition", "inline");
            response.setContentLength((int) pdfFile.length());

            FileInputStream fileInputStream = new FileInputStream(pdfFile);
            OutputStream responseOutputStream = response.getOutputStream();
            int bytes;
            while ((bytes = fileInputStream.read()) != -1) {
                responseOutputStream.write(bytes);
            }

            responseOutputStream.flush();
            responseOutputStream.close();

        } catch (Throwable ex) {
            log.error(String.format("error al generar el servlet del reporte: %s", ex.getMessage()));
            System.out.println(String.format("error al generar el servlet del reporte: %s", ex.getMessage()));
            ex.printStackTrace();
        }
    }
}
