package org.imprentas.sys.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "tjobdoc", schema = "public", catalog = "imprentadb")
public class TjobdocEntity {
    private int tjdId;
    private int tjobId;
    private String tjdRuta;
    private Date tjdFechacrea;
    private int tjdUsercrea;

    @Id
    @Column(name = "tjd_id")
    public int getTjdId() {
        return tjdId;
    }

    public void setTjdId(int tjdId) {
        this.tjdId = tjdId;
    }

    @Basic
    @Column(name = "tjob_id")
    public int getTjobId() {
        return tjobId;
    }

    public void setTjobId(int tjobId) {
        this.tjobId = tjobId;
    }

    @Basic
    @Column(name = "tjd_ruta")
    public String getTjdRuta() {
        return tjdRuta;
    }

    public void setTjdRuta(String tjdRuta) {
        this.tjdRuta = tjdRuta;
    }

    @Basic
    @Column(name = "tjd_fechacrea")
    public Date getTjdFechacrea() {
        return tjdFechacrea;
    }

    public void setTjdFechacrea(Date tjdFechacrea) {
        this.tjdFechacrea = tjdFechacrea;
    }

    @Basic
    @Column(name = "tjd_usercrea")
    public int getTjdUsercrea() {
        return tjdUsercrea;
    }

    public void setTjdUsercrea(int tjdUsercrea) {
        this.tjdUsercrea = tjdUsercrea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TjobdocEntity that = (TjobdocEntity) o;

        if (tjdId != that.tjdId) return false;
        if (tjobId != that.tjobId) return false;
        if (tjdUsercrea != that.tjdUsercrea) return false;
        if (tjdRuta != null ? !tjdRuta.equals(that.tjdRuta) : that.tjdRuta != null) return false;
        if (tjdFechacrea != null ? !tjdFechacrea.equals(that.tjdFechacrea) : that.tjdFechacrea != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tjdId;
        result = 31 * result + tjobId;
        result = 31 * result + (tjdRuta != null ? tjdRuta.hashCode() : 0);
        result = 31 * result + (tjdFechacrea != null ? tjdFechacrea.hashCode() : 0);
        result = 31 * result + tjdUsercrea;
        return result;
    }
}
