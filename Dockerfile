FROM eclipse-temurin:21-jdk
ADD target/reservation-manager.jar reservation-manager.jar
ENTRYPOINT ["java", "-jar", "reservation-manager.jar"]
