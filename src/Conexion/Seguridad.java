/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import serviciosincronizacion.Main;

/**
 *
 * @author samuel
 */
public class Seguridad {

    Statement _st;
    ResultSet _rs;
    public Connection conexion;
    private static final String patronBusqueda = "9f0AwB7J8CpDuÑd5E6FQGlxnHMbcI3ñK4LeUNzO1ms2PtRvSVkWXqirTYaghZjoy!.";
    private static final String patronEncripta = "wxBU7nIGj9Flm8f0ñAH1bcK3hdi4WJ5ZLCpDeMvTQuVkXqraYE6gosyNzÑOP2RSt!.";

    public Seguridad() {

    }

    public static String Cifrar(String cadena, boolean encriptar) {
        String resultado = "";

        if (encriptar) {
            for (int pos = 0; pos < cadena.length(); pos++) {
                if (pos == 0) {
                    resultado = encriptarCaracter(cadena.substring(pos, pos + 1), cadena.length(), pos);
                } else {
                    resultado += encriptarCaracter(cadena.substring(pos, pos + 1), cadena.length(), pos);
                }
            }
        } else {
            resultado = desencriptaCadena(cadena);

        }
        System.gc();
        return resultado;
    }

    public static String encriptarCaracter(String caracter, int variable, int indice) {
        int ind;
        if (patronBusqueda.contains(caracter)) {
            ind = (patronBusqueda.indexOf(caracter) + variable + indice) % patronBusqueda.length();
            return patronEncripta.substring(ind, ind + 1);
        }
        return caracter;
    }

    public static String desencriptaCadena(String cadena) {
        String original = "";
        for (int pos = 0; pos < cadena.length(); pos++) {
            if (pos == 0) {
                original = desencriptaCaracter(cadena.substring(pos, pos + 1), cadena.length(), pos);
            } else {
                original += desencriptaCaracter(cadena.substring(pos, pos + 1), cadena.length(), pos);
            }
        }
        return original;
    }

    public static String desencriptaCaracter(String caracter, int variable, int indice) {
        int ind = 0;
        if (patronEncripta.contains(caracter)) {
            if ((patronEncripta.indexOf(caracter) - variable - indice) > 0) {
                ind = (patronEncripta.indexOf(caracter) - variable - indice) % patronEncripta.length();
            } else {
                ind = (patronBusqueda.length()) + ((patronEncripta.indexOf(caracter) - variable - indice) % patronEncripta.length());
            }
            ind = ind % patronEncripta.length();
            return patronBusqueda.substring(ind, ind + 1);
        } else {
            return caracter;
        }
    }

 ///////////////////////////////////////
   

    public String NuevoId() {
        String Query = String.format("select uuid() ");
        String nuevoID = Main.conexion.EjecutarEscalar(Query);
        return nuevoID;
    }

    public String ObtenerId(String sql) {
        String id = "";
        ResultSet rs = null;
        try {
            rs = Main.conexion.EjecutarConsulta(sql);
            while (rs.next()) {
                id = rs.getString(1);
            }
        } catch (Exception ex) {

        } finally {
            Main.conexion.CerrarConexion();
            System.gc();
        }
        return id;
    }
}
