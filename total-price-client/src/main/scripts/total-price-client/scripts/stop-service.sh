#!/bin/bash
PID=`ps axj |grep "total-price-client-[^\s]\+.jar" |grep -v grep|awk '{print $2}'`
exitStatus=1

if [ ! -z "${PID}" ]; then
    echo kill -9 ${PID}
    set -x;kill -9 ${PID};set +x

     sleep 5s
     # check if service stopped
    PID=`ps axj |grep "total-price-client-[^\s]\+.jar" |grep -v grep|awk '{print $2}'`
    if [ ! -z "${PID}" ]; then
       echo ${PID}" total-price-client service is not stopped"
       exitStatus=1
    else
       echo "total-price-client service is stopped"
       exitStatus=0  
  fi
else
  echo "total-price-client service is already stopped"
  exitStatus=0
fi
echo "Returning exit code $exitStatus"

exit $exitStatus;
