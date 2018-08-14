package p18;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

public class Merge_gTruth_with_orgVerbNounPersonFile {
	
	// merge
	public static void merge_gTruth_with_orgVerbNounPersonFile(String 	FirstFile,
															   int 	   	primarykey_index_firstfile,
															   String  	delimiter_for_firstfile,
															   String  	SecondFile,
															   int 	   	primarykey_index_secondfile,
															   String  	delimiter_for_secondfile,
															   String  	outFile,
															   boolean 	is_HeaderPresentIn_first_file,
															   int 		flag_for_type_of_inputFile_format,
															   int 		index_for_LABEL
															   ){
		try {
			TreeMap<Integer,String> map_firstfile_lineNo_primarykey =new TreeMap();
			TreeMap<Integer,String> map_secondfile_lineNo_primarykey =new TreeMap();
			TreeMap<Integer,String> map_secondfile_lineNo_LABEL =new TreeMap();
			TreeMap<String,String> map_secondfile_URL_LABEL =new TreeMap();
			FileWriter writer=new FileWriter(new File(outFile));
			TreeMap<Integer, String> map_lineNo_Line_firstFile=
				ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
				readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
																(  FirstFile, 
																   -1, // 	startline, 
																   -1, //  	endline,
																   " debug_label",
																   false // isPrintSOP
																);
			
			TreeMap<Integer, String> map_lineNo_Line_secondFile=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
					readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
																	(  SecondFile, 
																	   -1, // 	startline, 
																	   -1, //  	endline,
																	   "2 debug_label",
																	   false // isPrintSOP
																	);
			
			//if exists in second file , then its true positive
			String header=map_lineNo_Line_firstFile.get(1);
			// FIRST FILE
			for(int lineNo:map_lineNo_Line_firstFile.keySet()){
				//
				if(is_HeaderPresentIn_first_file && lineNo==1){
					continue;
				}
				String [] arr_first=map_lineNo_Line_firstFile.get(lineNo).split(delimiter_for_firstfile);
				map_firstfile_lineNo_primarykey.put(lineNo, arr_first[primarykey_index_firstfile-1] );
			}
			// SECOND FILE
			for(int lineNo:map_lineNo_Line_secondFile.keySet()){
				 
				String [] arr_second=map_lineNo_Line_secondFile.get(lineNo).split(delimiter_for_secondfile);
				map_secondfile_lineNo_primarykey.put(lineNo,   arr_second[primarykey_index_secondfile-1 ]);
				//LABEL 
				map_secondfile_lineNo_LABEL.put(lineNo, arr_second[index_for_LABEL-1 ]);
				map_secondfile_URL_LABEL.put(arr_second[primarykey_index_secondfile-1 ], arr_second[index_for_LABEL-1 ]);
			}
			
		
			
			writer.append(header+  "@@@@"+"label"+ "\n");
			int linesWrite_to_outputFile=0;
			//  flag_for_type_of_inputFile_format==1
			if(flag_for_type_of_inputFile_format==1){
					//iterate first file
					for( int lineNo:map_firstfile_lineNo_primarykey.keySet() ){
						//True posit
						if(   map_secondfile_lineNo_primarykey.containsValue(map_firstfile_lineNo_primarykey.get(lineNo)  ) ){
							
							writer.append(map_lineNo_Line_firstFile.get(lineNo) +"@@@@"+"1"+"\n" );
							linesWrite_to_outputFile++;
						}
						else{
							writer.append(map_lineNo_Line_firstFile.get(lineNo) +"@@@@"+"0" +"\n");
							linesWrite_to_outputFile++;
						}
						writer.flush();
					}
			}
			 
			int count_error_in_LABEL=0;
			String debug_errorLABEL_lineNOs="";
			TreeMap<String, String> map_URL_alreadyWROTE2OUTPUT=new TreeMap<String, String>();
			//  flag_for_type_of_inputFile_format==2
			if(flag_for_type_of_inputFile_format==2){
						//iterate first file
						for( int lineNo:map_firstfile_lineNo_primarykey.keySet() ){
							//True posit
							if(   map_secondfile_URL_LABEL.containsKey( map_firstfile_lineNo_primarykey.get(lineNo)  ) 
								&& !map_URL_alreadyWROTE2OUTPUT.containsKey( map_firstfile_lineNo_primarykey.get(lineNo))
									){
							    //
								if( map_secondfile_URL_LABEL.get(map_firstfile_lineNo_primarykey.get(lineNo) ).equals("1") ){
									writer.append(map_lineNo_Line_firstFile.get(lineNo) +"@@@@"+"1"+"\n" );
									linesWrite_to_outputFile++;
								}
								else if( map_secondfile_URL_LABEL.get(map_firstfile_lineNo_primarykey.get(lineNo) ).equals("0") ){
									writer.append(map_lineNo_Line_firstFile.get(lineNo) +"@@@@"+"0" +"\n");
									linesWrite_to_outputFile++;
								}
								else{
									count_error_in_LABEL++;
									
									if(debug_errorLABEL_lineNOs.length()==0){
										debug_errorLABEL_lineNOs=String.valueOf(lineNo);
									}
									else{
										debug_errorLABEL_lineNOs+=","+lineNo;
									}
									
								}
								map_URL_alreadyWROTE2OUTPUT.put( map_firstfile_lineNo_primarykey.get(lineNo) , "");
							}
							writer.flush();
					}
			}
			System.out.println("----------------------------------");
			System.out.println("----------NOTE BELOW for ERRORS------------------------");
			System.out.println(" map_secondfile_lineNo_LABEL.size:"+map_secondfile_lineNo_LABEL.size()+
								" map_secondfile_URL_LABEL:"+map_secondfile_URL_LABEL.size()
								+" map_firstfile_lineNo_primarykey.size:"+map_firstfile_lineNo_primarykey.size()
								+" count_error_in_LABEL:"+count_error_in_LABEL
								+"\n debug_errorLABEL_lineNOs:"+debug_errorLABEL_lineNOs
							);
			
			System.out.println("linesWrite_to_outputFile:"+linesWrite_to_outputFile+"<--counter has (-1) as header is removed in the count, but header added in output file");
			System.out.println("map_lineNo_Line_firstFile.size:"+map_lineNo_Line_firstFile.size()+" map_lineNo_Line_secondFile.size:"+map_lineNo_Line_secondFile.size());
		} catch (Exception e) {
			e.printStackTrace( );
			// TODO: handle exception
		}
		
	}
	
