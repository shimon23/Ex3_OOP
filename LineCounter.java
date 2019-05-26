
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * 
 * @author Shimon and Zohar
 *
 *A class called LineCounter that represents the thread that calculates the line number of the file.
 *The class constructor gets the filename.
 */
public class LineCounter extends Thread{
	private String FN ;//the file name
	private static volatile Integer count=0; //counting lines
	
	
	public LineCounter(String name) {
		this.FN=name;
	}
	public static int getline() {
		return count;
	}
	public void run() {
		int Numlineinfile=Countline(FN);
			synchronized (count) {
				LineCounter.count=LineCounter.count+Numlineinfile;

			}		       
    }
	/**
	 * this function count line in file text, i take it from
	 * 	//https://stackoverflow.com/questions/1277880/how-can-i-get-the-count-of-line-in-a-file-in-an-efficient-way
	 * @param File_name the file name 
	 * @return number of line 
	 */
	public static int Countline(String File_name) {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(File_name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] buffer = new byte[8192];
		int count = 0;
		int n;
		try {
			while ((n = stream.read(buffer)) > 0) {
			    for (int i = 0; i < n; i++) {
			        if (buffer[i] == '\n') count++;
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Number of lines: " + count);
		return count;
	}


}
