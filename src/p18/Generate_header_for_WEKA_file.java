package p18;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

import crawler.Find_domain_name_from_Given_URL;
import crawler.Load_domainname_pattern4crawledHTML;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

//
public class Generate_header_for_WEKA_file {
	
	// merge two files
	public static void merge_two_files(
														String  baseFolder,
														String  input_file1,
														String  input_file2,
														String  outputFile
													   ){
	
		FileWriter writer=null;
		
		FileWriter writerDebug=null;
		try {
			writer =new FileWriter(new File(baseFolder+"WEKA_input.txt"));
			writer =new FileWriter(new File(outputFile));
			
			writerDebug =new FileWriter(new File(outputFile+"debug_WEKA_input_NOtokens_in_EACHLINE.txt"));
			  
			// map_file1
			TreeMap<Integer,String> map_inFile1_eachLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(input_file1, 
																							 	 -1, //startline, 
																								 -1, //endline,
																								 " ", //debug_label
																								 false //isPrintSOP
																								 );
			
			
			// map_file2
			TreeMap<Integer,String> map_inFile2_eachLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(input_file2, 
																							 	 -1, //startline, 
																								 -1, //endline,
																								 " ", //debug_label
																								 false //isPrintSOP
																								 );
			 
			String one_line_sample_2_generateHEADER=map_inFile1_eachLine.get(1);
			
			String[] arr_features=one_line_sample_2_generateHEADER.split(",");
			int c=1; String header="";
			//
			while(c<=arr_features.length){
				
				if(header.length()==0)
					header=String.valueOf(c);
				else
					header=header+","+String.valueOf(c);
				
				c++;
			}
			//HEADER
			writer.append(header+"\n");
			writer.flush();
			
			//
			for(int seq:map_inFile1_eachLine.keySet()){
				String eachLine=map_inFile1_eachLine.get(seq);
				writer.append(eachLine+"\n");
				writer.flush();
				
				//DEBUG
				arr_features=eachLine.split(",");
				writerDebug.append(arr_features.length+"\n");
				writerDebug.flush();
			} //
			
			for(int seq:map_inFile2_eachLine.keySet()){
				String eachLine=map_inFile2_eachLine.get(seq);
				writer.append(eachLine+"\n");
				writer.flush();
				
				//DEBUG
				arr_features=eachLine.split(",");
				writerDebug.append(arr_features.length+"\n");
				writerDebug.flush();
			} //
			
			System.out.println("map_file1.size:"+map_inFile1_eachLine.size() +" map_inFile2_eachLine:"+map_inFile2_eachLine.size() );
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
		String  baseFolder="/Users/lenin/Downloads/#problems/p18/ds1/17feb_set1/";
		
		String  file1=baseFolder+"bfr_17feb_set2_OUTPUT_merged_full_bodyText.txt";
		String  file2=baseFolder+"bfr_17feb_set2_OUTPUT_merged_full_bodyText.txt";
	 
		
		//merge_two_files
		merge_two_files(
						baseFolder,
						file1,
						file2,
						""
						);
		 
		
	}

}
