# 📚 Architecture Microservices - Guide Complet

## Table des Matières

1. [Introduction aux Microservices](#introduction-aux-microservices)
2. [Fondamentaux de l'Architecture](#fondamentaux-de-larchitecture)
3. [Composants de l'Architecture](#composants-de-larchitecture)
4. [Étapes de Création](#étapes-de-création)
5. [Annotations Spring Cloud](#annotations-spring-cloud)
6. [Dépendances Maven](#dépendances-maven)
7. [Configuration Détaillée](#configuration-détaillée)
8. [Fonctionnement de l'Architecture](#fonctionnement-de-larchitecture)
9. [Communication Inter-Services](#communication-inter-services)
10. [Patterns et Bonnes Pratiques](#patterns-et-bonnes-pratiques)

---

## 🎯 Introduction aux Microservices

### Qu'est-ce qu'une Architecture Microservices ?

L'architecture microservices est un style architectural qui structure une application comme une collection de **services faiblement couplés**, où chaque service :
- Est **indépendant** et peut être développé, déployé et mis à l'échelle séparément
- Communique via des **APIs REST** ou des **messages**
- Possède sa **propre base de données** (principe de base de données par service)
- Est responsable d'une **fonctionnalité métier spécifique**

### Avantages

✅ **Indépendance** : Chaque service peut être développé et déployé indépendamment  
✅ **Scalabilité** : Mise à l'échelle sélective des services  
✅ **Technologies diverses** : Chaque service peut utiliser différentes technologies  
✅ **Résilience** : La panne d'un service n'affecte pas les autres  
✅ **Maintenance** : Code plus simple et plus facile à maintenir

### Inconvénients

❌ **Complexité** : Gestion de la communication inter-services  
❌ **Déploiement** : Nécessite une infrastructure plus complexe  
❌ **Tests** : Tests d'intégration plus complexes  
❌ **Consistance** : Gestion de la cohérence des données distribuées

---

## 🏗️ Fondamentaux de l'Architecture

### Principes Clés

#### 1. **Service Discovery (Découverte de Services)**
Les services doivent pouvoir se trouver et communiquer entre eux sans connaître leurs adresses IP exactes.

**Solution** : Utilisation d'un **Service Registry** (Eureka) qui maintient une liste de tous les services disponibles.

#### 2. **API Gateway**
Point d'entrée unique pour tous les clients, qui route les requêtes vers les services appropriés.

**Avantages** :
- Centralisation de la logique de routage
- Authentification et autorisation centralisées
- Load balancing
- Rate limiting

#### 3. **Configuration Centralisée**
Gestion centralisée de la configuration pour tous les services.

**Avantages** :
- Modification de configuration sans redéploiement
- Gestion des environnements (dev, prod)
- Sécurité centralisée

#### 4. **Circuit Breaker (Disjoncteur)**
Protection contre les cascades de défaillances en cas de panne d'un service.

**États** :
- **Closed** : Fonctionne normalement
- **Open** : Service en panne, retourne immédiatement une erreur
- **Half-Open** : Teste si le service est revenu

#### 5. **Load Balancing**
Distribution des requêtes sur plusieurs instances d'un service.

---

## 🧩 Composants de l'Architecture

### Architecture du Projet

```
┌─────────────────────────────────────────────────────────────┐
│                      CLIENT (Browser/App)                     │
└───────────────────────────┬───────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   API GATEWAY (Port 8888)                     │
│              - Routage des requêtes                          │
│              - Load Balancing                                │
└───────────┬───────────────────────────────────┬──────────────┘
            │                                   │
            ▼                                   ▼
┌──────────────────────┐          ┌──────────────────────┐
│  CUSTOMER SERVICE    │          │  INVENTORY SERVICE   │
│     (Port 8081)      │          │     (Port 8082)      │
│  - Gestion clients   │          │  - Gestion produits  │
└───────────┬──────────┘          └───────────┬──────────┘
            │                                  │
            └──────────┬───────────────────────┘
                       │
                       ▼
            ┌──────────────────────┐
            │   BILLING SERVICE     │
            │     (Port 8083)      │
            │  - Gestion factures  │
            │  - Communication     │
            │    avec autres svc   │
            └──────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│              INFRASTRUCTURE SERVICES                        │
├──────────────────────┬──────────────────────────────────────┤
│  EUREKA SERVER       │      CONFIG SERVER                   │
│   (Port 8761)        │       (Port 9999)                    │
│  - Service Registry  │      - Configuration                 │
│  - Service Discovery │        Centralisée                   │
└──────────────────────┴──────────────────────────────────────┘
```

### Services de l'Application

#### 1. **Discovery Service (Eureka Server)**
- **Port** : 8761
- **Rôle** : Registre centralisé de tous les services
- **Interface** : http://localhost:8761

#### 2. **Config Server**
- **Port** : 9999
- **Rôle** : Serveur de configuration centralisé
- **Source** : Fichiers dans `config-repo/`

#### 3. **Customer Service**
- **Port** : 8081
- **Rôle** : Gestion des clients
- **Base de données** : H2 (mémoire)
- **Endpoints** : `/api/customers`

#### 4. **Inventory Service**
- **Port** : 8082
- **Rôle** : Gestion des produits
- **Base de données** : H2 (mémoire)
- **Endpoints** : `/api/products`

#### 5. **Billing Service**
- **Port** : 8083
- **Rôle** : Gestion des factures
- **Base de données** : H2 (mémoire)
- **Endpoints** : `/api/bills`
- **Communication** : Appelle Customer et Inventory Services

#### 6. **Gateway Service**
- **Port** : 8888
- **Rôle** : Point d'entrée unique, routage des requêtes
- **Technologie** : Spring Cloud Gateway (WebFlux)

---

## 🛠️ Étapes de Création

### Étape 1 : Créer le Discovery Service (Eureka Server)

#### 1.1 Créer le projet Spring Boot

```bash
# Utiliser Spring Initializr ou créer manuellement
# Dependencies :
# - Spring Boot Actuator
# - Eureka Server
```

#### 1.2 Configuration `pom.xml`

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

#### 1.3 Classe principale

```java
@SpringBootApplication
@EnableEurekaServer  // Active le serveur Eureka
public class DiscoreryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoreryServiceApplication.class, args);
    }
}
```

#### 1.4 Configuration `application.properties`

```properties
spring.application.name=discorery-service
server.port=8761

# Eureka ne doit pas s'enregistrer lui-même
eureka.client.fetch-registry=false
eureka.client.register-with-eureka=false
```

### Étape 2 : Créer le Config Server

#### 2.1 Dépendances

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

#### 2.2 Classe principale

```java
@SpringBootApplication
@EnableConfigServer  // Active le serveur de configuration
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

#### 2.3 Configuration

```properties
spring.application.name=config-server
server.port=9999

# Mode native : lit depuis le système de fichiers
spring.cloud.config.server.native.search-locations=file:../config-repo
spring.profiles.active=native

# S'enregistrer dans Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

#### 2.4 Créer le dépôt de configuration (`config-repo/`)

```
config-repo/
├── application.properties          # Configuration globale
├── customer-service.properties     # Config spécifique customer-service
├── inventory-service.properties    # Config spécifique inventory-service
└── billing-service.properties      # Config spécifique billing-service
```

### Étape 3 : Créer les Services Métier

#### 3.1 Customer Service

**Dépendances principales** :
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
</dependency>
```

**Configuration** :
```properties
spring.application.name=customer-service
server.port=8081

# Se connecter au Config Server
spring.config.import=optional:configserver:http://localhost:9999

# S'enregistrer dans Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.instance.prefer-ip-address=true
```

**Entité** :
```java
@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
}
```

**Repository** :
```java
@RepositoryRestResource  // Expose automatiquement les endpoints REST
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
```

#### 3.2 Inventory Service

Structure similaire à Customer Service, mais pour les produits.

#### 3.3 Billing Service

**Dépendances supplémentaires** :
```xml
<!-- OpenFeign pour la communication inter-services -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<!-- Resilience4j pour le Circuit Breaker -->
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot3</artifactId>
</dependency>
```

**Classe principale** :
```java
@SpringBootApplication
@EnableFeignClients  // Active Feign pour la communication inter-services
public class BillingServiceApplication {
    // ...
}
```

**Client Feign** :
```java
@FeignClient(name = "customer-service")  // Nom du service dans Eureka
public interface CustomerServiceRestClient {
    @GetMapping("/customers/{id}")
    @CircuitBreaker(name = "customer-service", fallbackMethod = "getDefaultCustomer")
    Customer findCustomerById(@PathVariable Long id);
    
    default Customer getDefaultCustomer(Long id, Exception e) {
        // Méthode de fallback en cas d'échec
        return new Customer(id, "default", "default");
    }
}
```

### Étape 4 : Créer le Gateway Service

#### 4.1 Dépendances

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway-server-webflux</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

#### 4.2 Configuration

```properties
spring.application.name=gateway-service
server.port=8888

# Désactiver Config Server (optionnel)
spring.cloud.config.enabled=false

# Activer la découverte de services
spring.cloud.discovery.enabled=true
spring.cloud.gateway.discovery.locator.enabled=true
spring.cloud.gateway.discovery.locator.lower-case-service-id=true

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.instance.prefer-ip-address=true
```

#### 4.3 Configuration des routes dynamiques

```java
@Bean
public DiscoveryClientRouteDefinitionLocator dinamycRoutes(
        ReactiveDiscoveryClient rdc, 
        DiscoveryLocatorProperties dlp
) {
    return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
}
```

---

## 🏷️ Annotations Spring Cloud

### Annotations Principales

#### 1. `@EnableEurekaServer`
**Usage** : Sur la classe principale du Discovery Service

```java
@SpringBootApplication
@EnableEurekaServer
public class DiscoreryServiceApplication {
    // Active le serveur Eureka
}
```

**Rôle** : Transforme l'application en serveur Eureka qui maintient le registre des services.

#### 2. `@EnableEurekaClient` / `@EnableDiscoveryClient`
**Usage** : Sur les classes principales des services clients

```java
@SpringBootApplication
@EnableEurekaClient  // Ou @EnableDiscoveryClient (plus générique)
public class CustomerServiceApplication {
    // S'enregistre automatiquement dans Eureka
}
```

**Rôle** : Permet au service de s'enregistrer dans Eureka et de découvrir d'autres services.

**Note** : Avec Spring Boot 3.x, cette annotation n'est plus nécessaire si la dépendance Eureka Client est présente.

#### 3. `@EnableConfigServer`
**Usage** : Sur la classe principale du Config Server

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    // Active le serveur de configuration
}
```

**Rôle** : Active le serveur de configuration centralisé.

#### 4. `@EnableFeignClients`
**Usage** : Sur la classe principale d'un service qui utilise Feign

```java
@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {
    // Active les clients Feign
}
```

**Rôle** : Active la découverte et la création des clients Feign pour la communication inter-services.

#### 5. `@FeignClient`
**Usage** : Sur une interface qui définit un client REST

```java
@FeignClient(name = "customer-service")
public interface CustomerServiceRestClient {
    @GetMapping("/customers/{id}")
    Customer findCustomerById(@PathVariable Long id);
}
```

**Paramètres** :
- `name` : Nom du service dans Eureka
- `url` : URL directe (si pas de service discovery)
- `fallback` : Classe de fallback

**Rôle** : Crée un client REST déclaratif qui communique avec un autre service.

#### 6. `@CircuitBreaker`
**Usage** : Sur une méthode d'un client Feign

```java
@CircuitBreaker(name = "customer-service", fallbackMethod = "getDefaultCustomer")
Customer findCustomerById(@PathVariable Long id);
```

**Paramètres** :
- `name` : Nom de l'instance du circuit breaker
- `fallbackMethod` : Méthode à appeler en cas d'échec

**Rôle** : Protège contre les cascades de défaillances.

#### 7. `@RepositoryRestResource`
**Usage** : Sur un repository Spring Data JPA

```java
@RepositoryRestResource
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
```

**Rôle** : Expose automatiquement les endpoints REST pour les opérations CRUD.

#### 8. `@RestController`
**Usage** : Sur une classe de contrôleur

```java
@RestController
@RequestMapping("/api")
public class BillRestController {
    // Définit les endpoints REST personnalisés
}
```

**Rôle** : Définit un contrôleur REST qui gère les requêtes HTTP.

### Annotations Spring Boot Standard

- `@SpringBootApplication` : Combine `@Configuration`, `@EnableAutoConfiguration`, `@ComponentScan`
- `@Entity` : Marque une classe comme entité JPA
- `@Id` : Marque un champ comme clé primaire
- `@GeneratedValue` : Stratégie de génération de l'ID
- `@Autowired` : Injection de dépendance
- `@Bean` : Définit un bean Spring
- `@Configuration` : Classe de configuration Spring

---

## 📦 Dépendances Maven

### Structure `pom.xml` Standard

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.7</version>
    </parent>
    
    <properties>
        <java.version>21</java.version>
        <spring-cloud.version>2025.0.0</spring-cloud.version>
    </properties>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

### Dépendances par Service

#### Discovery Service

```xml
<dependencies>
    <!-- Eureka Server -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
    
    <!-- Actuator pour le monitoring -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

#### Config Server

```xml
<dependencies>
    <!-- Config Server -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>
    
    <!-- Eureka Client pour s'enregistrer -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>
```

#### Services Métier (Customer, Inventory)

```xml
<dependencies>
    <!-- Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Data REST (expose automatiquement les repositories) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-rest</artifactId>
    </dependency>
    
    <!-- Config Client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    
    <!-- Eureka Client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    
    <!-- H2 Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

#### Billing Service (avec Feign)

```xml
<dependencies>
    <!-- Toutes les dépendances des services métier + -->
    
    <!-- OpenFeign -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
        <version>5.0.0-RC1</version>
    </dependency>
    
    <!-- Resilience4j Circuit Breaker -->
    <dependency>
        <groupId>io.github.resilience4j</groupId>
        <artifactId>resilience4j-spring-boot3</artifactId>
    </dependency>
</dependencies>
```

#### Gateway Service

```xml
<dependencies>
    <!-- Spring Cloud Gateway (WebFlux) -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway-server-webflux</artifactId>
    </dependency>
    
    <!-- Eureka Client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    
    <!-- Actuator -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

### Gestion des Versions

**Spring Boot** : 3.5.7  
**Spring Cloud** : 2025.0.0  
**Java** : 21

**Important** : Les versions de Spring Boot et Spring Cloud doivent être compatibles. Consultez le [Spring Cloud Release Train](https://spring.io/projects/spring-cloud) pour la compatibilité.

---

## ⚙️ Configuration Détaillée

### 1. Configuration Eureka Server

```properties
spring.application.name=discorery-service
server.port=8761

# Eureka ne doit pas s'enregistrer lui-même
eureka.client.fetch-registry=false
eureka.client.register-with-eureka=false
```

**Explication** :
- `fetch-registry=false` : Ne récupère pas le registre (car c'est le serveur)
- `register-with-eureka=false` : Ne s'enregistre pas lui-même

### 2. Configuration Config Server

```properties
spring.application.name=config-server
server.port=9999

# Mode native : lit depuis le système de fichiers
spring.cloud.config.server.native.search-locations=file:../config-repo
spring.profiles.active=native

# S'enregistrer dans Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

**Modes de Config Server** :
- **Native** : Lit depuis le système de fichiers
- **Git** : Lit depuis un dépôt Git
- **Vault** : Lit depuis HashiCorp Vault

### 3. Configuration des Services Clients

```properties
spring.application.name=customer-service
server.port=8081

# Connexion au Config Server
spring.config.import=optional:configserver:http://localhost:9999

# Enregistrement dans Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.instance.prefer-ip-address=true
```

**Explication** :
- `spring.config.import` : Importe la configuration depuis le Config Server
- `optional:` : Continue même si le Config Server n'est pas disponible
- `eureka.instance.prefer-ip-address=true` : Utilise l'IP au lieu du hostname

### 4. Configuration Gateway

```properties
spring.application.name=gateway-service
server.port=8888

# Découverte de services
spring.cloud.discovery.enabled=true
spring.cloud.gateway.discovery.locator.enabled=true
spring.cloud.gateway.discovery.locator.lower-case-service-id=true

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.instance.prefer-ip-address=true
```

**Routage automatique** :
- Avec `discovery.locator.enabled=true`, le gateway route automatiquement vers les services enregistrés dans Eureka
- Format d'URL : `http://gateway:8888/{service-name}/{path}`
- Exemple : `http://localhost:8888/customer-service/api/customers`

### 5. Configuration Circuit Breaker (Resilience4j)

```properties
# Configuration du Circuit Breaker pour customer-service
resilience4j.circuitbreaker.instances.customer-service.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.customer-service.wait-duration-in-open-state=10000
resilience4j.circuitbreaker.instances.customer-service.sliding-window-size=10
resilience4j.circuitbreaker.instances.customer-service.minimum-number-of-calls=5
```

**Paramètres** :
- `failure-rate-threshold` : Pourcentage d'échecs avant ouverture (50%)
- `wait-duration-in-open-state` : Temps d'attente avant de tester à nouveau (10s)
- `sliding-window-size` : Nombre de requêtes dans la fenêtre
- `minimum-number-of-calls` : Nombre minimum d'appels avant évaluation

### 6. Configuration Spring Data REST

```properties
# Base de données
spring.datasource.url=jdbc:h2:mem:customers-db
spring.h2.console.enabled=true

# Base path pour les endpoints REST
spring.data.rest.base-path=/api
```

**Endpoints automatiques générés** :
- `GET /api/customers` : Liste tous les clients
- `GET /api/customers/{id}` : Récupère un client
- `POST /api/customers` : Crée un client
- `PUT /api/customers/{id}` : Met à jour un client
- `DELETE /api/customers/{id}` : Supprime un client

---

## 🔄 Fonctionnement de l'Architecture

### Flux de Démarrage

#### 1. Démarrage d'Eureka Server
```
1. Eureka Server démarre sur le port 8761
2. Crée un registre vide des services
3. Attend les enregistrements des clients
```

#### 2. Démarrage du Config Server
```
1. Config Server démarre sur le port 9999
2. Se connecte à Eureka et s'enregistre
3. Charge les configurations depuis config-repo/
4. Prêt à servir les configurations
```

#### 3. Démarrage des Services Métier
```
1. Service démarre (ex: Customer Service sur 8081)
2. Se connecte au Config Server et récupère sa configuration
3. Se connecte à Eureka et s'enregistre
4. Eureka enregistre le service avec son nom et son URL
5. Service est prêt à recevoir des requêtes
```

#### 4. Démarrage du Gateway
```
1. Gateway démarre sur le port 8888
2. Se connecte à Eureka
3. Découvre tous les services enregistrés
4. Configure les routes dynamiques
5. Prêt à router les requêtes
```

### Flux d'une Requête

#### Exemple : Récupérer une facture via le Gateway

```
1. Client → GET http://localhost:8888/billing-service/api/bills/1
   │
   ▼
2. Gateway reçoit la requête
   │
   ▼
3. Gateway consulte Eureka pour trouver billing-service
   │
   ▼
4. Gateway route vers http://billing-service:8083/api/bills/1
   │
   ▼
5. Billing Service reçoit la requête
   │
   ▼
6. Billing Service récupère la facture depuis sa base de données
   │
   ▼
7. Billing Service doit récupérer les infos du client
   │
   ▼
8. Billing Service utilise Feign pour appeler Customer Service
   │   - Consulte Eureka pour trouver customer-service
   │   - Appelle http://customer-service:8081/api/customers/1
   │
   ▼
9. Customer Service retourne les infos du client
   │
   ▼
10. Billing Service doit récupérer les infos des produits
    │
    ▼
11. Billing Service utilise Feign pour appeler Inventory Service
    │   - Consulte Eureka pour trouver inventory-service
    │   - Appelle http://inventory-service:8082/api/products/1
    │
    ▼
12. Inventory Service retourne les infos du produit
    │
    ▼
13. Billing Service assemble la facture complète
    │
    ▼
14. Billing Service retourne la réponse au Gateway
    │
    ▼
15. Gateway retourne la réponse au client
```

### Service Discovery en Détail

#### Enregistrement d'un Service

```
1. Service démarre
2. Service lit sa configuration (nom, port)
3. Service se connecte à Eureka Server
4. Service envoie un heartbeat toutes les 30 secondes
5. Eureka enregistre le service dans son registre
```

#### Découverte d'un Service

```
1. Service A veut communiquer avec Service B
2. Service A interroge Eureka : "Où est Service B ?"
3. Eureka retourne l'URL de Service B
4. Service A utilise cette URL pour appeler Service B
```

#### Heartbeat et Désenregistrement

```
- Chaque service envoie un heartbeat toutes les 30 secondes
- Si Eureka ne reçoit pas de heartbeat pendant 90 secondes, il considère le service comme down
- Le service est retiré du registre
```

### Load Balancing

Avec plusieurs instances d'un service :

```
Service: customer-service
Instances:
  - customer-service-1:8081
  - customer-service-2:8081
  - customer-service-3:8081

Quand un client demande customer-service:
1. Eureka retourne les 3 instances
2. Le client (ou le Gateway) utilise un algorithme de load balancing
3. Les requêtes sont distribuées entre les instances
```

---

## 📡 Communication Inter-Services

### 1. Communication Synchronous (REST)

#### OpenFeign

**Avantages** :
- Déclaratif (pas de code boilerplate)
- Intégration avec Eureka
- Support du Circuit Breaker
- Facile à tester

**Exemple** :
```java
@FeignClient(name = "customer-service")
public interface CustomerServiceRestClient {
    @GetMapping("/customers/{id}")
    Customer findCustomerById(@PathVariable Long id);
}

// Utilisation
@Autowired
private CustomerServiceRestClient customerClient;

Customer customer = customerClient.findCustomerById(1L);
```

#### RestTemplate (Alternative)

```java
@Autowired
private RestTemplate restTemplate;

@Autowired
private DiscoveryClient discoveryClient;

public Customer getCustomer(Long id) {
    List<ServiceInstance> instances = discoveryClient.getInstances("customer-service");
    ServiceInstance instance = instances.get(0);
    String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/customers/" + id;
    return restTemplate.getForObject(url, Customer.class);
}
```

### 2. Circuit Breaker Pattern

**Objectif** : Éviter les cascades de défaillances

**États** :
1. **Closed** : Fonctionne normalement
2. **Open** : Trop d'échecs, retourne immédiatement une erreur
3. **Half-Open** : Teste si le service est revenu

**Exemple** :
```java
@CircuitBreaker(name = "customer-service", fallbackMethod = "getDefaultCustomer")
public Customer findCustomerById(Long id) {
    return customerClient.findCustomerById(id);
}

public Customer getDefaultCustomer(Long id, Exception e) {
    // Retourne un client par défaut
    return new Customer(id, "Default", "default@example.com");
}
```

### 3. Retry Pattern

```java
@Retry(name = "customer-service")
public Customer findCustomerById(Long id) {
    return customerClient.findCustomerById(id);
}
```

### 4. Timeout Configuration

```properties
# Feign timeout
feign.client.config.default.connect-timeout=5000
feign.client.config.default.read-timeout=5000
```

---

## 🎨 Patterns et Bonnes Pratiques

### 1. Database per Service

**Principe** : Chaque service a sa propre base de données

**Avantages** :
- Indépendance des données
- Pas de couplage entre services
- Scalabilité indépendante

**Dans ce projet** :
- Customer Service → `customers-db`
- Inventory Service → `products-db`
- Billing Service → `billing-db`

### 2. API Gateway Pattern

**Rôle** :
- Point d'entrée unique
- Routage des requêtes
- Authentification/Autorisation
- Rate limiting
- Load balancing

### 3. Service Discovery Pattern

**Rôle** :
- Découverte automatique des services
- Pas besoin de connaître les URLs exactes
- Gestion automatique des instances

### 4. Configuration Management

**Rôle** :
- Configuration centralisée
- Gestion des environnements
- Modification sans redéploiement

### 5. Circuit Breaker Pattern

**Rôle** :
- Protection contre les cascades de défaillances
- Dégradation gracieuse
- Résilience

### 6. API Versioning

**Bonnes pratiques** :
```java
// Version dans l'URL
@GetMapping("/api/v1/customers")

// Version dans les headers
@GetMapping(value = "/api/customers", headers = "API-Version=1")
```

### 7. Logging et Monitoring

**Actuator Endpoints** :
```properties
management.endpoints.web.exposure.include=*
```

**Endpoints disponibles** :
- `/actuator/health` : État de santé
- `/actuator/info` : Informations
- `/actuator/metrics` : Métriques
- `/actuator/loggers` : Configuration des logs

### 8. Error Handling

**Bonnes pratiques** :
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailable(ServiceUnavailableException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ErrorResponse(e.getMessage()));
    }
}
```

---

## 🚀 Démarrage de l'Architecture

### Ordre de Démarrage

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

### Vérification

1. **Eureka Dashboard** : http://localhost:8761
2. **Config Server** : http://localhost:9999/customer-service/default
3. **Customer Service** : http://localhost:8081/api/customers
4. **Inventory Service** : http://localhost:8082/api/products
5. **Billing Service** : http://localhost:8083/api/bills/1
6. **Gateway** : http://localhost:8888/customers

---

## 📚 Ressources et Documentation

### Documentation Officielle

- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Netflix Eureka](https://github.com/Netflix/eureka)
- [Resilience4j](https://resilience4j.readme.io/)

### Concepts Clés à Maîtriser

1. **Service Discovery** : Comment les services se trouvent
2. **API Gateway** : Point d'entrée unique
3. **Configuration Management** : Gestion centralisée
4. **Circuit Breaker** : Protection contre les défaillances
5. **Load Balancing** : Distribution des requêtes
6. **Distributed Tracing** : Suivi des requêtes (optionnel)

---

## 🎓 Conclusion

Cette architecture microservices démontre les concepts fondamentaux :

✅ **Service Discovery** avec Eureka  
✅ **Configuration Centralisée** avec Config Server  
✅ **API Gateway** pour le routage  
✅ **Communication Inter-Services** avec Feign  
✅ **Circuit Breaker** pour la résilience  
✅ **Database per Service** pour l'indépendance  

Cette architecture est prête pour être étendue avec :
- Authentification/Autorisation (OAuth2, JWT)
- Distributed Tracing (Zipkin, Sleuth)
- Message Queues (RabbitMQ, Kafka)
- Containerization (Docker, Kubernetes)
- Monitoring (Prometheus, Grafana)

---

**Auteur** : Travail Pratique - Architecture Microservices  
**Date** : 2025  
**Technologies** : Spring Boot 3.5.7, Spring Cloud 2025.0.0, Java 21
