"""
Heng Wei
Christina Duong

Ceci est un programme qui permet aux utilisateurs de jouer au jeu Boggle.
Le programme supporte tous les aspects de base du jeu ainsi que tous les aspects bonis 
(option grille 6x6, mots dont les lettres adjacentes ne sont pas sur une même ligne ou 
une même colonne, supporte les diagonales, la possibilité de faire une partie en plusieurs manches 
(points cummulatifs) et la possibilité de choisir le nombre de joueurs (2 et plus).
"""

import random
dice = [
['E', 'T', 'U', 'K', 'N', 'O'],
['E', 'V', 'G', 'T', 'I', 'N'],
['D', 'E', 'C', 'A', 'M', 'P'],
['I', 'E', 'L', 'R', 'U', 'W'],
['E', 'H', 'I', 'F', 'S', 'E'],
['R', 'E', 'C', 'A', 'L', 'S'],
['E', 'N', 'T', 'D', 'O', 'S'],
['O', 'F', 'X', 'R', 'I', 'A'],
['N', 'A', 'V', 'E', 'D', 'Z'],
['E', 'I', 'O', 'A', 'T', 'A'],
['G', 'L', 'E', 'N', 'Y', 'U'],
['B', 'M', 'A', 'Q', 'J', 'O'],
['T', 'L', 'I', 'B', 'R', 'A'],
['S', 'P', 'U', 'L', 'T', 'E'],
['A', 'I', 'M', 'S', 'O', 'R'],
['E', 'N', 'H', 'R', 'I', 'S'],
['A', 'T', 'S', 'I', 'O', 'U'],
['W', 'I', 'R', 'E', 'B', 'C'],
['Q', 'D', 'A', 'H', 'A', 'U'],
['A', 'C', 'F', 'L', 'N', 'E'],
['P', 'R', 'S', 'T', 'U', 'G'],
['J', 'P', 'R', 'X', 'E', 'Z'],
['E', 'K', 'V', 'Y', 'B', 'E'],
['A', 'L', 'C', 'H', 'E', 'M'],
['E', 'D', 'U', 'F', 'H', 'K'],
['E', 'T', 'U', 'K', 'N', 'O'],
['E', 'V', 'G', 'T', 'I', 'N'],
['D', 'E', 'C', 'A', 'M', 'P'],
['I', 'E', 'L', 'R', 'U', 'W'],
['E', 'H', 'I', 'F', 'S', 'E'],
['R', 'E', 'C', 'A', 'L', 'S'],
['E', 'N', 'T', 'D', 'O', 'S'],
['O', 'F', 'X', 'R', 'I', 'A'],
['N', 'A', 'V', 'E', 'D', 'Z'],
['E', 'I', 'O', 'A', 'T', 'A'],
['G', 'L', 'E', 'N', 'Y', 'U']
] #les 36 dés du jeu

grid = [] # une liste en 2D représentant la grille

grid_size = 0 #taille de la grille

dices_chosen = [] 
"""
dices_chosen contient les dés pour les grilles 4x4, 5x5 ou 6x6. Au début de chaque partie,
cette liste est mélangée aléatoirement
"""

number_players = 0
players_score = []
player_names = []

