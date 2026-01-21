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
public class Entidad_Cliente {
    private String IdCliente;
    private String IdDocumento;
    private String Fecha;
    private double puntos;
    
    public Entidad_Cliente() {        
    } 
    
    public String getIdCliente() {
        return IdCliente;
    }
 
    public void setIdCliente(String value) {
        IdCliente = value;
    }
    
    public String getIdDocumento() {
        return IdDocumento;
    }
 
    public void setIdDocumento(String value) {
        IdDocumento = value;
    }
    
    
    public String getFecha() {
        return Fecha;
    }
 
    public void setFecha(String value) {
        Fecha = value;
    }
        
    public double getPuntos() {
        return puntos;
    }
 
    public void setPuntos(double value) {
        puntos = value;
    }
}
