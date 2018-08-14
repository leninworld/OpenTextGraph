package p8;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import crawler.DocumentParser_AND_Cosine;
import crawler.LoadMultipleValueToMap2_AS_KEY_VALUE;
import crawler.Calc_tf_idf;
import crawler.Convert_DocID_WordID_Freq_To_another_format;
import crawler.Convert_mapString_to_TreeMap;
import crawler.Cosine_similarity;
import crawler.Find_2_Map_word_not_exist_in_another;
import crawler.Generate_Random_Number_Range;
import crawler.IsNumeric;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import crawler.Sort_given_treemap;

// this takes input 
public class Calc_3_similarity {
	
	// input is like <word_id1:freq word_id2:freq>
	public static TreeMap<String,Double> convert_wordID_TFIDFString_into_TreeMap(String wordID_tfidf_string){
		TreeMap<String,Double> mapOut=new TreeMap<String, Double>();
		try{
			String [] arr_wordID_colon_freq = wordID_tfidf_string.split(" ");
			int cnt11=0;
			while(cnt11<arr_wordID_colon_freq.length){
				String[] wordID_freq=arr_wordID_colon_freq[cnt11].split(":");
				//int cnt22=0;
				//while(cnt22<wordID_freq.length){
				mapOut.put(wordID_freq[0], new Double( wordID_freq[1]));
				//	cnt22++;
				//}
				cnt11++;
			}
		}
		catch(Exception e){
			
		}
		return mapOut;
	}
	
