#!/bin/bash
server_java_home=$(java -XshowSettings:properties -version 2> /tmp/file1.txt ; cat /tmp/file1.txt | grep java.home | tr -d ' ' | cut -d '=' -f2)
echo ${server_java_home}
${server_java_home}/bin/keytool -import -storetype JKS -storepass changeit -alias ATPCOLDAP -file /usr/local/tomcat/certs/ldap.cert -noprompt -keystore ${server_java_home}/lib/security/cacerts
