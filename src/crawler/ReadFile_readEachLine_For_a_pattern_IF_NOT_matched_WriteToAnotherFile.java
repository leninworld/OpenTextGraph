package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import jdk.jfr.events.FileWriteEvent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

// read given file for pattern match on each line
public class ReadFile_readEachLine_For_a_pattern_IF_NOT_matched_WriteToAnotherFile {
	
	// read each line of a file to match for a pattern
	public static void readFile_readEachLine_For_a_pattern_IF_NOT_matched_WriteToAnotherFile(
																		String InputFile,
																		String OutputFile,
																		String pattern,
																		boolean is_Append_out_File
																		){
		try {
			 FileWriter writer=new FileWriter(new File(OutputFile), is_Append_out_File);
			 LineIterator it = IOUtils.lineIterator(
				       new BufferedReader(new FileReader(InputFile)));
				 for (int lineNumber = 0; it.hasNext(); lineNumber++) {
				    String line = (String) it.next();
				    line=line.toLowerCase();
				    //System.out.println("line:"+line);
				    if(line.indexOf(pattern)>=0){
				    	System.out.println("matched line (skipping):"+line);
				    	continue;
				    }
				    else{
				    	//System.out.println("matched line:"+line);
				    	writer.append("\n"+line);
				    	writer.flush();
				    }
				    
				    
				    
//				    if (lineNumber == expectedLineNumber) {
//				        return line;
//				    }
				 }
				 writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	//
	public static void readFileSearchForAPatternInSpecificLineOrder(String inputFolder){
		File directoryF = new File(inputFolder);
		try {
			String line = (String) FileUtils.readLines(directoryF).get(100);
			System.out.println(""+line);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	//
	public static void r(){
		try {
			 String text =
				        "Line1 blah blah\n" +
				        "Line2 more blah blah\n" +
				        "Line3 let's try something new \r\n" +
				        "Line4 meh\n" + 
				        "Line5 bleh\n" + 
				        "Line6 bloop\n";
				    //Scanner sc = new Scanner(text).skip("(?:.*\\r?\\n|\\r){4}");
			 		String skipText="Line4";
			 		Scanner sc = new Scanner(text).skip("(?:.*\\r?\\n|\\r){4}");
			 
				    while (sc.hasNextLine()) {
				        System.out.println("t:"+sc.nextLine());
				    }
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	// main
	public static void main(String[] args) throws IOException {
		
		String InputFile="";
		String baseFolder="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data/NYT-2014-09-TO-2014-12/";
		
		InputFile=baseFolder+"NYT_out_crawled.SET.2.txt";
		String OutputFile="";
		OutputFile=baseFolder+"NYT_out_crawled.SET.2_out.txt";
		String pattern="failed!!!";
		boolean is_Append_out_File=true;
		//
		readFile_readEachLine_For_a_pattern_IF_NOT_matched_WriteToAnotherFile(
																	InputFile,
																	OutputFile,
																	pattern,
																	is_Append_out_File
																	);
		
		
	}

}
