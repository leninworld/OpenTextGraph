package crawler;
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

//import  me.champeau.ld.UberLanguageDetector;

public class Extract_keywords_from_thehindu_url {
	 
	// extract_keywords_from_thehindu_url
	public static void extract_keywords_from_thehindu_url(String inputFile, String outFile ){
		BufferedReader br=null;
		try{
			br = new BufferedReader(new FileReader(inputFile));
			FileWriter writer= new FileWriter(new File(outFile));
			String line="";
			while( (line = br.readLine()) !=null){
				System.out.println("l:"+line);
				if(line.length()>=1)
				//writer.append( line.substring(1, line.length()) +"\n");
				writer.append( "mv "+line+" " +line.replace(".g.txt", ".g") +"\n");
				writer.flush();
			}
			
			System.out.println("tes");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//main
	public static void main(String[] args) throws IOException {
		TreeMap<String,String> mapout= new TreeMap<String, String>();
  	
		System.out.println("clean:" );
		
	}
}
