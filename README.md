# Rapport du TP 3 – MapReduce avec Akka réalisé par Fatima-ezzahra MAJIDI sous l'encadrement du professeur Lionel Seinturier

## Introduction
Le présent rapport décrit le travail réalisé dans le cadre du TP 3 sur le thème du MapReduce avec Akka. L'objectif principal de ce projet était de développer une application Spring fournissant un service de comptage de mots à l'aide du paradigme MapReduce implémenté avec des acteurs Akka.

## Architecture du Projet
L'architecture du projet se compose d'un service AkkaService, de trois acteurs Mapper et de deux acteurs Reducer. Le service AkkaService est responsable de l'initialisation de l'architecture en créant les acteurs Mapper et Reducer, de la distribution des lignes du fichier à chaque acteur Mapper, et de l'interrogation des acteurs Reducer pour obtenir le nombre d'occurrences d'un mot.

## Implémentation
Le code de l'application est structuré de manière à respecter les exigences du TP. Voici un aperçu des principales fonctionnalités implémentées :

 1.Initialisation de l'architecture :

Le service AkkaService initialise l'architecture en créant les acteurs Mapper et Reducer nécessaires.

 2.Distribution des lignes du fichier :
Le service AkkaService distribue les lignes du fichier alternativement à chaque acteur Mapper pour le traitement.

 3.Acteurs Mapper et Méthode de Partitionnement :
Dans la classe ActeurMapper, chaque instance représente un acteur responsable de la tâche de mappage dans le cadre du paradigme MapReduce avec Akka. Son rôle est de recevoir les lignes de texte à traiter, de les diviser en mots, puis d'envoyer chaque mot au réducteur approprié pour le comptage.

La méthode de partitionnement utilisée pour déterminer quel réducteur doit recevoir un mot est implémentée dans la méthode partition(String word). Cette méthode prend en compte le mot à mapper et utilise une opération de hachage pour sélectionner dynamiquement le réducteur correspondant. Le choix du réducteur est effectué de manière équilibrée en fonction de la valeur de hachage du mot, garantissant ainsi une distribution uniforme des mots entre les réducteurs disponibles.

 4.Interrogation des acteurs Reducer : 
Le service AkkaService interroge les acteurs Reducer pour obtenir le nombre d'occurrences d'un mot. Le choix de l'acteur Reducer se fait à l'aide d'une méthode de partition basée sur un tableau de Reducer et un mot donné.

 5.Réinitialisation des acteurs : 
Une méthode resetActors() a été ajoutée au service AkkaService pour initialiser des acteurs à chaque fois qu'une nouvelle analyse de texte est effectuée. Cela garantit un ensemble propre d'acteurs pour chaque analyse.

## Démonstration
 
 1. Accès:
    
![image](https://github.com/DuklinDonut/TP_3_CAR/assets/86496857/fcf094cf-11d1-41ca-b234-bd913df2dc51)
 
 2. Choisir un fichier (pour cet exemple j'ai choisit l'énoncé du TP):
    
![image](https://github.com/DuklinDonut/TP_3_CAR/assets/86496857/33997ade-3b7c-4de3-9fdd-e269be03d931)

 3. Résultats en log:
 
![image](https://github.com/DuklinDonut/TP_3_CAR/assets/86496857/05895c2a-ff20-418f-ac23-ee40ce127eeb)
![image](https://github.com/DuklinDonut/TP_3_CAR/assets/86496857/6c5e7a5c-cf18-4ced-bef4-ce295e907abf)
 
 4. Exemple de comptage du nombre d'occurrences:
    
![image](https://github.com/DuklinDonut/TP_3_CAR/assets/86496857/4a42a831-3622-4df3-8f83-40661168fb14)


## Conclusion
Ce rapport résume le travail effectué dans le cadre du TP 3 sur le MapReduce avec Akka. L'application développée démontre une mise en œuvre réussie du paradigme MapReduce avec des acteurs Akka dans le contexte d'un service de comptage de mots.
