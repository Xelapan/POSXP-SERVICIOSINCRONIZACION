/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService.Entidad;

import WebService.Entidad.DetalleCorte;
import java.util.List;

/**
 *
 * @author samuel
 */
public class Entidad_Corte {    
    private String IdResolucion;
    private String Fecha;
    private String IdCorte;
    private String TipoCorte;
    private int NoCorte;
    private double Total;
    private double Efectivo;
    private double Descuento;
    private double Anluado;
    private double Tarjeta;
    private double Vales;
    private double AnticipoRecibido;
    private double AnticipoLiquidado;
    private double AnticipoAnulado;
    private double AnticipoSaldo;
    private double TotalDescuentoVale;        
    private double AnticiposTC;
    private int CantidadVales;
    private int del;
    private int al;
    private int rfs;
    private double Faltante;
    private double RecargoTC;
    private String UsuarioAutoriza;
    private int EsPanaderia;
    
     private double ClienteEmitidoExcento;
    private double ClienteAnuladoExcento;
    private double ClienteEmitido;
    private double ClienteAnulado;
    
    private double BienesEmitidos;
    private double BienesAnulados;
    private double ServiciosEmitidos;
    private double ServiciosAnulados;

    public int getEsPanaderia() {
        return EsPanaderia;
    }

    public void setEsPanaderia(int EsTienda) {
        this.EsPanaderia = EsTienda;
    }

    
    public double getClienteEmitidoExcento() {
        return ClienteEmitidoExcento;
    }

    public void setClienteEmitidoExcento(double ClienteEmitidoExcento) {
        this.ClienteEmitidoExcento = ClienteEmitidoExcento;
    }

    public double getClienteAnuladoExcento() {
        return ClienteAnuladoExcento;
    }

    public void setClienteAnuladoExcento(double ClienteAnuladoExcento) {
        this.ClienteAnuladoExcento = ClienteAnuladoExcento;
    }

    public double getClienteEmitido() {
        return ClienteEmitido;
    }

    public void setClienteEmitido(double ClienteEmitido) {
        this.ClienteEmitido = ClienteEmitido;
    }

    public double getClienteAnulado() {
        return ClienteAnulado;
    }

    public void setClienteAnulado(double ClienteAnulado) {
        this.ClienteAnulado = ClienteAnulado;
    }

    public double getBienesEmitidos() {
        return BienesEmitidos;
    }

    public void setBienesEmitidos(double BienesEmitidos) {
        this.BienesEmitidos = BienesEmitidos;
    }

    public double getBienesAnulados() {
        return BienesAnulados;
    }

    public void setBienesAnulados(double BienesAnulados) {
        this.BienesAnulados = BienesAnulados;
    }

    public double getServiciosEmitidos() {
        return ServiciosEmitidos;
    }

    public void setServiciosEmitidos(double ServiciosEmitidos) {
        this.ServiciosEmitidos = ServiciosEmitidos;
    }

    public double getServiciosAnulados() {
        return ServiciosAnulados;
    }

    public void setServiciosAnulados(double ServiciosAnulados) {
        this.ServiciosAnulados = ServiciosAnulados;
    }
 
    
    
    public double getRecargoTC() {
        return RecargoTC;
    }

    public void setRecargoTC(double RecargoTC) {
        this.RecargoTC = RecargoTC;
    }   

    public double getFaltante() {
        return Faltante;
    }

    public void setFaltante(double Faltante) {
        this.Faltante = Faltante;
    }
    
    private List<DetalleCorte> detalle;  
    
   

    public Entidad_Corte() {
        
    }

    public List<DetalleCorte> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleCorte> value) {
        detalle = value;
    }

    public void agregar(String id, double can) {
        detalle.add(new DetalleCorte(id, can));
    }

    public String getIdResolucion() {
        return IdResolucion;
    }

    public void setIdResolucion(String IdResolucion) {
        this.IdResolucion = IdResolucion;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String value) {
        Fecha = value;
    }

    public String getIdCorte() {
        return IdCorte;
    }

    public void setIdCorte(String value) {
        IdCorte = value;
    }

    public int getNoCorte() {
        return NoCorte;
    }

    public void setNoCorte(int value) {
        NoCorte = value;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double value) {
        Total = value;
    }

    public double getEfectivo() {
        return Efectivo;
    }

    public void setEfectivo(double value) {
        Efectivo = value;
    }

    public double getDescuento() {
        return Descuento;
    }

    public void setDescuento(double value) {
        Descuento = value;
    }

    public double getAnulado() {
        return Anluado;
    }

    public void setAnulado(double value) {
        Anluado = value;
    }

    public double getTarjeta() {
        return Tarjeta;
    }

    public void setTarjeta(double value) {
        Tarjeta = value;
    }

    public double getVales() {
        return Vales;
    }

    public void setVales(double value) {
        Vales = value;
    }

    public double getAnticipoRecibido() {
        return AnticipoRecibido;
    }

    public void setAnticipoRecibido(double value) {
        AnticipoRecibido = value;
    }

    public double getAticipoLiquidado() {
        return AnticipoLiquidado;
    }

    public void setAnticipoLiquidado(double value) {
        AnticipoLiquidado = value;
    }

    public double getAnticipoAnulado() {
        return AnticipoAnulado;
    }

    public void setAnticipoAnulado(double value) {
        AnticipoAnulado = value;
    }

    public double getAnticipoSaldo() {
        return AnticipoSaldo;
    }

    public void setAnticipoSaldo(double value) {
        AnticipoSaldo = value;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int value) {
        del = value;
    }

    public int getAl() {
        return al;
    }

    public void setAl(int value) {
        al = value;
    }

    public int getRfs() {
        return rfs;
    }

    public void setRfs(int value) {
        rfs = value;
    }
    
    public String getTipoCorte() {
        return TipoCorte;
    }

    public void setTipoCorte(String value) {
        TipoCorte = value;
    }
    
    public double getTotalDescuentoVale() {
        return TotalDescuentoVale;
    }

    public void setTotalDescuentoVale(double TotalDescuentoVale) {
        this.TotalDescuentoVale = TotalDescuentoVale;
    }
    
    public double getAnticiposTC() {
        return AnticiposTC;
    }

    public void setAnticiposTC(double AnticiposTC) {
        this.AnticiposTC = AnticiposTC;
    }

    public int getCantidadVales() {
        return CantidadVales;
    }

    public void setCantidadVales(int CantidadVales) {
        this.CantidadVales = CantidadVales;
    }

    public String getUsuarioAutoriza() {
        return UsuarioAutoriza;
    }

    public void setUsuarioAutoriza(String UsuarioAutoriza) {
        this.UsuarioAutoriza = UsuarioAutoriza;
    }
    
    

}
