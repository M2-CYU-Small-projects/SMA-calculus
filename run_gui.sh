#!/bin/sh

# This script permits to compile the program. 
# This creates an "out" folder where the .class files are stored
# If needed, it is possible to add an argument to define where the 
# "jade.jar" archive is stored.

# Stop at first error
set -e

# Get the script current directory
BASEDIR=$(dirname "$0")

# If the Jade JAR path is not in the classpath, you can specify it as 
# a parameter argument, else, default "." will be taken
JADE_JAR_PATH=${1:-.}

OUT_FOLDER=$BASEDIR/out

CP=".:$SRC_FOLDER:$JADE_JAR_PATH:$OUT_FOLDER:$CLASSPATH"

java -cp $CP jade.Boot -gui

