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
 

public class RemoveUnicodeChar {
	// 
	public static String removeUnicodeChar(String input){
		try{
			input=input.replaceAll("[\u0000-\u001f]", "").replaceAll("\\p{Cntrl}", "")
						.replace("\\", "").replace("/","");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return input;
	}
	 
	 
	
	//main
	public static void main(String[] args) throws IOException {
		TreeMap<String,String> mapout= new TreeMap<String, String>();
 
		String a= "jer\u00e9 \\/longman";
		//a="body\/head (music group)";
		System.out.println(a.replace("/\\", "").replace("//", "").replaceAll("(\\r|\\n)", "") )	;
		String str= a.replaceAll("[\u0000-\u001f]", "").replaceAll("\\p{Cntrl}", "").replaceAll("\\\\", "")
					 .replaceAll("////", "").replaceAll("[^\\x00-\\x7F]", "");;
 
		System.out.println(":"+a.replaceAll("\\\\", "").replace("\\", "")+"::"+a.indexOf("/"));
		int s=a.indexOf("/");
		
		String ss ="apple inc (amzn\\/nasdaq)".replace("\\", "").replace("/", "");
		System.out.println(ss);
		
		//
		//if(){
			
		//}
		
	}
}
