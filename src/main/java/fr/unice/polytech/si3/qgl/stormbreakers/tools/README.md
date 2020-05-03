# Consignes d’utilisations - Tools

## Runner

- Pour changer de carte, remplacer le numéro de semaine correspondant à la carte voulue dans la constante <i>WEEK_NUM</i> de la classe Engine. S’il n’est pas disponible, ajouter les fichiers <i>game.json</i> et <i>initgame.json</i> nécessaires dans le chemin de ressources <b>tools/runner/week<<i>nb</i>></b>. 

- Lancer l'outil à partir de la classe Engine.java à l'emplacement /runner

- Après l’affichage du rendu, cet outil s’utilise comme l’outil de visualisation (pointage et zoom)

## Visuals

- Pour changer de preview, remplacer le contenu de <i>output.bump</i> par <i>output_bump.txt</i> généré lors des rendus de livraison, ou par le webrunner pour la semaine/carte considérée. 

- Lancer l'outil à partir de la classe VisuBump.java à l'emplacement /visuals/bumps

- Pour connaitre la position à un emplacement donné de la visualisation, il suffit d’un <b>clic simple</b> à l’emplacement considéré pour voir affichée en bas à gauche la position souhaitée. 
Un simple <b>clic molette</b> permet de retirer le point. 

- Pour effectuer un zoom il faut, tout en maintenant <b>shift</b>, faire un premier <b>clic gauche</b> pour démarrer une sélection, puis un <b>clic droit</b> pour compléter la sélection. 
La combinaison <b>Shift + Clic molette</b> permet de revenir à la vue initiale. 

Note : On ne peut pas cumuler des zooms, il faut obligatoirement revenir à l’état initial pour pouvoir redémarrer une sélection.