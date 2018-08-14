package p18;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import crawler.Stemmer;
import crawler.Generate_Random_Number_Range;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import weka.core.Tee;

import org.apache.commons.lang3.ArrayUtils;

import com.aliasi.util.Math;


public class MyAlgo_p18 {
	
	//convert convert_treeMapStringARRString_treeMapStringTreeMap
	public static  TreeMap<String, TreeMap<String, String> >  convert_treeMapStringARRString_treeMapStringTreeMap(
																			TreeMap<String, String[]> map_StringARRString
																			){
		TreeMap<String, TreeMap<String, String> > mapOut=new TreeMap<String, TreeMap<String,String>>();
		try {
			 
	    		 //
				for(String key:map_StringARRString.keySet()){
					
	    			String [] noun_lineno=map_StringARRString.get(key);
	    			int cnt=0;
	    			TreeMap<String, String> tempMap = new TreeMap<String, String>();
	    			while( cnt< noun_lineno.length ){
	    				tempMap.put( String.valueOf(cnt)  , noun_lineno[cnt] );
	    				cnt++;
	    			}
	    			//System.out.println("docID:"+docID+"-----"+noun.length );
	    			mapOut.put(key , tempMap);
	    		}
				
		} catch (Exception e) {
			e.printStackTrace( );
			// TODO: handle exception
		}
		return mapOut;
	}
	
	
	//myAlgo_p18
	/*
		public static void myAlgo_p18(
									String baseFolder,
									TreeMap<String, String[]> map_NounWord_N_arr_lineNo, // lineno=docid
									TreeMap<String, String[]> map_VerbWord_N_arr_lineNo,
									TreeMap<String, String[]> map_PersonWord_N_arr_lineNo,
									TreeMap<String, String[]> map_OrganizWord_N_arr_lineNo,
									TreeMap<String, String[]> map_LocationWord_N_arr_lineNo,
									TreeMap<String, String[]> map_NumberWord_N_arr_lineNo,
									TreeMap<String, TreeMap<String, String>> map_lineNoNID_person_organiz_locati_numbers_nouns,
									TreeMap<Integer,String> map_docID_Document, 
									TreeMap<String, String> map_word_lineCSVofCustomWords, //seeds
									TreeMap<String, String> map_URL_N_LABEL_from_humanANNOTATOR
									){
			System.out.println("---------------------");
			FileWriter debugWriter=null;
			
				int C1_count_seed_institute_in_noun=0;int C1_count_seed_institute_in_organiz=0; int C1_count_seed_institute_in_verb=0;
				int C1_count_seed_institute_in_location=0;
				int C1_global_count_seed_institute_in_noun=0;int C1_global_count_seed_institute_in_organiz=0; int C1_global_count_seed_institute_in_verb=0;
				int C1_global_count_seed_institute_in_location=0;
				int C1_global2_count_seed_institute_in_noun=0;int C1_global2_count_seed_institute_in_organiz=0; int C1_global2_count_seed_institute_in_verb=0;
				int C1_global2_count_seed_institute_in_location=0;
				
				int C2_count_seed_institute_in_noun=0;int C2_count_seed_institute_in_organiz=0; int C2_count_seed_institute_in_verb=0;
	   			int C2_count_seed_institute_in_location=0;
	   			int C2_global_count_seed_institute_in_noun=0;int C2_global_count_seed_institute_in_organiz=0; int C2_global_count_seed_institute_in_verb=0;
	   			int C2_global_count_seed_institute_in_location=0;
	   			int C2_global2_count_seed_institute_in_noun=0;int C2_global2_count_seed_institute_in_organiz=0; int C2_global2_count_seed_institute_in_verb=0;
	   			int C2_global2_count_seed_institute_in_location=0;
			
			try {
				debugWriter=new FileWriter(new File(baseFolder+"debug_p18.txt"));
				TreeMap<String, TreeMap<String, String>>
					map_NounWord_N_TreeMaplineNoAsVALUE=convert_treeMapStringARRString_treeMapStringTreeMap(map_NounWord_N_arr_lineNo);
				
				TreeMap<String, TreeMap<String, String>>
					map_VerbWord_N_TreeMaplineNoAsVALUE=convert_treeMapStringARRString_treeMapStringTreeMap(map_VerbWord_N_arr_lineNo);
				TreeMap<String, TreeMap<String, String>>
					map_PersonWord_N_TreeMaplineNoAsVALUE=convert_treeMapStringARRString_treeMapStringTreeMap(map_PersonWord_N_arr_lineNo);
				TreeMap<String, TreeMap<String, String>>
					map_OrganizWord_N_TreeMaplineNoAsVALUE=convert_treeMapStringARRString_treeMapStringTreeMap(map_OrganizWord_N_arr_lineNo);
				TreeMap<String, TreeMap<String, String>>
					map_LocationWord_N_TreeMaplineNoAsVALUE=convert_treeMapStringARRString_treeMapStringTreeMap(map_LocationWord_N_arr_lineNo);
				TreeMap<String, TreeMap<String, String>>
					map_NumberWord_N_TreeMaplineNoAsVALUE=convert_treeMapStringARRString_treeMapStringTreeMap(map_NumberWord_N_arr_lineNo);

				
				//cluster2 seed nodes
				TreeMap<Integer,String> map_seq_SeedInstitues_Cluster2 =new TreeMap<Integer, String>();
				map_seq_SeedInstitues_Cluster2.put(1000, "hindu");
				map_seq_SeedInstitues_Cluster2.put(111019, "muslim");
				map_seq_SeedInstitues_Cluster2.put(2987, "christian");
				map_seq_SeedInstitues_Cluster2.put(3108, "sikh");
				map_seq_SeedInstitues_Cluster2.put(901, "united states");
				
				
				//cluster1 seed nodes
				TreeMap<Integer,String> map_seq_SeedInstitues_Cluster1 =new TreeMap<Integer, String>();
				map_seq_SeedInstitues_Cluster1.put(1000, "hindu");
				map_seq_SeedInstitues_Cluster1.put(111019, "muslim");
				map_seq_SeedInstitues_Cluster1.put(2987, "christian");
				map_seq_SeedInstitues_Cluster1.put(3108, "sikh");
				
				map_seq_SeedInstitues_Cluster1.put(1, "assembl");
				map_seq_SeedInstitues_Cluster1.put(111, "assembly");
				map_seq_SeedInstitues_Cluster1.put(2, "board");
				map_seq_SeedInstitues_Cluster1.put(3, "colleg"); map_seq_SeedInstitues_Cluster1.put(333, "college");
				map_seq_SeedInstitues_Cluster1.put(4, "commissi");map_seq_SeedInstitues_Cluster1.put(444, "commission");
				map_seq_SeedInstitues_Cluster1.put(5, "congres");map_seq_SeedInstitues_Cluster1.put(555, "congress");
				map_seq_SeedInstitues_Cluster1.put(6, "court");
				map_seq_SeedInstitues_Cluster1.put(7, "institut");map_seq_SeedInstitues_Cluster1.put(777, "institute");
				//map_seq_SeedInstitues_Cluster1.put(9, "stated");
				map_seq_SeedInstitues_Cluster1.put(10, "supreme");
				map_seq_SeedInstitues_Cluster1.put(11, "universiti");map_seq_SeedInstitues_Cluster1.put(11000111, "universities");
				map_seq_SeedInstitues_Cluster1.put(12, "university");
				map_seq_SeedInstitues_Cluster1.put(13, "thinktank");map_seq_SeedInstitues_Cluster1.put(14, "research");
				map_seq_SeedInstitues_Cluster1.put(15, "advocacy");map_seq_SeedInstitues_Cluster1.put(16, "policy");
				map_seq_SeedInstitues_Cluster1.put(17,"religious liberty");
				map_seq_SeedInstitues_Cluster1.put(18,"human rights");
//				map_seq_SeedInstitues_Cluster1.put(19,"freedom of speech");
//				map_seq_SeedInstitues_Cluster1.put(20,"center for");
//				map_seq_SeedInstitues_Cluster1.put(21,"institute of");
//				map_seq_SeedInstitues_Cluster1.put(22,"international center");
//				map_seq_SeedInstitues_Cluster1.put(23,"for policy");
//				map_seq_SeedInstitues_Cluster1.put(24,"foundation");
//				map_seq_SeedInstitues_Cluster1.put(25,"house");
//				map_seq_SeedInstitues_Cluster1.put(26,"public policy");
//				map_seq_SeedInstitues_Cluster1.put(27,"progress");
//				map_seq_SeedInstitues_Cluster1.put(28,"watch");
//				map_seq_SeedInstitues_Cluster1.put(29,"academy of");
//				map_seq_SeedInstitues_Cluster1.put(30,"international peace");
//				map_seq_SeedInstitues_Cluster1.put(31,"group");
//				map_seq_SeedInstitues_Cluster1.put(32,"consortium");
//				map_seq_SeedInstitues_Cluster1.put(33,"affairs");
//				map_seq_SeedInstitues_Cluster1.put(34,"bureau");
//				map_seq_SeedInstitues_Cluster1.put(35,"rights panel");
				

				System.out.println("ENTERED myAlgo_p18()");
				int num_of_doc= map_docID_Document.size();
				
				// CLUSTER 1: get DocIDs of all seed of Cluster1
				TreeMap<String, TreeMap<String, String> > map_SeedValue_Cluster1_allDocIDsAsValues = new TreeMap<String, TreeMap<String,String>>();
	    		for(int seq:map_seq_SeedInstitues_Cluster1.keySet()){
	    			String curr_seed_institute=map_seq_SeedInstitues_Cluster1.get(seq);
	    			TreeMap<String, String> tmap_lineNoasValue= map_NounWord_N_TreeMaplineNoAsVALUE.get(curr_seed_institute);
	    			map_SeedValue_Cluster1_allDocIDsAsValues.put(curr_seed_institute, tmap_lineNoasValue);
	    		}
	    		
	    		// CLUSTER 2: get DocIDs of all seed of Cluster2
	    			TreeMap<String, TreeMap<String, String> > map_SeedValue_Cluster2_allDocIDsAsValues = new TreeMap<String, TreeMap<String,String>>();
	    		    for(int seq:map_seq_SeedInstitues_Cluster2.keySet()){
	    		    	String curr_seed_institute=map_seq_SeedInstitues_Cluster2.get(seq);
	    		    	TreeMap<String, String> tmap_lineNoasValue= map_NounWord_N_TreeMaplineNoAsVALUE.get(curr_seed_institute);
	    		    	map_SeedValue_Cluster2_allDocIDsAsValues.put(curr_seed_institute, tmap_lineNoasValue);
	    		    }
	    		
				// each document
		    	for(int docID:map_docID_Document.keySet()){
		    		TreeMap<Integer, Integer> map_random_neighor_docID_as_KEY=new TreeMap<Integer, Integer>();
		    		//debug break
		    		//if(docID>300) break;
		    		
		    		int noun_yes_count=0;int noun_no_count=0;
		    		
		    		if(docID==1) //header
		    			continue;
		    		
					//generate random docIDs list
					while(map_random_neighor_docID_as_KEY.size()<=5){
						int curr_random_docID=generate_Random_Number_Range.generate_Random_Number_Range(1, num_of_doc);
						map_random_neighor_docID_as_KEY.put( curr_random_docID, -1);
					}
					
		    		//NOUN
		    		for(String currNoun:map_NounWord_N_arr_lineNo.keySet()){
		    		   
		    		   TreeMap<String,String> TreeMap_Seq_lineNo = map_NounWord_N_TreeMaplineNoAsVALUE.get(currNoun);
		    		   //
		    		   if( TreeMap_Seq_lineNo.containsValue( String.valueOf(docID ) )){
		    			   noun_yes_count++;
		    			   //System.out.println("docId:"+docID+" yes");
		    		   }
		    		   else{
		    			   noun_no_count++;
		    			   //System.out.println("docId:"+docID+" no");
		    		   }
		    		 
		    		} // end for
		    		int organiz_yes_count=0;int organiz_no_count=0;
		    		//ORGANIZATION
		    		for(String currOrganiz:map_OrganizWord_N_arr_lineNo.keySet()){
			    		   
			    		   TreeMap<String,String> TreeMap_Seq_lineNo = map_OrganizWord_N_TreeMaplineNoAsVALUE.get(currOrganiz);
			    		   //
			    		   if( TreeMap_Seq_lineNo.containsValue( String.valueOf(docID ) )){
			    			   organiz_yes_count++;
			    			   //System.out.println("docId:"+docID+" yes");
			    		   }
			    		   else{
			    			   organiz_no_count++;
			    			   //System.out.println("docId:"+docID+" no");
			    		   }
			    		 
			    	} // end for
			    	
		    		int count_seed_institute=0;
		    		String curr_matched_seed_institute_csv="";
		   			String documentLine=map_docID_Document.get(docID);
		   			C1_count_seed_institute_in_noun=0;C1_count_seed_institute_in_organiz=0; C1_count_seed_institute_in_verb=0;
		   			C1_count_seed_institute_in_location=0;
		   			C1_global_count_seed_institute_in_noun=0;C1_global_count_seed_institute_in_organiz=0; C1_global_count_seed_institute_in_verb=0;
		   			C1_global_count_seed_institute_in_location=0;
		   			C1_global2_count_seed_institute_in_noun=0;C1_global2_count_seed_institute_in_organiz=0; C1_global2_count_seed_institute_in_verb=0;
		   			C1_global2_count_seed_institute_in_location=0;
		   			
		    		//CLUSTER1 ---- match on map_seq_SeedInstitues_Cluster1
		    		for(int seq:map_seq_SeedInstitues_Cluster1.keySet()){
		    			String curr_seed_institute=map_seq_SeedInstitues_Cluster1.get(seq);
		    			// count_seed_institute present in document?
		    			if(documentLine.toLowerCase().indexOf( curr_seed_institute.toLowerCase() ) > 0 ){
		    				curr_matched_seed_institute_csv=curr_matched_seed_institute_csv+"!!!"+curr_seed_institute;
		    				count_seed_institute++;
		    			}
		    			//is it available part of noun
		    			if(map_NounWord_N_TreeMaplineNoAsVALUE.containsKey(curr_seed_institute)){
		    				if(map_NounWord_N_TreeMaplineNoAsVALUE.get(curr_seed_institute).containsValue(String.valueOf(docID))){
		    					C1_count_seed_institute_in_noun++;
		    					C1_global_count_seed_institute_in_noun++;
		    				}
		    				C1_global2_count_seed_institute_in_noun++;
		    			}
		    			//is it available part of organiz
		    			if(map_OrganizWord_N_TreeMaplineNoAsVALUE.containsKey(curr_seed_institute)){
			    			if(map_OrganizWord_N_TreeMaplineNoAsVALUE.get(curr_seed_institute).containsValue(String.valueOf(docID))){
			    				C1_count_seed_institute_in_organiz++;
		    					C1_global_count_seed_institute_in_organiz++;
		    				}
			    			C1_global2_count_seed_institute_in_organiz++;
		    			}
		    			//is it available part of verb
		    			if(map_VerbWord_N_TreeMaplineNoAsVALUE.containsKey(curr_seed_institute)){
			    			if(map_VerbWord_N_TreeMaplineNoAsVALUE.get(curr_seed_institute).containsValue(String.valueOf(docID))){
			    				C1_count_seed_institute_in_verb++;
		    					C1_global_count_seed_institute_in_verb++;
		    				}
			    			C1_global2_count_seed_institute_in_verb++;
		    			}
		    			//is it available part of location
		    			if(map_LocationWord_N_TreeMaplineNoAsVALUE.containsKey(curr_seed_institute)){
			    			if(map_LocationWord_N_TreeMaplineNoAsVALUE.get(curr_seed_institute).containsValue(String.valueOf(docID))){
			    				C1_count_seed_institute_in_location++;
			    				C1_global_count_seed_institute_in_location++;
		    				}
			    			C1_global2_count_seed_institute_in_location++;
		    			}
		    			
		    		}
		    		
		    		//
		    		C2_count_seed_institute_in_noun=0;C2_count_seed_institute_in_organiz=0; C2_count_seed_institute_in_verb=0;
		   			C2_count_seed_institute_in_location=0;
		   			C2_global_count_seed_institute_in_noun=0;C2_global_count_seed_institute_in_organiz=0; C2_global_count_seed_institute_in_verb=0;
		   			C2_global_count_seed_institute_in_location=0;
		   			C2_global2_count_seed_institute_in_noun=0;C2_global2_count_seed_institute_in_organiz=0; C2_global2_count_seed_institute_in_verb=0;
		   			C2_global2_count_seed_institute_in_location=0;
		    		
		    		
		    		// CLUSTER 2
		    		for(int seq:map_seq_SeedInstitues_Cluster2.keySet()){
		    			String curr_seed_institute=map_seq_SeedInstitues_Cluster2.get(seq);
		    			// count_seed_institute present in document?
		    			if(documentLine.toLowerCase().indexOf( curr_seed_institute.toLowerCase() ) > 0 ){
		    				curr_matched_seed_institute_csv=curr_matched_seed_institute_csv+"!!!"+curr_seed_institute;
		    				count_seed_institute++;
		    			}
		    			//is it available part of noun
		    			if(map_NounWord_N_TreeMaplineNoAsVALUE.containsKey(curr_seed_institute)){
		    				if(map_NounWord_N_TreeMaplineNoAsVALUE.get(curr_seed_institute).containsValue(String.valueOf(docID))){
		    					C2_count_seed_institute_in_noun++;
		    					C2_global_count_seed_institute_in_noun++;
		    				}
		    				C2_global2_count_seed_institute_in_noun++;
		    			}
		    			//is it available part of organiz
		    			if(map_OrganizWord_N_TreeMaplineNoAsVALUE.containsKey(curr_seed_institute)){
			    			if(map_OrganizWord_N_TreeMaplineNoAsVALUE.get(curr_seed_institute).containsValue(String.valueOf(docID))){
			    				C2_count_seed_institute_in_organiz++;
			    				C2_global_count_seed_institute_in_organiz++;
		    				}
			    			C2_global2_count_seed_institute_in_organiz++;
		    			}
		    			//is it available part of verb
		    			if(map_VerbWord_N_TreeMaplineNoAsVALUE.containsKey(curr_seed_institute)){
			    			if(map_VerbWord_N_TreeMaplineNoAsVALUE.get(curr_seed_institute).containsValue(String.valueOf(docID))){
			    				C2_count_seed_institute_in_verb++;
			    				C2_global_count_seed_institute_in_verb++;
		    				}
			    			C2_global2_count_seed_institute_in_verb++;
		    			}
		    			//is it available part of location
		    			if(map_LocationWord_N_TreeMaplineNoAsVALUE.containsKey(curr_seed_institute)){
			    			if(map_LocationWord_N_TreeMaplineNoAsVALUE.get(curr_seed_institute).containsValue(String.valueOf(docID))){
			    				C2_count_seed_institute_in_location++;
			    				C2_global_count_seed_institute_in_location++;
		    				}
			    			C2_global2_count_seed_institute_in_location++;
		    			}
		    			
		    		}

		    		System.out.println("docId:"+docID+ " yes_count:"+noun_yes_count+" no_count:"+noun_no_count
		    						+" count_seed_institute:"+count_seed_institute
		    						+" curr_matched_seed_institute_csv:"+curr_matched_seed_institute_csv);
		    		
		    		debugWriter.append("docId:"+docID+ " yes_count:"+noun_yes_count+" no_count:"+noun_no_count
		    							+ " yes_count:"+noun_yes_count+" no_count:"+noun_no_count
		    							+ " 2.yes_count:"+organiz_yes_count+" 2.no_count:"+organiz_no_count
			    						+" count_seed_institute:"+count_seed_institute
			    						+" count_seed_institute_in_noun:(C1,C2)="+C1_count_seed_institute_in_noun+","+C2_count_seed_institute_in_noun
			    						+" count_seed_institute_in_organiz:(C1,C2)="+C1_count_seed_institute_in_organiz+","+C2_count_seed_institute_in_organiz
			    						+" count_seed_institute_in_verb:(C1,C2)="+C1_count_seed_institute_in_verb+","+C2_count_seed_institute_in_verb
			    						+" count_seed_institute_in_location:(C1,C2)="+C1_count_seed_institute_in_location +","+C2_count_seed_institute_in_location
			    						+" gTruth:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get( docID+":8"  ).get("1")
			    						+" curr_matched_seed_institute_csv:"+curr_matched_seed_institute_csv+"\n");
		    		debugWriter.flush();
		    		
		    		 
		    		// System.out.println(map_lineNoNID_person_organiz_locati_numbers_nouns.get(docID+":8").get("1"));
		    		// calculate between map_word_lineCSVofCustomWords and each of document in iteration
		    		
		    		
		    		
		    	} // end for
				
		    	//
				System.out.println(  C1_global_count_seed_institute_in_noun+","+C2_global_count_seed_institute_in_noun
									+" "+C1_global_count_seed_institute_in_organiz+","+C2_global_count_seed_institute_in_organiz
									+" "+C1_global_count_seed_institute_in_verb+","+C2_global_count_seed_institute_in_verb
									+" "+C1_global_count_seed_institute_in_location+","+C2_global_count_seed_institute_in_location);
				
				System.out.println(  C1_global2_count_seed_institute_in_noun+","+C2_global2_count_seed_institute_in_noun
									+" "+C1_global2_count_seed_institute_in_organiz+","+C2_global2_count_seed_institute_in_organiz
									+" "+C1_global2_count_seed_institute_in_verb+","+C2_global2_count_seed_institute_in_verb
									+" "+C1_global2_count_seed_institute_in_location+","+C2_global2_count_seed_institute_in_location
									);
				
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
		}
		*/
	
