FROM openjdk:11-jdk as build
WORKDIR /workspace/app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /workspace/app/build/libs/*.war app.war
ENTRYPOINT ["java","-jar","/app/app.jar"]