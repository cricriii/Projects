# Nom du fichier: cafe-api.py
# Auteurs: Heng WEI et Christina DUONG
# La date de création: 25 Avril 2023
# Ce programme est un API pour un cafe étudiant, permettant aux utilisateurs de faire différentes requêtes: accèder/modifier les fichiers
# commandes.csv, menu.json et comptes.csv
#----------------------------------------------------------------------------------------------------------------------------------------

import json
import re
import csv
from csv import writer
from datetime import date

import os
import sys

os.chdir(os.path.dirname(__file__))


#account aura les infos de l'utilisateur qui se connecte
account = []
#count va servir à savoir cest quelle requete dans API_ENDPOINTS
count = 0
#compteur pour le nb de commandes dans commandes.csv
nb_commandes = 3

#Toutes les requetes possibles. On utilise re.compile pour spécifier les champs possibles pour les utilisateurs dans les requêtes
API_ENDPOINTS = {
    "get_items":  re.compile(r"GET /api/menu/items$"),
    "get_category_items": re.compile(r"GET /api/menu/([a-zA-Z]+)/items$"),
    "get_items_identifiant": re.compile(r"GET /api/menu/items/([1-3][0-9]?|[1-4][0]?){1}$"),
    "post_commandes_items": re.compile(r"POST /api/commandes (.+)$"),

    "get_commandes": re.compile(r"GET /api/commandes$"),
    "get_commandes_identifiant": re.compile(r"GET /api/commandes/(\d+)$"),
    "put_items_identifiant": re.compile(r"PUT /api/menu/items/([1-3][0-9]?|[1-4][0]?){1} disponible=([0-1]{1})$"),

    "get_comptes": re.compile(r"GET /api/comptes$"),
    "get_comptes_matricule": re.compile(r"GET /api/comptes/(\d{6,8})$"),
    "put_matricule_actif": re.compile(r"PUT /api/comptes/(\d{6,8}) ([0-1]{1})$")
}


def f_money(val):
    '''
    Fontionnalité: afficher une valeur en $
    Paramètres: val --> la valeur qu'on veut écrire en $
    '''
    return f"{'{:.2f} '.format(val)}$"


def regex_list(string,spacer=" "):
    '''
    Fonctionnalité: pour la requête POST /api/commandes --> split les commandes et voir si elles sont valides
    Paramètres: string (str)--> le string des commandes _x_
                spacer (str)--> ce qui sépare les différentes commandes
    Retourne une liste composée de sous-listes de chaque commande
    '''
    string=string.split(spacer)
    for e in string:
        e.strip()
    liste=[]
    regex = re.compile(r"(-?\d{1})x(-?\d{1})$")
    for i in string:
        liste.append([regex.search(i).group(1),regex.search(i).group(2)])
    return liste


def get_endpoint(request):
    '''
    Fonctionnalité: get les groups dans les requests (exemple une catégorie ou une matricule)
    Paramètre: request(str) --> la requête rentrée par l'utilisateur

    Retourne True si les groupes ont été trouvés et s'ils sont conformes aux normes
    Sinon, retourne False
    '''
    global count, recherche_group,regex2
    regex2=""
    cnt=0 
    recherche_group=[] #contiendra tous les groupes retrouvées
    x=False
    for i in API_ENDPOINTS: #pour chaque type de requete dans API_ENDPOINTS
        cnt+=1
        regex = API_ENDPOINTS[i]
        if regex.search(request): #si notre request match une requete dans API_ENDPOINTS
            count = cnt
            x=True
            regex2 = i
        for i in range (1,40): 
            try: 
                recherche_group.append(regex.search(request).group(i)) #on ajoute le groupe à recherche_group si cest conforme aux normes
                if cnt==4:
                    try:
                        recherche_group = regex_list(recherche_group[0],spacer=" ") 
                        try:
                            for k in recherche_group:
                                for i in range(0,len(k)):
                                    k[i]=int(k[i])
                                    if k[i]<=0:
                                        x=False
                                    else:
                                        continue
                        except:
                            continue
                    except:
                        x=False
            except:
                continue
    if x == False:
        return False
    else:
        return True


