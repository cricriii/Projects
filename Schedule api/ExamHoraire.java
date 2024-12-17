/**
 * importer les librairies standards fournies par Java
 */
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Class ExamHoraire: classe englobant les propriétés d'un examen dans un horaire
 */
public class ExamHoraire {
    /**
     * Variables date, heureDebut et heureFin reliés à un examen
     */
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    /**
     * Cette fonction va initialiser un examen
     * @param date date de l'examen
     * @param heureDebut heure de début de l'exam
     * @param heureFin heure de fin de l'exam
     */
    public ExamHoraire(LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    /**
     * Getter pour la date d'un examen
     * @return date de l'examen
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * getter pour l'heure de début d'un examen
     * @return heure de début de l'examen
     */
    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    /**
     * getter pour l'heure de fin d'un examen
     * @return heure de fin de l'examen
     */
    public LocalTime getHeureFin() {
        return heureFin;
    }

}
