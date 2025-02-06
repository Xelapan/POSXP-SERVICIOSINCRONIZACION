/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

//import Controlador.sysControlador;
//import Entidades.DatosCliente;
//import Entidades.RespuetaTarjetaVIP;
import WebService.Entidad.Entidad_OrdenCafe;
import WebService.Entidad.Entidad_Corte;
import WebService.Entidad.Entidad_Cliente;
import WebService.Entidad.Entidad_Anticipos;
import WebService.Entidad.EntidadTiempoCertificacion;
import WebService.Entidad.EncabezadoVentas;
import WebService.Entidad.DetalleVentaCliente;
import WebService.Entidad.DetalleVenta;
import WebService.Entidad.DetalleCorte;
import Operaciones.DatosCliente;
import WebService.Entidad.Corte;
import WebService.Entidad.EntidadClienteCubo;
import WebService.Entidad.Entidad_anulacion;
import WebService.Entidad.Entidad_menu_guarniciones;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import serviciosincronizacion.Main;

/**
 *
 * @author samuel
 */
public class EnviarCorte {

    Entidad_Corte corte;
    EncabezadoVentas venta;
    public static String Mensaje = "";

// <editor-fold defaultstate="collapsed" desc="Envia el corte Digial y consumos de cafeteria y panaderia">
    public void EnviarCorteFinal() {
        Hashtable ht = new Hashtable();
        ResultSet rs = Main.conexion.EjecutarConsulta("select idcorte, fecha from pos_corte where date(fecha) between (DATE_SUB(curdate(), Interval " + Main.settings.getDiasSincronizar() + " day)) and   curdate()    and tipocorte = 'F' order by fecha desc;");
        try {
            if (rs != null) {
                while (rs.next()) {
                    ht.put(rs.getString(1), rs.getString(2));
                }
            }
            Main.conexion.CerrarConexion();
            rs.close();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        String respuesta = "", error = "";
        Enumeration e = ht.keys();
        Object clave;

        while (e.hasMoreElements()) {
            clave = e.nextElement();
            if (!clave.equals("")) {
                respuesta = EnvioDeCortesCafe(clave.toString(), false, 0, 0);
                if (!respuesta.contains("")) {
                    error += respuesta;
                    System.out.println("El servicio no esta disponible" + error);
                } else {
                    Main.conexion.Ejecutar("update pos_corte set transferida = now() where idcorte = '" + clave.toString() + "' ", true);
                }
            }
        }

        System.out.println("Sincronizacion Corte " + error + Mensaje);

    }

    private static String saldoTienda(java.lang.String json) {
        try {
            org.tempuri.Pos service = new org.tempuri.Pos();
            org.tempuri.PosSoap port = service.getPosSoap();
            return port.saldoTienda(json);
        } catch (Exception x) {
            System.out.print(x.toString());
            return null;
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Envia los clientes para los puntos">
    public String EnviarCliente() {
        Gson gson;
        String json;
        Entidad_Cliente cliente;
        String respuesta = "";
        ResultSet rs = Main.conexion.EjecutarConsulta("select c.idcliente, d.iddocumento,  date(f.fecha) fecha, count(f.idfactura) puntos from gen_clientes c\n"
                + " inner join pos_factura f on c.idcliente = f.idcliente\n"
                + " inner join pos_documento d on f.iddocumento = d.iddocumento\n"
                + " where f.estado = 1 and c.idcliente != '1'\n"
                + " and date(f.fecha) between (  DATE_SUB(curdate(), INTERVAL '" + Main.settings.getDiasSincronizar() + "' DAY)) and (date(now()))\n"
                + " group by  d.iddocumento, c.idcliente , DATE(f.fecha)\n"
                + " ORDER BY date(f.fecha); ");
        try {
            if (rs != null) {
                while (rs.next()) {
                    gson = new Gson();
                    cliente = new Entidad_Cliente();
                    cliente.setIdCliente(rs.getString("idcliente"));
                    cliente.setIdDocumento(rs.getString("iddocumento"));
                    cliente.setFecha(rs.getString("fecha"));
                    cliente.setPuntos(rs.getDouble("puntos"));
                    json = gson.toJson(cliente);
                    respuesta += clientePOS(json);
                }
            }
        } catch (Exception ex) {
            System.out.println("Clientes  " + ex.toString());
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                System.out.println("Clientes " + ex.toString());
            }
        }
        return respuesta;
    }

    private static String clientePOS(java.lang.String json) {
        try {
            org.tempuri.Pos service = new org.tempuri.Pos();
            org.tempuri.PosSoap port = service.getPosSoap();
            return port.clientePOS(json);
        } catch (Exception x) {
            return "";
        }
    }
    // </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Envia la informacion para el cubo de clientes y producto">
    public void EnviarResumenVentas() {
        try {
            // Recorriendo las series de la tienda
            for (String iddoc : Main.settings.getSeries().keySet()) {
                for (int x = 0; x <= Main.settings.getDiasSincronizar(); x++) {
                    String fecha = Main.conexion.EjecutarEscalarConcurrente(" select DATE_SUB(CURDATE(), INTERVAL " + x + "  day)");
                    try {
                        EnviarVentas(fecha, iddoc);
                    } catch (Exception y) {
                        System.out.print(y.toString());
                    }
                    try {
                        EnviarVentasCliente(fecha, iddoc);
                    } catch (Exception y) {
                        System.out.print(y.toString());
                    }
                }
            }
            System.out.println("FIN CUBO");
        } catch (NumberFormatException x) {
            System.out.println("Cubo: " + x.toString());
        }
    }

    private void EnviarVentas(String fecha, String iddocumento) {
        int dia = 0;
        venta = new EncabezadoVentas();
        List<DetalleVenta> det = new ArrayList<>();
        try {
            String sql
                    = " SELECT    \n"
                    + "    DAY(f.fecha) dia,\n"
                    + "    MONTH(f.fecha) mes,\n"
                    + "    YEAR(f.fecha) ano,\n"
                    + "    HOUR(f.fecha) Hora,\n"
                    + "	   a.id IdArticulo,\n"
                    + "    doc.serie,\n"
                    + "    SUM(o.cantidad) cantidad,    \n"
                    + "    sum(o.monto) monto\n"
                    // Usuario de cajero y mesero
                    + "    ,'' Mesero "
                    + "    ,'' Cajero "
                    + " FROM\n"
                    + "    pos_factura f\n"
                    + "        INNER JOIN  pos_ordendetalle o ON f.idfactura = o.idfactura  AND f.iddocumento = o.iddocumento\n"
                    + "        left JOIN  gen_articulo a ON o.idarticulo = a.idarticulo\n"
                    + "        INNER JOIN  pos_documento doc ON f.iddocumento = doc.iddocumento      \n"
                    // Usuario de cajero y mesero
                  //  + "     left join  pos_orden ord on o.idorden = ord.idorden "
                 //   + "     left join pos_usuario u on ord.idusuario = u.idusuario "
                //    + "     left join pos_usuario uu on f.idusuario = uu.idusuario  "
                    + " WHERE\n"
                    + "    DATE(f.fecha) = '" + fecha + "'\n"
                    + "        AND f.estado = 1"
                    + " and doc.iddocumento = '" + iddocumento + "'  \n "
                    + " GROUP BY doc.iddocumento , a.idarticulo , YEAR(f.fecha), MONTH(f.fecha), DAY(f.fecha) ,HOUR(f.fecha),a.id,doc.serie   \n"
                    + " ORDER BY doc.serie , HOUR(f.fecha)";
            ResultSet rs = Main.conexion.EjecutarConsulta(sql);
            if (rs != null) {
                while (rs.next()) {
                    if (dia == 0) {
                        venta.setDia(rs.getInt("dia"));
                        venta.setMes(rs.getInt("mes"));
                        venta.setAno(rs.getInt("ano"));
                        dia = rs.getInt("dia");
                    }
                    DetalleVenta _detalle = new DetalleVenta(rs.getInt("IdArticulo"), rs.getString("serie"), rs.getDouble("cantidad"), rs.getDouble("monto"), rs.getInt("Hora"), rs.getString("Mesero"), rs.getString("Cajero"));
                    det.add(_detalle);
                }
            }
            rs.close();
            venta.setDetalle(det);

            Gson gson = new Gson();
            String json = gson.toJson(venta);
            String a = resumenVenta(json);
            if (a != null) {
                System.err.println(a);
            }
        } catch (SQLException xx) {
            System.out.println("excepcion resumen ventas: " + xx.toString());
        }
    }

    private void EnviarVentasCliente(String fecha, String iddocumento) {
        int dia = 0;
        venta = new EncabezadoVentas();
        List<DetalleVentaCliente> det = new ArrayList<>();
        try {
            String sql
                    = " SELECT    \n"
                    + "   DAY(f.fecha) dia,\n"
                    + "  MONTH(f.fecha) mes,\n"
                    + "  YEAR(f.fecha) ano,\n"
                    + "  HOUR(f.fecha) Hora,\n"
                    + "    doc.serie,\n"
                    + "   count(f.idfactura) Cantidad,    \n"
                    + "   sum(f.total) Monto\n"
                    + " FROM\n"
                    + "  pos_factura f   \n"
                    + "       INNER JOIN  pos_documento doc ON f.iddocumento = doc.iddocumento      \n"
                    + " WHERE\n"
                    + "   DATE(f.fecha) = '" + fecha + "'\n"
                    + "      AND f.estado = 1 "
                    + "   and doc.iddocumento = '" + iddocumento + "'\n"
                    + "  group by doc.iddocumento,  YEAR(f.fecha),  MONTH(f.fecha),DAY(f.fecha), hour(f.fecha) ,doc.serie \n"
                    + " ORDER BY doc.serie , HOUR(f.fecha);";

            ResultSet rs = Main.conexion.EjecutarConsulta(sql);
            if (rs != null) {
                while (rs.next()) {
                    if (dia == 0) {
                        venta.setDia(rs.getInt("dia"));
                        venta.setMes(rs.getInt("mes"));
                        venta.setAno(rs.getInt("ano"));
                        dia = rs.getInt("dia");
                    }
                    DetalleVentaCliente _detalle = new DetalleVentaCliente(rs.getString("serie"), rs.getInt("cantidad"), rs.getDouble("monto"), rs.getInt("Hora"));
                    det.add(_detalle);
                }
            }
            rs.close();
            venta.setDetalleCliente(det);

            Gson gson = new Gson();
            String json = gson.toJson(venta);

            String a = resumenVenta(json);
            if (a != null) {
                System.err.println(a);
            }
        } catch (Exception xx) {
            System.out.println("excepcion resumen ventas: " + xx.toString());
        }
    }

    private static String resumenVenta(java.lang.String json) {
        try {
            org.tempuri.Pos service = new org.tempuri.Pos();
            org.tempuri.PosSoap port = service.getPosSoap();
            return port.resumenVenta(json);
        } catch (Exception x) {
            System.out.println("excepcio ws: " + x.toString());
            return "";
        }
    }
    // </editor-fold>    

// <editor-fold defaultstate="collapsed" desc="Query detalle">
    private String qryDetalle(int del, int al, boolean esRevision, String tipocorte, int panaderia, String iddocumento) {
        String transferido = "";
        if (panaderia == 1) {
            transferido = " AND f.transferida is null ";
        }
        if (esRevision && tipocorte.equals("P")) {
            transferido = "";
        }

        String qry = "SELECT \n"
                + "    (sum(art.cantidad)) cantidad,\n"
                + "    art.nombre,\n"
                + "    sum(art.monto) monto,\n"
                + "    art.idarticulo    \n"
                + " FROM \n"
                + "    (SELECT DISTINCT \n"
                + "        IFNULL(SUM(o.cantidad), 0) cantidad, \n"
                + "            a.nombre, \n"
                + "            ROUND(IFNULL(SUM(o.monto), 0), 2) Monto,\n"
                + "            a.idarticulo \n"
                + "    FROM \n"
                + "        pos_factura f \n"
                + "    INNER JOIN pos_ordendetalle o ON f.idfactura = o.idfactura \n"
                + "        AND o.iddocumento = f.iddocumento \n"
                + "    INNER JOIN gen_articulo a ON o.idarticulo = a.idarticulo \n"
                + "    WHERE \n"
                + "        f.iddocumento = '" + iddocumento + "' \n"
                + "            AND o.iddocumento = '" + iddocumento + "' \n"
                + "            AND f.idfactura BETWEEN " + del + " AND " + al + " \n"
                + "            AND f.estado = 1 \n"
                + "           " + transferido + "  "
                + "            AND o.debaja is null"
                + "     GROUP BY a.nombre , a.idarticulo \n"
                + "    \n"
                + "    UNION ALL SELECT \n"
                + "        COUNT(a.idarticulo) cantidad,\n"
                + "            a.nombre,\n"
                + "            (a.precio * COUNT(a.idarticulo)) Monto,\n"
                + "            a.idarticulo \n"
                + "    FROM\n"
                + "        pos_ordendetalledetalle det\n"
                + "    INNER JOIN pos_opcion op ON det.idopcion = op.idopcion \n"
                + "    INNER JOIN gen_articulo a ON op.idarticulo = a.idarticulo \n"
                + "    INNER JOIN pos_ordendetalle od ON det.idorden_detalle = od.idorden_detalle \n"
                + "        AND det.iddocumento = od.iddocumento \n"
                + "    INNER JOIN pos_factura f ON od.idfactura = f.idfactura \n"
                + "        AND od.iddocumento = f.iddocumento \n"
                + "    WHERE \n"
                + "        det.debaja IS NULL AND op.debaja IS NULL \n"
                + "            AND a.debaja IS NULL \n"
                + "            AND f.idfactura BETWEEN " + del + " AND " + al + " \n"
                + "            AND f.estado = 1          \n"
                + "            AND od.iddocumento = '" + iddocumento + "' \n"
                + "            AND f.iddocumento = '" + iddocumento + "' \n"
                + "            AND det.iddocumento = '" + iddocumento + "' \n "
                + "            " + transferido + "  "
                + "            AND od.debaja is null  "
                + "            AND det.debaja is null "
                + " GROUP BY a.idarticulo, a.nombre, a.precio) AS art "
                + " GROUP BY art.idarticulo,art.nombre; ";
        return qry;
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Envia de corte parcial y final consumos">
    public void EnviarCortePF() {
//        if (tipo.equals("P") && corte.getEsPanaderia()== 1) {

        try {
            List<Corte> cor = new ArrayList<>();
            //Envio de los cortes del día sin importar si ya fueron enviados

            ResultSet rs = Main.conexion.EjecutarConsulta("select iddocumento,  idcorteparcial,  min(idfactura) del, max(idfactura) al from  pos_factura \n"
                    + "                           where date(fecha) between (  DATE_SUB(curdate(), INTERVAL " + Main.settings.getDiasSincronizar() + " DAY)) and (date(now()))   \n"
                    + "                        and idcorteparcial is not null\n"
                    + "                        group by iddocumento, idcorteparcial\n"
                    + "                        order by iddocumento, idcorteparcial, del ");

            while (rs.next()) {//             
                Corte _detalle = new Corte(rs.getString("iddocumento"), rs.getString("idcorteparcial"), rs.getInt("del"), rs.getInt("al"));
                cor.add(_detalle);
            }
            rs.close();

            // recorriendo la clase
            for (int i = 0; i < cor.size(); i++) {
                EnvioDeCortesCafe(cor.get(i).getIdCorte(), true, cor.get(i).getDel(), cor.get(i).getAl());
            }
            cor.clear();

        } catch (SQLException x) {
        }

//        } else {
//            Hashtable ht = new Hashtable();
//            ResultSet rs = Main.conexion.EjecutarConsulta(" SELECT \n" +
//                    "    fecha, idcorte\n" +
//                    "FROM\n" +
//                    "    pos_corte\n" +
//                    "WHERE\n" +
//                    "    DATE(fecha) BETWEEN DATE_SUB((SELECT \n" +
//                    "                (MAX(DATE(fecha)))\n" +
//                    "            FROM\n" +
//                    "                pos_corte),\n" +
//                    "        INTERVAL "+Main.settings.getDiasSincronizar()+ " DAY) AND (SELECT \n" +
//                    "            (MAX(DATE(fecha)))\n" +
//                    "        FROM\n" +
//                    "            pos_corte)\n" +
//                    "group by idcorte, datE(fecha)\n" +
//                    "ORDER BY fecha DESC , tipocorte DESC ");
//            try {
//                while (rs.next()) {
//                    ht.put(rs.getString(2), rs.getString(1));
//                }
//                Main.conexion.CerrarConexion();
//                rs.close();
//            } catch (SQLException ex) {
//            }
//            //Invocando el webservice
//            String respuesta = "", error = "";
//            Enumeration e = ht.keys();
//            Object clave;
//
//            while (e.hasMoreElements()) {
//                clave = e.nextElement();
//                if (!clave.equals("")) {
//                    respuesta = EnvioDeCortesCafe(clave.toString(), false, 0, 0);
//                    if (!respuesta.equals("")) {
//                        error += respuesta;
//                    }
//                }
//            }
//            ht.clear();
//            Main.settings.setIdCorte("");
//        }
    }

// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Envia de corte parcial y final consumos DETALLE DETALLE">
    private String EnvioDeCortesCafe(String IdCorte, boolean esRevision, int del, int al) {
        corte = new Entidad_Corte();
        List<DetalleCorte> det = new ArrayList<>();
        ResultSet rs = null;
        boolean existeCorte = false;
        try {
            rs = Main.conexion.EjecutarConsulta("SELECT \n"
                    + "    c.iddocumento,\n"
                    + "    c.fecha,\n"
                    + "    c.idcorte,\n"
                    + "    c.nocorte,\n"
                    + "    c.tipocorte,\n"
                    + "    c.total,\n"
                    + "    c.TotalCajero,\n"
                    + "    c.totaldescuentos,\n"
                    + "    c.totalanulaciones,\n"
                    + "    c.TotalTarjeta,\n"
                    + "    c.TotalVales,\n"
                    + "    c.AnticiposAnulados,\n"
                    + "    c.AnticiposLiquidados,\n"
                    + "    c.AnticiposRecibidos,\n"
                    + "    c.AnticiposSaldo,\n"
                    + "    c.del,\n"
                    + "    c.al,\n"
                    + "    c.totaldescuentovale,\n"
                    + "    c.AnticiposTC,\n"
                    + "    c.CantidadVales,\n"
                    + "    c.faltante,\n"
                    + "    u.nombre UsuarioAutoriza, \n"
                    + "    if (t.idtipotienda='23bfa661-7d2f-11e3-a48a-782bcb2714b9', '0','1') tienda "
                    + "  FROM\n"
                    + "    pos_corte c\n"
                    + "        INNER JOIN\n"
                    + "    pos_usuario u ON u.idusuario = c.idusuario "
                    + "   INNER JOIN\n"
                    + "    pos_documento d ON c.iddocumento = d.iddocumento\n"
                    + "        INNER JOIN\n"
                    + "    pos_caja ca ON d.idcaja = ca.idcaja\n"
                    + "        INNER JOIN\n"
                    + "    pos_tienda t ON ca.idtienda = t.idtienda\n"
                    + " where c.idcorte = '" + IdCorte + "'");

            if (rs != null) {
                if (rs.next()) {
                    corte.setIdResolucion(rs.getString("iddocumento"));
                    corte.setFecha(rs.getString("fecha"));
                    corte.setIdCorte(rs.getString("idcorte"));
                    corte.setNoCorte(rs.getInt("nocorte"));
                    corte.setTipoCorte(rs.getString("tipocorte"));
                    corte.setTotal(rs.getDouble("total"));
                    corte.setEfectivo(rs.getDouble("TotalCajero"));
                    corte.setDescuento(rs.getDouble("totaldescuentos"));
                    corte.setAnulado(rs.getDouble("totalanulaciones"));
                    corte.setTarjeta(rs.getDouble("TotalTarjeta"));
                    corte.setVales(rs.getDouble("TotalVales"));
                    corte.setAnticipoAnulado(rs.getDouble("AnticiposAnulados"));
                    corte.setAnticipoLiquidado(rs.getDouble("AnticiposLiquidados"));
                    corte.setAnticipoRecibido(rs.getDouble("AnticiposRecibidos"));
                    corte.setAnticipoSaldo(rs.getDouble("AnticiposSaldo"));
                    corte.setDel(rs.getInt("del"));
                    corte.setAl(rs.getInt("al"));
                    corte.setTotalDescuentoVale(rs.getDouble("totaldescuentovale"));
                    corte.setAnticiposTC(rs.getDouble("AnticiposTC"));
                    corte.setCantidadVales(rs.getInt("CantidadVales"));
                    corte.setFaltante(rs.getDouble("faltante"));
                    corte.setUsuarioAutoriza(rs.getString("UsuarioAutoriza"));
                    corte.setEsPanaderia(rs.getInt("tienda"));
                    existeCorte = true;
                }
            }
            rs.close();
            if (existeCorte) {
                if (corte.getTipoCorte().equals("P")) {
                    String transferido = "";
                    if (!esRevision) {
                        transferido = " AND transferida is null ";
                    } else {
                        corte.setDel(del);
                        corte.setAl(al);
                    }
                    corte.setRfs(Integer.valueOf(Main.conexion.EjecutarEscalar("SELECT COUNT(*) FROM pos_factura WHERE estado = 2  AND iddocumento = '" + corte.getIdResolucion() + "'\n"
                            + "    and idfactura between " + corte.getDel() + " and " + corte.getAl() + "     " + transferido + "  ")));
                } else {
                    corte.setRfs(Integer.valueOf(Main.conexion.EjecutarEscalar("SELECT COUNT(*) FROM pos_factura WHERE estado = 2  AND iddocumento = '" + corte.getIdResolucion() + "'\n"
                            + "    and idfactura between " + corte.getDel() + " and " + corte.getAl() + "  ")));
                    corte.setRecargoTC(Double.valueOf(Main.conexion.EjecutarEscalar("select ifnull(sum(recargotc),0) from pos_factura"
                            + " where idfactura between " + corte.getDel() + " and " + corte.getAl() + " and iddocumento =  '" + corte.getIdResolucion() + "' ")));

                    // Clientes excentos y no exentos
                    rs = Main.conexion.EjecutarConsulta("select \n"
                            + "	if(f.estado=1,1,2) estado,\n"
                            + "     sum(f.total) total,\n"
                            + "    if(c.institucional in (0,1),0,1) exento\n"
                            + "    from pos_factura f\n"
                            + " inner join gen_clientes c on f.idcliente = c.idcliente\n"
                            + " where date(f.fecha)=date('" + corte.getFecha() + "') and f.iddocumento = '" + corte.getIdResolucion() + "'\n"
                            + " group by if(f.estado=1,1,2), if(c.institucional in (0,1),0,1)\n"
                            + " order by 1,3");
                    while (rs.next()) {
                        // Clientes no excentos Emitidos
                        if (rs.getInt("estado") == 1 && rs.getInt("exento") == 0) {
                            corte.setClienteEmitido(rs.getDouble("total"));
                        } // Clientes Exentos Emitidos
                        else if (rs.getInt("estado") == 1 && rs.getInt("exento") == 1) {
                            corte.setClienteEmitidoExcento(rs.getDouble("total"));
                        } // Clientes No exentos anulados
                        else if (rs.getInt("estado") == 2 && rs.getInt("exento") == 0) {
                            corte.setClienteAnulado(rs.getDouble("total"));
                        } // Clientes Exentos Anulados
                        else if (rs.getInt("estado") == 2 && rs.getInt("exento") == 1) {
                            corte.setClienteAnuladoExcento(rs.getDouble("total"));
                        }
                    }
                    rs.close();
                    //Ventas de vienes y servicios
                    rs = Main.conexion.EjecutarConsulta("select 	\n"
                            + "    if(f.estado=1,1,2) estado,\n"
                            + "    if(t.tipoarticulo='Bien','B','S') tipoarticulo   ,\n"
                            + "    sum(d.monto) monto\n"
                            + "    from pos_factura f\n"
                            + " inner join pos_ordendetalle d on f.idfactura = d.idfactura and f.iddocumento = d.iddocumento\n"
                            + " inner join gen_articulo a on d.idarticulo = a.idarticulo\n"
                            + " inner join gen_tipoarticulo t on a.idtipoarticulo = t.idtipoarticulo\n"
                            + " where date(f.fecha)=date('" + corte.getFecha() + "') and f.iddocumento = '" + corte.getIdResolucion() + "'and f.estado in (1,2) \n"
                            + " group by  if(f.estado=1,1,2), if(t.tipoarticulo='Bien','B','S')\n"
                            + " order by 2, 1 desc");
                    while (rs.next()) {
                        // Bienes Anulados
                        if (rs.getInt("estado") == 2 && rs.getString("tipoarticulo").equals("B")) {
                            corte.setBienesAnulados(rs.getDouble("monto"));
                        } // Bienes emitidos
                        else if (rs.getInt("estado") == 1 && rs.getString("tipoarticulo").equals("B")) {
                            corte.setBienesEmitidos(rs.getDouble("monto"));
                        } //Servicios Anulados
                        else if (rs.getInt("estado") == 2 && rs.getString("tipoarticulo").equals("S")) {
                            corte.setServiciosAnulados(rs.getDouble("monto"));
                        } // Servicios Emitidos
                        else if (rs.getInt("estado") == 1 && rs.getString("tipoarticulo").equals("S")) {
                            corte.setServiciosEmitidos(rs.getDouble("monto"));
                        }
                    }
                    rs.close();
                }

            } else {
                System.out.println("No existe corte ");
            }
        } catch (Exception ex) {
            try {
                System.out.println("Error " + ex.toString());
                rs.close();

            } catch (Exception ex1) {
                System.out.println("Error " + ex1.toString());
            }
        }
        // Buscando el detalle del corte
        System.out.println(corte.getFecha());
        
        if (esRevision && corte.getTipoCorte().equals("F") && corte.getEsPanaderia() == 1) {
            rs = Main.conexion.EjecutarConsulta(qryDetalle(del, al, esRevision, "P", corte.getEsPanaderia(), corte.getIdResolucion()));
        } else {
            rs = Main.conexion.EjecutarConsulta(qryDetalle(corte.getDel(), corte.getAl(), esRevision, corte.getTipoCorte(), corte.getEsPanaderia(), corte.getIdResolucion()));
        }

        // rs = Main.conexion.EjecutarConsulta(qryDetalle(corte.getDel(), corte.getAl(), corte.getEsPanaderia(), corte.getIdDocumento()));
        try {
            while (rs.next()) {//             
                DetalleCorte _detalle = new DetalleCorte(rs.getString("idarticulo"), rs.getDouble("cantidad"));
                det.add(_detalle);
            }
            corte.setDetalle(det);
        } catch (Exception ex) {
            System.out.println("sincronizacion corte realizado  " + ex.toString());
        } finally {
            try {
                rs.close();
            } catch (Exception ex) {
                System.out.println("sincronizacion corte   " + ex.toString());
            }
        }
        //Fin detalle Tienda

        try {
            if (corte.getTipoCorte().equals("P") && corte.getEsPanaderia() == 0) {
                System.out.println("Es parcial cafeteria  ");
            } else {
                Gson gson = new Gson();
                String json = gson.toJson(corte);
                Mensaje = saldoTienda(json);
                if (!Mensaje.equals("")) {
                    System.out.println("Error  " + Mensaje);
                } else {
                    System.out.println("Aceptado  " + Mensaje);
                }
            }
        } catch (Exception e) {
            Mensaje = e.toString();
        }
        return Mensaje;
    }
    // </editor-fold> 

// <editor-fold defaultstate="collapsed" desc="Enviar informacion de los clientes NIT"> 
    public void EnviarCatalogoClientes() {
        try {
            ResultSet rs = Main.conexion.EjecutarConsulta("SELECT  \n"
                    + "            LOWER(c.idcliente) idcliente,\n"
                    + "            UPPER(c.nit) nit,\n"
                    + "            UPPER(c.nombre) nombre,\n"
                    + "            if(UPPER(c.direccion)='','CIUDAD',c.direccion) direccion,\n"
                    + "            ifnull(UPPER(c.correo),'') correo   \n"
                    + "        FROM\n"
                    + "            gen_clientes c\n"
                    + "            inner join pos_factura f on c.idcliente = f.idcliente            \n"
                    + "		where  (c.nit !='CF'  and c.nit != 'C.F.'  and c.nit != 'C/F' and c.nit != 'C.F' )\n"
                    + "         and c.idcliente != '' and c.nombre !=''\n"
                    + "         and date(f.fecha) >= (DATE_SUB(CURDATE(), INTERVAL 3  day))\n"
                    + "        group by \n"
                    + "         LOWER(c.idcliente) ,\n"
                    + "            UPPER(c.nit) ,\n"
                    + "            UPPER(c.nombre) ,\n"
                    + "            if(UPPER(c.direccion)='','CIUDAD',c.direccion) ,\n"
                    + "            ifnull(UPPER(c.correo),'') 		; ");

            ArrayList<DatosCliente> clientes = new ArrayList<DatosCliente>();
            DatosCliente registrocliente;
            if (rs != null) {
                while (rs.next()) {
                    registrocliente = new DatosCliente();
                    registrocliente.setId(rs.getString("idcliente"));
                    registrocliente.setNit(rs.getString("nit"));
                    registrocliente.setNombre(rs.getString("nombre"));
                    registrocliente.setDireccion(rs.getString("direccion"));
                    registrocliente.setCorreo(rs.getString("correo"));
                    clientes.add(registrocliente);
                }
            }
            rs.close();
            Gson gson = new Gson();
            String json = gson.toJson(clientes);
            insertarClientePos(json);

        } catch (Exception x) {
            System.out.printf(x.toString());
        }
    }

    private static String insertarClientePos(java.lang.String json) {
        try {
            org.tempuri.Pos service = new org.tempuri.Pos();
            org.tempuri.PosSoap port = service.getPosSoap();
            return port.insertarClientePos(json);
        } catch (Exception x) {
            return "Error";
        }
    }
    // </editor-fold> 

// <editor-fold defaultstate="collapsed" desc="Enviar Tiempo de Certificacion"> 
    public void EnviarTiempoCertificacion() {
        try {
            for (int x = 0; x <= Main.settings.getDiasSincronizar(); x++) {
                ResultSet rs = Main.conexion.EjecutarConsulta("SELECT \n"
                        + "	day(f.fecha) dia,\n"
                        + "    month(f.fecha) mes,\n"
                        + "    year(f.fecha) anyo,\n"
                        + "    f.idfactura,\n"
                        + "    d.serie,\n"
                        + "    time(f.fechacobro) horacobro,\n"
                        + "    IFNULL(time(f.fechafel), '') horafel,\n"
                        + "    TIME_TO_SEC(TIMEDIFF(f.fechafel, f.fechacobro)) segundos,\n"
                        + "    IFNULL(f.firmafel, '') firmafel,\n"
                        + "    IFNULL(f.numerodeacceso, '') numerodeacceso,\n"
                        + "    f.estado,\n"
                        + "    COUNT(det.idarticulo) detalles\n"
                        + "FROM\n"
                        + "    pos_factura f\n"
                        + "        INNER JOIN\n"
                        + "    pos_documento d ON f.iddocumento = d.iddocumento\n"
                        + "        INNER JOIN\n"
                        + "    pos_ordendetalle det ON f.idfactura = det.idfactura\n"
                        + "        AND det.iddocumento = f.iddocumento\n"
                        + "WHERE\n"
                        + "    DATE(f.fecha) = (DATE_SUB(CURDATE(), INTERVAL " + x + "  day))"
                        + "GROUP BY \n"
                        + "	day(f.fecha),\n"
                        + "    month(f.fecha) ,\n"
                        + "    year(f.fecha),\n"
                        + "    f.idfactura , \n"
                        + "    d.serie , \n"
                        + "    f.fechacobro , \n"
                        + "    f.fechafel ,\n"
                        + "    f.estado , \n"
                        + "    f.firmafel , \n"
                        + "    f.numerodeacceso;\n"
                        + "     ");

                ArrayList<EntidadTiempoCertificacion> clientes = new ArrayList<>();
                EntidadTiempoCertificacion certificacion;
                while (rs.next()) {
                    certificacion = new EntidadTiempoCertificacion();
                    certificacion.setDia(rs.getInt("dia"));
                    certificacion.setMes(rs.getInt("mes"));
                    certificacion.setAnyo(rs.getInt("anyo"));
                    certificacion.setIdFactura(rs.getInt("idfactura"));
                    certificacion.setSerie(rs.getString("serie"));
                    certificacion.setHoraCobro(rs.getString("horacobro"));
                    certificacion.setHoraFel(rs.getString("horafel"));
                    certificacion.setSegundos(rs.getInt("segundos"));
                    certificacion.setFirma(rs.getString("firmafel"));
                    certificacion.setNumeroAcceso(rs.getString("numerodeacceso"));
                    certificacion.setEstado(rs.getInt("estado"));
                    certificacion.setCantidadDetalle(rs.getInt("detalles"));
                    clientes.add(certificacion);
                }
                rs.close();
                if (clientes.size() > 0) {
                    Gson gson = new Gson();
                    String json = gson.toJson(clientes);
                    insertarTiempoCertificacion(json);
                    clientes = new ArrayList<>();
                }
            }
        } catch (Exception x) {
            System.out.printf(x.toString());
        }
    }

    private static String insertarTiempoCertificacion(java.lang.String json) {
        try {
            org.tempuri.Pos service = new org.tempuri.Pos();
            org.tempuri.PosSoap port = service.getPosSoap();
            return port.insertarTiempoCertificacion(json);
        } catch (Exception x) {
            System.err.print(x.toString());
            return "";
        }
    }
    // </editor-fold> 

// <editor-fold defaultstate="collapsed" desc="Enviar Anticipos"> 
    public void EnviarAnticipos() {
        try {
            ResultSet rs = Main.conexion.EjecutarConsulta("SELECT \n"
                    + "    ti.codigo tienda,\n"
                    + "    d.serie,\n"
                    + "    a.codigoanticipo,\n"
                    + "    t.tipo,\n"
                    + "    m.fecha,\n"
                    + "    IFNULL(m.idfactura, '') nofactura,\n"
                    + "    m.monto,\n"
                    + "    u.nombre usuario\n"
                    + "FROM\n"
                    + "    mov_anticipo m\n"
                    + "        INNER JOIN  mov_anticipotipo t ON m.idtipomovanticipo = t.idtipo\n"
                    + "        INNER JOIN  pos_anticipos a ON m.idanticipo = a.idanticipo\n"
                    + "        INNER JOIN   pos_documento d ON a.iddocumento = d.iddocumento\n"
                    + "        INNER JOIN  pos_usuario u ON a.idusuario = u.idusuario\n"
                    + "        INNER JOIN  pos_caja c ON c.idcaja = d.idcaja\n"
                    + "        INNER JOIN  pos_tienda ti ON c.idtienda = ti.idtienda\n"
                    + "WHERE\n"
                    + "    DATE(m.fecha) >= (DATE_SUB(CURDATE(), INTERVAL " + Main.settings.getDiasSincronizar() + " DAY))\n"
                    + "ORDER BY d.serie , m.fecha ASC;");

            ArrayList<Entidad_Anticipos> anticipos = new ArrayList<>();
            Entidad_Anticipos ant;
            if (rs != null) {
                while (rs.next()) {
                    ant = new Entidad_Anticipos();
                    ant.setTienda(rs.getString("tienda"));
                    ant.setSerie(rs.getString("serie"));
                    ant.setCodigoanticipo(rs.getInt("codigoanticipo"));
                    ant.setTipo(rs.getString("tipo"));
                    ant.setFecha(rs.getString("fecha"));
                    ant.setNofactura(rs.getString("nofactura"));
                    ant.setMonto(rs.getDouble("monto"));
                    ant.setUsuario(rs.getString("usuario"));
                    anticipos.add(ant);
                }
            }
            rs.close();
            if (anticipos.size() > 0) {
                Gson gson = new Gson();
                String json = gson.toJson(anticipos);
                insertarAnticipos(json);

            }
        } catch (Exception x) {
            System.err.print(x.toString());
        }
    }

    private static String insertarAnticipos(java.lang.String json) {
        try {
            org.tempuri.Pos service = new org.tempuri.Pos();
            org.tempuri.PosSoap port = service.getPosSoap();
            return port.insertarAnticipos(json);
        } catch (Exception x) {
            System.err.print("Sincronizar Anticipos  " + x.toString());
            return "";
        }
    }
    // </editor-fold>  

// <editor-fold defaultstate="collapsed" desc="Enviar Ordenes de cafeteria"> 
//     Este metodo se unifico con el metodo EnviarOrdenDetalleDetalle
    public void EnviarOrdenCafe() {

        try {
            ResultSet rs = Main.conexion.EjecutarConsulta("select \n"
                    + "	t.codigo tienda, \n"
                    + "    doc.serie, \n"
                    + "    o.noorden, \n"
                    + "    f.idfactura nofactura, \n"
                    + "    f.fecha fechafactura, \n"
                    + "    o.fecha fechaorden, \n"
                    + "    f.fechacobro,  n"
                    + "    d.fechacocina, \n"
                    + "    d.cantidad, \n"
                    + "    a.id idarticulo,\n"
                    + "    a.nombre articulo, \n"
                    + "    d.precio, \n"
                    + "    d.idorden_detalle iddetalle, \n "
                    + "    u.nombre mesero "
                    + "from pos_orden o\n"
                    + "inner join  pos_ordendetalle d on o.idorden = d.idorden\n"
                    + "inner join gen_articulo a on d.idarticulo = a.idarticulo\n"
                    + "inner join pos_factura f on d.idfactura = f.idfactura and d.iddocumento = f.iddocumento\n"
                    + "inner join pos_documento doc on f.iddocumento = doc.iddocumento\n"
                    + "inner join pos_caja c on doc.idcaja = c.idcaja\n"
                    + "inner join pos_tienda t on c.idtienda = t.idtienda\n"
                    + "  left join pos_usuario u on o.idusuario = u.idusuario "
                    + "where date(f.fecha) between  date(DATE_SUB(CURDATE(), INTERVAL " + Main.settings.getDiasSincronizar() + " DAY)) and  date(now())\n"
                    + "and f.estado = 1 and d.debaja is null\n"
                    + " \n"
                    + " order by o.noorden asc");

            ArrayList<Entidad_OrdenCafe> orden = new ArrayList<>();
            Entidad_OrdenCafe ord;
            if (rs != null) {
                while (rs.next()) {
                    ord = new Entidad_OrdenCafe();
                    ord.setTienda(rs.getString("tienda"));
                    ord.setSerie(rs.getString("serie"));
                    ord.setNoorden(rs.getInt("noorden"));
                    ord.setNofactura(rs.getInt("nofactura"));
                    ord.setFechafactura(rs.getString("fechafactura"));
                    ord.setFechaorden(rs.getString("fechaorden"));
                    ord.setFechacobro(rs.getString("fechacobro"));
                    ord.setFechacocina(rs.getString("fechacocina"));
                    ord.setCantidad(rs.getDouble("cantidad"));
                    ord.setIdarticulo(rs.getInt("idarticulo"));
                    ord.setArticulo(rs.getString("articulo"));
                    ord.setPrecio(rs.getDouble("precio"));
                    ord.setIddetalle(rs.getString("iddetalle"));
                    ord.setMesero("mesero");
                    orden.add(ord);
                }
            }
            rs.close();
            if (orden.size() > 0) {
                Gson gson = new Gson();
                String json = gson.toJson(orden);
                String a = insertarOrdenesCafe(json);
            }
        } catch (Exception x) {
            System.err.print(x.toString());
        }

    }

    private static String insertarOrdenesCafe(java.lang.String json) {
        try {
            org.tempuri.Pos service = new org.tempuri.Pos();
            org.tempuri.PosSoap port = service.getPosSoap();
            return port.insertarOrdenesCafe(json);
        } catch (Exception x) {
            System.err.print(x.toString());
            return null;
        }
    }
//     </editor-fold>   

// <editor-fold defaultstate="collapsed" desc="Enviar Clientes al Cubo"> 
    public void EnviarClientesCubo() {
        try {
            ResultSet rs = Main.conexion.EjecutarConsulta("SELECT\n"
                    + " t.codigo tienda,\n"
                    + "    a.id idarticulo,\n"
                    + "    f.firmafel,\n"
                    + "    day(f.fecha) dia,\n"
                    + "    Month(f.fecha) mes,\n"
                    + "    year(f.fecha) ano,\n"
                    + "    HOUR(f.fecha) hora,\n"
                    + "    cli.nit,\n"
                    + "    cli.nombre cliente,\n"
                    + "    a.nombre articulo,\n"
                    + "    d.precio,\n"
                    + "    sum(d.cantidad) cantidad\n"
                    + " FROM\n"
                    + "    pos_factura f\n"
                    + "        INNER JOIN gen_clientes cli ON f.idcliente = cli.idcliente\n"
                    + "        INNER JOIN pos_documento doc ON f.iddocumento = doc.iddocumento\n"
                    + "        INNER JOIN pos_ordendetalle d ON f.idfactura = d.idfactura AND f.iddocumento = d.iddocumento\n"
                    + "        INNER JOIN gen_articulo a ON d.idarticulo = a.idarticulo\n"
                    + "        INNER JOIN pos_caja c ON doc.idcaja = c.idcaja\n"
                    + "        INNER JOIN pos_tienda t ON c.idtienda = t.idtienda\n"
                    + " WHERE\n"
                    + "    f.estado = 1 and f.firmafel is not null \n"
                    + "        AND DATE(f.fecha) between date(DATE_SUB(CURDATE(), INTERVAL " + Main.settings.getDiasSincronizar() + " DAY)) and  date(now())\n"
                    + "      "
                    + "group by\n"
                    + " t.codigo  ,\n"
                    + "    a.id  ,\n"
                    + "    f.firmafel,\n"
                    + "    day(f.fecha)  ,\n"
                    + "    Month(f.fecha)  ,\n"
                    + "    year(f.fecha)  ,\n"
                    + "    HOUR(f.fecha)  ,\n"
                    + "    cli.nit,\n"
                    + "    cli.nombre  ,\n"
                    + "    a.nombre  ,\n"
                    + "    d.precio  ");

            ArrayList<EntidadClienteCubo> clienteCubo = new ArrayList<>();
            EntidadClienteCubo acliente;
            if (rs != null) {
                while (rs.next()) {
                    acliente = new EntidadClienteCubo();
                    acliente.setTienda(rs.getString("tienda"));
                    acliente.setIdarticulo(rs.getInt("idarticulo"));
                    acliente.setFirmafel(rs.getString("firmafel"));
                    acliente.setDia(rs.getInt("dia"));
                    acliente.setMes(rs.getInt("mes"));
                    acliente.setAno(rs.getInt("ano"));
                    acliente.setHora(rs.getInt("hora"));
                    acliente.setNit(rs.getString("nit"));

                    acliente.setCliente(rs.getString("cliente"));
                    acliente.setArticulo(rs.getString("articulo"));
                    acliente.setPrecio(rs.getDouble("precio"));
                    acliente.setCantidad(rs.getDouble("cantidad"));
                    clienteCubo.add(acliente);
                }
                rs.close();
                if (clienteCubo.size() > 0) {
                    Gson gson = new Gson();
                    cuboCliente(gson.toJson(clienteCubo));

                }
            }
        } catch (Exception x) {
            System.err.print(x.toString());
        }
    }

    private static String cuboCliente(java.lang.String json) {
        try {
            org.tempuri.Pos service = new org.tempuri.Pos();
            org.tempuri.PosSoap port = service.getPosSoap();
            return port.cuboCliente(json);
        } catch (Exception x) {
            System.err.print("Error en cubo cliente " + x.toString());
            return "";
        }
    }

    // </editor-fold>    
    
// <editor-fold defaultstate="collapsed" desc="Enviar Enviar Orden Detalle Detalle"> 
    public void EnviarOrdenDetalleDetalle() {

        try {
            for (String iddoc : Main.settings.getSeries().keySet()) {
                for (int x = 0; x <= Main.settings.getDiasSincronizar(); x++) {
                    ResultSet rs = Main.conexion.EjecutarConsulta("select  dd.idorden_detalle detalle,\n"
                            + "    ifnull(ddd.idorden_detdet ,'') detdet,\n"
                            + "    t.codigo tienda,\n"
                            + "    d.serie,\n"
                            + "    f.idfactura nofactura,\n"
                            + "    o.noorden,\n"
                            + "    f.fecha,\n"
                            + "    SUM(dd.cantidad) cantidad,\n"
                            + "    a.nombre Menu,\n"
                            + "    ifnull(ar.nombre,'') Submenu, "
                            + "    uf.nombre cajero, "
                            + "    uo.nombre mesero "
                            + " from pos_factura f\n"
                            + " left join pos_documento d on f.iddocumento = d.iddocumento  \n"
                            + " left join pos_ordendetalle dd on f.idfactura = dd.idfactura and f.iddocumento = dd.iddocumento\n"
                            + " inner join pos_orden o on dd.idorden = o.idorden\n"
                            + " left join gen_articulo a on dd.idarticulo = a.idarticulo\n"
                            + " left join pos_ordendetalledetalle ddd on dd.idorden_detalle = ddd.idorden_detalle\n"
                            + " left join pos_opcion op on ddd.idopcion = op.idopcion\n"
                            + " left join gen_articulo ar on op.idarticulo = ar.idarticulo\n"
                            + " inner join pos_caja c on d.idcaja = c.idcaja\n"
                            + " inner join pos_tienda t on c.idtienda = t.idtienda\n"
                            + " left join pos_usuario uf on f.idusuario = uf.idusuario "
                            + " left join pos_usuario uo on o.idusuario =  uo.idusuario "
                            + " where \n"
                            + "    d.iddocumento = '" + iddoc + "' and \n"
                            + "f.estado = 1 and date(f.fecha)\n"
                            + " =  date(DATE_SUB(CURDATE(), INTERVAL " + x + " DAY))  \n"
                            + " group by\n"
                            + " dd.idorden_detalle, ddd.idorden_detdet, t.codigo  , d.serie, f.idfactura  , o.noorden,  f.fecha,   a.nombre  ,   ar.nombre, uf.nombre, uo.nombre    \n"
                            + " ;");

                    ArrayList<Entidad_menu_guarniciones> orden = new ArrayList<>();
                    Entidad_menu_guarniciones ord;
                    if (rs != null) {
                        while (rs.next()) {
                            ord = new Entidad_menu_guarniciones();
                            ord.setDetalle(rs.getString("detalle"));
                            ord.setDetdet(rs.getString("detdet"));
                            ord.setTienda(rs.getString("tienda"));
                            ord.setSerie(rs.getString("serie"));
                            ord.setNofactura(rs.getString("nofactura"));
                            ord.setNoorden(rs.getString("noorden"));
                            ord.setFecha(rs.getString("fecha"));
                            ord.setCantidad(rs.getDouble("cantidad"));
                            ord.setMenu(rs.getString("Menu"));
                            ord.setSubmenu(rs.getString("Submenu"));
                            ord.setCajero(rs.getString("cajero"));
                            ord.setMesero(rs.getString("mesero"));
                            orden.add(ord);
                        }
                    }
                    rs.close();
                    if (orden.size() > 0) {
                        Gson gson = new Gson();
                        String json = gson.toJson(orden);
                        String a = insertarOrdenCafeGuarnicion(json);
                    }
                }
            }
        } catch (Exception x) {
            System.err.print(x.toString());
        }

    }

    private static String insertarOrdenCafeGuarnicion(java.lang.String json) {
        try {
            org.tempuri.Pos service = new org.tempuri.Pos();
            org.tempuri.PosSoap port = service.getPosSoap();
            return port.insertarOrdenCafeGuarnicion(json);
        } catch (Exception x) {
            System.err.print(x.toString());
            return null;
        }
    }

    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="Enviar facturas anuladas"> 
    public void EnviarFacturasAnuladas() {
        try {
            ResultSet rs = Main.conexion.EjecutarConsulta("SELECT \n" +
                "    t.nombre tienda,\n" +
                "    d.serie,\n" +
                "    f.idfactura nofactura,\n " +
                "    f.fechaanulacion,\n" +
                "    f.fecha ," +
                "    f.total,\n" +
                "    f.comentario,\n" +
                "    f.descuento,\n" +
                "    f.recargotc,\n" +
                "    u.nombre usuario,\n" +
                "    u2.nombre jefe,\n" +
                "    f.firmafel\n" +
                "FROM\n" +
                "    pos_factura f\n" +
                "        INNER JOIN\n" +
                "    pos_documento d ON f.iddocumento = d.iddocumento\n" +
                "        INNER JOIN\n" +
                "    pos_usuario u ON f.idusuario = u.idusuario\n" +
                "        INNER JOIN\n" +
                "    pos_caja c ON c.idcaja = d.idcaja\n" +
                "        INNER JOIN\n" +
                "    pos_tienda t ON c.idtienda = t.idtienda\n" +
                "        LEFT JOIN\n" +
                "    pos_anulaciones a ON f.idfactura = a.idfactura\n" +
                "        AND f.iddocumento = a.iddocumento\n" +
                "        INNER JOIN\n" +
                "    pos_usuario u2 ON a.idusuario = u2.idusuario\n" +
                "WHERE\n" +
                "    DATE(f.fecha) between date(DATE_SUB(CURDATE(), INTERVAL " + Main.settings.getDiasSincronizar() + " DAY)) and  date(now())\n" +
                "        AND f.estado = 2");

            ArrayList<Entidad_anulacion> anulacion = new ArrayList<>();
            Entidad_anulacion anu;
            if (rs != null) {
                while (rs.next()) {
                    anu = new Entidad_anulacion();
                    anu.setTienda(rs.getString("tienda"));
                    anu.setSerie(rs.getString("serie"));
                    anu.setNofactura(rs.getInt("nofactura"));
                    anu.setTotal(rs.getDouble("total"));
                    anu.setComentario(rs.getString("comentario"));
                    anu.setDescuento(rs.getDouble("descuento"));
                    anu.setRecargotc(rs.getDouble("recargotc"));
                    anu.setUsuario(rs.getString("usuario"));
                    anu.setJefe(rs.getString("jefe"));
                    anu.setFirmafel(rs.getString("firmafel"));  
                    anu.setFecha(rs.getString("fecha"));
                    anu.setFechaanulacion(rs.getString("fechaanulacion")); 
                    anulacion.add(anu);
                }
            }
            rs.close();
            if (anulacion.size() > 0) {
                Gson gson = new Gson();
                String json = gson.toJson(anulacion);
                System.err.print(insertarFacturaAnulada(json));
            }
        } catch (Exception x) {
            System.err.print(x.toString());
        }

    }
    
    
    
    // </editor-fold>   

    private static String insertarFacturaAnulada(java.lang.String json) {
        try{
        org.tempuri.Pos service = new org.tempuri.Pos();
        org.tempuri.PosSoap port = service.getPosSoap();
        return port.insertarFacturaAnulada(json);
        }catch(Exception x){
        
        }
        return null;
    }

}
