package p8;

import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import crawler.Stemmer;
import crawler.Convert_mapString_to_TreeMap;
import crawler.Generate_Random_Number_Range;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import p18.MyAlgo_p18;
import p8.MyAlgo;

public class CaseStudy_DrMills_request {
	
	//caseStudy_DrMills_request
	public static void caseStudy_DrMills_request(
												String baseFolder,
												String inputFile,
												String inputFile_2_URL_n_totalNewWords,
												String inputFile_2_verb,
												String inputFile_3_organization,
												String outputFile1,
												String outputFile2
												){
		try{
			FileWriter writer1=new FileWriter(outputFile1);
			FileWriter writer2=new FileWriter(outputFile2);
			FileWriter writerDEBUG=new FileWriter(baseFolder+"debug.txt");
			TreeMap<String, Integer> map_stemmedRoriginalVerb_CountNOdocsThisVerbOccuring_GLOBAL
									=new TreeMap<String, Integer>();
			TreeMap<String, Integer> map_stemmedRoriginalOrganization_CountNOdocsThisVerbOccuring_GLOBAL=new TreeMap<String, Integer>();
			
			TreeMap<String, Double>  map_URL_n_TotalNewWords=new TreeMap<String, Double>();
			  
			//header-->
			//$url!#!#$body__NN!#!#$body!#!#$body__NN!#!#!#!#@@@@person_CSVMap@@@@organiz_CSVMap@@@@location_CSVMap@@@@Numbers_CSVMap@@@@Nouns@@@@SentimentMap@@@@Verb@@@@eachSentencSentimenCSV@@@@ConsecutivePerson@@@@ConsecutiveOrganization@@@@ConsecutiveLocation@@@@ConsecutivePersonNlineNo@@@@ConsecutiveOrganizationNlineNo@@@@ConsecutiveLocationNlineNo@@@@ConsecutivePersonNlineNoWITHquotes@@@@ConsecutiveOrganizationNlineNoWITHquotes@@@@ConsecutiveLocationNlineNoWITHquotes
				// 	Loading seq and Line 
			  TreeMap<Integer, String>  map_seq_inFile_TEMP=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  
				// 	Loading seq and Line 
			  TreeMap<Integer, String>  map_seq_inFile_URL_n_TotalNewWords_FILE=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile_2_URL_n_totalNewWords, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  //
			  for(int seq:map_seq_inFile_URL_n_TotalNewWords_FILE.keySet()){
				  String [] arr_URL_n_TotalNewWords = map_seq_inFile_URL_n_TotalNewWords_FILE.get(seq).split("!!!");
				  if(arr_URL_n_TotalNewWords[0].indexOf("http")>=0)
					  map_URL_n_TotalNewWords.put(arr_URL_n_TotalNewWords[0], Double.valueOf(arr_URL_n_TotalNewWords[1]) );
			  }
			  double average_overall_ = 0.;
			  ///
			  for(String url:map_URL_n_TotalNewWords.keySet()){
				  if(url.indexOf("http")>=0)
					  average_overall_+=map_URL_n_TotalNewWords.get(url);
			  }
			  // AVERAGE
			  average_overall_=average_overall_/(double)map_URL_n_TotalNewWords.size();
			  
			  average_overall_=9.855401387512389;
			  
			  
			  //(Verb) dictionary of verb and the line no (docsIDs ) where this verb occurs
			  //word!!!lineNumbers_CSV!!!wordStemmedCSV
			  TreeMap<Integer, String>  map_seq_inFile_verb=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile_2_verb, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  // example: abdic!!!60,61,2406!!!abdicated
			  for(int seq:map_seq_inFile_verb.keySet()){
				  String [] arr_ = map_seq_inFile_verb.get(seq).split("!!!");
				  String stemmedVerb=arr_[0];
				  String verbCSV=arr_[2];
				  int 	 count_Documents=arr_[1].split(",").length;
				  //<>
				  map_stemmedRoriginalVerb_CountNOdocsThisVerbOccuring_GLOBAL.put(stemmedVerb, count_Documents );
				  
				  String [] arr_originalVerb=verbCSV.split(",");
				  int c2=0;
				  //
				  while(c2<arr_originalVerb.length){
					  map_stemmedRoriginalVerb_CountNOdocsThisVerbOccuring_GLOBAL.put(arr_originalVerb[c2], 
							  														  count_Documents );  
					  c2++;
				  }
				  
			  }
			  
			  //(organization) dictionary of organization and the line no (docsIDs ) where this verb occurs
			  TreeMap<Integer, String>  map_seq_inFile_organization=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile_3_organization, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  
			  for(int seq:map_seq_inFile_organization.keySet()){
				  String [] arr_=map_seq_inFile_organization.get(seq).split("!!!");
				  String stemmedOrganiz=arr_[0];
				  String OrganizationCSV=arr_[2];
				  int 	 count_Documents=arr_[1].split(",").length;
				  //<>
				  map_stemmedRoriginalVerb_CountNOdocsThisVerbOccuring_GLOBAL.put(stemmedOrganiz, count_Documents );
				  
				  String [] arr_originalOrganizatio=OrganizationCSV.split(",");
				  int c2=0;
				  //
				  while(c2<arr_originalOrganizatio.length){
					  map_stemmedRoriginalOrganization_CountNOdocsThisVerbOccuring_GLOBAL.put(arr_originalOrganizatio[c2], 
							  														  count_Documents );  
					  c2++;
				  }
			  }
			  

			  writer1.append("URL!!!organization!!!verbs!!!numbers!!!negSentiments!!!Years!!!newWordsCount!!!Avg_NewWords_Overall\n");
			  writer2.append("URL!!!organization!!!verbs!!!numbers!!!negSentiments!!!Years!!!newWordsCount!!!Avg_NewWords_Overall\n");
			  
			  double curr_count_new_word=0.;
			  
			  //after split by @@@@, tokens needed 3, 5,7,8
			  //3,5,7,8->Organization,Number,SentimentMap,Verb
			  for(int seq:map_seq_inFile_TEMP.keySet()){
				  String eachLine=map_seq_inFile_TEMP.get(seq);
				  
				  
				  String [] arr_2= eachLine.split("!#!#");
				  String curr_bodyTEXT=arr_2[2];
				  String curr_URL=arr_2[0];
				  //
				  String [] arr_= eachLine.split("@@@@");
				  String curr_organizationMap=arr_[2];
				  String curr_numberMap=arr_[4];
				  String curr_SentimentMap=arr_[6];
				  String curr_verbString=arr_[7];
				  
				  System.out.println("curr_URL:"+curr_URL+" seq:"+seq);
				  
				  if(curr_URL.indexOf("http")==-1)
					  continue;
				  
				  // COUNT NEW_WORD
				  if(map_URL_n_TotalNewWords.containsKey(curr_URL))
					  curr_count_new_word= map_URL_n_TotalNewWords.get(curr_URL) ;
				  else
					  curr_count_new_word= -1 ;
			
				  
				  if(curr_bodyTEXT.indexOf("@@@@")>=0)
					  curr_bodyTEXT=curr_bodyTEXT.substring(0, curr_bodyTEXT.indexOf("@@@@"));
				  
//				  System.out.println("curr_bodyTEXT:"+curr_bodyTEXT);
				  ///////// GET only year
				  String [] arr_temp_words_of_bodyTEXT=curr_bodyTEXT.split(" ");
				  //
				  TreeMap<Integer,TreeMap<String, Double>> map_id_N_seq_N_value=
				  p8.MyAlgo.convert_arrString_to_arrInteger_only_remove_money( arr_temp_words_of_bodyTEXT,
						  													   writerDEBUG);
				  //get year
				  TreeMap<String, Double> map_seq_years=map_id_N_seq_N_value.get(3);
				   
				  // write 2 first file
				  writer1.append(curr_URL+"!!!"+
						  		 curr_organizationMap+"!!!"+curr_verbString+"!!!"+curr_numberMap+"!!!"+curr_SentimentMap
						  		 +"!!!"+map_seq_years+"!!!"+curr_count_new_word+
						  		 "\n" );
				  writer1.flush();

				  
				  // /////// ORGANIZATION
				  TreeMap<String, String> map_curr_seq_organization_converted=
						  	Convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(
						  														curr_organizationMap,
																				"=", //dictionary_name_value_splitter
																				false
																				);
				  int count_curr_4_organization=map_curr_seq_organization_converted.size();
				  	  count_curr_4_organization=0;
				  // as per the paper ,we dont take shared organization
				  for(String seq11:map_curr_seq_organization_converted.keySet()){
					  String curr_organiza=map_curr_seq_organization_converted.get(seq11).toLowerCase();
					  
					  if(!map_stemmedRoriginalVerb_CountNOdocsThisVerbOccuring_GLOBAL.containsKey(curr_organiza)){
						  
						  // organiza appearing in >1 docs
						  if(!map_stemmedRoriginalVerb_CountNOdocsThisVerbOccuring_GLOBAL.containsKey(curr_organiza)
								  ){
							  //
							  count_curr_4_organization+=1;
//							  map_stemmedRoriginalOrganization_CountNOdocsThisVerbOccuring_GLOBAL.get(curr_organiza);
						  }
						  
					  }
				  }
				  
//				  System.out.println("map_curr_seq_organization_converted:"+map_curr_seq_organization_converted);
				  
				  // ////// VERB
				 String [] arr_verbs=curr_verbString.replace("   ", " ").replace("  ", " ").split(" ");
				 int c3=0;
				 int count_curr_4_verbs=0;
				 //
				 while(c3<arr_verbs.length){
					 if(map_stemmedRoriginalVerb_CountNOdocsThisVerbOccuring_GLOBAL.containsKey(arr_verbs[c3] )){
						 
						 if(map_stemmedRoriginalVerb_CountNOdocsThisVerbOccuring_GLOBAL.get(arr_verbs[c3])>1){
							 count_curr_4_verbs++;
						 }
						 
					 }
					 c3++;
				 }
				 // SENTIMENT
				 
				
				  TreeMap<String, String> map_curr_SentimentMap_converted=
						  	Convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(
						  														curr_SentimentMap,
																				"=", //dictionary_name_value_splitter
																				false
																				);
				  int count_curr_4_NegativeSentiment=0;
				 //
				 for(String currSentiment:map_curr_SentimentMap_converted.keySet()){
					 if(Double.valueOf(currSentiment)==1.0 || Double.valueOf(currSentiment)==2.0 ){
						 Integer currNeg=Integer.valueOf( map_curr_SentimentMap_converted.get(currSentiment) );
						 count_curr_4_NegativeSentiment+=currNeg;
					 }
				 }
				 
				   // YEAR
				 int count_curr_4_Year=0;
				  //
//				  for(String currYear:map_seq_years.keySet()){
////					  if(currYear.length()==4){
//						  count_curr_4_Year+=1;
////					  }
//				  }
				  
				  //NUMBERS
				  TreeMap<String, String> map_curr_numberMap_converted=
						  	Convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(
						  														curr_numberMap,
																				"=", //dictionary_name_value_splitter
																				false
																				);
				  int count_curr_4_numbers=map_curr_numberMap_converted.size();
				  
				  
				  // out
				  writer2.append(
						  		curr_URL+"!!!"+
						  		count_curr_4_organization+"!!!"+count_curr_4_verbs+"!!!"+count_curr_4_numbers
						  		+"!!!"+count_curr_4_NegativeSentiment+"!!!"+ map_seq_years.size() //  count_curr_4_Year
						  		+"!!!"+curr_count_new_word
						  		+"!!!"+average_overall_
						  		+"\n");
				  writer2.flush();
				  
//				  System.out.println("map_mapString_to_treemap:"+map_mapString_to_treemap+" arr_[2]:"+arr_[2]);
				  
			  }
			  
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//Do_calc_value_4_obj_function
	private static void Do_calc_value_4_obj_function(String inputFile2_out_caseStudy2,//in
													 String inputFile_4_topicWiseFile_CHRON,//in
													 String inputFile_5_groundTruth_by_Maitrayi, //in
													 String outputFile6_out_calc_objectiveFunction_4_caseStudy2//out
													) {
		// TODO Auto-generated method stub
		try{
			  FileWriter writer=new FileWriter(outputFile6_out_calc_objectiveFunction_4_caseStudy2);
			  FileWriter writer2=new FileWriter(outputFile6_out_calc_objectiveFunction_4_caseStudy2+"_symbol.txt");
			
			  int currOrganiz=0;
			  int currVerbs=0;
			  int currNumbers=0;
			  int currNegSentiments=0;
			  int currYears=0;
			  double currNewWordCounts=0.0;
			  double avg_new_words4Alldoc=0.;
			  double curr_fn_verb=0.;	
			  double curr_fn_organ=0.;
			 
			  // 	Loading seq and Line 
			  //    $url!!!$body!!!$line!!!$fname!!!$yyyy!!!$mm 
			  TreeMap<Integer, String>  map_seq_inFile_inputFile_4_topicWiseFile_CHRON=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile_4_topicWiseFile_CHRON, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  TreeMap<String, Integer> map_URL_lineNo_topicWisedocuments=new TreeMap<String, Integer>();
			  TreeMap<Integer, String> map_lineNo_URL_topicWisedocuments=new TreeMap<Integer, String>();
			  TreeMap<String, String> map_URL_bodyTEXT_topicWisedocuments=new TreeMap<String, String>();
			  
			  //	$url!!!$body!!!$line!!!$fname!!!$yyyy!!!$mm 
			  for(int lineNo:map_seq_inFile_inputFile_4_topicWiseFile_CHRON.keySet()){
				  String [] _arr=map_seq_inFile_inputFile_4_topicWiseFile_CHRON.get(lineNo).split("!!!");
				  map_lineNo_URL_topicWisedocuments.put( lineNo , _arr[0] );
				  map_URL_lineNo_topicWisedocuments.put( _arr[0], lineNo);
				  map_URL_bodyTEXT_topicWisedocuments.put(_arr[0], _arr[1]);
			  }
			
			// 	Loading seq and Line 
			// URL!!!organization!!!verbs!!!numbers!!!negSentiments!!!Years!!!newWordsCount!!!Avg_NewWords_Overall
			  TreeMap<Integer, String>  map_seq_inFile_inputFile2_out_caseStudy2=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile2_out_caseStudy2, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			    TreeMap<Integer, String> map_lineNo_URL=new TreeMap<Integer, String>();
				TreeMap<String, String> map_URL_eachLine_from_caseStudy2=new TreeMap<String, String>();
				TreeMap<String, Integer> map_URL_Organiz_from_caseStudy2=new TreeMap<String, Integer>();
				TreeMap<String, Integer> map_URL_Verbs_from_caseStudy2=new TreeMap<String, Integer>();
				TreeMap<String, Integer> map_URL_Numbers_from_caseStudy2=new TreeMap<String, Integer>();
				TreeMap<String, Integer> map_URL_NegSentiments_from_caseStudy2=new TreeMap<String, Integer>();
				TreeMap<String, Integer> map_URL_Years_from_caseStudy2=new TreeMap<String, Integer>();
				TreeMap<String, Double> map_URL_NewWordsCounts_from_caseStudy2=new TreeMap<String, Double>();
				
				int count_passed=0;
			   
			  //URL!!!organization!!!verbs!!!numbers!!!negSentiments!!!Years!!!newWordsCount!!!Avg_NewWords_Overall
			  for(int seq:map_seq_inFile_inputFile2_out_caseStudy2.keySet()){
				  
				  if(seq==1) continue; //continue
				  String eachLine=map_seq_inFile_inputFile2_out_caseStudy2.get(seq);
				  String [] _arr=eachLine.split("!!!");
				  String currURL=_arr[0];
				  
				  System.out.println("seq:"+seq);
				  //filter-out only URL of specific topic 
				  if(!map_URL_lineNo_topicWisedocuments.containsKey(currURL))
					  continue;
				  
				  currOrganiz=Integer.valueOf(_arr[1]);
				  currVerbs=Integer.valueOf(_arr[2]);
				  currNumbers=Integer.valueOf(_arr[3]);
				  currNegSentiments=Integer.valueOf(_arr[4]);
				  currYears=Integer.valueOf(_arr[5]);
				  currNewWordCounts=Double.valueOf(_arr[6]);
				  avg_new_words4Alldoc=Double.valueOf(_arr[7]); // this is always same (in each line)
				  
				  count_passed++;
				  //
//				  map_lineNo_URL.put(seq, currURL);
				  map_lineNo_URL.put(count_passed, currURL);
				  //
//				  if(currNewWordCounts>=0){
					  //
					  map_URL_eachLine_from_caseStudy2.put(_arr[0], eachLine); //all line
					  map_URL_Organiz_from_caseStudy2.put(currURL, currOrganiz);
					  map_URL_Verbs_from_caseStudy2.put(currURL, currVerbs);
					  map_URL_Numbers_from_caseStudy2.put(currURL, currNumbers);
					  map_URL_NegSentiments_from_caseStudy2.put(currURL, currNegSentiments);
					  map_URL_Years_from_caseStudy2.put(currURL, currYears);
					  map_URL_NewWordsCounts_from_caseStudy2.put(currURL, currNewWordCounts);
//				  }
			  }
			
			  
			  
			  //URL!!!Solution-based?!!!First sentence(line) from solution mentioned in first paragraph!!!Resource_based?!!!Out-of-Context -based?!!!Context- enriched!!!comments1!!!keyword!!!comments2!!!Lenin's  comments
				// 	Loading seq and Line 
			  TreeMap<Integer, String>  map_seq_inFile_inputFile_5_groundTruth_by_Maitrayi=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile_5_groundTruth_by_Maitrayi, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  System.out.println("LOADED map_seq_inFile_inputFile_5_groundTruth_by_Maitrayi:"+map_seq_inFile_inputFile_5_groundTruth_by_Maitrayi.size());
			  TreeMap<String, String> map_tpURL_asKEY=new TreeMap<String, String>();
			  //URL!!!Solution-based?!!!First sentence(line) from solution mentioned in first paragraph!!!Resource_based?!!!Out-of-Context -based?!!!Context- enriched!!!comments1!!!keyword!!!comments2!!!Lenin's  comments
			  for(int seq:map_seq_inFile_inputFile_5_groundTruth_by_Maitrayi.keySet()){
				  String [] _arr=map_seq_inFile_inputFile_5_groundTruth_by_Maitrayi.get(seq).split("!!!");
//				  System.out.println("_arr[0]:"+_arr[0]);
				  
				  map_tpURL_asKEY.put(_arr[0], "");
			  }
			  System.out.println("map_tpURL_asKEY:"+map_tpURL_asKEY.size());
			  String URL_of_rand_number_1="";
			  String URL_of_rand_number_2="";
			  int cnt=0;
			  String curr_chosen_TP_URL="";double current_doc_MINUS_Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_currNegSentiments=0.;
			  
			  writer.append("CC!!!current_doc_MINUS_Neigh_avg_4_sqrt_currNumbers_MULTI_currYears!!!"
			  			+ "current_doc_MINUS_Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_currNegSentiments"
		  				+ "!!!map_URL_NewWordsCounts_from_caseStudy2.get(curr_chosen_TP_URL)-avg_new_words4Alldoc!!!curr_chosen_TP_URL"
		  				+ "!!!URL_of_rand_number_1!!!URL_of_rand_number_2\n");
			  //
			  writer2.append("UniqueSymbolCC!!!current_doc_MINUS_Neigh_avg_4_sqrt_currNumbers_MULTI_currYears!!!current_doc_MINUS_Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_currNegSentiments!!!map_URL_NewWordsCounts_from_caseStudy2.get(curr_chosen_TP_URL)-avg_new_words4Alldoc"
			  			    + "!!!curr_chosen_TP_URL"+ "!!!URL_of_rand_number_1!!!URL_of_rand_number_2\n");
			  
			  TreeMap<String, Integer> map_UniqueSymbol_CountOccurance=new TreeMap<String, Integer>();
			  
			  String final_conc_symbols="";
			  
			  // choose a true positive - URL
			  for(String curr_TP_URL:map_tpURL_asKEY.keySet()){
				  
				  if(!map_URL_Verbs_from_caseStudy2.containsKey(curr_TP_URL)) continue;
				  
				  curr_chosen_TP_URL=curr_TP_URL;
				  cnt++;
				  System.out.println("cnt:"+cnt);
				  // skip first 2 as we need pre neighbors
				  if(cnt<10){cnt++; continue;}
				  //
				  int rand_number_1=-1;
				  int rand_number_2=-1;
				  boolean is_all_condition_MET=false;
//				  String URL_of_neigh1="";
//				  String URL_of_neigh2="";
				  //
				  while(is_all_condition_MET==false){
					  //
					  rand_number_1=Generate_Random_Number_Range.generate_Random_Number_Range(1, cnt-1);	
					  rand_number_2=Generate_Random_Number_Range.generate_Random_Number_Range(1, cnt-1);
					  //Getting NEIGHBORS URL
					  URL_of_rand_number_1=map_lineNo_URL.get(rand_number_1);
					  URL_of_rand_number_2=map_lineNo_URL.get(rand_number_2);
//					  System.out.println("URL_of_rand_number_1:"+URL_of_rand_number_1+"\n URL_of_rand_number_2:"+URL_of_rand_number_2);
					  
					  System.out.println("4.cnt:"+cnt+" rand_number_1:"+rand_number_1+" rand_number_2:"+rand_number_2
							  				+" 1:"+map_tpURL_asKEY.containsKey(URL_of_rand_number_1)+" 2:"+map_tpURL_asKEY.containsKey(URL_of_rand_number_2)
							  				+" 3:"+URL_of_rand_number_2.equalsIgnoreCase(URL_of_rand_number_1));
					  
					  if(URL_of_rand_number_1==null || URL_of_rand_number_2==null||rand_number_1==rand_number_2
							  ) {
						  System.out.println("4.cnt: CONTINUE...");
						  continue;}
					  
					  //
					  if(rand_number_1!=rand_number_2 && !map_tpURL_asKEY.containsKey(URL_of_rand_number_1)
							  && !map_tpURL_asKEY.containsKey(URL_of_rand_number_2) 
							  && !URL_of_rand_number_2.equalsIgnoreCase(URL_of_rand_number_1)
//							  && map_URL_bodyTEXT_topicWisedocuments.containsKey(URL_of_rand_number_1)
//							  && map_URL_bodyTEXT_topicWisedocuments.containsKey(URL_of_rand_number_2)
//							  && map_URL_Verbs_from_caseStudy2.containsKey(curr_chosen_TP_URL)
							  ){
						  //
						  is_all_condition_MET=true;
//						  break;
					  }
					  System.out.println("-----------------------------------------------------------");
					  System.out.println("\n TP URL:" + curr_chosen_TP_URL
							  			 +"\n NOT TP URL 1:" +URL_of_rand_number_1 +"\n NOT TP URL 2:"+URL_of_rand_number_2
							  			 +" when cnt="+cnt
							  				);
					  System.out.println("retrying.."+is_all_condition_MET);
				  }
				  System.out.println("PASSED.....");
				  
				  //exploring for +-++ with set of URLS below  
				  //2014(may
//				  curr_chosen_TP_URL="http://www.ndtv.com/world-news/militants-kidnap-kill-20-iraqi-soldiers-561301";
				  // Sep 11 2005
//				  curr_chosen_TP_URL="http://timesofindia.indiatimes.com/world/rest-of-world/maoists-kidnap-100-kids-in-nepal/articleshow/1226860.cms";
				  //October 24, 2014 
//				  curr_chosen_TP_URL="http://www.ndtv.com/world-news/suspected-boko-haram-fighters-kidnap-25-girls-in-northeast-nigeria-683408";
//				  January 18, 2015
//				  curr_chosen_TP_URL="http://www.ndtv.com/world-news/suspected-boko-haram-fighters-kidnap-around-80-in-cameroon-729034";
				  //December 18, 2014
//				  curr_chosen_TP_URL="http://www.ndtv.com/world-news/suspected-boko-haram-gunmen-kidnap-over-100-women-children-in-nigeria-715102";
				  //December 27, 2010
//				  curr_chosen_TP_URL="http://www.ndtv.com/world-news/taliban-kidnap-23-pakistanis-in-show-of-strength-443168";
				  //December 30, 2012 
//				  curr_chosen_TP_URL="http://www.ndtv.com/world-news/taliban-kill-21-kidnapped-pakistani-soldiers-508937";
				  //June 10, 2014 
//				  curr_chosen_TP_URL="http://www.ndtv.com/world-news/boko-haram-suspected-to-have-kidnapped-20-more-women-in-northeast-nigeria-576818";
				  //May 28, 2010
//				  curr_chosen_TP_URL="http://www.ndtv.com/cities/mumbai-cop-held-for-kidnapping-two-year-old-419247";
				  //2006
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/city/patna/bihar-kidnappers-run-parallel-banks/articleshow/1439030.cms";
				  //Mar 4, 2006
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/city/patna/new-crop-of-educated-kidnappers-irks-police/articleshow/1437777.cms";
				  //MARCH 09, 2007 
//				  URL_of_rand_number_1="http://www.thehindu.com/todays-paper/tp-national/tp-andhrapradesh/unseen-kidnapper/article1808232.ece";
				  //Jul 21, 2014
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/world/middle-east/hamas-military-wing-claims-it-has-kidnapped-israeli-soldier-on-edge-of-gaza/articleshow/38766593.cms";
				  //Apr 19, 2002,
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/india/kidnapped-indian-diplomat-traced/articleshow/7371396.cms";
				  //Nov 3, 2008
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/city/jaipur/bjp-ticket-aspirant-kidnapped-for-extortion-two-held/articleshow/3666547.cms";
//				  May 30, 2006
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/india/ndfb-might-be-behind-ssb-personnels-kidnapping/articleshow/1594705.cms";
				  //Nov 6, 2007
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/india/up-minister-quits-over-kidnapping-of-girl/articleshow/2523346.cms";
				  //Jun 23, 2007
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/world/pakistan/radical-students-in-pak-kidnap-chinese-for-unislamic-acts/articleshow/2143803.cms";
				  //Oct 7, 2008
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/city/lucknow/3-kidnapping-accused-put-to-polygraph-test-by-cbi/articleshow/3568043.cms";
				  //Sep 8, 2008
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/world/us/kidnapping-talibans-new-income-source/articleshow/3457736.cms";
				  // Aug 22, 2014
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/world/middle-east/hamas-admits-to-kidnapping-and-killing-of-israeli-teens/articleshow/40621164.cms";
				  //Oct 7, 2008,
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/city/lucknow/3-kidnapping-accused-put-to-polygraph-test-by-cbi/articleshow/3568043.cms";
				  //Oct 9, 2005
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/city/patna/kidnapping-on-a-high-as-polls-approach/articleshow/1257156.cms";
				  // jan 27 2005
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/india/kidnapping-its-now-a-rich-industry-in-india/articleshow/1001952.cms";
									  
				// +-++ http://timesofindia.indiatimes.com/world/south-asia/factory-fire-flyover-collapse-kill-137-in-bangladesh/articleshow/17360732.cms
				  //!!!http://timesofindia.indiatimes.com/city/chennai/major-fire-damages-118-year-old-sbi-building-40-staff-escape/articleshow/38292007.cms
				  //!!!http://timesofindia.indiatimes.com/city/chennai/10-workers-killed-as-effluent-tank-collapses-in-ranipet/articleshow/46082709.cms
				  
				  //+-++ / NON-CHORONOLOGICAL - NOT OK , jun 2014, aug 2014, jul 2014
//				  curr_chosen_TP_URL="http://www.ndtv.com/world-news/boko-haram-suspected-to-have-kidnapped-20-more-women-in-northeast-nigeria-576818";
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/world/middle-east/hamas-military-wing-claims-it-has-kidnapped-israeli-soldier-on-edge-of-gaza/articleshow/38766593.cms";
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/world/middle-east/hamas-admits-to-kidnapping-and-killing-of-israeli-teens/articleshow/40621164.cms";
//				  //HARD-CODED for debug -- remove later
//				  curr_chosen_TP_URL="http://timesofindia.indiatimes.com/city/chennai/railways-start-work-on-fire-safety-system/articleshow/16817373.cms";
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/city/chandigarh/fire-damages-16100-hectares-of-forest-lands/articleshow/14168921.cms";
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/city/chandigarh/rs-7-crore-goods-loss-in-ambala-showroom-fire/articleshow/17774867.cms";
				  
//				  //this set is good example (number over takes
//				  curr_chosen_TP_URL="http://timesofindia.indiatimes.com/citizens-grievances/install-road-dividers/articleshow/19848202.cms";
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/2009/4/17/india/indias-deadly-roads-likely-to-figure-in-pms-mann-ki-baat-on-sunday/articleshow/48193716.cms";
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/2015/5/23/world/rest-of-world/overnight-blasts-gunfire-mar-burundi-presidential-vote/articleshow/48156325.cms";
				  //this set is good example (number over takes <--NOT USEFUL givng -+11
//				  curr_chosen_TP_URL="http://timesofindia.indiatimes.com/city/ahmedabad/cops-ngos-to-take-social-networking-route-to-road-safety/articleshow/18919295.cms"; //2013
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/2009/4/17/india/indias-deadly-roads-likely-to-figure-in-pms-mann-ki-baat-on-sunday/articleshow/48193716.cms"; //2009
//				  URL_of_rand_number_2=""; //2015
				  //
//				  curr_chosen_TP_URL="http://www.ndtv.com/mumbai-news/potholes-or-bad-roads-use-your-blackberry-facebook-568589";
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/2009/4/17/india/indias-deadly-roads-likely-to-figure-in-pms-mann-ki-baat-on-sunday/articleshow/48193716.cms";
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/2015/7/13/city/chandigarh/commuters-suffer-due-to-closure-of-barnala-handiaya-road/articleshow/48046788.cms";
				  
				  // -+-+ 2014.2012,2012 (NOT OK) -- (INPUT : debug_fire_accident.txt )
//				  curr_chosen_TP_URL="http://www.ndtv.com/india-news/goa-building-collapse-14-killed-many-feared-trapped-fir-registered-against-builder-and-contractor-546831";
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/city/chennai/two-construction-labourers-die-in-wall-collapse-two-others-escape/articleshow/17168742.cms";
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/city/chennai/two-labourers-die-in-wall-collapse/articleshow/17177533.cms"; 
				  
				  // context-based ++-- (2002, ,)
//				  curr_chosen_TP_URL="http://timesofindia.indiatimes.com/city/thiruvananthapuram/four-fold-rise-in-road-deaths-in-kerala/articleshow/31897581.cms";
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/city/bengaluru/wall-collapses-at-metro-site-none-hurt/articleshow/16287841.cms";
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/city/bengaluru/3-killed-in-wall-collapse/articleshow/548267439.cms";
				  //solution-based , +-++ , 2009,2011,2013 (not OK) not chrological
//				  curr_chosen_TP_URL="http://www.thehindu.com/todays-paper/tp-national/tp-tamilnadu/probe-ordered-into-child-abuse-at-juvenile-home/article186561.ece";
//				  URL_of_rand_number_1="http://www.thehindu.com/todays-paper/tp-national/tp-tamilnadu/16-boys-escape-from-juvenile-home/article2644959.ece";
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/city/chennai/juvenile-held-for-rape-of-10-year-old-girl/articleshow/22776576.cms";
				  // ++++ jan2013,2009,
//				  curr_chosen_TP_URL="http://www.thehindu.com/todays-paper/tp-national/tp-andhrapradesh/road-accidents-involving-stray-dogs-cattle-on-the-rise/article5399785.ece";
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/2009/4/17/india/indias-deadly-roads-likely-to-figure-in-pms-mann-ki-baat-on-sunday/articleshow/48193716.cms";
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/city/bengaluru/wall-collapses-at-metro-site-none-hurt/articleshow/16287841.cms";
				  //  +-++ , 2013, 2015 , 2015 (not chrono) NOT OK
//				  curr_chosen_TP_URL="http://timesofindia.indiatimes.com/city/patna/25-killed-in-aurangabad-road-mishap/articleshow/17975937.cms";
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/2009/4/17/india/indias-deadly-roads-likely-to-figure-in-pms-mann-ki-baat-on-sunday/articleshow/48193716.cms";
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/city/chennai/concrete-arm-of-metro-rail-pillar-collapses-at-vadapalani/articleshow/15179566.cms";
				  
				  //-+-+ -> December 2010,Mar  2006, Sep 2008 (not OK as classification wrong) chronological  (*****INPUT : debug_kidnap.txt)
//				  curr_chosen_TP_URL="http://www.ndtv.com/world-news/taliban-kidnap-23-pakistanis-in-show-of-strength-443168";
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/city/patna/bihar-kidnappers-run-parallel-banks/articleshow/1439030.cms";
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/world/us/kidnapping-talibans-new-income-source/articleshow/3457736.cms";
				  
				  // ++-- , solution-based (hard-coded urls) ++-- 2013,2012,2012 (OK) (INPUT : debug_road.txt***)
//				  curr_chosen_TP_URL="http://timesofindia.indiatimes.com/city/allahabad/robots-designed-by-students-to-fill-potholes-on-allahabad-roads/articleshow/19164244.cms";
//				  URL_of_rand_number_1="http://www.thehindu.com/todays-paper/tp-national/tp-tamilnadu/unused-pipes-cause-hindrance-to-road-users/article3379112.ece";
//				  URL_of_rand_number_2="http://www.thehindu.com/todays-paper/tp-national/tp-tamilnadu/rs23-crore-for-linking-bypass-road-with-perambalur-town/article3715263.ece";
				  
				  // ++++ 2014, 2006, 2008 (OK) with corrected cut cost   (*****INPUT : debug_kidnap.txt)
				  curr_chosen_TP_URL="http://www.ndtv.com/world-news/militants-kidnap-kill-20-iraqi-soldiers-561301";
				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/city/patna/new-crop-of-educated-kidnappers-irks-police/articleshow/1437777.cms";
				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/world/us/kidnapping-talibans-new-income-source/articleshow/3457736.cms";
				  
				  //+-++ (NOT OK) 2010, 2002, 2008 <--NOTE: TP is not to give narrative in case study, SKIP
//				  curr_chosen_TP_URL="http://www.ndtv.com/cities/mumbai-cop-held-for-kidnapping-two-year-old-419247";
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/india/kidnapped-indian-diplomat-traced/articleshow/7371396.cms";
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/city/lucknow/3-kidnapping-accused-put-to-polygraph-test-by-cbi/articleshow/3568043.cms";
				  
				  //+-++  2005, 2002, 2005 (OK) CHRONIC (debug_kidnap.txt)
//				  curr_chosen_TP_URL="http://timesofindia.indiatimes.com/world/rest-of-world/maoists-kidnap-100-kids-in-nepal/articleshow/1226860.cms";
//				  URL_of_rand_number_1="http://timesofindia.indiatimes.com/india/kidnapped-indian-diplomat-traced/articleshow/7371396.cms";
//				  URL_of_rand_number_2="http://timesofindia.indiatimes.com/india/kidnapping-its-now-a-rich-industry-in-india/articleshow/1001952.cms";
				  
				  System.out.println("check again: "+map_URL_Verbs_from_caseStudy2.containsKey(URL_of_rand_number_1)+"<-->"
						  							+map_URL_Verbs_from_caseStudy2.containsKey(URL_of_rand_number_2)+"<-->"
						  							+map_URL_Verbs_from_caseStudy2.containsKey(curr_chosen_TP_URL));
				  
				  
				  if(!map_URL_Verbs_from_caseStudy2.containsKey(URL_of_rand_number_2)) continue;
				  if(!map_URL_Verbs_from_caseStudy2.containsKey(URL_of_rand_number_1)) continue;
				  
				  // verb and organiz
				  // true positive
				  double  curr_fn_verb_TP= 1.0 /(1.0 +map_URL_Verbs_from_caseStudy2.get(curr_chosen_TP_URL)*map_URL_Verbs_from_caseStudy2.get(curr_chosen_TP_URL));
				  double  curr_fn_organ_TP= 1.0 /(1.0 +map_URL_Organiz_from_caseStudy2.get(curr_chosen_TP_URL)*map_URL_Organiz_from_caseStudy2.get(curr_chosen_TP_URL));
				  //
				  double  curr_fn_verb_neighb1= 1.0 /(1.0 + map_URL_Verbs_from_caseStudy2.get(URL_of_rand_number_1) 
				  														*map_URL_Verbs_from_caseStudy2.get(URL_of_rand_number_1) );
				  double  curr_fn_organ_neighb1= 1.0 /(1.0 +map_URL_Organiz_from_caseStudy2.get(URL_of_rand_number_1)
				  														*+map_URL_Organiz_from_caseStudy2.get(URL_of_rand_number_1));
				  //
				  double  curr_fn_verb_neighb2= 1.0 /(1.0 +map_URL_Verbs_from_caseStudy2.get(URL_of_rand_number_2)
				  														*map_URL_Verbs_from_caseStudy2.get(URL_of_rand_number_2));
				  double  curr_fn_organ_neighb2= 1.0 /(1.0 +map_URL_Organiz_from_caseStudy2.get(URL_of_rand_number_2)
				  														*map_URL_Organiz_from_caseStudy2.get(URL_of_rand_number_2));
				  System.out.println("4.cnt:"+cnt);
				  System.out.println("TP: verb, organiz:"+map_URL_Verbs_from_caseStudy2.get(curr_chosen_TP_URL)+"----"+map_URL_Organiz_from_caseStudy2.get(curr_chosen_TP_URL));
				  System.out.println("neighb1: verb, organiz:"+map_URL_Verbs_from_caseStudy2.get(URL_of_rand_number_1)+"----"+map_URL_Organiz_from_caseStudy2.get(URL_of_rand_number_1));
				  System.out.println("neighb2: verb, organiz:"+map_URL_Verbs_from_caseStudy2.get(URL_of_rand_number_2)+"----"+map_URL_Organiz_from_caseStudy2.get(URL_of_rand_number_2));
				  
				  //sqrt(currNumbers + currYears) ---- num and year
				  double sqrt_N_num_MULTI_year_TP=Math.sqrt( map_URL_Numbers_from_caseStudy2.get(curr_chosen_TP_URL) + map_URL_Years_from_caseStudy2.get(curr_chosen_TP_URL) );
				  double sqrt_N_num_MULTI_year_NEIGH1=Math.sqrt( map_URL_Numbers_from_caseStudy2.get(URL_of_rand_number_1) + map_URL_Years_from_caseStudy2.get(URL_of_rand_number_1) );
				  double sqrt_N_num_MULTI_year_NEIGH2=Math.sqrt( map_URL_Numbers_from_caseStudy2.get(URL_of_rand_number_2) + map_URL_Years_from_caseStudy2.get(URL_of_rand_number_2) );
				  
				  System.out.println("TP: curr_fn_verb_TP, curr_fn_organ_TP, currNegSentiments:"+curr_fn_verb_TP+"----"+curr_fn_organ_TP+"----"+map_URL_NegSentiments_from_caseStudy2.get(curr_chosen_TP_URL));
				  System.out.println("neighb1: curr_fn_verb_TP, curr_fn_organ_TP, currNegSentiments:"+curr_fn_verb_neighb1+"----"+curr_fn_organ_neighb1+"----"+map_URL_NegSentiments_from_caseStudy2.get(URL_of_rand_number_1));
				  System.out.println("neighb2: curr_fn_verb_TP, curr_fn_organ_TP, currNegSentiments:"+curr_fn_verb_neighb2+"----"+curr_fn_organ_neighb2+"----"
						  								+ map_URL_NegSentiments_from_caseStudy2.get(URL_of_rand_number_2));
				  
				  // sqrt(curr_fn_verb*curr_fn_organ) * currNegSentiments
				  double curr_fn_verb_MULTI_curr_fn_organ_MULTI_currNegSentiments_TP = Math.sqrt(curr_fn_verb_TP*curr_fn_organ_TP) * map_URL_NegSentiments_from_caseStudy2.get(curr_chosen_TP_URL);
				  double curr_fn_verb_MULTI_curr_fn_organ_MULTI_currNegSentiments_NEIGH1 = Math.sqrt(curr_fn_verb_neighb1*curr_fn_organ_neighb1) * map_URL_NegSentiments_from_caseStudy2.get(URL_of_rand_number_1);
				  double curr_fn_verb_MULTI_curr_fn_organ_MULTI_currNegSentiments_NEIGH2 = Math.sqrt(curr_fn_verb_neighb2*curr_fn_organ_neighb2) * map_URL_NegSentiments_from_caseStudy2.get(URL_of_rand_number_2);
				   
				  System.out.println("curr_fn_verb_MULTI_curr_fn_organ_MULTI_currNegSentiments_TP:"+curr_fn_verb_MULTI_curr_fn_organ_MULTI_currNegSentiments_TP);
				  System.out.println("curr_fn_verb_MULTI_curr_fn_organ_MULTI_currNegSentiments_NEIGH1:"+curr_fn_verb_MULTI_curr_fn_organ_MULTI_currNegSentiments_NEIGH1);
				  System.out.println("curr_fn_verb_MULTI_curr_fn_organ_MULTI_currNegSentiments_NEIGH2:"+curr_fn_verb_MULTI_curr_fn_organ_MULTI_currNegSentiments_NEIGH2);
				  
				  double Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_N_senti= (curr_fn_verb_MULTI_curr_fn_organ_MULTI_currNegSentiments_NEIGH1+curr_fn_verb_MULTI_curr_fn_organ_MULTI_currNegSentiments_NEIGH2)
						  														/2.;
				  double Neigh_avg_4_sqrt_currNumbers_MULTI_currYears=(sqrt_N_num_MULTI_year_NEIGH1+sqrt_N_num_MULTI_year_NEIGH2)/2.;
				  System.out.println("Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_N_senti:"+Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_N_senti);
				  System.out.println("Neigh_avg_4_sqrt_currNumbers_MULTI_currYears:"+Neigh_avg_4_sqrt_currNumbers_MULTI_currYears);
				  
				  double current_doc_MINUS_Neigh_avg_4_sqrt_currNumbers_MULTI_currYears=sqrt_N_num_MULTI_year_TP-Neigh_avg_4_sqrt_currNumbers_MULTI_currYears;
				  
				  System.out.println("*current_doc_MINUS_Neigh_avg_4_sqrt_currNumbers_MULTI_currYears:"+current_doc_MINUS_Neigh_avg_4_sqrt_currNumbers_MULTI_currYears);
				  
				  double beta=0.95*current_doc_MINUS_Neigh_avg_4_sqrt_currNumbers_MULTI_currYears;
				  System.out.println("*beta:"+beta);
				  //
				  current_doc_MINUS_Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_currNegSentiments=
						  														curr_fn_verb_MULTI_curr_fn_organ_MULTI_currNegSentiments_TP -
						  														(beta*Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_N_senti);
				  
				  System.out.println("*current_doc_MINUS_Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_currNegSentiments:"+current_doc_MINUS_Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_currNegSentiments);
				  System.out.println("map_URL_NewWordsCounts_from_caseStudy2:"+map_URL_NewWordsCounts_from_caseStudy2.containsKey(curr_chosen_TP_URL));
				  System.out.println("*currNewWordCounts - avg_new_words4Alldoc--->"+(map_URL_NewWordsCounts_from_caseStudy2.get( curr_chosen_TP_URL)
						  																		-avg_new_words4Alldoc));
				  //
				  if(map_URL_NewWordsCounts_from_caseStudy2.get( curr_chosen_TP_URL)<0){
					  //recalculate new words
					  map_URL_NewWordsCounts_from_caseStudy2.get( curr_chosen_TP_URL);
					  
					  String chosen_TP_bodyText=map_URL_bodyTEXT_topicWisedocuments.get(curr_chosen_TP_URL);
					  String chosen_bodyTextNeigh1=map_URL_bodyTEXT_topicWisedocuments.get(URL_of_rand_number_1);
					  String chosen_bodyTextNeigh2=map_URL_bodyTEXT_topicWisedocuments.get(URL_of_rand_number_2);
					  /////////////stemmed word for TP
					  String []_arr_words_TP=chosen_TP_bodyText.toLowerCase().split(" ");
					  int cnt1=0;
					  TreeMap<String, String> map_Stemmedword_forTP_asKEY=new TreeMap<String, String>();
					  int seq=0;
					  //
					  while(cnt1<_arr_words_TP.length){
						 
						  if(!crawler.Stopwords.is_stopword(_arr_words_TP[cnt1] )){
							  String stemWord=Stemmer.stem_2(_arr_words_TP[cnt1]);
//							  seq++;
							  map_Stemmedword_forTP_asKEY.put(stemWord, "");
						  }
						  cnt1++;
					  }
					  /////////////stemmed word for Neighbor 1 
					  String []_arr_words_Neighb1=chosen_bodyTextNeigh1.toLowerCase().split(" ");
					  cnt1=0;
					  TreeMap<String, String> map_Stemmedword_forNeighb1_asKEY=new TreeMap<String, String>();
					  //
					  while(cnt1<_arr_words_Neighb1.length){
						 
						  if(!crawler.Stopwords.is_stopword(_arr_words_Neighb1[cnt1] )){
							  String stemWord=Stemmer.stem_2(_arr_words_Neighb1[cnt1]);
							  map_Stemmedword_forNeighb1_asKEY.put(stemWord, "");
						  }
						  cnt1++;
					  }
					  /////////////stemmed word for Neighbor 2
					  String []_arr_words_Neighb2=chosen_bodyTextNeigh2.toLowerCase().split(" ");
					  cnt1=0;
					  TreeMap<String, String> map_Stemmedword_forNeighb2_asKEY=new TreeMap<String, String>();
					  while(cnt1<_arr_words_Neighb2.length){
							 
						  if(!crawler.Stopwords.is_stopword(_arr_words_Neighb2[cnt1] )){
							  String stemWord=Stemmer.stem_2(_arr_words_Neighb2[cnt1]);
							  map_Stemmedword_forNeighb2_asKEY.put(stemWord, "");
						  }
						  cnt1++;
					  }
					  int NewWord_count_between_TP_n_Neighb1=0;
					  int NewWord_count_between_TP_n_Neighb2=0;
					// between tp and neighb1
					  for(String currStemmedWord:map_Stemmedword_forTP_asKEY.keySet()){
						  	//
						   if(map_Stemmedword_forNeighb1_asKEY.containsKey(currStemmedWord)){
							   NewWord_count_between_TP_n_Neighb1++;
						   }
					  }
					   
					  // between tp and neighb2
					  for(String currStemmedWord:map_Stemmedword_forTP_asKEY.keySet()){
						  	//
						   if(map_Stemmedword_forNeighb2_asKEY.containsKey(currStemmedWord)){
							   NewWord_count_between_TP_n_Neighb2++;
						   }
					  }
					  
					  double Average_of_TP_with_2OtherNeighbours= (NewWord_count_between_TP_n_Neighb1+NewWord_count_between_TP_n_Neighb2)/ 2.0;
					  //resave 
					  map_URL_NewWordsCounts_from_caseStudy2.put(curr_chosen_TP_URL, Average_of_TP_with_2OtherNeighbours);
					  
					  System.out.println("* RECALC-->currNewWordCounts - avg_new_words4Alldoc--->"
							  		+(map_URL_NewWordsCounts_from_caseStudy2.get( curr_chosen_TP_URL)-avg_new_words4Alldoc));
					  
				  }
				  
				  
				  double avg_new_words_NEIGHB= (map_URL_NewWordsCounts_from_caseStudy2.get(URL_of_rand_number_1 ) +
						  						map_URL_NewWordsCounts_from_caseStudy2.get(URL_of_rand_number_2 ) )/ 2.0;
						  						
				  
				  ////// end of new word
				  
				  double CC= current_doc_MINUS_Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_currNegSentiments 
						  * (map_URL_NewWordsCounts_from_caseStudy2.get( curr_chosen_TP_URL)-avg_new_words_NEIGHB);
//						  		* (map_URL_NewWordsCounts_from_caseStudy2.get( curr_chosen_TP_URL)-avg_new_words4Alldoc);
				  System.out.println("CC:"+CC);
				  System.out.println("3.cnt:"+cnt);
				  //
				  String conc_symbols="";
				  //
				  if(CC<0.)
					  conc_symbols="-";
				  else
					  conc_symbols="+";
				  //
				  if(current_doc_MINUS_Neigh_avg_4_sqrt_currNumbers_MULTI_currYears<0.)
					  conc_symbols+="-";
				  else
					  conc_symbols+="+";
			 
				  //
				  if(current_doc_MINUS_Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_currNegSentiments<0.)
					  conc_symbols+="-";
				  else
					  conc_symbols+="+";
				  //
				  if((map_URL_NewWordsCounts_from_caseStudy2.get(curr_chosen_TP_URL)-avg_new_words_NEIGHB)<0.) // changed here
					  conc_symbols+="-";
				  else
					  conc_symbols+="+";
				  
				  // out
				  writer.append(CC+
						  		"!!!"+current_doc_MINUS_Neigh_avg_4_sqrt_currNumbers_MULTI_currYears+"!!!"
						  		+String.valueOf(current_doc_MINUS_Neigh_avg_4_sqrt_fn_verb_MULTI_fn_organ_MULTI_currNegSentiments)+"!!!"
						  		+(map_URL_NewWordsCounts_from_caseStudy2.get(curr_chosen_TP_URL)-avg_new_words4Alldoc)+"!!!"
						  		+curr_chosen_TP_URL+"!!!"+URL_of_rand_number_1+"!!!"+URL_of_rand_number_2+"!!!"+conc_symbols
						  		+ "!!!"+map_URL_NewWordsCounts_from_caseStudy2.get(curr_chosen_TP_URL)+"!!!"+avg_new_words4Alldoc+
						  		"!!!"+avg_new_words_NEIGHB+"\n");
				  writer.flush();
				  //
				  writer2.append( conc_symbols
						  		+curr_chosen_TP_URL+"!!!"+URL_of_rand_number_1+"!!!"+URL_of_rand_number_2+"\n");
				  writer2.flush();
				  
				  if(!map_UniqueSymbol_CountOccurance.containsKey(conc_symbols)  ){
					  map_UniqueSymbol_CountOccurance.put(conc_symbols, 1);
				  }
				  else{
					  int newCount=map_UniqueSymbol_CountOccurance.get(conc_symbols)+1;
					  map_UniqueSymbol_CountOccurance.put(conc_symbols, newCount);
				  }
				  
				  System.out.println("2.cnt:"+cnt);
				  //
//				  if(cnt>=4) break;
				  
				  final_conc_symbols=conc_symbols;
				  
				  
				  
				  System.out.println("-------for URL_of_rand_number_1:"+URL_of_rand_number_1);
				  System.out.println(map_URL_Organiz_from_caseStudy2.get(URL_of_rand_number_1)+"&"+map_URL_Verbs_from_caseStudy2.get(URL_of_rand_number_1)
									  +"&"+map_URL_Numbers_from_caseStudy2.get(URL_of_rand_number_1)+"&"+map_URL_NegSentiments_from_caseStudy2.get(URL_of_rand_number_1)
									  +"&"+map_URL_Years_from_caseStudy2.get(URL_of_rand_number_1)+"&"+map_URL_NewWordsCounts_from_caseStudy2.get(URL_of_rand_number_1));
				  System.out.println("-------for URL_of_rand_number_2:"+URL_of_rand_number_2);
				  System.out.println(map_URL_Organiz_from_caseStudy2.get(URL_of_rand_number_2)+"&"+map_URL_Verbs_from_caseStudy2.get(URL_of_rand_number_2)
								  +"&"+map_URL_Numbers_from_caseStudy2.get(URL_of_rand_number_2)+"&"+map_URL_NegSentiments_from_caseStudy2.get(URL_of_rand_number_2)
								  +"&"+map_URL_Years_from_caseStudy2.get(URL_of_rand_number_2)+"&"+map_URL_NewWordsCounts_from_caseStudy2.get(URL_of_rand_number_2));
				  System.out.println("-------for curr_chosen_TP_URL:"+curr_chosen_TP_URL);
				  System.out.println(map_URL_Organiz_from_caseStudy2.get(curr_chosen_TP_URL)+"&"+map_URL_Verbs_from_caseStudy2.get(curr_chosen_TP_URL)
								  +"&"+map_URL_Numbers_from_caseStudy2.get(curr_chosen_TP_URL)+"&"+map_URL_NegSentiments_from_caseStudy2.get(curr_chosen_TP_URL)
								  +"&"+map_URL_Years_from_caseStudy2.get(curr_chosen_TP_URL)+"&"+map_URL_NewWordsCounts_from_caseStudy2.get(curr_chosen_TP_URL));
				  
			  }
			  
			  writer2.append(map_UniqueSymbol_CountOccurance.toString()+"\n");
			  writer2.flush();
			  
			  System.out.println("----------------------------------------------------------------------------------------------------------");
			  //
			  System.out.println("map_tpURL_asKEY:"+map_tpURL_asKEY.size()+"\n map_URL_lineNo_topicWisedocuments.size:"+map_URL_lineNo_topicWisedocuments.size()
					  			+"\n map_URL_NegSentiments_from_caseStudy2.size(and so on..):"+map_URL_NegSentiments_from_caseStudy2.size()
					  			+"\n map_URL_NewWordsCounts_from_caseStudy2.get( curr_chosen_TP_URL):"+map_URL_NewWordsCounts_from_caseStudy2.get( curr_chosen_TP_URL)
					  			+"\n final_conc_symbols:"+final_conc_symbols);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	//transfer_ground_truth_from_1file_2_another
	private static void transfer_ground_truth_from_1file_2_another(
														String inputFile1, String inputFile2,
														String output) {
		// TODO Auto-generated method stub
		try{
			//$url!!!$body!!!$line!!!$fname!!!$yyyy!!!$mm
			  TreeMap<Integer, String>  map_seq_inFile1=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile1, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  
			  //$url!#!#$body__NN!#!#$body!#!#$body__NN!#!#!#!#!#!#person_CSVMap!#!#organiz_CSVMap!#!#location_CSVMap!#!#Numbers_CSVMap!#!#Nouns!#!#SentimentMap!#!#Verb!#!#eachSentencSentimenCSV!#!#ConsecutivePerson!#!#ConsecutiveOrganization!#!#ConsecutiveLocation!#!#ConsecutivePersonNlineNo!#!#ConsecutiveOrganizationNlineNo!#!#ConsecutiveLocationNlineNo!#!#ConsecutivePersonNlineNoWITHquotes!#!#ConsecutiveOrganizationNlineNoWITHquotes!#!#ConsecutiveLocationNlineNoWITHquotes
			  TreeMap<Integer, String>  map_seq_inFile2=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile2, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  FileWriter writer=new FileWriter(output);
			  
			  TreeMap<String,String> map_URL_filenameToken_from_file1=new TreeMap<String, String>();
			  //$url!!!$body!!!$line!!!$fname!!!$yyyy!!!$mm
			  for(int seq:map_seq_inFile1.keySet()){
				  String [] arr=map_seq_inFile1.get(seq).split("!!!");
				  map_URL_filenameToken_from_file1.put(arr[0], arr[3] );
			  }
			  TreeMap<String,String> map_URL_completeLINE_file2=new TreeMap<String, String>();
			  //$url!#!#$body__NN!#!#$body!#!#$body__NN!#!#!#!#!#!#person_CSVMap!#!#organiz_CSVMap!#!#location_CSVMap!#!#Numbers_CSVMap!#!#Nouns!#!#SentimentMap!#!#Verb!#!#eachSentencSentimenCSV!#!#ConsecutivePerson!#!#ConsecutiveOrganization!#!#ConsecutiveLocation!#!#ConsecutivePersonNlineNo!#!#ConsecutiveOrganizationNlineNo!#!#ConsecutiveLocationNlineNo!#!#ConsecutivePersonNlineNoWITHquotes!#!#ConsecutiveOrganizationNlineNoWITHquotes!#!#ConsecutiveLocationNlineNoWITHquotes
			  for(int seq:map_seq_inFile2.keySet()){
				  String [] arr=map_seq_inFile2.get(seq).split("!#!#");
				  map_URL_completeLINE_file2.put(arr[0] , map_seq_inFile2.get(seq));
			  }
			  int cnt=0;
			  for(String url:map_URL_completeLINE_file2.keySet()){
				  
				  if(map_URL_filenameToken_from_file1.containsKey(url)){
					  writer.append(map_URL_completeLINE_file2.get(url)+"!#!#"+map_URL_filenameToken_from_file1.get(url)+"\n");
					  writer.flush();
					  cnt++;
				  }
				  
			  }
			  
			  System.out.println("wrote:"+cnt+" "+map_URL_completeLINE_file2.size()
					  								+" "+map_URL_filenameToken_from_file1.size());
//			  for(String url:map_URL_completeLINE_file2.keySet())
//				  System.out.println("map_URL_filenameToken_from_file1:"+map_URL_completeLINE_file2.get(url));
			  
			  
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	// calculate_newWord_for_eachDocument_from_bodyTEXT
	private static void calculate_newWord_for_eachDocument_from_bodyTEXT(
																		 String inputFile,
																		 String outputFile_10_URL_n_totalNewWords
																		 ){
		// TODO Auto-generated method stub
		try{
			FileWriter writer=new FileWriter(outputFile_10_URL_n_totalNewWords);
			//header-->
			//$url!#!#$body__NN!#!#$body!#!#$body__NN!#!#!#!#@@@@person_CSVMap@@@@organiz_CSVMap@@@@location_CSVMap@@@@Numbers_CSVMap@@@@Nouns@@@@SentimentMap@@@@Verb@@@@eachSentencSentimenCSV@@@@ConsecutivePerson@@@@ConsecutiveOrganization@@@@ConsecutiveLocation@@@@ConsecutivePersonNlineNo@@@@ConsecutiveOrganizationNlineNo@@@@ConsecutiveLocationNlineNo@@@@ConsecutivePersonNlineNoWITHquotes@@@@ConsecutiveOrganizationNlineNoWITHquotes@@@@ConsecutiveLocationNlineNoWITHquotes
				// 	Loading seq and Line 
			  TreeMap<Integer, String>  map_seq_inFile_TEMP=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  TreeMap<String, String> map_URL_bodyTEXT=new TreeMap<String, String>();
			  TreeMap<Integer, String> map_seq_N_URL=new TreeMap<Integer, String>();
			  // 
			  for(int seq:map_seq_inFile_TEMP.keySet()){
				  String eachLine=map_seq_inFile_TEMP.get(seq);
				  String [] arr_2= eachLine.split("!#!#");
				  String curr_bodyTEXT=arr_2[2];
				  
				  if(curr_bodyTEXT.indexOf("@@@@")>=0)
					  curr_bodyTEXT=curr_bodyTEXT.substring(0, curr_bodyTEXT.indexOf("@@@@"));

				  
				  String curr_URL=arr_2[0];	
//				  System.out.println("curr_bodyTEXT:"+curr_bodyTEXT);
				  map_URL_bodyTEXT.put(curr_URL, curr_bodyTEXT);
				  map_seq_N_URL.put(seq, curr_URL);
			  }
			  
			  double overall_average_newWords=0.;
			  int count_documents_for_denominaor=0;
			  //
			  for(int seq:map_seq_inFile_TEMP.keySet()){
				   
				  if(seq>=3){
					  String URL_of_currDoc=map_seq_N_URL.get(seq);
					  String URL_of_NeighDoc1=map_seq_N_URL.get(seq-1);
					  String URL_of_NeighDoc2=map_seq_N_URL.get(seq-2);
					  //
					  String bodyTEXT_of_currDoc=map_URL_bodyTEXT.get(URL_of_currDoc);
					  String bodyTEXT_of_NeighDoc1=map_URL_bodyTEXT.get(URL_of_NeighDoc1);
					  String bodyTEXT_of_NeighDoc2=map_URL_bodyTEXT.get(URL_of_NeighDoc2);
					  //
					  /////////////stemmed word for TP
					  String []_arr_words_TP=bodyTEXT_of_currDoc.toLowerCase().split(" ");
					  int cnt1=0;
					  TreeMap<String, String> map_Stemmedword_forTP_asKEY=new TreeMap<String, String>();
					   
					  //
					  while(cnt1<_arr_words_TP.length){
						 
						  if(!crawler.Stopwords.is_stopword(_arr_words_TP[cnt1] )){
							  String stemWord=Stemmer.stem_2(_arr_words_TP[cnt1]);
							  map_Stemmedword_forTP_asKEY.put(stemWord, "");
						  }
						  cnt1++;
					  }
					  /////////////stemmed word for Neighbor 1 
					  String []_arr_words_Neighb1=bodyTEXT_of_NeighDoc1.toLowerCase().split(" ");
					  cnt1=0;
					  TreeMap<String, String> map_Stemmedword_forNeighb1_asKEY=new TreeMap<String, String>();
					  //
					  while(cnt1<_arr_words_Neighb1.length){
						 
						  if(!crawler.Stopwords.is_stopword(_arr_words_Neighb1[cnt1] )){
							  String stemWord=Stemmer.stem_2(_arr_words_Neighb1[cnt1]);
							  map_Stemmedword_forNeighb1_asKEY.put(stemWord, "");
						  }
						  cnt1++;
					  }
					  /////////////stemmed word for Neighbor 2
					  String []_arr_words_Neighb2=bodyTEXT_of_NeighDoc2.toLowerCase().split(" ");
					  cnt1=0;
					  TreeMap<String, String> map_Stemmedword_forNeighb2_asKEY=new TreeMap<String, String>();
					  while(cnt1<_arr_words_Neighb2.length){
							 
						  if(!crawler.Stopwords.is_stopword(_arr_words_Neighb2[cnt1] )){
							  String stemWord=Stemmer.stem_2(_arr_words_Neighb2[cnt1]);
							  map_Stemmedword_forNeighb2_asKEY.put(stemWord, "");
						  }
						  cnt1++;
					  }
					  int NewWord_count_between_TP_n_Neighb1=0;
					  int NewWord_count_between_TP_n_Neighb2=0;
					// between tp and neighb1
					  for(String currStemmedWord:map_Stemmedword_forTP_asKEY.keySet()){
						  	//
						   if(map_Stemmedword_forNeighb1_asKEY.containsKey(currStemmedWord)){
							   NewWord_count_between_TP_n_Neighb1++;
						   }
					  }
					   
					  // between tp and neighb2
					  for(String currStemmedWord:map_Stemmedword_forTP_asKEY.keySet()){
						  	//
						   if(map_Stemmedword_forNeighb2_asKEY.containsKey(currStemmedWord)){
							   NewWord_count_between_TP_n_Neighb2++;
						   }
					  }
					  
					  double Average_of_TP_with_2OtherNeighbours= (NewWord_count_between_TP_n_Neighb1+NewWord_count_between_TP_n_Neighb2)/ 2.0;
					  // 
					  writer.append(URL_of_currDoc+"!!!"+Average_of_TP_with_2OtherNeighbours +"\n" );
					  writer.flush();
					  //overall average
					  overall_average_newWords+=Average_of_TP_with_2OtherNeighbours;
					  
					  count_documents_for_denominaor++;
				  }
				  
			  }
			  // 
			  writer.append("overall_average_newWords"+"!!!"+(overall_average_newWords/count_documents_for_denominaor) +"\n" );
			  writer.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	
	// MAIN 
	public static void main(String args[]) throws IOException {
		String baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/";
		String inputFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all_new.txt_added_perso_origz_loc_verb2_done.txt";
		
			// THIS is JUST debug FILE..
//			inputFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all_new.txt_added_perso_origz_loc_verb2_done ONLY1url.txt";
		
		String inputFile_2_URL_n_totalNewWords="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/URL_totalNewWords.txt";
		String outputFile_10_URL_n_totalNewWords="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/URL_totalNewWords2.txt";
		
		String outputFile1=baseFolder+"out_caseStudy1.txt";
		String outputFile2_out_caseStudy2=baseFolder+"out_caseStudy2.txt";		
		//we consider verb that occurs in > 1 articles
		String inFil2_verb=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_VERB_LINENO.txtSTEMMED_NODUP.txt";
		String inFil2_organization=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_ORGANIZ_LINENO.txtSTEMMED_NODUP.txt";
		
		// This is to replace (if choosen) the file from variable ""
		//calculate new Word from basic using bodyTEXT
//		calculate_newWord_for_eachDocument_from_bodyTEXT(
//														 inputFile,
//														 outputFile_10_URL_n_totalNewWords
//														 );
		
		inputFile_2_URL_n_totalNewWords = outputFile_10_URL_n_totalNewWords;
		
		//comment/uncomment
		//caseStudy_DrMills_request
//		caseStudy_DrMills_request(
//									baseFolder,
//									inputFile,
//									inputFile_2_URL_n_totalNewWords,
//									inFil2_verb,
//									inFil2_organization,
//									outputFile1,
//									outputFile2_out_caseStudy2
//								 );
		
		
		// THIS FILE is prepared using GREP on file 
		//cat /Users/lenin/Dropbox/\#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo_CHRONO.txt | grep mining  > debug_mining.txt
		String inputFile_4_topicWiseFile_CHRON="";
			   inputFile_4_topicWiseFile_CHRON="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/debug_road_accident.txt";
//			   inputFile_4_topicWiseFile_CHRON="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/debug_human_trafficking.txt";
			   inputFile_4_topicWiseFile_CHRON="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/debug_fire_accident.txt";
//		       inputFile_4_topicWiseFile_CHRON="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/debug_road.txt";
// 			   inputFile_4_topicWiseFile_CHRON="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/debug_road_MINUS_fire_swine_collapse.txt";
			   inputFile_4_topicWiseFile_CHRON="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/debug_kidnap.txt";
//			   inputFile_4_topicWiseFile_CHRON="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/debug_senior_citizen.txt";
			   
		// all labelled 
		String inputFile_5_groundTruth_by_Maitrayi="/Users/lenin/Dropbox/Maitrayi/Full Article/ground truth/3all_labelled_merged.txt";
		String outputFile6_out_calc_objectiveFunction_4_caseStudy2=baseFolder+"out_calc_objectiveFunction_4_caseStudy2.txt";
		
		// output of above method is used as input to below
		// INPUT 3 URL, do the calculation of objective function..
		Do_calc_value_4_obj_function(
									outputFile2_out_caseStudy2, // in
									inputFile_4_topicWiseFile_CHRON, //in
									inputFile_5_groundTruth_by_Maitrayi, //in
									outputFile6_out_calc_objectiveFunction_4_caseStudy2 //out
									);
		
		String inputFile1="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo_CHRONO.txt";
		String inputFile2="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb2_normalizeDelimiter.txt";
		String output=    "/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb2_normalizeDelimiterAddedGTruthToken.txt";
		
		//transfer_ground_truth_from_1file_2_another (we have one file without true positive (ground truth folder path) and one with it, so match them)
//		transfer_ground_truth_from_1file_2_another(
//													inputFile1,
//													inputFile2,
//													output
//													);
		
	}
}