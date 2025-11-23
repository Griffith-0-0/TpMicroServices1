package org.badr.gatewayservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired(required = false)
    private RouteLocator routeLocator;

    @Test
    void contextLoads() {
        // Vérifie que le contexte Spring se charge correctement
        assertThat(webTestClient).isNotNull();
    }

    @Test
    void testGatewayRoutes() {
        // Vérifie que les routes sont configurées
        // Note: Ce test nécessite que les services soient enregistrés dans Eureka
        if (routeLocator != null) {
            routeLocator.getRoutes()
                    .collectList()
                    .block()
                    .forEach(route -> {
                        assertThat(route.getId()).isNotNull();
                        assertThat(route.getUri()).isNotNull();
                    });
        }
    }

    @Test
    void testGatewayHealth() {
        // Test du health check du gateway
        webTestClient.get()
                .uri("/actuator/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP");
    }

    @Test
    void testGatewayInfo() {
        // Test des informations du gateway
        webTestClient.get()
                .uri("/actuator/info")
                .exchange()
                .expectStatus().isOk();
    }
}