def search_compte(matricule="",mdp=""):
    '''
    FonctionnalitéL voir si le compte existe et si le mot de passe est bon. Si oui, est-il actif?
    Paramètres: matricule (str) --> matricule rentrée par l'utilisateur, sinon ""
                mdp (str) --> mot de passe rentré par l'utilisateur, sinon ""

    Retourne False si les informations du compte sont mal rentrés ou si le compte n'est plus actif.
    True sinon.
    '''
    global account
    with open('comptes.csv') as csv_file: 
        csv_reader = csv.reader(csv_file, delimiter="|")
        for row in csv_reader:  #for every row, we're going to check if the matricule exists and if the mdp entered matches it
            if (str(row[0])).strip(" ") == matricule.strip(" ") and (str(row[3])).strip(" ") == mdp.strip(" "):
                for i in row:
                    account.append(i.strip(" ")) #on va append toutes les infos du compte dans account en enlevant les espaces inutiles

        if account==[]: #si la matricule existait pas ou si le password didnt match
            print("Erreur. Matricule inexistante ou mot de passe erroné.")
            return False
        else: #si ca match
            if account[6]!= "1": #on vérifie si cest actif ou non
                print("Ce compte n'est plus actif. Veuillez contacter les administrateurs pour modifier le statut du compte.")
                account=[]
                return False
            else:
                print("Authentification validée.")
                return True


def authorized(acc): 
    '''
    Fonction pour voir si la requete est permise selon le role
    Paramètre: acc (list)--> le account de l'utilisateur

    Retourne False si la requête est non autorisée, True sinon.
    '''
    if len(acc)!=7:
        return False
    
    if account[5] == "public": #si cest public, avec ma variable globale count, ca indique cetait quel request dans API_ENDPOINTS
        if count<1 or count>5: #the range of requests for public
            print("Requête non authorisé avec votre rôle.") #on affiche si cest autorisé ou non. idem pour staff and admin
            return False
        else:
            return True
        
    elif account[5] == "staff":
        if count<5 or count>7:
            print("Requête non authorisé avec votre rôle.")
            return False
        else:
            return True
    
    elif account[5] == "admin":
        if count<7 or count>10:
            print("Requête non authorisé avec votre rôle.")
            return False
        else:
            return True
        
    else:
        return False
        

def GET_menu(command, inCategory=False):
    '''
    Fonctionnalité: dépendamment de command, on ira chercher les informations dans le menu qu'on veut
    Paramètres: command(str or int) --> ce qu'on veut --> "all" pour tout avoir, un str qui représente la catégorie pour avoir les infos de celle-ci ou un int pour un item en particulier
                inCategory=False --> arrêter la récursion (True quand la récursion entre dans un catégorie, False quand on sort)
    
    '''
    menu = open("menu.json","r")
    jsonObj = json.loads(menu.read())
    menu.close()
    def recursive_parser_items(jsonObj,command,inCategory):
        '''
        Fonctionnalité: fonction récursive permettant de print les données
        Paramètres: jsonObj --> dépendamment de l'étape de récursion à laquelle on est. Le plus grand étant le menu.json, le plus petit étant les listes "items"
                    command (str) --> "all", nom de la catégorie
                    inCategory --> booléen servant à gerer la récursion
        '''
        def printItems(item_list):
            for item in item_list:
                print(item["id"], item["nom"])

        if type(jsonObj) is list:
            if command == "all" or inCategory is True:
                printItems(jsonObj)
                return
    
        for key in jsonObj:
            if key == command: 
                inCategory = True
            if type(jsonObj) is list:
                return
            recursive_parser_items(jsonObj[key], command, inCategory)
            if key == command: inCategory = False
        return


    def recursive_parser_item(jsonObj,id):
        '''
        Fonctionnalité: fonction récursive permettant de print les données
        Paramètres: jsonObj --> dépendamment de l'étape de récursion à laquelle on est. Le plus grand étant le menu.json, le plus petit étant les listes "items"
                    command (int) --> id
        '''
        if type(jsonObj) is list:
            for item in jsonObj:
                if item["id"] == id:
                        print(item["id"], item["nom"],f_money(item["prix"]), item["disponible"])
                        break
            return

        for key in jsonObj:
            recursive_parser_item(jsonObj[key],id)

    if type(command) is str:
        recursive_parser_items(jsonObj,command,inCategory)
    elif type(command) is int:
        recursive_parser_item(jsonObj,command)


