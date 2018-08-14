package p18;
import opennlp.tools.namefind.*;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import java.io.File;

import java.io.*;
import java.util.TreeMap;
 
//import org.apache.xpath.Arg;

import crawler.*;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.namefind.*;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.uima.namefind.NameFinder;
public class Analysis_organization_of_TP_N_TN {

		// get_cleaned_junk_words_at_endOF_bodyTEXT
		private static void get_cleaned_junk_words_at_endOF_bodyTEXT(
																	 String baseFolder,
																	 String outputFile,  //input_1
																	 String inFile2_HUMAN_ANNOTATOR, //input_2
																	 String outputFile_NEW, //out
																	 boolean is_having_header, 
																	 String domainNAME_patternTOcatchJUNKwords //pattern to catch the junk word in bodyTEXT
																	 ){
			// TODO Auto-generated method stub
			try{
				FileWriter writerDebug=new FileWriter(new File(baseFolder+"debug_JUNK.txt"));
				FileWriter writer=new FileWriter(new File(outputFile_NEW));
				  // url!#!#bodyTextNLP!#!#bodyText
				  TreeMap<Integer, String>  map_inFile1_seq_eachLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
																	 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																														 outputFile, 
																													 	 -1, //startline, 
																														 -1, //endline,
																														 " loading", //debug_label
																														 false //isPrintSOP
																														 );
				  String header="";
				  TreeMap<String,String> map_URL_bodyTEXTnlp=new TreeMap<String, String>();
				  TreeMap<String,String> map_URL_bodyTEXT=new TreeMap<String, String>();
				  //each line -- inputFile1
				  for(int seq:map_inFile1_seq_eachLine.keySet()){
					  if(seq==1){
						  header=map_inFile1_seq_eachLine.get(1);
						  continue;
					  }
					  
					  String [] arr=map_inFile1_seq_eachLine.get(seq).split("!#!#");
					  String curr_URL=arr[0];
					  String curr_bodyTEXTnlp=arr[1];
					  String curr_bodyTEXT=arr[2];
					  map_URL_bodyTEXTnlp.put(curr_URL, curr_bodyTEXTnlp);
					  map_URL_bodyTEXT.put(curr_URL, curr_bodyTEXT);
				  }
				  
				  
				  //inputFile2
				  //------------- reading inFile1
				  // keywords!!!label!!!comment!!!url!!!partTEXT!!!subject!!!fullTEXT!!!domain 
				  TreeMap<Integer, String>  map_inFile2_seq_eachLine_HUMANannotators=
						  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
											.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																											 inFile2_HUMAN_ANNOTATOR, 
																										 	 -1, //startline, 
																											 -1, //endline,
																											 " loading", //debug_label
																											 false //isPrintSOP
																											 );
				  
				  TreeMap<String, String> map_inFile1_URL_N_label = new TreeMap<String, String>();
				  TreeMap<String, String> map_inFile1_URL_N_keywords = new TreeMap<String, String>();
				  TreeMap<String, String> map_inFile1_URL_N_partTEXT = new TreeMap<String, String>();
				  TreeMap<String, String> map_inFile1_URL_N_fullTEXT = new TreeMap<String, String>();
				  TreeMap<String, String> map_inFile1_URL_N_subject = new TreeMap<String, String>();
				  TreeMap<String, String> map_inFile1_URL_N_DOMAIN = new TreeMap<String, String>();
				  // get <URL,label>
				  // HEADER -> keywords!!!label!!!comment!!!url!!!partTEXT!!!subject!!!fullTEXT!!!domain
				  for(int seq:map_inFile2_seq_eachLine_HUMANannotators.keySet()){
					  String [] arr=map_inFile2_seq_eachLine_HUMANannotators.get(seq).split("!!!");
					  // each PRIMARYKEY & feature
					  map_inFile1_URL_N_label.put(arr[3], arr[1]);
					  map_inFile1_URL_N_keywords.put(arr[3], arr[0]);
					  map_inFile1_URL_N_partTEXT.put(arr[3], arr[4]);
					  map_inFile1_URL_N_subject.put(arr[3], arr[5]);
					  map_inFile1_URL_N_fullTEXT.put(arr[3], arr[6]);
					  map_inFile1_URL_N_DOMAIN.put(arr[3], arr[7]);
				  }
				  
				  
				  TreeMap<String, TreeMap<String, String>> map_domain_pattern4JUNKWORDSinbodyTEXTasKEY=new TreeMap<String, TreeMap<String,String>>();
				  
