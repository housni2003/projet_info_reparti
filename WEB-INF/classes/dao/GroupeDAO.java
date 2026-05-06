package dao;

import model.Commission;
import model.Pupitre;

import java.util.List;

/**
 * Interface DAO pour la gestion des groupes (Pupitres et Commissions).
 */
public interface GroupeDAO {

    /** Récupère tous les pupitres existants dans la base. */
    List<Pupitre> listerTousLesPupitres() throws Exception;

    /** Récupère toutes les commissions existantes dans la base. */
    List<Commission> listerToutesLesCommissions() throws Exception;

    /** Récupère les identifiants des pupitres choisis par un utilisateur. */
    List<Integer> getPupitresUtilisateur(String nomUtilisateur) throws Exception;

    /** Récupère les identifiants des commissions choisies par un utilisateur. */
    List<Integer> getCommissionsUtilisateur(String nomUtilisateur) throws Exception;

    /** Met à jour la liste complète des pupitres d'un utilisateur. */
    void mettreAJourPupitresUtilisateur(String nomUtilisateur, List<Integer> idPupitres) throws Exception;

    /** Met à jour la liste complète des commissions d'un utilisateur. */
    void mettreAJourCommissionsUtilisateur(String nomUtilisateur, List<Integer> idCommissions) throws Exception;

    void ajouterPupitre(Pupitre p) throws Exception;
    void modifierPupitre(Pupitre p) throws Exception;
    void supprimerPupitre(int id) throws Exception;
    Pupitre trouverPupitreParId(int id) throws Exception;

    void ajouterCommission(Commission c) throws Exception;
    void modifierCommission(Commission c) throws Exception;
    void supprimerCommission(int id) throws Exception;
    Commission trouverCommissionParId(int id) throws Exception;
}