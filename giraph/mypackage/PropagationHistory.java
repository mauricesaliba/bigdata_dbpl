package mypackage;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class PropagationHistory implements Writable
  {

    private Long previousLabel;
    private Long currentLabel;
    
    public PropagationHistory() 
	{ 
	
	}

    public PropagationHistory(Long previousLabel, Long currentLabel) 
	{     
      this.previousLabel = previousLabel;
      this.currentLabel = currentLabel;
    }
	
	public Long getPreviousLabel()
	{
		return this.previousLabel;
	}
	
	public void setPreviousLabel(long previousLabel)
	{
		this.previousLabel = previousLabel;
	}	
		
	public Long getCurrentLabel()
	{
		return this.currentLabel;
	}
	
	public void setCurrentLabel(long currentLabel)
	{
		this.currentLabel = currentLabel;
	}	

    @Override
    public String toString() 
	{
      return "(previousLabel=" + previousLabel + ",currentLabel=" + currentLabel + ")";
    }

	public void readFields(DataInput input) throws IOException 
	{
		previousLabel = input.readLong();
		currentLabel = input.readLong();		
	}

	public void write(DataOutput output) throws IOException 
	{
		  output.writeLong(previousLabel);
		  output.writeLong(currentLabel);	
	}
  }