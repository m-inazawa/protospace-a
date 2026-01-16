FROM gradle:8.14.3-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN ./gradlew clean bootJar
# RUN gradle clean build -x test
# ↓ これを一時的に追加して、実際にどこに何が生成されたかログで確認する
RUN ls -R /app/build/libs/
FROM eclipse-temurin:21-alpine

WORKDIR /app
COPY --from=build /app/build/libs/protospace-a-0.0.1-SNAPSHOT.jar protospace-a.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "protospace-a.jar", "--spring.profiles.active=prod", "--debug"]