# builder image
FROM amazoncorretto:17-al2-jdk AS builder

RUN mkdir /kafka-user
WORKDIR /kafka-user

COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean bootJar

# runtime image
FROM amazoncorretto:17.0.12-al2

ENV TZ=Asia/Seoul
ENV PROFILE=${PROFILE}

RUN mkdir /kafka-user
WORKDIR /kafka-user

COPY --from=builder /kafka-user/build/libs/kafka-user-* /kafka-user/app.jar

CMD ["sh", "-c", " \
    java -Dspring.profiles.active=${PROFILE} \
         -jar /kafka-user/app.jar"]
