package org.imprentas.sys.util;

public class FilaReporteContable {
    private String codigo;
    private String nombre;
    private String saldo;

    public FilaReporteContable(String codigo, String nombre, String saldo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.saldo = saldo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }
}
