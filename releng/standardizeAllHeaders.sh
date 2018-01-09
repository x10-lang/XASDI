#!/bin/bash

# Dave Grove

mydir="`dirname "$0"`"
top="`cd "$mydir"/../.. && pwd`"
cdir="`pwd`"
[ "$cdir" = "/" ] && cdir="$cdir."
cd "$top"

# NOTE: x10.common/src to avoid including x10.common/contrib

for srcdir in \
	xasdi
do
  echo "Standardizing file headers in $srcdir"
  cd $srcdir
  JAVA_FILES=`find . -name .git -prune -o -name "*.java" -print`
  X10_FILES=`find . -name .git -prune -o -name "*.x10" -print`
  for file in $JAVA_FILES $X10_FILES
  do
      hasHeader=yes
      `grep -q "This file is part of the XASDI project (http://x10-lang.org/xasdi/)." $file` || hasHeader=no
      `grep -q "This file is licensed to You under the Eclipse Public License (EPL)" $file` || hasHeader=no
      `grep -q "(C) Copyright IBM Corporation 2014-2018." $file` || hasHeader=no
      if [[ "X$hasHeader" == "Xno" ]]; then
	  echo "Invoking standardizeHeader.pl on $file"
	  $top/xasdi/releng/standardizeHeader.pl $file
      fi
  done
  cd $top
done
