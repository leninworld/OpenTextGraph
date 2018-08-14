package p18;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

public class Clean_remove_irrelevant_news_withlabel_minusONE {
	
	
	// if label is -1, dont write that to the output file
	// clean
	public static void clean_remove_irrelevant_news_withlabel_minusONE(String 	inputFile,
																	   String 	outputFile,
																	   int 		index_having_label
																		){
		try {
			FileWriter writer=new FileWriter(new File(outputFile));
			  TreeMap<Integer, String>  map_seq_eachLine=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										inputFile, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  String header="";
			  header=map_seq_eachLine.get(1);
			  writer.append(header+"\n"); int cnt=0;
			  //
			  for(int seq:map_seq_eachLine.keySet()){
				  
				  if(seq==1){
					  continue;
				  }
				  System.out.println("seq:"+seq);
				  cnt++;
				  String eachLine=map_seq_eachLine.get(seq);
				  String []s=eachLine.split("!!!");
				  System.out.println("(s[index_having_label-1]:"+s[index_having_label-1]);
				  if(Integer.valueOf(s[index_having_label-1])==-1 ||
					 Integer.valueOf(s[index_having_label-1]) < 0  // negative
					 || seq >1500
						  ){
					  continue;
				  }
				  else{
					  writer.append(eachLine+"\n");
				  }
				  writer.flush();
			  }
			  System.out.println("\ncnt="+cnt);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	

	// main
	public static void main(String[] args) throws IOException {

		//
		String 	baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/";
		// input
		String 	inputFile=baseFolder+"O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt";
		// output
		String 	outputFile=baseFolder+"O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL.txt";
		int 	index_having_label=3;
		
		//  clean_remove_irrelevant_news_withlabel_minusONE
		clean_remove_irrelevant_news_withlabel_minusONE(
															inputFile,
															outputFile,
															index_having_label
														);
		
	}
	
	

}
