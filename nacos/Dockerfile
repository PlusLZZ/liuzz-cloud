FROM anolis-registry.cn-zhangjiakou.cr.aliyuncs.com/openanolis/openjdk:8
VOLUME /home/package/nacos
MAINTAINER liuzz
RUN mkdir -p /home/package/nacos
ADD ./target/nacos.jar /home/package/nacos/nacos.jar
EXPOSE 8848
EXPOSE 9848
EXPOSE 9849
ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
CMD sleep 10; java $JAVA_OPTS -jar /home/package/nacos/nacos.jar
