package p18;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

import crawler.Stemmer;
import crawler.Crawler;
import crawler.Find_domain_name_from_Given_URL;
import crawler.Load_domainname_pattern4crawledHTML;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import crawler.Stopwords;

//
public class Calc_average_document_length {
	
	// remove_NON_ENGLISH_newsarticles 
	// file1 -> file having URL and other column such as bodyText
	public static int calc_average_document_length(
														String  baseFolder,
														String  file1,
														String delimiter_4_file1,
														int 	token_having_bodyTEXT,
														String  OUTPUT_file3, //out
														boolean isSOPprint
													   ){
		int total_words_count_in_allDoc_include_stopwords=0;
		FileWriter writer =null;
		FileWriter writerDebug =null;
 
		try {
			writer=new FileWriter(new File(OUTPUT_file3));
			writerDebug=new FileWriter(new File(baseFolder+"debug_avgLength.txt"));
			 		
			TreeMap<Integer, String> map_seq_bodyTEXT=new TreeMap<Integer, String>();
			// map_file1
			TreeMap<Integer,String> map_file1=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								  file1,
																	 							  -1, //startline, 
																	 							  -1, //endline,
																	 							  "f1 ", //debug_label
																	 							  false //isPrintSOP
																							 	  );
			System.out.println("map_file1.size:"+map_file1.size()+" file:"+file1);
			int count_above_threshold=0;
			// in paper used 8433 news articles
			for(int seq:map_file1.keySet()){
				String eachLine=map_file1.get(seq);
				//
				String []s = eachLine.split(delimiter_4_file1);
				
				if(seq==1) continue; //header
				
				//
				if(s.length>=token_having_bodyTEXT){
					//clean URL
//					if(s[token_having_bodyTEXT-1].indexOf("url=")>=0)
//						url = clean_embeddedURLs.clean_get_embeddedURL( s[1].replace("new():", ""));
//					else
//						url = s[token_having_URL-1].replace("new():", "");
				
					String curr_bodyTEXT=s[token_having_bodyTEXT-1];
					
					curr_bodyTEXT=
							curr_bodyTEXT.replace(",", " ").replace("’", " ").replace("‘", " ").replace("(", " ").replace("—", " ")
									.replace(" what ", " ").replace(" why ", " ").replace(" when ", " ")
									.replace(" and ", " ").replace(" or ", " ").replace(" of ", " ").replace(" for ", " ").replace(" and ", " ")
									.replace(" in ", " ")
									.replace(" than ", " ").replace(" then ", " ").replace(" are ", " ").replace(" is ", " ")
									.replace(" another ", " ").replace(" while ", " ").replace(" you ", " ").replace(" did ", " ")
									.replace(" didnt ", " ").replace(" didn ", " ").replace(" there ", " ")
									.replace(" i ", " ").replace(" he ", " ").replace(" she ", " ").replace(" they ", " ")
									.replace(" them ", " ").replace(" but ", " ").replace(" themselves ", " ").replace(" on ", " ")
									.replace(" has ", " ").replace(" have ", " ").replace(" to ", " ").replace(" in ", " ")
									.replace(" where ", " ").replace(" whether ", " ").replace(" only ", " ")
									.replace("`", " ").replace(" which ", " ").replace(" back ", " ").replace(" their ", " ")
									.replace(" a ", " ").replace(" an ", " ").replace(" below ", " ").replace(" above ", " ")
									.replace(" these ", " ").replace(" with ", " ").replace(" the ", " ").replace(" by ", " ")
									.replace(" was ", " ").replace(" were ", " ").replace(" that ", " ").replace(" this ", " ")
									.replace(")", " ").replace("?", " ").replace(";", " ").replace("'", " ").replace("!", " ")
									.replace(".", "  ").replace("”", " ").replace("–", " ").replace("©", " ").replace("…", " ")
									.replace("… ", " ").replace("�", " ").replace("[", " ").replace("]", " ")
									.replace("“", " ").replace("-", " ").replace("\t", " ").replace("   ", " ")
									.replace("\"", " ").replace("   ", " ").replace("  ", " ");
					
//					if(curr_bodyTEXT.split(" ").length<70) //for p8 problem
//						continue;
					 
					map_seq_bodyTEXT.put(seq, curr_bodyTEXT);
					
//					System.out.println("seq:"+seq+" txt:"+curr_bodyTEXT);
					
				}
				
//				if(map_seq_bodyTEXT.size()==8433) //only for p8 problem
//					break;
//				if(map_seq_bodyTEXT.size()==10000) //only for p8 problem
//					break;
			} //
			
			System.out.println("LOADED map_seq_bodyTEXT.SIZE:"+map_seq_bodyTEXT.size()+" delimiter_4_file1:"+delimiter_4_file1);
			
			TreeMap<String, String> map_uniqueSTEMMEDword_asKEY=new TreeMap<String, String>();
			TreeMap<Integer, TreeMap<String,String>> map_docID_N_uniqueStemmedTokensASkey=new TreeMap<Integer, TreeMap<String,String>>();
			//
			for(int seq:map_seq_bodyTEXT.keySet()){
				
				if(seq==1) continue; //header
				
				int doc_id=seq;
				
				System.out.println("doc_id:"+doc_id);
				
				String [] arr_words=map_seq_bodyTEXT.get(seq).toLowerCase().split(" ");
				int c=0;
				//
				while(c<arr_words.length){
					
					if(arr_words[c].indexOf("beginindexis")>=0|| arr_words[c].indexOf("containe")>=0
							|| arr_words[c].indexOf("&")>=0|| arr_words[c].indexOf("brm")>=0
							|| arr_words[c].indexOf("with")>=0|| arr_words[c].indexOf("this")>=0|| arr_words[c].indexOf("the")>=0
							|| arr_words[c].indexOf("for")>=0|| arr_words[c].indexOf("and")>=0
							)
					{   c++;
						continue;
					}
					 //
					 if(arr_words[c].length()<=14){
						 total_words_count_in_allDoc_include_stopwords+=1;
					 }
					//
					if(Stopwords.is_stopword(arr_words[c])==false && arr_words[c].length()>2){
						//
						String stemmed_word=Stemmer.stem_2(arr_words[c]);
						
						if(stemmed_word.length()<=2){
							 c++;
							continue;
						}
						
						if(stemmed_word.indexOf("beginindexis")>=0|| stemmed_word.indexOf("containe")>=0|| stemmed_word.indexOf("when")>=0
								|| stemmed_word.indexOf("&")>=0|| stemmed_word.indexOf("brm")>=0
								|| stemmed_word.indexOf("with")>=0|| stemmed_word.indexOf("this")>=0
								|| stemmed_word.indexOf("the")>=0 || stemmed_word.indexOf("what")>=0
								|| stemmed_word.indexOf("for")>=0|| stemmed_word.indexOf("and")>=0
								|| stemmed_word.indexOf("ifyou")>=0|| stemmed_word.indexOf("ifimprud")>=0
								|| stemmed_word.indexOf("ifuncharacterist")>=0|| stemmed_word.indexOf("ifil")>=0
								|| stemmed_word.indexOf("makesgood")>=0|| stemmed_word.indexOf("makesno")>=0
								|| stemmed_word.indexOf("makesuch")>=0|| stemmed_word.indexOf("were")>=0
								|| stemmed_word.indexOf("from")>=0|| stemmed_word.indexOf("again")>=0
								|| ( (stemmed_word.indexOf("american")>=0) &&stemmed_word.length()>7)
								|| ( (stemmed_word.indexOf("capitalis")>=0) &&stemmed_word.length()>9)
								|| ( (stemmed_word.indexOf("carefully")>=0) &&stemmed_word.length()>9)
								|| ( (stemmed_word.indexOf("central")>=0) &&stemmed_word.length()>7)
								|| ( (stemmed_word.indexOf("expect")>=0) &&stemmed_word.length()>5)
								|| ( (stemmed_word.indexOf("express")>=0) &&stemmed_word.length()>7)
								|| ( (stemmed_word.indexOf("federal")>=0) &&stemmed_word.length()>7)
								|| ( (stemmed_word.indexOf("filth")>=0) &&stemmed_word.length()>5)
								|| ( (stemmed_word.indexOf("village")>=0) &&stemmed_word.length()>7)
								|| ( (stemmed_word.indexOf("violent")>=0) &&stemmed_word.length()>7)
								|| ( (stemmed_word.indexOf("which")>=0) &&stemmed_word.length()>5)
								|| ( (stemmed_word.indexOf("whose")>=0) &&stemmed_word.length()>5)
								|| ( (stemmed_word.indexOf("would")>=0) &&stemmed_word.length()>5)
								|| ( (stemmed_word.indexOf("which")>=0) &&stemmed_word.length()>5)
								|| ( (stemmed_word.indexOf("when")>=0) &&stemmed_word.length()>4)
								|| ( (stemmed_word.indexOf("women")>=0) &&stemmed_word.length()>5)
								|| ( (stemmed_word.indexOf("working")>=0) &&stemmed_word.length()>7)
								|| ( (stemmed_word.indexOf("have")>=0) &&stemmed_word.length()>4)
								|| ( (stemmed_word.indexOf("has")>=0) &&stemmed_word.length()>3)
								|| ( (stemmed_word.indexOf("how")>=0) &&stemmed_word.length()>3)
								|| ( (stemmed_word.indexOf("party")>=0) &&stemmed_word.length()>5)
								|| ( (stemmed_word.indexOf("party")>=0) &&stemmed_word.length()>5)
								|| ( (stemmed_word.indexOf("temperature")>=0) &&stemmed_word.length()>11)
								|| ( (stemmed_word.indexOf("centre")>=0) &&stemmed_word.length()>6)
								|| ( stemmed_word.indexOf("/")>0 )|| ( stemmed_word.indexOf(" ")>0 )
								|| ( stemmed_word.indexOf("#")>1 )|| ( stemmed_word.indexOf("@")>1 )
								)
						{   c++;
							continue;
						}
						//
						if(stemmed_word.length()<=14 ||
								( stemmed_word.indexOf("#")==0 )|| ( stemmed_word.indexOf("@")==0 )
								){ //p6 problem wehre two words often get concatenated causing noise
							//
							if(map_docID_N_uniqueStemmedTokensASkey.containsKey(doc_id)){
								TreeMap<String,String> map_stemmedWord_asKeY_temp=map_docID_N_uniqueStemmedTokensASkey.get(doc_id);
								map_stemmedWord_asKeY_temp.put(stemmed_word, "");
								map_docID_N_uniqueStemmedTokensASkey.put(doc_id, map_stemmedWord_asKeY_temp);
							}
							else{
								TreeMap<String,String> map_stemmedWord_asKeY_temp=new TreeMap<String, String>();
								map_stemmedWord_asKeY_temp.put(stemmed_word, "");
								map_docID_N_uniqueStemmedTokensASkey.put(doc_id, map_stemmedWord_asKeY_temp);
							}
							
							map_uniqueSTEMMEDword_asKEY.put(stemmed_word, "");
						}
						else{
							writerDebug.append(stemmed_word+"\n");
							writerDebug.flush();
						}
						
					} //if(stopwords.is_stopword(arr_words[c])==false){
					c++;
				}
				
				
			}
			
			//average 
			double average_document_length=0.;
			int total=0;
			//
			for(int currDocID:map_docID_N_uniqueStemmedTokensASkey.keySet()){
				
				if(currDocID==1) continue; //header
				
				total+=map_docID_N_uniqueStemmedTokensASkey.get(currDocID).size();
				
			}
			//
			
			System.out.println("average_document_length:"+average_document_length);
			System.out.println("map_docID_N_uniqueStemmedTokensASkey.size:"+map_docID_N_uniqueStemmedTokensASkey.size());
			
			average_document_length=total/map_docID_N_uniqueStemmedTokensASkey.size();
			System.out.println("average_document_length:"+average_document_length);
			System.out.println("map_seq_bodyTEXT.size:"+map_seq_bodyTEXT.size());
//		    System.out.println("map_docID_N_uniqueStemmedTokensASkey:"+map_docID_N_uniqueStemmedTokensASkey);
			System.out.println("map_uniqueSTEMMEDword_asKEY.size:"+map_uniqueSTEMMEDword_asKEY.size()+
								" map_seq_bodyTEXT.size:"+map_seq_bodyTEXT.size()+
								" average_document_length:"+average_document_length
//								+" map_uniqueSTEMMEDword_asKEY:"+map_uniqueSTEMMEDword_asKEY
								);
			writerDebug.append("\n map_uniqueSTEMMEDword_asKEY:"+map_uniqueSTEMMEDword_asKEY+"\n");
			writerDebug.flush();
		
			
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		return total_words_count_in_allDoc_include_stopwords;
	}
	

