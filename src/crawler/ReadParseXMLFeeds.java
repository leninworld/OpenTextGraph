package crawler;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.TreeMap;

//
public class ReadParseXMLFeeds {
	
	//extract_Features_as_Per_Static_Config (is_preserve_CASE=if true, preserve ORIGINAL case (includes UPPER case))
	// MAY BE LOWER case to be commented to speed-up
	public static String extract_Features_as_Per_Static_Config(String[] 	arrayofCSVelement,
														       String 		strFeature_concLine,
														       String       strFeature_concLine_ORIGINAL,
															   FileWriter 	writerDebug,
															   boolean      is_preserve_CASE, // if true, preserve ORIGINAL case (includes UPPER case)
															   boolean      isSOPprint){
		int cnt=0; int cnt2=0;
		String concOutputCSVLine="";
		String concOutputCSVLine_ORIGINAL="";
		String out_Final_String="";
		//assign
		String strFeature=strFeature_concLine;
		String strFeature_ORIGINAL=strFeature_concLine_ORIGINAL;
		try {
			//moved this to a method "extract_Features_as_Per_Static_Config"
			while(cnt<arrayofCSVelement.length){
				if(isSOPprint){
					System.out.println("-------------main entry---------"+arrayofCSVelement[cnt]);
				}
				cnt2=0;
				String[] s2=arrayofCSVelement[cnt].toLowerCase().replace(".", "#").split("#");
				String[] s2_ORIGINAL=arrayofCSVelement[cnt].replace(".", "#").split("#"); //original case preserved
				String []s3=null;
				String []s3_ORIGINAL=null;
				String currFeature=s2[0].toLowerCase();
				//original case preserved
				String currFeature_ORIGINAL=s2_ORIGINAL[0]; 
				if(isSOPprint){
					System.out.println("@@@arrayofCSVelement:"+arrayofCSVelement[cnt] +"; s2.len:"+s2.length);
				}
				//current* split of one of main csvElement
				while(cnt2<s2.length){
					// function* here you go
					if(ConvertPatternStringToMap.isFunction(s2[cnt2])){
						//remove tag
						if(s2[cnt2].indexOf("removetag")>=0){
							if(isSOPprint){
								System.out.println("removetag:");
							}
							//LOWER CASE (LOWER) 
							currFeature=
										ConvertPatternStringToMap.
										convert_to_targetStringForFunctionWithArguments(currFeature
																						,"removetag"
																					    ,""
																					    ,"");
							// BOTH CASE
							currFeature_ORIGINAL=
										ConvertPatternStringToMap.
										convert_to_targetStringForFunctionWithArguments(currFeature_ORIGINAL
																						,"removetag"
																					    ,""
																					    ,"");
							
						}
						//substring function
						if(s2[cnt2].indexOf("substr")>=0){
							// LOWER CASE
							//if(is_preserve_CASE==false)
								s3= ConvertPatternStringToMap.
									convertPattern_SubStringString2_StartEndIndexArrayString(s2[cnt2]);
							//BOTH CASE - preserve case
							s3_ORIGINAL=ConvertPatternStringToMap.
										convertPattern_SubStringString2_StartEndIndexArrayString(s2_ORIGINAL[cnt2]);
							
							if(isSOPprint){
								System.out.println("*fn:substr ; s2[cnt2]:"+s2[cnt2]+" -- "+s3.length
													+";s3[0]:"+s3[0]
													+";cnt2:"+cnt2+";s2[cnt2]:"+s2[cnt2]
													+";s2.len:"+s2.length+"<-");
							}
							
							//if(is_preserve_CASE==false)
								currFeature=currFeature.substring( Integer.valueOf(s3[0]), 
																   Integer.valueOf(s3[1]));
							//BOTH CASE - preserve case
							currFeature_ORIGINAL=currFeature_ORIGINAL.substring( Integer.valueOf(s3_ORIGINAL[0]), 
									   											 Integer.valueOf(s3_ORIGINAL[1]));
						}
						//replace function
						if(s2[cnt2].indexOf("replace")>=0){
							//currFeature=currFeature.replace("replace(", "");
							if(isSOPprint){
								System.out.println("*fn:replace: s2[cnt2]:"+s2[cnt2]);
							}
							s3=s2[cnt2].replace("replace(", "").replace(")", "").split("to");
							
							if(isSOPprint){
								System.out.println("target:"+
												ConvertPatternStringToMap.
												convert_to_targetStringForFunctionWithArguments(
																								currFeature,
																								"replace",
																								s3[0],
																								s3[1])
												);
							}
							//LOWER-CASE ONLY
							//if(is_preserve_CASE==false)
								currFeature=	ConvertPatternStringToMap.
												convert_to_targetStringForFunctionWithArguments(
																										currFeature,
																										"replace",
																										s3[0],
																										s3[1]);
							//BOTH CASE - preserve case
							currFeature_ORIGINAL=ConvertPatternStringToMap.
																		convert_to_targetStringForFunctionWithArguments(
																				currFeature_ORIGINAL,
																				"replace",
																				s3_ORIGINAL[0],
																				s3_ORIGINAL[1]);
						}
					} // ELSE: it is an element* extraction (example: <head>to</head>)
					else{
						//LOWER CASE ONLY
						//if(is_preserve_CASE==false)
							s3= ConvertPatternStringToMap.convert_to_Pattern_into_arrayString(s2[cnt2].replace("/", "#"));
						//PRESERVE CASE
						s3_ORIGINAL= ConvertPatternStringToMap.convert_to_Pattern_into_arrayString(s2_ORIGINAL[cnt2].replace("/", "#"));
						
						int begin=-1; int end=-1;
						if(s3.length>1){
							// 
							//if(is_preserve_CASE==false){ 
								begin=strFeature.indexOf(s3[0]);
							    end=strFeature.indexOf(s3[1],begin+s3[0].length());
							//}
							
							int begin_ORIGINAL=strFeature.indexOf(s3_ORIGINAL[0]);
							int end_ORIGINAL=strFeature.indexOf(s3_ORIGINAL[1],begin_ORIGINAL+s3_ORIGINAL[0].length());
							
//							int begin_ORIGINAL=strFeature_ORIGINAL.indexOf(s3_ORIGINAL[0]);
//							int end_ORIGINAL=strFeature_ORIGINAL.indexOf(s3_ORIGINAL[1],begin+s3_ORIGINAL[0].length());
							
							if(isSOPprint){
								System.out.println("*fn.element:>1: *NOT* fn-> s2[cnt2]=>"+s2[cnt2]+"((s3[0]=>"+s3[0]+" s3[1]=>"+s3[1]+"))"
												  +";\n begin,end:"+begin+","+end
												  +";substr:"+strFeature.substring(begin, end)
												  +";\n*****strFeature*start*******=>"
												  +strFeature+"\n<=********strFeature end*******");
							}
							
//							writerDebug.append("\n*fn.element:>1: *NOT* fn-> s2[cnt2]=>"+s2[cnt2]+"((s3[0]=>"+s3[0]+" s3[1]=>"+s3[1]+"))"
//									  +";\n begin,end:"+begin+","+end
//									  +";\n*****strFeature*start*******=>"
//									  +strFeature+"\n<=********strFeature end*******");
//							writerDebug.flush();
							
							if(isSOPprint){
								System.out.println("*2fn.element:>1: not fn-> s2[cnt2]=>"+s2[cnt2]+" s3[0]=>"+s3[0]+" s3[1]=>"+s3[1]);
								System.out.println(
													"@@@@@@ substring-> begin:"+ begin+" end:"+end
													 +" last.currFeature:"+currFeature
												  );
							}
							
							//if(begin>=0 && end>=0){
								if(s3[0].toLowerCase().indexOf("<head>")>=0){
									//LOWER CASE ONLY
									//if(is_preserve_CASE==false)
										currFeature="head:"+strFeature.substring(begin, end);
									//BOTH CASE - PRESERVE ORIGINAL CASE
									currFeature_ORIGINAL="head:"+strFeature_ORIGINAL.substring(begin, end);
								}
								else if(s3[0].toLowerCase().indexOf("<body")>=0){
									//LOWER CASE ONLY
									//if(is_preserve_CASE==false)
										currFeature="body::"+strFeature.substring(begin, end);
									//BOTH CASE - PRESERVE ORIGINAL CASE
									currFeature_ORIGINAL="body::"+strFeature_ORIGINAL.substring(begin, end);
								}
								else{
									//LOWER CASE ONLY
									//if(is_preserve_CASE==false)
										currFeature=s3[0]+":"+strFeature.substring(begin, end);
									//BOTH CASE - PRESERVE ORIGINAL CASE
									currFeature_ORIGINAL=s3[0]+":"+strFeature_ORIGINAL.substring(begin, end); 
								}
//							}
//							else{
//								buffer.clear();
//								continue;
//							}
						
						}
						else{//either not function or function with no argument
							if(isSOPprint){
								System.out.println("*not %#$@#$#$# fn:=1:s2[cnt2]:"+s2[cnt2]+" s3[0]:"+s3[0]);
							}
						}
					}
					
					if(isSOPprint){
						System.out.println("------###------------cnt2:"+cnt2+";currFeature:"+currFeature);
					}
					cnt2++;
				}
				if(isSOPprint){
					System.out.println("------****------------cnt:"+cnt);
				}
				cnt++;
				//LOWER CASE ONLY
				//if(is_preserve_CASE==false)
					currFeature=currFeature.replace("  ", " ").replace("/n", "")
											.replace(" !!! ", "!!!")
											.replace(" !!!", "!!!")
											.replaceAll("\\p{Cntrl}", ""); 
				
				//BOTH CASE - PRESERVE ORIGINAL CASE
				currFeature_ORIGINAL=currFeature_ORIGINAL.replace("  ", " ").replace("/n", "")
														.replace(" !!! ", "!!!")
														.replace(" !!!", "!!!")
														.replaceAll("\\p{Cntrl}", ""); 

				writerDebug.append("\ncurrFeature:"+currFeature);
				writerDebug.flush();
				
				//LOWER CASE ONLY
				//if(is_preserve_CASE==false)
					concOutputCSVLine=concOutputCSVLine+"!!!"+currFeature.replace("#", "/");
				
				//PRESERVE ORIGINAL CASE
				concOutputCSVLine_ORIGINAL=concOutputCSVLine_ORIGINAL+"!!!"+currFeature_ORIGINAL.replace("#", "/");
			 } //end for arrayofCSVelement*
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		//
//		String [] arr_s=concOutputCSVLine.split("!!!");
//		String [] arr_s_ORIGINAL=concOutputCSVLine_ORIGINAL.split("!!!");
		cnt=0;
		// BODY TEXT (+TITLE) needs have ORIGINAL case preserved 
//		while(cnt<arr_s.length){
//			
//			if( cnt==2){ //second token
//				//
//				if(out_Final_String.length()==0)
//					out_Final_String=arr_s[cnt];
//				else{
//					out_Final_String=out_Final_String+arr_s_ORIGINAL[cnt];
//				}
//			}
//			else{
//				//
//				if(out_Final_String.length()==0)
//					out_Final_String=arr_s[cnt];
//				else{
//					out_Final_String=out_Final_String+arr_s_ORIGINAL[cnt];
//				}
//			}
//			cnt++;
//		}
		
		
//		System.out.println("out_Final_String:"+out_Final_String);
//		System.out.println("concOutputCSVLine:"+concOutputCSVLine);
//		System.out.println("concOutputCSVLine_ORIGINAL:"+concOutputCSVLine_ORIGINAL);
		
		//
		if(is_preserve_CASE==true)
			return concOutputCSVLine_ORIGINAL; //PRESERVE ORIGINAL CASE
		else
			return concOutputCSVLine;
		
//		 return out_Final_String;
	}
	// read and parse single xml feed
	public static int readParseSingleXMLFeed( File 	     outputFileName
											, File 	     fileinput
											, String 	 absolutePathOfInputFile
											, boolean 	 is_run_only_static
											, boolean 	 is_run_only_dynamic
											, boolean 	 is_run_only_eachLineNEWSstring
											, FileWriter writerSuccess
											, FileWriter writerSuccess_only_static
											, FileWriter writerSuccess_only_dynamic
											, FileWriter writerFailure
											, FileWriter writer
											, boolean	 is_outFile_Static
											, FileWriter writer_eachLineNEWSstring
											, FileWriter writerDynamic
											, boolean 	 is_outFile_Dynamic
											, int i
											, String 	continueParsingFromThisPatternMMM
											, String 	continueParsingFromThisPatternYYYY
											, String 	confFile
											, File primaryFileName
											, FileWriter writerDebug
											, TreeMap<String, String> mapConfig
											, boolean Go_AFTER_found_ContinueParsingFromThisPattern
											, boolean isStreamInputFile
											, boolean is_convert_to_lowercase
											, boolean isSOPprint
											, boolean is_debug_WriteMore
											){
		
		int cnt_calling_getDynamicConfigurationInXMLFeedFileThenParseForFeature=0;
		continueParsingFromThisPatternMMM=continueParsingFromThisPatternMMM.toLowerCase();
		//HashMap<String,String> mapConfig = crawler.getConfig(confFile);
		//String xmlParser=mapConfig.get("s1.parser");
		//TreeMap<String, String> mapConfig=readConfig.readConfig( confFile, "=");
		String parser=mapConfig.get("s1.parser");
		//String attributefunction=mapConfig.get("attributefunction");
		String parser_rdf=mapConfig.get("s1.parser.rdf");
		boolean is_RDF_file=false; 
		TreeMap<String,String> mapPastRunFileName=new TreeMap<String, String>();
		int counter_dynamic=0;
		boolean is_preserve_CASE=true;
		// xml parser string 
		TreeMap<String ,String> mapParser=ConvertPatternStringToMap.convertXMLParserStringToMap(parser,isSOPprint);
		TreeMap<String ,String> mapParser_RF=ConvertPatternStringToMap.convertXMLParserStringToMap(parser_rdf,isSOPprint);
		
		String temp_startPattern="";
		String temp_endPattern="";
		String ORIG_concLine="";
		String concLine=""; String line="";
		String concLine_NoLower_Converted="";
		String strFeature="";
		String startPattern=mapParser.get("mainTagStart");
		String startPattern_RDF=mapParser_RF.get("mainTagStartRDF");
		
		if(is_convert_to_lowercase){
			startPattern=startPattern.toLowerCase();
			startPattern_RDF=startPattern_RDF.toLowerCase();
		}
		
		String left_concLine="";
		String endPattern=mapParser.get("mainTagEnd").replace("/", "#");
		String endPattern_RDF=mapParser_RF.get("mainTagEndRDF").replace("/", "#");
		
		if(is_convert_to_lowercase){
			endPattern=endPattern.toLowerCase();
			endPattern_RDF=endPattern_RDF.toLowerCase();
		}
		
		boolean isDynamic=false;
		boolean YES_Go_AFTER_found_ContinueParsingFromThisPattern=false;
		String concOutputCSVLine="";
		TreeMap<String, String> mapDuplicateCheck=new TreeMap<String, String>();
		try{
			final long t0 = System.nanoTime();
			
			if(isSOPprint){
				System.out.println("curr File1:"+absolutePathOfInputFile);
				System.out.println("curr File2:"+fileinput
									+" size:"+fileinput.length()/(1024.0*1024.0) +" MB");
			}
			
			BufferedReader reader11 = new BufferedReader(new FileReader(fileinput));
			
			//FileWriter writer = new FileWriter(outFile);
			concLine = ""; int lineNumber=0; String tempConcLine="";
			// input is NOT a STREAM*
			if(isStreamInputFile==false){
				
				// read each line of current file
//				while ((line = reader11.readLine()) != null) {
//					line=line.toLowerCase();
//					lineNumber++;
//					//skip junk
//					if( (line.indexOf("^")>=0 && line.indexOf("[")>=0)
//						||(line.indexOf("(^")>=0 && line.indexOf("])")>=0)
//						||(line.indexOf("(^")>=0 && line.indexOf(")")>=0)
//						||(line.indexOf("(^")>=0 && line.indexOf("]")>=0)
//						){
//						continue;
//					}
//					
//					//System.out.println("reading new line:"+line);
//					if(concLine.length()==0)
//						concLine=line;
//					else{
//						concLine = concLine  + line;
//					}
//					
//					if ( (lineNumber % 3000) == 0){
//						System.out.println( "line No:"+lineNumber+"..another 3000 lines read.."+fileinput);
//					}
//				} // WHILE END
				
				
				//below reads by BUFFER  (fast)
				concLine=ReadFile_CompleteFile_to_a_Single_String.readFile_CompleteFile_to_a_Single_String(fileinput.getAbsolutePath());
				//backup (case- upper and lower as it is);
				ORIG_concLine=concLine;
				// below reads by traditional slower(***but solves the problem of two words concatenate without space in between)
//				concLine=readFile_CompleteFile_to_a_Single_String
//							.readFile_CompleteFile_to_a_Single_String_TRAD(fileinput.getAbsolutePath());
				
				//below using FILES.readLine() tool from java itself (SLOW)
//				concLine=readFile_CompleteFile_to_a_Single_String
//						.readFile_CompleteFile_to_a_Single_String_use_FILES_readAllFiles(fileinput.getAbsolutePath());
				
				
				//RUNNING only THIS eachLine (is_run_only_static=false means will RETURN*****)
				if(is_run_only_eachLineNEWSstring==true){
					//eachLineNewsString
					String [] arr_newsArticles=ORIG_concLine.split("From -");
					
					writerDebug.append("\n arr_newsArticles: "+arr_newsArticles);
					writerDebug.flush();
					int len_arr_newsArticles=arr_newsArticles.length;
					int cnt20=0;
					// 
					while(cnt20<len_arr_newsArticles){
						String o_write2="!!!"+fileinput.getName() +"!!!"+"From - "+arr_newsArticles[cnt20].replace("\n", " ")
											.replaceAll("\\s+", " ").trim();
						writer_eachLineNEWSstring.append("\nnew(3)::"+o_write2);
						writer_eachLineNEWSstring.flush();
						cnt20++;
					}
					if(is_run_only_static==false){ //RETURING
						return 0;
					}
				}
				else{
					//eachLineNewsString
					String [] arr_newsArticles=ORIG_concLine.split("From -");
					
					writerDebug.append("\n arr_newsArticles: "+arr_newsArticles);
					writerDebug.flush();
					int len_arr_newsArticles=arr_newsArticles.length;
					int cnt20=0;
					// 
					while(cnt20<len_arr_newsArticles){
						String o_write2="!!!"+fileinput.getName() +"!!!"+"From - "+arr_newsArticles[cnt20].replace("\n", " ")
											.replaceAll("\\s+", " ").trim();
						writer_eachLineNEWSstring.append("\nnew(3)::"+o_write2);
						writer_eachLineNEWSstring.flush();
						cnt20++;
					}
					
				}
				
				if(is_convert_to_lowercase){
					concLine=concLine.replace("/", "#").toLowerCase();
				}
				else 
					concLine=concLine.replace("/", "#");
				///CASE preserved
				ORIG_concLine=ORIG_concLine.replace("/", "#");
					
				while ((line = reader11.readLine()) != null) {
					line=line.toLowerCase();
					lineNumber++;
					tempConcLine=tempConcLine+" "+line;
					
					if(lineNumber>25) break; //15 lines done	
				}
				if(isSOPprint){
					System.out.println("(got25lines)out...loop():tempConcLine:"+tempConcLine);
				}
				
				
				reader11.close(); //obsolete
			} // isStreamInputFile==false
			else if(isStreamInputFile==true){   // now this is streaming, so read first 15 lines and find static or dynamic config to choose 
				// CHECK dynamic or static
				while ((line = reader11.readLine()) != null) {
						line=line.toLowerCase();
						lineNumber++;
						tempConcLine=tempConcLine+" "+line;
						
						if(lineNumber>25) break; //15 lines done	
				}
				if(isSOPprint){
					System.out.println("(streaming-true)out...loop():tempConcLine:"+tempConcLine);
				}
			}
			
			 //not found "from:" and "date:"
			if(tempConcLine.toLowerCase().indexOf("from:")==-1 
					&& tempConcLine.toLowerCase().indexOf("date:")==-1){
						isDynamic=true;
						continueParsingFromThisPatternMMM="";
						continueParsingFromThisPatternYYYY="";
						if(isSOPprint){
							System.out.println("GOT:: **RDF static or dynamic it is..(rdf static or dynamic)..isStreamInputFile:"+isStreamInputFile);
							System.out.println("tempConcLine.toLowerCase():"+tempConcLine.toLowerCase());
						}
						//return 1; //dummy return
			}
			if(isSOPprint){
				System.out.println("isDynamic:"+isDynamic+" isStreamInputFile:"+isStreamInputFile);
			}
		    final int buffer_size=1024;
			int randomSplitToStart=2;
			String concLine2="";
			//clean up
			RandomAccessFile aFile = new RandomAccessFile(fileinput.getAbsolutePath(), "r");
			RandomAccessFile aFile2 = new RandomAccessFile(fileinput.getAbsolutePath(), "r");
			
			//Scanner scanner = new Scanner(fileinput.getAbsoluteFile(),"rw");
			FileChannel inChannel = aFile.getChannel();
			FileChannel inChannel2 = aFile2.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(buffer_size);
			ByteBuffer buffer2 = ByteBuffer.allocate(buffer_size);
			
			int countForNumberOFbufferREAD=0;
			int totalBlock=0;
			 //streaming
			if(isStreamInputFile==true){
			
				if(continueParsingFromThisPatternMMM.length()>0){
					//b-tree search pointer (obsolete to use)
					totalBlock=ReadFile_Using_DifferentTechniques.readFile2(  
																fileinput.getAbsolutePath()
															   , "" //stringRegexPatternForDate
															   , continueParsingFromThisPatternMMM //target_MMM to find and start reading from it
															   , continueParsingFromThisPatternYYYY
															   , writerDebug);
					
					
					totalBlock=totalBlock-(totalBlock/4);	
				}
	
				// seek set
				if(totalBlock>0){
					aFile.seek(totalBlock);
				
				}
			
			}// isStreaming true
			
			int no_of_block_count=0;
			boolean isFoundTheRequiredPattern=false;
			continueParsingFromThisPatternMMM=
        			" "+continueParsingFromThisPatternMMM+" ";
			continueParsingFromThisPatternYYYY=
						" "+continueParsingFromThisPatternYYYY+" ";
			int id2=0;
			//is RDF?
			if(fileinput.getName().toString().toLowerCase().indexOf(".rdf")>=0){
				temp_startPattern=startPattern_RDF;
				temp_endPattern =endPattern_RDF;
				is_RDF_file=true;
				if(isSOPprint){
					System.out.println("RDF static....");
				}
			}
			else{
				temp_startPattern=startPattern;
				temp_endPattern =endPattern;
				is_RDF_file=false;
			}
			// if RDF, NOT dynamic
			if(is_RDF_file){
				isDynamic=false;
				continueParsingFromThisPatternMMM="";
				continueParsingFromThisPatternYYYY="";
			}
			//
			if(is_run_only_dynamic==true //only want dynamic to run
				&& isDynamic==false //BUT, curr file is static  
				){
				if(isSOPprint){
					System.out.println(" ***skipping static:"+fileinput.getName());}
				
				return -1;
			}
			//
			if(is_run_only_static==true // only want static to run
					&& isDynamic==true//BUT, curr file is dynamic  
					){
				if(isSOPprint){
					System.out.println(" ***skipping dynamic:"+fileinput.getName()+" :isDynamic->"+isDynamic);
				}
				return -1;
			}
		// Static
		if(!isDynamic //static
			&& is_run_only_static==true
			){
			// 
		  if(isStreamInputFile==true){
			
	        //read the file into buffer
	        while(inChannel.read(buffer) > 0){
	        		no_of_block_count++;
	        		if(isSOPprint){
	        			System.out.println("!!!!!!!!!!!!!!!!no_of_block_count!!!!!!!!!!!!!!!!"+no_of_block_count);
	        		}
			 		buffer.flip();
		            for (int j = 0; j < buffer.limit(); j++)
		            {
		                //System.out.print((char) buffer.get());
		                concLine=concLine+" "+(char) buffer.get();
		                 
		                //writerDebug.append("..."+concLine+"\n");
		                writerDebug.flush();
		            }
		            
		            concLine=concLine.toLowerCase();
		            
		            // Static
		        	if(!isDynamic){
		        		//MMM given
			            if(continueParsingFromThisPatternMMM.length()>2){
				            //
				            if(concLine.indexOf(continueParsingFromThisPatternMMM)==-1){
				            	buffer.flip();
				            	concLine="";
				            	continueParsingFromThisPatternMMM="";
				            	continueParsingFromThisPatternYYYY="";
				            	continue;
				            }
				            else{
				            	
//				            	YES_Go_AFTER_found_ContinueParsingFromThisPattern=
//				            			Go_AFTER_found_ContinueParsingFromThisPattern;
				            }
			            }
		            }
		            
				//System.out.println("inFileOneXMLFeed:"+fileinput.getAbsolutePath());
				if(concLine.length()>=100 && concLine.indexOf(" 201")>=0){
					if(isSOPprint){
						System.out.println("--------------");
						System.out.println("parsing:(length:"+concLine.length()+") concLine(full):"
												+concLine);
											//+concLine.substring(0, concLine.indexOf(" 201")));
						System.out.println("--------------");
					}
				}
				String origconcLine=concLine;
				
				concLine=concLine.toLowerCase();
				concLine=concLine.replace("/", "#");
				concLine=concLine.replace("ocalh\\ost","ocalhost");
				if(isSOPprint){
					System.out.println("*"+concLine.indexOf(temp_startPattern)+" (given:)"
								+" "+temp_startPattern
								+ " || "+concLine.indexOf(temp_endPattern, concLine.indexOf(temp_startPattern)+1   )
								+" (given:)"+" "+temp_endPattern
								+ " || "+ concLine.indexOf(" "+continueParsingFromThisPatternMMM+" ")+" (given:)"
								+" "+continueParsingFromThisPatternMMM+" "+"|| "
								+
										YES_Go_AFTER_found_ContinueParsingFromThisPattern
								);
				}
				
				// Static
				if(!isDynamic
					&&  concLine.indexOf(temp_startPattern)>=0
					&& concLine.indexOf(temp_endPattern, concLine.indexOf(temp_startPattern)+1)>=0 ){
					
					if(isSOPprint)
						System.out.println("----!!-------start/conc-------!!---------------");
					if(concLine.length()>0){
						if(isSOPprint)
							System.out.println("\nconcLine(full):\n"+concLine);
					}
					if(isSOPprint){
						System.out.println("----!!-------end/conc----------!!-------------"
										    +concLine.indexOf(temp_startPattern)
											+" "+concLine.indexOf(temp_endPattern)+"<---");
					}
				
					if(
						(  concLine.indexOf(temp_startPattern)>=0
					   && concLine.indexOf(temp_endPattern, concLine.indexOf(temp_startPattern)+1   )>=0 
					   && concLine.indexOf(continueParsingFromThisPatternMMM)>=0
					   && concLine.indexOf(continueParsingFromThisPatternYYYY)>=0
					   )
					   || YES_Go_AFTER_found_ContinueParsingFromThisPattern==true
						){
						//
						if(concLine.indexOf(temp_startPattern)>=0
							&& concLine.indexOf(temp_endPattern, concLine.indexOf(temp_startPattern)+1) >=0 ){
							
							
//							writerDebug.append("\n b.trun.start::::::::::::::::startPattern,end:"
//													+temp_startPattern+"!"+temp_endPattern);
//							writerDebug.append("\n b.trun.start::::::::::::::::concLine::::");
//							writerDebug.append("\nconcLine:"+concLine);
//							writerDebug.append("\n b.trun.end::::::::::::::::concLine::::\n\n");
//							writerDebug.append("\n next index:"+ concLine.indexOf(temp_startPattern
//									,concLine.indexOf(temp_endPattern))+"!"
//									+concLine.indexOf(temp_endPattern)
//												 );
							
							if( concLine.indexOf(temp_startPattern,concLine.indexOf(temp_endPattern)) >0)
								left_concLine=concLine.substring( concLine.indexOf(temp_startPattern
															,concLine.indexOf(temp_endPattern)) , 
															 concLine.length()
														   );
						
								writerDebug.flush();
							
//							if(concLine.indexOf(startPattern, concLine.indexOf(startPattern)+1 )>=0)
//								left_concLine=concLine.substring(concLine.indexOf(startPattern, concLine.indexOf(startPattern)+1 )
//															 					 , 
//															 	 concLine.length());
						}
						
//						writerDebug.append("\n start.sub::::::::::::::::concLine::::");
//						writerDebug.append("\nleft_concLine:"+left_concLine);
//						writerDebug.append("\n end.sub::::::::::::::::concLine::::\n\n");
						writerDebug.flush();
						
						
//						writerDebug.append("\n start::::::::::::::::concLine::::");
//						writerDebug.append("\nconcLine:"+concLine);
//						writerDebug.append("\n end::::::::::::::::concLine::::\n\n");
						writerDebug.flush();
						
						if(Go_AFTER_found_ContinueParsingFromThisPattern==true){
							YES_Go_AFTER_found_ContinueParsingFromThisPattern=true;
						}
						
						buffer.clear();
					}
					
				} // Static
				else if(!isDynamic){
					
					if(isSOPprint){
						System.out.println("continue..");
						System.out.println("----!!--NOmat-----start/conc-------!!---------------");
					}
					
					if(concLine.length()>0){
						if(isSOPprint){
							System.out.println("\nconcLine(full):\n"+concLine);
						}
					}
					if(isSOPprint){
						System.out.println("----!!--NOmat-----end/conc----------!!-------------"
										+temp_startPattern+" "+temp_endPattern
										);
					}
					
					if(concLine.indexOf(temp_startPattern)>=0)
						concLine=concLine.substring(concLine.indexOf(temp_startPattern), 
													concLine.length() );
					
					buffer.clear();
					continue;
//					inChannel.read(buffer);
//					buffer.flip();
					//continue;
				}
				
				// %%% DYNAMIC %%% ( stream reading) 
				// Does the mainTagStart and mainTagEnd from config file exists 
				// or *dynamically *find the mainTagStart and mainTagEnd
				// dynamic...
//				if(isDynamic){
//					System.out.println("isDynamic(true)..file found:"+fileinput.getAbsolutePath());
//					
//					int endIndex2=0;
//					int beginIndex2=0;int beginIndexOffset2=0;
//					
//					//find the mainTagEnd*
//					beginIndex2=concLine.indexOf(".html");
//					beginIndexOffset2=6;
//					if(beginIndex2==-1){
//						beginIndex2=concLine.indexOf(".htm");
//						beginIndexOffset2=5;
//					}
//					if(beginIndex2==-1){
//						beginIndex2=concLine.indexOf("?");
//						beginIndexOffset2=2;
//					}
//					if(beginIndex2==-1){
//						beginIndex2=concLine.indexOf("@local");
//						beginIndexOffset2=7;
//					}
//					
//					endIndex2=concLine.indexOf("host.",beginIndex2);
//					concLine=concLine.toLowerCase().replace("</", "<#");
//					System.out.println("\n!!!..begin,end::"+beginIndex2 +" "+ endIndex2+"host".length());
//					
//					if(beginIndex2 >=0 && endIndex2>=0){
//							
//						String mainTagEnd=concLine.substring(beginIndex2, endIndex2+"host".length());
//						mainTagEnd=mainTagEnd.replace("$$$", "").replace("\\", "");
//						System.out.println("mainTagEnd:"+mainTagEnd);
//						//does adding ) exists ( meaning second duplicate of link exists along with pattern expected
//						if(concLine.indexOf(mainTagEnd+")")>=0){
//								mainTagEnd=mainTagEnd+")";
//						}
//						String startPattern2=mainTagEnd;
//						String endPattern2=mainTagEnd;
//						
//						cnt_calling_getDynamicConfigurationInXMLFeedFileThenParseForFeature++;
//						writerDebug.append("\n.calling.times.getDynam..():"
//									+cnt_calling_getDynamicConfigurationInXMLFeedFileThenParseForFeature
//									+" beginIndex2:"+beginIndex2 
//									+" endIndex2:"+endIndex2
//									);
//						writerDebug.flush();
//						
//						// dont call same file again
//						//if(!mapPastRunFileName.containsKey(fileinput.getAbsoluteFile().toString())){
//						String wrote_out_Line=
//						// call dynamic 
//						getDynamicConfigurationInXMLFeedFileThenParseForFeature(
//																			 fileinput, 
//																			 concLine,
//																			 startPattern2,
//																			 endPattern2,
//																			 beginIndex2,
//																			 beginIndexOffset2,
//																			 endIndex2,
//																			 mapDuplicateCheck,
//																			 writerDynamic, 
//																			 writerDebug
//																			 );
//						
//						mapDuplicateCheck.put(fileinput.getName()+"!!!"+wrote_out_Line 
//											 ,"dummy"  ); 
//						
//						mapPastRunFileName.put(fileinput.getAbsolutePath(), "dummy");
////						}
////						else{
////							//already ran the file
////							break;
////						}
//						concLine=concLine.substring(endIndex2 ,
//													concLine.length());
//						buffer.clear();
//						continue;
//					}
//					else{
//						buffer.clear();
//						continue;
//					}
//					//only if NOT STREAM
//					//if(isStreamInputFile==false) 
//						//return 1;
//				}// Dynamic
				
				TreeMap<Integer, String> mapOuterMain=new TreeMap<Integer, String>();
				int id=0;
				
				if(origconcLine.length()>=100){
					//System.out.println("origconcLine:"+origconcLine.substring(0, 99));
				}
				
				if(concLine.length()>=100){
					if(isSOPprint){
						System.out.println("concLine(part):"+concLine.substring(0, 99)+"<-----(part)");
					}
				}
				
//				writerDebug.append("\n concLine:------@@@@@--->\n"+concLine+"\n<------@@@@@@@@@@--------"
//									+temp_startPattern+"----"+temp_endPattern); 
				writerDebug.flush();
				 
				String [] arrayofCSVelement=null;
				String csvElements=null;
				// is RDF
				if(is_RDF_file==false){
					csvElements=mapParser.get("csvElements");
				}
				else{
					csvElements=mapParser_RF.get("csvElementsRDF");
				}
				
				if(csvElements!=null){
					arrayofCSVelement=csvElements.split(";");
				}
				int cnt=0; int cnt2=0;
				
				writer.flush();
				// 
				//for(int s:mapOuterMain.keySet()){
					//writer.flush();
					//strFeature=mapOuterMain.get(s);  // ?????
					strFeature=concLine;
					
					cnt=0;
				//static
				if(!isDynamic
					&&  concLine.indexOf(temp_startPattern)>=0
					&& concLine.indexOf(temp_endPattern, concLine.indexOf(temp_startPattern)+2 )>=0){
					
					if(isSOPprint){
						System.out.println("****xmlParser:"+mapParser);
						System.out.println("Given pattern(start,end):"+temp_startPattern+" "+temp_endPattern);
						System.out.println("**************");
						System.out.println(";strFeature(concLine):"+strFeature);
						System.out.println("arrayofCSVelement.length:"+arrayofCSVelement);
						System.out.println("**************");
					}
//					writerDebug.append("\narrayofCSVelement.length:"+arrayofCSVelement.length
//										+";"+arrayofCSVelement[0]+";"+arrayofCSVelement[1]);
					writerDebug.flush();
					
					//moved this to a method "extract_Features_as_Per_Static_Config"
					while(cnt<arrayofCSVelement.length){
						if(isSOPprint){
							System.out.println("-------------main entry---------"+arrayofCSVelement[cnt]);
						}
						cnt2=0;
						String[] s2=arrayofCSVelement[cnt].toLowerCase().replace(".", "#").split("#");
						String []s3=null;
						String currFeature=s2[0].toLowerCase();
						if(isSOPprint){
							System.out.println("@@@arrayofCSVelement:"+arrayofCSVelement[cnt] +"; s2.len:"+s2.length);
						}
						//current* split of one of main csvElement
						while(cnt2<s2.length){
							// function* here you go
							if(ConvertPatternStringToMap.isFunction(s2[cnt2])){
								//remove tag
								if(s2[cnt2].indexOf("removetag")>=0){
									if(isSOPprint){
										System.out.println("removetag:");
									}
									currFeature=
												ConvertPatternStringToMap.
												convert_to_targetStringForFunctionWithArguments(currFeature
																								,"removetag"
																							    ,""
																							    ,"");
								}
								//substring function
								if(s2[cnt2].indexOf("substr")>=0){
									s3= ConvertPatternStringToMap.
										convertPattern_SubStringString2_StartEndIndexArrayString(s2[cnt2]);
									
									if(isSOPprint){
										System.out.println("*fn:substr ; s2[cnt2]:"+s2[cnt2]+" -- "+s3.length
															+";s3[0]:"+s3[0]
															+";cnt2:"+cnt2+";s2[cnt2]:"+s2[cnt2]
															+";s2.len:"+s2.length+"<-");
									}
									
									currFeature=currFeature.substring( Integer.valueOf(s3[0]), 
																	   Integer.valueOf(s3[1]));
				 
								}
								//replace function
								if(s2[cnt2].indexOf("replace")>=0){
									//currFeature=currFeature.replace("replace(", "");
									if(isSOPprint){
										System.out.println("*fn:replace: s2[cnt2]:"+s2[cnt2]);
									}
									s3=s2[cnt2].replace("replace(", "").replace(")", "").split("to");
									
									if(isSOPprint){
										System.out.println("target:"+
														ConvertPatternStringToMap.
														convert_to_targetStringForFunctionWithArguments(
																										currFeature,
																										"replace",
																										s3[0],
																										s3[1])
														);
									}
									currFeature=	ConvertPatternStringToMap.
													convert_to_targetStringForFunctionWithArguments(
													currFeature,
													"replace",
													s3[0],
													s3[1]);
			 
								}
							} // ELSE: it is an element* extraction (example: <head>to</head>)
							else{
								s3= ConvertPatternStringToMap.convert_to_Pattern_into_arrayString(s2[cnt2].replace("/", "#"));
								
								if(s3.length>1){
									int begin=strFeature.indexOf(s3[0]);
									int end=strFeature.indexOf(s3[1],begin+s3[0].length());
									if(isSOPprint){
										System.out.println("*fn.element:>1: *NOT* fn-> s2[cnt2]=>"+s2[cnt2]+"((s3[0]=>"+s3[0]+" s3[1]=>"+s3[1]+"))"
														  +";\n begin,end:"+begin+","+end
														  +";substr:"+strFeature.substring(begin, end)
														  +";\n*****strFeature*start*******=>"
														  +strFeature+"\n<=********strFeature end*******");
									}
//									writerDebug.append("\n*fn.element:>1: *NOT* fn-> s2[cnt2]=>"+s2[cnt2]+"((s3[0]=>"+s3[0]+" s3[1]=>"+s3[1]+"))"
//											  +";\n begin,end:"+begin+","+end
//											  +";\n*****strFeature*start*******=>"
//											  +strFeature+"\n<=********strFeature end*******");
//									writerDebug.flush();
									if(isSOPprint){
										System.out.println("*2fn.element:>1: not fn-> s2[cnt2]=>"+s2[cnt2]+" s3[0]=>"+s3[0]+" s3[1]=>"+s3[1]);
										System.out.println(
															"@@@@@@ substring-> begin:"+ begin+" end:"+end
															 +" last.currFeature:"+currFeature
														  );
									}
									
									//if(begin>=0 && end>=0){
										currFeature=strFeature.substring(begin, end);
//									}
//									else{
//										buffer.clear();
//										continue;
//									}
								
								}
								else{//either not function or function with no argument
									if(isSOPprint){
										System.out.println("*not %#$@#$#$# fn:=1:s2[cnt2]:"+s2[cnt2]+" s3[0]:"+s3[0]);
									}
								}
							}
							
							if(isSOPprint){
								System.out.println("------###------------cnt2:"+cnt2+";currFeature:"+currFeature);
							}
							cnt2++;
						}
						if(isSOPprint){
							System.out.println("------****------------cnt:"+cnt);
						}
						cnt++;
						
						currFeature=currFeature.replace("  ", " ").replace("/n", "")
												.replace(" !!! ", "!!!")
												.replace(" !!!", "!!!")
												.replaceAll("\\p{Cntrl}", ""); 
						
						if(is_debug_WriteMore)
							writerDebug.append("\ncurrFeature:"+currFeature);
							writerDebug.flush();
						
						concOutputCSVLine=concOutputCSVLine+"!!!"+currFeature.replace("#", "/");
						writer.flush();
					 } //end for arrayofCSVelement*
					
				} // isDynamic==false
//				else{
//					System.out.println("continue..");
//					buffer.clear();
//					inChannel.read(buffer);
//					buffer.flip();
//					//continue;
//				}
				
					//continueParsingFromThisPatternMMM is NOT blank
//					if(concOutputCSVLine.indexOf(continueParsingFromThisPatternMMM)>=0
//						&& continueParsingFromThisPatternMMM.length()>0){
				
				concOutputCSVLine=concOutputCSVLine.replace("    ", " ").replace("   "," ")
												   .replace("  "," ").replace(" !!!", "!!!");
				
				if(continueParsingFromThisPatternMMM.length()>2){
					id2++;
					//
					if(concOutputCSVLine.indexOf(continueParsingFromThisPatternMMM)>=0){
						 writer.append("\nnew(2):"+id2+"!!!"+fileinput.getName()+" "+concOutputCSVLine);
						 writer.flush();
					}
				}
				else{
					id2++;
					
					writer.append("\nnew(0):"+id2+"!!!"+fileinput.getName()+" "+concOutputCSVLine);
					
					writer.flush();
				}
				//writer.append("\ncontinueParsingFromThisPatternMMM:"+continueParsingFromThisPatternMMM);
				//writer.append("\n");
				
				
					//}
					//concOutputCSVLine=BLANK 
//					if(concOutputCSVLine.indexOf(continueParsingFromThisPatternMMM)==-1
//							 ){
//							writer.append("\nnew(>0):"+fileinput.getName()+" "+concOutputCSVLine);
//							writer.append("\n");
					//	}
					
					writer.flush();
					concOutputCSVLine="";
					
					writerSuccess.append("\n"+fileinput.getName());
					writerSuccess.flush();
					//buffer.clear();
					//concOutputCSVLine="";
					
					//part of next startPattern in this concLine, take that part (substring)
					if(concLine.indexOf(temp_startPattern, 
										concLine.indexOf(temp_startPattern) + 2 )>=0){
						concLine=concLine.substring(concLine.indexOf(temp_endPattern), 
													concLine.length()); // reset
					}
					else{
						concLine="";
					}
					
					
					if(left_concLine.length()>0)
						concLine=left_concLine;
					
					
			} // while loop end of Reading file stream
	        
		 } // isStreamInputFile==true
	       
		  TreeMap<Integer,String> mapOuterMain= new TreeMap<Integer, String>();
		  int beginIndex=-1;int endIndex =-1;
			//NOT STREAM*
			if(isStreamInputFile==false){
				int id=0;
				mapOuterMain= new TreeMap<Integer, String>();
				
				String [] arrayofCSVelement=null;
				String csvElements=null;
				// is RDF
				if(is_RDF_file==false){
					csvElements=mapParser.get("csvElements");
				}
				else{
					csvElements=mapParser_RF.get("csvElementsRDF");
				}
				
				if(csvElements!=null){
					arrayofCSVelement=csvElements.split(";");
				}
				
				// extract each news item article from concLine
				while (concLine.indexOf(temp_startPattern) >= 0
						&& concLine.indexOf(temp_endPattern) >= 0) {
					id++;
					beginIndex = concLine.indexOf(temp_startPattern);
					endIndex = concLine.indexOf(temp_endPattern, beginIndex+2) - 1;
					//
					if(endIndex<beginIndex){
						endIndex=concLine.length();
					}
					
					String currOuterMain = concLine.substring(beginIndex, endIndex);
					//preserve CASE 
					String currOuterMain_ORIGINAL=ORIG_concLine.substring(beginIndex, endIndex);
					
					//System.out.println("currMain:"+currOuterMain);
					mapOuterMain.put(id, currOuterMain);
					//writer.append("\ncurrMain:"+currOuterMain+"<-");
					//writer.flush();
					
					
					if(isSOPprint){
						System.out.println("loop():mapOuterMain.size:"+mapOuterMain.size()
										+":startPattern:"+startPattern+";endPattern:"+endPattern
										+";beginIndex:"+beginIndex+";endIndex:"+endIndex);
					}
					
					//extract_Features_as_Per_Static_Config (this method gets feature value in both LOWER and UPPER case)
					String outParsed=extract_Features_as_Per_Static_Config(arrayofCSVelement,
																		   currOuterMain,	
																		   currOuterMain_ORIGINAL,// currOuterMain,	 [currOuterMain_ORIGINAL->CASE PRESERVED]
																		   writerDebug,
																		   is_preserve_CASE, //is_preserve_CASE
																		   false //isSOPprint
																		   );
					
					// 
					outParsed=outParsed.replace("   ", " ").replace("  ", " ").replace("  ", " ").replace("\n", "")
										.replace(" !!! ", "!!!")
										.replace(" !!!", "!!!");
					
					String o_write=	"!!!"+fileinput.getName()+
									outParsed.replace("\r", "").replace("\t", "").replaceAll("\\p{Cntrl}", "");
					
					
					writer.append("\nnew(fn)::"+o_write);
					writer.flush();
					
					concLine = concLine.substring(endIndex  , concLine.length());
					//preserve original
					ORIG_concLine=ORIG_concLine.substring(endIndex  , ORIG_concLine.length());
					
					if(is_debug_WriteMore){
						writerDebug.append("\n"+id+"\t"+currOuterMain);
						writerDebug.flush();
					}
					//System.out.println("end of concLine "+id);
				} //END // extract each news item article from concLine
				
				
				
				
				if(isSOPprint){
					System.out.println("mapOuterMain.size:"+mapOuterMain.size()
									+":temp_startPattern:"+temp_startPattern+";temp_endPattern:"+temp_endPattern
									+";beginIndex:"+beginIndex+";endIndex:"+endIndex
									+";concLine.indexOf(temp_startPattern):"+concLine.indexOf(temp_startPattern) 
									+";concLine.indexOf(temp_endPattern):"+concLine.indexOf(temp_endPattern));
				}
				
				if(is_debug_WriteMore){
					writerDebug.append("\nmapOuterMain.size:"+mapOuterMain.size());
					writerDebug.append("\nconcLine:"+concLine);
				}
				
				writerDebug.flush();
			} //end of IF not stream
	        
			//System.out.println("end of concLine 2 OUTER "+i);
	        writerSuccess_only_static.append("\n"+fileinput.getAbsolutePath());
			writerSuccess_only_static.flush();
	        
			if(isSOPprint){
				System.out.println("static end: isStreamInputFile:"+isStreamInputFile);
				System.out.println("mapOuterMain.size:"+mapOuterMain.size());
			}
			
		}//static if end
	    
			// Dynamic
			if(    isDynamic
				&& is_run_only_dynamic ==true
			  ){
				//dynamic
				ReadParseXMLFeeds.
				getDynamicConfigurationInXMLFeedFileThenParseForFeature2(
																		 fileinput.getAbsolutePath(), 
																		 writerDynamic, 
																		 writerDebug
																		 );
				writerSuccess_only_dynamic.append("\n"+fileinput.getAbsolutePath());
				writerSuccess_only_dynamic.flush();
				if(isSOPprint){
					System.out.println("dynamic end");
				}
			}
			
	        
			//} //end for mapOuterMain*
			
			
//			String graphtopology=mapConfig.get("s1.parser");
//
//			TreeMap<Integer,TreeMap<Integer,String>> mapgraphtopology=
//												convertPatternStringToMap.convertStringToMap(graphtopology,
//																							 ",","->");
//			System.out.println("****xmlParser2:"+mapgraphtopology);
 
			if(isSOPprint){
				System.out.println("xml.Time Taken:" +NANOSECONDS.toSeconds(System.nanoTime() - t0)
									+" file:"+fileinput.getAbsolutePath());
				System.out.println("flag:"+ Go_AFTER_found_ContinueParsingFromThisPattern+" "
								  	      + YES_Go_AFTER_found_ContinueParsingFromThisPattern);
			}
		     inChannel.close();
		     aFile.close();
			
			 writerSuccess.append("\n"+fileinput.getAbsolutePath());
			 writerSuccess.flush();
//			 writerSuccess.close();
//			 writerSuccess_only_static.close();
//			 writerSuccess_only_dynamic.close();
		}
		catch(Exception e){
			
			try {
				writerFailure.append("\n"+fileinput.getAbsolutePath());
				writerFailure.append("\n"+fileinput.getAbsolutePath()+"!!!"+e.getMessage());
				writerFailure.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		return 0;
	}
	
//	  Below example from "feeditems-3.rdf"
//	  <RDF:Description RDF:about="urn:feeditem:http:%2f%2fibnlive.in.com%2fnews%2fmilk-turning-sour-for-city-folk%2f295932-60-123.html"
//    fz:stored="true"
//    fz:last-seen-timestamp="1420308243371"
//    fz:valid="true">
//	  <fz:feed RDF:resource="http://ibnlive.in.com/ibnrss/rss/southindia/thiruvananthapuram.xml"/>
//    </RDF:Description>
//    <RDF:Description RDF:about="urn:feeditem:http:%2f%2fibnlive.in.com%2fnews%2f14-suspected-simi-members-who-were-arrested-last-year-get-bail%2f460033-3-235.html"
//    fz:stored="true"
//    fz:last-seen-timestamp="1420272874117">
//	  <fz:feed RDF:resource="http://ibnlive.in.com/ibnrss/rss/india/chhattisgarh.xml"/>
//	  </RDF:Description>
//   mainStart and End Tag=> <RDF:Description </RDF:Description>
	//
	public static void parse_RDF_Feed_File(String inputRDFFile){
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//wrapper read and parse XML feeds
	public static void wrapper_readParseXMLFeeds(
												  String inputFolderName
												, String outputFileName1
												, String slashtype
												, File[] listFiles
												)
												{
		try{
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	// read all xml feeds file in a folder and parse each of xml feed file
	public static void readParseXMLFeeds( String inputFolderName
										, File [] inputarrayFiles // either inputFolderName or inputarrayFiles to be given
										, String outputFileName_Static
										, boolean is_Append_outFile_Static
										, String outFile_Static_eachLineNEWSstring
										, String outputFileName_Dynamic
										, boolean is_Append_outFile_Dynamic
										, String slashtype
										, boolean is_run_only_static
										, boolean is_run_only_dynamic
										, boolean is_run_only_eachLineNEWSstring
										, String continueParsingFromThisPatternMMM
										, String continueParsingFromThisPatternYYYY
										, String confFile
										, String past_already_ran_success_file_for_skipping
										, boolean is_consider_past_already_ran_success_file_for_skipping
										, FileWriter writerSuccess
										, FileWriter writerSuccess_only_static
										, FileWriter writerSuccess_only_dynamic
										, FileWriter writerFailure
										, FileWriter writerDebug
										, TreeMap<String, String> mapConfig
										, boolean Go_AFTER_found_ContinueParsingFromThisPattern
										, boolean isStreamingInputFile
										, boolean is_convert_to_lowercase
										, boolean isSOPprint
										, boolean is_debug_WriteMore
										){
		continueParsingFromThisPatternMMM=continueParsingFromThisPatternMMM.toLowerCase();
		File file=null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String absolutePathOfInputFile=null;
		 Runtime rs =  Runtime.getRuntime();
		String Parent=null; 
		File[] Filenames=null;
		File fileinput=null;
		TreeMap<String, String> mapTree_Past_Already_Ran_Files=new TreeMap<String, String>();
		//is input folder name given
		if(inputFolderName.length()>2){ //BLANK inputfile name
			file = new File(inputFolderName);
			
				if(file.isDirectory() == false)
				{
					System.out.println(" Given file is not a directory");
				//	return;
				}
			  
	          absolutePathOfInputFile = file.getAbsolutePath();
	          Parent =  file.getParent(); 
	        
	          Filenames = file.listFiles();
		}
        else{
        	 Filenames = inputarrayFiles;
        }
        System.out.println("absolutePathOfInputFile:"+absolutePathOfInputFile);
        System.out.println("Parent:"+Parent);
        System.out.println("inputFolderName:"+inputFolderName);

  	  try{
  		  //past_already_ran_success_file>0 , load
  		  if(past_already_ran_success_file_for_skipping!=null ){
  			  
  			  if(is_consider_past_already_ran_success_file_for_skipping==true){	  
  			  
		  			 //pass dummy will skip
		  			past_already_ran_success_file_for_skipping=past_already_ran_success_file_for_skipping.replace("dummy", "");
		  			if(past_already_ran_success_file_for_skipping.length()>0 && 
		  					new File(past_already_ran_success_file_for_skipping).exists()
		  					){
		  			  // 
		  			  mapTree_Past_Already_Ran_Files=
		  					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
			  			  .readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
			  			  	(past_already_ran_success_file_for_skipping, 
			  			  		-1, -1
			  			  		, ""
			  			  		, false  //is_Append_outFile 
			  			  		, false //is_Write_To_OutputFile
			  			  		, " already load"// debug_label
			  			  		, -1 //token for primary key
			  			  		, true //boolean isSOPprint 
			  			  		);
		  			}
  			
  			  }// is_consider_past_already_ran_success_file_for_skipping
  		  }
  		  System.out.println("mapTree_Past_Already_Ran_Files.size:"+mapTree_Past_Already_Ran_Files.size());
  		  // continue
		    //String option="n";
//			option=crawler.read_input_from_command_line_to_continue_Yes_No(br);
//			
//			if(!option.equalsIgnoreCase("y"))
//			   	  System.exit(1);
  		
        //String outputFileName1 = "C:\\Documents and Settings\\lmookiah\\Desktop\\custom work\\output.txt";
        FileWriter writer = new FileWriter(outputFileName_Static, is_Append_outFile_Static);
        FileWriter writer_eachLineNEWSstring =new FileWriter(outFile_Static_eachLineNEWSstring, is_Append_outFile_Static);
        
        FileWriter writerDynamic = new FileWriter(outputFileName_Dynamic, is_Append_outFile_Dynamic);

        
        
		//While loop to read each file and write into.
		for (int i=0; i < Filenames.length; i++)
		{
			rs=Runtime.getRuntime();
		  try{
			  //absolutePathOfInputFile+ slashtype +
			  fileinput =  Filenames[i];
			   
			  System.out.println("Filenames:"+fileinput.getAbsolutePath() +" total files:"+Filenames.length);
			  
			  if(Filenames[i].getAbsolutePath().toLowerCase().indexOf("store")>=0
					  && Filenames[i].getAbsolutePath().toLowerCase().indexOf(".ds")>=0){
				  //i++;
				  System.out.println(" caught ds_store..");
				  continue;
			  }
			  
			  System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%%");
			  System.out.println("\npast_already_ran_success_file_for_skipping:"+past_already_ran_success_file_for_skipping);
			  for(String st:mapTree_Past_Already_Ran_Files.keySet()){
				  System.out.println("\n (PAST ran LOADING)-->"+st);
			  }
			  System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%%");
			  
			  //System.out.println("file1:"+ Filenames[i].getAbsolutePath().toLowerCase());
			  // past already run over
			  if(mapTree_Past_Already_Ran_Files.containsKey(Filenames[i].getName())){
				  System.out.println("file1.1.SKIPPING******past exist:(primary)***"+ Filenames[i].getName());
				  continue;
			  }
			  else if(mapTree_Past_Already_Ran_Files.containsKey(Filenames[i].getAbsolutePath())){
				  System.out.println("file1.1.SKIPPING***past *****exist***:(full)"+ Filenames[i].getAbsolutePath());
				  continue;
			  }
			  else{
				  System.out.println(" @@@@NOT@@@@ exist:"+ Filenames[i].getName());
			  }
			    //option="n";
			// continue
//	 			option=crawler.read_input_from_command_line_to_continue_Yes_No(br);
//	 			if(!option.equalsIgnoreCase("y"))
//	 			   	  System.exit(1);
					   
			  // already crawled (load path from past_already_ran_success_file.txt
			  
			  if(isSOPprint){
				  System.out.println ("File reading::"+absolutePathOfInputFile+ slashtype + Filenames[i]);
			  }
			  
			  System.out.println ("Processing full path fileinput START:"+fileinput.getAbsoluteFile());
			  
			  // reading / parsing single xml feed
			  readParseSingleXMLFeed(   fileinput , 
					  					fileinput 
					  				  , absolutePathOfInputFile
									  , is_run_only_static
									  , is_run_only_dynamic
									  , is_run_only_eachLineNEWSstring
					  				  , writerSuccess
					  				  , writerSuccess_only_static
					  				  , writerSuccess_only_dynamic
					  				  , writerFailure
					  				  , writer
					  				  , is_Append_outFile_Static  //obsolete
					  				  , writer_eachLineNEWSstring
					  				  , writerDynamic
					  				  , is_Append_outFile_Dynamic //obsolete
					  				  , i 
					  				  , continueParsingFromThisPatternMMM
					  				  , continueParsingFromThisPatternYYYY
					  				  , confFile
					  				  , Filenames[i]
					  				  , writerDebug
					  				  , mapConfig
					  				  , Go_AFTER_found_ContinueParsingFromThisPattern
					  				  , isStreamingInputFile
					  				  , is_convert_to_lowercase
					  				  , isSOPprint
					  				  , is_debug_WriteMore
					  				  );
			  System.out.println ("Processing full path fileinput END...");
			  
			  rs.gc();//garbage
			  //jvmtiError ForceGarbageCollection(jvmtiEnv* env);
			  writer.flush();
		  }
		  catch(Exception e)
		  {
			  System.out.println("An exception occurred while calling Write method ..!!");	
			  e.printStackTrace();
		  }
		}
		writer.close();
  	   } //end try
		 catch(Exception e){
			 System.out.println("error.");
			 //
			 try {
				writerDebug.append("\nreadParseXMLFeeds:"+e.getMessage()); 
				writerDebug.append("\n full path fileinput :: "+ fileinput.getAbsolutePath());
				writerDebug.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
			}
			 
			 e.printStackTrace();
		 }
	}
	// short string with first or last digit numeric
	public static boolean ShortStringWITHFirstORlastDigitNumeric(String inString){
		
		inString=inString.replace("$$$", "").replace(" ", "").replace("rss@localhost.localdomain", "")
					.replace("(","").replace(")","");
		try{
			System.out.println("shortstring:"+
							IsInteger.isInteger(inString.substring(0, 1), 10)+" "+
							IsInteger.isInteger(inString.substring(inString.length()-1, inString.length()), 10));
			
			boolean firstyes=IsInteger.isInteger(inString.substring(0, 1), 10);
			boolean lastyes=IsInteger.isInteger(inString.substring(inString.length()-1, inString.length()), 10);
 
			if( ((firstyes || lastyes) && inString.length()<=12)
					||  (inString.length()<8)
					|| ( inString.indexOf("&")>=0 && inString.length()<=12)
					|| Find_occurance_Count_Substring.find_occurance_Count_Substring("^", inString)>=2
				 )
				return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	// blindly take it (it works!!!)
	public static String getDynamicConfigurationInXMLFeedFileThenParseForFeature2(
																String   inputFile,
																FileWriter writerDynamic,
																FileWriter writerDebug
																){
		
		int counter_dynamic=0;
		try {
			String s=ReadFile_CompleteFile_to_a_Single_String.
					 readFile_CompleteFile_to_a_Single_String(inputFile);
			TreeMap<Integer,String> map=
			Find_Start_n_End_Index_Of_GivenString.getMatchedLines(s, 
																  "http", ")"
																  , true);
			
//			for(int k:map.keySet()){
//				System.out.println(k+"!!!"+map.get(k));
//			}

			
			//
			for(int k:map.keySet()){
					counter_dynamic++;
					System.out.println("----------------each of map--------");
					int idx=s.indexOf( map.get(k));
					int idxStart=idx-330;
					int idxEnd=idx; //+450;
					String body_1=s.substring(idxStart,
										 idxEnd);
					String body_2="";
//					System.out.println("#########");
//					System.out.println(t);
//					System.out.println("#########end");
					String tng="\n"+"dyn():!!!file:"+
									new File(inputFile).getName()+
									"!!!url:"+map.get(k)+
									"!!!body.1:"+body_1+"!!!";
					
					
					String tmp_body_1=body_1;					
					int cnt_http=Find_occurance_Count_Substring.find_occurance_Count_Substring("http", tmp_body_1);
					// http >1 occur
					if(cnt_http>=2){
						int id2= 0;
						// no HTTP
						while(cnt_http>0){
							System.out.println("cnt_http:"+cnt_http+"!!!"+tmp_body_1);
							
							int idx10=tmp_body_1.indexOf("http");
							id2=  tmp_body_1.indexOf(")", idx10 );
							
							if(id2 >= 0){
								tmp_body_1=tmp_body_1.substring(id2+1, tmp_body_1.length());	
							}
							if(idx10>=0 && id2 ==-1)
								break;
							if(idx10==-1 && id2 ==-1)
								break;
							cnt_http=Find_occurance_Count_Substring.find_occurance_Count_Substring("http", tmp_body_1);
						} 
						
						if(id2>=0){
							System.out.println("id2+1:"+id2+1+"!!!"+tmp_body_1.length());
							tng=tng+"body.2:" + tmp_body_1 +"!!!";
						}
						
					}
					else if(cnt_http==1){
					
						int body_2_idx=tmp_body_1.indexOf("http");
						//
						if(body_2_idx>=0){
							int id2=  tmp_body_1.indexOf(")", body_2_idx );
							if(id2 >= 0 ){
								tng=tng+"body.2:" + tmp_body_1.substring(id2+1, tmp_body_1.length())  +"!!!";
							}
						}
					}
					
					tng=tng+"cnt:"+counter_dynamic+"!!!";
					
					writerDynamic.append(	tng.replace("\r", "").replace("\t", "")
												.replaceAll("\\p{Cntrl}", "")
											+"\n");
					writerDynamic.flush();
					
//					if(t.indexOf("<")>=0){
//						if( t.indexOf(">", t.indexOf("<")+1 )>=0){
//						
//							System.out.println(k+"!@!@!"+t.substring(t.indexOf("<"), 
//										t.indexOf(">", t.indexOf("<")+1 ))+"!!!!!");
//							writerDebug.append("\nfound:"+k+"!@!@!"
//												+t.substring(t.indexOf("<"), 
//															 t.indexOf(">", t.indexOf("<")+1 ))+"!!!!!");
//							String event=t.substring(t.indexOf("<"), 
//									 	 t.indexOf(">", t.indexOf("<")+1 ));
//							String tng="\n"+map.get(k)+"!!!"+event;
//							
//							writerDynamic.append(tng.replace("\r", "").replace("\t", "")
//													.replaceAll("\\p{Cntrl}", "")
//													+"\n");
//							writerDynamic.flush();
//							
//							
//						}
//						else if(t.indexOf("@localhost", t.indexOf("<")+1 )>=0){
//							writerDebug.append("\nfound@local:"+k+"!@!@!"
//									+t.substring(t.indexOf("<"), 
//												 t.indexOf("@localhost", t.indexOf("<")+1 ))+"!!!!!");
//							String event=t.substring(t.indexOf("<"), 
//									 				 t.indexOf("@localhost", t.indexOf("<")+1 ));
//							
//							String tng="\n"+map.get(k)+"!!!"+event;
//							
//							writerDynamic.append("@local!!!"+tng.replace("\r", "").replace("\t", "")
//													.replaceAll("\\p{Cntrl}", "")
//													+"\n");
//							writerDynamic.flush();
//						}
//						else{
//							System.out.println("###found.not:"+map.get(k));
//							writerDebug.append("\n###found.not:"+map.get(k)+"!!!"+t
//													+":::idx:"+idx+":::sub:"+s.substring(idx, idx+100));
//							
//							String tng="\n#found.not::"+map.get(k)+"!!!"+t;
//							
//							writerDynamic.append(tng.replace("\r", "").replace("\t", "")
//													.replaceAll("\\p{Cntrl}", "")
//												 +"\n");
//							writerDynamic.flush();
//							
//						}
					}
					writerDebug.flush();
					//writerDynamic.append("\n");
					writerDynamic.flush();
//			}			
			
			
					System.out.println("got links: "+map.size());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return "";
	}
			
	
	//dynamically get the pattern / dynamically find the mainTagStart and mainTagEnd
	public static String getDynamicConfigurationInXMLFeedFileThenParseForFeature(
																			File   inputFile,
																			String concLine,
																			String startPattern,
																			String endPattern,
																			int beginIndex,
																			int beginIndexOffset,
																			int endIndex,
																			TreeMap<String, String> mapDuplicateCheck,
																			FileWriter writerDynamic,
																			FileWriter writerDebug){
		System.out.print("dynamic.getDynamic..()."+inputFile.getAbsolutePath());
		TreeMap<String, String> mapUniqueURL=new TreeMap<String, String>();
//		TreeMap<String, String> mapDuplicateCheck=new TreeMap<String, String>();
		String option="N"; BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String tmp="";
		int id=0;
		String concStringToWriteResult="";
//		int endIndex=0;
//		int beginIndex=0;
//		int beginIndexOffset=0;
		//last ) missing , so add it
		//concLine=concLine+")";
		try{
			
			writerDebug.append("\n current input *dynamic filename: "+inputFile.getAbsolutePath()
								+"!!!concLine:"+concLine.substring(beginIndex, endIndex));
			writerDebug.flush();
			
			//find the mainTagEnd*
//			beginIndex=concLine.indexOf(".html");
//			beginIndexOffset=6;
//			if(beginIndex==-1){
//				beginIndex=concLine.indexOf(".htm");
//				beginIndexOffset=5;
//			}
//			if(beginIndex==-1){
//				beginIndex=concLine.indexOf("?");
//				beginIndexOffset=2;
//			}
//			if(beginIndex==-1){
//				beginIndex=concLine.indexOf("@local");
//				beginIndexOffset=7;
//			}
//			
//			endIndex=concLine.indexOf("host.",beginIndex);
//			
//			System.out.println("\n!!!..begin,end::"+beginIndex +" "+ endIndex+"host".length());
//			String mainTagEnd=concLine.substring(beginIndex, endIndex+"host".length());
//			mainTagEnd=mainTagEnd.replace("$$$", "").replace("\\", "");
//			System.out.println("mainTagEnd:"+mainTagEnd);
//			//does adding ) exists ( meaning second duplicate of link exists along with pattern expected
//			if(concLine.indexOf(mainTagEnd+")")>=0){
//				mainTagEnd=mainTagEnd+")";
//			}

//			String startPattern=mainTagEnd;
//			String endPattern=mainTagEnd;
			String currOuterMain="";
	
			//int offset=10;
			//
			while (concLine.indexOf(startPattern) >= 0
					&& concLine.indexOf(endPattern, beginIndex+beginIndexOffset) >= 0) {
				id++;
				beginIndex = concLine.indexOf(startPattern)  ;
				endIndex = concLine.indexOf(endPattern, beginIndex+beginIndexOffset) - 1;
				System.out.println("begin,end:"+beginIndex+" "+endIndex
								   +" file:"+inputFile.getAbsolutePath());
				if(endIndex>=0)
					currOuterMain = concLine.substring(beginIndex, endIndex)
									.replace("#", "/").replace("\\$$$", "");
				
				currOuterMain=currOuterMain.replace(")(", "!!!!");
				
				System.out.println("----------");
				
				//(1) split on ")(" (2) once split, get A and B from "A=B"
				
				String []s1=currOuterMain.split("!!!!");
				 
				if(currOuterMain.length()>=100){
					System.out.println("currMain:"+currOuterMain.substring(0, 99)
									  +";sl.len:"+s1.length);				
				}
				
				int cnt=0;
				String newS2html="";
				while(cnt<s1.length){
					if(s1[cnt].indexOf("http:")>=0 && s1[cnt].indexOf("0E")>=0){ 
						cnt++;
						continue;
					}
					String []s2=s1[cnt].split("=");
					newS2html="";
					if(s2.length>=2){
						if(s2[1].indexOf("http://")>=0){
							int begin1=s2[1].indexOf(".");
							int begin2=s2[1].indexOf("/",begin1);
							int end=s2[1].indexOf(".",begin2);
							if(end==-1)
								end=s2[1].indexOf("?",begin2+2);
							if(end>0 && begin2>0 &&end>begin2){
								newS2html=s2[1].substring(begin2, end);
							}
									
						}
					}
					// if this url already wrote, then skip
					 if(mapUniqueURL.containsKey(newS2html)){
						 cnt++;
						 continue;
					 }
					
					//System.out.println("s1:"+s1[cnt]+" s2.len:"+s2.length);
					if(s2!=null){
					if(s2.length>=2){
						String t=s2[1].replace("$$$", "").replace(" ", "");
				
						//not short (>12)
						if(!ShortStringWITHFirstORlastDigitNumeric(s2[1])
								&& !mapUniqueURL.containsKey(newS2html)){
							System.out.println("s2[0]:"+s2[0]+" s2[1]:"+s2[1] +"(s2[1].len:"+s2[1].length()+")");
							concStringToWriteResult=concStringToWriteResult+"!!!"
																			+s2[1];
						}
						//get 3rd element, if second is short
						if( (t.length()<=16 && s2.length>=3) ){
							if( s2[2].replace(" ", "").length() >15  
									&& !s2[2].equalsIgnoreCase("rss@localhost.localdomain") ){
								if(!ShortStringWITHFirstORlastDigitNumeric(s2[2])
									&& !mapUniqueURL.containsKey(s2[2])){
									System.out.println(".3rd,Elm..s2[2]#1---:"+s2[2]);
									concStringToWriteResult=concStringToWriteResult+"!!!"
																				   +s2[2];
								}
							}
						}
						else if(s2.length>=3){
							if( s2[2].replace(" ", "").length() >15
								&& !s2[2].equalsIgnoreCase("rss@localhost.localdomain"))
								if(!ShortStringWITHFirstORlastDigitNumeric(s2[2])
									&& !mapUniqueURL.containsKey(s2[2])){
									System.out.println(".3rd,Elm..s2[2]#2---:"+s2[2]);
									concStringToWriteResult=concStringToWriteResult+"!!!"
											   +s2[2];
								}
						}
					}
				}//not null
					if(s2!=null){
					//
					if(s2.length>=2){
						if(s2[1].indexOf("http://")>=0){
							int begin1=s2[1].indexOf(".");
							int begin2=s2[1].indexOf("/",begin1);
							int end=s2[1].indexOf(".htm",begin2);
							if(end==-1)
								end=s2[1].indexOf(".",begin2);
							if(end==-1)
								end=s2[1].indexOf("?",begin2+2);
							if(end>0 && begin2>0 &&end>begin2)
								mapUniqueURL.put(s2[1].substring(begin2, end), "");
									
						}
					}
					}
					//System.out.println("newS2html:"+newS2html);
					cnt++;
				
				}
				//concStringToWriteResult="###"+concStringToWriteResult;
				concStringToWriteResult="!!!"+inputFile.getName()+"!!!"+ concStringToWriteResult;
				concStringToWriteResult=concStringToWriteResult.replace("!!!!!!", "!!!");
				tmp      ="\ndyn:"
							+concStringToWriteResult.replace("  ", " ").replace("/n", "")
							.replace("\r", "").replace("\t", "")
							.replace(" !!!", "!!!")
							.replaceAll("\\p{Cntrl}", "");
				//
				if(!mapDuplicateCheck.containsKey(inputFile.getName()+"!!!"+ tmp)){
					writerDynamic.append(tmp);
					writerDynamic.flush();
				}
				else{
					//System.out.println("already wrote** ->"+tmp);
				}
	             
				//mapDuplicateCheck.put(tmp, tmp);
//				System.out.println("mapDuplicate:"+mapDuplicateCheck.size()
//									+"::"+tmp);
				
//				// continue
//	 			option=crawler.read_input_from_command_line_to_continue_Yes_No(br);
//	 			
//	 			if(!option.equalsIgnoreCase("y"))
//	 			   	  System.exit(1);
				
				concStringToWriteResult="";
				//mapOuterMain.put(id, currOuterMain);
				//writer.append("\ncurrMain:"+currOuterMain+"<-");
				//writer.flush();
				if(endIndex>=0)
					concLine = concLine.substring(endIndex, concLine.length());
					
			}
			
//			writerDebug.append("\n success getDynamicConfigurationInXMLFeedFileThenParseFeature() :"
//								+ inputFile.getAbsolutePath());
			
			writerDebug.flush();
			
			
		}
		catch(Exception e){
			try{
				writerDebug.append("\n ****failed getDynamicConfigurationInXMLFeedFileThenParseFeature():error:"+e.getMessage());
				writerDebug.append("\n failed:endIndex:"+endIndex+" ;beginIndex:"+ beginIndex);
				writerDebug.flush();
			}
			catch(Exception e2){}
			
			e.printStackTrace();
		}
		return tmp;
	}
		// main method
		public static void main(String[] args) throws IOException {
			String inFile="/Users/lenin/Downloads/Feeds (5-Jun)/Minneapolis";
			inFile="/Users/lenin/Downloads/Feeds (5-Jun)/BBC News - Scotland";
			String outFolder="/Users/lenin/Downloads/output.xmlfeed/";
			//folder
			String inFolder="/Users/lenin/Downloads/Feeds-5-Jun";
				   inFolder="/Users/lenin/Downloads/Feeds-TT-Feed-3-Sep/Feed-US-TT-Set-1-backup-3-Sep/";
			String debugFile=outFolder+"debug.txt";
			String alreadySuccessfullyProcessedFiles=outFolder+"success_all.txt";
		    String alreadyFailureProcessedFiles=outFolder+"failure.txt";
		    String outFile_Static=outFolder+"output_static.txt";
		    String outputFileName_Dynamic=outFolder+"output_dynamic.txt";
		    String past_already_ran_success_file=outFolder+"success.txt";
		    String outputFileName_Only_Static=outFolder+"success_only_static.txt";
		    String outputFileName_Only_Dynamic=outFolder+"success_only_dynamic.txt";
		    String continueParsingFromThisPatternMMM="";
		    String continueParsingFromThisPatternYYYY="";
		    boolean Go_AFTER_found_ContinueParsingFromThisPattern=false;
		    new File(debugFile).delete();
		    new File(alreadySuccessfullyProcessedFiles).delete();
		    new File(alreadyFailureProcessedFiles).delete();
		    //create if not exist
		    if(!new File(alreadySuccessfullyProcessedFiles).exists() )
		    	new File(alreadySuccessfullyProcessedFiles).createNewFile();
		    
			FileWriter writerDebug= new FileWriter(new File(debugFile), true);
			FileWriter writerSuccess= new FileWriter(new File(alreadySuccessfullyProcessedFiles), true);
			FileWriter writerFailure= new FileWriter(new File(alreadyFailureProcessedFiles), true);
			FileWriter writerSuccess_only_static= new FileWriter(new File(outputFileName_Only_Static), true);
			FileWriter writerSuccess_only_dynamic= new FileWriter(new File(outputFileName_Only_Static), true);
			
			File file = new File(inFolder);
			File[] Filenames = file.listFiles();
			
			continueParsingFromThisPatternMMM=""; //not working
			continueParsingFromThisPatternYYYY=""; //not working
			Go_AFTER_found_ContinueParsingFromThisPattern=true;
			//read and parse XML feeds
			//readParseXMLFeeds(inFolder, Filenames , outFile,"/");
			
			writerDebug.append("\nBefore exec: number of files:Filenames.len:"+Filenames.length);
			writerDebug.flush();
		    final long t0 = System.nanoTime();
		    String confFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/2.readparsexmlfeeds/1.xmlFeedconfig.txt";
		    TreeMap<String, String> mapConfig = Crawler.getConfig(confFile);
		    boolean is_Append_outFile_Static=true;
		    boolean is_Append_outFile_Dynamic=true;
		    boolean is_run_only_dynamic=false;
			boolean is_run_only_static=true;
			boolean is_run_only_eachLineNEWSstring=true;
		    
		    // read parse xml feeds for static
			readParseXMLFeeds("",   //inputFolderName
							  Filenames , //array of input Files // either inputFolderName or inputarrayFiles to be given
							  outFile_Static,
							  is_Append_outFile_Static,
							  "",//outFile_Static_eachLineNEWSstring
							  outputFileName_Dynamic,
							  is_Append_outFile_Dynamic,
							  "/", 
							  is_run_only_eachLineNEWSstring,
							  is_run_only_static,
							  is_run_only_static,
							  continueParsingFromThisPatternMMM,
							  continueParsingFromThisPatternYYYY,
							  confFile,
							  past_already_ran_success_file,
							  true,// is_consider_past_already_ran_success_file_for_skipping
							  writerSuccess, 
							  writerSuccess_only_static,
							  writerSuccess_only_dynamic,
							  writerFailure, 
							  writerDebug,
							  mapConfig,
							  Go_AFTER_found_ContinueParsingFromThisPattern,
							  true, //isStreamingInputFile
							  false, //is_convert_to_lowercase
							  true, //isSOPprint
							  true //is_debug_WriteMore
							  );
			
			String writer_dyn="/Users/lenin/Downloads/output.xmlfeed/dyn.txt";
			FileWriter writerDynamic=new FileWriter(new File(writer_dyn));
			String inputFile="/Users/lenin/Downloads/Feeds-29-Jun-SplitBase/2(1.0-2.0).dummy/Africa - Voice of America.msf";
			boolean is_Append_outFile=false;
			inputFile="/Users/lenin/Downloads/Feeds-29-Jun-SplitBase/2(1.0-2.0).dummy/BBC News - Africa.msf";
			inputFile="/Users/lenin/Downloads/Feeds-29-Jun-SplitBase/2(1.0-2.0).dummy/BBC News - Northern Ireland.msf";
			 writerDebug= new FileWriter(new File(debugFile));
			 
			//get parsed the dynamic one
//			getDynamicConfigurationInXMLFeedFileThenParseForFeature2(
//										  inputFile,
//										  writerDynamic,
//										  writerDebug);
			
			//test case: use files-> News-4 ; Breaking; NYT  Liberia..msf; RT - Daily news
		    System.out.println("Time Taken:" + NANOSECONDS.toSeconds(System.nanoTime() - t0) +" seconds; "
					       	   + (NANOSECONDS.toSeconds(System.nanoTime() - t0))/60 +" minutes");
		    //
		}
}