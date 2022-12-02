#!/bin/sh

# Stop at first error
set -e

# Get the script current directory
BASEDIR=$(dirname "$0")

# If the Jade JAR path is not in the classpath, you can specify it as 
# a parameter argument, else, default "." will be taken
JADE_JAR_PATH=${1:-.}

SRC_FOLDER=$BASEDIR/src
OUT_FOLDER=$BASEDIR/out
PACKAGE_PATH=fr/cibultali

CP=".:$SRC_FOLDER:$JADE_JAR_PATH:$OUT_FOLDER:$CLASSPATH"

# Create the output folder if needed
mkdir -p $OUT_FOLDER

JAVAC_COMMAND="javac --release 8 -cp $CP -d $OUT_FOLDER"

$JAVAC_COMMAND $SRC_FOLDER/$PACKAGE_PATH/Function.java
$JAVAC_COMMAND $SRC_FOLDER/$PACKAGE_PATH/MyFunction.java
$JAVAC_COMMAND $SRC_FOLDER/$PACKAGE_PATH/FunctionFactory.java
$JAVAC_COMMAND $SRC_FOLDER/$PACKAGE_PATH/ComputeAgent.java
$JAVAC_COMMAND $SRC_FOLDER/$PACKAGE_PATH/ComputeCreatorAgent.java
$JAVAC_COMMAND $SRC_FOLDER/$PACKAGE_PATH/TestParallelAgent.java