def is_valid(word): #est_valide(grille,mot). Pas besoin d'avoir le tableau 'grid' comme paramètre car 'grid' est une variable globale 
    def recursive_search_from_slot(row, column, idx, slot_visited): 
        #row et column correspondent à l'index de la lettre visitée dans la grille. 
        #idx c'est l'index de la lettre du mot qu'on cherche dans la grille
        #word[idx] c'est la lettre qu'on cherche
        #slot_visited contient les cases déjà choisies
        local_state_slot_visited = []
        for _ in range(len(slot_visited)):
            local_state_slot_visited.append(slot_visited[_][:]) 
            #on copie slot_visited à local_state_slot_visited
            #slot_visited ici c'est le local_state_slot_visited précédent
        
        if row < 0 or column < 0 or row >= grid_size or column >= grid_size or slot_visited[row][column] == True:
            return False
            #on arrete cette "branche" car on sort de la grille ou on revisite une case
        elif grid[row][column] != word[idx]:
            return False
            #on arrete cette "branche" car la lettre dans la case ne match pas la lettre recherchée
        else: #la lettre match. on procède à chercher la prochaine lettre parmi les cases adjacentes
            local_state_slot_visited[row][column] = True #cette case contient la lettre recherchée, donc on la note comme "visitée"
            if idx+1 == len(word):#on a trouvé la derniere lettre
                return True
            if recursive_search_from_slot(row+1, column,idx+1,local_state_slot_visited): return True #un pas vers le bas. on cherche la prochaine lettre du mot (word[idx+1])
            if recursive_search_from_slot(row-1, column,idx+1,local_state_slot_visited): return True #un pas vers le haut. on cherche la prochaine lettre du mot (word[idx+1])
            if recursive_search_from_slot(row, column+1,idx+1,local_state_slot_visited): return True #etc
            if recursive_search_from_slot(row, column-1,idx+1,local_state_slot_visited): return True
            if recursive_search_from_slot(row+1, column+1,idx+1,local_state_slot_visited): return True
            if recursive_search_from_slot(row+1, column-1,idx+1,local_state_slot_visited): return True
            if recursive_search_from_slot(row-1, column+1,idx+1,local_state_slot_visited): return True
            if recursive_search_from_slot(row-1, column-1,idx+1,local_state_slot_visited) : return True
            return False
    
    if len(word) < 3:
            return False
    
    slot_visited = []
    for _ in range(grid_size):
        slot_visited.append([False]*grid_size) #mettre toutes les cases à 'non-visitées'

    word_found = False
    for row in range(0, grid_size):
        for column in range(0, grid_size):
            if word[0] == grid[row][column]:
                if recursive_search_from_slot(row, column, 0, slot_visited): 
                    word_found = True
                    return word_found #retourne true car on a trouvé le mot
    
    return word_found #retourne false car on n'a pas trouvé le mot
        
def generate_grid(): #generer_grille(taille)
    if grid_size < 4 or grid_size > 6:
        return
    
    global dices_chosen
    for i in range(grid_size**2):
        dices_chosen.append(dice[i]) 

    for i in range(grid_size**2-1,0,-1): 
        """ 
        On mélange l'ordre des dés dans 'dices_chosen'. 
        Mélanger les dés correspond à placer aléatoirement chaque dé 
        sur une case de la grille
        """
        choose_dice = int(random.random()*i) #choisir un dé parmi les i premiers dés
        buffer = dices_chosen[i]
        dices_chosen[i] = dices_chosen[choose_dice]
        dices_chosen[choose_dice] = buffer #swap les deux dés
    
    for row in range(grid_size):
        """
        On génère la grille en déterminant la face pointée vers le haut
        de chaque dé sur la grille
        """
        grid.append([])
        for column in range(grid_size):
            face_up = dices_chosen[column+4*row][int(random.random()*6)] 
            # on choisit la face pointée vers le haut de nos dés sur la grid. 
            # 'column+4*row' va de 0 à grid_size^2 - 1
            grid[row].append(face_up)

def print_grid(): #imprime la grille
    for i in range(grid_size): 
        print(' '+'______' * (grid_size))     
        print('|  '+'  |  '.join(grid[i]) + '  |')
    print(' '+'______' * (grid_size)) 
    print("\n")

