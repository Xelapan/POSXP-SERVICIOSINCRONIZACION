/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Operaciones;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import serviciosincronizacion.Main;

/**
 *
 * @author samuel
 */
public class backupBD {

    String so;
    static String fechahora;

    public backupBD() {
        so = System.getProperty("os.name").substring(0, 3).toUpperCase();
    }

    public void bkbd() {

    }

    public void backup_mantenimiento() {

        fechahora = Main.conexion.EjecutarEscalar("select Concat((Date_format(curdate(),'%Y%m%d')),'_', (Time_format(curtime(),'%H%i%s')))");
        String cadena = Main.settings.getServer();
        int l = cadena.indexOf("/");
        String BDurl = cadena.substring(0, l);
        String BDname = cadena.substring(l + 1);
        try {
           if (so.equals("LIN")) {
                //Creando el fichero
                String usuario = System.getProperty("user.name");               

                //Haciendo el backup y comprimiendolo
                String rutagzp = Main.settings.getRutaBackup() + "/" + Main.settings.getCodigoTienda().toUpperCase() + "_" + fechahora;                
                
                String ruta = "mysqldump -uroot -pmysql25xE! -h " + BDurl + " " + BDname + " ";
                String unionRutas = ruta + " |  gzip >" + rutagzp + ".gz";
//                int p = new ProcessBuilder("bash", "-c", unionRutas).start().waitFor();

                String RutaDir = "cd " +  Main.settings.getRutaBackup() + "/";

                int p = new ProcessBuilder("bash", "-c", ruta + " > " + rutagzp + ".sql").start().waitFor();
                Thread.sleep(1000);
                String nombreBK = Main.settings.getCodigoTienda().toUpperCase() + "_" + fechahora;

                int pp = new ProcessBuilder("bash", "-c", RutaDir + " && " + "zip -rm -P BACsies09 " + nombreBK + ".zip " + nombreBK + ".sql").start().waitFor();
                Thread.sleep(1000);

            }

        } catch (IOException ex) {
            System.out.print(ex.toString());
        } catch (InterruptedException ex) {
            Logger.getLogger(backupBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
