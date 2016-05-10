#!/bin/sh 
export CLASSPATH=../conf:$(find ../lib/ -name *.jar |tr "\n" ":")

java -server -d64 -Xmx1G -Dlog4j.configuration=svc-log4j.properties myapp.fibo.service.Main &

[[ $?==0 ]] && echo "The fibo service get started. View log at /var/log/fiboservice.log"
