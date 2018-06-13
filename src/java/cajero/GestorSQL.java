/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cajero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Administrador
 */
public class GestorSQL {

    private String numero_cuenta, clave, consignatario;
    private Connection conexion;
    private boolean estado_conexion;

    public GestorSQL() {
        conectar();
    }

    private void conectar() {
        conexion = new Conexion("root", "", "bancodb").tryConection();
        estado_conexion = conexion != null;
    }

    public String realizar_retiro(int monto) {
        try {
            String query = "select saldo_cuenta from cuentas where numero_cuenta='" + numero_cuenta + "'";
            Statement s = conexion.createStatement();
            ResultSet resultado = s.executeQuery(query);
            if (resultado.next()) {
                int saldo = resultado.getInt("saldo_cuenta");
                if (saldo >= monto) {
                    String sql = "INSERT INTO `retiros`(`numero_cuenta_cuentas`,"
                            + "`fecha_retiro`,`monto_retiro`) "
                            + "VALUES (?,?,?)";
                    PreparedStatement pst = conexion.prepareStatement(sql);
                    Date d = new Date();
                    java.sql.Date date = new java.sql.Date(d.getTime());
                    pst.setString(1, numero_cuenta);
                    pst.setDate(2, date);
                    pst.setInt(3, monto);
                    if (pst.executeUpdate() == 1) {
                        return String.valueOf(monto);
                    }
                }
            }
        } catch (SQLException e) {
            return e.getMessage();
        }
        return "0";
    }

    public boolean realizar_tranferencia(int monto, String cuenta_destino) {
        try {
            String query = "select saldo_cuenta from cuentas where numero_cuenta='" + numero_cuenta + "'";
            Statement s = conexion.createStatement();
            ResultSet resultado = s.executeQuery(query);
            //Verificacion basica de cuenta origen
            if (resultado.next()) {
                int saldo = resultado.getInt("saldo_cuenta");
                //Verificacion del saldo disponible
                if (saldo >= monto) {
                    query = "select id_cuenta from cuentas where numero_cuenta='" + cuenta_destino + "'";
                    s = conexion.createStatement();
                    resultado = s.executeQuery(query);
                    //Verificacion si existe cuenta destino
                    if (resultado.next()) {
                        String sql = "INSERT INTO `transferencias`(`numero_cuenta_cuentas`,"
                                + "`fecha_transferencia`,`monto_transferencia`,"
                                + "`cuenta_destino`)"
                                + "VALUES (?,?,?,?)";
                        PreparedStatement pst = conexion.prepareStatement(sql);
                        Date d = new Date();
                        java.sql.Date date = new java.sql.Date(d.getTime());
                        pst.setString(1, numero_cuenta);
                        pst.setDate(2, date);
                        pst.setInt(3, monto);
                        pst.setString(4, cuenta_destino);
                        if (pst.executeUpdate() == 1) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean realizar_consignacion(int monto) {
        String query;
        Statement s;
        ResultSet resultado;
        try {

            query = "select id_cuenta from cuentas where numero_cuenta='" + numero_cuenta + "'";
            s = conexion.createStatement();
            resultado = s.executeQuery(query);
            //Verificacion si existe cuenta destino
            if (resultado.next()) {
                String sql = "INSERT INTO `consignaciones`(`numero_cuenta_cuentas`,"
                        + "`fecha_consignacion`,`monto_consignacion`,"
                        + "`consignatario`)"
                        + "VALUES (?,?,?,?)";
                PreparedStatement pst = conexion.prepareStatement(sql);
                Date d = new Date();
                java.sql.Date date = new java.sql.Date(d.getTime());
                pst.setString(1, numero_cuenta);
                pst.setDate(2, date);
                pst.setInt(3, monto);

                query = "select saldo_cuenta from cuentas where numero_cuenta='" + consignatario + "'";
                s = conexion.createStatement();
                resultado = s.executeQuery(query);
                //Verificacion basica de cuenta origen
                if (resultado.next()) {
                    pst.setString(4, consignatario);
                } else {
                    pst.setString(4, "Anónimo");
                }
                if (pst.executeUpdate() == 1) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public String comprobar_saldo() {
        try {
            String query = "select saldo_cuenta from cuentas where numero_cuenta='" + numero_cuenta + "'";
            Statement s = conexion.createStatement();
            ResultSet resultado = s.executeQuery(query);
            if (resultado.next()) {
                return resultado.getString("saldo_cuenta");
            }
            return "";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public boolean verificar_usuario() {
        try {
            String query = "select numero_cuenta, clave from cuentas where numero_cuenta='" + numero_cuenta + "' and clave=sha('" + clave + "')";
            Statement s = conexion.createStatement();
            ResultSet resultado = s.executeQuery(query);
            return resultado.next();
        } catch (SQLException e) {
            return false;
        }    
    }

    public boolean isEstado_conexion() {
        return estado_conexion;
    }

    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setConsignatario(String consignatario) {
        this.consignatario = consignatario;
    }
}
