package Entites;

public class Proposition {

    private int id;
    private String description;
    private String objet;
    private int idConsomateur;

    public Proposition(int id, String description, String objet, int idConsomateur) {
        this.id = id;
        this.description = description;
        this.objet = objet;
        this.idConsomateur = idConsomateur;
    }
    public Proposition(int id, String description, String objet) {
        this.description = description;
        this.objet = objet;
        this.idConsomateur = idConsomateur;
    }

    public Proposition(String description, String objet, int id) {
        this.description = description;
        this.objet = objet;
        this.id = id;
    }

    public Proposition() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setIdConsomateur(int idConsomateur) {
        this.idConsomateur = idConsomateur;
    }

    @Override
    public String toString() {
        return "Proposition{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", objet='" + objet + '\'' +
                ", idConsomateur=" + idConsomateur +
                '}';
    }
}
