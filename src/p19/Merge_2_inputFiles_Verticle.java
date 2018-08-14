package p19;

import crawler.Convert_epoch_datetime;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import crawler.Sort_given_treemap;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

//import crawler.mergeFiles;

public class Merge_2_inputFiles_Verticle {
	
	//http://csr.lanl.gov/data/cyber1/ - proc.txt and flows.txt
	public static void read_2_file_merge(
										 String baseFolder,
										 String file1_flows,
										 String file2_proc,
										 String file3_auth,
										 String file4_redteam,
										 String output_File,
										 int Flag // {0=time conversion, 1= redteam merge}
										){
		FileWriter writerdebug=null;
		FileWriter writer_output=null;
		//
		TreeMap<Integer, TreeMap<String, String>> map_time_LinesTreeMap_UNSORTED=new TreeMap<Integer, TreeMap<String,String>>();
		//
		HashMap<Integer, Integer> map_distinct_Time_unsorted=new HashMap<Integer, Integer>();
		
		try {
			writerdebug=new FileWriter(new File(baseFolder+"debug.txt"));
			writer_output=new FileWriter(new File(output_File));
			//(flows.txt.gz) time,duration,source computer,source port,destination computer,destination port,protocol,packet count,byte count 
			TreeMap<Integer,String> map_file1_flows=
												ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
						 												 							  file1_flows, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 "f1 ", //debug_label
																									 false //isPrintSOP
						 																			);
			System.out.println(" File1 loading..");
			TreeMap<String,TreeMap<Integer,Integer>> map_file1_flows_primaryKey_lineNo=new TreeMap<String, TreeMap<Integer,Integer>>();
			// Flows -->"time,duration,source computer,source port,destination computer,destination port,protocol,packet count,byte count" 
			// form <uniqPrimarykey, lineNo> = ? primary key = time!!!sourceComputer!!!destintionComputer
			//FLOWS --- each primarykey set, its difference occurance (set) of lineNumbers stored.
			for(int seq:map_file1_flows.keySet()){
				String []s=map_file1_flows.get(seq).split(",");
				//System.out.println("line:"+map_file1_flows.get(seq));
				//time!!!sourceComputer!!!destintionComputer
				String primaryKey_time_source_destinaton=s[0]+"!!!"+s[2]+"!!!"+s[4];
				//store lineNo
				if(map_file1_flows_primaryKey_lineNo.containsKey(primaryKey_time_source_destinaton)){
					TreeMap<Integer, Integer> temp=map_file1_flows_primaryKey_lineNo.get(primaryKey_time_source_destinaton);
					temp.put(seq, -1);
					map_file1_flows_primaryKey_lineNo.put(primaryKey_time_source_destinaton,  temp);
				}
				else{
					TreeMap<Integer, Integer> temp=new TreeMap<Integer, Integer>();
					temp.put(seq, -1);
					map_file1_flows_primaryKey_lineNo.put(primaryKey_time_source_destinaton,  temp);
				}
			}
			
			//(auth.txt) "time,source user@domain,destination user@domain,source computer,destination computer,authentication type,logon type,authentication orientation,success/failure
			TreeMap<Integer,String> map_file2_auth=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
															 							  file3_auth,
																					 	 -1, //startline, 
																						 -1, //endline,
																						 "f1 ", //debug_label
																						 false //isPrintSOP
																						 );
			
			
			TreeMap<String,TreeMap<Integer,Integer>> map_file2_auth_primaryKey_lineNo=new TreeMap<String, TreeMap<Integer,Integer>>();
			TreeMap<String,TreeMap<Integer,Integer>> map_file2_auth_primaryKey2_lineNo=new TreeMap<String, TreeMap<Integer,Integer>>();
			TreeMap<String,TreeMap<Integer,Integer>> map_file2_auth_primaryKey3_lineNo=new TreeMap<String, TreeMap<Integer,Integer>>();
			
