FROM hseeberger/scala-sbt:17.0.1_1.6.2_2.13.8

RUN git clone https://github.com/misterjj/youtrack-issue-exporter.git

WORKDIR youtrack-issue-exporter

RUN sbt compile

CMD ["sbt", "run"]