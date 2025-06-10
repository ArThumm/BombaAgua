FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package

FROM openjdk:11-jre-slim
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    libgl1 \
    libglu1 \
    libgtk-3-0 \
    libxtst6 \
    openjfx && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /app/target/BombaAgua-1.0-SNAPSHOT.jar ./app.jar

ENV JAVA_OPTS="--module-path /usr/share/openjfx/lib --add-modules=ALL-MODULE-PATH -Dprism.verbose=true"

CMD ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]