package org.imprentas.sys.servlets;

import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
public class RestResponse {

    private Integer codigo;
    private String respuesta;

    public RestResponse(Integer codigo, String respuesta) {
        this.codigo = codigo;
        this.respuesta = respuesta;
    }

    public Integer getCodigo() {

        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
