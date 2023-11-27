FROM openjdk:11-jdk as build
RUN ./gradlew clean build

FROM openjdk:11-jre
COPY --from=build /build/libs/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.war"]