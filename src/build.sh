#!/usr/bin/env bash

echo "Building XASDI Java API..."
(cd bridge; ant clean dist)
echo "...DONE"


echo "Building XASDI Runtime..."
(cd core; ./build.sh)
echo "...DONE"


echo "Combining them to a single XASDI JAR..."

COREJAR=xasdi-core.jar
ALLJAR=xasdi.jar

JARDIR=../jar

rm -rf $JARDIR
mkdir -p $JARDIR

cp core/$COREJAR $JARDIR/$ALLJAR
jar uf $JARDIR/$ALLJAR -C bridge/bin .

echo "...DONE"


echo "Everything is DONE."
