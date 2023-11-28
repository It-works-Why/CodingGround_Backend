FROM openjdk:11-jdk as build
WORKDIR /workspace/app
COPY . .
RUN ls -al
RUN pwd
RUN chmod +x gradlew
RUN ["source", "/workspace/app/gradlew", "build"]

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /workspace/app/build/libs/*.war app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]