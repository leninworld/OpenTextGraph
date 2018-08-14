package p8;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.File;
import java.util.TreeMap;

import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import crawler.ReadFile_load_filter_out_lines_on_a_given_keyword_then_sample_N_lines_write2AnotherFile;
import crawler.ReadFile_verify_and_Correct_No_Of_Tokens_In_EachLine;
import crawler.ReadFiles_in_a_Folder_mergeFiles_Horizontal;

//prepData_sampleTN_mergeWith_GroundTruthTP
// this class file prepares data for p8 problem.
// (1) It samples N_sample from each of given set of keywords (crime, traffick,shark etc.) and validates the number of tokens in it. 
//		Finally merges output from each of the keyword file into one.
// (2) Merge the output from (1) into ground truth.
public class PrepData_sampleTN_mergeWith_GroundTruthTP {
	
	// main
	public static void main(String[] args) {
		long t0 = System.nanoTime();
		String  baseFolder="/Users/lenin/Downloads/#problems/p8/Full Article/samples/";
		String	inputFolderName=baseFolder;
		// *** NOTE: all files in this folder will be deleted.
		String  tempFolderName="sample.on.keywords_ds2/"; // "tmp" or "attempt.1" etc.
		// 
		if(!new File(baseFolder+tempFolderName).exists()) {
			new File(baseFolder+tempFolderName).mkdir();
		} else { 
			String []files_all=new File(baseFolder+tempFolderName).list(); int cnt100=0;
			while(cnt100<files_all.length){
				new File(baseFolder+tempFolderName+files_all[cnt100]).delete();
				System.out.println("deleted.."+files_all[cnt100]); cnt100++;
			}
		} 
		String  outputFolderName=baseFolder+tempFolderName;
		String  debugFile=baseFolder+tempFolderName+"debug.txt";
		String  keyword_CSV="collapse,ebola,fire,juvenile,kidnap,migrant,mining,road,senior,slavery,swine,traffic";
				//keyword_CSV="collapse";
		// number of samples on each keyword such as collapse, kidnap, migrant etc.
		int 	N_samples=10000;
		boolean is_append_primary_FileName_at_end=true;
		
		// ground truth 
		String ground_truth_merged_file="/Users/lenin/Downloads/#problems/p8/Full Article/ground truth/merging/ground_truth_merged_all_token1_3_normalized.txt";
		
		TreeMap<String, Integer> map_linePattern_MINallowedSamples=new TreeMap<String, Integer>();
		map_linePattern_MINallowedSamples.put("thehindu" , 3000);
		map_linePattern_MINallowedSamples.put("timesofindia" , 3000);
		map_linePattern_MINallowedSamples.put("ndtv" ,3000);
		
		TreeMap<String, Integer> map_linePattern_MAXallowedSamples=new TreeMap<String, Integer>();
		map_linePattern_MAXallowedSamples.put("thehindu" , 3001);
		map_linePattern_MAXallowedSamples.put("timesofindia" , 3001);
		map_linePattern_MAXallowedSamples.put("ndtv" , 3001);
		//1-  ; 2- Do sample 
		int Flag=2;
		
		int input_cnt_max_reach_exit=1000; //control this value
		boolean is_strict_on_min_range=true;
		
		String [] file=new String[2];
		file[0]=outputFolderName+"_merged_all_tokens_normalized_matched.txt";
		file[1]=ground_truth_merged_file+"_merged_all_tokens_normalized_matched.txt";

		//readFile_in_Folder_load_filter_out_lines_on_a_given_keyword_then_sample_N_lines_write2AnotherFile
		
		//(1) all files inside inputFolderName will be merged to "merged_all.txt"
//		//(2) sample on each of uniq linePattern from "map_linePattern_MINallowedSamples"
		ReadFile_load_filter_out_lines_on_a_given_keyword_then_sample_N_lines_write2AnotherFile.
		readFile_in_Folder_load_filter_out_lines_on_a_given_keyword_then_sample_N_lines_write2AnotherFile
		 																						(
																								baseFolder,
																								inputFolderName,
																								outputFolderName,
																								keyword_CSV,
																								N_samples,
																								map_linePattern_MINallowedSamples,
																								map_linePattern_MAXallowedSamples,
																								debugFile,
																								Flag,
																								input_cnt_max_reach_exit,
																								is_strict_on_min_range
																								);
		
		// Iterate only sampled (N lines) files from "outputFolderName" of above method 
		inputFolderName=outputFolderName;
		String  [] files=new File( outputFolderName).list();
		int total_files=files.length; int cnt=0;
		
		System.out.println("Total_files:"+total_files);
		
		//delete this merge_all.txxt
		if(new File(outputFolderName+"merged_all.txt").exists())
			new File(outputFolderName+"merged_all.txt").delete();
		
		// merge all the sampled (N_samples) files (on each keyword) into one.
		ReadFiles_in_a_Folder_mergeFiles_Horizontal.readFiles_in_a_Folder_mergeFiles_Horizontal(
																								outputFolderName, 
																								outputFolderName+"merged_all.txt",
																								N_samples+".txt",//only_inputFiles_match_on_pattern //take only sampled output file
																								false, //is_append_primary_FileName_at_end,
																								null, // String [] inputFiles_input_
																								false,
																								"!!!"// delimiter
																							   );
		
		//remove duplicate from outputFolderName+"_merged_all_tokens_normalized_matched.txt"
//		readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//		 .readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
//				 											 outputFolderName+"merged_all.txt", 
//														 	 -1, //startline, 
//															 -1, //endline,
//															 outputFolderName+"merged_all_nodup.txt", //outfile
//															 false, //is_Append_outFile
//															 true, //is_Write_To_OutputFile
//															 "no duplicate", //debug_label
//															 1, //  token_to_be_used_as_primarykey
//															 false //isPrintSOP
//															 ); 
		
		
		//normalize the tokens (some have 2 , some have 3 or 4 . if >=3 pick 1&3 token.
		p8.Clean_normalize_no_tokens_across_diff_newspapers.clean_normalize_no_tokens_across_diff_newspapers(
																									outputFolderName+"merged_all.txt",
																								    outputFolderName+"merged_all_tokens_normalized.txt"
																									);
		
		int no_mismatch_in_normalized_sampleFile=0, no_mismatch_in_normalized_groundTruth=0;
		
		// input normalized file from output of "clean_normalize_no_tokens_across_diff_newspapers"
		no_mismatch_in_normalized_sampleFile=
		ReadFile_verify_and_Correct_No_Of_Tokens_In_EachLine.readFile_only_verify_No_Of_Tokens_In_EachLine(2, //noTokensNeeded, 
																										  "!!!", //delimiter,
																										  outputFolderName+"merged_all_tokens_normalized.txt",
																										  outputFolderName+"_merged_all_tokens_normalized_not_matched.txt",
																										  outputFolderName+"_merged_all_tokens_normalized_matched.txt", 
																										  debugFile+"_merged_all_tokens_normalized_debug.txt",
																										  true,
																										  true,
																										  false //isSOPprint
																										  );



		//remove duplicate from outputFolderName+"_merged_all_tokens_normalized_matched.txt"
		ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
		 .readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
									 											 outputFolderName+"_merged_all_tokens_normalized_matched.txt", 
																			 	 -1, //startline, 
																				 -1, //endline,
																				 outputFolderName+"_merged_all_tokens_normalized_matched_nodup.txt", //outfile
																				 false, //is_Append_outFile
																				 true, //is_Write_To_OutputFile
																				 "no duplicate", //debug_label
																				 1, //  token_to_be_used_as_primarykey
																				 false //isPrintSOP
																				 ); 
		
