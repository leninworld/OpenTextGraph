package convert;

import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

import java.io.IOException;
import java.util.TreeMap;

public class Convert_GBAD_Neo3J_Cypher {

	// convert 
	public static void Convert_GBAD_Neo3J_Cypher(
												String baseFolder,
												String input_GBAD_file,
												String input_Neo4j_Cypher_file
												) {
		try {
			
			//load to map
			TreeMap<Integer,String> map_eachLine=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																							  input_GBAD_file
																							, -1
																							, -1
																							, "" //debug_label
																							, false// isPrintSOP
																							);
			//
			for(int seq:map_eachLine.keySet()){
				String eachLine=map_eachLine.get(seq);
				String [] arr_=eachLine.split(" ");
				
				if(eachLine.indexOf("XP #")>=0){
					
				}
				else if(eachLine.indexOf("v ")>=0){
					
				}
				else if(eachLine.indexOf("d ")>=0){
					
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	// main
	public static void main(String[] args) throws IOException {
			String baseFolder="";
			String input_GBAD_file="";
			String input_Neo4j_Cypher_file="";
			
			
			//
			Convert_GBAD_Neo3J_Cypher(
										baseFolder,
										input_GBAD_file,
										input_Neo4j_Cypher_file
										);
			
		
	}
}
