FROM openjdk:11
RUN mkdir /app
COPY target/app-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar"]

# clean
# package
# docker-compose up --build