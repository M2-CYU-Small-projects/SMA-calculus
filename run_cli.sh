#!/bin/sh

####################################################################
# This script permits to launch the Jade CLI with all agents 
# compiled in the "out" folder loaded.
#
# This script accepts multiple arguments :
# . The first one is the path where the jade jar is stored: If 
#   it is not necessary, you can simply put "."
#
# . The others are the ones that will be passed as parameters to 
#   the jade command (such as "-agents", "-gui", etc.)
#
#
# If needed, it is possible to add an argument to define where the 
# "jade.jar" archive is stored.
#
####################################################################

# Stop at first error
set -e

# Get the script current directory
BASEDIR=$(dirname "$0")

# If the Jade JAR path is not in the classpath, you can specify it as 
# a parameter argument, else, default "." will be taken
JADE_JAR_PATH=${1:-.}

OUT_FOLDER=$BASEDIR/out

CP=".:$SRC_FOLDER:$JADE_JAR_PATH:$OUT_FOLDER:$CLASSPATH"


shift
# Send all but the first argument to the program
java -cp $CP jade.Boot "$@"