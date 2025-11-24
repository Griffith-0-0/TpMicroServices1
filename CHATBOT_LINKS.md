# 🔗 Liens du Chatbot

## Accès Direct (Port 8087)

### Chat
```
http://localhost:8087/chat?query=VOTRE_QUESTION
```

### Chat Stream
```
http://localhost:8087/chatStream?query=VOTRE_QUESTION
```

## Via Gateway (Port 8888)

### Chat
```
http://localhost:8888/chat-bot/chat?query=VOTRE_QUESTION
```

### Chat Stream
```
http://localhost:8888/chat-bot/chatStream?query=VOTRE_QUESTION
```

## Exemples

```
http://localhost:8087/chat?query=Liste tous les clients
http://localhost:8087/chat?query=Montre-moi le produit avec l'ID 1
http://localhost:8087/chat?query=Trouve toutes les factures du client 1
http://localhost:8087/chat?query=Recherche le client nommé John Doe
```

## cURL

```bash
curl "http://localhost:8087/chat?query=Liste tous les clients"
```


