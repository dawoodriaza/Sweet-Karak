FROM eclipse-temurin:17-jdk-jammy

VOLUME /tmp

EXPOSE 8080

RUN mkdir -p /app/

RUN mkdir -p /app/logs/

ADD target/sweetandkarak-1.0.0.jar /app/app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]