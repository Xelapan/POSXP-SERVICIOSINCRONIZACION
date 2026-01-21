/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService.Entidad;

import java.util.Date;

/**
 *
 * @author Samuel
 */
public class DetalleVenta {
          int IdArticulo;
          String Serie;
          double cantidad;
          double Monto;
          int Hora;
          String Mesero;
          String Cajero;

    public DetalleVenta() {
    }

    public DetalleVenta( int _IdArticulo, String _Serie, double _cantidad, double _Monto, int _hora, String _mesero, String _cajero) {        
        IdArticulo = _IdArticulo;    
        Serie = _Serie;
        cantidad = _cantidad;
        Monto = _Monto;
        Hora = _hora;
        Mesero = _mesero;
        Cajero = _cajero;
    }
}
