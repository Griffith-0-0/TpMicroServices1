# Guide de Tests - Architecture Microservices

Ce document décrit tous les tests à effectuer pour valider le bon fonctionnement de l'architecture microservices.

## 📋 Prérequis

Avant de lancer les tests, assurez-vous que :
1. Java 21 est installé
2. Maven est installé
3. Tous les services peuvent être démarrés

## 🚀 Ordre de Démarrage des Services

Pour tester l'architecture complète, démarrez les services dans cet ordre :

```bash
# 1. Discovery Service (Eureka)
cd discorery-service
mvn spring-boot:run

# 2. Config Server
cd ../config-server
mvn spring-boot:run

# 3. Customer Service
cd ../customer-service
mvn spring-boot:run

# 4. Inventory Service
cd ../inventory-service
mvn spring-boot:run

# 5. Billing Service
cd ../billing-service
mvn spring-boot:run

# 6. Gateway Service
cd ../gateway-service
mvn spring-boot:run
```

## 🧪 Tests Unitaires

### Exécuter tous les tests unitaires

```bash
# Depuis la racine du projet
mvn test

# Pour un service spécifique
cd customer-service
mvn test
```

## 🔍 Tests d'Intégration par Service

### 1. Customer Service (Port 8081)

#### Tests disponibles :
- ✅ `CustomerRestControllerTest` - Tests des endpoints REST

#### Tests manuels à effectuer :

```bash
# 1. Récupérer tous les clients
curl http://localhost:8081/api/customers

# 2. Récupérer un client par ID
curl http://localhost:8081/api/customers/1

# 3. Créer un nouveau client
curl -X POST http://localhost:8081/api/customers \
  -H "Content-Type: application/json" \
  -d '{"name": "Test User", "email": "test@example.com"}'

# 4. Mettre à jour un client
curl -X PUT http://localhost:8081/api/customers/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Updated Name", "email": "updated@example.com"}'

# 5. Supprimer un client
curl -X DELETE http://localhost:8081/api/customers/1
```

**Résultats attendus :**
- GET `/api/customers` → 200 OK avec liste des clients
- GET `/api/customers/{id}` → 200 OK avec détails du client
- POST `/api/customers` → 201 Created avec le client créé
- PUT `/api/customers/{id}` → 200 OK avec le client mis à jour
- DELETE `/api/customers/{id}` → 204 No Content

### 2. Inventory Service (Port 8082)

#### Tests disponibles :
- ✅ `ProductRestControllerTest` - Tests des endpoints REST

#### Tests manuels à effectuer :

```bash
# 1. Récupérer tous les produits
curl http://localhost:8082/api/products

# 2. Récupérer un produit par ID
curl http://localhost:8082/api/products/1

# 3. Créer un nouveau produit
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "New Product", "price": 99.99, "quantity": 10}'

# 4. Mettre à jour un produit
curl -X PUT http://localhost:8082/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Updated Product", "price": 149.99, "quantity": 15}'

# 5. Supprimer un produit
curl -X DELETE http://localhost:8082/api/products/1
```

**Résultats attendus :**
- GET `/api/products` → 200 OK avec liste des produits
- GET `/api/products/{id}` → 200 OK avec détails du produit
- POST `/api/products` → 201 Created avec le produit créé
- PUT `/api/products/{id}` → 200 OK avec le produit mis à jour
- DELETE `/api/products/{id}` → 204 No Content

### 3. Billing Service (Port 8083)

#### Tests disponibles :
- ✅ `BillRestControllerTest` - Tests des endpoints REST

#### Tests manuels à effectuer :

```bash
# 1. Récupérer une facture par ID
curl http://localhost:8083/api/bills/1

# 2. Vérifier que la facture contient les informations du client et des produits
curl http://localhost:8083/api/bills/1 | jq
```

**Résultats attendus :**
- GET `/api/bills/{id}` → 200 OK avec :
  - Informations de la facture
  - Informations du client (depuis customer-service)
  - Informations des produits (depuis inventory-service)

### 4. Gateway Service (Port 8888)

#### Tests manuels à effectuer :

```bash
# 1. Accéder aux clients via le gateway
curl http://localhost:8888/customers

# 2. Accéder aux produits via le gateway
curl http://localhost:8888/products

# 3. Accéder aux factures via le gateway (nécessite un endpoint dans billing-service)
curl http://localhost:8888/bills/1
```

