FROM gradle:jdk11 as build
WORKDIR /app
COPY . .
RUN ls -al
RUN pwd
RUN ["gradle", "build"]

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /workspace/app/build/libs/*.war app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]