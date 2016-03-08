#!/bin/bash 

PWD=`pwd`
UNAME=`uname -smp | sed -e 's/ /,/g'`
FILE_SEP='/'; if [[ "$UNAME" = CYGWIN* ]]; then FILE_SEP='\\'; fi
PATH_SEP=':'; if [[ "$UNAME" = CYGWIN* ]]; then PATH_SEP=';'; fi

LOG=${PWD}${FILE_SEP}logs

java -classpath ${PWD}${FILE_SEP}..${FILE_SEP}jar${FILE_SEP}xasdi.jar${PATH_SEP}${PWD}${FILE_SEP}mysample.jar mysample.report.SampleLogConverter $LOG

