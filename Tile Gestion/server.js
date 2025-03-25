
const path = require('path');
const express = require('express');
const http = require('http');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const cors = require('cors');

const app = express();
const server = http.createServer(app);

app.use(bodyParser.json());
app.use(cors());
app.use(express.static(path.join(__dirname, 'public')));

// Connexion à la base de données MongoDB
async function connectToDatabase() {
    try {
        await mongoose.connect('mongodb://localhost:27017/gestion_tuiles',);
        console.log('Connecté à MongoDB');
    } catch (err) {
        console.error('Erreur de connexion à MongoDB:', err.message);
    }
}

connectToDatabase();

// Schéma et modèle pour les utilisateurs
const userSchema = new mongoose.Schema({
    email: { type: String, required: true, unique: true },
    password: { type: String, required: true },
    role: { type: String, default: 'user' }
});

const User = mongoose.model('User', userSchema);

// Schéma et modèle pour les tuiles
const tileSchema = new mongoose.Schema({
    title: { type: String, required: true, trim: true },
    date: { type: Date, required: true },
    category: { type: String, trim: true },
    description: { type: String, trim: true },
    userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true }
});

const Tile = mongoose.model('Tile', tileSchema);

let clients = [];

// Ajouter des clients à la liste des clients connectés
app.get('/events', (req, res) => {
    res.setHeader('Content-Type', 'text/event-stream');
    res.setHeader('Cache-Control', 'no-cache');
    res.setHeader('Connection', 'keep-alive');

    res.flushHeaders(); // Envoie les en-têtes immédiatement

    clients.push(res);

    req.on('close', () => {
        clients = clients.filter(client => client !== res);
    });
});

// Fonction pour envoyer les événements à tous les clients connectés
function sendEventsToAll(data) {
    clients.forEach(client => client.write(`data: ${JSON.stringify(data)}\n\n`));
}

// Inscription
app.post('/api/register', async (req, res) => {
    const { email, password, isAdmin } = req.body;
    const hashedPassword = await bcrypt.hash(password, 10);
    const role = isAdmin ? 'admin' : 'user';
    const user = new User({ email, password: hashedPassword, role });
    try {
        await user.save();
        const token = jwt.sign({ userId: user._id, role: user.role }, 'votre_clé_secrète');
        res.status(201).json({ token });
    } catch (err) {
        if (err.code === 11000) {
            res.status(400).send('Erreur d\'inscription: l\'email est déjà utilisé');
        } else {
            res.status(400).send('Erreur d\'inscription');
        }
    }
});

// Connexion
app.post('/api/login', async (req, res) => {
    const { email, password } = req.body;
    const user = await User.findOne({ email });
    if (user && await bcrypt.compare(password, user.password)) {
        const token = jwt.sign({ userId: user._id, role: user.role }, 'votre_clé_secrète');
        res.json({ token });
    } else {
        res.status(401).send('Erreur de connexion: email ou mot de passe incorrect');
    }
});

// Middleware d'authentification
const authenticate = async (req, res, next) => {
    const token = req.headers.authorization?.replace('Bearer ', '');
    if (!token) return res.status(401).send('Authentification requise');

    try {
        const decoded = jwt.verify(token, 'votre_clé_secrète');
        req.user = await User.findById(decoded.userId);
        if (!req.user) throw new Error();
        req.user.role = decoded.role;
        next();
    } catch (err) {
        res.status(401).send('Token invalide ou expiré');
    }
};

// Tuiles
app.post('/api/tiles', authenticate, async (req, res) => {
    const tile = new Tile({ ...req.body, userId: req.user._id });
    try {
        await tile.save();
        sendEventsToAll(tile); // Émet un événement pour mettre à jour les tuiles
        res.status(201).send(tile);
    } catch (err) {
        res.status(400).send('Erreur de création de tuile');
    }
});

app.get('/api/tiles', authenticate, async (req, res) => {
    try {
        const tiles = req.user.role === 'admin' ? await Tile.find({}) : await Tile.find({ userId: req.user._id });
        res.send(tiles);
    } catch (err) {
        res.status(500).send('Erreur de récupération des tuiles');
    }
});

app.get('/api/tiles/:id', authenticate, async (req, res) => {
    try {
        const tile = await Tile.findById(req.params.id);
        if (!tile) return res.status(404).send('Tuile non trouvée');
        if (tile.userId.toString() !== req.user._id.toString() && req.user.role !== 'admin') {
            return res.status(403).send('Accès interdit');
        }
        res.send(tile);
    } catch (err) {
        res.status(500).send('Erreur de récupération de la tuile');
    }
});

app.put('/api/tiles/:id', authenticate, async (req, res) => {
    try {
        const tile = await Tile.findById(req.params.id);
        if (!tile) return res.status(404).send('Tuile non trouvée');
        if (tile.userId.toString() !== req.user._id.toString() && req.user.role !== 'admin') {
            return res.status(403).send('Accès interdit');
        }
        Object.assign(tile, req.body);
        await tile.save();
        sendEventsToAll(tile); // Émet un événement pour mettre à jour les tuiles
        res.send(tile);
    } catch (err) {
        res.status(400).send('Erreur de mise à jour de la tuile');
    }
});

app.delete('/api/tiles/:id', authenticate, async (req, res) => {
    try {
        const tile = await Tile.findById(req.params.id);
        if (!tile) return res.status(404).send('Tuile non trouvée');
        if (tile.userId.toString() !== req.user._id.toString() && req.user.role !== 'admin') {
            return res.status(403).send('Accès interdit');
        }
        await tile.deleteOne();
        sendEventsToAll({ id: req.params.id, deleted: true }); // Émet un événement pour mettre à jour les tuiles
        res.send({ message: 'Tuile supprimée avec succès' });
    } catch (err) {
        res.status(500).send('Erreur de suppression de la tuile');
    }
});

app.post('/api/logout', authenticate, (req, res) => {
    res.send('Déconnexion réussie');
});

server.listen(3000, () => {
    console.log('Serveur démarré sur le port 3000');
});
