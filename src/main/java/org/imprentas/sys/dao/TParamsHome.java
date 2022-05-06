package org.imprentas.sys.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.entity.TReporteEntity;
import org.imprentas.sys.entity.TparamsEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    private String auxGetParamValue(String schema, String paramAbr, String section) {
        String whereSection = " and tprm_seccion = 0";
        if (section != null && !section.trim().isEmpty()) {
            whereSection = String.format(" and tprm_seccion = %s", section);
        }

        String sql = String.format("select tprm_val from %s.tparams where tprm_abrev='%s' %s", schema, paramAbr, whereSection);
        String result = "";

        List<String> res = this.entityManager.createNativeQuery(sql).getResultList();
        if (res != null && res.size() > 0) {
            result = res.get(0);
        }

        return result;

    }

    public String getParamValue(String esquema, String prmabrev) {
        return this.auxGetParamValue(esquema, prmabrev, null);
    }

    public String getParamValue(String esquema, String prmabrev, String section) {
        String result = this.auxGetParamValue(esquema, prmabrev, section);
        if (result == null || result.trim().isEmpty()) {
            result = this.auxGetParamValue(esquema, prmabrev, null);
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

    public Map<String, Object> getTransaccData(String schema, Integer trncod) {
        try {
            String sql = String.format("select tra_codigo, sec_codigo from %s.tasiento where trn_codigo = %d",
                    schema, trncod);
            Object[] result = (Object[]) entityManager.createNativeQuery(sql).getSingleResult();
            Map resultMap = new HashMap();
            if (result != null) {
                resultMap.put("tra_codigo", result[0]);
                resultMap.put("sec_codigo", result[1]);
            }
            return resultMap;

        } catch (RuntimeException ex) {
            log.error("Error al recuperar datos de transaccion", ex);
            throw ex;
        }
    }

    public TReporteEntity getDatosReporte(Integer repId, String esquema) {
        String queryStr = String.format("select rep_id, rep_nombre, rep_jasper, rep_detalle, rep_params, rep_cat from %s.treporte where rep_id = %s ",
                String.valueOf(esquema),
                String.valueOf(repId));
        Query query = entityManager.createNativeQuery(queryStr);
        Object[] res = (Object[]) query.getSingleResult();
        TReporteEntity reporteEntity = null;

        if (res != null) {
            Integer _repId = Integer.valueOf(String.valueOf(res[0]));
            String _repNombre = String.valueOf(res[1]);
            String _repJasper = String.valueOf(res[2]);
            String _repDetalles = String.valueOf(res[3]);
            String _repParams = String.valueOf(res[4]);
            Integer _repCat = Integer.valueOf(String.valueOf(res[5]));

            reporteEntity = new TReporteEntity(_repId, _repNombre, _repJasper, _repDetalles, _repParams, _repCat);
        }
        return reporteEntity;
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

    public Boolean isNotaVenta(Map<String, Object> transaccDataMap) {
        Integer tra_codigo = Integer.valueOf(String.valueOf(transaccDataMap.get("tra_codigo")));
        return tra_codigo.intValue() == 2;
    }

}
