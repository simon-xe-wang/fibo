#!/bin/sh
export CLASSPATH=../conf:$(find ../lib/ -name *.jar |tr "\n" ":")

java -d64 myapp.fibo.myapp.fibo.demo.FiboAyncDemo $@ 

