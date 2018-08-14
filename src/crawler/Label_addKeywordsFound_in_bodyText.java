package crawler;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Label_addKeywordsFound_in_bodyText {
	
	
		//label_addKeywordsFound_in_bodyText
		public static void label_addKeywordsFound_in_bodyText(
															  String baseFolder,
															  String inputFile_1,
															  String outputFile,
															  String delimiter_4_outputFile,
															  String HEADER_for_delimiter_4_outputFile,
															  String file2_having_list_of_keywords_2Search_in_inputFile_1,
															  int index_4_partial_bodyText,
															  int index_4_full_bodyText
															  ){
			try {
				
				FileWriter writer = new FileWriter(new File(outputFile));
				
				TreeMap<Integer,String> map_inputFile_1=
						  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
								 							 inputFile_1, 
														 	 -1, //startline, 
															 -1, //endline,
															 " reloading..", //debug_label
															 false //isPrintSOP
															 );
				
				TreeMap<Integer,String> map_file2_eachLine_a_keyword=
						  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
								 							 file2_having_list_of_keywords_2Search_in_inputFile_1, 
														 	 -1, //startline, 
															 -1, //endline,
															 " reloading..", //debug_label
															 false //isPrintSOP
															 );
				
				//REGEX 
//				 	String a = "This island is beautiful.";
//				    Pattern p = Pattern.compile("\bis\b");
//				    Matcher m = p.matcher(a);
//				    while(m.find()){
//				        System.out.println(a.substring(m.start(), m.end()));
//				    }


				writer.append(HEADER_for_delimiter_4_outputFile+"\n");
				writer.flush();
				
				// each line of file 1
				for(int seq_1:map_inputFile_1.keySet()){
					String file1_eachLine=map_inputFile_1.get(seq_1);
					String eachLine_file1_foundKEYWORDS="";
					//debug
//					if(seq_1!=41) continue;
//					if(seq_1==50) break;
					// each line of file 2
					for(int seq_2:map_file2_eachLine_a_keyword.keySet()){
						String file2_eachLine=map_file2_eachLine_a_keyword.get(seq_2);
						System.out.println("searching keyword:"+file2_eachLine.toLowerCase());
						//
						if(file1_eachLine.toLowerCase().indexOf(file2_eachLine.toLowerCase())>=0 ){
							//
							if(eachLine_file1_foundKEYWORDS.length()==0){
								eachLine_file1_foundKEYWORDS=file2_eachLine.toLowerCase();
							}
							else{
								eachLine_file1_foundKEYWORDS=eachLine_file1_foundKEYWORDS+","
															 +file2_eachLine.toLowerCase();
							}
						}
					} //for(int seq_2:map_file2_eachLine_a_keyword.keySet()){
					
					//System.out.println("lineno:"+seq_1+" keyword:"+eachLine_file1_foundKEYWORDS);
					
					
					String []s =file1_eachLine.split(delimiter_4_outputFile);
					System.out.println( "lineno:"+seq_1 +" s.len:"+s.length);// +" :"+file1_eachLine);
					//get partial bodyText
					String partial_bodyText=s[index_4_partial_bodyText-1];
					//get Full bodyText
					String full_bodyText=s[index_4_full_bodyText-1];
					
					//reconstruct line
					String new_construct_line="";
					//
					int cnt2=0;
					//
					while(cnt2 <s.length){
						
						if(new_construct_line.length()==0){
							//
							if( (cnt2==index_4_partial_bodyText-1)){
								s[cnt2]="^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^PART^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
										 +partial_bodyText;
							}
							else if((cnt2==index_4_full_bodyText-1) ){
								s[cnt2]="----------------------------------------------------FUL------------------------------------------------------------"
										+full_bodyText;
							}
							// 
							new_construct_line=s[cnt2];
						}
						else{
							//
							if( (cnt2==index_4_partial_bodyText-1)){
								s[cnt2]="^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^PART^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
										+partial_bodyText;
							}
							else if((cnt2==index_4_full_bodyText-1) ){
								s[cnt2]="-----------------------------------------------------FUL-----------------------------------------------------------"
										+full_bodyText;
							}
							
							new_construct_line=new_construct_line+delimiter_4_outputFile+s[cnt2];
						}
						cnt2++;
					}
					
					//writing
					writer.append(eachLine_file1_foundKEYWORDS +delimiter_4_outputFile
								 +"0"+delimiter_4_outputFile
								 +"comment"
								 +delimiter_4_outputFile
								 +new_construct_line //new_construct_line
								 +"\n"
								 );
					writer.flush();
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	
		// main
		public static void main(String[] args) {

			  String baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/";
			  String inputFile_1=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt";
			  String outputFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER_addKEYWORDS.txt";
			  String delimiter_4_outputFile="!!!";
			  String file2_having_list_of_keywords_2Search_in_inputFile_1=baseFolder+"keywords_for_label.txt";
			  int index_4_partial_bodyText=2;
			  int index_4_full_bodyText=4;
			  
			  // 
			  String HEADER_for_delimiter_4_outputFile="keywords!!!label!!!comment!!!URL!!!bodyText_PARTIAL!!!subject!!!bodyText_FULL!!!source!!!";
			  
			  //label_addKeywordsFound_in_bodyText
			  label_addKeywordsFound_in_bodyText(
												  baseFolder,
												  inputFile_1,
												  outputFile,
												  delimiter_4_outputFile,
												  HEADER_for_delimiter_4_outputFile,
												  file2_having_list_of_keywords_2Search_in_inputFile_1,
												  index_4_partial_bodyText,
												  index_4_full_bodyText
												  );			  
			  
			
		}
		
}
