/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService.Entidad;

import WebService.Entidad.DetalleVentaCliente;
import WebService.Entidad.DetalleVenta;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Samuel
 */
public class EncabezadoVentas {   
    private int Dia;
    private int Mes;
    private int Ano;
    
    private List<DetalleVenta> detalleVentas;
    private List<DetalleVentaCliente> detalleventaCliente;
    
    //Detalle Clientes
     public List<DetalleVentaCliente> getDetalleCliente() {
        return detalleventaCliente;
    }
     public void setDetalleCliente(List<DetalleVentaCliente> value) {
        detalleventaCliente = value;
    }

    public void agregarCliente( int _IdArticulo, String _Serie, int _cantidad, double _Monto, int _Hora) {
        detalleventaCliente.add(new DetalleVentaCliente(  _Serie,_cantidad,_Monto,_Hora));
    }
    
    //DetalleProducto
    public List<DetalleVenta> getDetalle() {
        return detalleVentas;
    }

    public void setDetalle(List<DetalleVenta> value) {
        detalleVentas = value;
    }

    public void agregar( int _IdArticulo, String _Serie, double _cantidad, double _Monto, int _Hora, String _mesero, String _cajero) {
        detalleVentas.add(new DetalleVenta( _IdArticulo,_Serie,_cantidad,_Monto, _Hora, _mesero, _cajero));
    }
    //Fin Detalle Producto
    
    public int getDia() {
        return Dia;
    }

    public int getMes() {
        return Mes;
    }

    public int getAno() {
        return Ano;
    }

     public void setDia(int Dia) {
        this.Dia = Dia;
    }

    public void setMes(int Mes) {
        this.Mes = Mes;
    }

    public void setAno(int Ano) {
        this.Ano = Ano;
    }   

   
}
