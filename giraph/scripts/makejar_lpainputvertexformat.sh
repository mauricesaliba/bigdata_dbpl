#copy format java class to io.formats folder
cp /home/temp/masters_dm_assignment/giraph/mypackage/JsonLongNullLongLongPropagationVertexInputFormat.java /usr/local/giraph/giraph-core/src/main/java/org/apache/giraph/io/formats

#copy PropagationHistory needed for compilation of previous class
cp /home/temp/masters_dm_assignment/giraph/mypackage/PropagationHistory.java /usr/local/giraph/giraph-core/src/main/java/org/apache/giraph/io/formats


cd /usr/local/giraph/giraph-core/src/main/java/org/apache/giraph/io/formats

#compile PropagationHistory 
javac -cp /usr/local/giraph/giraph-examples/target/giraph-examples-1.1.0-SNAPSHOT-for-hadoop-2.4.1-jar-with-dependencies.jar:$($HADOOP_HOME/bin/hadoop classpath)  PropagationHistory.java 

#compile lpa vertex format class
mkdir mypackage
cp PropagationHistory.class mypackage/

javac -cp /usr/local/giraph/giraph-core/src/main/java/org/apache/giraph/io/formats:/usr/local/giraph/giraph-examples/target/giraph-examples-1.1.0-SNAPSHOT-for-hadoop-2.4.1-jar-with-dependencies.jar:$($HADOOP_HOME/bin/hadoop classpath) JsonLongNullLongLongPropagationVertexInputFormat.java


#extend computation jar file with newly compiled custom vertex input formats
cd /usr/local/giraph/giraph-core/src/main/java/
jar uf /usr/local/giraph/giraph-examples/target/giraph-examples-1.1.0-SNAPSHOT-for-hadoop-2.4.1-jar-with-dependencies.jar org/apache/giraph/io/formats/JsonLongNullLongLongPropagationVertexInputFormat.class org/apache/giraph/io/formats/JsonLongNullLongLongPropagationVertexInputFormat\$JsonLongNullLongLongVertexReader.class