def PUT_menu(id, disponible):
    '''
    Fonctionnalité: changer la disponibilité d'un item dans menu.json par récursion
    Paramètre: id (str)--> l'identifiant de l'item à modifier
               disponible(str) --> 0 pour indisponible, 1 pour disponible
    '''
    menu = open("menu.json","r")
    jsonObj = json.loads(menu.read())
    menu.close()
    def recursive_edit_item(jsonObj,id): 
        '''
        Fontionnalité: trouver et modifier l'item
        '''
        if type(jsonObj) is list: #on boucle dans list jusqu'à retrouver notre item au id correspondant
            for item in jsonObj:
                if item["id"] == int(id): 
                        item["disponible"] = disponible #on change sa disponibilité
                        break
            return
        for key in jsonObj:
            recursive_edit_item(jsonObj[key],int(id))

    recursive_edit_item(jsonObj,id)
    menu = open("menu.json", 'w')
    json.dump(jsonObj, menu)
    menu.close()


def GET_orders(command):
    '''
    Fonctionnalité: dépendamment de command, on ira chercher de l'information dans commandes.csv
    Paramètres: command(str) --> "all" pour tout, sinon on ira chercher une commande en particulier 
    '''
    menu = open("menu.json","r")
    jsonObj = json.loads(menu.read())
    menu.close()
    def recursive_item_printer(jsonObj,id,qty):
        '''
        Fonctionnalité: print commande choisie
        '''
        if type(jsonObj) is list:
            for item in jsonObj:
                if item["id"] == id:
                        print(item["nom"], qty)
                        break
            return

        for key in jsonObj:
            recursive_item_printer(jsonObj[key],id,qty)

    with open("commandes.csv") as csv_file: 
        csv_reader = csv.reader(csv_file, delimiter="|")
        if command == "all": #tout afficher les informations des commandes
            for row in csv_reader:
                print("Commande:", row[0], "Date:", row[3], "Total:", f_money(float(row[4])))
        else: #pour une commande en particulier
            for row in csv_reader:
                if row[0].strip() == command:
                    print("Commande:", row[1], "Date:", row[3], "Total:", f_money(float(row[4])))
                    print("Items:")
                    items=regex_list(row[2].strip(), spacer=",")
                    for item in items:
                        recursive_item_printer(jsonObj,int(item[0]),int(item[1]))


def POST_orders(items,print): 
    '''
    Fonctionnalité: ajouter une nouvelle commande à commandes.csv
    Paramètre: items(list) --> une liste ayant comme sous-liste toutes les commandes
    Retourne le total de la commande
    '''
    global nb_commandes 
    nb_commandes+=1 #augmenter le numéro de commande 
    total = 0

    def recursive_item_price_calculator(jsonObj,items):
        '''
        Fonctionnalité: calculer le prix total d'une commande
        Paramètres: jsonObj --> dépendamment de l'étape de récursion à laquelle on est. Le plus grand étant le menu.json, le plus petit étant les listes "items"
                    items --> une liste ayant comme sous-liste toutes les commandes
        '''
        nonlocal total
        if type(jsonObj) is list:
            for menu_item in jsonObj: #calculer le prix total d'une commande en bouclant
                for item in items:
                        if menu_item["id"] == item[0]:
                            total += menu_item["prix"]*item[1]
            return

        for key in jsonObj:
            recursive_item_price_calculator(jsonObj[key],items)


    menu = open("menu.json","r")
    jsonObj = json.loads(menu.read())
    menu.close()
    recursive_item_price_calculator(jsonObj,items)


    data = ""
    for item in items:
        data = data + str(item[0]) + "x" + str(item[1]) + ", "
    data=data.strip(", ")

    new_order = [str(nb_commandes)+"  ", " "+account[0]+" ", " "+data+" ", " "+str(date.today())+" ", " "+str(f_money(total))]

    if print:
        with open('commandes.csv', 'a') as f_object: #this is going to write on a new line for the new order
            writer_object = writer(f_object,delimiter='|')
            writer_object.writerow(new_order)
            f_object.close()

        f = open("commandes.csv", "r+")
        lines = f.readlines()
        lines.pop()
        f.close()
        f=open("commandes.csv","w+")
        f.writelines(lines)
        f.close()


    return total

