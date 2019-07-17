package org.imprentas.sys.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.imprentas.sys.entity.TplantillaEntity;

import java.sql.Connection;

public class TplantillaHome {
	
private static final Log log = LogFactory.getLog(TplantillaHome.class);
	
	private EntityManager entityManager;
	
	public TplantillaHome(EntityManager entityManager){
		this.entityManager = entityManager;
	}
	
	public TplantillaEntity findByCod(Integer codPlantilla){
		try {
			String queryStr = String.format("from TplantillaEntity where tempId = %d", codPlantilla);
			Query query = entityManager.createQuery(queryStr);
			Object res =  query.getSingleResult();
			if (res != null){
				return (TplantillaEntity)res;
			}
			return null;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Connection getConnection() {

		java.sql.Connection connection = entityManager.unwrap(java.sql.Connection.class);
		return connection;

	}
}
