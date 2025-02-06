/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService.Entidad;

/**
 *
 * @author usuario
 */
public class Entidad_Anticipos {
    private String tienda;
    private String serie;
    private int codigoanticipo;
    private String tipo;
    private String fecha;
    private String nofactura;
    private double monto;
    private String usuario;

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

    public int getCodigoanticipo() {
        return codigoanticipo;
    }

    public void setCodigoanticipo(int codigoanticipo) {
        this.codigoanticipo = codigoanticipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNofactura() {
        return nofactura;
    }

    public void setNofactura(String nofactura) {
        this.nofactura = nofactura;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
}
