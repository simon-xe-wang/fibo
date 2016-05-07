#!/bin/bash

PKG_NAME=fiboservice-1.0
BUILD_ROOT=build/${PKG_NAME}

FIBO_SERVICE_JAR=fiboservice/target/*.jar
FIBO_EXECUTOR_JAR=fiboexecutor/target/*.jar
FIBO_COMMON_JAR=common/target/*.jar

FIBO_LIB_DIR=${BUILD_ROOT}/lib/

_fatal() {
    echo "$0: Error: $*" >&2
    exit 252
}

# check maven existed

# build jars by maven
mvn install -Dmaven.test.skip=true || _fatal "Error to build jars by maven"

# clean bulid
rm -rf build
rm -f ${PKG_NAME}.zip

# creating build dir hierarchy and copy conf/, bin/ and all docs 
mkdir -p ${BUILD_ROOT}
mkdir -p ${BUILD_ROOT}/lib
mkdir -p ${BUILD_ROOT}/conf
mkdir -p ${BUILD_ROOT}/logs
mkdir -p ${BUILD_ROOT}/bin

/bin/cp ${FIBO_SERVICE_JAR} ${FIBO_LIB_DIR} || _fatal "Error to copy service jars"
/bin/cp ${FIBO_COMMON_JAR} ${FIBO_LIB_DIR} || _fatal "Error to copy common jars"
/bin/cp ${FIBO_EXECUTOR_JAR} ${FIBO_LIB_DIR} || _fatal "Error to copy executor jars"
/bin/cp -r conf/ ${BUILD_ROOT}/ || _fatal "Error to copy conf dir"
/bin/cp -r bin/ ${BUILD_ROOT}/ || _fatal "Error to copy bin dir"
chmod a+x ${BUILD_ROOT}/bin/*

set -x
# copy all dependency jars
deps_jars=$(mvn dependency:build-classpath | grep "Dependencies classpath:" -A1 |grep -v "Dependencies classpath:")
deps_jars_space=$(echo ${deps_jars} | tr ":" " ")
for jar in ${deps_jars_space}; do
    cp ${jar} ${BUILD_ROOT}/lib
done

# should cp README and UserGuide and examples also

# zip
cd build
zip -r "../${PKG_NAME}.zip" "${PKG_NAME}" || _fatal "Error build package"

