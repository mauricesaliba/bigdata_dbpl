package mypackage;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;


//this data class store previous and current label which is used in label propagation algorithm
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
      StringBuilder str = new StringBuilder();
      str.append(previousLabel);
      str.append(",");
      str.append(currentLabel);
      return str.toString();
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