def play():
    while True:
        global number_players, grid_size, player_names
        reset() #clear toutes les données avant de demander à l'utilisateur le input
       
        while True: #continue à boucler jusqu'à temps que le joueur rentre un nombre de joueurs valide
            number_players = input("Veuillez entrer le nombre de joueurs\n")
            if number_players.isdigit():
                number_players = int(number_players)
                break
            print("Entrée invalide")

        for _ in range(0, number_players):
            players_score.append(0) #initialise le point de chaque joueur à 0
            _ = "Joueur " + str(_+1) + " veuillez entrer votre nom\n"
            while True: #continue à boucler jusqu'à temps que le joueur rentre un nom non-vide
                name_input = input(_)
                if name_input != "":
                    break
                print("Entrée invalide")

            player_names.append(name_input)

        while True: #continue à boucler jusqu'à temps que le joueur rentre un nombre de manche valide
            number_games = input("Veuillez entrer le nombre de manches\n")
            if number_games.isdigit():
                number_games = int(number_games)
                break
            print("Entrée invalide")

        while True: #continue à boucler jusqu'à temps que le joueur rentre un input valide
            grid_size = input("Veuillez entrer la taille de la grille de votre choix (4, 5, ou 6)\n")
            if grid_size.isdigit():
                grid_size = int(grid_size)
                if grid_size >= 4 and grid_size <= 6:
                    grid_size = int(grid_size)
                    break
            print("Entrée invalide")

        for m in range(0, number_games): #nombre de manches
            players_score_manche = []
            players_answers_manche = []

            for _ in range(0, number_players):
                players_score_manche.append(0)
                players_answers_manche.append([])

            set_game(grid_size)
            print("Manche " + str(m+1) + ":")
            stop = []
            for t in range(0,10): # nombres de tours (fixé à plus petit ou égal 10 tours)
                for j in range (0, number_players):
                    stop.append(False) # initialise un tableau qui représente si chaque joueur désire continuer la partie
                for j in range (0, number_players): # joueurs j joue
                    if stop[j]: #skip le tour si le joueur a déjà déclarer qu'il ne trouve plus de mots
                        continue                    
                    while True:
                        _ = player_names[j] + " à ton tour de jouer! 'Y' pour entrer un mot, 'N' pour arrêter.\n"
                        flag = input(_)
                        flag = flag.upper()
                        if flag == 'Y':
                            break
                        elif flag == 'N':
                            stop[j] = True
                            break
                        else:
                            print("Entrée invalide")
                    
                    if stop[j]: #skip la demande d'entrée d'un mot si le joueur vient de répondre 'N' à la question "Vois-tu d'autres mots? 'Y' pour continuer, 'N' pour arrêter."
                        continue

                    while True:
                        _ = player_names[j] + " veuillez entrer un mot\n"
                        player_answer = input(_).upper()
                        if len(player_answer) == 0:
                            print("SVP entrez quelquechose")
                        else:
                            break

                    if not is_valid(player_answer):
                        players_answers_manche[j].append([player_answer,"ILLEGAL"]) #La réponse est ajoutée au tableau des réponses des joueurs
                        print("Mot illégal")
                        continue #si le mot n'est pas valide, on saute l'étape de demander si les autres joueurs valident le mot

                    validate = True
                    for j_ in range(0,number_players): # les autres joueurs valident le mot du joueur j
                        if j_ == j:
                            continue
                        while True: #continue à boucler jusqu'à temps que le joueur rentre un input valide
                            check = input("Le mot " + player_answer + " est-il valide, " + player_names[j_] + "? Veuillez entre 'O' pour oui, 'N' pour non.\n")
                            check = check.upper()
                            if check == 'N': #le mot est rejeté
                                validate = False
                                break
                            elif check == 'O':
                                break
                        if not validate: #on arrête de demander aux autres joueurs si le mot est valide lorsqu'un joueur rejette
                            break

                    if validate: 
                        players_answers_manche[j].append([player_answer,"ACCEPTE"]) #La réponse est ajoutée au tableau des réponses des joueurs
                    else:
                        players_answers_manche[j].append([player_answer,"REJETE"]) #La réponse est ajoutée au tableau des réponses des joueurs

                if stop == number_players: # c-à-d qu'aucun joueur à jouer. la partie de termine
                    break
            
            score_calc(players_answers_manche, players_score_manche)
            print_result(players_answers_manche, players_score_manche)


        #trouver le(s) joueur(s) avec le plus de points
        highest_score = max(players_score)
        winners = []

        for p in range(0, number_players): #on rajoute le noms des gagnants dans winnners
            if players_score[p] == highest_score:
                winners.append(player_names[p]) 
        
        if len(winners) == 1: 
            print(winners[0], "a remporté la partie!")  
        else:
            _ = "" 
            for p in range(0,len(winners)):
                if p == len(winners)-1: _ = _ + winners[p]
                elif p == len(winners)-2: _ = _ + winners[p] + ' et '
                else: _ = _ + winners[p] + ', '
            print(_, "ont remporté la partie!") 

        while True: #continue à boucler jusqu'à temps l'utilisateur rentre 'O' ou 'N'
            _ = input("Voulez-vous jouer une nouvelle partie? [O/N]\n")
            _ = _.upper()
            if _ == 'O' or _ == 'N':
                break

        if _ == 'O': 
            continue
        else:
            break

