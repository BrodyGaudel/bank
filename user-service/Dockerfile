FROM openjdk:21-oracle

# Definition of the maintainer and author of the image
LABEL authors="Brody Gaudel MOUNANGA BOUKA"
LABEL maintainer="Brody Gaudel MOUNANGA BOUKA"

# Setting environment variables
ENV DISCOVERY_SERVER=http://localhost:8761/eureka/
ENV MYSQL_USER=root
ENV MYSQL_PWD=root
ENV MYSQL_HOST=172.20.0.2
ENV MYSQL_PORT=3307
ENV MYSQL_DATABASE=utilisateur
ENV JWT_SECRET=AaZzBbCcYyDdXxEeWwFf
ENV JWT_EXPIRATION=604800000

# Copying your jar application into the container
COPY target/user-service-0.0.3.jar user-service.jar

# Command to launch the application when running the container
ENTRYPOINT ["java","-jar","user-service.jar"]