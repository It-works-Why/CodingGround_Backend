FROM openjdk:11-jre
COPY build/libs/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.war"]