# projet_info_reparti

## Exercice 1 : Inscription

### Q1. 

### MCD (Modèle Conceptuel des Données)

```mermaid
erDiagram
    FANFARON {
        string nom_utilisateur PK "Identifiant unique"
        string email "Unique"
        string password "Haché"
        string prenom
        string nom
        string genre "homme, femme, autre"
        string contraintes_alimentaires "aucune, végétarien, etc."
        datetime date_creation
        datetime derniere_connexion
        boolean est_admin
    }

    PUPITRE {
        string nom_pupitre PK "Clarinette, Trompette, etc."
    }

    COMMISSION {
        string nom_commission PK "Prestation, Artistique, etc."
    }

    EVENEMENT {
        int id_evenement PK
        string nom
        datetime horodatage
        int duree
        string lieu
        string description
        string type "atelier, répétition, prestation"
    }

    INSCRIPTION_EVENT {
        string instrument
        string statut "présent, absent, incertain"
    }

    FANFARON }o--o{ PUPITRE : "joue dans"
    FANFARON }o--o{ COMMISSION : "s'implique dans"
    FANFARON ||--o{ INSCRIPTION_EVENT : "s'inscrit"
    EVENEMENT ||--o{ INSCRIPTION_EVENT : "concerne"
    FANFARON ||--o{ EVENEMENT : "propose (si comm. prestation)"
```

### MLD

```mermaid
    erDiagram
    COMMISSION ||--o{ S_IMPLIQUE_COMMISSION : "dirige"
    PUPITRE ||--o{ APPARTIENT_PUPITRE : "contient"

    S_IMPLIQUE_COMMISSION }o--|| FANFARON : "concerne"
    APPARTIENT_PUPITRE }o--|| FANFARON : "joue"

    FANFARON {
        string nom_utilisateur PK "Identifiant unique"
        string email "Unique"
        string mot_de_passe "Haché"
        string prenom
        string nom
        string genre "homme, femme, autre"
        string contraintes_alimentaires
        datetime date_creation
        datetime derniere_connexion
        boolean est_admin
    }

    FANFARON ||--o{ EVENEMENT : "propose (Prestation) "
    FANFARON ||--o{ INSCRIPTION_EVENT : "participe"
    EVENEMENT ||--o{ INSCRIPTION_EVENT : "reçoit"

    EVENEMENT {
        int id_evenement PK
        string nom
        datetime horodatage
        int duree
        string lieu
        string description
        string type "atelier, répétition, prestation"
        string nom_organisateur FK "Ref: FANFARON "
    }

    INSCRIPTION_EVENT {
        string nom_utilisateur PK, FK "Ref: FANFARON"
        int id_evenement PK, FK "Ref: EVENEMENT"
        string instrument
        string statut
    }

    COMMISSION {
        int id_commission PK
        string nom_commission ""
    }
    PUPITRE {
        int id_pupitre PK
        string nom_pupitre ""
    }
    APPARTIENT_PUPITRE {
        string nom_utilisateur PK, FK
        int id_pupitre PK, FK
    }
    S_IMPLIQUE_COMMISSION {
        string nom_utilisateur PK, FK
        int id_commission PK, FK
    }
```

<b>FANFARON</b> (<u>nom_utilisateur</u>, email, mot_de_passe, prenom, nom, genre, contraintes_alimentaires, date_creation, derniere_connexion, est_admin)

#### Contraintes d'intégrité :

<b>Clé Primaire</b> : nom_utilisateur.  
<b>Unicité</b> : email doit être unique.  
<b>Obligation (NOT NULL)</b> : nom_utilisateur, email, mot_de_passe, prenom, nom, date_creation, est_admin sont obligatoires pour l'inscription.  
<b>Domaine</b> :  
genre $\in$ {« homme », « femme », « autre »}.  
contraintes_alimentaires $\in$ {« aucune », « végétarien », « vegan », « sans porc »}.  
<b>Sécurité</b> : mot_de_passe doit être stocké sous forme hachée

# -------------------------------
### Création de la table SQL

```sql
CREATE DATABASE fanfarehub_db;

CREATE USER fanfare_hub_owner WITH PASSWORD '...';

GRANT ALL PRIVILEGES ON DATABASE fanfarehub_db TO fanfare_hub_owner;

ALTER DATABASE fanfarehub_db OWNER TO fanfare_hub_owner;

CREATE TABLE FANFARON (
    nom_utilisateur VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    nom VARCHAR(255) NOT NULL,
    genre VARCHAR(20) CHECK (genre IN ('homme', 'femme', 'autre')),
    contraintes_alimentaires VARCHAR(50) CHECK (contraintes_alimentaires IN ('aucune', 'végétarien', 'vegan', 'sans porc')),
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    derniere_connexion TIMESTAMP,
    est_admin BOOLEAN NOT NULL DEFAULT FALSE
);

ALTER TABLE FANFARON OWNER TO fanfare_hub_owner;
```