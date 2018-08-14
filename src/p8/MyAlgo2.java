package p8;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.TreeMap;

import crawler.LoadMultipleValueToMap2_AS_KEY_VALUE;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

public class MyAlgo2 {
	
	public static String getSubString(String inString, String startPattern){
		String newString="";
		int beginIndex=inString.indexOf(startPattern );
		int endIndex=inString.indexOf(" "  , beginIndex+1 );
		
		if(beginIndex>=0 && endIndex>=0)
			newString=inString.substring(beginIndex, endIndex).replace(startPattern, " ");
		
		return newString;
	}
	// 
	public static TreeMap<String,Double> myAlgo2(
												String base_baseFolder,
												String baseFolder,
												String inputFile,
												String curr_query,
												boolean is_append_query_docId,
												HashMap<Integer, Double> map_ID_docID_CosineSim, 
												HashMap<Integer, Double> map_ID_docID_Jaccard, 
												HashMap<Integer, Double> map_ID_docID_totalNewWords
							   ){
		
		//Pre-REQUISITION
		// (1) Before running this, run myAlgo() 
		// (2) This method gives output "debug_query_docID.txt" 
		
		int total_num_of_docs_in_given_file=0;
		TreeMap<String, Double> mapOut=new TreeMap<String, Double>();
		TreeMap<Integer, String> map_inFile_eachLine=new TreeMap<Integer, String>();
		FileWriter writer_debug=null;
		FileWriter writer_TP=null;
		FileWriter writer_FP=null;
		FileWriter writer_TN=null;
		FileWriter writer_FN=null;
		TreeMap<String, TreeMap<Integer,Integer>> map_query_N_filteredDocIDasKEY=new TreeMap<String, TreeMap<Integer,Integer>>();
		 double d_s_comp1=0, d_s_c_num1 = 0 , d_s_c_mon=0, d_s_c_yr=0, d_as_nprec_num=0, d_as_nprec_mon=0,d_as_nprec_yr=0;
		 double avg_cosine_simil=0.0;
		 double avg_Jaccard=0.0;
		 double avg_totalNewWords=0.0;
		 int cnt1=0, cnt2=0,cnt3=0;
		  
		 FileWriter write_query_docId=null;
		  
		  //cosine similarity averaging
		 for(int docID:map_ID_docID_CosineSim.keySet()){
			 avg_cosine_simil=avg_cosine_simil+map_ID_docID_CosineSim.get(docID);
		 }
		 avg_cosine_simil=avg_cosine_simil/map_ID_docID_CosineSim.size();
		 // jaccard avg
		 for(int docID:map_ID_docID_Jaccard.keySet()){
			 avg_Jaccard=avg_Jaccard+map_ID_docID_Jaccard.get(docID);
		 }
		 avg_Jaccard=avg_Jaccard/map_ID_docID_Jaccard.size();
		 //total new word avg
		 for(int docID:map_ID_docID_totalNewWords.keySet()){
			 avg_totalNewWords=avg_totalNewWords+map_ID_docID_totalNewWords.get(docID);
		 }
		 avg_totalNewWords=avg_totalNewWords/map_ID_docID_totalNewWords.size();
		 
		try {
			
			write_query_docId=new FileWriter(base_baseFolder+"debug_query_docID.txt" , is_append_query_docId);
			
			writer_debug=new FileWriter(baseFolder+"debug_myAlgo2.txt");
			writer_TP=new FileWriter(baseFolder+"newRUN_true_POSITIVE.txt");
			writer_FP=new FileWriter(baseFolder+"newRUN_false_POSITIVE.txt");
			writer_TN=new FileWriter(baseFolder+"newRUN_true_NEGATIVE.txt");
			writer_FN=new FileWriter(baseFolder+"newRUN_false_NEGATIVE.txt");
			 
			 //
			  map_inFile_eachLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(inputFile, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 " ", //debug_label
																									 false //isPrintSOP
																									 );
			  int lineNumber=0;
			  TreeMap<String,String> map_mapString_to_treemap=new TreeMap<String, String>();
			  System.out.println("lod:"+map_inFile_eachLine.size());
			  
			  //out of(fn_tn)11 0 out of(fn_tn)0 0 out of(fn_tn)11 0 out of (fn+tn)50 a1:1.0 a2:0.050561797752808994 b1:1.0 b2:1.0 a1,a2:true b1,b2:false m1:1.0 
			  //m2:0.0455056179775281 m3:false m4:false m5:false comp1:4.0 c.num:1.0 c.mon:0.0 c.yr:0.0 as.nprec.num:1.0 as.nprec.mon:0.0 as.nprec.yr:0.0 f.off:false
			  //f.off5:false f.off10:false f.off15:false f.off20:false f.off25:false f.off30:false f.off40:false f.off50:false fcnt:0 focus:0 offset:25 
			  //avg_on_sum_TF_IDF_on_verbs_FOR_currDocID:NaN avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID:NaN condVtfidf:NaN condVtfidfbool:false max1:0.0 max2:0.0 
			  //maxtfidfbool:false gtTP:true istfidf:false b1_b2:false a1_a2:true cond_1_:false cond_1_a:false bool_reset_total_count_relabel_1:true predict_for_curr_DocID:0 
			  //Fword.wiki:34.0 Fword.wikiNPREC:180.0 Fword.wikiNPREC(avg):90.0 Fword.bool:false

 
			  DecimalFormat df = new DecimalFormat("000.00");
			  DecimalFormat df2 = new DecimalFormat("00.00");

			  int TP=0, FP=0, TN=0, FN=0;
			  boolean is_print_header=false;
			  int debug_not_available_docID=0;
			  int debug_available_docID_newWords=0;int debug_available_docID_cosine=0;int debug_available_docID_jaccard=0;
			  double avg_on_d_s_comp1=0; int count_hit_d_s_comp1=0;
			  //read and get Average of a feature 
			  for(int seq:map_inFile_eachLine.keySet()){
				  String eachLine=map_inFile_eachLine.get(seq);
				  String s_comp1=getSubString(eachLine,"comp1:").trim();
				  if(s_comp1.length()>0){
					  d_s_comp1= Double.valueOf(getSubString(eachLine,"comp1:"));
					  avg_on_d_s_comp1=avg_on_d_s_comp1+d_s_comp1;
					  count_hit_d_s_comp1++;
				  }
			  }
			  avg_on_d_s_comp1=avg_on_d_s_comp1/ (double) count_hit_d_s_comp1; //average
			  boolean no_smoothing=false;
			  //
			  for(int seq:map_inFile_eachLine.keySet()){
				  String eachLine=map_inFile_eachLine.get(seq);
				  
				  //System.out.println("lineno:"+seq);
				  
				  double a2=0.;double b2=0.; double m1=0; double m2=0; 
				  double a1=0.;double b1=0.; boolean a1_a2=false; boolean b1_b2=false;
				  
				  
				  if(eachLine.indexOf(" 7.")>=0 && eachLine.indexOf("--")>=0){

					  String s_a1=getSubString(eachLine,"a1:").trim();
					  if(s_a1.length()>0)
						  a1= Double.valueOf(getSubString(eachLine,"a1:"));
					  
					  String s_a2=getSubString(eachLine,"a2:").trim(); 
					  if(s_a2.length()>0)
						  a2= Double.valueOf(getSubString(eachLine,"a2:"));
					  // 
					  String s_b1=getSubString(eachLine,"b1:").trim();
					  if(s_b1.length()>0)
						  b1= Double.valueOf(getSubString(eachLine,"b1:"));
					  
					  String s_b2=getSubString(eachLine,"b2:").trim();
					  if(s_b2.length()>0)
						  b2= Double.valueOf(getSubString(eachLine,"b2:"));
					  // no_smoothing
					  if(no_smoothing){
						  a1= Math.sqrt( ((1/a1) - 1) );
						  a2= Math.sqrt( ((1/a2) - 1) );
						  b1= Math.sqrt( ((1/b1) - 1) );
						  b2= Math.sqrt( ((1/b2) - 1) );
					  }
					  
					  String s_m1=getSubString(eachLine,"m1:").trim();
					  if(s_m1.length()>0)
						  m1= Double.valueOf(getSubString(eachLine,"m1:"));
					   

					  String s_m2=getSubString(eachLine,"m2:").trim();
					  if(s_m2.length()>0)
						  m2= Double.valueOf(getSubString(eachLine,"m2:"));
					  
					  String s_comp1=getSubString(eachLine,"comp1:").trim();
					  if(s_comp1.length()>0){
						  d_s_comp1= Double.valueOf(getSubString(eachLine,"comp1:"));
					  }
					  
					  //curr doc num
					  String s_c_num1=getSubString(eachLine,"c.num:").trim();
					  if(s_c_num1.length()>0)
						  d_s_c_num1= Double.valueOf(getSubString(eachLine,"c.num:"));

					  // money
					  String s_c_mon=getSubString(eachLine,"c.mon:").trim();
					  if(s_c_mon.length()>0)
						  d_s_c_mon= Double.valueOf(getSubString(eachLine,"c.mon:"));
					  else{
						  
					  }
					  // yr 
					  String s_c_yr=getSubString(eachLine,"c.yr:").trim();
					  if(s_c_yr.length()>0)
						  d_s_c_yr= Double.valueOf(getSubString(eachLine,"c.yr:"));
					  

					  
					  String as_nprec_num=getSubString(eachLine,"as.nprec.num:").trim();
					  if(as_nprec_num.length()>0)
						  d_as_nprec_num= Double.valueOf(getSubString(eachLine,"as.nprec.num:"));
					  
					  String as_nprec_mon=getSubString(eachLine,"as.nprec.mon:").trim();
					  if(as_nprec_mon.length()>0)
						  d_as_nprec_mon= Double.valueOf(getSubString(eachLine,"as.nprec.mon:"));
					  
					  String as_nprec_yr=getSubString(eachLine,"as.nprec.yr:").trim();
					  if(as_nprec_yr.length()>0)
						  d_as_nprec_yr= Double.valueOf(getSubString(eachLine,"as.nprec.yr:"));
					  

					  String s_a1_a2=getSubString(eachLine,"a1_a2:").trim();
					  if(s_a1_a2.length()>0)
						  a1_a2= Boolean.valueOf(getSubString(eachLine,"a1_a2:"));
					  
					  String s_b1_b2=getSubString(eachLine,"b1_b2:").trim();
					  if(s_b1_b2.length()>0)
						  b1_b2= Boolean.valueOf(getSubString(eachLine,"b1_b2:"));
					   
					  //Fword.wiki:34.0 Fword.wikiNPREC:180.0 Fword.wikiNPREC(avg):90.0 Fword.bool:false
					  double d_Fword_wiki=0;
					  as_nprec_yr=getSubString(eachLine,"Fword.wiki:").trim();
					  if(as_nprec_yr.length()>0)
						  d_Fword_wiki= Double.valueOf(getSubString(eachLine,"Fword.wiki:"));
					  
					  
					  double d_Fword_wikiNPREC_avg=0;
					  as_nprec_yr=getSubString(eachLine,"Fword.wikiNPREC(avg):").trim();
					  if(as_nprec_yr.length()>0)
						  d_Fword_wikiNPREC_avg= Double.valueOf(getSubString(eachLine,"Fword.wikiNPREC(avg):"));
					  
					  boolean b_Fword_bool=false;
					  String s_Fword_bool=getSubString(eachLine,"Fword.bool:").trim();
					  if(s_Fword_bool.length()>0)
						  b_Fword_bool= Boolean.valueOf(getSubString(eachLine,"Fword.bool:"));
					   
					  int currDocID=0;
					  String s_currDocID=getSubString(eachLine," currDocID:").trim();
					  if(s_currDocID.length()>0){
						  currDocID= Integer.valueOf(s_currDocID);
						  
						  //<query,<docid,>>  at a time, only 1 query name comes actualy
						  if(! map_query_N_filteredDocIDasKEY.containsKey(curr_query)){
							  TreeMap<Integer,Integer> temp=new TreeMap<Integer, Integer>();
							  temp.put( currDocID , -1);
							  map_query_N_filteredDocIDasKEY.put( curr_query , temp); 
						  }
						  else{ //
							  TreeMap<Integer,Integer> temp=map_query_N_filteredDocIDasKEY.get(curr_query);
							  temp.put( currDocID , -1);
							  map_query_N_filteredDocIDasKEY.put( curr_query , temp);
						  }
						  
					  }
					  
					  //System.out.println("got dociD:"+currDocID);
					   
					  int i_predict_for_curr_DocID=0;
					  String s_predict_for_curr_DocID=getSubString(eachLine,"predict_for_curr_DocID:").trim().replace(" ", "");
					  if(s_predict_for_curr_DocID.length()>0)
						  i_predict_for_curr_DocID= Integer.valueOf(s_predict_for_curr_DocID);
					  else{
						  writer_debug.append("\n error: not found s_predict_for_curr_DocID:");
						  writer_debug.flush();
					  }
					  
					  //
					  double d_max1=0;
					  String max1_TF_IDF_on_verbs_FOR_currDocID=getSubString(eachLine,"max1:").trim();
					  if(max1_TF_IDF_on_verbs_FOR_currDocID.length()>0)
						  d_max1= Double.valueOf(max1_TF_IDF_on_verbs_FOR_currDocID);
					  
					  double d_max2=0;
					  String max2_TF_IDF_on_verbs_FOR_currDocID=getSubString(eachLine,"max2:").trim();
					  if(max2_TF_IDF_on_verbs_FOR_currDocID.length()>0)
						  d_max2= Double.valueOf(max2_TF_IDF_on_verbs_FOR_currDocID);
					  
					   
					  	  boolean gtTP=false;
						  String s_gtTP=getSubString(eachLine," gtTP:").trim();
						  if(s_gtTP.length()>0)
							  gtTP= Boolean.valueOf(s_gtTP);
						  else{
							  writer_debug.append("\n error: not found GT TP:"+gtTP+" for docid:"+currDocID+" lineNo:"+seq
									  				+" currLine:"+eachLine);
							  writer_debug.flush();
						  }
						  
						  boolean B_cond_1_=false;
						  String s_cond_1_=getSubString(eachLine,"cond_1_:").trim();
						  if(s_cond_1_.length()>0)
							  B_cond_1_= Boolean.valueOf(getSubString(eachLine,"cond_1_:"));
						  
						  boolean B_cond_1_a=false;
						  String s_cond_1_a=getSubString(eachLine,"cond_1_a:").trim();
						  if(s_cond_1_a.length()>0)
							  B_cond_1_a= Boolean.valueOf(getSubString(eachLine,"cond_1_a:"));
						    
//						  String header="";
//							//header
//							if(is_print_header==false){
//								header=
//									df.format(1)+" "+df.format(2)+" "+df.format(3)+df.format(4)+" "+df.format(5)+" "+df.format(6)+df.format(7)+" "+df.format(8)+" "+df.format(9)
//									+" "+df.format(10)+" "+df.format(11)+" "+df.format(12)+df.format(13)+" "+df.format(14)+" "+df.format(15)+df.format(16)+" "+df.format(17)+" "+df.format(18)
//									+" "+df.format(19)+" "+df.format(20)+" "+df.format(21)+df.format(22)+" "+df.format(23)+" "+df.format(24)+df.format(25)+" "+df.format(26)+" "+df.format(27);
//								is_print_header=true;
//								
////								writer_FN.append("\n"+header);writer_FN.flush();
////								writer_FP.append("\n"+header);writer_FP.flush();
////								writer_TP.append("\n"+header);writer_TP.flush();
////								writer_TN.append("\n"+header);writer_TN.flush();	
//							}
					String currPrintString="1:a1:"+a1+" 2:a2:"+a2+" 3:b1:"+b1+" 4:b2:"+b2+ " 5:m1:"+m1+" 6:m2:"+m2+" 7:d_s_comp1:"+d_s_comp1+" 8:dum:"+"du"
				  			+" 9:a1_a2:"+a1_a2+" a:b1_b2:"+b1_b2 +" b:du:"+  " "+" c:d_s_c_num1:"+d_s_c_num1+" d:d_s_c_mon:"+df2.format(d_s_c_mon)
				  			+" e:d_s_c_yr:"+d_s_c_yr+" f:d_as_nprec_num:"+d_as_nprec_num+" g:d_as_nprec_mon:"+d_as_nprec_mon
				  			+" h:d_as_nprec_yr:"+d_as_nprec_yr+" i:d_Fword_wiki:"+d_Fword_wiki+" j:d_Fword_wikiNPREC_avg:"+d_Fword_wikiNPREC_avg
				  			+" k:gtTP:"+gtTP+" l:max1:"+d_max1+" m:max2:"+d_max2
				  			+" n:B_cond_1_:"+B_cond_1_+" o:B_cond_1_a:"+B_cond_1_a 
				  			+" p:predict:"+i_predict_for_curr_DocID +" q:a1/a2:"+df.format(a1/a2)+" r:b1/b2:"+df.format(b1/b2)
				  			+" !:docID:"+currDocID;
					//
					int print_type=2; //switch and see outut for right feature name
					if(print_type==1){
						//FORMATTED PRINT (EASY TO READ) <-- match this with above (example 1: with above 1:)
						currPrintString=
								" 1:"+df.format(a1)+" 2:"+df.format(a2)+" 3:"+df.format(b1)+" 4:"+df.format(b2)+ " 5:"+df.format(m1)
								+" 6:"+df.format(m2)+" 7:"+df.format(d_s_comp1)+" 8:"+"du"
					  			+" 9:"+a1_a2+" a:"+b1_b2 +" b:"+ "du"+" c:"+df2.format(d_s_c_num1)+" d:"+df2.format(d_s_c_mon)
					  			+" e:"+df2.format(d_s_c_yr)+" f:"+df2.format(d_as_nprec_num)+" g:"+df2.format(d_as_nprec_mon)
					  			+" h:"+df2.format(d_as_nprec_yr)+" i:"+df.format(d_Fword_wiki)+" j:"+df.format(d_Fword_wikiNPREC_avg)
					  			+" k:"+gtTP+" l:"+df.format(d_max1)+" m:"+df.format(d_max2)
					  			+" n:"+B_cond_1_+" o:"+B_cond_1_a
					  			+" p:"+i_predict_for_curr_DocID+" q:"+df.format(a1/a2)+" r:"+df.format(b1/b2)
					  			+" !:"+currDocID;
					}
					else {
						currPrintString=
								" "+df.format(a1)+" "+df.format(a2)+" "+df.format(b1)+" "+df.format(b2)+ " "+df.format(m1)
								+" "+df.format(m2)+" "+df.format(d_s_comp1)+" "+"du"
					  			+" "+a1_a2+" "+b1_b2 +" "+  "du"+" "+df2.format(d_s_c_num1)+" "+df2.format(d_s_c_mon)
					  			+" "+df2.format(d_s_c_yr)+" "+df2.format(d_as_nprec_num)+" "+df2.format(d_as_nprec_mon)
					  			+" "+df2.format(d_as_nprec_yr)+" "+df.format(d_Fword_wiki)+" "+df.format(d_Fword_wikiNPREC_avg)
					  			+" "+gtTP+" "+df.format(d_max1)+" "+df.format(d_max2)
					  			+" "+B_cond_1_+" "+B_cond_1_a
					  			+" "+i_predict_for_curr_DocID+" "+df.format(a1/a2)+" "+df.format(b1/b2)
					  			+" "+currDocID;
					  }
//					  System.out.println(currPrintString
							  			//+" for line:"+eachLine
	//						  			);

					  int predict_for_currDocID=-1;
					  
					  ///pick from past run ****
					  predict_for_currDocID=i_predict_for_curr_DocID;

					  boolean is_predict_positive=false;

					  if( java.lang.Math.sqrt( a1*b1) < java.lang.Math.sqrt( a2*b2)
							  //&&(d_s_c_num1 ) < ( d_as_nprec_num )
							  )
							  is_predict_positive=true;
					  
					  //System.out.println("cosinesime:"+map_ID_docID_CosineSim);
					  
					  double diff3=0.0;
					  double curr_docID_cosine=0.0;
					  double curr_docID_Jaccard=0.0;
					  double curr_docID_newWords=0.0;
					  //COSINE/jaccard/new word
					  if(map_ID_docID_totalNewWords.containsKey(currDocID)){
						  curr_docID_newWords=map_ID_docID_totalNewWords.get(currDocID);
						  debug_available_docID_newWords++;
					  }
					  if(map_ID_docID_Jaccard.containsKey(currDocID)){
						  curr_docID_Jaccard=map_ID_docID_Jaccard.get(currDocID);
						  debug_available_docID_jaccard++;
					  }
					  if(map_ID_docID_CosineSim.containsKey(currDocID)){
						  curr_docID_cosine=map_ID_docID_CosineSim.get(currDocID);
						  debug_available_docID_cosine++;
					  }
					  
					  double offset_a1_b1=0.1;

					  //
					  if(d_s_c_num1==0 &&d_s_c_yr==0  &&d_as_nprec_yr==0){
						  //d_s_c_num1=1;d_s_c_yr=1;d_as_nprec_yr=1;
					  }
					  double alpha=0.95; 					  
					  double penalty=  alpha* java.lang.Math.sqrt( 
							  			((d_s_c_num1+ d_s_c_yr) - (d_as_nprec_num + d_as_nprec_yr)) );
					  
					  double penalty_NOsmoothing=alpha* ((d_s_c_num1+ d_s_c_yr) - (d_as_nprec_num + d_as_nprec_yr)) ;
					  
					  double min=0.0;double diff1=0.; double diff2=0.0; double diff1_1=0.;
					  double diff2_NoSmoothing=0.0;
					  //
					  if(String.valueOf(diff2).indexOf("NaN") >=0){
						  min=diff1_1;
						  //penalty=1;
					  }else{
						  min=java.lang.Math.max(diff1_1, diff2);
					  }
					  
					    diff1=( curr_docID_newWords -  avg_totalNewWords);
					    diff1_1=( curr_docID_newWords -  avg_totalNewWords)/(double)curr_docID_newWords;
					    diff2=(java.lang.Math.sqrt( a1*b1) *d_s_comp1 - penalty* java.lang.Math.sqrt( a2*b2) *avg_on_d_s_comp1);
					    //smoothing
					    if(no_smoothing)
					    	diff2_NoSmoothing=( ( a1*b1) *d_s_comp1 - penalty_NOsmoothing * ( a2*b2) *avg_on_d_s_comp1);
					    
					  //System.out.println("dif:"+diff1+" "+diff2+" "+cnt1+" "+cnt2+" "+cnt3);
					  
					  if(String.valueOf(diff2).indexOf("NaN") >=0){
						 diff2=1;
						 if(no_smoothing)
						    	diff2_NoSmoothing=1;
					  }
					  
					  //if(diff1>0) diff1=0;
					   
					  //predict
//					  if(   diff1> 0 //|| 
//							||diff2<0  //|| String.valueOf(diff2).indexOf("NaN") >=0
							  //&& d_s_comp1 <  avg_on_d_s_comp1
					    
					    // max ( diff_content_as_cut_cost , minimize( cut_cost) )
					   //alpha=0.95;   double beta=0.8;
					   //double diff3= alpha* diff1 + beta*(1/diff2);
					   
					   if(no_smoothing){
						   diff3 = diff1 * diff2_NoSmoothing;
					   }else{
						   diff3= diff1 * diff2;
					   }
					  
					  //if(no_smoothing==false){
					  if(  diff3>0  ){
							  predict_for_currDocID=1;
							  if(diff1>0){
								  cnt1++;
									  System.out.println("if1:"+a1+" "+b1+" comp1:"+d_s_comp1+" pen:"+penalty+" a2:"+   a2+" b2:"+b2+" c1:"+ avg_on_d_s_comp1
													   +" c2:"+ d_s_c_num1+ " c3:"+d_s_c_yr+" c4:"+d_as_nprec_num +" c5:"+ d_as_nprec_yr 
													   +" diff1_1:"+diff1_1+" min:"+diff1_1 +" diff2:"+diff2
											  			);
									  writer_debug.append("i:"+" "+df.format(diff1)+" "+df.format(diff1_1) +" "+df.format(diff2)+" "+
					  										a1+" "+b1+" "+d_s_comp1+" "+penalty+" "+   a2+" "+b2+" "+ avg_on_d_s_comp1
					  										+" "+ d_s_c_num1+ " "+d_s_c_yr+" "+d_as_nprec_num +" "+ d_as_nprec_yr +" <-if1\n" 
			  					  				);
									  writer_debug.flush();
							  }
							  if(diff2<0){
								  cnt2++;
								  System.out.println("if2:a1:"+a1+" b1:"+b1+" comp1:"+d_s_comp1+" pen:"+penalty+" a2:"+   a2+" b2:"+b2+" c1:"+ avg_on_d_s_comp1
											   +" c2:"+ d_s_c_num1+ " c3:"+d_s_c_yr+" c4:"+d_as_nprec_num +" c5:"+ d_as_nprec_yr 
											   +" diff1_1:"+diff1_1+" min:"+diff1_1 +" diff2:"+diff2+" <-if2"
										  );
								  writer_debug.append("i:"+" "+df.format(diff1)+" "+df.format(diff1_1) +" "+df.format(diff2)+" "+
							  							a1+" "+b1+" "+d_s_comp1+" "+penalty+" "+   a2+" "+b2+" "+ avg_on_d_s_comp1
							  							+" "+ d_s_c_num1+ " "+d_s_c_yr+" "+d_as_nprec_num +" "+ d_as_nprec_yr+" <-if2\n" 
					  					  				);
								  writer_debug.flush();
							  }
							  
						  }
						  else{
							  predict_for_currDocID=0;
							
//							  writer_debug.append("else:a1:"+a1+" b1:"+b1+" comp1:"+d_s_comp1+" pen:"+penalty+" a2:"+   a2+" b2:"+b2+" c1:"+ avg_on_d_s_comp1
//										   			+" c2:"+ d_s_c_num1+ " c3:"+d_s_c_yr+" c4:"+d_as_nprec_num +" c5:"+ d_as_nprec_yr 
//										   			+" diff1_1:"+diff1_1+" min:"+diff1_1 +" diff2:"+diff2
//										  			);
							  
							  writer_debug.append("e:"+" "+df.format(diff1)+" "+df.format(diff1_1) +" "+df.format(diff2)+" "+
									  				a1+" "+b1+" "+d_s_comp1+" "+penalty+" "+   a2+" "+b2+" "+ avg_on_d_s_comp1
							   						+" "+ d_s_c_num1+ " "+d_s_c_yr+" "+d_as_nprec_num +" "+ d_as_nprec_yr +" <-else\n"
							  					  );
							  writer_debug.flush();
							  
							  if(String.valueOf(diff2).indexOf("NaN") >=0){
								  System.out.println("else: NAN value:"+a1+" "+b1+" comp1:"+d_s_comp1+" pen:"+penalty+" a2:"+   a2+" b2:"+b2+" c1:"+ avg_on_d_s_comp1
										   +" c2:"+ d_s_c_num1+ " c3:"+d_s_c_yr+" c4:"+d_as_nprec_num +" c5:"+ d_as_nprec_yr 
										   +" diff1_1:"+diff1_1+" min:"+diff1_1 +" diff2:"+diff2
										   +"\n"
										  );
								  	
								  cnt3++;
							  }
							  
						  } //END if(  diff3>0  ){
					   
					  

//					  if(map_ID_docID_CosineSim.containsKey(currDocID)){
//						  if( map_ID_docID_CosineSim.get(currDocID) < avg_cosine_simil ){
//							  predict_for_currDocID=1;
//						  }
//						  else{
//							  predict_for_currDocID=0;
//						  }
//					  }
//					  else{
//						  	  predict_for_currDocID=0;
//					  }
					  
					  
//					  if(map_ID_docID_Jaccard.containsKey(currDocID)){
//						  if( map_ID_docID_Jaccard.get(currDocID) > avg_Jaccard ){
//							  predict_for_currDocID=1;
//						  }
//						  else{
//							  predict_for_currDocID=0;
//						  }
//					  }
//					  else{
//						  	  predict_for_currDocID=0;
//					  }
					  
					  
					  //predict 
//					  if(  //( java.lang.Math.min(a1, b1) < offset_a1_b1*java.lang.Math.min(a2, b2) )
//							  
//							  //java.lang.Math.sqrt(  java.lang.Math.min(a1, b1)) < offset_a1_b1* java.lang.Math.sqrt( java.lang.Math.min(a2, b2) )
//							  java.lang.Math.sqrt( a1*b1) < java.lang.Math.sqrt( a2*b2) 
//							  //&& 
//							  //((d_s_c_num1+d_s_c_mon+d_s_c_yr) < 0.8*( d_as_nprec_num + d_as_nprec_mon+  d_as_nprec_yr))
//							  //((d_s_c_num1+ d_s_c_yr) > (d_as_nprec_num + d_as_nprec_yr)
//								//|| 
//							  && java.lang.Math.sqrt(d_s_c_num1) > java.lang.Math.sqrt(d_as_nprec_num)  && d_s_c_num1<=4
//								//)
//							 //&& ( d_s_comp1>10 )
//							 //&& d_Fword_wiki > d_Fword_wikiNPREC_avg
//							 &&
//							 (a1/a2)>=1
//							  //|| (d_s_c_num1>=3 & d_s_c_yr>0.00)
//							 ){
//						  predict_for_currDocID=1;
//						  //if(d_s_comp1<=0)
//							  //predict_for_currDocID=0;
//					  }
//					  else{
//						  predict_for_currDocID=0;
//					  }
//					  
////					  if( java.lang.Math.sqrt(  java.lang.Math.min(a1, b1)) < offset_a1_b1* java.lang.Math.sqrt( java.lang.Math.min(a2, b2) )
////							  && predict_for_currDocID==0
////							  &&   d_s_comp1<=0
////							  )
////					  		predict_for_currDocID=1;
//					  
//					  if(predict_for_currDocID==0) {
//						  System.out.println("neg:"+a1 
//								  			+" for docId:"+i_s_currDocID+ " num:"+(a1<=0.00)+ " "+eachLine);
//						  
////						  	if(a1<=0000.0000  // && d_s_c_num1>0.00
////						  				)
////							  predict_for_currDocID=1;
//						  if(d_s_comp1>10 && d_s_c_num1>=1  ) //&& is_predict_positive) //best
//							  predict_for_currDocID=1;
//					  }
//					  
//					  if((a1/a2)<0.8 && predict_for_currDocID==0)
//						  predict_for_currDocID=1;
//					  else if((a1/a2)<0.8 && predict_for_currDocID==1)
//						  predict_for_currDocID=0;
//					  
//					  if( predict_for_currDocID==1 && (a1>=0.04 || a2-a1>0.05   ))
//						  predict_for_currDocID=0;
					  
//					  if(predict_for_currDocID==1 && (d_s_c_num1>=3 ))
//						  predict_for_currDocID=0;
					  
					  //confusion matrix
					  if(predict_for_currDocID==1 && gtTP ==true){
						  //System.out.println(" TP: for DocId:"+currDocID+" "+ (a1/a2));
						  TP++;
						  writer_TP.append("\n"+currPrintString);
						  writer_TP.flush();
					  }
					  else if(predict_for_currDocID==0 && gtTP ==false){
						  //System.out.println(" TN: for DocId:"+currDocID+" "+ (a1/a2));
						  TN++;
						  writer_TN.append("\n"+currPrintString);
						  writer_TN.flush();
					  }
					  if(predict_for_currDocID==1 && gtTP ==false){
						  //System.out.println(" FP for DocId:"+currDocID+" "+ (a1/a2));
						  FP++;
						  writer_FP.append("\n"+currPrintString);
						  writer_FP.flush();
					  }
					  if(predict_for_currDocID==0 && gtTP ==true){
						  //System.out.println(" FN for DocId:"+currDocID +" "+ (a1/a2));
						  FN++;
						  writer_FN.append("\n"+currPrintString);
						  writer_FN.flush();
					  }
				  }
				  
			  }
			  
			  String print_metric="TP:"+TP+" TN:"+TN+" FP:"+FP+" FN:"+FN;
			  
			  
			  System.out.println("------>debug_not_available_docID:"+debug_not_available_docID
					  			+" debug_available_docID:"+debug_available_docID_cosine+" "+debug_available_docID_jaccard+" "+debug_available_docID_newWords);
			  
			  double total=TP+TN+FP+FN;
			  double precision=TP/(double)(TP+FP);
			  double recall=TP/(double)(TP+FN);
			  double F1=2*(precision*recall)/(precision+recall);
			  double accuracy=(TP+TN)/(double) total;
			  
			  writer_debug.append("\n ENTERING total document:"+total+" for query="+inputFile);
			  writer_debug.flush();
			  
			  mapOut.put("TP",(double) TP);mapOut.put("TN", (double)TN); mapOut.put("FP", (double)FP);mapOut.put("FN", (double)FN);
			  mapOut.put("precis",(double) precision); mapOut.put("recall",(double) recall);mapOut.put("f1",(double) F1);mapOut.put("accura",(double) accuracy);

			  
			  write_query_docId.append(curr_query+"!!!"+map_query_N_filteredDocIDasKEY.get(curr_query)+"\n");
			  write_query_docId.flush();
			  
			  System.out.println(print_metric);
			  System.out.println("precision:"+precision+" recall:"+recall +" F1:"+F1+" total:"+total+" accuracy:"+accuracy);
			  
			   
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mapOut;
	}
	
	// main
	public static void main(String[] args) {
		
		DecimalFormat df1_4 = new DecimalFormat("0.0000");
		
		//(1) grep "ENTERING" on output debug file to know each query matched document count
		//(2) after running this, run calc_3_similarity()
		
		
		//baseFolder="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data_prob/P8/p8_wikipedia_government_of_india";
		String neighbour_folder="neigh3";
		String base_baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/"+neighbour_folder+"/";
		String baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/"+neighbour_folder+"/";
		String baseFolder_for_chrono="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/"
																															 +neighbour_folder+"/";		
		String inputFile="";
		String in=" 7.TP:4--5 5 5 focus:0 a1:0.012195121951219513 a2:0.0889679715302491 b1:1.0 b2:1.0 m1:0.012195121951219513 m2:0.08007117437722419 m3:true m4:true m5:true a1,a2:false b1,b2:false comp1:56.0 c.num:1.0 c.mon:0.0 c.yr:0.0 as.nprec.num:3.4 as.nprec.mon:0.0 as.nprec.yr:0.6 f.off:false f.off5:false f.off10:false f.off15:false f.off20:false f.off25:false f.off30:false f.off40:false f.off50:false fcnt:0 focus:0 offset:25 avg_on_sum_TF_IDF_on_verbs_FOR_currDocID:NaN avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID:NaN condVtfidf:NaN condVtfidfbool:false max1:0.0 max2:0.0 maxtfidfbool:false gtTP:true istfidf:false b1_b2:false a1_a2:false cond_1_:true cond_1_a:true bool_reset_total_count_relabel_1:false predict_for_curr_DocID:1 Fword.wiki:181.0 Fword.wikiNPREC:202.0 Fword.wikiNPREC(avg):101.0 Fword.bool:true currDocID:5487";
		//
		System.out.println(getSubString(in," currDocID:"));
		
		String input_cosineFile="";
		String input_Jaccard="";
		String input_totalNewWords="";
		
		int flag=2;
		
		// below files produced by calc_3_similarity.java
		if(flag==1){
		     input_cosineFile=baseFolder_for_chrono+"docID_CosineSim.txt";
			 input_Jaccard=baseFolder_for_chrono+"docID_Jaccard.txt";
			 input_totalNewWords=baseFolder_for_chrono+"docID_totalNewWords.txt";
		}
		else{
			input_cosineFile=baseFolder_for_chrono+"docID_CosineSim_NORMALIZ.txt";
			input_Jaccard=baseFolder_for_chrono+"docID_Jaccard_NORMALIZ.txt";
			input_totalNewWords=baseFolder_for_chrono+"docID_totalNewWords_NORMALIZ.txt";
		}
		
		TreeMap<String, String> map_SS_docID_CosineSim=LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE(
																										  input_cosineFile,
																											 "!!!",
																											 "1,2",
																											 "" , //append_suffix_at_each_line
																											 false, //is_have_only_alphanumeric
																											  false //isPrintSOP
																											 );
		  
		  HashMap<Integer, Double> map_ID_docID_CosineSim=new HashMap();
		  //
		  for(String docID:map_SS_docID_CosineSim.keySet()){
			  map_ID_docID_CosineSim.put(Integer.valueOf(docID), Double.valueOf(map_SS_docID_CosineSim.get(docID)) ); 
		  }
		  
		  TreeMap<String, String> map_SS_docID_Jaccard=LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE(
																											  input_Jaccard,
																												 "!!!",
																												 "1,2",
																												 "" , //append_suffix_at_each_line
																												 false, //is_have_only_alphanumeric
																												  false //isPrintSOP
																												 );
		  
 
		  
		  HashMap<Integer, Double> map_ID_docID_Jaccard=new HashMap();
		  // 
		  for(String docID:map_SS_docID_Jaccard.keySet()){
			  map_ID_docID_Jaccard.put(Integer.valueOf(docID), Double.valueOf(map_SS_docID_Jaccard.get(docID)) ); 
		  }
		  
		  TreeMap<String, String> map_SS_docID_totalNewWords=LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE(
				  																									  input_totalNewWords,
																													 "!!!",
																													 "1,2",
																													 "" , //append_suffix_at_each_line
																													 false, //is_have_only_alphanumeric
																													  false //isPrintSOP
																													 );
		  HashMap<Integer, Double> map_ID_docID_totalNewWords=new HashMap();
		  //
		  for(String docID:map_SS_docID_totalNewWords.keySet()){
			  map_ID_docID_totalNewWords.put(Integer.valueOf(docID), Double.valueOf(map_SS_docID_totalNewWords.get(docID)) ); 
		  }
		
		  System.out.println("size loaded:"+map_ID_docID_totalNewWords.size()+" "+map_ID_docID_Jaccard.size()
				  			+" "+map_ID_docID_CosineSim.size());
		  
		  TreeMap<String,Double> mapERROR=new TreeMap<String, Double>(); int num_ERROR=0;
		  TreeMap<String,TreeMap<String,Double>> mapERRORGlobal=new TreeMap<String,TreeMap<String, Double>>();
		  TreeMap<String,TreeMap<String,Double>> mapOutGlobal=new TreeMap<String,TreeMap<String, Double>>(); 
		  TreeMap<String,Double> mapOut=new TreeMap<String, Double>(); int cnt=0; 
		  double total_TP=0, total_TN=0, total_FP=0, total_FN=0, total_precision=0, total_recall=0, total_f1=0, total_accuracy=0;
		  String query_Folder_CSV="swine,collapse,mining,road acciden,kidnap,juvenile,migrant,ebola,slavery,traffick,fire,senior citize";
		  query_Folder_CSV="swine,collapse,mining,road acciden,kidnap,juvenile,migrant,ebola,slavery,traffick,fire,senior citize";
		  
		  //query_Folder_CSV="senior";
		  String [] arr_curr_query_Foler=query_Folder_CSV.split(",");
		  int num_given_query_folders=arr_curr_query_Foler.length;
		  double curr_total=0;double total_on_curr_total=0;
		  FileWriter debug_writer=null;
		  try{
			  debug_writer=new FileWriter(baseFolder+"debug_Filted_docs_count.txt");
		  }
		  catch(Exception e){
			  e.printStackTrace();
		  }
		  boolean is_append_query_docId=false;
		  long t0 = System.nanoTime();

		  //each query folder
		  while(cnt<arr_curr_query_Foler.length){
			  // current query folder
			  baseFolder=base_baseFolder+arr_curr_query_Foler[cnt]+"/";

			  inputFile=baseFolder+"currRUN_all.txt";
			  System.out.println("inputFile:"+inputFile);
			  mapERROR=new TreeMap<String, Double>();
			  
			  if(cnt>0)
				  is_append_query_docId=true; //
			  
			  	//fire, collaps, senior, ebola.10 <-- error or high FP
				// calling 
			  mapOut=myAlgo2(
					  		base_baseFolder,
							baseFolder,
							inputFile,
							arr_curr_query_Foler[cnt], // curr_query
							is_append_query_docId,
							map_ID_docID_CosineSim,
							map_ID_docID_Jaccard,
							map_ID_docID_totalNewWords
							);
			  
			  //error (non zero)
			  if(mapOut.get("TP").toString().indexOf("NaN")>=0) {mapOut.put("TP",0.);mapERROR.put("TP", -1.0);}
			  if(mapOut.get("TN").toString().indexOf("NaN")>=0){mapOut.put("TN",0.);mapERROR.put("TN", -1.0);}
			  if(mapOut.get("FP").toString().indexOf("NaN")>=0){mapOut.put("FP",0.);mapERROR.put("FP", -1.0);}
			  if(mapOut.get("FN").toString().indexOf("NaN")>=0){mapOut.put("FN",0.);mapERROR.put("FN", -1.0);}
			  if(mapOut.get("f1").toString().indexOf("NaN")>=0){mapOut.put("f1",0.);mapERROR.put("f1", -1.0);}
			  if(mapOut.get("precis").toString().indexOf("NaN")>=0){mapOut.put("precis",0.);mapERROR.put("precis", -1.0);}
			  if(mapOut.get("recall").toString().indexOf("NaN")>=0){mapOut.put("recall",0.);mapERROR.put("recall", -1.0);}
			  if(mapOut.get("accura").toString().indexOf("NaN")>=0){mapOut.put("accura",0.);mapERROR.put("accura", -1.);}
			  
			  total_TP=total_TP+mapOut.get("TP");total_TN=total_TN+mapOut.get("TN");total_FP=total_FP+mapOut.get("FP");total_FN=total_FN+mapOut.get("FN");
			  total_precision=total_precision+mapOut.get("precis");total_recall=total_recall+mapOut.get("recall");total_f1=total_f1+mapOut.get("f1");total_accuracy=total_accuracy+mapOut.get("accura");
			  
			  //total
			  curr_total=mapOut.get("TP")+mapOut.get("TN")+mapOut.get("FP")+mapOut.get("FN");
			  total_on_curr_total=total_on_curr_total+curr_total;
			  
			  // accumulate
				  mapOutGlobal.put(arr_curr_query_Foler[cnt], mapOut);
			  //
			  if(mapERROR.size()>0){
				  mapERRORGlobal.put(arr_curr_query_Foler[cnt], mapERROR);
				  num_ERROR++;
			  }
			  cnt++;
			  
			  try{
				  debug_writer.append( "\n curr_total:"+curr_total +" "+inputFile
						  			 +" total_on_curr_total:"+total_on_curr_total );
				  debug_writer.flush();
			  }
			  catch(Exception e){
				  
			  }
			  
		  } //END while(cnt<arr_curr_query_Foler.length){ 
		  
	    	System.out.println("Time Taken:"
					+ NANOSECONDS.toSeconds(System.nanoTime() - t0) + " seconds; "
					+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
					+ " minutes");
		  
		  //
		  for(String cquery:mapOutGlobal.keySet()){
			  System.out.println("query:"+cquery+" mapout:"+mapOutGlobal.get(cquery));
		  }
		  
		  System.out.println("summary:total_TP:"+total_TP+" total_TN:"+total_TN+" total_FP:"+total_FP+" total_FN:"+total_FN);
		  System.out.println(
				  				"**Precis(avg):"+ total_precision /(double)num_given_query_folders
				  				+" Recall(avg):"+total_recall/(double)num_given_query_folders
				  				+" F1(avg):"+total_f1/(double)num_given_query_folders
				  				+" Acc(avg):"+ total_accuracy /(double)num_given_query_folders
				  				+"\n**TP(avg):"+total_TP /(double)num_given_query_folders
				  				+" TN(avg):"+total_TN/(double)num_given_query_folders
				  				+" FP(avg):"+total_FP/(double)num_given_query_folders
				  				+" FN(avg):"+total_FN/(double)num_given_query_folders
				  				+" total_on_curr_total:"+total_on_curr_total
				  				+"\n error(NaN reset to 0):"+mapERRORGlobal
				  			);
		  String latex= df1_4.format(total_precision /(double)num_given_query_folders)
		  				+","+df1_4.format(total_recall/(double)num_given_query_folders)
		  				+","+df1_4.format(total_f1/(double)num_given_query_folders)
		  				+","+ df1_4.format(total_accuracy /(double)num_given_query_folders);
		  
		  System.out.println("latex:"+latex);
		  System.out.println("output->"+base_baseFolder+"debug_query_docID.txt"
		  					+"\n out(f1,recall,precision):"+baseFolder+"debug_myAlgo.txt"+"--others:"+"newRUN_*");
		  
		  if(num_ERROR>0){
			  	System.out.println("total ERROR hit:"+num_ERROR);
		  }
		
//		if( map_ID_docID_CosineSim.get(currDocID) > avg_cosine_simil ){
		
	}
	
}
