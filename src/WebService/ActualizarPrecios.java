/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import Operaciones.backupBD;
import WebService.Promo.Consulta;
import WebService.Promo.clientePOS;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.awt.HeadlessException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import serviciosincronizacion.Main;

/**
 *
 * @author samuel
 */
public class ActualizarPrecios {

    String error = "";

    public void correrConsultas() {

        JsonParser parser = null;
        JsonArray jArray;
        String s = consulta();
        parser = new JsonParser();
        // Gson gson = new Gson();

        Gson gson = new GsonBuilder().create();
        Consulta query;
        query = gson.fromJson(s, Consulta.class);
        System.out.println(query.mensaje);
        System.out.println(query.query1);
        System.out.println(query.query2);

        try {
            Main.conexion.CrearConexion();
            Main.conexion._conexion.setAutoCommit(false);
            Statement st = Main.conexion._conexion.createStatement();
            if (!query.query1.equals("") || query.query1 != null) {
                st.execute(query.query1);
            }
            if (!query.query2.equals("") || query.query2 != null) {
                st.execute(query.query2);
            }
            Main.conexion._conexion.commit();

            if (!query.mensaje.equals("") || query.mensaje != null) {
                System.out.println("pos_ordendetalle  " + query.mensaje);
            }
        } catch (SQLException | HeadlessException x) {
            try {
                Main.conexion._conexion.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(ActualizarPrecios.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("pos_ordendetalle  " + error + "-" + x.toString());
        } finally {
            Main.conexion.CerrarConexion();
            System.out.println("Actualizacion Realizada " + error);
        }
    }

    public boolean LimpiarBD() {

        for (String serie : Main.settings.getSeries().values()) {
            String fecha = limpiarBD(serie);
            String resultado = "";
            if (fecha != null && !fecha.equals("")) {

                DateFormat format = new SimpleDateFormat("m d, yyyy", Locale.ROOT);
                Date date;
                backupBD bk = new backupBD();
                bk.backup_mantenimiento();
                date = Date.valueOf(fecha);
                System.out.println(date);

                String query = "delete d  from pos_ordendetalledetalle d\n"
                        + "        inner join pos_ordendetalle o on d.idorden_detalle= o.idorden_detalle and d.iddocumento = o.iddocumento\n"
                        + "        inner join pos_documento doc on d.iddocumento = doc.iddocumento                \n"
                        + "        where date(o.fechai)<=  date('" + fecha + "') and doc.serie = '" + serie + "';";
                resultado = Main.conexion.EjecutarSinMensaje(query, true);
                System.out.println("pos_ordendetalledetalle  " + resultado);

                query = "delete d  from pos_ordendetalle d\n"
                        + "        inner join pos_factura f on d.idfactura = f.idfactura and d.iddocumento = f.iddocumento\n"
                        + "        inner join pos_documento doc on  d.iddocumento = doc.iddocumento\n"
                        + "        where date(f.fecha)<= date('" + fecha + "') and doc.serie = '" + serie + "';";
                resultado = Main.conexion.EjecutarSinMensaje(query, true);
                System.out.println("pos_ordendetalle  " + resultado);

                query = "delete o from pos_orden o \n"
                        + "        inner join pos_ordendetalle d on o.idorden = d.idorden\n"
                        + "        where date(o.fecha) <= date('" + fecha + "');";
                resultado = Main.conexion.EjecutarSinMensaje(query, true);
                System.out.println("pos_orden  " + resultado);

                query = "delete fo from pos_formapagodetalle fo\n"
                        + "			inner join pos_factura f on fo.idfactura = f.idfactura and f.iddocumento = fo.iddocumento\n"
                        + "            inner join pos_documento d on f.iddocumento = d.iddocumento\n"
                        + "			where date(f.fecha) <= date('" + fecha + "') and d.serie = '" + serie + "';";
                resultado = Main.conexion.EjecutarSinMensaje(query, true);
                System.out.println("pos_formapagodetalle  " + resultado);

                query = "		delete m from mov_anticipo m \n"
                        + "			inner join pos_anticipos a on m.idanticipo = a.idanticipo  \n"
                        + "			inner join pos_factura f on a.iddocumento = f.iddocumento and a.idfactura = f.idfactura\n"
                        + "            inner join pos_corte c on m.idcorte = c.idcorte\n"
                        + "			where date(c.fecha)  <= date('" + fecha + "');\n";
                resultado = Main.conexion.EjecutarSinMensaje(query, true);
                System.out.println("mov_anticipo  " + resultado);

                query = "		delete d from pos_anticiposdetalle d\n"
                        + "			inner join pos_anticipos a on d.idanticipo = a.idanticipo \n"
                        + "            inner join pos_documento doc on a.iddocumento = doc.iddocumento\n"
                        + "			inner join pos_factura f on a.iddocumento = f.iddocumento and a.idfactura = f.idfactura\n"
                        + "			where date(f.fecha) <= date('" + fecha + "');\n";
                resultado = Main.conexion.EjecutarSinMensaje(query, true);
                System.out.println("pos_anticiposdetalle  " + resultado);

                query = "		delete a from pos_anticipos a \n"
                        + "			inner join pos_factura f on a.iddocumento = f.iddocumento and a.idfactura = f.idfactura\n"
                        + "            inner join pos_documento d on a.iddocumento = d.iddocumento\n"
                        + "			where date(f.fecha) <= date('" + fecha + "') and d.serie = '" + serie + "' ;\n";
                resultado = Main.conexion.EjecutarSinMensaje(query, true);
                System.out.println("pos_anticipos  " + resultado);

                query = "		delete f from pos_factura f\n"
                        + "        inner join pos_corte c on f.idcorte = c.idcorte  and f.iddocumento = c.iddocumento\n"
                        + "        inner join pos_documento d on f.iddocumento = d.iddocumento\n"
                        + "        where date(f.fecha) <= date('" + fecha + "') and d.serie = '" + serie + "'\n";
                resultado = Main.conexion.EjecutarSinMensaje(query, true);
                System.out.println("pos_factura  " + resultado);

                query = "delete c from pos_corte c\n"
                        + "        inner join pos_documento d on c.iddocumento = d.iddocumento\n"
                        + "        inner join pos_factura f on c.idcorte = f.idcorte and f.iddocumento = d.iddocumento\n"
                        + "        where date(f.fecha) <= date('" + fecha + "') and d.serie = '" + serie + "';";
                resultado = Main.conexion.EjecutarSinMensaje(query, true);
                System.out.println("pos_corte  " + resultado);

                query = "		delete  from  pos_anulaciones where  date(fechai)  <= date('" + fecha + "');\n";
                resultado = Main.conexion.EjecutarSinMensaje(query, true);
                System.out.println("pos_anulaciones  " + resultado);

                query = "		delete from gen_bitacora where date(fechai)  <= date('" + fecha + "');";
                resultado = Main.conexion.EjecutarSinMensaje(query, true);
                System.out.println("gen_bitacora  " + resultado);
            }
            if (resultado.equals("")) {
                return false;
            } else {
                return false;
            }
        }
        return true;

    }

    public void ActualizarCatalogos() {
        Gson gson = new Gson();
        JsonParser parser;
        JsonArray jArray;
        EnviarCorte e = new EnviarCorte();
        String s = "";
        s = actualizar();
        String qry = "";
        parser = new JsonParser();

        try {
            // <editor-fold defaultstate="collapsed" desc="Catalogo de Categorias">
            try {
                jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("categoria");
                for (JsonElement obj : jArray) {
                    Categoria cse = gson.fromJson(obj, Categoria.class);
                    int existe = Integer.valueOf(Main.conexion.EjecutarEscalar("SELECT count(*) FROM gen_categoria where idcategoria ='" + cse.idCategoriaArticulo + "' "));

                    if (existe == 0) { //Inserta
                        qry = ("insert into gen_categoria (idcategoria, idpadre, idicono, nombre, debaja, partedemenu) values "
                                + "( '" + cse.idCategoriaArticulo + "'," + (cse.idPadre == null ? null : "'" + cse.idPadre + "'") + ", '93e2fd54-5b93-11e3-871b-94de8058637a', '" + cse.nombre + "', " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + " ," + (cse.partedemenu == null ? null : "'" + cse.partedemenu + "'") + "   )");
                        Main.conexion.Ejecutar(qry, true);
                    } else //actualiza
                    {
                        Main.conexion.Ejecutar("update gen_categoria  set idpadre = " + (cse.idPadre == null ? null : "'" + cse.idPadre + "'") + ",  nombre='" + cse.nombre + "', debaja= " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + ", partedemenu= " + (cse.partedemenu == null ? null : "'" + cse.partedemenu + "'") + " \n"
                                + " where idcategoria = '" + cse.idCategoriaArticulo + "' ", true);
                    }
                    //System.out.println(String.format("%1s,%2s,%3s,%4s", cse.idCategoriaArticulo, cse.idPadre, cse.nombre, cse.debaja, cse.partedemenu));
                }
            } catch (JsonSyntaxException ge) {
                error += e.toString();
                System.out.print(error);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Catalogo de Articulos">
            jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("articulo");
            try {
                for (JsonElement obj : jArray) {
                    Articulo cse = gson.fromJson(obj, Articulo.class);
                    int coci = 0, esdivisible =0;
                    int existe = Integer.valueOf(Main.conexion.EjecutarEscalar("select count(*) from gen_articulo where idarticulo = '" + cse.idarticulo + "' "));
                  
                    int especial = 0;
                    if (cse.preciotope > 0) {
                        especial = 1;
                    }
                    if (null == cse.escocina) {
                        coci = 0;
                    } else switch (cse.escocina) {
                        case "1":
                            coci = 1;
                            break;
                        default:
                            coci = 0;
                            break;
                    }
  
                    if (null == cse.esdivisible) {
                        esdivisible = 0;
                    } else switch (cse.esdivisible) {
                        case "1":
                            esdivisible = 1;
                            break;
                        default:
                            esdivisible = 0;
                            break;
                    }

                    String tipoarticulo = "12dba557-c48b-11e2-a2ad-002719b373e4";
                    // evalua el tipo de articulo
                    if (cse.idtipoarticulo.equals("3")) {
                        tipoarticulo = "dc2a1b79-97bf-11e9-ad1c-080027cad439";
                    }
                    String des = "";
                    try {
                        des = cse.descripcion.substring(0, 95);
                    } catch (Exception x) {
                        des = "";
                    }
                    if (existe == 0) {//Inserta
                        String qr;
                        qr = ("insert into gen_articulo \n"
                                + " (idarticulo, id, idcategoria, codigo, nombre, precio, "
                                + " costo, descripcion, debaja, preciotope, escocina, especial, idicono, idtipoarticulo, esdivisible) values "
                                + "('" + cse.idarticulo + "', " + cse.id + ", '" + cse.idcategoria + "', '" + cse.codigo + "',   '" + cse.nombre + "'  "
                                + ", " + cse.precio + ", " + cse.costo + ", '" + des + "', " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + " "
                                + " , " + cse.preciotope + ", " + coci + ", " + especial + ", '93e2fd54-5b93-11e3-871b-94de8058637a', '" + tipoarticulo + "' , " + esdivisible + "    );");
                        Main.conexion.Ejecutar(qr, true);
                        Main.conexion.Ejecutar("insert into gen_multiplo (idmultiplo, idarticulo, idunidadmedida, multiplo, predeterminado)\n"
                                + "values ((select uuid()), '" + cse.idarticulo + "', 'f7f18bd1-5d03-11e3-871b-94de8058637a',1,1)", true);
                    } else //actualiza
                    {
                        String q = "update gen_articulo set id =" + cse.id + ", idcategoria='" + cse.idcategoria + "', codigo='" + cse.codigo + "', "
                                + " nombre ='" + cse.nombre + "', precio= " + cse.precio + ", costo=" + cse.costo + ", descripcion='" + des + "', "
                                + " debaja=" + (cse.debaja == null ? null : "'" + cse.debaja + "'") + ", preciotope=" + cse.preciotope + ", escocina='" + coci + "', especial='" + especial + "', \n"
                                + " idtipoarticulo = '" + tipoarticulo + "', esdivisible = " + esdivisible + " where idarticulo = '" + cse.idarticulo + "' ";
                       Main.conexion.Ejecutar(q, true);
                    }
                    //      System.out.println(String.format("%1s,%2s,%3s,%4s", cse.codigo, cse.costo, cse.descripcion, cse.nombre, cse.idtipoarticulo));
                }
            } catch (Exception x) {
                error += x.toString();
                System.out.print(error);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Catalogo Opcion Tipo">
            jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("opcionTipo");
            try {
                for (JsonElement obj : jArray) {
                    OpcionTipo cse = gson.fromJson(obj, OpcionTipo.class);
                    int existe = Integer.valueOf(Main.conexion.EjecutarEscalar("select count(*)  from pos_opciontipo where idopciontipo ='" + cse.idopciontipo + "' "));

                    if (existe == 0) { //Inserta
                        Main.conexion.Ejecutar("insert into pos_opciontipo ( idopciontipo, nombre, debaja ) values \n"
                                + "( '" + cse.idopciontipo + "', '" + cse.nombre + "', " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + "  )", true);
                    } else //actualiza
                    {
                        Main.conexion.Ejecutar("update pos_opciontipo set  nombre='" + cse.nombre + "', "
                                + " debaja = " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + " where idopciontipo = '" + cse.idopciontipo + "'  ", true);
                    }
                    //   System.out.println(String.format("%1s,%2s,%3s,%4s", cse.debaja, cse.idopciontipo, cse.nombre, cse.nombre));
                }
            } catch (JsonSyntaxException de) {
                error += e.toString();
                System.out.print(error);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Catalogo Opcion">
            try {

                jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("opcion");
                for (JsonElement obj : jArray) {
                    Opcion cse = gson.fromJson(obj, Opcion.class);
                    int existe = Integer.valueOf(Main.conexion.EjecutarEscalar("select count(*)  from pos_opcion where idopcion = '" + cse.idopcion + "' "));
                    //  String qry = "";
                    if (cse.idarticulopadre.equals("")) {
                        cse.idarticulopadre = null;
                    }
                    if (existe == 0) { //Inserta
                        qry = ("insert into pos_opcion ( idopcion, idarticulo, idopciontipo, idarticulopadre, debaja ) values \n"
                                + "('" + cse.idopcion + "', '" + cse.idarticulo + "', '" + cse.idopciontipo + "', " + (cse.idarticulopadre == null ? null : "'" + cse.idarticulopadre + "'") + ", " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + "  )");
                        Main.conexion.Ejecutar(qry, true);
                    } else //actualiza
                    {
                        // if (cse.debaja != null)
                        qry = ("update pos_opcion set  idarticulo='" + cse.idarticulo + "', idopciontipo='" + cse.idopciontipo + "', idarticulopadre=" + (cse.idarticulopadre == null ? null : "'" + cse.idarticulopadre + "'") + ", debaja=" + (cse.debaja == null ? null : "'" + cse.debaja + "'") + "  where\n"
                                + " idopcion = '" + cse.idopcion + "' ");
                        Main.conexion.Ejecutar(qry, true);

                    }
                    //System.out.println(String.format("%1s,%2s,%3s,%4s", cse.idarticulo, cse.idarticulopadre, cse.idopcion, cse.debaja, cse.idopciontipo));
                }
            } catch (JsonSyntaxException se) {
                error += e.toString();
                System.out.print(error);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Catalogo Clientes">
            try {
                jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("cliente");
                for (JsonElement obj : jArray) {
                    Cliente cse = gson.fromJson(obj, Cliente.class);
                    int existe = Integer.valueOf(Main.conexion.EjecutarEscalar("select count(*)  from gen_clientes where upper(trim(nit)) =  '" + cse.nit.trim().toUpperCase() + "' "));

                    if (existe == 0) { //Inserta
                        qry = ("insert into gen_clientes (idcliente, nit, nombre, direccion, debaja, correo)\n values "
                                + "('" + cse.uuid + "', '" + cse.nit + "', '" + cse.nombre + "', '" + cse.direccion + "',  " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + ", '" + cse.correo + "'  )");
                        Main.conexion.Ejecutar(qry, true);
                    } else //actualiza
                    {
                        // if (cse.debaja != null)
                        qry = ("update gen_clientes set nit ='" + cse.nit + "', nombre ='" + cse.nombre + "', direccion ='" + cse.direccion + "',  correo = '" + cse.correo + "', debaja=" + (cse.debaja == null ? null : "'" + cse.debaja + "'") + "  where\n"
                                + " upper(trim(nit)) = '" + cse.nit.trim().toUpperCase() + "' ");
                        Main.conexion.Ejecutar(qry, true);

                    }
//System.out.println(String.format("%1s,%2s,%3s,%4s", cse.idarticulo, cse.idarticulopadre, cse.idopcion, cse.debaja, cse.idopciontipo));
                }
            } catch (JsonSyntaxException es) {
                error += e.toString();
                System.out.print(error);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Catalogo Tipo Descuento">
            try {
                jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("descuento");
                for (JsonElement obj : jArray) {
                    Descuento cse = gson.fromJson(obj, Descuento.class);
                    int existe = Integer.valueOf(Main.conexion.EjecutarEscalar("SELECT count(*) FROM gen_tipodescuento where iddescuento = " + cse.iddescuento + " "));
                    if (existe == 0) { //Inserta
                        qry = ("INSERT INTO gen_tipodescuento (iddescuento, tipo) VALUES (" + cse.iddescuento + " ,'" + cse.tipo + "' );");
                        Main.conexion.Ejecutar(qry, true);
                    } else //actualiza
                    {
                        qry = ("UPDATE gen_tipodescuento SET  tipo = '" + cse.tipo + "' WHERE iddescuento = " + cse.iddescuento + " ; ");
                        Main.conexion.Ejecutar(qry, true);
                    }
                    //System.out.println(String.format("%1s,%2s,%3s,%4s", cse.idarticulo, cse.idarticulopadre, cse.idopcion, cse.debaja, cse.idopciontipo));
                }
            } catch (JsonSyntaxException es) {
                error += e.toString();
                System.out.print(error);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Catalogo Articulo Descuento">
            try {
                jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("articulodescuento");
                for (JsonElement obj : jArray) {
                    ArticuloDescuento cse = gson.fromJson(obj, ArticuloDescuento.class);
                    int existe = Integer.valueOf(Main.conexion.EjecutarEscalar(("select count(*) FROM gen_articulodescuento where Iddescuento =   " + cse.Iddescuento + " ")));

                    if (existe == 0) { //Inserta
                        qry = ("INSERT INTO gen_articulodescuento ( Iddescuento, idarticulo, idtipodescuento, descuento, debaja) VALUES\n"
                                + "('" + cse.Iddescuento + "' ,'" + cse.idarticulo + "' ," + cse.idtipodescuento + " ," + cse.descuento + " ," + (cse.debaja == null ? null : "'" + cse.debaja + "'") + ") ");
                        Main.conexion.Ejecutar(qry, true);
                    } else //actualiza
                    {
                        qry = ("UPDATE gen_articulodescuento SET idarticulo = '" + cse.idarticulo + "', idtipodescuento = " + cse.idtipodescuento + " , descuento = " + cse.descuento + " , debaja = " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + " WHERE Iddescuento = '" + cse.Iddescuento + "'");
                        Main.conexion.Ejecutar(qry, true);
                    }
                    //System.out.println(String.format("%1s,%2s,%3s,%4s", cse.idarticulo, cse.idarticulopadre, cse.idopcion, cse.debaja, cse.idopciontipo));
                }
            } catch (JsonSyntaxException es) {
                error += e.toString();
                System.out.print(error);
            }
            // </editor-fold>dd
            // <editor-fold defaultstate="collapsed" desc="Mensajes de Promocion">
            try {
                jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("promo");
                for (JsonElement obj : jArray) {
                    Promo cse = gson.fromJson(obj, Promo.class);
                    int existe = Integer.valueOf(Main.conexion.EjecutarEscalar(("select count(*) FROM pos_mensaje where idmensaje =   " + cse.IdMensaje + " ")));

                    String idcate = null;
                    if (!cse.IdCategoria.equals("")) {
                        idcate = "'" + cse.IdCategoria + "'";
                    }

                    if (existe == 0) { //Inserta
                        qry = ("INSERT INTO pos_mensaje ( idmensaje, mensaje, idcategoria, idtipo, debaja) VALUES\n"
                                + "('" + cse.IdMensaje + "' ,'" + cse.Mensaje + "' ," + idcate + " ," + cse.IdTipo + " ," + (cse.Debaja == null ? null : "'" + cse.Debaja + "'") + ") ");
                        Main.conexion.Ejecutar(qry, true);
                    } else //actualiza
                    {
                        qry = ("UPDATE pos_mensaje SET mensaje = '" + cse.Mensaje + "', idcategoria = " + idcate + " , idtipo = " + cse.IdTipo + " , debaja = " + (cse.Debaja == null ? null : "'" + cse.Debaja + "'") + " WHERE idmensaje = '" + cse.IdMensaje + "'");
                        Main.conexion.Ejecutar(qry, true);
                    }
                    //System.out.println(String.format("%1s,%2s,%3s,%4s", cse.idarticulo, cse.idarticulopadre, cse.idopcion, cse.debaja, cse.idopciontipo));
                }
            } catch (JsonSyntaxException es) {
                error += e.toString();
                System.out.print(error);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Clientes Institucionales">
            try {
                jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("clientePOS");
                for (JsonElement obj : jArray) {
                    clientePOS cse = gson.fromJson(obj, clientePOS.class);
                    int existe = Integer.valueOf(Main.conexion.EjecutarEscalar(("select count(*)  from gen_clientes where upper(trim(nit)) =  '" + cse.nit.toUpperCase().trim() + "' ")));

                    if (existe == 0) { //Inserta
                        qry = ("insert into gen_clientes (idcliente, nit, nombre, direccion, debaja,institucional, correo)\n values "
                                + "('" + cse.idcliente + "', '" + cse.nit + "', '" + cse.nombre + "', '" + cse.direccion + "',  " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + ",1 , '" + cse.correo + "' )");
                        Main.conexion.Ejecutar(qry, true);
                    } else //actualiza
                    {
                        // if (cse.debaja != null)
                        qry = ("update gen_clientes set institucional = 1, nit ='" + cse.nit + "', nombre ='" + cse.nombre + "', correo = '" + cse.correo + "' ,direccion ='" + cse.direccion + "'   where\n"
                                + " upper(trim(nit)) = '" + cse.nit.toUpperCase().trim() + "' ");
                        Main.conexion.Ejecutar(qry, true);
                    }
                    //System.out.println(String.format("%1s,%2s,%3s,%4s", cse.idarticulo, cse.idarticulopadre, cse.idopcion, cse.debaja, cse.idopciontipo));
                }
            } catch (JsonSyntaxException es) {
                error += e.toString();
                System.out.print(error);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Familia de Precios">
            try {
                Main.conexion.Ejecutar("delete from gen_articulofamilia;", true);
                jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("familia");
                if (jArray != null) {

                    for (JsonElement obj : jArray) {
                        FamiliaPrecios cse = gson.fromJson(obj, FamiliaPrecios.class);
                        int existe = Integer.valueOf(Main.conexion.EjecutarEscalar("select count(*) from gen_articulofamilia where trim(upper(articulo)) like trim(upper('" + cse.articulo + "'))"));
                        if (existe == 0) {
                            qry = ("INSERT INTO gen_articulofamilia (articulo, familia, codigo, precio) VALUES\n"
                                    + "('" + cse.articulo + "', '" + cse.familiaprecios + "', '" + cse.codigo + "', '" + cse.precio + "' )");
                            Main.conexion.Ejecutar(qry, true);
                        } else {
                            qry = ("update gen_articulofamilia set  familia = '" + cse.familiaprecios + "', codigo='" + cse.codigo + "', precio ='" + cse.precio + "'"
                                    + " where trim(upper(articulo)) like trim(upper('" + cse.articulo + "')) ");
                            Main.conexion.Ejecutar(qry, true);
                        }

                    }
                }
            } catch (JsonSyntaxException | NumberFormatException eee) {
                error += eee.toString();
                System.out.print(error);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Extras>
            try {
                jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("extras");
                if (jArray != null) {

                    for (JsonElement obj : jArray) {
                        Extras cse = gson.fromJson(obj, Extras.class);
                        int existe;
                        existe = Integer.valueOf(Main.conexion.EjecutarEscalar("SELECT  count(*) FROM pos_extras where idextra =   ('" + cse.idextra + "') "));

                        if (existe == 0) {
                            qry = ("insert into pos_extras  (idextra, idarticulo, idcategoria) values  "
                                    + "('" + cse.idextra + "', '" + cse.idarticulo + "', '" + cse.idcategoria + "' )");
                            Main.conexion.Ejecutar(qry, true);
                        } else {
                            qry = ("update pos_extras set  idextra = '" + cse.idextra + "', idarticulo='" + cse.idarticulo + "', idcategoria ='" + cse.idcategoria + "' , debaja =" + (cse.debaja == null ? null : "'" + cse.debaja + "'") + " "
                                    + " where trim( (idextra)) like trim( ('" + cse.idextra + "')) ");
                            Main.conexion.Ejecutar(qry, true);
                        }

                    }
                }
            } catch (JsonSyntaxException | NumberFormatException eee) {
                error += eee.toString();
                System.out.print(error);
            }
            // </editor-fold> 
            System.out.println(" Actualizacion Precios Realizado  " + error);
        } catch (JsonSyntaxException | NumberFormatException ex) {
            Logger.getLogger(ActualizarPrecios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ActualizarClientesWs() {
        Gson gson = new Gson();
        JsonParser parser;
        JsonArray jArray;
        String s = "";
        s = obtenerClientesPos();
        parser = new JsonParser();
        try {
            Main.conexion.CrearConexion();
            Main.conexion._conexion.setAutoCommit(false);
            Statement st;
            st = Main.conexion._conexion.createStatement();

            // <editor-fold defaultstate="collapsed" desc="Catalogo Clientes">
            jArray = (JsonArray) parser.parse(s).getAsJsonObject().get("cliente");
            for (JsonElement obj : jArray) {
                Cliente cse = gson.fromJson(obj, Cliente.class);
                int existe = 0;
                String qry = "";
//                    if (cse.debaja.equals("")) {
//                        cse.debaja = null;
//                    }
                ResultSet _rs = st.executeQuery("select count(*)  from gen_clientes where  upper(trim(nit))  =  '" + cse.nit + "' ");
                if (_rs.next()) {
                    existe = _rs.getInt(1);
                }
                if (existe == 0) { //Inserta
                    qry = ("insert into gen_clientes (idcliente, nit, nombre, direccion, debaja, correo)\n values "
                            + "('" + cse.uuid + "', '" + cse.nit + "', '" + cse.nombre + "', '" + cse.direccion + "',  " + (cse.debaja == null ? null : "'" + cse.debaja + "'") + " , '" + cse.correo + "' )");
                    st.execute(qry);
                } else //actualiza
                {
                    qry = ("update gen_clientes set institucional = 1, nit ='" + cse.nit + "', nombre ='" + cse.nombre + "', direccion ='" + cse.direccion + "', correo = '" + cse.correo + "'   where\n"
                            + " upper(trim(nit)) = '" + cse.nit.toUpperCase().trim() + "' ");
                    st.execute(qry);

                    qry = ("update gen_clientes set institucional = 1,  debaja=" + (cse.debaja == null ? null : "'" + cse.debaja + "'") + "  where\n"
                            + " idcliente = '" + cse.uuid + "' ");
                    st.execute(qry);
                }
//System.out.println(String.format("%1s,%2s,%3s,%4s", cse.idarticulo, cse.idarticulopadre, cse.idopcion, cse.debaja, cse.idopciontipo));
            }

            // </editor-fold>
            Main.conexion._conexion.commit();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        Main.conexion.CerrarConexion();
        System.out.println("Actualizacion clientes Realizado  " + error);
    }

    private static String actualizar() {
        try {
            org.tempuri.Sumacorte service = new org.tempuri.Sumacorte();
            org.tempuri.SumacorteSoap port = service.getSumacorteSoap();
            return port.actualizar();
        } catch (Exception x) {
            return "";
        }
    }

    private static String consulta() {
        try {
            org.tempuri.Sumacorte service = new org.tempuri.Sumacorte();
            org.tempuri.SumacorteSoap port = service.getSumacorteSoap();
            return port.consulta();
        } catch (Exception x) {
            return "";
        }

    }

    private static String limpiarBD(java.lang.String json) {
        try {
            org.tempuri.Pos service = new org.tempuri.Pos();
            org.tempuri.PosSoap port = service.getPosSoap();
            return port.limpiarBD(json);
        } catch (Exception x) {
            return "";
        }
    }

    private static String obtenerClientesPos() {
        try {
            org.tempuri.Sumacorte service = new org.tempuri.Sumacorte();
            org.tempuri.SumacorteSoap port = service.getSumacorteSoap();
            return port.obtenerClientesPos();
        } catch (Exception x) {
            return "";
        }
    }

}

// <editor-fold defaultstate="collapsed" desc="Clases auxiliares">        
class Categoria {

    public String idCategoriaArticulo;
    public String idPadre;
    public String nombre;
    public String debaja;
    public String partedemenu;

    public Categoria(String p, String p_2, String p_3, String nullable, String p_4) {
        // TODO: Complete member initialization
        this.idCategoriaArticulo = p;
        this.idPadre = p_2;
        this.nombre = p_3;
        this.debaja = nullable;
        this.partedemenu = p_4;
    }
}

class Articulo {

    public String idarticulo; //AliasPos
    public String id; // idArticulo Entero
    public String idcategoria;
    public String idtipoarticulo;
    public String codigo;
    public String nombre;
    public double precio;
    public double costo;
    public String descripcion;
    public String debaja;
    public String esespecial;
    public double preciotope;
    public String escocina;
    public String esdivisible;

    public Articulo(String p, String p_2, String p_3, String p_4, String p_5, String p_6, double p_7, double p_8, String p_9, String dateTime, String p_10, double p_11, String p_12, String _esdivisible) {
        // TODO: Complete member initialization
        this.idarticulo = p;
        this.id = p_2;
        this.idcategoria = p_3;
        this.idtipoarticulo = p_4;
        this.codigo = p_5;
        this.nombre = p_6;
        this.precio = p_7;
        this.costo = p_8;
        this.descripcion = p_9;
        this.debaja = dateTime;
        this.esespecial = p_10;
        this.preciotope = p_11;
        this.escocina = p_12;
        this.esdivisible = _esdivisible;
    }
}

class Opcion {

    public String idopcion;
    public String idarticulo;
    public String idopciontipo;
    public String idarticulopadre;
    public String debaja;

    public Opcion(String p, String p_2, String p_3, String p_4, String dateTime) {
        // TODO: Complete member initialization
        this.idopcion = p;
        this.idarticulo = p_2;
        this.idopciontipo = p_3;
        this.idarticulopadre = p_4;
        this.debaja = dateTime;
    }
}

class OpcionTipo {

    public String idopciontipo;
    public String nombre;
    public String debaja;

    public OpcionTipo(String p, String p_2, String p_3) {
        // TODO: Complete member initialization
        this.idopciontipo = p;
        this.nombre = p_2;
        this.debaja = p_3;
    }
}

class Cliente {

    public String uuid;
    public String nit;
    public String nombre;
    public String direccion;
    public String debaja;
    public String correo;

    public Cliente(String p, String p_2, String p_3, String p_4, String p_5, String p_6) {
        uuid = p;
        nit = p_2;
        direccion = p_3;
        nombre = p_4;
        debaja = p_5;
        correo = p_6;
    }
}

class Descuento {

    public int iddescuento;
    public String tipo;

    public Descuento(int p, String p_2) {
        iddescuento = p;
        tipo = p_2;
    }

}

class ArticuloDescuento {

    public int Iddescuento;
    public String idarticulo;
    public int idtipodescuento;
    public double descuento;
    public String debaja;

    public ArticuloDescuento(int p, String p_2, int p_3, double p_4, String p_5) {
        Iddescuento = p;
        idarticulo = p_2;
        idtipodescuento = p_3;
        descuento = p_4;
        debaja = p_5;
    }
}

class FamiliaPrecios {

    public String articulo;
    public String familiaprecios;
    public String codigo;
    public double precio;

    public FamiliaPrecios(String p, String p_2, String p_3, double p_4) {
        articulo = p;
        familiaprecios = p_2;
        codigo = p_3;
        precio = p_4;
    }
}

class Extras {

    public String idextra;
    public String idarticulo;
    public String idcategoria;
    public String debaja;

    public Extras(String p, String p_2, String p_3, String p_4) {
        // TODO: Complete member initialization
        this.idextra = p;
        this.idarticulo = p_2;
        this.idcategoria = p_3;
        this.debaja = p_4;
    }
}

class Promo {

    public int IdMensaje;
    public String Mensaje;
    public String IdCategoria;
    public int IdTipo;
    public String Debaja;

    public Promo(int p, String p_2, String p_3, int p_4, String dateTime) {
        // TODO: Complete member initialization
        this.IdMensaje = p;
        this.Mensaje = p_2;
        this.IdCategoria = p_3;
        this.IdTipo = p_4;
        this.Debaja = dateTime;
    }

    class clientePOS {

        public String idcliente;
        public String nit;
        public String nombre;
        public String direccion;
        public String debaja;
        public String correo;

        public clientePOS(String p, String p_2, String p_3, String p_4, String dateTime, String p_6) {
            // TODO: Complete member initialization
            this.idcliente = p;
            this.nit = p_2;
            this.nombre = p_3;
            this.direccion = p_4;
            this.debaja = dateTime;
            correo = p_6;
        }
    }
// </editor-fold>  

// <editor-fold defaultstate="collapsed" desc="Clase Consulta">   
    class Consulta {

        public String query1;
        public String query2;
        public String mensaje;

        public Consulta(String p, String p_2, String p_3) {
            // TODO: Complete member initialization
            this.query1 = p;
            this.query2 = p_2;
            this.mensaje = p_3;

        }
    }
}

// </editor-fold>  
