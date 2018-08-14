package crawler;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.GetterSetterReflection;

public  class ReadFile_CompleteFile_to_a_Single_String_onefile {
	
	//get_First_And_Last_Key_in_MAP
			private static TreeMap<Integer,String> get_First_And_Last_Key_in_MAP(TreeMap<Integer, String> mapIn) {
				// TODO Auto-generated method stub
				TreeMap<Integer,String> mapOut=new TreeMap();
				String first=""; String last="";
				try{
					int c=0;
					for(int i:mapIn.keySet()){
						c++;
						if(c==1){
							first=mapIn.get(i);
						}
						last=mapIn.get(i);
					}
					
					mapOut.put(1, first);
					mapOut.put(2, last);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return mapOut;
			}
	
 
			public static  String readFile_CompleteFile_to_a_Single_String_2(String pathToFile) throws Exception{
				BufferedReader reader=null; String line="";String out = null;
				try{
				reader = new BufferedReader(new FileReader(pathToFile));
				 

				while ((line = reader.readLine()) != null) {
				out=out+"!#!#"+line;	
				}
				}
				catch(Exception e){
					
				}
				return out;
			}
			
	// 
	public static  String readFile_CompleteFile_to_a_Single_String(String pathToFile) throws Exception{
	    StringBuilder strFile = new StringBuilder();
	    BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
	    char[] buffer = new char[512];
	    int num = 0;
	    while((num = reader.read(buffer)) != -1){
	        String current = String.valueOf(buffer, 0, num);
	        strFile.append(current);
	        buffer = new char[512];
	    }
	    reader.close();
	    return strFile.toString();
	}
	
	// wrapper for readFile_wrapper_for_readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found
	public void readFile_wrapper_for_readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found(
																					String inputFolderName,
																					String outputFolderName,
																					String StagingFolder,
																					boolean is_Append_Output_File,
																					TreeMap<Integer, String> mapKeysToSearchForInEachLine,
																					boolean is_it_NYC_File_where_YYYYMMDD_is_the_key,
																					TreeMap<String, String> mapPastAlreadyProcessedFile,
																					FileWriter writerGlobalDebug200
																					){
		 
		try {
			File file = new File(inputFolderName);
			
			if(file.isDirectory() == false)
			{
				System.out.println(" Given file is not a directory");
			//	return;
			}
			
	        String absolutePathOfInputFile = file.getAbsolutePath();
	        String Parent =  file.getParent(); 
	        
	        String[] Filenames = file.list();

	        System.out.println("absolutePathOfInputFile:"+absolutePathOfInputFile);
	        System.out.println("Parent:"+Parent);
	        System.out.println("inputFolderName:"+inputFolderName);
	         
			
	        //String outputFileName1 = "C:\\Documents and Settings\\lmookiah\\Desktop\\custom work\\output.txt";
	        //FileWriter writer = new FileWriter(outputFileName);

			//While loop to read each file and write into.
			for (int i=0; i < Filenames.length; i++)
			{
				
			  try{
				  //File fileinput = new File(absolutePathOfInputFile +"/"+ Filenames[i]);
				  System.out.println ("File reading::"+absolutePathOfInputFile +"/"+ Filenames[i]);
				  //System.out.println ("full path fileinput:"+fileinput.getAbsoluteFile());
				  
				  ReadFile_CompleteFile_to_a_Single_String_onefile.
				  readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found_2(
						  					inputFolderName+ Filenames[i],  // input file 
						  					outputFolderName + Filenames[i]+"_new.txt",
						  					StagingFolder, //StagingFolder
						  					outputFolderName + Filenames[i]+"debug_only_new.txt",
						  					is_Append_Output_File, //
						  					mapKeysToSearchForInEachLine,
						  					is_it_NYC_File_where_YYYYMMDD_is_the_key,
						  					mapPastAlreadyProcessedFile,
						  					writerGlobalDebug200
						  					);
				  //Write( fileinput , fileinput , absolutePathOfInputFile,writer, i, Filenames[i]);
		
			  }
			  catch(Exception e)
			  {
				  System.out.println("An exception occurred while calling Write method ..!!");	
				  e.printStackTrace();
				  
			  }
			  
			}
	  	  }
		 catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	// get only 30
		public static TreeMap<Integer, String>  for_loop_starting_from_a_index_and_get_30_elements(	
															int startPoint,
															TreeMap<Integer, String> mapRangeDateTimeFilter,
															int size_of_elements_in_each_set
															){
			TreeMap<Integer, String>  mapIndependentRangeOfDates=new TreeMap<Integer, String>();
			int outerCount=0;
			try {
				
				for(int s:mapRangeDateTimeFilter.keySet()){
					 //
					 if(s>=startPoint  &&mapIndependentRangeOfDates.size() < size_of_elements_in_each_set ){
						 mapIndependentRangeOfDates.put(s, mapRangeDateTimeFilter.get(s));
					 }
					 else if(s>=startPoint){
						 System.out.println("breakin...from loop:"+mapIndependentRangeOfDates.size()
								 			+";startPoint:"+startPoint+";s:"+s);
//						 outerCount++;
//						 mapIndependentRangeOfDates.put(outerCount, mapIndependentRangeOfDates);
//						 mapIndependentRangeOfDates=new TreeMap<>();
						 break;
					 }
						//System.out.println(s+"\n");
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return mapIndependentRangeOfDates;
		}
	
	//splitting date range to mutiple set. each set has 30 days.
		// here we filter out only set of months we want (ex: only april and may)
		public static TreeMap<Integer, TreeMap<Integer, String>> SplitGivenDateRange_To_multiple_set_each_set_has_30_days
														(
																String StartDate,
																String EndDate,
																int Number_of_elements_in_each_set,
																FileWriter writerdebugFile
														){
			TreeMap<Integer, TreeMap<Integer,String>> mapOut=new TreeMap();
			try{
				TreeMap<Integer, String> mapRangeDateTime=
				Get_NewsPaper_ArchiveURL.thehindu_print("/Users/lenin/Downloads/dummy.daterange.txt",//outFile, 
														"", //baseURL
														Integer.valueOf(StartDate), 
														Integer.valueOf(EndDate));
				TreeMap<Integer, String> mapRangeDateTimeFilter=new TreeMap();
				
				writerdebugFile.append("\n !!!###Number_of_elements_in_each_set#### "+Number_of_elements_in_each_set+"\n");
				writerdebugFile.flush();
				
				int counter=0;
				for(int i:mapRangeDateTime.keySet()){
					if(mapRangeDateTime.get(i).indexOf("2014/04")>=0 
					   || mapRangeDateTime.get(i).indexOf("2014/05")>=0
					   || mapRangeDateTime.get(i).indexOf("2014/06")>=0
					   || mapRangeDateTime.get(i).indexOf("2014/07")>=0
					   || mapRangeDateTime.get(i).indexOf("2014/08")>=0
					   || mapRangeDateTime.get(i).indexOf("2014/09")>=0
					   || mapRangeDateTime.get(i).indexOf("2014/10")>=0
					   || mapRangeDateTime.get(i).indexOf("2014/11")>=0
					   || mapRangeDateTime.get(i).indexOf("2014/12")>=0
					   )
					if(Find_isGivenString_ValidDate.isValidDate(mapRangeDateTime.get(i), "yyyy/MM/dd")){
						counter++;
						mapRangeDateTimeFilter.put(counter, mapRangeDateTime.get(i).replace("/","-") );
					}
				
				}
				System.out.println("mapRangeDateTime:"+mapRangeDateTime);
				System.out.println("mapRangeDateTimeFilter.size:"+mapRangeDateTimeFilter.values());
				System.out.println("mapRangeDateTimeFilter.size:"+mapRangeDateTimeFilter.size());
				System.out.println("--------------");
				int outerCount=1;
				TreeMap<Integer, String> mapIndependentRangeOfDates=new TreeMap();
				int startPoint=1;
				boolean isTrue=true;
				while(isTrue){
					System.out.println("-------startPoint-------"+startPoint);
					mapIndependentRangeOfDates=for_loop_starting_from_a_index_and_get_30_elements(startPoint, 
																								  mapRangeDateTimeFilter,
																								  Number_of_elements_in_each_set);
					
					if(mapIndependentRangeOfDates.size()<Number_of_elements_in_each_set){
						System.out.println("mapIndependentRangeOfDates.size:(break):"+mapIndependentRangeOfDates.size());
						isTrue=false;
						//break;
					}
					else{
						System.out.println("mapIndependentRangeOfDates.size:"+mapIndependentRangeOfDates.size());
						mapOut.put(outerCount, mapIndependentRangeOfDates);
						outerCount++;
						startPoint++;
					}
					
				}
				
				//check if the string is date
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return mapOut;
		}
		
	
	//SplitGivenSingleFile_to_multiple_files_Each_file_corresponds_to_one_YYYYMMDD
		private static void SplitGivenSingleFile_to_multiple_files_Each_file_corresponds_to_one_YYYYMMDD(
								String inputBigFileForSplitting, String outputStagingFolder, TreeMap<String, String> MAPuniqueYYYYMMDD) {
			// TODO Auto-generated method stub
			try{
				System.out.println("inputBigFileForSplitting:"+inputBigFileForSplitting);
				BufferedReader reader = new BufferedReader(new FileReader(inputBigFileForSplitting));
				String line="";
				for(String yyyymmdd:MAPuniqueYYYYMMDD.keySet()){
					FileWriter writer=new FileWriter(outputStagingFolder+yyyymmdd+".txt");
					reader = new BufferedReader(new FileReader(inputBigFileForSplitting));
					System.out.println("curr out file -> "+outputStagingFolder+yyyymmdd+".txt");
					//
					while ((line = reader.readLine()) != null) {
						String orig_line=line;
						if(line.indexOf("key=")>=0){
							line=line.substring(0, line.indexOf("key="));
						}
						
						
						if(line.indexOf(yyyymmdd) >=0){
							writer.append(orig_line+"\n");
							writer.flush();
						}
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	
	// Splitting 
		public static TreeMap<Integer, TreeMap<Integer, String>>
					splitting_Convert_TimeSlice_OneBigFile_To_30DaysEachFile(String inputBig_Complete_File_With_TimeStamp_EachLine,
																			 String start_date,
																			 String end_date,
																			 int number_of_elements_in_each_set,
																			 FileWriter debugFile
																		     ){
			TreeMap<Integer, TreeMap<Integer, String>> mapOut =new TreeMap();
			try{
				
				mapOut=
						SplitGivenDateRange_To_multiple_set_each_set_has_30_days(start_date,
																	end_date,
																	number_of_elements_in_each_set,
																	debugFile);
				System.out.println("splitting_Convert_TimeSlice_OneBigFile_To_30DaysEachFile.mapOut:"+mapOut.size());
				
			}
			catch(Exception e){
				
			}
			return mapOut;
		}
	
	//output -> <eachline,eachline>
	public static TreeMap<String,String> readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
																		(String inFile,
																		int startline, 
																		int endline,
																		String outFile,
																		boolean is_Append_outFile,
																		boolean is_Write_To_OutputFile
																		) {
		TreeMap<String,String> mapOut = new TreeMap();
		String line = ""; int counter=0;
		FileWriter writer= null;
		System.out.println("readFile_load_Each_Line_of_Generic_File_To_Map_String_String.inFile:"+inFile);
		// Read the mapping to drill-down
		HashMap<String, String> mapDrillDown = new HashMap();
		int newcounter=0;
		try {
			
			if(is_Write_To_OutputFile){
				writer= new FileWriter(new File(outFile), is_Append_outFile);
			}
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			
			
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				System.out.println("line:"+line);
//				if(line.indexOf("http")==-1)
//					continue;
//				
//				line=line.replace("2..r,", "").replace("rss_,", "")
//						.replace(" ", "").replace("!.!href=", "")
//						.replace("!!!", "").replace("\"", "")
//						.replace("'", "")
//						.replace("!.!", "")
//						.replace("“", "")
//						.replace("“", "");
				
				counter++;
				if(endline>0){
					//
					if(counter>=startline && counter<=endline )
					    newcounter++;
					else{
						System.out.println("line no :"+counter+"!!!skip.line:"+line);
						continue;
					}
					}
				else{
					System.out.println("line no :"+counter+"!!!loading...:"+line);
					newcounter++;
				}
				
				mapOut.put(line, line);
			}
			//
			if(is_Write_To_OutputFile){
				//write to output file
				for(String s:mapOut.keySet()){
					writer.append(s+"\n");
					writer.flush();
				}
			}
			
			reader.close();
			//writer.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	
    //readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found
	public static int readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found_2(
																		String inFile,
																		String outFile,
																		String StagingFolder, //
																		String outFile_only_url_to_debug,
																		boolean is_Append_outFile,
																		TreeMap<Integer, String> mapKeysToSearchForInEachLine,
																		boolean is_it_NYC_File_where_YYYYMMDD_is_the_key,
																		TreeMap<String,String> mapPastAlreadyProcessedFile,
																		FileWriter writerGlobalDebug200
																		){
		int offset= 100;
		String newSting="";
		try {
		 
				String line=""; String orig_line="";
				FileWriter writer= new FileWriter(new File(outFile), is_Append_outFile);
				FileWriter writerOnlyURLToDebug= new FileWriter(new File(outFile_only_url_to_debug), is_Append_outFile);
				TreeMap<String,String> mapAlreadyOccured=new TreeMap<String, String>();
				
				//BufferedReader reader = new BufferedReader(new FileReader(inFile));
				int lineNo=0;
				String MM="", DD="",YYYY="";
				String t="";
				
				writerGlobalDebug200.append("\n @@@outFile:"+outFile);
				writerGlobalDebug200.append("\n @@@outFile_only_url_to_debug:"+outFile_only_url_to_debug);
				writerGlobalDebug200.append("\n @@@mapKeysToSearchForInEachLine:"+mapKeysToSearchForInEachLine);
				writerGlobalDebug200.flush();
				int Count_mapAlreadyOccured=0;
				line="";
				TreeMap map_isDuplicateLine=new TreeMap();
				BufferedReader reader100=null; int lineno=0;
				for(String key:mapKeysToSearchForInEachLine.values()){
					key=key.toLowerCase().replace("-", "");
					System.out.println(" new staging file:"+StagingFolder+key+".txt");
					writerGlobalDebug200.append("\nnew staging file:"+StagingFolder+key+".txt");
					writerGlobalDebug200.flush();
					
					try{ 
					reader100=new BufferedReader(new FileReader(StagingFolder+key+".txt" ));
					lineno=0;
					}
					catch(Exception e){
						System.out.println(" *** file not found..: "+StagingFolder+key+".txt");
						writerGlobalDebug200.append("\n *** file not found..: "+StagingFolder+key+".txt");
						writerGlobalDebug200.flush();
						continue;
					}
					
					System.out.println("@@@@@@@@@@@@ start stagefile:"+StagingFolder+key+".txt");
					//
					while( (line=reader100.readLine()) !=null){
						lineno++;
						System.out.println("lno:"+lineno+";Stagefile:"+key);//+"%%%%%%%% line:"+line);
						if(map_isDuplicateLine.containsKey(line)){ //duplicate
							continue;
						}
						writer.append(line+"\n");
						writer.flush();
						map_isDuplicateLine.put(line, "");
					}
					System.out.println("@@@@@@@@@@@@ end stagefile:"+StagingFolder+key+".txt");
					line="";
				}
				writer.close();
				
				// 	read each line of given file
//				while ((line = reader.readLine()) != null) {
//					//System.out.println(s.subSequence(s.length()-2000, s.length()));
//					boolean isFound=false;
//					orig_line="";
//					orig_line=line;
//					String tempKey="";
//	        		if(lineNo%100000==0){
//	        			System.out.println("keys:"+mapKeysToSearchForInEachLine+";in File:"+inFile+";line:"+line);
//	        			System.out.println("mapPastAlreadyProcessedFile:"+mapPastAlreadyProcessedFile);
//	        		}
//					//check if at least one of the given key present in curr line 
//		        	for(String key:mapKeysToSearchForInEachLine.values()){
//		        		key=key.toLowerCase();
//		        		isFound=false;
//		        		if(!is_it_NYC_File_where_YYYYMMDD_is_the_key){ // NOT NYC
//			        		if(line.toLowerCase().indexOf(key)>=0){
//			        			isFound=true;
//			        			System.out.println("-----------------------Found");
//			        			writerOnlyURLToDebug.append("-----------------------1.Found:"+key+"\n");
//			        			writerOnlyURLToDebug.flush();
//			        			break;
//			        		}
//		        		}
//		        		else{ // NYC 
//		        			if(is_it_NYC_File_where_YYYYMMDD_is_the_key){
//								YYYY=key.substring(0,4);
//								MM=key.substring(5,7);
//								DD=key.substring(8,10);
//								System.out.println("------------------NYC-----KEY:"+YYYY+"-"+MM+"-"+DD+";File:"+outFile);
//								tempKey=YYYY+"-"+MM+"-"+DD;
//								if(line.length()<5) 
//									continue;
//								if(orig_line.indexOf("key=")==-1){
//									System.out.println("no key found in URL "+orig_line+"\n");
//									continue;
//								}
//								System.out.println( line.indexOf("key=")+";line:"+line);
//								//get only the url of the NYC current article.
//								line=orig_line.substring(0, orig_line.indexOf("key=")); //before api-key YYYYMMDD appears
//								if(line.toLowerCase().indexOf(YYYY+MM+DD)>=0 &&
//								   line.toLowerCase().indexOf(MM)>=0 &&
//								   line.toLowerCase().indexOf(DD)>=0){
//				        			isFound=true;
//				        			System.out.println("------------------NYC-----Found");
//				        		    t=line+";tempKey->"+tempKey+"-indexof->"
//					        				+line.toLowerCase().indexOf(YYYY+MM+DD)+"-"
//					        				+line.toLowerCase().indexOf(MM)+"-"
//					        				+line.toLowerCase().indexOf(DD)+"-"
//					        				+line.substring(line.toLowerCase().indexOf(YYYY), line.toLowerCase().indexOf(YYYY)+4)+"-"
//					        				+line.substring(line.toLowerCase().indexOf(MM), line.toLowerCase().indexOf(MM)+4)+"-"
//					        				+line.substring(line.toLowerCase().indexOf(DD), line.toLowerCase().indexOf(DD)+4)+"-"
//					        				+"\n";
//				        		    if(mapAlreadyOccured.containsKey(t)){
//				        		    	Count_mapAlreadyOccured++;
//					        			writerOnlyURLToDebug.append(
//					        							 ";********\n****already:"+t
//					        							+";mapKeysToSearchForInEachLine:"+mapKeysToSearchForInEachLine
//					        							+";key from map:"+key
//					        							+";Count_mapAlreadyOccured:"+Count_mapAlreadyOccured
//					        							+"\n"
//					        							);
//					        			writerOnlyURLToDebug.flush();
//					        			isFound=false;
//					        			continue; //this one already wrote, so check on next key
//					        			//return 0; //already ran this
//					        		}
//				        		    else{
//				        		    	writerOnlyURLToDebug.append("-----------------------2.Found:"+key+"\n");
//					        			writerOnlyURLToDebug.flush();
//				        		    	break;
//				        		    }
//				        		}
//								
//							}
//		        		}
//		        	} //for end key
//		        	//output
//		        	//out.println(s.subSequence(s.length()-10000000, s.length()));
//		        	if(isFound==true){
//		        		//
//			        	if(is_it_NYC_File_where_YYYYMMDD_is_the_key){
//			        	
//			         		System.out.println("writing."+line);
//				        	writer.append(orig_line+"\n");
//				        	writer.flush();
//			        		
//			        		writerOnlyURLToDebug.append("true::t:"+t +";outFile:"+outFile+"\n");// only URL WRITTEN
//			        		writerOnlyURLToDebug.flush();
//			        		
//			        		mapAlreadyOccured.put(t, "");
//			        	}
//			        	
//		        	}
//		        	 
//		        	System.out.println("!!! skipping.....");
//				} //each line
	        	System.out.println("end writing");
	        	//writer.flush();
	        	writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	     return 0;
	}

	
	    //readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found
//		public static int readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found(
//																			String inFile,
//																			String outFile,
//																			String outFile_only_url_to_debug,
//																			boolean is_Append_outFile,
//																			TreeMap<Integer, String> mapKeysToSearchForInEachLine,
//																			boolean is_it_NYC_File_where_YYYYMMDD_is_the_key,
//																			TreeMap<String,String> mapPastAlreadyProcessedFile,
//																			FileWriter writerGlobalDebug200
//																			){
//			int offset= 100;
//			String newSting="";
//			try {
//			 
//					String line=""; String orig_line="";
//					FileWriter writer= new FileWriter(new File(outFile), is_Append_outFile);
//					FileWriter writerOnlyURLToDebug= new FileWriter(new File(outFile_only_url_to_debug), is_Append_outFile);
//					TreeMap<String,String> mapAlreadyOccured=new TreeMap<String, String>();
//					
//					BufferedReader reader = new BufferedReader(new FileReader(inFile));
//					int lineNo=0;
//					String MM="", DD="",YYYY="";
//					String t="";
//					
//					writerGlobalDebug200.append("\n @@@outFile:"+outFile);
//					writerGlobalDebug200.append("\n @@@outFile_only_url_to_debug:"+outFile_only_url_to_debug);
//					writerGlobalDebug200.append("\n @@@mapKeysToSearchForInEachLine:"+mapKeysToSearchForInEachLine);
//					writerGlobalDebug200.flush();
//					int Count_mapAlreadyOccured=0;
//					// 	read each line of given file
//					while ((line = reader.readLine()) != null) {
//						//System.out.println(s.subSequence(s.length()-2000, s.length()));
//						boolean isFound=false;
//						orig_line="";
//						orig_line=line;
//						String tempKey="";
//		        		if(lineNo%100000==0){
//		        			System.out.println("keys:"+mapKeysToSearchForInEachLine+";in File:"+inFile+";line:"+line);
//		        			System.out.println("mapPastAlreadyProcessedFile:"+mapPastAlreadyProcessedFile);
//		        		}
//						//check if at least one of the given key present in curr line 
//			        	for(String key:mapKeysToSearchForInEachLine.values()){
//			        		key=key.toLowerCase();
//			        		isFound=false;
//			        		if(!is_it_NYC_File_where_YYYYMMDD_is_the_key){ // NOT NYC
//				        		if(line.toLowerCase().indexOf(key)>=0){
//				        			isFound=true;
//				        			System.out.println("-----------------------Found");
//				        			writerOnlyURLToDebug.append("-----------------------1.Found:"+key+"\n");
//				        			writerOnlyURLToDebug.flush();
//				        			break;
//				        		}
//			        		}
//			        		else{ // NYC 
//			        			if(is_it_NYC_File_where_YYYYMMDD_is_the_key){
//									YYYY=key.substring(0,4);
//									MM=key.substring(5,7);
//									DD=key.substring(8,10);
//									System.out.println("------------------NYC-----KEY:"+YYYY+"-"+MM+"-"+DD+";File:"+outFile);
//									tempKey=YYYY+"-"+MM+"-"+DD;
//									if(line.length()<5) 
//										continue;
//									if(orig_line.indexOf("key=")==-1){
//										System.out.println("no key found in URL "+orig_line+"\n");
//										continue;
//									}
//									System.out.println( line.indexOf("key=")+";line:"+line);
//									//get only the url of the NYC current article.
//									line=orig_line.substring(0, orig_line.indexOf("key=")); //before api-key YYYYMMDD appears
//									if(line.toLowerCase().indexOf(YYYY+MM+DD)>=0 &&
//									   line.toLowerCase().indexOf(MM)>=0 &&
//									   line.toLowerCase().indexOf(DD)>=0){
//					        			isFound=true;
//					        			System.out.println("------------------NYC-----Found");
//					        		    t=line+";tempKey->"+tempKey+"-indexof->"
//						        				+line.toLowerCase().indexOf(YYYY+MM+DD)+"-"
//						        				+line.toLowerCase().indexOf(MM)+"-"
//						        				+line.toLowerCase().indexOf(DD)+"-"
//						        				+line.substring(line.toLowerCase().indexOf(YYYY), line.toLowerCase().indexOf(YYYY)+4)+"-"
//						        				+line.substring(line.toLowerCase().indexOf(MM), line.toLowerCase().indexOf(MM)+4)+"-"
//						        				+line.substring(line.toLowerCase().indexOf(DD), line.toLowerCase().indexOf(DD)+4)+"-"
//						        				+"\n";
//					        		    if(mapAlreadyOccured.containsKey(t)){
//					        		    	Count_mapAlreadyOccured++;
//						        			writerOnlyURLToDebug.append(
//						        							 ";********\n****already:"+t
//						        							+";mapKeysToSearchForInEachLine:"+mapKeysToSearchForInEachLine
//						        							+";key from map:"+key
//						        							+";Count_mapAlreadyOccured:"+Count_mapAlreadyOccured
//						        							+"\n"
//						        							);
//						        			writerOnlyURLToDebug.flush();
//						        			isFound=false;
//						        			continue; //this one already wrote, so check on next key
//						        			//return 0; //already ran this
//						        		}
//					        		    else{
//					        		    	writerOnlyURLToDebug.append("-----------------------2.Found:"+key+"\n");
//						        			writerOnlyURLToDebug.flush();
//					        		    	break;
//					        		    }
//					        		}
//									
//								}
//			        		}
//			        	} //for end key
//			        	//output
//			        	//out.println(s.subSequence(s.length()-10000000, s.length()));
//			        	if(isFound==true){
//			        		//
//				        	if(is_it_NYC_File_where_YYYYMMDD_is_the_key){
//				        	
//				         		System.out.println("writing."+line);
//					        	writer.append(orig_line+"\n");
//					        	writer.flush();
//				        		
//				        		writerOnlyURLToDebug.append("true::t:"+t +";outFile:"+outFile+"\n");// only URL WRITTEN
//				        		writerOnlyURLToDebug.flush();
//				        		
//				        		mapAlreadyOccured.put(t, "");
//				        	}
//				        	
//			        	}
//			        	 
//			        	System.out.println("!!! skipping.....");
//					} //each line
//		        	System.out.println("end writing");
//		        	//writer.flush();
//		        	writer.close();
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//		     return 0;
//		}
//	
	//readFile_Find_A_Pattern_Substring_AND_return_From_Pattern_To_End_of_File
	public static int readFile_Find_A_Pattern_Substring_AND_return_From_Pattern_To_End_of_File(
																		String inFile,
																		String outFile,
																		String pattern
																		){
		int offset= 100;
		String newSting="";
		try {
		
				String s=ReadFile_CompleteFile_to_a_Single_String_onefile
						.readFile_CompleteFile_to_a_Single_String(inFile);
	        	//System.out.println(s.subSequence(s.length()-2000, s.length()));
	        	PrintWriter out = new PrintWriter(outFile);
	        	 
//	        	CharSequence newSting=
//	        				s.substring( s.toLowerCase().indexOf("18 jun") , s.length());
	        	
	        	int idx= s.toLowerCase().indexOf(pattern);
	        	if(idx==-1){
	        		System.out.println("**NOT found pattern:"+inFile);
	        		return -1;
	        	}
	        	else{
	        		
	        	}
//	        	
//	        	if(idx-offset>=0){ 
//	        		offset=1000;
//	        	}
//	        	else if(idx-5000>=0 ){
//	        		offset=2500; 
//	        	}
//	        	else if(idx-2000>=0 ){
//	        		offset=1000; 
//	        	}
//	        	else if(idx-1000>=0 ){
//	        		offset=500; 
//	        	}
//	        	else if(idx-500>=0 ){
//	        		offset=200; 
//	        	}
//	        	else{
//	        		offset=0;
//	        	}
	        	
	        	System.out.println("idx:"+idx +";offset:"+offset+";s:"+s);
	        	
	        	newSting=s.substring(idx -offset , s.length());
	        	
	        	System.out.println("**found pattern:idx:"+idx+" offset:"+offset+" for "+inFile);
	        	
	        	System.out.println("writing.");
	        	//output
	        	//out.println(s.subSequence(s.length()-10000000, s.length()));
	        	out.println(newSting);
	        	System.out.println("end writing");
	        	out.flush();
	        	out.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	     return 0;
	}
	// wrapper for readFile_wrapper_for_readFile_Find_A_Pattern_Substring_AND_return_From_Pattern_To_End_of_File
	public void readFile_wrapper_for_readFile_Find_A_Pattern_Substring_AND_return_From_Pattern_To_End_of_File(
																					String inputFolderName,
																					String outputFolderName,
																					String pattern_DD_MMM_YYYY
																					){
		pattern_DD_MMM_YYYY=pattern_DD_MMM_YYYY.toLowerCase();
		try {
			File file = new File(inputFolderName);
			
			if(file.isDirectory() == false)
			{
				System.out.println(" Given file is not a directory");
			//	return;
			}
			
	        String absolutePathOfInputFile = file.getAbsolutePath();
	        String Parent =  file.getParent(); 
	        
	        String[] Filenames = file.list();

	        System.out.println("absolutePathOfInputFile:"+absolutePathOfInputFile);
	        System.out.println("Parent:"+Parent);
	        System.out.println("inputFolderName:"+inputFolderName);
	        System.out.println("pattern_DD_MMM_YYYY:"+pattern_DD_MMM_YYYY);
			
	        //String outputFileName1 = "C:\\Documents and Settings\\lmookiah\\Desktop\\custom work\\output.txt";
	        //FileWriter writer = new FileWriter(outputFileName);

			//While loop to read each file and write into.
			for (int i=0; i < Filenames.length; i++)
			{
				
			  try{
				  //File fileinput = new File(absolutePathOfInputFile +"/"+ Filenames[i]);
				  System.out.println ("File reading::"+absolutePathOfInputFile +"/"+ Filenames[i]);
				  //System.out.println ("full path fileinput:"+fileinput.getAbsoluteFile());
				  
				  ReadFile_CompleteFile_to_a_Single_String_onefile.
				  readFile_Find_A_Pattern_Substring_AND_return_From_Pattern_To_End_of_File(
						  					inputFolderName+ Filenames[i],  // input file 
						  					outputFolderName + Filenames[i]+"_new.txt",
						  					pattern_DD_MMM_YYYY);
				  //Write( fileinput , fileinput , absolutePathOfInputFile,writer, i, Filenames[i]);
		
			  }
			  catch(Exception e)
			  {
				  System.out.println("An exception occurred while calling Write method ..!!");	
				  e.printStackTrace();
				  
			  }
			  
			}
	  	  }
		 catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	// main
	public static void main(String[] args) throws IOException {
        System.out.println("/sports/".indexOf("/sports"));
        String inFile="/Users/lenin/Downloads/Feeds-29-Jun-SplitBase/8(20.0-200.0)/EMM Alert India";
        String outFile="/Users/lenin/Downloads/Feeds-29-Jun-SplitBase/8(20.0-200.0)/out.txt";
        long t0 = System.nanoTime();
        try {
        	
        	System.out.println("1");
//        	CharSequence newSting=
//        				s.substring( s.toLowerCase().indexOf("18 jun") , s.length());
 
        	//
//        	readFile_Find_A_Pattern_Substring_AND_return_From_Pattern_To_End_of_File
//        	("/Users/lenin/Downloads/Feeds-29-Jun-SplitBase/dummy/",
//        	 "/Users/lenin/Downloads/Feeds-29-Jun-SplitBase/dummyout/",
//        	 generateDate.generate_Date_Pattern_for_RSS_XML_Parser_for_date_DD_MMM_YYYY("04 mar 2015")
//        	 );
        	
        	// 
//          	readFile_Find_A_Pattern_Substring_AND_return_From_Pattern_To_End_of_File
//        	("/Users/lenin/Downloads/crawleroutput/output/all think tank from world  (aug 20 2015)/out_google_search-list-of-all-think-tank-from-world-from-wiki-list_2.txt.only.link.txt",
//        	 "/Users/lenin/Downloads/crawleroutput/output/all think tank from world  (aug 20 2015)/out_google_search-list-of-all-think-tank-from-world-from-wiki-list_2.txt.only.link_clean.txt",
//        	   "="
//        	 );
        	
//        	System.out.println(
//        						GenerateDate.
//        						generate_Date_Pattern_for_RSS_XML_Parser_for_date_DD_MMM_YYYY("21 dec 2014")
//        					  );
        	
        	
        	TreeMap<Integer, String> mapKeysToSearchForInEachLine=new TreeMap<Integer, String>();
//        	mapKeysToSearchForInEachLine.put(1, "20140501");
//        	mapKeysToSearchForInEachLine.put(2, "20140502");
        	//TreeMap<String, String> mapPastAlreadyProcessedFile=new TreeMap<String, String>();
        	//mapPastAlreadyProcessedFile.put("", "");
        	String debugFile="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/output/debug.txt";
        	FileWriter writerGlobalDebug200=new FileWriter(debugFile);
        	
        	
        	
        	//either give folder (or) a file.
    		String inputFileOfNYT="/Users/lenin/OneDrive/Data.temp/NYT-Apr-May/NYT_out_crawled.SET.2.OnlyMay.txt";
    		String inputFolder_for_FilesOfNYT="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/";
    			   inputFolder_for_FilesOfNYT="/Users/lenin/OneDrive/Data.temp/dummy/";
    		String outputFolder="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/output/";
    		String pastAlreadyProcessedFile="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/output/NYT.AlreadyProcessed-All.txt";
    			   inputFolder_for_FilesOfNYT="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/All.Merged/";
    			   
    		String inputFolder_for_Staging_split_files="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/StagingSplitFiles/";
    		//int startline=0; int endLine=1000000000;
    		 
    		
    		File f=new File(inputFolder_for_FilesOfNYT);
    		String [] filesOfgivenFolder=f.list();
    		System.out.println("given files array:"+filesOfgivenFolder);
    		int cnt=0;
    		FileWriter writerAlreadyProcessedFile=null;
    		if(!new File(pastAlreadyProcessedFile).exists()){
    			try {
    				new File(pastAlreadyProcessedFile).createNewFile();
    				writerAlreadyProcessedFile=new FileWriter(new File(pastAlreadyProcessedFile));
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    		else{
    			try {
    				writerAlreadyProcessedFile=new FileWriter(new File(pastAlreadyProcessedFile),true); //append
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    		
    		// load past already run file 
    		TreeMap<String, String> mapPastAlreadyProcessedFile=
    		ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
    		readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
    																pastAlreadyProcessedFile, 
    																-1, //startline, 
    																-1, //endline,
    																"",//outFile, 
    																false, //is_Append_outFile,
    																false, //is_Write_To_OutputFile
    																"" //debug_label
    																, -1 // token for to be used as primary ky
    																,true //boolean isSOPprint
    																);
    		debugFile="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/output/debug.txt";
    		boolean boolean_isrunningSplitFiles=false;
    		String mainType="convertNYTnewsARTICLEtoGBADgraph";
    		try{
    			
    		// NYT news article to GBAD graph
    		if(mainType.equalsIgnoreCase("convertNYTnewsARTICLEtoGBADgraph")){
    			 
    			FileWriter writerDebug200=new FileWriter(new File(debugFile), true);
    			String Flag="SplitFilesForSlidingWindow";// {"SplitFilesForSlidingWindow","CreateGBADFile"}
    			
    			TreeMap<Integer, TreeMap<Integer, String>> mapOut=new TreeMap();
    			// 
    			mapOut= splitting_Convert_TimeSlice_OneBigFile_To_30DaysEachFile("",
    																	 "2014"
    																	,"2014"
    																	,30 //number_of_elements_in_each_set
    																	,writerDebug200
    																	);
    			TreeMap<String, String> MAPuniqueYYYYMMDD=new TreeMap();
    			// 
    			for(int i:mapOut.keySet()){
    					System.out.println("mapOut.size:"+mapOut.size()+";i:"+i+";mapOut:"+mapOut);
    					writerDebug200.append(i+"<->" + mapOut.get(i)+ "\n");
    					writerDebug200.flush();
    					for(Integer j:mapOut.get(i).keySet()){
    						MAPuniqueYYYYMMDD.put( mapOut.get(i).get(j).replace("-", ""), 
    											   mapOut.get(i).get(j).replace("-", "")
    											  );
    					}
    			}
    			
    			if(boolean_isrunningSplitFiles){
    				//SplitGivenSingleFile_to_multiple_files_Each_file_corresponds_to_one_YYYYMMDD
    				SplitGivenSingleFile_to_multiple_files_Each_file_corresponds_to_one_YYYYMMDD(
    									inputFolder_for_FilesOfNYT+"Merged-All.txt",
    									inputFolder_for_Staging_split_files,
    									MAPuniqueYYYYMMDD
    									);
    			}
    		
    			
    			//
    			while(cnt<filesOfgivenFolder.length){ // while start
    				if(filesOfgivenFolder[cnt].indexOf("Store") >=0 ){
    					cnt++;
    					continue;
    				}
    				  
    				System.out.println("##$$$$$$$$$$$$$$$$$$$#############");
    				//SplitFilesForSlidingWindow
    				if(Flag.equalsIgnoreCase("SplitFilesForSlidingWindow")){
    				
    					//each set of keys inside value of mapOut
    					for(int h:mapOut.keySet()){
    						//System.out.println("set of permuted keys:"+h+" -> "+mapOut.get(h) +"\n");
    						writerDebug200.append("\n*** start of for loop:"+h+"~~~"+mapOut.get(h)+"\n");
    						writerDebug200.flush();
    						TreeMap<Integer,String> mapFirstLastKey=get_First_And_Last_Key_in_MAP(mapOut.get(h));
    						String temp_output_file_name="nytimes_world_"+mapFirstLastKey.get(1)+"-"+mapFirstLastKey.get(2)+".txt";
    						String temp_output_debug_file_name="Debug-only"+temp_output_file_name;
    					//already process
    					if(!mapPastAlreadyProcessedFile.containsKey(temp_output_file_name)){
    						System.out.println(" already processed : "+temp_output_file_name+";mapOut.SIZE:"+mapOut.size());
    						cnt++;
    						//continue; //debug
    					
    						System.out.println(" already processed : 2");
    						writerDebug200.append("BEFORE calling readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found\n");
    						writerDebug200.flush();
    						System.out.println(" already processed : 3");
    						// Flag="SplitFilesForSlidingWindow"
    						ReadFile_CompleteFile_to_a_Single_String.
    						readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found_2
    						(   "", //inputFolder_for_FilesOfNYT+filesOfgivenFolder[cnt], //ONE BIG FILE IS NOT PASSED, PASSED FROM SPLIT STAGED FILE
    							outputFolder+temp_output_file_name,
    						    inputFolder_for_Staging_split_files,
    							outputFolder+temp_output_debug_file_name, //debug file
    							true, //is_Append_outFile
    							mapOut.get(h),
    							true, //is_it_NYC_File_where_YYYYMMDD_is_the_key
    							mapPastAlreadyProcessedFile,
    							writerDebug200
    							);
    						System.out.println(" already processed : 4");
    						writerDebug200.append("AFTER calling readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found\n");
    						writerDebug200.flush();
    						
    					  
    							//FileWriter writeDebug2=new FileWriter(outputFolder+"Debug-only"+temp_output_file_name, true);
    							
    						writerDebug200.append("\n#############end of one loop . h of mapOut##############"+h
    												+" of "+mapOut.get(h)+"\n");
    						writerDebug200.flush();
    							//writeDebug.close();
    							
    							writerAlreadyProcessedFile.append(temp_output_file_name+"\n");
    							writerAlreadyProcessedFile.flush();
    					
    							mapPastAlreadyProcessedFile.put(temp_output_file_name, "");	
    							
    							
    					}// NOT PROCESSED BEFORE mapPastAlreadyProcessedFile
    							
    					} // each element h of map
    				} //end of SplitFilesForSlidingWindow
    				//CreateGBADFile
    				if(Flag.equalsIgnoreCase("CreateGBADFile")){
    					// convert NYT to GBAD graph 
//    					convertNYTnewsARTICLEtoGBADgraph(  inputFolder_for_FilesOfNYT+ filesOfgivenFolder[cnt]
//    													, -1 //startline
//    													, -1 //endLine
//    													);	
    				}
    				

    				
//    				System.out.println("last file:"+filesOfgivenFolder[cnt]);
    				cnt++;
    			} //WHILE END (each file of given input folder)
    		 
    		} //end of mainType
        	
        	// 
//        	readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found_2
//        	("/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/Apr.May.Merged.2/Merged-Apr_May.txt",
//        	 "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/Apr.May.Merged.2/Merged-Apr_May.filter.txt",
//        	 "", //staging folder
//        	 "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/Apr.May.Merged.2/Merged-Apr_May.debug.only.txt",
//        	 true, //is_append
//        	 mapKeysToSearchForInEachLine,
//        	 true, //is_it_NYC_File_where_YYYYMMDD_is_the_key
//        	 mapPastAlreadyProcessedFile,
//        	 writerGlobalDebug200
//        	 );
        	
        	 
        	System.out.println("Time Taken:" + NANOSECONDS.toSeconds(System.nanoTime() - t0) +" seconds; "
	       	   				   + (NANOSECONDS.toSeconds(System.nanoTime() - t0))/60 +" minutes");
        	
        	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	
} //main
	
}
