package org.imprentas.sys.dao;

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


}
