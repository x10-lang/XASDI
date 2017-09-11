#!/bin/bash 

PWD=`pwd`
UNAME=`uname -smp | sed -e 's/ /,/g'`
FILE_SEP='/'; if [[ "$UNAME" = CYGWIN* ]]; then FILE_SEP='\\'; fi
PATH_SEP=':'; if [[ "$UNAME" = CYGWIN* ]]; then PATH_SEP=';'; fi

export TH=2
export MINHEAP=1g
export MAXHEAP=4g
export STACK=256m

export X10_MAX_THREADS=$TH
export X10_NPLACES=1
# export X10_HOSTFILE=x10hostfile
export X10_NTHREADS=$TH

export PROGRAM=com.ibm.xasdi.runtime.XASDIRuntime
export XMLFILE=${PWD}${FILE_SEP}boot.xml
export LOG=${PWD}${FILE_SEP}logs

PROJECT=${PWD}${FILE_SEP}..
LIB=${PWD}${FILE_SEP}..${FILE_SEP}..${FILE_SEP}jar

[ -d "$LOP" ] || mkdir -p "$LOG"
rm -f $LOG${FILE_SEP}*.csv $LOG${FILE_SEP}*.log $LOG${FILE_SEP}*.csv*
echo "Removed old log and csv files."


x10 -nocompressedrefs -v -J"-Xmx$MAXHEAP" -J"-Xms$MINHEAP" -J"-Xss$STACK" -J"-Dxasdi.location.bootxml=$XMLFILE" -J"-Dadaptive=false"  -classpath "${LIB}${FILE_SEP}xasdi.jar${PATH_SEP}${PROJECT}${FILE_SEP}bin" $PROGRAM


echo finished

