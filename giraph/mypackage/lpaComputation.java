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
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.HashMap;


import java.io.IOException;


/*

label propagation.
Initially label is vertex own id and then it evolves to the most edge id which is most present in community.


*/
public class lpaComputation extends BasicComputation<LongWritable, PropagationHistory, LongWritable, PropagatedMessage> 
{
    public void compute(Vertex<LongWritable, PropagationHistory, LongWritable> vertex, Iterable<PropagatedMessage> messages) throws IOException 
	{	
		log("______________________________________________________________________________________________ getId: " +  vertex.getId());
		
		
		if(getSuperstep() == 0)
		{
			vertex.setValue(new PropagationHistory(vertex.getId().get(),vertex.getId().get()));
			PropagatedMessage propagatedMessage = new PropagatedMessage(vertex.getId().get(),vertex.getId().get()); // send to all neighbours who is the mos frequent neighbour.
			sendMessageToAllEdges(vertex, propagatedMessage);			
		}
		else
		{
			
			for (PropagatedMessage message : messages) 
			{
				long labelValue = message.getLabelValue();
				long sourceVertexId = message.getSourceVertexId();
				vertex.setEdgeValue(new LongWritable(sourceVertexId), new LongWritable(labelValue));
			}
			
			Map<Long, Long> mapLabelsCount = new HashMap<Long, Long>();
			for (Edge<LongWritable, LongWritable> edge : vertex.getEdges()) 
			{
				Long edgeTargetVertexId = edge.getTargetVertexId().get();
				Long newEdgeTargetValue = edge.getValue().get() + 1;
				mapLabelsCount.put(edgeTargetVertexId,newEdgeTargetValue);
			}
			
			Long mostOccuringLabel = modeLabels(mapLabelsCount); //most frequent neighbouring edge
			Long currentLabel = vertex.getValue().getCurrentLabel();  
			Long previousLabel = vertex.getValue().getPreviousLabel();
			
			
			Long clOccurence = mapLabelsCount.get(currentLabel);
			Long maxLabelOccurence = mapLabelsCount.get(mostOccuringLabel);
			
			if(clOccurence < maxLabelOccurence && mostOccuringLabel != previousLabel)
			{
				vertex.setValue(new PropagationHistory(currentLabel,mostOccuringLabel)); // set previous most highly frequent neighbour and current highest one.
				PropagatedMessage propagatedMessage = new PropagatedMessage(vertex.getId().get(),mostOccuringLabel); // send to all neighbours who is the mos frequent neighbour.
				sendMessageToAllEdges(vertex, propagatedMessage);
			}
		}	
        
        vertex.voteToHalt();
    }
	
	private Long modeLabels(Map<Long, Long> mapLabelsCount)
	{
		Long maxLabelOccuring = -9999999L;
		Long maxOccurence = -9999999L;
		
		for (Map.Entry<Long, Long> entry : mapLabelsCount.entrySet()) 
		{
			Long label = entry.getKey();
			Long labelCount = entry.getValue();
			if((labelCount > maxOccurence) ||  (labelCount == maxOccurence && label < maxLabelOccuring))
			{
				maxOccurence = labelCount;
				maxLabelOccuring = label;
			}
		}		
		return maxLabelOccuring;
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


