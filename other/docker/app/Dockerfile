FROM gradle:5.6.0-jdk8

WORKDIR /app
RUN git clone https://github.com/markhuang19994/deploy-mail && \
    cp -R ./deploy-mail/* . && rm -rf ./deploy-mail

ENV TZ=Asia/Taipei
CMD ["gradle", "task", "bootrun"]
