package org.imprentas.sys.servlets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

        try {
            String codJob = request.getParameter("codjob");
            String emp_esquema = request.getParameter("emp_esquema");

            RestClient restClient = new RestClient();

            String rutaFile = restClient.getPathDocSaved(emp_esquema, codJob);

            rutaFile = removeComillas(rutaFile);

            /*
            String filename =  rutaFile.substring(rutaFile.lastIndexOf(File.separator));
            //String pdfFileName = filename;
             */

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
