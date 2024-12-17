import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.LocalDate;

/**
 * @author Christina Duong 20238163
 * 2023-07-13
 */



/**
 * classe de tests
 */
public class Main {
    public static void main(String[] args) {
        tests();
    }

    public static void tests() {
        HoraireEcole horaire = new HoraireEcole();
        horaire.setMaxCredits(15);

        /**
         * test de: ajouter un cours
         */
        Cours cours1 = new Cours(1000, "MAT", 4, Cours.thtpexam.TH);
        CoursHoraire coursHoraire1 = new CoursHoraire(DayOfWeek.MONDAY, LocalTime.parse("08:30"), LocalTime.parse("10:30"));
        cours1.ajouterCoursHoraire(coursHoraire1);
        boolean ajouter = !horaire.ajouterCours(cours1);
        System.out.println("Test Ajouter un Cours (TH): " + (ajouter ? "OK":"ERREUR"));

        /**
         * test de: ajouter un examen
         */
        Cours cours2 = new Cours(1025, "IFT", 3, Cours.thtpexam.EXAM);
        ExamHoraire examHoraire2 = new ExamHoraire(LocalDate.parse("2023-07-27"), LocalTime.parse("15:00"), LocalTime.parse("18:00"));
        cours2.ajouterExamenHoraire(examHoraire2);
        boolean ajouter2 = !horaire.ajouterExamen(cours2);
        System.out.println("Test Ajouter un Examen: " + (ajouter2 ? "OK":"ERREUR"));


        /**
         * test de: ajouter un cours et le modifier
         */
        Cours cours3 = new Cours(2400, "STT", 5, Cours.thtpexam.TH);
        CoursHoraire coursHoraire3 = new CoursHoraire(DayOfWeek.TUESDAY, LocalTime.parse("12:30"), LocalTime.parse("15:30"));
        cours3.ajouterCoursHoraire(coursHoraire3);
        horaire.ajouterCours(cours3);

        Cours modifiedCours = new Cours(2700, "STT2", 4, Cours.thtpexam.TP);
        CoursHoraire modifiedCoursHoraire = new CoursHoraire(DayOfWeek.WEDNESDAY, LocalTime.parse("15:00"), LocalTime.parse("19:00"));
        modifiedCours.ajouterCoursHoraire(modifiedCoursHoraire);
        boolean modified = horaire.modifierCours(2400, modifiedCours);
        System.out.println("Test Modifier un Cours: " + (modified ? "OK":"ERREUR"));

        /**
         * test de: supprimer un cours
         */
        boolean deleted = horaire.supprimercours(1000);
        System.out.println("Test Supprimer un Cours: " + (deleted ? "OK":"ERREUR"));

        /**
         * test d'affichage d'horaire
         */
        System.out.println("\nHORAIRE:");
        horaire.afficherHoraire();
    }
}