	//myAlgo_p18_2 (this method has no external clusters as mentioned in myAlgo_p18()
	public static void myAlgo_p18_2(
								String baseFolder,
								int											number_of_neighbors,
								TreeMap<String, String[]> 					map_NounWord_N_arr_lineNo, // lineno=docid
								TreeMap<String, String[]> 					map_VerbWord_N_arr_lineNo,
								TreeMap<String, String[]> 					map_PersonWord_N_arr_lineNo,
								TreeMap<String, String[]> 					map_OrganizWord_N_arr_lineNo,
								TreeMap<String, String[]> 					map_LocationWord_N_arr_lineNo,
								TreeMap<String, String[]> 					map_NumberWord_N_arr_lineNo,
								TreeMap<String, TreeMap<String, String>> 	map_lineNoNID_person_organiz_locati_numbers_nouns,
								TreeMap<Integer,String> 					map_docID_Document, 
								TreeMap<String, String> 					map_word_lineCSVofCustomWords, //seeds
								TreeMap<String, String> 					map_URL_N_LABEL_from_humanANNOTATOR,
								TreeMap<String, TreeMap<Integer,Integer>> 	map_URL_mapLINEnoHAVINGquotesASkey,
								TreeMap<String, Integer> 					map_URL_lineNoRDocID,  // 
								TreeMap<Integer, String> 					map_lineNoRdocID_URL,
								TreeMap<String, String> 					map_word_emotionCSV,
						    	TreeMap<String, String> 					map_STEMMEDword_emotionCSV,
						    	TreeMap<Integer,Integer> 					map_sortedbyDATEtime_RANKseq_n_docID,
								TreeMap<Integer,Integer> 					map_sortedbyDATEtime_docID_n_RANKseq,
								TreeMap<String, String>						map_sortedbyDATEtime_URL_n_DATE,
								TreeMap<Integer, String>					map_sortedbyDATEtime_docID_n_DATE,
								String									 	output_for_WEKA,
								String										output_for_CaseStudyBias1_TPonly,
								String 										output_for_CaseStudyBias1_ALL,
								boolean										is_run_for_WEKAinputFile
								){
		System.out.println("---------------------");
		FileWriter debugWriter=null;int hit_count_currVerb_withEmotions=0;
		int organiz_yes_count_4_currDocID=0;
		double alpha=0.3; double beta=0.7;
		String neighbor_1_featuresString="";
		String neighbor_2_featuresString="";
		    int    count_IF_count=0;
			int    count_ELSE_count=0;
			int noun_yes_count=0;int noun_no_count=0;
    		int verb_yes_count=0;int verb_no_count=0;
		TreeMap<Integer, Integer> map_docID_countDistinctNouns=new TreeMap<Integer, Integer>();
		TreeMap<Integer, Integer> map_docID_cnt=new TreeMap<Integer, Integer>();
		double CC=0.;
		Stemmer stemmer=new Stemmer();
		double gamma=0.3; double mu=0.7;
			int C1_count_seed_institute_in_noun=0;int C1_count_seed_institute_in_organiz=0; int C1_count_seed_institute_in_verb=0;
			int C1_count_seed_institute_in_location=0;
			int C1_global_count_seed_institute_in_noun=0;int C1_global_count_seed_institute_in_organiz=0; int C1_global_count_seed_institute_in_verb=0;
			int C1_global_count_seed_institute_in_location=0;
			int C1_global2_count_seed_institute_in_noun=0;int C1_global2_count_seed_institute_in_organiz=0; int C1_global2_count_seed_institute_in_verb=0;
			int C1_global2_count_seed_institute_in_location=0;
			
			int C2_count_seed_institute_in_noun=0;int C2_count_seed_institute_in_organiz=0; int C2_count_seed_institute_in_verb=0;
   			int C2_count_seed_institute_in_location=0;
   			int C2_global_count_seed_institute_in_noun=0;int C2_global_count_seed_institute_in_organiz=0; int C2_global_count_seed_institute_in_verb=0;
   			int C2_global_count_seed_institute_in_location=0;
   			int C2_global2_count_seed_institute_in_noun=0;int C2_global2_count_seed_institute_in_organiz=0; int C2_global2_count_seed_institute_in_verb=0;
   			int C2_global2_count_seed_institute_in_location=0;
   			
   			TreeMap<Integer, Integer> map_docID_predictedLabel=new TreeMap<Integer, Integer>();
		
   			TreeMap<Integer, Integer> map_docID_N_total_count_distinctEmotions_4_nouns=new TreeMap<Integer, Integer>();
   			TreeMap<Integer, Integer> map_docID_N_hit_count_currNoun_withEmotionss=new TreeMap<Integer, Integer>();
   			TreeMap<Integer, Double> map_docID_N_AVG_on_total_count_distinctEmotions_4_noun=new TreeMap<Integer, Double>();
   			
   			TreeMap<Integer, Integer> map_docID_N_total_count_distinctEmotions_4_verbs=new TreeMap<Integer, Integer>();
   			TreeMap<Integer, Integer> map_docID_N_hit_count_currVerb_withEmotionss=new TreeMap<Integer, Integer>();
   			
   			TreeMap<Integer, Double> map_docID_N_total_fn_count_distinctEmotions_4_verbs=new TreeMap<Integer, Double>();
   			
   			TreeMap<Integer, Double> map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_nouns=new TreeMap<Integer, Double>();
   			TreeMap<Integer, Double> map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs=new TreeMap<Integer, Double>();
   			
   			TreeMap<Integer,Integer> map_docID_N_TOTAL_ALL_NEIGHB_total_count_distinctEmotions_4_verbs=new TreeMap<Integer, Integer>();
   			
   			TreeMap<Integer, Double> map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs=new TreeMap<Integer, Double>();
   			
   			TreeMap<Integer,Double> map_docID_N_TOTAL_ALL_NEIGHB_total_fncount_distinctEmotions_4_verb=new TreeMap<Integer, Double>();
   			
   			TreeMap<Integer, Integer> map_docID_N_TOTAL_ALL_NEIGHB_total_count_distinctOrganization=new TreeMap<Integer, Integer>();
    		TreeMap<Integer, Double> map_docID_N_TOTAL_ALL_NEIGHB_total_count_distinctfnOrganization=new TreeMap<Integer, Double>();
    		
   			TreeMap<Integer, Double> map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Nouns=new TreeMap<Integer, Double>();
   			TreeMap<Integer, Double> map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization=new TreeMap<Integer, Double>();
   			TreeMap<Integer, Double> map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization=new TreeMap<Integer, Double>();
   			TreeMap<Integer, Double> map_docID_N_CC=new TreeMap<Integer, Double>();
   			
   			
			TreeMap<Integer, String> map_docID_N_concHITverbsForEmotions=new TreeMap<Integer, String>();
			TreeMap<Integer, String> map_docID_N_concHITEmotions=new TreeMap<Integer, String>();
			TreeMap<Integer, String> map_docID_N_concOrganization=new TreeMap<Integer, String>();
			
			String comment10="";
   			double curr_fn_verb=0.;
   			double curr_fn_organization=0.;
   			TreeMap<Integer, Integer> map_distinctDocIDsFromVerbMap1=new TreeMap<Integer, Integer>();
			TreeMap<Integer, Integer> map_distinctDocIDsFromVerbMap2=new TreeMap<Integer, Integer>();
			
			TreeMap<Integer, Integer> map_distinctDocIDsFromVerb_docID_N_countVerbs=new TreeMap<Integer, Integer>(); 
			
		try {
			FileWriter writerWeka=new FileWriter(output_for_WEKA);
			
			FileWriter writer_output_for_CaseStudyBias1_TPonly=new FileWriter(output_for_CaseStudyBias1_TPonly);
			FileWriter writer_output_for_CaseStudyBias1_ALL=new FileWriter(output_for_CaseStudyBias1_ALL);
			
			debugWriter=new FileWriter(new File(baseFolder+"debug_p18.txt"));
			TreeMap<String, TreeMap<String, String>>
				map_NounWord_N_TreeMaplineNoAsVALUE=convert_treeMapStringARRString_treeMapStringTreeMap(map_NounWord_N_arr_lineNo);
			
			TreeMap<String, TreeMap<String, String>>
				map_VerbWord_N_TreeMaplineNoAsVALUE=convert_treeMapStringARRString_treeMapStringTreeMap(map_VerbWord_N_arr_lineNo);
			TreeMap<String, TreeMap<String, String>>
				map_PersonWord_N_TreeMaplineNoAsVALUE=convert_treeMapStringARRString_treeMapStringTreeMap(map_PersonWord_N_arr_lineNo);
			TreeMap<String, TreeMap<String, String>>
				map_OrganizWord_N_TreeMaplineNoAsVALUE=convert_treeMapStringARRString_treeMapStringTreeMap(map_OrganizWord_N_arr_lineNo);
			TreeMap<String, TreeMap<String, String>>
				map_LocationWord_N_TreeMaplineNoAsVALUE=convert_treeMapStringARRString_treeMapStringTreeMap(map_LocationWord_N_arr_lineNo);
			TreeMap<String, TreeMap<String, String>>
				map_NumberWord_N_TreeMaplineNoAsVALUE=convert_treeMapStringARRString_treeMapStringTreeMap(map_NumberWord_N_arr_lineNo);
            
			System.out.println("ENTERED myAlgo_p18()");
			int num_of_doc= map_docID_Document.size();
			TreeMap<Integer, Integer> map_docID_OrganizationCount=new TreeMap<Integer, Integer>();
			TreeMap<Integer, Double> map_docID_fnOrganizationCount=new TreeMap<Integer, Double>();
			
			TreeMap<Integer, TreeMap<Integer, Integer>> map_DOCID_N_ITSrandom_neighor_docID_as_KEY=new TreeMap<Integer, TreeMap<Integer, Integer>>();
			
    		TreeMap<Integer, Integer> map_random_neighor_docID_as_KEY=new TreeMap<Integer, Integer>();
    		

			// each document (fill with zero for each hit count for verb)
	    	for(int docID:map_docID_Document.keySet()){
	    		map_docID_N_hit_count_currVerb_withEmotionss.put(docID, 0);
	    	}
    		

  		  noun_yes_count=0;  noun_no_count=0;
  		  verb_yes_count=0;  verb_no_count=0;
			//(statistics on how many distinct documents (lineno) has a specific context such as noun, verb , organization
			// each document 
	    	for(int docID:map_docID_Document.keySet()){
	    		//debug break
	    		//if(docID>300) break;
	    		
	    		
//	    		if(docID==1) //header
//	    			continue;

	    		//NOUN - START
				int hit_count_currNoun_withEmotions=0; int total_count_distinctEmotions_4_nouns=0;
				
				
	    		//NOUN
	    		for(String currNoun:map_NounWord_N_arr_lineNo.keySet()){
	    		   
	    		   TreeMap<String,String> TreeMap_Seq_lineNo = map_NounWord_N_TreeMaplineNoAsVALUE.get(currNoun);
	    		   
	    		   //count number of noun for each docID
	    		   for(String seq2:TreeMap_Seq_lineNo.keySet()){
	    			   String currDocID=TreeMap_Seq_lineNo.get(seq2);
	    			   
	    			   if(map_docID_countDistinctNouns.containsKey(currDocID)){
	    				   int newcount=map_docID_countDistinctNouns.get(currDocID)+1;
	    				   map_docID_countDistinctNouns.put(Integer.valueOf(currDocID), newcount);
	    			   }
	    			   else{
	    				   map_docID_countDistinctNouns.put(Integer.valueOf(currDocID), 1);
	    			   }
	    			   
	    		   }
	    		   
	    		   //
	    		   if( TreeMap_Seq_lineNo.containsValue( String.valueOf(docID ) )){
	    			   noun_yes_count++;
	    			   //System.out.println("docId:"+docID+" yes");	    			   
	    			   // stem the noun
		    		   String stemmed_currNoun=stemmer.stem(currNoun);

	    			   System.out.println("currNoun:"+currNoun+" emotion:"+map_STEMMEDword_emotionCSV.get(stemmed_currNoun));
		    		   
		    		   if(map_STEMMEDword_emotionCSV.containsKey(stemmed_currNoun)){
		    			   //total (stemmed noun)
		    			   total_count_distinctEmotions_4_nouns+=map_STEMMEDword_emotionCSV.get(stemmed_currNoun).split(",").length;
		    			   hit_count_currNoun_withEmotions++;
		    		   }
		    		   else if(map_word_emotionCSV.containsKey(currNoun)){
		    			   //total
		    			   total_count_distinctEmotions_4_nouns+=map_word_emotionCSV.get(currNoun).split(",").length;
		    			   hit_count_currNoun_withEmotions++;
		    		   }
	    			   //save
		    		   map_docID_N_total_count_distinctEmotions_4_nouns.put(docID, total_count_distinctEmotions_4_nouns);
		    		   map_docID_N_hit_count_currNoun_withEmotionss.put(docID, hit_count_currNoun_withEmotions);
		    		   
	    		   }
	    		   else{
	    			   noun_no_count++;
	    			   //System.out.println("docId:"+docID+" no");
	    		   }
	    		} // end for
	    		//NOUN - END

	    		// VERB -- START
				int total_count_distinctEmotions_4_verbs=0;

				
	    		// VERB
	    		for(String currVerb:map_VerbWord_N_arr_lineNo.keySet()){
	    		   
	    		   TreeMap<String,String> TreeMap_Seq_lineNo = map_VerbWord_N_TreeMaplineNoAsVALUE.get(currVerb);
	    		   
	    		   int number_of_documents_having_this_verb=TreeMap_Seq_lineNo.size();
	    		   
	    		   map_distinctDocIDsFromVerbMap1.put(docID, -1);
	    		   
	    		   //
    			   if(map_distinctDocIDsFromVerb_docID_N_countVerbs.containsKey(docID)){
    				   int newcount=map_distinctDocIDsFromVerb_docID_N_countVerbs.get(docID)+1;
    				   map_distinctDocIDsFromVerb_docID_N_countVerbs.put(docID, newcount);
    			   }
    			   else{
    				   map_distinctDocIDsFromVerb_docID_N_countVerbs.put(docID, 1);
    			   }
	    		   
	    		   //
	    		   if( TreeMap_Seq_lineNo.containsValue( String.valueOf(docID ) )
	    				 && number_of_documents_having_this_verb>1 //newly added  
	    				 ){
	    			   // catch distinct docIS which has verb
	    			   map_distinctDocIDsFromVerbMap2.put(docID, -1);
	    		
	    			   
	    			   verb_yes_count++;
	    			   //System.out.println("docId:"+docID+" yes");	    			   
	    			   // stem the verb
		    		   String stemmed_currVerb=stemmer.stem(currVerb);

	    			   System.out.println("currVerb:"+currVerb+" emotion:"+map_STEMMEDword_emotionCSV.get(stemmed_currVerb));
	    			   String temp_verb_stemmed_or_notStemmed="";
	    			   String temp_emotions_="";
		    		   //
		    		   if(map_STEMMEDword_emotionCSV.containsKey(stemmed_currVerb)){
		    			   //total (stemmed VERB)
		    			   total_count_distinctEmotions_4_verbs+=map_STEMMEDword_emotionCSV.get(stemmed_currVerb).split(",").length;
		    			   hit_count_currVerb_withEmotions++;
		    			   //
		    			   temp_verb_stemmed_or_notStemmed=stemmed_currVerb;
		    			   temp_emotions_=map_STEMMEDword_emotionCSV.get(stemmed_currVerb);
		    		   }
		    		   else if(map_word_emotionCSV.containsKey(currVerb)){
		    			   //total
		    			   total_count_distinctEmotions_4_verbs+=map_word_emotionCSV.get(currVerb).split(",").length;
		    			   hit_count_currVerb_withEmotions++;
		    			   //
		    			   temp_verb_stemmed_or_notStemmed=currVerb;
		    			   temp_emotions_=map_word_emotionCSV.get(currVerb);
		    		   }
	    			   //save
		    		   map_docID_N_total_count_distinctEmotions_4_verbs.put(docID, total_count_distinctEmotions_4_verbs);
		    		   map_docID_N_total_fn_count_distinctEmotions_4_verbs.put(docID, 
		    				   												   Double.valueOf(1./(1.+(total_count_distinctEmotions_4_verbs
		    				   														   				  *total_count_distinctEmotions_4_verbs))
		    				   													)
		    				   												  );
		    	      
		    		   		
		    		   // for this docID, how many verbs which are also appearing in emotional lexicon 
		    		   // , with or without any emotions available for it as 1
		    		   map_docID_N_hit_count_currVerb_withEmotionss.put(docID, hit_count_currVerb_withEmotions);
		    		   // catch all verbs HIT for emotions for a docID
		    		   if( map_docID_N_concHITverbsForEmotions.containsKey(docID ) ){
		    			   if(temp_verb_stemmed_or_notStemmed.length()>0){
			    			   String temp_Verb=map_docID_N_concHITverbsForEmotions.get(docID)+"<->"+temp_verb_stemmed_or_notStemmed;
			    			   map_docID_N_concHITverbsForEmotions.put(docID , temp_Verb);
		    			   }
		    			   //
		    			   if(temp_emotions_.length()>0){
			    			   String temp_emotion=map_docID_N_concHITEmotions.get(docID)+"<->"+temp_emotions_;
			    			   map_docID_N_concHITEmotions.put(docID, temp_emotion);
		    			   }
		    		   }
		    		   else{
		    			   if(temp_verb_stemmed_or_notStemmed.length()>0){
			    			   map_docID_N_concHITverbsForEmotions.put(docID, temp_verb_stemmed_or_notStemmed);
			    			   map_docID_N_concHITEmotions.put(docID, temp_emotions_);
		    			   }
		    		   }
	    		   }
	    		   else{
	    			   verb_no_count++;
	    			   //System.out.println("docId:"+docID+" no");
	    		   }
	    		} // end for
	    		// VERB -- END
	    			    		
	    		//ORGANIZATION
	    		int organiz_no_count_4_currDocID=0;
//	    		//
	    		for(String currOrganiz:map_OrganizWord_N_arr_lineNo.keySet()){
		    		   
		    		   TreeMap<String,String> TreeMap_Seq_lineNo = map_OrganizWord_N_TreeMaplineNoAsVALUE.get(currOrganiz);
		    		   int number_of_documents_having_this_organiz=TreeMap_Seq_lineNo.size();
		    		   //
		    		   if( TreeMap_Seq_lineNo.containsValue( String.valueOf(docID ))
		    				  && number_of_documents_having_this_organiz > 1 //new rule
		    				  ){
		    			   organiz_yes_count_4_currDocID++;
		    			   //System.out.println("docId:"+docID+" yes");
		    			   
		    			   //save organization for dbug
		        		   if( map_docID_N_concOrganization.containsKey(docID ) ){
			    			   String temp_organiz=map_docID_N_concOrganization.get(docID)+"<->"+currOrganiz;
			    			   map_docID_N_concHITverbsForEmotions.put(docID , temp_organiz);
			    		   }
			    		   else{
			    			   map_docID_N_concOrganization.put(docID, currOrganiz);
			    		   }
		    		   }
		    		   else{
		    			   organiz_no_count_4_currDocID++;
		    			   //System.out.println("docId:"+docID+" no");
		    		   }
		    		 
		    	} // end for(String currOrganiz:map_OrganizWord_N_arr_lineNo.keySet()){
		    	 
	    		//for each docID, save the organization present count..
	    		map_docID_OrganizationCount.put(docID,organiz_yes_count_4_currDocID);
	    		map_docID_fnOrganizationCount.put(docID,
	    									    Double.valueOf(1./(1.+(organiz_yes_count_4_currDocID*organiz_yes_count_4_currDocID) )) );
	    		
//	   			if(docID%100==0)
	   				System.out.println("docId:"+docID+ " yes_count:"+noun_yes_count+" no_count:"+noun_no_count
	   									+" total_count_distinctEmotions_4_nouns:"+total_count_distinctEmotions_4_nouns
	   									+" hit_count_currNoun_withEmotions:"+hit_count_currNoun_withEmotions);
	    		 
	   				
	   			//
//	   			debugWriter.append("\n map_lineNoNID_person_organiz_locati_numbers_nouns:"+map_lineNoNID_person_organiz_locati_numbers_nouns);
	   			debugWriter.flush();
	   			
//	   			debugWriter.append("\n count.docId:"+docID+ " yes_count:"+noun_yes_count+" no_count:"+noun_no_count);
	   			debugWriter.flush();
	    		debugWriter.append("\n docId:"+docID+ " yes_count:"+noun_yes_count+" no_count:"+noun_no_count
	    							+ " yes_count:"+noun_yes_count+" no_count:"+noun_no_count
	    							+ " 2.yes_count:"+organiz_yes_count_4_currDocID+" 2.no_count:"+organiz_no_count_4_currDocID
		    						+" count_seed_institute_in_noun:(C1,C2)="+C1_count_seed_institute_in_noun+","+C2_count_seed_institute_in_noun
		    						+" count_seed_institute_in_organiz:(C1,C2)="+C1_count_seed_institute_in_organiz+","+C2_count_seed_institute_in_organiz
		    						+" count_seed_institute_in_verb:(C1,C2)="+C1_count_seed_institute_in_verb+","+C2_count_seed_institute_in_verb
		    						+" count_seed_institute_in_location:(C1,C2)="+C1_count_seed_institute_in_location +","+C2_count_seed_institute_in_location
		    						+" gTruth:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get( docID+":8"  ).get("1")
		    						+" SIZE:"+map_lineNoNID_person_organiz_locati_numbers_nouns.size()
		    						+"\n");
	    		debugWriter.flush();
	    		
	    		// System.out.println(map_lineNoNID_person_organiz_locati_numbers_nouns.get(docID+":8").get("1"));
	    		// calculate between map_word_lineCSVofCustomWords and each of document in iteration
	    		
	    	} // end for for(int docID:map_docID_Document.keySet()){
			
	    	TreeMap<Integer, TreeMap<Integer, Integer > > map_docID_NEIGH_map_neighbDocID_N_EmotionVerbCount =new TreeMap<Integer, TreeMap<Integer,Integer>>();
	    	TreeMap<Integer, TreeMap<Integer, Integer > > map_docID_NEIGH_map_neighbDocID_N_OrganizationCount =new TreeMap<Integer, TreeMap<Integer,Integer>>();
	    	TreeMap<Integer, TreeMap<Integer, Double > > map_docID_NEIGH_map_neighbDocID_N_fnEmotionVerbCount =new TreeMap<Integer, TreeMap<Integer,Double>>();
	    	TreeMap<Integer, TreeMap<Integer, Double > > map_docID_NEIGH_map_neighbDocID_N_fnOrganizationCount =new TreeMap<Integer, TreeMap<Integer,Double>>();
	    	
	    	// if NOT any verb for a particular docID not present , put 1 /// bcos (1/1+0)=1 
    		for(int docID:map_docID_Document.keySet()){
    			if(!map_docID_N_total_count_distinctEmotions_4_verbs.containsKey(docID)){
    				 map_docID_N_total_count_distinctEmotions_4_verbs.put(docID, 1);
    			}
    			if(!map_docID_N_total_fn_count_distinctEmotions_4_verbs.containsKey(docID)){
		    		 map_docID_N_total_fn_count_distinctEmotions_4_verbs.put(docID, 1.);
    			}
    		}
	    	//
	    	int flag_4_stragegy_neighbors=2;
			// each document  --CALCULATE AVERAGE OF NEIGHBORS.....
	    	for(int docID:map_docID_Document.keySet()){
	    		TreeMap<Integer, Integer> map_docID_organizationCount_TEMP=new TreeMap<Integer, Integer>();
	    		TreeMap<Integer, Double> map_docID_fnorganizationCount_TEMP=new TreeMap<Integer, Double>();
	    		
	    		TreeMap<Integer, Integer> map_docID_EmotionsCountForVerb_TEMP=new TreeMap<Integer, Integer>();
	    		TreeMap<Integer, Double> map_docID_fnEmotionsCountForVerb_TEMP=new TreeMap<Integer, Double>();
	    		
	    		//reset
	    		map_random_neighor_docID_as_KEY=new TreeMap<Integer, Integer>();
	    		
	    		if(flag_4_stragegy_neighbors==1){
		    		//STRATEGY 1: generate random docIDs list ---- GENERATE RANDOM docIDs as NEIGHBORS
					while(map_random_neighor_docID_as_KEY.size()<= 50){
						int curr_random_docID=Generate_Random_Number_Range.generate_Random_Number_Range(2, num_of_doc);
						map_random_neighor_docID_as_KEY.put( curr_random_docID, -1);
						//
						if(map_random_neighor_docID_as_KEY.size()>=number_of_neighbors){
							break;
						}
					}
	    		}
				int N_preceding_docs=number_of_neighbors;
	    		//STRATEGY 2: 				
				TreeMap<Integer, Integer> map_preceding_N_document_docIDs_AS_KEY=new TreeMap<Integer, Integer>();
				// get preceding N document DocIDs
	    		if(flag_4_stragegy_neighbors==2){
//	    			System.out.println("ERROR docID:"+docID);
	    			if(map_sortedbyDATEtime_docID_n_RANKseq.containsKey(docID)){
						int start_seq_of_docID= map_sortedbyDATEtime_docID_n_RANKseq.get(docID) - N_preceding_docs;
						int end_seq_of_docID= map_sortedbyDATEtime_docID_n_RANKseq.get(docID) + N_preceding_docs;
						// get N docIDs
							while(start_seq_of_docID<=end_seq_of_docID){
								
								if(start_seq_of_docID!=docID && start_seq_of_docID>=2){
									map_random_neighor_docID_as_KEY.put(start_seq_of_docID, -1);
								}
								 // break
								if(map_random_neighor_docID_as_KEY.size()>=N_preceding_docs) break;
								
								start_seq_of_docID++;
							}
	    			}
	    			else{
	    				//if this docID NOT available, go to random
	    				//STRATEGY 1: generate random docIDs list ---- GENERATE RANDOM docIDs as NEIGHBORS
						while(map_random_neighor_docID_as_KEY.size()<= 50){
							int curr_random_docID=Generate_Random_Number_Range.generate_Random_Number_Range(2, num_of_doc);
							map_random_neighor_docID_as_KEY.put( curr_random_docID, -1);
							//
							if(map_random_neighor_docID_as_KEY.size()>=number_of_neighbors){
								break;
							}
						}
	    			}
	    		}
	    		
	    		//SAVE FOR each DocID, its neighbours DocID
	    		map_DOCID_N_ITSrandom_neighor_docID_as_KEY.put(docID, map_random_neighor_docID_as_KEY);
	    		
	    		// NEIGHBOR - average count for DISTINCT EMOTIONS of NOUN...
	    		int cnt=0; int TOTAL_ALL_NEIGHB_total_count_distinctEmotions_4_noun=0; double AVG_ALL_NEIGHB_total_count_distinctEmotions_4_noun=0.0;
	    		// 
	    		for(int neighb_curr_docID:map_random_neighor_docID_as_KEY.keySet()){
	    			cnt++;
	    			if(map_docID_N_total_count_distinctEmotions_4_nouns.containsKey(neighb_curr_docID)){
	    				TOTAL_ALL_NEIGHB_total_count_distinctEmotions_4_noun+=map_docID_N_total_count_distinctEmotions_4_nouns.get(neighb_curr_docID);
	    			}
	    		}
	    		AVG_ALL_NEIGHB_total_count_distinctEmotions_4_noun=TOTAL_ALL_NEIGHB_total_count_distinctEmotions_4_noun/ (double) cnt;
	    		//SAVE
	    		map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_nouns.put(docID, AVG_ALL_NEIGHB_total_count_distinctEmotions_4_noun);
	    		
	    		// NEIGHBOR - average count for DISTINCT EMOTIONS of VERB...
	    		cnt=0; int TOTAL_ALL_NEIGHB_total_count_distinctEmotions_4_verb=0; double AVG_ALL_NEIGHB_total_count_distinctEmotions_4_verb=0.0;
	    		
	    		cnt=0; double TOTAL_ALL_NEIGHB_total_fncount_distinctEmotions_4_verb=0; double AVG_ALL_NEIGHB_total_fncount_distinctEmotions_4_verb=0.0;
	    		// emotions of VERB
	    		for(int neighb_curr_docID:map_random_neighor_docID_as_KEY.keySet()){
	    			cnt++;
	    			if(map_docID_N_total_count_distinctEmotions_4_verbs.containsKey(neighb_curr_docID)){
	    				TOTAL_ALL_NEIGHB_total_count_distinctEmotions_4_verb+=map_docID_N_total_count_distinctEmotions_4_verbs.get(neighb_curr_docID);
	    				//debug
	    				map_docID_EmotionsCountForVerb_TEMP.put(neighb_curr_docID , map_docID_N_total_count_distinctEmotions_4_verbs.get(neighb_curr_docID));
	    			}
	    			//
	    			if(map_docID_N_total_fn_count_distinctEmotions_4_verbs.containsKey(neighb_curr_docID)){
	    				TOTAL_ALL_NEIGHB_total_fncount_distinctEmotions_4_verb+=map_docID_N_total_fn_count_distinctEmotions_4_verbs.get(neighb_curr_docID);
	    				//debug
	    				map_docID_fnEmotionsCountForVerb_TEMP.put(neighb_curr_docID , map_docID_N_total_fn_count_distinctEmotions_4_verbs.get(neighb_curr_docID));
	    			}
	    		}
	    		AVG_ALL_NEIGHB_total_count_distinctEmotions_4_verb=TOTAL_ALL_NEIGHB_total_count_distinctEmotions_4_verb/ (double) cnt;
	    		AVG_ALL_NEIGHB_total_fncount_distinctEmotions_4_verb=TOTAL_ALL_NEIGHB_total_fncount_distinctEmotions_4_verb/ (double) cnt;
	    		//SAVE
	    		map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.put(docID, AVG_ALL_NEIGHB_total_count_distinctEmotions_4_verb);
	    		map_docID_N_TOTAL_ALL_NEIGHB_total_count_distinctEmotions_4_verbs.put(docID, TOTAL_ALL_NEIGHB_total_count_distinctEmotions_4_verb);
	    		//SAVE
	    		map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.put(docID, AVG_ALL_NEIGHB_total_fncount_distinctEmotions_4_verb);
	    		map_docID_N_TOTAL_ALL_NEIGHB_total_fncount_distinctEmotions_4_verb.put(docID, Double.valueOf(TOTAL_ALL_NEIGHB_total_fncount_distinctEmotions_4_verb));
	    		//
	    		map_docID_cnt.put(docID, cnt);
	    		//debugging
	    		map_docID_NEIGH_map_neighbDocID_N_EmotionVerbCount.put(docID , map_docID_EmotionsCountForVerb_TEMP);
	    		
	    		// NEIGHBOR - average count for ORGANIZATION...
	    		cnt=0; int TOTAL_ALL_NEIGHB_total_count_distinctOrganization=0; 
	    		double AVG_ALL_NEIGHB_total_count_distinctOrganization=0.0;
	    		
	    		double TOTAL_ALL_NEIGHB_total_count_distinctfnOrganization=0;
	    		double AVG_ALL_NEIGHB_total_count_distinctfnOrganization=0.0;
	    		// ORGANIZATION
	    		for(int neighb_curr_docID:map_random_neighor_docID_as_KEY.keySet()){
	    			cnt++;
	    			if(map_docID_OrganizationCount.containsKey(neighb_curr_docID)){
//	    				TOTAL_ALL_NEIGHB_total_count_distinctOrganization+=map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(neighb_curr_docID);
	    				TOTAL_ALL_NEIGHB_total_count_distinctOrganization+=map_docID_OrganizationCount.get(neighb_curr_docID);
	    				
	    				//debug
	    				map_docID_organizationCount_TEMP.put(neighb_curr_docID , map_docID_OrganizationCount.get(neighb_curr_docID));
	    				//
	    				TOTAL_ALL_NEIGHB_total_count_distinctfnOrganization+=map_docID_fnOrganizationCount.get(neighb_curr_docID);
	    				//debug
	    				map_docID_fnorganizationCount_TEMP.put(neighb_curr_docID , map_docID_fnOrganizationCount.get(neighb_curr_docID));
	    			}
	    		}
	    		 
	    		AVG_ALL_NEIGHB_total_count_distinctOrganization=TOTAL_ALL_NEIGHB_total_count_distinctOrganization/ (double) cnt;	    		
	    		AVG_ALL_NEIGHB_total_count_distinctfnOrganization=TOTAL_ALL_NEIGHB_total_count_distinctfnOrganization/ (double) cnt;
	    		
	    		//SAVE
	    		map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.put(docID, AVG_ALL_NEIGHB_total_count_distinctOrganization);
	    		map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.put(docID, AVG_ALL_NEIGHB_total_count_distinctfnOrganization);
	    		
	    		//SAVE
	    		map_docID_N_TOTAL_ALL_NEIGHB_total_count_distinctOrganization.put(docID, TOTAL_ALL_NEIGHB_total_count_distinctOrganization);
	    		map_docID_N_TOTAL_ALL_NEIGHB_total_count_distinctfnOrganization.put(docID, TOTAL_ALL_NEIGHB_total_count_distinctfnOrganization);
	    		 
	    		//debugging
	    		map_docID_NEIGH_map_neighbDocID_N_OrganizationCount.put(docID , map_docID_organizationCount_TEMP);
	    		map_docID_NEIGH_map_neighbDocID_N_EmotionVerbCount.put(docID , map_docID_EmotionsCountForVerb_TEMP);
	    		map_docID_NEIGH_map_neighbDocID_N_fnOrganizationCount.put(docID , map_docID_fnorganizationCount_TEMP);
	    		map_docID_NEIGH_map_neighbDocID_N_fnEmotionVerbCount.put(docID , map_docID_fnEmotionsCountForVerb_TEMP);
	    		
	    		// NEIGHBOR - average count for NOUN...
	    		cnt=0; int TOTAL_ALL_NEIGHB_total_count_distinctNouns=0; double AVG_ALL_NEIGHB_total_count_distinctNouns=0.0;
	    		// 
	    		for(int neighb_curr_docID:map_random_neighor_docID_as_KEY.keySet()){
	    			cnt++;
	    			if(map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Nouns.containsKey(neighb_curr_docID)){
	    				TOTAL_ALL_NEIGHB_total_count_distinctNouns+=map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Nouns.get(neighb_curr_docID);
	    			}
	    		}
	    		AVG_ALL_NEIGHB_total_count_distinctNouns=TOTAL_ALL_NEIGHB_total_count_distinctNouns/ (double) cnt;
	    		//SAVE
	    		map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Nouns.put(docID, AVG_ALL_NEIGHB_total_count_distinctNouns);
	    		
	    	}
	    	
	    	
	    	
	    	// run for WEKA
	    	if(is_run_for_WEKAinputFile){
	    	
		    	writerWeka.append("1,2,3,4,label\n");
		    	writer_output_for_CaseStudyBias1_TPonly.append("CC!!!hit_count_currVerb_withEmotionss!!!total_count_distinctEmotions_4_verbs!!!OrganizationCount!!!"
											    		+ "N_Avg_Total_count_distinctEmotions_4_verb!!!"
											    		+ "N_Avg_Total_fncount_distinctEmotions_4_verb!!!"
											    		+ "N_Avg_Total_count_distinct_Organization!!!"
											    		+ "N_Avg_Total_count_distinct_fnOrganization\n");
		    	writer_output_for_CaseStudyBias1_ALL.append("CC!!!hit_count_currVerb_withEmotionss!!!total_count_distinctEmotions_4_verbs!!!OrganizationCount!!!"
												    		+ "N_Avg_Total_count_distinctEmotions_4_verb!!!"
												    		+ "N_Avg_Total_fncount_distinctEmotions_4_verb!!!"
												    		+ "N_Avg_Total_count_distinct_Organization!!!"
												    		+ "N_Avg_Total_count_distinct_fnOrganization"
												    		+"!!!TOTAL_ALL_NEIGHB_total_count_distinctEmotions_4_verbs!!!TOTAL_ALL_NEIGHB_total_fncount_distinctEmotions_4_verb!!!"
												    		+"TOTAL_ALL_NEIGHB_total_count_distinctOrganization!!!TOTAL_ALL_NEIGHB_total_count_distinctfnOrganization"
												    		+"!!!cnt"
												    		+ "\n");
		    	
											    	
		    	//Weka OUTPUT
		    	for(int currDocID:map_docID_Document.keySet()){
		    		//
		    		if(Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("1")) == 0){
		    			writerWeka.append(""+map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
		    							  +"," +map_docID_OrganizationCount.get(currDocID)
		    							  +"," +map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID) //neighbors  
		    							  +"," + map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(currDocID)//neighbors
		    							  +",yes\n");	
		    		}
		    		if(Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("1")) == 1){
		    			writerWeka.append(""+map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
		    							  +"," +map_docID_OrganizationCount.get(currDocID)
		    							  +"," +map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID) //neighbors  
		    							  +"," + map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(currDocID)//neighbors
		    							  +",no\n");	
		    		}
		    		writerWeka.flush();
		    		
