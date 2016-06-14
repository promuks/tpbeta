FROM openshift/base-centos7
MAINTAINER Veerendra Akula <veerendra.aakula>

# Install build tools on top of base image
# Java jdk 8, Maven 3.3, Gradle 2.6
#ENV GRADLE_VERSION 2.6
ENV MAVEN_VERSION 3.3.3
ENV CATALINA_HOME /usr/local/tomcat 


RUN yum install -y --enablerepo=centosplus \
    wget tar unzip bc which bzip2 lsof java-1.8.0-openjdk java-1.8.0-openjdk-devel && \
    yum clean all -y && \
    (curl -0 http://www.eu.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | \
    tar -zx -C /usr/local) && \
    mv /usr/local/apache-maven-$MAVEN_VERSION /usr/local/maven && \
    ln -sf /usr/local/maven/bin/mvn /usr/local/bin/mvn && \
    #curl -sL -0 https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o /tmp/gradle-${GRADLE_VERSION}-bin.zip && \
    #unzip /tmp/gradle-${GRADLE_VERSION}-bin.zip -d /usr/local/ && \
    #rm /tmp/gradle-${GRADLE_VERSION}-bin.zip && \
    #mv /usr/local/gradle-${GRADLE_VERSION} /usr/local/gradle && \
    #ln -sf /usr/local/gradle/bin/gradle /usr/local/bin/gradle && \
    wget http://apache.mirrors.hoobly.com/tomcat/tomcat-8/v8.0.35/bin/apache-tomcat-8.0.35.tar.gz && \
	tar -xvf apache-tomcat-8.0.35.tar.gz && \
	rm apache-tomcat*.tar.gz && \
	mv apache-tomcat* ${CATALINA_HOME} && \
    mkdir -p /opt/openshift && \
    mkdir -p /opt/app-root/source && chmod -R a+rwX /opt/app-root/source && \
    mkdir -p /opt/s2i/destination && chmod -R a+rwX /opt/s2i/destination && \
    mkdir -p /opt/app-root/src && chmod -R a+rwX /opt/app-root/src
    


RUN ls -ltr ${CATALINA_HOME}

ADD conf ${CATALINA_HOME}/conf
ADD lib ${CATALINA_HOME}/lib
ADD properties ${CATALINA_HOME}/properties
ADD bin ${CATALINA_HOME}/bin
ADD cert ${CATALINA_HOME}/certs
RUN chmod +x ${CATALINA_HOME}/bin/addldapcert.sh
RUN ${CATALINA_HOME}/bin/addldapcert.sh


RUN yum -y update; yum clean all
RUN yum -y install epel-release; yum clean all
RUN yum -y install nodejs npm; yum clean all
RUN npm install -g  bower grunt-cli tsd git

#RUN bower install --allow-root  

#ENV PATH=/opt/maven/bin/:/opt/gradle/bin/:$PATH

ENV PATH=/opt/maven/bin/:$PATH

ENV BUILDER_VERSION 1.0

LABEL io.k8s.description="Platform for building Web Application with maven or grunt" \
      io.k8s.display-name="Tomcat builder 1.0" \
      io.openshift.expose-services="8080:http" \
      io.openshift.tags="builder,maven-3,angular,tomcat"

# TODO (optional): Copy the builder files into /opt/openshift
# COPY ./<builder_folder>/ /opt/openshift/
# COPY Additional files,configurations that we want to ship by default, like a default setting.xml

LABEL io.openshift.s2i.scripts-url=image:///usr/local/sti
COPY .s2i/bin/ /usr/local/sti

#RUN chmod -R a+rwX /usr/local/sti 
RUN chmod +x /usr/local/sti/assemble /usr/local/sti/run /usr/local/sti/save-artifacts /usr/local/sti/usage
RUN chown -R 1001:1001 /opt/openshift
RUN chown -R 1001:1001 /usr/local/sti

RUN ls -ltr ${CATALINA_HOME}

RUN chmod 777 ${CATALINA_HOME}/webapps

# This default user is created in the openshift/base-centos7 image
USER 1001

# Set the default port for applications built using this image
EXPOSE 8080

# Set the default CMD for the image
# CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/openshift/app.jar"]
CMD ["usage"]
