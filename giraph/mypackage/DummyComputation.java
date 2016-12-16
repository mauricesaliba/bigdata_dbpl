package mypackage;

import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import org.apache.giraph.conf.LongConfOption;
import org.apache.giraph.edge.Edge;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;



public class DummyComputation extends BasicComputation<LongWritable, DoubleWritable, FloatWritable, DoubleWritable> 
{
	
    @Override
    public void compute(Vertex<LongWritable, DoubleWritable, FloatWritable> vertex, Iterable<DoubleWritable> messages) throws IOException 
	{
		
		
		try
		{
			
			PrintWriter writer = 
			
			
			 new PrintWriter(new FileOutputStream(
								new File("giraph_logs.txt"), 
								true /* append = true */)); 		
			
			writer.println("______________________________________________________________________________________________");
			writer.println("getId: " +  vertex.getId());
			writer.println("getNumEdges: " + vertex.getNumEdges());
			writer.println("getId: " +  vertex.getId());
			writer.close();
		} 
		catch (IOException e) 
		{
			   // do something
		}				
		
		
        vertex.setValue(new DoubleWritable(8.0));
        vertex.voteToHalt();
    }
}