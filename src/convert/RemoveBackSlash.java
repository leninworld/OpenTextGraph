package convert;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.util.TreeMap;
 

public class RemoveBackSlash {
	// 
	public static String removeBackSlash(String input){
		try{
			int i=input.indexOf("/");
			if(i>=0 ){
				input=input.substring(0, i) + input.substring(i+1, input.length());
			}
			else{
				return input;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return input;
	}
	//main
	public static void main(String[] args) throws IOException {
		TreeMap<String,String> mapout= new TreeMap<String, String>();
 
		String a= "jer \\/longman";
	
		int s=a.indexOf("/");
		System.out.println(removeBackSlash(a));
		//
		//if(){
			
		//}
		
	}
}