		    		TreeMap<Integer, Integer> map_NeighbourIDs_ASKEYS_forGivenDocID=new TreeMap<Integer, Integer>();
		    		
		    		if(map_DOCID_N_ITSrandom_neighor_docID_as_KEY.containsKey(currDocID))
		    			map_NeighbourIDs_ASKEYS_forGivenDocID=map_DOCID_N_ITSrandom_neighor_docID_as_KEY.get(currDocID);
		    		
		    		int neighb1_docID=-1;int neighb2_docID=-1;
		    		
		    		int cnt=0;
		    		//get the neighb1's docID and neighb2's docID 
		    		for(int curr_neighb_docID:map_NeighbourIDs_ASKEYS_forGivenDocID.keySet()){
		    			cnt++;
		    			 if(cnt==1)
		    				 neighb1_docID=curr_neighb_docID;
		    			 if(cnt==2)
		    				 neighb2_docID=curr_neighb_docID;
		    		}
		    		
		    		neighbor_1_featuresString
		    								=		map_docID_N_hit_count_currVerb_withEmotionss.get(neighb1_docID)
												+"!!!"+map_docID_N_total_count_distinctEmotions_4_verbs.get(neighb1_docID)
												+"!!!"+map_docID_OrganizationCount.get(neighb1_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(neighb1_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(neighb1_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(neighb1_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(neighb1_docID);
		    		
		    		neighbor_2_featuresString
											=		map_docID_N_hit_count_currVerb_withEmotionss.get(neighb2_docID)
												+"!!!"+map_docID_N_total_count_distinctEmotions_4_verbs.get(neighb2_docID)
												+"!!!"+map_docID_OrganizationCount.get(neighb2_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(neighb2_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(neighb2_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(neighb2_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(neighb2_docID);
		    		
		    		
		    		if(map_lineNoNID_person_organiz_locati_numbers_nouns.containsKey(neighb1_docID+":8") &&
		    				map_lineNoNID_person_organiz_locati_numbers_nouns.containsKey(neighb2_docID+":8")
		    				){
		    		
//		    			System.out.println("map_docID_N_CC.containsKey(currDocID):"+map_docID_N_CC.containsKey(currDocID)+"<--->"+currDocID);
		    			System.out.println("map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+:8  ).get(0):"
		    								+map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("0"));
		    			
		    			if(map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("0")==null) continue;
		    			
		    		//
		    		if( 
//		    			Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( neighb1_docID+":8"  ).get("1"))>0 &&
//		    			Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( neighb2_docID+":8"  ).get("1"))>0
		    			Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("0"))>0
		    			&& 
		    			map_docID_N_CC.containsKey(currDocID)
		    				){
		    		
				    		//write all the basic feature (verb, emotion, organiz) for URL_TP, URL_neighb1, URL_neighb2
		    			writer_output_for_CaseStudyBias1_TPonly.append(      
//				    												 map_docID_N_CC.get(currDocID)0
//				    												+
				    												"!!!"+map_docID_N_hit_count_currVerb_withEmotionss.get(currDocID)
				    												+"!!!"+map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
				    												+"!!!"+map_docID_OrganizationCount.get(currDocID)
				    												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID)
				    												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID)
				    												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(currDocID)
				    												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(currDocID)
				    												+"!!!"+map_NeighbourIDs_ASKEYS_forGivenDocID
				    												+"!!!"+neighb1_docID
				    												+"!!!"+neighb2_docID
				    												+"!!!"+map_sortedbyDATEtime_docID_n_DATE.get(currDocID)
				    												+"!!!"+map_sortedbyDATEtime_docID_n_DATE.get(neighb1_docID)
				    												+"!!!"+map_sortedbyDATEtime_docID_n_DATE.get(neighb2_docID)
				    												+"!!!TP_URL:"+map_lineNoRdocID_URL.get(currDocID)
				    												+"!!!"+map_lineNoRdocID_URL.get(neighb1_docID)
				    												+"!!!"+map_lineNoRdocID_URL.get(neighb2_docID)
				    												+"!!!neighb_1-->"
				    												+neighbor_2_featuresString
				    												+"!!!neighb_2-->"
				    												+neighbor_2_featuresString
				    												+"\n");
		    			writer_output_for_CaseStudyBias1_TPonly.flush();
		    		}
		    		
		    		}
		    		
		    	}
		    	
	    	} //if(is_run_for_WEKAinputFile){
	    	
	    	/// changeflag
	    	int Flag_2_choose_predictionAlgo=33333;
	    	//															3  = verb(emotion)+organization
	    	//															4  = verb(emotion) 
	    	// 33 		= 
	    	// 333 		==(GCVO)  				uses EMTION of verb + organization(finalized for paper KDD**) GCVO
	    	// 333333 	==(GCVO)  				uses EMTION of verb + organization(Single weight) GCVO
	    	// 3333 	==(GCV) (OBSOLETE)  	emotion of verbs  (finalized for paper) GCV
	    	// 33333 	==(GCV,weight)  		emotion of verbs WITH weights (finalized for paper KDD**) GCV
    		alpha=0.3;
    		
    		long t0 = System.nanoTime();
	    	// BEGIN - PREDICTION prediction
	    	if(Flag_2_choose_predictionAlgo==1){
		    	for(int currDocID:map_docID_Document.keySet()){
		    		
		    		System.out.println("1:"+map_docID_countDistinctNouns.get(currDocID)
		    							+" 2:"+map_docID_OrganizationCount.get(currDocID)
		    							+" 3:"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Nouns.get(currDocID)
		    							+" 4:"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(currDocID));
		    		if(map_docID_countDistinctNouns.containsKey(currDocID)){
		    			// 
		    			if( ( (map_docID_countDistinctNouns.get(currDocID) * map_docID_OrganizationCount.get(currDocID))  // 4
		    					-
		    					alpha *(
		    					map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Nouns.get(currDocID)* 
		    					 map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(currDocID)
		    					 )
		    					) >0
		    					){
		    				map_docID_predictedLabel.put( currDocID, 0);	
		    			}
		    			else{
		    				map_docID_predictedLabel.put( currDocID, 1);	
		    			}
		    			
		    		}
		    		else{
		    			map_docID_predictedLabel.put( currDocID, 1);
		    		}
		    		
		    	}
	    	}
	    	// NOUN + ORGANIZATION
	    	if(Flag_2_choose_predictionAlgo==2){
		    	for(int currDocID:map_docID_Document.keySet()){
		    		
		    		if(map_docID_N_total_count_distinctEmotions_4_nouns.containsKey(currDocID)){
		    			// 
		    			if( ((map_docID_N_total_count_distinctEmotions_4_nouns.get(currDocID) -
		    					map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_nouns.get(currDocID))
		    					 *
		    					 (map_docID_OrganizationCount.get(currDocID) 
		    					  - 
		    					  map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(currDocID))
		    					 )>0
		    					){
		    				System.out.println("predict yes:"+map_docID_OrganizationCount.get(currDocID)); //NOT BIASED
		    				map_docID_predictedLabel.put( currDocID, 0);	
		    			}
		    			else{
		    				System.out.println("predict no:"+map_docID_OrganizationCount.get(currDocID)); //BIASED
		    				map_docID_predictedLabel.put( currDocID, 1);	
		    			}
		    			
		    		}
		    		else{
		    			map_docID_predictedLabel.put( currDocID, 1);
		    		}
		    		
		    	}
	    	}
	    	alpha=0.3;beta=0.7;
	    	// uses EMTION of verb + organization
	    	if(Flag_2_choose_predictionAlgo==33){
	    		//	    		
		    	for(int currDocID:map_docID_Document.keySet()){
		    		//
		    		if(map_docID_N_total_count_distinctEmotions_4_verbs.containsKey(currDocID)){
		    			// 
		    			if(	 	  (( alpha* map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID) 
		    						* map_docID_OrganizationCount.get(currDocID)
		    					   )
		    						-
		    						( beta *
			    					  map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID)
			    					  *map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(currDocID)
			    					)
		    					  )
		    					 > 0
		    					){
		    				System.out.println("predict yes:"+map_docID_OrganizationCount.get(currDocID)); //NOT BIASED
		    				map_docID_predictedLabel.put( currDocID, 0);	
		    			}
		    			else{
		    				System.out.println("predict no:"+map_docID_OrganizationCount.get(currDocID)); //BIASED
		    				map_docID_predictedLabel.put( currDocID, 1);	
		    			}
		    			
		    		}
		    		else{
		    			map_docID_predictedLabel.put( currDocID, 1);
		    		}
		    		
		    	}
	    	}
	    	// uses EMTION of verb + organization
	    	if(Flag_2_choose_predictionAlgo==3){
	    		
		    	for(int currDocID:map_docID_Document.keySet()){
		    		
		    		if(map_docID_N_total_count_distinctEmotions_4_verbs.containsKey(currDocID)){
		    			
		    			System.out.println("1:"+map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
											+" 2:"+map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID)
											+" 3:"+map_docID_OrganizationCount.get(currDocID)
											+" 4:"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(currDocID));
		    			
		    			// 
		    			if( (	(map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID) 
		    						+
		    					 map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID)
		    					)
		    					 -
			    					 (map_docID_OrganizationCount.get(currDocID) 
			    							 + 
			    					  map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(currDocID)
			    					 )
		    					 ) > 0
		    					){
		    				System.out.println("predict yes:"+map_docID_OrganizationCount.get(currDocID)); //NOT BIASED
		    				map_docID_predictedLabel.put( currDocID, 0);	
		    			}
		    			else{
		    				System.out.println("predict no:"+map_docID_OrganizationCount.get(currDocID)); //BIASED
		    				map_docID_predictedLabel.put( currDocID, 1);	
		    			}
		    			
		    		}
		    		else{
		    			map_docID_predictedLabel.put( currDocID, 1);
		    		}
		    		
		    	}
	    	}
	    	// ONLY emotion of verbs
	    	if(Flag_2_choose_predictionAlgo==3333){
	    		for(int currDocID:map_docID_Document.keySet()){
		    		//
		    		if(map_docID_N_total_count_distinctEmotions_4_verbs.containsKey(currDocID)){
		    			count_IF_count++;
		    			
		    			CC=(	 map_docID_N_total_fn_count_distinctEmotions_4_verbs.get(currDocID)
		    					 -	map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID)  
		    					 );
		    			System.out.println("1:"+map_docID_N_total_fn_count_distinctEmotions_4_verbs.get(currDocID)
											+" 2:"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID));
		    			// 
		    			if( CC > 0
		    					){
		    				System.out.println("predict yes:"+map_docID_OrganizationCount.get(currDocID)); //NOT BIASED
		    				map_docID_predictedLabel.put( currDocID, 0);	
		    			}
		    			else{
		    				System.out.println("predict no:"+map_docID_OrganizationCount.get(currDocID)); //BIASED
		    				map_docID_predictedLabel.put( currDocID, 1);	
		    			}
		    		}
		    		else{
		    			count_ELSE_count++;
		    			map_docID_predictedLabel.put( currDocID, 1);
		    		}
		    		
		    	}
	    	}
	    	// only emotion of verbs (with weights)
	    	if(Flag_2_choose_predictionAlgo==33333){
	    		for(int currDocID:map_docID_Document.keySet()){
	    			gamma=0.3; mu=0.7;
		    		//
		    		if(map_docID_N_total_count_distinctEmotions_4_verbs.containsKey(currDocID)){
		    			
		    			 CC=(	gamma* Double.valueOf(map_docID_N_total_fn_count_distinctEmotions_4_verbs.get(currDocID))
		    					 -	mu*map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID)  
		    					 );
		    			
		    			 map_docID_N_CC.put(currDocID, CC);
		    			 
		    			System.out.println("1:"+map_docID_N_total_fn_count_distinctEmotions_4_verbs.get(currDocID)
											+" 2:"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID));
		    			// 
		    			if(CC < 0.
		    					){
		    				System.out.println("predict yes:"+map_docID_OrganizationCount.get(currDocID)); //NOT BIASED
		    				map_docID_predictedLabel.put( currDocID, 0);	
		    			}
		    			else{
		    				System.out.println("predict no:"+map_docID_OrganizationCount.get(currDocID)); //BIASED
		    				map_docID_predictedLabel.put( currDocID, 1);	
		    			}
		    		}
		    		else{
		    			count_ELSE_count++;
		    			map_docID_predictedLabel.put( currDocID, 1);
		    		}
		    		
		    	}
	    	}
	     	// uses EMTION of verb + organization
	    	if(Flag_2_choose_predictionAlgo==333){
	    		alpha=0.3;beta=0.7;
		    	for(int currDocID:map_docID_Document.keySet()){
		    		//
		    		if(map_docID_N_total_count_distinctEmotions_4_verbs.containsKey(currDocID)){
		    			count_IF_count++;
		    			System.out.println("1:"+map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
											+" 2:"+map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID)
											+" 3:"+map_docID_OrganizationCount.get(currDocID)
											+" 4:"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(currDocID));
		    			CC=(
	    					alpha* (
	    							//map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
	    							map_docID_N_total_fn_count_distinctEmotions_4_verbs.get(currDocID)
	    						   +
	    						    map_docID_fnOrganizationCount.get(currDocID)
	    						
	    					 //map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID)
	    					)
	    					 -
	    					 	 beta* (
	    					 			 map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID)  
		    							 + 
		    							 map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(currDocID)
		    					 )
	    					 );
		    		
		    			
		    			 //
		    			 map_docID_N_CC.put(currDocID, CC);
		    			// 
		    			if(CC > 0 ){
		    				System.out.println("predict yes:"+map_docID_OrganizationCount.get(currDocID)); //NOT BIASED
		    				map_docID_predictedLabel.put( currDocID, 0);
		    			}
		    			else{
		    				System.out.println("predict no:"+map_docID_OrganizationCount.get(currDocID)); //BIASED
		    				map_docID_predictedLabel.put( currDocID, 1);	
		    			} 
		    			

		    			// 
		    			if(CC<0.){
		    				comment10="-";
		    			}
		    			else{
		    				comment10="+";
		    			}
		    			
		    			// CaseStudyBias1
		    			writer_output_for_CaseStudyBias1_ALL.append("docID:"+currDocID
		    														+"!!!"+map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
		    														+"!!!"+map_docID_OrganizationCount.get(currDocID)
		    														+"!!!"+map_docID_N_total_fn_count_distinctEmotions_4_verbs.get(currDocID)
		    														+"!!!"+map_docID_fnOrganizationCount.get(currDocID)
		    														+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID)
		    														+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(currDocID)
		    														+"!!!"+CC
		    														+"!!!"+Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("1"))
		    														+"-"+map_docID_predictedLabel.get(currDocID)+"-"+comment10
		    														+"!!!"+map_lineNoRdocID_URL.get(currDocID)
		    														+"!!!"+map_docID_N_concHITEmotions.get(currDocID)
		    														+"!!!"+map_docID_N_concHITverbsForEmotions.get(currDocID)
		    														+"!!!"+map_docID_N_concOrganization.get(currDocID)
		    														+"\n"
		    														);
		    		}
		    		else{
		    			count_ELSE_count++;
		    			map_docID_predictedLabel.put( currDocID, 1);
		    			
		    			// 
		    			if(CC<0.){
		    				comment10="-";
		    			}
		    			else{
		    				comment10="+";
		    			}
		    			// CaseStudyBias1
		    			writer_output_for_CaseStudyBias1_ALL.append("docID:"+currDocID
		    														+"!!!"+map_docID_N_total_fn_count_distinctEmotions_4_verbs.get(currDocID)
		    														+"!!!"+map_docID_fnOrganizationCount.get(currDocID)
		    														+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID)
		    														+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(currDocID)
		    														+"!!!"+CC
		    														+"!!!"+Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("1"))
		    														+"-"+map_docID_predictedLabel.get(currDocID)+"-"+comment10
		    														+"!!!"+map_lineNoRdocID_URL.get(currDocID)
		    														+"!!!"+map_docID_N_concHITEmotions.get(currDocID)
		    														+"!!!"+map_docID_N_concHITverbsForEmotions.get(currDocID)
		    														+"!!!"+map_docID_N_concOrganization.get(currDocID)
		    														+"\n"
		    														);
		    			
		    		}
		    		
		    	}
	    	}
	    	//
	     	// uses EMTION of verb + organization (only 1 param)
	    	if(Flag_2_choose_predictionAlgo==333333){
	    		alpha=3.;
		    	for(int currDocID:map_docID_Document.keySet()){
		    		//
		    		if(map_docID_N_total_count_distinctEmotions_4_verbs.containsKey(currDocID)){
		    			count_IF_count++;
		    			System.out.println("1:"+map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
											+" 2:"+map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID)
											+" 3:"+map_docID_OrganizationCount.get(currDocID)
											+" 4:"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(currDocID));
		    			CC=(
	    					(
	    							//map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
	    							map_docID_N_total_fn_count_distinctEmotions_4_verbs.get(currDocID)
	    						   - alpha* map_docID_fnOrganizationCount.get(currDocID)
	    						
	    					 //map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID)
	    					)
//	    					 -
//	    					 	alpha* (
//	    					 			 map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID)  
//		    							 + 
//		    							 map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(currDocID)
//		    					 )
	    					 );
		    			
		    			 //
		    			 map_docID_N_CC.put(currDocID, CC);
		    			// 
		    			if(CC > 0 ){
		    				System.out.println("predict yes:"+map_docID_OrganizationCount.get(currDocID)); //NOT BIASED
		    				map_docID_predictedLabel.put( currDocID, 0);
		    			}
		    			else{
		    				System.out.println("predict no:"+map_docID_OrganizationCount.get(currDocID)); //BIASED
		    				map_docID_predictedLabel.put( currDocID, 1);	
		    			} 
		    			

		    			// 
		    			if(CC<0.){
		    				comment10="-";
		    			}
		    			else{
		    				comment10="+";
		    			}
		    			
		    			// CaseStudyBias1
		    			writer_output_for_CaseStudyBias1_ALL.append("docID:"+currDocID
		    														+"!!!"+map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
		    														+"!!!"+map_docID_OrganizationCount.get(currDocID)
		    														+"!!!"+map_docID_N_total_fn_count_distinctEmotions_4_verbs.get(currDocID)
		    														+"!!!"+map_docID_fnOrganizationCount.get(currDocID)
		    														+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID)
		    														+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(currDocID)
		    														+"!!!"+CC
		    														+"!!!"+Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("1"))
		    														+"-"+map_docID_predictedLabel.get(currDocID)+"-"+comment10
		    														+"!!!"+map_lineNoRdocID_URL.get(currDocID)
		    														+"!!!"+map_docID_N_concHITEmotions.get(currDocID)
		    														+"!!!"+map_docID_N_concHITverbsForEmotions.get(currDocID)
		    														+"!!!"+map_docID_N_concOrganization.get(currDocID)
		    														+"\n"
		    														);
		    		}
		    		else{
		    			count_ELSE_count++;
		    			map_docID_predictedLabel.put( currDocID, 1);
		    			
		    			// 
		    			if(CC<0.){
		    				comment10="-";
		    			}
		    			else{
		    				comment10="+";
		    			}
		    			// CaseStudyBias1
		    			writer_output_for_CaseStudyBias1_ALL.append("docID:"+currDocID
		    														+"!!!"+map_docID_N_total_fn_count_distinctEmotions_4_verbs.get(currDocID)
		    														+"!!!"+map_docID_fnOrganizationCount.get(currDocID)
		    														+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID)
		    														+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(currDocID)
		    														+"!!!"+CC
		    														+"!!!"+Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("1"))
		    														+"-"+map_docID_predictedLabel.get(currDocID)+"-"+comment10
		    														+"!!!"+map_lineNoRdocID_URL.get(currDocID)
		    														+"!!!"+map_docID_N_concHITEmotions.get(currDocID)
		    														+"!!!"+map_docID_N_concHITverbsForEmotions.get(currDocID)
		    														+"!!!"+map_docID_N_concOrganization.get(currDocID)
		    														+"\n"
		    														);
		    			
		    		}
		    		
		    	}
	    	}
	    	
	     	// ONLY uses EMTION of verb  
	    	if(Flag_2_choose_predictionAlgo==4){
	    		
		    	for(int currDocID:map_docID_Document.keySet()){
		    		
		    		if(map_docID_N_total_count_distinctEmotions_4_verbs.containsKey(currDocID)){
		    			// 
		    			if( ((map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID) -
		    					map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID))
		    					 )>0
		    					){
		    				System.out.println("predict yes:"+map_docID_OrganizationCount.get(currDocID)); //NOT BIASED
		    				map_docID_predictedLabel.put( currDocID, 0);	
		    			}
		    			else{
		    				System.out.println("predict no:"+map_docID_OrganizationCount.get(currDocID)); //BIASED
		    				map_docID_predictedLabel.put( currDocID, 1);	
		    			}
		    			
		    		}
		    		else{
		    			map_docID_predictedLabel.put( currDocID, 1);
		    		}
		    		
		    	}
	    	}
	    	//
	    	alpha=0.3; beta=0.7;
	    	// uses EMTION of verb + organization
	    	if(Flag_2_choose_predictionAlgo==5){
	    		
		    	for(int currDocID:map_docID_Document.keySet()){
		    		
		    		if(map_docID_N_total_count_distinctEmotions_4_verbs.containsKey(currDocID)){
		    			// 
		    			if( 
		    					alpha* 
		    					(map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
		    						*	 map_docID_OrganizationCount.get(currDocID))-
		    					beta*
		    					(map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID)
		    					  * map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(currDocID))
		    					   
		    					 >0
		    					){
		    				System.out.println("predict yes:"+map_docID_OrganizationCount.get(currDocID)); //NOT BIASED
		    				map_docID_predictedLabel.put( currDocID, 0);	
		    			}
		    			else{
		    				System.out.println("predict no:"+map_docID_OrganizationCount.get(currDocID)); //BIASED
		    				map_docID_predictedLabel.put( currDocID, 1);	
		    			}
		    			
		    		}
		    		else{
		    			map_docID_predictedLabel.put( currDocID, 1);
		    		}
		    		
		    	}
	    	}
	    	
	    	long end_time=System.nanoTime();
	    	double difference = (end_time - t0)/1e6;
	    	
	    	System.out.println("Time Taken:"
									+ NANOSECONDS.toSeconds(System.nanoTime() - t0) + " seconds; "
									+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
									+ " minutes"
									+" milli seconds:"+difference+" in milliseconds"
									);
	    	
	    	// END - PREDICTION prediction	    	
	    	
	    	// CONFUSION MATRIX
	    	int TN=0, TP=0, FP=0, FN=0;
	    	// each doc
	    	for(int currDocID:map_docID_Document.keySet()){
	    		
	    		// 0-->NOT-BIASED(YES); 1-->BIASED (NO)
	    		if(Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("1")) == 0
	    				&& map_docID_predictedLabel.get(currDocID) == 0
	    				){
//	    			TP++;
	    			TN++;
	    		}
	    		else if(Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("1")) == 1
	    				&& map_docID_predictedLabel.get(currDocID) == 1
	    				){
//	    			TN++;
	    			TP++;
	    		}
	    		else if(Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("1")) == 0
	    				&& map_docID_predictedLabel.get(currDocID) == 1
	    				){
//	    			FN++;
	    			FP++;
	    		}
	    		else if(Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( currDocID+":8"  ).get("1")) == 1
	    				&& map_docID_predictedLabel.get(currDocID) == 0
	    				){
//	    			FP++;
	    			FN++;
	    		}
	    		
	    	} //for(int currDocID:map_docID_Document.keySet()){
	    	
	    	double precision= TP/ ((double) TP+FP); double recall = TP/ ((double) TP+FN);
	    	double F1score= 2*(precision*recall) / (precision+recall);
	    	double accuracy = (TP+TN)/(double) (TP+TN+FP+FN);
	    	double total_articles=TP+TN+FP+FN;
	    	
	    	// 
	    	writer_output_for_CaseStudyBias1_TPonly.append("CC!!!hit_count_currVerb_withEmotionss!!!total_count_distinctEmotions_4_verbs!!!OrganizationCount!!!"
												    		+ "N_Avg_Total_count_distinctEmotions_4_verb!!!"
												    		+ "N_Avg_Total_fncount_distinctEmotions_4_verb!!!"
												    		+ "N_Avg_Total_count_distinct_Organization!!!"
												    		+ "N_Avg_Total_count_distinct_fnOrganization\n");
	    	
	    	String comment1="";
	    	
		    	//case study OUTPUT
		    	for(int currDocID:map_docID_Document.keySet()){
		    		
		    		TreeMap<Integer, Integer> map_NeighbourIDs_ASKEYS_forGivenDocID=new TreeMap<Integer, Integer>();
		    		
		    		if(map_DOCID_N_ITSrandom_neighor_docID_as_KEY.containsKey(currDocID))
		    			map_NeighbourIDs_ASKEYS_forGivenDocID=map_DOCID_N_ITSrandom_neighor_docID_as_KEY.get(currDocID);
		    		
		    		int neighb1_docID=-1;int neighb2_docID=-1;
		    		
		    		int cnt=0;
		    		//get the neighb1's docID and neighb2's docID 
		    		for(int curr_neighb_docID:map_NeighbourIDs_ASKEYS_forGivenDocID.keySet()){
		    			cnt++;
		    			 if(cnt==1)
		    				 neighb1_docID=curr_neighb_docID;
		    			 if(cnt==2)
		    				 neighb2_docID=curr_neighb_docID;
		    				 
		    			
		    		}
		    		
		    		neighbor_1_featuresString
		    								=		map_docID_N_hit_count_currVerb_withEmotionss.get(neighb1_docID)
												+"!!!"+map_docID_N_total_count_distinctEmotions_4_verbs.get(neighb1_docID)
												+"!!!"+map_docID_OrganizationCount.get(neighb1_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(neighb1_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(neighb1_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(neighb1_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(neighb1_docID);
		    		
		    		neighbor_2_featuresString
											=		map_docID_N_hit_count_currVerb_withEmotionss.get(neighb2_docID)
												+"!!!"+map_docID_N_total_count_distinctEmotions_4_verbs.get(neighb2_docID)
												+"!!!"+map_docID_OrganizationCount.get(neighb2_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(neighb2_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(neighb2_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(neighb2_docID)
												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(neighb2_docID);
		    		
		    		
		    		if(map_lineNoNID_person_organiz_locati_numbers_nouns.containsKey(neighb1_docID+":8") &&
		    				map_lineNoNID_person_organiz_locati_numbers_nouns.containsKey(neighb2_docID+":8")
		    				){
		    		
//		    			System.out.println("map_docID_N_CC.containsKey(currDocID):"+map_docID_N_CC.containsKey(currDocID)+"<--->"+currDocID);
		    		//
		    		if( 
//		    			Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( neighb1_docID+":8"  ).get("1"))>0 &&
//		    			Integer.valueOf(map_lineNoNID_person_organiz_locati_numbers_nouns.get( neighb2_docID+":8"  ).get("1"))>0
//		    			&& map_docID_N_CC.containsKey(currDocID) &&
		    			map_URL_N_LABEL_from_humanANNOTATOR.get( map_lineNoRdocID_URL.get(neighb1_docID)).equalsIgnoreCase("0")
		    				){
		    				//
		    				if(map_docID_N_hit_count_currVerb_withEmotionss.get(currDocID)==1) comment1+=">=1";
		    				if(map_docID_N_hit_count_currVerb_withEmotionss.get(currDocID)>1) comment1+=">1";
		    				if(map_docID_N_hit_count_currVerb_withEmotionss.get(currDocID)>2) comment1+=">2";
		    				if(map_docID_N_hit_count_currVerb_withEmotionss.get(currDocID)>3) comment1+=">3";
		    				if(map_docID_N_hit_count_currVerb_withEmotionss.get(currDocID)>4) comment1+=">4";
		    				if(map_docID_N_hit_count_currVerb_withEmotionss.get(currDocID)>5) comment1+=">5";
		    				if(map_docID_N_hit_count_currVerb_withEmotionss.get(currDocID)>6) comment1+=">6";
		    				String comment2="";
		    				//
		    				if( map_docID_N_CC.get(currDocID)>0){
		    					comment2="pos";
		    				}
		    				else
		    					comment2="neg";
		    				//
		    				if(		map_docID_N_hit_count_currVerb_withEmotionss.get(currDocID)==0 &&
		    						map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)==0 &&
		    						map_docID_OrganizationCount.get(currDocID)==1 &&
		    						map_URL_N_LABEL_from_humanANNOTATOR.get( map_lineNoRdocID_URL.get(currDocID)).equalsIgnoreCase("0")
		    						){
		    					comment2="##";
		    				}
		    				
				    		//write all the basic feature (verb, emotion, organiz) for URL_TP, URL_neighb1, URL_neighb2
				    		writer_output_for_CaseStudyBias1_TPonly.append(      
						    												 map_docID_N_CC.get(currDocID)
						    												+"!!!"+map_docID_N_hit_count_currVerb_withEmotionss.get(currDocID)
						    												+"!!!"+map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
						    												+"!!!"+map_docID_OrganizationCount.get(currDocID)
						    												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID)
						    												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID)
						    												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(currDocID)
						    												+"!!!"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(currDocID)
						    												+"!!!"+map_NeighbourIDs_ASKEYS_forGivenDocID
						    												+"!!!"+neighb1_docID
						    												+"!!!"+neighb2_docID
						    												+"!!!"+map_sortedbyDATEtime_docID_n_DATE.get(currDocID)
						    												+"!!!"+map_sortedbyDATEtime_docID_n_DATE.get(neighb1_docID)
						    												+"!!!"+map_sortedbyDATEtime_docID_n_DATE.get(neighb1_docID)
						    												+"!!!TP_URL:"+map_lineNoRdocID_URL.get(currDocID)
						    												+"!!!"+map_lineNoRdocID_URL.get(neighb1_docID)
						    												+"!!!"+map_lineNoRdocID_URL.get(neighb2_docID)
						    												+"!!!neighb_1=="
						    												+neighbor_2_featuresString
						    												+"!!!neighb_2=="
						    												+neighbor_2_featuresString
						    												+"!!!labels=="+
						    												map_URL_N_LABEL_from_humanANNOTATOR.get( map_lineNoRdocID_URL.get(currDocID))
						    												+"--"+
						    												map_URL_N_LABEL_from_humanANNOTATOR.get( map_lineNoRdocID_URL.get(neighb1_docID))
						    												+"--"+
						    												map_URL_N_LABEL_from_humanANNOTATOR.get( map_lineNoRdocID_URL.get(neighb2_docID))
						    												+"--"+comment2
						    												+"!!!comment1:"+comment1
						    												+"!!!"+map_docID_N_concHITEmotions.get(currDocID)
				    														+"!!!"+map_docID_N_concHITverbsForEmotions.get(currDocID)
				    														+"!!!"+map_docID_N_concOrganization.get(currDocID)
						    												+"\n");
				    		writer_output_for_CaseStudyBias1_TPonly.flush();
		    		}
		    		
		    		}
		    		
		    		//
		    		// write CASE STUDY for all URL
		    		writer_output_for_CaseStudyBias1_ALL.append(      
																 map_docID_N_CC.get(currDocID)
																+"!!!"+map_docID_N_hit_count_currVerb_withEmotionss.get(currDocID)
																+"!!!"+map_docID_N_total_count_distinctEmotions_4_verbs.get(currDocID)
																+"!!!"+map_docID_OrganizationCount.get(currDocID)
																+"!!!AonT(e):"+map_docID_N_NEIGHBRO_Avg_Total_count_distinctEmotions_4_verbs.get(currDocID)
																+"!!!*AonT(fn.e):"+map_docID_N_NEIGHBRO_Avg_Total_fncount_distinctEmotions_4_verbs.get(currDocID)
																+"!!!AonT(O):"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_Organization.get(currDocID)
																+"!!!*AonT(fn.O):"+map_docID_N_NEIGHBRO_Avg_Total_count_distinct_fnOrganization.get(currDocID)
																+"!!!T(e)=="+map_docID_N_TOTAL_ALL_NEIGHB_total_count_distinctEmotions_4_verbs.get(currDocID)
																+"!!!@T(fn.e)=="+map_docID_N_TOTAL_ALL_NEIGHB_total_fncount_distinctEmotions_4_verb.get(currDocID)
													    		+"!!!T(O)=="+map_docID_N_TOTAL_ALL_NEIGHB_total_count_distinctOrganization.get(currDocID)
													    		+"!!!@T(fn.O)=="+map_docID_N_TOTAL_ALL_NEIGHB_total_count_distinctfnOrganization.get(currDocID)
													    		+"!!!cnt=="+map_docID_cnt.get(currDocID)
																+"!!!"+map_docID_NEIGH_map_neighbDocID_N_EmotionVerbCount.get(currDocID)
																+"!!!"+map_docID_NEIGH_map_neighbDocID_N_OrganizationCount.get(currDocID)
																+"!!!"+map_docID_NEIGH_map_neighbDocID_N_fnEmotionVerbCount.get(currDocID)
																+"!!!"+map_docID_NEIGH_map_neighbDocID_N_fnOrganizationCount.get(currDocID)
																+"!!!neighb:"+map_NeighbourIDs_ASKEYS_forGivenDocID
																+"!!!"+map_sortedbyDATEtime_docID_n_DATE.get(currDocID)
																+"!!!"+map_lineNoRdocID_URL.get(currDocID)
																+"!!!currDocID:"+currDocID
																+"!!!lbl:"+map_URL_N_LABEL_from_humanANNOTATOR.get( map_lineNoRdocID_URL.get(currDocID))
																+"!!!conc:"+map_docID_N_concHITEmotions.get(currDocID)
	    														+"!!!"+map_docID_N_concHITverbsForEmotions.get(currDocID)
	    														+"!!!"+map_docID_N_concOrganization.get(currDocID)
																+"\n");
		    		writer_output_for_CaseStudyBias1_TPonly.flush();
		    		comment1="";	
		    	}
		    	 
		    	writer_output_for_CaseStudyBias1_TPonly.append("map_DOCID_N_ITSrandom_neighor_docID_as_KEY:"+map_DOCID_N_ITSrandom_neighor_docID_as_KEY+"\n");
		    	writer_output_for_CaseStudyBias1_TPonly.append("map_docID_N_total_count_distinctEmotions_4_verbs:"+map_docID_N_total_count_distinctEmotions_4_verbs+"\n");
		    	writer_output_for_CaseStudyBias1_TPonly.append("map_docID_N_total_fn_count_distinctEmotions_4_verbs:"+map_docID_N_total_fn_count_distinctEmotions_4_verbs+"\n");
		    	writer_output_for_CaseStudyBias1_TPonly.flush();
	    	  
	    	
	    	//
			System.out.println(  C1_global_count_seed_institute_in_noun+","+C2_global_count_seed_institute_in_noun
								+" "+C1_global_count_seed_institute_in_organiz+","+C2_global_count_seed_institute_in_organiz
								+" "+C1_global_count_seed_institute_in_verb+","+C2_global_count_seed_institute_in_verb
								+" "+C1_global_count_seed_institute_in_location+","+C2_global_count_seed_institute_in_location);
			
			System.out.println(  C1_global2_count_seed_institute_in_noun+","+C2_global2_count_seed_institute_in_noun
								+" "+C1_global2_count_seed_institute_in_organiz+","+C2_global2_count_seed_institute_in_organiz
								+" "+C1_global2_count_seed_institute_in_verb+","+C2_global2_count_seed_institute_in_verb
								+" "+C1_global2_count_seed_institute_in_location+","+C2_global2_count_seed_institute_in_location
								);
			
			System.out.println("map_LocationWord_N_arr_lineNo:"+map_LocationWord_N_arr_lineNo.size()
								+" map_URL_lineNo.size:"+map_URL_lineNoRDocID.size()+" map_lineNo_URL.size:"+map_lineNoRdocID_URL.size()
								+" map_docID_Document.size:"+map_docID_Document.size()
								+"\n map_docID_N_total_count_distinctEmotions_4_verbs.size:"+map_docID_N_total_count_distinctEmotions_4_verbs.size()
								+" map_docID_N_total_fn_count_distinctEmotions_4_verbs.size:"+map_docID_N_total_fn_count_distinctEmotions_4_verbs.size()
								+" count_ELSE_count:"+count_ELSE_count
								+"\n map_distinctDocIDsFromVerbMap1.size:"+map_distinctDocIDsFromVerbMap1.size()
								+" map_distinctDocIDsFromVerbMap2.size:"+map_distinctDocIDsFromVerbMap2.size()
								+"\n verb_yes_count:"+verb_yes_count
								+"\n verb_no_count:"+verb_no_count
								);
			int total_verbs_of_all_documents=0;
			//
			for(int docID:map_distinctDocIDsFromVerb_docID_N_countVerbs.keySet()){
				total_verbs_of_all_documents+=map_distinctDocIDsFromVerb_docID_N_countVerbs.get(docID);
			}
			
    		System.out.println("------------ below variables help to calculate number of nodes in our graph------------");
    		System.out.println("verb_yes_count:"+verb_yes_count
    							+" total_verbs_of_all_documents:"+total_verbs_of_all_documents
    							+" organiz_yes_count_4_currDocID:"+organiz_yes_count_4_currDocID);
    		System.out.println("------------------------------------------------------------------------------");
			
			System.out.println("(TP+TN):"+(TP+TN)+" (TP+TN+FP+FN):"+(TP+TN+FP+FN));
			String comment="(GCV)";
			if(Flag_2_choose_predictionAlgo==333)
				comment="(GCVO)";
			else if(Flag_2_choose_predictionAlgo==3333)
				comment="(GCV)";
			else if(Flag_2_choose_predictionAlgo==33333)
				comment="(GCV,weight)";
			
			System.out.println("TP:"+TP+" TN:"+TN+" FP:"+FP+" FN:"+FN+" total_articles:"+total_articles
								+" Flag_2_choose_predictionAlgo:"+Flag_2_choose_predictionAlgo +" comment:"+comment
											+" number_of_neighbors="+number_of_neighbors);
			System.out.println("precision:"+precision+" recall:"+recall+" F1score:"+F1score+" accuracy:"+accuracy);
			System.out.println("alpha:"+alpha+" beta:"+beta+" gamma:"+gamma+" mu:"+mu);
			System.out.println("map_docID_N_CC:"+map_docID_N_CC.size()+" count_ELSE_count:"+count_ELSE_count+" count_IF_count:"+count_IF_count);
//			System.out.println( " map_URL_N_LABEL_from_humanANNOTATOR:"+map_URL_N_LABEL_from_humanANNOTATOR);
//			System.out.println("map_lineNo_URL:"+map_lineNo_URL);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}
	
	// MAIN 
	public static void main(String args[]) throws IOException {
		//--------------------------------------------------------------------------------		
		// NOTE:
		//--------------------------------------------------------------------------------
		// (1) This method is run after running p18_MAIN_get_dupURL_N_domainNames()
		// (2) Use "merge_gTruth_with_orgVerbNounPersonFile.java" to produce file for variable "inFile_person_origz_loc"
		// (3) run "wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati.java"
		//--------------------------------------------------------------------------------
		
		// manual change (1) -- this prefix_filename has file mentioned in variable "inFile" from "wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati"
		String baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/";
			   baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/tmp/";
		String prefix_filename="OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED___ENGLISH__LEN_50_FILTER__PASSED_FILTER__url_tagline_untagline.3all__added_perso_origz_loc_verb2_done.txt";
			   prefix_filename="OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED___ENGLISH__LEN_50_FILTER__PASSED_FILTER__url_tagline_untagline.3all__added_perso_origz_loc_verb2_done__added##GroundTruth.txt";
			   prefix_filename="OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt.3all_p18.txtdded_perso_origz_loc_verb2.txt";
			   //this has all the 4 minorities - sikh, abortion, islam (muslims), african american
			   prefix_filename="O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged.txt";
			   prefix_filename="O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged_onlyAfricanAmerican.txt";
 
	    TreeMap<Integer, String>  map_seq_inFile=new TreeMap<Integer, String>();
	    int count_only_AfriAmerican=0;
	    // manual change (2) <--THIS file is not more use..ITS same as PREFIX FILE 
		// IMPORTANT: Line no used for files such as noun, verb, organiz etc. are from FIRST COLUMN from file mentioned <-------OBSELETE 
		//			  in variable "inFile" of "wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati"
        // p18 - NOTE: this file should be from variable "inFile" of "wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati" 		    		 
    	String inFile="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged.txt";
    	int    index_having_URL_4_inFile=5;																								
    	
		// manual change (3) --- > HUMAN LABELED DATA 
		// human labeled data (from maitrayi)
		// Maitrayi labeled files; kept in FOLDER->/Users/lenin/Dropbox/#problems/p18/From Maitrayi/
		String inFile_7_human_annotator_labeledFile="/Users/lenin/Dropbox/#problems/p18/FromMaitrayi/all.txt";
			   inFile_7_human_annotator_labeledFile="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txtNEW_1000labelled_confCorrected.txt_1st1000ln_removeIrrelevaLABEL.txt";
			   inFile_7_human_annotator_labeledFile="/Users/lenin/Dropbox/Maitrayi/p18 -/Labeling/1500labeled_marked_DOUBTS/O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL.txt";
			   //only african-american 
			   inFile_7_human_annotator_labeledFile="/Users/lenin/Dropbox/Maitrayi/p18 -/Labeling/1500labeled_marked_DOUBTS/O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_onlyAfricanAmerican_removeIrrelevaLABEL.txt";
			   
		String tokens_4_URL_N_LABEL_4_inFile_7_human_annotator_labeledFile="4,3"; // example  <4,2> -> <URL,LABEL>
    	//manually change <---------------below is an output file from "wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati"
    	String inFile_URL_LineNoCSVhavingQUOTES = baseFolder+"O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged.txt_COMMA_LINENO_CSV.txt";
    	String tokens_4_URL_N_lineNoCSV_4_inFile_URL_LineNoCSVhavingQUOTES="1,2";
		
		// manual change
		//NOTE:THIS file comes from OUTPUT of "merge_gTruth_with_orgVerbNounPersonFile.java"
    	String inFile_person_origz_loc=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED___ENGLISH__LEN_50_FILTER__PASSED_FILTER__url_tagline_untagline.dummy.all__added_perso_origz_loc_verb__added##GroundTruth.txt";
    	inFile_person_origz_loc=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED___ENGLISH__LEN_50_FILTER__PASSED_FILTER__url_tagline_untagline.3all__added_perso_origz_loc_verb2_done__added##GroundTruth.txt";
    	//this file has all the 4 social issues that is african american, muslim, WOMEN (abortion) and sikh
    	inFile_person_origz_loc=baseFolder+"O___ENGLISH__LEN_50_FILTER__PASSED_FILTER__url_tagline_untagline.3all_p18__removeIrrelevaLABEL_NLPtagged__added_perso_origz_loc_verb2__added##GroundTruth.txt";
    	inFile_person_origz_loc=baseFolder+"O___ENGLISH__LEN_50_FILTER__PASSED_FILTER__url_tagline_untagline.3all_p18__removeIrrelevaLABEL_NLPtagged_onlyAfricanAmerican__added_perso_origz_loc_verb2__added##GroundTruth.txt";
    	
    	String output_for_WEKA=baseFolder+"input_for_WEKA.txt";
    	String output_for_CaseStudyBias1_TPonly=baseFolder+"output_for_CaseStudyBias1_TPonly.txt";
    	String output_for_CaseStudyBias1_ALL=baseFolder+"output_for_CaseStudyBias1_ALL.txt";
    	FileWriter writerDebug=new FileWriter(baseFolder+"debug_p18_main.txt");
    	
		//NOTE: for files below,created with
    	// "wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati.readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber" (Step 3)
    	// <>,LINENO <<<< BELOW ARE input FILES created using "wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati.java" with below input file>>>
		// "p18-filtered-1-attempt-id37151552/experiment/OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.dummy.all.txt""
    	String inFile_nounWord_LineNoCSV = baseFolder+prefix_filename+"_NOUN_LINENO.txtSTEMMED_NODUP.txt";
    	String inFile_verbWord_LineNoCSV = baseFolder+prefix_filename+"_VERB_LINENO.txtSTEMMED_NODUP.txt";
    	String inFile_personWord_LineNoCSV = baseFolder+prefix_filename+"_PERSON_LINENO.txtSTEMMED_NODUP.txt";
    	String inFile_organizWord_LineNoCSV = baseFolder+prefix_filename+"_ORGANIZ_LINENO.txtSTEMMED_NODUP.txt";
    	String inFile_locationWord_LineNoCSV = baseFolder+prefix_filename+"_LOCATION_LINENO.txtSTEMMED_NODUP.txt";
//    	String inFile_numberWord_LineNoCSV = baseFolder+prefix_filename+"_NUMBER_LINENO.txt";
    	String inFile_numberWord_LineNoCSV = baseFolder+prefix_filename+"_NUMBERS_LINENO.txtSTEMMED_NODUP.txt";
    	
    	  // Loading seq and Line 
		  TreeMap<Integer, String>  map_seq_inFile_TEMP=
				  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
									.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																									 baseFolder+prefix_filename, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 " loading", //debug_label
																									 false //isPrintSOP
																									 );
		  boolean is_allow=false;
		  //FILTER here only AFRICAN-AMERICAN, other filter out..
		  for(int seq:map_seq_inFile_TEMP.keySet()){
			  is_allow=false;
			  String curr_line=map_seq_inFile_TEMP.get(seq).toLowerCase();
			  //
//			  if(curr_line.indexOf("muslim")==-1  && curr_line.indexOf("islam")==-1 &&
//				 curr_line.indexOf("abortion")==-1 && curr_line.indexOf("sikh")==-1
//				){
				  is_allow=true;
//			  }
			  //
			  if(is_allow){
				  count_only_AfriAmerican++;
				  map_seq_inFile.put(seq, map_seq_inFile_TEMP.get(seq)); //only african-american
			  }
		  }
		  
		  TreeMap<String, Integer> map_URL_lineNo=new TreeMap<String,Integer>();
		  TreeMap<Integer, String> map_lineNo_URL=new TreeMap<Integer, String>();
		  // get <LineNo,URL>
		  for(int seq:map_seq_inFile.keySet()){
			  
			  if(seq==1) continue;
			  
			  String []arr_=map_seq_inFile.get(seq).split("!!!");
			  String curr_URL =arr_[index_having_URL_4_inFile-1];
			  int curr_lineNo_external=Integer.valueOf(arr_[0]);
			  // 
			  map_URL_lineNo.put(curr_URL, curr_lineNo_external);
			  map_lineNo_URL.put(curr_lineNo_external, curr_URL);
		  }
		  
		  
    	  /////////////////////////////////////
    	  // loading URL and LABEL
    	// LoadMultipleValueToMap2_AS_KEY_VALUE
    	TreeMap<String, String> map_URL_N_LABEL_from_humanANNOTATOR=
					    	crawler.
					    	LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE(inFile_7_human_annotator_labeledFile, 
					    																			  "!!!", //delimiter, 
					    																			  tokens_4_URL_N_LABEL_4_inFile_7_human_annotator_labeledFile, //"1,2",
					    																			  "", //append_suffix_at_each_line
					    																			  false, //is_have_only_alphanumeric
					    																			  false //isSOPprint
					    																			  );
    	
    	// Loading URL and LineNoCSV
    	TreeMap<String, String> map_URL_N_LineNoCSVhavingQUOTE=
    							crawler.
    							LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE(
    																			  inFile_URL_LineNoCSVhavingQUOTES, 
    																			  "!!!", //delimiter, 
    																			  tokens_4_URL_N_lineNoCSV_4_inFile_URL_LineNoCSVhavingQUOTES, //"1,2",
    																			  "", //append_suffix_at_each_line
    																			  false, //is_have_only_alphanumeric
    																			  false //isSOPprint
    																			  );
    	
    	TreeMap<String, TreeMap<Integer,Integer>> map_URL_mapLINEnoHAVINGquotesASkey=new TreeMap<String, TreeMap<Integer,Integer>>();
    	int local_lineNumber=0;
    	// AFTER Loading URL and LineNoCSV ABOVE, split the line numbers CSV and put into another map as KEY
	    	for(String currURL:map_URL_N_LineNoCSVhavingQUOTE.keySet()){
	    		local_lineNumber++;
	    		// HEADER (DOES NOT HAVE http, so NOT a URL)
	    		if(currURL.indexOf("http")==-1) continue;
	    		//
	    		String [] arr_lineNO=map_URL_N_LineNoCSVhavingQUOTE.get(currURL).split(",");
	    		int c=0;
	    		TreeMap<Integer,Integer> map_currURL_LINEnoASmap=new TreeMap<Integer, Integer>();
	    		//
	    		while(c<arr_lineNO.length){
	    			map_currURL_LINEnoASmap.put(Integer.valueOf(arr_lineNO[c]), -1);
	    			c++;
	    		}
	    		map_URL_mapLINEnoHAVINGquotesASkey.put(currURL, map_currURL_LINEnoASmap);
	    	}
	    	
		  /////////////////////////////////////
		  // loading NOUN words (each word) and its occurrance LINE_no CSV
		  System.out.println("Loadin NOUN");
		  TreeMap<Integer, String>  map_NounWord_LineNoOccuranceCSV=
				  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
									.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																									 inFile_nounWord_LineNoCSV, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 " loading", //debug_label
																									 false //isPrintSOP
																									 );
		  TreeMap<String, String []> map_NounWord_N_arr_lineNo=new TreeMap<String, String[]>();
		  
		  for(int seq:map_NounWord_LineNoOccuranceCSV.keySet()){
			  String [] s=map_NounWord_LineNoOccuranceCSV.get(seq).split("!!!");
			  map_NounWord_N_arr_lineNo.put( s[0] , s[1].split(","));
		  }
		  map_NounWord_LineNoOccuranceCSV.clear();
		  System.out.println("----loaded noun csv:"+map_NounWord_N_arr_lineNo.size());
		  /////////////////////////////////////
		  System.out.println("Loadin VERB");
		  // loading VERB words (each word) and its occurrance LINE_no CSV
		  TreeMap<Integer, String>  map_VerbWord_LineNoOccuranceCSV=
				  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
									.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																									 inFile_verbWord_LineNoCSV, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 " loading", //debug_label
																									 false //isPrintSOP
																									 );
		  TreeMap<String, String []> map_VerbWord_N_arr_lineNo=new TreeMap<String, String[]>();
		  for(int seq:map_VerbWord_LineNoOccuranceCSV.keySet()){
			  String [] s=map_VerbWord_LineNoOccuranceCSV.get(seq).split("!!!");
			  map_VerbWord_N_arr_lineNo.put( s[0] , s[1].split(","));
		  }
		  map_VerbWord_LineNoOccuranceCSV.clear();
		  System.out.println("----loaded verb csv:"+map_VerbWord_N_arr_lineNo.size());
		  /////////////////////////////////////
		  System.out.println("Loadin PERSON");
		  // loading VERB words (each word) and its occurrance LINE_no CSV
		  TreeMap<Integer, String>  map_PersonWord_LineNoOccuranceCSV=
				  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
									.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																									 inFile_personWord_LineNoCSV, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 " loading", //debug_label
																									 false //isPrintSOP
																									 );
		  TreeMap<String, String []> map_PersonWord_N_arr_lineNo=new TreeMap<String, String[]>();
		  for(int seq:map_PersonWord_LineNoOccuranceCSV.keySet()){
			  String [] s=map_PersonWord_LineNoOccuranceCSV.get(seq).split("!!!");
			  map_PersonWord_N_arr_lineNo.put( s[0] , s[1].split(","));
		  }
		  map_PersonWord_LineNoOccuranceCSV.clear();
		  System.out.println("----loaded person csv:"+map_PersonWord_N_arr_lineNo.size());
		  /////////////////////////////////////
		  System.out.println("Loadin ORGANIZ");
		  // loading VERB words (each word) and its occurrance LINE_no CSV
		  TreeMap<Integer, String>  map_OrgaizWord_LineNoOccuranceCSV=
				  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
									.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																									 inFile_organizWord_LineNoCSV, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 " loading", //debug_label
																									 false //isPrintSOP
																									 );
		  TreeMap<String, String []> map_OrganizWord_N_arr_lineNo=new TreeMap<String, String[]>();
		  for(int seq:map_OrgaizWord_LineNoOccuranceCSV.keySet()){
			  String [] s=map_OrgaizWord_LineNoOccuranceCSV.get(seq).split("!!!");
			  map_OrganizWord_N_arr_lineNo.put( s[0] , s[1].split(","));
		  }
		  map_OrgaizWord_LineNoOccuranceCSV.clear();
		  System.out.println("----loaded organiz csv:"+map_OrganizWord_N_arr_lineNo.size());
		  
		  /////////////////////
		  ///////////////////////////////////
		  System.out.println("Loadin LOCATION");
		  // loading VERB words (each word) and its occurrance LINE_no CSV
		  TreeMap<Integer, String>  map_LocationWord_LineNoOccuranceCSV=
				  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
									.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																									 inFile_locationWord_LineNoCSV, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 " loading", //debug_label
																									 false //isPrintSOP
																									 );
		  TreeMap<String, String []> map_LocationWord_N_arr_lineNo=new TreeMap<String, String[]>();
		  for(int seq:map_LocationWord_LineNoOccuranceCSV.keySet()){
			  String [] s=map_LocationWord_LineNoOccuranceCSV.get(seq).split("!!!");
			  map_LocationWord_N_arr_lineNo.put( s[0] , s[1].split(","));
		  }
		  map_LocationWord_LineNoOccuranceCSV.clear();
		  System.out.println("----loaded location csv:"+map_LocationWord_N_arr_lineNo.size());
		  ///////////////////////////////////
		  System.out.println("Loadin NUMBER");
		  // loading VERB words (each word) and its occurrance LINE_no CSV
		  TreeMap<Integer, String>  map_NumberWord_LineNoOccuranceCSV=
				  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
									.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																									 inFile_numberWord_LineNoCSV, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 " loading", //debug_label
																									 false //isPrintSOP
																									 );
		  TreeMap<String, String []> map_NumberWord_N_arr_lineNo=new TreeMap<String, String[]>();
		  for(int seq:map_NumberWord_LineNoOccuranceCSV.keySet()){
			  String [] s=map_NumberWord_LineNoOccuranceCSV.get(seq).split("!!!");
			  map_NumberWord_N_arr_lineNo.put( s[0] , s[1].split(","));
		  }
		  map_NumberWord_LineNoOccuranceCSV.clear();
		  System.out.println("----loaded number csv:"+map_NumberWord_N_arr_lineNo.size());
		  
			boolean is_having_header=true;
	    	
			  /////////////////////////////////// START : load inFile_person_origz_loc to know total number of documents
			  
			  TreeMap<Integer, String>  map_seq_Document_temp=
					  					 ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inFile_person_origz_loc, //inFile,  
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  System.out.println(" map_seq_Document_temp: "+map_seq_Document_temp.size());
			  
			  TreeMap<Integer, String>  map_docID_Document=new TreeMap<Integer, String>();
			  // get <DocID,eachLine>  <--docID and eachLine (Document)
			  for(int seq:map_seq_Document_temp.keySet()){
				  //HEADER ,SKIP
				  if(seq==1) continue;
				  ////BEGIN - ONLY AFRICAN AMERICAN 
				  is_allow=false;
				  String curr_line=map_seq_Document_temp.get(seq).toLowerCase();
				  //
//				  if(curr_line.indexOf("muslim")==-1   && curr_line.indexOf("islam")==-1 &&
//					 curr_line.indexOf("abortion")==-1 && curr_line.indexOf("sikh")==-1
//					){
 

						  ////END - ONLY AFRICAN AMERICAN
						  String [] s=map_seq_Document_temp.get(seq).split("!!!");
						  int currDocID=Integer.valueOf(s[0]);
						  map_docID_Document.put(currDocID, map_seq_Document_temp.get(seq) );
				  
				  
//				  }
			  }
			  
			  /////////////////////////////////// END : load inFile_person_origz_loc to know total number of documents
	    	
	    	// LOADING... DocID (lineNo) and person , organiz, locaiton
	    	// mapOut( <lineNo>:1  ) -> person  <<----here lineNo = docID
	    	// mapOut( <lineNo>:2  ) -> Organiz
	    	// mapOut( <lineNo>:3  ) -> Location
	    	// mapOut( <lineNo>:4  ) -> Numbers
	    	// mapOut( <lineNo>:5  ) -> Noun
	    	// mapOut( <lineNo>:6  ) -> Sentiment
	    	// mapOut( <lineNo>:7  ) -> Verb
	    	// mapOut( <lineNo>:8  ).get("1") -> Ground Truth label
	    	TreeMap<String, TreeMap<String,String>> map_lineNoNID_person_organiz_locati_numbers=
	    												p8.MyAlgo.
												    	load_DocID_AND_person_organi_locati_numbers(
												    										baseFolder,
												    										inFile_person_origz_loc,
												    										"@@@@",
												    										 1,//    token_having_docID_OR_lineNo_as_weCall (but uses delimiter !!!)
																							 2,//    token_having_person_mapString,
																							 3,//    token_having_organiz_mapString,
																							 4,//    token_having_location_mapString,
																							 5,//    token_having_numbers_mapString,
																							 6,//    token_having_nouns
																							 7,//    token_having_sentiment
																							 8,//    token_having_verbs
																							 19, // label <--check this 
												    										 is_having_header
												    										);
	    	System.out.println("loaded p.o.l.n:"+map_lineNoNID_person_organiz_locati_numbers.size());
	    	
	    	//set seeds 
	    	TreeMap<String, String> map_word_lineCSVofCustomWords=new TreeMap<String, String>();
	    	//manual change  <----> THIS WENT OBSELETE ,... BUT might reapply the concept if required
	    	String customFile_lineCSV=baseFolder+"customwords_4_lineCSV_2searchin2ndfile.txt_LINECSV.txt";
	    	//LoadMultipleValueToMap2_AS_KEY_VALUE
