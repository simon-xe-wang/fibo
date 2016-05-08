#!/bin/sh -x
export CLASSPATH=../conf:$(find ../lib/ -name *.jar |tr "\n" ":")

java -d64 myapp.fibo.myapp.fibo.demo.SyncNoCachePerfTest $@ 

