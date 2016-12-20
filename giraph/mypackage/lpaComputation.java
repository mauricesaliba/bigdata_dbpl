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
		
		
		
		if(getSuperstep() == 0)
		{
			vertex.setValue(new PropagationHistory(vertex.getId().get(),vertex.getId().get()));
			PropagatedMessage propagatedMessage = new PropagatedMessage(vertex.getId().get(),vertex.getId().get()); // send to all neighbours own id as label.
			sendMessageToAllEdges(vertex, propagatedMessage);			
		}
		else
		{
			//receive message from neighbours and set edge value with each receive propagated value
			for (PropagatedMessage message : messages) 
			{
				long labelValue = message.getLabelValue();
				long sourceVertexId = message.getSourceVertexId();
				vertex.setEdgeValue(new LongWritable(sourceVertexId), new LongWritable(labelValue));
			}
			
			//store count(value) for each label per label (key)
			Map<Long, Long> mapLabelsCount = new HashMap<Long, Long>();
			for (Edge<LongWritable, LongWritable> edge : vertex.getEdges()) 
			{
				//Long edgeTargetVertexId = edge.getTargetVertexId().get();
				//Long newEdgeTargetValue = edge.getValue().get() + 1;
				//mapLabelsCount.put(edgeTargetVertexId,newEdgeTargetValue);
				
				
				long edgeLabel = edge.getValue().get();
				//if label exists increase its count else store it for the first time
				if(mapLabelsCount.containsKey(edgeLabel))
				{
					Long currentLabelCountValue = mapLabelsCount.get(edgeLabel);
					Long newLabelCountValue = currentLabelCountValue + 1;
					mapLabelsCount.put(edgeLabel,newLabelCountValue);
				}
				else
				{
					mapLabelsCount.put(edgeLabel,1L);
				}
								
			}
			
			Long mostOccuringLabel = modeLabels(mapLabelsCount); //most frequent neighbouring edge
			Long currentLabel = vertex.getValue().getCurrentLabel();  
			Long previousLabel = vertex.getValue().getPreviousLabel();			
			Long currentLabelOccurence = mapLabelsCount.get(currentLabel) == null? 0L : mapLabelsCount.get(currentLabel);
			Long maxLabelOccurence = mapLabelsCount.get(mostOccuringLabel);
			
			log( " getId:   " + vertex.getId() + 
					" mostOccuringLabel:   " + 	mostOccuringLabel + 
					" vertex.getValue():   " + vertex.getValue() + 
					" currentLabelOccurence:   " + currentLabelOccurence +
					" maxLabelOccurence:   " + maxLabelOccurence 
					
					
					);
			
			
			//check here // converging too quickly because of null check.
			//if(maxLabelOccurence !=null && mostOccuringLabel != null && previousLabel != null)
			//{
				if(mostOccuringLabel != null && currentLabelOccurence != null)
				{
					if((getSuperstep() == 1) || (currentLabelOccurence < maxLabelOccurence  && mostOccuringLabel != previousLabel))
					{
						vertex.setValue(new PropagationHistory(currentLabel,mostOccuringLabel)); // set previous most highly frequent neighbour and current highest one.
						PropagatedMessage propagatedMessage = new PropagatedMessage(vertex.getId().get(),mostOccuringLabel); // send to all neighbours who is the mos frequent neighbour.
						sendMessageToAllEdges(vertex, propagatedMessage);
					}
				}
			//}
		}	
        
        vertex.voteToHalt();
    }
	
	private Long modeLabels(Map<Long, Long> mapLabelsCount)
	{
		Long maxLabelOccuring = Long.MIN_VALUE;
		Long maxOccurence = Long.MIN_VALUE;
		
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
		return maxLabelOccuring == Long.MIN_VALUE ? null : maxLabelOccuring;
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


