/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cajero;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Administrador
 */
@WebService(serviceName = "CajeroWS")
public class CajeroWS {

    @WebMethod(operationName = "consulta_saldo")
    public String consulta_saldo(@WebParam(name = "numero_cuenta") String numero_cuenta, @WebParam(name = "clave") String clave) {
        GestorSQL consulta = new GestorSQL();
        if (consulta.isEstado_conexion()) {
            consulta.setNumero_cuenta(numero_cuenta);
            consulta.setClave(clave);
            if (consulta.verificar_usuario()) {
                return consulta.comprobar_saldo();
            }
        }
        return "";
    }

    @WebMethod(operationName = "realizar_retiro")
    public String realizar_retiro(@WebParam(name = "numero_cuenta") String numero_cuenta, @WebParam(name = "clave") String clave, @WebParam(name = "monto") String monto) {
        GestorSQL consulta = new GestorSQL();
        if (consulta.isEstado_conexion()) {
            consulta.setNumero_cuenta(numero_cuenta);
            consulta.setClave(clave);
            if (consulta.verificar_usuario()) {
                int salida = Integer.parseInt(monto);
                return consulta.realizar_retiro(salida);
            }
        }
        return "";
    }

    @WebMethod(operationName = "realizar_transferencia")
    public boolean realizar_transferencia(@WebParam(name = "numero_cuenta") String numero_cuenta, @WebParam(name = "clave") String clave, @WebParam(name = "monto") String monto, @WebParam(name = "codigo_cuenta_destino") String cuenta_destino) {
        GestorSQL consulta = new GestorSQL();
        if (consulta.isEstado_conexion()) {
            consulta.setNumero_cuenta(numero_cuenta);
            consulta.setClave(clave);
            if (consulta.verificar_usuario()) {
                int salida = Integer.parseInt(monto);
                return consulta.realizar_tranferencia(salida, cuenta_destino);
            }
        }
        return false;
    }

    @WebMethod(operationName = "realizar_consignacion")
    public boolean realizar_consignacion(@WebParam(name = "cuenta_destino") String cuenta_destino, @WebParam(name = "monto") String monto, @WebParam(name = "consignatario") String consignatario) {
        GestorSQL consulta = new GestorSQL();
        if (consulta.isEstado_conexion()) {
            consulta.setNumero_cuenta(cuenta_destino);
            int entrada = Integer.parseInt(monto);
            return consulta.realizar_consignacion(entrada);
        }
        return false;
    }
}
