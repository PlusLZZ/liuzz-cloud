FROM openjdk:8
VOLUME /home/package/gateway
MAINTAINER liuzz
RUN mkdir -p /home/package/gateway
ADD ./target/gateway.jar /home/package/gateway/gateway.jar
EXPOSE 8080
ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
CMD sleep 10; java $JAVA_OPTS -jar /home/package/gateway/gateway.jar
