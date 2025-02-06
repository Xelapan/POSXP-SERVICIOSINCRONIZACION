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
    String idArticulo;
    double Cantidad;    
    private List<DetalleDetalleCorte> detalledetalle;
    private DetalleDetalleCorte detdet;
    public DetalleCorte(){
        detdet = new DetalleDetalleCorte();
    }

    public DetalleCorte(List<DetalleDetalleCorte> detalle) {
        this.detalledetalle = detalle;
    }   

    public DetalleCorte(String id, double can) {
        idArticulo = id;
        Cantidad = can;
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
}
