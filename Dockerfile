FROM gradle:jdk11 as build
WORKDIR /app
COPY . .
RUN ls -al
RUN pwd
RUN ["gradle", "bootJar"]
RUN ls -al /app/build/libs

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/app/app.jar"]