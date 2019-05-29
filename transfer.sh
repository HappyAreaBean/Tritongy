#!/bin/sh
ftp -n mc.islandearth.net <<EOF
quote USER $1
quote PASS $2
put generated_jar/*.jar plugins/update/Languagy.jar
close
quit
EOF
EXITSTATUS=$?
if [ $EXITSTATUS != "0" ]
then 
    exit 1
fi
exit 0