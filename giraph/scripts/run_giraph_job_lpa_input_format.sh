#!/bin/bash

code_file=$1
input_file=$2
echo "----------------------        Starting running giraph job!! with $code_file -----------------------"
#change current direcotry to giraph home
cd $GIRAPH_HOME
#build jar file
/home/temp/masters_dm_assignment/giraph/scripts/makejar_multiple_files.sh $code_file
#clean output of previous jobs
$HADOOP_HOME/bin/hdfs dfs -rm -f -r  /user/root/dummy-output
#clean input file before loading
$HADOOP_HOME/bin/hdfs dfs -rm -f -r  /user/root/input/$input_file
#load giraph input file onto hadoop cluster
$HADOOP_HOME/bin/hdfs dfs -put /home/temp/masters_dm_assignment/giraph/files_input/$input_file /user/root/input/$input_file
#run giraph job on top of hadoop
$HADOOP_HOME/bin/hadoop jar /usr/local/giraph/myassignment/myjar.jar org.apache.giraph.GiraphRunner mypackage.$code_file --yarnjars myjar.jar --yarnheap 2048 --workers 2 --vertexInputFormat org.apache.giraph.io.formats.JsonLongNullLongLongPropagationVertexInputFormat --vertexInputPath /user/root/input/$input_file -vertexOutputFormat org.apache.giraph.io.formats.IdWithValueTextOutputFormat --outputPath /user/root/dummy-output
echo "----------------------------         FINISHED GIRAPH JOB          -------------------------------"