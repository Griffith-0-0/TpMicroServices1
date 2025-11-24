# 💬 Guide d'Utilisation du Chatbot

## 🔗 Liens pour Utiliser le Chatbot

### 📍 Accès Direct au Service Chatbot

Le service chatbot tourne sur le **port 8087**.

#### 1. **Endpoint Chat (Réponse Simple)**
```
GET http://localhost:8087/chat?query=VOTRE_QUESTION
```

**Exemple** :
```bash
curl "http://localhost:8087/chat?query=Liste tous les clients"
```

**Dans le navigateur** :
```
http://localhost:8087/chat?query=Liste tous les clients
```

#### 2. **Endpoint Chat Stream (Réponse en Streaming)**
```
GET http://localhost:8087/chatStream?query=VOTRE_QUESTION
```

**Exemple** :
```bash
curl "http://localhost:8087/chatStream?query=Quels sont les produits disponibles?"
```

**Note** : Cet endpoint retourne un flux (Stream) de texte, utile pour les réponses longues.

---

### 🌐 Accès via le Gateway

Le gateway tourne sur le **port 8888** et route automatiquement vers les services enregistrés dans Eureka.

Le service chatbot s'appelle `chat-bot` dans Eureka, donc la route est : `/chat-bot/**`

#### 1. **Via Gateway - Chat (Réponse Simple)**
```
GET http://localhost:8888/chat-bot/chat?query=VOTRE_QUESTION
```

**Exemple** :
```bash
curl "http://localhost:8888/chat-bot/chat?query=Montre-moi la facture avec l'ID 1"
```

**Dans le navigateur** :
```
http://localhost:8888/chat-bot/chat?query=Montre-moi la facture avec l'ID 1
```

#### 2. **Via Gateway - Chat Stream (Réponse en Streaming)**
```
GET http://localhost:8888/chat-bot/chatStream?query=VOTRE_QUESTION
```

**Exemple** :
```bash
curl "http://localhost:8888/chat-bot/chatStream?query=Liste tous les produits"
```

---

## 📝 Exemples de Questions

Le chatbot peut répondre à des questions sur les **clients**, **produits** et **factures** :

### Questions sur les Clients
```
http://localhost:8087/chat?query=Liste tous les clients
http://localhost:8087/chat?query=Trouve le client avec l'ID 1
http://localhost:8087/chat?query=Recherche le client nommé John Doe
http://localhost:8087/chat?query=Recherche le client avec l'email john@example.com
http://localhost:8087/chat?query=Supprime le client avec l'ID 5
```

### Questions sur les Produits
```
http://localhost:8087/chat?query=Liste tous les produits
http://localhost:8087/chat?query=Montre-moi le produit avec l'ID 1
http://localhost:8087/chat?query=Recherche le produit nommé Laptop
http://localhost:8087/chat?query=Met à jour le prix du produit 1 à 1500
http://localhost:8087/chat?query=Supprime le produit avec l'ID 3
```

### Questions sur les Factures
```
http://localhost:8087/chat?query=Liste toutes les factures
http://localhost:8087/chat?query=Montre-moi la facture avec l'ID 1
http://localhost:8087/chat?query=Trouve toutes les factures du client 1
http://localhost:8087/chat?query=Trouve toutes les factures contenant le produit 2
```

### Questions Complexes
```
http://localhost:8087/chat?query=Combien de clients avons-nous?
http://localhost:8087/chat?query=Quels sont les produits les plus chers?
http://localhost:8087/chat?query=Montre-moi toutes les factures du client nommé John
```

---

## 🛠️ Utilisation avec cURL

### Exemple Complet
```bash
# Question simple
curl "http://localhost:8087/chat?query=Liste tous les clients"

# Question avec caractères spéciaux (encodage URL)
curl -G "http://localhost:8087/chat" \
  --data-urlencode "query=Recherche le client nommé John Doe"

# Via Gateway
curl "http://localhost:8888/chat-bot/chat?query=Liste tous les produits"
```

### Avec Streaming
```bash
# Réponse en streaming
curl "http://localhost:8087/chatStream?query=Liste tous les produits"
```

---

## 🌐 Utilisation dans le Navigateur

### Format d'URL
```
http://localhost:8087/chat?query=VOTRE_QUESTION
```