	// main
	public static void main(String[] args) throws Exception{
		//
		int 	total_words_count_in_allDoc_include_stopwords=0;
		int 	token_having_bodyTEXT=0; 
		String  baseFolder="";String  file1="";String delimiter_4_file1="";
		String Flag="p18"; // p18, p8, p6
				// 
				if(Flag.equalsIgnoreCase("p18")){
						// p18
						baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/tmp/";
				}
				else if(Flag.equalsIgnoreCase("p8")){
						//p8
						baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/";
				}
				else if(Flag.equalsIgnoreCase("p6")){
					//p6
					baseFolder="/Users/lenin/Dropbox/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds10/";
				}
				
				//"p18" header for file "_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb2_normalizeDelimiterAddedGTruthToken.txt"
				//"$url!#!#$body__NN!#!#$body!#!#$body__NN!#!#!#!#!#!#person_CSVMap!#!#organiz_CSVMap!#!#location_CSVMap!#!#Numbers_CSVMap!#!#Nouns!#!#SentimentMap!#!#Verb!#!#eachSentencSentimenCSV!#!#ConsecutivePerson!#!#ConsecutiveOrganization!#!#ConsecutiveLocation!#!#ConsecutivePersonNlineNo!#!#ConsecutiveOrganizationNlineNo!#!#ConsecutiveLocationNlineNo!#!#ConsecutivePersonNlineNoWITHquotes!#!#ConsecutiveOrganizationNlineNoWITHquotes!#!#ConsecutiveLocationNlineNoWITHquotes!#!#$fname"
				
				//file1
				if(Flag.equalsIgnoreCase("p18")){
					//	p18
					file1=baseFolder+"O___ENGLISH__LEN_50_FILTER__PASSED_FILTER__url_tagline_untagline.3all_p18__removeIrrelevaLABEL_NLPtagged_onlyAfricanAmerican__added_perso_origz_loc_verb2__added##GroundTruth.txt";
				}
				else if(Flag.equalsIgnoreCase("p8")){
					//p8
					file1=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb2_normalizeDelimiterAddedGTruthToken.txt";
				}
				else if(Flag.equalsIgnoreCase("p6")){
					//p6
					file1=baseFolder+"20000.txt";
				}
				//delimiter_4_file1
				if(Flag.equalsIgnoreCase("p8")){
					delimiter_4_file1="!#!#"; //p8
				}
				else if(Flag.equalsIgnoreCase("p18")){
					delimiter_4_file1="!!!"; //p18
				}
				else if(Flag.equalsIgnoreCase("p6")){
					delimiter_4_file1="!!!"; //p6
				}
				//token_having_bodyTEXT
				if(Flag.equalsIgnoreCase("p18")){
					token_having_bodyTEXT=9; // p18
				}
				else if(Flag.equalsIgnoreCase("p8")){
					token_having_bodyTEXT=3; //3 for p8,	
				}
				else if(Flag.equalsIgnoreCase("p6")){
					token_having_bodyTEXT=1; //3 for p6,	
				}
				
		String  OUTPUT_file3=baseFolder+"bfr_17feb_set2_OUTPUT_merged_full_bodyText_filter_"; //out
		boolean isSOPprint=false;
		
		System.out.println("Flag:"+Flag);
		
		//p8 - 8433 news articles -p8 - has average document length - 8433 docs - 115; 8431 docs ->123.0 - unique number of tokens - 51779
		//p18 - 
		// We calculate \emph{average document length}, which provides the average number of terms over all documents after removing stop words and applying stemming on these terms.
		// The \emph{average document length} of our corpus is 178. unique number of tokens-> 10287
		
		//calc_average_document_length
		total_words_count_in_allDoc_include_stopwords=	calc_average_document_length(
																				   		baseFolder,
																				   		file1,
																				   		delimiter_4_file1,
																				   		token_having_bodyTEXT,
																				   		OUTPUT_file3, //out
																				   		isSOPprint
																				   		);
		
		System.out.println("total_words_count_in_allDoc_include_stopwords:"+total_words_count_in_allDoc_include_stopwords);
		
		////////////////////BEGIN: calculates unique tokens for p6 - removed noise by hand..
		
//		TreeMap<Integer,String> map_file1=
//				readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
//																							  "/Users/lenin/Dropbox/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds10/debug_avgLength_backup.txt",
//																 							  -1, //startline, 
//																 							  -1, //endline,
//																 							  "f1 ", //debug_label
//																 							  false //isPrintSOP
//																						 	  );
//		
//		 System.out.println(" "+map_file1.get(1).split("=,").length);
//		 int count=0;
//		 String []arr= map_file1.get(1).split("=,");
//		 int c=0;
//		 while(c<arr.length){
//			 String orig=arr[c]=arr[c];
//			 arr[c]=arr[c].replace(" ", "");
//			 
//			 if(arr[c].length()<=12 ){
////				 if(orig.indexOf(" ")==-1)
//				 System.out.println(arr[c]);
//					 count++;
//			 }
//			 else{
////				 System.out.println(arr[c]);
//			 }
//			 c++;
//		 }
//		 System.out.println("count:"+count);
		 
		////////////////////END: calculates unique tokens for p6 - removed noise by hand..
		
//		System.out.println(stopwords.is_stopword("and"));
	}

	//p18- map_uniqueSTEMMEDword_asKEY.size:8980 map_seq_bodyTEXT.size:610 average_document_length:168.0
	
}
