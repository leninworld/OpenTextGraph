package p19;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.TreeMap;

import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

public class split_inputFile_2_multipleEachFile1_N_minutes {
	
	//http://csr.lanl.gov/data/cyber1/ - proc.txt and flows.txt
	public static void split_inputFile_2_multipleEachFile1_N_minutes(
																String baseFolder,
																String file1, //output of convert_epoch_datetime
																String delimiter_for_file1,
																//String output_File,
																int	   token_having_ddmmyyhhmmss_datetime,
																int    number_of_minutes_to_split,
																String flag_for_token_type_4_datetime,
																String flag_hour_R_min_R_sec, //{h,m,s,weekly}
																String alreadyRAN_files,
																boolean isAppend_alreadyRAN_files
																){
		try {
			FileWriter writer_debug=new FileWriter(new File(baseFolder+"debug_.txt"));
			///create out folder
			String baseFolder_out=baseFolder+flag_hour_R_min_R_sec+"/";
			File f=new File(baseFolder_out);
			// create a folder
			if(!f.exists())
				f.mkdir();
			
			String input_primaryFileName= (new File(file1)).getName();
			FileWriter writer_AlreadyRANfiles=new FileWriter(alreadyRAN_files, isAppend_alreadyRAN_files);
			
			TreeMap<Integer,String> map_input_ALREADY=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
								 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
										 												 alreadyRAN_files, 
																					 	 -1, //startline, 
																						 -1, //endline,
																						 " reloading..", //debug_label
																						 false //isPrintSOP
																						 );
			
			//(flows.txt.gz) + added epoch using
			//EPOCH_time,CONVERTED_DATETIME,duration,source computer,source port,destination computer,destination port,protocol,packet count,byte count 
			TreeMap<Integer,String> map_file1=
												ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
						 												 							  file1, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 "f1 ", //debug_label
																									 false //isPrintSOP
																									 );
			
			TreeMap<String,String> map_unique_MM_as_key=new TreeMap<String, String>();
			int cnt_file=0;
			TreeMap<Integer,String> map_currFile_set_of_lines=new TreeMap<Integer,String>();
			System.out.println("size:"+map_file1.size());
			
