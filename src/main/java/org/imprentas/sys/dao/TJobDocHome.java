package org.imprentas.sys.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.entity.TjobdocEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

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
            List<TjobdocEntity> res = query.getResultList();
            if (res != null && res.size() > 0) {
                return res.get(0);
            }
            return null;
        } catch (Throwable ex) {
            log.error("get failed", ex);
            //throw re;
            return null;
        }
    }

    public void saveOrUpdate(Integer jobId, String ruta) {

        entityManager.getTransaction().begin();
        try {

            TjobdocEntity tjobdocEntity = findByCodJob(jobId);
            if (tjobdocEntity != null) {
                tjobdocEntity.setTjdRuta(ruta);
                entityManager.persist(tjobdocEntity);
            } else {
                tjobdocEntity = new TjobdocEntity();
                tjobdocEntity.setTjdRuta(ruta);
                tjobdocEntity.setTjobId(jobId);
                tjobdocEntity.setTjdFechacrea(new Date());
                entityManager.persist(tjobdocEntity);
            }

            entityManager.getTransaction().commit();
        } catch (Throwable ex) {
            entityManager.getTransaction().rollback();

        }


    }
}
