package org.imprentas.sys.entity;


import java.util.Objects;

public class TReporteEntity {
    private int repId;
    private String repNombre;
    private String repJasper;
    private String repDetalle;
    private String repParams;
    private int repCat;

    public TReporteEntity() {

    }

    public TReporteEntity(int repId, String repNombre, String repJasper, String repDetalle, String repParams, int repCat) {
        this.repId = repId;
        this.repNombre = repNombre;
        this.repJasper = repJasper;
        this.repDetalle = repDetalle;
        this.repParams = repParams;
        this.repCat = repCat;
    }

    public int getRepId() {
        return repId;
    }

    public void setRepId(int repId) {
        this.repId = repId;
    }

    public String getRepNombre() {
        return repNombre;
    }

    public void setRepNombre(String repNombre) {
        this.repNombre = repNombre;
    }


    public String getRepJasper() {
        return repJasper;
    }

    public void setRepJasper(String repJasper) {
        this.repJasper = repJasper;
    }


    public String getRepDetalle() {
        return repDetalle;
    }

    public void setRepDetalle(String repDetalle) {
        this.repDetalle = repDetalle;
    }


    public String getRepParams() {
        return repParams;
    }

    public void setRepParams(String repParams) {
        this.repParams = repParams;
    }


    public int getRepCat() {
        return repCat;
    }

    public void setRepCat(int repCat) {
        this.repCat = repCat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TReporteEntity that = (TReporteEntity) o;
        return repId == that.repId && repCat == that.repCat && Objects.equals(repNombre, that.repNombre) && Objects.equals(repJasper, that.repJasper) && Objects.equals(repDetalle, that.repDetalle) && Objects.equals(repParams, that.repParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repId, repNombre, repJasper, repDetalle, repParams, repCat);
    }
}

