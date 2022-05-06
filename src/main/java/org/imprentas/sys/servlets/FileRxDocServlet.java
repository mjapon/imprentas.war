package org.imprentas.sys.servlets;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.dao.AdjuntosHome;
import org.imprentas.sys.util.FileUtils;
import org.imprentas.sys.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/GetAttach")
public class FileRxDocServlet extends HttpServlet {
    private static final Log log = LogFactory.getLog(FileRxDocServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = null;
        PrintWriter out = null;
        FileInputStream inputStream = null;
        Map<String, String> datosDocMap = null;

        try {
            String idfile = request.getParameter("cod");
            String esquema = request.getParameter("sqm");
            String tipo = request.getParameter("tipo");

            String strRuta = String.format("%s_ruta", tipo);
            String strExt = String.format("%s_ext", tipo);
            String strFilename = String.format("%s_filename", tipo);

            em = JPAUtil.getEntityManagerFactoryComp().createEntityManager();
            AdjuntosHome adjuntosHome = new AdjuntosHome(em);
            if ("rxd".equals(tipo)) {
                datosDocMap = adjuntosHome.getDatosDocRx(esquema, idfile);
            } else {
                datosDocMap = adjuntosHome.getDatosDocAttach(esquema, idfile);
            }

            out = response.getWriter();
            if (datosDocMap != null) {

                String rutaDbFile = datosDocMap.get(strRuta);
                String extDbFile = datosDocMap.get(strExt);
                String nameDbFile = datosDocMap.get(strFilename);

                FileUtils.ADD_FILE_RESPONSE_HEADERS(response, extDbFile, nameDbFile);
                inputStream = new FileInputStream(rutaDbFile);
                int in;
                while ((in = inputStream.read()) != -1) {
                    out.write(in);
                }
            } else {
                response.setContentType("text/html");
                out.println("<h1>No pude recuperar los datos del archivo</h1>");
            }
        } catch (Throwable ex) {
            log.error(String.format("error al ejecutar Getrxdoc: %s", ex.getMessage()));
            System.out.println(String.format("error al ejecutar servlet Getrxdoc: %s", ex.getMessage()));
            ex.printStackTrace();
        } finally {
            try {
                em.close();
            } catch (Throwable exx) {
                log.error("Error en el cierre de la conexion", exx);
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Throwable exx) {
                log.error("Error en el cierre del outputstream", exx);
            }
        }
    }
}
