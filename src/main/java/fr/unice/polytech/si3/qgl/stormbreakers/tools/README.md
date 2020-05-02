# Consignes d’utilisations - Tools

## Runner

- Pour changer de carte, remplacer le numéro de semaine correspondant à la carte voulue dans la constante WEEK_NUM de la classe Engine. S’il n’est pas disponible, ajouter les fichiers game.json et initgame.json nécessaires dans le chemin de ressources tools/runner/week<nb>. 

- Après l’affichage du rendu, cet outil s’utilise comme l’outil de visualisation (pointage et zoom)

## Visuals

- Pour changer de preview, remplacer le contenu de output.bump  par output_bump.txt généré lors des rendus de livraison, ou par le webrunner pour la semaine/carte considérée. 

- Pour connaitre la position à un emplacement donné de la visualisation, il suffit d’un clic simple à l’emplacement considéré pour voir affiché en bas à gauche la position souhaitée. 
Un simple clic molette permet de retirer le point. 

- Pour effectuer un zoom il faut, tout en maintenant shift, faire un premier clic gauche pour démarrer une sélection, puis un clic droit pour compléter la sélection. 
La combinaison Shift + Clic molette permet de revenir à la vue initiale. 

Note : On ne peut pas cumuler des zooms, il faut obligatoirement revenir à l’état initial pour pouvoir redémarrer une sélection.