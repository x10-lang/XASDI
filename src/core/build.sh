#!/usr/bin/env bash

COREJAR=xasdi-core.jar
BIN=bin

rm -f $COREJAR
rm -rf $BIN
mkdir $BIN

DIR=.

X10C_FLAGS="-O -NO_CHECKS"
x10c ${X10C_FLAGS} -classpath "../bridge/bin" -d $BIN -o $COREJAR $DIR/src/com/ibm/xasdi/util/*.x10 $DIR/src/com/ibm/xasdi/runtime/*.x10
