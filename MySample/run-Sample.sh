#!/bin/bash 

PWD=`pwd`
UNAME=`uname -smp | sed -e 's/ /,/g'`
FILE_SEP='/'; if [[ "$UNAME" = CYGWIN* ]]; then FILE_SEP='\\'; fi
PATH_SEP=':'; if [[ "$UNAME" = CYGWIN* ]]; then PATH_SEP=';'; fi

export TH=1
export MINHEAP=1024m
export MAXHEAP=7000m
export STACK=256m

export X10_MAX_THREADS=$TH
export X10_NPLACES=2
#export X10_HOSTFILE=x10hostfile
export X10_NTHREADS=$TH

export PROGRAM=com.ibm.xasdi.runtime.XASDIRuntime
export XMLFILE=${PWD}${FILE_SEP}Sample${FILE_SEP}boot.xml
export LOG=${PWD}${FILE_SEP}logs

[ -d "$LOG" ] || mkdir -p "$LOG"
rm -f $LOG${FILE_SEP}*.csv $LOG${FILE_SEP}*.log $LOG${FILE_SEP}*.csv*
echo "Removed old log and csv files."


x10 -v -J"-Xmx$MAXHEAP" -J"-Xms$MINHEAP" -J"-Xss$STACK" -J"-Dxasdi.location.bootxml=$XMLFILE" -J"-Dadaptive=false" -J"-DlogVhcl=true" -classpath "${PWD}${FILE_SEP}..${FILE_SEP}jar${FILE_SEP}xasdi.jar${PATH_SEP}${PWD}${FILE_SEP}mysample.jar" $PROGRAM


echo finished

