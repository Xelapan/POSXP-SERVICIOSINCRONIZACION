/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService.Entidad;

import java.util.List;

/**
 *
 * @author samuel
 */
public class DetalleCorte {
    private String idArticulo;
    private double Cantidad;    
    private int Encabezado = 0;
    private double Precio;  
    private List<DetalleDetalleCorte> detalledetalle;
    private DetalleDetalleCorte detdet;
    public DetalleCorte(){
        detdet = new DetalleDetalleCorte();
    }

    public DetalleCorte(List<DetalleDetalleCorte> detalle) {
        this.detalledetalle = detalle;
    }   

     public DetalleCorte(String id, double can, int encabezado, double precio) {
        idArticulo = id;
        Cantidad = can;
        Encabezado = encabezado;
        Precio = precio;
    }
    
     public List<DetalleDetalleCorte> getDetalle() {
        return detalledetalle;
    }

    public void setDetalleDetalle(List<DetalleDetalleCorte> value) {
        detalledetalle = value;
    }

    public void agregar(String id, double can) {
        detalledetalle.add(new DetalleDetalleCorte(id, can));
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double Cantidad) {
        this.Cantidad = Cantidad;
    }

    public int getEncabezado() {
        return Encabezado;
    }

    public void setEncabezado(int Encabezado) {
        this.Encabezado = Encabezado;
    }
    
       public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double Precio) {
        this.Precio = Precio;
    }
    
}
