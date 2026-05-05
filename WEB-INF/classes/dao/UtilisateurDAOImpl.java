package dao;

import model.Utilisateur;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Implémentation JDBC du patron DAO pour l'entité Utilisateur.
 */
public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Override
    public boolean inserer(Utilisateur u) throws Exception {
        String sql = "INSERT INTO utilisateur " +
                     "(nom_utilisateur, email, mot_de_passe, prenom, nom, genre, contraintes_alimentaires) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNomUtilisateur());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getMotDePasse());
            ps.setString(4, u.getPrenom());
            ps.setString(5, u.getNom());
            ps.setString(6, u.getGenre());
            ps.setString(7, u.getContraintesAlimentaires());

            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public Utilisateur trouverParNom(String nomUtilisateur) throws Exception {
        String sql = "SELECT * FROM utilisateur WHERE nom_utilisateur = ?";

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
        String sql = "SELECT * FROM utilisateur WHERE email = ?";

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
        String sql = "UPDATE utilisateur SET derniere_connexion = NOW() WHERE nom_utilisateur = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomUtilisateur);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean nomUtilisateurExiste(String nomUtilisateur) throws Exception {
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE nom_utilisateur = ?";

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
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Mappe un ResultSet vers un objet Utilisateur
    private Utilisateur mapperResultSet(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setId(rs.getInt("id"));
        u.setNomUtilisateur(rs.getString("nom_utilisateur"));
        u.setEmail(rs.getString("email"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setPrenom(rs.getString("prenom"));
        u.setNom(rs.getString("nom"));
        u.setGenre(rs.getString("genre"));
        u.setContraintesAlimentaires(rs.getString("contraintes_alimentaires"));

        Timestamp di = rs.getTimestamp("date_inscription");
        if (di != null) u.setDateInscription(di.toLocalDateTime());

        Timestamp dc = rs.getTimestamp("derniere_connexion");
        if (dc != null) u.setDerniereConnexion(dc.toLocalDateTime());

        return u;
    }
}
