/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Operaciones;

import WebService.ActualizarPrecios;
import WebService.ActualizarUsuarios;
import WebService.EnviarCorte;

/**
 *
 * @author usuario
 */
public class InicioSincronizacion {

    EnviarCorte c;
    ActualizarPrecios P;
    ActualizarUsuarios ac;

    public InicioSincronizacion() {
        c = new EnviarCorte();
        P = new ActualizarPrecios();
        ac = new ActualizarUsuarios();
    }

    public void inicio() {
        
 
        try {
            try {
                c.EnviarCorteFinal();
                System.out.println("FIN Sincronizacion corte");
            } catch (Exception x) {
                System.out.println("Error Corte Final " + x.toString());
            }
            try {
                c.EnviarCortePF();
                System.out.println("FIN Sincronizacion corte");
            } catch (Exception x) {
                System.out.println("Error Corte Parcial y Final " + x.toString());
            }

            try {
                c.EnviarResumenVentas();
                System.out.println("FIN Sincronizacion resumen ventas ");
            } catch (Exception x) {
                System.out.println("Error resumen ventas " + x.toString());
            }
            try {
                c.EnviarAnticipos();
                System.out.println("FIN Sincronizacion anticipos ");
            } catch (Exception x) {
                System.out.println("Error anticipos " + x.toString());
            }
            try {
                c.EnviarFacturasAnuladas();
                System.out.println("FIN Sincronizacion Facturas anuladas");
            } catch (Exception x) {
                System.out.println("Error factura anulada " + x.toString());
            }
            //Envio de informacion 
            try {
                c.EnviarTiempoCertificacion();
                System.out.println("FIN Sincronizacion tiempo certificacion ");
            } catch (Exception x) {
                System.out.println("Error tiempo certificacion " + x.toString());
            }
            try {
                c.EnviarOrdenCafe();
                System.out.println("SFIN incronizacion orden cafe");
            } catch (Exception x) {
                System.out.println("Error orden cafe " + x.toString());
            }
            try {
                c.EnviarClientesCubo();
                System.out.println("FIN Sincronizacion cubo clientes ");
            } catch (Exception x) {
                System.out.println("Error cubo clientes " + x.toString());
            }
            try {
                c.EnviarOrdenDetalleDetalle();
                System.out.println("FIN Sincronizacion orden detalle detalle");
            } catch (Exception x) {
                System.out.println("Error orden detalle detalle " + x.toString());
            }
            try {
                c.EnviarCatalogoClientes();
                System.out.println("FIN Sincronizacion catalogo clientes ");
            } catch (Exception x) {
                System.out.println("Error catalogo clientes " + x.toString());
            }

//            Envio de puntos de clientes
//               c.EnviarCliente(); 
//            Actualizar Catalogos+
            try {
                P.ActualizarClientesWs();
                System.out.println("FIN Sincronizacion actualizar clientes");
            } catch (Exception x) {
                System.out.println("Error actulizar clientes " + x.toString());
            }
            try {
                ac.ActualizarUsuarios();
                System.out.println("FIN Sincronizacion actualizar usuarios");
            } catch (Exception x) {
                System.out.println("Error actualizar usuarios " + x.toString());
            }
            try {
                P.ActualizarCatalogos();
                System.out.println("FIN Sincronizacion actualizar catalogos");
            } catch (Exception x) {
                System.out.println("Error actualizar catalogos " + x.toString());
            }

            Mantenimiento();
        } catch (Exception x) {
            System.err.println("Error " + x.toString());
        }
    }

    private void Mantenimiento() {
        try {
            P.correrConsultas();
            P.LimpiarBD();
        } catch (Exception x) {
            System.out.println("Mantenimiento: " + x.toString());
        }
    }
}
