package model;

import java.time.LocalDateTime;

/**
 * Modèle (Bean) représentant un utilisateur de FanfareHub.
 * Correspond à la table `utilisateur` de la base de données.
 */
public class Utilisateur {

    private int           id;
    private String        nomUtilisateur;
    private String        email;
    private String        motDePasse;      // stocké hashé
    private String        prenom;
    private String        nom;
    private String        genre;
    private String        contraintesAlimentaires;
    private LocalDateTime dateInscription;
    private LocalDateTime derniereConnexion;

    public Utilisateur() {}

    public Utilisateur(String nomUtilisateur, String email, String motDePasse,
                       String prenom, String nom, String genre, String contraintesAlimentaires) {
        this.nomUtilisateur          = nomUtilisateur;
        this.email                   = email;
        this.motDePasse              = motDePasse;
        this.prenom                  = prenom;
        this.nom                     = nom;
        this.genre                   = genre;
        this.contraintesAlimentaires = contraintesAlimentaires;
    }

    // ---- Getters & Setters ----

    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }

    public String getNomUtilisateur()                    { return nomUtilisateur; }
    public void   setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }

    public String getEmail()               { return email; }
    public void   setEmail(String email)   { this.email = email; }

    public String getMotDePasse()                  { return motDePasse; }
    public void   setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getPrenom()               { return prenom; }
    public void   setPrenom(String prenom)  { this.prenom = prenom; }

    public String getNom()            { return nom; }
    public void   setNom(String nom)  { this.nom = nom; }

    public String getGenre()              { return genre; }
    public void   setGenre(String genre)  { this.genre = genre; }

    public String getContraintesAlimentaires()                           { return contraintesAlimentaires; }
    public void   setContraintesAlimentaires(String contraintesAlimentaires) { this.contraintesAlimentaires = contraintesAlimentaires; }

    public LocalDateTime getDateInscription()                        { return dateInscription; }
    public void          setDateInscription(LocalDateTime d)         { this.dateInscription = d; }

    public LocalDateTime getDerniereConnexion()                      { return derniereConnexion; }
    public void          setDerniereConnexion(LocalDateTime d)       { this.derniereConnexion = d; }
}
