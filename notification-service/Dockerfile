FROM openjdk:21-oracle

# Definition of the maintainer and author of the image
LABEL authors="Brody Gaudel MOUNANGA BOUKA"
LABEL maintainer="Brody Gaudel MOUNANGA BOUKA"

# Setting environment variables
ENV DISCOVERY_SERVICE=http://localhost:8761/eureka/
ENV MAIL_HOST=172.18.0.4
ENV MAIL_PORT=1025
ENV MAIL_USERNAME=brody
ENV MAIL_PASSWORD=brody
ENV EMAIL_SYSTEM=notification@bank.com



# Copying your jar application into the container
COPY target/notification-service-0.0.3.jar notification-service.jar

# Command to launch the application when running the container
ENTRYPOINT ["java","-jar","notification-service.jar"]