**Résultats attendus :**
- Le gateway route correctement les requêtes vers les services appropriés
- Les services sont découverts automatiquement via Eureka

## 🔗 Tests d'Intégration Inter-Services

### Test 1 : Communication Billing → Customer Service

```bash
# 1. Vérifier qu'un client existe
curl http://localhost:8081/api/customers/1

# 2. Créer une facture pour ce client
# (La facture est créée automatiquement au démarrage)

# 3. Récupérer la facture (doit inclure les infos du client)
curl http://localhost:8083/api/bills/1
```

**Vérification :**
- La facture doit contenir les informations du client récupérées depuis customer-service

### Test 2 : Communication Billing → Inventory Service

```bash
# 1. Vérifier qu'un produit existe
curl http://localhost:8082/api/products/1

# 2. Récupérer une facture (doit inclure les infos des produits)
curl http://localhost:8083/api/bills/1
```

**Vérification :**
- La facture doit contenir les informations des produits récupérées depuis inventory-service

### Test 3 : Circuit Breaker (Resilience4j)

```bash
# 1. Arrêter le customer-service
# 2. Essayer de récupérer une facture
curl http://localhost:8083/api/bills/1
```

**Vérification :**
- Le circuit breaker doit activer le fallback et retourner un client par défaut

## 🌐 Tests de Découverte de Services (Eureka)

### Vérifier l'enregistrement des services

```bash
# Accéder à l'interface Eureka
open http://localhost:8761

# Ou via curl
curl http://localhost:8761/eureka/apps
```

**Vérification :**
- Tous les services doivent être visibles dans Eureka :
  - CUSTOMER-SERVICE
  - INVENTORY-SERVICE
  - BILLING-SERVICE
  - GATEWAY-SERVICE
  - CONFIG-SERVER

## ⚙️ Tests de Configuration (Config Server)

### Vérifier la récupération de configuration

```bash
# 1. Récupérer la configuration globale
curl http://localhost:9999/application/default

# 2. Récupérer la configuration du customer-service
curl http://localhost:9999/customer-service/default

# 3. Récupérer la configuration du customer-service en dev
curl http://localhost:9999/customer-service/dev
```

**Vérification :**
- Le config server doit retourner les configurations depuis config-repo

## 📊 Tests de Santé (Actuator)

### Vérifier l'état des services

```bash
# Health check pour chaque service
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8888/actuator/health
curl http://localhost:9999/actuator/health

# Info sur les services
curl http://localhost:8081/actuator/info
```

**Résultats attendus :**
- Tous les endpoints `/actuator/health` doivent retourner `{"status":"UP"}`

## 🎯 Checklist Complète de Tests

### Tests Fonctionnels
- [ ] Customer Service : CRUD complet
- [ ] Inventory Service : CRUD complet
- [ ] Billing Service : Récupération de facture avec infos complètes
- [ ] Gateway Service : Routage correct vers tous les services

### Tests d'Intégration
- [ ] Billing Service peut communiquer avec Customer Service
- [ ] Billing Service peut communiquer avec Inventory Service
- [ ] Circuit Breaker fonctionne en cas de panne
- [ ] Tous les services s'enregistrent dans Eureka

### Tests de Configuration
- [ ] Config Server sert les configurations correctement
- [ ] Les services récupèrent leur configuration depuis Config Server

### Tests de Performance
- [ ] Les services répondent en moins de 2 secondes
- [ ] Le gateway route les requêtes efficacement

## 🐛 Dépannage

### Problèmes courants

1. **Service ne s'enregistre pas dans Eureka**
   - Vérifier que Eureka est démarré
   - Vérifier la configuration `eureka.client.service-url.defaultZone`

2. **Erreur de connexion au Config Server**
   - Vérifier que Config Server est démarré
   - Vérifier le chemin du config-repo

3. **Erreur 404 sur les endpoints**
   - Vérifier que le service est démarré
   - Vérifier le port et le chemin de base

4. **Circuit Breaker ne fonctionne pas**
   - Vérifier la configuration Resilience4j
   - Vérifier que le fallback est bien défini

## 📝 Notes

- Les tests unitaires utilisent MockMvc pour tester les contrôleurs
- Les tests d'intégration nécessitent que tous les services soient démarrés
- Utilisez Postman ou un autre outil pour faciliter les tests manuels
- Surveillez les logs de chaque service pour identifier les problèmes

