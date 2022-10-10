package org.imprentas.sys.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imprentas.sys.util.FechasUtil;
import org.imprentas.sys.util.FilaReporteContable;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/BalanceGeneral")
public class ReporteContableHome {
    private static final Log log = LogFactory.getLog(ReporteContableHome.class);
    private EntityManager entityManager;

    public ReporteContableHome(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Map<String, String>> build(String esquema, String hasta, String secid, Double resultadoEjercicio) throws Exception {

        List<Map<String, String>> resultList = new ArrayList<>();

        try {
            String perContSql = String.format("select pc_id, pc_desde, pc_hasta, pc_fechacrea from " +
                            "%s.tperiodocontable where pc_activo = true",
                    String.valueOf(esquema));
            List<Object[]> result = this.entityManager.createNativeQuery(perContSql).getResultList();

            String fechaDesdeStr = "";
            if (result != null && !result.isEmpty()) {
                Object[] dataPeriodoContable = result.get(0);
                Date pcDesde = (Date) dataPeriodoContable[1];
                fechaDesdeStr = FechasUtil.formatCadenaDb(pcDesde);
            } else {
                throw new Exception("No existe periodo contable configurado, no puedo generar el balance");
            }

            java.util.Date hastaDate = FechasUtil.parseCadena(hasta);
            String fechaHastaStr = FechasUtil.formatCadenaDb(hastaDate);


            TParamsHome tParamsHome = new TParamsHome(this.entityManager);
            String ctaContab = tParamsHome.getParamValue(esquema, "cta_contab_resultado");

            StringBuilder sqlUnionCtaResult = new StringBuilder("");
            if (ctaContab != null && !ctaContab.isEmpty()) {
                sqlUnionCtaResult.append(
                        String.format("union all select ic.ic_id, %.2f total from %s.titemconfig ic where " +
                                "ic.ic_code = '%s'", resultadoEjercicio, esquema, ctaContab)
                );
            }

            String sqlCuentas = String.format("with data as ( " +
                    " select scd.cta_id, round(sum(scd.scd_saldo),2) as total " +
                    " from tsaldos_ctas_diario scd " +
                    " where date(scd_dia) between '%1$s' and '%2%s' " +
                    " group by scd.cta_id " +
                    " %3$s ) " +
                    " select ic.ic_id, ic_code, ic_nombre, ic_code||' '||ic_nombre as codenombre, ic_padre, ic_haschild, " +
                    " coalesce(data.total,0.0) as total " +
                    " from %4$s.titemconfig ic " +
                    " join %4$s.titemconfig_sec ics on ics.ic_id = ic.ic_id " +
                    " left join data on ic.ic_id = data.cta_id " +
                    " where ic.tipic_id = 3 and ic.ic_estado = 1 and ics.sec_id = %5$s and " +
                    " (ic_code like '1%' or ic_code like '2%' or ic_code like '3%') " +
                    " order by ic_code asc  ", fechaDesdeStr, fechaHastaStr, sqlUnionCtaResult.toString(), esquema, secid);

            List<Object[]> resultBalance = this.entityManager.createNativeQuery(sqlCuentas).getResultList();

            for (Object[] fila : resultBalance) {
                Map map = new HashMap<String, String>();
                map.put("ic_code", String.valueOf(fila[1]));
                map.put("ic_nombre", String.valueOf(fila[2]));
                map.put("total", String.valueOf(fila[6]));
                resultList.add(map);
            }

        } catch (RuntimeException re) {
            log.error("Error al obtener datos para el balance general", re);
            throw re;
        }

        return resultList;

    }

}
