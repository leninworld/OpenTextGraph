package p18;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

import crawler.Find_domain_name_from_Given_URL;
import crawler.Load_domainname_pattern4crawledHTML;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import crawler.Clean_retain_only_alpha_numeric_characters;

// each line , a specific token will be filter based on a given length.
public class ReadFile_read_a_token_FILTER_on_length {
	
 
	// file1 -> (1) REMOVE note:news articles (2) remove NOT FOUND startPattern for news article
	//          (3) REMOVE short message on given length
	public static void readFile_read_a_token_FILTER_on_length(
														String  baseFolder,
														String  file1,
														int 	token_1_for_applying_length_FILTER,
														int 	token_2_for_applying_length_FILTER,
														int     filter_length,
														String  OUTPUT_file3, //out
														boolean isSOPprint
													   ){
 
		FileWriter writer_PASSED=null;
		FileWriter writer_FAILED=null;
		FileWriter writerDebug=null;
		try {
			writer_PASSED=new FileWriter(new File(OUTPUT_file3+"_PASSED_FILTER.txt"));
			writer_FAILED=new FileWriter(new File(OUTPUT_file3+"_FAILED_FILTER.txt"));
			writerDebug=new FileWriter(new File(OUTPUT_file3+"_DEBUG.txt"));
			
			// map_file1
			TreeMap<Integer,String> map_file1=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								  file1,
																	 							  -1, //startline, 
																	 							  -1, //endline,
																	 							  "f1 ", //debug_label
																	 							  false //isPrintSOP
																							 	  );
			String interested_token_1=""; int hit_pass=0; int hit_fail=0;
			String interested_token_2="";
			//
			for(int seq:map_file1.keySet()){
				String eachLine=map_file1.get(seq);
				boolean is_passed=false;
				//
				String []s = eachLine.split("!!!");
				if(s.length>=token_1_for_applying_length_FILTER){
					//interested token
					interested_token_1=s[token_1_for_applying_length_FILTER-1].replace("&nbsp", "").replace("&quot", "");
					interested_token_2=s[token_2_for_applying_length_FILTER-1].replace("&nbsp", "").replace("&quot", "");
					int len_1=Clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters( interested_token_1, true).length();
					int len_2=Clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters( interested_token_2, true).length();
					
					//check if this length matches
					//NON-ENGLISH/FORUM/
					if(eachLine.toLowerCase().indexOf("!!!!note:")>=0){//NON-ENGLISH/FORUM/
						is_passed=false;
					}
					else if( len_1 >=filter_length ||
							 len_2 >=filter_length
					   ){
						is_passed=true;
					}
					else if( len_2 >= filter_length &&
							eachLine.indexOf("beginINDEXis-1")==-1 //this means we crawled and found PatternForStart news body text,BUT SHORT bodyText
							){
						is_passed=true;
					}
					else{
						is_passed=false; 	
					}
					//DEBUG
					writerDebug.append("linenumbeR: len1, len2 ->"+ seq+" "+len_1+" "+len_2+
											" " +" is_passed:"+is_passed+
											"\n");
					writerDebug.flush();
					
					if(is_passed){
						writer_PASSED.append(eachLine+"\n");
						writer_PASSED.flush();
						hit_pass++;
					}
					else{
						writer_FAILED.append(eachLine+"\n");
						writer_FAILED.flush();
						hit_fail++;
					}
					
				}
			} //
			
			System.out.println("map_file1.size:"+map_file1.size() +" hit_pass:"+hit_pass
								+" hit_fail:"+hit_fail);

		} 
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			// TODO: handle finally clause
			
		}
	}
	

	// main
	public static void main(String[] args) throws Exception{
		String prefix="bfr_17Feb_set1";
		
		String  baseFolder="/Users/lenin/Downloads/#problems/p18/ds1/"+prefix+"/";
		String  file1=baseFolder+prefix+"_OUTPUT_merged_full_bodyText_filter__ENGLISH.txt";
		int 	token_1_for_applying_length_FILTER=2;
		int 	token_2_for_applying_length_FILTER=4;
		String  OUTPUT_file3=baseFolder+prefix+"_OUTPUT_merged_full_bodyText_filter__ENGLISH_LEN_50_FILTER.txt"; //out
		boolean isSOPprint=false;
		int filter_length=600;
		
		//readFile_read_a_token_FILTER_on_length
		readFile_read_a_token_FILTER_on_length(
								   		baseFolder,
								   		file1,
								   		token_1_for_applying_length_FILTER,//can be -1
								   		token_2_for_applying_length_FILTER, //can be -1
								   		filter_length,
								   		OUTPUT_file3, //out
								   		isSOPprint);
		
		 
		
	}

}
