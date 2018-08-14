package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ReadFile_sort_by_a_given_token_in_eachLine_write2OutputFile {

	// readFile_sort_by_a_given_token_in_eachLine_write2OutputFile
	public static TreeMap<Integer,String> readFile_sort_by_a_given_token_in_eachLine_write2OutputFile(
																					String baseFolder,
																					String inputFile,
																					String outputFile,
																					String interested_token_to_be_used_for_sorting_CSV, //this together has to be an integer
																					boolean is_write_to_outputFile,
																					boolean is_header_present
																					){
		TreeMap<Integer,String> mapOut=new TreeMap<Integer, String>();
		TreeMap<Integer,String> map_inFile=new TreeMap<Integer, String>();
		FileWriter writer=null;
		try{
			if(is_write_to_outputFile)
				writer = new FileWriter(new File(outputFile));
			
			String[] token_interested_for_sorting=interested_token_to_be_used_for_sorting_CSV.split(",");
			
			map_inFile=			ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
								readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										inputFile, 
																										 -1, // 	startline, 
																										 -1, // 	endline,
																										 "debug_label",
																										 false //isPrintSOP
																										);
			
			
			// 
			Map<String, Integer> map_eachLine_yyyymmdd=new TreeMap<String, Integer>();
			String primaryKey_to_sort="";
			//
			for(int seq:map_inFile.keySet()){
				if(is_header_present==true && seq==1) continue;
				
				primaryKey_to_sort="";
				System.out.println("lineNo->"+seq);
				String currLine=map_inFile.get(seq);
				String [] s_currLine=currLine.split("!!!");
				int c=0;
				// 
				if(c<token_interested_for_sorting.length){
					
					if(primaryKey_to_sort.length()==0)
						primaryKey_to_sort=s_currLine[Integer.valueOf(token_interested_for_sorting[c]) -1  ];
					else
						primaryKey_to_sort=primaryKey_to_sort+s_currLine[ Integer.valueOf(token_interested_for_sorting[c]) - 1  ];
					
					c++;
				}
				map_eachLine_yyyymmdd.put(currLine, Integer.valueOf(primaryKey_to_sort) );
				
			}
			
			Map sorted_eachLine_yyyymmdd=Sort_given_treemap.sortByValue_SI_method4_ascending(map_eachLine_yyyymmdd);
			
			// write sorted output
			for(Object s:sorted_eachLine_yyyymmdd.keySet() ){
				writer.append(s+"\n");
				writer.flush();
			}
			
			System.out.println("map_eachLine_yyyymmdd.size:"+map_eachLine_yyyymmdd.size());
			System.out.println("sorted_eachLine_yyyymmdd.size:"+sorted_eachLine_yyyymmdd.size());
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	
	
	  // main
	  public static void main(String[] args) throws IOException {
		  
		  String baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/";
		  String inputFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_URLs_NOT_exists_in_sample_ds1_AND_ground_truth_DATETIME_exists_AddLineNo.txt";
		  String outputFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_URLs_NOT_exists_in_sample_ds1_AND_ground_truth_DATETIME_exists_AddLineNo_CHRONO.txt";
		  
		  String interested_token_to_be_used_for_sorting_CSV="5,6,7";
		  
		  boolean is_write_to_outputFile=true;

		  // 
		  readFile_sort_by_a_given_token_in_eachLine_write2OutputFile(
																	  baseFolder,
																	  inputFile,
																	  outputFile,
																	  interested_token_to_be_used_for_sorting_CSV, //this together has to be an integer
																	  is_write_to_outputFile,
																	  true //is_header_present
																	  );
					
		  
	  }
			
	
}
