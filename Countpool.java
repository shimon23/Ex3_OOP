

import java.util.concurrent.Callable;

/**
 * 
 * @author Shimon and Zohar
 *
 * class that implements the Callable
 */
public class Countpool implements Callable<Integer> 
{
    private	String filename;
	public Countpool(String file) 
	{
		this.filename = file;
	}

  
  public Integer call() throws Exception 
  { 
	  
    Integer CL= LineCounter.Countline(filename);
    return CL;
  } 
  
}