//	    	map_word_lineCSVofCustomWords=
//						    	crawler.
//						    	LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE(customFile_lineCSV, 
//						    																			  "", //delimiter, 
//						    																			  "1,2",
//						    																			  "", //append_suffix_at_each_line
//						    																			  false, //is_have_only_alphanumeric
//						    																			  false //isSOPprint
//						    																			  );
	    	
	    	///myAlgo_p18 - different clusters for noun, organiz, etc. and see how many docID belong to each
//	    	myAlgo_p18(
//	    			baseFolder,
//					map_NounWord_N_arr_lineNo,
//					map_VerbWord_N_arr_lineNo,
//					map_PersonWord_N_arr_lineNo,
//					map_OrganizWord_N_arr_lineNo,
//					map_LocationWord_N_arr_lineNo,
//					map_NumberWord_N_arr_lineNo,
//					map_lineNoNID_person_organiz_locati_numbers,
//					map_docID_Document,  //<docid,document>
//					map_word_lineCSVofCustomWords,
//					map_URL_N_LABEL_from_humanANNOTATOR
//					);
 
	    	
	    	
	    	//load the emotions file. (get map output with and without stemming)
	    	String inFile_emotion_lexicon = "/Users/lenin/Dropbox/#problems/p18/StaticTwitterSent-master/extra/NRC-emotion-lexicon-wordlevel-v0.92.txt";
	    	String inFile_ = "/Users/lenin/Dropbox/#problems/p18/StaticTwitterSent-master/extra/NRC-emotion-lexicon-wordlevel-v0.92.txt";
	    	
	    	TreeMap<String, String> map_word_emotionCSV=new TreeMap<String, String>();
	    	TreeMap<String, String> map_STEMMEDword_emotionCSV=new TreeMap<String, String>();
	    	
	    	////////////////// GET word and its corresponding EMOTIONS...
	    	TreeMap<Integer, String>  map_seq_eachLine_EmotionLexicon=
 					 ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																					 inFile_emotion_lexicon, //inFile_emotion_lexicon,  
																				 	 -1, //startline, 
																					 -1, //endline,
																					 " loading", //debug_label
																					 false //isPrintSOP
																					 );
	    	Stemmer stemmer=new Stemmer(); 
	    	// 
	    	for(int seq:map_seq_eachLine_EmotionLexicon.keySet()){
	    		String []s=map_seq_eachLine_EmotionLexicon.get(seq).split("\t");
	    		String curr_word=s[0];
	    		String curr_emotion=s[1];
	    		int curr_yes_OR_no_4_curr_Emotion=Integer.valueOf(s[2]);
	    		
	    		//only if it is "1", that curr_emotion is valid for curr_word
	    		if(curr_yes_OR_no_4_curr_Emotion==1){
		    		//
		    		System.out.println("emotion rel:"+curr_word+" "+curr_emotion+" "+curr_yes_OR_no_4_curr_Emotion);
		    		
	    			// curr word (without stemming)  and its corresponding emotions
		    		if(map_word_emotionCSV.containsKey(curr_word)){
		    			String concatenate_emotions=map_word_emotionCSV.get(curr_word);
		    			
		    			if(concatenate_emotions.indexOf(curr_emotion)==-1){
		    				concatenate_emotions+=","+curr_emotion;
		    				map_word_emotionCSV.put(curr_word, concatenate_emotions);
		    			}
		    			
		    		}
		    		else{
		    			map_word_emotionCSV.put(curr_word, curr_emotion);
		    		}
	    			
	    			//curr word with stemming and its corresponding emotions
	    			String stemmed_curr_word=stemmer.stem(curr_word);
	    			//
	    			if(map_STEMMEDword_emotionCSV.containsKey(stemmed_curr_word)){
	    				String concatenate_Emotions=map_STEMMEDword_emotionCSV.get(stemmed_curr_word);
	    				if(concatenate_Emotions.indexOf(curr_emotion)==-1){
		    				concatenate_Emotions+=","+curr_emotion;
		    				map_STEMMEDword_emotionCSV.put(stemmed_curr_word, concatenate_Emotions);	//concatenated
	    				}
	    			}
	    			else{
	    				map_STEMMEDword_emotionCSV.put(stemmed_curr_word, curr_emotion);
	    			}
	    			
	    		}
	    		
	    	}
