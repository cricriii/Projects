/**
 * importer les librairies standards fournies par Java
 */
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class HoraireEcole: gérer les fonctions reliées à l'horaire.
 */
public class HoraireEcole {
    /**
     * listedeCours: liste des cours qui seront ajoutés/modifiés/supprimés
     * listeExamens: liste des examens qui seront ajoutés/modifiés/supprimés
     * maxCredits: un int représentant le max de crédits que l'horaire peut avoir
     * sommecredits: un int représentant la somme des crédits des cours qui ont étés ajoutés/modifiés/supprimés
     */
    private List<Cours> listedeCours;
    private List<Cours> listeExamens;
    private int maxCredits;
    private int sommecredits;

    /**
     * Cette fonction sert à créer un nouvel horaire avec une nouvelle listedeCours et listeExamens
     */
    public HoraireEcole() {
        listedeCours = new ArrayList<>();
        listeExamens = new ArrayList<>();
    }

    /**
     * Cette fonction va chercher le cours avec le numéro spécifié en paramètre.
     * @param numero numéro du cours qu'on recherche
     * @return retourne le cours qu'on recherche. si le cours n'est pas trouvé, on retourne null.
     */
    public Cours getCoursNumero(int numero) {
        for (Cours cours : listedeCours) {
            if (cours.getNumero() == numero) {
                return cours;
            }
        }
        return null;
    }

    /**
     * Cette fonction ajoute un cours à l'horaire. Il va appeler la fonction confliHoraire pour vérifier
     * s'il y a un conflit entre le cours en paramètre et les autres cours qui ont été ajoutés dans la listedeCours.
     * @param cours le cours qu'on veut ajouter.
     * @return un boolean: true s'il y a un conflit or false sinon.
     */
    public boolean ajouterCours(Cours cours) {
        boolean conflit = conflitHoraire(cours);
        if (conflit) {
            System.out.println("ATTENTION! Il y a un conflit d'horaire pour le cours.");
        }
        listedeCours.add(cours);
        return conflit;
    }

    /**
     * Cette fonction ajoute un examen à l'horaire. Il va appeler la fonction conflitExamen pour vérifier
     * s'il y a un conflit entre l'examen en paramètre et les autres examens qui ont été ajoutés dans la listeExamens
     * @param examen l'examen qu'on veut ajouter.
     * @return un boolean: true s'il y a un conflit or false sinon.
     */
    public boolean ajouterExamen(Cours examen) {
        boolean conflit = conflitExamen(examen);
        if (conflit) {
            System.out.println("ATTENTION! Il y a un conflit d'horaire pour l'examen.");
        }
        listeExamens.add(examen);
        return conflit;
    }

    /**
     * Cette fonction supprime le cours avec le numéro de cours spécifié en paramètre.
     * On va parcourir la listedeCours pour trouver le cours avec le numéro spécifié et on va le supprimer.
     * @param numero numéro du cours qu'on veut supprimer.
     * @return boolean: true si le cours a été supprimé, false sinon.
     */
    public boolean supprimercours(int numero) {
        Cours coursSupprime = null;
        for (Cours cours : listedeCours) {
            if (cours.getNumero() == numero) {
                coursSupprime = cours;
                break;
            }
        }
        if (coursSupprime != null) {
            listedeCours.remove(coursSupprime);
            return true;
        }
        return false;
    }

    /**
     * Cette fonction va chercher le nombre de crédits associé au cours avec le numéro spécifié en paramètre.
     * On va parcourir la listedeCours et trouver le cours que l'on cherche.
     * @param numero numéro du cours qu'on veut ses crédits.
     * @return sommecredits: le nombre de crédits associés au cours avec le numéro en paramètre
     */
    public int getCredits(int numero) {
        Cours coursrecherche = null;
        for (Cours cours : listedeCours) {
            if (cours.getNumero() == numero) {
                coursrecherche = cours;
                break;
            }
        }
        if (coursrecherche != null) {
            int creds = coursrecherche.getCredits();
            sommecredits = creds;
            return sommecredits;
    }
        return sommecredits;
    }


    /**
     * Cette fonction modifie le cours avec le numéro spécifié en paramètre. On regarde s'il y a un conflit
     * entre le cours qu'on vient de modifier et les cours ajoutés dans listedeCours
     * @param numeromodifie le numéro du cours à modifier
     * @param coursmodifie le nouveau cours modifié
     * @return boolean: true s'il y a un conflit, false sinon.
     */
    public boolean modifierCours(int numeromodifie, Cours coursmodifie) {
        Cours modification = getCoursNumero(numeromodifie);
        if (modification != null) {
            boolean conflit = false;
            for (Cours coursExistant : listedeCours) {
                if (coursExistant != modification) {
                    for (CoursHoraire horaire : coursExistant.getHoraireCours()) {
                        for (CoursHoraire newSchedule : coursmodifie.getHoraireCours()) {
                            if (horaire.getJour() == newSchedule.getJour()) {
                                LocalTime debutExistant = horaire.getHeureDebut();
                                LocalTime finExistant = horaire.getHeureFin();
                                LocalTime nouveauDebut = newSchedule.getHeureDebut();
                                LocalTime nouveauFin = newSchedule.getHeureFin();

                                if (nouveauDebut.isBefore(finExistant) && nouveauFin.isAfter(debutExistant)) {
                                    conflit = true;
                                    break;
                                }
                            }
                        }
                        if (conflit) {
                            break;
                        }
                    }
                    if (conflit) {
                        break;
                    }
                }
            }

            if (!conflit) {
                modification.setNumero(coursmodifie.getNumero());
                modification.setMatiere(coursmodifie.getMatiere());
                modification.setCoursHoraire(coursmodifie.getHoraireCours());
                modification.setCredits(coursmodifie.getCredits());
                modification.setSortecours(coursmodifie.getSortecours());
            }

            return !conflit;
        } else {
            return false;
        }
    }

