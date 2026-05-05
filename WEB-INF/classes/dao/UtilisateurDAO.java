package dao;

import model.Utilisateur;

import java.util.List;

/**
 * Interface DAO pour l'entité Utilisateur.
 * Définit le contrat des opérations de persistance.
 */
public interface UtilisateurDAO {

    /** Insère un nouvel utilisateur. Retourne true si succès. */
    boolean inserer(Utilisateur utilisateur) throws Exception;

    /** Cherche un utilisateur par son nom d'utilisateur. Retourne null si absent. */
    Utilisateur trouverParNom(String nomUtilisateur) throws Exception;

    /** Cherche un utilisateur par son email. Retourne null si absent. */
    Utilisateur trouverParEmail(String email) throws Exception;

    /** Met à jour la date de dernière connexion. */
    void mettreAJourDerniereConnexion(String nomUtilisateur) throws Exception;

    /** Vérifie si un nom d'utilisateur est déjà pris. */
    boolean nomUtilisateurExiste(String nomUtilisateur) throws Exception;

    /** Vérifie si un email est déjà utilisé. */
    boolean emailExiste(String email) throws Exception;

    /** Récupère la liste de tous les utilisateurs (pour l'affichage du tableau admin). */
    List<Utilisateur> listerTous() throws Exception;

    /** Supprime un utilisateur par son nom d'utilisateur. Retourne true si succès. */
    boolean supprimer(String nomUtilisateur) throws Exception;

    /** Met à jour les informations d'un utilisateur (notamment ses droits admin). */
    boolean mettreAJour(Utilisateur utilisateur) throws Exception;
}
