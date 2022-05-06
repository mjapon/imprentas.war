package org.imprentas.sys.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;

public class AdjuntosHome {

    private static final Log log = LogFactory.getLog(AdjuntosHome.class);
    private EntityManager entityManager;

    public AdjuntosHome(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private Map<String, String> auxGetDatosAdjunto(String esquema, String cod, String preftable, String table) {

        Map<String, String> resultMap = null;
        try {
            String strRuta = String.format("%s_ruta", preftable);
            String strExt = String.format("%s_ext", preftable);
            String strFilename = String.format("%s_filename", preftable);
            String strId = String.format("%s_id", preftable);

            String queryString = String.format("select %s, %s, %s from %s.%s where %s=%s",
                    strRuta, strExt, strFilename, esquema.trim(), table, strId, cod.trim());
            Query query = entityManager.createNativeQuery(queryString);
            Object[] res = (Object[]) query.getSingleResult();
            if (res != null) {
                resultMap = new HashMap();
                resultMap.put(strRuta, String.valueOf(res[0]));
                resultMap.put(strExt, String.valueOf(res[1]));
                resultMap.put(strFilename, String.valueOf(res[2]));
            }

        } catch (RuntimeException re) {
            log.error("Error al obtener datos de adjunto ", re);
            throw re;
        }

        return resultMap;
    }

    public Map<String, String> getDatosDocRx(String esquema, String cod) {
        return this.auxGetDatosAdjunto(esquema, cod, "rxd", "todrxdocs");
    }

    public Map<String, String> getDatosDocAttach(String esquema, String cod) {
        return this.auxGetDatosAdjunto(esquema, cod, "adj", "tadjunto");
    }

}
