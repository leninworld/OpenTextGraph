package p8;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.TreeMap;

import crawler.DocumentParser_AND_Cosine;
import crawler.Calc_tf_idf;
import crawler.Convert_DocID_WordID_Freq_To_another_format;
import crawler.Cosine_similarity;
import crawler.Find_2_Map_word_not_exist_in_another;
import crawler.Generate_Random_Number_Range;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import crawler.Sort_given_treemap;

// this takes input 
public class Compare_GTruth_truePositive_AND_givenFile {
	
	// input is like 
	public static void merge_2_files_vertically(String file1_groundTruth_TP,
												String file_2,
												String outputFile
												){
		try{
			FileWriter writer=new FileWriter(new File(outputFile+"_GT_truePositive.txt"));
			FileWriter writer2=new FileWriter(new File(outputFile)+"_NOT_in_GT_truePositive.txt");

			TreeMap<Integer, String> map_inFile_EachLine_of_document_f1=new TreeMap<Integer, String>();
			TreeMap<Integer, String> map_inFile_EachLine_of_document_f2=new TreeMap<Integer, String>();
			
			// loading
			map_inFile_EachLine_of_document_f1=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 							.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line( 
					 																									 file1_groundTruth_TP, 
					 																									 -1, //start_line, //startline, 
																														 -1, //end_line, //endline,
																														 " map_inFile_EachLine_of_document ", //debug_label
																														 false //isPrintSOP
																														 );
			
			map_inFile_EachLine_of_document_f2=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
													.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line( 
																							 file_2, 
																							 -1, //start_line, //startline, 
																							 -1, //end_line, //endline,
																							 " map_inFile_EachLine_of_document ", //debug_label
																							 false //isPrintSOP
																							 );
			//
			for(int seq1:map_inFile_EachLine_of_document_f1.keySet()){
				
				if(map_inFile_EachLine_of_document_f1.get(seq1).indexOf("ground_truth") >=0){
					writer.append(map_inFile_EachLine_of_document_f2.get(seq1)+"\n" );
					writer.flush();
				}
				else{
					writer2.append(map_inFile_EachLine_of_document_f2.get(seq1)+"\n" );
					writer2.flush();
					
				}
			}
			
			writer.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	// main
	public static void main(String[] args) throws Exception {
		
		String baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/";
		
		String file1_groundTruth_TP="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/"
				+ "_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt";
				
		String file2="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/"+
				"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb.txt";
 
		String outFile=baseFolder+
				"_txt_added_perso_origz_loc";
		
		//
		merge_2_files_vertically(file1_groundTruth_TP, 
								file2, 
								outFile
								);
 
		
			
	}
	
}
