# set environment variables for SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD, SPRING_DATASOURCE_URL
# defaults are meetupapi, password, jdbc:postgresql://localhost:5432/meetupapi
git pull
mvn package
java -jar target/meetupapi-0.0.1-SNAPSHOT.jar
