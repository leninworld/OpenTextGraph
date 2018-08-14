package crawler;

import java.util.*;
 
import crawler.StanfordNER;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
//import p6_new.P6_prepare_Input_feature_file_from_raw_data_p6;
import p8.MyAlgo;

import java.lang.*;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
public   class Find_Verb_AND_Noun_JustBfr_from_TaggedString_inFile
{
	//find_VERB_AND_Noun_from_TaggedString_function
	//mapOut.get(1) -> curr sentence noun / mapOut.get(2)-> curr sentence noun-verb pair. Picks Verb and Noun Just before that.  
	//mapOut.get(3).get(lineNumber) -> curr sentence dependency tree
	//mapOut.put(4, Map<lineNumber,<noun_2_CSV> );  <- contains map_unique_noun_2 
	//mapOut.get(5, Map<lineNumber,<Numbers>>)  <- contains Numbers from current sentence
	//mapOut.put(6, Map<1111,<person_CSV> );  <- contains person in CSV ; 1111 is the lineNumber by default.
	//mapOut.put(7, Map<2222,<organization_CSV> );  <- contains organization in CSV ; 2222 is the lineNumber by default.
	//mapOut.put(8, Map<3333,<location_CSV );  <- contains organization in CSV ; 3333 is the lineNumber by default.
	public static TreeMap<Integer,TreeMap<Integer, String>> find_VERB_AND_Noun_JustBfr_from_TaggedString_function(
																							    String 		  baseFolder,
																								String 	  	  inText_TaggedNLP,//can be CSV (or) String
																								String        inText_untagged,// can be blank
																								int  		  token_for_inText_containing_TaggedNLP,//required if inText_TaggedNLP is CSV 
																								boolean 	  is_inText_TaggedNLP_CSV,//boolean if inText_TaggedNLP=CSV
																								boolean 	  isSOPprint, 
																								int     	  lineNumber,  //primary key for output map of verb/noun
																								ParserModel   inflag_model2_chunking,
																								CRFClassifier<CoreLabel>  NER_classifier,
																								FileWriter  writer_debug,
																								String    	  debug_File
																								){
		TreeMap<Integer,TreeMap<Integer, String>> mapOut=new TreeMap<Integer,TreeMap<Integer, String>>();
		String currLine=inText_TaggedNLP.toLowerCase();
		int cnt=0; String currBodyText_taggedNLP="";
		String t1="",t2="",t3="";
		Stemmer stemmer=new Stemmer();
		 
		try{
			//appending
//			FileWriter  writer_debug=new FileWriter(new File(debug_File) , true);
			
			//pick verb, and noun that just occurs before it.
            TreeMap<Integer, String> map_unique_verb=new TreeMap<Integer, String>();
            TreeMap<Integer, String> map_unique_noun=new TreeMap<Integer, String>();

			//pick verb, and noun that just occurs after it.
            TreeMap<Integer, String> map_unique_verb_2=new TreeMap<Integer, String>();
            TreeMap<Integer, String> map_unique_noun_2=new TreeMap<Integer, String>();

            currLine=currLine.toLowerCase().replace("head:", "")
            				 .replace("bodytext.feature.tags:", "");
            currLine=currLine.replace(".", " ").replace("  ", " ").replace("  ", " ");
            String[] arr_line= currLine.split("!!!");
            //take token from CSV 
            if(is_inText_TaggedNLP_CSV)
            	currBodyText_taggedNLP=arr_line[token_for_inText_containing_TaggedNLP-1];
            else
            	currBodyText_taggedNLP=inText_TaggedNLP;
            
            if(isSOPprint)
            	System.out.println("TOKEN->"+currBodyText_taggedNLP);
            
            String [] arrBodyText=currBodyText_taggedNLP.toLowerCase().split(" ");
            int len_arrBodyText=arrBodyText.length;
            cnt=0;
            if(isSOPprint)
            	System.out.println("----------------line no="+lineNumber+" len_arrBodyText:"+len_arrBodyText);
            String concNoun="";
            String concNoun_2="";
            String concNumbers="";
            String word_only="";
            //
            while(cnt<len_arrBodyText){
            	concNoun="";concNoun_2=""; //RESET
            	if(arrBodyText[cnt].indexOf("_")>=0)
            		word_only=arrBodyText[cnt].substring(0 , arrBodyText[cnt].indexOf("_"));
            	else{
            		word_only=arrBodyText[cnt];
            	}
            	word_only=word_only.replace("_", "");
            	if(isSOPprint)
            		System.out.println("???? WORD ONLY HAS VALUE:"+word_only);
                if( word_only.toLowerCase().length()>0 ) {
                	if(isSOPprint)
                		System.out.println(" WORD ONLY HAS VALUE");
                    //verb
                    if( arrBodyText[cnt].indexOf("_v") >=0){
                    	
                    	if(map_unique_verb.containsKey(lineNumber)){
                    		String t="";
                    		
                    		if(arrBodyText[cnt].length()>0){
                    			  t=map_unique_verb.get(lineNumber)+"!!!"+ stemmer.stem(arrBodyText[cnt]);
                    		}
                    		else{
                    			  t=map_unique_verb.get(lineNumber)+"!!!"+ " "; //dummy # so that split on !!! is good
                    		}
 
                    		map_unique_verb.put(lineNumber, t);
                    		
                    	}
                    	else{
                    		map_unique_verb.put(lineNumber, stemmer.stem(arrBodyText[cnt]));
                    	}
                       
                         if(cnt>=2 ){
                        	 
                        	 //System.out.println("cnt >2 ENTRY");
                        	 
                             //BEGIN pick noun before verb
                             if(arrBodyText[cnt-1].indexOf("_n") >=0 ){
                                 concNoun=arrBodyText[cnt-1];
                             }
                             if(arrBodyText[cnt-2].indexOf("_n") >=0 ){
                                 if(concNoun.length()==0)
                                     concNoun=arrBodyText[cnt-2];
                                 else
                                     concNoun=concNoun+" "+arrBodyText[cnt-2];
                             }
                             //END pick noun before verb
                             
                             //BEGIN pick noun after verb
                             if((cnt+1) < arrBodyText.length -1 ){
                            	 if(arrBodyText[cnt+1].indexOf("_n") >=0 ){
                                	 //System.out.println("&&&& ENTER just after.1:" );
                                	 //System.out.println("&&&& just after" );
                            		 if(concNoun_2.length()==0)
                            			 concNoun_2=arrBodyText[cnt+1];
                                     else
                                    	 concNoun_2=concNoun_2+" "+arrBodyText[cnt+1]; 
                            	 }
                             }
                             else{
                            	 //System.out.println("&&&& NO ENTER just after.1:" +(cnt+1) +"<->" + (arrBodyText.length -1) );
                             }
                             
                             if((cnt+2) < arrBodyText.length -1 ){
                            	 //System.out.println("&&&& ENTER just after.2:" );
                            	 if(arrBodyText[cnt+2].indexOf("_n") >=0 ){
                                	 //System.out.println("&&&& just after.2:" );
                            		 if(concNoun_2.length()==0)
                            			 concNoun_2=arrBodyText[cnt+2];
                                     else
                                    	 concNoun_2=concNoun_2+" "+arrBodyText[cnt+2];
                            	 }
                             }
                             else{
                            	 //System.out.println("&&&& NO ENTER just after.1:" +(cnt+3) +"<->" + (arrBodyText.length -1) );
                             }
                             
                             if((cnt+3) < arrBodyText.length -1 ){
                            	 //System.out.println("&&&& ENTER just after.3:" );
                            	 if(arrBodyText[cnt+3].indexOf("_n") >=0 ){
                                	 //System.out.println("&&&& just after.3:" );
                            		 if(concNoun_2.length()==0)
                            			 concNoun_2=arrBodyText[cnt+3];
                                     else
                                    	 concNoun_2=concNoun_2+" "+arrBodyText[cnt+3];
                            		 
                            	 }
                            	 else{
                                	 //System.out.println("&&&& NO ENTER just after.1:" +(cnt+3) +"<->" + (arrBodyText.length -1) );
                                 }
                            	 
                             }
//                             if((cnt+4) < arrBodyText.length -1 ){
//                            	 if(arrBodyText[cnt+4].indexOf("_n") >=0 ){
//                            		 if(concNoun_2.length()==0)
//                            			 concNoun_2=arrBodyText[cnt+4];
//                                     else
//                                    	 concNoun_2=concNoun_2+"!!!"+arrBodyText[cnt+4];
//                            		 
//                            	 }
//                             }
                             
                             
                             //END pick noun after verb
                             
                         }
                         else if(cnt>3 ){
                        	 //System.out.println("cnt >3 ENTRY");
                        	 
                             //BEGIN pick noun before verb
                             if(arrBodyText[cnt-1].indexOf("_n") >=0 ){
                                 concNoun=arrBodyText[cnt-1];
                             }
                             if(arrBodyText[cnt-2].indexOf("_n") >=0 ){
                                 if(concNoun.length()==0)
                                     concNoun=arrBodyText[cnt-2];
                                 else
                                     concNoun=concNoun+" "+arrBodyText[cnt-2];
                             }
                             
                             if((cnt-3) < arrBodyText.length -1 ){
	                             if(arrBodyText[cnt-3].indexOf("_n") >=0 ){
	                                 if(concNoun.length()==0){
	                                     concNoun=arrBodyText[cnt-3];
	                                 }
	                                 else{
	                                     concNoun=concNoun+" "+arrBodyText[cnt-3];
	                                 }
	                             }
                             }
                             //END pick noun before verb
                             
                             //BEGIN pick noun after verb
                             if((cnt+1) < arrBodyText.length -1 ){
                            	 if(arrBodyText[cnt+1].indexOf("_n") >=0 ){
                                	 //System.out.println("&&&& ENTER just after.1:" );
                                	 //System.out.println("&&&& just after" );
                            		 if(concNoun_2.length()==0)
                            			 concNoun_2=arrBodyText[cnt+1];
                                     else
                                    	 concNoun_2=concNoun_2+" "+arrBodyText[cnt+1]; 
                            	 }
                             }
                             else{
                            	 //System.out.println("&&&& NO ENTER just after.1:" +(cnt+1) +"<->" + (arrBodyText.length -1) );
                             }
                             
                             if((cnt+2) < arrBodyText.length -1 ){
                            	 //System.out.println("&&&& ENTER just after.2:" );
                            	 if(arrBodyText[cnt+2].indexOf("_n") >=0 ){
                                	 //System.out.println("&&&& just after.2:" );
                            		 if(concNoun_2.length()==0)
                            			 concNoun_2=arrBodyText[cnt+2];
                                     else
                                    	 concNoun_2=concNoun_2+" "+arrBodyText[cnt+2];
                            	 }
                             }
                             else{
                            	 //System.out.println("&&&& NO ENTER just after.1:" +(cnt+3) +"<->" + (arrBodyText.length -1) );
                             }
                             
                             if((cnt+3) < arrBodyText.length -1 ){
                            	 //System.out.println("&&&& ENTER just after.3:" );
                            	 if(arrBodyText[cnt+3].indexOf("_n") >=0 ){
                                	 //System.out.println("&&&& just after.3:" );
                            		 if(concNoun_2.length()==0)
                            			 concNoun_2=arrBodyText[cnt+3];
                                     else
                                    	 concNoun_2=concNoun_2+" "+arrBodyText[cnt+3];
                            		 
                            	 }
                            	 else{
                                	 //System.out.println("&&&& NO ENTER just after.1:" +(cnt+3) +"<->" + (arrBodyText.length -1) );
                                 }
                            	 
                             }
//                             if((cnt+4) < arrBodyText.length -1 ){
//                            	 if(arrBodyText[cnt+4].indexOf("_n") >=0 ){
//                            		 if(concNoun_2.length()==0)
//                            			 concNoun_2=arrBodyText[cnt+4];
//                                     else
//                                    	 concNoun_2=concNoun_2+"!!!"+arrBodyText[cnt+4];
//                            		 
//                            	 }
//                             }
                             
                             
                             //END pick noun after verb
                             
                         } 
                         
	                         //NOUN just before verb
	                     	if(map_unique_noun.containsKey(lineNumber)){
	                     		String t="";
	                     		if(concNoun.length()>0)
	                     			  t=map_unique_noun.get(lineNumber)+"!!!"+concNoun;
	                     		else
	                     			  t=map_unique_noun.get(lineNumber)+"!!!"+" "; //add dummy " " so that split on # not an issue
	                    		
	                    		map_unique_noun.put(lineNumber, t);
	                    	}
	                    	else{
	                    		map_unique_noun.put(lineNumber, concNoun);
	                    	}
	                     	
	                     	 //NOUN that comes just after VERB
	                     	if(map_unique_noun_2.containsKey(lineNumber)){
	                     		String t="";
	                     		if(concNoun_2.length()>0)
	                    			 t=map_unique_noun_2.get(lineNumber)+"!!!"+concNoun_2;
	                     		else
	                     			 t=map_unique_noun_2.get(lineNumber)+"!!!"+" ";
	                    		map_unique_noun_2.put(lineNumber, t);
	                    	}
	                    	else{
	                    		map_unique_noun_2.put(lineNumber, concNoun_2);
	                    	}
                         
                    } //END if( arrBodyText[cnt].indexOf("_v") >=0){
                } //END if( word_only.toLowerCase().length()>0 ) { 
         
                if(isSOPprint==true)
                	System.out.println("1 found verb:"+arrBodyText[cnt].indexOf("_v"));
                if(cnt>3 && isSOPprint==true){
                	System.out.println("word_only:"+word_only+" orig:"+arrBodyText[cnt]
                						+" found verb:"+arrBodyText[cnt].indexOf("_v")
                						+" <-> "+arrBodyText[cnt-1]+" <-> "+arrBodyText[cnt-2]
                						+" <-> "+arrBodyText[cnt-3]);
                }
                
                
                if(arrBodyText[cnt].toLowerCase().indexOf("_cd")>=0){
                	//
                	if(concNumbers.length()==0){
                		t1=t1.replace("_", "_$_number _");
                		t2=t2.replace("_", "_$_number _");
                		//
                		if(cnt+1 < arrBodyText.length-1 ){
                			
                			if(Stopwords.is_stopword(arrBodyText[cnt].substring(0, arrBodyText[cnt].indexOf("_"))  )) t1=""; else t1=arrBodyText[cnt];
            				if(Stopwords.is_stopword(arrBodyText[cnt+1].substring(0, arrBodyText[cnt+1].indexOf("_")) )) t2="";else t2=arrBodyText[cnt+1];
                			
                			concNumbers=t1+"@"+t2;
                		}
                		else{
                			if(Stopwords.is_stopword(arrBodyText[cnt].substring(0, arrBodyText[cnt].indexOf("_"))  )) t1=""; else t1=arrBodyText[cnt];
                			concNumbers=t1;
                		}
                	}
                	else{
                		
                		//
                		if(arrBodyText[cnt].length()>0){
                			if(cnt+2 < arrBodyText.length-1 ){
                				// 
                				if(arrBodyText[cnt].indexOf("_")>=0)
                					if(Stopwords.is_stopword(arrBodyText[cnt].substring(0, arrBodyText[cnt].indexOf("_"))  )) t1=""; else t1=arrBodyText[cnt];
                				else
                					if(Stopwords.is_stopword(arrBodyText[cnt] )) t1=""; else t1=arrBodyText[cnt];
                				
                				if(arrBodyText[cnt+1].indexOf("_")>=0)
                					if(Stopwords.is_stopword(arrBodyText[cnt+1].substring(0, arrBodyText[cnt+1].indexOf("_")) )) t2="";else t2=arrBodyText[cnt+1];
                				else 
                					if(Stopwords.is_stopword(arrBodyText[cnt+1] )) t2="";else t2=arrBodyText[cnt+1];
                				
                				if(arrBodyText[cnt+2].indexOf("_")>=0)
                					if(Stopwords.is_stopword(arrBodyText[cnt+2].substring(0, arrBodyText[cnt+2].indexOf("_"))  )) t3="";else t3=arrBodyText[cnt+2];
                				else
                					if(Stopwords.is_stopword(arrBodyText[cnt+2] )) t3="";else t3=arrBodyText[cnt+2];
                				
                				t1=t1.replace("_", "_$_number _");
                        		t2=t2.replace("_", "_$_number _");
                        		t3=t3.replace("_", "_$_number _");
                				concNumbers=concNumbers+"!!!"+t1+"@"+t2+"@"+t3;
                				
                			}
                			if(cnt+1 < arrBodyText.length-1 ){
                				if(arrBodyText[cnt].indexOf("_")>=0)
                					if(Stopwords.is_stopword(arrBodyText[cnt].substring(0, arrBodyText[cnt].indexOf("_"))  )) t1=""; else t1=arrBodyText[cnt];
                				else
                					if(Stopwords.is_stopword(arrBodyText[cnt] )) t1=""; else t1=arrBodyText[cnt];
                				
                				if(arrBodyText[cnt+1].indexOf("_")>=0)
                					if(Stopwords.is_stopword(arrBodyText[cnt+1].substring(0, arrBodyText[cnt+1].indexOf("_")) )) t2="";else t2=arrBodyText[cnt+1];
                				else 
                					if(Stopwords.is_stopword(arrBodyText[cnt+1] )) t2="";else t2=arrBodyText[cnt+1];
                				
                				t1=t1.replace("_", "_$_number _");
                        		t2=t2.replace("_", "_$_number _");
                				concNumbers=concNumbers+"!!!"+t1+"@"+t2;
                			}
                			else{
                				
                				if(arrBodyText.length >= (cnt+1) ){
                				
	                				if(arrBodyText[cnt+1]!=null){
	                				
		                				if(arrBodyText[cnt+1].indexOf("_")>=0)
		                					 if(Stopwords.is_stopword(arrBodyText[cnt+1].substring(0, arrBodyText[cnt+1].indexOf("_")) )) t2="";else t2=arrBodyText[cnt+1];
		                				else
		                					if(Stopwords.is_stopword(arrBodyText[cnt+1] )) t2="";else t2=arrBodyText[cnt+1];
		                				
		                				 
		                        		t2=t2.replace("_", "_$_number_");
		                				concNumbers=concNumbers+"!!!"+t2;
	                				}
                				
                				}
                			}
                			
                		}
                		else{
                			concNumbers=concNumbers+"!!!"+" ";
                		}
                	}
                
                	
                	writer_debug.append("\n concNumbers:"+concNumbers);
                	writer_debug.flush();
                }
                cnt++;
            } // END while(cnt<len_arrBodyText){
            TreeMap<Integer, String > mapTemp=new TreeMap<Integer, String>();
          //number
            mapTemp.put(lineNumber, concNumbers.replace("!!!", "_$_number!!!"));
            mapOut.put(5, mapTemp);
            
            TreeMap<Integer, String> temp=new TreeMap<Integer, String>();
            Parser parser =null;
            // inText_untagged (inflag_model2_chunking to be given)
            if(inText_untagged.length()>0 && inflag_model2_chunking!= null ){
            	
				// chunker to create dependency tree of sentence(input)
				parser = ParserFactory.create(inflag_model2_chunking);
				Parse topParses[] = ParserTool.parseLine(inText_untagged, parser, 1);
				
				StringBuffer  sb2 = null;
				// parse dependency tree to String 
			    for (opennlp.tools.parser.Parse p : topParses){
			        sb2 = new StringBuffer(inText_untagged.length() * 4);
			      
			        p.show(sb2);
			        //sb now contains all the tags
			        if(isSOPprint)
			        	System.out.println("stringbuilder:" +sb2);
 
					//System.out.println("sb2.toString()----->"+sb2.toString());
					
				}
			    
			    //adding dependency tree.
			    temp.put( lineNumber ,  sb2.toString());
			    mapOut.put(3 , temp );
			     
			} // END for (opennlp.tools.parser.Parse p : topParses){
            /**
            *  Output: TreeMap<Integer,TreeMap<Integer,String>> --> <category_id,<seq_id,value>>
            * 			<category_id=map_value>->{1=map_person, 2=map_organization, 3=map_location, 11=map_person_begin,22=map_organization_begin, 33=map_location_begin,
            * 			, 111=map_person_end,222=map_organization_end, 333=map_location_end, , 1111=map_person_CSV,2222=map_organization_CSV, 3333=map_location_CSV } 
            */
            TreeMap<Integer, TreeMap<Integer, String>> map = StanfordNER.identifyNER( inText_untagged,
																						"",
																					    NER_classifier,
																						true, // is_override_with_model2
																						false, //isSOPpring
																						debug_File,
																						true //is_write_to_debug
																						);
            
            
            
            
			mapOut.put(1, map_unique_noun);
			mapOut.put(2, map_unique_verb);
			// 3 is for DEPENDENCY TREE
			// NOUN that comes just after verb 
			mapOut.put(4, map_unique_noun_2);
			// 5 is for numbers
			mapOut.put(6, map.get(1111)); // person_csv
			mapOut.put(7, map.get(2222) );//organization_csv
			mapOut.put(8, map.get(3333) ); //location_csv
			
			// appending n2, person, organization, location to map.key("1") to a map that already has "noun"
			int size_of_noun=mapOut.get(1).size();
			// noun_2 append to map
			for( int s:map_unique_noun_2.keySet()){
				size_of_noun++;
				map_unique_noun.put(size_of_noun,  map_unique_noun_2.get(s));
			}
			//person
			if( map.containsKey(1111)) {
				for( int s:map.get(1111).keySet()){
					size_of_noun++;
					map_unique_noun.put(size_of_noun,  map.get(1111).get(s));
				}
			}
			//organization
			if( map.containsKey(2222)) {
				for( int s:map.get(2222).keySet()){
					size_of_noun++;
					map_unique_noun.put(size_of_noun,  map.get(2222).get(s));
				}
			}
			//location
			if( map.containsKey(3333)) {
				for( int s:map.get(3333).keySet()){
					size_of_noun++;
					map_unique_noun.put(size_of_noun,  map.get(3333).get(s));
				}
			}
			if( mapOut.containsKey(5)) {
				//numbers 
				for( int s:mapOut.get(5).keySet()){
					size_of_noun++;
					map_unique_noun.put(size_of_noun,  mapOut.get(5).get(s));
				}
			}
			
			// append 
			 
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	
	//get_organization_person
	public static void get_organization_person_location_numbers(
												String baseFolder,
												String 	inFile, 
												int 	token_containing_body_text,
												int 	token_containing_body_text_WITH_NLP,
												int		token_containing_primary_key,
												String 	output_organization_person_count,
												int     skip_top_N_lines
												) {
		Stemmer stemmer=new Stemmer();
		TreeMap<Integer, String> map_docID_bodyText =new TreeMap<Integer, String>();
		TreeMap<Integer, String> map_docID_primaryKEY =new TreeMap<Integer, String>();
		TreeMap<Integer, String> map_docID_bodyText_with_NLP =new TreeMap<Integer, String>();
		FileWriter writer=null;
		// TODO Auto-generated method stub
		try {
			FileWriter writerDebug=new FileWriter(new File(baseFolder+"debug_organiz_person.txt"));
			
			if(skip_top_N_lines<=0)
				writer=new FileWriter(output_organization_person_count);
			else
				writer=new FileWriter(output_organization_person_count,true );
			
			//
			TreeMap<Integer, String> map_eachLine_all_tokens =
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
					readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
												inFile,
												-1, 
												-1, 
												 "load bodytext",//debug_label,
												 false // isPrintSOP
												);
			
			for(int seq_ : map_eachLine_all_tokens.keySet()){
				
				String eachLine=map_eachLine_all_tokens.get(seq_);
				
				String []s=eachLine.split("!!!");
				
				try{
					String eachLine_primarykey=s[token_containing_primary_key-1];
					String bodyText_noNLP=s[token_containing_body_text-1];
					String bodyText_with_NLP=s[token_containing_body_text_WITH_NLP-1];	
					
					System.out.println("s.len:"+s.length+ " token_containing_body_text:"+token_containing_body_text); 
//										+ " eachLine:"+eachLine);
					
					map_docID_bodyText.put(seq_, bodyText_noNLP);
					map_docID_bodyText_with_NLP.put(seq_, bodyText_with_NLP);
					map_docID_primaryKEY.put(seq_, eachLine_primarykey);
				}
				catch(Exception e){
					map_docID_bodyText.put(seq_, "");
					writerDebug.append( "mismatch tokens count: lineNo:"+seq_ +" eachLine:"+eachLine+"\n");
					writerDebug.flush();
				}
				
			}
			TreeMap<String, String> map_Word_StemmedWord=new TreeMap<String, String>();
			//
			String serializedClassifier ="/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.conll.4class.distsim.crf.ser.gz";
			CRFClassifier<CoreLabel>  classifier=CRFClassifier.getClassifierNoExceptions(serializedClassifier);
			
			TreeMap<Integer,TreeMap<Integer,String>>  map=new TreeMap<Integer, TreeMap<Integer,String>>();
			
			int max_size_docID=map_eachLine_all_tokens.size();
			int seqID=1;
			
			writer.append("@@@@person@@@@organization@@@@location@@@@numbers@@@@docID@@@@primarykey\n");
			writer.flush();
			
			//
			while(seqID<=max_size_docID){
				System.out.println("running docId="+seqID +" out of total="+max_size_docID);
				String curr_bodyText=map_docID_bodyText.get(seqID);
				String curr_bodyText_with_NLP=map_docID_bodyText_with_NLP.get(seqID);
				
				String currLine="";
				
				if(skip_top_N_lines >0 && skip_top_N_lines <= seqID ){
					continue;
				}
				
				System.out.println("243 lineNo:"+seqID);
									//+" curr_bodyText:"+curr_bodyText+" curr_bodyText_with_NLP:"+curr_bodyText_with_NLP);
				
				//GET location, person, and organization..
				 map = StanfordNER.identifyNER( curr_bodyText,
												"/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.conll.4class.distsim.crf.ser.gz",
												classifier,
												true, // is_override_with_model2
												false, //isSOPprint
												baseFolder+"debug.txt",
												false //is_write_to_debugFile
												);
				 
				 if(map.containsKey(111 )){ //person
					 currLine=currLine+"@@@@"+map.get(1);
				 }
				 else
					 currLine=currLine+"@@@@"+"d";
				 if(map.containsKey(222)){//organi
					 currLine=currLine+"@@@@"+map.get(2);
				 }
				 else
					 currLine=currLine+"@@@@"+"d";
				 if(map.containsKey(333)){//locat
					 currLine=currLine+"@@@@"+map.get(3);
				 }
				 else
					 currLine=currLine+"@@@@"+"d";
				  
				 
				 
				 //					 
				 TreeMap<Integer,String> map_IS_numbers= MyAlgo.find_count_number_words_with_specific_tag(  
						 																			curr_bodyText_with_NLP,
						 																			stemmer,
						 																			map_Word_StemmedWord,
						 																			"_C"); // 
				 //number
				 if(map_IS_numbers.size()>0){
					 currLine=currLine+"@@@@"+map_IS_numbers;
				 }
				 else
					 currLine=currLine+"@@@@"+"d";
				 
				 //WRITING
				 writer.append(currLine + "@@@@"+ seqID +"@@@@"+map_docID_primaryKEY.get(seqID)+  "\n");
				 writer.flush();
			
				 // "@@@@"+ curr_bodyText_with_NLP+
				 
				 seqID++;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}
	
    // find_Verb_AND_Noun_from_TaggedString_inFile
	////give either input_1 or input_2 (textNLPtagged). when inpu_2, output lineNumber(primary key) of mapNoun/mapVerb will be "99".
    public static TreeMap<Integer,TreeMap<Integer, String>> find_VERB_AND_Noun_JustBfr_from_TaggedString_inFile
			                                             ( 
			                                              String 	  baseFolder,
			                                              String  	  inFile_input_1 // <---input_1
			                                             ,String  	  intext_NLP_tagged_input_2 //give either input_1 or input_2 (textNLPtagged)// <---input_2
			                                             ,String  	  intext_NLP_untagged_input_2// can be blank
			                                             ,String  	  				outFile
			                                             ,int 	  	  				index_of_token_for_bodytext_taggedNLP_inFile
			                                             ,boolean 	  				isSOPprint
			                                             ,ParserModel 				inflag_model2_chunking
			                                             ,CRFClassifier<CoreLabel>  NER_classifier
			                                             ,int 					    run_top_N_lines_only
			                                             ,String 					debug_File
			                                             ){
       
        TreeMap<String, String> map_eachLine_=new TreeMap<String,String>();
        BufferedReader reader=null;
        FileWriter writer =null;int lineNumber=0;
        //TreeMap<Integer, String> map_unique_verb=new TreeMap<Integer, String>();
        //TreeMap<Integer, String> map_unique_noun=new TreeMap<Integer, String>();
        String currLine="";
        TreeMap<Integer,TreeMap<Integer, String>> mapOut=new TreeMap<Integer, TreeMap<Integer,String>>();
        try{
        	FileWriter writer_debug=new FileWriter(new File(baseFolder+"debug_find_verb.txt"));
        	
        	// input File is given -- input_1
        	if(inFile_input_1.length()>0){
	            reader=new BufferedReader(new FileReader(inFile_input_1));
	            writer = new FileWriter(new File(outFile));
	            //
	            map_eachLine_=
	            ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
	            readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
												                        (inFile_input_1,
												                        -1,
												                        -1,
												                		"", //outFile,
																		false, //is_Append_outFile,
																		false, //is_Write_To_OutputFile,
																		"labl",// debug_label,
																		-1, // token to be used as primarykey
																		false //isSOPprint 
												                        );
	            
	            String currBodyText_taggedNLP=""; int cnt=0;
	            String concTitle=""; 
	            TreeMap<String, String> mapStringAlreadyWrote=new TreeMap<String, String>();
	            
	            TreeMap<Integer, String> map_unique_verb=new TreeMap<Integer, String>();
	            TreeMap<Integer, String> map_unique_noun=new TreeMap<Integer, String>();
	            lineNumber=0;
//				FileWriter  writer_debug=new FileWriter(new File(debug_File) , true);
	            //each line (has a token containing taggedNLP
	            while((currLine=reader.readLine())!=null){
	            	lineNumber++;
	            	
	            	if(lineNumber>run_top_N_lines_only &&  run_top_N_lines_only >0) 
	            			{break;}
	            	
	            	String [] arr_currLine=currLine.split("!!!");
	            	
	            	if(arr_currLine.length < index_of_token_for_bodytext_taggedNLP_inFile){
	            		writer_debug.append(" error: not necessary tokens available; "+lineNumber+" arr_currLine.size:"+arr_currLine.length 
	            								+" token_for_bodytext_taggedNLP_inFile:"+index_of_token_for_bodytext_taggedNLP_inFile +"\n");
	            		writer_debug.flush();
	            		continue;
	            	}
	            	
	            	System.out.println("------------lineNumber: "+lineNumber+" arr_currLine.size:"+arr_currLine.length
	            									+" token_for_bodytext_taggedNLP_inFile:"+index_of_token_for_bodytext_taggedNLP_inFile);
	            	
	            	String curr_taggedNLP_string=arr_currLine[index_of_token_for_bodytext_taggedNLP_inFile -1];
	            	//////////////////
	            	mapOut=find_VERB_AND_Noun_JustBfr_from_TaggedString_function(
				            													  baseFolder,
				            													  curr_taggedNLP_string,// inText_TaggedNLP,
				            													  "", // inText_untagged
				            													  -1, // token_for_bodytext_taggedNLP_inFile
				            													  false, // is_inText_TaggedNLP_CSV,
				            													  isSOPprint, //isSOPprint
				            													  lineNumber, //lineNumber
				            													  inflag_model2_chunking,
				            													  NER_classifier,
				            													  writer_debug,
				            													  debug_File
	            													  		    );
	            	//
	            	map_unique_noun=mapOut.get(1);
	            	map_unique_verb=mapOut.get(2);
	            	
	            	System.out.println("------------lineNumber: "+lineNumber+" arr_currLine.size:"+arr_currLine.length
										+" token_for_bodytext_taggedNLP_inFile:"+index_of_token_for_bodytext_taggedNLP_inFile
//										+" map_unique_noun:"+map_unique_noun
//										+" map_unique_verb:"+map_unique_verb
//										+"curr_taggedNLP_string:"+curr_taggedNLP_string
										);
	            	
			                //
	//		                if(cnt>3 && isSOPprint==true){
	            	
	            	if(map_unique_verb!=null && map_unique_noun!=null ){
	            	
		            	if(map_unique_verb.containsKey(lineNumber ) && map_unique_noun.containsKey(lineNumber)){
//				                	System.out.println( "lineNo: "+lineNumber
//				                						+" <-> map_unique_verb="+map_unique_verb.get(lineNumber)
//				                						+" <-> map_unique_noun="+map_unique_noun.get(lineNumber) );
		            	}
		            	
	            	}
	            ////////////////
	            if(map_unique_verb !=null || map_unique_noun!=null ){
		               writer.append(map_unique_verb.get(lineNumber)+"!#!#!"+map_unique_noun.get(lineNumber)+"!#!#"+lineNumber+"\n");
		               writer.flush();
	            }
	            else{
		               writer.append("!#!#"+"!#!#"+lineNumber+"\n");
			           writer.flush();
	            }
	      
	                
	           //     mapStringAlreadyWrote.put(map_unique_verb+"!!!"+map_unique_noun, "");
	                
	            } //end each line currLine
	            
        	} //END if(inFile_input_1.length()>0){
           
        	// input_2
        	if(intext_NLP_tagged_input_2.length()>0){
        		//find_VERB_AND_Noun_JustBfr_from_TaggedString_function
        		mapOut=find_VERB_AND_Noun_JustBfr_from_TaggedString_function(
        															  baseFolder,
        															  intext_NLP_tagged_input_2,// inText_TaggedNLP,
        													  		  intext_NLP_untagged_input_2,
	            													  -1, // token_for_bodytext_taggedNLP_inFile
	            													  false, // is_inText_TaggedNLP_CSV,
	            													  true, //isSOPprint
	            													  99, // lineNumber (standardize) when input is map
	            													  inflag_model2_chunking,
	            													  NER_classifier,
	            													  writer_debug,
	            													  debug_File
	            													  );
        		
        		
        	} // END if(intextNLPtagged_input_2.length()>0){
        	
        }
        catch(Exception e){
        	System.out.println("!!! error ");
            e.printStackTrace();
        }
       return mapOut;
    }
    // main
    public static void main (String[] args) throws java.lang.Exception
    {
    	 
		long t0 = System.nanoTime();
    	
    	// STEP 1 : 
		TreeMap<String,String>  mapConfig=new TreeMap<String, String>();
		String	 confFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/p.2.extract_features_from_Each_doc/trad.config.txt";
		
//		File file_confFile=new File(confFile);
//		FileWriter writer=new FileWriter(file_confFile);
//		//create this file 
//		if(!file_confFile.exists()){
//			file_confFile.createNewFile();
//		}
		
		mapConfig=Crawler.getConfig(confFile);
		 
		TreeMap<String, String> maptaggedNLP=new TreeMap<String, String>(); 
		String  input_ExtractedText_OR_input2="";
		String  inFile100=mapConfig.get("inFile");
		String  baseFolder=mapConfig.get("baseFolder");
        		inFile100=baseFolder+inFile100; // INPUT
        		
        String inFile_alreadyRan_pastTagging=""; // INPUT 		
        
        	/**  BELOW are the p6 configuration trad.config.txt
				NLPfolder=/Users/lenin/OneDrive/jar/NLP/models/
				isSOPdebug=false
				baseFolder=/Users/lenin/Dropbox/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds10/
				baseFolder.remark= For both input and output file. also debug files
				inFile=20000.txt
				inFile.remarks=in java, Takes baseFolder+inFile name. 
				outFile=20000.txt_bTextNLPtagged.txt
        		debugFileName=debug.trad.tagging.txt
        		token_containing_body_text=8
        		token_containing_title=6
        		token_containing_auth_Name=4
        		token_containing_sourceURL=2
        		token_containing_keywords=5
        		token_containing_date=3
        		start_pattern_containing_author_name=
        		start_pattern_containing_author_name.remark=from:
        		is_Append_outFile=true
        		skip_top_N_lines=-1
        		skip_top_N_lines.remarks=skip top N lines
        		Flag_2=dummy
        		Flag_2.remarks={"all","P,P,O,L,D,T","dummy"} <-Tagging for set of features such as person, organization, data, time etc. this flag nothing to do with normal NLP tagging
        		Flag_3_feature_parser_types=2
        		**/
        
        String	outFile=mapConfig.get("outFile");
        		outFile=baseFolder+outFile;
		String  NLPfolder=mapConfig.get("NLPfolder");
	    boolean isSOPdebug=Boolean.valueOf(mapConfig.get("isSOPdebug"));
	    String  debugFileName=baseFolder+mapConfig.get("debugFileName"); 
	    int token_containing_body_text= Integer.valueOf(mapConfig.get("token_containing_body_text"));
	    int token_containing_title=Integer.valueOf(mapConfig.get("token_containing_title"));
	    int token_containing_auth_Name=Integer.valueOf(mapConfig.get("token_containing_auth_Name"));
	    int token_containing_sourceURL= Integer.valueOf(mapConfig.get("token_containing_sourceURL"));
		int token_containing_keywords= Integer.valueOf(mapConfig.get("token_containing_keywords"));
		int token_containing_date= Integer.valueOf(mapConfig.get("token_containing_date"));
	    String	start_pattern_containing_author_name=mapConfig.get("start_pattern_containing_author_name"); //from:
	    boolean is_Append_outFile=Boolean.valueOf(mapConfig.get("is_Append_outFile"));
	    int		skip_top_N_lines= Integer.valueOf(mapConfig.get("skip_top_N_lines"));
	    String  Flag_2=mapConfig.get("Flag_2");
	    int  	Flag_3_feature_parser_types=Integer.valueOf(mapConfig.get("Flag_3_feature_parser_types"));
	    
		System.out.println("Given Configuration:");
			//
		for(String i:mapConfig.keySet()){
			System.out.println(i+" "+mapConfig.get(i));
		} 
		
		//NOTE: not need NLP , THEN use "readFile_readEachLine_For_a_Set_of_Start_String_End_String_WriteToAnotherFile" to extract
		// extract_features_from_Each_doc ( NLP tagging for title and bodyText )
		//NOTE: use "readFile_pick_a_Token_doNLPtagging_writeOUT" for only to do NLP TAGGING
		///////////////////EITHER USE extract_features_from_Each_doc() 
		//  (OR) BELOW** readFile_pick_a_Token_doNLPtagging_writeOUT() <--for p6 problem this was RAN..
//		p6_prepare_Input_feature_file_from_raw_data_p6.
//		extract_features_from_Each_doc( 				//THIS METHOD PICKS THINGS LIKE ORGANIZATION, NUMBERS, LOCATION ETC..
//											inFile,
//											outFile,
//											is_Append_outFile, //is_Append_outFile
//											start_pattern_containing_author_name, //"from:",
//											token_containing_body_text, //body_text
//											token_containing_title,  //title
//											token_containing_auth_Name, // authName
//											token_containing_sourceURL,
//											token_containing_keywords,
//											token_containing_date,
//											NLPfolder, //NLP model folder
//											debugFileName,
//											isSOPdebug, // false, //	is_SOP_print
//											skip_top_N_lines,
//											Flag_2,
//											Flag_3_feature_parser_types
//										 );
		
		// STEP 2: (a) run for bodyText
		POSModel inflag_model2 = new POSModelLoader().load(new File(NLPfolder+ "en-pos-maxent.bin"));
		////
		ReadFile_pick_a_Token_doNLPtagging_writeOUT.
							readFile_pick_a_Token_doNLPtagging_writeOUT(
															baseFolder,
															token_containing_body_text, //token_position_to_insert_lineNumber,
															inFile100, 
															outFile, 
															inFile_alreadyRan_pastTagging,
															1, //default_start_lineNo, 
															NLPfolder, 
															inflag_model2, 
															true, //is_overwrite_flag_with_flag_model, 
															isSOPdebug);

			// STEP 2: (b) run for TITLE
			
			String outFile_2 = outFile+"_titleNLP.txt";
			/////////
			ReadFile_pick_a_Token_doNLPtagging_writeOUT.
								readFile_pick_a_Token_doNLPtagging_writeOUT(
																baseFolder,
																token_containing_title, //<TITLE>  token_position_to_insert_lineNumber,
																outFile,// IN 
																outFile_2,  //OUT
																inFile_alreadyRan_pastTagging, //IN
																1, //default_start_lineNo, 
																NLPfolder, 
																inflag_model2, 
																true, //is_overwrite_flag_with_flag_model, 
																isSOPdebug);
			
			// STEP 3 : 
    		TreeMap<Integer,TreeMap<Integer, String>> mapOut=new TreeMap<Integer, TreeMap<Integer,String>>();
    				
//    	    String NLPfolder="/Users/lenin/OneDrive/jar/NLP/models/";
//    		String baseFolder="/Users/lenin/Downloads/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds10/";
    		String textNLPtagged_input_2="u.s._DT defense_NN secretary_NN chuck_NN hagel_NN said_VBD on_IN tuesday_IN he_PRP expected_VBD to_TO discuss_VB lessons_NNS learned_VBN from_IN the_DT search_NN for_IN a_DT missing_VBG malaysian_JJ jetliner_NN at_IN talks_NNS with_IN southeast_JJ asian_JJ defense_NN chiefs,_NN but_CC stopped_VBD short_RB of_IN criticizing_JJ malaysia's_NN coordination_NN effort.";
    			   textNLPtagged_input_2="";
    	    String intext_NLP_untagged_input_2="";
    		    		
//            String inFile_input_1=baseFolder+"mergedall-2016-T9-10-Topics-all-tokens.txt_ONLY_ENGLISH.txt_removed_NOISE.txt_REMOVE_STOPWORDS_FINAL.txt";
//            	   inFile_input_1="";
            
            String 	outFile2=baseFolder+"Noun_count.txt";
             
            int 	token_having_body_text_taggedNLP_inFile=10;
    				InputStream is = null;  ParserModel inflag_model2_chunking = null;
    		
    		String serializedClassifier="/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.conll.4class.distsim.crf.ser.gz";
    		 CRFClassifier<CoreLabel>  NER_classifier=CRFClassifier.getClassifierNoExceptions(serializedClassifier);
    		
    		 String debug_File=baseFolder+"debug.txt";
    		 
    		try{is=new FileInputStream(NLPfolder+"en-parser-chunking.bin");
    			inflag_model2_chunking=new ParserModel(is);
    		}
    		catch(Exception e){ e.printStackTrace();}
    		int run_top_N_lines_only=-1;// *******
            // find_VERB_AND_Noun_from_TaggedString_inFile
    		 mapOut=find_VERB_AND_Noun_JustBfr_from_TaggedString_inFile(						 baseFolder,
    				 																			 outFile_2,
											                                                     textNLPtagged_input_2,
											                                                     intext_NLP_untagged_input_2,
											                                                     outFile2,
											                                                     token_having_body_text_taggedNLP_inFile,
											                                                     false, //isSOPprint
											                                                     inflag_model2_chunking,
											                                                     NER_classifier,
											                                                     run_top_N_lines_only, //-1 allowed 
											                                                     debug_File
											                             );
    		 
    		 System.out.println("---output---");
    		 for(int i:mapOut.keySet()){
    			 if(mapOut.containsKey(i) && mapOut.get(i)!=null){
	    			 for(int j:mapOut.get(i).keySet()){
	    				 System.out.println(i+"<-->"+ mapOut.get(i).get(j)+"<-->"
	    						 +  mapOut.get(i).get(j).replace("!!!", "!#!#").split("!#!#").length  +   "<-->"
	    						 + mapOut.get(i).get(j)+"<-->"+mapOut.get(i).size());
	    			 }
    			 }
    		 }
    		 
    		 String []s="h._nn!!!charles_nns!!!financier_nn!!!time_nn!!!!!!!!!!!!".replace("!!!", "!#!#").split("!#!#");
    		 System.out.println("a@b".split("@").length);
    		  
    		 
    		 String content="Charles H. Keating Jr., the notorious financier who served prison time and was disgraced for his role in the costliest savings and loan failure of the 1980s,"
    		 				+ " has died. He was 90.";
 
    		 // OVERWRITE IT ..
    		 inFile100=outFile_2; //OVERWRITE IT ..
    		 token_containing_body_text=9; //OVERWRITE IT ..
    		 
    		 int token_containing_body_text_WITH_NLP=10;
    		 int token_containing_primary_key=2;
    		 
    		 System.out.println("token_containing_body_text:"+token_containing_body_text+" inFile:"+inFile100 );
    		 String output_organization_person_count=baseFolder+"output_organiz_person_locations_numbers_docID.txt";
 
    		 //note: INPUT OF THIS METHOD IS OUTPUT OF readFile_pick_a_Token_doNLPtagging_writeOUT() ABOVE
 		    // STEP: run and get Organizations and Person names mentioned in the token?
    		 get_organization_person_location_numbers(
						 		    					baseFolder,
						 		    					inFile100, // has all tokens
						 		    					token_containing_body_text,
						 		    					token_containing_body_text_WITH_NLP,
						 		    					token_containing_primary_key,
						 		    					output_organization_person_count,
						 		    					-1 // skip_top_N_lines
 		    										);
    		 
    		 
    		 System.out.println("run_top_N_lines_only:"+run_top_N_lines_only);
    		 System.out.println("Time Taken:"
    					+ NANOSECONDS.toSeconds(System.nanoTime() - t0) + " seconds; "
    					+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
    					+ " minutes");
    		 
    }
	
    
}
