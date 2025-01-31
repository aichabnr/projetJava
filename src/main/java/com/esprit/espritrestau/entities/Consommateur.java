package com.esprit.espritrestau.entities;

import com.esprit.espritrestau.entities.TPA ;
import com.esprit.espritrestau.entities.Personne ;


public class Consommateur extends Personne{
private TPA type;

    public Consommateur(TPA type) {
        this.type = type;
    }
    public Consommateur(int id, String nom, String prenom, String  tel, String password) {
        super(id, nom, prenom, tel, password);
    }


    public Consommateur(int id, String nom, String prenom, String  tel, String password, TPA type) {
        super(id, nom, prenom, tel, password);
        this.type = type;
    }

    public Consommateur(int id, String nom, String prenom, String  tel, TPA type) {
        super(id, nom, prenom, tel);
        this.type = type;
    }



    public TPA getType() {
        return type;
    }

    public void setType(TPA type) {
        this.type = type;
    }
}
