FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/ArchiMageAlex/singularity-core.git

FROM maven:3.5-jdk-8-alpine
WORKDIR /app
COPY --from=0 /app/singularity-core /app
RUN mvn install

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=1 /app/target/s/singularity-core-SNAPSHOT.jar /app
CMD ["java -jar /singularity-core-SNAPSHOT.jar"]