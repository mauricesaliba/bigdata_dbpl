#!/bin/bash

code_file=$1
input_file=$2
echo "----------------------        Starting running giraph job!! with $code_file -----------------------"

#build jar file
/home/temp/masters_dm_assignment/giraph/scripts/makejar.sh $code_file
#clean output of previous jobs
$HADOOP_HOME/bin/hdfs dfs -rm -f -r  /user/root/dummy-output
#load giraph input file onto hadoop cluster
$HADOOP_HOME/bin/hdfs dfs -put /home/temp/masters_dm_assignment/giraph/files_input/$input_file /user/root/input/$input_file
#run giraph job on top of hadoop
$HADOOP_HOME/bin/hadoop jar /usr/local/giraph/myassignment/myjar.jar org.apache.giraph.GiraphRunner mypackage.$code_file --yarnjars myjar.jar --workers 1 --vertexInputFormat org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat --vertexInputPath /user/root/input/$input_file -vertexOutputFormat org.apache.giraph.io.formats.IdWithValueTextOutputFormat --outputPath /user/root/dummy-output
echo "----------------------------         FINISHED GIRAPH JOB          -------------------------------"