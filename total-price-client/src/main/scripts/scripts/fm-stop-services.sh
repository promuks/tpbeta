#!/bin/bash
# wrapper script to invoke services-stop script based on service name
#some environments use short name; some use FQDN

HOSTNAME=`hostname -s`

echo "Running: $HOSTNAME:$0 ..."

if [ $# -lt 1 ]; then
    echo "No service name specified!"
	exit 1
fi

SERVICE_INSTANCE_INPUT=$1
SERVICE_SCRIPT_PREFIX=/opt/fme/fm-services
SERVICE_SCRIPT_SUFFIX=scripts/stop-service.sh

echo "You have requested to stop Service instance: $SERVICE_INSTANCE_INPUT"
SERVICE_SCRIPT_PATH=$SERVICE_SCRIPT_PREFIX/$SERVICE_INSTANCE_INPUT/$SERVICE_SCRIPT_SUFFIX
if [ ! -f $SERVICE_SCRIPT_PATH ]; then
	echo "$SERVICE_INSTANCE_INPUT is not a valid Service instance."
	exit 2
fi

echo "Kicking off: $SERVICE_SCRIPT_PATH"

# run the service start script
$SERVICE_SCRIPT_PATH

# return the exit code from previous command
exit $?
