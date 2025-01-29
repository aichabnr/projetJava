package com.esprit.espritrestau.entities;

import java.util.Date;

public class Presence {
    private int id;
    private Date date;
    private int idRepas;
    private int idConsomateur;
    public Presence() {

    }
    public Presence(int id, Date date, int idRepas, int idConsomateur) {
        this.id = id;
        this.date = date;
        this.idRepas = idRepas;
        this.idConsomateur = idConsomateur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdRepas() {
        return idRepas;
    }

    public void setIdRepas(int idRepas) {
        this.idRepas = idRepas;
    }

    public int getIdConsomateur() {
        return idConsomateur;
    }

    public void setIdConsomateur(int idConsomateur) {
        this.idConsomateur = idConsomateur;
    }
}
