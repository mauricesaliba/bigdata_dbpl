package mypackage;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class PropagatedMessage implements Writable 
{

	private Long sourceVertexId;
	private Long labelValue;

	public PropagatedMessage() { }


	public PropagatedMessage(Long sourceVertexId, Long labelValue) 
	{     
	  this.sourceVertexId = sourceVertexId;
	  this.labelValue = labelValue;
	}
	
	public Long getLabelValue()
	{
		return this.labelValue;
	}
	
	public void setLabelValue(Long labelValue)
	{
		this.labelValue = labelValue;
	}
	
		
	public long getSourceVertexId()
	{
		return this.sourceVertexId;
	}
	
	public void setsourceVertexId(long sourceVertexId)
	{
		this.sourceVertexId = sourceVertexId;
	}
	

	@Override
	public String toString() 
	{
	  return "(sourceVertexId=" + sourceVertexId + ",sourceVertexId=" + sourceVertexId + ")";
	}

	public void readFields(DataInput input) throws IOException {
		  labelValue = input.readLong();
		  sourceVertexId = input.readLong();		
	}

	public void write(DataOutput output) throws IOException {
		  output.writeLong(sourceVertexId);
		  output.writeLong(labelValue);
		
	}
}