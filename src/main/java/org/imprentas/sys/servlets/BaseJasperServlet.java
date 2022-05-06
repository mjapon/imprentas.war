package org.imprentas.sys.servlets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;

public class BaseJasperServlet extends HttpServlet {
    private static final Log baseLog = LogFactory.getLog(BaseJasperServlet.class);

    protected ServletOutputStream sos = null;
    protected EntityManager em = null;
    protected Connection conexion = null;


    protected void clearConn() {
        this.sos = null;
        this.em = null;
        this.conexion = null;
    }

    protected void closeConn() {
        try {
            if (sos != null) {
                sos.close();
            }
            if (conexion != null) {
                conexion.close();
            }
            if (em != null) {
                em.close();
            }
        } catch (Throwable exx) {
            baseLog.error("Error en el cierre de la conexion", exx);
        }
    }
}
