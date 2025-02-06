/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import serviciosincronizacion.Main;

/**
 *
 * @author jorge
 */
public class Conexion {

    public Connection _conexion;
    Statement _st;
    ResultSet _rs;
    private static String _usuario, _pass;

    public Conexion() {
        asignarCifrado();
    }

    private void asignarCifrado() {
        _usuario = Seguridad.Cifrar(Main.settings.getUserDB(), false);
        _pass = Seguridad.Cifrar(Main.settings.getPassword(), false);
    }

    public Connection CrearConexion() {
        try {

            if (_conexion == null || _conexion.isClosed()) {
               
                _conexion = DriverManager.getConnection("jdbc:mysql://" + Main.settings.getServer(), _usuario, _pass);
                _conexion.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                _conexion.setAutoCommit(false);
            }
        } catch (Exception xx) {
            System.out.println(xx);
        }
        return _conexion;
    }

    public ResultSet EjecutarConsulta(String query) {
        _st = null;
        _rs = null;
        CrearConexion();
        try {
            _st = _conexion.createStatement();
            _rs = _st.executeQuery(query);
        } catch (SQLException ex) {
            

        }
        return _rs;
    }

    public String EjecutarEscalar(String query) {
        _st = null;
        _rs = null;
        CrearConexion();
        try {
            _st = _conexion.createStatement();
            _rs = _st.executeQuery(query);
            if (_rs.next()) {
                return _rs.getString(1);
            }
        } catch (SQLException ex) {

            

        } finally {
            CerrarConexion();
        }
        return "";
    }

    public String Ejecutar(String query, boolean cerrarConexion) {
        String resultado = "";
        try {
            CrearConexion();
            _conexion.setAutoCommit(false);
            Statement st = _conexion.createStatement();
            st.execute(query);
            _conexion.commit();
        } catch (SQLException ex) {
            resultado = ex.toString();

            try {
                _conexion.rollback();
            } catch (SQLException ex1) {

            }
        } finally {
            if (cerrarConexion) {
                CerrarConexion();
            }
        }
        return resultado;
    }

    public ResultSet EjecutarConsultaConcurrente(String query) {
        Statement st = null;
        ResultSet rs = null;
        CrearConexion();
        try {
            st = _conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
            rs = st.executeQuery(query);
        } catch (SQLException ex) {


        }
        return rs;
    }

    public void CerrarConexion() {
        try {
            if (_conexion != null || !_conexion.isClosed()) {
                _conexion.close();
                System.gc();
            }
        } catch (SQLException ex) {

        }

    }

    public String FechaServidor(boolean SoloFecha) {
        String fecha = "";
        if (SoloFecha) {
            fecha = this.EjecutarEscalar("select date_format(now(),'%d/%m/%Y')");
        } else {
            fecha = this.EjecutarEscalar("select date_format(now(),'%d/%m/%Y %H:%i:%s') ");
        }
        return fecha;
    }

    public String EjecutarEscalarConcurrente(String query) {
        _st = null;
        _rs = null;
        CrearConexion();
        try {
            _st = _conexion.createStatement();
            _rs = _st.executeQuery(query);
            if (_rs.next()) {
                return _rs.getString(1);
            }
        } catch (SQLException ex) {



        }

        return "";
    }

    //Metodo para ejecutar limpieza de BD
    public String EjecutarSinMensaje(String query, boolean cerrarConexion) {
        String resultado = "";
        try {
            _usuario = "root";
            _pass = "mysql25xE!";
            CrearConexion();
            _conexion.setAutoCommit(false);
            Statement st = _conexion.createStatement();
            st.execute(query);
            _conexion.commit();
        } catch (SQLException ex) {
            resultado = ex.toString();

            try {
                _conexion.rollback();

            } catch (SQLException ex1) {

            }
        } finally {
            if (cerrarConexion) {
                CerrarConexion();
            }
        }
        return resultado;
    }

    public void bitacora(int idaccion, String idusuario, String nota) {

        Main.conexion.Ejecutar("INSERT INTO gen_bitacora (idbitacora,idaccion,idusuario,nota) "
                + " VALUES ((select uuid()), " + idaccion + " ,'" + idusuario + "','" + nota + "');", true);
    }

}
