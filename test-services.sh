#!/bin/bash

# Script de test pour l'architecture microservices
# Ce script teste tous les endpoints des services

echo "🧪 Tests de l'Architecture Microservices"
echo "========================================"
echo ""

# Couleurs pour les messages
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Fonction pour tester un endpoint
test_endpoint() {
    local name=$1
    local url=$2
    local expected_status=$3
    
    echo -n "Test: $name... "
    response=$(curl -s -o /dev/null -w "%{http_code}" "$url" 2>/dev/null)
    
    if [ "$response" == "$expected_status" ]; then
        echo -e "${GREEN}✓ OK${NC} (Status: $response)"
        return 0
    else
        echo -e "${RED}✗ FAILED${NC} (Expected: $expected_status, Got: $response)"
        return 1
    fi
}

# Compteur de tests
passed=0
failed=0

echo "📦 Test 1: Customer Service (Port 8081)"
echo "----------------------------------------"
test_endpoint "GET /api/customers" "http://localhost:8081/api/customers" "200" && ((passed++)) || ((failed++))
test_endpoint "GET /api/customers/1" "http://localhost:8081/api/customers/1" "200" && ((passed++)) || ((failed++))
test_endpoint "Health Check" "http://localhost:8081/actuator/health" "200" && ((passed++)) || ((failed++))
echo ""

echo "📦 Test 2: Inventory Service (Port 8082)"
echo "----------------------------------------"
test_endpoint "GET /api/products" "http://localhost:8082/api/products" "200" && ((passed++)) || ((failed++))
test_endpoint "GET /api/products/1" "http://localhost:8082/api/products/1" "200" && ((passed++)) || ((failed++))
test_endpoint "Health Check" "http://localhost:8082/actuator/health" "200" && ((passed++)) || ((failed++))
echo ""

echo "📦 Test 3: Billing Service (Port 8083)"
echo "----------------------------------------"
test_endpoint "GET /api/bills/1" "http://localhost:8083/api/bills/1" "200" && ((passed++)) || ((failed++))
test_endpoint "Health Check" "http://localhost:8083/actuator/health" "200" && ((passed++)) || ((failed++))
echo ""

echo "📦 Test 4: Gateway Service (Port 8888)"
echo "----------------------------------------"
test_endpoint "GET /customers via Gateway" "http://localhost:8888/customers" "200" && ((passed++)) || ((failed++))
test_endpoint "GET /products via Gateway" "http://localhost:8888/products" "200" && ((passed++)) || ((failed++))
test_endpoint "Health Check" "http://localhost:8888/actuator/health" "200" && ((passed++)) || ((failed++))
echo ""

echo "📦 Test 5: Config Server (Port 9999)"
echo "----------------------------------------"
test_endpoint "GET /application/default" "http://localhost:9999/application/default" "200" && ((passed++)) || ((failed++))
test_endpoint "GET /customer-service/default" "http://localhost:9999/customer-service/default" "200" && ((passed++)) || ((failed++))
test_endpoint "Health Check" "http://localhost:9999/actuator/health" "200" && ((passed++)) || ((failed++))
echo ""

echo "📦 Test 6: Eureka Discovery Service (Port 8761)"
echo "----------------------------------------"
test_endpoint "GET /eureka/apps" "http://localhost:8761/eureka/apps" "200" && ((passed++)) || ((failed++))
echo ""

echo "========================================"
echo "📊 Résultats des Tests"
echo "========================================"
echo -e "${GREEN}Tests réussis: $passed${NC}"
echo -e "${RED}Tests échoués: $failed${NC}"
echo "Total: $((passed + failed))"
echo ""

if [ $failed -eq 0 ]; then
    echo -e "${GREEN}✓ Tous les tests sont passés !${NC}"
    exit 0
else
    echo -e "${RED}✗ Certains tests ont échoué${NC}"
    exit 1
fi