//	    	System.out.println("map_word_emotion:"+map_word_emotion);
//	    	System.out.println("map_STEMMEDword_emotion:"+map_STEMMEDword_emotion);
	    	
	    	writerDebug.append("\nmap_word_emotionCSV:"+map_word_emotionCSV);
	    	writerDebug.append("\nmap_STEMMEDword_emotion:"+map_STEMMEDword_emotionCSV);
	    	writerDebug.flush();
	    	
	    	
//	    	"/Users/lenin/Dropbox/#problems/p18/StaticTwitterSent-master/extra/SentiStrength/EmotionLookupTableGeneral.txt
//	    	/Users/lenin/Dropbox/\#problems/p18/StaticTwitterSent-master/extra/AFINN-111.txt
	    	
	    	//load the file which is sorted by datetime in chronological order <---CREATED using "merge_URL_n_dateTime.java"
	    	//each line has <url!!!date!!!docID> <--first and last column is impornat
	    	  // Loading seq and Line  
			  TreeMap<Integer, String>  map_seq_inFile_eachLine=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
															baseFolder+"sorted_url_date_docID_DONT_DELETE.txt",//<---CREATED using "merge_URL_n_dateTime.java" 
															-1, //startline, 
															-1, //endline,
															" loading", //debug_label
															false //isPrintSOP
															);
			  ///RANK indicates the order of sorting..
			  TreeMap<Integer,Integer> map_sortedbyDATEtime_RANKseq_n_docID=new TreeMap<Integer, Integer>();
			  TreeMap<Integer,Integer> map_sortedbyDATEtime_docID_n_RANKseq=new TreeMap<Integer,Integer>();
			  TreeMap<String,String> map_sortedbyDATEtime_URL_n_DATE=new TreeMap<String,String>();
			  TreeMap<Integer,String> map_sortedbyDATEtime_docID_n_DATE=new TreeMap<Integer,String>();
			  // 
			  for(int seq:map_seq_inFile_eachLine.keySet()){
				  String [] s=map_seq_inFile_eachLine.get(seq).split("!!!");
				  map_sortedbyDATEtime_RANKseq_n_docID.put(seq, Integer.valueOf(s[2]));
				  map_sortedbyDATEtime_docID_n_RANKseq.put( Integer.valueOf(s[2]), seq);
				  map_sortedbyDATEtime_URL_n_DATE.put(s[0], s[1]);
				  map_sortedbyDATEtime_docID_n_DATE.put(Integer.valueOf(s[2]), s[1]);
			  }
			  
			  boolean is_run_for_WEKAinputFile=true; // < -it also runs case study
	    	System.out.println("map_sortedbyDATEtime_docID_n_RANKseq:"+map_sortedbyDATEtime_docID_n_RANKseq);
	    	//p18 -- comment/uncomment
	    	//myAlgo_p18_2 (this method has no external clusters as mentioned in myAlgo_p18()
	    	myAlgo_p18_2(
				    			baseFolder,
				    			3, //number_of_neighbors
								map_NounWord_N_arr_lineNo,
								map_VerbWord_N_arr_lineNo,
								map_PersonWord_N_arr_lineNo,
								map_OrganizWord_N_arr_lineNo,
								map_LocationWord_N_arr_lineNo,
								map_NumberWord_N_arr_lineNo,
								map_lineNoNID_person_organiz_locati_numbers,
								map_docID_Document,  //<docid,document(eachLine)>
								map_word_lineCSVofCustomWords,
								map_URL_N_LABEL_from_humanANNOTATOR,
								map_URL_mapLINEnoHAVINGquotesASkey, //<URL,<LINENOhavingQUOTES,-1>>
								map_URL_lineNo,
								map_lineNo_URL,
								map_word_emotionCSV,
								map_STEMMEDword_emotionCSV,
								map_sortedbyDATEtime_RANKseq_n_docID,
								map_sortedbyDATEtime_docID_n_RANKseq,
								map_sortedbyDATEtime_URL_n_DATE,
								map_sortedbyDATEtime_docID_n_DATE,
								output_for_WEKA,
								output_for_CaseStudyBias1_TPonly,
								output_for_CaseStudyBias1_ALL,
								is_run_for_WEKAinputFile
								);
	    	
	    	System.out.println("loaded p.o.l.n:"+map_lineNoNID_person_organiz_locati_numbers.size()+" map_seq_inFile.size:"+map_seq_inFile.size()
	    						+" count_only_AfriAmerican:"+count_only_AfriAmerican+" map_URL_N_LABEL_from_humanANNOTATOR.size:"+map_URL_N_LABEL_from_humanANNOTATOR.size()
	    						+" map_docID_Document.SIZE:"+map_docID_Document.size()
	    						);
	    	System.out.println("inFile_organizWord_LineNoCSV:"+inFile_organizWord_LineNoCSV
	    						+"\n inFile_verbWord_LineNoCSV:"+inFile_verbWord_LineNoCSV);
