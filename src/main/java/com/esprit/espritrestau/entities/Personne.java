package com.esprit.espritrestau.entities;

import java.io.Serializable;

public class Personne implements Serializable {
    private int id;
    private String nom;
    private String prenom;
    private String  tel;
    private  String password;
    public Personne() {

    }
    public Personne(int id, String nom, String prenom, String  tel) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
    }

    public Personne(int id, String nom, String prenom, String  tel, String password) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String  getTel() {
        return tel;
    }

    public void setTel(String  tel) {
        this.tel = tel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
