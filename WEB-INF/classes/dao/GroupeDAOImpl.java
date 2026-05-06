package dao;

import model.Commission;
import model.Pupitre;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GroupeDAOImpl implements GroupeDAO {

    @Override
    public List<Pupitre> listerTousLesPupitres() throws Exception {
        List<Pupitre> liste = new ArrayList<>();
        String sql = "SELECT * FROM pupitre ORDER BY nom_pupitre ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(new Pupitre(rs.getInt("id_pupitre"), rs.getString("nom_pupitre")));
            }
        }
        return liste;
    }

    @Override
    public List<Commission> listerToutesLesCommissions() throws Exception {
        List<Commission> liste = new ArrayList<>();
        String sql = "SELECT * FROM commission ORDER BY nom_commission ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(new Commission(rs.getInt("id_commission"), rs.getString("nom_commission")));
            }
        }
        return liste;
    }

    @Override
    public List<Integer> getPupitresUtilisateur(String nomUtilisateur) throws Exception {
        List<Integer> liste = new ArrayList<>();
        String sql = "SELECT id_pupitre FROM appartient_pupitre WHERE nom_utilisateur = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomUtilisateur);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(rs.getInt("id_pupitre"));
                }
            }
        }
        return liste;
    }

    @Override
    public List<Integer> getCommissionsUtilisateur(String nomUtilisateur) throws Exception {
        List<Integer> liste = new ArrayList<>();
        String sql = "SELECT id_commission FROM s_implique_commission WHERE nom_utilisateur = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomUtilisateur);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(rs.getInt("id_commission"));
                }
            }
        }
        return liste;
    }

    @Override
    public void mettreAJourPupitresUtilisateur(String nomUtilisateur, List<Integer> idPupitres) throws Exception {
        String sqlDelete = "DELETE FROM appartient_pupitre WHERE nom_utilisateur = ?";
        String sqlInsert = "INSERT INTO appartient_pupitre (nom_utilisateur, id_pupitre) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Début de la transaction

            try (PreparedStatement psDelete = conn.prepareStatement(sqlDelete)) {
                psDelete.setString(1, nomUtilisateur);
                psDelete.executeUpdate();
            }

            if (idPupitres != null && !idPupitres.isEmpty()) {
                try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                    for (Integer id : idPupitres) {
                        psInsert.setString(1, nomUtilisateur);
                        psInsert.setInt(2, id);
                        psInsert.addBatch();
                    }
                    psInsert.executeBatch();
                }
            }
            conn.commit();

        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void mettreAJourCommissionsUtilisateur(String nomUtilisateur, List<Integer> idCommissions) throws Exception {
        String sqlDelete = "DELETE FROM s_implique_commission WHERE nom_utilisateur = ?";
        String sqlInsert = "INSERT INTO s_implique_commission (nom_utilisateur, id_commission) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psDelete = conn.prepareStatement(sqlDelete)) {
                psDelete.setString(1, nomUtilisateur);
                psDelete.executeUpdate();
            }

            if (idCommissions != null && !idCommissions.isEmpty()) {
                try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                    for (Integer id : idCommissions) {
                        psInsert.setString(1, nomUtilisateur);
                        psInsert.setInt(2, id);
                        psInsert.addBatch();
                    }
                    psInsert.executeBatch();
                }
            }
            conn.commit();

        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void ajouterPupitre(Pupitre p) throws Exception {
        String sql = "INSERT INTO pupitre (nom_pupitre) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.executeUpdate();
        }
    }

    @Override
    public void modifierPupitre(Pupitre p) throws Exception {
        String sqlSelectOld = "SELECT nom_pupitre FROM pupitre WHERE id_pupitre = ?";
        String sqlUpdatePupitre = "UPDATE pupitre SET nom_pupitre = ? WHERE id_pupitre = ?";
        String sqlUpdateInscriptions = "UPDATE inscription_event SET instrument = ? WHERE instrument = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String ancienNom = null;
            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelectOld)) {
                psSelect.setInt(1, p.getId());
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        ancienNom = rs.getString("nom_pupitre");
                    }
                }
            }

            try (PreparedStatement psUpdatePupitre = conn.prepareStatement(sqlUpdatePupitre)) {
                psUpdatePupitre.setString(1, p.getNom());
                psUpdatePupitre.setInt(2, p.getId());
                psUpdatePupitre.executeUpdate();
            }

            if (ancienNom != null && !ancienNom.equals(p.getNom())) {
                try (PreparedStatement psUpdateInscr = conn.prepareStatement(sqlUpdateInscriptions)) {
                    psUpdateInscr.setString(1, p.getNom());
                    psUpdateInscr.setString(2, ancienNom);
                    psUpdateInscr.executeUpdate();
                }
            }

            conn.commit();

        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void supprimerPupitre(int id) throws Exception {
        String sqlSelectOld = "SELECT nom_pupitre FROM pupitre WHERE id_pupitre = ?";
        String sqlUpdateInscriptions = "UPDATE inscription_event SET instrument = NULL WHERE instrument = ?";
        String sqlDeletePupitre = "DELETE FROM pupitre WHERE id_pupitre = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String ancienNom = null;
            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelectOld)) {
                psSelect.setInt(1, id);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        ancienNom = rs.getString("nom_pupitre");
                    }
                }
            }

            if (ancienNom != null) {
                try (PreparedStatement psUpdateInscr = conn.prepareStatement(sqlUpdateInscriptions)) {
                    psUpdateInscr.setString(1, ancienNom);
                    psUpdateInscr.executeUpdate();
                }
            }

            try (PreparedStatement psDelete = conn.prepareStatement(sqlDeletePupitre)) {
                psDelete.setInt(1, id);
                psDelete.executeUpdate();
            }

            conn.commit();

        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public Pupitre trouverPupitreParId(int id) throws Exception {
        String sql = "SELECT id_pupitre, nom_pupitre FROM pupitre WHERE id_pupitre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pupitre(rs.getInt("id_pupitre"), rs.getString("nom_pupitre"));
                }
            }
        }
        return null;
    }

    @Override
    public void ajouterCommission(Commission c) throws Exception {
        String sql = "INSERT INTO commission (nom_commission) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNom());
            ps.executeUpdate();
        }
    }

    @Override
    public void modifierCommission(Commission c) throws Exception {
        String sql = "UPDATE commission SET nom_commission = ? WHERE id_commission = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNom());
            ps.setInt(2, c.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimerCommission(int id) throws Exception {
        String sql = "DELETE FROM commission WHERE id_commission = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Commission trouverCommissionParId(int id) throws Exception {
        String sql = "SELECT id_commission, nom_commission FROM commission WHERE id_commission = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Commission(rs.getInt("id_commission"), rs.getString("nom_commission"));
                }
            }
        }
        return null;
    }
}