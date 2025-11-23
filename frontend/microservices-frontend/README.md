# Frontend Angular - Architecture Microservices

Application frontend Angular pour consommer les APIs des microservices.

## 🚀 Démarrage

### Prérequis

- Node.js 18+ et npm
- Tous les microservices doivent être démarrés (voir README principal)

### Installation

```bash
cd frontend/microservices-frontend
npm install
```

### Démarrage

```bash
npm start
```

L'application sera accessible sur `http://localhost:4200`

## 📁 Structure du Projet

```
src/
├── app/
│   ├── components/
│   │   ├── customers/      # Composant de gestion des clients
│   │   ├── products/       # Composant de gestion des produits
│   │   └── bills/          # Composant de gestion des factures
│   ├── models/             # Modèles TypeScript
│   │   ├── customer.model.ts
│   │   ├── product.model.ts
│   │   └── bill.model.ts
│   ├── services/           # Services Angular pour les APIs
│   │   ├── customer.ts
│   │   ├── inventory.ts
│   │   └── billing.ts
│   ├── app.ts              # Composant principal
│   ├── app.routes.ts       # Configuration du routing
│   └── app.config.ts       # Configuration de l'application
└── styles.css               # Styles globaux
```

## 🔧 Configuration

### Proxy Configuration

Le fichier `proxy.conf.json` est configuré pour rediriger les requêtes vers le Gateway Service (port 8888) afin d'éviter les problèmes CORS.

### URLs des Services

Les services sont configurés pour communiquer via le Gateway :

- **Customer Service** : `http://localhost:8888/customers`
- **Inventory Service** : `http://localhost:8888/products`
- **Billing Service** : `http://localhost:8888/billing-service/api/bills`

## 📱 Fonctionnalités

### Gestion des Clients

- ✅ Liste tous les clients
- ✅ Créer un nouveau client
- ✅ Modifier un client existant
- ✅ Supprimer un client
- ✅ Afficher les détails d'un client

### Gestion des Produits

- ✅ Liste tous les produits
- ✅ Créer un nouveau produit
- ✅ Modifier un produit existant
- ✅ Supprimer un produit
- ✅ Afficher les détails d'un produit

### Gestion des Factures

- ✅ Rechercher une facture par ID
- ✅ Afficher les détails d'une facture
- ✅ Afficher les informations du client associé
- ✅ Afficher la liste des produits avec les totaux
- ✅ Calculer le total de la facture

## 🛠️ Technologies Utilisées

- **Angular 20** : Framework frontend
- **TypeScript** : Langage de programmation
- **RxJS** : Programmation réactive
- **HttpClient** : Communication HTTP avec les APIs

## 📝 Scripts Disponibles

```bash
# Démarrer le serveur de développement
npm start

# Build de production
npm run build

# Tests unitaires
npm test
```

## 🔗 Intégration avec les Microservices

L'application frontend communique avec les microservices via le **Gateway Service** qui :

1. Route les requêtes vers les services appropriés
2. Gère la découverte de services via Eureka
3. Évite les problèmes CORS
4. Centralise l'authentification (à implémenter)

## 🎨 Interface Utilisateur

L'interface est moderne et responsive avec :

- Navigation par onglets
- Design cards pour les listes
- Formulaires de création/édition
- Affichage détaillé des informations
- Gestion des erreurs et états de chargement

## 🐛 Dépannage

### Problèmes CORS

Si vous rencontrez des erreurs CORS, assurez-vous que :
1. Le proxy est bien configuré dans `angular.json`
2. Le Gateway Service est démarré sur le port 8888
3. Tous les microservices sont enregistrés dans Eureka

### Services non disponibles

Vérifiez que :
1. Tous les microservices sont démarrés
2. Eureka Server est démarré et accessible
3. Les services sont bien enregistrés dans Eureka (http://localhost:8761)

## 📚 Documentation

Pour plus d'informations sur l'architecture complète, consultez le README principal du projet.
