FROM openjdk:21-oracle

# Definition of the maintainer and author of the image
LABEL authors="Brody Gaudel MOUNANGA BOUKA"
LABEL maintainer="Brody Gaudel MOUNANGA BOUKA"

# Setting environment variables
ENV ALLOWED_ORIGINS=http://localhost:4200
ENV DISCOVERY_SERVER=http://localhost:8761/eureka/

# Copying your jar application into the container
COPY target/gateway-service-0.0.3.jar gateway-service.jar

# Command to launch the application when running the container
ENTRYPOINT ["java","-jar","gateway-service.jar"]