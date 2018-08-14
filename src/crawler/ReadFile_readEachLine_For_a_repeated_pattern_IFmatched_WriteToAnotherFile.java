package crawler;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.util.Scanner;
import java.util.TreeMap;

// read given file for pattern match on each line
public class ReadFile_readEachLine_For_a_repeated_pattern_IFmatched_WriteToAnotherFile {
	
	// output of method "URLOFthehinduprint_any_year_range" is input to this method
	public static void readFile_readEachLine_For_a_repeated_pattern_IFmatched_WriteToAnotherFile_thehindu_range_2000_2005_only(
												String baseFolder,
												String patternString,
												String InputFile_OR_Folder,
												String OutputFile,
												String OutputFile2,
												String OutputFile3_for_labeling,
												boolean is_Append_out_File,
												int interested_column_to_search_for_repeated_pattern, // starts from 1
												int primary_key_column, //can start from 1
												String filter_allow_on_extracted_output_pattern,
												String filter_NOT_allow_on_extracted_output_pattern,
												boolean is_convert_eachLine_ofInputFile_to_lowerCase,
												String  append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder,
												boolean isSOPprint
												){
		try {
		TreeMap<String, String> mapStartPatternEndPattern=new TreeMap();
		
				boolean is_input_a_folder=new File(InputFile_OR_Folder).isDirectory();
				String [] arr_inputFolder_OR_File=new String[1];
				int total_no_input_files=-1;
				//input is folder
				if(is_input_a_folder){
					arr_inputFolder_OR_File=new File(InputFile_OR_Folder).list();
					total_no_input_files=arr_inputFolder_OR_File.length;
				}
				else{ // input is file
					arr_inputFolder_OR_File[0]=InputFile_OR_Folder;
					total_no_input_files=1;
				}
		
		String s1[] = patternString.split(",");
		int cnt1=0; 
		String orig_start_pattern=s1[0];
		String orig_end_pattern=s1[1];
		
		int cnt2=0;
		//read and process each file
		while(cnt2<total_no_input_files){
			//reuse
			InputFile_OR_Folder=baseFolder+arr_inputFolder_OR_File[cnt2];
			//
			if(InputFile_OR_Folder.indexOf("Store")>=0) {cnt2++;continue;}
			//subdirectory skip
			if(new File(InputFile_OR_Folder).isDirectory() ) {cnt2++;continue;}
			
			//
			//while(cnt1<s1.length){
			//String s2[] =s1[cnt1].split(",");
			//mapStartPatternEndPattern.put(s2[0], s2[1]);
			//cnt1++;
			//}
			
			OutputFile=OutputFile+"."+cnt2+".txt";
			OutputFile2=OutputFile2+"."+cnt2+".txt";
			OutputFile3_for_labeling=OutputFile3_for_labeling+"."+cnt2+".txt";
		
			FileWriter writer=new FileWriter(new File(OutputFile), is_Append_out_File);
			FileWriter writer2=new FileWriter(new File(OutputFile2), is_Append_out_File);
			FileWriter writer3=new FileWriter(new File(OutputFile3_for_labeling), is_Append_out_File);
			LineIterator it = IOUtils.lineIterator(
			new BufferedReader(new FileReader(InputFile_OR_Folder)));
			 
			boolean is_exist_primary_key_column=false;
			String curr_primary_key="";
			boolean is_exit=false;
			String curr_complete_url=""; String a=""; String b="";
			//
			if(primary_key_column>=1)
				is_exist_primary_key_column=true;
		
			//each line
			for (int lineNumber = 0; it.hasNext(); lineNumber++) {
					String line = (String) it.next();
					is_exit=false;
					 
					if(is_convert_eachLine_ofInputFile_to_lowerCase)
						line=line.toLowerCase();
					
					String concLine="";
					int counterPattern=0;
					if(is_exist_primary_key_column)
					curr_primary_key=line.split("!!!")[primary_key_column-1];//starts from 1
					//
					while(is_exit==false){
						int startIndex=line.indexOf(orig_start_pattern);
						int endIndex=line.indexOf(orig_end_pattern, startIndex + orig_start_pattern.length()+2);
						
						//
						if(startIndex>=0 && endIndex>0 && endIndex>startIndex){
							//
							concLine= 	 curr_primary_key+"!!!"
										+line.substring(startIndex, endIndex);
							
							if(concLine.indexOf(filter_NOT_allow_on_extracted_output_pattern)>=0
									){
								line=line.substring(endIndex, line.length());
								continue;
							}
							else{
								if(concLine.indexOf(filter_allow_on_extracted_output_pattern)>=0){
									
									if(concLine.indexOf("hdline")>=0){
										a=concLine.substring(0, 
															 concLine.indexOf("hdline")-2);
									}
									else{
										a=concLine.substring(0, 
												  			 concLine.indexOf("!!!"));	
									}
									System.out.println("concLine:"+concLine);
									System.out.println(concLine.indexOf("stories")+"!!!"+concLine.indexOf(".htm"));
									int startIdx=concLine.indexOf("stories");
									int endIdx=concLine.indexOf(".htm", startIdx);
									//
									if( startIdx <0 || endIdx <0){
										is_exit=true;
										break;
									}
									System.out.println(concLine.length()+"!!!"+startIdx+"!!!"+endIdx);
									if(concLine.length()>=endIdx+ orig_end_pattern.length()){
										b=concLine.substring(startIdx, 
															 endIdx+ orig_end_pattern.length());
									}
									else{
										b=concLine.substring(startIdx, 
												 			 endIdx);
									}
									
									writer.append("\n"+concLine);
									writer.flush();
									String c=(a+"/"+b);
									c=c.replace("////stories", "//stories");
									writer2.append("\n"+c+"!!!"+concLine);
									writer2.flush();
									
									writer3.append("\n"+c+"!!!"+concLine.substring( concLine.indexOf("!!!")+3, 
																					concLine.length()) );
									writer3.flush();
									System.out.println("write.line:"+lineNumber+" start:"+startIndex+" end:"+endIndex+"!!"+concLine);
									
								}
							}
							System.out.println("line:"+lineNumber+" start:"+startIndex+" end:"+endIndex+"!!"+concLine);
							line=line.substring(endIndex, line.length());
							counterPattern++;
						}
						else{
							is_exit=true;
						}
						
					} //end while
					
			 
					
					//if (lineNumber == expectedLineNumber) {
					//return line;
					//}
			}
			writer.close();
			
	cnt2++;
	} // END while(cnt2<total_no_input_files){
	
	} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
	}
}
	// output of method "URLOFthehinduprint_any_year_range" is input to this method
	public static void readFile_readEachLine_For_a_repeated_pattern_IFmatched_WriteToAnotherFile_thehindu_range_2006_2009_only(
												String baseFolder,
												String  patternString,
												String  InputFile_OR_Folder,
												String  OutputFile,
												String  OutputFile2,
												String  OutputFile3_for_labeling,
												boolean is_Append_out_File,
												int 	interested_column_to_search_for_repeated_pattern, // starts from 1
												int 	primary_key_column, //can start from 0
												String  filter_allow_on_extracted_output_pattern,
												String  filter_NOT_allow_on_extracted_output_pattern,
												boolean is_convert_eachLine_ofInputFile_to_lowerCase,
												String  append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder,
												boolean isSOPprint
												){
		try {
		String line =""; 
		TreeMap<String, String> mapStartPatternEndPattern=new TreeMap(); 
		TreeMap<String, String> map_is_this_url_already_wrote_to_out_file=new TreeMap();
		String s1[] = null;
		
		boolean is_input_a_folder=new File(InputFile_OR_Folder).isDirectory();
		String [] arr_inputFolder_OR_File=new String[1];
		int total_no_input_files=-1;
		//input is folder
		if(is_input_a_folder){
			arr_inputFolder_OR_File=new File(InputFile_OR_Folder).list();
			total_no_input_files=arr_inputFolder_OR_File.length;
		}
		else{ // input is file
			arr_inputFolder_OR_File[0]=InputFile_OR_Folder;
			total_no_input_files=1;
		}
		
		if(is_convert_eachLine_ofInputFile_to_lowerCase)
			s1= patternString.toLowerCase().split(",");
		else
			s1= patternString.split(",");
		
		int cnt2=0;
		System.out.println("total files given:"+total_no_input_files);
		//read and process each file
		while(cnt2<total_no_input_files){
		
			//reuse
			InputFile_OR_Folder=baseFolder+arr_inputFolder_OR_File[cnt2];
			System.out.println("curr file processing: "+InputFile_OR_Folder+" "+new File(baseFolder+InputFile_OR_Folder).isDirectory());
			//
			if(InputFile_OR_Folder.indexOf("Store")>=0) {cnt2++; continue;}
			//subdirectory skip
			if(new File(InputFile_OR_Folder).isDirectory() ) {cnt2++;
			System.out.println("continue");
				continue;}
				
				OutputFile2=OutputFile2+"_all_"+cnt2+".txt";
				int cnt1=0;
				String orig_start_pattern=s1[0];
				String orig_end_pattern=s1[1];
				OutputFile=OutputFile+"."+cnt2+".txt";
				OutputFile2=OutputFile2+"."+cnt2+".txt";
				OutputFile3_for_labeling=OutputFile3_for_labeling+"."+cnt2+".txt";
		
			//
			//while(cnt1<s1.length){
			//String s2[] =s1[cnt1].split(",");
			//mapStartPatternEndPattern.put(s2[0], s2[1]);
			//cnt1++;
			//}
		
			FileWriter writer=new FileWriter(new File(OutputFile), is_Append_out_File);
			FileWriter writer2=new FileWriter(new File(OutputFile2), is_Append_out_File);
			FileWriter writer3=new FileWriter(new File(OutputFile3_for_labeling), is_Append_out_File);
			LineIterator it = IOUtils.lineIterator(new BufferedReader(new FileReader(InputFile_OR_Folder)));
			 
			boolean is_exist_primary_key_column=false;
			String curr_primary_key="";
			boolean is_exit=false;
			String curr_complete_url=""; String a=""; String b="";
			boolean is_skip_hdline=true; String [] s2=null;
			//
			if(primary_key_column>=0)
				is_exist_primary_key_column=true;
			String intermed_line="";
			String line_orig="";
			//each line
			for (int lineNumber = 0; it.hasNext(); lineNumber++) {
					line=(String) it.next() +" "+append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder ;
					line_orig=line;
					if(is_convert_eachLine_ofInputFile_to_lowerCase)
						line=line.toLowerCase();
					
					is_exit=false;
				
					if(isSOPprint)
						if(intermed_line.length()>100 )
							System.out.println("intermed_line.100:"+intermed_line.substring(0, 100));
					
					String concLine="";
					int counterPattern=0;
					String s[]=line.split("!!!");
					if(s.length<interested_column_to_search_for_repeated_pattern){
						if(isSOPprint)
							System.out.println("continue.s.length<interested_column_to_search_for_repeated_pattern");
						continue;
					}
					
					if(is_exist_primary_key_column){
						curr_primary_key=s[primary_key_column-1];
					}
					//
					if(interested_column_to_search_for_repeated_pattern>=1){
						intermed_line=s[interested_column_to_search_for_repeated_pattern-1];
					}
					else{
						intermed_line=line;
					}
					
					if(isSOPprint){
					System.out.println("curr_primary_key:"+curr_primary_key);
					System.out.println("intermed_line.len:"+intermed_line.length());
					System.out.println("intermed_line:"+intermed_line);
					}
					//
					while(is_exit==false){
						int startIndex=intermed_line.indexOf(orig_start_pattern);
						int endIndex=intermed_line.indexOf(orig_end_pattern, startIndex + orig_start_pattern.length()+2);
						
						//
						if(startIndex>=0 && endIndex>0 && endIndex>startIndex){
							//
							concLine= 	 curr_primary_key+"!!!"
										+RemoveUnicodeChar.removeUnicodeChar(Crawler.removeTagsFromHTML(
												intermed_line.substring(startIndex, endIndex),false ));
							concLine=concLine.replace("<", "").replace(">", "");// left over
							
							if(isSOPprint)
								System.out.println("concLine:"+concLine);
							
							if(concLine.indexOf(filter_NOT_allow_on_extracted_output_pattern)>=0
									){
								intermed_line=RemoveUnicodeChar.removeUnicodeChar(Crawler.removeTagsFromHTML(
																intermed_line.substring(endIndex, intermed_line.length()),false)) ;
								intermed_line=intermed_line.replace("<", "").replace(">", "");// left over
								
								continue;
							}
							else{
								if(concLine.indexOf(filter_allow_on_extracted_output_pattern)>=0){
									
									if(concLine.indexOf("hdline")>=0
										 ){
										a=concLine.substring(0, 
															 concLine.indexOf("hdline")-2);
									}
									else{
										a=concLine.substring(0, 
												  			 concLine.indexOf("!!!"));	
									}
									if(isSOPprint){
										System.out.println("concLine:"+concLine);
										System.out.println("s,b:"+concLine.indexOf("stories")+"!!!"+concLine.indexOf(".htm"));
									}
		//							int startIdx=concLine.indexOf("stories");
		//							int endIdx=concLine.indexOf(".htm", startIdx);
									//
									int cnt=0;
									//add rest of columns (other than primary and interested column)
									if(interested_column_to_search_for_repeated_pattern>=1){
										//
										while(cnt<s1.length){
											if( cnt==primary_key_column-1
											 || cnt==interested_column_to_search_for_repeated_pattern-1
											 ){
												if(isSOPprint)
													System.out.println("continue..");
												cnt++;
												continue;
											}
											else{
												
												if(isSOPprint)
													System.out.println("concLine..");
												
												concLine=concLine+"!!!"+s1[cnt];
											}
											cnt++;
										}
									}
									
									String c=a;
									//
									if(map_is_this_url_already_wrote_to_out_file.containsKey(c)){
										writer.append("\n"+concLine);
										writer.flush();
										//this has all
										writer2.append("\n"+c+"!!!"+concLine.replace(".ece", ".ece!!!"));
										writer2.flush();
										
										writer3.append("\n"+concLine.substring( concLine.indexOf("!!!")+3, 
																				concLine.length())
																				.replace("<a href=\"", "")
																				
																				);
										writer3.flush();
									}
									
									map_is_this_url_already_wrote_to_out_file.put(c, "dummy");
									if(isSOPprint)
										System.out.println("write.line:"+lineNumber+" start:"+startIndex+" end:"+endIndex+"!!"+concLine);
									
								}
							}
							
							if(isSOPprint)
									System.out.println("line:"+lineNumber+" start:"+startIndex+" end:"+endIndex+"!!"+concLine
											+ "!!!line.length():"+line.length());
							
							if(endIndex<line.length()){
								line=line.substring(endIndex, line.length());
							}
							else{
								if(isSOPprint)
									System.out.println("break:endIndex>line.length() ");
								is_exit=true;
							}
							
							
							counterPattern++;
							if(isSOPprint)
								System.out.println("success for curr line");
						}
						else{
							if(isSOPprint)
								System.out.println("exiting..lineNumber:"+lineNumber+" start:"+startIndex+" end:"+endIndex);
							is_exit=true;
						}
						
					} //end while
			
					//if (lineNumber == expectedLineNumber) {
					//return line;
					//}
			}
			writer.close();
			
	cnt2++;
	} // END while(cnt2<total_no_input_files){
	
	} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
	}
}
	//Patterns from HTML or JSON or XML style files can be done in this  
	//readFile_readEachLine_For_a_repeated_pattern_IFmatched_THEN_split_each_found_pattern_WriteToAnotherFile
	//NOT yet tested
	// caution: if input is a folder, then  output will have to be tested (NOT YET TESTED ) <- read call before FOR loop.
	public static TreeMap<String, String> readFile_readEachLine_For_a_repeated_pattern_IFmatched_THEN_split_each_found_pattern_WriteToAnotherFile
																		(
																				String baseFolder,
																				String patternString,
																				String InputFile_OR_Folder,
																				String OutputFile,
																				boolean is_Append_out_File,
																				int interested_column_to_search_for_repeated_pattern, // starts from 0
																				int primary_key_column, //can start from 0
																				String filter_allow_on_extracted_output_pattern,
																				String filter_NOT_allow_on_extracted_output_pattern,
																				String eachLine_to_replace,
																				String eachLine_to_replace_with,
																				String debugFile,
																				String CSV_style_pattern_to_be_used_on_found_pattern_to_split_further,
																				String OutputFile_for_CSV_style_Split,
																				boolean is_append_OutputFile_for_CSV_style_Split,
																				boolean is_convert_eachLine_ofInputFile_to_lowerCase,
																				String  append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder,
																				boolean isSOPprint
																		){
		TreeMap<String, String> mapOut=new TreeMap<String, String>();
		TreeMap<Integer, String> mapOutFrom=new TreeMap<Integer, String>();
		try{
			boolean is_input_a_folder=new File(InputFile_OR_Folder).isDirectory();
			String [] arr_inputFolder_OR_File=new String[1];
			int total_no_input_files=-1;
			//input is folder
			if(is_input_a_folder){
				arr_inputFolder_OR_File=new File(InputFile_OR_Folder).list();
				total_no_input_files=arr_inputFolder_OR_File.length;
			}
			else{ // input is file
				arr_inputFolder_OR_File[0]=InputFile_OR_Folder;
				total_no_input_files=1;
			}
			
			int cnt2=0;
			//read and process each file
			while(cnt2<total_no_input_files){
				//reuse
				InputFile_OR_Folder=baseFolder+arr_inputFolder_OR_File[cnt2];
				//
				if(InputFile_OR_Folder.indexOf("Store")>=0) {cnt2++;continue;}
				//subdirectory skip
				if(new File(InputFile_OR_Folder).isDirectory() ) {cnt2++;continue;}
				
				OutputFile=OutputFile+"."+cnt2+".txt";
			
			
				FileWriter writer=new FileWriter(new File(OutputFile_for_CSV_style_Split));
				// caution: if input is a folde, then  output will have to be tested (NOT YET TESTED )
				mapOutFrom = readFile_readEachLine_For_a_repeated_pattern_IFmatched_WriteToAnotherFile(
						      baseFolder,
							  patternString,
							  InputFile_OR_Folder,
							  OutputFile,
							  is_Append_out_File,
							  interested_column_to_search_for_repeated_pattern, // starts from 0
							  primary_key_column, //can start from 0
							  filter_allow_on_extracted_output_pattern,
							  filter_NOT_allow_on_extracted_output_pattern,
							  eachLine_to_replace,
							  eachLine_to_replace_with,
							  debugFile,
							  is_convert_eachLine_ofInputFile_to_lowerCase,
							  append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder,
							  isSOPprint
							);
				//
				for(int i:mapOutFrom.keySet()){
					String [] s=
					mapOutFrom.get(i).split(CSV_style_pattern_to_be_used_on_found_pattern_to_split_further);
					int cnt=0;
					
					while(cnt<s.length){
						writer.append("lineno:"+i+"!!!"+ s[cnt]+"\n");
						writer.flush();
						mapOut.put(i +"!!!"+cnt, s[cnt]);
						cnt++;
					}
				}//end of for
				
				
				cnt2++;
			}// while(cnt2<total_no_input_files){
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	
	// read each line of a file to match for a pattern
	// Pattern String = <start_string,end_string> 
	// Example pattern string: "<A HREF,>"	
	public static TreeMap<Integer, String> readFile_readEachLine_For_a_repeated_pattern_IFmatched_WriteToAnotherFile(
																	    String baseFolder,
																		String patternString,
																		String InputFile_OR_Folder,
																		String OutputFile,
																		boolean is_Append_out_File,
																		int interested_column_to_search_for_repeated_pattern, // starts from 0
																		int primary_key_column, //can start from 0
																		String filter_allow_on_extracted_output_pattern,
																		String filter_NOT_allow_on_extracted_output_pattern,
																		String eachLine_to_replace,
																		String eachLine_to_replace_with,
																		String debugFile,
																		boolean is_convert_eachLine_ofInputFile_to_lowerCase,
																		String  append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder,
																		boolean isSOPprint
																		){
		TreeMap<Integer, String> mapOut=new TreeMap<Integer, String>();
		try {
			
			TreeMap<String, String> mapStartPatternEndPattern=new TreeMap(); 
			TreeMap<String, String> isDuplicateMap=new TreeMap();
			boolean is_input_a_folder=new File(InputFile_OR_Folder).isDirectory();
			String [] arr_inputFolder_OR_File=null;
			int total_no_input_files=-1;
			//input is folder
			if(is_input_a_folder){
				arr_inputFolder_OR_File=new File(InputFile_OR_Folder).list();
				total_no_input_files=arr_inputFolder_OR_File.length;
			}
			else{ // input is file
				arr_inputFolder_OR_File[0]=InputFile_OR_Folder;
				total_no_input_files=1;
			}
			
			
			String s1[] = null;
			//
			if(is_convert_eachLine_ofInputFile_to_lowerCase)
				s1=patternString.toLowerCase().split(",");
			else
				s1=patternString.split(",");
			
			
			int cnt2=0;
			//read and process each file
			while(cnt2<total_no_input_files){
				//reuse
				InputFile_OR_Folder=baseFolder+arr_inputFolder_OR_File[cnt2];
				System.out.println("Curr Processing File:"+InputFile_OR_Folder);
				
				//
				if(InputFile_OR_Folder.indexOf("Store")>=0) {cnt2++;continue;}
				//subdirectory skip
				if(new File(InputFile_OR_Folder).isDirectory() ) {cnt2++;continue;}
				
				OutputFile=InputFile_OR_Folder+"_output_."+cnt2+".txt"; 	
			
				int cnt1=0;
				String orig_start_pattern=s1[0];
				String orig_end_pattern=s1[1];
				
				//
	//			while(cnt1<s1.length){
	//				String s2[] =s1[cnt1].split(",");
	//				mapStartPatternEndPattern.put(s2[0], s2[1]);
	//				cnt1++;
	//			}
				
				 FileWriter writer=new FileWriter(new File(OutputFile), is_Append_out_File);
				 FileWriter writerDebug=new FileWriter(new File(debugFile), is_Append_out_File);
				 LineIterator it = IOUtils.lineIterator(new BufferedReader(new FileReader(InputFile_OR_Folder)));
				 boolean is_exist_primary_key_column=false;
				 String curr_primary_key="";
				 boolean is_exit=false; int lineCount=0;
				 //
				 if(primary_key_column>=0)
					 is_exist_primary_key_column=true;
					 
				 	 //each line
					 for (int lineNumber = 0; it.hasNext(); lineNumber++) {
						lineCount++;
					    String line = (String) it.next()+append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder;
					    line=line.replace(eachLine_to_replace, eachLine_to_replace_with);
					    is_exit=false;
					    //lower case
					    if(is_convert_eachLine_ofInputFile_to_lowerCase)
					    	line=line.toLowerCase();
					    
					    String concLine="";
					    int counterPattern=0;
					    if(is_exist_primary_key_column){
					    	curr_primary_key=line.split("!!!")[primary_key_column];
					    	if(isDuplicateMap.containsKey(curr_primary_key)){
					    		writerDebug.append("\n duplicate:"+curr_primary_key);
					    		writerDebug.flush();
					    	}
					    	
					    }
					    
					    //
					    while(is_exit==false){
					    	int startIndex=line.indexOf(orig_start_pattern);
					    	int endIndex=line.indexOf(orig_end_pattern, startIndex + orig_start_pattern.length()+2);
					    	if(isSOPprint)
					    		System.out.println("line:"+lineNumber+" start:"+startIndex+" end:"+endIndex);
					    	//
					    	if(startIndex>=0 && endIndex>0 && endIndex>startIndex){
					    		//
					    		concLine= 	  curr_primary_key+"!!!"
					    					 +RemoveUnicodeChar.removeUnicodeChar(Crawler.removeTagsFromHTML(
					    							 							line.substring(startIndex, endIndex),false));
					    		concLine=concLine.replace("<", "").replace(">", "");// left over 
					    		
					    		if(concLine.indexOf(filter_NOT_allow_on_extracted_output_pattern)>=0){
					    			line=line.substring(endIndex, line.length());
					    			writerDebug.append("\n NOT allow match:"+concLine);
				    				writerDebug.flush();
					    			continue;
					    		}
					    		else{
					    			if(concLine.indexOf(filter_allow_on_extracted_output_pattern)>=0){
						    			writer.append("\n"+concLine);
						    			writer.flush();
						    			mapOut.put(lineNumber, concLine);
					    			}
					    			else{
					    				writerDebug.append("\n not match:"+concLine);
					    				writerDebug.flush();
					    			}
					    		}
					    		
						    	line=line.substring(endIndex, line.length());
					    		counterPattern++;
					    	}
					    	else{
					    		if(isSOPprint)
						    		System.out.println("exiting.."+" start:"+startIndex+" end:"+endIndex
						    				+" start:"+orig_start_pattern+" end:"+orig_end_pattern);
					    		writerDebug.append("\n 1.exiting.."+" start:"+startIndex+" end:"+endIndex
								    				+"start:"+orig_start_pattern+" end:"+orig_end_pattern
								    				+"\n 2.prime:" +primary_key_column
								    				+"\n 3.line:"
								    				);
					    		writerDebug.flush();
					    		
					    		is_exit=true;
					    	}
	
					    } //end while
					    
					    if(isSOPprint)
					    	System.out.println("matched line:"+concLine);
					 
					    
	//				    if (lineNumber == expectedLineNumber) {
	//				        return line;
	//				    }
					    
					    isDuplicateMap.put(curr_primary_key, "dummy");
					    
					 }
					 
					 if(isSOPprint)
						 System.out.println("lineCount:"+lineCount);
					 
					 writer.close();
					 
				 cnt2++;
			} // END while(cnt2<total_no_input_files){
				 
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mapOut;
	}
	//
	public static void readFileSearchForAPatternInSpecificLineOrder(String inputFolder, boolean isSOPprint)  {
		File directoryF = new File(inputFolder);
		try {
			String line = (String) FileUtils.readLines(directoryF).get(100);
			if(isSOPprint)
				System.out.println(""+line);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	//
	public static void r(boolean isSOPprint){
		try {
			 String text =
				        "Line1 blah blah\n" +
				        "Line2 more blah blah\n" +
				        "Line3 let's try something new \r\n" +
				        "Line4 meh\n" + 
				        "Line5 bleh\n" + 
				        "Line6 bloop\n";
				    //Scanner sc = new Scanner(text).skip("(?:.*\\r?\\n|\\r){4}");
			 		String skipText="Line4";
			 		Scanner sc = new Scanner(text).skip("(?:.*\\r?\\n|\\r){4}");
			 
			 		if(isSOPprint){
				    while (sc.hasNextLine()) {
				        System.out.println("t:"+sc.nextLine());
				    }
			 		}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	// main
	public static void main(String[] args) throws IOException {
		String baseFolder="";
		String InputFile_OR_Folder="";  String debugFile="";
		String OutputFile=""; String OutputFile2=""; String OutputFile3_for_labeling="";
		boolean is_Append_out_File=false;
		String eachLine_to_replace="";
		String eachLine_to_replace_with="";
		String filter_allow_on_extracted_output_pattern="";
		String filter_NOT_allow_on_extracted_output_pattern="";
		String patterString_CSV="";
		boolean is_convert_eachLine_ofInputFile_to_lowerCase=false;
		boolean isSOPprint=true;
		String append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder=">";
		
		//NDTV start,FROM fully crawled articles HTML tagged file TO <URL!!!body> 
		baseFolder="/Users/lenin/Downloads/#crawleroutput/output/to-crawl-articles-maitrayi-ndtv-toi-thehindu/urlonly/ndtv.full.article.crawled/";
		InputFile_OR_Folder=baseFolder+"";
		OutputFile=baseFolder+"ndtv.only.full.article.o1.txt";
		OutputFile2=baseFolder+"ndtv.only.full.article.o2.txt";
		OutputFile3_for_labeling=baseFolder+"ndtv.only.full.article.o3.labeling.txt";
		append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder=">";
		debugFile=baseFolder+"debug.txt";
		filter_allow_on_extracted_output_pattern="a";
		filter_NOT_allow_on_extracted_output_pattern="hhhhhhhhhhhhh";
		patterString_CSV="place_cont,Published:";
		isSOPprint=false;
		//NDTV end
		
		//TOI start, FROM fully crawled articles HTML tagged file TO <URL!!!body>
//		baseFolder="/Users/lenin/Downloads/#crawleroutput/output/to-crawl-articles-maitrayi-ndtv-toi-thehindu/urlonly/toi.full.article.crawled/";
//		InputFile_OR_Folder=baseFolder+"";
//		OutputFile=baseFolder+"toi.only.full.article.0.o1.txt";
//		OutputFile2=baseFolder+"toi.only.full.article.0.o2.txt";
//		OutputFile3_for_labeling=baseFolder+"toi.only.full.article.0.o3.labeling.txt";
//		append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder=">";
//		debugFile=baseFolder+"debug.0.txt";
//		filter_allow_on_extracted_output_pattern=""; // can be ""
//		filter_NOT_allow_on_extracted_output_pattern="hhhhhhhhhhhhh";
//		patterString_CSV ="<div class=Normal>,storyendpath";
//		eachLine_to_replace="\"";
//		eachLine_to_replace_with="";
//		isSOPprint=false;
		// TOI end

		// generic
		readFile_readEachLine_For_a_repeated_pattern_IFmatched_WriteToAnotherFile(
																	baseFolder,
																	patterString_CSV, 
																	InputFile_OR_Folder,
																	OutputFile,
																	is_Append_out_File,
																	1,// 			  (starts zero)
																	0, // primary_key (starts zero)
																	filter_allow_on_extracted_output_pattern,
																	filter_NOT_allow_on_extracted_output_pattern,
																	eachLine_to_replace,
																	eachLine_to_replace_with,
																	debugFile,
																	is_convert_eachLine_ofInputFile_to_lowerCase,
																	append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder,
																	isSOPprint
																	);
		
		//config setting for 2000-2005 thehindu (extracting individual url from main-pages)
//		InputFile_OR_Folder="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/thehindu.backup.success./2000-2005/thehindu.allnewsArticles-thehindu-URL-print-2000-2005_2-opt-option2.txt";
//		OutputFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/thehindu.backup.success./2000-2005/thehindu.allnewsArticles-thehindu-URL-print-2000-2005_2-opt-option2-atom.txt";
//		OutputFile2="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/thehindu.backup.success./2000-2005/thehindu.allnewsArticles-thehindu-URL-print-2000-2005_2-opt-option2-atom2.txt";
//		OutputFile3_for_labeling="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/thehindu.backup.success./2000-2005/thehindu.allnewsArticles-thehindu-URL-print-2000-2005_2-opt-option2-atom3_label.txt";
//		patterString_CSV="<a href,<a"; //<start_string,end_string>
//		filter_allow_on_extracted_output_pattern="stories";
//		filter_NOT_allow_on_extracted_output_pattern="http:";
//		
//		//THEHINDU,FROM fully crawled articles HTML tagged file TO <URL!!!body>
//		// for hindu , output of URLOFthehinduprint_any_year_range is input to this method
//		readFile_readEachLine_For_a_repeated_pattern_IFmatched_WriteToAnotherFile_thehindu_range_2000_2005_only(
//																			baseFolder,
//																			patterString_CSV, 
//																			InputFile_OR_Folder,
//																			OutputFile,
//																			OutputFile2,
//																			OutputFile3_for_labeling,
//																			is_Append_out_File,
//																			1, // (starts from zero)
//																			0, // primary_key (starts from zero)
//																			filter_allow_on_extracted_output_pattern,
//																			filter_NOT_allow_on_extracted_output_pattern,
//																			is_convert_eachLine_ofInputFile_to_lowerCase,
//																			append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder,
//																			isSOPprint
//																			);

		//config setting for 2006-2009 thehindu (extracting individual url from main-pages)
		baseFolder="/Users/lenin/Downloads/#crawleroutput/output/to-crawl-articles-maitrayi-ndtv-toi-thehindu/urlonly/thehindu.full.article.crawled/";
		InputFile_OR_Folder=baseFolder+""; // "thehindu.only.full.article.4.txt";
		OutputFile=baseFolder+"out.thehindu.only.full.article.4_type1.txt";
		OutputFile2=baseFolder+"out.thehindu.only.full.article.4_type2.txt";
		OutputFile3_for_labeling=baseFolder+"out.thehindu.only.full.article.4_type1_labeling.txt";
		
		is_convert_eachLine_ofInputFile_to_lowerCase=false;
		isSOPprint=false;
		filter_allow_on_extracted_output_pattern=".ece";
		filter_NOT_allow_on_extracted_output_pattern="hhhhhhhhhh";
		patterString_CSV="<div class=\"articleLead\",script type"; //pattern for THEHINDU newspaper
		append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder=">";
		
		// for hindu , output of URLOFthehinduprint_any_year_range is input to this method
//		readFile_readEachLine_For_a_repeated_pattern_IFmatched_WriteToAnotherFile_thehindu_range_2006_2009_only(
//																				    baseFolder,
//																					patterString_CSV, 
//																					InputFile_OR_Folder,
//																					OutputFile,
//																					OutputFile2,
//																					OutputFile3_for_labeling,
//																					is_Append_out_File,
//																					2, // interested_column_to_search_for_repeated_pattern (starts from 1)
//																					1, // primary_key (starts from 1)
//																					filter_allow_on_extracted_output_pattern,
//																					filter_NOT_allow_on_extracted_output_pattern,
//																					is_convert_eachLine_ofInputFile_to_lowerCase,
//																					append_suffix_to_eachLine_of_processing_file_of_InputFile_OR_Folder,
//																					isSOPprint
//																					);
		
		System.out.println(" success. finished..");
		
		
	}

}