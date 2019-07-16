package org.imprentas.sys.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.servlets.ReportePathServlet;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil implements Serializable {

    private static final long serialVersionUID = 2442287362354114263L;

    private static final Log log = LogFactory.getLog(DbUtil.class);

    public static Connection getDbConecction() {

        Connection conexion = null;

        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/imprentadb", "postgres", "postgres");
            System.out.println("Conexion creada con la base de datos--->");
        } catch (Exception e) {
            log.error(String.format("Error al crear la conexion a la base de datos %s", e.getMessage()),e);
            e.printStackTrace();
        }

        return conexion;


    }
}
