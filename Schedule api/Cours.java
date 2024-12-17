/**
 * importer les librairies standards fournies par Java
 */
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Class Cours: classe contenant les propriétés ainsi que les getters et setters reliés à un Cours.
 */
public class Cours {
    /**
     * Numéro, matière, crédits et type d'un cours. On va aussi créer 2 listes: une pour les cours, une pour les examens.
     * On fait aussi appelle à la classe Examen.
     */
    private int numero;
    private String matiere;
    private int credits;
    private thtpexam sortecours;
    private List<CoursHoraire> coursHoraires;
    private List<ExamHoraire> examenHoraires;
    private Examen examen;

    /**
     * Type de cours: TH TP EXAM
     */
    public enum thtpexam {
        TH, TP, EXAM
    }

    /**
     * Cette fonction créera un cours avec les propriétés voulues.
     * @param numero numéro du cours
     * @param matiere matière du cours
     * @param credits crédits du cours
     * @param thtpexam type du cours
     * On créé aussi nos deux ArrayList
     */
    public Cours(int numero, String matiere, int credits, thtpexam thtpexam) {
        this.numero = numero;
        this.matiere = matiere;
        this.credits = credits;
        this.sortecours = thtpexam;
        this.coursHoraires = new ArrayList<>();
        this.examenHoraires = new ArrayList<>();
    }

    /**
     * Getter pour le numéro du cours
     * @return numéro du cours
     */
    public int getNumero() {
        return numero;
    }

    /**
     * setter pour le numéro du cours
     * @param numero numéro du cours
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * getter pour la matière du cours
     * @return matière du cours
     */
    public String getMatiere() {
        return matiere;
    }

    /**
     * Setter pour la matière du cours
     * @param matiere matière du cours
     */
    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    /**
     * Getter pour le nombre de crédits du cours
     * @return nombre de crédits du cours
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Setter pour le nombre de crédits du cours
     * @param credits nombre de crédits du cours
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Getter pour le type de cours
     * @return le type du cours
     */
    public thtpexam getSortecours() {
        return sortecours;
    }

    /**
     * setter pour le type de cours
     * @param sortecours typoe du cours
     */
    public void setSortecours(thtpexam sortecours) {
        this.sortecours = sortecours;
    }

    /**
     * getter pour HoraireCours
     * @return coursHoraires l'horaire du cours
     */
    public List<CoursHoraire> getHoraireCours() {
        return coursHoraires;
    }

    /**
     * getter pour ExamenHoraire
     * @return examenHoraires l'horaire de l'examen
     */
    public List<ExamHoraire> getExamenHoraire() {
        return examenHoraires;
    }

    /**
     * Getter pour la date d'un examen
     * @return null si examenHoraires est vide, la date sinon
     */
    public LocalDate getExamenDate() {
        if (examenHoraires.isEmpty()) {
            return null;
        }
        return examenHoraires.get(0).getDate();
    }

    /**
     * Setter pour CoursHoraire
     * @param coursHoraires cours Horaires
     */
    public void setCoursHoraire(List<CoursHoraire> coursHoraires) {
        this.coursHoraires = coursHoraires;
    }

    /**
     * Ajouter un cours à l'horaire
     * @param coursHoraire le cours à ajouter
     */
    public void ajouterCoursHoraire(CoursHoraire coursHoraire) {
        coursHoraires.add(coursHoraire);
    }

    /**
     * Ajouter un examen à l'horaire
     * @param examHoraire l'examen à ajouter
     */
    public void ajouterExamenHoraire(ExamHoraire examHoraire) {
        examenHoraires.add(examHoraire);
    }

    /**
     * Override le string property d'un cours
     * @return retourne l'affichage du cours
     */
    @Override
    public String toString() {
        return "Cours{" +
                "numéro=" + numero +
                ", matière='" + matiere + '\'' +
                ", crédits=" + credits +
                ", Type de cours=" + sortecours +
                '}';
    }
}