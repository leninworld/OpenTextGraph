package crawler;
import java.io.*;

public class Check_for_existence_of_a_file_N_create_if_not_exist {
	
	//check_for_existence_of_a_file_N_create_if_not_exist
	public static boolean check_for_existence_of_a_file_N_create_if_not_exist(String inFile){
		try{
			
			if(! new File(inFile).exists() )
				new File(inFile).createNewFile();
			
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	// main
	public static void main(String[] args){
		
	}
	
}
