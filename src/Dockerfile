FROM openjdk:17.0.1-jdk-slim
COPY /target/portfolio-0.0.1-SNAPSHOT.jar /app/portfolio.jar
COPY /assets /assets
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/portfolio.jar"]