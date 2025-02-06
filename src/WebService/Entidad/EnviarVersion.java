/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService.Entidad;

/**
 *
 * @author pos
 */
public class EnviarVersion {

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        this.Version = version;
    }

    public String getSerie() {
        return Serie;
    }

    public void setSerie(String Serie) {
        this.Serie = Serie;
    }
    String Version;
    String Serie;
    
}
