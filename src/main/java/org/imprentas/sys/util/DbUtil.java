package org.imprentas.sys.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.servlets.ReportePathServlet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil implements Serializable {

    private static final long serialVersionUID = 2442287362354114263L;

    private static final Log log = LogFactory.getLog(DbUtil.class);


    public static Connection getDbConecction() {

        Connection conexion = null;

        try {

            InitialContext cxt = new InitialContext();
            if ( cxt == null ) {
                throw new Exception("Uh oh -- no context!");
            }

            DataSource ds = (DataSource) cxt.lookup( "java:/comp/env/jdbc/ImprentasDB" );

            if ( ds == null ) {
                throw new Exception("Data source not found!");
            }

            conexion = ds.getConnection();
            /*
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/imprentadb", "postgres", "root");
            System.out.println("Conexion creada con la base de datos--->");
            */
        } catch (Exception e) {
            log.error(String.format("Error al crear la conexion a la base de datos %s", e.getMessage()),e);
            e.printStackTrace();
        }

        return conexion;

    }

}