def print_comptes(id):
    '''
    Fonctionnalité: afficher les comptes voulus de comptes.csv
    Paramètre: id(str) --> "all" pour imprimer tous les comptes, une matricule pour un compte en particulier
    '''
    if id == "all":
        with open('comptes.csv') as csv_file:
            csv_reader = csv.reader(csv_file, delimiter="|")
            for row in csv_reader: #imprimer chaque row dans comptes.csv
                print(row)
    else:
        with open('comptes.csv') as csv_file: 
            csv_reader = csv.reader(csv_file, delimiter="|")
            for row in csv_reader: 
                if (str(row[0])).strip(" ") == id.strip(" "): #seulement imprimer si on a la matricule cherchée (id)
                    print(row)


def change_actif(id):
    '''
    Fonctionnalité: changer la section actif d'un compte
    Paramètre: id(str) --> matricule du compte à changer
    '''
    with open('comptes.csv', 'r') as csv_file:
        csv_reader = csv.reader(csv_file, delimiter='|')

        updated = []

        for row in csv_reader: 
            if str(row[0]).strip() == id.strip(): #si on trouve la matricule (id) voulue
                if str(row[6].strip()) == '0': #un compte inactif devient actif
                    row[6] = '1'
                elif str(row[6].strip()) == '1': #un compte actif devient inactif
                    row[6] = '0'
            updated.append(row) #on rajoutera chaque ligne (qu'elle ait changé ou non)

    with open('comptes.csv', 'w',newline='', encoding='utf-8') as csv_file: #on réécrit le fichier comptes.csv
        csv_writer = csv.writer(csv_file, delimiter='|')
        csv_writer.writerows(updated)

def program_input(program_args):
    '''
    Pour le bonus #2: Supporter le paramétrage du programme pour la connexion, permettant de démarrer le programme avec un utilisateur connecté
    Fonctionnalité: démarrer le programme avec un utilisateur connecté, sinon connection usuelle (fonction login qui inclut l'aspect paramétrage)
    Paramètre: program_args --> arguments rentrés lors de l'exécution du programme (in terminal)
    '''
    authen_list = []
    if len(program_args)==3: #il faut aavoir 3 arguments: le fichier, la matricule, le mdp
        if not search_compte(program_args[1], program_args[2]): 
            while len(authen_list)!=2 or search_compte(matricule,mdp)==False: 
                authentification = input("Entrez votre matricule et votre mot de passe: ")
                try:
                    authen_list = authentification.split()
                    matricule= authen_list[0].strip(" ")
                    mdp= authen_list[1].strip(" ")
                except:
                    continue   
    else: #connection usuelle
        while len(authen_list)!=2 or search_compte(matricule,mdp)==False:
            authentification = input("Entrez votre matricule et votre mot de passe: ")
            try:
                authen_list = authentification.split()
                matricule= authen_list[0].strip(" ")
                mdp= authen_list[1].strip(" ")
            except:
                continue

def main():
    '''
    Fonctionnalité: ensemble du programme 
    '''
    global regex2, recherche_group
    program_input(sys.argv)
    entree=""
    categorie = ["boisson","boisson_chaude","cafe","the","chocolat","boisson_froide","sandwich","regulier","wrap","fruit","viennoiserie","pain","chausson","croissant","muffin"]
    while entree.upper() != "FIN": #tant que l'utilisateur n'aura pas entré fin (peu importe comment c'est écrit)
        entree = input("Requête> ").strip() #demander une requête
        if entree.upper() == "FIN":
            break
        if get_endpoint(entree): #si les endpoints sont trouvés
            if authorized(account): #et que la requête est valide selon le rôle
                match regex2: #on va match la requête à l'action désirée
                    case "get_items":
                        GET_menu("all")

                    case "get_category_items":
                        if recherche_group[0].strip() in categorie: #il faut que la catégorie existe
                            GET_menu(recherche_group[0])
                        else:
                            print("Requête invalide.")

                    case "get_items_identifiant":
                        GET_menu(int(recherche_group[0]))

                    case "post_commandes_items":

                        POST_orders(recherche_group,True)

                    case "get_commandes":
                        GET_orders("all")

                    case "get_commandes_identifiant":
                        if 1<=int(recherche_group[0])<=nb_commandes:
                            GET_orders(recherche_group[0])
                        else:
                            print("Requête invalide.")


                    case "put_items_identifiant":
                        if int(recherche_group[1])==0:
                            recherche_group[1]="False"
                        else:
                            recherche_group[1]="True"
                        PUT_menu(recherche_group[0],recherche_group[1])

                    case "get_comptes":
                        print_comptes("all")

                    case "get_comptes_matricule":
                        print_comptes(recherche_group[0])

                    case "put_matricule_actif":
                        change_actif(recherche_group[0])
        else:
            print("La requête est mal formée ou n'existe pas.")

