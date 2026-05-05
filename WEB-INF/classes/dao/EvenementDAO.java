package dao;

import model.Evenement;
import model.Inscription;

import java.util.List;

public interface EvenementDAO {

    /** Récupère la liste de tous les événements prévus. */
    List<Evenement> listerTous() throws Exception;

    /** Récupère un événement spécifique par son ID. */
    Evenement trouverParId(int idEvenement) throws Exception;

    /** Ajoute un nouvel événement en base. Retourne l'ID généré si possible. */
    boolean ajouter(Evenement evenement) throws Exception;

    /** Met à jour les informations (lieu, date, etc.) d'un événement. */
    boolean mettreAJour(Evenement evenement) throws Exception;

    /** Supprime un événement. */
    boolean supprimer(int idEvenement) throws Exception;

    /**
     * Ajoute ou met à jour l'inscription d'un fanfaron à un événement
     * (gère le statut et l'instrument).
     */
    boolean inscrireFanfaron(String nomUtilisateur, int idEvenement, String instrument, String statut) throws Exception;

    /** Récupère l'inscription spécifique d'un utilisateur pour un événement (pour pré-remplir son formulaire). */
    Inscription obtenirInscription(String nomUtilisateur, int idEvenement) throws Exception;

    /**
     * Récupère la liste de toutes les inscriptions pour un événement donné.
     * (Utile pour afficher le tableau des présents/absents exigé par le sujet).
     */
    List<Inscription> listerInscriptionsParEvenement(int idEvenement) throws Exception;
}