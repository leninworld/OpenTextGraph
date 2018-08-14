package crawler;


/*
*  AUTHOR: LENIN
 */

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

//  
public class Clean_gbad {
	 
	//clean_GBAD_file
	public static void clean_GBAD_file(
										String baseFolder,
										String file1,
										String OUTPUT_file3
										){
		
	 
		FileWriter writer=null;
		String curr_URL=""; 
		try {
			writer=new FileWriter(new File(OUTPUT_file3));
			 
			// 
			TreeMap<Integer,String> map_file1=
						 ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								  file1,
																	 							  -1, //startline, 
																	 							  -1, //endline,
																	 							  " ", //debug_label
																	 							  false //isPrintSOP
																							 	  );
			
			//
			for(int seq:map_file1.keySet()){
				String eachLine=map_file1.get(seq);
				 
				eachLine=eachLine.replace("o.conf.idx:15:val=", "").replace("o.conf.idx:14:val=", "").replace("o.conf.idx:13:val=", "").replace("o.conf.idx:12:val=", "")
								.replace("o.conf.idx:11:val=", "").replace("o.conf.idx:10:val=", "").replace("o.conf.idx:9:val=", "").replace("o.conf.idx:8:val=", "")
								.replace("o.conf.idx:7:val=", "").replace("o.conf.idx:6:val=", "").replace("o.conf.idx:5:val=", "").replace("o.conf.idx:4:val=", "")
								.replace("o.conf.idx:3:val=", "").replace("o.conf.idx:2:val=", "").replace("o.conf.idx:1:val=", "");
								
				eachLine=eachLine.replace("o.conf.idx:", "");
				
				writer.append(eachLine+"\n");
				writer.flush();
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	// main
	public static void main(String[] args) throws Exception{
		
		//
		String baseFolder="/Users/lenin/Downloads/#Data2/cyber/C17693_C728/";
		String file1_=baseFolder+"crawled/CRAWLED_output.txt";
			   file1_=baseFolder+"C17693_C728_OUTPUT.g";
		String OUTPUT_file3=baseFolder+"C17693_C728_OUTPUT_cleaned.g";
		
		// main
		clean_GBAD_file(
							baseFolder,
							file1_,
							OUTPUT_file3
						);
		
		
	}

}
