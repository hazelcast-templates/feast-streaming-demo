FROM docker.io/ubuntu:22.04

RUN useradd sam -m

RUN \
    apt-get update && \
    apt-get install -y \
        python3-venv  \
        openjdk-21-jdk-headless \
        curl \
        vim \
        nano \
        jq \
        ncat \
        make \
        git \
        && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists && \
    true

COPY etc/run.sh /home/sam/.local/bin/run

RUN \
    mkdir /home/sam/etc &&\
    chmod +x /home/sam/.local/bin/run &&\
    chown -R sam:sam /home/sam/.local

USER sam

WORKDIR /home/sam

RUN \
    curl -L -o install.sh https://hazelcast.com/clc/install.sh &&\
    bash install.sh && \
    rm install.sh

COPY etc/download_kafkactl.sh .
RUN \
   tr -d '\015' <download_kafkactl.sh >download_kafkactl.sh.LF &&\
   mv download_kafkactl.sh.LF download_kafkactl.sh &&\
   bash download_kafkactl.sh &&\
   rm download_kafkactl.sh &&\
   mkdir -p $HOME/.config/kafkactl

COPY etc/kafkactl_config.yaml $HOME/.config/kafkactl/config.yaml

ENV HZ_PHONE_HOME_ENABLED=false
ENV CLC_SKIP_UPDATE_CHECK=1

COPY --chown=sam:sam feature_repo ./feature_repo/
COPY --chown=sam:sam jet ./jet/
COPY requirements.txt .

ENV PATH="$PATH:/home/sam/.local/bin:/home/sam/.hazelcast/bin:/home/sam/.venv/bin"

RUN \
    python3 -m venv .venv &&\
    $HOME/.venv/bin/pip install -r requirements.txt

COPY etc/start.sh .
COPY transaction_producer.py .
COPY StreamingFeatures.ipynb .

RUN \
    tr -d '\015' <start.sh >start.sh.LF &&\
    mv start.sh.LF start.sh &&\
    mkdir -p /home/sam/feast/data &&\
    clc config add default cluster.address=hazelcast

RUN \
    run build_jet streaming_features

ENTRYPOINT ["/bin/bash"]
CMD ["/home/sam/start.sh"]