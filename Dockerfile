FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD initialize-project-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]