package crawler;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Check_if_a_file_path_is_absolute_or_not {
	
	//check_if_a_file_path_is_absolute_or_not
	public static boolean check_if_a_file_path_is_absolute_or_not(String inFile){
		try{
 
			Path p = Paths.get(inFile); 
			if ( p.isAbsolute() ) {
			     return true;
			}
			else
				return false;
		}
		catch(Exception e){
			return false;
		}
		 
	}
	
	// main
	public static void main(String[] args){
		
	}
	
}
