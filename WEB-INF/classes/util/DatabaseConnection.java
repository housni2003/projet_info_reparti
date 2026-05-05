package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilitaire de connexion JDBC à la base MySQL fanfarehub.
 * Centralise les paramètres de connexion en un seul endroit.
 */
public class DatabaseConnection {

    private static final String URL      = "jdbc:postgresql://localhost:5432/fanfarehub_db";
    private static final String USER     = "fanfare_user";
    private static final String PASSWORD = "motDePasse";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Driver PostgreSQL introuvable : " + e.getMessage());
        }
    }

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