	// (1) calc_cosine_similarity
	// (2) new words not found in previous set of documents (25/50 documents)
	// (3) new words with THRESHOLD
	// (4) JACCARD
	public static void calc_3_similarity(
												String 	baseFolder,
												String 	baseFolder_out,
												String 	inputFile_chrono,  // input Chronologically sorted file
												String  delimiter_4_inputFile_chrono,
												boolean is_inputFile_has_header,
												int   	token_index_having_text,
												int		top_N_lines_only_run_for_inputFile_chrono,
												int 	top_N_percentage_to_be_considered_as_anomaly,
												String  query_CSV,
												String  query_CSV_NO_FILTER,
												String  input_output_From_myAlgo2, //from myAlgo
												int 	sample_N_previous_document, //SAMPLING N prev document for similarity calc
												boolean is_do_stemming,
												boolean isSOPdebug,
												boolean is_ignore_stop_words_all_together,
												boolean is_skip_TF_IDF_calc, // this means in past we already got these files. Skips call to calc_tf_idf.calc_tf_idf
												boolean is_run_cosine_only,		// is_run_cosine_only
												boolean is_run_newWord_only,
												boolean is_run_newWord_THRESHOLD_only,
												boolean is_run_jaccard_only
								 			 ){
		int total_documents_on_All_queries=0;
		Cosine_similarity cs=new Cosine_similarity();
		TreeMap<Integer,String> map_inFile_EachLine_of_document=new TreeMap<Integer, String>();
		TreeMap<Integer,String> map_inFile_EachLineDoc_of_TFIDF=new TreeMap<Integer, String>();
		TreeMap<Integer,String> map_Seq_N_WordID_Freq_To_another_format_2=new TreeMap<Integer, String>();
		TreeMap<Integer,String> map_DocID_N_WordID_Freq_To_another_format_2=new TreeMap<Integer, String>();
		TreeMap<Integer,String> mapOut=new TreeMap<Integer, String>();
		
		TreeMap<Integer,String> map_DocID_URL=new TreeMap<Integer, String>();
		
		
		TreeMap<String,TreeMap<Integer,Integer>> map_QUERY_curr_DocID_N_TotalNewWordCount=new TreeMap<String, TreeMap<Integer,Integer>>();
		TreeMap<String,TreeMap<Integer,Double>> map_QUERY_curr_DocID_N_AvgNewWordCount=new TreeMap<String, TreeMap<Integer,Double>>();
		
		// compare avg new word with percentage of no distinct words in curr doc 
		TreeMap<String,TreeMap<Integer,Integer>> map_QUERY_curr_DocID_N_AvgNewWord_Percentage=new TreeMap<String, TreeMap<Integer,Integer>>();
		TreeMap<String, String> map_SS_query_N_DocIDsKEYS=new TreeMap<String, String>();
		
		FileWriter writer_debug=null;
		FileWriter writer_DocID_CosineSim=null;
		FileWriter writer_DocID_CosineSim_NORMALIZ=null;
		FileWriter writer_DocID_TotalNewWords=null;
		FileWriter writer_DocID_TotalNewWords_NORMALIZ=null;
		
		FileWriter writer_URL_TotalNewWords=null;
		FileWriter writer_URL_TotalNewWords_NORMALIZ=null;
		
		FileWriter writer_DocID_URL=null;
		
		FileWriter writer_DocID_Jaccard=null;
		FileWriter writer_DocID_Jaccard_NORMALIZ=null;
		String file_doc_ID_wordID_N_tf_idf = "";
    	long t0 = System.nanoTime();
		try{
					//
			  		map_SS_query_N_DocIDsKEYS=LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE(
					  																					input_output_From_myAlgo2,
					  																					"!!!",
					  																					"1,2",
					  																					"", //append_suffix_at_each_line
					  																					false,
					  																					false //isPrintSOP
						 																				);
			
			//
			writer_debug=new FileWriter(baseFolder+"debug_cosine_sim.txt");
			writer_DocID_CosineSim=new FileWriter(baseFolder+"docID_CosineSim.txt");
			writer_DocID_CosineSim_NORMALIZ=new FileWriter(baseFolder+"docID_CosineSim_NORMALIZ.txt");
			
			writer_DocID_TotalNewWords=new FileWriter(baseFolder+"docID_totalNewWords.txt");
			writer_DocID_TotalNewWords_NORMALIZ=new FileWriter(baseFolder+"docID_totalNewWords_NORMALIZ.txt");
			
			writer_URL_TotalNewWords=new FileWriter(baseFolder+"URL_totalNewWords.txt");
			writer_URL_TotalNewWords_NORMALIZ=new FileWriter(baseFolder+"URL_totalNewWords_NORMALIZ.txt");
			
			writer_DocID_Jaccard=new FileWriter(baseFolder+"docID_Jaccard.txt");
			writer_DocID_Jaccard_NORMALIZ=new FileWriter(baseFolder+"docID_Jaccard_NORMALIZ.txt");
			
			writer_DocID_URL=new FileWriter(baseFolder+"docID_URL.txt");
			
			// skipping TF-IDF
			if(is_skip_TF_IDF_calc==false){
				
				//calculate TF-IDF for each article (each line)
										mapOut=Calc_tf_idf.calc_tf_idf( baseFolder,
																		baseFolder_out,
																		inputFile_chrono, 
																		delimiter_4_inputFile_chrono, //delimiter
																		token_index_having_text,
																		inputFile_chrono+"_TF_IDF.txt",
																		is_do_stemming, //is_do_stemming_on_word
																		is_ignore_stop_words_all_together,
																		false //isSOPprint
																		);
										 
										
			
				file_doc_ID_wordID_N_tf_idf = mapOut.get(-99);
			}
			else{
				file_doc_ID_wordID_N_tf_idf = inputFile_chrono+"_TF_IDF.txt"+"_noStopWord_WORD_ID.txt";
			}
			
			// NOTE: below two files have same number of lines  (excluding header from both) . inputFile && file_doc_ID_wordID_N_tf_idf
			int start_line=-1; int end_line=-1;
			if(top_N_lines_only_run_for_inputFile_chrono>0){
				start_line=1; end_line=top_N_lines_only_run_for_inputFile_chrono;
			}
			
			// loading
			map_inFile_EachLine_of_document=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line( inputFile_chrono, 
					 																									 start_line, //startline, 
																														 end_line, //endline,
																														 " map_inFile_EachLine_of_document ", //debug_label
																														 false //isPrintSOP
																														 ); 
			
			//convert this to format 2
			Convert_DocID_WordID_Freq_To_another_format.convert_DocID_WordID_Freq_To_another_format_2(baseFolder,
																									  file_doc_ID_wordID_N_tf_idf, //inputFile, 
																									  file_doc_ID_wordID_N_tf_idf+"_format_2.txt",
																									  "!!!", //delimiter_4_inputFile
																									  false //isSOPprint
																									  );
			
            
			//LOAD ABOVE CONVERSION
			map_Seq_N_WordID_Freq_To_another_format_2=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
													 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line( 
																								file_doc_ID_wordID_N_tf_idf+"_format_2.txt", 
																							 	 -1, //startline, 
																								 -1, //endline,
																								 " map_inFile_EachLine_of_document ", //debug_label
																								 false //isPrintSOP
																								 ); 
			//get DocID and WordID_TFIDF
			for(int seq:map_Seq_N_WordID_Freq_To_another_format_2.keySet()){
				String [] s=map_Seq_N_WordID_Freq_To_another_format_2.get(seq).split("!!!");
				map_DocID_N_WordID_Freq_To_another_format_2.put(Integer.valueOf(s[0]), s[1]); //doc_ID and wordID_TFIDFString
			}
			
			//each query
			String [] arr_query=query_CSV.split(",");
			String [] arr_query_NO_FILTER=query_CSV_NO_FILTER.split(",");
			
			TreeMap<Integer,String> map_DocID_N_curr_query_matched_only_document=new TreeMap<Integer,String>();
			TreeMap<String,TreeMap<Integer,String>> map_QUERY_curr_DocID_N_query_matched_only_document=new TreeMap<String,TreeMap<Integer,String>>();
			TreeMap<String,TreeMap<Integer,String>> map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP
																						=new TreeMap<String,TreeMap<Integer,String>>();
			
			TreeMap<String,TreeMap<Integer,Double>> map_QUERY_curr_DocID_N_avgCosineSimilarity=new TreeMap<String,TreeMap<Integer,Double>>();
			TreeMap<String,TreeMap<Integer,Double>> map_QUERY_curr_DocID_N_avgJaccardCoefficient=new TreeMap<String,TreeMap<Integer,Double>>();
			
			int cnt=0; String curr_query="";String [] arr_curr_query_space=null;
			
			String curr_query_NO_FILTER="";String [] arr_curr_query_NO_FILTER_delim_hash=null;
			int lineNo=0;
			TreeMap<String,String> map_currQuery_DocIDasKEYS=new TreeMap<String, String>();
			String DocIDasKEY_mapString="";
			// 
			while(cnt<arr_query.length){
				//
				curr_query=arr_query[cnt];//   "road acciden"
				arr_curr_query_space=curr_query.split(" ");
				
				// FROM myAlgo2 run get DocIDS
				DocIDasKEY_mapString=map_SS_query_N_DocIDsKEYS.get(curr_query);
				System.out.println("curr_query---->"+curr_query);
				if(DocIDasKEY_mapString == null)	System.out.println(" len:"+DocIDasKEY_mapString);
				
				System.out.println(" len:"+DocIDasKEY_mapString.length());
				map_currQuery_DocIDasKEYS=Convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(DocIDasKEY_mapString, "=",false);
				
				writer_debug.append("\n (myAlgo2)curr_query:"+curr_query+" size:"+map_currQuery_DocIDasKEYS);
				writer_debug.flush();
				
				curr_query_NO_FILTER=arr_query_NO_FILTER[cnt].replace("dummy", ""); //   "road#acciden"
				arr_curr_query_NO_FILTER_delim_hash=curr_query_NO_FILTER.split("#");
				
				lineNo=0;
				// iterate each doc (lineNumber used as DocID)
				for(int seq:map_inFile_EachLine_of_document.keySet()){
					lineNo++;
					//skip header
					if(is_inputFile_has_header==true && seq==1) continue;
					String currLine=map_inFile_EachLine_of_document.get(seq);
					String []s=currLine.split(delimiter_4_inputFile_chrono);
					int docID=lineNo;
					String curr_bodyText=s[1];
					//
					map_DocID_URL.put(docID, s[0]); // <docID,URL>
					
					boolean is_all_token_present=true;
					int cnt222=0;
					// all token ? present
					while(cnt222<arr_curr_query_space.length){
						if(curr_bodyText.toLowerCase().indexOf(arr_curr_query_space[cnt222].toLowerCase()) >=0 ) {
							is_all_token_present=true;
						}
						else{
							is_all_token_present=false;
							break; // 
						}
						cnt222++;
					}
					// at least one of token not present.. example   "road accident" .. road present and accident absent. SO SKIP 
					if(is_all_token_present ==false ) {
						continue;
					}
					
					//NO_FILTER present
					cnt222=0;
					boolean is_all_token_present_NO_FILTER=false;
					if(curr_query_NO_FILTER.length()>0){
						// all token ? present
						while(cnt222<arr_curr_query_NO_FILTER_delim_hash.length){
							if(curr_bodyText.toLowerCase().indexOf(arr_curr_query_NO_FILTER_delim_hash[cnt222].toLowerCase()) >=0 ) {
								is_all_token_present_NO_FILTER=true;
								break;
							}
							else{
								is_all_token_present_NO_FILTER=false; 
							}
							cnt222++;
						}
						// at least one of NO_FILTER token present, then SKIP.. example   "ceasefire" 
						if(is_all_token_present_NO_FILTER ==true ) {
							continue;
						}
					}
					
					
					//
					if(!map_QUERY_curr_DocID_N_query_matched_only_document.containsKey(curr_query)){
						// query-matched doc only
						TreeMap<Integer,String> map_DocID_Document=new TreeMap<Integer, String>();
						map_DocID_Document.put(docID, curr_bodyText);
						map_QUERY_curr_DocID_N_query_matched_only_document.put(curr_query, map_DocID_Document);
						
						//only ground truth
						if(currLine.toLowerCase().indexOf("ground_truth") >=0    && 
								!map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.containsKey(curr_query) ) {
							TreeMap<Integer,String> map_DocID_Document2=new TreeMap<Integer, String>();
							map_DocID_Document2.put(docID, curr_bodyText);
							map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.put(curr_query, map_DocID_Document2); // <----
						}
					}
					else{
						//System.out.println(map_QUERY_curr_DocID_N_query_matched_only_document.get(docID));
						// query-matched doc only
						TreeMap<Integer,String> map_DocID_Document=map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query);
						map_DocID_Document.put(docID, curr_bodyText);
						map_QUERY_curr_DocID_N_query_matched_only_document.put(curr_query, map_DocID_Document);
						//only ground truth
						if(currLine.toLowerCase().indexOf("ground_truth") >=0    && 
								!map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.containsKey(curr_query) ) {
							TreeMap<Integer,String> map_DocID_Document2=new TreeMap<Integer, String>();
							map_DocID_Document2.put(docID, curr_bodyText);
							map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.put(curr_query, map_DocID_Document2);
						}
						else if (currLine.toLowerCase().indexOf("ground_truth") >=0    && 
								map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.containsKey(curr_query) ) {
							//only ground ruth
							TreeMap<Integer,String> map_DocID_Document2=map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.get(curr_query);
							map_DocID_Document2.put(docID, curr_bodyText);
							if(currLine.toLowerCase().indexOf("ground_truth") >=0  ){
								map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.put(curr_query, map_DocID_Document2);
							}
						}
						
					} //END if(!map_QUERY_curr_DocID_N_query_matched_only_document.containsKey(curr_query)){
				} //END 				for(int seq:map_inFile_EachLine_of_document.keySet()){
				cnt++;
			}
			
			TreeMap<Integer,String> mapQUERY_curr_DocID_N_query_matched_only_document_process=new TreeMap<Integer, String>();
			double curr_cosine=0.0;
			cnt=0; //reset
			int total_newWords_of_currDocID=0;
			 int position_counter=0;
			TreeMap<Integer,Integer> map_Interested_Past_or_Future_Documents=new TreeMap<Integer, Integer>();
			TreeMap<Integer,Integer> map_curr_DocID_NoDistinctWORDSinCurrDoc=new TreeMap<Integer, Integer>();
			TreeMap<Integer,Integer> map_curr_DocID_NoWORDSinCurrDoc=new TreeMap<Integer, Integer>();
			
			TreeMap<String,TreeMap<Integer,Integer>> map_QUERY_curr_DocID_NoDistinctWORDSinCurrDoc=new TreeMap<String,TreeMap<Integer, Integer>>();
			
			double denom_total_avg_jaccard_for_NORMALIZATION=0;
			double denom_total_newWords_for_NORMALIZATION=0;
			double denom_total_avg_cosine_for_NORMALIZATION=0;
			TreeMap<Integer, Double> map_DocID_avgCosineSimilarity=new TreeMap<Integer, Double>();
			TreeMap<Integer, Double> map_DocID_totalNewWords=new TreeMap<Integer, Double>();
			TreeMap<Integer, Double> map_DocID_avgJaccardCoeffic=new TreeMap<Integer, Double>();
			
			TreeMap<String, Double> map_URL_totalNewWords=new TreeMap<String, Double>();

			//each query
			while(cnt<arr_query.length){
				curr_query=arr_query[cnt];
				// now filtered above
				mapQUERY_curr_DocID_N_query_matched_only_document_process=map_QUERY_curr_DocID_N_query_matched_only_document.get(arr_query[cnt]);
				
				//instead use the one from myAlgo2
				// FROM myAlgo2 run get DocIDS
//				DocIDasKEY_mapString=map_SS_query_N_DocIDsKEYS.get(curr_query);
//				System.out.println("curr_query---->"+curr_query);
//				System.out.println(" len:"+DocIDasKEY_mapString.length());
//				map_currQuery_DocIDasKEYS=convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(DocIDasKEY_mapString, "=",false);
//				mapQUERY_curr_DocID_N_query_matched_only_document_process=new TreeMap<Integer, String>(); //OVERWRITING ABOVE ONE.
//				// Filter only docid from myAlgo2
//				 
//				for(String docID:map_currQuery_DocIDasKEYS.keySet()){
//					//writer_debug.append("\nSKIPPED AS not found in myAlgo2 output : docID:"+docID+" query:"+curr_query);
//					writer_debug.flush();
//					mapQUERY_curr_DocID_N_query_matched_only_document_process.put(Integer.valueOf(docID)  , "");
//					//continue;
//				}
				
				
				position_counter=0;
				
				if(mapQUERY_curr_DocID_N_query_matched_only_document_process!=null){
					writer_debug.append("\n ENTERING.1 "+" doc filtered.size:"+mapQUERY_curr_DocID_N_query_matched_only_document_process.size()+
											" arr_query[cnt]:"+arr_query[cnt]
											+ " arr_query_NO_FILTER[cnt]:"+arr_query_NO_FILTER[cnt]
	//										+"\n got mapQUERY_curr_DocID_N_query_matched_only_document_process:"+mapQUERY_curr_DocID_N_query_matched_only_document_process
											);
					total_documents_on_All_queries=total_documents_on_All_queries+mapQUERY_curr_DocID_N_query_matched_only_document_process.size();
				}
				else{
					writer_debug.append("\n ENTERING.1 mapQUERY_curr_DocID_N_query_matched_only_document_process: GOT NULL FOR "+" arr_query[cnt]:"+arr_query[cnt]
										+" filter->"+arr_query_NO_FILTER[cnt]);
				}
				
				writer_debug.flush();
				
				// each docId of curr QUERY
				for(int DocID:mapQUERY_curr_DocID_N_query_matched_only_document_process.keySet()){
					
					position_counter++;

					writer_debug.append("\n entering.2 position_counter->"+position_counter+" for curr_query="+curr_query);
					writer_debug.flush();
					//TreeMap<String, Double> map_doc1_tfidf_string=convert_wordID_TFIDFString_into_TreeMap(map_DocID_N_WordID_Freq_To_another_format_2.get(DocID));
					
					//first 25/50 documents, sample from future 50 , rest sample from past
					if(position_counter==1){
						//iterate next fifty documents.
						//System.out.println("position 1");
						//available is lesser , pick all
						if(mapQUERY_curr_DocID_N_query_matched_only_document_process.size()<sample_N_previous_document){	
								for( int DocID2:mapQUERY_curr_DocID_N_query_matched_only_document_process.keySet()){
									if(DocID!=DocID2){
										map_Interested_Past_or_Future_Documents.put(DocID2,DocID2);
									}
								}
							 
						}
						else {
							//System.out.println("position 2");
							//take next N samples
							int size=1;
							//
							for( int DocID2:mapQUERY_curr_DocID_N_query_matched_only_document_process.keySet()){
								if(map_Interested_Past_or_Future_Documents.size()>=sample_N_previous_document){
									break;
								}
								if(DocID!=DocID2){
									map_Interested_Past_or_Future_Documents.put(DocID2,DocID2);
								}
							}
						}
						
					}
					else if(position_counter<=sample_N_previous_document){
						//System.out.println("position 3->"+position_counter);
						//pick all past, and for rest pick from future to get "sample_N_previous_document"
						//
						for( int DocID2:mapQUERY_curr_DocID_N_query_matched_only_document_process.keySet()){
							if(map_Interested_Past_or_Future_Documents.size()>=sample_N_previous_document){
								break;
							}
							if(DocID!=DocID2){
								map_Interested_Past_or_Future_Documents.put(DocID2,DocID2);
							}
						}
						//generate_Random_Number_Range.generate_Random_Number_Range(1, sample_N_previous_document);
					}
					else if(position_counter>sample_N_previous_document){
						//System.out.println("position 4->"+position_counter);
						int size=0;
						//recent past "sample_N_previous_document" from current position to be taken. 
						for( int DocID2:mapQUERY_curr_DocID_N_query_matched_only_document_process.keySet()){
							size++;
							if(map_Interested_Past_or_Future_Documents.size()>=sample_N_previous_document){
								break;
							}
							if(DocID!=DocID2 && size >= (position_counter-sample_N_previous_document) ){
								map_Interested_Past_or_Future_Documents.put(DocID2,DocID2);
							}
						}
						
					}
					double average_cosine=0.0;

					//
					if(is_run_cosine_only){
					
						// 1. calculate cosine similarity between current Doc (DocID) and other documents from map_Interested_Past_or_Future_Documents (and get Average)
						  average_cosine=getAverage_Cosine(    map_DocID_N_WordID_Freq_To_another_format_2,
															   map_Interested_Past_or_Future_Documents,
															   DocID
																);
						
	
						// not numeric
						if(! IsNumeric.isNumeric(String.valueOf(average_cosine))) 
							average_cosine=0.0; 
						
						denom_total_avg_cosine_for_NORMALIZATION=denom_total_avg_cosine_for_NORMALIZATION+average_cosine;
						
						// all docId ,similarity calc
						map_DocID_avgCosineSimilarity.put(DocID, average_cosine);
					
					}
					//
					if(is_run_newWord_only){
					
						// 2. new word 
						total_newWords_of_currDocID=get_newWords_of_currDocID(  map_DocID_N_WordID_Freq_To_another_format_2,
																				map_Interested_Past_or_Future_Documents,
																				DocID,
																				2 //flag=2 gives average, flag=1 gives total
													 				    		);
						
						// not numeric
						if(! IsNumeric.isNumeric(String.valueOf(total_newWords_of_currDocID))) 
							total_newWords_of_currDocID=0;
						
						denom_total_newWords_for_NORMALIZATION=denom_total_newWords_for_NORMALIZATION+total_newWords_of_currDocID;
						
						// all docId ,similarity calc
						map_DocID_totalNewWords.put(DocID, Double.valueOf(total_newWords_of_currDocID));
					    //
						map_URL_totalNewWords.put( map_DocID_URL.get(DocID), 
								                   Double.valueOf(total_newWords_of_currDocID)
								                 );
					}

					double average_jaccard=0.0;
					//
					if(is_run_jaccard_only){
						// 3. JACCARD COEFFICIENT 
						average_jaccard=get_JaccardCoefficient_of_currDocID( map_DocID_N_WordID_Freq_To_another_format_2,
															  						map_Interested_Past_or_Future_Documents,
															  						DocID,
															  						2 //flag=2 gives average, flag=1 gives total
						 				    										);
						
						// not numeric
						if(! IsNumeric.isNumeric(String.valueOf(average_jaccard))) 
							average_jaccard=0.;
						
						denom_total_avg_jaccard_for_NORMALIZATION=denom_total_avg_jaccard_for_NORMALIZATION+average_jaccard;
						
						// all docId ,similarity calc
						map_DocID_avgJaccardCoeffic.put(DocID, average_jaccard);
						//System.out.println("average_jaccard:"+average_jaccard);
					}
					
					// no words in curr document
					int no_words_in_curr_Doc=map_DocID_N_WordID_Freq_To_another_format_2.get(DocID).split(" ").length;
					map_curr_DocID_NoDistinctWORDSinCurrDoc.put(DocID , no_words_in_curr_Doc);
										
					// accum cosine similarity
					if(!map_QUERY_curr_DocID_N_avgCosineSimilarity.containsKey( curr_query)){
						TreeMap<Integer,Double> temp=new TreeMap<Integer, Double>();
						temp.put(DocID , average_cosine);
						// 
						map_QUERY_curr_DocID_N_avgCosineSimilarity.put( curr_query,temp);
					}
					else{
						TreeMap<Integer,Double> temp=map_QUERY_curr_DocID_N_avgCosineSimilarity.get(curr_query);
						temp.put(DocID , average_cosine);
						map_QUERY_curr_DocID_N_avgCosineSimilarity.put( curr_query, temp);
					}
					
					// accum new word TOTAL
					if(!map_QUERY_curr_DocID_N_TotalNewWordCount.containsKey( curr_query)){
						TreeMap<Integer,Integer> temp=new TreeMap<Integer, Integer>();
						temp.put(DocID , total_newWords_of_currDocID);
						// 
						map_QUERY_curr_DocID_N_TotalNewWordCount.put( curr_query, temp);
					}
					else{
						TreeMap<Integer,Integer> temp=map_QUERY_curr_DocID_N_TotalNewWordCount.get(curr_query);
						temp.put(DocID , total_newWords_of_currDocID);
						map_QUERY_curr_DocID_N_TotalNewWordCount.put( curr_query, temp);
					}
					// accum new word AVERAGE (this one is really not used)
					if(!map_QUERY_curr_DocID_N_AvgNewWordCount.containsKey( curr_query)){
						TreeMap<Integer,Double> temp=new TreeMap<Integer, Double>();
						temp.put(DocID , total_newWords_of_currDocID/ (double) map_Interested_Past_or_Future_Documents.size() );
						// 
						map_QUERY_curr_DocID_N_AvgNewWordCount.put( curr_query, temp);
					}
					else{
						TreeMap<Integer, Double> temp=map_QUERY_curr_DocID_N_AvgNewWordCount.get(curr_query);
						temp.put(DocID, total_newWords_of_currDocID/ (double) map_Interested_Past_or_Future_Documents.size());
						map_QUERY_curr_DocID_N_AvgNewWordCount.put( curr_query, temp);
					}
					// accum JACCARD
					if(!map_QUERY_curr_DocID_N_avgJaccardCoefficient.containsKey( curr_query)){
						TreeMap<Integer,Double> temp=new TreeMap<Integer, Double>();
						temp.put(DocID , average_jaccard);
						// 
						map_QUERY_curr_DocID_N_avgJaccardCoefficient.put( curr_query,temp);
					}
					else{
						TreeMap<Integer,Double> temp=map_QUERY_curr_DocID_N_avgJaccardCoefficient.get(curr_query);
						temp.put(DocID , average_jaccard);
						map_QUERY_curr_DocID_N_avgJaccardCoefficient.put( curr_query, temp);
					}
					
					
					//accum total new word count & avg new word count
					

//	   			    curr_cosine=cs.calculateCosineSimilarity_approach_1(map_doc1_tfidf_string ,
//	   			    													map_doc2_tfidf_string);
	   			    //debug 0
	   			    //System.out.println("curr DocID:"+DocID);
					
	   			    // +" map_Interested_Past_or_Future_Documents.size:"+map_Interested_Past_or_Future_Documents.size()
//	   			    					+" average_cosine:"+average_cosine
//	   			    					);//+map_doc1_tfidf_string);
	   			    
	   			 writer_debug.append(  "\n curr DocID:"+DocID
	   					 				+" curr_query:"+curr_query
	   					 				+" map_Interested_Past_or_Future_Documents.size:"+map_Interested_Past_or_Future_Documents.size()
	   			    					+" average_cosine:"+average_cosine
	   			    					+" position_counter:"+position_counter
	   			    					+" curr_query:"+curr_query
	   			    					+" map_Interested_Past_or_Future_Documents:"+map_Interested_Past_or_Future_Documents
	   			    					//+" map_QUERY_curr_DocID_N_TotalNewWordCount:"+map_QUERY_curr_DocID_N_TotalNewWordCount
	   			    					);
	   			
	   			 //writer_debug.append("\n mapQUERY_curr_DocID_N_query_matched_only_document_process:"+mapQUERY_curr_DocID_N_query_matched_only_document_process);
	   			 writer_debug.flush();
	   			    
	   			 //reset
	   			 map_Interested_Past_or_Future_Documents=new TreeMap<Integer, Integer>();
				}
				cnt++;
			}
			
			//write output Cosine
			for(int docID:map_DocID_avgCosineSimilarity.keySet()){
				writer_DocID_CosineSim.append(docID+"!!!"+map_DocID_avgCosineSimilarity.get(docID)+"\n");
				writer_DocID_CosineSim.flush();
				
				writer_DocID_CosineSim_NORMALIZ.append(docID+"!!!"+map_DocID_avgCosineSimilarity.get(docID) / (double) denom_total_avg_cosine_for_NORMALIZATION 
														+"!!!"+denom_total_avg_cosine_for_NORMALIZATION+"\n");
				writer_DocID_CosineSim_NORMALIZ.flush();
			}


			//write output NEW WORD
			for(int docID:map_DocID_totalNewWords.keySet()){
				
				String currURL=map_DocID_URL.get(docID);
				
				writer_DocID_URL.append(docID+"!!!"+currURL+"\n");
				writer_DocID_URL.flush();
				//
				writer_DocID_TotalNewWords.append(docID+"!!!"+map_DocID_totalNewWords.get(docID)+"\n");
				writer_DocID_TotalNewWords.flush();
				//
				writer_URL_TotalNewWords.append(  currURL+ "!!!" + map_URL_totalNewWords.get(currURL)+"\n");
				writer_URL_TotalNewWords.flush();
				//
				writer_DocID_TotalNewWords_NORMALIZ.append(docID+"!!!"+map_DocID_totalNewWords.get(docID) / (double) denom_total_newWords_for_NORMALIZATION 
															+"!!!"+denom_total_newWords_for_NORMALIZATION+"\n");
				writer_DocID_TotalNewWords_NORMALIZ.flush();
				//
				writer_URL_TotalNewWords_NORMALIZ.append(currURL+"!!!"+map_URL_totalNewWords.get(currURL) / (double) denom_total_newWords_for_NORMALIZATION 
															+"!!!"+denom_total_newWords_for_NORMALIZATION+"\n");
				writer_URL_TotalNewWords_NORMALIZ.flush();
				
			}

			writer_debug.append("map_DocID_totalNewWords:"+map_DocID_totalNewWords+"\n");
			writer_debug.flush();
			
			//write output JACCARD
			for(int docID:map_DocID_avgJaccardCoeffic.keySet()){
				writer_DocID_Jaccard.append(docID+"!!!"+map_DocID_avgJaccardCoeffic.get(docID)+"\n");
				writer_DocID_Jaccard.flush();
				
				writer_DocID_Jaccard_NORMALIZ.append(docID+"!!!"+map_DocID_avgJaccardCoeffic.get(docID) / (double) denom_total_avg_jaccard_for_NORMALIZATION
															+"!!!"+denom_total_avg_jaccard_for_NORMALIZATION +"\n");
				writer_DocID_Jaccard_NORMALIZ.flush();
				
			}
			
			
		
			// STATISTICS 
			for(String curr_query2:map_QUERY_curr_DocID_N_query_matched_only_document.keySet()){
				System.out.println("stats:query:"+curr_query2+"<->size:"+map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query2).size());
				writer_debug.append("-------------------------------curr_query2:"+curr_query2
											+" size:"+map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query2).size()
											+" filter:"+curr_query_NO_FILTER 
											+"-----------\n");
				//writer_debug.append( curr_query2 +"<--->"+map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query2) +"\n");
				writer_debug.flush();
			}
			
			//each query, each document -> AVG cosine
			for(String curr_query2:map_QUERY_curr_DocID_N_avgCosineSimilarity.keySet()){
				writer_debug.append("\ncurr_query2:"+curr_query2+"<-->"+map_QUERY_curr_DocID_N_avgCosineSimilarity.get(curr_query2)+"\n");
				writer_debug.flush();
			}
			
			System.out.println("map_DocID_N_WordID_Freq_To_another_format_2:"+map_DocID_N_WordID_Freq_To_another_format_2.size());
			System.out.println("map_QUERY_curr_DocID_N_query_matched_only_document:"+map_QUERY_curr_DocID_N_query_matched_only_document.size());
			
			TreeMap<String,Integer> mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim=new TreeMap<String, Integer>();
			
			
			//process each query , OFFSET get NoDocumentsAnomalousAfterSortedByCosineSim
			for(String curr_query2:map_QUERY_curr_DocID_N_query_matched_only_document.keySet()){
				System.out.println("size->"+map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query2).size()
									+" top_N_percentage_to_be_considered_as_anomaly:"+top_N_percentage_to_be_considered_as_anomaly);
				// COSINE (apply same for new word)
				mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim.put( curr_query2,
																			Integer.valueOf(
																				(int) (map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query2).size() * (top_N_percentage_to_be_considered_as_anomaly/100.0))
																				)
																		  );
				
			}
			
			System.out.println("mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim:"+mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim);
			
			// **THRESHOLD NEW WORD - compare average new word with percentage of no distinct words in curr doc
			// Curr Query
			for(String q:map_QUERY_curr_DocID_N_TotalNewWordCount.keySet()){
				writer_debug.append("\nmap_QUERY_curr_DocID_N_TotalNewWordCount:key:"+q+"<-->size:"+map_QUERY_curr_DocID_N_TotalNewWordCount.get(q).size()
								    +"<-->value:"+map_QUERY_curr_DocID_N_TotalNewWordCount.get(q) +"\n");
				//(Sample) iterate first 3 docs for no of distinct words each doc has
				int h=0;int perc_=0;
				//reset
				//map_curr_DocID_NoDistinctWORDSinCurrDoc=new TreeMap<Integer, Integer>();
				//Curr Query
				for(Integer DocID:map_QUERY_curr_DocID_N_TotalNewWordCount.get(q).keySet()){
					//h++;
//					writer_debug.append("\n  docID="+DocID+" distinct no words(curr doc):"+ map_curr_DocID_NoDistinctWORDSinCurrDoc.get(DocID)
//									+"<-->avg.new.word.count:"+map_QUERY_curr_DocID_N_TotalNewWordCount.get(q).get(DocID));
					
					try{
						writer_debug.append("\n 1:"+map_QUERY_curr_DocID_N_TotalNewWordCount.get(q).get(DocID));
						writer_debug.append("\n 2:"+map_curr_DocID_NoDistinctWORDSinCurrDoc.get(DocID));
						
						perc_=(map_QUERY_curr_DocID_N_TotalNewWordCount.get(q).get(DocID)/map_curr_DocID_NoDistinctWORDSinCurrDoc.get(DocID))*100;
						
						map_curr_DocID_NoDistinctWORDSinCurrDoc.put(DocID, perc_);	
					}
					catch(Exception e){
						writer_debug.append("\n error: DocID:"+DocID+" q:"+q +" E:"+e.getMessage());
						writer_debug.flush();
					}
					
					//if(h>10) break;
				}
				map_QUERY_curr_DocID_NoDistinctWORDSinCurrDoc.put(q, map_curr_DocID_NoDistinctWORDSinCurrDoc);
				writer_debug.flush();
			}
			
			TreeMap<String,TreeMap<Integer,Integer>> map_Query_anomalousDocIdASKEY_for_COSINE_SORTED
																							=new TreeMap<String, TreeMap<Integer,Integer>>();
			TreeMap<String,TreeMap<Integer,Integer>> map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD_SORTED
																							=new TreeMap<String, TreeMap<Integer,Integer>>();
			TreeMap<String,TreeMap<Integer,Integer>> map_Query_anomalousDocIdASKEY_for_THRESHOLD_TOTAL_NEW_WORD_SORTED
																							=new TreeMap<String, TreeMap<Integer,Integer>>();
			TreeMap<String,TreeMap<Integer,Integer>> map_Query_anomalousDocIdASKEY_for_AVG_JACCARD_SORTED
																				=new TreeMap<String, TreeMap<Integer,Integer>>();
			
			Map<Integer, Integer> mapDocID_sortedTotalNewWord=new TreeMap<Integer, Integer>();
			Map<Integer, Integer> mapDocID_sorted_Threshold_TotalNewWord=new TreeMap<Integer, Integer>();
			Map<Integer, Double>  mapDocID_sortedTotal_R_AVG_Jaccard=new TreeMap<Integer, Double>();
			Map<Integer, Double> mapDocID_sortedCosineSim=new TreeMap<Integer, Double>();
			
			// process each query, calculate accuracy (COSINE SIMILARITY)
			for(String curr_query2:map_QUERY_curr_DocID_N_query_matched_only_document.keySet()){
				TreeMap<Integer,Integer> map_currQuery_anomalousDocIdASKEY_COSINE=new TreeMap<Integer,Integer>();
				TreeMap<Integer,Integer> map_currQuery_anomalousDocIdASKEY_TOTAL_NEW_WORDS=new TreeMap<Integer,Integer>();
				TreeMap<Integer,Integer> map_currQuery_anomalousDocIdASKEY_THRESHOLD_TOTAL_NEW_WORDS=new TreeMap<Integer,Integer>();
				TreeMap<Integer,Integer> map_currQuery_anomalousDocIdASKEY_AVG_JACCARD=new TreeMap<Integer,Integer>();
				
				if(is_run_cosine_only){
				//sort ( cosine similarity )
									 mapDocID_sortedCosineSim=Sort_given_treemap.
															  sortByValue_ID_method4_ascending(map_QUERY_curr_DocID_N_avgCosineSimilarity.get(curr_query2));
				}
				System.out.println("query:"+curr_query2+" mapDocID_sortedCosineSim:"+mapDocID_sortedCosineSim);
				
				//sort ( TOTAL NEW WORD )
				if(is_run_newWord_only){
									mapDocID_sortedTotalNewWord=Sort_given_treemap.
																  sortByValue_II_method4_ascending(map_QUERY_curr_DocID_N_TotalNewWordCount.get(curr_query2) );
				}
				 
				//sort ( TOTAL NEW WORD - THRESHOLD)
				if(is_run_newWord_THRESHOLD_only){
									mapDocID_sorted_Threshold_TotalNewWord=Sort_given_treemap.
																  			sortByValue_II_method4_ascending(map_QUERY_curr_DocID_NoDistinctWORDSinCurrDoc.get(curr_query2) );
				}
				
				//sort ( JACCARD COEFFICIENT )
				if(is_run_jaccard_only){
									mapDocID_sortedTotal_R_AVG_Jaccard=
																Sort_given_treemap.
						  										sortByValue_ID_method4_ascending(map_QUERY_curr_DocID_N_avgJaccardCoefficient .get(curr_query2) );
				}
				//max_for_this_query_to_mark_anomaly
				int max_for_this_query_to_mark_anomaly=mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim.get(curr_query2);
				// START COSINE SIMILARITY
				int cnt20=0;
				// sorted cosine sim
				for(int DocId:mapDocID_sortedCosineSim.keySet()){
					cnt20++;	
					if(cnt20>max_for_this_query_to_mark_anomaly){
						break;
					}
					map_currQuery_anomalousDocIdASKEY_COSINE.put(DocId, -1);
				}
				map_Query_anomalousDocIdASKEY_for_COSINE_SORTED.put(curr_query2, map_currQuery_anomalousDocIdASKEY_COSINE);//GLOBAL
				// START TOTAL NEW WORD
				cnt20=0;
				//sorted Total New Word
				for(int DocId:mapDocID_sortedTotalNewWord.keySet()){
					cnt20++;	
					if(cnt20>max_for_this_query_to_mark_anomaly){
						break;
					}
					map_currQuery_anomalousDocIdASKEY_TOTAL_NEW_WORDS.put(DocId, -1);
				}
				map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD_SORTED.put(curr_query2, map_currQuery_anomalousDocIdASKEY_TOTAL_NEW_WORDS); //GLOBAL
				//END TOTAL NEW WORD
				
				//START THRESHOLD NEW WORD 
				cnt20=0;
				//sorted THRESHOLD Total New Word
				for(int DocId:mapDocID_sorted_Threshold_TotalNewWord.keySet()){
					cnt20++;	
					if(cnt20>max_for_this_query_to_mark_anomaly){
						break;
					}
					map_currQuery_anomalousDocIdASKEY_THRESHOLD_TOTAL_NEW_WORDS.put(DocId, -1);
				}
				map_Query_anomalousDocIdASKEY_for_THRESHOLD_TOTAL_NEW_WORD_SORTED.put(curr_query2, map_currQuery_anomalousDocIdASKEY_THRESHOLD_TOTAL_NEW_WORDS); //GLOBAL
				//END THRESHOLD NEW WORD
				
				// START JACCARD SIMILARITY
				cnt20=0;
				// sorted Jaccard Similarity
				for(int DocId:mapDocID_sortedTotal_R_AVG_Jaccard.keySet()){
					cnt20++;	
					if(cnt20>max_for_this_query_to_mark_anomaly){
						break;
					}
					map_currQuery_anomalousDocIdASKEY_AVG_JACCARD.put(DocId, -1);
				}
				map_Query_anomalousDocIdASKEY_for_AVG_JACCARD_SORTED.put(curr_query2, map_currQuery_anomalousDocIdASKEY_AVG_JACCARD); //GLOBAL
				// end JACCARD SIMILARITY
				
			} //END for(String curr_query2:map_QUERY_curr_DocID_N_query_matched_only_document.keySet()){
			
			for(String q:map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD_SORTED.keySet()){
				writer_debug.append("\n(10)map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD:key:"+q+"<-->size:"+map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD_SORTED.get(q).size()
								    +"<-->value:"+map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD_SORTED.get(q) +"\n");
				writer_debug.flush();
			}
			String t1_latex_out="",  t2_latex_out="",  t3_latex_out="",  t4_latex_out=""; int cnt_no_queries=0;double F1=0.0;
			int TP_COSINE=0; int total_TP_COSINE=0; double avg_accuracy_cosine=0.0;  double avg_ =0.0;
			int TN_COSINE=0;  int total_TN_COSINE=0;
			int FP_COSINE=0;  int FN_COSINE=0; 
			int total_FP_COSINE=0;  int total_FN_COSINE=0;
			double total_accuracy=0.;
			double total_prec=0.;   double total_recall=0.;  double total_F1=0.; cnt_no_queries=0;
			// BEGIN - COSINE
			
			if(is_run_cosine_only){
			
			// each query process marked anomalies (using COSINE similarity) and compare to ground Truth.
			for(String curr_query3:map_Query_anomalousDocIdASKEY_for_COSINE_SORTED.keySet()){
				cnt_no_queries++;
				TP_COSINE=0;FP_COSINE=0;
				System.out.println("curr_query3:"+curr_query3+" "+map_Query_anomalousDocIdASKEY_for_COSINE_SORTED.get(curr_query3).size() 
									+" "+map_Query_anomalousDocIdASKEY_for_COSINE_SORTED.get(curr_query3)
//									+" "+map_inFile_EachLine_of_document.get(map_Query_anomalousDocIdASKEY.get(curr_query3)).indexOf("ground_truth")
//									+" "+map_inFile_EachLine_of_document.get(map_Query_anomalousDocIdASKEY.get(curr_query3))
									);

				// 
				for(int DocID:map_Query_anomalousDocIdASKEY_for_COSINE_SORTED.get(curr_query3).keySet()){
//					System.out.println("1->"+map_inFile_EachLine_of_document.get(DocID)
//									+" idx->"+map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth"));
					
					if(map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth")>=0){
						TP_COSINE++;
					}
					else{
						FP_COSINE++;
					}
				}
				total_TP_COSINE=total_TP_COSINE+TP_COSINE;
				TN_COSINE=0;FN_COSINE=0;
				 //TN
				 TreeMap<Integer, String> map_docID_gtTP_currQuery= map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.get(curr_query3);
				 for(int DocID:map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query3).keySet()){
					 // 
					 if(!map_docID_gtTP_currQuery.containsKey(DocID) &&  !map_Query_anomalousDocIdASKEY_for_COSINE_SORTED.get(curr_query3).containsKey(DocID )
							 && map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth")==-1 
						    ){
						TN_COSINE++; 	
					 }
					 else if(map_docID_gtTP_currQuery.containsKey(DocID) &&
							 !map_Query_anomalousDocIdASKEY_for_COSINE_SORTED.get(curr_query3).containsKey(DocID )
							 && map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth")>=0 ) {
						FN_COSINE++;
					 }
				 }
				 total_TN_COSINE=total_TN_COSINE+TN_COSINE;
				 
				double prec=(TP_COSINE/(double)mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim.get(curr_query3));
				double recall=TP_COSINE/(double)map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.get(curr_query3).size();
				double accuracy=(TP_COSINE+TN_COSINE) / (double) map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query3).size();
				 
				if(prec+recall>0){
					  F1=(2*(prec * recall )/ (prec+recall));
				}
				else{
					  F1=0;
				}
				
				total_prec=total_prec+prec;
				total_recall=total_recall+recall;
				total_F1=total_F1+F1;
				total_accuracy=total_accuracy+accuracy;
				
				System.out.println("\n query(3):"+curr_query3+" COSINE TP:"+TP_COSINE
									+" precision:"+ (TP_COSINE/(double)mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim.get(curr_query3))
									);
				writer_debug.append("\n query(3):"+curr_query3+" COSINE TP:"+TP_COSINE
									+" precision:"+ prec
									+" recall:"+ recall
									+" F1:"+ F1
									+" Accuracy:"+accuracy
									);
				writer_debug.flush();
			}
			
			}
			//END  - COSINE
			
			writer_debug.append( "\n Precision(avg):"+total_prec/  (double) cnt_no_queries
								 +" Recall(avg):"+total_recall/  (double) cnt_no_queries
								 +" F1(avg):"+total_F1/  (double) cnt_no_queries
								 +" accuracy(avg):"+total_accuracy/  (double) cnt_no_queries
								 +" denominator:"+ map_Query_anomalousDocIdASKEY_for_AVG_JACCARD_SORTED.size()
								 +"\n");
			  t1_latex_out=
								  top_N_percentage_to_be_considered_as_anomaly+"\\% &"+
								  sample_N_previous_document+"&"+
								  "Cosine"+"&"+
								  total_prec/  (double)  cnt_no_queries
								+","+total_recall/ (double) cnt_no_queries
								+","+total_F1/ (double) cnt_no_queries
								+","+total_accuracy/ (double)  cnt_no_queries
								+"\\"
								+"\n";
			
			writer_debug.flush();
			
			int TP_NEW_WORDS_TOTAL=0;  int TN_NEW_WORDS_TOTAL=0; 
			int FP_NEW_WORDS_TOTAL=0;int FN_NEW_WORDS_TOTAL=0;  
			int total_TP_NEW_WORDS_TOTAL=0;
			int total_TN_NEW_WORDS_TOTAL=0;
			total_prec=0.; total_recall=0.; total_F1=0.;cnt_no_queries=0;
			total_accuracy=0;
			
			// BEGIN - NEW WORD
			if(is_run_newWord_only){
				// each query process marked anomalies (using TOTAL NEW WORDS) and compare to ground Truth.
				for(String curr_query4:map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD_SORTED.keySet()){
					cnt_no_queries++;
					TP_NEW_WORDS_TOTAL=0;FP_NEW_WORDS_TOTAL=0;
					System.out.println("curr_query4:"+curr_query4+" "+map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD_SORTED.get(curr_query4).size() 
										+" "+map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD_SORTED.get(curr_query4)
	//									+" "+map_inFile_EachLine_of_document.get(map_Query_anomalousDocIdASKEY.get(curr_query3)).indexOf("ground_truth")
	//									+" "+map_inFile_EachLine_of_document.get(map_Query_anomalousDocIdASKEY.get(curr_query3))
										);
	
					// TP
					for(int DocID:map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD_SORTED.get(curr_query4).keySet()){
	//					System.out.println("1->"+map_inFile_EachLine_of_document.get(DocID)
	//									+" idx->"+map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth"));
						
						if(map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth")>=0){
							TP_NEW_WORDS_TOTAL++;
						}
						else{
							FP_NEW_WORDS_TOTAL++;
						}
					}
					total_TP_NEW_WORDS_TOTAL=total_TP_NEW_WORDS_TOTAL+TP_NEW_WORDS_TOTAL;
					 //TN
					TN_NEW_WORDS_TOTAL=0;FN_NEW_WORDS_TOTAL=0;
					
//					System.out.println("map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP:"+map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP);
					 TreeMap<Integer, String> map_docID_gtTP_currQuery= map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.get(curr_query4);
					 
					 for(int DocID:map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query4).keySet()){
						 if(map_docID_gtTP_currQuery==null) {
							 System.out.println(" IS NULL <---ERROR="+curr_query4);
	//						 continue;
						 }
						 // 
						 if(!map_docID_gtTP_currQuery.containsKey(DocID) &&  
								 !map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD_SORTED.get(curr_query4).containsKey(DocID )
								 && map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth")==-1 
							    ){
							 TN_NEW_WORDS_TOTAL++; 	
						 }
						 else if(map_docID_gtTP_currQuery.containsKey(DocID) &&
								 !map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD_SORTED.get(curr_query4).containsKey(DocID )
								 && map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth")>=0 ) {
							 FN_NEW_WORDS_TOTAL++;
						 }
					 }
					  
					total_TN_NEW_WORDS_TOTAL=total_TN_NEW_WORDS_TOTAL+TN_NEW_WORDS_TOTAL;
					
					System.out.println("\nquery(4):"+curr_query4+" NEW WORDS TP:"+TP_NEW_WORDS_TOTAL
										+" precision:"+ (TP_NEW_WORDS_TOTAL/(double)mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim.get(curr_query4))
										+" "+total_TP_NEW_WORDS_TOTAL/ (double) map_Query_anomalousDocIdASKEY_for_TOTAL_NEW_WORD_SORTED.size()
										);
					double prec=(TP_NEW_WORDS_TOTAL/(double)mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim.get(curr_query4));
					double recall=TP_NEW_WORDS_TOTAL/(double)map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.get(curr_query4).size();
					double accuracy=(TP_NEW_WORDS_TOTAL+TN_NEW_WORDS_TOTAL) / (double) map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query4).size();
					if(prec+recall>0){
						  F1=(2*(prec * recall )/ (prec+recall));
					}
					else{
						  F1=0;
					}
					total_prec=total_prec+prec;
					total_recall=total_recall+recall;
					total_F1=total_F1+F1;
					total_accuracy=total_accuracy+accuracy;
					
					writer_debug.append("\n query(4):"+curr_query4+" NEW WORDS TP:"+TP_NEW_WORDS_TOTAL
										+" precision:"+ prec
										+" recall:"+ recall
										+" F1:"+ F1
										+" accuracy:"+accuracy
										);
	
					writer_debug.flush();
	
					writer_debug.append( "\n Precision(avg):"+total_prec/  (double) cnt_no_queries
										 +" Recall(avg):"+total_recall/ (double)  cnt_no_queries
										 +" F1(avg):"+total_F1/ (double)  cnt_no_queries
										 +" Accuracy(avg):"+total_accuracy/ (double)  cnt_no_queries
										 +" denominator:"+ map_Query_anomalousDocIdASKEY_for_AVG_JACCARD_SORTED.size()
										 +"\n");
					  t2_latex_out=
							  		  top_N_percentage_to_be_considered_as_anomaly+"\\% &"+
									  sample_N_previous_document+"&"+
							  				 "New word count"+"&"+
							  				 total_prec/  (double)  cnt_no_queries
										+","+total_recall/ (double) cnt_no_queries
										+","+total_F1/  (double) cnt_no_queries
										+","+total_accuracy/ (double)  cnt_no_queries
										+"\\"
										+"\n";
					
				}
				writer_debug.flush();
				// END - NEW WORD
			}
			
			if(is_run_newWord_THRESHOLD_only){
			{//START THRESHOLD NEW WORD
				total_prec=0.;   total_recall=0.;   total_F1=0.;cnt_no_queries=0;
				total_accuracy=0;
				int TP_THRESHOLD_NEW_WORDS_TOTAL=0; int total_TP_THRESHOLD_NEW_WORDS_TOTAL=0;
				int total_TN_THRESHOLD_NEW_WORDS_TOTAL=0;
				int FP_THRESHOLD_NEW_WORDS_TOTAL=0; int TN_THRESHOLD_NEW_WORDS_TOTAL=0;
				// each query process marked anomalies (using TOTAL NEW WORDS) and compare to ground Truth.
				for(String curr_query4:map_Query_anomalousDocIdASKEY_for_THRESHOLD_TOTAL_NEW_WORD_SORTED.keySet()){
					cnt_no_queries++;
					TP_THRESHOLD_NEW_WORDS_TOTAL=0;FP_THRESHOLD_NEW_WORDS_TOTAL=0;
					System.out.println("curr_query5:"+curr_query4+" "+map_Query_anomalousDocIdASKEY_for_THRESHOLD_TOTAL_NEW_WORD_SORTED.get(curr_query4).size() 
										+" "+map_Query_anomalousDocIdASKEY_for_THRESHOLD_TOTAL_NEW_WORD_SORTED.get(curr_query4)
//										+" "+map_inFile_EachLine_of_document.get(map_Query_anomalousDocIdASKEY.get(curr_query3)).indexOf("ground_truth")
//										+" "+map_inFile_EachLine_of_document.get(map_Query_anomalousDocIdASKEY.get(curr_query3))
										);

					//TP
					for(int DocID:map_Query_anomalousDocIdASKEY_for_THRESHOLD_TOTAL_NEW_WORD_SORTED.get(curr_query4).keySet()){
//						System.out.println("1->"+map_inFile_EachLine_of_document.get(DocID)
//										+" idx->"+map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth"));
						
						if(map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth")>=0){
							TP_THRESHOLD_NEW_WORDS_TOTAL++;
						}
						else{
							FP_THRESHOLD_NEW_WORDS_TOTAL++;
						}
						
						
					}
					total_TP_THRESHOLD_NEW_WORDS_TOTAL=total_TP_THRESHOLD_NEW_WORDS_TOTAL+TP_THRESHOLD_NEW_WORDS_TOTAL;
					
					//TN
					TN_THRESHOLD_NEW_WORDS_TOTAL=0;FP_THRESHOLD_NEW_WORDS_TOTAL=0;
					 //TN
					 TreeMap<Integer, String> map_docID_gtTP_currQuery= map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.get(curr_query4);
					 
					 for(int DocID:map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query4).keySet()){
						 // 
						 if(!map_docID_gtTP_currQuery.containsKey(DocID) &&  !map_Query_anomalousDocIdASKEY_for_THRESHOLD_TOTAL_NEW_WORD_SORTED.get(curr_query4).containsKey(DocID )
								 && map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth")==-1 
							    ){
							 TN_THRESHOLD_NEW_WORDS_TOTAL++; 	
						 }
						 else if(map_docID_gtTP_currQuery.containsKey(DocID) &&
								 !map_Query_anomalousDocIdASKEY_for_THRESHOLD_TOTAL_NEW_WORD_SORTED.get(curr_query4).containsKey(DocID )
								 && map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth")>=0 ) {
							 FP_THRESHOLD_NEW_WORDS_TOTAL++;
						 }
					 }
					 total_TN_THRESHOLD_NEW_WORDS_TOTAL=total_TN_THRESHOLD_NEW_WORDS_TOTAL+TN_THRESHOLD_NEW_WORDS_TOTAL;
 
					
					System.out.println("\nquery(5):"+curr_query4+" NEW THRESHOLD WORDS TP:"+TP_THRESHOLD_NEW_WORDS_TOTAL
										+" precision:"+ (TP_THRESHOLD_NEW_WORDS_TOTAL/(double)mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim.get(curr_query4))
										+" "+total_TP_THRESHOLD_NEW_WORDS_TOTAL/ (double) map_Query_anomalousDocIdASKEY_for_THRESHOLD_TOTAL_NEW_WORD_SORTED.size()
										);
					
					double prec=(TP_THRESHOLD_NEW_WORDS_TOTAL/(double)mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim.get(curr_query4));
					double recall=TP_THRESHOLD_NEW_WORDS_TOTAL/(double)map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.get(curr_query4).size();
					double accuracy=(TP_THRESHOLD_NEW_WORDS_TOTAL+TN_THRESHOLD_NEW_WORDS_TOTAL) 
											/ (double) map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query4).size();
					
					if(prec+recall>0){
						  F1=(2*(prec * recall )/ (prec+recall));
					}
					else{
						  F1=0;
					}
					total_prec=total_prec+prec;
					total_recall=total_recall+recall;
					total_F1=total_F1+F1;
					total_accuracy=total_accuracy+accuracy;
					
					writer_debug.append("\n query(5):"+curr_query4+" NEW THRESHOLD WORDS TP:"+TP_THRESHOLD_NEW_WORDS_TOTAL
										+" precision:"+ prec
										+" recall:"+ recall
										+" F1:"+ F1
										+" accuracy:"+ accuracy
										);

					writer_debug.flush();

					
				}
				writer_debug.append( "\n Precision(avg):"+total_prec/ (double)  cnt_no_queries
						 +" Recall(avg):"+total_recall/ (double)  cnt_no_queries
						 +" F1(avg):"+total_F1/ (double)  cnt_no_queries
						 +" accuracy(avg):"+total_accuracy/ (double)  cnt_no_queries
						 +" denominator:"+ map_Query_anomalousDocIdASKEY_for_THRESHOLD_TOTAL_NEW_WORD_SORTED.size()
						 +"\n");
				t3_latex_out=
						 top_N_percentage_to_be_considered_as_anomaly+"\\% &"+
						 sample_N_previous_document+"&"+
						 "New word count-Threshold"+"&"+
						 total_prec/  (double) cnt_no_queries
						 +","+total_recall/  (double) cnt_no_queries
						 +","+total_F1/ (double)  cnt_no_queries
						 +","+total_accuracy/ (double)  cnt_no_queries
						 +"\\"
						 +"\n";				
				writer_debug.flush();
				
			}
			//END THRESHOLD NEW WORD
			}
			
			
			if(is_run_jaccard_only){
			//START JACCARD
			{
				total_prec=0.;   total_recall=0.;   total_F1=0.;total_accuracy=0; 
				int TP_NEW_JACCARD_TOTAL=0; int total_TP_JACCARD_TOTAL=0; cnt_no_queries=0;
				int FP_NEW_JACCARD_TOTAL=0; int TN_NEW_JACCARD_TOTAL=0;int FN_NEW_JACCARD_TOTAL=0;
				int total_TN_JACCARD_TOTAL=0;
				// each query process marked anomalies (using TOTAL NEW JACCARD) and compare to ground Truth.
				for(String curr_query4:map_Query_anomalousDocIdASKEY_for_AVG_JACCARD_SORTED.keySet()){
					cnt_no_queries++;
					TP_NEW_JACCARD_TOTAL=0;FP_NEW_JACCARD_TOTAL=0;
					System.out.println("curr_query5:"+curr_query4+" "+map_Query_anomalousDocIdASKEY_for_AVG_JACCARD_SORTED.get(curr_query4).size() 
										+" "+map_Query_anomalousDocIdASKEY_for_AVG_JACCARD_SORTED.get(curr_query4)
//										+" "+map_inFile_EachLine_of_document.get(map_Query_anomalousDocIdASKEY.get(curr_query3)).indexOf("ground_truth")
//										+" "+map_inFile_EachLine_of_document.get(map_Query_anomalousDocIdASKEY.get(curr_query3))
										);

					// TP
					for(int DocID:map_Query_anomalousDocIdASKEY_for_AVG_JACCARD_SORTED.get(curr_query4).keySet()){
//						System.out.println("1->"+map_inFile_EachLine_of_document.get(DocID)
//										+" idx->"+map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth"));
						
						if(map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth")>=0){
							TP_NEW_JACCARD_TOTAL++;
						}
						else{
							FP_NEW_JACCARD_TOTAL++;
						}
						
					}
					total_TP_JACCARD_TOTAL=total_TP_JACCARD_TOTAL+TP_NEW_JACCARD_TOTAL;
					 
					TN_NEW_JACCARD_TOTAL=0;FN_NEW_JACCARD_TOTAL=0;
					 //TN
					 TreeMap<Integer, String> map_docID_gtTP_currQuery= map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.get(curr_query4);
					 
					 for(int DocID:map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query4).keySet()){
						 // 
						 if(!map_docID_gtTP_currQuery.containsKey(DocID) &&  !map_Query_anomalousDocIdASKEY_for_THRESHOLD_TOTAL_NEW_WORD_SORTED.get(curr_query4).containsKey(DocID )
								 && map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth")==-1 
							    ){
							 TN_NEW_JACCARD_TOTAL++; 	
						 }
						 else if(map_docID_gtTP_currQuery.containsKey(DocID) &&
								 !map_Query_anomalousDocIdASKEY_for_THRESHOLD_TOTAL_NEW_WORD_SORTED.get(curr_query4).containsKey(DocID )
								 && map_inFile_EachLine_of_document.get(DocID).indexOf("ground_truth")>=0 ) {
							 FN_NEW_JACCARD_TOTAL++;
						 }
					 }
					 total_TN_JACCARD_TOTAL=total_TN_JACCARD_TOTAL+TN_NEW_JACCARD_TOTAL;
					
					System.out.println("\nquery(5):"+curr_query4+" NEW JACCARD TP:"+TP_NEW_JACCARD_TOTAL
										+" precision:"+ (TP_NEW_JACCARD_TOTAL/(double)mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim.get(curr_query4))
										+" "+total_TP_JACCARD_TOTAL/ (double) map_Query_anomalousDocIdASKEY_for_AVG_JACCARD_SORTED.size()
										);
					
					double prec=(TP_NEW_JACCARD_TOTAL/(double)mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim.get(curr_query4));
					double recall=TP_NEW_JACCARD_TOTAL/(double)map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.get(curr_query4).size();
					double accuracy=(TP_NEW_JACCARD_TOTAL+TN_NEW_JACCARD_TOTAL)/ (double) map_QUERY_curr_DocID_N_query_matched_only_document.get(curr_query4).size();
					
					if(prec+recall>0){
						  F1=(2*(prec * recall )/ (prec+recall));
					}
					else{
						  F1=0;
					}
					total_prec=total_prec+prec;
					total_recall=total_recall+recall;
					total_F1=total_F1+F1;
					total_accuracy=total_accuracy+accuracy;
					
					writer_debug.append("\n query(5):"+curr_query4+" NEW JACCARD TP:"+TP_NEW_JACCARD_TOTAL
										+" precision:"+ prec
										+" recall:"+ recall
										+" F1:"+ F1
										+" accuracy:"+accuracy
										);
					writer_debug.flush();
						
				}

				writer_debug.append( "\n Precision(avg):"+total_prec/ (double)  cnt_no_queries
						 +" Recall(avg):"+total_recall/ (double)  cnt_no_queries
						 +" F1(avg):"+total_F1/ (double)  cnt_no_queries
						 +" Accuracy(avg):"+total_accuracy/ (double)  cnt_no_queries
						 +" denominator:"+ map_Query_anomalousDocIdASKEY_for_AVG_JACCARD_SORTED.size()
						 +"\n");
				t4_latex_out=
								 top_N_percentage_to_be_considered_as_anomaly+"\\% &"+
								 sample_N_previous_document+"&"+
								 "Jaccard"+"&"+
								 total_prec/ (double)  cnt_no_queries
								+","+total_recall/  (double) cnt_no_queries
								+","+total_F1/ (double)  cnt_no_queries
								+","+total_accuracy/ (double)  cnt_no_queries
								+"\\"
								+"\n";
				writer_debug.flush();
				
				
				
			} // END JACCARD
			

			} //if(is_run_jaccard_only){
			
			//END JACCARD
			
			writer_debug.append("\n for latex -----"+"\n"
													+"\\hline"
													+t1_latex_out.replace(",", "&")
													+"\\hline"
													+t2_latex_out.replace(",", "&")
													+"\\hline"
													+t3_latex_out.replace(",", "&")
													+"\\hline"
													+t4_latex_out.replace(",", "&")
													+"\\hline"
													+"\n-----end latex\n");
			writer_debug.flush();
			
			writer_debug.append("\n map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth.size:"
									+map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.size());
			
			// 
	    	System.out.println("Time Taken:"
								+ NANOSECONDS.toSeconds(System.nanoTime() - t0) + " seconds; "
								+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
								+ " minutes");
	    	//
	    	writer_debug.append("\n "+"Time Taken:"
	    							+ NANOSECONDS.toSeconds(System.nanoTime() - t0) + " seconds; "
	    							+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
	    							+ " minutes");
	    	writer_debug.flush();
			
			// ground truth count for each query
			for(String q:map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.keySet()){
				
				if(map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.containsKey(q))
					writer_debug.append("\n ground truth Doc count for each query: q:"+q+"<-->"+map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.get(q).size()
												);
				else
					writer_debug.append("\n ground truth Doc count for each query: q:"+q+"<-->"+"IS NULL");
						
				if(map_QUERY_curr_DocID_N_query_matched_only_document.containsKey(q))
					writer_debug.append("\n out of Total doc (matched on query) is  on q:"+q+"<-->"+map_QUERY_curr_DocID_N_query_matched_only_document.get(q).size() );
				else
					writer_debug.append("\n out of Total doc (matched on query) is  on q:"+q+"<-->"+"IS NULL");
				
				// % of document as anomaly 
				if(map_QUERY_curr_DocID_N_query_matched_only_document.containsKey(q) && map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.containsKey(q)){
					writer_debug.append(" \n Ground Truth-% of document as anomaly out of total for query:" 
									+q +"-->"+ map_QUERY_curr_DocID_N_query_matched_only_document_ONLY_ground_truth_TP.get(q).size() 
																						/ (double) map_QUERY_curr_DocID_N_query_matched_only_document.get(q).size()
										+"\n");
					writer_debug.flush();
				}
				writer_debug.flush();
			}
			
			writer_debug.append("\n mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim:"+mapQuery_N_NoDocumentsAnomalousAfterSortedByCosineSim);
			writer_debug.append("\n top_N_percentage_to_be_considered_as_anomaly:"+top_N_percentage_to_be_considered_as_anomaly);
			
			writer_debug.append("\n ENTERING: total_documents_on_All_queries:"+total_documents_on_All_queries);
			writer_debug.flush();
			System.out.println("note: new word count uses the input file="+file_doc_ID_wordID_N_tf_idf+"_format_2.txt" );
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	//get JaccardCoefficient
	private static double get_JaccardCoefficient_of_currDocID(
															TreeMap<Integer, String> map_DocID_N_WordID_Freq_To_another_format_2,
															TreeMap<Integer, Integer> map_Interested_Past_or_Future_Documents,
															int docID,
															int Flag //{1,2} 1=total, 2=Average
															) {
		// TODO Auto-generated method stub
		double get_total_jaccard_currDocID=0;
		try {
			TreeMap<String, Double> map_doc1_tfidf_string=new TreeMap<String, Double>();
			TreeMap<String, Double> map_doc2_tfidf_string=new TreeMap<String, Double>();
			TreeMap<Integer,TreeMap<String,Double>> mapOut=new TreeMap<Integer, TreeMap<String,Double>>();
			 
				double total=0.;
				// 
				map_doc1_tfidf_string=convert_wordID_TFIDFString_into_TreeMap(map_DocID_N_WordID_Freq_To_another_format_2.get(docID));
				//
				for(int DocID2:map_Interested_Past_or_Future_Documents.keySet()){
					//
					map_doc2_tfidf_string=
							convert_wordID_TFIDFString_into_TreeMap(map_DocID_N_WordID_Freq_To_another_format_2.get(DocID2));
					//
					mapOut=Find_2_Map_word_not_exist_in_another.find_2_Map_word_not_exist_in_another(map_doc1_tfidf_string, map_doc2_tfidf_string);
	   			    // Jaccard exists(intersect) / (map1.size+map2.size)
					double d=mapOut.get(1).size() / (double) (map_doc1_tfidf_string.size()+map_doc2_tfidf_string.size());
//					System.out.println("intrersect:"+mapOut.get(1).size() +" d:"+d 
//											+" "+map_doc1_tfidf_string.size()+" "+map_doc2_tfidf_string.size());
					get_total_jaccard_currDocID=get_total_jaccard_currDocID+ d;
				}
				
				if(Flag==2){
					get_total_jaccard_currDocID=get_total_jaccard_currDocID/map_Interested_Past_or_Future_Documents.size();
				}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return get_total_jaccard_currDocID;
	}

	// get_newWords_of_currDocID
	private static int get_newWords_of_currDocID(TreeMap<Integer, String>  map_DocID_N_WordID_Freq_To_another_format_2,
												  TreeMap<Integer, Integer> map_Interested_Past_or_Future_Documents, 
												  int docID,
												  int Flag // {1,2} , 1->total and 2->average
												  ) {
		// TODO Auto-generated method stub
		int get_total_newWords_of_currDocID=0;
		try{
			
			TreeMap<String, Double> map_doc1_tfidf_string=new TreeMap<String, Double>();
			TreeMap<String, Double> map_doc2_tfidf_string=new TreeMap<String, Double>();
			TreeMap<Integer,TreeMap<String,Double>> mapOut=new TreeMap<Integer, TreeMap<String,Double>>();
			  
				// 
				map_doc1_tfidf_string=convert_wordID_TFIDFString_into_TreeMap(map_DocID_N_WordID_Freq_To_another_format_2.get(docID));
				//
				for(int DocID2:map_Interested_Past_or_Future_Documents.keySet()){
					//
					map_doc2_tfidf_string=
							convert_wordID_TFIDFString_into_TreeMap(map_DocID_N_WordID_Freq_To_another_format_2.get(DocID2));
					//
					mapOut=Find_2_Map_word_not_exist_in_another.find_2_Map_word_not_exist_in_another(map_doc1_tfidf_string, map_doc2_tfidf_string);
	   			    
					get_total_newWords_of_currDocID=get_total_newWords_of_currDocID+ mapOut.get(2).size();
				}
				
				if(Flag==2){
					get_total_newWords_of_currDocID=get_total_newWords_of_currDocID/map_Interested_Past_or_Future_Documents.size();
				}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return get_total_newWords_of_currDocID;
	}

	private static double getAverage_Cosine(TreeMap<Integer, String> map_DocID_N_WordID_Freq_To_another_format_2,
								  TreeMap<Integer, Integer> map_Interested_Past_or_Future_Documents,
								  int DocID
									) {
		// TODO Auto-generated method stub
		TreeMap<String, Double> map_doc1_tfidf_string=new TreeMap<String, Double>();
		TreeMap<String, Double> map_doc2_tfidf_string=new TreeMap<String, Double>();
		Cosine_similarity cs=new Cosine_similarity();
		double total=0.;
		try{
			//
			map_doc1_tfidf_string=convert_wordID_TFIDFString_into_TreeMap(map_DocID_N_WordID_Freq_To_another_format_2.get(DocID));
			
			
			for(int DocID2:map_Interested_Past_or_Future_Documents.keySet()){
				map_doc2_tfidf_string=
						convert_wordID_TFIDFString_into_TreeMap(map_DocID_N_WordID_Freq_To_another_format_2.get(DocID2));
				
   			    total=total+cs.calculateCosineSimilarity_approach_1(map_doc1_tfidf_string ,
   			    											  		map_doc2_tfidf_string);
   			    
   			    
			}
			
		}
		catch(Exception e){
		}
		
		
		return total/new Double(map_Interested_Past_or_Future_Documents.size());
	}

	// main
	public static void main(String[] args) throws Exception {

		
		// PREREQUISITION:
		// (1) //NOTE : Use readFile_sort_by_a_given_token_in_eachLine_write2OutputFile() method to create this INPUT (chrono sorted_ below file
		//		String interested_token_to_be_used_for_sorting_CSV="5,6,7";
		// (2) Run myAlgo2() before this method to get "debug_query_docID.txt"
		String neighb_folder="neigh3";
		String baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/";
			   baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/";
		String inputFile_chrono=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_URLs_NOT_exists_in_sample_ds1_AND_ground_truth_DATETIME_exists_AddLineNo_CHRONO.txt";
		String input_output_From_myAlgo2="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/"+neighb_folder
													+"/debug_query_docID.txt";
			   input_output_From_myAlgo2="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/"+neighb_folder
					   								+"/debug_query_docID.txt";
		
		int 	top_N_percentage_to_be_considered_as_anomaly=15;
		int		sample_N_previous_document=2;
		
		//baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/";
		
		//NOTE : Use readFile_sort_by_a_given_token_in_eachLine_write2OutputFile() method to create this below file 
		inputFile_chrono=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo_CHRONO.txt";
		inputFile_chrono=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo_CHRONO.txt";
		inputFile_chrono=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb2_normalizeDelimiter.txt";
		inputFile_chrono=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb2_normalizeDelimiterAddedGTruthToken.txt";
		
		/*
		 * Lenins-MacBook-Pro:All lenin$ ls
		ndtv.mai.label.collapse.all.txt		ndtv.mai.label.kidnapping.all.txt	ndtv.mai.label.senior.all.txt
		ndtv.mai.label.ebola.all.txt		ndtv.mai.label.migrant.all.txt		ndtv.mai.label.slavery.all.txt
		ndtv.mai.label.fire.all.txt		    ndtv.mai.label.mining.all.txt		ndtv.mai.label.swine.all.txt
		ndtv.mai.label.juvenile.all.txt		ndtv.mai.label.road.all.txt		ndtv.mai.label.traffick.all.txt
		 */
		
		String query_CSV="collapse,kidnap,senior citize,ebola,migrant,slavery,fire,mining,swine,juvenile,road acciden,traffick";
//			   query_CSV="collapse,kidnap,senior citize,ebola,migrant,slavery,fire,mining,swine,juvenile,road,traffick";
		// (1) fill "dummy" at last place to avoid splitting issue (2) dummy word replaced by method "calc_3_similarity"  
		String query_CSV_exclude="ceasefire#fire,,,,,,ceasefire#collapse,,,,,dummy";
		
				//"collapse,kidnap,senior,ebola,migrant,slavery,fire,mining,swine,juvenile,road,traffick";

		String inFile_Doc_ID_AND_tf_idf_file=""; 
		int top_N_lines_only_run_for_inputFile_chrono=-1;
		String baseFolder_out="";
		
		//debug
		String []arr_query_CSV=query_CSV.split(","); int cnt90=0;
		String []arr_query_CSV_exclude=query_CSV_exclude.split(",");
		System.out.println("1:"+arr_query_CSV.length+" 2:"+arr_query_CSV_exclude.length);
		//
		while(cnt90<arr_query_CSV.length){
			System.out.println(arr_query_CSV[cnt90]+" exclude:"+arr_query_CSV_exclude[cnt90]);
			cnt90++;
		}
		
		// filter on DOCID ( this file prepared from myAlgo2.txt output ).. same docID we use here so that total DocId matches.
		
		// calc_cosine_similarity
		Calc_3_similarity.
		calc_3_similarity( 		baseFolder,
								baseFolder_out,
								inputFile_chrono,
								"!#!#", //delimiter_4_inputFile_chrono 
								true, //is_inputFile_has_header
//								inFile_Doc_ID_AND_tf_idf_file,
								3, //token_index_having_text
								top_N_lines_only_run_for_inputFile_chrono,
								top_N_percentage_to_be_considered_as_anomaly, //top_N_percentage_to_be_considered_as_anomaly
								query_CSV,
								query_CSV_exclude,
								input_output_From_myAlgo2, //input
								sample_N_previous_document,  //sample_N_previous_document, //SAMPLING N prev document for similarity calc
								true,  //is_do_stemming
								true,  //isSOPdebug
								true,  //is_ignore_stop_words_all_together
								false,  //is_skip_TF_IDF_calc,( true will skip the TF-IDF process) <---NOTE:say false when you have new inputFile_chrono
								false,  //is_run_cosine_only
								true,   //is_run_newWord_only
								false,  //is_run_newWord_THRESHOLD_only
								false   //is_run_jaccard_only
								);
		
		
		System.out.println("baseFolder:"+baseFolder);
		System.out.println("NOTE:output is in for 4 similarities->"+baseFolder+"debug_cosine_sim.txt");
	}
	
}
