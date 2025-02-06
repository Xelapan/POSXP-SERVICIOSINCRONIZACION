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
public class EntidadTiempoCertificacion {
    private int Anyo;
    private int Mes;
    private int Dia;
    private int IdFactura;
    private String Serie;
    private String HoraCobro;
    private String HoraFel;
    private int Segundos;
    private String Firma;
    private String NumeroAcceso;
    private int Estado;
    private int CantidadDetalle;  

    public int getAnyo() {
        return Anyo;
    }

    public void setAnyo(int Anyo) {
        this.Anyo = Anyo;
    }

    public int getMes() {
        return Mes;
    }

    public void setMes(int Mes) {
        this.Mes = Mes;
    }

    public int getDia() {
        return Dia;
    }

    public void setDia(int Dia) {
        this.Dia = Dia;
    }

    public int getIdFactura() {
        return IdFactura;
    }

    public void setIdFactura(int IdFactura) {
        this.IdFactura = IdFactura;
    }

    public String getSerie() {
        return Serie;
    }

    public void setSerie(String Serie) {
        this.Serie = Serie;
    }

    public String getHoraCobro() {
        return HoraCobro;
    }

    public void setHoraCobro(String HoraCobro) {
        this.HoraCobro = HoraCobro;
    }

    public String getHoraFel() {
        return HoraFel;
    }

    public void setHoraFel(String HoraFel) {
        this.HoraFel = HoraFel;
    }

    public int getSegundos() {
        return Segundos;
    }

    public void setSegundos(int Segundos) {
        this.Segundos = Segundos;
    }

    public String getFirma() {
        return Firma;
    }

    public void setFirma(String Firma) {
        this.Firma = Firma;
    }

    public String getNumeroAcceso() {
        return NumeroAcceso;
    }

    public void setNumeroAcceso(String NumeroAcceso) {
        this.NumeroAcceso = NumeroAcceso;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int Estado) {
        this.Estado = Estado;
    }

    public int getCantidadDetalle() {
        return CantidadDetalle;
    }

    public void setCantidadDetalle(int CantidadDetalle) {
        this.CantidadDetalle = CantidadDetalle;
    }
}
