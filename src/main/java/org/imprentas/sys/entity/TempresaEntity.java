package org.imprentas.sys.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "tempresa", schema = "public", catalog = "imprentadb")
public class TempresaEntity {
    private int empId;
    private String empRuc;
    private String empRazonsocial;
    private String empNombrecomercial;
    private String empNroautorizacion;
    private Date empFechaautorizacion;

    @Id
    @Column(name = "emp_id")
    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    @Basic
    @Column(name = "emp_ruc")
    public String getEmpRuc() {
        return empRuc;
    }

    public void setEmpRuc(String empRuc) {
        this.empRuc = empRuc;
    }

    @Basic
    @Column(name = "emp_razonsocial")
    public String getEmpRazonsocial() {
        return empRazonsocial;
    }

    public void setEmpRazonsocial(String empRazonsocial) {
        this.empRazonsocial = empRazonsocial;
    }

    @Basic
    @Column(name = "emp_nombrecomercial")
    public String getEmpNombrecomercial() {
        return empNombrecomercial;
    }

    public void setEmpNombrecomercial(String empNombrecomercial) {
        this.empNombrecomercial = empNombrecomercial;
    }

    @Basic
    @Column(name = "emp_nroautorizacion")
    public String getEmpNroautorizacion() {
        return empNroautorizacion;
    }

    public void setEmpNroautorizacion(String empNroautorizacion) {
        this.empNroautorizacion = empNroautorizacion;
    }

    @Basic
    @Column(name = "emp_fechaautorizacion")
    public Date getEmpFechaautorizacion() {
        return empFechaautorizacion;
    }

    public void setEmpFechaautorizacion(Date empFechaautorizacion) {
        this.empFechaautorizacion = empFechaautorizacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TempresaEntity that = (TempresaEntity) o;

        if (empId != that.empId) return false;
        if (empRuc != null ? !empRuc.equals(that.empRuc) : that.empRuc != null) return false;
        if (empRazonsocial != null ? !empRazonsocial.equals(that.empRazonsocial) : that.empRazonsocial != null)
            return false;
        if (empNombrecomercial != null ? !empNombrecomercial.equals(that.empNombrecomercial) : that.empNombrecomercial != null)
            return false;
        if (empNroautorizacion != null ? !empNroautorizacion.equals(that.empNroautorizacion) : that.empNroautorizacion != null)
            return false;
        if (empFechaautorizacion != null ? !empFechaautorizacion.equals(that.empFechaautorizacion) : that.empFechaautorizacion != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = empId;
        result = 31 * result + (empRuc != null ? empRuc.hashCode() : 0);
        result = 31 * result + (empRazonsocial != null ? empRazonsocial.hashCode() : 0);
        result = 31 * result + (empNombrecomercial != null ? empNombrecomercial.hashCode() : 0);
        result = 31 * result + (empNroautorizacion != null ? empNroautorizacion.hashCode() : 0);
        result = 31 * result + (empFechaautorizacion != null ? empFechaautorizacion.hashCode() : 0);
        return result;
    }
}
