# FullStackDev – Application backend CI/CD (Spring Boot)

## Description
**FullStackDev** est une application web backend développée avec **Java Spring Boot**, conçue pour démontrer un **pipeline CI/CD complet** intégrant compilation, tests, build, conteneurisation et déploiement automatisé sur AWS.

Ce projet illustre la mise en œuvre de bonnes pratiques **DevOps** et **backend Java**, en reproduisant un environnement de livraison continue professionnel.

---

## Technologies principales
- **Langage :** Java 17  
- **Framework :** Spring Boot  
- **Base de données :** PostgreSQL  
- **ORM :** Spring Data JPA, JdbcTemplate  
- **Versionnage BD :** Flyway  
- **Tests :** JUnit 5, Mockito  
- **CI/CD :** GitHub Actions, Maven, Jib, Docker, AWS  

---

##  Architecture du projet
1. **Compilation et tests** → Maven + GitHub Actions  
2. **Construction de l’image Docker** → Jib  
3. **Publication** → Docker Hub  
4. **Déploiement** → AWS (conteneur automatisé)

![Pipeline CI/CD](./docs/pipeline-architecture.png)  
*(Schéma illustratif du pipeline — à ajouter si tu as une image)*

---

## Fonctionnalités
- API RESTful pour la gestion des entités (CRUD)
- Configuration de pipelines automatisés CI/CD
- Migration versionnée de la base de données (Flyway)
- Tests unitaires et d’intégration automatisés

---

## Objectif du projet
Ce projet a pour but de :
- Mettre en pratique la **construction et le déploiement automatisé** d’une application Java.
- Illustrer les étapes d’un pipeline **CI/CD complet** de bout en bout.
- Démontrer la rigueur, la maintenabilité et la fiabilité d’un backend Spring Boot.

---

## Liens utiles
- **Docker Hub :** [faycaldr](https://hub.docker.com/repositories/faycaldr)  
- **LinkedIn :** [Fayçal Draoua Derbouz](https://www.linkedin.com/in/faycaldraoua)  

---

## Auteur
**Fayçal Draoua Derbouz**  
Étudiant en maîtrise – Intelligence et gestion des données (UQAM)  
Passionné par le développement backend, l’ingénierie des données et les pratiques DevOps.
