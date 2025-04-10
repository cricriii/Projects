document.addEventListener('DOMContentLoaded', () => {
    // Connexion aux événements SSE (pour socket.io)
    const eventSource = new EventSource('http://localhost:3000/events');

    eventSource.onmessage = function(event) {
        console.log('Mise à jour reçue du serveur:', event.data);
        chargerTuiles();
    };

    // Gestion des tuiles
    const boutonAjouterTuile = document.getElementById('bouton-ajouter-tuile');
    const modalTuile = document.getElementById('modal-tuile');
    const boutonFermer = document.querySelector('.bouton-fermer');
    const formulaireTuile = document.getElementById('formulaire-tuile');
    const conteneurTuiles = document.getElementById('conteneur-tuiles');
    const conteneurPagination = document.getElementById('conteneur-pagination');
    const sectionTuiles = document.getElementById('section-tuiles');
    const formulaireInscription = document.getElementById('formulaire-inscription');
    const formulaireConnexion = document.getElementById('formulaire-connexion');
    const sectionConnexionInscription = document.getElementById('section-connexion-inscription');
    const searchInput = document.getElementById('search-input');
    const filterCategory = document.getElementById('filter-category');
    const logoutButton = document.getElementById('logout-button');

    let tuiles = [];
    let pageActuelle = 1;
    const tuilesParPage = 15;
    let token = null;
    let isAdmin = false;
    let userId = null;

    // Vérifie si l'utilisateur est connecté
    function estConnecte() {
        return !!token;
    }

    // Fonction pour gérer la déconnexion
    function deconnexion() {
        token = null;
        localStorage.removeItem('token'); // Si vous utilisez le stockage local pour stocker le token
        mettreAJourInterface();
        alert('Déconnexion réussie.');
    }

    // Ajout de l'événement au bouton de déconnexion
    logoutButton.addEventListener('click', deconnexion);

    // Mise à jour de l'interface utilisateur en fonction de l'état de connexion
    function mettreAJourInterface() {
        if (estConnecte()) {
            boutonAjouterTuile.style.display = 'block';
            sectionTuiles.style.display = 'block';
            sectionConnexionInscription.style.display = 'none';
            logoutButton.style.display = 'inline';
            chargerTuiles();
        } else {
            boutonAjouterTuile.style.display = 'none';
            sectionTuiles.style.display = 'none';
            sectionConnexionInscription.style.display = 'block';
            logoutButton.style.display = 'none';
        }
    }

    // Ouvre la modal pour ajouter une nouvelle tuile
    boutonAjouterTuile.addEventListener('click', () => {
        ouvrirModal();
    });

    // Ferme la modal
    boutonFermer.addEventListener('click', () => {
        fermerModal();
    });

    window.addEventListener('click', (event) => {
        if (event.target == modalTuile) {
            fermerModal();
        }
    });

    // Soumet le formulaire pour ajouter ou mettre à jour une tuile
    formulaireTuile.addEventListener('submit', (event) => {
        event.preventDefault();
        const donneesTuile = {
            title: document.getElementById('titre').value,
            date: document.getElementById('date').value,
            category: document.getElementById('categorie').value,
            description: document.getElementById('description').value,
        };
        if (document.getElementById('id-tuile').value) {
            mettreAJourTuile(document.getElementById('id-tuile').value, donneesTuile);
        } else {
            ajouterTuile(donneesTuile);
        }
        fermerModal();
    });

    // Ouvre la modal avec les données d'une tuile
    function ouvrirModal(donneesTuile = {}) {
        document.getElementById('id-tuile').value = donneesTuile._id || '';
        document.getElementById('titre').value = donneesTuile.title || '';
        document.getElementById('date').value = donneesTuile.date || '';
        document.getElementById('categorie').value = donneesTuile.category || '';
        document.getElementById('description').value = donneesTuile.description || '';
        modalTuile.style.display = 'block';
    }

    // Ferme la modal
    function fermerModal() {
        modalTuile.style.display = 'none';
    }

    // Ajoute une nouvelle tuile
    function ajouterTuile(donneesTuile) {
        fetch('http://localhost:3000/api/tiles', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(donneesTuile)
        })
            .then(response => response.json())
            .then(nouvelleTuile => {
                tuiles.push(nouvelleTuile);
                afficherTuiles();
                mettreAJourCategories();
            })
            .catch(error => console.error('Erreur:', error));
    }

    // Met à jour une tuile existante
    function mettreAJourTuile(id, donneesTuile) {
        fetch(`http://localhost:3000/api/tiles/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(donneesTuile)
        })
            .then(response => response.json())
            .then(tuileMiseAJour => {
                const index = tuiles.findIndex(tuile => tuile._id === tuileMiseAJour._id);
                if (index !== -1) {
                    tuiles[index] = tuileMiseAJour;
                    afficherTuiles();
                    mettreAJourCategories();
                }
            })
            .catch(error => console.error('Erreur:', error));
    }

    // Pour supprimer une tuile
    function supprimerTuile(idTuile) {
        fetch(`http://localhost:3000/api/tiles/${idTuile}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(() => {
                tuiles = tuiles.filter(tuile => tuile._id !== idTuile);
                afficherTuiles();
            })
            .catch(error => console.error('Erreur:', error));
    }

    // Ajoute un écouteur d'événement pour la recherche par titre
    searchInput.addEventListener('input', () => {
        afficherTuiles();
    });

    // Ajoute un écouteur d'événement pour le filtrage par catégorie
    filterCategory.addEventListener('change', () => {
        afficherTuiles();
    });

    // Affiche les tuiles avec pagination, recherche et filtrage
    function afficherTuiles() {
        conteneurTuiles.innerHTML = '';
        const startIndex = (pageActuelle - 1) * tuilesParPage;
        const endIndex = startIndex + tuilesParPage;

        const rechercheTitre = searchInput.value.toLowerCase();
        const categorieFiltre = filterCategory.value;

        const tuilesFiltres = tuiles.filter(tuile => {
            const correspondTitre = tuile.title.toLowerCase().includes(rechercheTitre);
            const correspondCategorie = categorieFiltre ? tuile.category === categorieFiltre : true;
            return correspondTitre && correspondCategorie;
        });

        const tuilesActuelles = tuilesFiltres.slice(startIndex, endIndex);

        tuilesActuelles.forEach(tuile => {
            const elementTuile = document.createElement('div');
            elementTuile.className = 'tuile';

            // Formater la date
            const date = new Date(tuile.date);
            const options = { year: 'numeric', month: 'long', day: 'numeric' };
            const dateFormatee = date.toLocaleDateString('fr-FR', options);

            elementTuile.innerHTML = `
                <h3>${tuile.title}</h3>
                <p>${dateFormatee}</p>
                <p>${tuile.category}</p>
                <p>${tuile.description}</p>
                ${isAdmin || tuile.userId === userId ? `
                <button class="btn-modifier" onclick="modifierTuile('${tuile._id}')">Modifier</button>
                <button class="btn-supprimer" onclick="supprimerTuile('${tuile._id}')">Supprimer</button>
                ` : ''}
            `;
            conteneurTuiles.appendChild(elementTuile);
        });

        afficherPagination();
    }

    // Met à jour les options de catégories dans le menu déroulant
    function mettreAJourCategories() {
        const categories = [...new Set(tuiles.map(tuile => tuile.category))];

        filterCategory.innerHTML = '<option value="">Toutes les catégories</option>';
        categories.forEach(categorie => {
            const option = document.createElement('option');
            option.value = categorie;
            option.textContent = categorie;
            filterCategory.appendChild(option);
        });
    }

    // Affiche les boutons de pagination
    function afficherPagination() {
        conteneurPagination.innerHTML = '';
        const totalPages = Math.ceil(tuiles.length / tuilesParPage);

        for (let i = 1; i <= totalPages; i++) {
            const boutonPage = document.createElement('button');
            boutonPage.textContent = i;
            if (i === pageActuelle) {
                boutonPage.classList.add('active');
            }
            boutonPage.addEventListener('click', () => {
                pageActuelle = i;
                afficherTuiles();
            });
            conteneurPagination.appendChild(boutonPage);
        }
    }

    window.modifierTuile = function (idTuile) {
        const tuile = tuiles.find(t => t._id === idTuile);
        if (tuile) {
            ouvrirModal(tuile);
        }
    };

    window.supprimerTuile = function (idTuile) {
        if (confirm('Êtes-vous sûr de vouloir supprimer cette tuile?')) {
            supprimerTuile(idTuile);
        }
    };

    // Trier par titre ascendant
    document.getElementById('sort-title-asc').addEventListener('click', () => {
        tuiles.sort((a, b) => a.title.localeCompare(b.title));
        afficherTuiles();
    });

    // Trier par titre descendant
    document.getElementById('sort-title-desc').addEventListener('click', () => {
        tuiles.sort((a, b) => b.title.localeCompare(a.title));
        afficherTuiles();
    });

    // Trier par date ascendante
    document.getElementById('sort-date-asc').addEventListener('click', () => {
        tuiles.sort((a, b) => new Date(a.date) - new Date(b.date));
        afficherTuiles();
    });

    // Trier par date descendante
    document.getElementById('sort-date-desc').addEventListener('click', () => {
        tuiles.sort((a, b) => new Date(b.date) - new Date(a.date));
        afficherTuiles();
    });

    function chargerTuiles() {
        fetch('http://localhost:3000/api/tiles', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => response.json())
            .then(data => {
                tuiles = data;
                afficherTuiles();
            })
            .catch(error => console.error('Erreur:', error));
    }

    // Gestion de l'inscription
    document.getElementById('formulaire-inscription').addEventListener('submit', async (e) => {
        e.preventDefault();
        const email = document.getElementById('email-inscription').value;
        const password = document.getElementById('password-inscription').value;
        const isAdmin = document.getElementById('admin-checkbox').checked;

        try {
            const response = await fetch('http://localhost:3000/api/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password, isAdmin })
            });

            if (response.ok) {
                alert('Inscription réussie');
                const data = await response.json();
                token = data.token;
                isAdmin = JSON.parse(atob(token.split('.')[1])).role === 'admin';
                userId = JSON.parse(atob(token.split('.')[1])).userId;
                localStorage.setItem('token', token);
                mettreAJourInterface();
                chargerTuiles();
            } else {
                const errorText = await response.text();
                alert('Erreur lors de l\'inscription: ' + errorText);
            }
        } catch (error) {
            console.error('Erreur lors de l\'inscription:', error);
        }
    });

    // Gestion de la connexion
    document.getElementById('formulaire-connexion').addEventListener('submit', async (e) => {
        e.preventDefault();
        const email = document.getElementById('email-connexion').value;
        const password = document.getElementById('password-connexion').value;

        try {
            const response = await fetch('http://localhost:3000/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            });

            if (response.ok) {
                const data = await response.json();
                token = data.token;
                isAdmin = JSON.parse(atob(token.split('.')[1])).role === 'admin';
                userId = JSON.parse(atob(token.split('.')[1])).userId;
                localStorage.setItem('token', token);
                mettreAJourInterface();
                chargerTuiles();
            } else {
                const errorText = await response.text();
                alert('Erreur lors de la connexion: ' + errorText);
            }
        } catch (error) {
            console.error('Erreur lors de la connexion:', error);
        }
    });

    // Vérifie s'il y a un token dans le localStorage
    token = localStorage.getItem('token');
    if (token) {
        isAdmin = JSON.parse(atob(token.split('.')[1])).role === 'admin';
        userId = JSON.parse(atob(token.split('.')[1])).userId;
        mettreAJourInterface();
    }
});
