FROM openjdk:21
COPY ./build/libs/FaceAnalyze-0.0.1-SNAPSHOT.jar /app/faceAnalyzer.jar
WORKDIR /app
CMD ["java", "-jar", "faceAnalyzer.jar"]
