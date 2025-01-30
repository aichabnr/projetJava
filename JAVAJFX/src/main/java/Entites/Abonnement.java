package Entites;

import java.util.Date;

public class Abonnement {

    private int id;
    private Date dateDebut;
    private Date dateFin;
    private double solde;
    private TPA type;
    private int idConsomateur;
    public Abonnement() {

    }
    public Abonnement(int id, Date dateDebut, Date dateFin, double solde, TPA type, int idConsomateur) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.solde = solde;
        this.type = type;
        this.idConsomateur = idConsomateur;
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

    public TPA getType() {
        return type;
    }

    public void setType(TPA type) {
        this.type = type;
    }

    public int getIdConsomateur() {
        return idConsomateur;
    }

    public void setIdConsomateur(int idConsomateur) {
        this.idConsomateur = idConsomateur;
    }
}
