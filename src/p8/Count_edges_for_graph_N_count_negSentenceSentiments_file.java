package p8;

import crawler.LoadMultipleValueToMap2_AS_KEY_VALUE;

import java.util.TreeMap;

public class Count_edges_for_graph_N_count_negSentenceSentiments_file {
	
	//
	public static void count_sentences_with_1_negative_sentim(String baseFolder,
															 String inFile_person_origz_loc){
		int total_sentence_with_neg_sentime=0;
		TreeMap<String,String> map_curr_SEQ_sentiment=new TreeMap<String, String>();
		try{
			
	    	TreeMap<String, TreeMap<String,String>> map_lineNoNID_person_organiz_locati_numbers=
			    	MyAlgo.load_DocID_AND_person_organi_locati_numbers(
			    										baseFolder,
			    										inFile_person_origz_loc,
			    										"@@@@",
														 1,
														 2,//    token_having_person_mapString,
														 3,//    token_having_organiz_mapString,
														 4,//    token_having_location_mapString,
														 5,//    token_having_numbers_mapString,
														 6,//    token_having_nouns
														 7,//    token_having_sentiment
														 8,//    token_having_verbs
													9,
			    										 true //is_having_header
			    										);
	    	
	    	
	    	for(String docID:map_lineNoNID_person_organiz_locati_numbers.keySet()){
	    		
	    		map_curr_SEQ_sentiment=map_lineNoNID_person_organiz_locati_numbers.get(docID);
	    		
	    		if( map_curr_SEQ_sentiment.toString().indexOf("{1.0=") >=0){
	    			System.out.println(map_curr_SEQ_sentiment+" "+map_curr_SEQ_sentiment.get("1.0"));
	    			
	    			
	    			total_sentence_with_neg_sentime=total_sentence_with_neg_sentime+Integer.valueOf(map_curr_SEQ_sentiment.get("1.0"));
	    			//convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(map_curr_SEQ_sentiment.toString(), "=", false);
	    			
	    		}
	    		
	    	}
	    	
	    	System.out.println("total_sentence_with_neg_sentime:"+total_sentence_with_neg_sentime);
//	    	System.out.println(map_lineNoNID_person_organiz_locati_numbers);
	    	
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void count_edges_for_graph_file(String inFile){
		TreeMap<String, String> map_SS_key_value=new TreeMap();
		int count_number_of_edges=0; 
		//each line a vertex
		int no_of_vertex=0;
		try{

			//each line has format -> abated!!!1319,5028,8510
			map_SS_key_value=LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE(
																							  	  inFile,
																								 "!!!",
																								  "1,2",
																								   "",
																								  true,
																								  false //isPrintSOP
																								 );
			
			for(String key:map_SS_key_value.keySet()){
				count_number_of_edges=count_number_of_edges+map_SS_key_value.get(key).split(",").length;
			}
			
			
			 
			System.out.println("vertex.size="+map_SS_key_value.size()+" edge size:"+count_number_of_edges);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		String baseFolder_out="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/";
		String inFile=baseFolder_out+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_VERB_LINENO.txt_NODUP.txt";
		
		
//		inFile=baseFolder_out+
//		"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_NUMBER_LINENO.txt_NumberToken_LINENOCSV.txt";
		
		inFile=baseFolder_out+
		"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_ORGANIZ_LINENO.txtSTEMMED.txt";

		// method 1 
		//count_edges_for_graph_file(inFile);
		
		
		// verb-> vertex.size=9048 edge size:230681
		// numbers -> vertex.size=288 edge size:461
		// Organization -> vertex.size=1902 edge size:3860
		// No news -> 8433 - each has at  least 4 hard-coded nodes 
		// No sentences in total..for "S1" and "S2" in topology ->  63940
		
		// V=8433 +8433*4+63940+9048 +288+1902->117343 ; E=230681+461->231142
		
		//method 2
		inFile=baseFolder_out+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb.txt";
		count_sentences_with_1_negative_sentim( baseFolder_out,
												inFile
												);
		
	}

 


	 

}
