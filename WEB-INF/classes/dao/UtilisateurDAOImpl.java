package dao;

import model.Utilisateur;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation JDBC du patron DAO pour l'entité Utilisateur.
 * Fait le lien avec la table `fanfaron` dans la base de données.
 */
public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Override
    public boolean inserer(Utilisateur u) throws Exception {
        String sql = "INSERT INTO fanfaron " +
                "(nom_utilisateur, email, mot_de_passe, prenom, nom, genre, contraintes_alimentaires, est_admin) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNomUtilisateur());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getMotDePasse());
            ps.setString(4, u.getPrenom());
            ps.setString(5, u.getNom());
            ps.setString(6, u.getGenre());
            ps.setString(7, u.getContraintesAlimentaires());
            ps.setBoolean(8, u.isEstAdmin());

            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public Utilisateur trouverParNom(String nomUtilisateur) throws Exception {
        String sql = "SELECT * FROM fanfaron WHERE nom_utilisateur = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomUtilisateur);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapperResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public Utilisateur trouverParEmail(String email) throws Exception {
        String sql = "SELECT * FROM fanfaron WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapperResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public void mettreAJourDerniereConnexion(String nomUtilisateur) throws Exception {
        String sql = "UPDATE fanfaron SET derniere_connexion = NOW() WHERE nom_utilisateur = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomUtilisateur);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean nomUtilisateurExiste(String nomUtilisateur) throws Exception {
        String sql = "SELECT COUNT(*) FROM fanfaron WHERE nom_utilisateur = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomUtilisateur);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public boolean emailExiste(String email) throws Exception {
        String sql = "SELECT COUNT(*) FROM fanfaron WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private Utilisateur mapperResultSet(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();

        u.setNomUtilisateur(rs.getString("nom_utilisateur"));
        u.setEmail(rs.getString("email"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setPrenom(rs.getString("prenom"));
        u.setNom(rs.getString("nom"));
        u.setGenre(rs.getString("genre"));
        u.setContraintesAlimentaires(rs.getString("contraintes_alimentaires"));
        u.setEstAdmin(rs.getBoolean("est_admin"));

        Timestamp dc = rs.getTimestamp("date_creation");
        if (dc != null) u.setDateCreation(dc.toLocalDateTime());

        Timestamp dconn = rs.getTimestamp("derniere_connexion");
        if (dconn != null) u.setDerniereConnexion(dconn.toLocalDateTime());

        return u;
    }

    @Override
    public List<Utilisateur> listerTous() throws Exception {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM fanfaron ORDER BY nom_utilisateur ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(mapperResultSet(rs));
            }
        }
        return liste;
    }

    @Override
    public boolean supprimer(String nomUtilisateur) throws Exception {
        String sql = "DELETE FROM fanfaron WHERE nom_utilisateur = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomUtilisateur);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean mettreAJour(Utilisateur u) throws Exception {
        String sql = "UPDATE fanfaron " +
                "SET email = ?, prenom = ?, nom = ?, genre = ?, contraintes_alimentaires = ?, est_admin = ? " +
                "WHERE nom_utilisateur = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getEmail());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getNom());
            ps.setString(4, u.getGenre());
            ps.setString(5, u.getContraintesAlimentaires());
            ps.setBoolean(6, u.isEstAdmin());
            ps.setString(7, u.getNomUtilisateur()); // Clause WHERE

            return ps.executeUpdate() == 1;
        }
    }
}