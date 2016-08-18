#!/bin/bash 

PWD=`pwd`
UNAME=`uname -smp | sed -e 's/ /,/g'`
FILE_SEP='/'; if [[ "$UNAME" = CYGWIN* ]]; then FILE_SEP='\\'; fi
PATH_SEP=':'; if [[ "$UNAME" = CYGWIN* ]]; then PATH_SEP=';'; fi

export LOG=${PWD}${FILE_SEP}logs

java -classpath ${PWD}${FILE_SEP}..${FILE_SEP}jar${FILE_SEP}xasdi.jar${PATH_SEP}${PWD}${FILE_SEP}mysample2.jar mysample2.report.SampleLogConverter $LOG

sort -g -n -t , -k 1 -k 2 "sample.csv" > "__temp.csv"
mv "__temp.csv" "sample.csv"
