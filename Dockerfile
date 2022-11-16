FROM maven:3.6.0-jdk-11-slim AS build
COPY ./ /home/app/chatty
RUN mvn -f /home/app/chatty/pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=build /home/app/chatty/target/application.jar /usr/local/lib/application.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/application.jar"]