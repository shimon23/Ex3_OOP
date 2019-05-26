import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random ;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor; 
/**
 * 
 * @author Shimon and Zohar
 *
 * A program that receives a list of text files and performs counting,deleting, and so on. 
 * The program should build a thread for each file that should calculate the number of rows of each file in parallel.
 */
public class Ex3B {
	/**
	 * A static function that creates a given number of text files. 
	 * Each file contains a random number of rows, per line
     * Write one sentence: "World Hello."
     * The function receives an integer n representing the number of files. 
     * The function returns an array of file names.
     * 
	 * @param n
	 * @return String[]
	 */
//https://stackoverflow.com/questions/11496700/how-to-use-printwriter-and-file-classes-in-java/28702195		
public static String[] createFiles(int n) {
		String[] arr= new String[n];
		
		for(int i=0;i<n;i++) {
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("File_"+ i +".txt", "UTF-8");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Random r = new Random(123);
			int numLines = r.nextInt(1000);
			for(int j=0;j<numLines;j++) {
			writer.println("Hello World");
			}
			writer.close();

			arr[i]="File_"+ i +".txt";
		}
		return arr;
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * A function that accepts and deletes file names
	 * @param fileNames
	 */
public static void deleteFiles(String[] fileNames) {
		for(int i=0;i<fileNames.length;i++) {
			try {
				Files.delete(Paths.get("C:\\Users\\Shimo\\eclipse-workspace\\Ex3_OOP\\"+fileNames[i]));
				fileNames[i]="";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
/**
 * A function that calculates the total number of lines without using threads,
 * The function creates files, reads files one by one, 
 * prints the total number of rows and the run time
 * (Excluding creation and deletion of files).
 * At the end of the function all files are deleted.	

 * @param numFiles
 */
public static void countLinesOneProcess(int numFiles) {
		long start= System.currentTimeMillis();
		String []filename =createFiles(numFiles);
		int sum=0;
		for(int i=0;i<filename.length;i++) {			
		sum=sum+LineCounter.Countline(filename[i]);
	}
		long end= System.currentTimeMillis();
		System.out.println("OneProcess time = " +(end-start)+" ms, lines ="+ sum);
		deleteFiles(filename);

	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The function accepts multiple files as an argument.
	 * The function creates files, triggers for each file the thread and prints the total 
	 * number of rows in all files. 
	 * The function also prints the runtime of the threads (not including creating and deleting files)
     * at the end of the function all files are deleted.
     * 
	 * @param numFiles
	 */
public static void countLinesThreads(int numFiles) {
		long start = System.currentTimeMillis();
		String []filename = createFiles(numFiles);
		LineCounter [] L = new LineCounter[numFiles];
		Thread [] threads = new Thread[numFiles];
		for(int i=0;i<filename.length;i++) {
			L[i]=new LineCounter(filename[i]);
			threads[i]=new Thread(L[i]);
			threads[i].start();
		}
		for(int i=0;i<filename.length;i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		
		System.out.println("Thread time = " +(end-start)+" ms, lines ="+ LineCounter.getline());
	    deleteFiles(filename);	
		
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The function accepts multiple files as an argument. 
	 * The function creates files, triggers for each file the thread
 	 * Created in TreadPool and prints the total number of rows in all files.
   	 * The function also prints the total runtime (not including creating and deleting files)
 	 * At the end of the function all files are deleted.
	 * @param num
	 */
	//https://stackoverflow.com/questions/46718594/threadpool-with-callable	
	public static void countLinesThreadPool(int num) {
    	long start = System.currentTimeMillis();
    	int sum=0;
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(num);
        String [] filename = createFiles(num);
        ArrayList<Future<Integer>> future = new ArrayList<Future<Integer>>();
        for (int i = 0; i < filename.length; i++) {
            Future<Integer> f = pool.submit(new Countpool(filename[i]));
            future.add(f);
        }
        for (int j = 0; j < future.size(); j++) {
                Future<Integer> f = future.get(j);
                try {
					sum+=f.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            
        }
        pool.shutdown();
        long end = System.currentTimeMillis();
	    System.out.println("Threadpool time = " +(end-start)+" ms, lines ="+sum);
		deleteFiles(filename);
            
        }
        

	public static void main(String[]args)
	{
		countLinesOneProcess(1000);
		countLinesThreads(1000);
    	countLinesThreadPool(1000);
	}
		
		
}
	

