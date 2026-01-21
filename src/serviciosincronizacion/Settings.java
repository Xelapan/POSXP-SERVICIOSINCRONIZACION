/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serviciosincronizacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author  
 */
public class Settings {

    public String _raiz;
    String codigoTienda,  IdTienda, idCaja, server, _userDB, password,  rutaBackup, IdCorte;
    int DiasSincronizar = 3;

  
    HashMap<String, String> Series = new HashMap<String, String>();

    public Settings() {
        int index = 0;
        String XML_CONFIG_FILE;
        SAXBuilder builder;
        File path = new File("");
        String _path;

//        String Path = path.getAbsolutePath();
        try {
            java.io.File currentDir = new java.io.File("");
            _path = currentDir.getAbsolutePath();
            _raiz = _path;
            // reading xml data 
            String[] WB_CONFIG = new String[100];  // determinando la cantidad de registros a almacenar        
//            XML_CONFIG_FILE = Path  + "/pos.xml";  // estableciendo path de lectura del archivo xml
            XML_CONFIG_FILE = _path + "/pos.xml";  // estableciendo path de lectura del archivo xml
            builder = new SAXBuilder();
           FileInputStream  fis = new FileInputStream(_path + "/pos.xml");
            Document doc = builder.build(fis); // creando documento
            org.jdom.Element root = doc.getRootElement();           // obteniendo registros del documento
            index = 0;
            Iterator itr = (root.getChildren()).iterator();
            while (itr.hasNext()) {
                org.jdom.Element property = (org.jdom.Element) itr.next();
                String x = property.getChild("key").getTextTrim();
                switch (x.toUpperCase()) {
                    case "URL":
                        server = property.getChild("value").getText();
                        break;
                    case "USUARIO":
                        _userDB = property.getChild("value").getText();
                        break;
                    case "PASS":
                        password = property.getChild("value").getText();
                        break;
                    case "TIENDA":
                        codigoTienda = property.getChild("value").getText();
                        break;
                    case "PATHBACKUP":
                        rutaBackup = property.getChild("value").getText();
                        break;
                        
                }
                index++;
            }
            
        } catch (JDOMException | NumberFormatException e) {
            System.out.println(e.toString());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void llenarSeries() {
        ResultSet rs = Main.conexion.EjecutarConsulta("select iddocumento, serie from pos_documento where activa = 1 and idtipodoc ='60241118-c405-11e2-a2ad-002719b373e4' "
                + " and tipoactivo = 'e8267104-77c2-11e3-8e78-782bcb2714b9' order by serie asc");
        try {
            while (rs.next()) {
                Series.put(rs.getString(1), rs.getString(2));
            }
        } catch(SQLException x){
        }finally {
            try{
            rs.close();
            }catch(SQLException x){}
            
        }
    } 
    
    public void llenarDiasSincronizar(){
        try{
        DiasSincronizar = Double.valueOf(Main.conexion.EjecutarEscalar("select valor from sys_configuracion where idconfiguracion ='f5f27293-7838-11e5-be93-94de80fadb93' ")).intValue();
        }catch(NumberFormatException x){
            System.out.print("Fallo en Consulta: "+x.toString());
            DiasSincronizar = 3;
        }
    
    }
    
      public int getDiasSincronizar() {
        return DiasSincronizar;
    }

    public String getCodigoTienda() {
        return codigoTienda;
    } 
   
    public String getIDcaja() {
        return idCaja;
    }

  
    public String getPassword() {
        return password;
    }
 
    public String getServer() {
        return server;
    }
 

    public String getUserDB() {
        return _userDB;
    }

    public String getRutaBackup() {
        return rutaBackup;
    } 

    public HashMap<String, String> getSeries() {
        return Series;
    } 

    public String getIdCorte() {
        return IdCorte;
    }

    public void setIdCorte(String IdCorte) {
        this.IdCorte = IdCorte;
    }
    
    

}
