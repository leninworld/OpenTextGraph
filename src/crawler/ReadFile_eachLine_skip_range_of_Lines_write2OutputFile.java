package crawler;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

import jdk.jfr.events.FileWriteEvent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

// read each line of a file, remove duplicates and write to another
public class ReadFile_eachLine_skip_range_of_Lines_write2OutputFile {

	//USE AWK script which is FASTER
	// read each line of a file, remove duplicates and write to another
	public static void readFile_eachLine_skip_range_of_Lines_write2OutputFile(
																		String InputFile,
																		String OutputFile,
																		int start_lineNumber_to_skip,
																		int end_lineNumber_to_skip,
																		boolean is_Append_out_File,
																		boolean isSOPprint
																		){
		TreeMap<String , String> map_checkDuplicate=new TreeMap<String , String>();
		 
		try {
			 FileWriter writer=new FileWriter(new File(OutputFile), is_Append_out_File);
			 //LineIterator it = IOUtils.lineIterator(new BufferedReader(new FileReader(InputFile)));
			 String primarykey="";  int n_currLine=0; String line="";
			 	 //
			 BufferedReader reader = new BufferedReader(new FileReader(InputFile));
				// read each line of given file
				while ((line = reader.readLine()) != null) {
					 n_currLine++;
					 //skip range of lines
					 if(n_currLine >= start_lineNumber_to_skip && n_currLine<=end_lineNumber_to_skip ){
						 continue;
					 }
					 
				    //blank skip
				    if(line.length()<=1) continue;
				    
				    System.out.println("lineNumber:"+n_currLine);
				     
				    //System.out.println("line:"+line);
				    //if(line.length()>0 && !map_checkDuplicate.containsKey(calc_checksum.calc_checksum_for_string(primarykey))) {
				    	
				    	if(isSOPprint)
				    		System.out.println("matched line:"+line);
				    	 
				    	writer.append(line+"\n");
				    	writer.flush();
//				    }
				    
//				    if (lineNumber == expectedLineNumber) {
//				        return line;
//				    }
				    
//				    map_checkDuplicate.put(  calc_checksum.calc_checksum_for_string(primarykey), "");
				 }
				 writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	// main
	public static void main(String[] args) throws IOException {
		
		String InputFile="";String OutputFile="";
		
		String baseFolder="";
			   baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/";
			   baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/";
		 
	    InputFile =baseFolder+"output_gbad_.g";
		OutputFile=baseFolder+"output_gbad_clean.g";
		
		InputFile="OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txtNEW.txt_bTextNLPtagged.txt";
		OutputFile="OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txtNEW.txt_bTextNLPtagged_NODUP.txt";
		
		boolean is_Append_out_File=true;
		int token_index_to_consider_for_duplicate=5;
		
		//USE AWK script which is FASTER
		// readFile_readEachLine_removeDuplicates_WriteToAnotherFile
		readFile_eachLine_skip_range_of_Lines_write2OutputFile(
																	InputFile,
																	OutputFile,
																	1600597,//start_lineNumber_to_skip
																	1747465,//end_lineNumber_to_skip
																	false , //is_Append_out_File,
																	false //isSOPprint
																	);
		
		
	}

}

