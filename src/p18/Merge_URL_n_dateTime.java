package p18;

import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

//import org.apache.tomcat.jni.File;

import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

public class Merge_URL_n_dateTime {
	
	
	public static void merge_URL_n_dateTime(
							String baseFolder,
							TreeMap<String, String> map_URL_n_DateTime_inputFile1,
							TreeMap<String, String> map_URL_N_docID_inputFile2,
							String					outputFile
							){
		try{
			FileWriter writer=new FileWriter(outputFile);
			
//			TreeMap<Integer,String> map_inFile_seq_eachLine_file1=
//					readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
//																									map_URL_n_DateTime_inputFile1, 
//																								 	 -1, //startline, 
//																									 -1, //endline,
//																									 "f1 ", //debug_label
//																									 false //isPrintSOP
//																									 );
			
			 int count_exists=0;
			//HOW MANY from file 2 exists in file 1?
			for(String file2_url:map_URL_N_docID_inputFile2.keySet()){
				
				if(map_URL_n_DateTime_inputFile1.containsKey(file2_url)){
					count_exists++;
				
					writer.append(file2_url+"!"+map_URL_n_DateTime_inputFile1.get(file2_url)+"!"+map_URL_N_docID_inputFile2.get(file2_url)+"\n");
					writer.flush();
				}
				else{
					System.out.println("NOT EXISTS:"+file2_url);
				}
			}
			
			System.out.println("map_URL_N_docID_inputFile2.siz:"+map_URL_N_docID_inputFile2.size()
								+" count_exists:"+count_exists);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	// generate 
	public static TreeMap<String,String> generate_MAP_of_URL_N_DateTime(
												String  inputFile,
												int  	index_having_datetime,
												int  	index_having_URL
												){
		TreeMap<String,String> map_URL_datetime=new TreeMap<String,String>();
		try{
			
			TreeMap<Integer,String> map_inFile_seq_eachLine_file1=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																									 inputFile, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 "f1 ", //debug_label
																									 false //isPrintSOP
																									 );
			//
			for(int seq:map_inFile_seq_eachLine_file1.keySet()){
				String currLine=map_inFile_seq_eachLine_file1.get(seq);
				
				String [] s=currLine.replace("!!!!!!", "!!!d!!!").split("!!!");
//				System.out.println(s.length
//								+"<-->seq:"+seq+"<->curr line:"+map_inFile_seq_eachLine_file1.get(seq));
				if(s.length <3 ) continue;
				String currURL=s[index_having_URL-1];
				String currDateTime=s[index_having_datetime-1];
				
				//cleaning
				if(currDateTime.indexOf("from")>=0)
					currDateTime=currDateTime.replace("from - ","");
				if(currDateTime.indexOf(",")>=0){
					int index_comma=currDateTime.indexOf(",");
					
					currDateTime=currDateTime.substring(index_comma, currDateTime.length() );
				}
				if(currDateTime.indexOf("+")>=0){
					int index_comma=currDateTime.indexOf("+");
					
					currDateTime=currDateTime.substring(0, index_comma);
				}
				currDateTime=currDateTime.replace(", ", "").replace(" gmt x-mozilla-status: 0041 x-mozilla-status2: ", "")
														   .replace(" gmt x-mozilla-status: ", "");
				
				//
				if(currURL.indexOf("http")>=0)
					map_URL_datetime.put(currURL, currDateTime);
			}
			
			System.out.println("map_URL_datetime:"+map_URL_datetime);
			System.out.println("map_URL_datetime.size:"+map_URL_datetime.size());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return map_URL_datetime;
	}
	

	// MAIN 
	public static void main(String args[]) throws IOException {
		
		String baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/tmp/";
		
		//this file has all the URL and datetime without any filtering on keyword such as muslim, african american etc.
		String inputFile1="/Users/lenin/Dropbox/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/all_issues_id37151552.txt";
		
		//p18 problem - this file has all the URL with filtering on keyword african american and black
		String inputFile2="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/tmp/O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged_onlyAfricanAmerican.txt";

		////////////////// FILE 1 /////////
		
		//inputFile1: get URL n DateTime (global file without any filtering on keyword such as muslim, african american etc.)
		TreeMap<String,String> map_URL_n_DateTime_inputFile1=
							generate_MAP_of_URL_N_DateTime(
															inputFile1,
															3, //index_having_datetime
															2 // index_having_url 
															);
		
		////////////////// FILE 2 /////////
		TreeMap<Integer,String> map_inFile_seq_eachLine_inputFile2=
				ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								 inputFile2,
																							 	 -1, //startline, 
																								 -1, //endline,
																								 "f1 ", //debug_label
																								 false //isPrintSOP
																								 );
		TreeMap<String,String> map_URL_N_docID_inputFile2 =new TreeMap<String, String>(); 
		for(int seq:map_inFile_seq_eachLine_inputFile2.keySet()){
			if(seq==1) continue; //hdear
			String currLine=map_inFile_seq_eachLine_inputFile2.get(seq);
			String []s=currLine.split("!!!");
			String currURL=s[4];
			map_URL_N_docID_inputFile2.put(currURL, s[0]); //url , docID
		}
		
		System.out.println("map_URL_N_docID_inputFile2:"+map_URL_N_docID_inputFile2.size());
		
		// ONLY for the p18 problem 
		merge_URL_n_dateTime(
								baseFolder,
								map_URL_n_DateTime_inputFile1,
								map_URL_N_docID_inputFile2,
								baseFolder+"output_URL_dateTime_docID.txt"
								);
		
	}
	
}
