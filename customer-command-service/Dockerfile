# =============================================================================
# Build Stage - Compilation de l'application
# =============================================================================
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /build

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Télécharger les dépendances (mise en cache)
RUN ./mvnw dependency:go-offline -B

# Copier le code source et compiler
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# =============================================================================
# Runtime Stage - Image finale légère
# =============================================================================
FROM eclipse-temurin:21-jre-alpine

# Métadonnées
LABEL authors="Brody Gaudel MOUNANGA BOUKA"
LABEL maintainer="Brody Gaudel MOUNANGA BOUKA"
LABEL description="CUSTOMER COMMAND SERVICE"
LABEL version="4.0.0"

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -S appgroup && \
    adduser -S appuser -G appgroup && \
    mkdir -p /app/logs && \
    chown -R appuser:appgroup /app

WORKDIR /app

# Variables d'environnement avec valeurs par défaut sécurisées
ENV DISCOVERY_PREFER_IP=true \
    CONSUL_HOST=consul \
    CONSUL_PORT=8500 \
    DB_USERNAME=customer_command_user\
    DB_PASSWORD=customer_command_pwd\
    DB_HOST=postgres\
    DB_PORT=5432\
    DB_NAME=customer_command_db\
    AXON_HOST=axon-server\
    AXON_PORT=8124\
    KEYCLOAK_ISSUER_URI=http://keycloak:7070/realms/bank\
    KEYCLOAK_JWK_URI=http://keycloak:7070/realms/bank/protocol/openid-connect/certs\
    KEYCLOAK_CLIENT_ID=customer-command-service\
    KEYCLOAK_CLIENT_SECRET=aoNEUIKnjrlNSBSA1234dztg5\
    KAFKA_BOOTSTRAP_SERVERS=kafka:9092\
    ZIPKIN_ENDPOINT=http://zipkin:9411/api/v2/spans \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Copier le JAR depuis le build stage
COPY --from=build --chown=appuser:appgroup /build/target/customer-command-service-4.0.0.jar customer-command-service.jar

# Volume pour les logs (optionnel mais bonne pratique)
VOLUME ["/app/logs"]

# Changer vers l'utilisateur non-root
USER appuser

# Exposer le port
EXPOSE 8881

# Health check pour vérifier que l'application est démarrée
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8881/actuator/health || exit 1

# Commande de démarrage optimisée
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar customer-command-service.jar"]