package com.esprit.espritrestau.entities;

import com.esprit.espritrestau.entities.TPA;
import com.esprit.espritrestau.entities.Personne;

public class Consommateur extends Personne {
    private TPA type;

    // Existing constructors
    public Consommateur(TPA type) {
        this.type = type;
    }

    public Consommateur(int id, String nom, String prenom, int tel, String password, TPA type) {
        super(id, nom, prenom, tel, password);
        this.type = type;
    }

    public Consommateur(int id, String nom, String prenom, int tel, TPA type) {
        super(id, nom, prenom, tel);
        this.type = type;
    }

    // New constructor for your use case
    public Consommateur(int id, String nom, String prenom) {
        super(id, nom, prenom); // Call the superclass constructor
        this.type = null; // Set type to null or handle as needed
    }

    public TPA getType() {
        return type;
    }

    public void setType(TPA type) {
        this.type = type;
    }
<<<<<<< HEAD
}
=======
    @Override
    public String toString() {
        return getNom() + " " + getPrenom();
    }
}
>>>>>>> 49116bedd0d23f21793a754a55363b8d48ce5db3