			TreeMap<String, TreeMap<Integer, String>> map_feature_MapSeqNLine=new TreeMap<String, TreeMap<Integer,String>>();
			String currDateTimetoken="";
			// 
			for(int seq:map_file1.keySet()){
				 
				String eachLine=map_file1.get(seq);
				String [] s=eachLine.split(delimiter_for_file1);
				
				//System.out.println("in file having epoch:"+file1);
				//System.out.println("each:"+eachLine);
				
				// HEADER SKIP
				if(s.length==1 || eachLine.equals("") ||seq==1 ) { 
					System.out.println(" continue...lineno:" +seq+" line:"+eachLine+" "+s.length);
					continue;
				}
				
				String curr_datetime=s[token_having_ddmmyyhhmmss_datetime-1];
				//HH:MM:SS
				currDateTimetoken=curr_datetime.substring(curr_datetime.indexOf(" ")+1,curr_datetime.length() );
				
				//DEBUG
				//if(seq>=10) break;
				
				String [] arr_curr_datetime_=currDateTimetoken.split(":");
				String HH =arr_curr_datetime_[0];
				String MM =arr_curr_datetime_[1];
				String SS =arr_curr_datetime_[2];
				String curr_time_metric_FEATURE_to_split="";

				//either minute or hour or second
				if(flag_hour_R_min_R_sec.toLowerCase().equalsIgnoreCase("m"))
					curr_time_metric_FEATURE_to_split=MM;
				else if(flag_hour_R_min_R_sec.toLowerCase().equalsIgnoreCase("ms")){
					curr_time_metric_FEATURE_to_split=MM+SS;
				}
				else if(flag_hour_R_min_R_sec.toLowerCase().equalsIgnoreCase("s"))
					curr_time_metric_FEATURE_to_split=SS;
				else if(flag_hour_R_min_R_sec.toLowerCase().equalsIgnoreCase("h"))
					curr_time_metric_FEATURE_to_split=HH;
				else if(flag_for_token_type_4_datetime.equalsIgnoreCase("2") && flag_hour_R_min_R_sec.indexOf("weekly")>=0 ){
					//example-->2012-04-01 00:00:00
//					System.out.println("currDateTimetoken:"+curr_datetime);
					//we assume the file has only the monthly data 
					curr_time_metric_FEATURE_to_split= 
									curr_datetime.substring(curr_datetime.lastIndexOf("-"),
													curr_datetime.indexOf(" ")); //date DD only
					
					curr_time_metric_FEATURE_to_split=curr_time_metric_FEATURE_to_split.replace("-", "");
					
					int curr_time_metric_FEATURE_to_split_INT=Integer.valueOf(curr_time_metric_FEATURE_to_split);
					//
					if(curr_time_metric_FEATURE_to_split_INT>=1 && curr_time_metric_FEATURE_to_split_INT<=7){
						curr_time_metric_FEATURE_to_split="1week";
					}
					else if(curr_time_metric_FEATURE_to_split_INT>=8 && curr_time_metric_FEATURE_to_split_INT<=14){
						curr_time_metric_FEATURE_to_split="2week";
					}
					else if(curr_time_metric_FEATURE_to_split_INT>=15 && curr_time_metric_FEATURE_to_split_INT<=21){
						curr_time_metric_FEATURE_to_split="3week";
					}
					else if(curr_time_metric_FEATURE_to_split_INT>=22 && curr_time_metric_FEATURE_to_split_INT<=31){
						curr_time_metric_FEATURE_to_split="4week";
					}
					
				}
				else if(flag_for_token_type_4_datetime.equalsIgnoreCase("2") && flag_hour_R_min_R_sec.indexOf("daily")>=0 ){
					// we get the date only
					curr_time_metric_FEATURE_to_split= curr_datetime.substring(0, curr_datetime.lastIndexOf(" ")).replace("\"", ""); 
				}
				
//				System.out.println("curr_time_metric_FEATURE_to_split: arr_curr_datetime_:"+curr_time_metric_FEATURE_to_split
//									+" arr_curr_datetime_.len:"+arr_curr_datetime_.length+" HH:"+HH+" MM:"+MM+" SS:"+SS
//									+" currDateTimetoken:"+currDateTimetoken +" "+map_feature_MapSeqNLine.containsKey(curr_time_metric_FEATURE_to_split));
				
				//
				writer_debug.append("curr_time_metric_FEATURE_to_split: arr_curr_datetime_:"+curr_time_metric_FEATURE_to_split
									+" arr_curr_datetime_.len:"+arr_curr_datetime_.length+" HH:"+HH+" MM:"+MM+" SS:"+SS
									+" currDateTimetoken:"+currDateTimetoken +" "+map_feature_MapSeqNLine.containsKey(curr_time_metric_FEATURE_to_split)+"\n");
				writer_debug.flush();
					
				// 
				if(map_feature_MapSeqNLine.containsKey(curr_time_metric_FEATURE_to_split)){
					TreeMap<Integer,String> tmp_map=map_feature_MapSeqNLine.get(curr_time_metric_FEATURE_to_split);
					int size=tmp_map.size()+1;
					
					tmp_map.put(size, eachLine);
					
					map_feature_MapSeqNLine.put(curr_time_metric_FEATURE_to_split, tmp_map);
				}
				else{
					TreeMap<Integer,String> tmp_map=new TreeMap<Integer, String>();
					tmp_map.put(1, eachLine);
					map_feature_MapSeqNLine.put(curr_time_metric_FEATURE_to_split, tmp_map);
				}
				 
			} //END seq
			
			//ENDING
			System.out.println(" after cnt_file:"+cnt_file +" "+map_unique_MM_as_key.size()+" "
					+map_currFile_set_of_lines.size());
			
			//********** BEGIN -- write the last SPLIT
			cnt_file++;
 
				//********** END -- write the last SPLIT
			 
			//WRITE each split based on feature
			for( String curr_time_metric_FEATURE_to_split:map_feature_MapSeqNLine.keySet()){
				
				String orig_curr_feat=curr_time_metric_FEATURE_to_split;
				
				//
				if(flag_hour_R_min_R_sec.equals("1"))
					curr_time_metric_FEATURE_to_split=String.format("%010d", Integer.valueOf(curr_time_metric_FEATURE_to_split)   );
				
				String fileName="";
				// 
				if(flag_hour_R_min_R_sec.equalsIgnoreCase("weekly")|| flag_hour_R_min_R_sec.equalsIgnoreCase("daily")){
					fileName=baseFolder_out+input_primaryFileName.replace(".csv","") +"_"+curr_time_metric_FEATURE_to_split+".csv";
				}
				else
					fileName=baseFolder_out+input_primaryFileName+"_"+curr_time_metric_FEATURE_to_split+".txt";
				
				//if already exists , DONT write
				if((new File(fileName)).exists()){
					System.out.println("skipping file->"+fileName);
					continue;
				}
				else{
					writer_AlreadyRANfiles.append(fileName +"\n");
					writer_AlreadyRANfiles.flush();
				}
				
				// NEW FILE WRITER
				FileWriter writer=new FileWriter(new File(fileName));
				TreeMap<Integer,String> tmpMap=map_feature_MapSeqNLine.get(orig_curr_feat);
				//
				if(tmpMap!=null){
					System.out.println("writing size: "+tmpMap.size()+ " feature split:"+curr_time_metric_FEATURE_to_split
										+" file:"+baseFolder_out+curr_time_metric_FEATURE_to_split+".txt");
					//
					for(int seq:tmpMap.keySet()){
						writer.append(tmpMap.get(seq) +"\n" );
						writer.flush();
					}
				}
				else{
					System.out.println("tmpMap is NULL feature split:"+curr_time_metric_FEATURE_to_split+" out_file:"+fileName);
				}
				
			}
			
