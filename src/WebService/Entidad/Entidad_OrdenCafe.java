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
public class Entidad_OrdenCafe {
    private String tienda;
    private String serie;
    private int noorden;
    private int nofactura;
    private String fechafactura;
    private String fechaorden;
    private String fechacobro;
    private String fechacocina;
    private double cantidad;
    private int idarticulo;
    private String articulo;
    private Double precio;
    private String iddetalle;
    private String mesero;

    public String getMesero() {
        return mesero;
    }

    public void setMesero(String mesero) {
        this.mesero = mesero;
    } 
  

    public void setIddetalle(String iddetalle) {
        this.iddetalle = iddetalle;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public void setNoorden(int noorden) {
        this.noorden = noorden;
    }

    public void setNofactura(int nofactura) {
        this.nofactura = nofactura;
    }

    public void setFechafactura(String fechafactura) {
        this.fechafactura = fechafactura;
    }  

    public void setFechaorden(String fechaorden) {
        this.fechaorden = fechaorden;
    }

    public void setFechacobro(String fechacobro) {
        this.fechacobro = fechacobro;
    }

    public void setFechacocina(String fechacocina) {
        this.fechacocina = fechacocina;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public void setIdarticulo(int idarticulo) {
        this.idarticulo = idarticulo;
    }
    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
    
    
}
