# Gestion des Compétitions de Pigeons Voyageurs 🕊️

## Contexte du Projet 🎯
Ce projet vise à gérer les compétitions organisées par la **Fédération Marocaine des Pigeons Voyageurs**, impliquant plusieurs éleveurs participant à des courses réparties sur trois étapes : **vitesse**, **demi-fond** et **fond**.  
L'objectif est de garantir l'intégrité de la compétition grâce à une application respectant des règles strictes et permettant une gestion optimale des événements.

---

## Fonctionnalités Principales 🚀

### Pour les Éleveurs 🦜
- **Enregistrement** :
  - Création de compte avec :
    - Nom unique de colombier.
    - Nom d’utilisateur et mot de passe.
    - Coordonnées GPS du colombier.
- **Gestion des pigeons** :
  - Ajout de pigeons :
    - Numéro de bague unique.
    - Sexe, âge, couleur, et image.

### Pour l'Organisateur 🧑‍🏫
- **Gestion des compétitions** :
  - Définir des compétitions avec :
    - Nom, coordonnées GPS du point de lâcher.
    - Date/heure de départ et distance prévisionnelle.
  - Ajouter des pigeons participants via leur numéro de bague.
  - Clôturer une compétition et déclencher le calcul des résultats.
- **Upload des données collectées** :
  - Heure d'arrivée et numéro de bague des pigeons.
- **Consultation et export des résultats** :
  - Génération de fichiers PDF des résultats.

---

## Calcul des Résultats 📊
1. **Collecte des données** :
   - Heure d’arrivée et numéro de bague des pigeons.
2. **Calcul de la distance** :
   - Utilisation de la **formule de Haversine** pour mesurer la distance entre deux points GPS.
3. **Calcul du temps de vol** :
   - Différence entre l’heure d’arrivée et l’heure de lâcher.
4. **Calcul de la vitesse** :
   - Vitesse = Distance parcourue / Temps de vol.
   - Ajustement avec un coefficient basé sur la distance moyenne des pigeons.
5. **Classement** :
   - Classement des pigeons selon la vitesse, de la plus rapide à la plus lente.
6. **Attribution des points** :
   - Points basés sur le classement et le pourcentage d’admission.
   - Cumul des points des 5 premiers pigeons par colombier pour un classement général.

---

## Affichage des Résultats 📈
- Résultats par course avec :
  - **Colombier**, **Numéro de bague**, **Heure d’arrivée**, **Distance**, **Vitesse**, **Points**.
- Classement général automatique pour consultation/export.

---

## Exigences Techniques ⚙️
- **Backend** : Développement de l’API avec **Spring Boot**.
- **Base de données** : **MongoDB**.
- **Architecture** :
  - Application structurée en plusieurs couches (Controller, Service, Repository).
- **Validation des données** : Obligatoire pour garantir l'intégrité des informations.
- **Gestion des exceptions** : Centralisée pour une meilleure gestion des erreurs.
- **Tests unitaires** : Requis pour assurer la fiabilité du code.
- **Configuration** : Fichier de configuration au format **YAML**.

---

## Installation et Lancement 🚀

### Prérequis 📋
- **Java 17+** ☕
- **Maven** 🔨
- **MongoDB** 🗄️



## Installation avec Docker 🐳

Si vous souhaitez utiliser Docker pour configurer l'application avec MongoDB et Mongo Express, voici les étapes à suivre.

### Docker Compose 📝

Vous pouvez utiliser le fichier `docker-compose.yml` pour configurer MongoDB et Mongo Express.

Lancez Docker Compose :

```bash
docker-compose up -d
```

## Architecture du Projet 🏗️

### Backend

Le backend de l'application est développé avec **Spring Boot**, un framework Java permettant de créer des applications web et des services RESTful de manière simple et rapide.

### Base de Données

La base de données utilisée est **MongoDB**, un système de gestion de base de données NoSQL avec un modèle de données flexible, qui s'adapte facilement aux besoins de l'application.

### Modèle de Données

Le modèle de données est géré à travers des **DTOs** (Data Transfer Objects) qui permettent de transmettre des données de manière structurée entre différentes couches de l'application.

### MapStruct

**MapStruct** est utilisé pour le mappage automatique entre les DTOs et les entités. Cela simplifie la conversion des objets et améliore la lisibilité du code.

### Lombok

**Lombok** est utilisé pour simplifier le code en générant automatiquement des méthodes getter, setter, des constructeurs, et autres méthodes utilitaires, afin de réduire la boilerplate code.

### Docker

Le projet utilise **Docker** pour faciliter l'exécution de MongoDB et de Mongo Express, une interface web permettant d'interagir facilement avec MongoDB.

## Prérequis 🛠️

Avant de démarrer l'application, assurez-vous d'avoir les éléments suivants installés :

- **Java 11 ou supérieur** : Vous pouvez télécharger [Java JDK](https://adoptopenjdk.net/).
- **Maven** : Utilisé pour la gestion des dépendances et la compilation du projet. Téléchargez-le [ici](https://maven.apache.org/).
- **Docker** : Utilisé pour exécuter MongoDB et Mongo Express dans des conteneurs. Installez Docker [ici](https://www.docker.com/products/docker-desktop).

## Lancer l'Application Spring Boot 🏃‍♂️

### 1. Clonez le dépôt

Clonez le projet depuis GitHub avec la commande suivante :

```bash
git clone https://github.com/erradaoumaimaa/PigeonSkyRace.git
```
## Lancer l'application Spring Boot 🏃‍♂️

### 1. Accédez au répertoire du projet

Déplacez-vous dans le répertoire du projet cloné :

```bash
cd PigeonSkyRace
```
## 2. Compilez le projet avec Maven

Utilisez Maven pour nettoyer et compiler le projet :

```bash
mvn clean install
```
