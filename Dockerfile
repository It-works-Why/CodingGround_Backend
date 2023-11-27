# 빌드 단계
FROM openjdk:11-jdk as build
WORKDIR /workspace/app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build

# 실행 단계
FROM openjdk:11-jre
COPY --from=build /workspace/app/build/libs/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.war"]