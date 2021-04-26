package org.imprentas.sys.dao;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.entity.TparamsEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class TParamsHome {

    private static final Log log = LogFactory.getLog(TParamsHome.class);
    private EntityManager entityManager;

    public TParamsHome(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public TparamsEntity findByAbr(String abr) {
        try {
            String queryStr = String.format("from TparamsEntity where tprmAbrev = '%s'", abr);
            Query query = entityManager.createQuery(queryStr);
            Object res = query.getSingleResult();
            if (res != null) {
                return (TparamsEntity) res;
            }
            return null;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public String getPathSaveJobs() {
        TparamsEntity tparamsEntity = findByAbr("pathSaveJobs");
        if (tparamsEntity != null) {
            return tparamsEntity.getTprmVal();
        }
        return null;
    }

    public String getParamValue(String esquema, String prmabrev) {
        String sql = "select tprm_val from " + esquema + ".tparams where tprm_abrev='" + prmabrev + "';";
        String result = "";

        List<String> res = this.entityManager.createNativeQuery(sql).getResultList();
        if (res != null && res.size() > 0) {
            result = res.get(0);
        }

        return result;
    }

    public Integer getTraCodigo(String esquema, Integer trncodigo) {
        try {
            Integer tracodigo = 0;
            String queryStr = String.format("select tra_codigo, trn_codigo from %s.tasiento where trn_codigo = %s ",
                    String.valueOf(esquema),
                    String.valueOf(trncodigo));
            Query query = entityManager.createNativeQuery(queryStr);
            Object[] res = (Object[]) query.getSingleResult();
            if (res != null) {
                tracodigo = Integer.valueOf(String.valueOf(res[0]));
            }
            return tracodigo;
        } catch (RuntimeException re) {
            log.error("Error al obtener datos de la factura", re);
            throw re;
        }
    }

    public Boolean isNotaVenta(String esquema, Integer trncodigo) {
        Boolean isNotaVenta = Boolean.FALSE;
        try {
            Integer tra_codigo = getTraCodigo(esquema, trncodigo);
            if (tra_codigo.intValue() == 2) {
                isNotaVenta = Boolean.TRUE;
            }
        } catch (Throwable ex) {
            log.error("Error al tratar de verificar el tipo de transaccion", ex);
        }
        return isNotaVenta;
    }


}