				  // first delimiter ,
				  String [] arr_domain_pattern4=domainNAME_patternTOcatchJUNKwords.split(",");
				  int c=0;
				  // 
				  while(c<arr_domain_pattern4.length){
					  // second delimiter !!!
					  String [] arr2= arr_domain_pattern4[c].split("!!!");
					  System.out.println(" arr_domain_pattern4[c]:"+ arr_domain_pattern4[c]);
					  String curr_domain=arr2[0];
					  String curr_pattern4JUNKWORDSinbodyTEXT=arr2[1];
					  // third delimiter !#!#
					  String [] arr_curr_pattern4JUNKWORDSinbodyTEXT_atom=curr_pattern4JUNKWORDSinbodyTEXT.split("!#!#");
					  
					  int c2=0;
					  //
					  while(c2<arr_curr_pattern4JUNKWORDSinbodyTEXT_atom.length){
						  //
						  if(!map_domain_pattern4JUNKWORDSinbodyTEXTasKEY.containsKey(curr_domain)){
							  TreeMap<String, String> tmp=new TreeMap<String, String>();
							  tmp.put(arr_curr_pattern4JUNKWORDSinbodyTEXT_atom[c2] , "");
							  map_domain_pattern4JUNKWORDSinbodyTEXTasKEY.put(curr_domain, tmp);
						  }
						  else{
							  TreeMap<String, String> tmp=map_domain_pattern4JUNKWORDSinbodyTEXTasKEY.get(curr_domain);
							  tmp.put(arr_curr_pattern4JUNKWORDSinbodyTEXT_atom[c2] , "");
							  map_domain_pattern4JUNKWORDSinbodyTEXTasKEY.put(curr_domain, tmp);
						  }
						  c2++;
					  }
					  c++;
				  }
				  int count_found=0; int count_notfound=0;
				  
				  writer.append("keyword!!!title!!!label!!!URL!!!bodyTEXT\n");
				  writer.flush();
				  
				  ///
				  for(String currURL:map_URL_bodyTEXT.keySet()){
					  String found_curr_domain="";
					  String curr_bodyTEXT=map_URL_bodyTEXT.get(currURL);
					  boolean isfound=false;
					  for(String curr_domain:map_domain_pattern4JUNKWORDSinbodyTEXTasKEY.keySet()){
						  found_curr_domain=curr_domain.toLowerCase();
						  //
						  if(currURL.indexOf(found_curr_domain) >=0 ){
							  isfound=true;
							  break;
						  }
					  }
					  String curr_bodyTEXT_new="";
					  String curr_bodyTEXT_2_WRITE="";
					   //found that the currURL belongs to a domain which is present in "map_domain_pattern4JUNKWORDSinbodyTEXTasKEY" 
					  if(isfound){
						  
						  TreeMap<String,String> map_patterns_as_KEY=map_domain_pattern4JUNKWORDSinbodyTEXTasKEY.get(found_curr_domain);
						  String found_curr_Pattern="";
						  int found_index=-1;
						  
						  System.out.println("found_curr_domain:"+found_curr_domain+" map_patterns_as_KEY:"+map_patterns_as_KEY
								  				+" for curr_url:"+currURL);
						  
						  if(map_patterns_as_KEY!=null){
							  // from list of patterns, find the one which is found in the bodyTEXT
							  for(String curr_Pattern:map_patterns_as_KEY.keySet()){
								  found_index=curr_bodyTEXT.toLowerCase().indexOf(curr_Pattern.toLowerCase());
								  if( found_index>=0 ){
									  if(found_curr_domain.equalsIgnoreCase("ibtimes.com"))
									  System.out.println(" found_index:"+found_index);
									  found_curr_Pattern=curr_Pattern;
									  break;
								  }
							  }
						  }
						  
						  // 
						  if(found_index>=0){
							  
							  curr_bodyTEXT_new=curr_bodyTEXT.substring(0, found_index);
							  
							  if(found_curr_domain.equalsIgnoreCase("ibtimes.com"))
								  System.out.println("2.found_index:"+found_index+" curr_bodyTEXT:"+curr_bodyTEXT
										  					+" curr_bodyTEXT_new:"+curr_bodyTEXT_new);
							  
							  int index_beginINDEXi=curr_bodyTEXT_new.indexOf(" beginINDEXis");
							  if(index_beginINDEXi>0)
								  curr_bodyTEXT_new=curr_bodyTEXT_new.substring(0, index_beginINDEXi);
							  
							  curr_bodyTEXT_2_WRITE = curr_bodyTEXT_new;
							  
//							  writer.append(  map_inFile1_URL_N_keywords.get(currURL)+"!!!"+map_inFile1_URL_N_subject.get(currURL)+"!!!" 
//									  			+map_inFile1_URL_N_label.get(currURL)+"!!!"+currURL+"!!!"+curr_bodyTEXT_new +"\n");
//							  writer.flush();
						  }
						  else{
							  int index_beginINDEXi=curr_bodyTEXT.indexOf(" beginINDEXis");
							  if(index_beginINDEXi>0)
								  curr_bodyTEXT=curr_bodyTEXT.substring(0, index_beginINDEXi);
							  
							  curr_bodyTEXT_2_WRITE = curr_bodyTEXT;
							  
//							  writer.append(  map_inFile1_URL_N_keywords.get(currURL)+"!!!"+map_inFile1_URL_N_subject.get(currURL)+"!!!" 
//							  				 +map_inFile1_URL_N_label.get(currURL)+"!!!"+currURL+"!!!"+curr_bodyTEXT +"\n");
//							  writer.flush();
						  }
						  count_found++;
					  }
					  else{
						  int index_beginINDEXi=curr_bodyTEXT.indexOf(" beginINDEXis");
						  if(index_beginINDEXi>0)
							  curr_bodyTEXT=curr_bodyTEXT.substring(0, index_beginINDEXi);
						  
						  curr_bodyTEXT_2_WRITE = curr_bodyTEXT;
						  
//						  writer.append(  map_inFile1_URL_N_keywords.get(currURL)+"!!!"+map_inFile1_URL_N_subject.get(currURL)+"!!!" 
//						  				  +map_inFile1_URL_N_label.get(currURL)+"!!!"+currURL+"!!!"+curr_bodyTEXT +"\n");
//						  writer.flush();
						  //
						  writerDebug.append("url:"+currURL+"\n");
						  writerDebug.flush();
						  count_notfound++;
					  }
					  //WRITING
					  writer.append(  map_inFile1_URL_N_keywords.get(currURL)+"!!!"+map_inFile1_URL_N_subject.get(currURL)+"!!!" 
	  				  					+map_inFile1_URL_N_label.get(currURL)+"!!!"+currURL+"!!!"+curr_bodyTEXT_2_WRITE +"\n");
	  				  writer.flush();  
					  
				  } // for(String currURL:map_URL_bodyTEXT.keySet()){

