package crawler;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;
 
/**
 * Standford Named Entity Demo
 * @author Ganesh
 */
public class StanfordNER
{
	
	//for a map_begin() an map_end() of organization or person or anything , we get two consecutive tokens occuring next to each other
	public static TreeMap<Integer,TreeMap<Integer, String>> get_consecutiveTokens_for_givenTokenMAPbeginNend(
																String content, //input_text
																TreeMap<Integer, String> map_organ_begin_IN,
																TreeMap<Integer, String> map_organ_end_IN,
																boolean is_debug
//																TreeMap<Integer,TreeMap<Integer,String>> map
																){
		int curr_organization_name_inside_lineNo=0;
		TreeMap<Integer,TreeMap<Integer, String>> mapOut=new TreeMap<Integer, TreeMap<Integer,String>>();
		TreeMap<Integer, String> map_seq_ConsecutiveTokens=new TreeMap<Integer, String>();
		TreeMap<Integer, String> map_seq_concTokens3EXCLAMlineNo=new TreeMap<Integer, String>();
		TreeMap<Integer, String> map_seq_concTokens3EXCLAMlineNo_onlyQUOTESavailableInThisLINE=new TreeMap<Integer, String>();
		
		TreeMap<Integer, Integer> map_lineNo_PositionOFdot=new TreeMap<Integer, Integer>();
		String [] arr_curr_bodyText_without_NLP=content.split("\\.");
		
		TreeMap<Integer, Integer> map_lineNo_CountOFfoundConsecutiveOrganize=new TreeMap<Integer, Integer>();
		
		int output_lineNo=0;
		int counter=0;
		TreeMap<String, Integer> map_line_N_lineNo=new TreeMap<String, Integer>();
		
		//METHOD 1 :
		String temp_content=content;
		//get each postion of . in the content
//		int c2=0;
//			while(temp_content.indexOf(".")>=0){
//				try{
//					int position_dot=temp_content.indexOf(".");
//					if(position_dot>=0){
//						c2++;
//						map_lineNo_PositionOFdot.put(c2, position_dot);
//					}
//					if(position_dot>=0)
//						temp_content=temp_content.substring(position_dot+1 , temp_content.length());
//					System.out.println(position_dot+" temp_content:"+temp_content);
//				
//					if(temp_content.indexOf(".")==-1) break;
//							
//				}
//				catch(Exception e){
//					System.out.println("ERROR : "+e.getMessage());
//				}
//				
//			}
			
			int position_2=0; int total_length=0;
			// METHOD 2 :
			int c3=0;
			while(c3<arr_curr_bodyText_without_NLP.length){
				total_length+=arr_curr_bodyText_without_NLP[c3].length();
				map_lineNo_PositionOFdot.put(c3+1, total_length);
				c3++;
			}
			
		int c=0;
		while(c<arr_curr_bodyText_without_NLP.length){
			String compressedSentences_of_curr_doc=Clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters(arr_curr_bodyText_without_NLP[c],
																true);
			map_line_N_lineNo.put(compressedSentences_of_curr_doc, (c+1));
			c++;
		}//
		try {
			 
			 TreeMap<Integer, String> map_organ_begin=map_organ_begin_IN; //map.get(22);
			 TreeMap<Integer, String> map_organ_end=map_organ_end_IN; //map.get(222);

			 if(is_debug)
				 System.out.println("begin,end->"+map_organ_begin_IN +" "+map_organ_end_IN);
			 
			 int cnt_size=map_organ_begin.size();int next_begin=-1;
			 String conc_organization_name="";
			 int first_begin=-1;
			 //
			 for(int seq:map_organ_begin.keySet()){
				 int begin=Integer.valueOf(map_organ_begin.get(seq));
				 int end=Integer.valueOf(map_organ_end.get(seq));
				 if(seq+1<=cnt_size)
					 next_begin=Integer.valueOf(map_organ_begin.get(seq+1));
//				 System.out.println("begin,end->"+begin +" "+end);

				 // 
				 if( begin>0 && end>0 ){
					 if(end+1==next_begin){
						 //get first begin
						 if(conc_organization_name.length()==0)
							 first_begin=begin-1;
						 
						 conc_organization_name=conc_organization_name+content.substring(begin-1 , end).replace(",", " ").replace("  "," ");
					 }
					 else{
						 if(conc_organization_name.length()>0){
							 conc_organization_name=conc_organization_name+content.substring(begin-1 , end).replace(",", " ").replace("  "," ");
							 counter++;
							 //
							 map_seq_ConsecutiveTokens.put( counter,
									 						conc_organization_name);
 
//							 System.out.println("begin:"+first_begin+" "+content.indexOf(".",end));
							 // we have the end of the line "." that having current organization , but we have to find beginning .
							 curr_organization_name_inside_lineNo=find_begin_of_line(map_lineNo_PositionOFdot, first_begin);
							 // count the number of times each line is choosen
							 if(map_lineNo_CountOFfoundConsecutiveOrganize.containsKey(curr_organization_name_inside_lineNo)){
								 int t=map_lineNo_CountOFfoundConsecutiveOrganize.get(curr_organization_name_inside_lineNo)+1;
								 map_lineNo_CountOFfoundConsecutiveOrganize.put(curr_organization_name_inside_lineNo, t);
							 }
							 else
								 map_lineNo_CountOFfoundConsecutiveOrganize.put(curr_organization_name_inside_lineNo, 1);
							 
//							 line_of_conc_organizationName=line_of_conc_organizationName.substring(line_of_conc_organizationName.lastIndexOf("."), 
//									 																line_of_conc_organizationName.length());
							 // get <concTokens, lineNo>
							 map_seq_concTokens3EXCLAMlineNo.put(counter,conc_organization_name +"!!!"+String.valueOf(curr_organization_name_inside_lineNo));
							 
							 // line only has quotes, those lines' consecutive organization names only needed
							 if(arr_curr_bodyText_without_NLP[curr_organization_name_inside_lineNo-1].indexOf("\"")>=0)
								 map_seq_concTokens3EXCLAMlineNo_onlyQUOTESavailableInThisLINE.put(
										 										counter,
										 							 			conc_organization_name +"!!!"+String.valueOf(curr_organization_name_inside_lineNo));
							 
							 if(is_debug)
								 System.out.println( "inside lineno:"+curr_organization_name_inside_lineNo +" first_begin:"+first_begin 
										 				+" map_lineNo_PositionOFdot:"+map_lineNo_PositionOFdot
										 				+" conc_organization_name:"+conc_organization_name);
							 
							 //get the line and compress line
//							 System.out.println("concatenate organiz:"+conc_organization_name);
						 }
							 conc_organization_name="";
					 }
				 }
			 }
		
			 //are there too many consecutive organization names at the last line? if  > 6 such organizations, then delete all corresponding 2 last lines..
			 int number_line=arr_curr_bodyText_without_NLP.length;
			  
			 if(map_lineNo_CountOFfoundConsecutiveOrganize.size()>0)
			 	 number_line= Collections.max( map_lineNo_CountOFfoundConsecutiveOrganize.keySet());
			 else 
				 number_line=arr_curr_bodyText_without_NLP.length-1;
			 	 
			 if(is_debug)
			 		System.out.println("map_lineNo_CountOFfoundConsecutiveOrganize:"+map_lineNo_CountOFfoundConsecutiveOrganize+" number_line:"+number_line);
			 TreeMap<Integer,Integer> map_tobeDELETEDseq_asKEY=new TreeMap<Integer, Integer>();
			 if(map_lineNo_CountOFfoundConsecutiveOrganize.get(number_line)>7){
				 
				 for(int seq:map_seq_concTokens3EXCLAMlineNo.keySet()){
					 String [] arr_=map_seq_concTokens3EXCLAMlineNo.get(seq).split("!!!");
					 
					 if(Integer.valueOf(arr_[1]) ==number_line){
						 map_tobeDELETEDseq_asKEY.put(seq, -1);
					 }
				 }
				 
			 }
			 if(is_debug)
				 System.out.println("map_tobeDELETEDseq_asKEY:"+map_tobeDELETEDseq_asKEY);
			 // DELETE the too large number of consecutive organization from last line...last line might be junk..
			 for(int seq:map_tobeDELETEDseq_asKEY.keySet()){
					 map_seq_concTokens3EXCLAMlineNo.remove(seq);
					 map_seq_ConsecutiveTokens.remove(seq);
					 map_seq_concTokens3EXCLAMlineNo_onlyQUOTESavailableInThisLINE.remove(seq);
			 }
			 
			 //lineNo having this particular (curr) organization name , person, location
			  
		
			 mapOut.put(1, map_seq_ConsecutiveTokens);
			 mapOut.put(2, map_seq_concTokens3EXCLAMlineNo);
			 mapOut.put(3, map_seq_concTokens3EXCLAMlineNo_onlyQUOTESavailableInThisLINE);
			 
			 if(is_debug)
				 System.out.println("map_lineNo_PositionOFdot:"+map_lineNo_PositionOFdot);
			 
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mapOut;
	}
	
	
	// find the line no that having the current organization / person /location
 private static int find_begin_of_line(TreeMap<Integer, Integer> map_lineNo_PositionOFdot, int first_begin_of_context) {
	 	int positionDOT_just_before_first_begin_of_context=-1;
	 	int min_diff_positions=2000000000;
	 	int last_min_diff_positions=-1;
	 	
	 	int min_valueLineNo=Collections.min(map_lineNo_PositionOFdot.values());
	 	
	 	
		// TODO Auto-generated method stub
		 try {
			 
			 for(int lineno:map_lineNo_PositionOFdot.keySet()){
				 if(first_begin_of_context< min_valueLineNo ){ //first line
					 return 1;
				 }
				 else if( map_lineNo_PositionOFdot.get(lineno) < first_begin_of_context ){
					 
					 if(( first_begin_of_context- map_lineNo_PositionOFdot.get(lineno))<= min_diff_positions ){
						 min_diff_positions= first_begin_of_context- map_lineNo_PositionOFdot.get(lineno);
						 
						 if(min_diff_positions<last_min_diff_positions)
							 positionDOT_just_before_first_begin_of_context=lineno;
					 }
					 
				 }
				 last_min_diff_positions=min_diff_positions;
			 }
			 
			 //its still -1 (NOT right lineNo found, rerun and get the nearest MAXIMUM..
			 if(positionDOT_just_before_first_begin_of_context==-1){
				 min_diff_positions=2000000000;
				 for(int lineno:map_lineNo_PositionOFdot.keySet()){
					 if(first_begin_of_context< min_valueLineNo ){ //first line
						 return 1;
					 }
					 else if( map_lineNo_PositionOFdot.get(lineno) > first_begin_of_context ){
						 
						 if( (map_lineNo_PositionOFdot.get(lineno)-first_begin_of_context)<= min_diff_positions ){
							 min_diff_positions= map_lineNo_PositionOFdot.get(lineno)-first_begin_of_context;
							 
							 if(min_diff_positions<last_min_diff_positions)
								 positionDOT_just_before_first_begin_of_context=lineno;
						 }
						 
					 }
					 last_min_diff_positions=min_diff_positions;
				 }
			 }
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return positionDOT_just_before_first_begin_of_context;
	}


/**
 * identify Name,organization location etc entities and return Map<List>
 * @param text -- data
 * @param model - Stanford model names out of the three models
 * @return
 * 
 *  Output: TreeMap<Integer,TreeMap<Integer,String>> --> <category_id,<seq_id,value>>
 * 			<category_id=map_value>->{1=map_person, 2=map_organization, 3=map_location, 11=map_person_begin,22=map_organization_begin, 33=map_location_begin,
 * 			, 111=map_person_end,111111-><seq,consectivePersonName!!!lineNo>
 * 			222=map_organization_end, 333=map_location_end, 1111=map_person_CSV,11111=map_person_consecutiveTokens
 * 			,2222=map_organization_CSV,22222=map_organization_consecutiveTokens, 222222-><seq,consectiveOrganizationName !!!lineNo>
 *  		3333=map_location_CSV, 33333=map_location_consecutiveTokens, 333333-> <seq,consectiveLocationName!!!lineNo>
 * 			 
 * } 
 */
 public static TreeMap<Integer,TreeMap<Integer,String>> identifyNER(	String text,
		 																String model,
		 																CRFClassifier<CoreLabel> model2,
		 																boolean is_override_with_model2,
		 																boolean isSOPprint,
		 																String debugFile,
		 																boolean is_write_to_debugFile
		 																){
 //LinkedHashMap<String,LinkedHashSet<String>> map=new LinkedHashMap<String,LinkedHashSet<String>>();
 TreeMap<Integer,TreeMap<Integer,String>> mapOut=new TreeMap<Integer, TreeMap<Integer, String>>();
 FileWriter writer_debug=null;
 String serializedClassifier=model;
 try{
	 writer_debug=new FileWriter(new File(debugFile), true);
 if(isSOPprint)
 	System.out.println(serializedClassifier);
 
 CRFClassifier<CoreLabel> classifier =null;
 
 if(is_override_with_model2){
	 classifier=model2;
 }
 else{
	 classifier=CRFClassifier.getClassifierNoExceptions(serializedClassifier);
 }
 int category_id=-1;int category_id_start=-1; int category_id_end=-1;
 int last_category_id_start=-1;
 int last_category_id_end=-1;
 TreeMap<Integer, String> mapTemp=new TreeMap<Integer, String>();
 TreeMap<Integer, String> mapTemp3=new TreeMap<Integer, String>();
 List<List<CoreLabel>> classify = classifier.classify(text);
 int last_word_endposition_plus_1=-1;int last_word_beginposition_plus_1=-1;
 String concLine="";
 TreeMap<String, String> map_already_exist=new TreeMap<String, String>();
 String person_csv="";String location_csv=""; String organization_csv="";
 
 for (List<CoreLabel> coreLabels : classify){
	 concLine="";
	 for (CoreLabel coreLabel : coreLabels){
		 
		 String word = coreLabel.word();
		 if(word==null) continue;
		 
		 String clean_word=word.toLowerCase().replace("'", "").replace(",", "").replace(".", "").replace("=", "");
		 ///stop ward skipin
		 if(Stopwords.is_stopword(clean_word)|| clean_word.length()==0 || clean_word.equalsIgnoreCase("--") || clean_word.equalsIgnoreCase(".")) 
				 	continue;
		 
		 String category = coreLabel.get(CoreAnnotations.AnswerAnnotation.class);
		 	    //category = coreLabel.get(CoreAnnotations.NamedEntityTagAnnotation.class);
		 int begin=coreLabel.beginPosition();
		 int end=coreLabel.endPosition();
		 
		 if(!"O".equals(category)){
			 	 mapTemp=new TreeMap<Integer, String>();
//				 if(map.containsKey(category))
//				 {
//					 // key is already their just insert in arraylist
//					 map.get(category).add(word);
//				 }
//				 else
//				 {
//					 LinkedHashSet<String> temp=new LinkedHashSet<String>();
//					 temp.add(word);
//					 map.put(category,temp);
//				 }
				 
				 //set category_id
				 if(category.equalsIgnoreCase("person")) {category_id=1 ;category_id_start=11; category_id_end=111;}
				 if(category.equalsIgnoreCase("organization")) {category_id=2; category_id_start=22;category_id_end=222;}
				 if(category.equalsIgnoreCase("location")){ category_id=3;category_id_start=33;category_id_end=333;}
				
				 if(category_id<=0 || category_id>3)
					 continue;
				 
				 if(isSOPprint){
					 if(last_word_endposition_plus_1==begin){
						 System.out.println("..Continuation");
					 }
				 }
				 
				 //csv
				 if(category_id==1){
					 //
					 if(person_csv.length()==0  && !Stopwords.is_stopword(word)){
						 	person_csv= word+"_$_person";
					 }
					 else if(person_csv.length()>0  && !Stopwords.is_stopword(word)){
						 person_csv=person_csv+"!!!"+word+"_$_person";
					 }
				 }
				 if(category_id==2){
					 //
					 if(organization_csv.length()==0  && !Stopwords.is_stopword(word) ){
						 organization_csv= word+"_$_organization";
					 }
					 else if(organization_csv.length()>0  && !Stopwords.is_stopword(word) ){
						 organization_csv=organization_csv+"!!!"+word+"_$_organization";
					 }
				 }
				 if(category_id==3){
					 //
					 if(location_csv.length()==0 && !Stopwords.is_stopword(word) ){
						 location_csv= word+"_$_location" ;
					 }
					 else if(location_csv.length()>0 && !Stopwords.is_stopword(word) ){
						 location_csv=location_csv+"!!!"+word+"_$_location";
					 }
				 }
				 
				 
				 //store the position
				 if(!mapOut.containsKey(category_id_start)){
					 //start 
					 mapTemp.put(1 ,String.valueOf(last_word_beginposition_plus_1) );
					 mapOut.put( category_id_start, mapTemp);
					 //end position
					 mapTemp3.put(1 ,String.valueOf(last_word_endposition_plus_1) );
					 mapOut.put(category_id_end, mapTemp3);
				 }
				 else{
					 //start
					 mapTemp=mapOut.get(category_id_start);
					 int sz=mapTemp.size()+1;
					 mapTemp.put(sz ,String.valueOf(last_word_beginposition_plus_1));
					 mapOut.put( category_id_start, mapTemp);
					 //end position
					 mapTemp3=mapOut.get(category_id_end);
					 sz=mapTemp3.size()+1;
					 mapTemp3.put(sz ,String.valueOf(last_word_endposition_plus_1));
					 mapOut.put( category_id_end, mapTemp3);
					 
				 }
				 mapTemp=new TreeMap<Integer, String>();
				 mapTemp3=new TreeMap<Integer, String>();
				 //
				 if(!mapOut.containsKey(category_id)){
					 mapTemp.put(1 , word+" " //+":"+ begin+":"+end+":"+last_word_endposition_plus_1
							 			);
					 mapOut.put( category_id, mapTemp);
				 }
				 else{
					 mapTemp=mapOut.get(category_id);
					 int sz=mapTemp.size()+1;
					 mapTemp.put(sz , word+" " //+":"+ begin+":"+end+":"+last_word_endposition_plus_1
							 			);
					 mapOut.put( category_id, mapTemp);
					 // 
//					 if(last_word_endposition_plus_1==begin){
//						 
//						 if(!map_already_exist.containsKey(mapTemp.get(sz-1)))
//							 concLine=concLine+ mapTemp.get(sz-1);
//						 
//						 if(!map_already_exist.containsKey(mapTemp.get(sz)))
//							 concLine=concLine+" "+mapTemp.get(sz);
//						 
//						 map_already_exist.put(mapTemp.get(sz-1), "");
//						 map_already_exist.put(mapTemp.get(sz), "");
//						 
//					 }
//					 else{
//						 if(mapOut.containsKey(last_category_id_extension))
//							 sz=mapOut.get(last_category_id_extension).size()+1;
//						 else 
//							 sz=1;
//						 TreeMap<Integer,String> mapTemp2=new TreeMap<Integer, String>();
//						 if(sz==1){
//							 mapTemp2.put(sz , concLine);
//							 
//							 mapOut.put(last_category_id_extension,mapTemp2);
//						 }
//						 else{
//							 if(mapOut.get(last_category_id_extension).containsValue(concLine)){
//								 mapTemp2=mapOut.get(last_category_id_extension);
//								 mapTemp2.put(sz , concLine);
//								 mapOut.put(last_category_id_extension,mapTemp2);
//							 }
//						 }
//						 //mapTemp2=new TreeMap<Integer, String>();
//						 concLine="";
//						 
//					 }
				 }
				 mapTemp=new TreeMap<Integer, String>();
				 if(isSOPprint)
					 System.out.println(word+":"+category);
				 last_word_endposition_plus_1=end+1;
				 last_word_beginposition_plus_1=begin+1;
				 //System.out.println("last_word_endposition_plus_1:"+last_word_endposition_plus_1+" concLine:"+concLine);
				 
				 last_category_id_start=category_id_start;
				 last_category_id_end=category_id_end;
		 }
		 //add the last one
		 if(mapOut.containsKey(last_category_id_start)  ){
			 //
			 if(mapOut.get(last_category_id_start).containsValue(concLine)){
				 TreeMap<Integer,String> mapTemp2=new TreeMap<Integer, String>();
				 mapTemp2=mapOut.get(last_category_id_start);
				 mapTemp2.put(mapTemp2.size()+1 , concLine);
				 mapOut.put(last_category_id_start,mapTemp2);
			 }
		 }
		 
	 }
	 
	 
	  String [] arr_curr_bodyText_without_NLP=text.split("\\.");
	  TreeMap<Integer, TreeMap<Integer, String>> map_seq_ConsecutiveTokensEXCLAlineNo_PERSON=new TreeMap<Integer, TreeMap<Integer,String>>();
	  TreeMap<Integer, TreeMap<Integer, String>> map_seq_ConsecutiveTokensEXCLAlineNo_ORGANIZ=new TreeMap<Integer, TreeMap<Integer,String>>();
	  TreeMap<Integer, TreeMap<Integer, String>> map_seq_ConsecutiveTokensEXCLAlineNo_LOCATION=new TreeMap<Integer, TreeMap<Integer,String>>();
	  
	 
	  if(mapOut.get(11)!=null && mapOut.get(111)!=null ){
		 //get consecutive 4 PERSON
		  map_seq_ConsecutiveTokensEXCLAlineNo_PERSON=	get_consecutiveTokens_for_givenTokenMAPbeginNend(
				  																						text, mapOut.get(11),mapOut.get(111)
				  																						 , false );// is_debug
	  }
	  
	  if(mapOut.get(22)!=null && mapOut.get(222)!=null ){
		 // get consecutive 4 ORGANIZATION
		  map_seq_ConsecutiveTokensEXCLAlineNo_ORGANIZ= get_consecutiveTokens_for_givenTokenMAPbeginNend(text, mapOut.get(22),mapOut.get(222)
				  																								, false );// is_debug
	  }
	  
	  if(mapOut.get(33)!=null && mapOut.get(333)!=null ){
		 // get consecutive 4 LOCATION
		  map_seq_ConsecutiveTokensEXCLAlineNo_LOCATION= get_consecutiveTokens_for_givenTokenMAPbeginNend(text, mapOut.get(33),mapOut.get(333)
				  																											, false );// is_debug
	  }
	 
	  
	 //person name
	 mapTemp=new TreeMap<Integer, String>();
	 mapTemp.put( 1,  person_csv);
	 mapOut.put( 1111, mapTemp );
	 mapOut.put( 11111, map_seq_ConsecutiveTokensEXCLAlineNo_PERSON.get(1));
	 mapOut.put( 111111, map_seq_ConsecutiveTokensEXCLAlineNo_PERSON.get(2)); // <seq,consectivePersonName!!!lineNo>
	 mapOut.put( 1111111, map_seq_ConsecutiveTokensEXCLAlineNo_PERSON.get(3)); // <seq,consectivePersonName!!!lineNo> ONLY lines that having quotes ""
	 //organization
	 mapTemp=new TreeMap<Integer, String>();
	 mapTemp.put( 1,  organization_csv);
	 mapOut.put( 2222, mapTemp );
	 mapOut.put( 22222, map_seq_ConsecutiveTokensEXCLAlineNo_ORGANIZ.get(1));
	 mapOut.put( 222222, map_seq_ConsecutiveTokensEXCLAlineNo_ORGANIZ.get(2)); // <seq,consectiveOrganizationName!!!lineNo>
	 mapOut.put( 2222222, map_seq_ConsecutiveTokensEXCLAlineNo_ORGANIZ.get(3)); // <seq,consectiveOrganizationName!!!lineNo> ONLY lines that having quotes ""
	 //location
	 mapTemp=new TreeMap<Integer, String>();
	 mapTemp.put( 1,  location_csv);
	 mapOut.put( 3333, mapTemp );
	 mapOut.put( 33333, map_seq_ConsecutiveTokensEXCLAlineNo_LOCATION.get(1));
	 mapOut.put( 333333, map_seq_ConsecutiveTokensEXCLAlineNo_LOCATION.get(2)); // <seq,consectiveLocationName!!!lineNo>
	 mapOut.put( 3333333, map_seq_ConsecutiveTokensEXCLAlineNo_LOCATION.get(3)); // <seq,consectiveLocationName!!!lineNo> ONLY lines that having quotes ""
	 
	 if(is_write_to_debugFile){
		 writer_debug.append("\n person, organ, mapOut:"+mapOut);
		 writer_debug.flush();
	 }
	 
	 //System.out.println("mapOut:"+mapOut);
 
 }
 
 }
 catch(Exception e){
	 try{
		 writer_debug.append("\n error: person, organ, mapOut:"+mapOut);
		 writer_debug.flush();
	 }
	 catch(Exception e2){}
 }
 //return map;
 return mapOut;
 
 
 }
 
 /// main
 public static void main(String args[]){
	 
 String content="Japan's Renesas Electronics Corp has talked with several companies, including Apple Inc, on a possible sale of its display chip design unit, a source familiar with the situation said on Wednesday."
		 			+"This is 100  USD.";
 
 content="TOKYO ANA Holdings will focus on world destinations where no Japanese airlines currently go, the carrier's incoming president says. &nbsp;&nbsp;&nbsp;&nbsp; The Japanese airline aims to launch service to Istanbul and Mexico City in the near future, and it will broaden the service network to South America and Africa in 10 years, Shinya Katanozaka, who takes the helm Wednesday, told The Nikkei. Shinya Katanozaka Close Shinya Katanozaka &nbsp;&nbsp;&nbsp;&nbsp; Core subsidiary All Nippon Airways competes with Japan Airlines on many international routes. But it doesn't help the people of Japan if the country's two top carriers fly to the same destinations, the new chief said. &nbsp;&nbsp;&nbsp;&nbsp; To respond to travelers\' needs, ANA needs to expand its network to areas where neither airline has service, he said. &nbsp;&nbsp;&nbsp;&nbsp; Asked about carrier operations, Katanozaka said the holding company is eyeing an entry into resort markets through wholly owned subsidiary Vanilla Air and Peach Aviation, in which it owns a roughly 40% stake. Vanilla has been incurring operating losses even after ANA terminated joint ownership with Malaysian carrier AirAsia. &nbsp;&nbsp;&nbsp;&nbsp; It\'s imperative that Vanilla swings to the black for the year ending March 2016, Katanozaka said. Related stories Skymark, Integral accept ANA&#039;s help in turnaround bid i 0; i i 0; i Company in Huge cyber heist via RCBC rocks Philippine banking Foxconn board to take up Sharp deal Wednesday Foxconn, Sharp agree to trim bailout by 100bn yen IHH Healthcare&#039;s real estate arm to acquire nursing home in Japan with a partner SapuraKencana logs net loss, adjusts costs based on oil at $30 per barrel ";
 content="A dog owner who dyed her three chow chows to make them look like pandas has rejected accusations that what she did was cruel. Ms Jiang Meng, 27, said she used a safe and approved dye to paint black patches on their white fur. She runs the website pandachowchows.com, which offers photoshoots with the animals named Tu Dou, Yu Mi and Dou Dou. The dogs have attracted curious onlookers around Singapore, but have raised divided reactions online, with some cooing over their \"cuteness\" and others accusing her of subjecting them to stress and cruelty. Mr Ricky Yeo, president of Action for Singapore Dogs, said the practice is exploitative. \"Anything chemical in nature is always detrimental to the dog's health. I don't really approve,\" he said. The Society for the Prevention of Cruelty to Animals is also against the practice. \"Animals have natural coats and should be appreciated for what they are, rather than trying to alter them artificially.\" But vet Kenneth Tong said that he would not consider it cruelty if the pets show no signs of distress, and if the product is not toxic or an irritant. However, he cautioned that care should be taken when dyeing the fur around sensitive regions, like the eyes, muzzle, nose and genitalia. Ms Jiang told The Straits Times yesterday that her dogs were dyed by a qualified groomer with more than 10 years of experience, adding that areas, such as around the eyes, were \"treated with meticulous care\". Two groomers were also present to make sure that the dogs did not lick the wet dye. The process has to be repeated around every six months. She said it gave her dogs, which she bought last October, \"a new level of energy\" and called her critics \"hypocrites\". \"Maybe they have been to see the pandas at the Singapore Zoo and exchanged money to do so for Now you can see the total hypocrisy of these people.\" The &amp; Veterinary Authority said on Thursday that it is investigating the case to ensure that the dogs were not harmed. It added that the use of safe, food dyes would be unlikely to harm the dogs. Ms Jiang would not name the brand of dye she used. She also said it was safe and approved by authorities, adding that a patch test was done before it was applied, to ensure that her dogs were not allergic to it. Fur dyeing services have been available in Singapore for years. There have been reports since 2010 about the popularity of pet owners in China dyeing dogs to look like pandas and other animals. The South China Morning Post reported in 2014 that the trend had caught on in Hong Kong. In Singapore, though, such extreme makeovers are uncommon. Dog groomers said some owners dye a small part of the dog, such as the tail or paws, but few go for such an extensive dye job. A spokesman for Pet Loft in Upper Paya Lebar Road said he did not think there was anything wrong with the practice, but had not seen much demand for it. \"As long as the colouring is safe and everything is organic, and the dye is not in contact with the skin and sensitive areas, I don't see a problem,\" he said. However, Mr Desmond Chan, of Bubbly Petz, did not feel it was necessary to dye the fur of \"The dogs don't ask for it and it may result in unwanted attention and stress for them.";
// content="The South China Morning Post reported in 2014 that the trend had caught on in Hong Kong. In Singapore, though, such extreme makeovers are uncommon.";
 //4class used before
 String serializedClassifier="/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.muc.7class.distsim.crf.ser.gz";
 		serializedClassifier ="/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.conll.4class.distsim.crf.ser.gz";
 		serializedClassifier ="/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.all.3class.distsim.crf.ser.gz";
 		
 CRFClassifier<CoreLabel>  classifier=CRFClassifier.getClassifierNoExceptions(serializedClassifier);
 String baseFolder="";
 //LinkedHashMap<String, LinkedHashSet<String>>
 /*Output: TreeMap<Integer,TreeMap<Integer,String>> --> <category_id,<seq_id,value>>
 * 			<category_id=map_value>->{1=map_person, 2=map_organization, 3=map_location, 11=map_person_begin,22=map_organization_begin, 33=map_location_begin,
 * 			, 111=map_person_end,111111-><seq,consectivePersonName!!!lineNo>
 * 			222=map_organization_end, 333=map_location_end, 1111=map_person_CSV,11111=map_person_consecutiveTokens
 * 			,2222=map_organization_CSV,22222=map_organization_consecutiveTokens, 222222-><seq,consectiveOrganizationName !!!lineNo>
 *  		3333=map_location_CSV, 33333=map_location_consecutiveTokens, 333333-> <seq,consectiveLocationName!!!lineNo>
 */
 //
 TreeMap<Integer,TreeMap<Integer,String>>  map = StanfordNER.identifyNER(content,
		 						"/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.all.3class.distsim.crf.ser.gz",
		 						//"/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.conll.4class.distsim.crf.ser.gz",
																		classifier,
																		true, // is_override_with_model2
																		false, //isSOPprint
																		baseFolder+"debug.txt",
																		false //is_write_to_debugFile
																		);
     System.out.println("========");
     //
	 for(int s:map.keySet()) {
		 TreeMap<Integer,String> tmp=map.get(s);
		 System.out.println("&&&&&"+s+"<-->"+map.get(s));
		 //
//		 for(int t:tmp.keySet()){	 //temporarily commmented. ...comment/uncomment
//			 System.out.println(s+"<-->"+t+"<-->"+map.get(s).get(t));
//		 }
	 } //
	 
	 //
	 System.out.println("--organiz begin:"+map.get(22) +"\n------------end:"+map.get(222));
	 // 
	 TreeMap<Integer, TreeMap<Integer, String>> map_seq_ConsecutiveTokens=get_consecutiveTokens_for_givenTokenMAPbeginNend(content, map.get(22), map.get(222)
			 																													, false );// is_debug
	 System.out.println("consecutive tokens:"+map_seq_ConsecutiveTokens.get(1) 
	 									+"\n 2:"+map_seq_ConsecutiveTokens.get(2));
	 
 }
 
 
}
