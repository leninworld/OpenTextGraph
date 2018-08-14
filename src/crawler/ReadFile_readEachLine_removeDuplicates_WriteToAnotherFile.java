package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;
import java.util.TreeMap;

import jdk.jfr.events.FileWriteEvent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import crawler.ID3.baseImpure;

// read each line of a file, remove duplicates and write to another
public class ReadFile_readEachLine_removeDuplicates_WriteToAnotherFile {

	//USE AWK script which is FASTER
	// read each line of a file, remove duplicates and write to another
	public static void readFile_readEachLine_removeDuplicates_WriteToAnotherFile(
																		String InputFile,
																		String OutputFile,
																		String token_index_to_consider_for_duplicateCSV,
																		boolean is_token_index_to_consider_for_duplicate_a_URL,
																		boolean is_Append_out_File,
																		boolean isSOPprint
																		){
		TreeMap<String , String> map_checkDuplicate=new TreeMap<String , String>();
		String [] arr_index_primaryKeys=token_index_to_consider_for_duplicateCSV.split(",");
		try {
			 FileWriter writer=new FileWriter(new File(OutputFile), is_Append_out_File);
			 LineIterator it = IOUtils.lineIterator(new BufferedReader(new FileReader(InputFile)));
			 String primarykey=""; 
			 TreeMap<Integer, Integer> map_index_ofPrimaryKEYS_asKEYS=new TreeMap<Integer, Integer>();
			 int c=0;
			 while(c<arr_index_primaryKeys.length){
				 map_index_ofPrimaryKEYS_asKEYS.put(Integer.valueOf(arr_index_primaryKeys[c])-1, -1);
				 c++;
			 }
			 
			 
			 	 //
				 for (int lineNumber = 0; it.hasNext(); lineNumber++) {
				    String line = (String) it.next();
				    String originalLine=line;
				    line=line.toLowerCase();
				    //blank skip
				    if(line.length()<=1) continue;
				    
				    System.out.println("lineNumber:"+lineNumber);
				    String [] arr_token=line.split("!!!");
				    // 
				    c=0; String concPrimaryKey="";
				    while(c<arr_token.length){
				    	
				    	if(map_index_ofPrimaryKEYS_asKEYS.containsKey(c)){
				    		if(concPrimaryKey.length()==0)
				    			concPrimaryKey=arr_token[c];
				    		else
				    			concPrimaryKey+=","+arr_token[c];	
				    	}
				    	c++;
				    }
				    
				    int max_index=Collections.max(map_index_ofPrimaryKEYS_asKEYS.keySet());
				    
				    // violation in length of tokens
				    if(max_index>arr_token.length){
				    	System.out.println("TOKEN MISMATCH: lineNumber:"+lineNumber);
				    	continue;
				    }
				    
//				    primarykey=arr_token[token_index_to_consider_for_duplicate-1];
				    primarykey=concPrimaryKey;
			    	// CLEANING URL				    
				    if(is_token_index_to_consider_for_duplicate_a_URL==true && primarykey.indexOf("url=")>=0){
				    	primarykey=Clean_embeddedURLs.clean_get_embeddedURL(primarykey);
				    }
				    	
				    //System.out.println("line:"+line);
				    //if(line.length()>0 && !map_checkDuplicate.containsKey(calc_checksum.calc_checksum_for_string(primarykey))) {
				    	
				    	if(isSOPprint)
				    		System.out.println("matched line:"+line);
				    	
				    	 if(!map_checkDuplicate.containsKey(primarykey)){
					    	writer.append("\n"+originalLine);
					    	writer.flush();
					    	map_checkDuplicate.put(primarykey, "");
				    	 }
				    	
				    	
				    }
				    
//				    if (lineNumber == expectedLineNumber) {
//				        return line;
//				    }
				    
				    //map_checkDuplicate.put(  calc_checksum.calc_checksum_for_string(primarykey), "");
				 
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
			   baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/";
			   baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2014Only-numeric/";
			   baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/";
		 
	    InputFile =baseFolder+"all_issues_id37151552.txt";
	    InputFile =baseFolder+"out_dataID_mean_SD.txt";
	    InputFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txtNEW.txt_bTextNLPtagged.txt";
	    InputFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txtNEW_1000labelled_confCorrected.txt_1st1000ln_removeIrrelevaLABEL.txt_bTextNLPtagged.txt";
	    
		OutputFile=baseFolder+"all_issues_id37151552_NODUP.txt";
		OutputFile=baseFolder+"out_dataID_mean_SD_unique.txt";
		OutputFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txtNEW.txt_bTextNLPtagged_NODUP.txt";
		OutputFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txtNEW_1000labelled_confCorrected.txt_1st1000ln_removeIrrelevaLABEL.txt_bTextNLPtagged_NODUP.txt";
		
		boolean is_Append_out_File=true;
		String token_index_to_consider_for_duplicateCSV="5";
		
		//USE AWK script which is FASTER
		// readFile_readEachLine_removeDuplicates_WriteToAnotherFile
		ReadFile_readEachLine_removeDuplicates_WriteToAnotherFile.
		readFile_readEachLine_removeDuplicates_WriteToAnotherFile(
																	InputFile,
																	OutputFile,
																	token_index_to_consider_for_duplicateCSV,
																	false, //is_token_index_to_consider_for_duplicate_a_URL
																	false, //append
																	false //isSOPprint
																	);
		
		
	}

}