				  System.out.println("count_notfound:"+count_notfound+" count_found:"+count_found);
				  System.out.println("map_inFile2_seq_eachLine_HUMANannotators.size:"+map_inFile2_seq_eachLine_HUMANannotators.size());
				  System.out.println("map_URL_bodyTEXT.size:"+map_URL_bodyTEXT.size());
				  System.out.println("map_inFile1_URL_N_keywords.size:"+map_inFile1_URL_N_keywords.size());
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		// get_fix_BLANK_bodyTEXT_in_mainFILE
		private static void get_fix_BLANK_bodyTEXT_in_mainFILE( 
																String  baseFolder,
															    String  inFile1_gTruth_human_Annotator,
																String  inFile2_NLPtagged,
																String  outputFile,
																boolean is_it_rerunning
																) {
			// TODO Auto-generated method stub
			TreeMap<String, String> maptaggedNLP = new TreeMap<String, String>();
			try{
				FileWriter writer=null;
				FileWriter writer_ONLYmismatch=null;
				if(is_it_rerunning==false){
					writer=new FileWriter(new File(outputFile));
					writer_ONLYmismatch=new FileWriter(new File(outputFile+"_ONLY_mismatch.txt"));
				}
				else{ //appending
					writer=new FileWriter(new File(outputFile), is_it_rerunning);
					writer_ONLYmismatch=new FileWriter(new File(outputFile+"_ONLY_mismatch.txt"), is_it_rerunning);
				}
				
				TreeMap<String, String> map_URL_as_KEY_alreadyRANinPast=new TreeMap<String, String>();
				
					//loading from outputFile (recent)
				  TreeMap<Integer, String>  map_inFile3_seq_eachLine_OUTPUTfile=
						  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
											.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										     outputFile, 
																										 	 -1, //startline, 
																											 -1, //endline,
																											 " loading", //debug_label
																											 false //isPrintSOP
																											 );
				//
				  for(int seq:map_inFile3_seq_eachLine_OUTPUTfile.keySet()){
					  String [] arr=map_inFile3_seq_eachLine_OUTPUTfile.get(seq).split("!!!");
					  map_URL_as_KEY_alreadyRANinPast.put(arr[0], "");
				  }
				  
				  System.out.println("map_URL_as_KEY_alreadyRANinPast:"+map_URL_as_KEY_alreadyRANinPast.size()+" "+map_URL_as_KEY_alreadyRANinPast);
				
				  //------------- reading inFile1
				  // keywords!!!label!!!comment!!!url!!!partTEXT!!!subject!!!fullTEXT!!!domain 
				  TreeMap<Integer, String>  map_inFile1_seq_eachLine_HUMANannotators=
						  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
											.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																											 inFile1_gTruth_human_Annotator, 
																										 	 -1, //startline, 
																											 -1, //endline,
																											 " loading", //debug_label
																											 false //isPrintSOP
																											 );
				  
