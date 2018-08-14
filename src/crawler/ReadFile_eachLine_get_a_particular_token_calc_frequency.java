package crawler;
/*
 * (1) Read a folder name 
 * (2) Read all the text files in it.
 * (3) Merge the files into a single file "output.txt"
 * 
 * Note: Merge here is only horizontal merge
 * 
 * */ 

import crawler.Stemmer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import crawler.Ltrim;

import p6_new.Cleaning_and_convert;
import p6_new.P6_MAIN_Work;


	//readFile_eachLine_get_a_particular_token_calc_frequency
	public class  ReadFile_eachLine_get_a_particular_token_calc_frequency {

		//merge Files calc_Frequency_Of_Token_AND_output_unique_values_in_token
		public static TreeMap<String, Integer> calc_Frequency_Of_Token_AND_output_unique_values_in_token(
													  String 	inputFolderName_OR_Single_File,
													  String 	delimiter_4_inputFolderName_OR_Single_File,
													  String 	outputFileName1, //OUTPUT
													  boolean	is_Append_outputFileName1,
													  String    outputFileName_onlykeywords,
													  boolean	is_Append_outputFileName_onlykeywords,
													  String    outputFileName_WORD_DOCUMENT_FREQ,
													  int       index_of_token_interested_for_freq_count, //starts with 1
													  int		index_of_token_of_SourceURL, //used only is_token_interested__authors==true
													  int       index_of_token_of_Source_FileName, //used only is_token_interested__authors==true
													  String    replace_string_for_sourceChannelFilePath, //used only is_token_interested__authors==true
													  boolean   is_token_interested__authors,
													  boolean   is_split_on_blank_space_in_token,
													  boolean   is_do_stemming_on_word,
													  String    YES_Filter,
				                                      String    NO_Filter,
													  boolean 	isMac,
													  boolean   is_split_CSV_on_the_given_token,
													  String    output_debugFile,
													  boolean   is_add_last_token_of_inputFolderName_to_OUTPUT,
				                                      boolean   isSOPprint
									  				  ){
			int total_num_of_lines=0;
			TreeMap<String, Integer> mapToken_Freq=new TreeMap();
			TreeMap<String, Integer> mapTokenONLYalphaNumeric_Freq=new TreeMap();
			TreeMap<String, String> mapToken_DomainName =new TreeMap<String,String>();
			
			TreeMap<String, Integer> map_alreadyWrote_authNameANDsourceChannel_AS_key=new TreeMap<String, Integer>();
			TreeMap<String, String> map_authNameANDsourceChannel_N_newlinePARTIAL=new TreeMap<String, String>();
			
			TreeMap<String, List<Integer> > map_Word_UniqueDocumentIdHavingIT=new TreeMap();
			TreeMap<String, String> mapToken_LastToken=new TreeMap();
			TreeMap<String, String> mapTokenONLYalphaNumeric_LastToken=new TreeMap();
			Stemmer stemmer=new Stemmer();
			
			File file = new File(inputFolderName_OR_Single_File);
			boolean isDirectory=false;
			String[] Filenames=new String[1];
			if(file.isDirectory() == true)
			{
				isDirectory=true;
				System.out.println(" Given file is not a directory");
			//	return;
			}
			
	        String absolutePathOfInputFile = file.getAbsolutePath();
	        String Parent =  file.getParent();
	        String line="";
	        if(isDirectory)
	        	Filenames = file.list();
	        else{
	        	Filenames[0]=new File(inputFolderName_OR_Single_File).getName();
	        	System.out.println("single file:"+Filenames[0]);
	        }
	        
	        String outputFileName="";
	        System.out.println("absolutePathOfInputFile:"+absolutePathOfInputFile);
	        System.out.println("Parent:"+Parent);
	        System.out.println("inputFolderName or single file:"+inputFolderName_OR_Single_File);
	        int error_count=0;
	  	  try{
			FileWriter writerDebug=new FileWriter(new File(output_debugFile));
                        //String outputFileName1 = "C:\\Documents and Settings\\lmookiah\\Desktop\\custom work\\output.txt";
            FileWriter writer = new FileWriter(outputFileName1, is_Append_outputFileName1);
            FileWriter writerOnlyKeywords = new FileWriter(outputFileName_onlykeywords, is_Append_outputFileName_onlykeywords);
            FileWriter writer_word_document_freq = new FileWriter(outputFileName_WORD_DOCUMENT_FREQ);
            
	        String pathslash="";
	        // if directory
	        //if(isDirectory==true){
	        File fileinput=null; int last_doc_id=-1; int curr_doc_id=-1;
				//While loop to read each file and write into.
				for (int i=0; i < Filenames.length; i++){
				  
				  try{
					  if(isMac)
						  pathslash="//";
					  else
						  pathslash="\\";
					  
					  //File fileinput = new File(absolutePathOfInputFile+pathslash + Filenames[i]);
					  
					  if(isDirectory)
						  fileinput = new File(inputFolderName_OR_Single_File+ Filenames[i]);
					  else
						  fileinput = new File(inputFolderName_OR_Single_File);
					  
					  System.out.println ("File reading::"+absolutePathOfInputFile+pathslash + Filenames[i]);
					  System.out.println (fileinput.getAbsoluteFile());
					  String token=""; int lineNumber=0;
					  //findToken( fileinput , fileinput , absolutePathOfInputFile, writer);
					  
						BufferedReader reader = new BufferedReader(new InputStreamReader(
													new FileInputStream(fileinput.getAbsoluteFile())));
						System.out.println("---------- ");
						System.out.println("Inside write method input file :"+ fileinput 
											+" no.files->"+ Filenames.length);
						System.out.println("Inside write file "+ outputFileName1 );
						System.out.println("line:"+line);
						TreeMap<String, String> map_isAlready_TokenDomainName=new TreeMap<String, String>();
						TreeMap<String, String> map_isAlready_TokenANDurl_as_KEY=new TreeMap<String, String>();
						 String token_only_alphanumeric="";
								String current_sourceURL="";
								String current_sourceDomainName="";
								String curr_source_FileName="";
							  //
							  while ((line = reader.readLine()) != null) {
								  total_num_of_lines++;
	                              lineNumber++; 
	                              line=line.replace("!!!!!!", "!!!d!!!").replace("!!!!!", "!!!").replace("!!!!", "!!!");
	                              
	                              String []s=null;
								  curr_doc_id=lineNumber;
	                              String last_TOKEN="";
	                              //debug break
//	                              if(lineNumber== 1000) break; 
	                              
	                              System.out.println("lineNumber:"+lineNumber);
								  if(line.length()<3) {
										continue;
								 }
								  if(YES_Filter.length()>0 && line.toLowerCase().indexOf(YES_Filter)==-1 ){
									  if(isSOPprint)
										  System.out.println(" yes_filter skip:"+line);
									  continue;
								  }
								  else{
									  if(isSOPprint)
	                                  System.out.println(" FOUND:"+line);
								  }
	                                                         
								  //System.out.println(reader.readLine());
								  //System.out.println("############# BEFORE APPENDING #############");
								 try{
									 s=line.split(delimiter_4_inputFolderName_OR_Single_File);
									 token=s[index_of_token_interested_for_freq_count-1].replace("from: ", "").replace("From:", "");
									 token=token.toLowerCase();
									 // 
									 
									 //cleaning authors name
									 if(is_token_interested__authors){
										 //
										 current_sourceURL=s[index_of_token_of_SourceURL-1].toLowerCase();
										 curr_source_FileName=s[index_of_token_of_Source_FileName-1].toLowerCase();
										  
										 current_sourceDomainName=Find_domain_name_from_Given_URL.find_domain_name_from_Given_URL(current_sourceURL , false );
										 //cleaning
										 current_sourceDomainName=current_sourceDomainName.replace("!", "");
										 // source TRAD OR POLI FILE NAME
										 last_TOKEN=curr_source_FileName;
										 
										 writerDebug.append("\n lineno:"+lineNumber+" token:"+token+
												 			" last_TOKEN:"+last_TOKEN);writerDebug.flush();
										 
										 if(lineNumber <=20)
										 System.out.println("current_sourceURL:"+current_sourceURL
												 			+" curr_source_FileName:"+curr_source_FileName
												 			+" current_sourceDomainName:"+current_sourceDomainName
												 			);
										 
										 String before_=token;
										 //System.out.println("");
										 //clean author name
										 token= clean_author_name(token);
										 
										 token_only_alphanumeric=token.replaceAll("[^A-Za-z0-9]", "");
										  
										 System.out.println("before:"+before_  +" after:"+token);
										 // 
										 if(token.indexOf("|") >=0 ){
											 token=token.substring(0,  token.indexOf("|") );
										 }
										 else if (token.indexOf("/") >=0 ){
											 token=token.substring(0,  token.indexOf("/") );
										 }
										 
										 // main debug 
//										 writerDebug.append("before:"+before_  +" after:"+token+"\n");
										 writerDebug.flush();
									 } //END if(is_token_interested__authors){
									 
									 if(isSOPprint)
										 System.out.println("token of curr line:"+token);
									 
//	                                 last_TOKEN=s[s.length -1];    //last token
	                                 // 
	                                 if(NO_Filter.length()>0 && token.toLowerCase().indexOf(NO_Filter)>=0)
	                                	 continue;
	                                                                 
								 }
								 catch(Exception e){
									 error_count++;
									 writerDebug.append("\ntoken errro:"+line+" error_count:"+error_count);
									 writerDebug.flush();
								 }
								 //is_split_CSV_on_the_given_token
								 if(is_split_CSV_on_the_given_token==false){
									 System.out.println("IF entry 1");
									 
									 if(!mapToken_Freq.containsKey(token)){
										 mapToken_Freq.put(token, 1);
										 mapTokenONLYalphaNumeric_Freq.put(token_only_alphanumeric, 1);
									 }
									 else{
										  int freq=mapToken_Freq.get(token)+1;
										  mapToken_Freq.put(token, freq);
										  mapTokenONLYalphaNumeric_Freq.put(token_only_alphanumeric, freq);
										  if(token.indexOf("ronald b")>=0){
											  writerDebug.append("2. cnt ronald b:"+freq +"\n");
											  writerDebug.flush();
										  }
									 }	 
								 }  
								 //is_split_on_blank_space_in_token (THIS IS MORE OF WORD-FREQUENCY COUNT)
								 else if(is_split_on_blank_space_in_token==true){
									 System.out.println("IF entry 2");
									 //clean
									 token=token.replace("\"", " ").replace("'", " ").replace(",", " ").replace(";"," ").replace(".", " ")
											    .replace("‘", " ").replace("“", " ").replace("–", " ").replace(")", " ").replace("(", " ")
											    .replace("-", " ").replace(":", " ").replace("’", " ").replace("”", " ").replace("—", " ")
											    .replace("#", " ").replace("%", " ")//.replace("$", " ")
											    .replace("  ", " ");
									 // 
									 String [] arr_token=token.split(" ");
									 int cnt=0;
									 // 
									 while(cnt < arr_token.length){
										 String temp= Ltrim.ltrim( arr_token[cnt]);
										 // do stemming
										 if(is_do_stemming_on_word)
											 //ltrim.ltrim(
											 temp=  stemmer.stem( temp) ;
											 // 
											 if(!mapToken_Freq.containsKey(temp)){
												  //term-freq
												  mapToken_Freq.put(temp, 1);												  
												  //word-document freq- unique number of documents this word occurs.
												  List l=new ArrayList<Integer>();
												  l.add(curr_doc_id);
												  map_Word_UniqueDocumentIdHavingIT.put(temp, l);
											 }
											 else{
												  int freq=mapToken_Freq.get(temp)+1;
												  //System.out.println("last_doc_id!=curr_doc_id->"+last_doc_id+"<-->"+curr_doc_id );
												  //term-freq
												  mapToken_Freq.put(temp, freq);
												  
												  if(temp.indexOf("ronald b")>=0){
													  writerDebug.append("3. cnt ronald b:"+freq +"\n");
													  writerDebug.flush();
												  }
												  
												  
												  // word-document freq- unique number of documents this word occurs.
												  
													  List<Integer> uniq_doc=map_Word_UniqueDocumentIdHavingIT.get(temp);
													  // if this docID not contained before in list
													  if(!uniq_doc.contains(curr_doc_id)){
														  uniq_doc.add(curr_doc_id );
														  map_Word_UniqueDocumentIdHavingIT.put(temp, uniq_doc);
													  }
											 }
										cnt++;
									 } 
								 }
								 else{ // is_split_CSV_on_the_given_token=true
									 System.out.println("IF entry 3 - AUTHOR ENTRY");
									 
									 //either of 1 approach below works
									 //String[] s_token=token.split(","); 
									 TreeMap<String, String> mapAuthorNames=
											 						SplitCSVAuthors_to_atom.  
											 						splitCSVAuthors_to_atom(
											 											token,
											 											"",
											 											false //isSOPprint
											 											); //approach 1
									 
									 //cleaning each atom author
									 for(String atom_author :mapAuthorNames.keySet() ){
										 
									 }
									 
									 // iterate each author name _atom
									 if(mapAuthorNames.size()>0) { //approach 1
										 //
										 for(String temp:mapAuthorNames.keySet()){
											 String temp_only_alphaNumeric=temp.replaceAll("[^A-Za-z0-9]", "");
											 
											 if(IsInteger.isInteger(temp))
												 continue;
											 // 
											 if(!mapToken_Freq.containsKey(temp)){
												 mapToken_Freq.put(temp, 1);
												 // domainName
												 if(is_token_interested__authors){
													 mapToken_DomainName.put( temp, current_sourceDomainName );
													 map_isAlready_TokenDomainName.put(temp+current_sourceDomainName, "");
												 }
												 
												 mapToken_LastToken.put(temp, last_TOKEN);
												 mapTokenONLYalphaNumeric_LastToken.put(temp_only_alphaNumeric, last_TOKEN);
											 }
											 else{
												  
												 if(map_isAlready_TokenANDurl_as_KEY.containsKey( temp+ current_sourceURL)){
													 continue;
												 }
												 
												  int freq=mapToken_Freq.get(temp)+1;
												  mapToken_Freq.put(temp, freq);

												  
//												  int freq2=mapTokenONLYalphaNumeric_Freq.get(temp_only_alphaNumeric)+1;
												  
												  mapTokenONLYalphaNumeric_Freq.put(temp_only_alphaNumeric , freq);
												  
//												  if(temp.indexOf("ronald b")>=0){
//													  writerDebug.append("4. cnt ronald b:"+freq + " docid:"+curr_doc_id+
//															  					" url:"+ current_sourceURL+ 
//															  							"\n");
//													  writerDebug.flush();
//												  }
												  
												  if(is_token_interested__authors){
													  // domainName
													  String conc_domainName= mapToken_DomainName.get(temp);
													  
													  ////
													  if(!map_isAlready_TokenDomainName.containsKey(temp+current_sourceDomainName )){
													  		conc_domainName=conc_domainName +"$$$"+current_sourceDomainName;
													  		mapToken_DomainName.put( temp , conc_domainName );
													  		/// 
													  		map_isAlready_TokenDomainName.put(temp+current_sourceDomainName, "");
													  }
													  
													  //System.out.println("conc_domainName:"+conc_domainName+" temp:"+temp);
												  }
												  
												  mapToken_LastToken.put(temp, last_TOKEN);
												  
												  map_isAlready_TokenANDurl_as_KEY.put( temp+ current_sourceURL, "");
												  
											 }
										 } // END for 
										 
									 }
									 else{ //approach 2 (obsolete)
										 String[] s_token=token.split(",");
										 int c=0;
										 //token 
//										 while(c<s_token.length){ //OBSELETE
//											 String temp=s_token[c];
//											 //String[] s2_token=temp.split(",");	 
//											 //int c2=0;
//											 //while(c2<s2_token.length){	 
//											//	 String temp2=s2_token[c2];
//												 if(!mapToken_Freq.containsKey(temp)){
//													  mapToken_Freq.put(temp, 1);
//												 }
//												 else{
//													  int freq=mapToken_Freq.get(temp)+1;
//													  mapToken_Freq.put(temp, freq);
//												 }
//												
//											//	 c2++;
//											 //}
//											 c++;
//										 } //while end
									 } //else END
								 }
								 last_doc_id=lineNumber;
							  } //end of WHILE 
							  System.out.println ("----------flushing ");
							  
					// write output to word-document frequency
					for(String word:map_Word_UniqueDocumentIdHavingIT.keySet()){
						writer_word_document_freq.append(word+"!!!"+map_Word_UniqueDocumentIdHavingIT.get(word).size() 
															 +"\n");
						writer_word_document_freq.flush();
						
						if(word.indexOf("ronald b")>=0){
							writerDebug.append("list ronald b: "+map_Word_UniqueDocumentIdHavingIT.get(word).toString() +"\n");
							writerDebug.flush();
						}
					}
							  
				  }
				  catch(Exception e)
				  {
					  System.out.println("An exception occurred while calling Write method ..!!");	
					  e.printStackTrace();
					  
				  }
				  //writer.close();
				} //end FOR 			//While loop to read each file and write into.
			
//	        } // folder only
//	        else if(isDirectory==false){
//	        	
//	        	
//	        	
//	        	
//	        }
				
				if(is_token_interested__authors ==true ){
					writer.append( "AuthorName!!!AuthorNameOccuranceFreq_in_doc!!!SourceFileName!!!DomainNames\n");
					writer.flush();
				}
				

				//output   write token and freq
				for(String token:mapToken_Freq.keySet()){
					
					//
					if(is_token_interested__authors ==true ){
						String new_line=token.trim()+"!!!"+mapToken_Freq.get(token)
						+"!!!"+mapToken_LastToken.get(token).replace(replace_string_for_sourceChannelFilePath, "")
							+"!!!"+mapToken_DomainName.get(token);
						
						//
						if(is_author_name(token)==true && token.indexOf("2015")==-1  && token.indexOf("2016")==-1 
								 && token.indexOf("2014")==-1 && token.indexOf("2013")==-1 && token.indexOf("2012")==-1
								 && new_line.toLowerCase().indexOf("subject:") == -1
								 ){
							
							//CLEANING
							token=clean_author_name(token);
							
							String token_noAlphaNumeric=token.replaceAll("[^A-Za-z0-9]", "");;
							String last_token=mapToken_LastToken.get(token_noAlphaNumeric);
							
							if(last_token==null || last_token.equals("") ){  
									last_token=mapTokenONLYalphaNumeric_LastToken.get(token_noAlphaNumeric);		
							};
							
							if(mapToken_Freq.get(token)!=null){
									new_line=token.trim()+"!!!"+mapToken_Freq.get(token)
														+"!!!"+last_token.replace(replace_string_for_sourceChannelFilePath, "")
							 							+"!!!"+mapToken_DomainName.get(token);
							}
							else if(mapTokenONLYalphaNumeric_Freq.get(token_noAlphaNumeric)!=null) {
								new_line=token.trim()+"!!!"+mapTokenONLYalphaNumeric_Freq.get(token_noAlphaNumeric)
													+"!!!"+last_token.replace(replace_string_for_sourceChannelFilePath, "")
						 							+"!!!"+mapToken_DomainName.get(token);
							}
							else{
								new_line=token.trim()+"!!!"+"1"
													+"!!!"+last_token.replace(replace_string_for_sourceChannelFilePath, "")
						 							+"!!!"+mapToken_DomainName.get(token);
								
							}
							
							
							
							String new_line_PARTIAL=token.trim()+"!!!"+last_token.replace(replace_string_for_sourceChannelFilePath, "")
															+"!!!"+mapToken_DomainName.get(token);
								
							String uniq_authName_n_Channel=token+last_token.replace(replace_string_for_sourceChannelFilePath, "");
							
							writer.append(new_line+"\n"); writer.flush();
							
//							if(mapToken_Freq.containsKey(token)){
//								if(!map_alreadyWrote_authNameANDsourceChannel_AS_key.containsKey(uniq_authName_n_Channel)){
//								 	map_alreadyWrote_authNameANDsourceChannel_AS_key.put(uniq_authName_n_Channel,Integer.valueOf(mapToken_Freq.get(token)));
//								 	map_authNameANDsourceChannel_N_newlinePARTIAL.put(uniq_authName_n_Channel, new_line_PARTIAL);
//								}
//								else{
//									int cumm_freq=map_alreadyWrote_authNameANDsourceChannel_AS_key.get(uniq_authName_n_Channel)+mapToken_Freq.get(token) ;
//									map_alreadyWrote_authNameANDsourceChannel_AS_key.put(uniq_authName_n_Channel, cumm_freq);
//								}
//							}
//							else{
//								writerDebug.append("error: cant find freq for author:"+token+"\n");
//								writerDebug.flush();
//							}
						 	
						}
					}
					else{
						writer.append(token.trim()+"!!!"+mapToken_Freq.get(token)+
												  "!!!"+mapToken_LastToken.get(token).replace(replace_string_for_sourceChannelFilePath, "")
						 						  +"!!!"+mapToken_DomainName.get(token)+'\n');
						writer.flush();
					}
 
//				 	writerOnlyKeywords.append(token.trim()+"\n");
//				 	writerOnlyKeywords.flush();
	  	     	}
				
				
				//AUTHOR
//				if(is_token_interested__authors ==true ){
//					writerDebug.append("map_alreadyWrote_authNameANDsourceChannel_AS_key:"+map_alreadyWrote_authNameANDsourceChannel_AS_key.size()+"\n");
//					writerDebug.append("map_authNameANDsourceChannel_N_newlinePARTIAL.size:"+map_authNameANDsourceChannel_N_newlinePARTIAL.size()+"\n");
//					
//					for(String curr_authName_n_Channel:map_alreadyWrote_authNameANDsourceChannel_AS_key.keySet()){
//						String currLine=map_authNameANDsourceChannel_N_newlinePARTIAL.get(curr_authName_n_Channel);
//						String []s=currLine.split("!!!");
//						int curr_cummFreq=map_alreadyWrote_authNameANDsourceChannel_AS_key.get(curr_authName_n_Channel);
//						//
//						writer.append(s[0]+"!!!"+curr_cummFreq +"!!!"+s[1]+"!!!"+s[2] +'\n');
//					 	writer.flush();
//					}
//				}
				
				writerDebug.append("map_authNameANDsourceChannel_N_newlinePARTIAL:"+map_authNameANDsourceChannel_N_newlinePARTIAL+"\n");
				writerDebug.append("\n\n map_alreadyWrote_authNameANDsourceChannel_AS_key:"+map_alreadyWrote_authNameANDsourceChannel_AS_key+"\n");
				writerDebug.flush();
				
					// SORTING
				TreeMap<String, Integer> mapToken_lengthOfToken=new TreeMap<String, Integer>();
				
				//length of the keyword calculated and stored
				for(String token:mapToken_Freq.keySet()){
					mapToken_lengthOfToken.put(token, token.length());
				}
				
				// /sort  ASCENDING (write only the token) AND WRITE 
				Map<String, Integer> mapToken_lengthOfToken_SORTED= Sort_given_treemap.sortByValue_SI_method4_ascending(mapToken_lengthOfToken);
				/// WRITE
				for(String token:mapToken_lengthOfToken_SORTED.keySet()){
					
					// 
					if(is_author_name(token)==true ){
						//
						token=clean_author_name(token);
						/// writing
						writerOnlyKeywords.append(  token+ "\n" );
						writerOnlyKeywords.flush();
					}
					else{
						/// writing
						writerOnlyKeywords.append(  token+ "\n" );
						writerOnlyKeywords.flush();
						
					}
				
				}
				
				// 
				writerDebug.append("mapToken_Freq:"+mapToken_Freq+"\n");
				writerDebug.flush();
				writerDebug.append("\n\nmapToken_LastToken:"+mapToken_LastToken+"\n");
				//
				System.out.println("inputFolderName_OR_Single_File:"+inputFolderName_OR_Single_File);
				System.out.println("outputFileName1:"+outputFileName1);
				System.out.println("outputFileName_onlykeywords:"+outputFileName_onlykeywords);
				System.out.println("outputFileName_WORD_DOCUMENT_FREQ:"+outputFileName_WORD_DOCUMENT_FREQ);
				System.out.println("output_debugFile:"+output_debugFile);
				
			 //System.out.println("mapToken_Freq_SORTED:"+mapToken_lengthOfToken_SORTED);
	  	   } //end try
			 catch(Exception e){
				 System.out.println("error."+e.getMessage());
				 e.printStackTrace();
			 }
	  	  	System.out.println("total_num_of_lines:"+total_num_of_lines);
	  	  	
	  	  return mapToken_Freq;	
		}
				// find Tokens
				public static void findToken(File outputFileName, 
											 File fileinput, 
											 String absolutePathOfInputFile, 
											 FileWriter writer){
				String line = null ;	
				//String outputFileName1 =  absolutePathOfInputFile  + "\\output.txt";
				try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(
											new FileInputStream(fileinput.getAbsoluteFile())));
				System.out.println ("---------- ");
				System.out.println ("Inside write method input file :"+ fileinput );
				System.out.println ("Inside write method "+ outputFileName +" : "+ reader);
				 
					System.out.println ("outputFileName :"+outputFileName);
					
					  while ((line = reader.readLine()) != null) {
						  //System.out.println(reader.readLine());
						  //System.out.println("############# BEFORE APPENDING #############");
						  writer.append(line.toString()+ '\n');
						  writer.flush(); 
					  }
					  System.out.println ("----------flushing ");
					 
				}
				  
				  //reader.close();
				catch(Exception e)
				  {
					  System.out.println("An exception occurred while writing the files..!!");	
					  e.printStackTrace();
				  }
		  
				}
				//p6_remove_duplicates_of_authorName_in_freq_file
				public static void p6_remove_duplicates_of_authorName_in_freq_file(
										String baseFolder,
										String outputFileName, //AuthorName!!!AuthorNameOccuranceFreq_in_doc!!!SourceFileName!!!DomainNames
										String inputFile_AuthorName_AuthorID, //IN 
										String outFile_noDuplicates
										) {
					// TODO Auto-generated method stub
					String line="";
					TreeMap<String, Integer> mapAuthNameAuthID=new TreeMap<String, Integer>();
					FileWriter writerDebug=null;
					try{
						FileWriter writer=new FileWriter(new File(outFile_noDuplicates));
									writerDebug=new FileWriter(new File(baseFolder+"debug_authorName_noDuplicate.txt")); 
						 
						BufferedReader reader = new BufferedReader(new InputStreamReader(
												new FileInputStream(outputFileName)));
						//
						mapAuthNameAuthID=Cleaning_and_convert.load_AuthName_and_Auth_id(
																			inputFile_AuthorName_AuthorID,
																			true // normalize name
																						);
						
						TreeMap<String, Integer> map=new TreeMap();
						writer.append("AuthName!!!Frequency!!!AuthID!!!SourceFilePath!!!SourceDomainNames\n");
						writer.flush();
						TreeMap<String, String> map_authName_SourceFileName=new TreeMap<String, String>();
						
						TreeMap<String,TreeMap<String,  String>> map_authName_SourceURLDomain=new TreeMap<String, TreeMap<String,String> >();
						
						int lineNo=0;
						//AuthorName!!!AuthorNameOccuranceFreq_in_doc!!!SourceFileName!!!DomainNames
						while ((line = reader.readLine()) != null) {
							lineNo++;
							if(lineNo==1) //header
								continue;
							
							String [] s=line.split("!!!");
							String authName=P6_MAIN_Work.p6_normalize_auth_name(s[0]).replace("auth:from: ", ""); 
							
							if(s[1].equals("null"))
								s[1]="1"; //setting it to 1 instead of null
							
							if(s[1]!=null && authName!=null ){
								if(!s[1].equals("")){
									
									if(!map.containsKey(authName)){
//										System.out.println("s[1]:"+s[1]);
										map.put(authName, Integer.valueOf(s[1])); //frequency
										map_authName_SourceFileName.put(authName, s[2]); //sourceFileName

										//domainName for curr Authorname
										TreeMap<String, String> temp=new TreeMap<String, String>();
										if(s.length>=4){
											String [] atom_domainName=new String[1];
											///
											if(s[3].indexOf("$$$")>=0){
												atom_domainName=s[3].split("\\$\\$\\$");
												
												writerDebug.append("3.lineno:"+lineNo+" .len:"+atom_domainName.length+" "+s[3]+
																" "+s[3].indexOf("$$$")+" "+atom_domainName.length+"\n");	
												
											}
											else{
												atom_domainName[0]=s[3];
												
												writerDebug.append("4.lineno:"+lineNo+" .len:"+atom_domainName.length+" "+s[3]+
														" "+s[3].indexOf("$$$")+" "+atom_domainName.length+"\n");
												
											}
											 
											writerDebug.flush();
											
											int c=0;
											while(c < atom_domainName.length) {
												temp.put(atom_domainName[c],"");
												c++;
											}
											
											map_authName_SourceURLDomain.put(authName, temp ); //domainName
										}
										else{
											writerDebug.append( "\n no sufficient token: lineNo:"+lineNo+" line:"+line);
											writerDebug.flush();
											
										}
										
									}
									else{
										int cumm_frequeny=Integer.valueOf(s[1]) +map.get(authName);
										map.put(authName, cumm_frequeny);
										
										//domainName for curr Authorname
										if(s.length>=4){
											String [] atom_domainName=new String[1];
											TreeMap<String, String> temp= map_authName_SourceURLDomain.get(authName);
											///
											if(s[3].indexOf("$$$")>=0){
												atom_domainName=s[3].split("\\$\\$\\$");
												writerDebug.append("1.lineno:"+lineNo+" .len:"+atom_domainName.length+" "+s[3]+
																	" "+s[3].indexOf("$$$")+" "+atom_domainName.length+"\n");
											}
											else{
												atom_domainName[0]=s[3];
												writerDebug.append("2.lineno:"+lineNo+" .len:"+atom_domainName.length+" "+s[3]+
														" "+s[3].indexOf("$$$")+" "+atom_domainName.length+"\n");
											}
											
											writerDebug.flush();
											
											int c=0; 
											while(c < atom_domainName.length) {
												temp.put(atom_domainName[c],"");
												c++;
											}
											
											map_authName_SourceURLDomain.put(authName, temp ); //domainName
										}
										else{
											writerDebug.append( "\n no sufficient token: lineNo:"+lineNo+" line:"+line);
											writerDebug.flush();
											
										}
										
									}	
								}
							}
							
						}
						
						// 
						for(String name:map.keySet()){
							
							//Fix the authid tht are NULL
							if(mapAuthNameAuthID.get(name) == null){
							    int authID_new=mapAuthNameAuthID.size()+1;
							    mapAuthNameAuthID.put( name , authID_new);
							    //name can't have numeric 
							    if(name.indexOf("0")==-1 && name.indexOf("1")==-1 && name.indexOf("2")==-1 && name.indexOf("3")==-1 && name.indexOf("4")==-1
							    		&& name.indexOf("5")==-1 && name.indexOf("6")==-1 && name.indexOf("7")==-1 && name.indexOf("8")==-1 
							    		&& name.indexOf("9")==-1 & map_authName_SourceURLDomain.get(name) != null
							    		){
							    	writer.append(  name+"!!!"+map.get(name)+"!!!" +mapAuthNameAuthID.get(name)
													+"!!!"+map_authName_SourceFileName.get(name)
													+"!!!"+map_authName_SourceURLDomain.get(name)
													+"\n");
							    }
							    else{
							    	writerDebug.append( "\n NOT a authorName really: name->"+name +" domainName:"+ map_authName_SourceURLDomain.get(name)+"\n");
							    	writerDebug.flush();
							    }
									
							}
							else {
							    // name can't have numeric 
								if(name.indexOf("0")==-1 && name.indexOf("1")==-1 && name.indexOf("2")==-1 && name.indexOf("3")==-1 && name.indexOf("4")==-1
							    		&& name.indexOf("5")==-1 && name.indexOf("6")==-1 && name.indexOf("7")==-1 && name.indexOf("8")==-1 
							    		&& name.indexOf("9")==-1 && map_authName_SourceURLDomain.get(name)!=null
							    		){
									writer.append(name+"!!!"+map.get(name)+"!!!"+ mapAuthNameAuthID.get(name)
													+"!!!"+map_authName_SourceFileName.get(name)
													+"!!!"+map_authName_SourceURLDomain.get(name)+
													"\n");
								}
								else{
									writerDebug.append( "\n NOT a authorName really: name->"+name +" domainName:"+ map_authName_SourceURLDomain.get(name)+"\n");
							    	writerDebug.flush();
								}
							}
							// 
							writer.flush();
						}
						
					}
					catch(Exception e){
						e.printStackTrace();
					}
					
					
					
				}
				
				//clean_author_name
				public static String clean_author_name(String token){
					//
					if(token.indexOf("writer")>=0 && token.indexOf(" ap ")>=0){
						String subString=token.substring(token.indexOf(" ap "), token.indexOf("writer")+"writer".length());
						token=token.replace(subString, " ");
					}
					token=token.replace(" daily news staff writer", "").replace(" staff writer", "");
					token=token.replace("/associated press","")
							.replace(" / ap","")
							.replace(" / ap","")
							.replace(" | ap","")
							.replace(" the associated press","")
							.replace(" associated press","")
							.replace("associated press","")
							.replace(" / las vegas","")
							.replace("/ap","")
							.replace(" /ap","")
							.replace("a commentary by ","")
							.replace("photographs by ","")
							.replace(", telegraph)","")
							.replace(" | sky news","")
							.replace("| nj advance media for nj.com","")
							.replace(" / people","")
							 .replace("/ nbc news","")
							 .replace("| for nj.com","")
							 .replace("| nj advance media for nj.com","").replace("| religion news service", "")
							 .replace(" the denver post", "")
							 .replace(" the washington post", "")
							 .replace( "ap technology writer", "")
							 .replace( "| for nj advance media","")
							 .replace( "/ oilprice.com","")
							 .replace(" / taipei", "")
							 .replace("/washington post", "").replace(" | reuters", "").replace(" / paris", "")
							 .replace(" / fortune", "").replace(" / fortune", "")
							 .replace("|the star-ledger", "").replace(" | the star-ledger", "")
							 .replace(" / entertainment weekly", "")
							 .replace("/fortune", "").replace(" | cleveland.com", "")
							 .replace(" | northeast ohio media group", "")
							 .replace(" / entertainment weekly", "").replace(" | for the jersey journal", "")
							 .replace(" | the warren reporter", "").replace(" / history news network", "")
							 .replace(" / history news network", "").replace(" / time books","");
								 
					// ( avaiable, but missing )
					if(token.indexOf("(")>=0 && token.indexOf(")") ==-1 ){
						token=token+")";
					}
					
					//
					if( token.indexOf("rnd:")>=0   ){
						token=token.substring( 0, token.indexOf("rnd:") );
					}
					
				 	return token;
				}
				
				///
				public static boolean is_author_name(String token){
					//skip the names that are not AUTHOR NAMES
 
						
						if(token.indexOf(" in ")>=0 || token.indexOf(" to ")>=0 || token.indexOf(" among ")>=0||
							token.indexOf(" at ")>=0 ||token.indexOf(" for ")>=0||
							token.indexOf(" the ")>=0||token.indexOf(" near ")>=0 ||
							token.indexOf(" is ")>=0  ||token.indexOf(" with ")>=0 ||
							token.indexOf(" over ")>=0 ||token.indexOf(" of ")>=0||token.indexOf(" if ")>=0
							||token.indexOf(" now ")>=0||token.indexOf(" photo ")>=0||token.indexOf(" against ")>=0
							||token.indexOf(" x-mozilla-statu")>=0
							||token.indexOf(" government ")>=0 ||token.indexOf(" passes ")>=0    
							||token.indexOf(" on ")>=0||token.indexOf(" food ")>=0
							||token.indexOf("-rnd:")>=0||token.indexOf(" food ")>=0
							||token.indexOf(" top ")>=0||token.indexOf(" indian ")>=0
							||token.indexOf(" three ")>=0||token.indexOf(" two ")>=0||token.indexOf(" success")>=0
							||token.indexOf(" as ")>=0||token.indexOf(" from ")>=0
							||token.indexOf(" boats ")>=0||token.indexOf(" have ")>=0||token.indexOf(" gets ")>=0
							||token.indexOf(" interviews ")>=0||token.indexOf(" interview ")>=0
							
							){
							 return false;
						}
						
						return true;
				}
				 
				
				// main
				public static void main(String[] args) {
					
//					if (args.length != 1) {
//						System.out.println("Incorrect input provided..!! Please provide proper folder name");
//						System.out.println("Correct Usage is: java MergeFiles <input folder name>");
//						return;
//					}
				  try{
					String inputFolderName = "";
					
					if(args.length>0)
						inputFolderName=args[0];
					
					String baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/";
						   baseFolder="/Users/lenin/Downloads/#problems/p19/";
						   baseFolder="/Users/lenin/Dropbox/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds10/";
					String inputFolderName_OR_Single_File = ""; String debugFile="";
					String outputFileName="";
					String outputFileName_onlykeywords="";
					
					//INPUT FOLDER
//					inputFolderName="/Users/lenin/Downloads/crawleroutput/output/all.think.tank.from.world.aug.20.2015/merge/";
					
					// INPUT FILE
					inputFolderName_OR_Single_File=baseFolder+"tf-idfmergedall-2015-T8-10-Topics-all-tokens.txt";
					inputFolderName_OR_Single_File=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt";
					inputFolderName_OR_Single_File=baseFolder+"20000.txt";
					
					//only keywords
					outputFileName=baseFolder+"outputUniqNames_only_authors.txt";
					outputFileName_onlykeywords=baseFolder+"OnlyAuthors_asc_length.txt";
					int index_of_token_interested_for_freq_count=4; //change manual
					int index_of_token_of_Source_FileName=7 ; 		//change manual
					int index_of_token_of_SourceURL=2; 				//change manual
							debugFile=baseFolder+"debugTooken.txt";
					String  delimiter_4_inputFolderName_OR_Single_File="!!!";
					boolean is_split_on_blank_space_in_token=false;
					boolean is_do_stemming_on_word=false;
					boolean is_split_CSV_on_the_given_token=true;
					boolean is_token_interested__authors=true;
					String replace_string_for_sourceChannelFilePath="infile:/users/lenin/downloads/#problems/p6/merged_/rawxmlfeeds2csv/merged/all_all_body_url_datetime_auth_keywor_subjec_";
					String pathslash="//";
					
					//METHOD: calc_Frequency_Of_Token_AND_output_unique_values_in_token 
					calc_Frequency_Of_Token_AND_output_unique_values_in_token
																		(
																		inputFolderName_OR_Single_File,
																		delimiter_4_inputFolderName_OR_Single_File,
																		outputFileName, //OUTPUT
																		false, //is_Append_outputFileName,
																		outputFileName_onlykeywords,
																		false, //is_Append_outputFileName_onlykeywords,
																		outputFileName+"_WORD_DOCUMENT_FREQ.txt",
																		index_of_token_interested_for_freq_count, //index_of_token_interested_for_freq_count
																		index_of_token_of_SourceURL, //used only is_token_interested__authors==true
																		index_of_token_of_Source_FileName, //used only is_token_interested__authors==true
																		replace_string_for_sourceChannelFilePath,
																		is_token_interested__authors,
																		is_split_on_blank_space_in_token, //is_split_on_blank_space_in_token
																		is_do_stemming_on_word, //is_do_stemming_on_word
																		"", //YES_filter
																		"", // NO_FILTER
																		true, //  isMac
																		is_split_CSV_on_the_given_token, //  is_split_CSV_on_the_given_token
																		debugFile,
																		true,  //is_add_last_token_of_inputFolderName_to_OUTPUT
																		false  // isSOPprint
																		);
					   
				       String outFile_noDuplicates=baseFolder+"outputUniqNames_auth_DistinctDocs_no_duplicates.txt";
				       //This file is produced by running "Flag_for_Task==1"
				       String inputFile_AuthorName_AuthorID=baseFolder+"authName_AND_auth_id.txt"; 
			            //remove duplicates in name for p6 problem
			            p6_remove_duplicates_of_authorName_in_freq_file(
			            												baseFolder,
								            							outputFileName, //authorName!!!Frequency  // input
								            							inputFile_AuthorName_AuthorID, //authName!!!authID // input
								            							outFile_noDuplicates
			            												);
			            
			           //note: ds10 database: some authors belongs to many domain->zeina karam
			            	
					System.out.println("out file:"+baseFolder+"outputUniqNames_auth_freq_no_duplicates.txt");
 
					
					//there is no 
					
				  }
				  catch(Exception e)
				  {
					  System.out.println("An exception occurred while calling Write method ..!!");	
					  e.printStackTrace();
				  }
				
				  //String test="amir vahdat & jon gambrell / ap";
				 //System.out.println(test.replace(" / ap", ""));
				  
				} //main end
				
}