	// MAIN
	public static void main(String args[]) throws IOException {
				String baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/";
				baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/tmp/";
				
				/////FIRST FILE.. NOTE: This file is output from "run_Flag==2" in "wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati"
				String FirstFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb.txt";
					   FirstFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all.txt_added_perso_origz_loc_verb2_done.txt";
					   FirstFile=baseFolder+"O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged.txt_added_perso_origz_loc_verb2.txt";
					   FirstFile=baseFolder+"O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged_onlyAfricanAmerican.txt_added_perso_origz_loc_verb2.txt";
					   
				int primarykey_index_firstfile=1;
					primarykey_index_firstfile=5;
				String delimiter_for_firstfile="!!!";
				/////SECOND FILE				
				String 	SecondFile=baseFolder+"groundTruth_TP.txt";
				
				int primarykey_index_secondfile=1;
				//WHEN Flag_for_type_of_inputFile_format==1 (p8 used this format)
				String delimiter_for_secondfile="!!!";
				///////////////// OR ////////////////////// ANOTHER FORMAT as BELOW..
				//WHEN Flag_for_type_of_inputFile_format==2 <-----   human annotator labeled File (p18 used this format)
				SecondFile="/Users/lenin/Dropbox/#problems/p18/FromMaitrayi/all.txt";
				SecondFile="/Users/lenin/Dropbox/Maitrayi/p18 -/Labeling/1500labeled_marked_DOUBTS/O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL.txt";
				SecondFile="/Users/lenin/Dropbox/Maitrayi/p18 -/Labeling/1500labeled_marked_DOUBTS/O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_onlyAfricanAmerican_removeIrrelevaLABEL.txt";
				
					primarykey_index_secondfile=4;
				int index_for_LABEL=3;   // ONLY Flag_for_type_of_inputFile_format=2 REQUIRES THIS.
				delimiter_for_secondfile="!!!";
				// OUTPUT
				String outFile=FirstFile.replace(".txt", "_")+"_added##GroundTruth.txt";
				// 1==having primaryKEY of only "true positive" ; 2=having primaryKEY of both "true positive" and "true negative"
				int Flag_for_type_of_inputFile_format=2;  /// <------- FLAG
				
				//merge_gTruth_with_orgVerbNounPersonFile
				merge_gTruth_with_orgVerbNounPersonFile(
															FirstFile,
												     	    primarykey_index_firstfile,
												     	    delimiter_for_firstfile,
												     	    SecondFile,
												     	    primarykey_index_secondfile,
												     	    delimiter_for_secondfile,
												     	    outFile,
												     	    true, //is_HeaderPresentIn_first_file
												     	    Flag_for_type_of_inputFile_format,
												     	    index_for_LABEL //// ONLY Flag_for_type_of_inputFile_format=2 REQUIRES THIS.
														);
			 
				
	}

}