//	    	System.out.println(" map_docID_Document: "+map_docID_Document);
	    	
//			String baseFolder,
//			TreeMap<String, String[]> map_NounWord_N_arr_lineNo, // lineno=docid
//			TreeMap<String, String[]> map_VerbWord_N_arr_lineNo,
//			TreeMap<String, String[]> map_PersonWord_N_arr_lineNo,
//			TreeMap<String, String[]> map_OrganizWord_N_arr_lineNo,
//			TreeMap<String, String[]> map_LocationWord_N_arr_lineNo,
//			TreeMap<String, String[]> map_NumberWord_N_arr_lineNo,
//			TreeMap<String, TreeMap<String, String>> map_lineNoNID_person_organiz_locati_numbers_nouns,
//			TreeMap<Integer,String> map_docID_Document, 
//			HashMap<String, String> map_word_lineCSVofCustomWords //seeds
	     
	    	
//	    	String baseFolder,
//			TreeMap<String, String[]> map_NounWord_N_arr_lineNo, // lineno=docid
//			TreeMap<String, String[]> map_VerbWord_N_arr_lineNo,
//			TreeMap<String, String[]> map_PersonWord_N_arr_lineNo,
//			TreeMap<String, String[]> map_OrganizWord_N_arr_lineNo,
//			TreeMap<String, String[]> map_LocationWord_N_arr_lineNo,
//			TreeMap<String, String[]> map_NumberWord_N_arr_lineNo,
//			TreeMap<String, TreeMap<String, String>> map_lineNoNID_person_organiz_locati_numbers_nouns,
//			TreeMap<Integer,String> map_docID_Document, 
//			HashMap<String, String> map_word_lineCSVofCustomWords //seeds
	    	
	}

}
