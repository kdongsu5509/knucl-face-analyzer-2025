FROM openjdk:21

WORKDIR /app

COPY out/artifacts/FaceAnalyze_main_jar/FaceAnalyze.main.jar /app/FaceAnalyze.main.jar

CMD ["java", "-jar", "FaceAnalyze.main.jar"]
