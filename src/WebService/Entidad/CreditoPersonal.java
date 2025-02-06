/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService.Entidad;

import WebService.Entidad.CreditoPersonaDetalle;
import java.util.List;

/**
 *
 * @author Samuel
 */
public class CreditoPersonal {

    private String Codigo;
    private double Monto;
    private String Fecha;
    private String Serie;
    private String NoFactura;
    private List<CreditoPersonaDetalle> detalle;
    
    //Detalle del credito personal
    //DetalleProducto
    public List<CreditoPersonaDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<CreditoPersonaDetalle> value) {
        detalle = value;
    }

    public void agregar( String _IdArticulo, double _cantidad, double _Monto) {
        detalle.add(new CreditoPersonaDetalle( _IdArticulo,_cantidad,_Monto ));
    }     
    //

    public String getNoFactura() {
        return NoFactura;
    }

    public void setNoFactura(String NoFactura) {
        this.NoFactura = NoFactura;
    }    

    public int getTipo() {
        return Tipo;
    }

    public void setTipo(int Tipo) {
        this.Tipo = Tipo;
    }
    int Tipo;

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public void setMonto(double Monto) {
        this.Monto = Monto;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public void setSerie(String Serie) {
        this.Serie = Serie;
    }

    public String getCodigo() {
        return Codigo;
    }

    public double getMonto() {
        return Monto;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getSerie() {
        return Serie;
    }

}
