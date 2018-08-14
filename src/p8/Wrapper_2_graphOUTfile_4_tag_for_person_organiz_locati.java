package p8;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

import crawler.LoadMultipleValueToMap2_AS_KEY_VALUE;
import crawler.StanfordNER;
import crawler.Stemmer;
import crawler.Calc_checksum;
import crawler.Calc_tf_idf;
import crawler.Clean_retain_only_alpha_numeric_characters;
import crawler.Find_sentiment_stanford_OpenNLP;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import crawler.ReadFolder_load_checksum_N_sentiment_from_eachFile;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

import p8.MyAlgo;

//
public class Wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati {
	
	    // Flag_type={1=works as usual(past),2="add extra Organization (without token breaking) "}
		// get person, locati, organiz
		public static TreeMap<String, Integer> graphOUTfile_4_tag_for_person_organiz_locati(  
															String 	baseFolder, 
															String 	inFile, 
															String  delimiter, 
															int 	token_containing_bodyText_withoutTagging,
															int 	token_containing_bodyText_withNLPTagging,
															boolean is_having_header,
															TreeMap<String, String > map_Word_StemmedWord,
															String  inFolder_repositoryFolder_for_past_sentiment_saved,
															String  prefix_for_problem, // example: p18
															int     Flag_type // 
															){
			TreeMap<Integer,String> map_inFile_eachLine=new TreeMap<Integer, String>();
			TreeMap<String, Integer> map_NAMECount_value=new TreeMap<String, Integer>();
			System.out.println("inside tag_for_person_organiz_locati ()");
			FileWriter writer=null;
			TreeMap<Integer,TreeMap<String,String>> map_Out_Sentim_repo=new TreeMap<Integer,TreeMap<String, String>>();
			Stemmer stemmer=new Stemmer();
			TreeMap<String,String> map_repo_checksum_score=new TreeMap<String, String>();
			TreeMap<String,String> map_repo_checksum_compressedSentence=new TreeMap<String, String>();
			String checksum_of_curr_document="";
			FileWriter writer_global_repository_to_save_sentenceWise_sentiments=null;
			int count_empty_bodyTEXT=0;
			// TODO Auto-generated method stub
			try {
				
				//read all files and choose one ( not DS_Store and README)
				String [] repo_folder_files=new File(inFolder_repositoryFolder_for_past_sentiment_saved).list();
				int max_files=repo_folder_files.length; int cnt=0;
				String file_to_write_semantic_repository="";
				while(cnt < max_files ){
					if(repo_folder_files[cnt].indexOf("senti.repo") >=0){
						file_to_write_semantic_repository=repo_folder_files[cnt];
						break;
					}
					cnt++;
				}
				// if this still blank, then assign one file name.
				if(file_to_write_semantic_repository.length()==0)
					file_to_write_semantic_repository=prefix_for_problem+".senti.repo.1.txt";
				
				//adding base folder..
				file_to_write_semantic_repository=inFolder_repositoryFolder_for_past_sentiment_saved+file_to_write_semantic_repository;
				
				System.out.println("***File to write for repository:"+file_to_write_semantic_repository) ;
				//APPEND
				writer_global_repository_to_save_sentenceWise_sentiments=new FileWriter(file_to_write_semantic_repository, true);
				
				// LOAD REPOSITORIES (sentiment) 0:very negative, 1:negative 2:neutral 3:positive and 4:very positive. 
				map_Out_Sentim_repo=ReadFolder_load_checksum_N_sentiment_from_eachFile
								   .readFolder_load_checksum_N_sentiment_from_eachFile(inFolder_repositoryFolder_for_past_sentiment_saved,
										  											   baseFolder+"debug.semantic.repo.txt" , //debugFile,
										  											   false //isSOPprint
										  											  );
				map_repo_checksum_score=map_Out_Sentim_repo.get(1);
				map_repo_checksum_compressedSentence=map_Out_Sentim_repo.get(2);
				  // 
				  String serializedClassifier ="/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.conll.4class.distsim.crf.ser.gz";
				  
				  		 serializedClassifier ="/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.all.3class.distsim.crf.ser.gz";
				  
				  CRFClassifier<CoreLabel>  classifier=CRFClassifier.getClassifierNoExceptions(serializedClassifier);
				  
				  //OUTPUT file
				  if(Flag_type==1)
					  writer=new FileWriter(new File(inFile+"_added_perso_origz_loc_verb.txt"));
				  else if(Flag_type==2){
					  writer=new FileWriter(new File(inFile+"_added_perso_origz_loc_verb"+Flag_type+".txt"));
				  }
				  // 
				  map_inFile_eachLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
									 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(inFile, 
																											 	 -1, //startline, 
																												 -1, //endline,
																												 " ", //debug_label
																												 false //isPrintSOP
																												 );
				  String header="";
				  if(is_having_header){
					  header=map_inFile_eachLine.get(1)+"@@@@person_CSVMap@@@@organiz_CSVMap@@@@location_CSVMap@@@@Numbers_CSVMap@@@@Nouns@@@@SentimentMap@@@@Verb@@@@eachSentencSentimenCSV";
					  header=header+"@@@@ConsecutivePerson@@@@ConsecutiveOrganization@@@@ConsecutiveLocation";
					  header=header+"@@@@ConsecutivePersonNlineNo@@@@ConsecutiveOrganizationNlineNo@@@@ConsecutiveLocationNlineNo";
					  header=header+"@@@@ConsecutivePersonNlineNoWITHquotes@@@@ConsecutiveOrganizationNlineNoWITHquotes@@@@ConsecutiveLocationNlineNoWITHquotes";
					  writer.append(header+"\n");
					  writer.flush();
				  }
				  TreeMap<Double,Integer> map_Sentiment_Count=new TreeMap<Double, Integer>();
				  TreeMap<Integer,TreeMap<Integer,String>>  map=new TreeMap<Integer, TreeMap<Integer,String>>();
				  int end=-1;
				  int count_passed_after_filters=0;
				  // each line
				  for(int seq:map_inFile_eachLine.keySet()){
					  map_Sentiment_Count=new TreeMap<Double, Integer>();
					  System.out.println("lineNo:"+seq);
					  String currLine_ORIG=map_inFile_eachLine.get(seq);
					  String currLine=currLine_ORIG.replace(delimiter+delimiter, delimiter+"d"+delimiter);
					  if(is_having_header && seq==1) continue;
	 				  
					  	 String [] s=currLine.split(delimiter);
					  	 String curr_NLP_string=s[token_containing_bodyText_withNLPTagging-1];
					  	 String curr_bodyText_without_NLP=s[token_containing_bodyText_withoutTagging-1];
					  	 
//					  	 System.out.println("curr_NLP_string:"+curr_NLP_string);
//					  	System.out.println("curr_bodyText_without_NLP:"+curr_bodyText_without_NLP);
					  	 
					  	 //CLEANING 
					  	curr_bodyText_without_NLP=curr_bodyText_without_NLP.replace( "&amp;", " ").replace("&lt"," ").replace("&gt"," ").replace("&quot"," ")
					  															.replace("&nbsp;", " ").replace("  ", " ");
					  	
						  	end=curr_bodyText_without_NLP.toLowerCase().indexOf("follow us",1000);
				  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
				  			
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf("published on",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf("get a copy ",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf("var var ",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf(" var ",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf(" more+ view ",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		
					  		
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf(" may be reached at ",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf(". follow ",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf("editing by ",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf(".storylist",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf("the author also",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf(". more news",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf(".single ",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf("tmgads.embedplayer ",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf("this article ",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf("for related stor",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf(" related stories ",1000);
					  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf("for related news",1000);
					  		if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
					  		end=curr_bodyText_without_NLP.toLowerCase().indexOf("published at",1000);
				  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
				  			end=curr_bodyText_without_NLP.toLowerCase().indexOf("text text ",1000);
				  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
				  			//finally
				  			end=curr_bodyText_without_NLP.toLowerCase().indexOf("beginindexis",1000);
				  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
				  			
				  			end=curr_bodyText_without_NLP.toLowerCase().indexOf("follow inquirer",1000);
				  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
				  			
				  			end=curr_bodyText_without_NLP.toLowerCase().indexOf("watch the video here",1000);
				  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end); 
				  			
				  			end=curr_bodyText_without_NLP.toLowerCase().indexOf("below article thumbnails",1000);
				  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
				  			
				  			end=curr_bodyText_without_NLP.toLowerCase().indexOf("all rights reserved ",1000);
				  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
				  			
				  			end=curr_bodyText_without_NLP.toLowerCase().indexOf("jshint true,",1000);
				  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
				  			
				  			end=curr_bodyText_without_NLP.toLowerCase().indexOf("photos click",1000);
				  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
				  			
				  			end=curr_bodyText_without_NLP.toLowerCase().indexOf("for further details",1000);
				  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
				  			
				  			end=curr_bodyText_without_NLP.toLowerCase().indexOf("in other news...",1000);
				  			if(end>0) curr_bodyText_without_NLP=curr_bodyText_without_NLP.substring(0, end);
				  			 
					  	curr_NLP_string=curr_NLP_string.replace( "&amp;", " ").replace("&lt"," ").replace("&gt"," ").replace("&quot"," ");
					  	
					  	 
					  	 String [] arr_curr_NLP_string=curr_NLP_string.split(" ");
					  	 //NOUN
					  	 String _currLine_nouns=""; int cnt222=0;
					  	 while(cnt222<arr_curr_NLP_string.length){
					  		 	
					  		int find_1_idx=arr_curr_NLP_string[cnt222].indexOf("__N");
					  		int find_2_idx=arr_curr_NLP_string[cnt222].indexOf("_N");
					  		
					  		if(find_1_idx<0 || find_2_idx<0){
					  			find_1_idx=arr_curr_NLP_string[cnt222].indexOf("__n");
					  			find_2_idx=arr_curr_NLP_string[cnt222].indexOf("_n");
					  		}
					  		
					  		 // //
					  		 if(find_1_idx>=0 || find_2_idx>=0){
					  			 
					  			 if(find_1_idx>=0){
						  			 if(_currLine_nouns.length()==0)
						  				_currLine_nouns=arr_curr_NLP_string[cnt222].substring(0, find_1_idx);
						  			 else
						  				_currLine_nouns=_currLine_nouns+" "+arr_curr_NLP_string[cnt222].substring(0, find_1_idx);
					  			 }
					  			 else if(find_2_idx>=0){
						  			 if(_currLine_nouns.length()==0)
						  				_currLine_nouns=arr_curr_NLP_string[cnt222].substring(0, find_2_idx);
						  			 else
						  				_currLine_nouns=_currLine_nouns+" "+arr_curr_NLP_string[cnt222].substring(0, find_2_idx);
					  			 }
					  			 
//						  		 System.out.println("is noun ? "+find_1_idx+" token_containing_bodyText_withNLPTagging:"+token_containing_bodyText_withNLPTagging 
//			  				 				+" token="+arr_curr_NLP_string[cnt222]+" find_1_idx:"+find_1_idx+" find_2_idx:"+find_2_idx
//			  				 				+" _currLine_nouns:"+_currLine_nouns
//			  				 				+" curr_NLP_string:"+curr_NLP_string+" currLine:"+currLine+" currLine_ORIG:"+currLine_ORIG
//			  				 				);
					  			 
					  		 }
					  		 cnt222++;
					  	 }
					  	 String _currLine_verb="";
					  	////////////VERB
					  	cnt222=0;
					  	while(cnt222<arr_curr_NLP_string.length){
//					  		System.out.println("is verb ? "+arr_curr_NLP_string[cnt222].indexOf("__V")+" token_containing_bodyText_withNLPTagging:"+token_containing_bodyText_withNLPTagging 
//					  						+" token="+arr_curr_NLP_string[cnt222]+" curr_NLP_string:"+curr_NLP_string
//					  							+" currLine:"+currLine+" currLine_ORIG:"+currLine_ORIG);
					  		
					  		
					  		int find_1_idx=arr_curr_NLP_string[cnt222].indexOf("__V");
					  		int find_2_idx=arr_curr_NLP_string[cnt222].indexOf("_V");
					  		
					  		if(find_1_idx<0)
					  			find_1_idx=arr_curr_NLP_string[cnt222].indexOf("__v");
					  		if(find_2_idx<0)
					  			find_2_idx=arr_curr_NLP_string[cnt222].indexOf("_v");
					  		
					  		 if(find_1_idx>=0){
					  			 if(_currLine_verb.length()==0)
					  				_currLine_verb=arr_curr_NLP_string[cnt222].substring(0, find_1_idx);
					  			 else
					  				_currLine_verb=_currLine_verb+" "+arr_curr_NLP_string[cnt222].substring(0, find_1_idx);
					  		 }
					  		 else if(find_2_idx>=0){
					  			 if(_currLine_verb.length()==0)
					  				_currLine_verb=arr_curr_NLP_string[cnt222].substring(0, find_2_idx);
					  			 else
					  				_currLine_verb=_currLine_verb+" "+arr_curr_NLP_string[cnt222].substring(0, find_2_idx);
					  		 }
					  		 
					  		 cnt222++;
					  	 }
					  	 
					  	//each SENTENCE
					  	String [] arr_curr_bodyText_without_NLP=curr_bodyText_without_NLP.split("\\.");
					  	String [] arr_curr_taggedNLP=curr_NLP_string.split("\\.");
					  	String 	curr_sentence_taggedNLP="";
					  	int 	cnt2222=0;
					  	
					  	String conc_currLine_eachSentenc_sentiment="";
					  	//System.out.println("sentiment ->len "+arr_curr_bodyText_without_NLP.length+" "+curr_bodyText_without_NLP);
					  	// 
					  	while(cnt2222<arr_curr_bodyText_without_NLP.length){
					  		 //untagged NLP
					  		 String sentence_= arr_curr_bodyText_without_NLP[cnt2222];
					  		 // tagged NLP
					  		 try{
					  			curr_sentence_taggedNLP= arr_curr_taggedNLP[cnt2222];
					  		 }
					  		 catch(Exception e){
					  		 }
					  		 
					  		 if(sentence_==null || sentence_.equalsIgnoreCase("")){
					  			 cnt2222++;
					  			 continue;
					  		 }
					  		 
					  		//CHECKSUM  (SENTENCE-LEVEL)
							checksum_of_curr_document=Calc_checksum.calc_checksum_for_string(sentence_);
							//COMPRESS -- compress all sentences of given document.
							String compressedSentences_of_curr_doc=Clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters(sentence_, true);
							double curr_sente_sentiment=0.0;
							
							//not IN REPOSITORY
							if(!map_repo_checksum_score.containsKey(checksum_of_curr_document) &&
							   !map_repo_checksum_compressedSentence.containsKey(compressedSentences_of_curr_doc )){
								   // 0:very negative, 1:negative 2:neutral 3:positive and 4:very positive. 
						  		   //sentiment find_sentiment_stanford_OpenNLP
						  		   curr_sente_sentiment=Find_sentiment_stanford_OpenNLP.find_sentiment_stanford_OpenNLP_returnScore(sentence_);
						  		 
									//System.out.println("curr_doc_curr_sent_tagged: "+curr_doc_curr_sent_tagged);
									writer_global_repository_to_save_sentenceWise_sentiments.append(
																				  "\n" +checksum_of_curr_document+"!!!"+curr_sente_sentiment+"!!!"
																				  +sentence_+"!!!"+compressedSentences_of_curr_doc//convert to lower-case
																				  );
									writer_global_repository_to_save_sentenceWise_sentiments.flush();
									
							}
					  		else{
								System.out.println(" FOUND ......lineNo:"+   seq);
					  			if(map_repo_checksum_score.containsKey(checksum_of_curr_document)){
					  				curr_sente_sentiment=Double.valueOf(map_repo_checksum_score.get(checksum_of_curr_document));
					  			}
					  			else if(map_repo_checksum_compressedSentence.containsKey(compressedSentences_of_curr_doc) ){
					  				curr_sente_sentiment=Double.valueOf(map_repo_checksum_compressedSentence.get(compressedSentences_of_curr_doc));
					  			}
					  		}
							  
					  		 if(conc_currLine_eachSentenc_sentiment.length()==0)
					  			conc_currLine_eachSentenc_sentiment=String.valueOf(curr_sente_sentiment);
					  		  else
					  			conc_currLine_eachSentenc_sentiment=conc_currLine_eachSentenc_sentiment+","+curr_sente_sentiment;
					  		
					  		 //System.out.println("sentiment -> "+curr_sente_sentiment+ " "+_currLine_nouns +" lineNumber:"+seq);
					  		 
					  		 // 
					  		 if(map_Sentiment_Count.containsKey(curr_sente_sentiment)){
					  			int t=map_Sentiment_Count.get(curr_sente_sentiment)+1;
					  			map_Sentiment_Count.put(curr_sente_sentiment , t);
					  		 }
					  		 else{
					  			map_Sentiment_Count.put(curr_sente_sentiment , 1); 
					  		 }
					  		 cnt2222++;
					  	} // END while(cnt2222<arr_curr_bodyText_without_NLP.length){
					  	
//					  	System.out.println(" s.len :"+s.length +" token_containing_bodyText_withoutTagging:"+token_containing_bodyText_withoutTagging 
//					  						+"--->"+currLine);
					  	System.out.println("text---->"+s[token_containing_bodyText_withoutTagging-1]+"\n "+classifier);
					  	
						 //LinkedHashMap<String, LinkedHashSet<String>>
						 //Output: TreeMap<Integer,TreeMap<Integer,String>> --> <category_id,<seq_id,value>>
					  	 /*** <category_id=map_value>->TreeMap<Integer,TreeMap<Integer,String>> --> <category_id,<seq_id,value>>
						 * 			<category_id=map_value>->{1=map_person, 2=map_organization, 3=map_location, 11=map_person_begin,22=map_organization_begin, 33=map_location_begin,
						 * 			, 111=map_person_end,111111-><seq,consectivePersonName!!!lineNo>
						 * 			222=map_organization_end, 333=map_location_end, 1111=map_person_CSV,11111=map_person_consecutiveTokens
						 * 			,2222=map_organization_CSV,22222=map_organization_consecutiveTokens, 222222-><seq,consectiveOrganizationName !!!lineNo>
						 *  		3333=map_location_CSV, 33333=map_location_consecutiveTokens, 333333-> <seq,consectiveLocationName!!!lineNo> 
					  	 ****/
					  	
					  	if(s[token_containing_bodyText_withoutTagging-1]==null){
					  		count_empty_bodyTEXT++;
					  		continue;
					  	}
					  	
					  	count_passed_after_filters++;
					  	
					  	 // GET location, person, and organization..
						 map = StanfordNER.identifyNER(  s[token_containing_bodyText_withoutTagging-1],
														"/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.conll.4class.distsim.crf.ser.gz",
														classifier,
														true, // is_override_with_model2
														false, //isSOPprint
														baseFolder+"debug.txt",
														false //is_write_to_debugFile
														);
						 
						 if(map.containsKey(111 )){ ////////////person
							 currLine=currLine+"@@@@"+map.get(1);
							 
							 //get the begining location  
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 //
						 if(map.containsKey(222)){////////////organi (organization)
							 currLine=currLine+"@@@@"+map.get(2);
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 if(map.containsKey(333)){////////////locat (location)
							 currLine=currLine+"@@@@"+map.get(3);
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 
						 
						 // find_count_number_words_with_specific_tag
						 TreeMap<Integer,String> map_IS_numbers= MyAlgo.find_count_number_words_with_specific_tag(  
								 																			curr_NLP_string,
								 																			stemmer,
								 																			map_Word_StemmedWord,
								 																			"_C"); // 
						 //number
						 if(map_IS_numbers.size()>0){
							 currLine=currLine+"@@@@"+map_IS_numbers;
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 //NOUN
						 if(_currLine_nouns.length() >0){
							 currLine=currLine+"@@@@"+_currLine_nouns;
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 
						 //sentiment
						 if(map_Sentiment_Count.size() > 0){
							 currLine=currLine+"@@@@"+map_Sentiment_Count;
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 
						 //verb
						 if(_currLine_verb.length() >0){
							 currLine=currLine+"@@@@"+_currLine_verb;
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 
						 //conc  each sentence sentiment
						 if(conc_currLine_eachSentenc_sentiment.length() >0){
							 currLine=currLine+"@@@@"+conc_currLine_eachSentenc_sentiment;
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 
						 ///////////START/////////////////// get concatenated tokens for person, organization , location
						 if(map.containsKey(11)){ //person
							 currLine=currLine+"@@@@"+map.get(11111);
							 //get the begining location  
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 //
						 if(map.containsKey(22)){//organi (organization)
							 currLine=currLine+"@@@@"+map.get(22222);
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 if(map.containsKey(33)){//locat (location)
							 currLine=currLine+"@@@@"+map.get(33333);
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 
						 //******* we need lineno : we get 
						 // <seq,consecutivePerson!!!LineNo>, <seq,consecutiveOrganization!!!LineNo>,<seq,consecutiveLocation!!!LineNo>
						 
						 if(map.containsKey(11)){ //person
							 currLine=currLine+"@@@@"+map.get(111111);
							 //get the begining location  
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 //
						 if(map.containsKey(22)){//organi (organization)
							 currLine=currLine+"@@@@"+map.get(222222);
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 if(map.containsKey(33)){//locat (location)
							 currLine=currLine+"@@@@"+map.get(333333);
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 
						 
						 //******* we need lineno : we get --only lines having quotes ""
						 // <seq,consecutivePerson!!!LineNo>, <seq,consecutiveOrganization!!!LineNo>,<seq,consecutiveLocation!!!LineNo>

						 if(map.containsKey(11)){ //person
							 currLine=currLine+"@@@@"+map.get(1111111);
							 //get the begining location  
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 //
						 if(map.containsKey(22)){//organi (organization)
							 currLine=currLine+"@@@@"+map.get(2222222);
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 if(map.containsKey(33)){//locat (location)
							 currLine=currLine+"@@@@"+map.get(3333333);
						 }
						 else
							 currLine=currLine+"@@@@"+"d";
						 
					    /////////END/////////////////// get concatenated tokens for person, organization , location
					
						 System.out.println("text:"+ s[token_containing_bodyText_withoutTagging-1]);
						 
						 //writer.append(_currLine_verb+"\n");
						 writer.append(currLine+"\n");
						 writer.flush();
				  }
				  
				  System.out.println("-->  count_passed_after_filters:"+count_passed_after_filters +" out of map_inFile_eachLine.size:"+map_inFile_eachLine.size());
				  System.out.println("NOTE:Output of below method is given as input to \"readFile_eachWord_of_given_ConsecutiveToken_apperance_get_as_CSV_LineNumber\" in \"readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber.java\"");
				  
				  System.out.println("note:OUTPUT tagged file:"+inFile+"_added_perso_origz_loc_verb"+Flag_type+".txt");
				  System.out.println("--------------> ERROR: count_empty_bodyTEXT:"+count_empty_bodyTEXT);
				  System.out.println("LOADED : map_inFile_eachLine.SIZE:"+map_inFile_eachLine.size());
				  //
				  map_NAMECount_value.put("loadedDocs_R_Lines", map_inFile_eachLine.size());
				  map_NAMECount_value.put("count_empty_bodyTEXT", count_empty_bodyTEXT);
				  
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return map_NAMECount_value;
		}

	    //readFile_output_LINENOsCSV_having_quotes
		private static void readFile_output_LINENOsCSV_having_quotes(String baseFolder, 
																	 String inputFile,
																	 String delimiter_4_inputFile,
																	 int    index_having_bodyTEXT_in_inputFile,
																	 int    index_having_URL,
																	 String outputFile
																	 ) {
			// TODO Auto-generated method stub
			TreeMap<String, String > map_URL_lineNoHAVINGquotesCSV=new TreeMap<String, String>();
			int count_wrote=0;
			try{
				FileWriter writer=new FileWriter(outputFile);
				// inputFile
				TreeMap<Integer, String> map_lineNo_Line_inputFile=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
						readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
																		(  inputFile, 
																		   -1, // 	startline, 
																		   -1, //  	endline,
																		   " debug_label",
																		   false // isPrintSOP
																		);
				
				
				String curr_URL_="";
				String header=map_lineNo_Line_inputFile.get(1);
				//EACH LINE
				for(int seq:map_lineNo_Line_inputFile.keySet()){
					
					if(seq==1) continue;
					
					String eachLine=map_lineNo_Line_inputFile.get(seq);
					String[] arr_features_of_inputFile=eachLine.split(delimiter_4_inputFile);
					String curr_bodyTEXT=arr_features_of_inputFile[index_having_bodyTEXT_in_inputFile-1];
					//current lineNO (docID) already in inputFile
					curr_URL_=arr_features_of_inputFile[index_having_URL-1];
					
					String [] arr_curr_bodyTEXT_linesAfterDOTsplit=curr_bodyTEXT.split("\\.");
					
					System.out.println(" lineNo:"+seq+"<--->split lines len:"+arr_curr_bodyTEXT_linesAfterDOTsplit.length);
					
					String concLINEnoCSV="";
					
					//
					int c=0;
					while(c<arr_curr_bodyTEXT_linesAfterDOTsplit.length){
						//curr line having quoates?
						if(arr_curr_bodyTEXT_linesAfterDOTsplit[c].indexOf("\"")>0){
							
							if(concLINEnoCSV.length()==0){
								concLINEnoCSV=String.valueOf(c+1);
							}
							else{
								concLINEnoCSV+=","+String.valueOf(c+1);
							}
							
						}
						c++;
					} // END while(c<arr_curr_bodyTEXT_linesAfterDOTsplit.length){
					
					map_URL_lineNoHAVINGquotesCSV.put(curr_URL_, concLINEnoCSV);
					//reset
					concLINEnoCSV=""; 
				} //END for(int seq:map_lineNo_Line_inputFile.keySet()){
			
				 
				writer.append(header+"\n");
				
				//output writing
				for(String currURL:map_URL_lineNoHAVINGquotesCSV.keySet()){
					//
					if(currURL.indexOf("http")>=0){
						writer.append(currURL+"!!!"+map_URL_lineNoHAVINGquotesCSV.get(currURL)+"\n");
						writer.flush();
						count_wrote++;
					}
					else{
						System.out.println("ERROR: url does not have HTTP:"+currURL);
					}
				}
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			System.out.println("map_URL_lineNoHAVINGquotesCSV:"+map_URL_lineNoHAVINGquotesCSV.size()+" count_wrote:"+count_wrote
								);
//								+" "+map_URL_lineNoHAVINGquotesCSV);
		}
		
		// main
	    public static void main(String[] args) throws IOException {
	    	long t0 = System.nanoTime();
	    		// NOTE:
	    		//------
	    		// (1) "p18_MAIN_get_dupURL_N_domainNames()" ran to get file for variable "inFile".
	    		//     note: this needed only when automate ground truth OR preparing files for baselines such as perceptron, maxent
	    		// (2) run step  1 to step 4 , but run either step(2a) or Step (2b).. both together might not be necessary..

	    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    		//**************STEP 1: CAN RUN ONLY ONCE TO TAG <ORGANIZATION, PERSON, LOCATION>
		      //**************STEP 2(a):
				 // Loading Word and Stemmed_word  (below input file created when running tf-idf method  calc_tf_idf() )
		    	 // && (NOT NEEDED HERE) convert_DocID_WordID_Freq_To_another_format.convert_DocID_WordID_Freq_To_another_format_2()
		         // check (NOT NEEDED HERE) P18_get_dupURL_N_domainNames() flag==3 for example
	    	//-----------------------------STEP 2(a) (OR) STEP 2(b)-----------------------------
		    //**************STEP 2(b): <-- NOTE: this adds ORGANIZATION without breaking (CONSECUTIVE)..
	    	//--->Output of below method is given as input to "readFile_eachWord_of_given_ConsecutiveToken_apperance_get_as_CSV_LineNumber" in "readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber.java"
	    	//    using "wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati.graphOUTfile_4_tag_for_person_organiz_locati"
	    	
	    	//**************STEP 3: readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber
	    	
	    	//**************STEP 4: CUSTOMIZED: (TOKEN and ADJACENCY LIST (line numbers) )
			// These custom words are picked from WIKIPEDIA 
			// pick list of token (words) in first file. Look for match in a token of second file, and get the matched lines as CSV
	    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    	
	    		//**************STEP 1: CAN RUN ONLY ONCE TO TAG <ORGANIZATION, PERSON, LOCATION>
				TreeMap<Integer,String> mapOut=new TreeMap<Integer, String>();
				// need manual change
		    	// CAN RUN ONLY ONCE TO TAG <ORGANIZATION, PERSON, LOCATION>
		    	String baseFolder="";
		    	baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds2/final/ds2_30000/";
		    	//P18
		    	baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/";
		    	//mac mini 
		    	baseFolder="/Users/lenin/Dropbox/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/";
		    	//p18 (first half labelling done, kept input here , which is NLP TAGGED) <-------------
		    	baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/tmp/";
		    	//p8
//		    	baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/";
		    	//NOTE: This file has bodyText and taggedText also (got by running "Convert_For_GBAD.createGBADgraphFromTextUsingNLP")
		    	// NOTE: EXTERNAL LINE NUMBER from "inFile" variable is used to output for *_LINENO_CSV files
		    	// below file has <URL!!!bodyText_withNLP!!!bodyText_withoutNLP> <-------------
		    	String inFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.dummy.all.txt";
		    		   inFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.dummy.all.txt";
		    	// input file for p18 problem
		    		   // this file has 39xx total docs
		    		   inFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all.txt";
		    		   // this file has subset of above 
		    		   inFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED___ENGLISH__LEN_50_FILTER__PASSED_FILTER__url_tagline_untagline.3all__added_perso_origz_loc_verb2_done__added##GroundTruth.txt";
		    			//below ground truth is NOW.. replaced with human annotated GROUND TRUTH as ..************2187 docs********
		    		   inFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED___ENGLISH__LEN_50_FILTER__PASSED_FILTER__url_tagline_untagline.3all__added_perso_origz_loc_verb2_done__added##GroundTruth.txt";
		    		   // out of first 1000 news, removed irrelevant, fixed confused to be labeled articles by Maitrayi	
		    		   inFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txtNEW_1000labelled_confCorrected.txt_1st1000ln_removeIrrelevaLABEL.txt_bTextNLPtagged.txt";
		    		   //p18 - above file name renamed as below for smaller file name , otherwise longer file name error.. 
		    		   inFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt.3all_p18.txtdded_perso_origz_loc_verb2.txt";
		    		   //p18 - 		    		 
		    		   inFile=baseFolder+"O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged.txt";
		    		   inFile=baseFolder+"O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged_onlyAfricanAmerican.txt";
		    		   //p8
		    		   inFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all_new.txt";
		    		   
		        // input File
//				String FirstFile=baseFolder+"O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged.txt_added_perso_origz_loc_verb2.txt";
		    		   
		    	////////////////////////////
		    	int run_Flag=2; //<-------------------set the RUN FLAG
		    		   
		    	String baseFolder_out=baseFolder;
		    	// comment/uncomment as required
		    	if(run_Flag==1){
		    		//
					mapOut=Calc_tf_idf.calc_tf_idf( baseFolder,
													baseFolder_out,
													inFile, 
													"!!!", //delimiter
													9 , //token_index_having_text,
													inFile+"_TF_IDF.txt",
													true, //is_do_stemming, //is_do_stemming_on_word
													false, // is_ignore_stop_words_all_together,
													false //isSOPprint
													);
		    	}
		    	
		      //**************STEP 2(a):
				 // Loading Word and Stemmed_word  (below input file created when running tf-idf method  calc_tf_idf() )
		    	 // && (NOT NEEDED HERE) convert_DocID_WordID_Freq_To_another_format.convert_DocID_WordID_Freq_To_another_format_2()
		         // check (NOT NEEDED HERE) P18_get_dupURL_N_domainNames() flag==3 for example
		     TreeMap<String, String> map_Word_StemmedWord=new TreeMap<String, String>();
			  String infile2=inFile+"_TF_IDF.txt"+"_Word_stemmedWord.txt";
//			  map_Word_StemmedWord=LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE(
//					  																											 infile2,
//															  																	 "!!!",
//															  																	  "1,2", // index_of_key_AND_value_as_CSV
//															  																	  "", // append_suffix_at_each_line
//															  																	  false, //is_have_only_alphanumeric
//															  																	  false //isPrintSOP
//															  																	 );
			  
	    	String inFolder_semantic_REPOSITORY="/Users/lenin/Downloads/#repository/p18.sentiment/";
//	    	 	   inFolder_semantic_REPOSITORY="/Users/lenin/Downloads/#repository/p8.sentiment/";
	    	int Flag_type=1;
	    	
	    	// Flag_type={1=works as usual(past),2="add extra Organization (without token breaking) "}
	    	// comment/uncomment as required
	    	// graphOUTfile_4_tag_for_person_organiz_locati ( create a graph type output as set of files )
//	    	wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati.
//	    	graphOUTfile_4_tag_for_person_organiz_locati( baseFolder,
//					    								  inFile,
//					    								  "!#!#",
//					    								  3, //TOKEN without NLP tagging
//					    								  2, //TOKEN with NLP tagging
//					    								  true, // is_having_header
//														  map_Word_StemmedWord,
//														  inFolder_semantic_REPOSITORY, //REPOSITORY
//														  "p18", //prefix_for_problem
//														  Flag_type // <----------------Flag_type=1
//					    								 );
	    	 //-----------------------------STEP 2(a) (OR) STEP 2(b)-----------------------------
		    //**************STEP 2(b): <-- NOTE: this adds ORGANIZATION without breaking (CONSECUTIVE)..
	    	//--->Output of below method is given as input to "readFile_eachWord_of_given_ConsecutiveToken_apperance_get_as_CSV_LineNumber" in "readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber.java"
	    	//    using "wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati.graphOUTfile_4_tag_for_person_organiz_locati"
	    	// comment/uncomment as required
	    	 	Flag_type=2; //<---- Resetting..( 2="add extra Organization (without token breaking)" )
	    	 	//COMMENT / uncomment
	    	    // Flag_type=2 <----> wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati
	    	 	if(run_Flag==2){
			    	 // Flag_type=2 <----> wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati
	    	 		TreeMap<String, Integer> map_Name_Value=
						    	Wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati.
						    	graphOUTfile_4_tag_for_person_organiz_locati( baseFolder,
										    								  inFile,
										    								  "!!!",
										    								  9, //TOKEN without NLP tagging
										    								  11, //TOKEN with NLP tagging
										    								  true, // is_having_header
																			  map_Word_StemmedWord,
																			  inFolder_semantic_REPOSITORY, //REPOSITORY
																			  "p8", //prefix_for_problem
																			  Flag_type // <----------------Flag_type=2
										    								 );
	    	 		System.out.println("---->map_Name_Value:"+map_Name_Value);
	    	 	}
	    	
	    	//**************STEP 3: readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber
			String 	delimiter_to_be_used_to_append_in_OUTfile="";
			// ************************* START ************************ 
			// need manual change
	    	//CAN RUN ONLY ONCE TO TAG <ORGANIZATION, PERSON, LOCATION>
	    	baseFolder=baseFolder;
	    	//THIS IS output from Flag==2 
			String inputFile=inFile+"_added_perso_origz_loc_verb"+Flag_type+".txt";
			
			String out_File =""; 
			// PreREQUISTION: For numeric, (1)  is_given_token_interested_a_mapString=false, and (2) token_for_taggedNLP_in_first_File is IMPORTANT
			// delimiter_to_be_added_to_input_File-> FOR taggedNLP text use "!#!#" , otherwise use @@@@
			//*(********* BELOW ARE MOSTLY FIXED*********/
			String delimiter_to_be_added_to_input_File="";
			delimiter_to_be_used_to_append_in_OUTfile="!!!";
			String delimiter_for_first_File_2_split="@@@@"; //switch between {!#!#,@@@@}
			int run_for_top_N_lines=-1;
			boolean is_YES_only_to_WORDNET_words=true;
			boolean is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words=false;
			boolean	is_YES_INTRESTED_only_NUMERIC_tokens=false;
			boolean is_YES_skip_first_or_second_char_NUMERIC_tokens=true;
			int 	token_with_taggedNLP_in_first_File=10; // 10 or 11 
			int		token_for_EXTERNAL_lineNO_in_first_File=1;
			//RUN ALL BELOW SETTINGS IN A LOOP
			//PERSON(2)->is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words=true;is_given_token_interested_a_mapString=true,is_YES_skip_first_or_second_char_NUMERIC_tokens=true
			//ORGANIZ(3)-> same as PERSON
			//LOCATION(4)->is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words=true;is_given_token_interested_a_mapString=true,is_YES_skip_first_or_second_char_NUMERIC_tokens=true
			//NOUN(token=6)->inSynsetType=NOUN,is_YES_only_to_WORDNET_words=true;is_given_token_interested_a_mapString=false,is_YES_skip_first_or_second_char_NUMERIC_tokens=true
			//VERB(token=8)->inSynsetType=VERB,is_YES_only_to_WORDNET_words=true;is_given_token_interested_a_mapString=false,is_YES_skip_first_or_second_char_NUMERIC_tokens=true
			//NUMBERS(5)->token_for_taggedNLP_in_first_File=2;is_given_token_interested_a_mapString=false
			//DONT FORGET - CHANGE OUTPUT FILE NAME ABOVE
				boolean is_given_token_interested_a_mapString=false;
				String[] arr_feature_name=new String[] {"PERSON", "NOUN", "ORGANIZ"
											   		   ,"LOCATION","NUMBERS","VERB"};
				
				//overwritten for p18 problem
				arr_feature_name=new String[] { "PERSON", "NOUN", "ORGANIZ","LOCATION","NUMBERS","VERB","ConsecutivePerson","ConsecutiveOrganization","ConsecutiveLocation",
												"ConsecutivePersonNlineNo","ConsecutiveOrganizationNlineNo","ConsecutiveLocationNlineNo",
												"ConsecutivePersonNlineNoWITHquotes","ConsecutiveOrganizationNlineNoWITHquotes","ConsecutiveLocationNlineNoWITHquotes"};
				
				int SIZE_of_arr_feature_name=arr_feature_name.length;
				int[] 	 arr_index_of_feature_name=new int[] {2,6,3,4,5,8};
				//overwritten for p18 problem
				arr_index_of_feature_name=new int[] {2,6,3,4,5,8,10,11,12,13,14,15,16,17,18};
				/*
				 * 		 inFile2_index_for_interested_CONTEXT= INCREMENT BY index by 1 
		 		------------------------------------------------------------------------
				 index:9 arr10[c]:ConsecutivePerson index:10 arr10[c]:ConsecutiveOrganization index:11 arr10[c]:ConsecutiveLocation 
				 index:12 arr10[c]:ConsecutivePersonNlineNo index:13 arr10[c]:ConsecutiveOrganizationNlineNo index:14 arr10[c]:ConsecutiveLocationNlineNo
				 index:15 arr10[c]:ConsecutivePersonNlineNoWITHquotes index:16 arr10[c]:ConsecutiveOrganizationNlineNoWITHquotes index:17 arr10[c]:ConsecutiveLocationNlineNoWITHquotes
				*/
				
				int max=arr_feature_name.length;
				FileWriter writerDebug=new FileWriter(new File(baseFolder+"debug.txt"));
				writerDebug.append("Below are the files... \n");
				int cnt=0;
				System.out.println("cnt:"+cnt +" SIZE_of_arr_feature_name-1:"+(SIZE_of_arr_feature_name-1));
				// each feature iterate
				while(cnt < (SIZE_of_arr_feature_name) ){
					//************** BELOW ARE MOSTLY DYNAMIC*********START/
					String  curr_prefix_of_a_feature=arr_feature_name[cnt];
					//take interested feature index 
					int 	token_interested_in_first_File=arr_index_of_feature_name[cnt];
					String  inSynsetType="VERB"; //{NOUN,VERB};
					// person / organization/ .....
					if(arr_feature_name[cnt].equalsIgnoreCase("PERSON")  ){
						is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words=true;
						is_given_token_interested_a_mapString=true;
						is_YES_skip_first_or_second_char_NUMERIC_tokens=true;
						inSynsetType="VERB";
					}
					else if(arr_feature_name[cnt].equalsIgnoreCase("ORGANIZ")  ){
						is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words=true;
						is_given_token_interested_a_mapString=true;
						is_YES_skip_first_or_second_char_NUMERIC_tokens=true;
						inSynsetType="VERB";
					}
					else if(arr_feature_name[cnt].equalsIgnoreCase("LOCATION")  ){
						is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words=true;
						is_given_token_interested_a_mapString=true;
						is_YES_skip_first_or_second_char_NUMERIC_tokens=true;
						inSynsetType="VERB";
					}
					else if(arr_feature_name[cnt].equalsIgnoreCase("NUMBERS")  ){
//						token_with_taggedNLP_in_first_File=2; //this may be obsolete when "is_YES_INTRESTED_only_NUMERIC_tokens==false"
						is_given_token_interested_a_mapString=false;
					}
					else if(arr_feature_name[cnt].equalsIgnoreCase("NOUN")  ){
						inSynsetType="NOUN";
						is_YES_only_to_WORDNET_words=true; // true for proper noun filtering
						is_given_token_interested_a_mapString=false;
						is_YES_skip_first_or_second_char_NUMERIC_tokens=true;
						
					}
					else if(arr_feature_name[cnt].equalsIgnoreCase("VERB")  ){
						inSynsetType="VERB";
						is_YES_only_to_WORDNET_words=true;
						is_given_token_interested_a_mapString=false;
						is_YES_skip_first_or_second_char_NUMERIC_tokens=true;
					}
					else if(arr_feature_name[cnt].equalsIgnoreCase("ConsecutivePerson") ||arr_feature_name[cnt].equalsIgnoreCase("ConsecutiveOrganization") ||arr_feature_name[cnt].equalsIgnoreCase("ConsecutiveLocation") ||
							arr_feature_name[cnt].equalsIgnoreCase("ConsecutivePersonNlineNo") ||arr_feature_name[cnt].equalsIgnoreCase("ConsecutiveOrganizationNlineNo") ||arr_feature_name[cnt].equalsIgnoreCase("ConsecutiveLocationNlineNo") ||
							arr_feature_name[cnt].equalsIgnoreCase("ConsecutivePersonNlineNoWITHquotes") ||arr_feature_name[cnt].equalsIgnoreCase("ConsecutiveOrganizationNlineNoWITHquotes") ||arr_feature_name[cnt].equalsIgnoreCase("ConsecutiveLocationNlineNoWITHquotes") 
							){
						is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words=false;
						is_YES_only_to_WORDNET_words=false;
						is_given_token_interested_a_mapString=true;
						is_YES_skip_first_or_second_char_NUMERIC_tokens=true;
						inSynsetType="VERB";
					}
					
//					"ConsecutivePerson","ConsecutiveOrganization","ConsecutiveLocation",
//					"ConsecutivePersonNlineNo","ConsecutiveOrganizationNlineNo","ConsecutiveLocationNlineNo",
//					"ConsecutivePersonNlineNoWITHquotes","ConsecutiveOrganizationNlineNoWITHquotes","ConsecutiveLocationNlineNoWITHquotes"
					
					System.out.println("RUNNING FOR "+inSynsetType);
					//for ConsecutiveTOKENS run method "readFile_eachWord_of_given_ConsecutiveToken_apperance_get_as_CSV_LineNumber" instead below from "readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber.java"
					// output
					out_File =inFile + "_"+curr_prefix_of_a_feature+"_LINENO.txt";
					// comment/uncomment as req
		    	 	if(run_Flag==3){
				    	// NOTE: EXTERNAL LINE NUMBER from "inFile" variable is used to output for *_LINENO_CSV files
		    	 		
		    	 		//DEBUG.. LATER COMMENT THIS BELOW LINE
//		    	 		if(arr_feature_name[cnt].equalsIgnoreCase("NUMBERS")){
		    	 		
						//readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String
						crawler.
						ReadFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber.
						readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber(
																						baseFolder,
																						inputFile,
																						delimiter_for_first_File_2_split, //delimiter_for_input_File_2_split,
																						delimiter_to_be_added_to_input_File, //delimiter_to_be_added_to_input_File,
																						token_interested_in_first_File, //token_interested_in_first_File,//<-PERSON,ORGANIZ,LOCATION ETC CHANGE HERE																				
																						token_with_taggedNLP_in_first_File, //token_for_taggedNLP_in_first_File
																						token_for_EXTERNAL_lineNO_in_first_File, //token_for_EXTERNAL_lineNO_in_first_File
																						is_given_token_interested_a_mapString,//**is_given_token_interested_a_mapString **
																						is_YES_only_to_WORDNET_words,  // is_YES_only_to_WORDNET_words
																						is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words, // is_YES_only_to_NOT_AVAILABLE_in_WORDNET_words
																						inSynsetType, //**** inSynsetType={NOUN,VERB}**
																						is_YES_skip_first_or_second_char_NUMERIC_tokens,//is_YES_skip_first_or_second_char_NUMERIC_tokens
																						is_YES_INTRESTED_only_NUMERIC_tokens, //is_YES_INTRESTED_only_NUMERIC_tokens
																						delimiter_to_be_used_to_append_in_OUTfile,//delimiter_to_be_used_to_append_in_OUTfile,
																						out_File,
																						run_for_top_N_lines, //run_for_top_N_lines
																						false, //is_Append_outFile
																						true, //is_header_present
																						arr_feature_name[cnt] 	// {{ "PERSON", "NOUN", "ORGANIZ","LOCATION","NUMBERS","VERB","ConsecutivePerson","ConsecutiveOrganization","ConsecutiveLocation",
																												// "ConsecutivePersonNlineNo","ConsecutiveOrganizationNlineNo","ConsecutiveLocationNlineNo",
																												// "ConsecutivePersonNlineNoWITHquotes","ConsecutiveOrganizationNlineNoWITHquotes","ConsecutiveLocationNlineNoWITHquotes"};}
																					    );
						//
						writerDebug.append( out_File +"\n");
						writerDebug.flush();
						
//		    	 		}
		    	 	}
		    	 	System.out.println("arr_feature_name[cnt]:"+arr_feature_name[cnt]+" out_File:"+out_File);
		    	 	
					cnt++;
					System.out.println("GIVEN inputFile:"+inputFile);
			}
				
				
		    	//**************STEP 3(b): readFile_output_LINENOsCSV_having_quotes
				
	    	 	if(run_Flag==3){
					//readFile_output_LINENOsCSV_having_quotes
					readFile_output_LINENOsCSV_having_quotes(
															baseFolder,
															inputFile,
															"!!!", // delimiter_4_inputFile,
															9, //index_having_bodyTEXT_in_inputFile
															5, //index_having_URL
															inputFile+"_COMMA_LINENO_CSV.txt" // OUTPUT
															);
	    	 	}
				
		    	//**************STEP 3: readFile_output_LINENOsCSV_having_quotes
				
		    	//**************STEP 4: CUSTOMIZED: (TOKEN and ADJACENCY LIST (line numbers) )
				// These custom words are picked from WIKIPEDIA 
				// pick list of token (words) in first file. Look for match in a token of second file, and get the matched lines as CSV 
				
				// STEP 4************************* START ************************
				String  first_File=baseFolder+"customwords_4_lineCSV_2searchin2ndfile.txt";
				   	     delimiter_for_first_File_2_split="!!!";
				int	    token_interested_in_first_File=1;
				String  Second_File=inFile; // from step 1 input
				String  delimiter_for_second_File_2_split="!#!#";
				int	    token_interested_in_second_File=3;
				boolean is_HeaderPresentIn_first_file=false;
				// comment/uncomment
				// readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber_CUSTOMIZED
//				crawler.
//				readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber.
//				readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber_CUSTOMIZED(
//																				  baseFolder,
//																				  first_File,
//																				  delimiter_for_first_File_2_split,
//																				  token_interested_in_first_File,
//																				  Second_File,
//																				  delimiter_for_second_File_2_split,
//																				  token_interested_in_second_File,
//																				  is_HeaderPresentIn_first_file
//																			  );
				
				// STEP 4************************* END ************************
				
				//************** BELOW ARE MOSTLY DYNAMIC*********END/
				
				System.out.println("*** just ran for run_Flag="+run_Flag);
				System.out.println( "Time Taken (FINAL ENDED):"+ NANOSECONDS.toSeconds(System.nanoTime() - t0)+ " seconds; "
									+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
									+ " minutes");
				
	    } //main


	    
	    
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