/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Operaciones;

import java.awt.image.BufferedImage;

/**
 *
 * @author samuel
 */
public class DatosCliente {
    private String id;
    private String nit;
    private String nombre;
    private String direccion;
    private String correo;
    private boolean correcto;
    private String Mensaje;

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String Mensaje) {
        this.Mensaje = Mensaje;
    }

    
    public boolean isCorrecto() {
        return correcto;
    }

    public void setCorrecto(boolean correcto) {
        this.correcto = correcto;
    }
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getId() 
    {
        return id;
    }
    
    public final void setId(String _id) 
    {
        id = _id;
    }
    
    public String getNit() 
    {
        return nit;
    }
    
    public final void setNit(String _nit) 
    {
        nit = _nit;
    }
    
    public String getNombre() 
    {
        return nombre;
    }
    
    public final void setNombre(String _nombre) 
    {
        nombre = _nombre;
    }
    
    public String getDireccion() 
    {
        return direccion;
    }
    
    public final void setDireccion(String _direccion) 
    {
        direccion = _direccion;
    }
    
}
