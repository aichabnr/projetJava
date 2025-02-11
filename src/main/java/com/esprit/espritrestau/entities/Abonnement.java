package com.esprit.espritrestau.entities;

import java.util.Date;
import com.esprit.espritrestau.entities.TPA ;


public class Abonnement {

    private int id;
    private Date dateDebut;
    private Date dateFin;
    private double solde;
    private int idConsomateur;
    private String nomConsomateur;
    private String prenomConsomateur;
    public Abonnement(){

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

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
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
