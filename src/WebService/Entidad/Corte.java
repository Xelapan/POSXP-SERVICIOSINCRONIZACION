/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService.Entidad;

import java.util.List;

/**
 *
 * @author Programador
 */
public class Corte {
    private String idCorte;
    private int del;
    private int al;
    private String idDocumento;

    private List<Corte> cortes;
    private Corte cortesdetalle;

    public Corte() {
        cortesdetalle = new Corte();
    }

    public Corte(String iddoc, String id, int _del, int _al) {
        idDocumento = iddoc;
        idCorte = id;
        del = _del;
        al = _al;
    }

    public String getIdCorte() {
        return idCorte;
    }

    public void setIdCorte(String idCorte) {
        this.idCorte = idCorte;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    public int getAl() {
        return al;
    }

    public void setAl(int al) {
        this.al = al;
    }

    public List<Corte> getCortes() {
        return cortes;
    }

    public void setCortes(List<Corte> cortes) {
        this.cortes = cortes;
    }

    public Corte getCortesdetalle() {
        return cortesdetalle;
    }

    public void setCortesdetalle(Corte cortesdetalle) {
        this.cortesdetalle = cortesdetalle;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }
    
    
    
}
