package org.imprentas.sys.entity;

import javax.persistence.*;

@Entity
@Table(name = "tplantilla", schema = "public", catalog = "imprentadb")
public class TplantillaEntity {
    private int tempId;
    private String tempName;
    private String tempJrxml;

    @Id
    @Column(name = "temp_id")
    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    @Basic
    @Column(name = "temp_name")
    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    @Basic
    @Column(name = "temp_jrxml")
    public String getTempJrxml() {
        return tempJrxml;
    }

    public void setTempJrxml(String tempJrxml) {
        this.tempJrxml = tempJrxml;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TplantillaEntity that = (TplantillaEntity) o;

        if (tempId != that.tempId) return false;
        if (tempName != null ? !tempName.equals(that.tempName) : that.tempName != null) return false;
        if (tempJrxml != null ? !tempJrxml.equals(that.tempJrxml) : that.tempJrxml != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tempId;
        result = 31 * result + (tempName != null ? tempName.hashCode() : 0);
        result = 31 * result + (tempJrxml != null ? tempJrxml.hashCode() : 0);
        return result;
    }
}
