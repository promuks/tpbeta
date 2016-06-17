#!/bin/bash
source /opt/fme/fm-services/total-price-client/version.properties
JAR_VERSION=$version
nohup java -Xmx2048m -Xms256m -classpath /opt/fme/fm-services/total-price-client/config/:/opt/fme/fm-services/total-price-client/total-price-client-"$JAR_VERSION".jar org.springframework.boot.loader.JarLauncher --spring.config.location=file:////opt/fme/fm-services/total-price-client/config/application.yml,file:////opt/fme/fm-services/total-price-client/config/engine.properties > /dev/null 2>&1 &

 # check if the service is started
 sleep 25s
 PID=`ps axj |grep "total-price-client-[^\s]\+.jar" |grep -v grep|awk '{print $2}'`
 echo ${PID}


 if [ ! -z "${PID}" ]; then
       echo ${PID}" total-price-client service is started successfully"
       exitStatus=0
    else
       echo "total-price-client service cannot be started"
       exitStatus=1  
  fi

echo "Returning exit code $exitStatus"

exit $exitStatus;