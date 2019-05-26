
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Simon and Zohar
 * 	
 *	We have a function that receives and returns a natural number. 
 *	The function sometimes gets stuck, but most times returns
 *	Now we are building a shell
 *	To the function so that it can be called with a maximum time (max) value
 *	if the function of the function is completed within the time frame -
 *  The envelope will return (immediately)
 *  
 */
public class Ex3A
{
	
	/**
	 * A method that receives a natural number and duration
     * Time, and calculates if the natural number is prime - 
     * as long as the time allotted for calculating has not passed,
     * once the time has passed for the function to throw Error.
	 * 
	 * @param n
	 * @param maxTime
	 * @return boolean
	 * @throws RuntimeException
	 */
	public boolean isPrime(long n, double maxTime) throws RuntimeException
	{
		boolean ans;

		ExecutorService executor = Executors.newCachedThreadPool();

		Future<Boolean> future = executor.submit(new Callable<Boolean>() {

		    @Override
		    public Boolean call() throws RuntimeException {
		        
		        Boolean ans= Ex3_tester.isPrime(n);
		        
		        return ans;
		        
		    }
		});		
		try {
			 ans=future.get((long) (maxTime*1000), TimeUnit.MILLISECONDS);

		} catch (RuntimeException | InterruptedException | ExecutionException | TimeoutException e) {
			future.cancel(true);
			throw new RuntimeException() ;

		}
		executor.shutdown();

		return ans;

		
	}
	
}