    /**
     * Cette fonction affiche l'horaire.
     * On parcourt la liste de cours et on va les afficher par jour, en ordre d'heure
     * Les cours seront affichés de la manière suivante: Heures |||| Numéro, matière, crédits, type de cours
     * Les examens seront affichés de la manière suivante: Date, Heures |||| Numéro, matière, crédits, types de cours
     */
    public void afficherHoraire() {
        if (!listedeCours.isEmpty() || !listeExamens.isEmpty()) {
            Map<DayOfWeek, List<CoursHoraire>> horaireJOUR = new TreeMap<>();
            for (Cours cours : listedeCours) {
                List<CoursHoraire> horaire2 = cours.getHoraireCours();
                for (CoursHoraire horaire1 : horaire2) {
                    DayOfWeek Jour = horaire1.getJour();
                    horaireJOUR.computeIfAbsent(Jour, k -> new ArrayList<>()).add(horaire1);
                }
            }

            for (Map.Entry<DayOfWeek, List<CoursHoraire>> entry : horaireJOUR.entrySet()) {
                DayOfWeek Jour = entry.getKey();
                List<CoursHoraire> horaire3 = entry.getValue();
                System.out.println("Jour " + Jour + ":");
                horaire3.sort(Comparator.comparing(CoursHoraire::getHeureDebut));

                List<Cours> printedCourses = new ArrayList<>();
                for (CoursHoraire horaire : horaire3) {
                    for (Cours cours : listedeCours) {
                        if (cours.getHoraireCours().contains(horaire) && !printedCourses.contains(cours)) {
                            System.out.println("- " + horaire.getHeureDebut() + " - " + horaire.getHeureFin()
                                    + " |||| Numéro: " + cours.getNumero()
                                    + ", Matière: " + cours.getMatiere()
                                    + ", Crédits: " + cours.getCredits()
                                    + ", Type de cours: " + cours.getSortecours());
                            printedCourses.add(cours);
                        }
                    }
                }
                System.out.println();
            }

            if (!listeExamens.isEmpty()) {
                System.out.println("Examens:");
                List<Cours> examenordre = new ArrayList<>(listeExamens);
                examenordre.sort(Comparator.comparing(Cours::getExamenDate));

                for (Cours examen : examenordre) {
                    for (ExamHoraire horaire : examen.getExamenHoraire()) {
                        System.out.println("- Date: " + horaire.getDate() + " | Heure: " + horaire.getHeureDebut() + " - " + horaire.getHeureFin()
                                + " |||| Numéro: " + examen.getNumero()
                                + ", Matière: " + examen.getMatiere()
                                + ", Crédits: " + examen.getCredits()
                                + ", Type de cours: " + examen.getSortecours());
                    }
                }
                System.out.println();
            }
        } else {
            System.out.println("Aucun cours ni examen n'a été ajouté.");
        }
    }


    /**
     * Cette fonction regarde s'il y a un conflit d'horaire entre nouveaucours et les cours dans listedeCours
     * Un conflit est détecté si, par exemple avec un cours A et un cours B, l'heure de début du cours B est avant la fin de A
     * et si la fin de B est est après le début de A.
     * @param nouveaucours le cours qu'on voudra voir s'il y a des conflits
     * @return boolean: true s'il y a un conflit, false sinon
     */
    private boolean conflitHoraire(Cours nouveaucours) {
        for (Cours coursExistant : listedeCours) {
            for (CoursHoraire horaireExistant : coursExistant.getHoraireCours()) {
                for (CoursHoraire nouveauHoraire : nouveaucours.getHoraireCours()) {
                    if (horaireExistant.getJour() == nouveauHoraire.getJour()) {
                        LocalTime debutExistant = horaireExistant.getHeureDebut();
                        LocalTime finExistant = horaireExistant.getHeureFin();
                        LocalTime nouveauDebut = nouveauHoraire.getHeureDebut();
                        LocalTime nouveauFin = nouveauHoraire.getHeureFin();

                        if (nouveauDebut.isBefore(finExistant) && nouveauFin.isAfter(debutExistant)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Cette fonction regarde s'il y a un conflit d'examens entre nouveauexamen et les examens dans listeExamens
     * Un conflit est détecté si pour une même date, par exemple avec un examen A et un examen B, l'heure de début de l'examen B est avant la fin de A
     * et si la fin de B est est après le début de A.
     * @param nouveauexamen l'examen qu'on voudra voir s'il rentre en conflit avec les autres examens
     * @return boolean: true s'il y a un conflit, false sinon.
     */
    public boolean conflitExamen(Cours nouveauexamen) {
        for (Cours coursExistant : listeExamens) {
            for (ExamHoraire horaireExistant : coursExistant.getExamenHoraire()) {
                for (ExamHoraire nouveauexamen1 : nouveauexamen.getExamenHoraire()) {
                    if (horaireExistant.getDate().equals(nouveauexamen1.getDate())) {
                        LocalTime debutExistant = horaireExistant.getHeureDebut();
                        LocalTime finExistant = horaireExistant.getHeureFin();
                        LocalTime nouveauDebut = nouveauexamen1.getHeureDebut();
                        LocalTime nouveauFin = nouveauexamen1.getHeureFin();

                        if (nouveauDebut.isBefore(finExistant) && nouveauFin.isAfter(debutExistant)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Cette fonction met en place le maximum de crédits permis pour l'horaire
     * @param maximumCredits le maximum de crédits permis
     */
    public void setMaxCredits(int maximumCredits) {
        this.maxCredits = maximumCredits;
    }
}
