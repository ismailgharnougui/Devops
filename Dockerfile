FROM openjdk:17
EXPOSE 8082
ADD target/ismailelgharnougui-0.0.1.jar /ismailelgharnougui-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/ismailelgharnougui-0.0.1.jar"]