			System.out.println("given baseFolder:"+baseFolder);
			System.out.println("given baseFolder_out:"+baseFolder_out);
			System.out.println("debug file->"+baseFolder+"debug_.txt");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			
		}
	}
	// main
	public static void main(String[] args) throws Exception{
		 
		/**** p21 steps notes
		 * (1) split_inputFile_2_multipleEachFile1_N_minutes() <- split weekly or monthly or seconds
		 * (2) p19_p21_convert_token_with_numbers_to_AggregateNumbers() <- convert to categorical data
		 * (3) p21_convert_to_GBAD ()   ////OR may be crawler() "prob" "p5" <- create GBAD files
		 */
		
		/***
		 * 
		 * input datetime token example:
		 * flag_for_token_type_4_datetime==1-->01/01/1970 00:07:39 
		 * flag_for_token_type_4_datetime==2-->2012-04-01 00:00:00
		 * 
		 */
		
		//p19 setup start
		String baseFolder="/Users/lenin/Downloads/#problems/p19/";
		//OUTPUT of convert_epoch_datetime
		String in_Folder_or_file=baseFolder+"C17693_C721_flows_sample_CONVERED.txt";
		String flag_hour_R_min_R_sec="s"; //{h,m,s,weekly}
		//p19 setup end		

		//p21 setup start
		baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/daily_try1/daily-split-NOseeding/daily_missingMonthlyMay20152016/temp/";
		baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/2014Only/";
		
		in_Folder_or_file=baseFolder; //+"March12.csv";
		flag_hour_R_min_R_sec="daily"; //{h,m,s,weekly,daily} <--weekly/daily only works with flag_for_token_type_4_datetime="2"
		//p21 setup end
	
		String alreadyRAN_files=baseFolder+"alreadyRANfiles.txt";
		boolean isAppend_alreadyRAN_files=true;
		//Create output folder.
		String only_files_with_EXTENSION=".csv";
		String flag_for_token_type_4_datetime="2"; //{}
		//String outputFile=baseFolder+"flow_OUTPUT.txt";
		String delimiter_for_file1=",";
		int token_having_ddmmyyhhmmss_datetime=2; 
		int number_of_minutes_to_split=15;
		boolean is_inputFolder_or_File=(new File(in_Folder_or_file)).isDirectory();
		
		//INPUT is ONLY a file
		if(is_inputFolder_or_File==false){
		//note:We often assume that the file is in chronological order of datetime
			
			// split_inputFile_2_multipleEachFile1_N_minutes
			split_inputFile_2_multipleEachFile1_N_minutes(
														 baseFolder,
														 in_Folder_or_file,
														 delimiter_for_file1,
														 //outputFile,
														 token_having_ddmmyyhhmmss_datetime,
														 number_of_minutes_to_split,
														 flag_for_token_type_4_datetime,
														 flag_hour_R_min_R_sec, //{h,m,s,weekly}
														 alreadyRAN_files,
														 isAppend_alreadyRAN_files
														 );
		}
		else{ // INPUT is ONLY a FOLDER
			
		 String [] list_of_Files=(new File(baseFolder)).list();
		 int max_files=list_of_Files.length;
		 int c=0;
		 //
		 while(c<max_files){
			 	String curr_inFile=baseFolder+list_of_Files[c];
			 	if(curr_inFile.indexOf("Store")>=0){c++;continue;}
			 	if(curr_inFile.toLowerCase().indexOf(only_files_with_EXTENSION)==-1){c++;continue;}
			 	
			 	System.out.println("--------------processing..."+curr_inFile);
				// split_inputFile_2_multipleEachFile1_N_minutes
				split_inputFile_2_multipleEachFile1_N_minutes(
															 baseFolder,
															 curr_inFile,
															 delimiter_for_file1,
															 //outputFile,
															 token_having_ddmmyyhhmmss_datetime,
															 number_of_minutes_to_split,
															 flag_for_token_type_4_datetime,
															 flag_hour_R_min_R_sec, //{h,m,s,weekly}
															 alreadyRAN_files,
															 isAppend_alreadyRAN_files
															 );
			 
			   c++;
		 }
		 
		}
		
	}
}