package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import crawler.Sort_given_treemap;

import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

public class Convert_docIDWord_freq_2_WorkNFreq {
	
	//convert_docIDWord_freq_2_WorkNFreq
	public static void convert_docIDWord_freq_2_WorkNFreq(
													String baseFolder,
													String inFile,
													String outFile
													){
		  outFile=inFile+"_##Word_Freq.txt";
		  TreeMap<String, Integer> map_word_freq=new TreeMap<String, Integer>();
		try {
			FileWriter writer=new FileWriter(new File(outFile));
			TreeMap<Integer, String> map_lineNo_Line=
				ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
				readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
																(  inFile, 
																   -1, // 	startline, 
																   -1, //  	endline,
																   " debug_label",
																   false // isPrintSOP
																);
			//each line -> docID!!!word!!!frequency
			for(int i:map_lineNo_Line.keySet()){
				
				String currLine=map_lineNo_Line.get(i);
				currLine=currLine.replace("!!!!!!","!!!#!!!")
							    .replace("!!!!!","!!!")
								.replace("!!!!","!!!");
						
				System.out.println("currLine:"+currLine);
				String [] arr_line=currLine.split("!!!");
				
				
				if(!map_word_freq.containsKey(arr_line[1])){
					map_word_freq.put(arr_line[1], Integer.valueOf(arr_line[2]) );
				}
				else{
					int sum=map_word_freq.get(arr_line[1] ) + Integer.valueOf(arr_line[2]) ;
					map_word_freq.put(arr_line[1], sum);
				}
				
				
			} //
			
			//sort it before write
			Map<String, Integer> map3_sorted_asc=
								Sort_given_treemap.sortByValue_SI(map_word_freq );
			
							//sortByValue_ID_method4_ascending(map_word_freq);
			
			
			
			//write
			for(String word:map3_sorted_asc.keySet()){
				writer.append(word+"!!!"+map_word_freq.get(word)+"\n");
				writer.flush();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}

	// MAIN
	public static void main(String args[]) throws IOException {
				String baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/";
				String inFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.dummy.all.txt_TF_IDF.txt_docWordID_freq2.txt";
				String outFile="";
		
				//convert_docIDWord_freq_2_WorkNFreq
				convert_docIDWord_freq_2_WorkNFreq(
														baseFolder,
														inFile,
														outFile
													);
	}
}
