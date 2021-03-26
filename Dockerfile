ARG BUILD_IMAGE=maven:3.6.3-jdk-11
ARG RUNTIME_IMAGE=openjdk:11-jdk-slim

FROM ${BUILD_IMAGE} as dependencies

WORKDIR /build
COPY pom.xml /build/

RUN mvn -B dependency:go-offline

FROM dependencies as build

WORKDIR /build
COPY src /build/src

RUN mvn -B clean package

FROM ${RUNTIME_IMAGE}

WORKDIR /app
COPY --from=build /build/target/youtube-parser.war /app/youtube-parser.war

CMD ["sh", "-c", "java -jar /app/youtube-parser.war"]