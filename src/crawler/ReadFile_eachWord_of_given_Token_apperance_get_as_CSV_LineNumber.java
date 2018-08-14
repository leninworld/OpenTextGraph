package crawler;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import crawler.Verify_input_createGBADfromTextUsing.WrongInputException;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import p8.Wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati;

  
public class ReadFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber {
	 
       // is_numeric
		public static HashMap<Integer,Boolean> is_numeric(String inString){
			HashMap<Integer,Boolean> mapOut=new HashMap<Integer, Boolean>();
			try{
				
				char a= inString.charAt(0);
				char b= inString.charAt(1);
				
				boolean b_=false;
				boolean a_=IsNumeric.isNumeric(String.valueOf(a));
				
				try{
					b_=IsNumeric.isNumeric(String.valueOf(b));
				}
				catch(Exception e){
					b_=false;
				}
				// 
				if(a_==true || b_==true){										
				}
				
				mapOut.put(1, a_);
				mapOut.put(2, b_);
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return mapOut;
		}
		// read File ; readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber_CUSTOMIZED
		//first file has a token to be matched and searched in second file. if matched get LINE_CSV for each token of first file in second.
		public static void readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber_CUSTOMIZED(
																					String  baseFolder,
																					String  first_File,
																					String  delimiter_for_first_File_2_split,
																					int	    token_interested_in_first_File,
																					String  Second_File,
																					String  delimiter_for_second_File_2_split,
																					int	    token_interested_in_second_File,
																					boolean is_HeaderPresentIn_first_file
																					){
			
			try {
				TreeMap<Integer,String> map_firstfile_lineNo_interestedtoken =new TreeMap();
				TreeMap<Integer,String> map_secondfile_lineNo_interestedtoken =new TreeMap();
				FileWriter writer=new FileWriter(new File(first_File+"_LINECSV.txt"));
				TreeMap<Integer, String> map_lineNo_Line_firstFile=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
					readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
																	(  first_File, 
																	   -1, // 	startline, 
																	   -1, //  	endline,
																	   " debug_label",
																	   false // isPrintSOP
																	);
				
				TreeMap<Integer, String> map_lineNo_Line_secondFile=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
						readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
																		(  Second_File, 
																		   -1, // 	startline, 
																		   -1, //  	endline,
																		   "2 debug_label",
																		   false // isPrintSOP
																		);
				
				//if exists in second file , then its true positive
				String header="";
				
				if(is_HeaderPresentIn_first_file)
					header=map_lineNo_Line_firstFile.get(1);
				//FIRST FILE (interested list of words)
				for(int lineNo:map_lineNo_Line_firstFile.keySet()){
					//
					if(is_HeaderPresentIn_first_file && lineNo==1){
						continue;
					}
					String [] arr_first=map_lineNo_Line_firstFile.get(lineNo).split(delimiter_for_first_File_2_split);
					map_firstfile_lineNo_interestedtoken.put(lineNo,    arr_first[token_interested_in_first_File-1] );
				}
				//SECOND FILE
				for(int lineNo:map_lineNo_Line_secondFile.keySet()){
					 
					String [] arr_second=map_lineNo_Line_secondFile.get(lineNo).split(delimiter_for_second_File_2_split);
					map_secondfile_lineNo_interestedtoken.put(lineNo,   arr_second[token_interested_in_second_File-1 ]);
				}

				int linesWrite_to_outputFile=0;
				TreeMap<String, String> map_TokenFromFile1_matchedLinesCSVfromFile2=new TreeMap<String, String>();
				//iterate first file
				for( int lineNo1:map_firstfile_lineNo_interestedtoken.keySet() ){
					
					String curr_token_from_file1_2_searchfor=map_firstfile_lineNo_interestedtoken.get(lineNo1).toLowerCase();
					
					//
					for( int lineNo2:map_secondfile_lineNo_interestedtoken.keySet()  ){
						String curr_token_file2=map_secondfile_lineNo_interestedtoken.get( lineNo2  ).toLowerCase();
						
						if(curr_token_file2.indexOf(curr_token_from_file1_2_searchfor ) >=0){
							
							//
							if(!map_TokenFromFile1_matchedLinesCSVfromFile2.containsKey(curr_token_from_file1_2_searchfor ) ){
								map_TokenFromFile1_matchedLinesCSVfromFile2.put(curr_token_from_file1_2_searchfor, String.valueOf( lineNo2) );
							}
							else{
								String temp=map_TokenFromFile1_matchedLinesCSVfromFile2.get(curr_token_from_file1_2_searchfor);
								temp=temp+","+lineNo2;
								map_TokenFromFile1_matchedLinesCSVfromFile2.put(curr_token_from_file1_2_searchfor, temp); 
							}
							
						}
						
					} //end for 
				} //end for 
				
				
				//HEADER
				if(header.length()>0)
					writer.append(header+  "\n");
				//writing 
				for(String token_File1:map_TokenFromFile1_matchedLinesCSVfromFile2.keySet()){
					writer.append( token_File1 +delimiter_for_first_File_2_split +  map_TokenFromFile1_matchedLinesCSVfromFile2.get(token_File1) +"\n");
					writer.flush();
				} //end for
				
				
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
		}
		
		// Iterate each of the "consecutive organization" and the line no where it is found in that document..
		// read a array of <key,value>
		public static TreeMap<Integer, TreeMap<Integer, TreeMap<Integer,String>>> 
																		get_keyValue(
																				String [] arr_1,
																				TreeMap<Integer, TreeMap<Integer,String>> map_DocID_uniqSeqNorganizationCONTEXT,
																				TreeMap<Integer, TreeMap<Integer,String>> map_DocID_uniqSeqNlineNoWITHINdocument_Organizat,
																				int curr_docId
																				){
			
			TreeMap<Integer,TreeMap<Integer, TreeMap<Integer,String>>>  mapOut=new TreeMap<Integer,TreeMap<Integer, TreeMap<Integer,String>>>();
			try {
				//----BEGIN - iterate each of the "consecutive organization" and the line no where it is found in that document..
		  		  int c1=0; int uniqSeq=0;
		  		  while(c1<arr_1.length){
		  			  String feature_N_value=arr_1[c1];
		  			  String [] arr_1_2=feature_N_value.split("="); // splits on "1=Arlington Police Department responded !!!1"
		  			  
		  			  if(arr_1_2.length<2) {c1++; continue;}
//		  			  System.out.println("feature_N_value:"+feature_N_value);
		  			  String curr_seq_of_organiz=arr_1_2[0]; //this local seq for each consecutive organization within document IS not useful here.. 
		  			  String curr_OrganizContextNlineNo=arr_1_2[1];
		  			  
		  			// PS: docID !=lineNo here, this is just lineNo within document where this consecutive organization is found
		  			  String [] curr_lineNo_within_currDocID= curr_OrganizContextNlineNo.split("!!!"); 
		  			  
		  			  String curr_OrganizContextNlineNo_ONLY=curr_lineNo_within_currDocID[0]; // consecutive organization..
		  			  int curr_lineNo_withinDocID=Integer.valueOf(curr_lineNo_within_currDocID[1].replace("!",""));
		  			  
		  			  uniqSeq++;
		  			   //store the doc-wise 
		  			  if(map_DocID_uniqSeqNorganizationCONTEXT.containsKey(curr_docId)){
		  				  //uniq Seq  + context for curr DocID
		  				  TreeMap<Integer, String> tmp =map_DocID_uniqSeqNorganizationCONTEXT.get(curr_docId);
		  				  tmp.put(Integer.valueOf(uniqSeq), curr_OrganizContextNlineNo_ONLY);
		  				  map_DocID_uniqSeqNorganizationCONTEXT.put(curr_docId, tmp);
		  				  
		  				  //uniq Seq  + lineNo (within Doc) for curr DocID
		  				  TreeMap<Integer, String> tmp2 =map_DocID_uniqSeqNlineNoWITHINdocument_Organizat.get(curr_docId);
		  				  tmp2.put(Integer.valueOf(uniqSeq), String.valueOf(curr_lineNo_withinDocID));
		  				  map_DocID_uniqSeqNlineNoWITHINdocument_Organizat.put(curr_docId, tmp2);
		  			  }
		  			  else{
		  				  //uniq Seq (docID)  + context for curr DocID
		  				  TreeMap<Integer, String> tmp =new TreeMap<Integer, String>();
		  				  tmp.put(Integer.valueOf(uniqSeq), curr_OrganizContextNlineNo_ONLY);
		  				  map_DocID_uniqSeqNorganizationCONTEXT.put(curr_docId, tmp);
		  				  
		  				  //uniq Seq (docID) + lineNo (within Doc) for curr DocID
		  				  TreeMap<Integer, String> tmp2 =new TreeMap<Integer, String>();
		  				  tmp2.put(Integer.valueOf(uniqSeq), String.valueOf(curr_lineNo_withinDocID));
		  				  map_DocID_uniqSeqNlineNoWITHINdocument_Organizat.put(curr_docId, tmp2);
		  				  
		  			  }
		  			  
		  			  c1++;
		  		  }  //END while(c1<arr_1.length){
		  		  
		  		  //----END - iterate each of the "consecutive organization" and the line no where it is found in that document..
		  		  mapOut.put(1, map_DocID_uniqSeqNorganizationCONTEXT);
		  		  mapOut.put(2, map_DocID_uniqSeqNlineNoWITHINdocument_Organizat);
				
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			return mapOut;
		}
		
//		  
//		  //uniq Seq  + lineNo (within Doc) for curr DocID
//		  TreeMap<Integer, String> tmp2 =map_DocID_uniqSeqNlineNoWITHINdocument_Organizat.get(curr_docId);
//		  tmp2.put(Integer.valueOf(uniqSeq), String.valueOf(curr_lineNo_withinDocID));
//		  map_DocID_uniqSeqNlineNoWITHINdocument_Organizat.put(curr_docId, tmp2);
//	  }
//	  else{
//		  //uniq Seq (docID)  + context for curr DocID
//		  TreeMap<Integer, String> tmp =new TreeMap<Integer, String>();
//		  tmp.put(Integer.valueOf(uniqSeq), curr_OrganizContextNlineNo_ONLY);
//		  map_DocID_uniqSeqNorganizationCONTEXT.put(curr_docId, tmp);
//		  
//		  //uniq Seq (docID) + lineNo (within Doc) for curr DocID
//		  TreeMap<Integer, String> tmp2 =new TreeMap<Integer, String>();
//		  tmp2.put(Integer.valueOf(uniqSeq), String.valueOf(curr_lineNo_withinDocID));
//		  map_DocID_uniqSeqNlineNoWITHINdocument_Organizat.put(curr_docId, tmp2);
//		  
//	  }
//	  
//	  c1++;
//  }  //END while(c1<arr_1.length){

//		  c1=0;
//		  uniqSeq=0;
//		  while(c1<arr_2.length){
//			  String feature_N_value=arr_2[c1];
//			  String [] arr_1_2=feature_N_value.split("="); // splits on "1=Arlington Police Department responded !!!1"
//			  
//			  if(arr_1_2.length<2) {c1++; continue;}
////			  System.out.println("feature_N_value:"+feature_N_value);
//			  String curr_seq_of_person=arr_1_2[0]; //this local seq for each consecutive organization within document IS not useful here.. 
//			  String curr_PersonContextNlineNo=arr_1_2[1];
//			  
//			// PS: docID !=lineNo here, this is just lineNo within document where this consecutive organization is found
//			  String [] curr_lineNo_within_currDocID= curr_PersonContextNlineNo.split("!!!"); 
//			  
//			  String curr_PersonContextNlineNo_ONLY=curr_lineNo_within_currDocID[0]; // consecutive organization..
//			  int curr_lineNo_withinDocID=Integer.valueOf(curr_lineNo_within_currDocID[1].replace("!",""));
//			  
//			  uniqSeq++;
//			   //store the doc-wise 
//			  if(map_DocID_uniqSeqNpersonCONTEXT.containsKey(curr_docId)){
//				  //uniq Seq  + context for curr DocID
//				  TreeMap<Integer, String> tmp =map_DocID_uniqSeqNpersonCONTEXT.get(curr_docId);
//				  tmp.put(Integer.valueOf(uniqSeq), curr_PersonContextNlineNo_ONLY);
//				  map_DocID_uniqSeqNpersonCONTEXT.put(curr_docId, tmp);
//				  
//				  //uniq Seq  + lineNo (within Doc) for curr DocID
//				  TreeMap<Integer, String> tmp2 =map_DocID_uniqSeqNlineNoWITHINdocument_Person.get(curr_docId);
//				  tmp2.put(Integer.valueOf(uniqSeq), String.valueOf(curr_lineNo_withinDocID));
//				  map_DocID_uniqSeqNlineNoWITHINdocument_Person.put(curr_docId, tmp2);
//			  }
//			  else{
//				  //uniq Seq (docID)  + context for curr DocID
//				  TreeMap<Integer, String> tmp =new TreeMap<Integer, String>();
//				  tmp.put(Integer.valueOf(uniqSeq), curr_PersonContextNlineNo_ONLY);
//				  map_DocID_uniqSeqNpersonCONTEXT.put(curr_docId, tmp);
//				  
//				  //uniq Seq (docID) + lineNo (within Doc) for curr DocID
//				  TreeMap<Integer, String> tmp2 =new TreeMap<Integer, String>();
//				  tmp2.put(Integer.valueOf(uniqSeq), String.valueOf(curr_lineNo_withinDocID));
//				  map_DocID_uniqSeqNlineNoWITHINdocument_Person.put(curr_docId, tmp2);
//				  
//			  }
//			  
//			  c1++;
//		  }  //END while(c1<arr_1.length){
		
		// p18 - for "consecutive tokens" , try to connect edge to another consecutive token based on edit_distance match or other similarity measures.
		// so the way graph is created is different from below method "readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber()"
		public static void readFile_eachWord_of_given_ConsecutiveToken_apperance_get_as_CSV_LineNumber_p18(
																						String  baseFolder,
																						String  inputFile_1,
																						String  delimiter_for_first_File_1_split,
																						String  delimiter_to_be_added_to_first_File,
																						int	    token_interested_in_first_File,
																						String  inFile_2_human_annotator_labeledFile,
																						String  tokens_4_URL_N_LABEL_4_inFile_2_human_annotator_labeledFile,
																						String  delimiter_to_be_used_to_append_in_OUTfile,
																						String  out_File,
																						int		run_for_top_N_lines,
																						boolean is_append_out_File_,
																						boolean is_header_present,
																						String  curr_feature_name, //mainly to debug print
																						boolean is_writeOUTPUT_mismatchedURLnBODYTEXT_and_NLPthem, //only time set this flag as tru and run
																						TreeMap<Integer, String> map_seq_subsequentFileshavingNLPtagged
																						){
	  		  int index_4_person=14;
	  		  int index_4_organization=15;
	  		  int index_4_location=16;
			try {
				FileWriter writer_debug=new FileWriter(new File(baseFolder+"debug_p18_algo.txt"));
				String outFile=baseFolder+"out_url_with_emptyBODY_inFile1_butHASBODYinFile2.txt";
				// above "outFile" is input for "readFile_pick_a_Token_doNLPtagging_writeOUT" and its
				// output is "outFile2" 
				String outFile2=baseFolder+"out_url_with_emptyBODY_inFile1_butHASBODYinFile2_NLPtagged.txt";
				//read file from outFile2 and exchange 2nd and 3rd column..
				String outFile3=baseFolder+"out_url_with_emptyBODY_inFile1_butHASBODYinFile2_url_NLPtagged_bodyText.txt";
				
				FileWriter writer_out=null,writer_out3=null;
				//
				if(is_writeOUTPUT_mismatchedURLnBODYTEXT_and_NLPthem==true){
					writer_out=new FileWriter(new File(outFile));
					writer_out3=new FileWriter(new File(outFile3));
				}
				TreeMap<Integer, TreeMap<String, String>> map_FileSequence_urlNorganization=new TreeMap<Integer, TreeMap<String,String>>();
				TreeMap<Integer, TreeMap<String, String>> map_FileSequence_urlNperson=new TreeMap<Integer, TreeMap<String,String>>();
				TreeMap<Integer, TreeMap<String, String>> map_FileSequence_urlNlocation=new TreeMap<Integer, TreeMap<String,String>>();
				
				// we ran NLP tagging again and again for missing organization documents..so we store that in a map here..
				// read each file from list of files in "map_seq_subsequentFileshavingNLPtagged"
				for(int seq:map_seq_subsequentFileshavingNLPtagged.keySet()){
					
					  String currFile=map_seq_subsequentFileshavingNLPtagged.get(seq);
					  //
					  TreeMap<Integer, String>  map_eachLine_inFile_2=
							  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
												.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																												 currFile, 
																											 	 -1, //startline, 
																												 -1, //endline,
																												 " loading", //debug_label
																												 false //isPrintSOP
																												 );
					  writer_debug.append("\ncurrently processing: map_eachLine_inFile_2.size:"+map_eachLine_inFile_2.size()    +" currFile:"+currFile);
					  writer_debug.flush();
					  
					  // READ each file and store it in a separate  MAP
					  for(int seq2:map_eachLine_inFile_2.keySet()){
						  String eachLine=map_eachLine_inFile_2.get(seq2).replace("!#!#", "@@@@").replace("!!!", "@@@@"); //normalize delimiter
			  		  	  String[] arr_features=eachLine.split("@@@@");
			  		  	  if(seq2==1) continue; //header
			  		  	  if(seq2<=3){
			  		  		  writer_debug.append("\n arr_features.len="+arr_features.length +" 1:"+map_eachLine_inFile_2.get(seq2)+" 2:"+eachLine );
			  		  		  writer_debug.flush();
			  		  	  }
				  		  if(arr_features.length>=19){
					  		  String curr_url=arr_features[0]; // url			  		  
					  		  String curr_sentimentsOFallLINESinCURRdoc_CSV=arr_features[10];
					  		  String curr_personNLineno=arr_features[index_4_person];
					  		  String curr_organizationNLineno=arr_features[index_4_organization];
					  		  String curr_locationNLineno=arr_features[index_4_location];
				  			  //organization
					  		  if(map_FileSequence_urlNorganization.containsKey(seq2)){
					  			  TreeMap<String,String> tmp=map_FileSequence_urlNorganization.get(seq2);
					  			  tmp.put(curr_url, curr_organizationNLineno);
					  			  map_FileSequence_urlNorganization.put(seq2, tmp);
					  		  }
					  		  else{
					  			  TreeMap<String,String> tmp=new TreeMap<String, String>();
					  			  tmp.put(curr_url, curr_organizationNLineno);
					  			  map_FileSequence_urlNorganization.put(seq2, tmp);
					  		  }
					  		  //person
					  		  if(map_FileSequence_urlNperson.containsKey(seq2)){
					  			  TreeMap<String,String> tmp=map_FileSequence_urlNperson.get(seq2);
					  			  tmp.put(curr_url, curr_personNLineno);
					  			  map_FileSequence_urlNperson.put(seq2, tmp);
					  		  }
					  		  else{
					  			  TreeMap<String,String> tmp=new TreeMap<String, String>();
					  			  tmp.put(curr_url, curr_personNLineno);
					  			  map_FileSequence_urlNperson.put(seq2, tmp);
					  		  }
					  		  //location
					  		  if(map_FileSequence_urlNlocation.containsKey(seq2)){
					  			  TreeMap<String,String> tmp=map_FileSequence_urlNlocation.get(seq2);
					  			  tmp.put(curr_url, curr_locationNLineno);
					  			  map_FileSequence_urlNlocation.put(seq2, tmp);
					  		  }
					  		  else{
					  			  TreeMap<String,String> tmp=new TreeMap<String, String>();
					  			  tmp.put(curr_url, curr_locationNLineno);
					  			  map_FileSequence_urlNlocation.put(seq2, tmp);
					  		  }
				  		  } //end if(arr_features.length>=19){
					  } //END for(int seq2:map_eachLine_inFile_2.keySet()){
					  
				} // END for(int seq:map_seq_subsequentFileshavingNLPtagged.keySet()){
				
		    	// File 2 ---  <URL,LABEL> LoadMultipleValueToMap2_AS_KEY_VALUE
		    	TreeMap<String, String> map_URL_N_LABEL_from_humanANNOTATOR_inFile_2=
							    	LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE(inFile_2_human_annotator_labeledFile,
							    																			  "!!!", //delimiter,
							    																			  tokens_4_URL_N_LABEL_4_inFile_2_human_annotator_labeledFile, //"1,2",
							    																			  "", //append_suffix_at_each_line
							    																			  false, //is_have_only_alphanumeric
							    																			  false //isSOPprint
							    																			  );
		    	
		    	  // File 2 --- eachLine ---- LoadMultipleValueToMap2_AS_KEY_VALUE
			  	  TreeMap<Integer, String>  map_eachLine_inFile_2=
						  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
											.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																											 inFile_2_human_annotator_labeledFile, 
																										 	 -1, //startline, 
																											 -1, //endline,
																											 " loading", //debug_label
																											 false //isPrintSOP
																											 );
			  	  TreeMap<String, String > map_url_partText= new TreeMap<String, String>();
			  	  TreeMap<String, String > map_url_fullText= new TreeMap<String, String>();
			  	  // for each url ,get part text and full text from file 2
			  	  for(int seq:map_eachLine_inFile_2.keySet()){
			  		  String [] arr_features_inFile2=map_eachLine_inFile_2.get(seq).split("!!!");
			  		  String curr_url_inFile2=arr_features_inFile2[3];
			  		  String curr_PART_bodyText=arr_features_inFile2[4].replace("^PART^", " ").replace("^", "").replace("  ", "");
			  		  String curr_FULL_bodyText=arr_features_inFile2[6].replace("-FUL-", " ").replace("-", "").replace("  ", "");
			  		  map_url_partText.put(curr_url_inFile2, curr_PART_bodyText);
			  		  map_url_fullText.put(curr_url_inFile2, curr_FULL_bodyText);
			  	  }
		    	
//		    	TreeMap<String, String> map_URL_N_LABEL_from_humanANNOTATOR_inFile_2=new TreeMap<String, String>();
//		    	 // 
//		    	for(String eachLine:map_URL_N_LABEL_from_humanANNOTATOR_inFile_2_eachLine.keySet()){
//		    		
//		    	}
		    	
//				String filename,
//				String delimiter,
//				String index_of_key_AND_value_as_CSV,
//				String append_suffix_at_each_line,
//				boolean is_have_only_alphanumeric,
//				boolean isSOPprint
		    	
//		    	$url!#!#$body__NN!#!#$body@@@@person_CSVMap@@@@organiz_CSVMap@@@@location_CSVMap@@@@Numbers_CSVMap@@@@Nouns@@@@SentimentMap@@@@Verb
//		    	@@@@eachSentencSentimenCSV@@@@ConsecutivePerson@@@@ConsecutiveOrganization@@@@ConsecutiveLocation@@@@ConsecutivePersonNlineNo@@@@
//		    	ConsecutiveOrganizationNlineNo@@@@ConsecutiveLocationNlineNo@@@@ConsecutivePersonNlineNoWITHquotes@@@@ConsecutiveOrganizationNlineNoWITHquotes
//		    	@@@@ConsecutiveLocationNlineNoWITHquotes
		    	 /*   /// SPLITTING using @@@@ will get BELOW...
		    	  c2:0 $url c2:1 $body__NN c2:2 $body c2:3 person_CSVMap c2:4 organiz_CSVMap c2:5 location_CSVMap c2:6 Numbers_CSVMap c2:7 Nouns c2:8 SentimentMap
 				  c2:9 Verb c2:10 eachSentencSentimenCSV c2:11 ConsecutivePerson c2:12 ConsecutiveOrganization c2:13 ConsecutiveLocation c2:14 ConsecutivePersonNlineNo
 				  c2:15 ConsecutiveOrganizationNlineNo c2:16 ConsecutiveLocationNlineNo c2:17 ConsecutivePersonNlineNoWITHquotes c2:18 ConsecutiveOrganizationNlineNoWITHquotes
 				  c2:19 ConsecutiveLocationNlineNoWITHquotes
		    	 */
		    	
		    	//File 1 --- each line is a document. 
		  	  TreeMap<Integer, String>  map_eachLine_inputFile_1=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile_1, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
		  	  TreeMap<Integer, Integer> map_docID_occuranceOFquotes=new TreeMap<Integer, Integer>();
		  	  // File 1 ---  each Line (docID ==lineNo) --- calcualted number of times " occuring in each line (each document)
		  	  for(int seq:map_eachLine_inputFile_1.keySet() ){
		  		  if(seq==1) continue; //header
		  		  String eachLine=map_eachLine_inputFile_1.get(seq);
		  		  int occurance = eachLine.split("\"").length-1;
		  		  map_docID_occuranceOFquotes.put(seq, occurance);
		  	  }
		  	  
		  	  int uniqSeq=0;
		  	  //organization
		  	  TreeMap<Integer, TreeMap<Integer,String>> map_DocID_uniqSeqNorganizationCONTEXT=new TreeMap<Integer, TreeMap<Integer,String>>();
			  TreeMap<Integer, TreeMap<Integer,String>> map_DocID_uniqSeqNlineNoWITHINdocument_Organizat=new TreeMap<Integer, TreeMap<Integer,String>>();
			  //person
			  TreeMap<Integer, TreeMap<Integer,String>> map_DocID_uniqSeqNpersonCONTEXT=new TreeMap<Integer, TreeMap<Integer,String>>();
			  TreeMap<Integer, TreeMap<Integer,String>> map_DocID_uniqSeqNlineNoWITHINdocument_Person=new TreeMap<Integer, TreeMap<Integer,String>>();
			  //location
			  TreeMap<Integer, TreeMap<Integer,String>> map_DocID_uniqSeqNlocationCONTEXT=new TreeMap<Integer, TreeMap<Integer,String>>();
			  TreeMap<Integer, TreeMap<Integer,String>> map_DocID_uniqSeqNlineNoWITHINdocument_Location=new TreeMap<Integer, TreeMap<Integer,String>>();
			  
		  	   // primary key from file 1
		  	  TreeMap<Integer, String> map_inFile1_docID_URL=new TreeMap<Integer, String>();
		  	  TreeMap<String, Integer> map_inFile1_URL_docID=new TreeMap<String, Integer>();
		  	  
		  	  TreeMap<Integer, TreeMap<Integer,String>> map_DocID_lineNoNSentiment=new TreeMap<Integer, TreeMap<Integer,String>>();
		  	  int count_subsequent_catch_organiz=0;int count_subsequent_catch_person=0;int count_subsequent_catch_location=0;
		  	
		  	  // docID == lineNo
		  	  // File 1 ---  each Line --- get URL and docID ( this has to be matched with File 2 above to see if matching (verification)
		  	  for(int seq:map_eachLine_inputFile_1.keySet() ){
		  		  int curr_docId=seq;
		  		  //
		  		  if(is_header_present && seq==1) continue;
		  		  
		  		  String eachLine=map_eachLine_inputFile_1.get(seq).replace("!#!#", "@@@@"); //normalize delimiter
		  		  String[] arr_features=eachLine.split("@@@@");
		  		  if(arr_features.length>=19){
			  		  String curr_url=arr_features[0];  
			  		  String curr_sentimentsOFallLINESinCURRdoc_CSV=arr_features[10];
			  		  
			  		   String [] arr_curr_sentimentsOFallLINESinCURRdoc_CSV=curr_sentimentsOFallLINESinCURRdoc_CSV.split(",");
			  		  //BEGIN SENTIMENT
			  		  int c3=0;
			  		  while(c3<arr_curr_sentimentsOFallLINESinCURRdoc_CSV.length){
			  			   
			  			  if(arr_curr_sentimentsOFallLINESinCURRdoc_CSV[c3] ==null)
			  				  	{c3++; continue;}
			  			  
			  			 //store the doc-wise 
			  			  if(map_DocID_lineNoNSentiment.containsKey(curr_docId)){
			  				  TreeMap<Integer,String> tmp=map_DocID_lineNoNSentiment.get(curr_docId);
			  				  tmp.put(c3+1, arr_curr_sentimentsOFallLINESinCURRdoc_CSV[c3] ); // lineNo and its sentiment of current document 
			  				  map_DocID_lineNoNSentiment.put(seq, tmp);
			  			  }
			  			  else{
			  				  TreeMap<Integer,String> tmp=new TreeMap<Integer, String>();
			  				  tmp.put(1, arr_curr_sentimentsOFallLINESinCURRdoc_CSV[c3] ); //sentiment of first line in curr doc
			  				  map_DocID_lineNoNSentiment.put(seq, tmp);
			  			  }
			  			  c3++;
			  		  } // while(c3<arr_curr_sentimentsOFallLINESinCURRdoc_CSV.length){
			  		  //END SENTIMENT
			  		  
			  		 // BEGIN primary key
			  		  map_inFile1_docID_URL.put(seq, curr_url);
			  		  map_inFile1_URL_docID.put( curr_url, seq);
			  		 // END primary key
			  		  
			  		  // get - CURR feature - organization, person
			  		  String curr_OrganizationNlineNo=arr_features[index_4_organization];
			  		  String curr_ConsecutivePersonNlineNo=arr_features[index_4_person];
			  		  String curr_ConsecutiveLocationNlineNo=arr_features[index_4_location];
			  		  
			  		  //if any one of above two organizaiton/person is empty or fewer , then look for consecutive runs of NLP tagging to see optimal tagged organiz/person/location
			  		  
			  		  System.out.println(" curr organization:"+curr_OrganizationNlineNo 
			  				  			+"<-->"+ConvertPatternStringToMap.convertStringToMap(curr_OrganizationNlineNo, ", ", "="));
			  		  
			  		  //----BEGIN - iterate each of the "consecutive organization" and the line no where it is found in that document..
			  		  //consecutive organization example: {1=Arlington Police Department responded !!!1, 2=California Police Department !!!11}
			  		  String [] arr_1 =curr_OrganizationNlineNo.replace("{", "").replace("}", "").split(", ");
			  		  //consecutive person	
			  		  String [] arr_2 =curr_ConsecutivePersonNlineNo.replace("{", "").replace("}", "").split(", ");
			  		  //consecutive location
			  		  String [] arr_3 =curr_ConsecutiveLocationNlineNo.replace("{", "").replace("}", "").split(", ");
			  		  
			  		  boolean is_exists_inFilesSequence_organiz=false;boolean is_exists_inFilesSequence_person=false;boolean is_exists_inFilesSequence_location=false;
			  		  int found_fileIndex_person=-1;int found_fileIndex_organiz=-1;int found_fileIndex_location=-1;
			  		  String newFound_organizNLineNo="";String newFound_personNLineNo="";String newFound_locationNLineNo="";
			  		  //if organiz exists in any subsequent files
			  		  for(int fileIndex:map_FileSequence_urlNorganization.keySet()){
			  			  if(map_FileSequence_urlNorganization.get(fileIndex).containsKey(curr_url)){
			  				newFound_organizNLineNo=map_FileSequence_urlNorganization.get(fileIndex).get(curr_url).replace("{", "").replace("}", "");
			  				is_exists_inFilesSequence_organiz=true;
			  				found_fileIndex_organiz=fileIndex;
			  				break;
			  			  }
			  		  }
			  		  //if person exists in any subsequent files
			  		  for(int fileIndex:map_FileSequence_urlNperson.keySet()){
			  			  if(map_FileSequence_urlNperson.get(fileIndex).containsKey(curr_url)){
				  			newFound_personNLineNo=map_FileSequence_urlNperson.get(fileIndex).get(curr_url).replace("{", "").replace("}", "");
			  				is_exists_inFilesSequence_person=true;
			  				found_fileIndex_person=fileIndex;
			  				break;
			  			  }
			  		  }
			  		  //if location exists in any subsequent files
			  		  for(int fileIndex:map_FileSequence_urlNlocation.keySet()){
			  			  if(map_FileSequence_urlNlocation.get(fileIndex).containsKey(curr_url)){
				  			newFound_locationNLineNo=map_FileSequence_urlNlocation.get(fileIndex).get(curr_url).replace("{", "").replace("}", "");
			  				is_exists_inFilesSequence_location=true;
			  				found_fileIndex_location=fileIndex;
			  				break;
			  			  }
			  		  }
			  		  //can we find organi/location in subsequent runs?
			  		  String [] arr_4 =newFound_organizNLineNo.split(", ");
			  		  String [] arr_5 =newFound_personNLineNo.split(", ");
			  		  String [] arr_6 =newFound_locationNLineNo.split(", ");
			  		  
			  		  if(is_exists_inFilesSequence_organiz){
			  			  writer_debug.append("\norganiz_2: old:"+curr_OrganizationNlineNo.replace("{", "").replace("}", "")
					  							+"<-----> new:"+newFound_organizNLineNo);
			  			  writer_debug.flush();
			  		  }
			  		  if(is_exists_inFilesSequence_person){
			  			  writer_debug.append("\nperson_2: old:"+curr_ConsecutivePersonNlineNo.replace("{", "").replace("}", "")
					  							+"<-----> new:"+newFound_personNLineNo);
			  			  writer_debug.flush();
			  		  }
			  		  
			  		  // did we find new organization/person/locatin in subsequent 
			  		  if(arr_1.length<2 && arr_4.length>=2 ){
			  			  writer_debug.append("\norganiz : old:"+curr_OrganizationNlineNo.replace("{", "").replace("}", "")
			  					  				+"<-----> new:"+newFound_organizNLineNo);
			  			  writer_debug.flush();
			  			  count_subsequent_catch_organiz++;
			  		  }
			  		  if(arr_2.length<2 && arr_5.length>=2 && newFound_personNLineNo.indexOf("=")>=0){
			  			  writer_debug.append("\nperson : old:"+curr_ConsecutivePersonNlineNo.replace("{", "").replace("}", "")
			  					  				+"<-----> new:"+newFound_personNLineNo);
			  			  writer_debug.flush();
			  			  count_subsequent_catch_person++;
			  		  }
			  		  if(arr_3.length<2 && arr_6.length>=2){
			  			  writer_debug.append("\nlocation : old:"+curr_ConsecutiveLocationNlineNo.replace("{", "").replace("}", "")
			  					  				+"<-----> new:"+newFound_locationNLineNo);
			  			  writer_debug.flush();
			  			  count_subsequent_catch_location++;
			  		  }
			  		  
			  		  ////////////////////////////////////////////////////////////////////////////////////////////////
			  		  // iterate each of the "consecutive organization" and the line no where it is found in that document..
			  		  // consecutive organization 
			  		  TreeMap<Integer, TreeMap<Integer, TreeMap<Integer,String>>> mapOut_organiz=get_keyValue(
																											arr_1,
																											map_DocID_uniqSeqNorganizationCONTEXT,
																											map_DocID_uniqSeqNlineNoWITHINdocument_Organizat,
																											curr_docId);
			  		  //incremental update (organization)
			  		  map_DocID_uniqSeqNorganizationCONTEXT=mapOut_organiz.get(1);
			  		  map_DocID_uniqSeqNlineNoWITHINdocument_Organizat=mapOut_organiz.get(2);

			  		  //----END - iterate each of the "consecutive organization" and the line no where it is found in that document..
			  		  //////////////////////////////////////////////////////////////////////////////////////////////////
			  		  //----BEGIN - iterate each of the "consecutive person" and the line no where it is found in that document..
			  		  
			  		  // iterate each of the "consecutive PERSON" and the line no where it is found in that document..
			  		  //consecutive PERSON
			  		  TreeMap<Integer, TreeMap<Integer, TreeMap<Integer,String>>> mapOut_person=get_keyValue(
																										arr_2,
																										map_DocID_uniqSeqNpersonCONTEXT,
																										map_DocID_uniqSeqNlineNoWITHINdocument_Person,
																										curr_docId);
			  		  //incremental update (person)
			  		  map_DocID_uniqSeqNpersonCONTEXT=mapOut_person.get(1);
			  		  map_DocID_uniqSeqNlineNoWITHINdocument_Person=mapOut_person.get(2);
			  		  
			  		  //----END - iterate each of the "consecutive person" and the line no where it is found in that document..
			  		  //////////////////////////////////////////////////////////////////////////////////////////////////////			  		 
			  		  //----BEGIN - iterate each of the "consecutive location" and the line no where it is found in that document..
			  		  
			  		  // iterate each of the "consecutive location" and the line no where it is found in that document..
			  		  //consecutive location
			  		  TreeMap<Integer, TreeMap<Integer, TreeMap<Integer,String>>> mapOut_location=
																	  					get_keyValue(
																									arr_3,
																									map_DocID_uniqSeqNlocationCONTEXT,
																									map_DocID_uniqSeqNlineNoWITHINdocument_Location,
																									curr_docId);
						//incremental update (location)
			  		  	map_DocID_uniqSeqNlocationCONTEXT=mapOut_location.get(1);
			  			map_DocID_uniqSeqNlineNoWITHINdocument_Location=mapOut_location.get(2);
			  		  
			  		  //----END - iterate each of the "consecutive location" and the line no where it is found in that document..
			  		  
//			  		  TreeMap<Integer, TreeMap<Integer, String>> mapTemp=
//			  				  				convertPatternStringToMap.convertStringToMap(curr_OrganizationNlineNoWITHquotes, ",", "=");
//			  		 System.out.println("thiss---"+mapTemp +" for line ->"+curr_OrganizationNlineNoWITHquotes); 
		  		  } // END if(arr_features.length>=19){
		  		  
		  	  } // END for(int seq:map_eachLine_inputFile_1.keySet() ){
		  	  
		  	  //From inFile2 -- validate the URL from maitrayi's annotation to inFile_1
		  	  int matched_on_url_count_file1_file2=0;
		  	  for(int curr_docID:map_inFile1_docID_URL.keySet()){
		  		  if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.containsKey(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) )){	  
		  			matched_on_url_count_file1_file2++;
		  		  }
		  	  }
		  	  
		  	  // 1 == organization, 2 == person
		  	  int Flag=1;
		  	  int TP=0, TN=0, FP=0, FN=0;
		      int matched_on_url_count_map_DocID_uniqSeqNspecificCONTEXT_N_file2=0;
		      // organization
		  	  if(Flag==1){
			  		  // BEGIN --- for only the url in both the files, we do precision, recall <--------
			  		  // Does this has organization  ? if yes, how many are TP TN FP FN
				  	  for(int curr_docID:map_DocID_uniqSeqNorganizationCONTEXT.keySet()){
				  		  String curr_url=map_inFile1_docID_URL.get(curr_docID);
				  		  //see if the organization LineNo exists in 1 of the subsequent files
				  		  boolean is_exists_inFilesSequence_organiz=false;
				  		  // is in anyone of all the sequence of files, the organization (exists) extracted for this curr_url?
				  		  for(int fileIndex:map_FileSequence_urlNorganization.keySet()){
				  			  if(map_FileSequence_urlNorganization.get(fileIndex).containsKey(curr_url)){
				  				is_exists_inFilesSequence_organiz=true;
				  				break;
				  			  }
				  		  }
				  		//exists ?
				  		if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.containsKey(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) )
//				  			&&  is_exists_inFilesSequence_organiz==true 
				  			){
				  			matched_on_url_count_map_DocID_uniqSeqNspecificCONTEXT_N_file2++;
				  			// 
				  			if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) ).equals("1") )
				  				TP++;
				  			else if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) ).equals("0") )
				  				TN++;
				  		}
				  	  }
				  	  // this is reverse of above to get FP and FN
				  	  for(String curr_url:map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.keySet()){
				  		  if(curr_url==null) continue;
				  		  if(map_inFile1_URL_docID==null)
				  			  System.out.println("map_inFile1_URL_docID is null");
				  		  else 
				  			System.out.println("map_inFile1_URL_docID is NOT null -"+curr_url);
				  		  if(curr_url!=null && map_inFile1_URL_docID.containsKey(curr_url)){
					  		  int curr_docID= map_inFile1_URL_docID.get(curr_url);
					  		  //see if the organizaitnoLineNo exists in 1 of the subsequent files
					  		  boolean is_exists_inFilesSequence_organiz=false;
					  		  for(int fileIndex:map_FileSequence_urlNorganization.keySet()){
					  			  if(map_FileSequence_urlNorganization.get(fileIndex).containsKey(curr_url)){
					  				is_exists_inFilesSequence_organiz=true;
					  				break;
					  			  }
					  		  }
			//		  			if(curr_docID==null) continue;
					  		  //does not have a organization entry for this document?
					  		  	if(!map_DocID_uniqSeqNorganizationCONTEXT.containsKey(curr_docID)){
							  			if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(curr_url).equals("1")
							  				&& is_exists_inFilesSequence_organiz==false){
							  				FP++;
							  			}
							  			else if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(curr_url ).equals("0")
							  					&& is_exists_inFilesSequence_organiz==false)
							  				FN++;
								  			writer_debug.append("\n NOT FOUND(organiz) for url:"+curr_url
								  									+"-----"+ map_eachLine_inputFile_1.get( map_inFile1_URL_docID.get(curr_url))
								  									);
								  			writer_debug.flush();
							  			//one time writing...when flag "is_writeOUTPUT_mismatchedURLnBODYTEXT_and_NLPthem" true
							  			if(is_writeOUTPUT_mismatchedURLnBODYTEXT_and_NLPthem){
								  			// write full or part bodyText for those empty bodyTEXT found in file 1 
								  			if(map_url_partText.get(curr_url).length()> map_url_fullText.get(curr_url).length()){
								  				System.out.println("1:"+curr_url);
									  			//
									  			writer_out.append(curr_url+"!!!"+map_url_partText.get(curr_url)+"\n"); //url with emtpy bodytext in file 1 but has bodytext in file 2
									  			writer_out.flush();
								  			}
								  			else {
								  				//
								  				writer_out.append(curr_url+"!!!"+map_url_fullText.get(curr_url)+"\n"); //url with emtpy bodytext in file 1 but has bodytext in file 2
									  			writer_out.flush();
								  			}
							  			}
					  			}
				  		  } // if(curr_url!=null && map_inFile1_URL_docID.containsKey(curr_url)){
				  		} // for(String curr_url:map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.keySet()){
		  	   } //if(Flag==1){
		  	    // person 
			  	if(Flag==2){
						  	  for(int curr_docID:map_DocID_uniqSeqNpersonCONTEXT.keySet()){
						  		  //
						  		if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.containsKey(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) )){
						  			matched_on_url_count_map_DocID_uniqSeqNspecificCONTEXT_N_file2++;
						  			// 
						  			if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) ).equals("1") )
						  				TP++;
						  			else if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) ).equals("0") )
						  				TN++;
						  		}
						  	  }
						  	  // this is reverse of above to get FP and FN
						  	  for(String curr_url:map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.keySet()){
						  		  if(curr_url==null) continue;
						  		  if(map_inFile1_URL_docID==null)
						  			  System.out.println("map_inFile1_URL_docID is null");
						  		  else 
						  			System.out.println("map_inFile1_URL_docID is NOT null -"+curr_url);
						  		  if(curr_url!=null && map_inFile1_URL_docID.containsKey(curr_url)){
							  		  int curr_docID= map_inFile1_URL_docID.get(curr_url);
							  		  // if exists in sequence of subsequent files..
					//		  			if(curr_docID==null) continue;
							  		  //does not have a person entry for this document?
							  		  	if(!map_DocID_uniqSeqNpersonCONTEXT.containsKey(curr_docID)){
									  			if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(curr_url).equals("1") ){
									  				FP++;
									  			}
									  			else if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(curr_url ).equals("0") )
									  				FN++;
							  			}
						  		  }
						  		} //for(String curr_url:map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.keySet()){
			  	}
		  	    // organization 
			  	if(Flag==3){
						  	  for(int curr_docID:map_DocID_uniqSeqNorganizationCONTEXT.keySet()){
						  		  //
						  		if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.containsKey(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) )){
						  			matched_on_url_count_map_DocID_uniqSeqNspecificCONTEXT_N_file2++;
						  			// 
						  			if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) ).equals("1") )
						  				TP++;
						  			else if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) ).equals("0") )
						  				TN++;
						  		}
						  	  }
						  	  // this is reverse of above to get FP and FN
						  	  for(String curr_url:map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.keySet()){
						  		  if(curr_url==null) continue;
						  		  if(map_inFile1_URL_docID==null)
						  			  System.out.println("map_inFile1_URL_docID is null");
						  		  else 
						  			System.out.println("map_inFile1_URL_docID is NOT null -"+curr_url);
						  		  if(curr_url!=null && map_inFile1_URL_docID.containsKey(curr_url)){
							  		  int curr_docID= map_inFile1_URL_docID.get(curr_url);
							  		  // if exists in sequence of subsequent files..
					//		  			if(curr_docID==null) continue;
							  		  //does not have a person entry for this document?
							  		  	if(!map_DocID_uniqSeqNorganizationCONTEXT.containsKey(curr_docID)){
									  			if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(curr_url).equals("1") ){
									  				FP++;
									  			}
									  			else if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(curr_url ).equals("0") )
									  				FN++;
							  			}
						  		  }
						  		} //for(String curr_url:map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.keySet()){
			  	}
		  	    // location 
			  	if(Flag==4){
						  	  for(int curr_docID:map_DocID_uniqSeqNlocationCONTEXT.keySet()){
						  		  //
						  		if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.containsKey(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) )){
						  			matched_on_url_count_map_DocID_uniqSeqNspecificCONTEXT_N_file2++;
						  			// 
						  			if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) ).equals("1") )
						  				TP++;
						  			else if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(String.valueOf(map_inFile1_docID_URL.get(curr_docID)) ).equals("0") )
						  				TN++;
						  		}
						  	  }
						  	  // this is reverse of above to get FP and FN
						  	  for(String curr_url:map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.keySet()){
						  		  if(curr_url==null) continue;
						  		  if(map_inFile1_URL_docID==null)
						  			  System.out.println("map_inFile1_URL_docID is null");
						  		  else 
						  			System.out.println("map_inFile1_URL_docID is NOT null -"+curr_url);
						  		  if(curr_url!=null && map_inFile1_URL_docID.containsKey(curr_url)){
							  		  int curr_docID= map_inFile1_URL_docID.get(curr_url);
							  		  // if exists in sequence of subsequent files..
					//		  			if(curr_docID==null) continue;
							  		  //does not have a person entry for this document?
							  		  	if(!map_DocID_uniqSeqNlocationCONTEXT.containsKey(curr_docID)){
									  			if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(curr_url).equals("1") ){
									  				FP++;
									  			}
									  			else if(map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.get(curr_url ).equals("0") )
									  				FN++;
							  			}
						  		  }
						  		} //for(String curr_url:map_URL_N_LABEL_from_humanANNOTATOR_inFile_2.keySet()){
			  	}

			  	
		  	    // END --- for only the url in both the files, we do precision, recall <--------
			  	
			  	//DEBUG
			  	for(int fileIndex:map_FileSequence_urlNorganization.keySet()){
			  		writer_debug.append("\n fileindex(organz):"+fileIndex+" "+map_FileSequence_urlNorganization.get(fileIndex).size());
			  		writer_debug.flush();
			  	}
			  	for(int fileIndex:map_FileSequence_urlNperson.keySet()){
			  		writer_debug.append("\n fileindex(per):"+fileIndex+" "+map_FileSequence_urlNperson.get(fileIndex).size());
			  		writer_debug.flush();
			  	}
			  	for(int fileIndex:map_FileSequence_urlNlocation.keySet()){
			  		writer_debug.append("\n fileindex(loc):"+fileIndex+" "+map_FileSequence_urlNlocation.get(fileIndex).size());
			  		writer_debug.flush();
			  	}
			  	
		  	    int total = TP+TN+FP+FN;
		  	    double precision = TP / (double) (TP+FP); double recall = TP/(double)(TP+FN); 
		  	  
				System.out.println("map_DocID_SeqNspecificCONTEXT:"+map_DocID_uniqSeqNorganizationCONTEXT);
				System.out.println("map_DocID_SeqNspecificCONTEXT.size:"+map_DocID_uniqSeqNorganizationCONTEXT.size()
									+" map_eachLine_inputFile_1.size:"+map_eachLine_inputFile_1.size());
				System.out.println("map_DocID_uniqSeqNlineNoWITHINdocument:"+map_DocID_uniqSeqNlineNoWITHINdocument_Organizat);
				System.out.println("map_DocID_lineNoNSentiment:"+map_DocID_lineNoNSentiment);
				
				////////// 
				writer_debug.append("\nmap_DocID_SeqNspecificCONTEXT:"+map_DocID_uniqSeqNorganizationCONTEXT);
				writer_debug.append("\n\n\n\n\n map_DocID_uniqSeqNlineNoWITHINdocument_Organizat:"+map_DocID_uniqSeqNlineNoWITHINdocument_Organizat);
				writer_debug.append("\n\n\n\n\n\nmap_DocID_lineNoNSentiment:"+map_DocID_lineNoNSentiment);
				writer_debug.append("\n\n matched_on_url_count_file1_file2:"+matched_on_url_count_file1_file2);
				writer_debug.append("\n\n matched_on_url_count_map_DocID_uniqSeqNspecificCONTEXT_N_file2:"+matched_on_url_count_map_DocID_uniqSeqNspecificCONTEXT_N_file2);
				
				writer_debug.append("\n\n\n\n map_DocID_uniqSeqNpersonCONTEXT:"+map_DocID_uniqSeqNpersonCONTEXT);
				
				writer_debug.append("\n\n\n\n\n map_DocID_uniqSeqNlineNoWITHINdocument_Person:"+map_DocID_uniqSeqNlineNoWITHINdocument_Person);
