FROM openjdk:17
EXPOSE 8082
ADD target/ismailgharnougui-0.0.1.jar /ismailgharnougui-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/ismailgharnougui-0.0.1.jar"]