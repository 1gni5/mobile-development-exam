# Auteurs
DUCANGE Jules
CHEMIN Bastien

## Spécifications
L'application se compose de plusieurs parties:
- Un écran d'acceuil/connexion
- Un écran d'inscription
- Un écran de sélection de difficulté
- L'écran de jeu

### Acceuil

le joueur peut se connecter ou s'inscrire. 

__Si le joueur est déjà connecté__: il est redirigé vers le menu de choix de difficulté de sa partie.

__Si le joueur n'est pas encore connecté__: si le joueur à un compte il se connecte à  celui-ci, 
si  il n'en possède pas il est invité à s'inscrire en cliquant sur la mention "Pas de compte ? Clique ici !".

__Le  joeur ayant cliqué sur la mention__: est redirigé vers la page de création de compte. Une fois toute les informations renseignées,
au clique sur le bouton "S'inscrire", il est directement redirigé vers le menu de selection de difficulté.

### Sélection de la difficulté : 

Sur cette page, le joueur peut choisir un mode de jeu (facile, difficile, expert ou chrono).
Au clique sur un mode, il est alors redirigé sur l'activité de jeu choisie, laquelle est chargée différement en fonction du mode.
Aussi il peut reprendre une potentielle partie précedente qu'il avait mis en pause.
Sur cette page, le joueur peut aussi consulter son classement.

### En Jeu

__Suivant de mode de jeu séléctionné seul la difficulté augmente__: Dans chaque mode de jeu, la machine montre d'abord la séquence que l'utilisateur devra répeter ensuite.
Si le joueur commet une erreur, il perd une vie. Lorsque l'utilisateur n'a plus de vie, il perd. 
Le score du joueur est enregistré lorsqu'il complète un niveau.
Une fois une partie perdu, une nouvelle recommence.
En recommençant, le joueur efface le score de la partie précédente mais evidemment pas son meilleur score.


