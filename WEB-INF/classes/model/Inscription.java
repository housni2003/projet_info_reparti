package model;

public class Inscription {
    private String nomUtilisateur;
    private int idEvenement;
    private String instrument;
    private String statut;

    public Inscription() {}

    public Inscription(String nomUtilisateur, int idEvenement, String instrument, String statut) {
        this.nomUtilisateur = nomUtilisateur;
        this.idEvenement = idEvenement;
        this.instrument = instrument;
        this.statut = statut;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}