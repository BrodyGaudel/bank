# Definition of the base image
FROM openjdk:17-oracle

# Definition of the maintainer and author of the image
LABEL authors="Brody Gaudel MOUNANGA BOUKA"
LABEL maintainer="Brody Gaudel MOUNANGA BOUKA"

# Setting environment variables
ENV MYSQL_USER=root
ENV MYSQL_PWD=root
ENV MYSQL_DATABASE=bank_db
ENV MYSQL_HOST=172.20.0.2
ENV MYSQL_PORT=3307
ENV AXON_HOST=172.20.0.3
ENV AXON_PORT=8124
ENV DISCOVERY_SERVER=http://localhost:8761/eureka/

# Copying your jar application into the container
COPY target/account-service-0.0.2.jar account-service.jar

# Command to launch the application when running the container
ENTRYPOINT ["java","-jar","account-service.jar"]