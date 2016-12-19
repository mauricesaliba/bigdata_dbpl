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
import org.apache.hadoop.io.IntWritable;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import java.io.IOException;


//modified DummyComputation_02 to except Long^4 format
public class DummyComputation_03 extends BasicComputation<LongWritable, LongWritable, LongWritable, LongWritable> 
{
	
    @Override
    public void compute(Vertex<LongWritable, LongWritable, LongWritable> vertex, Iterable<LongWritable> messages) throws IOException 
	{	
		log("______________________________________________________________________________________________ getId: " +  vertex.getId());
		
	
		//output edges in a list
		for (Edge<LongWritable,LongWritable> e : vertex.getEdges()) 
		{
			log(String.valueOf(e.getTargetVertexId()));
		}
		
        vertex.setValue(new LongWritable(16L));
        vertex.voteToHalt();
    }
	
	
	private void log(String text)
	{	
		try
		{		
			PrintWriter writer = new PrintWriter(new FileOutputStream( new File("giraph_logs.txt"), true /* append = true */)); 
			writer.println(text);
			writer.close();
		} 
		catch (IOException e) 
		{
			   // do something
		}	
	}
}


