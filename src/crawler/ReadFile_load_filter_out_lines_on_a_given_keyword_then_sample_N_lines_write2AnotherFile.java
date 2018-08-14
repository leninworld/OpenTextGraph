package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;
//
public class ReadFile_load_filter_out_lines_on_a_given_keyword_then_sample_N_lines_write2AnotherFile {
	
	//First merge all files in a folder,
	// Unique LinePatterns will be taken from TreeMap<String, Integer> map_linePattern_MINallowedSamples
	// Flag={1,2}, // obselete 
	public static void readFile_in_Folder_load_filter_out_lines_on_a_given_keyword_then_sample_N_lines_write2AnotherFile(
																										String  baseFolder,
																										String	inputFolderName,
																										String  outputFolderName,
																										String  keywords_CSV, 
																										int 	n_samples, 
																										TreeMap<String, Integer> map_linePattern_MINallowedSamples,
																										TreeMap<String, Integer> map_linePattern_MAXallowedSamples,
																										String   debugFile, 
																										int 	 Flag, //obselete
																										int		 input_cnt_max_reach_exit, 
																										boolean is_strict_on_min_range
																										){
		
		//get unique linePatterns from map_linePattern_MINallowedSamples.
		TreeMap<String, Integer> map_linePattern_Count =new TreeMap<String, Integer>();
		
		TreeMap<Integer, String > map_seq_line =new TreeMap<Integer , String  >();
		
		TreeMap<String,Integer> map_isAlready_done_linePattern=new TreeMap<String,Integer>();
		
		
		String currLinepattern_="";
		FileWriter writer_debug=null;
		FileWriter writer_2=null;
		FileWriter writer_3=null;
		String [] keyword=keywords_CSV.split(",");int cnt=0; String line="";
		try{
			writer_debug=new FileWriter(new File(debugFile));
			// merge files
			ReadFiles_in_a_Folder_mergeFiles_Horizontal.readFiles_in_a_Folder_mergeFiles_Horizontal(inputFolderName,
																								    baseFolder+"merged_all.txt",
																								    "", //only_inputFiles_match_on_pattern
																								    false, //is_append_primary_FileName_at_end
																								    null, //inputFiles_input_2
																								    false, //isSOPprint
																								    "!!!"// delimiter
																								    );
			
			//get interested token number values from file
			TreeMap<Integer,String> map_inFile_eachLine=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
							 															 baseFolder+"merged_all.txt", 
																					 	 -1, 
																						 -1,
																						 " map_inFile_eachLine ", //debug_label
																						 false //isPrintSOP
																						 );
			int total_so_far_picked_samples_count=0;
			TreeMap<String, TreeMap<Integer,String>> map_linepattern_SeqNLine=new TreeMap<String, TreeMap<Integer,String>>();
			int t=0;int count_for_curr_keyword=0;TreeMap<String,Integer> map_linePattern_Count_for_DEBUG=new TreeMap<String, Integer>();
			String curr_linePattern="";
			FileWriter writer=null;
			// iterate each keyword 
			while(cnt<keyword.length){
					map_linePattern_Count=new TreeMap<String, Integer>();
					//create new file for this keyword 
					writer= new FileWriter(new File(outputFolderName+"filtered_on_"+keyword[cnt]+".txt" ) );
					count_for_curr_keyword=0;
					map_linePattern_Count_for_DEBUG=new TreeMap<String, Integer>();
					//
					for(int i:map_inFile_eachLine.keySet()){
						//
						if(map_inFile_eachLine.get(i).toLowerCase().indexOf(keyword[cnt])  >= 0 ){
							writer.append(map_inFile_eachLine.get(i)+"\n");
							writer.flush();
						}
					}
					
					writer_debug.append("\n for keyword " +keyword[cnt] +" hit count = "+count_for_curr_keyword+" <- reading file \n");
					writer_debug.flush();
					
					cnt++;
			} //END while(cnt<keyword.length){
			
			for(String i:map_linePattern_MINallowedSamples.keySet()){
				writer_debug.append("here map_linePattern_MINallowedSamples:"+map_linePattern_MINallowedSamples +"\n");
				writer_debug.flush();
			}
			
			
			// load each keyword
			cnt=0;
			
			//iterat each keyword file
			while(cnt<keyword.length){
				
				TreeMap<Integer,String> map_inFile_eachKeyword_eachLine=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
								 															  outputFolderName+"filtered_on_"+keyword[cnt]+".txt" , 
																						 	 -1, 
																							 -1,
																							 " map_inFile_eachKeyword_eachLine ", //debug_label
																							 false //isPrintSOP
																							 );
				
				
				
				
				// each curr keyword file search for each linePattern  
				for(String linePattern:map_linePattern_MINallowedSamples.keySet()){
				
					writer_2= new FileWriter(new File(outputFolderName+"filtered_on_"+keyword[cnt]+ "-"+linePattern+ ".txt" ) );
					
					String found_linePattern="";
	
					// 
					for(int i:map_inFile_eachKeyword_eachLine.keySet()){
						
						if(i==1){
							writer_debug.append("\n entering first time--> keyword:"+keyword[cnt]+" linePattern:"+linePattern+" i="+i
										+" <-\n"+" map_linePattern_MINallowedSamples:"+map_linePattern_MINallowedSamples+"\n");
							writer_debug.flush();
							
						}
						
						String currOrigLine=map_inFile_eachKeyword_eachLine.get(i);
						String currLine=currOrigLine.toLowerCase();
						//
//						if(currLine.indexOf(keyword[cnt]) >=0 ){
//							count_for_curr_keyword++;
//							writer.append("\n"+currOrigLine);
//							writer.flush();
//						}
						
						
//						if(!map_isAlready_done_linePattern.containsKey(linePattern)){
							writer_debug.append("\n does already exist? "+linePattern+" inside "+map_isAlready_done_linePattern+"\n");
							writer_debug.flush();	
							
							map_seq_line =new TreeMap<Integer , String  >();
							curr_linePattern=linePattern;
							//
							if( currLine.indexOf(linePattern)>=0  ){
									// DEBUG
		//							if( currLine.indexOf(keyword[cnt]) >=0  ){
		//							
		//								if(map_linePattern_Count_for_DEBUG!=null){
		//									
		//									if(map_linePattern_Count_for_DEBUG.containsKey( linePattern)){
		//										t=map_linePattern_Count_for_DEBUG.get(linePattern)+1;
		//										map_linePattern_Count_for_DEBUG.put(linePattern, t);
		//									}
		//								}
		//							}
									//
									if(map_linepattern_SeqNLine.containsKey(linePattern+":"+keyword[cnt])){
										TreeMap<Integer , String  > map_seq_line2=map_linepattern_SeqNLine.get(linePattern+":"+keyword[cnt]);
										int sz=map_seq_line2.size()+1;
										map_seq_line2.put( sz , currOrigLine);
										writer_debug.append("size->"+sz+"---"+map_seq_line2.size() + " for "+linePattern+ " keyword:"+keyword[cnt]+
																" "+map_linepattern_SeqNLine.get(linePattern+":"+keyword[cnt]).size()+ "\n");
										writer_debug.flush();
										
										writer_2.append( currOrigLine+"\n" );
										writer_2.flush();
										
										map_linepattern_SeqNLine.put( linePattern+":"+keyword[cnt] , map_seq_line2);
									}{
										TreeMap<Integer , String  > map_seq_line2=new TreeMap<Integer, String>();
										//map_seq_line=new TreeMap<Integer, String>();
										map_seq_line2.put(1, currOrigLine);
										map_linepattern_SeqNLine.put( linePattern +":"+keyword[cnt], map_seq_line2);
										writer_debug.append("size->"+1+"---"+map_seq_line2.size()+ " for "+linePattern+ " keyword:"+keyword[cnt]+
														    " "+map_linepattern_SeqNLine.get(linePattern+":"+keyword[cnt]).size()+ "\n");
										writer_debug.flush();
										
										writer_2.append( currOrigLine+"\n" );
										writer_2.flush();
										
									}
									//break;
								}
								else if(currLine.indexOf(keyword[cnt]) >=0 ){
									map_linePattern_Count_for_DEBUG.put(linePattern, 1);
								}
								
						   // } // END if(!map_isAlready_done_linePattern.containsKey(linePattern)){
						} // END for(int i:map_inFile_eachKeyword_eachLine.keySet()){
						
					 
					} //END for(String linePattern:map_linePattern_MINallowedSamples.keySet()){
				
					map_isAlready_done_linePattern.put( curr_linePattern , -1);
					cnt++;
			   } // END while(cnt<keyword.length){
			
			
			
