/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService.Entidad;

/**
 *
 * @author Samuel
 */
public class DetalleVentaCliente {

   
    String Serie;
    int cantidad;
    double Monto;
    int Hora;
     public DetalleVentaCliente() {
    }

    public DetalleVentaCliente(  String _Serie, int _cantidad, double _Monto, int _Hora) {
             
        Serie = _Serie;
        cantidad = _cantidad;
        Monto = _Monto;
        Hora = _Hora;
    }
}