def score_calc(players_answers_manche, players_score_manche): 
    #fonction 'calcul_point(grille, mots)' en anglais avec paramétrage modifié. 
    #Calcule le score de chaque joueur d'une manche, et les scores totals pour chaque joueurs dans "players_score"
    global players_score
    global number_players
    for p in range(0, number_players):
        for word in players_answers_manche[p]:        
            if word[1] == "ACCEPTE":
                if len(word[0]) == 3: 
                    players_score[p] += 1
                    players_score_manche[p] += 1
                    word.append('1')
                if len(word[0]) == 4: 
                    players_score[p] += 2
                    players_score_manche[p] += 2
                    word.append('2')
                if len(word[0]) == 5: 
                    players_score[p] += 3
                    players_score_manche[p] += 3
                    word.append('3')
                if len(word[0]) == 6: 
                    players_score[p] += 5
                    players_score_manche[p] += 5
                    word.append('5')
                if len(word[0]) == 7: 
                    players_score[p] += 8
                    players_score_manche[p] += 8
                    word.append('8')
                if len(word[0]) == 8: 
                    players_score[p] += 10
                    players_score_manche[p] += 10
                    word.append('10')
                if len(word[0]) > 8: 
                    if grid_size < 6:
                        players_score[p] += 10
                        players_score_manche[p] += 10
                        word.append('10')
                    else:
                        players_score[p] += 12
                        players_score_manche[p] += 12
                        word.append('12')                        
            else:
                word.append('x')

def print_result(players_answers_manche, players_score_manche):
    global players_score
    for j in range(0, number_players):
        print(player_names[j])
        print("-----------------------------")
        for word in players_answers_manche[j]:
            if word[1] == "ACCEPTE":
                print('-', word[0], "  (" + word[2] + ")")
            else:
                print('-', word[0], "  (" + word[2] + ")", word[1])  
        print("=============================")
        print("TOTAL: " + str(players_score_manche[j]) + "\n\n")
         
def set_game(size): #mettre en place les données (grille, etc) pour une manche
    global grid
    global dices_chosen
    global all_words
    global grid_size
    grid_size = size #possibilité de modifier le code pour permettre une grille de dimension différente entre deux manches
    grid = [] 
    dices_chosen = [] 
    all_words = [] 
    generate_grid()
    print_grid()
                                       
def reset(): #clear toutes les données (pour une nouvelle jeu, ou pour les différentes cas du test unitaire)
    global grid
    global grid_size
    global dices_chosen
    global all_words
    global number_players
    global players_score
    grid = []
    grid_size = 0 
    dices_chosen = [] 
    all_words = [] 
    number_players = 0
    players_score = []


