/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cajero;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Administrador
 */
public class Conexion {
    private String driver, user, password, database, url;

    public Conexion(String u, String p, String db) {
        user = u;
        password = p;
        database = db;
    }

    public Connection tryConection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://localhost/"+database;
            Connection conection = DriverManager.getConnection(url,user,password);
            return conection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
