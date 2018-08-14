	package crawler;
	
	import static java.util.concurrent.TimeUnit.NANOSECONDS;

	import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.GetterSetterReflection;
	
	public  class ReadFile_CompleteFile_to_a_Single_String {
		
		// readFile_CompleteFile_to_a_Single_String
		public static  String readFile_CompleteFile_to_a_Single_String(String pathToFile) throws Exception{
			long t0 = System.nanoTime();
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
		    
		    System.out.println("readFile_CompleteFile_to_a_Single_String->Time Taken:"
					+ NANOSECONDS.toSeconds(System.nanoTime() - t0) + " seconds; "
					+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
					+ " minutes");
		    
		    return strFile.toString();
		}
		
		//readFile_CompleteFile_to_a_Single_String_TRAD
		public static String readFile_CompleteFile_to_a_Single_String_TRAD(String pathToFile){
			  String concLine="";
			String line="";
			System.out.println("readFile_CompleteFile_to_a_Single_String_TRAD" );
			long t0 = System.nanoTime();
//			   FILEINPUTSTREAM FIS = NULL;
//		        BUFFEREDREADER READER = NULL;
 
			try{
				// reader100=new BufferedReader(new FileReader(pathToFile));
				int lineNo=0;
				BufferedReader reader100 = new BufferedReader(new InputStreamReader(new FileInputStream(pathToFile), "UTF-16"));
				//
				while( (line=reader100.readLine()) !=null){
					lineNo++;
					concLine=concLine+" "+line;
					System.out.println("lineNo:(_TRAD)"+lineNo);
				}
				System.out.println("Read FILE using readFile_CompleteFile_to_a_Single_String_TRAD");
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Time Taken:"
					+ NANOSECONDS.toSeconds(System.nanoTime() - t0) + " seconds; "
					+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
					+ " minutes");
			return concLine;
		}
		
		//readFile_CompleteFile_to_a_Single_String_use_FILES_readAllFiles
		public static String readFile_CompleteFile_to_a_Single_String_use_FILES_readAllFiles(
																String pathToFile
																){
			String concLine="";
			long t0 = System.nanoTime();
			try{

			    Charset charset = Charset.forName("ISO-8859-1");
				List<String> lines = Files.readAllLines(Paths.get(pathToFile), charset);
				
				for (int i = 0; i < lines.size(); i++) {
					concLine=concLine+" "+lines.get(i);
					System.out.println("FILES_readAllFiles:lineNo="+i);
					//System.out.println(lines.get(i));
				}
				
				System.out.println("Read FILE using readFile_CompleteFile_to_a_Single_String_use_FILES_readAllFiles");
			}
			catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Time Taken:"
					+ NANOSECONDS.toSeconds(System.nanoTime() - t0) + " seconds; "
					+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
					+ " minutes");
			return concLine;
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
					  
					  ReadFile_CompleteFile_to_a_Single_String.
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
						map_isDuplicateLine=new TreeMap();
						
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
			
					String s=ReadFile_CompleteFile_to_a_Single_String
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
					  
					  ReadFile_CompleteFile_to_a_Single_String.
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
	        	
//	        	System.out.println(
//	        						GenerateDate.
//	        						generate_Date_Pattern_for_RSS_XML_Parser_for_date_DD_MMM_YYYY("21 dec 2014")
//	        					  );
//
	        	
	        	TreeMap<Integer, String> mapKeysToSearchForInEachLine=new TreeMap<Integer, String>();
	        	mapKeysToSearchForInEachLine.put(1, "20140501");
	        	mapKeysToSearchForInEachLine.put(2, "20140502");
	        	TreeMap<String, String> mapPastAlreadyProcessedFile=new TreeMap<String, String>();
	        	mapPastAlreadyProcessedFile.put("", "");
	        	String debugFile="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/output/debug.txt";
	        	FileWriter writerGlobalDebug200=new FileWriter(debugFile);
	        	
	        	// 
	        	readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found_2
	        	("/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/Apr.May.Merged.2/Merged-Apr_May.txt",
	        	 "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/Apr.May.Merged.2/Merged-Apr_May.filter.txt",
	        	 "", //staging folder
	        	 "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/Apr.May.Merged.2/Merged-Apr_May.debug.only.txt",
	        	 true, //is_append
	        	 mapKeysToSearchForInEachLine,
	        	 true, //is_it_NYC_File_where_YYYYMMDD_is_the_key
	        	 mapPastAlreadyProcessedFile,
	        	 writerGlobalDebug200
	        	 );
	        	
	        	 
	        	System.out.println("Time Taken:" + NANOSECONDS.toSeconds(System.nanoTime() - t0) +" seconds; "
		       	   				   + (NANOSECONDS.toSeconds(System.nanoTime() - t0))/60 +" minutes");
	        	
	        	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
	}
