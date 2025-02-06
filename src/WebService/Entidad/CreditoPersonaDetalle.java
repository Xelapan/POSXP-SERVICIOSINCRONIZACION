/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService.Entidad;

import java.util.List;

/**
 *
 * @author pos
 */
public class CreditoPersonaDetalle {
          String IdArticulo;
          double Cantidad;
          double Precio;

    public CreditoPersonaDetalle() {
    }

    public CreditoPersonaDetalle(String _IdArticulo, double _cantidad, double _Monto) {        
        IdArticulo = _IdArticulo; 
        Cantidad = _cantidad;
        Precio = _Monto;
        
    }
}
