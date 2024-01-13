# Build a JAR File
FROM maven:3.8.4-openjdk-17 AS stage1
WORKDIR /home/app
COPY . /home/app/
RUN mvn -f /home/app/pom.xml clean package

# Create an Image
FROM openjdk:17-jdk-slim
EXPOSE 5000
COPY --from=stage1 /home/app/target/auth-course-0.0.1-SNAPSHOT.jar auth-course-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["sh", "-c", "java -jar /auth-course-0.0.1-SNAPSHOT.jar"]