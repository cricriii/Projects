const codeGenetiqueAbrege = {
    'UUU': 'Phe', 'UUC': 'Phe', 'UUA': 'Leu', 'UUG': 'Leu',
    'CUU': 'Leu', 'CUC': 'Leu', 'CUA': 'Leu', 'CUG': 'Leu',
    'AUU': 'Ile', 'AUC': 'Ile', 'AUA': 'Ile', 'AUG': 'Met',
    'GUU': 'Val', 'GUC': 'Val', 'GUA': 'Val', 'GUG': 'Val',

    'UCU': 'Ser', 'UCC': 'Ser', 'UCA': 'Ser', 'UCG': 'Ser',
    'CCU': 'Pro', 'CCC': 'Pro', 'CCA': 'Pro', 'CCG': 'Pro',
    'ACU': 'Thr', 'ACC': 'Thr', 'ACA': 'Thr', 'ACG': 'Thr',
    'GCU': 'Ala', 'GCC': 'Ala', 'GCA': 'Ala', 'GCG': 'Ala',

    'UAU': 'Tyr', 'UAC': 'Tyr',
    'CAU': 'His', 'CAC': 'His', 'CAA': 'Gln', 'CAG': 'Gln',
    'AAU': 'Asn', 'AAC': 'Asn', 'AAA': 'Lys', 'AAG': 'Lys',
    'GAU': 'Asp', 'GAC': 'Asp', 'GAA': 'Glu', 'GAG': 'Glu',

    'UGU': 'Cys', 'UGC': 'Cys', 'UGG': 'Trp',
    'CGU': 'Arg', 'CGC': 'Arg', 'CGA': 'Arg', 'CGG': 'Arg',
    'AGU': 'Ser', 'AGC': 'Ser', 'AGA': 'Arg', 'AGG': 'Arg',
    'GGU': 'Gly', 'GGC': 'Gly', 'GGA': 'Gly', 'GGG': 'Gly'
};

function showAminoAcid() {
    const nucleotide1 = document.getElementById('nucleotide1').value;
    const nucleotide2 = document.getElementById('nucleotide2').value;
    const nucleotide3 = document.getElementById('nucleotide3').value;
    const codon = nucleotide1 + nucleotide2 + nucleotide3;
    const aminoAcid = codeGenetiqueAbrege[codon];

    const displayAminoAcid = document.getElementById('amino-acid-display');
    displayAminoAcid.innerHTML = '';
    if (aminoAcid) {
        const img = document.createElement('img');
        img.src = `Images/${aminoAcid}.png`;
        img.alt = aminoAcid;
        displayAminoAcid.appendChild(img);
    } else {
        displayAminoAcid.textContent = 'STOP';
    }
}

document.getElementById('nucleotide1').addEventListener('change', showAminoAcid);
document.getElementById('nucleotide2').addEventListener('change', showAminoAcid);
document.getElementById('nucleotide3').addEventListener('change', showAminoAcid);

//Tous les acides aminés avec leur nom complet pour l'animation
const codeGenetiquePasAbrege = {
    'UUU': 'PhénylAlaninenine', 'UUC': 'PhénylAlaninenine', 'UUA': 'Leucine', 'UUG': 'Leucine',
    'CUU': 'Leucine', 'CUC': 'Leucine', 'CUA': 'Leucine', 'CUG': 'Leucine',
    'AUU': 'Isoleucine', 'AUC': 'Isoleucine', 'AUA': 'Isoleucine', 'AUG': 'Méthionine',
    'GUU': 'Valine', 'GUC': 'Valine', 'GUA': 'Valine', 'GUG': 'Valine',

    'UCU': 'Sérine', 'UCC': 'Sérine', 'UCA': 'Sérine', 'UCG': 'Sérine',
    'CCU': 'Proline', 'CCC': 'Proline', 'CCA': 'Proline', 'CCG': 'Proline',
    'ACU': 'Thréonine', 'ACC': 'Thréonine', 'ACA': 'Thréonine', 'ACG': 'Thréonine',
    'GCU': 'Alanine', 'GCC': 'Alanine', 'GCA': 'Alanine', 'GCG': 'Alanine',

    'UAU': 'Tyrosine', 'UAC': 'Tyrosine',
    'CAU': 'Histidine', 'CAC': 'Histidine', 'CAA': 'Glutamine', 'CAG': 'Glutamine',
    'AAU': 'Asparagine', 'AAC': 'Asparagine', 'AAA': 'Lysine', 'AAG': 'Lysine',
    'GAU': 'Aspartate', 'GAC': 'Aspartate', 'GAA': 'Glutamate', 'GAG': 'Glutamate',

    'UGU': 'Cystéine', 'UGC': 'Cystéine', 'UGG': 'Tryptophane',
    'CGU': 'Arginine', 'CGC': 'Arginine', 'CGA': 'Arginine', 'CGG': 'Arginine',
    'AGU': 'Sérine', 'AGC': 'Sérine', 'AGA': 'Arginine', 'AGG': 'Arginine',
    'GGU': 'Glycine', 'GGC': 'Glycine', 'GGA': 'Glycine', 'GGG': 'Glycine'
};

function animateRNASequence() {
    //Récupère la chaîne d'ARN entrée et enlève les espaces et convertit en majuscules
    const rnaSequence = document.getElementById('rna-sequence').value.replace(/\s+/g, '').toUpperCase();
    const animationSpeed = parseInt(document.getElementById('animation-speed').value, 10); // Vitesse de l'animation
    
    const display = document.getElementById('rna-animation-display');
    const table = document.getElementById('amino-acid-table');
    display.textContent = '';
    
    //Le tableau où vont être affichés les acides aminés
    table.innerHTML = '<table><thead><tr><th>Nom complet</th><th>Nom abrégé</th><th>Image</th></tr></thead><tbody></tbody></table>';
    const tbody = table.querySelector('tbody');
    
    let index = 0;

    //Fonction pour animer la chaîne d'ARN
    function animate() {
        if (index < rnaSequence.length) {
            //Récupère le codon actuel, c'est à dire 3 nucléotides
            const codon = rnaSequence.slice(index, index + 3);
            const aminoAcidComplet = codeGenetiquePasAbrege[codon];// Nom complet de l'acide aminé
            const aminoAcidAbbr = codeGenetiqueAbrege[codon];// Nom abrégé de l'acide aminé

            if (aminoAcidComplet && aminoAcidAbbr) {
                display.textContent = rnaSequence.slice(0, index) + `[${codon}]` + rnaSequence.slice(index + 3);
                
                //Nouvelle ligne dans le tableau pour l'acide aminé sélectionné
                const row = document.createElement('tr');
                row.innerHTML = `<td>${aminoAcidComplet}</td><td>${aminoAcidAbbr}</td><td><img src="Images/${aminoAcidAbbr}.png" alt="${aminoAcidComplet}" width="50"></td>`;
                tbody.appendChild(row);
                
                //Incrémente l'index de 3 pour passer au codon suivant
                index += 3;
            } else {
                //Si le codon n'est pas valide, passe au nucléotide suivant
                index += 1;
            }
            //Delai qui permet de créer l'effet d'animation
            setTimeout(animate, animationSpeed);
        }
    }
    animate();
}
document.getElementById('start-animation').addEventListener('click', animateRNASequence);


