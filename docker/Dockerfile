FROM ibmjava:8-sdk

MAINTAINER Hideyuki Mizuta

RUN apt-get update
RUN apt-get install -y ant
RUN apt-get install -y git
RUN apt-get install -y unzip
RUN wget https://sourceforge.net/projects/x10/files/x10/2.6.0/x10-2.6.0_linux_x86_64.tgz
RUN mkdir /opt/x10
RUN tar xvzf x10-2.6.0_linux_x86_64.tgz -C /opt/x10
RUN rm x10-2.6.0_linux_x86_64.tgz
ENV X10_HOME /opt/x10
ENV PATH $PATH:$X10_HOME/bin

RUN mkdir /work
RUN wget https://github.com/x10-lang/xasdi/archive/master.zip
RUN unzip master.zip -d /opt
RUN mv /opt/xasdi-master /opt/xasdi
# RUN git clone https://github.com/x10-lang/xasdi.git /work/xasdi
WORKDIR /opt/xasdi/src
RUN ./build.sh
WORKDIR /work

RUN apt-get install -y vim
CMD ["bash"]
