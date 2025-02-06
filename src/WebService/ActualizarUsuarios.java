/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import serviciosincronizacion.Main;

/**
 *
 * @author Samuel
 */
public class ActualizarUsuarios {

    String error = "";

    public void ActualizarUsuarios() {
        LlamadaWebService();
        UsuariosDeBaja();

    }

    private void LlamadaWebService() {
        try {
            Gson gson = new Gson();
            JsonParser parser;
            JsonArray jArray;
            String s = obtenerUsuarios();
            if (!s.equals("")) {
                parser = new JsonParser();
                int x = 0;
                try {

                    // <editor-fold defaultstate="collapsed" desc="Catalogo de municipio">
                    try {
                        jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("municipio");
                        String qry = "";

                        for (JsonElement obj : jArray) {
                            municipio cse = gson.fromJson(obj, municipio.class);
                            int existe = 0;
                            existe = Integer.valueOf(Main.conexion.EjecutarEscalar("SELECT count(*) FROM pos_municipio where idmunicipio = '" + cse.idmunicipio + "' "));

                            if (existe == 0) { //Inserta                 
                                qry = (" INSERT INTO pos_municipio (idmunicipio, iddepartamento, nombre, codigopostal) VALUES\n"
                                        + " ('" + cse.idmunicipio + "',\n"
                                        + " '" + cse.iddepartamento + "',\n"
                                        + " '" + cse.nombre + "',\n"
                                        + " '" + cse.codigopostal + "'); ");
                                Main.conexion.Ejecutar(qry, true);
                            } else //actualiza
                            {
                                qry
                                        = (" UPDATE pos_municipio SET\n"
                                        + " iddepartamento = '" + cse.iddepartamento + "' ,\n"
                                        + " nombre = '" + cse.nombre + "',\n"
                                        + " codigopostal = '" + cse.codigopostal + "' \n"
                                        + " WHERE idmunicipio = '" + cse.idmunicipio + "'; ");
                                Main.conexion.Ejecutar(qry, true);
                            }

                            //System.out.println(String.format("%1s,%2s,%3s,%4s", cse.idCategoriaArticulo, cse.idPadre, cse.nombre, cse.debaja, cse.partedemenu));
                        }
                    } catch (Exception e) {
                        error += e.toString();
                        System.out.println("Er municipio: " + error);
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Catalogo de Tienda">
                    try {
                        jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("tienda");
                        String qry = "";

                        for (JsonElement obj : jArray) {
                            tienda cse = gson.fromJson(obj, tienda.class);
                            int existe = 0;
                            if (cse.idtienda.equals("c7e27c91-c155-11e5-b673-94de80fadb93")) {
                                existe = 0;
                            }
                            existe = Integer.valueOf(Main.conexion.EjecutarEscalar("SELECT    count(*) FROM     pos_tienda where idtienda =  '" + cse.idtienda + "' "));

                            if (existe == 0) { //Inserta                 
                                qry = (" INSERT INTO  pos_tienda (idtienda,  idtipotienda, idmunicipio, codigo, nombre, direccion, telefono, debaja, empresa, nombrecomercial, nit,\n"
                                        + "regimen, codigoestablecimiento, nombreestablecimiento) VALUES (\n"
                                        + " '" + cse.idtienda + "',\n"
                                        + " '" + cse.idtipotienda + "',\n"
                                        + " '" + cse.idmunicipio + "',\n"
                                        + " '" + cse.codigo + "',\n"
                                        + " '" + cse.nombre + "',\n"
                                        + " '" + cse.direccion + "',\n"
                                        + " '" + cse.telefono + "',\n"
                                        + " " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + ",\n"
                                        + " '" + cse.empresa + "',\n"
                                        + " '" + cse.nombrecomercial + "',\n"
                                        + " '" + cse.nit + "',\n"
                                        + " '" + cse.regimen + "',\n"
                                        + " '" + cse.codigoestablecimiento + "',\n"
                                        + " '" + cse.nombreestablecimiento + "');");

                                Main.conexion.Ejecutar(qry, true);
                            } else //actualiza
                            {
                                qry = (" UPDATE pos_tienda SET\n"
                                        + " idtipotienda = '" + cse.idtipotienda + "',\n"
                                        + " idmunicipio = '" + cse.idmunicipio + "',\n"
                                        + " codigo = '" + cse.codigo + "',\n"
                                        + " nombre = '" + cse.nombre + "',\n"
                                        + " direccion = '" + cse.direccion + "',\n"
                                        + " telefono = '" + cse.telefono + "',\n"
                                        + " debaja = " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + ",\n"
                                        + " empresa = '" + cse.empresa + "',\n"
                                        + " nombrecomercial = '" + cse.nombrecomercial + "',\n"
                                        + " nit = '" + cse.nit + "',\n"
                                        + " regimen = '" + cse.regimen + "',\n"
                                        + " codigoestablecimiento = '" + cse.codigoestablecimiento + "',\n"
                                        + " nombreestablecimiento = '" + cse.nombreestablecimiento + "' \n"
                                        + " WHERE idtienda = '" + cse.idtienda + "' ");
                                Main.conexion.Ejecutar(qry, true);
                            }
                        }
                    } catch (Exception e) {
                        error += e.toString();
                        System.out.println("Er tineda: " + error);
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Catalogo de uSUARIOS">                   
                    try {
                        jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("usuarios");
                        String qry = "";

                        for (JsonElement obj : jArray) {
                            usuarios cse = gson.fromJson(obj, usuarios.class);
                            int existe = 0;
                            existe = Integer.valueOf(Main.conexion.EjecutarEscalar("select count(*) from pos_usuario where idusuario = '" + cse.idusuario + "' "));

                            if (existe == 0) { //Inserta     

                                qry = (" INSERT INTO  pos_usuario (idusuario, idtienda, nombre, nombreapellido, contrasena,  debaja) VALUES\n"
                                        + "( '" + cse.idusuario + "' ,   "
                                        + " '" + cse.idtienda + "', "
                                        + " '" + cse.nombre + "' , "
                                        + " '" + cse.nombreapellido + "', "
                                        + " 'qEMudW', "
                                        + " " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + " );");
                                Main.conexion.Ejecutar(qry, true);
                            } else //actualiza
                            {
                                qry
                                        = ("UPDATE pos_usuario SET\n"
                                        + " idtienda =  '" + cse.idtienda + "',\n"
                                        + " nombre = '" + cse.nombre + "',\n"
                                        + " nombreapellido = '" + cse.nombreapellido + "',\n"
                                        + " debaja = " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + " \n"
                                        + " WHERE idusuario = '" + cse.idusuario + "' ");
                                Main.conexion.Ejecutar(qry, true);
                            }
                        }
                    } catch (Exception e) {
                        error += e.toString();
                        System.out.println("Er usuario: " + error);
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Catalogo de Objetos">
                    jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("objetos");
                    try {
                        for (JsonElement obj : jArray) {
                            objetos cse = gson.fromJson(obj, objetos.class);
                            int existe = 0;
                            existe = Integer.valueOf(Main.conexion.EjecutarEscalar("select count(*) from pos_objeto where idobjeto =  '" + cse.idobjeto + "' "));

                            if (existe == 0) {//Inserta
                                String qr;
                                qr = ("INSERT INTO  pos_objeto (idobjeto, nombre, descripcion) VALUES\n"
                                        + "( '" + cse.idobjeto + "',\n"
                                        + " '" + cse.nombre + "',\n"
                                        + " '" + cse.descripcion + "' );");
                                Main.conexion.Ejecutar(qr, true);
                            } else //actualiza
                            {
                                String q = "UPDATE  pos_objeto SET\n"
                                        + " nombre = '" + cse.nombre + "',\n"
                                        + " descripcion ='" + cse.descripcion + "' \n"
                                        + " WHERE idobjeto = '" + cse.idobjeto + "'; ";
                                Main.conexion.Ejecutar(q, true);
                            }
                        }
                    } catch (Exception e) {
                        error += e.toString();
                        System.out.println("Er objeto: " + error);
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Catalogo roldetalle">
                    jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("roldetalle");
                    try {
                        for (JsonElement obj : jArray) {
                            roldetalle cse = gson.fromJson(obj, roldetalle.class);
                            int existe = 0;
                            String qry;
                            existe = Integer.valueOf(Main.conexion.EjecutarEscalar("select count(*) from pos_rolldetalle where idrolldetalle = '" + cse.idrolldetalle + "' "));

                            if (existe == 0) { //Inserta    
                                qry
                                        = ("INSERT INTO  pos_rolldetalle (idrolldetalle, idrol, idobjeto, aplicar, anular, imprimir, consultar, editar, crear,  debaja) VALUES\n"
                                        + "( '" + cse.idrolldetalle + "',\n"
                                        + " '" + cse.idrol + "',\n"
                                        + " '" + cse.idobjeto + "',\n"
                                        + " " + cse.aplicar + ",\n"
                                        + " " + cse.anular + ",\n"
                                        + " " + cse.imprimir + ",\n"
                                        + " " + cse.consultar + ",\n"
                                        + " 1,\n"
                                        + " " + cse.crear + ",\n"
                                        + " " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + "  )");
                                Main.conexion.Ejecutar(qry, true);
                            } else //actualiza
                            {
                                qry = (" UPDATE  pos_rolldetalle SET\n"
                                        + " idrol = '" + cse.idrol + "',\n"
                                        + " idobjeto = '" + cse.idobjeto + "',\n"
                                        + " aplicar = " + cse.aplicar + ",\n"
                                        + " anular = " + cse.anular + ",\n"
                                        + " imprimir = " + cse.imprimir + ",\n"
                                        + " consultar = " + cse.consultar + ",\n"
                                        + " crear = " + cse.crear + ",\n"
                                        + " debaja = " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + " \n"
                                        + " WHERE idrolldetalle = '" + cse.idrolldetalle + "' ; ");
                                Main.conexion.Ejecutar(qry, true);
                            }
                            //   System.out.println(String.format("%1s,%2s,%3s,%4s", cse.debaja, cse.idopciontipo, cse.nombre, cse.nombre));
                        }
                    } catch (Exception e) {
                        error += e.toString();
                        System.out.println("Er roldetalle: " + error);
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Catalogo permisos">           
                    try {
                        jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("permisos");
                        for (JsonElement obj : jArray) {
                            permisos cse = gson.fromJson(obj, permisos.class);
                            String qry = "";
                            int existe = 0;
                            x++;
                            if (x == 1) {
                                int y = 0;
                            }

                            existe = Integer.valueOf(Main.conexion.EjecutarEscalar("SELECT count(*) FROM pos_permisos where idpermisos =  '" + cse.idpermisos + "' "));

                            if (existe == 0) { //Inserta                          
                                qry = ("INSERT INTO pos_permisos (idpermisos, idusuario, idrol, debaja) VALUES\n"
                                        + "('" + cse.idpermisos + "',\n"
                                        + "'" + cse.idusuario + "',\n"
                                        + "'" + cse.idrol + "',\n"
                                        + " " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + " );");

                                Main.conexion.Ejecutar(qry, true);
                            } else //actualiza
                            {
                                qry = ("UPDATE pos_permisos SET\n"
                                        + " idusuario = '" + cse.idusuario + "',\n"
                                        + " idrol = '" + cse.idrol + "', \n"
                                        + " debaja = " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + " \n"
                                        + " WHERE idpermisos = '" + cse.idpermisos + "' ; ");
                                Main.conexion.Ejecutar(qry, true);
                            }
                        }
                    } catch (Exception e) {
                        error += e.toString();
                        System.out.println("Er permisos: " + error);
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Catalogo configuracion">
                    try {
                        jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("configuracion");
                        for (JsonElement obj : jArray) {
                            configuracion cse = gson.fromJson(obj, configuracion.class);
                            int existe = 0;
                            String qry = "";
                            existe = Integer.valueOf(Main.conexion.EjecutarEscalar("SELECT count(*) FROM sys_configuracion where idconfiguracion =   '" + cse.idconfiguracion + "' "));

                            if (existe == 0) { //Inserta                          
                                qry = (" INSERT INTO sys_configuracion (idconfiguracion, nombre, valor, debaja) VALUES\n"
                                        + "('" + cse.idconfiguracion + "',\n"
                                        + "'" + cse.nombre + "',\n"
                                        + "'" + cse.valor + "',\n"
                                        + " " + (cse.debaja == null ? null : "' " + cse.debaja + "' ") + " ); ");

                                Main.conexion.Ejecutar(qry, true);
                            } else //actualiza
                            {
                                qry = (" UPDATE sys_configuracion SET\n"
                                        + " nombre = '" + cse.nombre + "',\n"
                                        + " valor = '" + cse.valor + "',\n"
                                        + " debaja = " + (cse.debaja == null ? null : "' " + cse.debaja + "' ") + " \n"
                                        + " WHERE idconfiguracion = '" + cse.idconfiguracion + "';");
                                if ((Main.settings.getCodigoTienda().equals("116TUMAN") || Main.settings.getCodigoTienda().equals("ROT"))
                                        && cse.idconfiguracion.equals("5")) {
                                    qry = "";
                                }
                                Main.conexion.Ejecutar(qry, true);
                            }
                            //System.out.println(String.format("%1s,%2s,%3s,%4s", cse.idarticulo, cse.idarticulopadre, cse.idopcion, cse.debaja, cse.idopciontipo));
                        }
                    } catch (Exception e) {
                        error += e.toString();
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Catalogo mesas">
                    try {
                        jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("mesas");
                        for (JsonElement obj : jArray) {
                            mesas cse = gson.fromJson(obj, mesas.class);
                            int existe = 0;
                            String qry = "";
                            existe = Integer.valueOf(Main.conexion.EjecutarEscalar("SELECT count(*) FROM pos_mesa where idmesa =  '" + cse.idmesa + "' "));

                            if (existe == 0) { //Inserta                          
                                qry = ("  INSERT INTO pos_mesa (idmesa, idtienda, codigo,  estado, debaja, parallevar) VALUES\n"
                                        + "('" + cse.idmesa + "',\n"
                                        + " '" + cse.idtienda + "',\n"
                                        + " '" + cse.codigo + "',\n"
                                        + " '" + cse.estado + "',\n"
                                        + " " + (cse.debaja == null ? null : "' " + cse.debaja + "' ") + ",\n"
                                        + " '" + cse.parallevar + "');");

                                Main.conexion.Ejecutar(qry, true);
                            } else //actualiza
                            {
                                qry = (" UPDATE pos_mesa SET\n"
                                        + " idtienda = '" + cse.idtienda + "',\n"
                                        + " codigo = '" + cse.codigo + "',\n"
                                        + " debaja = " + (cse.debaja == null ? null : "' " + cse.debaja + "' ") + ",\n"
                                        + " parallevar = '" + cse.parallevar + "'\n"
                                        + " WHERE idmesa = '" + cse.idmesa + "';");
                                Main.conexion.Ejecutar(qry, true);
                            }
                            //System.out.println(String.format("%1s,%2s,%3s,%4s", cse.idarticulo, cse.idarticulopadre, cse.idopcion, cse.debaja, cse.idopciontipo));
                        }
                    } catch (Exception e) {
                        error += e.toString();
                        System.out.println("Err mesas: " + error);
                    }
                    // </editor-fold>

                } catch (Exception ex) {

                } finally {
                    Main.conexion.CerrarConexion();
                    System.out.println("Actualizacion de usuarios realizada  " + error);
                }
            }
        } catch (JsonSyntaxException | HeadlessException x) {
        }

    }

    private static String obtenerUsuarios() {
        try {
            org.tempuri.Pos service = new org.tempuri.Pos();
            org.tempuri.PosSoap port = service.getPosSoap();
            return port.obtenerUsuarios();
        } catch (Exception x) {
            return "";
        }
    }

    private void UsuariosDeBaja() {
        String idtienda = Main.settings.getCodigoTienda().substring(0, 3);
        Main.conexion.Ejecutar("update pos_usuario set debaja = null ", true);
        String n = Main.conexion.Ejecutar("update pos_usuario u\n"
                + " inner join pos_permisos p on u.idusuario = p.idusuario \n"
                + " inner join pos_tienda t on u.idtienda = t.idtienda\n"
                + " set u.debaja = now()\n"
                + " where p.idrol not in ('7c9bead9-c7d8-11e2-a2ad-002719b373e4','6df7398b-a588-11e3-9b23-782bcb2714ba',\n"
                + " '461fdbf9-f0ad-11e3-9243-5cd998f62b11','78bd426f-b045-11e3-9aa1-782bcb2714ba')\n"
                + " and u.idusuario not in ('c623f542-5b9b-11e3-871b-94de8058637a','5258fae2-5b9c-11e3-871b-94de8058637a')\n"
                + " and t.codigo  not like '" + idtienda + "%'", true);
    }
}

// <editor-fold defaultstate="collapsed" desc="Clases auxiliares">        
class usuarios {

    public String idusuario;
    public String idtienda;
    public String nombre;
    public String nombreapellido;
    public String debaja;

    public usuarios(String p, String p_2, String p_3, String p_4, String nullable) {
        // TODO: Complete member initialization
        this.idusuario = p;
        this.idtienda = p_2;
        this.nombre = p_3;
        this.debaja = nullable;
        this.nombreapellido = p_4;
    }
}

class objetos {

    public String idobjeto;
    public String nombre;
    public String descripcion;

    public objetos(String p, String p_2, String p_3) {
        this.idobjeto = p;
        this.nombre = p_2;
        this.descripcion = p_3;
    }
}

class roldetalle {

    public String idrolldetalle;
    public String idrol;
    public String idobjeto;
    public int aplicar;
    public int anular;
    public int imprimir;
    public int consultar;
    public int crear;
    public String debaja;

    public roldetalle(String p, String p_2, String p_3, int p_4, int p_5, int p_6, int p_7, int p_8, String dateTime) {
        // TODO: Complete member initialization
        this.idrolldetalle = p;
        this.idrol = p_2;
        this.idobjeto = p_3;
        this.aplicar = p_4;
        this.anular = p_5;
        this.imprimir = p_6;
        this.consultar = p_7;
        this.crear = p_8;
        this.debaja = dateTime;
    }
}

class permisos {

    public String idpermisos;
    public String idusuario;
    public String idrol;
    public String debaja;

    public permisos(String p, String p_2, String p_3, String p_4) {
        this.idpermisos = p;
        this.idusuario = p_2;
        this.idrol = p_3;
        this.debaja = p_4;
    }
}

class configuracion {

    public String idconfiguracion;
    public String nombre;
    public String valor;
    public String debaja;

    public configuracion(String p, String p_2, String p_3, String p_4) {
        idconfiguracion = p;
        nombre = p_2;
        valor = p_3;
        debaja = p_4;
    }
}

class mesas {

    public String idmesa;
    public String idtienda;
    public String codigo;
    public int estado;
    public int parallevar;
    public String debaja;

    public mesas(String p, String p_2, String p_3, int p_4, int p_5, String p_6) {
        idmesa = p;
        idtienda = p_2;
        codigo = p_3;
        estado = p_4;
        parallevar = p_5;
        debaja = p_6;

    }
}

class municipio {

    public String idmunicipio;
    public String iddepartamento;
    public String nombre;
    public String codigopostal;

    public municipio(String p, String p_2, String p_3, String p_4) {
        idmunicipio = p;
        iddepartamento = p_2;
        nombre = p_3;
        codigopostal = p_4;
    }
}

class tienda {

    public String idtienda;
    public String idtipotienda;
    public String idmunicipio;
    public String codigo;
    public String nombre;
    public String direccion;
    public String telefono;
    public String empresa;
    public String nombrecomercial;
    public String nit;
    public String regimen;
    public String codigoestablecimiento;
    public String nombreestablecimiento;
    public String debaja;

    public tienda(String p, String p_2, String p_3, String p_4, String p_5, String p_6,
            String p_7, String p_8, String p_9, String p_10, String p_11, String p_12,
            String p_13, String p_14) {
        idtienda = p;
        idtipotienda = p_2;
        idmunicipio = p_3;
        codigo = p_4;
        nombre = p_5;
        direccion = p_6;
        telefono = p_7;
        empresa = p_8;
        nombrecomercial = p_9;
        nit = p_10;
        regimen = p_11;
        codigoestablecimiento = p_12;
        nombreestablecimiento = p_13;
        debaja = p_14;

    }
}
// </editor-fold>  
