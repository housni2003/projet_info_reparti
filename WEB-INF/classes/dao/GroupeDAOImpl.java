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
}