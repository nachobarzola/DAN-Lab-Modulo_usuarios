FROM openjdk:11.0.7-slim 
LABEL maintainer="nachobarzola97@gmail.com" 
ARG JAR_FILE 
ADD target/${JAR_FILE} usuarios-0.0.1-SNAPSHOT.jar 
RUN echo ${JAR_FILE} ENTRYPOINT ["java","-jar","/usuarios-0.0.1-SNAPSHOT.jar"]