			//auth.txt->"time,source user@domain,destination user@domain,source computer,destination computer,authentication type,logon type,authentication orientation,success/failure" 
			System.out.println(" File2 loading..");
			//each primarykey set, its difference occurrance (set) of lineNumbers stored.
			//AUTH ---- form <uniqPrimarykey, lineNo> = ? primary key = time!!!sourceComputer!!!destintionComputer
			for(int seq:map_file2_auth.keySet()){
				String []s=map_file2_auth.get(seq).split(",");
				//time!!!sourceComputer_user!!!sourceComputer!!!destintionComputer
				String primaryKey_time_source_destinaton=s[0]+"!!!"+s[1]+"!!!"+s[3]+"!!!"+s[4];
				String primaryKey2_time_sourceUser_Source=s[0]+"!!!"+s[1]+"!!!"+s[3];
				String primaryKey2_time_destUser_Destination=s[0]+"!!!"+s[2]+"!!!"+s[4];
				//store lineNo
				if(map_file2_auth_primaryKey_lineNo.containsKey(primaryKey_time_source_destinaton)){
					TreeMap<Integer, Integer> temp=map_file2_auth_primaryKey_lineNo.get(primaryKey_time_source_destinaton);
					temp.put(seq, -1);
					map_file2_auth_primaryKey_lineNo.put(primaryKey_time_source_destinaton,  temp);
					map_file2_auth_primaryKey2_lineNo.put(primaryKey2_time_sourceUser_Source, temp);
					map_file2_auth_primaryKey3_lineNo.put(primaryKey2_time_destUser_Destination , temp);
				}
				else{
					TreeMap<Integer, Integer> temp=new TreeMap<Integer, Integer>();
					temp.put(seq, -1);
					map_file2_auth_primaryKey_lineNo.put(primaryKey_time_source_destinaton,  temp);
					map_file2_auth_primaryKey2_lineNo.put(primaryKey2_time_sourceUser_Source, temp);
					map_file2_auth_primaryKey3_lineNo.put(primaryKey2_time_destUser_Destination , temp);
				}
			}

