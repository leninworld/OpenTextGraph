package p8;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

public class Clean_normalize_no_tokens_across_diff_newspapers {
	
	//clean_normalize_no_tokens_across_diff_newspapers
	public static void clean_normalize_no_tokens_across_diff_newspapers(
																	String inputFile,
																	String outputFile
																	  ){
		try {
			
			FileWriter writer=new FileWriter(new File(outputFile));
			FileWriter writer_debug=new FileWriter(new File(outputFile+"_debug.txt"));
			 //get interested token number values from file
			TreeMap<Integer,String> map_inFile_eachLine=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
							 															  inputFile, 
																					 	 -1, 
																						 -1,
																						 " clean_normalize_no_tokens_across_diff_newspapers ", //debug_label
																						 false //isPrintSOP
																						 );
			
			//
			for(int seq:map_inFile_eachLine.keySet()){
				String currline=map_inFile_eachLine.get(seq);
				String [] token=currline.split("!!!");
				//
				if(token.length==2){
					writer.append(currline.replace("Staff Reporter", " ")+"\n");
					writer.flush();
				}
				else {
					
					System.out.println(currline);
					writer_debug.append(currline+"\n");
					writer_debug.flush();
					if(token.length>=4){
						String concLine=token[0];
						if(token[2].length()>1 && token[2].indexOf("http")==-1 ){
							concLine=concLine+"!!!"+token[2];
						}

						if(token[3].length()>1 && token[3].indexOf("http")==-1 ){
							concLine=concLine+"!!!"+token[3];
						}
						
						writer.append(concLine.replace("Staff Reporter", " ")+"\n");
						writer.flush();
						
					}
					else if(token.length>=3){
						String concLine=token[0];
						if(token[1].length()>1 && token[1].indexOf("http")==-1 ){
							concLine=concLine+"!!!"+token[1];
						}

						if(token[2].length()>1 && token[2].indexOf("http")==-1 ){
							concLine=concLine+"!!!"+token[2];
						}
						
						
						writer.append(concLine.replace("Staff Reporter", " ")  +"\n");
						
					}
					
					writer.flush();
				}
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	

	//main
	public static void main(String[] args) throws IOException {
		 
		 String baseFolder="/Users/lenin/Downloads/#problems/p8/work-Full Article/samples/merged/";
		
    	 String inputFileName=baseFolder+"thehindu_merged_all.txt";
		 String outputFileName=baseFolder+"thehindu_merged_all_normalized_token.txt";
		 
		 //
		 clean_normalize_no_tokens_across_diff_newspapers(inputFileName, 
				 										  outputFileName);
		 
		 
		 
	}

}
