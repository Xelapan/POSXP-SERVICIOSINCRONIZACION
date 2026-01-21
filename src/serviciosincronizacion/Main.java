/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serviciosincronizacion;

import Conexion.Conexion;
import Operaciones.InicioSincronizacion;
import java.io.IOException;
import java.net.ServerSocket;


/**
 *
 * @author jorge
 */
public class Main {

    public static Settings settings;
    public static Conexion conexion;
    public static ServerSocket b;
    public static String version = "Sincronizacion. ws.06112025";
    private static String MensajeFel = "";
    private InicioSincronizacion inicio;

    public Main() {
        try {
            verAplicacionJava();
            settings = new Settings();//carga los parametros de conexion        
            conexion = new Conexion();//crea la conexion 
            settings.llenarSeries();
            settings.llenarDiasSincronizar();
            System.out.println(version);
            inicio = new InicioSincronizacion();
            inicio.inicio();
        } catch (Exception x) {
            System.err.println("Error " + x.toString());
        }

    }

    public static void verAplicacionJava() {
        try {
            b = new ServerSocket(1339);
            System.out.println("Es la primera instancia de la aplicación...");
        } catch (IOException x) {
            System.out.println("Otra instancia de la aplicación se está ejecutando...");
            
            System.exit(0);
        }
    }

}
