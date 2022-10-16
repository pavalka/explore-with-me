FROM amazoncorretto:11.0.16-alpine
ENV STAT_LOCATION=/explorewithme/stat.jar
COPY target/*.jar $STAT_LOCATION
ENTRYPOINT java -jar $STAT_LOCATION