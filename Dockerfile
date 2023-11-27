# 빌드 단계
FROM openjdk:11-jdk as build
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build

# 실행 단계
FROM openjdk:11-jre
COPY --from=build /app/build/libs/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.war"]