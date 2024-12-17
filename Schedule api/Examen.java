/**
 * importer les librairies standards fournies par Java
 */
import java.util.ArrayList;
import java.util.List;

/**
 * Class Examen: classe contenant les propriétés ainsi que les getters et setters reliés à un Examen.
 */
public class Examen {
    /**
     * Numéro, matière, crédits et type d'un cours car un examen fait parti d'un cours.
     * On va aussi créer une liste d'examen
     */
    private int numero;
    private String matiere;
    private int credits;
    private thtpexam sortecours;
    private List<ExamHoraire> examenList;

    /**
     * Type de cours: TH TP EXAM
     */
    public enum thtpexam {
        TH, TP, EXAM
    }

    /**
     * Cette fonction créé un ArrayList d'examens.
     */
    public Examen() {
        this.examenList = new ArrayList<>();
    }

    /**
     * Override le string property d'un cours
     * @return retourne l'affiche de l'exmamen
     */
    @Override
    public String toString() {
        return "Examen{" +
                "numero=" + numero +
                ", matiere='" + matiere + '\'' +
                ", credits=" + credits +
                ", sortecours=" + sortecours +
                '}';
    }
}
