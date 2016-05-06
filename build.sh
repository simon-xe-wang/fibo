#!/bin/bash

PKG_NAME=fibservice-1.0
BUILD_ROOT=build/${PKG_NAME}

SRC_FIBO_SERVICE_JAR=target/fibservice-1.0-SNAPSHOT.jar
DST_FIBO_SERVICE_JAR=${BUILD_ROOT}/lib/fibservice-1.0.jar

_fatal() {
    echo "$0: Error: $*" >&2
    exit 252
}

# check maven existed

# build jars by maven
mvn package -Dmaven.test.skip=true || _fatal "Error to build jars by maven"

# clean bulid
rm -rf build
rm -f ${PKG_NAME}.zip

# creating build dir hierarchy and copy conf/, bin/ and all docs 
mkdir -p ${BUILD_ROOT}
mkdir -p ${BUILD_ROOT}/lib
mkdir -p ${BUILD_ROOT}/conf
mkdir -p ${BUILD_ROOT}/logs
mkdir -p ${BUILD_ROOT}/bin

/bin/cp ${SRC_FIBO_SERVICE_JAR} ${DST_FIBO_SERVICE_JAR} || _fatal "Error to copy service jars"
/bin/cp -r conf/ ${BUILD_ROOT}/ || _fatal "Error to copy conf dir"
/bin/cp -r bin/ ${BUILD_ROOT}/ || _fatal "Error to copy bin dir"

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

