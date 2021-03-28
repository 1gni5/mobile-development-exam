# README
DUCANGE Jules
CHEMIN Bastien

## L'Application
L'application se compose de plusieurs parties:
- Un écran d'acceuil/connexion
- Un écran d'inscription
- Un écran de sélection de difficulté
- L'écran de jeu

### Acceuil/Connexion
Sur cette page l'utilisateur peux se connecter à l'aide son _email_ ainsi que de son _mot de passe_.

__Une fois connecté__: il est redirigé vers le menu de choix de difficulté.

__Si l'utilisateur n'a pas de compte__: Si l'utilisateur n'a pas de compte il est invité à un créer un en cliquant sur le lien "Pas de compte ? Clique ici !".
Il sera alors redirigé vers la page d'inscription (Voir __Inscription__).

### Inscription
Sur cette page l'utilisateur peux se créer un compte. Pour cela il lui faudra remplir __tout__ les champs du formulaire, si un champs n'est pas renseigné, l'inscription se bloquera indiquant à l'utilisateur le champs à remplir. 

#### Date de naissance
Parmi les champs du formulaire d'inscription se trouve un champs réservé à la date de naissance. L'utilisateur peut remplir celui-ci à l'aide de l'icône de calendrier, cette icône ouvrira un calendrier sur lesquel l'utilisateur pourra choisir une date. L'utilisateur peux également entrer une date de naissance à l'aide du clavier mais celle-ci ne sera accepté uniquement si elle est donnée au format : "jj/mm/aa".

### Sélection de la difficulté : 

Sur cette page, le joueur peut choisir un mode de jeu (facile, difficile ou expert). Une fois le niveau de difficulté choisi la partie est lancé et le joueur est redirigé sur l'activité de jeu.

#### Reprendre
Si le joueur possède une sauvegarde associée à son identifiant il lui sera proposé de la reprendre en cliquant sur le bouton "Reprendre". Si une partie est gagné sa sauvegarde est supprimé est il ne sera pas possible de reprendre celle-ci après coup. 

> Note: L'application est exécutée sur l'émulateur d'Android Studio la suppression de la partie ne se fait que de manière locale et la sauvegarde n'est pas supprimé de FireBase. De ce fait le bouton "Reprendre" réapparait si l'on relance l'application. Il est a noté que le `OnSuccessListener` attaché à la méthode `delete()` s'exécute correctement.

### En Jeu
Le fonctionnement du jeu est conforme au fonctionnement décrit dans le sujet fourni en début de projet.

#### Score et Highscore
Le score est comparé au highscore quand le joueur gagne la partie, si le score obtenu est plus grand que celui enregistré dans FireBase celui-ci est mis à jour. Le meilleur score n'est pas associé à une sauvegarde mais à un joueur, il est donc complétement indépendants de la sauvegarde actuelle du joueur.

> Si le joueur perd alors qu'il n'a plus de vie son score est remit à zéro.


