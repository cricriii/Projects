/**
 * importer les librairies standards fournies par Java
 */
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main body du code. C'est ici qu'on compose le programme et qu'on fera appelle aux autres classes et leurs fonctions.
 * C'est aussi ici qu'on sera en interaction avec l'utilisateur
 */
public class HORAIRE {
    private static HoraireEcole horaire;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); //to use the scanner
        horaire = new HoraireEcole(); //on créé un horaire
        System.out.print("Entrez le nombre maximum de crédits pour la session: ");
        int maxCredits = scanner.nextInt(); // input de l'utilisateur: max de crédits
        scanner.nextLine();
        boolean exit = false; // lorsque exit=true, on arrête le programme
        int sommecredits = 0; //on initialise sommecredits et nbcours à 0 au lancement.
        int nbcours = 0;

        while (!exit) {
            System.out.println("Menu: "); //on affiche le menu
            System.out.println("1. Gestion des cours");
            System.out.println("2. Afficher l'horaire");
            System.out.println("3. Quitter");
            System.out.print("Choix : ");
            try {
                int entree = scanner.nextInt();
                scanner.nextLine();
                switch (entree) {
                    case 1: //Gestion des cours
                        if (nbcours >= 10) { //tant qu'on dépasse pas le maximum de 10 cours
                            System.out.println("Vous avez atteint le nombre maximum de cours.");
                            break;
                        }
                        System.out.println("1. Ajouter un cours"); // trois options de plus reliés au propriétés d'un cours
                        System.out.println("2. Modifier un cours");
                        System.out.println("3. Supprimer un cours");
                        System.out.print("Choix : ");
                        try {
                            int choix = scanner.nextInt();
                            scanner.nextLine();
                            switch (choix) {
                                case 1: //ajouter un cours
                                    if (sommecredits >= maxCredits) {
                                        System.out.println("Vous avez atteint le maximum de crédits permis.");
                                        break;
                                    } //on demande les inputs de l'utilisateur concernant toutes les propriétés d'un cours comme le numéro par exemple
                                    System.out.print("Numéro du cours: ");
                                    int numero = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Matière du cours: ");
                                    String matiere = scanner.nextLine();
                                    boolean matiereValide = true;
                                    for (int i = 0; i < matiere.length(); i++) { //la matière doit être des lettres seulement
                                        if (!Character.isLetter(matiere.charAt(i))) {
                                            matiereValide = false;
                                            break;
                                        }
                                    }
                                    if (!matiereValide) {
                                        System.out.println("Erreur : La matière du cours doit contenir uniquement des lettres.");
                                        continue;
                                    }
                                    System.out.print("Nombre de crédits: ");
                                    int credits = scanner.nextInt();
                                    scanner.nextLine();
                                    if (sommecredits + credits > maxCredits) {
                                        System.out.println("Ajouter ce cours dépassera le maximum de crédits permis.");
                                        break;
                                    }
                                    sommecredits += credits; //on additionne le nombre de crédits à sommecredits pour keep track de notre nombre de crédits total dans l'horaire
                                    boolean pluscours = true;
                                    while (pluscours) { //autres options pour input: un cours ou un examen
                                        System.out.println("1. Ajouter un cours (TH/TP)");
                                        System.out.println("2. Ajouter un examen");
                                        System.out.print("Choix: ");
                                        try {
                                            int choixcours = scanner.nextInt();
                                            scanner.nextLine();
                                            switch (choixcours) {
                                                case 1: // un cours
                                                    System.out.println("1. Ajouter un cours TH");
                                                    System.out.println("2. Ajouter un cours TP");
                                                    System.out.print("Choix: ");
                                                    int choice = scanner.nextInt();
                                                    scanner.nextLine();
                                                    switch (choice) {
                                                        case 1: //TH
                                                            Cours.thtpexam th = Cours.thtpexam.TH;
                                                            Cours nouveauCours = new Cours(numero, matiere, credits, th); //on créé le cours avec les infos rentrées plus haut
                                                            try { //on demande le jour et les heures
                                                                System.out.print("Jour de la semaine du cours (Monday à Sunday): ");
                                                                String jourentree = scanner.nextLine();
                                                                DayOfWeek jour = DayOfWeek.valueOf(jourentree.toUpperCase());
                                                                System.out.print("Heure de début du cours (HH:mm): ");
                                                                String debutentree = scanner.nextLine();
                                                                LocalTime debut = LocalTime.parse(debutentree);
                                                                System.out.print("Heure de fin du cours (HH:mm): ");
                                                                String finentree = scanner.nextLine();
                                                                LocalTime fin = LocalTime.parse(finentree);

                                                                CoursHoraire coursHoraire = new CoursHoraire(jour, debut, fin);
                                                                nouveauCours.ajouterCoursHoraire(coursHoraire);

                                                                System.out.print("Ajouter un autre horaire pour ce cours (O/N)? : "); //on demande si l'utilisateur veut rajouter un cours
                                                                String ajouterautrecours = scanner.nextLine();
                                                                pluscours = ajouterautrecours.equalsIgnoreCase("O");
                                                            } catch (IllegalArgumentException | DateTimeParseException e) {
                                                                System.out.println("ERREUR! Entrée de jour ou d'heure invalide.");
                                                                break;
                                                            }
                                                            boolean conflit = horaire.ajouterCours(nouveauCours);
                                                            if (conflit) { //affichage du message de conflit
                                                                System.out.println("ATTENTION! Le cours est en conflit avec un autre cours.");
                                                            } else {
                                                                System.out.println("Cours ajouté.");
                                                            }
                                                            break;
                                                        case 2: //TP. pareil que TH
                                                            Cours.thtpexam tp = Cours.thtpexam.TP;
                                                            Cours nouveauCours2 = new Cours(numero, matiere, credits, tp);
                                                            try {
                                                                System.out.print("Jour de la semaine du cours (Monday à Sunday): ");
                                                                String jourentree = scanner.nextLine();
                                                                DayOfWeek jour = DayOfWeek.valueOf(jourentree.toUpperCase());
                                                                System.out.print("Heure de début du cours (HH:mm): ");
                                                                String debutentree = scanner.nextLine();
                                                                LocalTime debut = LocalTime.parse(debutentree);
                                                                System.out.print("Heure de fin du cours (HH:mm): ");
                                                                String finentree = scanner.nextLine();
                                                                LocalTime fin = LocalTime.parse(finentree);

                                                                CoursHoraire coursHoraire = new CoursHoraire(jour, debut, fin);
                                                                nouveauCours2.ajouterCoursHoraire(coursHoraire);

                                                                System.out.print("Ajouter un autre horaire pour ce cours (O/N)?: ");
                                                                String ajouterautrecours = scanner.nextLine();
                                                                pluscours = ajouterautrecours.equalsIgnoreCase("O");
                                                            } catch (IllegalArgumentException | DateTimeParseException e) {
                                                                System.out.println("ERREUR! Entrée de jour ou d'heure invalide.");
                                                                break;
                                                            }

                                                            boolean conflit2 = horaire.ajouterCours(nouveauCours2);
                                                            if (conflit2) {
                                                                System.out.println("ATTENTION! Le cours est en conflit avec un autre cours.");
                                                            } else {
                                                                System.out.println("Cours ajouté.");
                                                            }
                                                            break;
                                                    }
                                                    break;
                                                case 2: //EXAM
                                                    Cours.thtpexam exam = Cours.thtpexam.EXAM;
                                                    Cours nouveauCours3 = new Cours(numero, matiere, credits, exam);
                                                    try { //on demande la date et les heures de l'examen. le reste est pareil que les cours th et tp
                                                        System.out.print("Date de l'EXAM du cours (AAAA-MM-JJ): ");
                                                        String dateEntree = scanner.nextLine();
                                                        LocalDate date = LocalDate.parse(dateEntree);

                                                        System.out.print("Heure de début de l'EXAM (HH:mm): ");
                                                        String debutEntree = scanner.nextLine();
                                                        LocalTime debut = LocalTime.parse(debutEntree);
                                                        System.out.print("Heure de fin de l'EXAM (HH:mm): ");
                                                        String finEntree = scanner.nextLine();
                                                        LocalTime fin = LocalTime.parse(finEntree);

                                                        ExamHoraire examen = new ExamHoraire(date, debut, fin);
                                                        nouveauCours3.ajouterExamenHoraire(examen);

                                                        System.out.print("Ajouter un autre horaire pour ce cours (O/N)?: ");
                                                        String ajouterautrecours = scanner.nextLine();
                                                        pluscours = ajouterautrecours.equalsIgnoreCase("O");
                                                    } catch (IllegalArgumentException | DateTimeParseException e) {
                                                        System.out.println("ERREUR! Entrée de date ou d'heure invalide.");
                                                        break;
                                                    }

                                                    boolean conflit2 = horaire.ajouterExamen(nouveauCours3);
                                                    if (conflit2) {
                                                        System.out.println("ATTENTION! L'examen est en conflit avec un autre examen.");
                                                    } else {
                                                        System.out.println("Examen ajouté.");
                                                    }
                                                    break;

                                                default:
                                                    System.out.println("Option invalide.");
                                                    break;
                                            }
                                        } catch (InputMismatchException e) { //on attrape les exceptions.
                                            System.out.println("ERREUR! Entrée de choix invalide.");
                                            scanner.nextLine();
                                            break;
                                        }
                                    }
                                    nbcours++; //on ajoute 1 au nb de cours
                                    break;

                                case 2: //modifier un cours
                                    System.out.print("Numéro du cours à modifier: ");
                                    try {
                                        int numeromodifier = scanner.nextInt(); //on demande le numéro du cours que la personne veut modifier
                                        scanner.nextLine();

                                        Cours coursmodifier = horaire.getCoursNumero(numeromodifier); //on va changer le cours en redemandant tout
                                        if (coursmodifier != null) {
                                            System.out.print("Nouveau numéro du cours: ");
                                            int nouveaunumero = scanner.nextInt();
                                            scanner.nextLine(); // Consume the newline character
                                            System.out.print("Nouvelle matière du cours: ");
                                            String nouveaumatiere = scanner.nextLine();
                                            boolean matiereValide2 = true;

                                            for (int i = 0; i < nouveaumatiere.length(); i++) {
                                                if (!Character.isLetter(nouveaumatiere.charAt(i))) {
                                                    matiereValide2 = false;
                                                    break;
                                                }
                                            }

                                            if (!matiereValide2) {
                                                System.out.println("ERREUR. La matière doit seulement contenir des lettres.");
                                                continue;
                                            }

                                            System.out.print("Nouveau nombre de crédits: ");
                                            int nouveaucredits = scanner.nextInt();
                                            scanner.nextLine();

                                            if (sommecredits + (nouveaucredits - coursmodifier.getCredits()) > maxCredits) {
                                                System.out.println("Ajouter ce cours dépassera le maximum de crédits permis.");
                                                break;
                                            }

                                            sommecredits += (nouveaucredits - coursmodifier.getCredits());
                                            Cours.thtpexam changertype = null;
                                            boolean typeexistant = false;
                                            while (!typeexistant) {
                                                System.out.print("Nouvelle catégorie du cours (TH/TP/EXAM): ");
                                                String typeentree = scanner.nextLine().toUpperCase();
                                                try {
                                                    changertype = Cours.thtpexam.valueOf(typeentree);
                                                    typeexistant = true;
                                                } catch (IllegalArgumentException e) {
                                                    System.out.println("ERREUR! Catégorie de cours invalide.");
                                                    break;
                                                }
                                            }
                                            Cours coursmodifiee = new Cours(nouveaunumero, nouveaumatiere, nouveaucredits, changertype);
                                            boolean modifiercours = true;
                                            while (modifiercours) {
                                                try {
                                                    if (changertype == Cours.thtpexam.TH || changertype == Cours.thtpexam.TP) {
                                                        System.out.print("Jour de la semaine du cours (Monday à Sunday): ");
                                                        String jourentree = scanner.nextLine();
                                                        DayOfWeek nouveauJour = DayOfWeek.valueOf(jourentree.toUpperCase());
                                                        System.out.print("Heure de début du cours (HH:mm): ");
                                                        String debutentree = scanner.nextLine();
                                                        LocalTime nouveaudebut = LocalTime.parse(debutentree);
                                                        System.out.print("Heure de fin du cours (HH:mm): ");
                                                        String finentree = scanner.nextLine();
                                                        LocalTime nouveaufin = LocalTime.parse(finentree);

                                                        CoursHoraire nouveauCoursHoraire = new CoursHoraire(nouveauJour, nouveaudebut, nouveaufin);
                                                        coursmodifiee.ajouterCoursHoraire(nouveauCoursHoraire);

                                                        System.out.print("Ajouter un autre horaire pour ce cours (O/N)?: ");
                                                        String autremodif = scanner.nextLine();
                                                        modifiercours = autremodif.equalsIgnoreCase("O");
                                                    } else if (changertype == Cours.thtpexam.EXAM) {
                                                        System.out.print("Date de l'EXAM du cours (AAAA-MM-JJ): ");
                                                        String dateEntree = scanner.nextLine();
                                                        LocalDate date = LocalDate.parse(dateEntree);

                                                        System.out.print("Heure de début de l'EXAM (HH:mm): ");
                                                        String debutEntree = scanner.nextLine();
                                                        LocalTime debut = LocalTime.parse(debutEntree);
                                                        System.out.print("Heure de fin de l'EXAM (HH:mm): ");
                                                        String finEntree = scanner.nextLine();
                                                        LocalTime fin = LocalTime.parse(finEntree);

                                                        ExamHoraire examen = new ExamHoraire(date, debut, fin);
                                                        coursmodifiee.ajouterExamenHoraire(examen);

                                                        System.out.print("Ajouter un autre horaire pour ce cours (O/N)?: ");
                                                        String autremodif = scanner.nextLine();
                                                        modifiercours = autremodif.equalsIgnoreCase("O");
                                                    } else {
                                                        System.out.println("Option invalide.");
                                                        break;
                                                    }
                                                } catch (IllegalArgumentException | DateTimeParseException e) {
                                                    System.out.println("ERREUR! Entrée de jour/heure invalide.");
                                                    break;
                                                }
                                            }
                                            boolean conflit2 = horaire.modifierCours(numeromodifier, coursmodifiee);
                                            if (conflit2) {
                                                System.out.println("ATTENTION! Le cours est en conflit avec un autre cours.");
                                            } else {
                                                System.out.println("Cours modifié.");
                                            }
                                        } else {
                                            System.out.println("Le cours n'a pas été trouvé."); //affiche ce message si on a pas trouvé le cours
                                            break;
                                        }
                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out.println("ERREUR! Entrée de numéro invalide.");
                                    }
                                    break;

                                case 3: //supprimer un cours
                                    System.out.print("Numéro du cours à supprimer: ");
                                    try {
                                        int numerosupp = scanner.nextInt(); //on demande le numéro du cours à supprimer
                                        scanner.nextLine();
                                        int creds = horaire.getCredits(numerosupp); //il faut soustraire le nombre de crédits du cours qu'on supprimer à sommecredits
                                        sommecredits+= creds;

                                        boolean supprimer = horaire.supprimercours(numerosupp);
                                        if (supprimer) { //affiche le message dépendamment de si le cours a été supprimé
                                            System.out.println("Cours supprimé.");
                                        } else {
                                            System.out.println("Le cours n'a pas été trouvé.");
                                            break;
                                        }
                                    } catch (InputMismatchException e) { //attraper les exceptions
                                        System.out.println("ERREUR! Entrée de numéro invalide.");
                                        break;
                                    }
                                    nbcours--;
                                    break;

                                default:
                                    System.out.println("Option invalide.");
                                    break;
                            }
                        } catch (InputMismatchException e) { //attraper les exceptions
                            System.out.println("ERREUR! Entrée d'opération invalide.");
                        }
                        break;

                    case 2: //afficher l'horaire
                        System.out.println("2. Afficher l'horaire");
                        horaire.afficherHoraire();
                        break;

                    case 3: //quitter. exit= true
                        System.out.println("3. Quitter");
                        exit = true;
                        break;

                    default:
                        System.out.println("Choix invalide.");
                        break;
                }
            } catch (InputMismatchException e) { //attraper les exceptions
                System.out.println("ERREUR! Entrée de choix invalide.");
                scanner.nextLine();
            }
            System.out.println();
        }
        //fermer le scanner
        scanner.close();
    }
}
