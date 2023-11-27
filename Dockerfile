FROM openjdk:11-jdk as build
WORKDIR /workspace/app
COPY . .
RUN ./gradlew clean build

FROM openjdk:11-jre
VOLUME /tmp
COPY --from=build /workspace/app/build/libs/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.war"]