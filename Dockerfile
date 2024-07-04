FROM openjdk:21
VOLUME /tmp
EXPOSE 8080

ARG JAR_FILE=build/libs/feeds-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} feeds-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/feeds-0.0.1-SNAPSHOT.jar"]
