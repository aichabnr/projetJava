package Entites;

import java.util.Date;

public class Reclamation {
    private int id;
    private String date;
    private String description;
    private String objet;
    private int idConsomateur;


    public Reclamation() {

    }

    public Reclamation(int id, String date, String description, String objet, int idConsomateur) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.objet = objet;
        this.idConsomateur = idConsomateur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setIdConsomateur(int idConsomateur) {
        this.idConsomateur = idConsomateur;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public int getIdConsomateur() {
        return idConsomateur;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", objet='" + objet + '\'' +
                ", idConsomateur=" + idConsomateur +
                '}';
    }
}
