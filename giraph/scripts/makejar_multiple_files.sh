#!/bin/bash

code_file=$1


echo "------------------     Starting compilation and packaging!! with $code_file  ----------------------"
#create directories only if they do not yet exist
mkdir -p /usr/local/giraph/myassignment && mkdir -p /usr/local/giraph/myassignment/mypackage
#clean old jar
rm -f /usr/local/giraph/myassignment/myjar.jar 
#copy jar to extend
cp /usr/local/giraph/giraph-examples/target/giraph-examples-1.1.0-SNAPSHOT-for-hadoop-2.4.1-jar-with-dependencies.jar /usr/local/giraph/myassignment/myjar.jar 
#delete old code
rm -f /usr/local/giraph/myassignment/mypackage/*
#copy new code
cp -a /home/temp/masters_dm_assignment/giraph/mypackage/.  /usr/local/giraph/myassignment/mypackage
#compile new code
cd /usr/local/giraph/myassignment/mypackage

javac -cp /usr/local/giraph/giraph-examples/target/giraph-examples-1.1.0-SNAPSHOT-for-hadoop-2.4.1-jar-with-dependencies.jar:$($HADOOP_HOME/bin/hadoop classpath) /usr/local/giraph/myassignment/mypackage/PropagatedMessage.java
javac -cp /usr/local/giraph/giraph-examples/target/giraph-examples-1.1.0-SNAPSHOT-for-hadoop-2.4.1-jar-with-dependencies.jar:$($HADOOP_HOME/bin/hadoop classpath) /usr/local/giraph/myassignment/mypackage/PropagationHistory.java
javac -cp /usr/local/giraph/myassignment:/usr/local/giraph/giraph-examples/target/giraph-examples-1.1.0-SNAPSHOT-for-hadoop-2.4.1-jar-with-dependencies.jar:$($HADOOP_HOME/bin/hadoop classpath) /usr/local/giraph/myassignment/mypackage/$code_file.java

#extend jar file with compiled code
cd /usr/local/giraph/myassignment
jar uf /usr/local/giraph/myassignment/myjar.jar  mypackage
echo "------------------     Ending compilation and packaging!! with $code_file  ----------------------"

