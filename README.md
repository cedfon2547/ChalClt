Mise en contexte
> On fait appel à votre équipe pour le développement d’un logiciel (ChalCLT) qui servira à réaliser le design
> de chalets en bois massif lamellé-croisé. Ce projet est inspiré d’une recherche en cours en collaboration
> avec l’entreprise SOKIO, qui a gracieusement accepté de fournir la problématique et les données.
> Les murs en bois massif lamellé-croisé (en anglais, CLT) diffèrent des murs standards puisqu’ils sont
> constitués de bois massifs. Un panneau CLT est composé de 3, 5, 7 ou 9 couches croisées de bois collées
> ensemble.
> Il est à noter qu’on fait l’acquisition de ces panneaux déjà collés/assemblés. Une entreprise de
> construction devra cependant les tailler aux bonnes dimensions, usiner des ouvertures et des rainures,
> de manière à pouvoir les assembler sur le chantier pour former un chalet.
> Le procédé actuel de l’entreprise consiste à faire le design du chalet dans un premier logiciel de design
> 3D (Revit), puis de transférer le design dans un second logiciel (AutoCAD) afin de générer les plans de
> découpage des différents panneaux. Malheureusement, le transfert d’un logiciel à l’autre implique un
> processus technique de reconception pour chaque mur ainsi que le toit – ce qui est un processus lent et
> fastidieux.
> Votre mission, si vous l’acceptez, consiste à produire un logiciel combinant les fonctionnalités des deux
> logiciels. Votre logiciel devra être en mesure d’informatiser le processus de design des quatre côtés
> (façade, arrière, gauche et droit) ainsi que le toit d’un chalet. Par la suite, il sera possible d’exporter
> automatiquement les différents panneaux en format STL.
>
> [Plus d'information](2023A_Projet_session.pdf)

## Contribution
> Je considère qu'il est important de noter que je n'avais jamais utilisé Java et encore moins Swing avant ce projet.

- Implémentation de l'environnement en 3D (from scratch!! Interdiction d'utiliser des dépendances comme JavaFX)
  - Implémentation de la structure des objets (mesh), camera, material, etc...
  - Transformation & Changement de système de coordonées
  - Rastérization
  - Projection
  - Source lumineuse
- Structure du modèle du domaine
- Construction des objets 3D et leurs manipulation
- Construction des STL, exportation & importation
  - Il faut noter que le moteur 3D à été développer de facon à être le plus générique possible et supporte tout type de structure 3D. Seulement, cela n'a pas été ajouter comme fonctionnalité puisque ce n'était pas dans les objectifs.
- Puis bin du stock en lien avec pas mal toute le reste
  > Voir commits au besoin

## Résultats
Bin satisfait. nous étions la seule équipe à avoir présentée une interface graphique en 3 dimensions. L'application a été sélectionner pour le concours, mais n'a malheureusement pas gagné.

## Compétences mise en pratique ou acquises
- Les bases de Java, ses concepts & son environnement de programmation 
- Beaucoup trop de mathématique relié au rendu en 3D 🥲
- Utilisation de Swing 😅
- Structuration d'un projet selon le principe du controleur unique de Larmann (pu certain du nom, mais c'était ce qui était demandé)
- Travail en équipe & communication (pas toujours facile...)
- Design pattern
- Puis bin d'autre chose que j'oubli forcément

Finalement, il s'agit d'un projet qui a été en général agréable à réaliser. L'interface graphique en 3 dimensions à été un bon défi à relevé et je suis plutôt satisfait du résulat même si cela est loin d'être parfait, il s'agit de ma première expérience avec le graphisme 3D, ainsi que son fonctionnement.

Le code n'a jamais été retouché depuis la remise finale. Aucune refactorisation n'a été faite et il aurait aussi été préférable de mieux commenter le projet en lien avec son fonctionnement (surtout le moteur de rendu 3D).

---
# Résultat final
<img src="images/ui_1.png"></img>
<img src="images/ui_2.png"></img>
<img src="images/ui_3.png"></img>
<img src="images/ui_4.png"></img>
