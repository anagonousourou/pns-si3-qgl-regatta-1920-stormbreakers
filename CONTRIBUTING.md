# Stratégie de Branching - projet QGL StormBreakers

![Drapeau des Stormbreakers](flag.png)

## Table des matières

* [Master](#master)
* [Release](#release)
* [Feature](#feature)
* [Hotfix](#hotfix)

### Master

Seuls les releases et correctifs sont autorisés à modifier cette branche. Interdiction de développer directement dessus.

### Release

Convention de nommage des branches releases : **release/...** "..." étant le nom de la release.

* Une branche de type release peut être modifiée à n'importe quel moment.
* Avant le merge vers master, attendre que les autres contributeurs acceptent la pull request.
  * :exclamation: Ne sera mergée à master que lorsqu'elle sera finie, documentée, et complétement testée.
  * Le commit qui déclenchera le merge doit être taggé avec le bon nom de version.  

### Feature

Convention de nommage des branches features : **feature/...** "..." étant le nom du nouveau feature.

* Une ou plusieurs features peuvent soummettre une pull request à une branche de type release.
* Peut engendrer une pull request à n'importe quel moment vers la release correspondante.
* Avant le merge vers une release, attendre que les autres contributeurs acceptent la pull request.

### Hotfix

Convention de nommage des branches correctifs : **hotfix/...** "..." étant le nom du correctif à corriger.

* Un correctif peut soumettre une pull request vers n'importe quelle branche.
* Avant le merge vers la branche souhaitée, attendre que les autres contributeurs acceptent la pull request.