			//(proc.txt.gz) time,user@domain,computer,process name,start/end 
			TreeMap<Integer,String> map_file3_proc=
												ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
												.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																		 							  file2_proc,
																								 	 -1, //startline, 
																									 -1, //endline,
																									 "f1 ", //debug_label
																									 false //isPrintSOP
																									 );
			TreeMap<String,TreeMap<Integer,Integer>> map_file3_proc_primaryKey_lineNo=new TreeMap<String, TreeMap<Integer,Integer>>();
			System.out.println(" File3 loading..");
			//each primarykey set, its difference occurrence (set) of lineNumbers stored.
			// PROC ---- form <uniqPrimarykey, lineNo> = ? primary key = time!!!source
			for(int seq:map_file3_proc.keySet()){
				String []s=map_file3_proc.get(seq).split(",");
				//time!!!Computer_User!!!Computer
				String primaryKey_time_source_process=s[0]+"!!!"+s[1]+"!!!"+s[2];
				//store lineNo
				if(map_file3_proc_primaryKey_lineNo.containsKey(primaryKey_time_source_process)){
					TreeMap<Integer, Integer> temp=map_file3_proc_primaryKey_lineNo.get(primaryKey_time_source_process);
					temp.put(seq, -1);
					map_file3_proc_primaryKey_lineNo.put(primaryKey_time_source_process,  temp);
				}
				else{
					TreeMap<Integer, Integer> temp=new TreeMap<Integer, Integer>();
					temp.put(seq, -1);
					map_file3_proc_primaryKey_lineNo.put(primaryKey_time_source_process,  temp);
				}
			}
			
			//READ redteam (ground truth) "time,user@domain,source computer,destination computer"
			TreeMap<Integer,String> map_file4_redteam=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																						 file4_redteam,
																					 	 -1, //startline, 
																						 -1, //endline,
																						 "f1 ", //debug_label
																						 false //isPrintSOP
																						 );
			TreeMap<String,String> map_file4_redteam_primaryKey_lineNo=new TreeMap<String, String>();
			//  "time,user@domain,source computer,destination computer"
			for(int seq:map_file4_redteam.keySet()){
				String []s=map_file4_redteam.get(seq).split(",");
				String primaryKey_time_user_sourceComputer=s[0]+"!!!"+s[1]+"!!!"+s[2];
				map_file4_redteam_primaryKey_lineNo.put( primaryKey_time_user_sourceComputer , map_file4_redteam.get(seq));
			}
			//////  read ////
			

			
			/////TIME CONVERSION START ////////
			int found_file2_in_file1=0;int not_found_file2_in_file1=0;
			Map<Integer, Integer> map_distinct_Time_sorted=new HashMap<Integer, Integer>();
			System.out.println(" iterate for output..");
			int size_file2_auth_primaryKey_time_source_destinaton =map_file2_auth_primaryKey_lineNo.size();
			int counter_for_size_file2_auth_primaryKey_time_source_destinaton=0;
			
			//time conversion
			if(Flag==0){
			
			//iterate through primary key from 2nd file
			for(String file2_auth_primaryKey_time_source_destinaton: map_file2_auth_primaryKey_lineNo.keySet() ){
				counter_for_size_file2_auth_primaryKey_time_source_destinaton++;
				System.out.println("counter ="+counter_for_size_file2_auth_primaryKey_time_source_destinaton+
									" out of size="+size_file2_auth_primaryKey_time_source_destinaton);
				//
				TreeMap<Integer,Integer> curr_map_file2_auth_primaryKey_lineNo
								=map_file2_auth_primaryKey_lineNo.get(file2_auth_primaryKey_time_source_destinaton);
				 
				// file2_AUTH - for currPrimaryKey - each line (seq) 
				for(int lineNo:curr_map_file2_auth_primaryKey_lineNo.keySet()){
					String eachLine_file2_auth=map_file2_auth.get(lineNo);
					//String []arr_file2_eachLine=eachLine_file2_auth.split("!!!");
					
					//for curr curr_map_file2_auth_primaryKey_line, get its corresponding values from first file.
					
					
					//****(NOTE: ELSE CONDITION TESTED,BUT NEED 2 CHECK ON THIS IF CONDITION LATER= I HAVE TO REPLICATE SAME AS IN ELSE CONDITION) 
					//DOES EXISTS??? file2 primaryKey available in file1 ???
					if(map_file1_flows_primaryKey_lineNo.containsKey(file2_auth_primaryKey_time_source_destinaton)){
						
						TreeMap<Integer,Integer> curr_map_file1_flows_primaryKey_lineNo=
											map_file1_flows_primaryKey_lineNo.get(file2_auth_primaryKey_time_source_destinaton);
						
						//BEGIN below 2 lines for file 3 - primary key
						//split primary key to get time + source (pick only source to get source's process)
						String []arr_file2_auth_primaryKey_time_source_destinaton=file2_auth_primaryKey_time_source_destinaton.split("!!!");
						//primary key for file3
						String file3_auth_primaryKey_time_source=arr_file2_auth_primaryKey_time_source_destinaton[0]
																+"!!!"+arr_file2_auth_primaryKey_time_source_destinaton[1];
						//END  2 lines for file 3 - primary key
						
						//FILE1 -- file1_auth - for currPrimaryKey - each line (seq)
						for(int lineNo2:curr_map_file1_flows_primaryKey_lineNo.keySet()){
							String eachLine_file1_flows=map_file1_flows.get(lineNo2);
							System.out.println(" file1 iterate...");
							
//							writerdebug.append("\n map_file3_proc_primaryKey_lineNo:"+map_file3_proc_primaryKey_lineNo
//														+" \n file3_auth_primaryKey_time_source:"+file3_auth_primaryKey_time_source);
							writerdebug.flush();
							
							if(map_file3_proc_primaryKey_lineNo.containsKey(file3_auth_primaryKey_time_source)){
								TreeMap<Integer,Integer> curr_map_file3_proc_primaryKey_lineNo=
														map_file3_proc_primaryKey_lineNo.get(file3_auth_primaryKey_time_source);
								
	
								//FILE3 -- file3_proc - for currPrimaryKey - each line (seq)
								for(int lineNo3:curr_map_file3_proc_primaryKey_lineNo.keySet()){
									String eachLine_file3_proc=map_file3_proc.get(lineNo3);
									
									//System.out.println("writing final EXISTS...");
									
									writer_output.append(eachLine_file2_auth + "!!!"+eachLine_file1_flows+"!!!"+eachLine_file3_proc+"\n");
									writer_output.flush();
									
								}
							}
							
							
						}//END FILE1 -- file1_auth - for currPrimaryKey - each line (seq)
						found_file2_in_file1++;
					}
					else{ // NOT EXISTS FILE2 IN FILE1
						 
						
						//BEGIN below 2 lines for file 3 - primary key
						//split primary key to get time + source (pick only source to get source's process)
						//	file2_auth_primaryKey_time_source_destinaton-->
						String []arr_file2_auth_primaryKey_time_source_destinaton=file2_auth_primaryKey_time_source_destinaton.split("!!!");//<-time!!!sourceComputer_user!!!sourceComputer!!!destintionComputer
						//primary key for file3
						String file3_auth_primaryKey_time_source=arr_file2_auth_primaryKey_time_source_destinaton[0]//time
																+"!!!"+arr_file2_auth_primaryKey_time_source_destinaton[1]//sourceComputer_user
																+"!!!"+arr_file2_auth_primaryKey_time_source_destinaton[2];//sourceComputer
						//END  2 lines for file 3 - primary key
						
							// 
							if(map_file3_proc_primaryKey_lineNo.containsKey(file3_auth_primaryKey_time_source)){//<-time!!!Computer_User!!!Computer
								TreeMap<Integer,Integer> curr_map_file3_proc_primaryKey_lineNo=
														map_file3_proc_primaryKey_lineNo.get(file3_auth_primaryKey_time_source);
								
	
								//FILE3 -- file3_proc - for currPrimaryKey - each line (seq)
								for(int lineNo3:curr_map_file3_proc_primaryKey_lineNo.keySet()){
									//System.out.println(" file3 iterate...");
									String eachLine_file3_proc=map_file3_proc.get(lineNo3);
									
									//System.out.println("writing final NOT EXISTS...");
									String output_all_lines_concatenated=eachLine_file2_auth +","
																			//+ "!!!"+ ""+"!!!"
																			+eachLine_file3_proc;
									
									//OUTPUT
//									writer_output.append(output_all_lines_concatenated+"\n");
//									writer_output.flush();
									
									//UNSORTED
									String [] s2=output_all_lines_concatenated.split(",");
									int  time =Integer.valueOf(s2[0]);
									//already time exists
									if(map_time_LinesTreeMap_UNSORTED.containsKey(time)){
										TreeMap<String,String> temp=map_time_LinesTreeMap_UNSORTED.get(time);
										
										if(temp.containsKey(output_all_lines_concatenated)){//this line already exists
											System.out.println("continue....");
											continue;
										}
										
										temp.put(output_all_lines_concatenated, "");
										map_time_LinesTreeMap_UNSORTED.put(time, temp);
									}
									else{
										TreeMap<String,String> temp=new TreeMap<String, String>();
										temp.put(output_all_lines_concatenated, "");
										map_time_LinesTreeMap_UNSORTED.put(time, temp);
										//distinct time unsorted
										int time_seq=map_distinct_Time_unsorted.size()+1;
										map_distinct_Time_unsorted.put(time_seq, time);
										
									}
								}
							}
							else{
								
								//System.out.println("writing final (ELSE) proc NOT AVAILABLE...");
								String output_all_lines_concatenated=eachLine_file2_auth +","
																		//+ "!!!"+ ""+"!!!"
																		+",,,,,,,,"; //empty NOT AVAILABLE PROC
								
								//OUTPUT
//								writer_output.append(output_all_lines_concatenated+"\n");
//								writer_output.flush();
								
								//UNSORTED
								String [] s2=output_all_lines_concatenated.split(",");
								int  time =Integer.valueOf(s2[0]);
								//already time exists
								if(map_time_LinesTreeMap_UNSORTED.containsKey(time)){
									TreeMap<String,String> temp=map_time_LinesTreeMap_UNSORTED.get(time);
									temp.put(output_all_lines_concatenated, "");
									map_time_LinesTreeMap_UNSORTED.put(time, temp);
								}
								else{
									TreeMap<String,String> temp=new TreeMap<String, String>();
									temp.put(output_all_lines_concatenated, "");
									map_time_LinesTreeMap_UNSORTED.put(time, temp);
									//distinct time unsorted
									int time_seq=map_distinct_Time_unsorted.size()+1;
									map_distinct_Time_unsorted.put(time_seq, time);
									
								}
								
							}
						//
						writerdebug.append("\n NOT FOUND IN file1 (flows) from file2 (file2_auth): "+file2_auth_primaryKey_time_source_destinaton );
						writerdebug.flush();
						not_found_file2_in_file1++;
					}
				}
				
				// SORTING TIME
				 map_distinct_Time_sorted=
								Sort_given_treemap.sortByValue_II_method4_ascending(map_distinct_Time_unsorted);
				
	
				
				TreeMap<Integer,Integer> map_file2_auth_primaryKey_line=new TreeMap<Integer, Integer>();
				TreeMap<Integer,Integer> map_file3_proc_primaryKey_line=new TreeMap<Integer, Integer>();
				
			}
			System.out.println("found_file2_in_file1:"+found_file2_in_file1+" not_found_file2_in_file1:"+not_found_file2_in_file1);
			
			
			System.out.println("SORTED each time running map_distinct_Time_sorted.SIZE:"+map_distinct_Time_sorted.size()
								+" map_time_LinesTreeMap_UNSORTED.size:"+map_time_LinesTreeMap_UNSORTED.size());
			int COUNT_not_found_concline=0;
			//add header 
//			writer_output.append("1time,2datetimeCONVERTED,3source user@domain,4destination user@domain,5source computer,6destination computer,7authentication type,8logon type,9authentication orientation,10success/failure"
//								+"11time,12user@domain,13computer,14process name,15start/end\n"
//								 );
			
			//sorted each time 
			for(int curr_seq2:map_distinct_Time_sorted.keySet()){
				//time 
				int curr_time=map_distinct_Time_sorted.get(curr_seq2);
				
				if(map_time_LinesTreeMap_UNSORTED.containsKey(curr_time)){
					TreeMap<String, String> map_concLINE_as_KEY= map_time_LinesTreeMap_UNSORTED.get(curr_time);
						System.out.println("map_concLINE_as_KEY.size="+map_concLINE_as_KEY.size());
						for(String concLINE:map_concLINE_as_KEY.keySet()){
								//OUTPUT
								writer_output.append(concLINE+"\n");
								writer_output.flush();
						
						}
				}
				else{
					COUNT_not_found_concline++;
				}
			
				System.out.println("COUNT_not_found_concline:"+COUNT_not_found_concline);
			}
			
			} //flag ==0
			
			
			// Merging redteam
			if(Flag==1){
				//flag ==1 
				//read g truth, and map
				for(String primarykey:map_file4_redteam_primaryKey_lineNo.keySet()){
					String newLine="";
					if(map_file3_proc_primaryKey_lineNo.containsKey( primarykey)){
						newLine=primarykey+ "!!!gtruthLine->"+map_file3_proc_primaryKey_lineNo.get(primarykey)+"<-";
					}
					//get all lineNo having this primary key 2 and concatenate...
					if(map_file2_auth_primaryKey2_lineNo.containsKey(primarykey)  ){
						
						
						TreeMap<Integer,Integer> curr_map_file2_auth_primaryKey2_lineNo=
												map_file2_auth_primaryKey2_lineNo.get(primarykey);
		
						String eachLine_file2_auth_concLine="";
						//FILE2 -- file2_auth - for currPrimaryKey - each line (seq)
						for(int lineNo3:curr_map_file2_auth_primaryKey2_lineNo.keySet()){
							eachLine_file2_auth_concLine=eachLine_file2_auth_concLine+
														" line:"+map_file2_auth.get(lineNo3);
						}
						
						if(newLine.length()==0)
							newLine=primarykey+ "!!!authprimkey2->"+map_file2_auth_primaryKey2_lineNo.get(primarykey)+"<-"
												+"!!!Lines:"+eachLine_file2_auth_concLine;
						else
							newLine=newLine+ "!!!authprimkey2->"+map_file2_auth_primaryKey2_lineNo.get(primarykey)+"<-"
												+"!!!Lines:"+eachLine_file2_auth_concLine;
						
					}
					
					//get all lineNo having this primary key  3 and concatenate...
					if(map_file2_auth_primaryKey3_lineNo.containsKey(primarykey)  ){
						if(newLine.length()==0)
							newLine=primarykey+ "!!!authprimkey3->"+map_file2_auth_primaryKey3_lineNo.get(primarykey)+"<-";
						else
							newLine=newLine+ "!!!authprimkey3->"+map_file2_auth_primaryKey3_lineNo.get(primarykey)+"<-";
					}
					//
					writer_output.append(newLine+"\n");
					writer_output.flush();
					
					
				} //for(int primarykey:map_file4_redteam.keySet()){
				
			} // if(Flag==1){
			
			//System.out.println("map_time_LinesTreeMap_UNSORTED:"+map_time_LinesTreeMap_UNSORTED);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			
		}
		
	}
	
	// main
	public static void main(String[] args) throws Exception{
		String source_machinename="C995";
		
		String baseFolder="/Users/lenin/Downloads/#problems/p19/"+source_machinename+"_only/";
		
		//
		String file1_flows=baseFolder+source_machinename+"_only_flows.txt";
		String file2_proc=baseFolder+source_machinename+"_only_proc.txt";
		String file3_auth=baseFolder+source_machinename+"_only_auth.txt";
		String file4_redteam=baseFolder+source_machinename+"_only_redteam.txt";
		String file5_DNS=baseFolder+source_machinename+"_only_dns.txt";
		
		String output_file=baseFolder+"output.txt";
		
		// comment and uncomment below
		// this method implementation can be replaced using 
		// "readFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom_SecondFile_to_FirstFile"
		//  merging files (FLAG=0 merging files, no ground truth file , file=1 merge with ground truth)
//		read_2_file_merge(
//							 baseFolder,
//							 file1_flows,
//							 file2_proc,
//							 file3_auth,
//							 file4_redteam,
//							 output_file,
//							 1 // flag = {0,1} 
//						 );
	

		///*********************************************************************************************************
		// ***** merging vertical 
		TreeMap<Integer,String> map_seq_FirstFile=new TreeMap<Integer, String>();
		TreeMap<Integer,String> map_seq_SecondFile=new TreeMap<Integer, String>();
		 
		String token_in_first_file_having_primary_Key_CSV="";String token_in_second_file_having_primary_Key_CSV="";
		String token_interested_in_second_File_to_be_appended_to_first_CSV="";
		TreeMap<String, String> map_file_Header=new TreeMap<String, String>();
		//header
		map_file_Header.put("auth.txt",
							"time,source user@domain,destination user@domain,source computer,destination computer,authentication type,logon type,authentication orientation,success/failure");
		map_file_Header.put("proc.txt",
							"time,user@domain,computer,process name,start/end");
		map_file_Header.put("flows.txt",
							"time,duration,source computer,source port,destination computer,destination port,protocol,packet count,byte count");
		map_file_Header.put("dns.txt",
							"time,source computer,computer resolved");
		String tmp_suffix="";
		//files pair
		map_seq_FirstFile.put(1,file3_auth); 
		map_seq_SecondFile.put(1, file1_flows);
		
		map_seq_FirstFile.put(2, file3_auth);
		map_seq_SecondFile.put(2, file2_proc);
		
		map_seq_FirstFile.put(3, file3_auth);
		map_seq_SecondFile.put(3, file5_DNS);
		
		TreeMap<Integer, String> map_seq_outFile=new TreeMap<Integer, String>();
		
		for(int seq:map_seq_FirstFile.keySet() ){
				
			// 1.
			//redteam.txt - "time,user@domain,source computer,destination computer"
			String first_File="";
			//firstFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/file.us.50.thinktank.txt";
			first_File = map_seq_FirstFile.get(seq);
			// 2.
			//auth.txt - "time,source user@domain,destination user@domain,source computer,destination computer,authentication type,logon type,authentication orientation,success/failure"
			String second_File="";
			//secondFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_isRSS_true.SET.1.copy.txt";
			second_File= map_seq_SecondFile.get(seq);
			
			tmp_suffix= new File( first_File).getName() +"_"+new File(second_File).getName();
			String outFile_Exists=baseFolder+"__exists.txt_"+tmp_suffix;
			String outFile_Not_Exists=baseFolder+"__NOexists.txt_"+ tmp_suffix;
			String delimiter_to_be_added_to_first_File="";
			String [] arr_list_Files=new String[2];
			String outFile_header="";
			//
			if(first_File.equals(file3_auth) && second_File.equals(file1_flows) ){
				//primary key created in same given order
				token_in_first_file_having_primary_Key_CSV="1,4,5";
				//primary key created in same given order
				token_in_second_file_having_primary_Key_CSV="1,3,5";
				token_interested_in_second_File_to_be_appended_to_first_CSV="1,2,3,4,5,6,7,8,9";
				outFile_header= map_file_Header.get("auth.txt").replace(",", "!!!")+"!!!"
							   +map_file_Header.get("flows.txt").replace(",", "!!!");
			}
			else if(first_File.equals(file3_auth) && second_File.equals(file2_proc) ){
				//primary key created in same given order
				token_in_first_file_having_primary_Key_CSV="1,2,4";
				//primary key created in same given order
				token_in_second_file_having_primary_Key_CSV="1,2,3";
				token_interested_in_second_File_to_be_appended_to_first_CSV="1,2,3,4,5";
				
				outFile_header=  map_file_Header.get("auth.txt").replace(",", "!!!")+"!!!"
						   		+map_file_Header.get("proc.txt").replace(",", "!!!");
			}
			else if(first_File.equals(file3_auth) && second_File.equals(file5_DNS) ){
				//primary key created in same given order
				token_in_first_file_having_primary_Key_CSV="1,4";
				//primary key created in same given order
				token_in_second_file_having_primary_Key_CSV="1,2";
				token_interested_in_second_File_to_be_appended_to_first_CSV="1,2,3";
				//
				outFile_header=map_file_Header.get("auth.txt").replace(",", "!!!")+"!!!"
						   		+map_file_Header.get("dns.txt").replace(",", "!!!");
			}
			String outFile="";
			// comment and uncomment below (START 
			//readFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom_SecondFile_to_FirstFile
//			readFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom_SecondFile_to_FirstFile.
//			readFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom_SecondFile_to_FirstFile(
//																								first_File,
//																								delimiter_to_be_added_to_first_File,
//																								token_in_first_file_having_primary_Key_CSV,//-1
//																								second_File,
//																								token_in_second_file_having_primary_Key_CSV,
//																								token_interested_in_second_File_to_be_appended_to_first_CSV,
//																								outFile_Exists,
//																								false, //is_Append_outFile_exists
//																								outFile_Not_Exists,
//																								false, //is_Append_outFile_not_exists
//																								true //is_add_dummy_tokens_for_NOT_exists
//																								);
			arr_list_Files[0]=outFile_Exists;
			arr_list_Files[1]=outFile_Not_Exists;
			outFile=baseFolder+"MERGED_existNnotExist_"+tmp_suffix;
//			//merging exists and NOT exists
//			mergeFiles.mergeFiles("", //inputFolderName,
//								  arr_list_Files, //
//								  outFile,
//								  true, //isMac, 
//								  false //is_add_file_name_of_source_as_last_column_of_eachLine_in_mergedFile
//								);
// 
//			//write header at end
//			FileWriter currWriter=new FileWriter(new File(outFile), true);
//			//
//			currWriter.append(outFile_header);
//			currWriter.flush();
			
			// comment and uncomment below ( END); 
			
			map_seq_outFile.put(seq, outFile);
			
		} //for(int seq:map_seq_FirstFile.keySet() ){
		
		System.out.println(" baseFolder:"+baseFolder+"\n"
							+" header is in Eof:"+baseFolder+"MERGED_existNnotExist_"+tmp_suffix);
		System.out.println("map_seq_outFile:"+map_seq_outFile.size()+"--"+map_seq_outFile);
		//auth and proc
		String file_merged_out_auth_proc=map_seq_outFile.get(2);
		System.out.println("input for convert_epoch_datetime_4_FILE-> file_merged_out_auth_proc:"+file_merged_out_auth_proc);
		///*********************************************************************************************************
		//convert epoch time to 
		String file1_= file_merged_out_auth_proc; //output of read_2_file_merge()
		String OUTPUT_file3=file1_+"_#epoch_to_datetime.txt";
		long start_epoch_range=1;
		long end_epoch_range=111605015;
		
		int token_having_epoch=1;
		String delimiter_for_file1="!!!";
		
		// epoch to datetime // comment and uncomment below
		Convert_epoch_datetime.
		convert_epoch_datetime_4_FILE(
									  baseFolder,
									  file1_,
									  true,//  is_file1_has_header
									  delimiter_for_file1,
									  token_having_epoch,
									  -1, //start_epoch_range,
									  -1, //end_epoch_range,
									  OUTPUT_file3
									 );
		//write header at the end. (
		FileWriter writer=new FileWriter(new File(OUTPUT_file3),true);
		String header="1time,2datetimeCONVERTED,3source user@domain,4destination user@domain,5source computer,6destination computer,7authentication type,8logon type,9authentication orientation,10success/failure"
 						+"11time,12user@domain,13computer,14process name,15start/end\n";
		writer.append(header);
		System.out.println("NOTE: convert_epoch_datetime_4_FILE() - header is written in the END of file ->"+OUTPUT_file3);
		//**********************************************************************
		
		//OUTPUT of convert_epoch_datetime
		String file1=OUTPUT_file3;
		//String outputFile=baseFolder+"flow_OUTPUT.txt";
		int token_having_ddmmyyhhmmss_datetime=2;
		int number_of_minutes_to_split=1;
		String flag_hour_R_min_R_sec="s"; // {m,ms,s,h}
		
		// split_inputFile_2_multipleEachFile1_N_minutes
		split_inputFile_2_multipleEachFile1_N_minutes.
		split_inputFile_2_multipleEachFile1_N_minutes(
														 baseFolder,
														 file1,
														 delimiter_for_file1,
														 //outputFile,
														 token_having_ddmmyyhhmmss_datetime,
														 number_of_minutes_to_split,
														 flag_hour_R_min_R_sec,
													    "",
														"",
														true
													 );

		
		System.out.println("input File given for splitting:"+file1);
		
	}

}
