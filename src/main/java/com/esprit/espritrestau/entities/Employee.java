package com.esprit.espritrestau.entities;

public class Employee extends Personne {

    private String post;
    private String matriculeSocial;

    public Employee() {
    }

    public Employee(String post, String matriculeSocial) {
        this.post = post;
        this.matriculeSocial = matriculeSocial;
    }

    public Employee(int id, String nom, String prenom, String tel, String post, String matriculeSocial) {
        super(id, nom, prenom, tel);
        this.post = post;
        this.matriculeSocial = matriculeSocial;
    }

    public Employee(int id, String nom, String prenom, String tel, String password, String post, String matriculeSocial) {
        super(id, nom, prenom, tel, password);
        this.post = post;
        this.matriculeSocial = matriculeSocial;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getMatriculeSocial() {
        return matriculeSocial;
    }

    public void setMatriculeSocial(String matriculeSocial) {
        this.matriculeSocial = matriculeSocial;
    }
}