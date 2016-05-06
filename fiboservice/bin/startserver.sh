#!/bin/sh -x
export CLASSPATH=$(find ../lib/ -name *.jar |tr "\n" ":")

java -server -d64 -Xmx1G -Dlog4j.configuration=authsvc-log4j.properties -Dfibo.home=.. myapp.fibo.Main &

[[ $?==0 ]] && echo "The fibo server get started successfully. View log at ../logs"
