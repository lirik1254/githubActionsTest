# Используем официальный образ для JDK 17
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный jar файл в контейнер
COPY target/user-service-0.0.1-SNAPSHOT.jar /app/user-service-0.0.1-SNAPSHOT.jar

# Открываем порт для взаимодействия
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java", "-jar", "user-service-0.0.1-SNAPSHOT.jar"]