				  TreeMap<String, String> map_inFile1_URL_N_label = new TreeMap<String, String>();
				  TreeMap<String, String> map_inFile1_URL_N_keywords = new TreeMap<String, String>();
				  TreeMap<String, String> map_inFile1_URL_N_partTEXT = new TreeMap<String, String>();
				  TreeMap<String, String> map_inFile1_URL_N_fullTEXT = new TreeMap<String, String>();
				  TreeMap<String, String> map_inFile1_URL_N_subject = new TreeMap<String, String>();
				  TreeMap<String, String> map_inFile1_URL_N_DOMAIN = new TreeMap<String, String>();
				  // get <URL,label>
				  // HEADER -> keywords!!!label!!!comment!!!url!!!partTEXT!!!subject!!!fullTEXT!!!domain
				  for(int seq:map_inFile1_seq_eachLine_HUMANannotators.keySet()){
					  String [] arr=map_inFile1_seq_eachLine_HUMANannotators.get(seq).split("!!!");
					  // each PRIMARYKEY & feature
					  map_inFile1_URL_N_label.put(arr[3], arr[1]);
					  map_inFile1_URL_N_keywords.put(arr[3], arr[0]);
					  map_inFile1_URL_N_partTEXT.put(arr[3], arr[4]);
					  map_inFile1_URL_N_subject.put(arr[3], arr[5]);
					  map_inFile1_URL_N_fullTEXT.put(arr[3], arr[6]);
					  map_inFile1_URL_N_DOMAIN.put(arr[3], arr[7]);
				  }
				  
