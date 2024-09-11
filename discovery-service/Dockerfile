# Definition of the base image
FROM openjdk:21-oracle

# Definition of the maintainer and author of the image
LABEL authors="Brody Gaudel MOUNANGA BOUKA"
LABEL maintainer="Brody Gaudel MOUNANGA BOUKA"

# Copying your jar application into the container
COPY target/discovery-service-0.0.3.jar discovery-service.jar

# Command to launch the application when running the container
ENTRYPOINT ["java","-jar","discovery-service.jar"]