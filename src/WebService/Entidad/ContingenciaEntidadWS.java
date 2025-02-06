/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService.Entidad;

import java.util.List;

/**
 *
 * @author usuario
 */
public class ContingenciaEntidadWS {
    private String iddocumento;
    private String tienda;
    private String serie;
    private int idfactura;
    private String Estado;
    private String Fecha;
    private String numerodeacceso;
    private String firmafel;
    private String firmafelanulacion; 
    private String codigoestablecimiento; 
    
    public String getIddocumento() {
        return iddocumento;
    }

    public void setIddocumento(String iddocumento) {
        this.iddocumento = iddocumento;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getIdfactura() {
        return idfactura;
    }

    public void setIdfactura(int idfactura) {
        this.idfactura = idfactura;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getNumerodeacceso() {
        return numerodeacceso;
    }

    public void setNumerodeacceso(String numerodeacceso) {
        this.numerodeacceso = numerodeacceso;
    }

    public String getFirmafel() {
        return firmafel;
    }

    public void setFirmafel(String firmafel) {
        this.firmafel = firmafel;
    }

    public String getFirmafelanulacion() {
        return firmafelanulacion;
    }

    public void setFirmafelanulacion(String firmafelanulacion) {
        this.firmafelanulacion = firmafelanulacion;
    }

    public String getCodigoestablecimiento() {
        return codigoestablecimiento;
    }

    public void setCodigoestablecimiento(String codigoestablecimiento) {
        this.codigoestablecimiento = codigoestablecimiento;
    }
    
}
