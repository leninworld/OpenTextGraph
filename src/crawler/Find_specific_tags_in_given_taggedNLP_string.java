package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.TreeMap;


public class Find_specific_tags_in_given_taggedNLP_string {
	
	// Input file ,  that extracts specific tag such as noun, and write output
	public static void wrapper_4_find_specific_tags_in_given_taggedNLP_string(  String 		baseFolder,
																				int 		token_position_to_insert_lineNumber,
																				String 		inFile,
																				String 		outFile,
																				String      interested_NLP_tag,
																				int    		default_start_lineNo
																			  ){
		try {
			
			
	    	BufferedReader reader = null;
			String line=""; int lineNo=0;
			String tmp_NEW ="";
			if(default_start_lineNo>0)
				lineNo=default_start_lineNo;
			 
				reader=new BufferedReader(new FileReader(inFile));
				FileWriter writer=new FileWriter(new File(outFile));
				FileWriter writer_ONLY=new FileWriter(new File(outFile+"_ONLY_N.txt"));
				 
				// read each line of given file
				while ((line = reader.readLine()) != null) {
					lineNo++;
					String [] s= line.split("!!!");
					int cnt=0;
					String concLine="";
					System.out.println("lineNo:"+lineNo);
					// each token
					while(cnt < s.length){
						// interested token (from given input)
						if(cnt==token_position_to_insert_lineNumber-1){
							String tmp=s[cnt]; 
							
							

					    	//find NOUN from this taggedNLPbodyText
							  tmp_NEW =
							    	Find_specific_tags_in_given_taggedNLP_string.
							    	find_specific_tags_in_given_taggedNLP_string
							    												(tmp,
							    												 interested_NLP_tag, // intrested_tag_pattern, // __N, __V, __C
											   									 false, //is_remove_tag,
											   									 true, //is_skip_stopwords,
											   									 false //is_apply_numeric_filter
											   									 );
							System.out.println(tmp_NEW+" "+tmp_NEW.length() +"----"+ tmp);
					    	
							// 
							if(concLine.length()==0){
								concLine=tmp+"!!!"+tmp_NEW;
							}
							else{
								concLine=concLine+"!!!"+tmp+"!!!"+tmp_NEW;
							}
						}
						else{
							if(concLine.length()==0){
								concLine=s[cnt];
							}
							else{
								concLine=concLine+"!!!"+s[cnt];
							}
						}
						
						//
					
						cnt++;
					}
					
					writer.append(concLine+"\n");
					writer.flush();
					// ONLY 
					writer_ONLY.append(tmp_NEW+"\n");
					writer_ONLY.flush();
				}
				
				writer.close();
			 
	    	
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
 
	// function that extracts specific tag such as noun
	public static String find_specific_tags_in_given_taggedNLP_string(String curr_taggedNLP_string,
																	   String intrested_tag_pattern, // __N, __V, __C
																	   boolean is_remove_tag,
																	   boolean is_skip_stopwords,
																	   boolean is_apply_numeric_filter
																	   ){
		String _currLine_nouns="";String concRelatedWordsAfterNumber=""; 
		String new_token="";
		try{

		  	 String [] arr_curr_NLP_string=curr_taggedNLP_string.split(" ");
		  	 
		  	 
		  	 
		  	 //NOUN
		  	  int cnt222=0;
		  	 while(cnt222<arr_curr_NLP_string.length){
		  		 String curr_token=arr_curr_NLP_string[cnt222];
		  		 
		  		 int idx=curr_token.indexOf(intrested_tag_pattern);
		  		  
		  		 
		  		 if(idx>=0){
		  			 
		  			 // removing tag
			  		 if(is_remove_tag){
				  		 new_token=curr_token.substring(0, idx );
			  		 }
			  		 else new_token=curr_token;
			  		 
			  		 String next_one="";
			  		 String next_two="";
			  		 //
			  		 try{
			  			 
			  			 if(arr_curr_NLP_string[cnt222+1].indexOf("_")==-1)	 
			  				 next_one=arr_curr_NLP_string[cnt222+1];
			  			 else
			  				 next_one=arr_curr_NLP_string[cnt222+1].substring(0, arr_curr_NLP_string[cnt222+1].indexOf("_"));
			  		 }
			  		 catch(Exception e){
			  			next_one="";
			  		 }
			  		 //
			  		 try{
			  			 
			  			if(arr_curr_NLP_string[cnt222+2].indexOf("_")==-1)	 
			  				next_two=arr_curr_NLP_string[cnt222+2];
			  			else
			  				next_one=arr_curr_NLP_string[cnt222+2].substring(0, arr_curr_NLP_string[cnt222+2].indexOf("_"));
			  			
			  		 }
			  		 catch(Exception e){
			  			next_two="";
			  		 }
			  		 
			  		 //
			  		 if(is_skip_stopwords){
			  			 // not stopword
			  			 if(! Stopwords.is_stopword(new_token.replace(",", "").toLowerCase())){
			  				
			  				 if(is_apply_numeric_filter==false){
			  				 
				  				if(_currLine_nouns.length()==0)
					  				_currLine_nouns=new_token;
					  			 else
					  				_currLine_nouns=_currLine_nouns+" "+new_token;
			  				 }
			  				 else{ //NUMERIC filter
			  					 HashMap<Integer, Boolean> a=IsNumeric.is_numeric_2( new_token);
			  					 
			  					 // filter (1) either of first two digit numeric, and not time (am/pm)
			  					 if((a.get(1) ==true || a.get(2)==true)  && ( new_token.indexOf("am")==-1 )
			  							&& ( new_token.indexOf("pm")==-1 ) ){
			  						 
			  						 //filter - age remove example -> (25) (30) etc
			  						 if((new_token.indexOf("(")==-1 || new_token.indexOf(")")==-1)
			  								&& new_token.replace("(", "").replace(")", "").length()!=2 
			  								&&new_token.indexOf("8:30")==-1&&new_token.indexOf("9:30")==-1&&new_token.indexOf("10:30")==-1
			  								&&new_token.indexOf("12:45")==-1&&new_token.indexOf("9:00")==-1&&new_token.indexOf("11:00")==-1
			  								&&new_token.indexOf("12:30") ==-1 &&new_token.indexOf("3:17")==-1  &&new_token.indexOf("4:30")==-1
			  								&&new_token.indexOf("4:50")==-1&&new_token.indexOf("7:30")==-1&&new_token.indexOf("6:30")==-1
			  								&&new_token.indexOf("6:45")==-1&&new_token.indexOf("2:30")==-1&&new_token.indexOf("2:45")==-1
			  								&&new_token.indexOf("5:45")==-1&&new_token.indexOf("5:20")==-1&&new_token.indexOf("3:25")==-1
			  								&&new_token.indexOf("4:00")==-1&&new_token.indexOf("9:47")==-1&&new_token.indexOf("11:45")==-1
			  								&&new_token.indexOf("1:15")==-1 && new_token.indexOf("8:15")==-1&&new_token.indexOf("8:45")==-1
			  								&&new_token.indexOf("5:15")==-1&&new_token.indexOf("8:40")==-1&&new_token.indexOf("1:45")==-1
			  								&&new_token.indexOf("2:00")==-1 &&new_token.indexOf("9:15")==-1&&new_token.indexOf("12:55")==-1
			  								&&new_token.indexOf("12:01")==-1&&new_token.indexOf("3:40")==-1&&new_token.indexOf("2:15")==-1
			  								 ){

						  					if(_currLine_nouns.length()==0)
								  				_currLine_nouns=new_token;
								  			 else
								  				_currLine_nouns=_currLine_nouns+" "+new_token;
						  					
						  					
						  					//relatd words after number
						  					if(next_one.indexOf("die")>=0 || next_two.indexOf("die")>=0 ||
						  					   next_one.indexOf("deat")>=0 || next_two.indexOf("deat")>=0 ||
						  					   next_one.indexOf("injur")>=0 || next_two.indexOf("injur")>=0 ||
						  					   next_one.indexOf("kill")>=0 || next_two.indexOf("kill")>=0 ||
						  					   next_one.indexOf("murde")>=0 || next_two.indexOf("murder")>=0 ||
						  					   next_one.indexOf("lost")>=0 || next_two.indexOf("lost")>=0
						  							){
						  						if(concRelatedWordsAfterNumber.length()==0)
						  							concRelatedWordsAfterNumber=new_token+" "+next_one+" "+next_two;
						  						else
						  							concRelatedWordsAfterNumber=concRelatedWordsAfterNumber+"@@@"+ new_token+" "+next_one+" "+next_two;
						  						
						  					}
						  					
			  							 
			  						 }
			  						 
			  					 
			  					 }
			  					
			  				 }			  				
			  			 }
			  			 
			  		 }
			  		 else{
			  			 if(_currLine_nouns.length()==0)
			  				_currLine_nouns=new_token;
			  			 else
			  				_currLine_nouns=_currLine_nouns+" "+new_token;
			  		 }
		  		 }

		  		 cnt222++;
		  	 }
 			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return _currLine_nouns+"!!!"+concRelatedWordsAfterNumber;
	}
	

	// main
	public static void main(String[] args) {
		
		
		
	}
	
	
}
