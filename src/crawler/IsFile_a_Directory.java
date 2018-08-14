package crawler;

import java.io.File;
import java.util.HashMap;

public class IsFile_a_Directory {
	

	// check numeric
	public static boolean isFile_a_Directory(String inputFile) {
		try {
			
			boolean isDirectory=(new File(inputFile)).isDirectory();
			
			if(isDirectory)
				return true;
			else 
				return false;
		} catch (Exception e) {
			
		}
		return false;
	}
	 
		


}
