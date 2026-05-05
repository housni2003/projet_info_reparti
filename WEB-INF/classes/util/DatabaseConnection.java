package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilitaire de connexion JDBC à la base MySQL fanfarehub.
 * Centralise les paramètres de connexion en un seul endroit.
 */
public class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/fanfarehub?useSSL=false&serverTimezone=Europe/Paris&characterEncoding=UTF-8";
    private static final String USER     = "root";
    private static final String PASSWORD = "";   // à adapter selon votre configuration

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Driver MySQL introuvable : " + e.getMessage());
        }
    }

    private DatabaseConnection() {}

    /** Retourne une nouvelle connexion JDBC. Doit être fermée par l'appelant. */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
