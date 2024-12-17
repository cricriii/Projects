/**
 * importer les librairies standards fournies par Java
 */
import java.time.LocalTime;
import java.time.DayOfWeek;

/**
 * Class CoursHoraire: classe englobant les propriétés d'un cours dans un horaire
 */
public class CoursHoraire {
    /**
     * Variables jour, heureDébut et heureFin reliés à un cours
     */
    private DayOfWeek jour;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    /**
     * Cette fonction créé un cours pour l'horair4e avec les propriétés nécessaires.
     * @param jour jour du cours
     * @param heureDebut heure de début du cours
     * @param heureFin heure de fin du cours
     */
    public CoursHoraire(DayOfWeek jour, LocalTime heureDebut, LocalTime heureFin) {
        this.jour= jour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    /**
     * Getter pour le jour du cours
     * @return jour du cours
     */
    public DayOfWeek getJour() {
        return jour;
    }

    /**
     * getter pour l'heure de début du cours
     * @return heure de début du cours
     */
    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    /**
     * getter pour l'heure de fin du cours
     * @return heure de fin du cours
     */
    public LocalTime getHeureFin() {
        return heureFin;
    }

}
