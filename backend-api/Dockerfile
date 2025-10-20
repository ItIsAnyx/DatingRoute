FROM maven:3.9.11-amazoncorretto-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine AS corretto-jdk
RUN apk add --no-cache binutils
RUN $JAVA_HOME/bin/jlink \
	--verbose \
	--add-modules ALL-MODULE-PATH \
	--strip-debug \
	--no-man-pages \
	--no-header-files \
	--compress=2 \
	--output /customjre

FROM alpine:latest
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY --from=corretto-jdk /customjre $JAVA_HOME

ARG APPLICATION_USER=appuser
RUN adduser --no-create-home -u 1000 -D $APPLICATION_USER

RUN mkdir /app \
	&& chown -R $APPLICATION_USER /app

COPY --chown=1000:1000 --from=builder /app/target/*.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["/jre/bin/java", "-jar", "app.jar"]
