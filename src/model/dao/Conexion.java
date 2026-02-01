package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Ajusta tu usuario y contraseña aquí
    private static final String URL = "jdbc:mysql://localhost:3306/megatienda";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConexion() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error de Conexión: " + e.getMessage());
            return null;
        }
    }
}