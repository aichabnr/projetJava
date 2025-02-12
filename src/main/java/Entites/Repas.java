package Entites;

public class Repas {
    private int id;
    private String date;
    private String nom;
    private int nbRepas;
    private double cout;

    public Repas() {

    }
    public Repas(int id, String date, String nom, int nbRepas, double cout) {
        this.id = id;
        this.date = date;
        this.nom = nom;
        this.nbRepas = nbRepas;
        this.cout = cout;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public int getRevenue() {
        return   this.nbRepas * 4 ;
    }
}
