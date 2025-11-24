# 🚀 Ordre de Démarrage des Services

## 📋 Vue d'ensemble

Cet document décrit l'ordre correct de démarrage des services microservices pour éviter les erreurs de dépendances.

## 🔄 Ordre de Démarrage (Séquentiel)

### **Étape 1 : Services d'Infrastructure** ⚙️

#### 1️⃣ **Discovery Service (Eureka Server)**
- **Port** : `8761`
- **Chemin** : `discorery-service/`
- **Commande** :
  ```bash
  cd discorery-service
  ./mvnw spring-boot:run
  # ou
  mvn spring-boot:run
  ```
- **Vérification** : http://localhost:8761
- **Dépendances** : ❌ Aucune
- **Rôle** : Registre centralisé des services

**⏱️ Attendre** : ~10-15 secondes que le serveur Eureka soit complètement démarré

---

#### 2️⃣ **Config Server**
- **Port** : `9999`
- **Chemin** : `config-server/`
- **Commande** :
  ```bash
  cd config-server
  ./mvnw spring-boot:run
  ```
- **Vérification** : http://localhost:9999/actuator/health
- **Dépendances** : ✅ Eureka (pour s'enregistrer)
- **Rôle** : Configuration centralisée

**⏱️ Attendre** : ~5-10 secondes

---

### **Étape 2 : Services Métier de Base** 🏢

Ces services peuvent démarrer **en parallèle** car ils sont indépendants :

#### 3️⃣ **Customer Service**
- **Port** : `8081`
- **Chemin** : `customer-service/`
- **Commande** :
  ```bash
  cd customer-service
  ./mvnw spring-boot:run
  ```
- **Vérification** : http://localhost:8081/api/customers
- **Dépendances** : ✅ Eureka, ✅ Config Server
- **Rôle** : Gestion des clients

#### 4️⃣ **Inventory Service**
- **Port** : `8082`
- **Chemin** : `inventory-service/`
- **Commande** :
  ```bash
  cd inventory-service
  ./mvnw spring-boot:run
  ```
- **Vérification** : http://localhost:8082/api/products
- **Dépendances** : ✅ Eureka, ✅ Config Server
- **Rôle** : Gestion des produits

**⏱️ Attendre** : ~10-15 secondes que les deux services soient enregistrés dans Eureka

---

### **Étape 3 : Services Dépendants** 🔗

#### 5️⃣ **Billing Service**
- **Port** : `8083`
- **Chemin** : `billing-service/`
- **Commande** :
  ```bash
  cd billing-service
  ./mvnw spring-boot:run
  ```
- **Vérification** : http://localhost:8083/api/bills
- **Dépendances** : ✅ Eureka, ✅ Config Server, ✅ Customer Service, ✅ Inventory Service
- **Rôle** : Gestion des factures (appelle Customer et Inventory via Feign)

**⏱️ Attendre** : ~10-15 secondes

---

### **Étape 4 : Services d'Accès** 🌐

#### 6️⃣ **Gateway Service**
- **Port** : `8888`
- **Chemin** : `gateway-service/`
- **Commande** :
  ```bash
  cd gateway-service
  ./mvnw spring-boot:run
  ```
- **Vérification** : http://localhost:8888/actuator/gateway/routes
- **Dépendances** : ✅ Eureka (pour découvrir les services)
- **Rôle** : Point d'entrée unique, routage des requêtes

**Note** : Le Gateway peut démarrer avant les services métier, mais il ne pourra router que vers les services déjà enregistrés dans Eureka.

---

#### 7️⃣ **Chat Bot Service**
- **Port** : `8087`
- **Chemin** : `chat-bot/`
- **Commande** :
  ```bash
  cd chat-bot
  ./mvnw spring-boot:run
  ```
- **Vérification** : http://localhost:8087/actuator/health
- **Dépendances** : ✅ Eureka, ✅ Config Server, ✅ Customer Service, ✅ Inventory Service, ✅ Billing Service
- **Rôle** : Chatbot AI qui consomme les 3 services métier

**⏱️ Attendre** : ~10-15 secondes

---

## 📊 Résumé Visuel

```
┌─────────────────────────────────────────────────────────┐
│  ÉTAPE 1 : Infrastructure                                │
├─────────────────────────────────────────────────────────┤
│  1. Discovery Service (Eureka) - Port 8761              │
│     ⬇️                                                    │
│  2. Config Server - Port 9999                           │
└─────────────────────────────────────────────────────────┘
                    ⬇️
┌─────────────────────────────────────────────────────────┐
│  ÉTAPE 2 : Services Métier de Base (Parallèle)         │
├─────────────────────────────────────────────────────────┤
│  3. Customer Service - Port 8081                        │
│  4. Inventory Service - Port 8082                       │
└─────────────────────────────────────────────────────────┘
                    ⬇️
┌─────────────────────────────────────────────────────────┐
│  ÉTAPE 3 : Services Dépendants                          │
├─────────────────────────────────────────────────────────┤
│  5. Billing Service - Port 8083                         │
│     (dépend de Customer + Inventory)                    │
└─────────────────────────────────────────────────────────┘
                    ⬇️
┌─────────────────────────────────────────────────────────┐
│  ÉTAPE 4 : Services d'Accès                             │
├─────────────────────────────────────────────────────────┤
│  6. Gateway Service - Port 8888                         │
│  7. Chat Bot Service - Port 8087                        │
│     (dépend de Customer + Inventory + Billing)          │
└─────────────────────────────────────────────────────────┘
```

## 🎯 Ordre Minimal (Séquentiel)

Si vous démarrez les services un par un, suivez cet ordre :

```bash
# 1. Infrastructure
cd discorery-service && ./mvnw spring-boot:run &
sleep 15

cd ../config-server && ./mvnw spring-boot:run &
sleep 10

# 2. Services de base
cd ../customer-service && ./mvnw spring-boot:run &
cd ../inventory-service && ./mvnw spring-boot:run &
sleep 15

# 3. Service dépendant
cd ../billing-service && ./mvnw spring-boot:run &
sleep 15

# 4. Services d'accès
cd ../gateway-service && ./mvnw spring-boot:run &
cd ../chat-bot && ./mvnw spring-boot:run &
```

## ✅ Vérification du Démarrage

### Vérifier Eureka
```bash
curl http://localhost:8761
# ou ouvrir http://localhost:8761 dans le navigateur
```

### Vérifier les services enregistrés
```bash
# Via Eureka Dashboard
http://localhost:8761

# Ou via API
curl http://localhost:8761/eureka/apps
```

### Vérifier chaque service
```bash
# Customer Service
curl http://localhost:8081/api/customers

# Inventory Service
curl http://localhost:8082/api/products

# Billing Service
curl http://localhost:8083/api/bills

# Gateway
curl http://localhost:8888/customer-service/api/customers

# Chat Bot
curl http://localhost:8087/actuator/health
```

## ⚠️ Problèmes Courants

### 1. Service ne démarre pas
- **Cause** : Service dépendant non démarré
- **Solution** : Vérifier l'ordre de démarrage

### 2. Service ne s'enregistre pas dans Eureka
- **Cause** : Eureka non démarré ou service trop rapide
- **Solution** : Attendre que Eureka soit complètement démarré

### 3. Erreur de connexion au Config Server
- **Cause** : Config Server non démarré
- **Solution** : Démarrer Config Server avant les services métier
- **Note** : Avec `optional:configserver`, le service peut démarrer sans Config Server

### 4. Feign ne trouve pas les services
- **Cause** : Services cibles non enregistrés dans Eureka
- **Solution** : Attendre que les services soient enregistrés (visible dans Eureka Dashboard)

## 🚀 Script de Démarrage Automatique

Un script `start-all-services.sh` peut être créé pour automatiser le démarrage dans le bon ordre.

## 📝 Notes Importantes

1. **Temps d'attente** : Les temps indiqués sont approximatifs. Ajustez selon votre machine.

2. **Config Server optionnel** : Avec `optional:configserver`, les services peuvent démarrer sans Config Server, mais utiliseront leur configuration locale.

3. **Eureka obligatoire** : Les services métier ont besoin d'Eureka pour la découverte de services (sauf si vous utilisez des URLs directes).

4. **Parallélisation** : Customer et Inventory Services peuvent démarrer en parallèle car ils sont indépendants.

5. **Gateway** : Le Gateway peut démarrer à tout moment, mais ne pourra router que vers les services déjà enregistrés.


