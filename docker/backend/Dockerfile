FROM openjdk:8-jre
LABEL maintainer="wu812730157@gmail.com"

ADD brand-report-backend-0.0.1-SNAPSHOT.jar /tmp
ADD entrypoint.sh /tmp
CMD bash /tmp/entrypoint.sh
VOLUME /var/log/message/brand-report-backend /data