		//merge "merged_all_tokens_normalized.txt" and ground truth file

		//variable "ground_truth_merged_file" set 
		no_mismatch_in_normalized_groundTruth=
		ReadFile_verify_and_Correct_No_Of_Tokens_In_EachLine.readFile_only_verify_No_Of_Tokens_In_EachLine(2, //noTokensNeeded, 
																										  "!!!", //delimiter,
																										  ground_truth_merged_file,
																										  ground_truth_merged_file+"_merged_all_tokens_normalized_not_matched.txt", 
																										  ground_truth_merged_file+"_merged_all_tokens_normalized_matched.txt", 
																										  debugFile+"verify_no_tokens_debug.txt",
																										  true,
																										  true,
																										  false //isSOPprint
																										  );
		// take only verified and matched files and merge to one.
		// array "file" was set
		// merge (merged) ground truth file and (merged) sampled file
		ReadFiles_in_a_Folder_mergeFiles_Horizontal.readFiles_in_a_Folder_mergeFiles_Horizontal(
																					"",  // inputFolderName_input_1
																					outputFolderName+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP.txt",
																					"",//only_inputFiles_match_on_pattern
																					is_append_primary_FileName_at_end,
																					file, //inputFiles_input_2
																					false, //isSOPprint
																					"!!!"// delimiter
																					);
				
				//	remove duplicates (using first column as primary key
				//	TreeMap<String, String> mapout=
				 ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
				 .readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
																					 outputFolderName+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP.txt", 
																				 	 -1, //startline, 
																					 -1, //endline,
																					 outputFolderName+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup.txt", //outfile
																					 false, //is_Append_outFile
																					 true, //is_Write_To_OutputFile
																					 "timesOfIndia", //debug_label
																					 1, //  token_to_be_used_as_primarykey
																					 false //isPrintSOP
																					 );
		
		System.out.println("----------------------------------------------------------------");
		System.out.println("!!! Caution: there might be duplicates (same url) news articles in file "
							+outputFolderName+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP.txt. \nThis is because one for ground truth and"
							+ " other from sampled one.  ");
		System.out.println("\n no_mismatch_in_normalized_sampleFile:"+no_mismatch_in_normalized_sampleFile
							+"\n no_mismatch_in_normalized_groundTruth:"+no_mismatch_in_normalized_groundTruth
							+"\n ground_truth_merged_file:"+ground_truth_merged_file
							);
		
		System.out.println("\ndebugFiles-->\n (1) "+debugFile+"verify_no_tokens_debug.txt"
						+"\n (2) "+debugFile+"SAMPLESverify_no_tokens_merged_all_tokens_normalized_debug.txt"
						+"\n (3) "+debugFile
							);
		
		System.out.println("\n merged below 2 files:\n"+		file[0]+"\n"+		file[1]);
		
		System.out.println("Time Taken (FINAL ENDED):"
				+ NANOSECONDS.toSeconds(System.nanoTime() - t0)
				+ " seconds; "
				+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
				+ " minutes");
 
		
	}
	

}
