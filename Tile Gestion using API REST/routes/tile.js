// routes/tile.js
const express = require('express');
const Tile = require('../models/tile');
const router = new express.Router();

// Créer une nouvelle tuile
router.post('/tiles', async (req, res) => {
    const tile = new Tile(req.body);
    try {
        await tile.save();
        res.status(201).send(tile);
    } catch (e) {
        res.status(400).send(e);
    }
});

// Lister toutes les tuiles
router.get('/tiles', async (req, res) => {
    try {
        const tiles = await Tile.find({});
        res.send(tiles);
    } catch (e) {
        res.status(500).send(e);
    }
});

// Afficher une tuile par ID
router.get('/tiles/:id', async (req, res) => {
    const _id = req.params.id;
    try {
        const tile = await Tile.findById(_id);
        if (!tile) {
            return res.status(404).send();
        }
        res.send(tile);
    } catch (e) {
        res.status(500).send(e);
    }
});

// Supprimer une tuile par ID
router.delete('/tiles/:id', async (req, res) => {
    const _id = req.params.id;
    try {
        const tile = await Tile.findByIdAndDelete(_id);
        if (!tile) {
            return res.status(404).send();
        }
        res.send(tile);
    } catch (e) {
        res.status(500).send(e);
    }
});

module.exports = router;