//				writer_debug.append("\n\n\n map_URL_N_LABEL_from_humanANNOTATOR_inFile_2:"+map_URL_N_LABEL_from_humanANNOTATOR_inFile_2);
				
				writer_debug.append("\n BELOW are 2nd time TAGGED...");
				writer_debug.append("\n map_FileSequence_urlNorganization:"+map_FileSequence_urlNorganization.size()
									+" map_FileSequence_urlNperson:"+map_FileSequence_urlNperson.size()
									+" map_FileSequence_urlNlocation:"+map_FileSequence_urlNlocation.size()
									);

				writer_debug.append("\n count_subsequent_catch_organiz:"+count_subsequent_catch_organiz
										+" count_subsequent_catch_person:"+count_subsequent_catch_person
										+" count_subsequent_catch_location:"+count_subsequent_catch_location);
				
				writer_debug.append("\n map_DocID_uniqSeqNorganizationCONTEXT.size:"+map_DocID_uniqSeqNorganizationCONTEXT.size()
									+" map_DocID_uniqSeqNpersonCONTEXT.size:"+map_DocID_uniqSeqNpersonCONTEXT.size()
									+" map_eachLine_inputFile_1.size:"+map_eachLine_inputFile_1.size());
				
				writer_debug.append("\n map_seq_subsequentFileshavingNLPtagged:"+map_seq_subsequentFileshavingNLPtagged);
				
				writer_debug.append("\n\n TP:"+TP+" TN:"+TN+" FP:"+FP+" FN:"+FN+" total:"+total 
										 +" precision:" + precision + " recall:"+ recall 
										 +" accuracy:"+ (TP+TN)/ (double) total 
										 +" F1-score:"+ (2*precision*recall)/ (precision+recall));
				writer_debug.flush();
				
	  			//ONE time writing...when flag "is_writeOUTPUT_mismatchedURLnBODYTEXT_and_NLPthem" true
				if(is_writeOUTPUT_mismatchedURLnBODYTEXT_and_NLPthem) {
					
					/// NLP tagged 
					String NLPfolder="/Users/lenin/OneDrive/jar/NLP/";
					POSModel inflag_model2 = new POSModelLoader().load(new File(NLPfolder+ "en-pos-maxent.bin"));
					boolean is_overwrite_flag_with_flag_model=true;
					int token_position_to_insert_lineNumber=2;
					// readFile_pick_a_Token_doNLPtagging_writeOUT
					ReadFile_pick_a_Token_doNLPtagging_writeOUT.readFile_pick_a_Token_doNLPtagging_writeOUT(
																					baseFolder,
																					token_position_to_insert_lineNumber,
																					outFile, // INPUT 
																					outFile2,
																					"", //inFile_alreadyRan_pastTagging
																					0, //default_start_lineNo
																					NLPfolder,
																					inflag_model2,
																					is_overwrite_flag_with_flag_model, //is_overwrite_flag_with_flag_model,
																					false //isSOPdebug
																					);
					 
			    	 //read this file to exchange position of 2nd and 3rd column of file in variable "outFile2"
				  	  TreeMap<Integer, String>  map_eachLine_inFile_outFile2=
							  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
												.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																												 outFile2, 
																											 	 -1, //startline, 
																												 -1, //endline,
																												 " loading", //debug_label
																												 false //isPrintSOP
																								);
				  	  
				  	  //header
				  	  writer_out3.append("url!!!NLPtagged!!!bodyTEXT\n");
				  	  //exchange position of 2nd and 3rd column of file in variable "outFile2" and write to new file
				  	  for(int seq:map_eachLine_inFile_outFile2.keySet()){
				  		  String [] arr_features_outFile2=map_eachLine_inFile_outFile2.get(seq).split("!!!");
				  		  //
				  		  writer_out3.append(arr_features_outFile2[0]+"!!!"+arr_features_outFile2[2]+"!!!"+arr_features_outFile2[1] +"\n");
				  		  writer_out3.flush();
				  	  }
				  	  
			    	    // now extract organization, person, location 
						String inFolder_semantic_REPOSITORY="/Users/lenin/Downloads/#repository/p18.sentiment/";
						TreeMap<String,String> map_Word_StemmedWord=new TreeMap<String,String>();
						int Flag_type=2; //<---- consective organization/person.location..
			    	    // now extract organization, person, location
				    	Wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati.
				    	graphOUTfile_4_tag_for_person_organiz_locati( baseFolder,
				    												  outFile3,
								    								  "!!!", //delimiter for outFile3 
								    								  3, //TOKEN without NLP tagging
								    								  2, //TOKEN with NLP tagging
								    								  true, // is_having_header
																	  map_Word_StemmedWord,
																	  inFolder_semantic_REPOSITORY, //REPOSITORY
																	  "p18", //prefix_for_problem
																	  Flag_type // <----------------Flag_type=2
								    								 );
				  	  
				}
				else{

				}
				//
				System.out.println("(1) There are empty bodyTEXT (empty organization) in inFile1, so to overcome that "
									+ "\n this file created ->"+baseFolder+"out_url_with_emptyBODY_inFile1_butHASBODYinFile2.txt"
									+ "\n so that resepctive bodyText is taken from inFile 2 (matched with URL) is matched and saved in this file..");
				System.out.println("(2) Above mentioned file is again rearranged to get <url!!!NLPtaggedText!!!bodyText> in File "
									+" \n "+outFile3);
				System.out.println("(3) In this method we already ran --graphOUTfile_4_tag_for_person_organiz_locati()-- for organization");
				
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		
		// read File -- readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber
		public static void readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber(
																					String  baseFolder,
																					String  first_File,
																					String  delimiter_for_first_File_2_split,
																					String  delimiter_to_be_added_to_first_File,
																					int	    token_interested_in_first_File,
																					int	    token_with_taggedNLP_in_first_File,
																					int	    token_for_EXTERNAL_lineNO_in_first_File,
																					boolean is_given_token_interested_a_mapString ,
																					boolean is_YES_only_to_WORDNET_words,
																					boolean is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words,
																					String 	inSynsetType,
																					boolean is_YES_skip_first_or_second_char_NUMERIC_tokens,
																					boolean is_YES_INTRESTED_only_NUMERIC_tokens,
																					String  delimiter_to_be_used_to_append_in_OUTfile,
																					String  out_File,
																					int		run_for_top_N_lines,
																					boolean is_append_out_File_,
																					boolean is_header_present,
																					String  curr_feature_name
																					){
			Stemmer stemmer=new Stemmer();
			TreeMap<Integer, String> mapFirstFileLines=new TreeMap<Integer, String>();
			String secondFile_CompleteString="";
			int existsCount=0; int notExistsCount=0;
			Runtime rs =  Runtime.getRuntime();
			String conc_numeric_="";
			int cnt=0;
			File file=new File( out_File);
			String out_fileName=file.getName().substring( file.getName().length()-100, file.getName().length()  );
			try {
				FileWriter writer_ExistsFile=new FileWriter(file, is_append_out_File_);
				FileWriter writer_ExistsFile_NODUP=new FileWriter(new File(out_File+"_NODUP.txt"), is_append_out_File_);
				FileWriter writer_ExistsFile_stemmed=new FileWriter(new File(out_File+"STEMMED.txt"), is_append_out_File_);
				FileWriter writer_ExistsFile_stemmed_NODUP=new FileWriter(new File(out_File+"STEMMED_NODUP.txt"), is_append_out_File_);
				FileWriter writer_lineNo_distinctValues=new FileWriter(new File(out_File+"_LINENO_DISTINCTVALUES.txt"), is_append_out_File_);
				FileWriter writer_lineNo_NumberCSV=new FileWriter(new File(out_File+"_LINENO_NumberCSV_RelatedWordCSV.txt"), is_append_out_File_);
				FileWriter writer_Number_LineNoCSV=new FileWriter(new File(out_File+"_NumberToken_LINENOCSV.txt"), is_append_out_File_);
				
				//make a debug folder
				if(new File(baseFolder+"debug").exists() ){
					if(!new File(baseFolder+"debug").isDirectory()){
						new File(baseFolder+"debug").mkdir();
					}
				}
				// 
				mapFirstFileLines=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.						
						readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																												  first_File
																												, -1
																												, -1
																												, "loading" //debug label
																												, false //boolean isSOPprint
																												);
				
				int firstFile_count=mapFirstFileLines.size();
				 
				String s_orig="";
				int lineNumber=0;
				int physical_lineNumber=0;
				TreeMap<String, String> map_word_CSVlinesHavingTHISword=new TreeMap<String, String>();
				TreeMap<String, String> map_STEMMEDword_CSVlinesHavingTHISSTEMMEDword=new TreeMap<String, String>();
				
				TreeMap<String, String> map_STEMMEDword_wordVARIANTS=new TreeMap<String, String>();
				TreeMap<String, String> map_already_STEMMEDword_wordVARIANTS=new TreeMap<String, String>();
				
				
				TreeMap<String, String> map_isalready_wordLineNo=new TreeMap<String, String>();
				TreeMap<String, String> map_isalready_wordLineNo_NODUP=new TreeMap<String, String>();
				TreeMap<String, String> map_isalready_wordLineNo_stem=new TreeMap<String, String>();
				TreeMap<String, String> map_isalready_wordLineNo_stem_NODUP=new TreeMap<String, String>();
				
				TreeMap<String, String> map_word_CSVlinesHavingTHISwordNODUPLICATE=new TreeMap<String, String>();
				TreeMap<String, String> map_STEMMEDword_CSVlinesHavingTHISSTEMMEDwordNODUPLICATE=new TreeMap<String, String>();
				
				TreeMap<Integer, String> map_lineNo_concDistinctValues=new TreeMap<Integer, String>();
				
				TreeMap<Integer, String> map_lineNo_numbersCSV=new TreeMap<Integer, String>();
				
				String currLine_taggedNLP="";
				//System.out.println("mapFirstFileLines:"+mapFirstFileLines);
				int lineNo_of_the_file=0;
				//
				for(int counter:mapFirstFileLines.keySet()){
					lineNo_of_the_file++;
					System.out.println("curr_feature_name:"+curr_feature_name+"--ent.lineNumber:"+counter) ; //+" out file:"+" file:"+out_fileName);
					String s=mapFirstFileLines.get(counter);
					//replace empty feature with "d"
					s=s.replace(delimiter_for_first_File_2_split+delimiter_for_first_File_2_split, delimiter_for_first_File_2_split+"d"+delimiter_for_first_File_2_split);
					
					s_orig=s;
//					lineNumber++;
					// HEADER
					if(counter==1)
						continue;
					
					physical_lineNumber++;
					
					//BEGIN  -- BELOW 3 LINES added so that I get lineNumber in both if and else condition
					//HERE use taggedNLP 
					s=s.replace("&amp", " " ).replace("&lsquo", " ").replace("”", " ").replace("&lt"," ").replace("&gt"," ").replace("&quot"," ")
							.replace("  ", " ");
					
					//.replace("—", " ").replace("\""," ").replace("'", " ").replace("“", " ").replace("‘", " ").replace("–", " ").replace(":", " ").replace(";", " ").replace("—", " ").replace("{", " ").replace("}", " ").replace("?", " ").replace(")"," ").replace("("," ")
					//
					String []s2=s.split(delimiter_for_first_File_2_split);
					String []s2_using_anotherDELIMITER=s.split("!!!");
					// (NO MORE EXTERNAL LINE NUMBER)EXTERNAL line number from first file (first column)
//					lineNumber=Integer.valueOf(s2_using_anotherDELIMITER[token_for_EXTERNAL_lineNO_in_first_File-1]);
					
					
					lineNumber=lineNo_of_the_file;
					
					//END  -- BELOW 3 LINES added so that I get lineNumber in both if and else condition
					
					//IF only NUMERIC interested
					if(is_YES_INTRESTED_only_NUMERIC_tokens){
						conc_numeric_="";
						
						//HERE use taggedNLP 
						s=s.replace("&amp", " " ).replace("&lsquo", " ").replace("”", " ").replace("&lt"," ").replace("&gt"," ").replace("&quot"," ")
								.replace("  ", " ");
						
						//.replace("—", " ").replace("\""," ").replace("'", " ").replace("“", " ").replace("‘", " ").replace("–", " ").replace(":", " ").replace(";", " ").replace("—", " ").replace("{", " ").replace("}", " ").replace("?", " ").replace(")"," ").replace("("," ")
						//
						s2=s.split(delimiter_for_first_File_2_split);
 
						
						//debug
						//if(lineNumber<6700) continue;
						
						System.out.println("lineNumber:" + lineNumber+" file:"+out_fileName); //+" s:"+s);
						
						//taggedNLP
						currLine_taggedNLP=s2_using_anotherDELIMITER[token_with_taggedNLP_in_first_File-1];
						
						if(lineNumber%25==0)
							System.out.println("tagged:"+currLine_taggedNLP);
						
						//NUMERIC only
						if(is_YES_INTRESTED_only_NUMERIC_tokens){
							conc_numeric_=
										Find_specific_tags_in_given_taggedNLP_string.find_specific_tags_in_given_taggedNLP_string(currLine_taggedNLP, 
																																  "__C", // 
																																  true, //is_remove_tag
																																  true, //remove stop words
																																  true // apply numeric filter
																																  );
							
//							System.out.println( "-->"+lineNumber+" token_for_taggedNLP_in_first_File:"+token_for_taggedNLP_in_first_File
//												+"--"+conc_numeric_ +" l:"+currLine_taggedNLP);
							//
							map_lineNo_numbersCSV.put( lineNumber, conc_numeric_ );
							
						}
					}
					//
					else{
					
					//
					if(token_interested_in_first_File>0){
						// 
						s2 = s.split(delimiter_for_first_File_2_split);
						
						if(s2.length>=token_interested_in_first_File){
							System.out.println("if.lineNumber:"+lineNumber+"--token:"+s2[token_interested_in_first_File-1]);
						}
						else{
							if(s2.length%50==0)
								System.out.println("else.lineNumber:"+lineNumber+" NO ENOUGH TOKENS "+" s2.length:"+s2.length+" token_interested_in_first_File:"+token_interested_in_first_File+" s:"+s);
							else
								System.out.println("else.lineNumber:"+lineNumber+" NO ENOUGH TOKENS "+" s2.length:"+s2.length+" token_interested_in_first_File:"+token_interested_in_first_File);
						}
						
					}else{
						System.out.println("else2.lineNumber:"+lineNumber+"-- (token_interested_in_first_File<=0) token:"+s);	
					}
					//
					if(is_header_present==true && physical_lineNumber==1){
						System.out.println("skipping heder lineNumber:"+lineNumber+"--"+s);
						continue;
					}
					
					//break 
					if(run_for_top_N_lines>0 && physical_lineNumber>run_for_top_N_lines)
						break;
					
					cnt++;
					
					// LOWERCASE
					s=s.toLowerCase();
 					
 
						s=s.replace("\""," ").replace(","," ").replace(".", " ").replace("'", " ").replace("“", " ").replace("‘", " ").replace("–", " ").replace(":", " ")
								.replace(";", " ").replace("—", " ").replace("{", " ").replace("}", " ").replace("?", " ").replace(")"," ").replace("("," ")
								.replace("&amp", " " ).replace("&lsquo", " ").replace("”", " ")
								.replace("&lt"," ").replace("&gt"," ").replace("&quot"," ").replace("—", " ")
								.replace("  ", " ");
					 
 					// TOKEN splitting
					if(token_interested_in_first_File>0){
						s2=s.split(delimiter_for_first_File_2_split);
						
						if(s2.length<token_interested_in_first_File){
							//number desired no tokens
						}
						else{
							// pick token of interest 
							s=s2[token_interested_in_first_File-1];
							
							// BELOW not for NUMERIC only. (meaning NO map String)
							// is given token a mapString (for NUMERIC ,it will NEVER be map)
							if(is_given_token_interested_a_mapString){
									// convert map to VALUE string
									s=Convert_mapString_Values_into_String.convert_mapString_Values_into_String( s , 
																												 true //is_remove_Duplicates_on_mapValues
																												);
									s=s.replace("!!!", " " );
							}
							
							
							
						}
					//	System.out.println("picked token "+s);
					}
					
//					else{
//						s_orig=s;
//					}
					
					//split the token with blank space
					String [] s_words=s.split(" ");
					int c20=0;
					String currLine_numbers="";
					//each word
					while(c20<s_words.length){
						//  <-OBSELETE -> ONLY NUMERIC tokens interested. 
						// (1) FILTER NUMERIC related
						if(is_YES_INTRESTED_only_NUMERIC_tokens){
							// extract token _C from taggedNLP text 
//							try{
//								//single character and NUMBER
//								if(s_words[c20].length()==1 &&  isNumeric.isNumeric(s_words[c20])){
//									//pass
//								}
//								else{
//									//if NOT a NUMBER , remove , , otherwise keep ,
//							
//									// ??? NUMERIC
//									char a= s_words[c20].charAt(0);
//									char b= s_words[c20].charAt(1);
//									
//									boolean b_=false;
//									boolean a_=isNumeric.isNumeric(String.valueOf(a));
//									
//									try{
//										b_=isNumeric.isNumeric(String.valueOf(b));
//									}
//									catch(Exception e){
//										b_=false;
//									}
//									// 
//									if(a_==true || b_==true){
//										//pass
//										
//										if(currLine_numbers.length()==0){
//											currLine_numbers=s_words[c20];
//											map_lineNo_concDistinctValues.put(lineNumber,  currLine_numbers);
//										}
//										else{
//											currLine_numbers=currLine_numbers+"@@@"+s_words[c20]; //delimiter
//											map_lineNo_concDistinctValues.put(lineNumber,  currLine_numbers);
//										}
//										
//									}
//									else if(a_==false && b_==false ){ //IF both NOT numeric
//										c20++;
//										continue;
//									}
//								}
//								
//							}
//							catch(Exception e){		
//							}
							
						}
						else{ 	// is skip if it has numeric
						
								// is skip if it has numeric
								if(is_YES_skip_first_or_second_char_NUMERIC_tokens){
										
										try{
											//single character and NUMBER
											if(s_words[c20].length()==1 &&  IsNumeric.isNumeric(s_words[c20])){
												c20++;
												continue;
											}
											
											char a= s_words[c20].charAt(0);
											char b= s_words[c20].charAt(1);
											boolean b_=false;
											boolean a_=IsNumeric.isNumeric(String.valueOf(a));
											
											try{
												b_=IsNumeric.isNumeric(String.valueOf(b));
											}
											catch(Exception e){
												b_=false;
											}
											// if one is NUMERIC then skip
											if(a_==true || b_==true){
												c20++;
												continue;
											}
											else if(a_==false && b_==false ){ //IF both NOT numeric
											}
											
										}
										catch(Exception e){
											
										}
								}
						}
						
						// (2) FILTER WORDNET related
						// CONSIDER only NOT AVAILABLE in WORDNET words
						if(is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words){
							//////
							String wordnetout=Wordnet.wordnet_extension(s_words[c20], 
													  					1, //print_top_N_definition,
													  					false, //is_remove_stop_words
													  					inSynsetType
													 				   );
							//FOUND IN wordnet, then SKIP
							if(wordnetout.length()>0) {c20++; 
														continue;}
							
							
						}
						//WORDNET word only
						else if(is_YES_only_to_WORDNET_words){
							//wordnet 
							String wordnetout=Wordnet.wordnet_extension(s_words[c20], 
													  					1, //print_top_N_definition,
													  					false, //is_remove_stop_words
													  					inSynsetType
													 				   );
							//NOT FOUND IN wordnet, SKIP
							if(wordnetout.length()==0) {c20++; 
														continue;}
							
						}
						//CLEANING
						s_words[c20]=s_words[c20].replace("!#!#", "").replace("1=", "").replace("2=", "").replace("3=", "").replace("4=", "").replace("5=", "")
									.replace("6=", "").replace("7=", "").replace("8=", "").replace("9=", "")
									.replace("@@@@d","").replace("@@@@","").replace("[", "").replace("]", "").replace("`", "");
						
						if(s_words[c20].indexOf("__")>=0)
							s_words[c20]=s_words[c20].substring(0, s_words[c20].indexOf("__"));
						
						// DUPLICATE
						if(!map_word_CSVlinesHavingTHISword.containsKey( s_words[c20])){
							map_word_CSVlinesHavingTHISword.put(s_words[c20], String.valueOf(lineNumber) );
							//already
							map_isalready_wordLineNo.put( s_words[c20]+":"+lineNumber,"");
						}
						else {
								// HAS DUPLICATE LINE NUMBER
								String t=map_word_CSVlinesHavingTHISword.get(s_words[c20]) +","+lineNumber;
								map_word_CSVlinesHavingTHISword.put(s_words[c20], t);
							//already
							map_isalready_wordLineNo.put( s_words[c20]+":"+lineNumber,"");							
						}
						
						// NO DUPLICATES (for this current word)
						if(!map_word_CSVlinesHavingTHISwordNODUPLICATE.containsKey(s_words[c20])){
//							System.err.println("not dupe:"+s_words[c20]);
							map_word_CSVlinesHavingTHISwordNODUPLICATE.put(s_words[c20], String.valueOf(lineNumber) );
							map_isalready_wordLineNo_NODUP.put( s_words[c20]+":"+lineNumber,"");
						}
						else{
							// NO DUPLICATES
							if(!map_isalready_wordLineNo_NODUP.containsKey(s_words[c20]+":"+lineNumber)){
								String t=map_word_CSVlinesHavingTHISwordNODUPLICATE.get(s_words[c20]) +","+lineNumber;
								map_word_CSVlinesHavingTHISwordNODUPLICATE.put(s_words[c20], t);	
							}
							map_isalready_wordLineNo_NODUP.put( s_words[c20]+":"+lineNumber,"");
						}
						
						String stem_word=stemmer.stem( s_words[c20] );
						
						//not already exists
						if(!map_already_STEMMEDword_wordVARIANTS.containsKey(s_words[c20]+":"+ stem_word )){
							// 
							if(  !map_STEMMEDword_wordVARIANTS.containsKey( stem_word)){
								map_STEMMEDword_wordVARIANTS.put(stem_word, s_words[c20]);
							}
							else{
								String t=map_STEMMEDword_wordVARIANTS.get(stem_word)+","+s_words[c20];
								map_STEMMEDword_wordVARIANTS.put(stem_word, t);
							}
						}
						//already
						map_already_STEMMEDword_wordVARIANTS.put(s_words[c20]+":"+ stem_word , "");
						
						//STEM (DUPLICATE LINE NUMBER)
						if(!map_STEMMEDword_CSVlinesHavingTHISSTEMMEDword.containsKey( stem_word )){
							//DUPLICATE
							map_STEMMEDword_CSVlinesHavingTHISSTEMMEDword.put(stem_word, String.valueOf(lineNumber) );
							
						}
						else {
							//DUPLICATE
							String t=map_STEMMEDword_CSVlinesHavingTHISSTEMMEDword.get(stem_word) +","+lineNumber;
							map_STEMMEDword_CSVlinesHavingTHISSTEMMEDword.put(stem_word, t);
							
							//already
							map_isalready_wordLineNo_stem.put( stem_word+":"+lineNumber,"");					
						}

						//STEM (NO DUPLICATES
						if(!map_STEMMEDword_CSVlinesHavingTHISSTEMMEDwordNODUPLICATE.containsKey( stem_word )){
							// NO DUPLICATE
							map_STEMMEDword_CSVlinesHavingTHISSTEMMEDwordNODUPLICATE.put(stem_word, String.valueOf(lineNumber) );
							//already
							map_isalready_wordLineNo_stem_NODUP.put( stem_word+":"+lineNumber,"");
						}
						else{
							// NO DUPLICATE
							if(!map_isalready_wordLineNo_stem_NODUP.containsKey(stem_word+":"+lineNumber)){
								String t=map_STEMMEDword_CSVlinesHavingTHISSTEMMEDwordNODUPLICATE.get(stem_word) +","+lineNumber;
								map_STEMMEDword_CSVlinesHavingTHISSTEMMEDwordNODUPLICATE.put(stem_word, t);
							}
							//already
							map_isalready_wordLineNo_stem_NODUP.put( stem_word+":"+lineNumber,"");
						}
						c20++;
					}
					 
						
					}
						existsCount++;
						
				} // for(int counter:mapFirstFileLines.keySet()){
				
				// OUT ( LineNumber CSV will have duplicate LineNumbers ) --DUPLICATE
				for(String word:map_word_CSVlinesHavingTHISword.keySet()){
					if(word.length()>0){
						writer_ExistsFile.append(word+delimiter_to_be_used_to_append_in_OUTfile+map_word_CSVlinesHavingTHISword.get(word)+"\n" );
					}
					writer_ExistsFile.flush();
				}
				// STEM OUT ( LineNumber CSV will have duplicate LineNumbers ) --DUPLICATE
				for(String stemmed_word:map_STEMMEDword_CSVlinesHavingTHISSTEMMEDword.keySet()){
					if(stemmed_word.length()>0){
						writer_ExistsFile_stemmed.append(stemmed_word+delimiter_to_be_used_to_append_in_OUTfile
														+map_STEMMEDword_CSVlinesHavingTHISSTEMMEDword.get(stemmed_word)
														+delimiter_to_be_used_to_append_in_OUTfile
														+map_STEMMEDword_wordVARIANTS.get(stemmed_word)
														+"\n" );
					}
					writer_ExistsFile_stemmed.flush();
				}
				// OUT ( LineNumber CSV will have duplicate LineNumbers ) -- NO DUPLICATE
				for(String word:map_word_CSVlinesHavingTHISwordNODUPLICATE.keySet()){
					if(word.length()>0){
						writer_ExistsFile_NODUP.append(word+delimiter_to_be_used_to_append_in_OUTfile+map_word_CSVlinesHavingTHISwordNODUPLICATE.get(word)+"\n" );
					}
					writer_ExistsFile_NODUP.flush();
				}
				// STEM - OUT ( LineNumber CSV will have duplicate LineNumbers ) --NO DUPLICATE
				for(String stemmed_word:map_STEMMEDword_CSVlinesHavingTHISSTEMMEDwordNODUPLICATE.keySet()){
					if(stemmed_word.length()>0){
						writer_ExistsFile_stemmed_NODUP.append(stemmed_word+delimiter_to_be_used_to_append_in_OUTfile
																+map_STEMMEDword_CSVlinesHavingTHISSTEMMEDwordNODUPLICATE.get(stemmed_word)
																+delimiter_to_be_used_to_append_in_OUTfile
																+map_STEMMEDword_wordVARIANTS.get(stemmed_word)
																+"\n" );
					}
					writer_ExistsFile_stemmed_NODUP.flush();
				}
				
				//LineNo!!!concDistinctValues -> example <lineNo,concDistinctNumbers>
				for(int lineNo:map_lineNo_concDistinctValues.keySet()){
					writer_lineNo_distinctValues.append(lineNo+"!!!"+map_lineNo_concDistinctValues.get(lineNo)+"\n");
					writer_lineNo_distinctValues.flush();
				}
				
				System.out.println("writing to map_lineNo_numbersCSV");
				//lineNo!!!Numbers_CSV
				for(int lineNo:map_lineNo_numbersCSV.keySet()){
					//3 tokesns
					writer_lineNo_NumberCSV.append(lineNo+"!!!"+map_lineNo_numbersCSV.get(lineNo) //related words added as another token with !!!
													+"\n");
					writer_lineNo_NumberCSV.flush();
				}
				//number only reverse map_lineNo_numbersCSV -> get <NumberToken!!!LineNumberCSV>
				TreeMap<String, String> map_NumberToken_LineNumberOccuredCSV=new TreeMap<String, String>();
				
				for(int lineNo:map_lineNo_numbersCSV.keySet()){
					System.out.println("l:"+lineNo+"--"+map_lineNo_numbersCSV.get(lineNo));
					String [] arr_currStrng=map_lineNo_numbersCSV.get(lineNo).split("!!!");
					if(arr_currStrng.length<2) continue;
					String currStrng=arr_currStrng[0]; // first is linenoCSV, secnd one is relatedwords 
					System.out.println("currStrng:"+currStrng);
					//blank
					if(currStrng.replace(" ", "").length()==0  ) {continue;}
					
					String []s=currStrng.split(" ");
					//  if < two tokens, skip??
					if(s.length<2){
						System.out.println(" leng? "+currStrng);
						continue;
					}
					//split CSVV number (space delimiter
					String [] arr_number=s[1].split(" ");
					int cnt2=0;
					//
					while(cnt2<arr_number.length){
						
						// numberToken,LineNumber
						if(!map_NumberToken_LineNumberOccuredCSV.containsKey(arr_number[cnt2])){
							map_NumberToken_LineNumberOccuredCSV.put(arr_number[cnt2] ,String.valueOf(lineNo));	
						}
						else{
							String t=map_NumberToken_LineNumberOccuredCSV.get(arr_number[cnt2])+","+lineNo;
							map_NumberToken_LineNumberOccuredCSV.put(arr_number[cnt2] , t);	
						}
						
						cnt2++;
					}
				} // for(int lineNo:map_lineNo_numbersCSV.keySet()){
				
				// writing <numbertoken!!!LineNumbeRCSV>
				for(String numberToken:map_NumberToken_LineNumberOccuredCSV.keySet()){
					writer_Number_LineNoCSV.append(  numberToken+"!!!"+map_NumberToken_LineNumberOccuredCSV.get(numberToken)+"\n");
					writer_Number_LineNoCSV.flush();	
				}
				
				System.out.println("mapFirstFileLines.size:"+mapFirstFileLines.size());
				System.out.println("secondFile_CompleteString.len:"+secondFile_CompleteString.length());
				System.out.println("Exists Count:"+existsCount);
				System.out.println("NOT Exists Count:"+notExistsCount);
				System.out.println("First FILE:"+first_File);
				
				 
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		// main
		public static void main(String[] args) {
			// 
			String 	delimiter_for_input_File_2_split="@@@@";
			int 	token_interested_in_first_File=-1;
			String 	delimiter_to_be_used_to_append_in_OUTfile="";
			// 
			String baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/";
			String inputFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb2.txt";
			String out_File =baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb"
										+ "_VERB_LINENO.txt"; //<---DON'T FORGET - CHANGE OUTPUT FILE NAME ABOVE
			
			// PreREQUISTION: For numeric, (1)  is_given_token_interested_a_mapString=false, and (2) token_for_taggedNLP_in_first_File is IMPORTANT
			// delimiter_to_be_added_to_input_File-> FOR taggedNLP text use "!#!#" , otherwise use @@@@
			
			String delimiter_to_be_added_to_input_File="";
			String delimiter_for_first_File_2_split="@@@@"; //switch between {!#!#,@@@@}
			String curr_feature_name="";
			///////////////
			//PERSON(2)->is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words=true;is_given_token_interested_a_mapString=true,is_YES_skip_first_or_second_char_NUMERIC_tokens=true
			//ORGANIZ(3)->
			//LOCATION(4)->is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words=true;is_given_token_interested_a_mapString=true,is_YES_skip_first_or_second_char_NUMERIC_tokens=true
			//NOUN(token=6)->inSynsetType=NOUN,is_YES_only_to_WORDNET_words=true;is_given_token_interested_a_mapString=false,is_YES_skip_first_or_second_char_NUMERIC_tokens=true
			//VERB(token=8)->inSynsetType=VERB,is_YES_only_to_WORDNET_words=true;is_given_token_interested_a_mapString=false,is_YES_skip_first_or_second_char_NUMERIC_tokens=true
			//DONT FORGET - CHANGE OUTPUT FILE NAME ABOVE 
			
			//NUMBERS(5)->token_for_taggedNLP_in_first_File=2;is_given_token_interested_a_mapString=false
			///////////////
			//comment/ uncomment
			//readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String
			readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber(
																				baseFolder,
																				inputFile,
																				delimiter_for_first_File_2_split, //delimiter_for_input_File_2_split,
																				delimiter_to_be_added_to_input_File, //delimiter_to_be_added_to_input_File,
																				8, //token_interested_in_first_File,//<-PERSON,ORGANIZ,LOCATION ETC CHANGE HERE
																				2, //token_for_taggedNLP_in_first_File
																				1, //token_for_EXTERNAL_lineNO_in_first_File
																				false, // is_given_token_interested_a_mapString **
																				true,  // is_YES_only_to_WORDNET_words
																				false, // is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words
																				"VERB", //**** inSynsetType={NOUN,VERB}**
																				true,  //is_YES_skip_first_or_second_char_NUMERIC_tokens
																				false, //is_YES_INTRESTED_only_NUMERIC_tokens
																				"!!!", //delimiter_to_be_used_to_append_in_OUTfile,
																				out_File,
																				100000000, //run_for_top_N_lines
																				false, //is_Append_outFile
																				true, //is_header_present
																				curr_feature_name //mainly to debug print
																		      );
			
			// SETTING variables for below method
			//p18
			//*below file created using file as input-> "OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all.txt"
			//*below file created using "Flag_type=2 in "wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati.graphOUTfile_4_tag_for_person_organiz_locati()"
			baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/";
			// p18 input
			String inputFile_1=
						baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all.txt_added_perso_origz_loc_verb2_done.txt";
			
			token_interested_in_first_File=2;
			String delimiter_for_input_File_1_split="!!!";
			//Dont change below fileName, same file Name used inside "readFile_eachWord_of_given_ConsecutiveToken_apperance_get_as_CSV_LineNumber_p18"
			//below file created NLPtagged using input from "out_url_with_emptyBODY_inFile1_butHASBODYinFile2_url_NLPtagged_bodyText.txt"
			String outFile3=baseFolder+"out_url_with_emptyBODY_inFile1_butHASBODYinFile2_url_NLPtagged_bodyText.txt_added_perso_origz_loc_verb2_done.txt";
			
			TreeMap<Integer,String> map_seq_subsequentFileshavingNLPtagged=new TreeMap<Integer, String>();
			map_seq_subsequentFileshavingNLPtagged.put(1, outFile3);
			
			//human labeled data (from maitrayi) --
			// ******This below file has bodyTEXT which for some docs may not be available in "inputFile_1" which is captured in var "outFile3" 
			// 		 when running below method with "is_writeOUTPUT_mismatchedURLnBODYTEXT_and_NLPthem==true"
			// Maitrayi labeled files; kept in FOLDER->/Users/lenin/Dropbox/#problems/p18/From Maitrayi/
			String inputFile_2_human_annotator_labeledFile="/Users/lenin/Dropbox/#problems/p18/FromMaitrayi/all.txt";
			// example  <4,2> -> <URL,LABEL>
			String tokens_4_URL_N_LABEL_4_inFile_7_human_annotator_labeledFile="4,2";  
			out_File = inputFile+ "_VERB_LINENO.txt"; //<---DON'T FORGET - CHANGE OUTPUT FILE NAME HERE
			// only one time do this, then set to false <------is_writeOUTPUT_mismatchedURLnBODYTEXT_and_NLPthem
			System.out.println("file 1:"+inputFile_1);
			System.out.println("file 2:"+inputFile_2_human_annotator_labeledFile);
			// 1) run with "is_writeOUTPUT_mismatchedURLnBODYTEXT_and_NLPthem=false" first, then change it to true.
			// 2) make sure files in "map_seq_subsequentFileshavingNLPtagged" are right files 
			//				..ie.. marked with possibly "_done.txt" added manually for latest file out from (1)
			//comment/ uncomment  ---------> "ConsecutiveToken_apperance"
			 //mostly run one on above or below method to get (below method is totally different from above one)
			//-------------------------------------or---------------------------------------------
//			readFile_eachWord_of_given_ConsecutiveToken_apperance_get_as_CSV_LineNumber_p18(
//																						baseFolder,
//																						inputFile_1, //first File 
//																						delimiter_for_input_File_1_split, //delimiter_for_first_File_2_split, 
//																						"", //delimiter_to_be_added_to_first_File,
//																						token_interested_in_first_File,
//																						inputFile_2_human_annotator_labeledFile, //second File <--annotated
//																						"4,2", //tokens_4_URL_N_LABEL_4_inFile_2_human_annotator_labeledFile
//																						tokens_4_URL_N_LABEL_4_inFile_7_human_annotator_labeledFile, //
//																						out_File,
//																						1, //run_for_top_N_lines,
//																						false, //is_append_out_File_, 
//																						true, //is_header_present (first File)
//																						curr_feature_name, //mainly to debug print
//																						false, //<--only once--is_writeOUTPUT_mismatchedURLnBODYTEXT_and_NLPthem*******
//																						map_seq_subsequentFileshavingNLPtagged
//																						);
			
//			System.out.println(clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters("108lenin", true));
			
		}

}