FROM ubuntu:20.04

RUN apt-get update && apt-get install -y \
    wget \
    curl \
    unzip \
    haproxy \
    supervisor

COPY ./haproxy/consul_1.15.4_linux_amd64.zip /
COPY ./haproxy/consul-template_0.25.2_linux_amd64.zip /

RUN unzip consul_1.15.4_linux_amd64.zip \
    && mv consul /usr/local/bin/ \
    && rm consul_1.15.4_linux_amd64.zip

RUN unzip consul-template_0.25.2_linux_amd64.zip \
    && mv consul-template /usr/local/bin/ \
    && rm consul-template_0.25.2_linux_amd64.zip

COPY ./haproxy/supervisord.conf /etc/supervisor/conf.d/supervisord.conf

RUN mkdir /run/haproxy

COPY ./haproxy/consul_config.hcl /etc/consul/consul_config.hcl
COPY ./haproxy/haproxy.cfg /haproxy.conf.tmpl
COPY ./haproxy/consul.json /consul.json

EXPOSE 8500 80 9090

CMD ["/usr/bin/supervisord"]
