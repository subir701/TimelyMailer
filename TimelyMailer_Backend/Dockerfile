# First stage: build JAR
FROM eclipse-temurin:17-jdk-alpine as builder

WORKDIR /app

# Copy Gradle Wrapper scripts and Gradle files
COPY TimelyMailer_Backend/gradlew TimelyMailer_Backend/gradlew
COPY TimelyMailer_Backend/gradle TimelyMailer_Backend/gradle

# Copy rest of backend code
COPY TimelyMailer_Backend ./TimelyMailer_Backend

WORKDIR /app/TimelyMailer_Backend

RUN chmod +x gradlew

# Build while skipping tests
RUN ./gradlew clean build --no-daemon -x test

# Second stage: run app
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/TimelyMailer_Backend/build/libs/*.jar app.jar

EXPOSE 8080
ENV TZ=Asia/Kolkata

ENTRYPOINT ["java", "-jar", "app.jar"]