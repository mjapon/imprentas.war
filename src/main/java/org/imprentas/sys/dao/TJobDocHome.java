package org.imprentas.sys.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.entity.TjobdocEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.Date;

public class TJobDocHome {

    private static final Log log = LogFactory.getLog(TplantillaHome.class);
    private EntityManager entityManager;

    public TJobDocHome(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public TjobdocEntity findByCodJob(Integer codJobDoc) {
        try {
            String queryStr = String.format("from TjobdocEntity where tjobId = %d", codJobDoc);
            Query query = entityManager.createQuery(queryStr);
            Object res = query.getSingleResult();
            if (res != null) {
                return (TjobdocEntity) res;
            }
            return null;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            //throw re;
            return null;
        }
    }

    public void saveOrUpdate(Integer jobId , String ruta) {

        TjobdocEntity tjobdocEntity =  findByCodJob(jobId);
        if (tjobdocEntity != null) {
            tjobdocEntity.setTjdRuta(ruta);
            entityManager.persist(tjobdocEntity);
        }
        else{
            tjobdocEntity = new TjobdocEntity();
            tjobdocEntity.setTjdRuta(ruta);
            tjobdocEntity.setTjobId(jobId);
            tjobdocEntity.setTjdFechacrea(new Date());
            entityManager.persist(tjobdocEntity);
        }
    }
}
