package com.esprit.espritrestau.entities;

import java.sql.Date; // Use java.sql.Date

public class Abonnement {

    private int id;
    private Date dateDebut; // Change to java.sql.Date
    private Date dateFin;   // Change to java.sql.Date
    private double solde;
    private int idConsomateur;
    private String nomConsomateur;
    private String prenomConsomateur;

    public Abonnement() {
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "id=" + id +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", solde=" + solde +
                ", idConsomateur=" + idConsomateur +
                '}';
    }

    public Abonnement(int id, Date dateDebut, Date dateFin, double solde, int idConsomateur) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.solde = solde;
        this.idConsomateur = idConsomateur;
    }

    public Abonnement(int id, Date dateDebut, Date dateFin, double solde, String nomConsomateur, String prenomConsomateur) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.solde = solde;
        this.nomConsomateur = nomConsomateur;
        this.prenomConsomateur = prenomConsomateur;
    }

    public String getNomConsomateur() {
        return nomConsomateur;
    }

    public void setNomConsomateur(String nomConsomateur) {
        this.nomConsomateur = nomConsomateur;
    }

    public String getPrenomConsomateur() {
        return prenomConsomateur;
    }

    public void setPrenomConsomateur(String prenomConsomateur) {
        this.prenomConsomateur = prenomConsomateur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateDebut() { // Change return type
        return dateDebut; // No change needed here
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() { // Change return type
        return dateFin; // No change needed here
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public int getIdConsomateur() {
        return idConsomateur;
    }

    public void setIdConsomateur(int idConsomateur) {
        this.idConsomateur = idConsomateur;
    }
}