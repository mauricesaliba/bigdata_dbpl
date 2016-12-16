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
rm -f /usr/local/giraph/myassignment/mypackage/$code_file.java
#delete old compiled code
rm -f /usr/local/giraph/myassignment/mypackage/$code_file.class
#copy new code
cp /home/temp/masters_dm_assignment/giraph/mypackage/$code_file.java  /usr/local/giraph/myassignment/mypackage
#compile new code
cd /usr/local/giraph/myassignment/
javac -cp /usr/local/giraph/giraph-examples/target/giraph-examples-1.1.0-SNAPSHOT-for-hadoop-2.4.1-jar-with-dependencies.jar:$($HADOOP_HOME/bin/hadoop classpath) /usr/local/giraph/myassignment/mypackage/$code_file.java
#extend jar with new code
cd /usr/local/giraph/myassignment
jar uf /usr/local/giraph/myassignment/myjar.jar  mypackage
echo "------------------     Ending compilation and packaging!! with $code_file  ----------------------"

