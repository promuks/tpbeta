# pricingui-tomcat
FROM rhel7.2

# TODO: Put the maintainer name in the image metadata
MAINTAINER Mukul Nadkarni <mnadkarni@atpco.net>


# Install build tools on top of base image
# Java jdk 8, Maven 3.3
ENV MAVEN_VERSION 3.3.9
ENV CATALINA_HOME /usr/local/tomcat

RUN rpm -Uvh https://dl.fedoraproject.org/pub/epel/7/x86_64/e/epel-release-7-7.noarch.rpm 
#RUN rpm -q git-all

RUN yum install -y  --enablerepo rhel-7-server-optional-rpms --enablerepo rhel-7-server-rpms   \
    wget tar unzip bc which lsof java-1.8.0-openjdk java-1.8.0-openjdk-devel && \
    wget http://apache.mirrors.hoobly.com/tomcat/tomcat-8/v8.0.35/bin/apache-tomcat-8.0.35.tar.gz && \
	tar -xvf apache-tomcat-8.0.35.tar.gz && \
	rm apache-tomcat*.tar.gz && \
	mv apache-tomcat* ${CATALINA_HOME} && \	
    mkdir -p /opt/openshift && \
    mkdir -p /opt/app-root/source && chmod -R a+rwX /opt/app-root/source && \
    mkdir -p /opt/s2i/destination && chmod -R a+rwX /opt/s2i/destination && \
    mkdir -p /opt/app-root/src && chmod -R a+rwX /opt/app-root/src

RUN yum -y install git; yum clean all
#RUN yum -y update; yum clean all
RUN yum -y install npm; yum clean all


RUN npm install -g  bower grunt-cli tsd

#RUN bower install --allow-root  

ENV PATH=/opt/maven/bin/:$PATH


ENV BUILDER_VERSION 1.0

LABEL io.k8s.description="Platform for building Spring Boot applications with maven or gradle" \
      io.k8s.display-name="Spring Boot builder 1.0" \
      io.openshift.expose-services="8080:http" \
      io.openshift.tags="builder,maven-3,gradle-2.6,springboot"

# TODO (optional): Copy the builder files into /opt/openshift
# COPY ./<builder_folder>/ /opt/openshift/
# COPY Additional files,configurations that we want to ship by default, like a default setting.xml

LABEL io.openshift.s2i.scripts-url=image:///usr/local/sti
#LABEL io.openshift.s2i.destination=image:///usr/local/src

COPY .sti/bin/ /usr/local/sti

RUN chown -R 1001:1001 /opt/openshift
#RUN chown -R 1001:1001 /usr/local/sti/* 
# This default user is created in the openshift/base-centos7 image
USER 1001

# Set the default port for applications built using this image
EXPOSE 8080

# Set the default CMD for the image
# CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/openshift/app.jar"]
CMD ["usage"]