				  //$url!#!#$body__NN!#!#$body
				  TreeMap<Integer, String>  map_inFile2_seq_eachLine_URL_BODYnn_BODY=
						  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
											.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																											 inFile2_NLPtagged, 
																										 	 -1, //startline, 
																											 -1, //endline,
																											 " loading", //debug_label
																											 false //isPrintSOP
																											 );
				  
				  //------------- reading inFile2
				  TreeMap<String, String> map_inFile2_URL_N_bodyTextNN = new TreeMap<String, String>();
				  TreeMap<String, String> map_inFile2_URL_N_bodyText = new TreeMap<String, String>();
				  // get <URL,BODYTEXT> && <URL,BODYTEXTNN>
				  for(int seq:map_inFile2_seq_eachLine_URL_BODYnn_BODY.keySet()){
					  // HEADER
					  if(seq==1){
						  String [] arr_HEADER=map_inFile2_seq_eachLine_URL_BODYnn_BODY.get(seq).split("!#!#");  
 
					  }
					  String [] arr1=map_inFile2_seq_eachLine_URL_BODYnn_BODY.get(seq).split("!#!#");
					  String curr_URL=arr1[0];
					  String curr_bodyTextNN=arr1[1];
					  String curr_bodyText=arr1[2];
					  
					  // 
					  map_inFile2_URL_N_bodyTextNN.put(curr_URL , curr_bodyTextNN);
					  map_inFile2_URL_N_bodyText.put(curr_URL , curr_bodyText);
					   
				  }
				  
				  int count_of_bodyTEXT_hit_and_fixed=0;
				  int count_partTEXT_picked=0;int count_fullTEXT_picked=0; int count_skipping=0;
				  
				  String NLPfolder="/Users/lenin/OneDrive/jar/NLP.backup/";
			
				  
				  //ITERATE FROM inFile2
				  for(String curr_URL:map_inFile2_URL_N_bodyText.keySet()){
					  //already ran ??
					  if(map_URL_as_KEY_alreadyRANinPast.containsKey(curr_URL)){
						  count_skipping++;
						  System.out.println("skipping.. count:"+count_skipping);
						  continue;
					  }
					  
					  //if only exists in human annotators
					  if(map_inFile1_URL_N_label.containsKey(curr_URL)){
						
							System.out.println("calling...20.1...");
						  
						  	//if the NLPbodyTEXT and bodyTEXT are NOT EMPTY in inFile2
						  	if(	map_inFile2_URL_N_bodyText.get(curr_URL).length() >100 &&
						  		map_inFile2_URL_N_bodyTextNN.get(curr_URL).length() >100
						  		){
						  		//already available with tagged NLP , NOT having any bodyTEXT EMPTY
								writer.append(curr_URL+"!!!"+map_inFile2_URL_N_bodyTextNN.get(curr_URL)
														+"!!!"+map_inFile2_URL_N_bodyText.get(curr_URL)+"\n");
								writer.flush();
						  	}
						  	else{ // we need to take bodyTEXT from human annotator inFile1 file and NLP tag it..
						  
						  			String input_ExtractedText_OR_input2="";
						  			// 
						  			if(map_inFile1_URL_N_partTEXT.get(curr_URL).length() > map_inFile1_URL_N_fullTEXT.get(curr_URL).length() ){
						  				input_ExtractedText_OR_input2=map_inFile1_URL_N_partTEXT.get(curr_URL);
						  				count_partTEXT_picked++;
						  			}
						  			else {
						  				input_ExtractedText_OR_input2=map_inFile1_URL_N_fullTEXT.get(curr_URL);
						  				count_fullTEXT_picked++;
						  			}
						  			//
						  			input_ExtractedText_OR_input2=input_ExtractedText_OR_input2.replace("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^PART^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", "")
						  											.replace("-----------------------------------------------------FUL-----------------------------------------------------------", "");
						  			
								    ////// NLP TAGGING
								    
									String inputSingleURL_OR_input1 = "";
									
									boolean is_overwrite_flag_with_flag_model=true;
									POSModel inflag_model2 = new POSModelLoader().load(new File(NLPfolder+ "en-pos-maxent.bin"));
									
									InputStream is = new FileInputStream(NLPfolder+"en-parser-chunking.bin");
									ParserModel inflag_model2_chunking = new ParserModel(is);
									
									System.out.println("calling...20-->"+input_ExtractedText_OR_input2);
									// wrapper_getNLPTrained_en_pos_maxent
									maptaggedNLP = Crawler.wrapper_getNLPTrained_en_pos_maxent(
																			inputSingleURL_OR_input1, // give input_1 or input_2
																			input_ExtractedText_OR_input2, 
																			NLPfolder,
																			"en-pos-maxent.bin", // Flag
																								// ={en-pos-maxent.bin,en-ner-person.bin,en-ner-organization.bin,en-ner-location.bin,en-ner-time.bin,en-ner-money.bin}
																			"", // flag2
																			true, //isSOPdebug,
																			is_overwrite_flag_with_flag_model,
																			inflag_model2,
																			null, //inflag_model2_chunking,
																			baseFolder+"debug_ANALYSIS.txt" //debugFileName
																			);
		
									System.out.println("maptaggedNLP:" + maptaggedNLP);
									String taggedNLP = maptaggedNLP.get("taggedNLP");
									
									writer.append(curr_URL+"!!!"+taggedNLP+"!!!"+input_ExtractedText_OR_input2+"\n");
									writer.flush();
									 // only mismatch
									writer_ONLYmismatch.append(curr_URL+"!!!"+taggedNLP+"!!!"+input_ExtractedText_OR_input2+"\n");
									writer_ONLYmismatch.flush();
									
									count_of_bodyTEXT_hit_and_fixed++;
									System.out.println("calling...20..end");
						  	}
					  } //if(map_inFile1_URL_N_label.containsKey(curr_URL)){
				  } // for(String curr_URL:map_inFile2_URL_N_bodyText.keySet()){
				  
				
				  System.out.println( "count_of_bodyTEXT_hit_and_fixed:"+count_of_bodyTEXT_hit_and_fixed 
						  			 +" count_partTEXT_picked:"+count_partTEXT_picked + " count_fullTEXT_picked:"+count_fullTEXT_picked);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
	
     	// This method used to analyse the CONTEXT (organiz/person/location) among TP AND TN
	 	// get_matched_gTruth_N_Organization_for_TP_OR_TN
		public static void get_matched_gTruth_N_Organization_for_TP_OR_TN(
																			String  inFile1_gTruth_human_Annotator,
																			int		index_for_label_inFile1,
																			int		index_for_URL_inFile1,
																			String  inFile2_NLPtagged,
																			int		inFile2_index_for_interested_CONTEXT,
																			int		inFile2_index_for_URL,
																			String  inFile2_delimiter1,
																			String  inFile2_delimiter2,
																			String  outputFile_TP,
																			String  outputFile_TN
																			){
			FileWriter writerTP=null;
			FileWriter writerTN=null;
			try{
				  
				  writerTP=new FileWriter(outputFile_TP);
				  writerTN=new FileWriter(outputFile_TN);  
				   
				
				  // header->keywords!!!label!!!comment!!!url!!!body!!!....
				  TreeMap<Integer, String>  map_inFile1_seq_eachLine=
						  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
											.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																											 inFile1_gTruth_human_Annotator, 
																										 	 -1, //startline, 
																											 -1, //endline,
																											 " loading", //debug_label
																											 false //isPrintSOP
																											 );
				  
				  // header of inFile2
				  //$url!#!#$body__NN!#!#$body@@@@person_CSVMap@@@@organiz_CSVMap@@@@location_CSVMap@@@@Numbers_CSVMap@@@@Nouns@@@@SentimentMap@@@@
				  //Verb@@@@eachSentencSentimenCSV@@@@ConsecutivePerson@@@@ConsecutiveOrganization@@@@ConsecutiveLocation@@@@ConsecutivePersonNlineNo
				  //@@@@ConsecutiveOrganizationNlineNo@@@@ConsecutiveLocationNlineNo@@@@ConsecutivePersonNlineNoWITHquotes@@@@ConsecutiveOrganizationNlineNoWITHquotes
				  //@@@@ConsecutiveLocationNlineNoWITHquotes
				  //
				  TreeMap<Integer, String>  map_inFile2_seq_eachLine=
						  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
											.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																											 inFile2_NLPtagged, 
																										 	 -1, //startline, 
																											 -1, //endline,
																											 " loading", //debug_label
																											 false //isPrintSOP
																											 );
				  // reading inFile1
				  TreeMap<String, String> map_inFile1_URL_N_label = new TreeMap<String, String>();
				  // get <URL,label>
				  for(int seq:map_inFile1_seq_eachLine.keySet()){
					  String [] arr=map_inFile1_seq_eachLine.get(seq).split("!!!");
					  map_inFile1_URL_N_label.put(arr[index_for_URL_inFile1-1], arr[index_for_label_inFile1-1]);
				  }
				  
				  // reading inFile2
				  TreeMap<String, String> map_inFile2_URL_N_OrganizExtractedFromNLPtagged = new TreeMap<String, String>();
				  String choosenCONTEXT="";
				  // get <URL,OrganizExtractedFromNLPtagged>
				  for(int seq:map_inFile2_seq_eachLine.keySet()){
					  // HEADER
					  if(seq==1){
						  String [] arr_HEADER=map_inFile2_seq_eachLine.get(seq).split(inFile2_delimiter2);  
						  choosenCONTEXT=arr_HEADER[inFile2_index_for_interested_CONTEXT-1];
					  }
					  String [] arr1=map_inFile2_seq_eachLine.get(seq).split(inFile2_delimiter1);
					  String [] arr2=map_inFile2_seq_eachLine.get(seq).split(inFile2_delimiter2);
					  String curr_URL=arr1[inFile2_index_for_URL-1];
					  String curr_Organization=arr2[inFile2_index_for_interested_CONTEXT-1];
					  //
					  map_inFile2_URL_N_OrganizExtractedFromNLPtagged.put(curr_URL, curr_Organization);
				  }
				  
				  String curr_context="";
				  //match
				  for(String curr_url:map_inFile1_URL_N_label.keySet()){
					  curr_context="";
					  // 
					  if(map_inFile2_URL_N_OrganizExtractedFromNLPtagged.containsKey(curr_url))
						  curr_context=map_inFile2_URL_N_OrganizExtractedFromNLPtagged.get(curr_url);
					  //
					  if(map_inFile1_URL_N_label.get(curr_url).equals("1")){
						  writerTP.append(curr_url+"!!!"+ curr_context +"\n");
						  writerTP.flush();
					  }
					  // 
					  if(map_inFile1_URL_N_label.get(curr_url).equals("0")){
						  writerTN.append(curr_url+"!!!"+ curr_context +"\n");
						  writerTN.flush();
					  }
					  
				  }
				  System.out.println("######################################");
				  System.out.println("map_inFile1_seq_eachLine.size:"+map_inFile1_seq_eachLine.size() +" <--URL,LABEL");
				  System.out.println("map_inFile2_seq_eachLine.size:"+map_inFile2_seq_eachLine.size());
				  System.out.println("######################################");
				  System.out.println("map_inFile1_URL_N_label.size:"+map_inFile1_URL_N_label.size());				  
				  System.out.println("map_inFile2_URL_N_OrganizExtractedFromNLPtagged.size:"+map_inFile2_URL_N_OrganizExtractedFromNLPtagged.size());

				  System.out.println("######################################");
				  System.out.println("choosenCONTEXT:"+choosenCONTEXT);
				  System.out.println("######################################");
			}
			catch(Exception e){
				e.printStackTrace();
			}
			  
		}
	  
		//main
		public static void main(String[] args) throws IOException {
			 //
			 String header_inFile2= "$url!#!#$body__NN!#!#$body@@@@person_CSVMap@@@@organiz_CSVMap@@@@location_CSVMap@@@@Numbers_CSVMap@@@@Nouns@@@@SentimentMap@@@@"+
				 	 				"Verb@@@@eachSentencSentimenCSV@@@@ConsecutivePerson@@@@ConsecutiveOrganization@@@@ConsecutiveLocation@@@@ConsecutivePersonNlineNo"+
				 	 				"@@@@ConsecutiveOrganizationNlineNo@@@@ConsecutiveLocationNlineNo@@@@ConsecutivePersonNlineNoWITHquotes"+
				 	 				"@@@@ConsecutiveOrganizationNlineNoWITHquotes@@@@ConsecutiveLocationNlineNoWITHquotes";
			 String[] arr10=header_inFile2.split("@@@@");
			////// above just for finding the header purpose
			 
			String baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/";
			// inFile1  -- human annotator 
			String inFile1_gTruth_human_Annotator="/Users/lenin/Dropbox/#problems/p18/FromMaitrayi/all.txt";
				   inFile1_gTruth_human_Annotator="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txtNEW_1000labelled_confCorrected.txt_1st1000ln_removeIrrelevaLABEL.txt";
				   inFile1_gTruth_human_Annotator="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL.txt";
			// inFile2  --
			String inFile2_NLPtagged=
					"/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/tmp/"+
					"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt.3all_p18.txtdded_perso_origz_loc_verb2.txt";
					inFile2_NLPtagged="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/tmp/"
//									  +"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt.3all_p18.txtdded_perso_origz_loc_verb2.txt";
									  +"O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged.txt_added_perso_origz_loc_verb2.txt";
			
			// (SEE SCRIPT AT THE END TO FIND APPROPRIATE INDEX
			// organiz/ person /locaiton 
			 int inFile2_index_for_interested_CONTEXT=16;
			 
			 String outputFile_TP=baseFolder+"OUTPUT_matched_url_N_CONTEXT_TP_"+inFile2_index_for_interested_CONTEXT+"_"+ arr10[inFile2_index_for_interested_CONTEXT-1] +".txt";
			 String outputFile_TN=baseFolder+"OUTPUT_matched_url_N_CONTEXT_TN_"+inFile2_index_for_interested_CONTEXT+"_"+ arr10[inFile2_index_for_interested_CONTEXT-1] +".txt";
			 
			 //comment/uncomment
			 // This method used to analyse the CONTEXT (organiz/person/loca) among TP AND TN
			 // get_matched_gTruth_N_Organization_for_TP_OR_TN
			 get_matched_gTruth_N_Organization_for_TP_OR_TN(
															inFile1_gTruth_human_Annotator,
															3, //index_for_label_inFile1,
															4, //index_for_URL_inFile1,
															inFile2_NLPtagged,
															inFile2_index_for_interested_CONTEXT,
															5, //inFile2_index_for_URL
															"!!!", //inFile2_delimiter1
															"@@@@", //inFile2_delimiter2
															outputFile_TP,
															outputFile_TN
															);
			 
			 
			 
			 //fixing the blank text in file "OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all.txt"
			 
			 // SOME OF THE urls (docs) have blank bodyTEXT in inFile, match url, and get the bodyTEXT from inFile2
			 String inFile1_blank_text_file=
					 baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all.txt";
			 // SOME OF THE urls (docs) have blank bodyTEXT
			 String inFile2_HUMAN_ANNOTATOR=inFile1_gTruth_human_Annotator;
			 String outputFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt";
			 //comment / uncomment
			 // fixing the blank text in file inFile1_blank_text_file="OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all.txt"
//			 get_fix_BLANK_bodyTEXT_in_mainFILE(
//					 									baseFolder,
//					 									inFile2_HUMAN_ANNOTATOR,
//					 									inFile1_blank_text_file,
//														outputFile,
//														true //<----------is_it_rerunning (first time run as false)
//												);
			 
			 String domainNAME_patternTOcatchJUNKwords=  "hindustantimes.com!!! inputField !!!inputField__NN,"+
														 "gulftoday.ae!!!Internews &nbsp;!!!Internews__NNP &nbsp;__NNP,"+
														 "ibnlive.com!!!more+!!!More+__NNP ,"+
														 "reuters.!!!; Editing by !#!# var var!!!;__NNP Editing__NNP"+
														 "straitstimes.com.feedsportal!!! AGENCE NEW YORK TIMES,"+
														 "thehimalayantimes.!!!a version of this article !!!A__DT version__NN article__NN,"+
														 "thebangladeshtoday.com!!!.single 160px!!!.single__NNP 160px__CD,"+
														 "straitstimes.com!!!More Share!!!More__JJR Share__NN,"+
														 "japantimes.co.jp!!!Photos Click!!!Photos__NNP Click__NNP ,"+
//														 "ibtimes.com!!!document.location.protocol!!!sct__NN document.location.protocol,"+
														 "ibtimes.com!!!|| || sct!#!#document.location.protocol!!!sct__NN document.location.protocol,"+
														 "denverpost.com!!!Print &nbsp;&nbsp; Email &nbsp;&nbsp; Fo!!!Print__NNP &nbsp;&nbsp;__NNP,"+
														 "Abc.net!!!if inlineVideoData!!!inlineVideoData__NNS,"+
														 "dn.pt!!! or Print !!!,"+
														 "chron.com!!! a ... more,"+
														 "clarionproject.org!!!follow him on facebook!#!#check this box!!!follow_VBP him_PRP on_IN facebook_NN!#!#check_NN this_DT box_NN,"+
														 "english.irib.ir!!!Define button default language!!!Define__NNP button__NN default__NN,"+
														 "timesofindia.feedsportal!!!RELATED From around the web More!!!RELATED__VBN From__IN web__NN,"+
														 "telegraph.feedsportal.com/!!!var optaTeamLinkingCallback!!!var__JJ optaTeamLinkingCallback__NN,"+
														 "bbc.co.uk!!!Share this story!!!Share__NNP story__NN,"+
														 "cbc.ca!!!with files from Reuters!!!With__IN files__NNS Reuters__NNP,"+
														 "health.com!!!Get the latest health!!!,"+
														 "globalpost.com!!!section:&nbsp; living location,"+
														 "cnn.com!!!contributed to this report,"+
														 "biznews.com!!!| Anglo America,"+
														 "abc.net.au!!!. ABC!#!# Top Stories!!!,"+
														 "ndtv.!!!Share this story on width,"+
														 "feeds.nbcnews.com!!!ET Next Story !!!,"+
														 "24-7pressrelease!!!Read more Press Releases,"+
														 "e-pao.net!!!var but Define a,"+
														 "edition.cnn.com!!!jshint true,"+
														 "english.sina.com!!!var emo new var,"+
														 "feeds.nbcnews.com!!!advertisement Play,"+
														 "feeds.washingtonpost.com!!!read full article,"+
														 "gulfnews.com!!!— Gulf News Archive Add,"+
														 "kaiserhealthnews.org!!!. this article,"+
														 "kathmandupost.ekantipur.com!!! full story ,"+
														 "khn.org!!!this story is part of a,"+
														 "muscatdaily.com!!!Privacy policy Sitemap ,"+
														 "myscienceacademy.org!!!Materials may be edited,"+
														 "nation.com.pk!!!Tweet More by SpeciaL,"+
														 "ndtv.com.feedsportal.com!!!Share this story on wid,"+
														 "news.yahoo.com!!!also watch:,"+
														 "newsinfo.inquirer.net!!!FOLLOW INQUIRER ,"+
														 "online.wsj.com!!!To Read the Full Story,"+
														 "rt-tv.f29hgb.ru!!! read more:,"+
														 "timesofindia.indiatimes.com!!!RELATED From around the web More,"+
														 "aljazeera.com!!!Al Jazeera Content on this website,"+
														 "andhrawishesh.com!!!If you enjoyed this Post,"+
														 "assamtimes.org!!! Hot news AUTHOR INFORMATION AT,"+
														 "azernews.az!!!Follow us on Twitter,"+
														 "bignewsnetwork.com!!!try share this story,"+
														 "biznews.com!!!; Editor,"+
														 "brecorder.com!!!Copyright APP Press,"+
														 "buenosairesherald.com!!!Comment Size email,"+
														 "castanet.net!!!Top Stories Report,"+
														 "dailystar.co.uk!!!try Comments ,"+
														 "globalpost.com!!!updated date:,"+
														 "hellenicshippingnews.com!!!Bloomberg hellenicshippingnews,"+
														 "huffingtonpost.com!!!on twitter,"+
														 "israelnationalnews.com!!! read more";
			 
			 // output of method "get_fix_BLANK_bodyTEXT_in_mainFILE" is having junk words at the end of the bodyTEXT, same thing got NLP tagged.
			 // in this below method, we will clean the junk words at the end of bodyTEXT, and get it again TAGGED.
//			 get_cleaned_junk_words_at_endOF_bodyTEXT(
//					 								  baseFolder,
//					 								  outputFile, //input_1
//					 								  inFile2_HUMAN_ANNOTATOR, // input_2
//					 								  outputFile+"NEW.txt", //output
//					 								  true, //is_header_present
//					 								  domainNAME_patternTOcatchJUNKwords
//					 								 );
			 
			 //---------------- BEGIN			
			 // FIND INDEX 
			 //comment / uncomment ----
			 //below is head of "inFile2_NLPtagged"
//			 String header_inFile2=
//					 	 "$url!#!#$body__NN!#!#$body@@@@person_CSVMap@@@@organiz_CSVMap@@@@location_CSVMap@@@@Numbers_CSVMap@@@@Nouns@@@@SentimentMap@@@@"+
//					 	 "Verb@@@@eachSentencSentimenCSV@@@@ConsecutivePerson@@@@ConsecutiveOrganization@@@@ConsecutiveLocation@@@@ConsecutivePersonNlineNo"+
//					 	 "@@@@ConsecutiveOrganizationNlineNo@@@@ConsecutiveLocationNlineNo@@@@ConsecutivePersonNlineNoWITHquotes"+
//					 	 "@@@@ConsecutiveOrganizationNlineNoWITHquotes@@@@ConsecutiveLocationNlineNoWITHquotes";
//			 String[] arr10=header_inFile2.split("@@@@");
			 
			 
			 int c=0;
			 //
			 while(c<arr10.length){
				 System.out.println("index:"+c  +" arr10[c]:"+arr10[c]+" <-------- INCREMENT BY index by 1 for var inFile2_index_for_interested_CONTEXT ");
				 c++;
			 }

			 /*
			 ------------------------------------------------------------------------
			 inFile2_index_for_interested_CONTEXT= INCREMENT BY index by 1 
			 ------------------------------------------------------------------------
			 index:0 arr10[c]:$url!#!#$body__NN!#!#$body
			 index:1 arr10[c]:person_CSVMap
			 index:2 arr10[c]:organiz_CSVMap
			 index:3 arr10[c]:location_CSVMap
			 index:4 arr10[c]:Numbers_CSVMap
			 index:5 arr10[c]:Nouns
			 index:6 arr10[c]:SentimentMap
			 index:7 arr10[c]:Verb
			 index:8 arr10[c]:eachSentencSentimenCSV
			 index:9 arr10[c]:ConsecutivePerson
			 index:10 arr10[c]:ConsecutiveOrganization
			 index:11 arr10[c]:ConsecutiveLocation
			 index:12 arr10[c]:ConsecutivePersonNlineNo
			 index:13 arr10[c]:ConsecutiveOrganizationNlineNo
			 index:14 arr10[c]:ConsecutiveLocationNlineNo
			 index:15 arr10[c]:ConsecutivePersonNlineNoWITHquotes
			 index:16 arr10[c]:ConsecutiveOrganizationNlineNoWITHquotes
			 index:17 arr10[c]:ConsecutiveLocationNlineNoWITHquotes
			 */
			 //---------------- END
		}

		

	
}
