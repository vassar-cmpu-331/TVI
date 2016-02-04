#!/bin/bash

DEST=$1

if [ ! -e VERSION ] ; then
	echo "You must generate a VERSION file before you can install TVI."
	exit 1
fi

VERSION=`cat VERSION`
SOURCE=target/tvi-$VERSION.jar
if [ ! -e $SOURCE ] ; then
	echo "Please generate the jar file before trying to install it!"
	exit 1
fi

if [ -z $DEST ] ; then
	DEST=$HOME/bin
fi

if [ ! -d $DEST ] ; then
	echo "No such directory: $DEST"
	exit 1
fi

echo "#!/bin/bash" > $DEST/tvi
echo "java -Xmx512M -jar $DEST/tvi-$VERSION.jar \$@" >> $DEST/tvi

chmod a+x $DEST/tvi
cp target/tvi-$VERSION.jar $DEST