### Exemples
1. **Liste des clients** :
   ```
   http://localhost:8087/chat?query=Liste tous les clients
   ```

2. **Recherche d'un produit** :
   ```
   http://localhost:8087/chat?query=Recherche le produit nommé Laptop
   ```

3. **Facture d'un client** :
   ```
   http://localhost:8087/chat?query=Trouve toutes les factures du client 1
   ```

### Encodage des Caractères Spéciaux

Si votre question contient des caractères spéciaux, encodez-les :
- Espace : `%20` ou `+`
- É : `%C3%A9`
- À : `%C3%A0`

**Exemple** :
```
http://localhost:8087/chat?query=Recherche%20le%20client%20nomm%C3%A9%20Jean
```

---

## 🔧 Utilisation avec Postman ou Insomnia

### Configuration de la Requête

1. **Méthode** : `GET`
2. **URL** : `http://localhost:8087/chat`
3. **Paramètres** :
   - Clé : `query`
   - Valeur : `Votre question ici`

### Exemple Postman
```
GET http://localhost:8087/chat?query=Liste tous les clients
```

---

## 📱 Utilisation dans une Application Frontend

### JavaScript (Fetch API)
```javascript
const query = "Liste tous les clients";
const url = `http://localhost:8087/chat?query=${encodeURIComponent(query)}`;

fetch(url)
  .then(response => response.text())
  .then(data => console.log(data))
  .catch(error => console.error('Error:', error));
```

### JavaScript (Axios)
```javascript
const axios = require('axios');

axios.get('http://localhost:8087/chat', {
  params: {
    query: 'Liste tous les clients'
  }
})
.then(response => console.log(response.data))
.catch(error => console.error('Error:', error));
```

### Via Gateway (Frontend)
```javascript
const query = "Liste tous les clients";
const url = `http://localhost:8888/chat-bot/chat?query=${encodeURIComponent(query)}`;

fetch(url)
  .then(response => response.text())
  .then(data => console.log(data))
  .catch(error => console.error('Error:', error));
```

---

## ⚠️ Notes Importantes

1. **Service doit être démarré** : Assurez-vous que le service chatbot est démarré sur le port 8087
2. **Services dépendants** : Le chatbot a besoin que les services suivants soient démarrés :
   - Customer Service (port 8081)
   - Inventory Service (port 8082)
   - Billing Service (port 8083)
   - Eureka (port 8761)
3. **Format de réponse** : Les réponses sont en texte brut (`TEXT_PLAIN`)
4. **Timeout** : Les réponses peuvent prendre quelques secondes selon la complexité de la question
5. **API Key OpenAI** : Le chatbot nécessite une clé API OpenAI valide dans la configuration

---

## 🔍 Vérification de l'État du Service

### Health Check
```bash
curl http://localhost:8087/actuator/health
```

### Via Gateway
```bash
curl http://localhost:8888/chat-bot/actuator/health
```

---

## 📊 Format des Réponses

### Réponse Simple (`/chat`)
- **Type** : `text/plain`
- **Format** : Texte brut
- **Exemple** :
  ```
  Voici la liste des clients:
  - Client ID: 1, Nom: John Doe, Email: john@example.com
  - Client ID: 2, Nom: Jane Smith, Email: jane@example.com
  ```

### Réponse Streaming (`/chatStream`)
- **Type** : `text/plain` (flux)
- **Format** : Texte brut en streaming
- **Utilité** : Pour les réponses longues, affichage progressif

---

## 🎯 Résumé des URLs

| Accès | Endpoint Chat | Endpoint Chat Stream |
|-------|---------------|---------------------|
| **Direct** | `http://localhost:8087/chat?query=...` | `http://localhost:8087/chatStream?query=...` |
| **Via Gateway** | `http://localhost:8888/chat-bot/chat?query=...` | `http://localhost:8888/chat-bot/chatStream?query=...` |

---

## 🚀 Démarrage Rapide

1. **Démarrer tous les services** (voir `ORDER_STARTUP.md`)
2. **Tester le chatbot** :
   ```bash
   curl "http://localhost:8087/chat?query=Liste tous les clients"
   ```
3. **Ou dans le navigateur** :
   ```
   http://localhost:8087/chat?query=Liste tous les clients
   ```