def test(): #tests unitaires
    def test_get_endpoints(): 
        #test pour la fonction get_endpoints()
        request = "GET /api/menu/sandwich/items"
        assert get_endpoint(request)==True, "Échec, la section catégorie ne devrait contenir que des lettres."
        request = "POST /api/commandes 3x4 2x-6"
        assert get_endpoint(request)==False, "Échec, une de vos commandes contient un nombre négatif ou égal à 0."
        request = "PUT /api/menu/items/4 disponible=0"
        assert get_endpoint(request)==True, "Échec, l'item doit être un chiffre entre 1 et 40 et/ou la disponibilité devrait être soit 0 ou 1."
        request = "GET /api/comptes"
        assert get_endpoint(request)==True, "Échec, get_endpoint n'a pas trouvé un endpoint conforme aux règles."
        request = "PUT /api/menu/items/41 disponible=0"
        assert get_endpoint(request)==False, "Échec, l'item doit être un chiffre entre 1 et 40 et/ou la disponibilité devrait être soit 0 ou 1."

    def test_POST_orders():
        #test pour la fonction post_orders()
        global account, nb_commandes
        account.append("20130405")
        items=[[1,1],[6,1]]
        assert POST_orders(items,False)==5.20, "Échec, le total de plusieurs items (de qté 1 chacun) n'est bien calculé"
        items=[[1,2],[6,3]]
        assert POST_orders(items,False)==13.40, "Échec, le total de plusieurs items (de qté quelconque) n'est bien calculé"
        items=[]
        assert POST_orders(items,False)==0, "Échec, le total d'une commande vide n'est pas bien calculé"
        items=[[1,0]]
        assert POST_orders(items,False)==0, "Échec, le total d'une commande avec un seul item de qté 0 n'est pas bien calculé"
        items=[[1,0],[40,1]]
        assert POST_orders(items,False)==2.80, "Échec, le total d'une commande avec un item de qté 0 n'est pas bien calculé"
        account=[]
        nb_commandes = 3

    
    def test_authorized():
        #test pour la fonction authorized()
        global account
        account=[]
        assert not authorized(account), "La fonction 'authorized' a authorisé un account vide"
        account=['20130405', 'Ai', 'Yan', 'yaPass_01', 'yan.ai@umontreal.ca', 'public', '1', '!']
        assert not authorized(account), "La fonction 'authorized' a authorisé un account de format trop longue"
        account=['20130405', 'Ai', 'Yan', 'yaPass_01', 'yan.ai@umontreal.ca', 'president', '1']
        assert not authorized(account), "La fonction 'authorized' a authorisé un account de role invalide"
        account=['20130405', 'Ai', 'Yan', 'yaPass_01', 'yan.ai@umontreal.ca']
        assert not authorized(account), "La fonction 'authorized' a authorisé un account de format trop courte"
        account=['20130405', 'Ai', 'Yan', 'yaPass_01', 'yan.ai@umontreal.ca', 10, '1']
        assert not authorized(account), "La fonction 'authorized' a authorisé un account de role de type invalide"
        account=[] #vider account pour pouvoir appeler main()

    test_get_endpoints()
    test_POST_orders()
    test_authorized()
    print("----------------------------------------")
    
test()
main()  
