package Entites;

import java.util.Date;

public class Repas {
    private int id;
    private Date date;
    private String nom;
    private int nbRepas;
    private double cout;
    private double prix;

    public Repas() {

    }
    public Repas(int id, Date date, String nom, int nbRepas, double cout) {
        this.id = id;
        this.date = date;
        this.nom = nom;
        this.nbRepas = nbRepas;
        this.cout = cout;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbRepas() {
        return nbRepas;
    }

    public void setNbRepas(int nbRepas) {
        this.nbRepas = nbRepas;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
