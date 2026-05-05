package dao;

import model.Evenement;
import model.Inscription;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvenementDAOImpl implements EvenementDAO {

    public List<Evenement> listerTous() throws Exception {
        List<Evenement> liste = new ArrayList<>();
        String sql = "SELECT * FROM evenement ORDER BY id_evenement ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(mapperResultSet(rs));
            }
        }
        return liste;
    }

    public Evenement trouverParId(int idEvenement) throws Exception {
        String sql = "SELECT * FROM evenement WHERE id_evenement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEvenement);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapperResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public boolean ajouter(Evenement evenement) throws Exception {
        String sql = "INSERT INTO evenement (nom, horodatage, duree, lieu, description, type, nom_organisateur) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, evenement.getNom());

            ps.setTimestamp(2, java.sql.Timestamp.valueOf(evenement.getHorodatage()));

            ps.setInt(3, evenement.getDuree());
            ps.setString(4, evenement.getLieu());
            ps.setString(5, evenement.getDescription());
            ps.setString(6, evenement.getType());
            ps.setString(7, evenement.getNomOrganisateur());

            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean mettreAJour(Evenement evenement) throws Exception {
        String sql = "UPDATE evenement SET nom = ?, horodatage = ?, duree = ?, lieu = ?, description = ?, type = ?, nom_organisateur = ? " +
                "WHERE id_evenement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, evenement.getNom());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(evenement.getHorodatage()));
            ps.setInt(3, evenement.getDuree());
            ps.setString(4, evenement.getLieu());
            ps.setString(5, evenement.getDescription());
            ps.setString(6, evenement.getType());
            ps.setString(7, evenement.getNomOrganisateur());
            ps.setInt(8, evenement.getIdEvenement()); // Clause WHERE

            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean supprimer(int idEvenement) throws Exception {
        String sql = "DELETE FROM evenement WHERE id_evenement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEvenement);
            // executeUpdate() retourne le nombre de lignes supprimées
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean inscrireFanfaron(String nomUtilisateur, int idEvenement, String instrument, String statut) throws Exception {
        // Le fameux UPSERT de PostgreSQL : Insert si nouveau, Update si existe déjà !
        String sql = "INSERT INTO inscription_event (nom_utilisateur, id_evenement, instrument, statut) " +
                "VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (nom_utilisateur, id_evenement) " +
                "DO UPDATE SET instrument = EXCLUDED.instrument, statut = EXCLUDED.statut";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomUtilisateur);
            ps.setInt(2, idEvenement);
            ps.setString(3, instrument);
            ps.setString(4, statut);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Inscription obtenirInscription(String nomUtilisateur, int idEvenement) throws Exception {
        String sql = "SELECT * FROM inscription_event WHERE nom_utilisateur = ? AND id_evenement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomUtilisateur);
            ps.setInt(2, idEvenement);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapperInscription(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Inscription> listerInscriptionsParEvenement(int idEvenement) throws Exception {
        List<Inscription> liste = new ArrayList<>();
        // Tri exigé par le cahier des charges du projet (TP)
        String sql = "SELECT * FROM inscription_event WHERE id_evenement = ? ORDER BY instrument ASC, statut ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEvenement);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapperInscription(rs));
                }
            }
        }
        return liste;
    }

    /**
     * Transforme une ligne de résultat SQL en objet Java (Inscription).
     */
    private Inscription mapperInscription(ResultSet rs) throws SQLException {
        Inscription i = new Inscription();
        i.setNomUtilisateur(rs.getString("nom_utilisateur"));
        i.setIdEvenement(rs.getInt("id_evenement"));
        i.setInstrument(rs.getString("instrument"));
        i.setStatut(rs.getString("statut"));
        return i;
    }

    private Evenement mapperResultSet(ResultSet rs) throws SQLException {
        return new Evenement(
                rs.getInt("id_evenement"),
                rs.getString("nom"),
                rs.getTimestamp("horodatage").toLocalDateTime(),
                rs.getInt("duree"),
                rs.getString("lieu"),
                rs.getString("description"),
                rs.getString("type"),
                rs.getString("nom_organisateur")
        );
    }
}