			cnt=0;
			
			//iterat each keyword file
			while(cnt<keyword.length){
				
				// File of each curr keyword each linePattern  
				for(String linePattern:map_linePattern_MINallowedSamples.keySet()){
					
					TreeMap<Integer,String> map_inFile_eachKeyword_eachPattern_eachLine=
							ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
							 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
									 															 outputFolderName+"filtered_on_"+keyword[cnt]+ "-"+linePattern+ ".txt", 
																							 	 -1, 
																								 -1,
																								 " map_inFile_eachKeyword_eachPattern_eachLine ", //debug_label
																								 false //isPrintSOP
																								 );
				
					int size_map_inFile_eachKeyword_eachPattern_eachLine=map_inFile_eachKeyword_eachPattern_eachLine.size();
					
					writer_3= new FileWriter(new File(outputFolderName+"filtered_on_"+keyword[cnt]+ "-"+linePattern+"_"+n_samples+ ".txt" ) );
					
					
					
					// total available < min allowed, then write all, otherwise sample
					if(size_map_inFile_eachKeyword_eachPattern_eachLine<=map_linePattern_MINallowedSamples.get(linePattern)){
						// 
						for(int seq:map_inFile_eachKeyword_eachPattern_eachLine.keySet()){
							writer_3.append(map_inFile_eachKeyword_eachPattern_eachLine.get(seq)+"\n");
							writer_3.flush();
						}
					}
					else{
						TreeMap<Integer, String> map_already_= new TreeMap<Integer, String>();
						int count_collected=0;
						//
						while(count_collected <= map_linePattern_MINallowedSamples.get(linePattern)){
							int sample=Generate_Random_Number_Range.generate_Random_Number_Range(1, size_map_inFile_eachKeyword_eachPattern_eachLine);
							
							// 
							if(map_already_.containsKey(sample)){
								writer_3.append(map_inFile_eachKeyword_eachPattern_eachLine.get(sample)+"\n");
								writer_3.flush();
								count_collected++;
								//
								if(count_collected >= map_linePattern_MINallowedSamples.get(linePattern)){
									writer_debug.append("\ncount_collected:"+count_collected
														+" map_linePattern_MINallowedSamples.get(linePattern):"+map_linePattern_MINallowedSamples.get(linePattern));
									writer_debug.flush();
									break;
								}
							}
							map_already_.put(sample, "");
						}
					}
					
				}//END for(String linePattern:map_linePattern_MINallowedSamples.keySet()){
				cnt++;
			}
				
				writer_debug.append("\n map_linePattern_Count_for_DEBUG:"+map_linePattern_Count_for_DEBUG+" <- reading file"
									+"\n map_isAlready_done_linePattern:"+map_isAlready_done_linePattern);
				writer_debug.flush();
				map_linePattern_Count_for_DEBUG=new TreeMap<String, Integer>();
				writer.close();
				
				
				for(String linePattern_keyword:  map_linepattern_SeqNLine.keySet()){
					writer_debug.append("\n linePatternNkeyword:"+linePattern_keyword+"->size()->"+map_linepattern_SeqNLine.get(linePattern_keyword).size());
					writer_debug.flush();
					
				}
				
				if(Flag==2){
				
					// map_currKeyword_eachLine_File
//					TreeMap<Integer,String> map_currKeyword_eachLine_File=
//							readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//							 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
//									 															 outputFolderName+"filtered_on_"+keyword[cnt]+".txt" , 
//																							 	 -1, 
//																								 -1,
//																								 " map_currKeyword_eachLine_File ", //debug_label
//																								 false //isPrintSOP
//																								 );
//					TreeMap<Integer,Integer> map_isAlready_picked_sample=new TreeMap<Integer,Integer>();
//					int size_of_filtered_curr_keywords_setLines=map_currKeyword_eachLine_File.size();
//					int so_far_picked_samples_count=0;
//					TreeMap<String,Integer> map_noMore_pick_this=new TreeMap<String, Integer>();
//					FileWriter writer_sample=new FileWriter(new File(outputFolderName+"filtered_on_"+keyword[cnt]+"_and_sample_"+n_samples+".txt" ) );
//					int cnt_max_reach_exit=0;
//					
//					
//					for(String curr_linePattern:map_linepattern_SeqNLine.keySet()){
//						writer_debug.append(  "\n size: "+map_linepattern_SeqNLine.get(curr_linePattern).size()+" of "+curr_linePattern+ " map map_linepattern_SeqNLine");
//						writer_debug.flush();
//					}
//					
//					// iterate each linePattern and get minimum 
//					for(String curr_linePattern:map_linepattern_SeqNLine.keySet()){
//						
//						int min_allowed =map_linePattern_MINallowedSamples.get(curr_linePattern);
//						int cnt22=0; int offset =-1;
//						//
//						TreeMap<Integer,String> map_curr_map_linepattern_SeqNLine=map_linepattern_SeqNLine .get(curr_linePattern);
//						
//						int size_map_curr_map_linepattern_SeqNLine=map_curr_map_linepattern_SeqNLine.size();
//						//set offset
//						if(size_map_curr_map_linepattern_SeqNLine<=min_allowed)
//							offset=size_map_curr_map_linepattern_SeqNLine;
//						else
//							offset=min_allowed;
//						
//						// 
//						for (int seq:map_linepattern_SeqNLine.get(curr_linePattern).keySet() ){
//							int sample=generate_Random_Number_Range.generate_Random_Number_Range(1, size_map_curr_map_linepattern_SeqNLine);
//							//
//							if(cnt22>offset){
//								break;	
//							}
//							
//							writer_sample.append(map_curr_map_linepattern_SeqNLine.get(sample)+"\n");
//							writer_sample.flush();
//							
//							cnt22++;
//						}
//					}
					
					
					// 
					//for(int i:map_currKeyword_eachLine_File.keySet()){
//					while(so_far_picked_samples_count<=n_samples){
//						System.out.println("Entering..");
//
//						int sample=generate_Random_Number_Range.generate_Random_Number_Range(1, size_of_filtered_curr_keywords_setLines);
//						String origLine=map_currKeyword_eachLine_File.get(sample);
//						
//						//
//						if(is_strict_on_min_range){
//							boolean is_no_more=false;
//							//
//							for(String noMore:map_noMore_pick_this.keySet()){
//								if(origLine.toLowerCase().indexOf(noMore)>=0 ){
//									is_no_more=true;
//								}
//							}
//							if(is_no_more){
//								System.out.println("no more true ");
//								continue;
//							}
//						}
//						
//						cnt_max_reach_exit++;
//						
//						if(cnt_max_reach_exit>input_cnt_max_reach_exit) {
//							writer_debug.append("\n breaking as max_reach_exit:");
//							writer_debug.flush();
//							break;}
						
						 //
						
						//
//						if(!map_isAlready_picked_sample.containsKey(sample)){
//							
//							
////							else{
////								map_linePattern_Count.put(currLinepattern_, 1 );
////							}
//							
//							currLinepattern_="";
//							//linePattern present in ths line
//							for(String currLinepattern:map_linePattern_MINallowedSamples.keySet()){
//								currLinepattern_=currLinepattern;
//								if(origLine.toLowerCase().indexOf(currLinepattern)>=0){
//									if(!map_linePattern_Count.containsKey(currLinepattern))
//										map_linePattern_Count.put(currLinepattern, 1);
//									else{
//										int t=map_linePattern_Count.get(currLinepattern)+1;
//										map_linePattern_Count.put(currLinepattern, t);
//									}
//									break;
//								}
//								System.out.println("present origline");
//								
//							}//
//							
//							if(map_linePattern_Count.containsKey(currLinepattern_)){
//								if( map_linePattern_Count.get(currLinepattern_) >= map_linePattern_MAXallowedSamples.get(currLinepattern_)){
//									System.out.println("reached max limit for currLinepattern_->"+currLinepattern_
//														+"--"+map_linePattern_Count
//														+"--"+map_linePattern_Count.get(currLinepattern_)
//														+"--"+map_linePattern_MAXallowedSamples.get(currLinepattern_)
//														+"---"+origLine);
//									
//									map_noMore_pick_this.put(currLinepattern_, 0);
//									int total=0;
//									for(String linePattern:map_linePattern_Count.keySet()){
//										total=total+map_linePattern_Count.get(linePattern);
//									}
//									System.out.println(total+"---"+n_samples);
//									if(total==n_samples){
//										writer_debug.append("\n TOTAL n_samples reached..");
//										writer_debug.flush();
//										break;
//									}
//									continue;
//								}
//							}
//							
//							System.out.println("writng origline");
//							writer_sample.append(origLine+"\n");
//							writer_sample.flush();
//							so_far_picked_samples_count++;
//							
//							//
//							if(so_far_picked_samples_count==n_samples){
//								writer_debug.append("BREAKING..");
//								writer_debug.flush();
//								break;
//							}
//							
//						} //END if(!map_isAlready_picked_sample.containsKey(sample)){
//						else {
//							//already checked
//							//map_isAlready_picked_sample.put(sample,0 );
//						}
//						
//						map_isAlready_picked_sample.put(sample, 0);
//					} // END for(int i:map_currKeyword_eachLine_File.keySet()){
					
//					System.out.println("keyword:"+keyword[cnt]+"---map_linePattern_Count:"+map_linePattern_Count+"----"+map_linePattern_MAXallowedSamples);
//					writer_debug.append("\nkeyword:"+keyword[cnt]+"---map_linePattern_Count:"+map_linePattern_Count
//										+"----"+map_linePattern_MAXallowedSamples+" (sample) "+"---so_far_picked_samples_count:"
//											+so_far_picked_samples_count+"---n_samples:"+n_samples
//												+ "\n");
//					total_so_far_picked_samples_count=total_so_far_picked_samples_count+so_far_picked_samples_count;
//					writer_debug.flush();
//					
//					writer_sample.close();
				}
				cnt++;
			//} // END 			while(cnt<keyword.length){
		
			System.out.println("**Debug file stores all statistics..");
			writer_debug.append("\n total_so_far_picked_samples_count :"+total_so_far_picked_samples_count
								+"\n is_strict_on_min_range:"+is_strict_on_min_range);
			writer_debug.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	// main
	public static void main(String[] args) throws IOException {
		
		String  baseFolder="/Users/lenin/Downloads/#problems/p8/Full Article/samples/";
		String	inputFolderName=baseFolder;
		// 
		if(!new File(baseFolder+"tmp").exists()) new File(baseFolder+"tmp/").mkdir();
		String  outputFolderName=baseFolder+"tmp/";
		String debugFile=baseFolder+"tmp/"+"debug.txt";
		String  keyword_CSV="collapse,ebola,fire,juvenile,kidnap,migrant,mining,road,senior,slavery,swine,traffick";
				//keyword_CSV="collapse";
		int 	N_samples=2000;
		TreeMap<String, Integer> map_linePattern_MINallowedSamples=new TreeMap<String, Integer>();
		map_linePattern_MINallowedSamples.put("thehindu" , 250);
		map_linePattern_MINallowedSamples.put("timesofindia" , 250);
		map_linePattern_MINallowedSamples.put("ndtv" ,250);
		
		TreeMap<String, Integer> map_linePattern_MAXallowedSamples=new TreeMap<String, Integer>();
		map_linePattern_MAXallowedSamples.put("thehindu" , 333);
		map_linePattern_MAXallowedSamples.put("timesofindia" , 333);
		map_linePattern_MAXallowedSamples.put("ndtv" , 334);
		//OBSELETE/ no use
		int Flag=2;
		//OBSELETE/ no use
		int input_cnt_max_reach_exit=1000; //control this value
		boolean is_strict_on_min_range=true;

		//readFile_in_Folder_load_filter_out_lines_on_a_given_keyword_then_sample_N_lines_write2AnotherFile
		readFile_in_Folder_load_filter_out_lines_on_a_given_keyword_then_sample_N_lines_write2AnotherFile(
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
		
		
	}

}
