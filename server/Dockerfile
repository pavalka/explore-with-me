FROM amazoncorretto:11.0.16-alpine
ENV SERVER_LOCATION=/explorewithme/server.jar
COPY target/*.jar $SERVER_LOCATION
ENTRYPOINT java -jar $SERVER_LOCATION