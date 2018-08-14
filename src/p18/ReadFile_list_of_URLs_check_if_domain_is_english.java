package p18;

import java.io.File;
import p18.ReadFile_read_a_token_FILTER_on_length;
import java.io.FileWriter;
import java.util.TreeMap;

import crawler.Find_domain_name_from_Given_URL;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

//readFile_list_of_URLs_check_if_domain_is_english 
public class ReadFile_list_of_URLs_check_if_domain_is_english {

	//readFile_list_of_URLs_check_if_domain_is_english
	public static void readFile_list_of_URLs_check_if_domain_is_english(
																String baseFolder,
																String file1_HAVING_URL,
																int    token_having_URL_for_file1,
																String file2_HAVING_domainName_N_isEnglishNOTE,
																String OUTPUT_file3
																){
		 
		FileWriter writer=null;
		String curr_url="";
		TreeMap<String, String> map_domainName_isEnglishNOTE=new TreeMap<String, String>();
		int domainName_Found=0;int domainName_NOT_Found=0;
		int count_ENGLISH_URL_from_file1=0;int count_NON_ENGLISH_URL_from_file1=0;
		try {
			writer=new FileWriter(new File(OUTPUT_file3));
			// 
			TreeMap<Integer,String> map_file1=
						 ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								   file1_HAVING_URL,
																	 							  -1, //startline, 
																	 							  -1, //endline,
																	 							  "f1 ", //debug_label
																	 							  false //isPrintSOP
																							 	  );
			
			TreeMap<Integer,String> map_file2=
					 ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																							   file2_HAVING_domainName_N_isEnglishNOTE,
																 							  -1, //startline, 
																 							  -1, //endline,
																 							  "f1 ", //debug_label
																 							  false //isPrintSOP
																						 	  );
			//
			for(int seq:map_file2.keySet()){
				String [] s=map_file2.get(seq).split("!!!");
				System.out.println("s.len:"+s.length+" "+map_file2.get(seq) );
				if(s.length>=2){
					map_domainName_isEnglishNOTE.put(s[0],s[1] );
				}
			}
			
			//
			for(int seq:map_file1.keySet()){
				String []s=map_file1.get(seq).split("!!!");
				
				if(s.length<token_having_URL_for_file1) {continue;}
				
				curr_url=s[token_having_URL_for_file1-1];
				
				String curr_domainName=Find_domain_name_from_Given_URL.find_domain_name_from_Given_URL(curr_url, false);
				//  domainName found
				if(map_domainName_isEnglishNOTE.containsKey(curr_domainName )){
					domainName_Found++;
					String startPattern_4_news=map_domainName_isEnglishNOTE.get(curr_domainName);
					//found "note:" means NOT ENGLISH/ FORUM
					if(startPattern_4_news.toLowerCase().indexOf("note:")>=0){
						count_NON_ENGLISH_URL_from_file1++;
					}
					else{
						count_ENGLISH_URL_from_file1++;
					}
				}//domainName NOT found
				else{
					
					domainName_NOT_Found++;
				}
				
				
//				writer.append(curr_primarykey+"!!!"+curr_crawledhtml+"\n");
//				writer.flush();
				
			}
		
			System.out.println("count_ENGLISH_URL_from_file1:"+count_ENGLISH_URL_from_file1+" count_NON_ENGLISH_URL_from_file1:"+count_NON_ENGLISH_URL_from_file1);
			System.out.println("file1_HAVING_URL.size:"+map_file1.size()+" map_domainName_isEnglishNOTE.size:"+map_domainName_isEnglishNOTE.size());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	// main
	public static void main(String[] args) throws Exception{
		
		//
		String baseFolder="/Users/lenin/Downloads/#problems/p18/ds1/before_17Feb_set2/before_1feb_URL_set2.txt";
		String file1_HAVING_URL="/Users/lenin/Downloads/#problems/p18/ds1/merge/all.txt";

		
		//This file has 2 tokens <domainName!!!NOTE:NOT english/FORUM>
		String file2_having_domainName_isEnglishNOTE="/Users/lenin/Downloads/#problems/p18/ds1/global_domainNAMES_patternFORNEWSBODYSTART.txt";
		int token_having_URL_for_file1=1;
		String OUTPUT_file3=baseFolder+"englishONLY.txt";
		
		//STEP 1 : 
		//readFile_list_of_URLs_check_if_domain_is_english
		readFile_list_of_URLs_check_if_domain_is_english(
													  baseFolder,
													  file1_HAVING_URL,
													  token_having_URL_for_file1,
													  file2_having_domainName_isEnglishNOTE,
													  OUTPUT_file3
													);
		
		//
		String  file1=OUTPUT_file3;
		int 	token_1_for_applying_length_FILTER=2;
		int 	token_2_for_applying_length_FILTER=4;
		String OUTPUT_file3_new=OUTPUT_file3+"_LEN_50_FILTER.txt"; //out
		boolean isSOPprint=false;
		int filter_length=380;
		
		//STEP 2 : 
		//readFile_read_a_token_FILTER_on_length > -  FILTER ON LENGTH
		ReadFile_read_a_token_FILTER_on_length.
		readFile_read_a_token_FILTER_on_length(
										   		baseFolder,
										   		file1,
										   		token_1_for_applying_length_FILTER,//can be -1
										   		token_2_for_applying_length_FILTER, //can be -1
										   		filter_length,
										   		OUTPUT_file3_new, //out
										   		isSOPprint);
		
	}

}