"""Test unitaire"""
def test():
    def test_generate_grid(): #deux grilles générées successivement ne sont pas identiques (probabilité négligeable)
        global grid_size

        grid_size = 4
        generate_grid()
        grid1 = grid
        reset()
        grid_size = 4
        generate_grid()
        grid2 = grid
        reset()
        assert grid1 != grid2, "Échec, generate_grid() a affiché deux grilles identiques (deux grilles générées successivement ne sont pas identiques (probabilité négligeable))"
       
        grid_size = 0
        generate_grid()
        assert grid == [], "Échec, generate_grid() a modifié grid malgré que la taille de la grille n'est pas valide"
        reset()

        grid_size = -1
        assert grid == [], "Échec, generate_grid() a modifié grid malgré que la taille de la grille n'est pas valide"
        reset()

        grid_size = 4
        generate_grid()
        assert len(grid) == 4, "Échec, generate_grid() n'a pas générée le bon nombre de rangées"
        reset()

        grid_size = 5
        generate_grid()
        assert len(grid[0]) == len(grid[1]) == len(grid[2]) == len(grid[3]) == len(grid[4]) == 5 , "Échec, generate_grid() n'a pas générée le bon nombre de colonnes"
        reset()


    def test_is_valid():
        global grid
        global grid_size
        grid_size = 4
        grid = [
            ['F','A','I','L'],
            ['M','U','R','O'],
            ['J','E','U','O'],
            ['P','I','C','K'],
        ]
        #les 5 cas de test pour is_valid()
        assert is_valid("PERL") == True, "Échec, mot valide (horizontal) n'est pas reconnu"
        assert is_valid("FAIL") == True, "Échec, mot valide (vertical) n'est pas reconnu"
        assert is_valid("LOOK") == True, "Échec, mot valide (diagonal) n'est pas reconnu"
        assert is_valid("RUE") == True, "Échec, mot valide (adjacente) n'est pas reconnu"
        assert is_valid("ARBRE") == False, "Échec, mot invalide a été reconnu"
        reset()

    def test_score_calc():
        global grid
        global grid_size
        grid_size = 4
        grid = [
            ['F','A','I','L'],
            ['M','U','R','O'],
            ['J','E','U','O'],
            ['P','I','C','K'],
        ]
        global players_score
        global number_players
        number_players = 2
        players_score = [0,0,0,0,0]
    

        #manche 1
        players_answers_manche = [[],[]]
        players_score_manche = [0,0,0,0,0]

        players_answers_manche[0].append(["FAIL","ACCEPTE"])
        players_answers_manche[0].append(["ARBRE","ILLEGAL"])
        players_answers_manche[1].append(["CURI","REJETE"])
        players_answers_manche[1].append(["PICK","ACCEPTE"])
        score_calc(players_answers_manche, players_score_manche)
        assert players_score[0] == players_score_manche[0] == 2, "Échec, le score n'a pas été calculé correctement"
        assert players_score[1] == players_score_manche[1] == 2, "Échec, le score n'a pas été calculé correctement"

        #manche 2
        players_answers_manche = [[],[]]
        players_score_manche = [0,0,0,0,0]

        players_answers_manche[0].append(["JEU","ACCEPTE"])
        players_answers_manche[1].append(["LOOK","ACCEPTE"])
        score_calc(players_answers_manche, players_score_manche)
        assert players_score[0] == 3, "Échec, le score total n'a pas été calculé correctement"
        assert players_score_manche[0] == 1, "Échec, le score pour la 2e manche n'a pas été calculé correctement"
        assert players_score[1] == 4, "Échec, le score total n'a pas été calculé correctement"
        assert players_score_manche[1] == 2, "Échec, le score pour la 2e manche n'a pas été calculé correctement"

        #6 cas de test en tout pour la fonction score_calc
        reset()

    test_generate_grid()
    test_is_valid()
    test_score_calc()

test()




