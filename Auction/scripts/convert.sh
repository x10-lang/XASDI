#!/bin/bash 

PWD=`pwd`
UNAME=`uname -smp | sed -e 's/ /,/g'`
FILE_SEP='/'; if [[ "$UNAME" = CYGWIN* ]]; then FILE_SEP='\\'; fi
PATH_SEP=':'; if [[ "$UNAME" = CYGWIN* ]]; then PATH_SEP=';'; fi

PROJECT=${PWD}${FILE_SEP}..
LIB=${PWD}${FILE_SEP}..${FILE_SEP}..${FILE_SEP}jar
LOG=${PWD}${FILE_SEP}logs

java -classpath ${LIB}${FILE_SEP}xasdi.jar${PATH_SEP}${PROJECT}${FILE_SEP}bin auction.report.AuctionLogConverter $LOG

# sort -g -n -t , -k 1 -k 2 "sample.csv" > "__temp.csv"
# mv "__temp.csv" "sample.csv"
