package p8;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import p8.Wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati;
import org.tartarus.snowball.ext.PorterStemmer;
import crawler.Calc_tf_idf;

import crawler.ReadFile_2_EachLineORtoken_from_First_File_exists_in_Second_File_as_String;
import crawler.Find_sentiment_stanford_OpenNLP;
import crawler.LoadMultipleValueToKeyMap3;
import crawler.LoadMultipleValueToMap2_AS_KEY_VALUE;
import crawler.StanfordNER;
import crawler.Find_sentiments_of_a_sentence;
import crawler.Stemmer;
import crawler.Calc_checksum;
import crawler.Clean_retain_only_alpha_numeric_characters;
import crawler.Convert_DocID_WordID_Freq_To_another_format;
import crawler.Convert_mapString_to_TreeMap;
import crawler.Print_all_methods_of_a_given_class;
import crawler.ReadFile_CompleteFile_to_a_Single_String_onefile;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import crawler.ReadFile_verify_and_Correct_No_Of_Tokens_In_EachLine;
import crawler.ReadFolder_load_checksum_N_sentiment_from_eachFile;
import crawler.Sort_given_treemap;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeFactory;
import net.sf.antcontrib.math.Math;
import crawler.Find_occurance_Count_Substring;
import crawler.Generate_Random_Number_Range;
import crawler.IsNumeric;
import crawler.Stopwords;

public class MyAlgo {
	
	Object currWord=null;
	
	//
	public static TreeMap<Integer,TreeMap<String, Double>> convert_arrString_to_arrInteger_only_remove_money(  
																							String [] s,
																							FileWriter writer_debug
																							){
		
		TreeMap<Integer,TreeMap<String, Double>> map_FINAL_StringNumber_DoubleNumber=new TreeMap<Integer,TreeMap<String, Double>>();
		TreeMap<String, Double> map_NumberString_NumberDouble=new TreeMap<String, Double>();
		TreeMap<String, Double> map_MoneyString_MoneyDouble=new TreeMap<String, Double>();
		TreeMap<String, Double> map_NumberString_YearDouble=new TreeMap<String, Double>();
		String t="";
		try{
			int c=0;
			// 
			while(c<s.length){
				s[c]=s[c].toLowerCase().replace("the", "");
				t=t+" "+s[c];
				s[c]=clean_String(s[c]).replace(",", "").replace("sbut", "").replace("but", "");
				writer_debug.append("\n clean_String(s[c]):"+(s[c]) +" "+IsNumeric.isNumeric(s[c]));
				String orig_s_c=s[c];
				s[c] = s[c].replaceAll("[^\\d.]", "");
				// YEAR
				if (s[c].indexOf("2010") >=0 || s[c].indexOf("2011")>=0|| s[c].indexOf("2012")>=0|| s[c].indexOf("2013")>=0|| s[c].indexOf("2014")>=0
					|| s[c].indexOf("2015")>=0|| s[c].indexOf("2016")>=0 || s[c].indexOf("1982")>=0
					|| s[c].indexOf("2009")>=0 
					|| s[c].indexOf("1990")>=0 
					|| ( s[c].indexOf("199")>=0  && s[c].length() >= 4 )
					|| ( s[c].indexOf("195")>=0  && s[c].length() >= 4 )
					|| ( s[c].indexOf("198")==0  && s[c].length() == 4 )
						){
					s[c]=s[c].replace("s", "");
					
					map_NumberString_YearDouble.put(s[c], Double.valueOf(s[c] ) );
					
				}
				//MONEY
				else if (orig_s_c.indexOf("$") >=0 || orig_s_c.indexOf("£")>=0
						 || s[c].toLowerCase().indexOf("million")>=0|| s[c].toLowerCase().indexOf("billion")>=0
						) { //money
					s[c]=s[c].replace("$", "").replace("£", "").replace("million", "").replace("billion", "");
					 
					map_MoneyString_MoneyDouble.put( orig_s_c ,  Double.valueOf( s[c]) ); 
				}
				//NUMERIC
				else if( IsNumeric.isNumeric(s[c])  ){
					//writer_debug.append("\n ENTERED clean_String(s[c]):"+clean_String(s[c]));
					map_NumberString_NumberDouble.put(  s[c]  , 
														Double.valueOf(s[c].replace(",", ""))
													   );
				}
				//
				else if (s[c].indexOf("$") >=0 && s[c].indexOf("£")>=0 ) { 
					
				}
				
				c++;
			}
			
			writer_debug.append( "\nconverted:"+map_NumberString_NumberDouble+ " from->"+t);
			writer_debug.flush();
			 
		}
		catch(Exception e){
			
		}
		//  
		map_FINAL_StringNumber_DoubleNumber.put(1, map_NumberString_NumberDouble);
		map_FINAL_StringNumber_DoubleNumber.put(2, map_MoneyString_MoneyDouble);
		map_FINAL_StringNumber_DoubleNumber.put(3, map_NumberString_YearDouble);
		
		return map_FINAL_StringNumber_DoubleNumber;
	}
	
	// isNumeric()
	private static boolean isNumeric(String clean_String) {
		// TODO Auto-generated method stub
		return false;
	}

	// multiple_comma_tokens_clean_for_numeric
	public static String multiple_comma_tokens_clean_for_numeric(String a){
		
		
		 
		String []arr_word=new String[1];
		//arr_word=a.split(",");
		  
		 //int cn= find_occurance_Count_Substring.find_occurance_Count_Substring( ",", a);
		 String concWord="";
		try{
			 arr_word=a.split(",");
			
			 int cnt=0;
			 
			 boolean last_token_last_Character_numeric=false;
			 //
			 while(cnt<arr_word.length){
			
				 String []  h=arr_word[cnt].split(",");
					// last token
					 //System.out.println( h[h.length-1]);
					 
					 //last token after , split -> its first character
					 String c=  h[h.length-1].substring(0, 1);
					 
					 if(! IsNumeric.isNumeric( c)){
						 //System.out.println("false");
						 String new2=arr_word[cnt].substring( arr_word[cnt].lastIndexOf(",")+1 ,arr_word[cnt].length());
						 arr_word[cnt]=new2;
						 
						 
						 concWord=concWord+" "+arr_word[cnt];
						 //System.out.println(" SKIP proper noun new..(continue) "+concWord);
					 }
					 else{ // NUMERIC .. 1,00,00  <- this should not skip 
						 //System.out.println("true");
						 
						 if(concWord.length()==0)
							 concWord=arr_word[cnt];
						 else{
							 
							 // get last 
							 if(cnt>0 && last_token_last_Character_numeric){ //NUMERIC 
								 concWord=concWord+","+arr_word[cnt];	 
							 }
							 else if(cnt>0 && last_token_last_Character_numeric==false){ // NOT NUMERIC 
								 concWord=concWord+"#"+arr_word[cnt];
							 }
							 else { //NUMERIC
								 concWord=concWord+","+arr_word[cnt];
							 }
							 
						 }

						 //System.out.println(" SKIP proper noun new..(continue) "+concWord);
					 }
					 
					 
					 //last token , last character is NUMERIC
					 try{
						 
						 String lastToken_lastChar=arr_word[cnt].substring(arr_word[cnt].length()-1, arr_word[cnt].length());
						 //
						 if(IsNumeric.isNumeric(lastToken_lastChar)){
							 last_token_last_Character_numeric=true;
						 }
						 
						 
					 }
					 catch(Exception e){
						 
					 }
					 
				cnt++;
			 }
			
			
		}
		catch(Exception e){
			
		}
		
		return concWord;
	}
	
	//convert 
	public static TreeMap<String, String> convert_map_IS_SS_(TreeMap<Integer,String> mapIS ){
		TreeMap<String,String> mapSS=new TreeMap<String, String>();
		try {
			
			for(int i:mapIS.keySet()){
				mapSS.put( String.valueOf(i), mapIS.get(i) );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mapSS;
	}
	// stem_2 stemming
	public static String stem_2(String s, PorterStemmer stemmer2) {
		  //PorterStemmer stemmer2 = new PorterStemmer();
		  String out="";
		  try{
			  stemmer2.setCurrent(s); //set string you need to stem
			  stemmer2.stem();  //stem the word
			  out=stemmer2.getCurrent();
			  //get the stemmed word
			  System.out.println("2 inside stem_2 :"+out);
			   
		  }
		  catch(Exception e){
			  e.printStackTrace();
		  }
		  return out;
	  }
	
	//convert 
		public static TreeMap<String, String> convert_map_SF_SS_(TreeMap<String, Float> mapFS ){
			TreeMap<String,String> mapSS=new TreeMap<String, String>();
			try {
				
				for(String i:mapFS.keySet()){
					mapSS.put(i,  String.valueOf(mapFS.get(i)) );
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			return mapSS;
		}
	
	//get all root Name of News articles
	public static TreeMap<Integer,String> get_rootNodes_of_all_news_articles(TreeMap<Integer, String> mapNodeID_NodeName){
		TreeMap<Integer,String> map_NewsRootNodeID_NodeName= new TreeMap<Integer, String>();
		int no_root_node=0;
		try{
			
			
			//pick the root NAme of each article
			for(int nodeID:mapNodeID_NodeName.keySet()){
				int count_=Find_occurance_Count_Substring.find_occurance_Count_Substring(
									  "aa", mapNodeID_NodeName.get(nodeID));
				if(count_==2){
					no_root_node++;
//					// root node for Article (each)
//					System.out.println("node:"+ mapNodeID_NodeName.get(nodeID)
//								+" IN:"+mapEdge_NodeID1_NodeID2_IN.get(nodeID)
//								+" OUT:"+mapEdge_NodeID1_NodeID2_OUT.get(nodeID)
//
//								);
					map_NewsRootNodeID_NodeName.put(nodeID, mapNodeID_NodeName.get(nodeID));
					
			
				}
				//System.out.println( "count:"+count_+" for node name="+mapNodeID_NodeName.get(nodeID));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return map_NewsRootNodeID_NodeName;
	}
	
	//DO_n_hops_get_IN_OR_OUT_nodes
	private static TreeMap<Integer, TreeMap<String,Integer>> DO_n_hops_get_IN(  TreeMap<Integer, String> map_interested_NodeID_NodeName,
																				TreeMap<Integer, List<Integer>> mapEdge_NodeID1_NodeID2_IN,
																				boolean isSOPprint
																				) {
		TreeMap<Integer, TreeMap<String,Integer>> mapOut=new TreeMap<Integer, TreeMap<String,Integer>>();
		TreeMap<String,Integer> mapINpaths=new TreeMap<String, Integer>();
		TreeMap<String,Integer> mapOUTpaths=new TreeMap<String, Integer>();
		TreeMap<Integer, List<Integer>> mapEdge_NodeID1_NodeID2_IN_EXTENSION=new TreeMap<Integer, List<Integer>>();
		int max_=2; int cnt=0;
		int not_exists_count=0, exists_count=0;
		// TODO Auto-generated method stub
		try{
			System.out.println( "DO_n_hops_get_IN() GIVEN map_interested_NodeID_NodeName:"+map_interested_NodeID_NodeName.size()+
								" mapEdge_NodeID1_NodeID2_IN:"+mapEdge_NodeID1_NodeID2_IN.size());
			 	
				//   
				for(int nodeID:map_interested_NodeID_NodeName.keySet()){
					//
						//IN paths
						if(mapEdge_NodeID1_NodeID2_IN.containsKey(nodeID)){
							exists_count++;
							List<Integer> tmp_l=mapEdge_NodeID1_NodeID2_IN.get(nodeID);
							System.out.println("nodeID:"+nodeID+"--tmp_l:"+tmp_l);
							
							//
							for(int temp:tmp_l){
								cnt++;		
									if(isSOPprint)
										System.out.println("cnt:"+cnt+" 1.Inward node of nodeID:"+nodeID+"<--->"+"is FROM "+temp);
									mapOUTpaths.put(nodeID+":"+cnt , temp);
									
									//
									List<Integer> tmp_l_2=mapEdge_NodeID1_NodeID2_IN.get(nodeID);
//									if(tmp_l_2!=null){
//										for(int temp_2:tmp_l_2){
//											System.out.println("cnt:"+cnt+" 2.Inward node of nodeID:"+temp_2+"<--->"+"is FROM "+nodeID);	
//										}
//									}
							}
						}
						else{
							not_exists_count++;
						}
				}
 
			
			mapOut.put(2, mapINpaths);
			mapOut.put(1, mapOUTpaths);
			System.out.println("not_exists_count:"+not_exists_count+"<-->exists_count:"+exists_count);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	//DO_n_hops_get_IN_OR_OUT_nodes
	private static TreeMap<Integer, TreeMap<String, Integer>> DO_n_hops_get_OUT( TreeMap<Integer, String> map_interested_NodeID_NodeName,
																				TreeMap<Integer, List<Integer>> mapEdge_NodeID1_NodeID2_OUT
																				) {
		TreeMap<Integer, TreeMap<String,Integer>> mapOut=new TreeMap<Integer, TreeMap<String,Integer>>();
		TreeMap<String,Integer> mapINpaths=new TreeMap<String, Integer>();
		TreeMap<String,Integer> mapOUTpaths=new TreeMap<String, Integer>();
		TreeMap<Integer, List<Integer>> mapEdge_NodeID1_NodeID2_IN_EXTENSION=new TreeMap<Integer, List<Integer>>();
		int max_=2; int cnt=0;
		int not_exists_count=0, exists_count=0;
		// TODO Auto-generated method stub
		try{
			System.out.println("DO_n_hops_get_OUT() GIVEN map_interested_NodeID_NodeName:"+map_interested_NodeID_NodeName.size()+
								" mapEdge_NodeID1_NodeID2_OUT:"+mapEdge_NodeID1_NodeID2_OUT.size());
			 
				//   
				for(int nodeID:map_interested_NodeID_NodeName.keySet()){
					//
						//IN paths
						if(mapEdge_NodeID1_NodeID2_OUT.containsKey(nodeID)){
							exists_count++;
							List<Integer> tmp_l=mapEdge_NodeID1_NodeID2_OUT.get(nodeID);
							System.out.println(nodeID+"--"+tmp_l);
							
							//
							for(int temp:tmp_l){
									cnt++;
									//System.out.println("cnt:"+cnt+" 2.Inward node of nodeID:"+nodeID+"<--->"+"is FROM "+temp);
									mapOUTpaths.put(nodeID+":"+cnt , temp);
									
									//
									List<Integer> tmp_l_2=mapEdge_NodeID1_NodeID2_OUT.get(nodeID);
//									if(tmp_l_2!=null){
//										for(int temp_2:tmp_l_2){
//											System.out.println("cnt:"+cnt+" 2.Inward node of nodeID:"+temp_2+"<--->"+"is FROM "+nodeID);	
//										}
//									}
							}
						}
						else{
							not_exists_count++;
						}
				}
			
			mapOut.put(2, mapINpaths);
			mapOut.put(1, mapOUTpaths);
			System.out.println("mapEdge_NodeID1_NodeID2_OUT.not_exists_count:"+not_exists_count+"<-->exists_count:"+exists_count);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	
	// _CD (numbers) etc
	public static TreeMap<Integer, String> find_count_number_words_with_specific_tag(
																				String  currText,
																				Stemmer stemmer,
																				TreeMap<String,String> map_Word_StemmedWord,
																				String  interested_tag_
																				){
		 
		//
		TreeMap<Integer, String> map_Seq_stemmedWordNumber= new TreeMap<Integer, String>();
		try {
			//System.out.println("currText:"+currText);
			String []s=currText.split(" ");
			int c=0;
			// 
			while(c<s.length){
				//System.out.println("token:"+s[c]+" idx:"+s[c].indexOf(interested_tag_)+" :interested_tag_:"+interested_tag_);
				//
				if(s[c].equalsIgnoreCase("__") ){
					//System.out.println("junk:"+s[c]);
					c++; continue;}
				//				
				if(s[c].indexOf(interested_tag_)>=0 ) {
					//System.out.println("caught:"+s[c]);
					String word=s[c].toLowerCase().substring(0 , s[c].indexOf("_"));
					// not stopwords
					if(!Stopwords.is_stopword(word) && map_Word_StemmedWord.containsKey(word)){	
						String stem_word=map_Word_StemmedWord.get(word);
						map_Seq_stemmedWordNumber.put(c, stem_word);// stem_2( word , stemmer));
					}
					else if(!Stopwords.is_stopword(word) && !map_Word_StemmedWord.containsKey(word)){
						map_Seq_stemmedWordNumber.put(c,   stemmer.stem(word) );
					}
					
					
				 }
				c++;
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return map_Seq_stemmedWordNumber;
	}
	
	class MyString
	{
	  String string;

	  MyString(String string)
	  {
	    this.string = new String(string);
	  }
	}
	
//	@Override
//	public boolean equals(Object args0) {
//	   if(args0 == this) { return true; }
//	   if(!(args0 instanceof MyString)) { return false; }
//	   MyString obj = (MyString) args0;
//
//	   return obj.getString().equals(this.string); // Create getter
//	}

//	@Override
//	public int hashCode(){
//	   return this.currWord.hashCode() * 37;
//	}
	
//	public static class Word{
//		int _id;
//		String currWord="";
//		
//		public Word(int i) {
//			   this._id = i;
//			// TODO Auto-generated constructor stub
//		}
// 
//		
//		public  int getWord(){
//			return _id;
//		}
//		
//		//
//	    public void currWord(String currWord) {
//	        this.currWord = currWord;
//	    }
//	    
//	    public String getcurrWord() {
//	        return currWord;
//	    }
//	    
//	    @Override
//	    public int hashCode() {
//	    	final int prime = 31;
//	        int result = prime* _id; 
//	        return result;
//	    }
//	    
//	    //Compare only account numbers
//	    @Override
//	    public boolean equals(Object obj) {
//	        if (this == obj)
//	            return true;
//	        if (obj == null)
//	            return false;
//	        if (getClass() != obj.getClass())
//	            return false;
//	        Word other = (Word) obj;
//	        if (_id != other._id)
//	            return false;
//	        return true;
//	    }
//	    
//	    
//	}
	
	
	// read a <seqID,word> and give <word,TF-IDF>
	//mapOut.get(1) <- map_curr_Word_TFIDF
	//mapOut.get(2) <- map_curr_WordID_TFIDF
	public static TreeMap<Integer,TreeMap<String,Double>>  for_a_given_SeqIDNWord_to_wordNTFIDF(
																				int curr_DocID,
																				TreeMap<String,String> map_seqIDNword,
																				TreeMap<String, Double> map_VocWord_WordID,
																				TreeMap<String, Double> map_WordID_TFIDF,
																				FileWriter      writer_debug,
																				Stemmer 	stemmer2,
																				TreeMap<String, String > map_Word_StemmedWord
																				){
		//stemmer2=new PorterStemmer();
		TreeMap<Integer,TreeMap<String,Double>> mapOut=new TreeMap<Integer,TreeMap<String, Double>>();
		
		TreeMap<String,Double> map_curr_Word_TFIDF=new TreeMap<String, Double>();
	    TreeMap<String,Double> map_curr_WordID_TFIDF=new TreeMap<String, Double>();
	    
	    //TreeMap<myAlgo.Word,String> mapNew=new TreeMap<myAlgo.Word, String>();
	    int i=0;
//	    try{
//		    // iterate
//		    for(String o:map_Word_StemmedWord.keySet()){
//		    	i++;
//		    	Word w=new Word(i);
//		    	if(map_Word_StemmedWord.get(w)!=null){
//		    		w.currWord(map_Word_StemmedWord.get(w).toString());
//		    		mapNew.put(w, map_Word_StemmedWord.get(w).toString());
//		    	}
//		    }
//	    }
//	    catch(Exception e){
//	    	System.out.println("erron on mapnew "+e.getMessage());
//	    }
	    
		String docIDcolonWordID="";
		try {
			
				//writer_debug.append("\n mapNew ->"+mapNew.size());
				writer_debug.flush();
			
		    	//String completeline=
		    	//readFile_CompleteFile_to_a_Single_String_onefile.readFile_CompleteFile_to_a_Single_String_2("/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/tf-idf/tf-idf.txt_Word_stemmedWord.txt");			 
		    	//writer_debug.append("\nmap_Word_StemmedWord(Docid=2:"+map_Word_StemmedWord);
		    	writer_debug.flush();
		      	 
			// each word
			for(String seqID:map_seqIDNword.keySet()){
				double wordID=-1;
				//System.out.println("redo stemming:"+ stem_2( currWord,stemmer2));
				
//				Word w=new Word(Integer.valueOf(seqID));
				
//				if(map_Word_StemmedWord.get(w)!=null)
//					w.currWord(map_Word_StemmedWord.get(w).toString());
				
				String currWord=map_seqIDNword.get(seqID).toLowerCase().trim();
				//if(currWord.toLowerCase().indexOf("null") >=0) {continue;}
				String stem_currWord="";
				//String stem_currWord= stem_2(currWord,stemmer2);
				
				//map_Word_StemmedWord.equals(currWord)
				//get_stemword_for_givenWord(currWord.toString(), completeline);
				//writer_debug.append("\n mapNew ->"+currWord+" "+mapNew.get(w));
				writer_debug.flush();
				
				
				
				//VALUE
				if(map_Word_StemmedWord.containsValue(currWord)){
					stem_currWord=currWord;
				}
				//KEY
				else if(map_Word_StemmedWord.get(currWord)!=null)
					stem_currWord=map_Word_StemmedWord.get(currWord).toString();
				else{
				//	System.out.println("**** NOT found cant find in key "+currWord);
					//stem_currWord=map_Word_StemmedWord.get(currWord).toString().toLowerCase();
				}
				
				//NO AVAIBLE STEM
//				if(map_Word_StemmedWord.get(currWord)==null){
//					System.out.println("no STEM available:"+currWord
//								+"--"+map_Word_StemmedWord.containsKey(currWord)+"--"+map_Word_StemmedWord.containsValue(currWord)
//								+"--"+map_Word_StemmedWord.containsKey(currWord.toString())
//								+"--"+map_Word_StemmedWord.equals(currWord)
//								+"--"+map_Word_StemmedWord.equals(currWord.toString())
//								+"--"+map_Word_StemmedWord.size()
//								+"--contains:"+completeline.toLowerCase().contains(currWord.toString()+"!!!")
//								+"--index:"+completeline.indexOf(currWord.toString()+"!!!")
//								+"--stem_currWord="+stem_currWord	
//								+"--line:"+completeline.length()
////								+"--new:"+mapNew.get(w)
//								//+"--completeline:"+completeline
//								);
//					//writer_debug.append("\n NOT found stem:"+currWord+"--"+mapNew.get(w));
//					writer_debug.flush();
//					//System.out.println("map_Word_StemmedWord:"+map_Word_StemmedWord);
//					continue;
//				}
//				else{
//					//writer_debug.append("\n found stem:"+mapNew.get(w));
//					writer_debug.flush();
//					System.out.println("found STEM available:stem_currWord="+stem_currWord );
//				}

				
				
				//
				if( map_VocWord_WordID.get( stem_currWord ) !=null ){
					wordID=(double) map_VocWord_WordID.get( stem_currWord );
				}
				else if(map_VocWord_WordID.get(currWord ) !=null){
					wordID=(double) map_VocWord_WordID.get( currWord );
				} 
				//writer_debug.append("\nmap_VocWord_WordID:"+map_VocWord_WordID);
				//writer_debug.append("\n word:"+currWord+" wordID:"+wordID+" stem_currWord:"+stem_currWord);
				writer_debug.flush();
				// 
				if(wordID>0){
					int wordId= (int)wordID;
					docIDcolonWordID=String.valueOf(curr_DocID)+":"+ String.valueOf(  wordId);

					//writer_debug.append( "\n wordID>0: docID:wordId->"+docIDcolonWordID);
					writer_debug.flush();
					
					//
					if(map_WordID_TFIDF.containsKey( docIDcolonWordID))
						map_curr_Word_TFIDF.put( currWord.toString() , map_WordID_TFIDF.get(docIDcolonWordID) );
					if(map_WordID_TFIDF.containsKey(docIDcolonWordID))
						map_curr_WordID_TFIDF.put(String.valueOf(wordID), map_WordID_TFIDF.get( docIDcolonWordID ));
				}
				
			}
			
		} 
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
//			if(writer_debug!=null)
//				try {
//					writer_debug.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
		}
		
		mapOut.put(1, map_curr_Word_TFIDF);
		mapOut.put(2, map_curr_WordID_TFIDF);
		return mapOut;
	}
	
	private static void get_stemword_for_givenWord(String stringToserch, String completeline) {
		// TODO Auto-generated method stub
		
		try{
			int beginidx=completeline.indexOf(stringToserch);
			int endidx=completeline.indexOf("!#!#",beginidx);
			
		}
		catch(Exception e ) {
			
		}
		
	}
	// getNormalize 
	public static TreeMap<Integer,TreeMap<String,Double>> getNormalize(     TreeMap<String,String []>   map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE,
																			TreeMap<String, String[]>   map_inFile_NodeID_N_arrOUTNodes_from_OUTNODES_FILE,
																			TreeMap<String,Integer> 	map_inFile_NodeeName_N_NodeID_from_OUTNODES_FILE,
																			TreeMap<String, Integer>    map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP,
																			TreeMap<String, Integer> 	map_NodeName_N_DocID_groundTruthTP,
																			TreeMap<String, String> 	map_SS_NodePair_EdgeValue,
																			String                      debug_file,
																			double						interested_edge_weight,
																			String						curr_query
																		){
		double sum=0.0; int TOTAL_number_of_valide_out_NODEPair_with_interes_edge_weight=0;int 
		total_num_OUT_nodes=0;
		TreeMap<Integer,TreeMap<String,Double>> mapFinalOut=new TreeMap<Integer, TreeMap<String, Double>>();
		
		TreeMap<String,Double> mapOut=new TreeMap<String, Double>();
		//beow has only root node name (doc ID)
		TreeMap<String, Double> map_NodeName_OUTNodes_Count_With_interestedEdgeWeight=new TreeMap<String, Double>();
		double curr_docID_outNodeCount_With_interested=0;
		int total_count_groundTruth_TP_having_OUTnode_with_interested_edge_weight=0;
		TreeMap<String,Integer> map_only_groundTruth_TP=new TreeMap<String, Integer>();
		FileWriter writer_debug=null;
		try{
			
			writer_debug=new FileWriter(new File(debug_file),true);
			int cnt=0;
			String curr_node_pair_edge_weight_first_level=""; int nullcount=0;
			
			
			if(map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE!=null){
				writer_debug.append("\n map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE:"+map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.size());
			}
			if(map_inFile_NodeID_N_arrOUTNodes_from_OUTNODES_FILE!=null){
				writer_debug.append("\n map_inFile_NodeID_N_arrOUTNodes_from_OUTNODES_FILE:"+map_inFile_NodeID_N_arrOUTNodes_from_OUTNODES_FILE.size());
			}
			if(map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP!=null){
				writer_debug.append("\n map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP:"+map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.size());
			}
			if(map_SS_NodePair_EdgeValue!=null){
				writer_debug.append("\n map_SS_NodePair_EdgeValue:"+map_SS_NodePair_EdgeValue.size());
			}
			writer_debug.flush();
			int currNodeID_one=-1;
			//  
			for(String currNodeName:map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.keySet()){
				curr_docID_outNodeCount_With_interested=0;
				String [] arr_Corresp_OUTnodes=map_inFile_NodeID_N_arrOUTNodes_from_OUTNODES_FILE.get(currNodeName);
				
				if(map_inFile_NodeeName_N_NodeID_from_OUTNODES_FILE.containsKey(currNodeName)){
						currNodeID_one=  map_inFile_NodeeName_N_NodeID_from_OUTNODES_FILE.get(currNodeName);
				}
				else{
					writer_debug.append("\n  cant find node id for nodename:"+currNodeName);
					writer_debug.flush();
					continue;
				}
				
				if(arr_Corresp_OUTnodes==null){nullcount++;continue;}
//				writer_debug.append("\n 1.11");
//				writer_debug.flush();
				cnt=0;
				// 
				while(cnt<arr_Corresp_OUTnodes.length){
					int curr_PairNodeID_two=Integer.valueOf(arr_Corresp_OUTnodes[cnt]);
					 
					if(arr_Corresp_OUTnodes[cnt].length()==0) {cnt++; continue;}
					
					String edge_node_pair=currNodeID_one+":"+curr_PairNodeID_two;
					String edge_node_pair_reverse=curr_PairNodeID_two+":"+currNodeID_one;
					
//					writer_debug.append("\n 1.2  pair:"+edge_node_pair );
//					writer_debug.flush();
					 
						// edge weight
						if( map_SS_NodePair_EdgeValue.containsKey( edge_node_pair ) ||
								map_SS_NodePair_EdgeValue.containsKey( edge_node_pair_reverse )
								){
							
							
//							writer_debug.append("\n 1.4 "+"exists");
//							writer_debug.flush();
							
							if(map_SS_NodePair_EdgeValue.containsKey( edge_node_pair  ) ){
								curr_node_pair_edge_weight_first_level=map_SS_NodePair_EdgeValue.get(edge_node_pair);
							}
							if(map_SS_NodePair_EdgeValue.containsKey( edge_node_pair_reverse)){
								curr_node_pair_edge_weight_first_level=map_SS_NodePair_EdgeValue.get(edge_node_pair_reverse);
							}			
						}
//						writer_debug.append("\n 1.5 curr_node_pair_edge_weight_first_level:"+curr_node_pair_edge_weight_first_level+
//											" interested_edge_weight:"+interested_edge_weight );
//						writer_debug.flush();
						//
						if(curr_node_pair_edge_weight_first_level.length()>0){
							/// edge weight MATCHING
							if( interested_edge_weight == Double.valueOf( curr_node_pair_edge_weight_first_level )){
								
//								writer_debug.append("\n entered:" );
								TOTAL_number_of_valide_out_NODEPair_with_interes_edge_weight++;
								sum=sum+Double.valueOf( curr_node_pair_edge_weight_first_level );
								
								curr_docID_outNodeCount_With_interested++;
//								writer_debug.append("\n entered:sum:"+sum +" NUM:"+number_of_valide_outNODEid_with_interes_edge_weight );
//								writer_debug.flush();
								
								//ground truth TP
								if( map_NodeName_N_DocID_groundTruthTP.containsKey(currNodeName) ){
									   total_count_groundTruth_TP_having_OUTnode_with_interested_edge_weight++; 
										map_only_groundTruth_TP.put(currNodeName , -1);
										
								}

							}
							else{
//								writer_debug.append("\n NOT entered:" );
//								writer_debug.flush();
							}
						}
						total_num_OUT_nodes++;
						cnt++;
					}
				map_NodeName_OUTNodes_Count_With_interestedEdgeWeight.put(currNodeName, curr_docID_outNodeCount_With_interested );
			} //FOR for(String currNodeName:map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.keySet()){
			
//			writer_debug.append("\n for curr_query="+curr_query+" sum/ (double)number_of_valide_outNODEid="+sum/ (double)number_of_valide_outNODEid_with_interes_edge_weight
//												+" number_of_valide_outNODEid / (double) total_num_OUT_nodes:"
//															+number_of_valide_outNODEid_with_interes_edge_weight / (double) total_num_OUT_nodes);
			writer_debug.flush();
			
			mapOut.put("1", new Double(TOTAL_number_of_valide_out_NODEPair_with_interes_edge_weight) );
			mapFinalOut.put(1, mapOut);
			
			mapOut=new TreeMap<String, Double>();
			mapOut.put("2", new Double(sum));
			mapFinalOut.put(2, mapOut);
			
			mapOut=new TreeMap<String, Double>();
			mapOut.put("3", new Double(total_num_OUT_nodes));
			mapFinalOut.put(3, mapOut);
			
			mapOut=new TreeMap<String, Double>();
			if(map_only_groundTruth_TP!=null)
				mapOut.put("5", new Double(map_only_groundTruth_TP.size()));
			else 
				mapOut.put("5", 0.0);
			mapFinalOut.put(5, mapOut);
			
			mapOut=new TreeMap<String, Double>();
			mapOut.put("6", new Double(total_count_groundTruth_TP_having_OUTnode_with_interested_edge_weight));
			mapFinalOut.put(6, mapOut);
			 
			mapFinalOut.put(4, map_NodeName_OUTNodes_Count_With_interestedEdgeWeight);
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				if(writer_debug!=null)
					writer_debug.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		return mapFinalOut;
	}
	//

	// wrapper_2_get_given_DocID_TopN_TFIDF_for_person_organiz_locatio_numbers
	// mapOut.get(1) -> person,
	// mapOut.get(11).get("1") -> person average of sum of TF-IDF
	// mapOut.get(2) -> organization
	// mapOut.get(22).get("2") -> organiz average of sum of TF-IDF
	// mapOut.get(3) -> location
	// mapOut.get(33).get("3") -> location average of sum of TF-IDF
	// mapOut.get(4) -> number
	// mapOut.get(44).get("4") -> number average of sum of TF-IDF
	public static TreeMap<Integer,TreeMap<String, Double >> wrapper_2_get_given_DocID_TopN_TFIDF_for_person_organiz_locatio_numbers(
																			int	curr_DocID,
																			TreeMap<String, String > map_curr_SEQ_person,
																			TreeMap<String, String > map_curr_SEQ_locat,
																			TreeMap<String, String > map_curr_SEQ_organiz,
																			TreeMap<String, String > map_curr_SEQ_numbers,
																			TreeMap<String,Double>   map_VocWord_WordID,
																			TreeMap<String,Double>   map_DocIDWordID_TFIDF,
																			int top_N_TFIDF_only,
																			double offset_for_TFIDF,
																			TreeMap<String, String > map_Word_StemmedWord,
																			Stemmer stemmer,
																			String debugFile,
																			boolean is_debug_more
																			){
		//stemmer=new PorterStemmer();
		TreeMap<Integer, Double> map_OUT_of_SUM_AVG_TF_IDF_curr_feature=new TreeMap<Integer, Double>();
		double avg_person_currDocID_TFIDF=0.;
		double avg_organiz_currDocID_TFIDF=0.;
		double avg_location_currDocID_TFIDF=0.;
		double avg_number_currDocID_TFIDF=0.;
		TreeMap<Integer,TreeMap<String, Double >> mapOut=new TreeMap<Integer, TreeMap<String,Double>>();
		TreeMap<Integer,TreeMap<String, Double >> map_curr_WordNtfidf_N_WordIDNtfidf=new TreeMap<Integer, TreeMap<String,Double>>();
		FileWriter writer_debug=null;
		try {
			writer_debug=new FileWriter(debugFile, true);
			//person
			TreeMap<String,Double> map_curr_person_WordNtfidf=new TreeMap<String, Double>();
			double sum_curr_DocID_person_TopN_TFIDF=0;
			TreeMap<String,Double> map_curr_person_WordIDNtfidf=new TreeMap<String, Double>();
			double sum_person_curr_DocID_TFIDF_Top_N_=0;
			 
			if(map_curr_SEQ_person!=null){

				// focus words
				//mapOut.get(1) <- map_curr_Word_TFIDF
				//mapOut.get(2) <- map_curr_WordID_TFIDF
				map_curr_WordNtfidf_N_WordIDNtfidf=for_a_given_SeqIDNWord_to_wordNTFIDF(
																						 curr_DocID,
																						 map_curr_SEQ_person,
																						 map_VocWord_WordID,
																						 map_DocIDWordID_TFIDF,
																						 writer_debug,
																						 stemmer,
																						 map_Word_StemmedWord
																						);
				//
				map_curr_person_WordNtfidf=map_curr_WordNtfidf_N_WordIDNtfidf.get(1);
				map_curr_person_WordIDNtfidf=map_curr_WordNtfidf_N_WordIDNtfidf.get(2);
				
				//if(is_debug_more)
					writer_debug.append("\n BEFORE for_a_given_SeqIDNWord_to_wordNTFIDF() person..len:"+map_curr_SEQ_person.size()
												+" filtered.size:"+map_curr_SEQ_person.size());writer_debug.flush();
				 
				
				//System.out.println("map_curr_person_WordIDNtfidf:"+map_curr_person_WordIDNtfidf);
				if(is_debug_more){
					writer_debug.append("\n map_curr_person_WordIDNtfidf person..:"+map_curr_person_WordIDNtfidf);
					writer_debug.append("\n map_curr_person_WordNtfidf person..: "+map_curr_person_WordNtfidf);
				}
				writer_debug.flush();
				
				// sort
				map_OUT_of_SUM_AVG_TF_IDF_curr_feature=sort_AND_get_TOP_N_(
//																	map_curr_person_WordIDNtfidf
																	map_curr_person_WordNtfidf,
																	top_N_TFIDF_only,
																	offset_for_TFIDF,
																	"debug label person ",
																	debugFile
																	);
				
				mapOut.put(1 , map_curr_person_WordNtfidf);
				//System.out.println("person------"+map_curr_person_WordNtfidf.size()+" sum:"+map_OUT_of_SUM_AVG_TF_IDF_curr_feature);
				// .get(1) = SUM , .get(2) = AVG
				avg_person_currDocID_TFIDF=map_OUT_of_SUM_AVG_TF_IDF_curr_feature.get(2);
				//AVERAGE OF SUM
				TreeMap<String, Double > temp=new TreeMap<String, Double>();
				temp.put("1", avg_person_currDocID_TFIDF);
				mapOut.put(11 , temp );
			}
			else{
				if(is_debug_more){
					writer_debug.append("\n map_curr_SEQ_person is NULL");
					writer_debug.flush();
				}
			}

			 //organization
			TreeMap<String,Double> map_curr_organiz_WordNtfidf=new TreeMap<String, Double>();
			TreeMap<String,Double> map_curr_organiz_WordIDNtfidf=new TreeMap<String, Double>();
			double sum_curr_DocID_organiz_TopN_TFIDF=0;
			if(map_curr_SEQ_organiz!=null){

				//mapOut.get(1) <- map_curr_Word_TFIDF
				//mapOut.get(2) <- map_curr_WordID_TFIDF
				map_curr_WordNtfidf_N_WordIDNtfidf=for_a_given_SeqIDNWord_to_wordNTFIDF(
																						 curr_DocID,
																						 map_curr_SEQ_organiz,
																						 map_VocWord_WordID,
																						 map_DocIDWordID_TFIDF,
																						 writer_debug,
																						 stemmer,
																						 map_Word_StemmedWord
																						);
				 
				
				
				map_curr_organiz_WordNtfidf=map_curr_WordNtfidf_N_WordIDNtfidf.get(1);
				map_curr_organiz_WordIDNtfidf=map_curr_WordNtfidf_N_WordIDNtfidf.get(2);
				// focus words
				//if(is_debug_more)
					writer_debug.append("\n BEFORE for_a_given_SeqIDNWord_to_wordNTFIDF() organiz..len:"+map_curr_SEQ_organiz.size()
															+" filtered.size:"+map_curr_SEQ_organiz.size());writer_debug.flush();
				
				//
				if(is_debug_more){
					writer_debug.append("\n num map_curr_organiz_WordNtfidf:"+map_curr_organiz_WordNtfidf);
					writer_debug.append("\n num map_curr_organiz_WordIDNtfidf:"+map_curr_organiz_WordIDNtfidf);
					writer_debug.flush();
				}
				
				// sort
				map_OUT_of_SUM_AVG_TF_IDF_curr_feature=sort_AND_get_TOP_N_(map_curr_organiz_WordNtfidf, 
																	 	   top_N_TFIDF_only, 
																	 	   offset_for_TFIDF,
																	      "debug label organiz ",
																	      debugFile
																	      );
				
				mapOut.put(2 , map_curr_organiz_WordNtfidf);
				//System.out.println("organiz------"+map_curr_organiz_WordNtfidf.size()+" sum:"+map_OUT_of_SUM_AVG_TF_IDF_curr_feature);
				// .get(1) = SUM , .get(2) = AVG
				avg_organiz_currDocID_TFIDF=map_OUT_of_SUM_AVG_TF_IDF_curr_feature.get(2);
				//AVERAGE OF SUM
				TreeMap<String, Double > temp=new TreeMap<String, Double>();
				temp.put("2", avg_organiz_currDocID_TFIDF);
				mapOut.put(22 , temp );
			}
			else{
				if(is_debug_more){
					writer_debug.append("\n map_curr_SEQ_organiz is NULL");
					writer_debug.flush();
				}
			}
			 
			//location
			TreeMap<String,Double> map_curr_location_WordNtfidf=new TreeMap<String, Double>();
			double sum_curr_DocID_location_TopN_TFIDF=0;
			TreeMap<String,Double> map_curr_location_WordIDNtfidf=new TreeMap<String, Double>();
			if(map_curr_SEQ_locat!=null){

				// focus words
				//mapOut.get(1) <- map_curr_Word_TFIDF
				//mapOut.get(2) <- map_curr_WordID_TFIDF
				map_curr_WordNtfidf_N_WordIDNtfidf=for_a_given_SeqIDNWord_to_wordNTFIDF(
						 															     curr_DocID,
																						 map_curr_SEQ_locat,
																						 map_VocWord_WordID,
																						 map_DocIDWordID_TFIDF,
																						 writer_debug,
																						 stemmer,
																						 map_Word_StemmedWord
																						);
				
				map_curr_location_WordNtfidf=map_curr_WordNtfidf_N_WordIDNtfidf.get(1);
				map_curr_location_WordIDNtfidf=map_curr_WordNtfidf_N_WordIDNtfidf.get(2);
				
				writer_debug.append("\n BEFORE for_a_given_SeqIDNWord_to_wordNTFIDF() locat..len:"+map_curr_SEQ_locat.size()
										+" filtered.size:"+map_curr_location_WordNtfidf.size());writer_debug.flush();
				
				// sort
				map_OUT_of_SUM_AVG_TF_IDF_curr_feature=sort_AND_get_TOP_N_( 
																		map_curr_location_WordNtfidf, 
																		top_N_TFIDF_only,
																		offset_for_TFIDF,
																		" debug label location ",
																		debugFile
																		);
				//System.out.println("locat------"+map_curr_location_WordNtfidf.size()+" sum:"+map_OUT_of_SUM_AVG_TF_IDF_curr_feature);
				mapOut.put(3 , map_curr_location_WordNtfidf);
				// .get(1) = SUM , .get(2) = AVG
				avg_location_currDocID_TFIDF=map_OUT_of_SUM_AVG_TF_IDF_curr_feature.get(2);
				//AVERAGE OF SUM
				TreeMap<String, Double > temp=new TreeMap<String, Double>();
				temp.put("3", avg_location_currDocID_TFIDF);
				mapOut.put(33 , temp );
			}
			else{
				if(is_debug_more){
					writer_debug.append("\n map_curr_SEQ_locat is NULL");
					writer_debug.flush();
				}
			}
			 
			//numbers
			TreeMap<String,Double> map_curr_numbers_WordNtfidf=new TreeMap<String, Double>();
			double sum_curr_DocID_numbers_TopN_TFIDF=0;
			TreeMap<String,Double> map_curr_numbers_WordIDNtfidf=new TreeMap<String, Double>();
			if(map_curr_SEQ_numbers!=null){
				// focus words
				 
				
				//mapOut.get(1) <- map_curr_Word_TFIDF
				//mapOut.get(2) <- map_curr_WordID_TFIDF
				map_curr_WordNtfidf_N_WordIDNtfidf=for_a_given_SeqIDNWord_to_wordNTFIDF(
						 																 curr_DocID,
																						 map_curr_SEQ_numbers,
																						 map_VocWord_WordID,
																						 map_DocIDWordID_TFIDF,
																						 writer_debug,
																						 stemmer,
																						 map_Word_StemmedWord
																						);
				map_curr_numbers_WordNtfidf=map_curr_WordNtfidf_N_WordIDNtfidf.get(1);
				map_curr_numbers_WordIDNtfidf=map_curr_WordNtfidf_N_WordIDNtfidf.get(2);
				
				
				writer_debug.append("\n BEFORE for_a_given_SeqIDNWord_to_wordNTFIDF() numbers..len:"+map_curr_SEQ_numbers.size()
										+" filtered.size:"+map_curr_SEQ_numbers.size());writer_debug.flush();
				 
				
				if(is_debug_more){
					writer_debug.append("\n num map_curr_numbers_WordNtfidf:"+map_curr_numbers_WordNtfidf);
					writer_debug.append("\n num map_curr_numbers_WordIDNtfidf:"+map_curr_numbers_WordIDNtfidf);
					writer_debug.flush();
				}
				
				map_OUT_of_SUM_AVG_TF_IDF_curr_feature=sort_AND_get_TOP_N_(map_curr_numbers_WordIDNtfidf, 
																	 		top_N_TFIDF_only,
																	 		offset_for_TFIDF,
																	 		"debug label numbers ",
																	 		debugFile
																	 		);
		
				mapOut.put(4 , map_curr_numbers_WordNtfidf);
				//System.out.println("numbers------"+map_curr_numbers_WordIDNtfidf.size()+" sum:"+map_OUT_of_SUM_AVG_TF_IDF_curr_feature);
				// .get(1) = SUM , .get(2) = AVG
				avg_number_currDocID_TFIDF=map_OUT_of_SUM_AVG_TF_IDF_curr_feature.get(2);
				//AVERAGE OF SUM
				TreeMap<String, Double > temp=new TreeMap<String, Double>();
				temp.put("4", avg_number_currDocID_TFIDF);
				mapOut.put(44 , temp );
			}
			else{
				if(is_debug_more){
					writer_debug.append("\n map_curr_SEQ_numbers is NULL");
					writer_debug.flush();
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			
			try{
				if(writer_debug!=null)
					writer_debug.close();
			}
			catch(Exception e){
				
			}
		}
		return mapOut;
	}
	// clean String
	public static String clean_String(String inString){
		return inString.replace("\""," ").replace("'", " ").replace("“", " ").replace("‘", " ").replace("–", " ").replace(":", " ")
		.replace(";", " ").replace("—", " ").replace("{", " ").replace("}", " ").replace("?", " ").replace(")"," ").replace("("," ")
		.replace("&amp", " " ).replace("&lsquo", " ").replace("”", " ").replace("ece", " ")
		.replace("&lt"," ").replace("&gt"," ").replace("&quot"," ").replace("—", " ")
		.replace("  ", " ");
		
	}
	// 
	private static void myAlgo(	
								String baseFolder,
								String inFile_GBAD, 
								TreeMap<String, String> map_SS_NodePair_EdgeValue,
								TreeMap<String, String> map_Word_StemmedWord,
								TreeMap<String, String> map_Word_StemmedWord_WIKIPEDIA,
								TreeMap<Integer, TreeMap<String, String>> mapOut3_networkX_mapString_property_1, //docID starts from zero
								TreeMap<Integer, TreeMap<String, String>> mapOut3_networkX_mapString_property_2, //docID starts from zero
								TreeMap<Integer, TreeMap<String, Double>> map_VocabFile_TFIDFfile,
								TreeMap<Integer, TreeMap<String, String>> map_Nodes_GT_N_NOT_GT,
								TreeMap<String, TreeMap<String, String>> map_lineNoNID_person_organiz_locati_numbers_nouns,
								TreeMap<Integer, String>  map_inFile_seqID_N_URL_outOFcontext,
								TreeMap<String, Integer>  map_NodeName_N_DocID_groundTruthTP, 
								TreeMap<String, Integer>  map_NodeName_N_DocID_NOTgroundTruthTP, 
								TreeMap<String, Integer>  map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_GLOBAL,
								TreeMap<Integer, String>  map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes,
								TreeMap<Integer, String>  map_DocID_NLPtext,
								TreeMap<Integer, String>  map_DocID_bodytext,
								TreeMap<Integer, String>  map_DocID_URL,
								TreeMap<String, String[]> map_NounWord_N_arr_lineNo,
								TreeMap<String, String[]> map_VerbWord_N_arr_lineNo,
								TreeMap<String, String[]> map_PersonWord_N_arr_lineNo,
								TreeMap<String, String[]> map_OrganizWord_N_arr_lineNo,
								TreeMap<String, String[]> map_LocationWord_N_arr_lineNo,
								TreeMap<String, String[]> map_NumberWord_N_arr_lineNo,
								TreeMap<Integer, String[]>  map_docIDrlineNo_numbersCSV,
								TreeMap<Integer, String []> map_docIDrlineNo_arrelatedWordsForNumbersCSV,
								String  debugFile,
								boolean isSOPprint,
								String  curr_query_CSV,
								String  filter_NO_apply_for_inputFile_CSV,
								double  offset_for_TFIDF,
								int		N_preceding_docs,
								boolean is_YES_skip_edge_normalization,
								Stemmer stemmer,
								boolean is_debug_more
								) {
		
		// TODO Auto-generated method stub
		int top_N_TFIDF_only=20;
		crawler.Stemmer stemmer2=new crawler.Stemmer();
		 //PorterStemmer stemmer2=new PorterStemmer();
		TreeMap<String, Double> map_VocWord_WordID=new TreeMap<String, Double>();
		TreeMap<String, Double> map_DocIDWordID_TFIDF=new TreeMap<String, Double>();
		TreeMap<String, Double> map_NodeName_OUTNodes_Count_With_interestedEdgeWeight_one=new TreeMap<String, Double>();
		TreeMap<String, Integer> map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY=new TreeMap<String, Integer>();
		TreeMap<String,TreeMap<String, Integer>> map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY
																=new TreeMap<String,TreeMap<String, Integer>>();								
		map_VocWord_WordID=map_VocabFile_TFIDFfile.get(1);
		map_DocIDWordID_TFIDF=map_VocabFile_TFIDFfile.get(2);
 
    	// NetworkX (convert2.py) products output of graph properties such as centrality which is a dictionary. now this has to be converted to MAP. ONE such file read.
		TreeMap<String, String> map_DocID_GraphPropValue_from_NetworkX_prop1=mapOut3_networkX_mapString_property_1.get(1);
		TreeMap<String, String> map_DocID_GraphPropValue_from_NetworkX_prop2=mapOut3_networkX_mapString_property_2.get(1);
		TreeMap<String, Double> map_DocID_GraphPropValue_from_NetworkX_prop1_NORMALIZE=new TreeMap<String, Double>();
		TreeMap<String, Double> map_DocID_GraphPropValue_from_NetworkX_prop2_NORMALIZE=new TreeMap<String, Double>();
		TreeMap<String, String> map_Nodes_GT_from_NetworkX_out=map_Nodes_GT_N_NOT_GT.get(1);
		TreeMap<String, String> map_Nodes_NOT_GT_from_NetworkX_out=map_Nodes_GT_N_NOT_GT.get(2);
		String classify_method_type="7"; //{1=use one graph prop, }
		TreeMap<String, String[]> map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE=new TreeMap<String, String[]>();
		TreeMap<String, String[]> map_inFile_NodeID_N_arrOUTNodes_from_OUTNODES_FILE=new TreeMap<String, String[]>();
		TreeMap<Integer, String> map_inFile_NodeID_N_NodeName_from_OUTNODES_FILE=new TreeMap<Integer, String>();
		TreeMap<String,Integer> map_inFile_NodeeName_N_NodeID_from_OUTNODES_FILE=new TreeMap<String, Integer>();		
		//variable
		TreeMap<Double,Double> mapcurr_personWordID_TF_IDF=new TreeMap<Double, Double>();
		TreeMap<Double,Double> mapcurr_organizWordID_TF_IDF=new TreeMap<Double, Double>();
		TreeMap<Double,Double> mapcurr_locationWordID_TF_IDF=new TreeMap<Double, Double>();
		TreeMap<Double,Double> mapcurr_numbersWordID_TF_IDF=new TreeMap<Double, Double>();
		TreeMap<String, Integer> map_NodeNames_DocID_OUT_ofContext=new TreeMap<String, Integer>();
		TreeMap<Integer, Double> map_OUT_of_SUM_AVG_TF_IDF_curr_feature=new TreeMap<Integer, Double>();
		
		int predict_for_curr_DocID=0;
		FileWriter writer_debug=null;
		FileWriter writer_debug_currRUN_false_POSITIVE=null;
		FileWriter writer_debug_currRUN_false_NEGATIVE=null;
		FileWriter writer_debug_currRUN_true_POSITIVE=null;
		FileWriter writer_debug_currRUN_true_NEGATIVE=null;
		FileWriter writer_debug_currRUN_all=null;
		
		int count_number_of_verbs_FOR_curr_preceding_DocID=0;
		int count_number_of_nouns_FOR_curr_preceding_DocID=0;
		double average_on_count_number_of_verbs_FOR_curr_preceding_DocID=0;
		double average_on_count_number_of_nouns_FOR_curr_preceding_DocID=0;
		double offset_on_average_on_total_COUNT_of_numbers_N_preceding_Docs=0.0; 
		double offset_average_on_total_COUNT_of_organization_N_preceding_Docs=0.0;
		double offset_average_on_count_number_of_verbs_FOR_curr_preceding_DocID=0.0;
		String print1=""; String print2=""; String print3=""; double average_on_count_number_of_verbs_FOR_currDocID=0;
		int rule_4_a=0;int rule_4_b=0;
		int rule_4_a_AND_a1_GLOBAL=0; int rule_4_a1_AND_a2_GLOBAL=0;int rule_4_a2_AND_a3_GLOBAL=0;
		int rule_4_a1=0;int rule_4_b1=0; int rule_4_a2=0; int rule_4_b2=0; int rule_4_a3=0; int rule_4_b3=0;
		int rule_4_a_GLOBAL=0;int rule_4_b_GLOBAL=0;
		int rule_4_a1_GLOBAL=0;int rule_4_b1_GLOBAL=0; int rule_4_a2_GLOBAL=0; int rule_4_b2_GLOBAL=0; int rule_4_a3_GLOBAL=0; int rule_4_b3_GLOBAL=0;
		int rule_4_a1_AND_a3_GLOBAL=0;int rule_4_a3_AND_a2_GLOBAL=0;
		int rule_4_a_tn=0;int rule_4_b_tn=0;
		int rule_4_a1_tn=0;int rule_4_b1_tn=0; int rule_4_a2_tn=0; int rule_4_b2_tn=0; int rule_4_a3_tn=0; int rule_4_b3_tn=0;
		int total_COUNT_of_numbers_N_preceding_Docs=0;
		int total_COUNT_of_organization_N_preceding_Docs=0;
		double	average_on_total_COUNT_of_numbers_N_preceding_Docs=0.0;
		int start_docID=0;int end_docID=0;
		//
		double avg_avg_person_N_prec_DocID_TFIDF=0;
		double avg_avg_organiz_N_prec_DocID_TFIDF=0;
		double avg_avg_location_N_prec_DocID_TFIDF=0;
		double avg_avg_number_N_prec_DocID_TFIDF=0;
		
		int cond_1=0; int cond_2=0; int cond_3=0;
		int cond_11_fp=0; int cond_12_fp=0; int cond_13_fp=0; int cond_14_fp=0;
		int cond_11_tp=0; int cond_12_tp=0; int cond_13_tp=0;int cond_14_tp=0;
		int cond_11_tn=0; int cond_12_1_tn=0;int cond_12_2_tn=0;int cond_12_3_tn=0; int cond_13_tn=0;int cond_14_tn=0;
		int cond_11_fn=0; int cond_12_1_fn=0;int cond_12_2_fn=0;int cond_12_3_fn=0; int cond_13_fn=0;int cond_14_fn=0;
		boolean bool_reset_total_count_relabel_1=false;
		int total_count_relabel_1=0;
		int total_count_relabel_1_in_GT_TP=0;int total_count_relabel_1_NOT_in_GT_TP=0;
		int total_count_relabel_0_in_GT_TP=0;int total_count_relabel_0_NOT_in_GT_TP=0;
		int total_count_relabel_0=0;
		
		
		int total_count_relabel_1_verb_tfidf=0;
		int total_count_relabel_1_verb_tfidf_in_GT_TP=0;int total_count_relabel_1_verb_tfidf_NOT_in_GT_TP=0;
		
		int total_count_relabel_0_verb_tfidf=0;
		int total_count_relabel_0_verb_tfidf_in_GT_TP=0;int total_count_relabel_0_verb_tfidf_NOT_in_GT_TP=0;

		boolean b1_b2=false;
		boolean a1_a2=false;
		boolean istfidf=false;
		int count_number_of_organization_FOR_currDocID=0;
		double sum_curr_DocID_person_TopN_TFIDF=0;
		double sum_curr_DocID_organiz_TopN_TFIDF=0;
		double sum_curr_DocID_location_TopN_TFIDF=0;
		double sum_curr_DocID_numbers_TopN_TFIDF=0;
		
		double avg_number_currDocID_TFIDF=0.;
		double avg_organiz_currDocID_TFIDF=0.;
		double avg_person_currDocID_TFIDF=0.0;
		double avg_location_currDocID_TFIDF=0.0;	
		String currDocID_bodyText="";
		TreeMap<Integer,Integer> map_preceding_N_document_docIDs_AS_KEY=new TreeMap<Integer, Integer>();
		TreeMap<String,Double>	map_curr_person_WordNtfidf=new TreeMap<String, Double>();
		TreeMap<String,Double>	map_curr_organiz_WordIDNtfidf=new TreeMap<String, Double>();
		TreeMap<String,Double>	map_curr_location_WordIDNtfidf=new TreeMap<String, Double>();
		TreeMap<String,Double>	map_curr_numbers_WordIDNtfidf=new TreeMap<String, Double>();
		TreeMap<String, String>	map_focusWords_AS_key=new TreeMap<String, String>();
		
		int TOTAL_FocusWord_Count_currDocID_=0;
		int FocusWord_Count_currDocID_OFFSET=25;
		int TOTAL_FocusWord_Count_currDocID_from_WIKIPEDIA=0;
		
		
		double sum_of_FocusWord_Count_curr_DocID_=0;
		double sum_of_FocusWord_Count_N_PRECED_DocID_=0;
		
		double sum_of_FocusWord_Count_curr_DocID_FROM_WIKIPEDIA=0;
		double sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA=0;
		double avg_sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA=0;
		
	    double avg_on_sum_of_FocusWord_Count_N_PRECED_DocID_=0;
	    TreeMap<Integer, TreeMap<String,String>  > map_docID_N_FocusWord_TFIDF=new TreeMap<Integer, TreeMap<String,String>>();
	    TreeMap<Integer, TreeMap<Integer,String>  > map_docID_N_FocusWordID_TFIDF=new TreeMap<Integer, TreeMap<Integer, String>>();
	    TreeMap<Integer, TreeMap<String,String>  > map_docID_N_FocusWord_dummy=new TreeMap<Integer, TreeMap<String,String>>();
	    TreeMap<Integer,String> map_docID_N_tempbodyText=new TreeMap<Integer,String>();
	    
	    TreeMap<Integer, TreeMap<String,String>  > map_docID_N_FocusWord_TFIDF_FROM_WIKIPEDIA=new TreeMap<Integer, TreeMap<String,String>>();
	    TreeMap<Integer, TreeMap<Integer,String>  > map_docID_N_FocusWordID_TFIDF_FROM_WIKIPEDIA=new TreeMap<Integer, TreeMap<Integer, String>>();
	    TreeMap<Integer, TreeMap<String,String>  > map_docID_N_FocusWord_dummy_FROM_WIKIPEDIA=new TreeMap<Integer, TreeMap<String,String>>();
	    
	    String [] arr_filter_NO_apply_for_inputFile=filter_NO_apply_for_inputFile_CSV.split(",");
	    
		try {
			//
			map_focusWords_AS_key=Stopwords.get_FocusedWords();
			

			// 
			if(map_DocID_GraphPropValue_from_NetworkX_prop1!=null){
				//normalize
				if(map_DocID_GraphPropValue_from_NetworkX_prop1.size()>0){
					double sum=0.;
					for(String DocID:map_DocID_GraphPropValue_from_NetworkX_prop1.keySet()){
						sum=sum+Double.valueOf(map_DocID_GraphPropValue_from_NetworkX_prop1.get(DocID));
					}
					for(String DocID:map_DocID_GraphPropValue_from_NetworkX_prop1.keySet()){
						map_DocID_GraphPropValue_from_NetworkX_prop1_NORMALIZE.put(DocID,
																				   Double.valueOf( map_DocID_GraphPropValue_from_NetworkX_prop1.get(DocID))/(double)sum );
					}
				}
			}
			if(map_DocID_GraphPropValue_from_NetworkX_prop2!=null){
				//normalize
				if(map_DocID_GraphPropValue_from_NetworkX_prop2.size()>0){
					double sum=0.;
					//
					for(String DocID:map_DocID_GraphPropValue_from_NetworkX_prop2.keySet()){
						sum=sum+Double.valueOf(map_DocID_GraphPropValue_from_NetworkX_prop2.get(DocID));
					}
					//
					for(String DocID:map_DocID_GraphPropValue_from_NetworkX_prop2.keySet()){
						map_DocID_GraphPropValue_from_NetworkX_prop2_NORMALIZE.put(DocID,
																				   Double.valueOf( map_DocID_GraphPropValue_from_NetworkX_prop2.get(DocID))/(double)sum );
					}
				}
			}
			writer_debug=new FileWriter(new File(baseFolder+"debug_myAlgo.txt"));
			writer_debug.append("\n---------------------");
			
			if(mapOut3_networkX_mapString_property_1!=null){
				if(mapOut3_networkX_mapString_property_1.containsKey(1))
					writer_debug.append("\n mapOut3_networkX_mapString_property_1.size="+mapOut3_networkX_mapString_property_1.get(1).size());
			}
			else
				writer_debug.append("\n mapOut3_networkX_mapString_property_1 is NULL");
			if(mapOut3_networkX_mapString_property_2!=null){
			
				if(mapOut3_networkX_mapString_property_2.containsKey(1))
					writer_debug.append("\nmapOut3_networkX_mapString_property_2.size="+mapOut3_networkX_mapString_property_2.get(1).size());
			}
			else
				writer_debug.append("\n mapOut3_networkX_mapString_property_2 is NULL");
			if(map_VocabFile_TFIDFfile!=null)
				writer_debug.append("\nmap_VocabFile_TFIDFfile.size="+map_VocabFile_TFIDFfile.size() +"--"+map_VocabFile_TFIDFfile.get(1).size()+"--"+map_VocabFile_TFIDFfile.get(2).size());
			else
				writer_debug.append("\n map_VocabFile_TFIDFfile is NULL");
			if(map_Nodes_GT_N_NOT_GT!=null){
				if(map_Nodes_GT_N_NOT_GT.containsKey(1))
					writer_debug.append("\nmap_Nodes_GT_N_NOT_GT.size="+map_Nodes_GT_N_NOT_GT.size()+"--"+map_Nodes_GT_N_NOT_GT.get(1).size()+"--"+map_Nodes_GT_N_NOT_GT.get(2).size());
			}
			else
				writer_debug.append("\nmap_Nodes_GT_N_NOT_GT is NULL");
			if(map_lineNoNID_person_organiz_locati_numbers_nouns!=null)
				writer_debug.append("\n map_lineNoNID_person_organiz_locati_numbers.size="+map_lineNoNID_person_organiz_locati_numbers_nouns.size());
			else
				writer_debug.append("\n map_lineNoNID_person_organiz_locati_numbers is NULL");
			
			if(map_NodeName_N_DocID_groundTruthTP!=null)
				writer_debug.append("\n map_NodeName_N_DocID_groundTruthTP.size="+map_NodeName_N_DocID_groundTruthTP.size());
			else
				writer_debug.append("\n map_NodeName_N_DocID_groundTruthTP is NULL");
			
			if(map_Word_StemmedWord!=null)
				writer_debug.append("\n map_Word_StemmedWord.size="+map_Word_StemmedWord.size());
			else
				writer_debug.append("\n map_Word_StemmedWord is NULL");
			
			if(map_NodeName_N_DocID_NOTgroundTruthTP!=null)
				writer_debug.append("\n map_NodeName_N_DocID_NOTgroundTruthTP.size="+map_NodeName_N_DocID_NOTgroundTruthTP.size());
			else
				writer_debug.append("\n map_NodeName_N_DocID_NOTgroundTruthTP is NULL");
			
			if(map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_GLOBAL!=null)
				writer_debug.append("\n map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.size="+map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_GLOBAL.size());
			else
				writer_debug.append("\n map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP is NULL");
			
			if(map_NounWord_N_arr_lineNo!=null)
				writer_debug.append("\n map_NounWord_N_arr_lineNo.size="+map_NounWord_N_arr_lineNo.size());
			else
				writer_debug.append("\n map_NounWord_N_arr_lineNo is NULL");
			
			if(map_VerbWord_N_arr_lineNo!=null)
				writer_debug.append("\n map_VerbWord_N_arr_lineNo.size="+map_VerbWord_N_arr_lineNo.size());
			else
				writer_debug.append("\n map_VerbWord_N_arr_lineNo is NULL");
			
			if(map_PersonWord_N_arr_lineNo!=null)
				writer_debug.append("\n map_PersonWord_N_arr_lineNo.size="+map_PersonWord_N_arr_lineNo.size());
			else
				writer_debug.append("\n map_PersonWord_N_arr_lineNo is NULL");
			
			if(map_OrganizWord_N_arr_lineNo!=null)
				writer_debug.append("\n map_OrganizWord_N_arr_lineNo.size="+map_OrganizWord_N_arr_lineNo.size());
			else
				writer_debug.append("\n map_OrganizWord_N_arr_lineNo is NULL");
			
			if(map_LocationWord_N_arr_lineNo!=null)
				writer_debug.append("\n map_LocationWord_N_arr_lineNo.size="+map_LocationWord_N_arr_lineNo.size());
			else
				writer_debug.append("\n map_OrganizWord_N_arr_lineNo is NULL");
			
			if(map_NumberWord_N_arr_lineNo!=null)
				writer_debug.append("\n map_NumberWord_N_arr_lineNo.size="+map_NumberWord_N_arr_lineNo.size());
			else
				writer_debug.append("\n map_NumberWord_N_arr_lineNo is NULL");
			
			if(map_docIDrlineNo_arrelatedWordsForNumbersCSV!=null)
				writer_debug.append("\n map_docIDrlineNo_arrelatedWordsForNumbersCSV.size="+map_docIDrlineNo_arrelatedWordsForNumbersCSV.size());
			else
				writer_debug.append("\n map_docIDrlineNo_arrelatedWordsForNumbersCSV is NULL");
			
			if(map_docIDrlineNo_numbersCSV!=null)
				writer_debug.append("\n map_docIDrlineNo_numbersCSV.size="+map_docIDrlineNo_numbersCSV.size());
			else
				writer_debug.append("\n map_docIDrlineNo_numbersCSV is NULL");
			
			if(map_DocID_NLPtext!=null)
				writer_debug.append("\n map_DocID_NLPtext.size="+map_DocID_NLPtext.size());
			else
				writer_debug.append("\n map_DocID_NLPtext is NULL");
			
			if(map_DocID_bodytext!=null)
				writer_debug.append("\n map_DocID_bodytext.size="+map_DocID_bodytext.size());
			else
				writer_debug.append("\n map_DocID_bodytext is NULL");
			
			if(map_DocID_URL!=null)
				writer_debug.append("\n map_DocID_URL.size="+map_DocID_URL.size());
			else
				writer_debug.append("\n map_DocID_URL is NULL");
			 
			 
			//writer_debug.append("\n map_docIDrlineNo_arrelatedWordsForNumbersCSV:"+map_docIDrlineNo_arrelatedWordsForNumbersCSV);
			writer_debug.flush();
			//debug
//			for(Integer docID:map_docIDrlineNo_arrelatedWordsForNumbersCSV.keySet() ){
//				String t="";
//				for(String s:map_docIDrlineNo_arrelatedWordsForNumbersCSV.get(docID)){
//					t=t+"!!!"+s;
//				}
//				writer_debug.append( "\n "+docID+" "+t);
//				writer_debug.flush();
//			}
			 
			writer_debug.flush();
			// map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes
			if(map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes!=null){
				writer_debug.append("\n map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.size="+map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.size());
				for(int currDocID:map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.keySet()){
					String s[]=map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.get(currDocID).split("!!!");
					String [] arr_outNodes=s[2].split(",");
					int NodeID=Integer.valueOf(s[0]);
					String NodeName=s[1].replace("\"", "").split(" ")[0];
					map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.put(NodeName, arr_outNodes );
					map_inFile_NodeID_N_arrOUTNodes_from_OUTNODES_FILE.put(NodeName, arr_outNodes );
					map_inFile_NodeID_N_NodeName_from_OUTNODES_FILE.put(NodeID, NodeName );
					map_inFile_NodeeName_N_NodeID_from_OUTNODES_FILE.put(NodeName, NodeID );
				}
				
				writer_debug.append("\n map_inFile_NodeID_N_NodeName_from_OUTNODES_FILE.size="+map_inFile_NodeID_N_NodeName_from_OUTNODES_FILE.size());
				writer_debug.append("\n map_inFile_NodeeName_N_NodeID_from_OUTNODES_FILE.size="+map_inFile_NodeeName_N_NodeID_from_OUTNODES_FILE.size());
			}
			else
				writer_debug.append("\n map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes is NULL");
			
			if(map_DocID_NLPtext!=null)
				writer_debug.append("\n map_DocID_NLPtext.size="+map_DocID_NLPtext.size());
			else
				writer_debug.append("\n map_DocID_NLPtext is NULL");
			
			writer_debug.append("\n---------------------");
			
			//writer_debug.append("\n map_VocWord_WordID:"+map_VocWord_WordID);
			//writer_debug.append("\n map_DocIDWordID_TFIDF:"+map_DocIDWordID_TFIDF);
			writer_debug.flush();
			int stat_groundTruth_TP_docs_having_person_Count=0;
			int stat_NOT_groundTruth_TP_docs_having_person_Count=0;
			int stat_groundTruth_TP_docs_having_organiz_Count=0;
			int stat_NOT_groundTruth_TP_docs_having_organiz_Count=0;
			int stat_groundTruth_TP_docs_having_location_Count=0;
			int stat_NOT_groundTruth_TP_docs_having_location_Count=0;
			int stat_groundTruth_TP_docs_having_numbers_Count=0;
			int stat_NOT_groundTruth_TP_docs_having_numbers_Count=0;
			
			
			int stat_groundTruth_TP_docs_having_person_Count_TOTAL=0;
			int stat_NOT_groundTruth_TP_docs_having_person_Count_TOTAL=0;
			int stat_groundTruth_TP_docs_having_organiz_Count_TOTAL=0;
			int stat_NOT_groundTruth_TP_docs_having_organiz_Count_TOTAL=0;
			int stat_groundTruth_TP_docs_having_location_Count_TOTAL=0;
			int stat_NOT_groundTruth_TP_docs_having_location_Count_TOTAL=0;
			int stat_groundTruth_TP_docs_having_numbers_Count_TOTAL=0;
			int stat_NOT_groundTruth_TP_docs_having_numbers_Count_TOTAL=0;
			
			TreeMap<Integer,String> map_DocID_N_NodeName_bothgroundTruthTPAND_NOTgroundtruthTP_GLOBAL=new TreeMap<Integer, String>();
			
			//get reverse of map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP
			for(String currNodeName:map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_GLOBAL.keySet()){
				map_DocID_N_NodeName_bothgroundTruthTPAND_NOTgroundtruthTP_GLOBAL.put(map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_GLOBAL.get(currNodeName), 
																			   		  currNodeName);
			}
			int TP=0, TN=0, FP=0, FN=0;
			int currNodeID=-1;
			String [] arr_curr_query_CSV=curr_query_CSV.split(",");
			int cnt30=0;
			
			//map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_GLOBAL.values().remove( );
			
			System.out.println("length nofiltr,filter,: "+arr_filter_NO_apply_for_inputFile.length+" "+arr_curr_query_CSV.length);
			String new_baseFolder="";
//			TreeMap<Integer,String> map_cnt30_outFolder=new TreeMap<Integer, String>();
//			//new folder create
//			while(cnt30<arr_curr_query_CSV.length){
//				//new flder
//				
//				new_baseFolder=baseFolder+arr_curr_query_CSV[cnt30]+"/";
//				
//				map_cnt30_outFolder.put(cnt30, new_baseFolder);
//				//
//				new File(new_baseFolder).mkdir();
//			    
//			    //writer_debug.append("\n new folder name:"+new_baseFolder);
//				//writer_debug.flush();
//				cnt30++;
//			}
			
			
			cnt30=0;
			// filter out yes and NO
			while(cnt30<arr_curr_query_CSV.length){
				//set the outputFolder as per query name.
				//String curr_outFolder=map_cnt30_outFolder.get(cnt30);
 
				//
				String curr_query_to_match=arr_curr_query_CSV[cnt30];; //SPLIT on , above
				String curr_query_NO_FILTER_to_match=arr_filter_NO_apply_for_inputFile[cnt30]; //SPLIT on , above
				//
				String [] arr_curr_query_to_match=curr_query_to_match.split(" ");
				String [] arr_curr_query_NO_FILTER_to_match=curr_query_NO_FILTER_to_match.split("#");
				
				//curr Query only
				for(String NodeName:map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_GLOBAL.keySet()){
					  int currDocID= map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_GLOBAL.get(NodeName);

					  // 
					  if(map_DocID_NLPtext.containsKey(currDocID)){
						  
						  // ALL TOKENS OF CURR_QUERY IS PRESENT IN CURR_LINE OF INPUTFILE ("road accident" - both term should present)
						  boolean isPresent_all_tokens_after_split_on_space=false;
						  int cn=0; int count_present=0;
						  //
						  while(cn<arr_curr_query_to_match.length){
							  if(map_DocID_NLPtext.get(currDocID).toLowerCase().indexOf(arr_curr_query_to_match[cn].toLowerCase())>=0){
								  count_present++;
							  }
							  cn++;
						  }
						  // all tokens present
						  if(count_present==arr_curr_query_to_match.length)
							  isPresent_all_tokens_after_split_on_space=true;
						  
						  
					  // NO FILTER
					  if(filter_NO_apply_for_inputFile_CSV.length()==0){
 			  
						  // all tokens present
						  if( isPresent_all_tokens_after_split_on_space){
							  // 
							  if(map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.containsKey(curr_query_to_match)){
								   
								  TreeMap<String, Integer> temp=map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.get(curr_query_to_match);
								  temp.put(NodeName , currDocID);
								  map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.put(curr_query_to_match, temp);
								  
							  }
							  else{
								  TreeMap<String, Integer> temp=new TreeMap<String, Integer>();
								  temp.put(NodeName , currDocID);
								  map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.put(curr_query_to_match, temp);
							  } 
						  }
					  }
					  // given NO_FILTER
					  else if(filter_NO_apply_for_inputFile_CSV.length()>0){

						  //NO_FILTER CHECK
						  boolean isPresent=false;
						  cn=0;
						  //
						  while(cn<arr_curr_query_NO_FILTER_to_match.length){
							  arr_curr_query_NO_FILTER_to_match[cn]=arr_curr_query_NO_FILTER_to_match[cn].replace("dummy", "");
							  
							  if(arr_curr_query_NO_FILTER_to_match[cn].length()>0){
								  if(map_DocID_NLPtext.get(currDocID).toLowerCase().indexOf(arr_curr_query_NO_FILTER_to_match[cn].toLowerCase())>=0){
									  isPresent=true;
									  break;
								  }
							  }
							  cn++;
						  }
						  
						   
						  // NO_FILTER given and NOT present
						  if( isPresent_all_tokens_after_split_on_space && //<--all tokens preseent 
								  isPresent==false //SHOULD NOT BE PRESENT (NO FILTERE_
								  ){
							  // 
							  if(map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.containsKey(curr_query_to_match)
									  ){
								   
								  TreeMap<String, Integer> temp=map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.get(curr_query_to_match);
								  temp.put(NodeName , currDocID);
								  map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.put(curr_query_to_match, temp);
								  
							  }
							  else{
								  TreeMap<String, Integer> temp=new TreeMap<String, Integer>();
								  temp.put(NodeName , currDocID);
								  map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.put(curr_query_to_match, temp);
							  } 
						  }
						  
					  }
					  
					  
					  }
					  else{
						  writer_debug.append("\n ERROR: no doument NLPtext for currDocID->"+currDocID);
						  writer_debug.flush();
					  }
					
				}
				cnt30++;
			}
			
			TreeMap<String, Integer> map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP=new TreeMap<String, Integer>();
			TreeMap<Integer, String > map_DocID_N_NodeName_bothgroundTruthTPAND_NOTgroundtruthTP=new TreeMap<Integer, String>();
			
			int total=0; double precision=0,recall=0, F1=0;
			int total_total=0; double total_precision=0, total_recall=0, total_F1=0;
			int total_TN=0; int total_TP=0; int total_FP=0; int total_FN=0;
			double average_on_total_precision=0,average_on_total_recall=0,average_on_total_F1=0;
			TreeMap<String, Double> map_ID_N_Metric=new TreeMap<String, Double>();
			TreeMap<String,TreeMap<String, Double>> map_Query_ID_N_Metric=new TreeMap<String,TreeMap<String, Double>>();
			double TOTAL_number_of_valide_out_NODEPair_with_interes_edge_weight=0.0;
			double sum=0; double total_num_OUT_nodes=0; double total_count_groundTruth_TP_having_interested_edge_weight=0;
			double total_count_groundTruth_TP_having_OUTnode_with_interested_edge_weight=0;
			TreeMap<Integer,TreeMap<String,Double>> mapOut21=new TreeMap<Integer, TreeMap<String,Double>>();
			int denom_total_count_query_for_metric=0;
			TreeMap<String,String> map_currQuery_TNTPFNFP=new TreeMap<String, String>();
			int total_no_given_queries=map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.size();
			int curr_counter_query=0;
			TreeMap<Integer,String> map_stat_no_DocID_found_as_key=new TreeMap<Integer, String>();
			double offset_a1_b1=0.9;
			//FOR EACH OF QUERY 
			for(String curr_query: map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.keySet()){
				int total_no_documents_curr_query=map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.get(curr_query).size();
				//
				new_baseFolder=baseFolder+curr_query+"/";				
				new File(new_baseFolder).mkdir();
				
				 
				
				writer_debug.append("\n---------------------");
				
				
				writer_debug_currRUN_false_POSITIVE=new FileWriter(new File(new_baseFolder+"currRUN_false_POSITIVE.txt"));
				writer_debug_currRUN_true_POSITIVE=new FileWriter(new File(new_baseFolder+"currRUN_true_POSITIVE.txt"));
				writer_debug_currRUN_true_NEGATIVE=new FileWriter(new File(new_baseFolder+"currRUN_true_NEGATIVE.txt"));
				writer_debug_currRUN_false_NEGATIVE=new FileWriter(new File(new_baseFolder+"currRUN_false_NEGATIVE.txt"));
				writer_debug_currRUN_all=new FileWriter(new File(new_baseFolder+"currRUN_all.txt"));
				
				curr_counter_query++;
				writer_debug_currRUN_false_POSITIVE.append(" For curr_query -> "+curr_query+"\n");
				writer_debug_currRUN_false_POSITIVE.flush();
				
				writer_debug_currRUN_true_POSITIVE.append(" For curr_query -> "+curr_query+"\n");
				writer_debug_currRUN_true_POSITIVE.flush();
				
				map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP=map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.get(curr_query);
				//REVERSE map
				for(String currNodeName:map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.keySet()){
					map_DocID_N_NodeName_bothgroundTruthTPAND_NOTgroundtruthTP.put(map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.get(currNodeName) 
																				   , currNodeName);
				}
				
				writer_debug.append("\n BEFORE entering getNormalize():");
				writer_debug.flush();
				
				
				//skipping EDGE normalization
				if(is_YES_skip_edge_normalization){
				
				//EDGE WEIGHT 1 START - Find number of OUTnodes with interested edge weight(=1) and divide by total 
				{
					
					writer_debug.append("\n ---------------entering 1.0----------------");
					TOTAL_number_of_valide_out_NODEPair_with_interes_edge_weight=0;
					sum=0;
					total_num_OUT_nodes=0;
					total_count_groundTruth_TP_having_interested_edge_weight=0;
					total_count_groundTruth_TP_having_OUTnode_with_interested_edge_weight=0;
					
					map_NodeName_OUTNodes_Count_With_interestedEdgeWeight_one=new TreeMap<String, Double>();
					mapOut21=new TreeMap<Integer, TreeMap<String,Double>>();
					System.out.println("RUNNIN myAlgo..getNormalize()weight =1 ");
	//				mapOut.put(1, new Double(number_of_valide_out_NODEPair_with_interes_edge_weight) );
	//				mapOut.put(2, new Double(sum));
	//				mapOut.put(3, new Double(total_num_OUT_nodes));
					// NORMALIZE - find total NO of first-level out NODES IN map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP (OF CURR QUERY)
					// % of out nodes 
					mapOut21=getNormalize( map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE,
																  map_inFile_NodeID_N_arrOUTNodes_from_OUTNODES_FILE,
																  map_inFile_NodeeName_N_NodeID_from_OUTNODES_FILE,
																  map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP,
																  map_NodeName_N_DocID_groundTruthTP,
																  map_SS_NodePair_EdgeValue,
																  debugFile,
																  1.0, //interested_edge_weight
																  curr_query
																  );
				
					System.out.println("END RUNNIN myAlgo..getNormalize() ");

				
				TOTAL_number_of_valide_out_NODEPair_with_interes_edge_weight=Double.valueOf( mapOut21.get(1).get("1") );
				 //(less imp) SUM of weights of all nodes matches the interst edge weight
				sum=Double.valueOf( mapOut21.get(2).get("2"));
				total_num_OUT_nodes=Double.valueOf( mapOut21.get(3).get("3") );
				total_count_groundTruth_TP_having_interested_edge_weight=Double.valueOf( mapOut21.get(5).get("5") );
				total_count_groundTruth_TP_having_OUTnode_with_interested_edge_weight=mapOut21.get(6).get("6") ;
				//
				map_NodeName_OUTNodes_Count_With_interestedEdgeWeight_one=mapOut21.get(4);
				
				writer_debug.append("\n TOTAL_num_of_OUT_nodes_with_edge_weight_of_interest::"+TOTAL_number_of_valide_out_NODEPair_with_interes_edge_weight
									+" for query:"+curr_query
									+" total_count_groundTruth_TP_having_interested_edge_weight:"+total_count_groundTruth_TP_having_interested_edge_weight
									+" map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.size():"+map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.size()
									+" mapOut21->"+mapOut21);
				writer_debug.append("\ncurr_query"+curr_query
						+"\n\n (1.0)% of Gtruth TP having interested edge weight= "
						+ (total_count_groundTruth_TP_having_interested_edge_weight/(double)map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.size()) );
				writer_debug.append("\n\n (1.0)% of outnode Gtruth TP having interested edge weight w.r.t total outnodes= "
						+ (total_count_groundTruth_TP_having_OUTnode_with_interested_edge_weight/TOTAL_number_of_valide_out_NODEPair_with_interes_edge_weight) );
				
				//writer_debug.append(" \n map_NodeName_OUTNodes_Count_With_interestedEdgeWeight:"+map_NodeName_OUTNodes_Count_With_interestedEdgeWeight_one);
				writer_debug.flush();
				
				}
				
				}//if(is_YES_skip_edge_normalization){
				
				
				// 
				//writer_debug.append("\n map_VocWord_WordID(100):"+map_VocWord_WordID);
				writer_debug.flush();
				
				//EDGE WEIGHT 1 END
			double comp1=0.0; double comp2=0.0;
			int total_nodes=map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.size();
			int cntnodes=0;
			// iterate each docID (both ground truth TP and not TP)
			for(String currNodeName:map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.keySet()){
				cntnodes++;
				System.out.println("\n MAIN entering for currNodeName-> "+ currNodeName +" cntnodes:"+cntnodes+" out of total="+total_nodes
											+" curr_query:"+curr_query +" curr_counter_query="+curr_counter_query+" out of total="+total_no_given_queries
											);
				
				writer_debug.append("\n MAIN entering for currNodeName-> "+ currNodeName);
				writer_debug.flush();
				average_on_total_COUNT_of_numbers_N_preceding_Docs=0.0;
				double average_on_total_COUNT_of_organization_N_preceding_Docs=0.0;
				double average_on_total_COUNT_of_location_N_preceding_Docs=0.0;
				int total_COUNT_of_location_N_preceding_Docs=0;
				int count_number_of_numbers_FOR_currDocID= 0; int sentiment_for_curr_DocID=0;
				int count_number_of_location_FOR_currDocID= 0; 
				int count_number_of_distinct_sentiment_for_curr_DocID=0;
				int total_count_number_of_distinct_sentiment_N_preced_docs=0;
				double average_on_total_count_number_of_distinct_sentiment_N_preced_docs=0.;
				int total_neg_sentiment=0; int total_no_sentence_curr_DocID=0;
				int 	total_no_sentence_preced_N_docs=0; 
				double  ratio_neg_senti_currDocID_per_sentence=0.;
				double  ratio_neg_senti_currDocID_per_document=0.;
				double 	ratio_neg_senti_N_preced_docs_per_sentence=0.;
				double 	ratio_neg_senti_N_preced_docs_per_document=0.;
				
				String curr_DocID_nouns_withSpace_asDelimeter= "";
				
				if(currNodeName==null){
					continue;
				}
				//
				if(map_inFile_NodeeName_N_NodeID_from_OUTNODES_FILE.containsKey(currNodeName)){
					currNodeID=map_inFile_NodeeName_N_NodeID_from_OUTNODES_FILE.get(currNodeName);
				}
				else{
					writer_debug.append("\n nodeid for nodename doesnt exist: nodename:"+currNodeName);
					writer_debug.flush();
				}
				
				if(isSOPprint)
					System.out.println("------1.4-out 20> 88888888888");
				TreeMap<String, String > map_curr_SEQ_person=new TreeMap<String, String>();
				TreeMap<String, String > map_curr_SEQ_perso_TFIDF=new TreeMap<String, String>();
				TreeMap<String, String > map_curr_SEQ_organiz=new TreeMap<String, String>();
				TreeMap<Integer,TreeMap<String, Double >> map_curr_WordNtfidf_N_WordIDNtfidf=new TreeMap<Integer,TreeMap<String, Double >>();
				TreeMap<String, String > map_curr_SEQ_locat=new TreeMap<String, String >();
				TreeMap<String, String > map_curr_SEQ_locat_TFIDF=new TreeMap<String, String >();
				TreeMap<String, String > map_curr_SEQ_numbers=new TreeMap<String, String >();
				String [] map_2_arr_curr_SEQ_numbers_CLEANED=null;
				String [] map_2_arr_curr_SEQ_RelatedWords4Numbers=null;
				TreeMap<String, String > map_curr_SEQ_nouns_CSVwithSpace=new TreeMap<String, String >();
				TreeMap<String, String > map_curr_SEQ_sentiment=new TreeMap<String, String >();
				TreeMap<String, String > map_curr_SEQ_verbs_CSVwithSpace=new TreeMap<String, String >();
				TreeMap<String, String > map_curr_SEQ_numbers_TFIDF=new TreeMap<String, String >();
				// 
				int curr_DocID=map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.get(currNodeName);
				// bodyText
				currDocID_bodyText=map_DocID_bodytext.get(curr_DocID);
				//CLEANED NUMBER
				map_2_arr_curr_SEQ_numbers_CLEANED=map_docIDrlineNo_numbersCSV.get(curr_DocID);
				map_2_arr_curr_SEQ_RelatedWords4Numbers=map_docIDrlineNo_arrelatedWordsForNumbersCSV.get(curr_DocID);
				
				TreeMap<String, Double> map_currDocsNUMBER=new TreeMap<String, Double>();
				TreeMap<String, Double> map_currDocsMONEY=new TreeMap<String, Double>();
				TreeMap<String, Double> map_currDocsYEAR=new TreeMap<String, Double>();
				// convert String array to Integer array (remove money part)
				TreeMap<Integer,TreeMap<String, Double>> mapTemp=convert_arrString_to_arrInteger_only_remove_money(map_2_arr_curr_SEQ_numbers_CLEANED, 
																												   writer_debug);
				//
				map_currDocsNUMBER=mapTemp.get(1);   // NUMBER
				map_currDocsMONEY =mapTemp.get(2);   // MONEY
				map_currDocsYEAR  =mapTemp.get(3);   // YEAR 
				
				
				if(is_debug_more){
					writer_debug.append("\nmapTemp:"+mapTemp);
					writer_debug.append("\nmapTemp.num:"+map_currDocsNUMBER);
					writer_debug.append("\nmapTemp.money:"+map_currDocsMONEY);
					writer_debug.append("\nmapTemp.year:"+map_currDocsYEAR);
				}
				writer_debug.flush();
				 
				//System.out.println("***** curr DocID:"+curr_DocID);
				
				/// might be blank line (mismatch between two files - read prerequistion of main() method)
				if( !map_lineNoNID_person_organiz_locati_numbers_nouns.containsKey(curr_DocID+":2") &&
					!map_lineNoNID_person_organiz_locati_numbers_nouns.containsKey(curr_DocID+":1") &&
					!map_lineNoNID_person_organiz_locati_numbers_nouns.containsKey(curr_DocID+":3") &&
					!map_lineNoNID_person_organiz_locati_numbers_nouns.containsKey(curr_DocID+":4")){
					continue;
				}
				String currURL=map_DocID_URL.get(curr_DocID);
				// 
				if(map_inFile_seqID_N_URL_outOFcontext.containsValue(currURL)){
					writer_debug.append(" out of context DOCID: "+curr_DocID+" "+currURL+"\n");
					writer_debug.flush();
					map_NodeNames_DocID_OUT_ofContext.put( currNodeName, curr_DocID);	 
					continue;
				}
				// 
				map_curr_SEQ_person=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":1");
				map_curr_SEQ_organiz=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":2");
				map_curr_SEQ_locat=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":3");
				//ths number below further cleaned and available in 
				map_curr_SEQ_numbers=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":4");

				//
				map_curr_SEQ_nouns_CSVwithSpace=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":5");
				map_curr_SEQ_sentiment=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":6");
				map_curr_SEQ_verbs_CSVwithSpace=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":7");
				//System.out.println("RUNNING wrapper_2_get_given_DocID_TopN_TFIDF_for_person_organiz_locatio_numbers....");
				
				double  sum_TF_IDF_on_verbs_FOR_currDocID=0.0;
				double avg_on_sum_TF_IDF_on_verbs_FOR_currDocID=0.; 
				double max_TF_IDF_on_verbs_FOR_currDocID=0;
				double max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs=0;
				int denom_count_number_of_verb=0;
				System.out.println("\n BEGIN AVERAGE OF tfidf for VERB (CURR DOCID)"); 
				// BEGIN AVERAGE OF tfidf for VERB (CURR DOCID)
				String[] arr_verb=map_curr_SEQ_verbs_CSVwithSpace.get("1").split(" ");
				int c20=0;
				// 
				while(c20<arr_verb.length){
					String word=arr_verb[c20];
					
					double wordID=-1.;
					String stem_word=stemmer.stem(word);
					if(map_VocWord_WordID.containsKey(word))
						wordID=map_VocWord_WordID.get(word);
					
					// System.out.println(" word:"+word+" stem:"+stem_word);
					if(wordID<0){
						if(map_VocWord_WordID.containsKey(stem_word)){
							wordID=map_VocWord_WordID.get(stem_word);
						}
					}
					// 	
					if(wordID>0){
						//int convert
						int wordID_int= (int) wordID; //BEGIN AVERAGE OF tfidf for VERB (CURR DOCID)
						if(map_DocIDWordID_TFIDF.get(curr_DocID +":" +  wordID_int )!=null){
							//sum TFIDF 
							sum_TF_IDF_on_verbs_FOR_currDocID=sum_TF_IDF_on_verbs_FOR_currDocID+
															      Double.valueOf(map_DocIDWordID_TFIDF.get(curr_DocID +":" +  wordID_int ));
							denom_count_number_of_verb++;
							//max value
							if(max_TF_IDF_on_verbs_FOR_currDocID==0)
								max_TF_IDF_on_verbs_FOR_currDocID=Double.valueOf(map_DocIDWordID_TFIDF.get(curr_DocID +":" +  wordID_int ) );
							else if(Double.valueOf(map_DocIDWordID_TFIDF.get(curr_DocID +":" +  wordID_int ) )>max_TF_IDF_on_verbs_FOR_currDocID  )
								max_TF_IDF_on_verbs_FOR_currDocID=Double.valueOf(map_DocIDWordID_TFIDF.get(curr_DocID +":" +  wordID_int ) );
								
							
						//
						}
					}
					c20++;
				}
				//VERB (AVERAGE)
				avg_on_sum_TF_IDF_on_verbs_FOR_currDocID=sum_TF_IDF_on_verbs_FOR_currDocID/(double)denom_count_number_of_verb;
 
				
				//System.out.println("\n wrapper person organi (CURR DOC ID ) ");
				 
				// wrapper_2_get_given_DocID_TopN_TFIDF_for_person_organiz_locatio_numbers
				// mapOut.get(1) -> person,
				// mapOut.get(11).get("1") -> person average of sum of TF-IDF
				// mapOut.get(2) -> organization
				// mapOut.get(22).get("2") -> organiz average of sum of TF-IDF
				// mapOut.get(3) -> location
				// mapOut.get(33).get("3") -> location average of sum of TF-IDF
				// mapOut.get(4) -> number
				// mapOut.get(44).get("4") -> number average of sum of TF-IDF
				TreeMap<Integer,TreeMap<String, Double >> mapOUT_topN_=
												wrapper_2_get_given_DocID_TopN_TFIDF_for_person_organiz_locatio_numbers(
																								curr_DocID,
																								map_curr_SEQ_person,
																								map_curr_SEQ_locat,
																								map_curr_SEQ_organiz,
																								map_curr_SEQ_numbers,
																								map_VocWord_WordID,
																								map_DocIDWordID_TFIDF,
																								top_N_TFIDF_only,
																								offset_for_TFIDF,
																								map_Word_StemmedWord,	
																								stemmer2,
																								debugFile,
																								is_debug_more
																						);
				//System.out.println("END RUNNING wrapper_2_get_given_DocID_TopN_TFIDF_for_person_organiz_locatio_numbers....");
				
				//person
				map_curr_person_WordNtfidf=mapOUT_topN_.get(1);
				if(mapOUT_topN_.get(11)!=null){
					avg_person_currDocID_TFIDF=mapOUT_topN_.get(11).get("1");
				}
				else
					avg_person_currDocID_TFIDF=0.;
				//organi
				map_curr_organiz_WordIDNtfidf=mapOUT_topN_.get(2);
				if(mapOUT_topN_.get(22)!=null){
					avg_organiz_currDocID_TFIDF=mapOUT_topN_.get(22).get("2");
				}
				else
					avg_organiz_currDocID_TFIDF=0.;
				//location
				map_curr_location_WordIDNtfidf=mapOUT_topN_.get(3);
				if(mapOUT_topN_.get(33)!=null){
					avg_location_currDocID_TFIDF=mapOUT_topN_.get(33).get("3");
				}
				else{
					avg_location_currDocID_TFIDF=0;
				}
				//numbers
				map_curr_numbers_WordIDNtfidf=mapOUT_topN_.get(4);
				if(mapOUT_topN_.get(44)!=null)
					avg_number_currDocID_TFIDF=mapOUT_topN_.get(44).get("4");
				else
					avg_number_currDocID_TFIDF=0.0;
				// 
				
				//nouns
				if(map_curr_SEQ_nouns_CSVwithSpace!=null){
					curr_DocID_nouns_withSpace_asDelimeter=map_curr_SEQ_nouns_CSVwithSpace.get("1");
				}
				
				map_preceding_N_document_docIDs_AS_KEY=new TreeMap<Integer, Integer>();
				//int N_preceding_docs=25;
				// get preceding N document DocIDs
				start_docID=curr_DocID-N_preceding_docs;
				end_docID=curr_DocID+N_preceding_docs;
				// get N docIDs
					while(start_docID<=end_docID){
						if(map_DocID_N_NodeName_bothgroundTruthTPAND_NOTgroundtruthTP.containsKey(start_docID)
								&& map_DocID_N_NodeName_bothgroundTruthTPAND_NOTgroundtruthTP.containsKey(start_docID) //only DocID from curr Query
								&& !map_NodeNames_DocID_OUT_ofContext.containsValue( start_docID) //OUT OF CONTEXT
								){
							map_preceding_N_document_docIDs_AS_KEY.put(start_docID, -1);
						}
						if(map_preceding_N_document_docIDs_AS_KEY.size()>=N_preceding_docs) break;
						start_docID++;
					}
					
				

				// BEGIN BELOW FOR 5" AND "6" (below SLOW*** PROCESS) <---N PRECED
				if(classify_method_type.equalsIgnoreCase("5") ||
							classify_method_type.equalsIgnoreCase("6")){
				 //RESETTING
					avg_avg_person_N_prec_DocID_TFIDF=0;
					avg_avg_organiz_N_prec_DocID_TFIDF=0;
					avg_avg_location_N_prec_DocID_TFIDF=0;
					avg_avg_number_N_prec_DocID_TFIDF=0;
					 
				//System.out.println("\n PRECEDING DOC ID - Find  sum and average of top N TF-IDF of each doc of preceding docID.");
			    // PRECEDING DOC ID - Find  sum and average of top N TF-IDF of each doc of preceding docID.  
				for(int curr_preced_DocID:map_preceding_N_document_docIDs_AS_KEY.keySet()){
						
					map_curr_SEQ_person=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preced_DocID+":1");
					map_curr_SEQ_organiz=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preced_DocID+":2");
					map_curr_SEQ_locat=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preced_DocID+":3");
					map_curr_SEQ_numbers=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preced_DocID+":4");
					map_curr_SEQ_nouns_CSVwithSpace=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preced_DocID+":5");
					map_curr_SEQ_sentiment=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preced_DocID+":6");
					map_curr_SEQ_verbs_CSVwithSpace=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preced_DocID+":7");					
					 
 
					// wrapper_2_get_given_DocID_TopN_TFIDF_for_person_organiz_locatio_numbers
					// mapOut.get(1) -> person,
					// mapOut.get(11).get("1") -> person average of sum of TF-IDF
					// mapOut.get(2) -> organization
					// mapOut.get(22).get("2") -> organiz average of sum of TF-IDF
					// mapOut.get(3) -> location
					// mapOut.get(33).get("3") -> location average of sum of TF-IDF
					// mapOut.get(4) -> number
					// mapOut.get(44).get("4") -> number average of sum of TF-IDF
					 
						mapOUT_topN_=wrapper_2_get_given_DocID_TopN_TFIDF_for_person_organiz_locatio_numbers(
																							    curr_preced_DocID,
																								map_curr_SEQ_person,
																								map_curr_SEQ_locat,
																								map_curr_SEQ_organiz,
																								map_curr_SEQ_numbers,
																								map_VocWord_WordID,
																								map_DocIDWordID_TFIDF,
																								top_N_TFIDF_only,
																								offset_for_TFIDF,
																								map_Word_StemmedWord,
																								stemmer2,
																								debugFile,
																								is_debug_more
																								);
						
						//person
						map_curr_person_WordNtfidf=mapOUT_topN_.get(1);
						if(mapOUT_topN_.get(11)!=null){
							avg_person_currDocID_TFIDF=avg_person_currDocID_TFIDF+mapOUT_topN_.get(11).get("1");
						} 
					 
					//organi
					map_curr_organiz_WordIDNtfidf=mapOUT_topN_.get(2);
					if(mapOUT_topN_.get(22)!=null){
						avg_organiz_currDocID_TFIDF=avg_organiz_currDocID_TFIDF+mapOUT_topN_.get(22).get("2");
					}
					 
					//location
					map_curr_location_WordIDNtfidf=mapOUT_topN_.get(3);
					if(mapOUT_topN_.get(33)!=null){
						avg_location_currDocID_TFIDF=avg_location_currDocID_TFIDF+mapOUT_topN_.get(33).get("3");
					}
					 
					//numbers
					map_curr_numbers_WordIDNtfidf=mapOUT_topN_.get(4);
					if(mapOUT_topN_.get(44)!=null)
						avg_number_currDocID_TFIDF=avg_number_currDocID_TFIDF+mapOUT_topN_.get(44).get("4");
					
				}
				//END OF N PREC 
				//Averaging
				avg_avg_person_N_prec_DocID_TFIDF=avg_person_currDocID_TFIDF / (double)map_preceding_N_document_docIDs_AS_KEY.size();
				avg_avg_organiz_N_prec_DocID_TFIDF=avg_organiz_currDocID_TFIDF/ (double)map_preceding_N_document_docIDs_AS_KEY.size();
				avg_avg_location_N_prec_DocID_TFIDF=avg_location_currDocID_TFIDF/ (double)map_preceding_N_document_docIDs_AS_KEY.size();
				avg_avg_number_N_prec_DocID_TFIDF=avg_number_currDocID_TFIDF/ (double)map_preceding_N_document_docIDs_AS_KEY.size();
				
				} //if(classify_method_type.equalsIgnoreCase("5") ||classify_method_type.equalsIgnoreCase("6")){
				
				// END ABOVE FOR 5" AND "6"
				
				TOTAL_FocusWord_Count_currDocID_=0;
				// BELOW ( CURR DOCUMENT ID)
				TreeMap<String, String> map_Temp=new TreeMap<String, String>();
				TreeMap<String, String> map_Temp2=new TreeMap<String, String>();
				TreeMap<Integer, String> map_Temp3=new TreeMap<Integer, String>();
//				// BEGIN : FOCUS WORD ( CURR DOCUMENT ID)
//				for(String word:map_focusWords_AS_key.keySet()){
//					String orig_word=word;
//					//remove extra space
//					word=word.replace(" ", " ");
//					
//					// stop words
//					if(stopwords.is_stopword(word)){ continue; };
//					
//					// 
//					if(currDocID_bodyText.toLowerCase().indexOf(word.toLowerCase())>=0 ){
//						// no tfidf (1)
//						map_Temp.put(word, "");
//						//|| currDocID_bodyText.toLowerCase().indexOf(map_focusWords_AS_key.get(word).toLowerCase())>=0 
//						double wordID=-1.;
//						String stem_word=stemmer.stem(word);
//						if(map_VocWord_WordID.containsKey(word))
//							wordID=map_VocWord_WordID.get(word);
//						
//						// System.out.println(" word:"+word+" stem:"+stem_word);
//						if(wordID<0){
//							if(map_VocWord_WordID.containsKey(stem_word)){
//								wordID=map_VocWord_WordID.get(stem_word);
//							}
//						}
//							
//						if(wordID>0){ // FOCUS WORD ( CURR DOCUMENT ID)
//							//int convert
//							int wordID_int= (int) wordID;
//							//wordid (2)
//							map_Temp3.put( wordID_int, String.valueOf(map_DocIDWordID_TFIDF.get(curr_DocID +":" +  wordID_int )) );
//							//word (3)
//							map_Temp2.put(word, String.valueOf(map_DocIDWordID_TFIDF.get(curr_DocID +":" + wordID_int )) );
//							//
//							
//						}
//						//sum_of_FocusWord_Count_N_PRECED_DocID_=sum_of_FocusWord_Count_N_PRECED_DocID_+1;
//						TOTAL_FocusWord_Count_currDocID_++; //TOTAL
//						//
//						map_docID_N_tempbodyText.put(curr_DocID, currDocID_bodyText);
//					}
//				}
//				//GLOBAL
//				map_docID_N_FocusWord_TFIDF.put(curr_DocID, map_Temp2 );
//				map_docID_N_FocusWordID_TFIDF.put(curr_DocID, map_Temp3);
//				map_docID_N_FocusWord_dummy.put(curr_DocID, map_Temp);
				
				
				
				// BEGIN : WIKIPEDIA FOCUS WORD ( CURR DOCUMENT ID)
				TOTAL_FocusWord_Count_currDocID_=0;
				sum_of_FocusWord_Count_curr_DocID_FROM_WIKIPEDIA=0;
				map_Temp=new TreeMap<String, String>();
				map_Temp2=new TreeMap<String, String>();
				map_Temp3=new TreeMap<Integer, String>();
				
				String[] arr_currDocID_word=currDocID_bodyText.toLowerCase().split(" ") ;
				int cnt300=0;
				//System.out.println("\n BEGIN : WIKIPEDIA FOCUS WORD ( CURR DOCUMENT ID) "+arr_currDocID_word.length);
				
				//each word
				while(cnt300<arr_currDocID_word.length){
					//System.out.println("cnt300:"+cnt300);
					String word =arr_currDocID_word[cnt300];
					String stem_word= stemmer.stem(word);
					
					if(Stopwords.is_stopword(word)){cnt300++; continue; };
					
					// 
					if( (  map_Word_StemmedWord_WIKIPEDIA.containsKey(word)
						|| map_Word_StemmedWord_WIKIPEDIA.containsValue(stem_word))
						&& !IsNumeric.isNumeric(word) //NOT NUMERIC
						){
						map_Temp.put(stem_word , "");
						sum_of_FocusWord_Count_curr_DocID_FROM_WIKIPEDIA++;
						TOTAL_FocusWord_Count_currDocID_from_WIKIPEDIA++;
					}
					
					cnt300++;
				}
				//get and sum TF-IDF
 
	 
				//GLOBAL
				map_docID_N_FocusWord_dummy_FROM_WIKIPEDIA.put(curr_DocID, map_Temp);
				//map_docID_N_FocusWord_TFIDF.put(curr_DocID, map_Temp2 );
				//map_docID_N_FocusWordID_TFIDF.put(curr_DocID, map_Temp3);
				
				// END : WIKIPEDIA FOCUS WORD ( CURR DOCUMENT ID)
				
				//  
				writer_debug.append("\n DocID:"+map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.get(currNodeName)+" currNodeName:"+currNodeName 
										//+" person:"+map_lineNoNID_person_organiz_locati_numbers.get(curr_DocID+":1")
										//+" number:"+map_lineNoNID_person_organiz_locati_numbers.get(curr_DocID+":4")
//										+" map_curr_organiz_WordIDNtfidf:"+map_curr_organiz_WordIDNtfidf.size()
//										+" map_curr_person_WordNtfidf:"+map_curr_person_WordNtfidf.size()
//										+" map_curr_location_WordIDNtfidf:"+map_curr_location_WordIDNtfidf.size()
//										+" map_curr_numbers_WordIDNtfidf:"+map_curr_numbers_WordIDNtfidf.size()
										+" map_preceding_N_document_docIDs_AS_KEY.size:"+map_preceding_N_document_docIDs_AS_KEY.size()
										+" "
										);
				writer_debug.flush();	
				// TF-IDF of focus words
				double total_graph_prop1_value_N_RANDOM=0.0;
				double total_graph_prop2_value_N_RANDOM=0.0;
				int    total_num_OUT_nodes_N_RANDOM=0;
				double avg_total_graph_prop1_value_N_RANDOM=0.0;
				double avg_total_graph_prop2_value_N_RANDOM=0.0;
				double avg_total_num_OUT_nodes_RANDOM=0.0;
				double curr_DocID_graph_prop_value_prop1=0; //reset
				double curr_DocID_graph_prop_value_prop2=0; //reset
				predict_for_curr_DocID=-1; //resetting
				double graph_curr_docID_value_prop1=0.0;
				double graph_curr_docID_value_prop2=0.0;
				int    graph_curr_docID_OUTNodes_size=0;
				int    graph_RANDOM_docID_OUTNodes_size=0;
				String curr_DocID_graph_curr_docID_OUTNodes_CSVstring="";
				String curr_NodeName_FROM_docID_N_OUTNodes_File="";
				// 
				//if(map_DocID_GraphPropValue_from_NetworkX_prop1!=null){
				// predict -- classify_method_type={"1","2"} uses this
				//if(map_DocID_GraphPropValue_from_NetworkX_prop1.containsKey( String.valueOf(curr_DocID -1)) ){
				
					if(map_DocID_GraphPropValue_from_NetworkX_prop1!=null){
						// Graph property 1
						if(map_DocID_GraphPropValue_from_NetworkX_prop1.containsKey(String.valueOf(curr_DocID -1) ))
							curr_DocID_graph_prop_value_prop1=Double.valueOf(map_DocID_GraphPropValue_from_NetworkX_prop1.get(String.valueOf(curr_DocID -1)));
					}
					if(map_DocID_GraphPropValue_from_NetworkX_prop2!=null){
						// Graph property 2
						if(map_DocID_GraphPropValue_from_NetworkX_prop2.containsKey(String.valueOf(curr_DocID -1) ))
							curr_DocID_graph_prop_value_prop2=Double.valueOf(map_DocID_GraphPropValue_from_NetworkX_prop2.get(String.valueOf(curr_DocID -1)));
					}
					if(map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes!=null)
						// OUT nodes
						if(map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.containsKey(curr_DocID)){
							String []s=map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.get(curr_DocID).split("!!!");
							curr_DocID_graph_curr_docID_OUTNodes_CSVstring=s[2];
							curr_NodeName_FROM_docID_N_OUTNodes_File=s[1];
							graph_curr_docID_OUTNodes_size=curr_DocID_graph_curr_docID_OUTNodes_CSVstring.split(",").length;
						
						}
					
					writer_debug.append("doc_ID:"+curr_DocID+"<-->graph props:"+curr_DocID_graph_prop_value_prop1+" "+curr_DocID_graph_prop_value_prop2 +"\n");
					writer_debug.flush();
					
					// for MOST methods
					if( classify_method_type.equalsIgnoreCase("1") || classify_method_type.equalsIgnoreCase("2")
						|| classify_method_type.equalsIgnoreCase("5") || classify_method_type.equalsIgnoreCase("22") ){
						int cnt=0;
						//
						while(cnt<=2){ //randomly pick 7 root nodes of article and get average
							
							int random_DocID=Generate_Random_Number_Range.generate_Random_Number_Range(0, map_DocID_N_NodeName_bothgroundTruthTPAND_NOTgroundtruthTP.size()-1 );
							//System.out.println("1."+map_DocID_GraphPropValue_from_NetworkX_prop1.size());
							//System.out.println("11."+map_DocID_GraphPropValue_from_NetworkX_prop1.containsKey(String.valueOf(random_DocID)) );
							try{
								if(map_DocID_GraphPropValue_from_NetworkX_prop1!=null){
									if(map_DocID_GraphPropValue_from_NetworkX_prop1.containsKey(String.valueOf(random_DocID)) 
										&& !map_NodeNames_DocID_OUT_ofContext.containsValue( random_DocID) //OUT OF CONTEXT
											){
										//System.out.println("1.1 :"+String.valueOf(random_DocID));
										graph_curr_docID_value_prop1=Double.valueOf(map_DocID_GraphPropValue_from_NetworkX_prop1.get(String.valueOf(random_DocID) ));
									}
								}
								//System.out.println("2.");	
								if(map_DocID_GraphPropValue_from_NetworkX_prop2!=null){
									if(map_DocID_GraphPropValue_from_NetworkX_prop2.containsKey(String.valueOf(random_DocID))
											&& !map_NodeNames_DocID_OUT_ofContext.containsValue( random_DocID) //OUT OF CONTEXT
											){
										graph_curr_docID_value_prop2=Double.valueOf(map_DocID_GraphPropValue_from_NetworkX_prop2.get(String.valueOf(random_DocID) ));
									}
								}
								//System.out.println("3.");
								 
								// outNODE
								if(map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.containsKey(random_DocID)){
									//System.out.println("3.1");	
									graph_RANDOM_docID_OUTNodes_size=map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.get(random_DocID).split(",").length ;
								}
								else{
									writer_debug.append("\n (only for method 3) error on random docID:"+random_DocID);
									writer_debug.flush();	
								}
								//System.out.println("4.");
							}
							catch(Exception e){
								System.out.println("error on random docID:"+random_DocID+" "+e.getMessage());
								writer_debug.append("\n error on random docID:"+random_DocID);
								writer_debug.flush();
								continue;
							}
							
							if(random_DocID!=(curr_DocID-1)){
								//System.out.println("got random:"+graph_docID_value_);
								total_graph_prop1_value_N_RANDOM=total_graph_prop1_value_N_RANDOM + graph_curr_docID_value_prop1;
								total_graph_prop2_value_N_RANDOM=total_graph_prop2_value_N_RANDOM + graph_curr_docID_value_prop2;
								total_num_OUT_nodes_N_RANDOM	   =total_num_OUT_nodes_N_RANDOM  + graph_RANDOM_docID_OUTNodes_size;
								cnt++;
								
							}
						}
						//
						avg_total_graph_prop1_value_N_RANDOM=total_graph_prop1_value_N_RANDOM/(double) cnt;
						avg_total_graph_prop2_value_N_RANDOM=total_graph_prop2_value_N_RANDOM/(double) cnt;
						avg_total_num_OUT_nodes_RANDOM=total_num_OUT_nodes_N_RANDOM/(double) cnt;
				   }
				
					// classification method 1 (uses only 1 graph property)
					if(classify_method_type.equalsIgnoreCase("1")){
						writer_debug.append("\n entering 1 for docId-> "+ curr_DocID);
						writer_debug.flush();
						if(curr_DocID_graph_prop_value_prop1 > 1 * avg_total_graph_prop1_value_N_RANDOM)
							predict_for_curr_DocID=1;
						else
							predict_for_curr_DocID=0;
					}
					else if(classify_method_type.equalsIgnoreCase("2")){ //use 2 graph prop
						writer_debug.append("\n entering 2 for docId-> "+ curr_DocID);
						writer_debug.flush();
						if(curr_DocID_graph_prop_value_prop1+ curr_DocID_graph_prop_value_prop2 > 1 * avg_total_graph_prop2_value_N_RANDOM  )
							predict_for_curr_DocID=1;
						else
							predict_for_curr_DocID=0;
						
					}
					else if(classify_method_type.equalsIgnoreCase("22")){ //compare curr doc OUTnode Size count < Random N nodes
						if( graph_curr_docID_OUTNodes_size < 1 * avg_total_num_OUT_nodes_RANDOM )
							predict_for_curr_DocID=1;
						else
							predict_for_curr_DocID=0;	
					}
					// (1) 
					else if(classify_method_type.equalsIgnoreCase("3")){
						writer_debug.append("\n entering 3 for docId-> "+ curr_DocID);
						writer_debug.flush();
						//iterate first and second level.
						double  curr_DocID_avg_OUTnodes_graph_prop1_values=0.;
						double  OUTnodes_of_currNode_total_graph_prop1_value=0.0;
						boolean is_first_level_only=false;
						String  Flag="graphprop"; // {graphprop, OutNodesCountSize }
						
						//nodeID!!!nodeIDName!!!Corresp_OUTnodes!!!Corresp_INnodes!!!Corresp_OUTnodesNames!!!Corresp_INnodesNames <-header of each line
						//get all the out node
						 
						//String curr_DocID_eachLine_OF_OUTnodes_INnodes=map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.get(curr_DocID);
						if(isSOPprint)
							System.out.println("\n curr_DocID_graph_curr_docID_OUTNodes_CSVstring:"+curr_DocID_graph_curr_docID_OUTNodes_CSVstring);
						// 
						if(map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.containsKey(currNodeName)==false){
							writer_debug.append("\n error:(1) NOT found OUTnodes for NodeName(DocID->"+currNodeName);
							writer_debug.flush();
							continue;
						}
						
						//System.out.println("map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE:"+map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE);
						String [] arr_Corresp_OUTnodes=map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.get(currNodeName);
						
						String [] arr_Corresp_OUTnodes_SECOND_level=null;
						int size_arr_Corresp_OUTnodes_FIRST_n_SECOND=arr_Corresp_OUTnodes.length; //first level
						int cnt=0;double tm=0.0; int cnt_hit_used=0;
						String curr_node_pair_edge_weight_first_level="";
						
						// step 1: currDocID OUTnodes  (FIRST level)
						while(cnt<arr_Corresp_OUTnodes.length){
							if(isSOPprint)
								System.out.println("arr_Corresp_OUTnodes[cnt]:"+arr_Corresp_OUTnodes[cnt]);
							if(arr_Corresp_OUTnodes[cnt].length()==0) {cnt++; continue;}
							
							if(currNodeID>=0 && String.valueOf(currNodeID) !=null&&map_SS_NodePair_EdgeValue!=null){
							
								// edge weight
								if( map_SS_NodePair_EdgeValue.containsKey( currNodeID+":"+arr_Corresp_OUTnodes[cnt]  ) ||
										map_SS_NodePair_EdgeValue.containsKey( arr_Corresp_OUTnodes[cnt] +":"+currNodeID )
										){
									if(map_SS_NodePair_EdgeValue.containsKey( currNodeID+":"+arr_Corresp_OUTnodes[cnt]) )
										curr_node_pair_edge_weight_first_level=map_SS_NodePair_EdgeValue.get(currNodeID+":"+arr_Corresp_OUTnodes[cnt]);
									if(map_SS_NodePair_EdgeValue.containsKey( arr_Corresp_OUTnodes[cnt]+ ":"+currNodeID) )
										curr_node_pair_edge_weight_first_level=map_SS_NodePair_EdgeValue.get(arr_Corresp_OUTnodes[cnt] +":"+currNodeID);
								
									writer_debug.append("\n edge weight:"+curr_node_pair_edge_weight_first_level
														+" for "+currNodeID+":"+arr_Corresp_OUTnodes[cnt]);
									writer_debug.flush();
								}
							}
							
							String cur_NodeName=map_inFile_NodeID_N_NodeName_from_OUTNODES_FILE.get(Integer.valueOf(arr_Corresp_OUTnodes[cnt]));
							
							if(cur_NodeName==null){
								writer_debug.append("\n error: cur_NodeName cant be null . null for out nodeID ->"+arr_Corresp_OUTnodes[cnt] +"(first level) ");
								writer_debug.flush();
								cnt++; continue;
							}
							
							if(map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE==null)
								System.out.println(" map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE is coming null *******************");
							
							//System.out.println("cur_NodeName:"+cur_NodeName
									           //+" is?:"+map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE
		//							           );
							//get (SECOND level) OUTnodes of curr OutNode 
							if(map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.containsKey( cur_NodeName )){
								arr_Corresp_OUTnodes_SECOND_level=map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.get(cur_NodeName);
							}
							else{
								cnt++;
								System.out.println(" coninuing ..");
								continue;
							}
							
							//FIRST level
							if(Flag.equalsIgnoreCase("graphprop")){
								if(map_DocID_GraphPropValue_from_NetworkX_prop1!=null)

									if(map_DocID_GraphPropValue_from_NetworkX_prop1.containsKey(arr_Corresp_OUTnodes[cnt]))
										tm=Double.valueOf(map_DocID_GraphPropValue_from_NetworkX_prop1.get(arr_Corresp_OUTnodes[cnt]));
							}
							else if(Flag.equalsIgnoreCase("OutNodesCountSize")){
									tm=Double.valueOf(map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.get(cur_NodeName).length );
							}
							
							//aggregate
							OUTnodes_of_currNode_total_graph_prop1_value=OUTnodes_of_currNode_total_graph_prop1_value+tm;
							
							//SECOND LEVEL
							if(is_first_level_only==false){
								// first n Second size together
								size_arr_Corresp_OUTnodes_FIRST_n_SECOND=size_arr_Corresp_OUTnodes_FIRST_n_SECOND+arr_Corresp_OUTnodes_SECOND_level.length;
								//
								int cnt20=0; int cnt20_used_hit=0;
								// -(SECOND level) -> each of OUTnode of OUT node processing..
								while(cnt20 <  arr_Corresp_OUTnodes_SECOND_level.length ){
									String curr_NodeName_secLevel="";
									curr_NodeName_secLevel=map_inFile_NodeID_N_NodeName_from_OUTNODES_FILE.get(Integer.valueOf(arr_Corresp_OUTnodes_SECOND_level[cnt20]));
									//edge weight
									if(currNodeID>=0 && String.valueOf(currNodeID) !=null
										&&	map_SS_NodePair_EdgeValue !=null
											){
										
										// edge weight (second level
										if( map_SS_NodePair_EdgeValue.containsKey( currNodeID+":"+arr_Corresp_OUTnodes_SECOND_level[cnt20]  ) ||
												map_SS_NodePair_EdgeValue.containsKey( arr_Corresp_OUTnodes_SECOND_level[cnt20] +":"+currNodeID )
												){
											if(map_SS_NodePair_EdgeValue.containsKey( currNodeID+":"+arr_Corresp_OUTnodes_SECOND_level[cnt20]) )
												curr_node_pair_edge_weight_first_level=map_SS_NodePair_EdgeValue.get(currNodeID+":"+arr_Corresp_OUTnodes_SECOND_level[cnt20]);
											if(map_SS_NodePair_EdgeValue.containsKey( arr_Corresp_OUTnodes_SECOND_level[cnt20] +":"+ currNodeID) )
												curr_node_pair_edge_weight_first_level=map_SS_NodePair_EdgeValue.get(arr_Corresp_OUTnodes_SECOND_level[cnt20] +":"+currNodeID);
										
											writer_debug.append("\n edge weight(2nd):"+curr_node_pair_edge_weight_first_level
																+" for "+currNodeID+":"+arr_Corresp_OUTnodes_SECOND_level[cnt20]);
											writer_debug.flush();
										}
									}
									 
									if(Flag.equalsIgnoreCase("graphprop")){
										//takes value from graph property
										if(map_DocID_GraphPropValue_from_NetworkX_prop1!=null)
											if(map_DocID_GraphPropValue_from_NetworkX_prop1.containsKey(arr_Corresp_OUTnodes_SECOND_level[cnt20]))
												tm=Double.valueOf(map_DocID_GraphPropValue_from_NetworkX_prop1.get(arr_Corresp_OUTnodes_SECOND_level[cnt20]));
									}
									else if(Flag.equalsIgnoreCase("OutNodesCountSize")){
										
										if(curr_NodeName_secLevel==null){
											writer_debug.append("\n nodeName:"+curr_NodeName_secLevel+" for nodeID:"+arr_Corresp_OUTnodes_SECOND_level[cnt20]);
											writer_debug.flush();
											cnt20++;
											continue;
										}
										
										if(map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.containsKey(curr_NodeName_secLevel))
											tm=Double.valueOf(map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.get(curr_NodeName_secLevel).length );
										else{
											writer_debug.append("\n nodeName:"+curr_NodeName_secLevel
																+" not availble inside map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE(second level)");
											writer_debug.close();
											cnt20++;
											continue;
										}
										
									}
									
									//aggregate
									OUTnodes_of_currNode_total_graph_prop1_value=OUTnodes_of_currNode_total_graph_prop1_value+tm;
									cnt20_used_hit++;
									cnt20++;
								}
							}
							cnt++;
						}
						// curr docID outNodes average (only first level)
						if(is_first_level_only==true)
							curr_DocID_avg_OUTnodes_graph_prop1_values=OUTnodes_of_currNode_total_graph_prop1_value/arr_Corresp_OUTnodes.length;
						if(is_first_level_only==false) //SECOND
							curr_DocID_avg_OUTnodes_graph_prop1_values=OUTnodes_of_currNode_total_graph_prop1_value/size_arr_Corresp_OUTnodes_FIRST_n_SECOND;
						if(isSOPprint)
							System.out.println("------1.0");
						double total_OUTnodes_of_currNode_total_graph_prop1_value=0.0;
						double average_on_total_OUTnodes_of_currNode_total_graph_prop1_value=0.0;
						int total_arr_Corresp_OUTnodes=0; int counter222=0;
						// step 2: N (25) preceding documents's Corresponding OUTnodes
						for(int curr_preceding_DocID:map_preceding_N_document_docIDs_AS_KEY.keySet()){
							
							
							if(!map_DocID_N_NodeName_bothgroundTruthTPAND_NOTgroundtruthTP.containsKey(start_docID)){ //only DocID from curr Query
								writer_debug.append("\n NOT in curr query DocID ->"+start_docID);
								writer_debug.flush();
							}
							
							counter222++;
							OUTnodes_of_currNode_total_graph_prop1_value=0;
							
							if(isSOPprint)
								System.out.println( "------1.2");
							String cur_NodeName=map_DocID_N_NodeName_bothgroundTruthTPAND_NOTgroundtruthTP.get(curr_preceding_DocID);
							
							if(map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.containsKey( cur_NodeName))
								arr_Corresp_OUTnodes=map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.get(cur_NodeName);
							else{
								writer_debug.append(" \n error: not found OUTnodes for :"+curr_preceding_DocID );
								writer_debug.flush();
								continue;
							}
							
							total_arr_Corresp_OUTnodes=total_arr_Corresp_OUTnodes+arr_Corresp_OUTnodes.length;
							if(isSOPprint)
								System.out.println("------1.3");
							cnt=0;
							// step : curr PRECEDING DocID OUTnodes 
							while(cnt<arr_Corresp_OUTnodes.length){
								
								if(Flag.equalsIgnoreCase("graphprop")){
									if(map_DocID_GraphPropValue_from_NetworkX_prop1!=null)
										if(map_DocID_GraphPropValue_from_NetworkX_prop1.containsKey(arr_Corresp_OUTnodes[cnt]))
											tm=Double.valueOf(map_DocID_GraphPropValue_from_NetworkX_prop1.get(arr_Corresp_OUTnodes[cnt]));
								}
								else if(Flag.equalsIgnoreCase("OutNodesCountSize")){
									tm=Double.valueOf(map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.get(cur_NodeName).length );
								}
								
								//aggregate
								OUTnodes_of_currNode_total_graph_prop1_value=OUTnodes_of_currNode_total_graph_prop1_value+tm;
								cnt++;
							}
							total_OUTnodes_of_currNode_total_graph_prop1_value=total_OUTnodes_of_currNode_total_graph_prop1_value+OUTnodes_of_currNode_total_graph_prop1_value;
							if(isSOPprint)
								System.out.println("------1.4->"+curr_preceding_DocID+ " counter222:"+counter222
												   +" out of total:"+map_preceding_N_document_docIDs_AS_KEY.size() );
							
							//if(counter222==map_preceding_N_document_docIDs_AS_KEY.size()) break;
							
						} //END for(int curr_preceding_DocID:map_preceding_N_document_docIDs_AS_KEY.keySet()){
						
						if(isSOPprint)
							System.out.println("------1.4-out>");
						//average of N preceding docs (above total_graph_prop1_value calculated)
						average_on_total_OUTnodes_of_currNode_total_graph_prop1_value=total_graph_prop1_value_N_RANDOM/(double)total_arr_Corresp_OUTnodes;
						
						if(map_inFile_NodeName_N_arrOUTNodes_from_OUTNODES_FILE.get(currNodeName).length
								>average_on_total_OUTnodes_of_currNode_total_graph_prop1_value){
							predict_for_curr_DocID=1;
						}
						else
							predict_for_curr_DocID=0;
						
						if(isSOPprint)
							System.out.println("------1.4-out 20>");
						
					}
					else if(classify_method_type.equalsIgnoreCase("4")){
						writer_debug.append("\n entering 4 for docId-> "+ curr_DocID);
						writer_debug.flush();
						
						double OUTnodes_of_currNode_total_graph_prop1_value=0.0;
						//nodeID!!!nodeIDName!!!Corresp_OUTnodes!!!Corresp_INnodes!!!Corresp_OUTnodesNames!!!Corresp_INnodesNames <-header of each line
						//get all the out node
						String curr_DocID_eachLine_OF_OUTnodes_INnodes=map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.get(curr_DocID);
						String []s=curr_DocID_eachLine_OF_OUTnodes_INnodes.split("!!!");
						String Corresp_OUTnodes=s[2];
						String Corresp_INnodes=s[3];
						String currNodeName_from_IN_OUTnodesFile=s[1].replace("\"", "").split(" ")[0];
						String [] arr_Corresp_OUTnodes=Corresp_OUTnodes.split(",");
						int cnt=0;
						// step 1: currDocID OUTnodes 
						while(cnt<arr_Corresp_OUTnodes.length){
							if(isSOPprint)
								System.out.println("arr_Corresp_OUTnodes[cnt]:"+arr_Corresp_OUTnodes[cnt]);
							//takes value from graph property graph_prop
							double tm=Double.valueOf(map_DocID_GraphPropValue_from_NetworkX_prop1.get(arr_Corresp_OUTnodes[cnt]));
							//aggregate
							OUTnodes_of_currNode_total_graph_prop1_value=OUTnodes_of_currNode_total_graph_prop1_value+tm;
							cnt++;
						}
						// curr docID outNodes average
						double curr_DocID_avg_OUTnodes_graph_prop1_values=OUTnodes_of_currNode_total_graph_prop1_value/arr_Corresp_OUTnodes.length;
						double total_OUTnodes_of_currNode_total_graph_prop1_value=0.0;
						double average_on_total_OUTnodes_of_currNode_total_graph_prop1_value=0.0;
						int total_arr_Corresp_OUTnodes=0;
						// step 2: N (25) preceding documents's Corresponding OUTnodes
						for(int curr_preceding_DocID:map_preceding_N_document_docIDs_AS_KEY.keySet()){
							OUTnodes_of_currNode_total_graph_prop1_value=0;
							curr_DocID_eachLine_OF_OUTnodes_INnodes=map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.get(curr_preceding_DocID);
							if(curr_DocID_eachLine_OF_OUTnodes_INnodes==null) {
								//System.out.println("------(deb)2.curr_DocID_eachLine_OF_OUTnodes_INnodes IS NULL for curr_preceding_DocID->"+curr_preceding_DocID);
								continue;
							}
							 
							// 
							s=curr_DocID_eachLine_OF_OUTnodes_INnodes.split("!!!");
							Corresp_OUTnodes=s[1];
							Corresp_INnodes=s[2];
							arr_Corresp_OUTnodes=Corresp_OUTnodes.split(",");
							
							total_arr_Corresp_OUTnodes=total_arr_Corresp_OUTnodes+arr_Corresp_OUTnodes.length;
							cnt=0;
							// step : curr PRECEDING DocID OUTnodes 
							while(cnt<arr_Corresp_OUTnodes.length){
								//takes value from graph property graph_prop
								double tm=Double.valueOf(map_DocID_GraphPropValue_from_NetworkX_prop1.get(arr_Corresp_OUTnodes[cnt]));
								
								//aggregate
								OUTnodes_of_currNode_total_graph_prop1_value=OUTnodes_of_currNode_total_graph_prop1_value+tm;
								cnt++;
							}
							if(isSOPprint)
								System.out.println("------2");
							total_OUTnodes_of_currNode_total_graph_prop1_value=total_OUTnodes_of_currNode_total_graph_prop1_value+OUTnodes_of_currNode_total_graph_prop1_value;
							
						} //END for(int curr_preceding_DocID:map_preceding_N_document_docIDs_AS_KEY.keySet())
						
						//average of N preceding docs ( graph_prop )
						average_on_total_OUTnodes_of_currNode_total_graph_prop1_value=total_graph_prop1_value_N_RANDOM/(double)total_arr_Corresp_OUTnodes;
						
						if(curr_DocID_avg_OUTnodes_graph_prop1_values<average_on_total_OUTnodes_of_currNode_total_graph_prop1_value){
							predict_for_curr_DocID=1;
						}
						else
							predict_for_curr_DocID=0;
					}
					else if(classify_method_type.equalsIgnoreCase("5")){
						//:1 -> person, :2 -> organization, :3 -> location, :4 -> numbers, :6 -> sentiments, :5 -> nouns, :7 -> Verbs						
						writer_debug.append("\n entering 5 for docId-> "+ curr_DocID);
						writer_debug.flush();
						
						int count_number_of_nouns_FOR_currDocID= 0;
						double average_on_count_number_of_nouns_FOR_currDocID=0;
						int count_number_of_verbs_FOR_currDocID= 0;
						
						average_on_count_number_of_verbs_FOR_currDocID=0;
						count_number_of_numbers_FOR_currDocID= 0;
						// how many numbers?  //if it has any of : , - , ignore it all together, also year yyyy
						count_number_of_numbers_FOR_currDocID=count_numbers_for_currDocID( map_curr_SEQ_numbers );
						// ORGANIZATION
						count_number_of_organization_FOR_currDocID=0;
						int total_number_of_organization=0;
						count_number_of_organization_FOR_currDocID=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":2").size();
						TreeMap<String, String> h =map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":2");
						//System.out.println("map_VocWord_WordID:"+map_VocWord_WordID);
						//System.out.println("map_DocIDWordID_TFIDF:"+map_DocIDWordID_TFIDF);
						
						//LOCATION
						count_number_of_location_FOR_currDocID=0;
						map_curr_SEQ_locat=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":3");
						count_number_of_location_FOR_currDocID=map_curr_SEQ_locat.size();
						
						// PERSON
						int count_number_of_person_FOR_currDocID=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":1").size();
						int total_number_of_person=0;
						//get NOUNS
						map_curr_SEQ_nouns_CSVwithSpace=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":5" );
						String[] arr_noun=map_curr_SEQ_nouns_CSVwithSpace.get("1").split(" ");
						c20=0;
						// 
						while(c20<arr_noun.length){
							if(map_NounWord_N_arr_lineNo.containsKey(arr_noun[c20])){
								count_number_of_nouns_FOR_currDocID=count_number_of_nouns_FOR_currDocID+map_NounWord_N_arr_lineNo.get(arr_noun[c20]).length ;
							}
							c20++;
						}	
						average_on_count_number_of_nouns_FOR_currDocID=count_number_of_nouns_FOR_currDocID/(double)arr_noun.length;
						//get VERBS 
						map_curr_SEQ_verbs_CSVwithSpace=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":7" );
						arr_verb=map_curr_SEQ_verbs_CSVwithSpace.get("1").split(" ");
						c20=0;
						// 
						while(c20<arr_verb.length){
							if(map_VerbWord_N_arr_lineNo.containsKey(arr_verb[c20])){
								count_number_of_verbs_FOR_currDocID=count_number_of_verbs_FOR_currDocID+map_VerbWord_N_arr_lineNo.get(arr_verb[c20]).length ;
							}
							c20++;
						}
						//VERB (AVERAGE)
						average_on_count_number_of_verbs_FOR_currDocID=count_number_of_verbs_FOR_currDocID/(double)arr_verb.length;
						
						//sentiments
						map_curr_SEQ_sentiment=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":6");
						//DISTINCT sentiments
						count_number_of_distinct_sentiment_for_curr_DocID=map_curr_SEQ_sentiment.size();
						
						//  total negative sentiment (sentence) / total no of sentences
						for(String i:map_curr_SEQ_sentiment.keySet()){
							if(map_curr_SEQ_sentiment.containsKey("1.0") ){
								total_neg_sentiment=total_neg_sentiment+ Integer.valueOf( map_curr_SEQ_sentiment.get("1.0"));
								
							}
//							if(map_curr_SEQ_sentiment.containsKey("2.0") ){
//								total_neg_sentiment=total_neg_sentiment+ Integer.valueOf( map_curr_SEQ_sentiment.get("2.0"));
//							}
//							if(map_curr_SEQ_sentiment.containsKey("3.0") ){
//								total_neg_sentiment=total_neg_sentiment+ Integer.valueOf( map_curr_SEQ_sentiment.get("3.0"));
//							}
							// total sentiment of all sentences
							for(String senti:map_curr_SEQ_sentiment.keySet())
								total_no_sentence_curr_DocID=total_no_sentence_curr_DocID+Integer.valueOf( map_curr_SEQ_sentiment.get(senti) );
						}
						// ratio of negative sentiment sentence
						ratio_neg_senti_currDocID_per_sentence=total_neg_sentiment/total_no_sentence_curr_DocID;
						ratio_neg_senti_currDocID_per_document=Double.valueOf( map_curr_SEQ_sentiment.get("1.0"));
						
						double OUTnodes_of_currNode_total_graph_prop1_value=0.0;
						  total_COUNT_of_numbers_N_preceding_Docs=0;
						  total_COUNT_of_organization_N_preceding_Docs=0;
							average_on_total_COUNT_of_numbers_N_preceding_Docs=0.0;
						
						int total_COUNT_of_person_N_preceding_Docs=0;
						double average_on_total_COUNT_of_person_N_preceding_Docs=0;
							
						//nodeID!!!nodeIDName!!!Corresp_OUTnodes!!!Corresp_INnodes!!!Corresp_OUTnodesNames!!!Corresp_INnodesNames <-header of each line
						//get all the out node of curr DocID
						String curr_DocID_eachLine_OF_OUTnodes_INnodes=map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.get(curr_DocID);
						
						if(curr_DocID_eachLine_OF_OUTnodes_INnodes==null){
							writer_debug.append("\nerror:(2) OUTnodes NULL for "+curr_DocID);
							writer_debug.flush();
							continue;
						}
						
						String []s=curr_DocID_eachLine_OF_OUTnodes_INnodes.split("!!!");
						int total_count_of_numbers_allOUTnode_for_currDocID=0;
						String Corresp_OUTnodes=s[2];
						String Corresp_INnodes=s[3];
						String currNodeName_from_IN_OUTnodesFile=s[1].replace("\"", "").split(" ")[0];
						String [] arr_Corresp_OUTnodes=Corresp_OUTnodes.split(",");

						int cnt=0;
						// step 1: currDocID OUTnodes (of CURR DOC ID)
						while(cnt<arr_Corresp_OUTnodes.length){
							if(isSOPprint)
								System.out.println("arr_Corresp_OUTnodes[cnt]:"+arr_Corresp_OUTnodes[cnt]);
							//takes value from graph property graph_prop
							double tm=Double.valueOf(map_DocID_GraphPropValue_from_NetworkX_prop1.get(arr_Corresp_OUTnodes[cnt]));
							//aggregate ( outNodes )
							OUTnodes_of_currNode_total_graph_prop1_value=OUTnodes_of_currNode_total_graph_prop1_value+tm;
							
							// numbers (not all nodes are DocID)
							TreeMap<String,String> t=map_lineNoNID_person_organiz_locati_numbers_nouns.get(arr_Corresp_OUTnodes[cnt]+":4" );
							total_count_of_numbers_allOUTnode_for_currDocID=total_count_of_numbers_allOUTnode_for_currDocID+ count_numbers_for_currDocID(t);
							
							cnt++;
						}
						// outNodes average => curr docID outNodes average
						double curr_DocID_avg_OUTnodes_graph_prop1_values=OUTnodes_of_currNode_total_graph_prop1_value/arr_Corresp_OUTnodes.length;
						double total_OUTnodes_of_currNode_total_graph_prop1_value=0.0;
						double average_on_total_OUTnodes_of_currNode_total_graph_prop1_value=0.0;
						int total_arr_Corresp_OUTnodes=0; int total_denomin_arr_verbs_leng=0;
						int total_denomin_arr_nouns_leng=0;
						total_no_sentence_preced_N_docs=0; ratio_neg_senti_N_preced_docs_per_sentence=0.;

						// step 2: N (25) preceding documents's Corresponding OUTnodes
						for(int curr_preceding_DocID:map_preceding_N_document_docIDs_AS_KEY.keySet()){
							//	:1 -> person, :2 -> organization, :3 -> location, :4 -> numbers, :6 -> sentiments, :5 -> nouns, :7 -> Verbs
							
							/// might be blank line (mismatch between two files - read pre-requistion of main() method)
							if( !map_lineNoNID_person_organiz_locati_numbers_nouns.containsKey(curr_preceding_DocID+":2") &&
								!map_lineNoNID_person_organiz_locati_numbers_nouns.containsKey(curr_preceding_DocID+":1") &&
								!map_lineNoNID_person_organiz_locati_numbers_nouns.containsKey(curr_preceding_DocID+":3") &&
								!map_lineNoNID_person_organiz_locati_numbers_nouns.containsKey(curr_preceding_DocID+":4")
									){	
								continue;
							}
							
							OUTnodes_of_currNode_total_graph_prop1_value=0;
							curr_DocID_eachLine_OF_OUTnodes_INnodes=map_inFile_DocID_N_givenNode_corresp_OutNodes_INnodes.get(curr_preceding_DocID);
							//count NUMBERS
							TreeMap<String,String> curr_count_number=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preceding_DocID+":4" );
							total_COUNT_of_numbers_N_preceding_Docs=total_COUNT_of_numbers_N_preceding_Docs+count_numbers_for_currDocID( curr_count_number);
							//count LOCATION
							map_curr_SEQ_organiz=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preceding_DocID+":3");
							total_COUNT_of_location_N_preceding_Docs=total_COUNT_of_location_N_preceding_Docs+map_curr_SEQ_organiz.size();
							//System.out.println("curr_preceding_DocID:"+curr_preceding_DocID);
							//count ORGANIZATION
							map_curr_SEQ_organiz=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preceding_DocID+":2");
							total_COUNT_of_organization_N_preceding_Docs=total_COUNT_of_organization_N_preceding_Docs+map_curr_SEQ_organiz.size();
							//count PERSON
							map_curr_SEQ_person=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preceding_DocID+":1");
							total_COUNT_of_person_N_preceding_Docs=total_COUNT_of_person_N_preceding_Docs+map_curr_SEQ_person.size();
							//get NOUNS
							map_curr_SEQ_nouns_CSVwithSpace=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preceding_DocID+":5" );
							arr_noun=map_curr_SEQ_nouns_CSVwithSpace.get("1").split(" ");
							c20=0;
							//
							while(c20<arr_noun.length){
								if(map_NounWord_N_arr_lineNo.containsKey(arr_noun[c20])){
									count_number_of_nouns_FOR_curr_preceding_DocID=count_number_of_nouns_FOR_curr_preceding_DocID
																					+map_NounWord_N_arr_lineNo.get(arr_noun[c20]).length ;
								}
								c20++;
							}
							total_denomin_arr_nouns_leng=total_denomin_arr_nouns_leng+arr_noun.length;
							
							//get VERBS 
							map_curr_SEQ_verbs_CSVwithSpace=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preceding_DocID+":7" );
							arr_verb=map_curr_SEQ_verbs_CSVwithSpace.get("1").split(" ");
							c20=0;
							// 
							while(c20<arr_verb.length){
								if(map_VerbWord_N_arr_lineNo.containsKey(arr_verb[c20])){
									count_number_of_verbs_FOR_curr_preceding_DocID=count_number_of_verbs_FOR_curr_preceding_DocID
																					+map_VerbWord_N_arr_lineNo.get(arr_verb[c20]).length ;
								}
								c20++;
							}
							total_denomin_arr_verbs_leng=total_denomin_arr_verbs_leng+arr_verb.length;
							
							//**sentiment
							total_neg_sentiment=0; //RESET
							total_no_sentence_preced_N_docs=0;
							map_curr_SEQ_sentiment=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_preceding_DocID+":6");
							//DISTINCT sentiments
							total_count_number_of_distinct_sentiment_N_preced_docs=total_count_number_of_distinct_sentiment_N_preced_docs
																					+map_curr_SEQ_sentiment.size();
							//  total negative sentiment (sentence) / total no of sentences
							for(String i:map_curr_SEQ_sentiment.keySet()){
								if(map_curr_SEQ_sentiment.containsKey("1.0") ){
									total_neg_sentiment=total_neg_sentiment+ Integer.valueOf( map_curr_SEQ_sentiment.get("1.0"));
								}
//								if(map_curr_SEQ_sentiment.containsKey("2.0") ){
//									total_neg_sentiment=total_neg_sentiment+ Integer.valueOf( map_curr_SEQ_sentiment.get("2.0"));
//								}
//								if(map_curr_SEQ_sentiment.containsKey("3.0") ){
//									total_neg_sentiment=total_neg_sentiment+ Integer.valueOf( map_curr_SEQ_sentiment.get("3.0"));
//								}
								// total sentiment of all sentences
								for(String senti:map_curr_SEQ_sentiment.keySet())
									total_no_sentence_preced_N_docs=total_no_sentence_preced_N_docs+Integer.valueOf( map_curr_SEQ_sentiment.get(senti) );
							}
							// ratio of negative sentiment sentence
							ratio_neg_senti_N_preced_docs_per_sentence=total_neg_sentiment/total_no_sentence_preced_N_docs;
							//
							ratio_neg_senti_N_preced_docs_per_document=total_neg_sentiment / (double) map_preceding_N_document_docIDs_AS_KEY.size();
							 
							if(curr_DocID_eachLine_OF_OUTnodes_INnodes==null) {
								//System.out.println("------(deb)curr_DocID_eachLine_OF_OUTnodes_INnodes IS NULL for curr_preceding_DocID->"+curr_preceding_DocID);
								continue;
							}
							 
							// 
							s=curr_DocID_eachLine_OF_OUTnodes_INnodes.split("!!!");
							Corresp_OUTnodes=s[1];
							Corresp_INnodes=s[2];
							arr_Corresp_OUTnodes=Corresp_OUTnodes.split(",");
							
							total_arr_Corresp_OUTnodes=total_arr_Corresp_OUTnodes+arr_Corresp_OUTnodes.length;
							cnt=0;
							// step : OUTNODES ----------- curr PRECEDING DocID OUTnodes 
							while(cnt<arr_Corresp_OUTnodes.length){
									
								double tm=0.0;
								String graphvalue=map_DocID_GraphPropValue_from_NetworkX_prop1.get(arr_Corresp_OUTnodes[cnt]);
								//System.out.println("graph value->"+graphvalue);
								//takes value from graph property graph_prop
								if(graphvalue==null){
									cnt++; continue;
								}
								if(graphvalue.length()>0)
									  tm=Double.valueOf(graphvalue);
								else
									  tm=0.0;
								
								//aggregate
								OUTnodes_of_currNode_total_graph_prop1_value=OUTnodes_of_currNode_total_graph_prop1_value+tm;
								cnt++;
							}
							//averaging
							
							if(isSOPprint)
								System.out.println("------2");
							total_OUTnodes_of_currNode_total_graph_prop1_value=total_OUTnodes_of_currNode_total_graph_prop1_value+OUTnodes_of_currNode_total_graph_prop1_value;
							
						} //END for(int curr_preceding_DocID:map_preceding_N_document_docIDs_AS_KEY.keySet())
						
						// VERB (average)
						average_on_count_number_of_verbs_FOR_curr_preceding_DocID=count_number_of_verbs_FOR_curr_preceding_DocID/(double)total_denomin_arr_verbs_leng;
						// NOUN (average)
						average_on_count_number_of_nouns_FOR_curr_preceding_DocID=count_number_of_nouns_FOR_curr_preceding_DocID/(double)total_denomin_arr_nouns_leng;
						// COUNT NUMBERS (average)
						average_on_total_COUNT_of_numbers_N_preceding_Docs=total_COUNT_of_numbers_N_preceding_Docs/(double)map_preceding_N_document_docIDs_AS_KEY.size();
						// COUNT LOCATION (average)
						average_on_total_COUNT_of_location_N_preceding_Docs=total_COUNT_of_location_N_preceding_Docs/(double)map_preceding_N_document_docIDs_AS_KEY.size();
						// COUNT ORGANIZATION (average)
						average_on_total_COUNT_of_organization_N_preceding_Docs=total_COUNT_of_organization_N_preceding_Docs/(double)map_preceding_N_document_docIDs_AS_KEY.size();
						// COUNT PERSON (average)
						average_on_total_COUNT_of_person_N_preceding_Docs=total_COUNT_of_person_N_preceding_Docs/(double)map_preceding_N_document_docIDs_AS_KEY.size();
						
						// GRAPH PROPERTIES  (average of N preceding docs )
						average_on_total_OUTnodes_of_currNode_total_graph_prop1_value=total_graph_prop1_value_N_RANDOM/(double)total_arr_Corresp_OUTnodes;
						// COUNT DISTINCT SENTIMENT (AVERAGE)
						average_on_total_count_number_of_distinct_sentiment_N_preced_docs=total_count_number_of_distinct_sentiment_N_preced_docs/
																							(double) map_preceding_N_document_docIDs_AS_KEY.size();
						
						//System.out.println(" Inside method=5->"+count_number_of_numbers_FOR_currDocID+" "+average_total_OUTnodes_COUNT_of_numbers_first_level);
						
						print1="\n -----ratio_neg_senti_currDocID:"+ratio_neg_senti_currDocID_per_sentence 
								+" count_number_of_numbers_FOR_currDocID:"+count_number_of_numbers_FOR_currDocID
								+" count_number_of_organization_FOR_currDocID:"+count_number_of_organization_FOR_currDocID
								+" curr_DocID_graph_prop_value_prop1:"+curr_DocID_graph_prop_value_prop1
								+" curr_DocID_graph_prop_value_prop2:"+curr_DocID_graph_prop_value_prop2
								+" count_number_of_nouns_FOR_curr_preceding_DocID:"+count_number_of_nouns_FOR_curr_preceding_DocID
								+" total_denomin_arr_nouns_leng:"+total_denomin_arr_nouns_leng
								+" average_on_count_number_of_nouns_FOR_currDocID:"+average_on_count_number_of_nouns_FOR_currDocID
								+" count_number_of_verbs_FOR_curr_preceding_DocID:"+count_number_of_verbs_FOR_curr_preceding_DocID
								+" total_denomin_arr_verbs_leng:"+total_denomin_arr_verbs_leng
								+" average_on_count_number_of_verbs_FOR_currDocID:"+average_on_count_number_of_verbs_FOR_currDocID
								+" count_number_of_distinct_sentiment_for_curr_DocID:"+count_number_of_distinct_sentiment_for_curr_DocID
								+" count_number_of_location_FOR_currDocID:"+count_number_of_location_FOR_currDocID
								+" count_number_of_numbers_FOR_currDocID:"+count_number_of_numbers_FOR_currDocID
								;
						
						
						print2="\n -----ratio_neg_senti_N_preced_docs:"+ratio_neg_senti_N_preced_docs_per_sentence+
								" average_total_OUTnodes_COUNT_of_numbers_N_preceding_Docs:"+average_on_total_COUNT_of_numbers_N_preceding_Docs+
								" average_on_total_COUNT_of_organization_N_preceding_Docs:"+average_on_total_COUNT_of_organization_N_preceding_Docs+
								" avg_total_graph_prop1_value_N_RANDOM:"+avg_total_graph_prop1_value_N_RANDOM+
								" avg_total_graph_prop2_value_N_RANDOM:"+avg_total_graph_prop2_value_N_RANDOM+
								" average_on_count_number_of_nouns_FOR_curr_preceding_DocID:"+average_on_count_number_of_nouns_FOR_curr_preceding_DocID+
								" average_on_count_number_of_verbs_FOR_curr_preceding_DocID:"+average_on_count_number_of_verbs_FOR_curr_preceding_DocID+
								" average_on_total_count_number_of_distinct_sentiment_N_preced_docs:"+average_on_total_count_number_of_distinct_sentiment_N_preced_docs+
								" average_on_total_COUNT_of_location_N_preceding_Docs:"+average_on_total_COUNT_of_location_N_preceding_Docs+
								" average_on_total_COUNT_of_numbers_N_preceding_Docs:"+average_on_total_COUNT_of_numbers_N_preceding_Docs
								;
						//
						writer_debug.append(print1);
						writer_debug.append(print2);
						writer_debug.flush();
						 
						// compare
						comp1=java.lang.Math.sqrt(ratio_neg_senti_currDocID_per_sentence+count_number_of_numbers_FOR_currDocID);
						comp2=java.lang.Math.sqrt(ratio_neg_senti_N_preced_docs_per_sentence+average_on_total_COUNT_of_numbers_N_preceding_Docs);
						// OFFSET
						offset_on_average_on_total_COUNT_of_numbers_N_preceding_Docs=0.8;
						comp1=ratio_neg_senti_currDocID_per_sentence+count_number_of_numbers_FOR_currDocID;
						comp2=ratio_neg_senti_N_preced_docs_per_sentence+(offset_on_average_on_total_COUNT_of_numbers_N_preceding_Docs
															*average_on_total_COUNT_of_numbers_N_preceding_Docs);
						
						
						writer_debug.append("\n compare:"+comp1+" "+comp2);
						writer_debug.flush();
						//OFFSET
						offset_average_on_total_COUNT_of_organization_N_preceding_Docs=1.3;
						offset_average_on_count_number_of_verbs_FOR_curr_preceding_DocID=1.3;
						//
						print3= "\n count_number_of_organization_FOR_currDocID:"+count_number_of_organization_FOR_currDocID+
								"\n offset_average_on_total_COUNT_of_organization_N_preceding_Docs*average_on_total_COUNT_of_organization_N_preceding_Docs:"
								+(offset_average_on_total_COUNT_of_organization_N_preceding_Docs
										  *average_on_total_COUNT_of_organization_N_preceding_Docs) 
								+"\n for offset_average_on_total_COUNT_of_organization_N_preceding_Docs:"+offset_average_on_total_COUNT_of_organization_N_preceding_Docs
								+"\n average_on_count_number_of_verbs_FOR_currDocID:"+average_on_count_number_of_verbs_FOR_currDocID
								+"\n offset_average_on_count_number_of_verbs_FOR_curr_preceding_DocID * average_on_count_number_of_verbs_FOR_curr_preceding_DocID:"
								+ (offset_average_on_count_number_of_verbs_FOR_curr_preceding_DocID * average_on_count_number_of_verbs_FOR_curr_preceding_DocID)
								+"\n for offset:"+offset_average_on_count_number_of_verbs_FOR_curr_preceding_DocID
								+" \n comp1:"+comp1+" comp2:"+comp2;
						
						
						//  Predict
						if( comp1 > comp2 && 
							count_number_of_organization_FOR_currDocID >  offset_average_on_total_COUNT_of_organization_N_preceding_Docs
																		  *average_on_total_COUNT_of_organization_N_preceding_Docs  &&  // 1.4 times tried
							count_number_of_numbers_FOR_currDocID > average_on_total_COUNT_of_numbers_N_preceding_Docs &&
					//		count_number_of_person_FOR_currDocID >  1.2 * average_on_total_COUNT_of_person_N_preceding_Docs
							//&&
							//count_number_of_organization_FOR_currDocID >  average_on_total_COUNT_of_person_N_preceding_Docs &&
							//(
							//java.lang.Math.sqrt(curr_DocID_graph_prop_value_prop1*curr_DocID_graph_prop_value_prop2)
							//>java.lang.Math.sqrt(avg_total_graph_prop1_value_N_RANDOM * avg_total_graph_prop2_value_N_RANDOM))
							( average_on_count_number_of_verbs_FOR_currDocID > offset_average_on_count_number_of_verbs_FOR_curr_preceding_DocID 
																				* average_on_count_number_of_verbs_FOR_curr_preceding_DocID )
							//&& ( average_on_count_number_of_nouns_FOR_currDocID > average_on_count_number_of_nouns_FOR_curr_preceding_DocID )
								&&
							   avg_organiz_currDocID_TFIDF  > avg_avg_organiz_N_prec_DocID_TFIDF 
							   //&&
							   //avg_number_currDocID_TFIDF   > avg_avg_number_N_prec_DocID_TFIDF
							){
							predict_for_curr_DocID=1;
							cond_1++;
						}
//						else if(
//									count_number_of_organization_FOR_currDocID >  offset_average_on_total_COUNT_of_organization_N_preceding_Docs
//																				  *average_on_total_COUNT_of_organization_N_preceding_Docs  &&  // 1.4 times tried
//								  avg_organiz_currDocID_TFIDF  > avg_avg_organiz_N_prec_DocID_TFIDF &&
//								   avg_number_currDocID_TFIDF   > avg_avg_number_N_prec_DocID_TFIDF
//								
//								) {
//									boolean is_first_NOT_true=true;
//
//									predict_for_curr_DocID=1;
//									cond_2++;
//						}
						else{
									predict_for_curr_DocID=0;
									cond_3++;
						}
						
						
						average_on_count_number_of_nouns_FOR_curr_preceding_DocID=0;
						average_on_count_number_of_verbs_FOR_curr_preceding_DocID=0;
						 
						count_number_of_verbs_FOR_curr_preceding_DocID=0;
						count_number_of_nouns_FOR_curr_preceding_DocID=0;
						average_on_count_number_of_verbs_FOR_currDocID=0;
						average_on_count_number_of_nouns_FOR_currDocID=0;
					} //END else if(classify_method_type.equalsIgnoreCase("5")){
					else if(classify_method_type.equalsIgnoreCase("6")){
						offset_on_average_on_total_COUNT_of_numbers_N_preceding_Docs=1.3;
						comp1=ratio_neg_senti_currDocID_per_sentence+count_number_of_numbers_FOR_currDocID;
						comp2=ratio_neg_senti_N_preced_docs_per_sentence+(offset_on_average_on_total_COUNT_of_numbers_N_preceding_Docs
															*average_on_total_COUNT_of_numbers_N_preceding_Docs);
						
						
						if(
						   ratio_neg_senti_currDocID_per_sentence < 2*ratio_neg_senti_N_preced_docs_per_sentence && 
						   avg_organiz_currDocID_TFIDF  > avg_avg_organiz_N_prec_DocID_TFIDF &&
						   avg_number_currDocID_TFIDF   > avg_avg_number_N_prec_DocID_TFIDF
						   ){
							predict_for_curr_DocID=1;
						}
						else
							predict_for_curr_DocID=0;
						
					}
					else if(classify_method_type.equalsIgnoreCase("7")){
						System.out.println("\n ENTERING 7 ...");
						// clean String
						currDocID_bodyText=clean_String(currDocID_bodyText);
						
						String [] arr_word=currDocID_bodyText.split(" ");
						String [] arr_word2=new String[1];
						int cnt=0;
 
						sum_of_FocusWord_Count_N_PRECED_DocID_=0;
					    avg_on_sum_of_FocusWord_Count_N_PRECED_DocID_=0;
						
						writer_debug.append("\n 7.start docID:+"+curr_DocID +" arr_word.len:"+arr_word.length +" len:"+currDocID_bodyText.length() );
						writer_debug.flush();
//						map_VocWord_WordID
//						map_DocIDWordID_TFIDF
//						TreeMap<String, TreeMap<String, String>> map_lineNoNID_person_organiz_locati_numbers_nouns, <-special one (from 
//						TreeMap<Integer, String>  map_DocID_NLPtext,
//						TreeMap<Integer, String>  map_DocID_bodytext,
//						TreeMap<Integer, String>  map_DocID_URL,
//						TreeMap<String, String[]> map_NounWord_N_arr_lineNo,
//						TreeMap<String, String[]> map_VerbWord_N_arr_lineNo,
//						TreeMap<String, String[]> map_PersonWord_N_arr_lineNo,
//						TreeMap<String, String[]> map_OrganizWord_N_arr_lineNo,
//						TreeMap<String, String[]> map_LocationWord_N_arr_lineNo,
//						TreeMap<String, String[]> map_NumberWord_N_arr_lineNo,
//						TreeMap<Integer, String[]>  map_docIDrlineNo_numbersCSV,  <--> map_2_arr_curr_SEQ_numbers_CLEANED MNumber 
//						TreeMap<Integer, String []> map_docIDrlineNo_arrelatedWordsForNumbersCSV  <--> map_2_arr_curr_SEQ_RelatedWords4Numbers  <-Relatd words for Number 
						//map_VerbWord_N_arr_lineNo.get( )
//						map_curr_SEQ_person=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":1");
//						map_curr_SEQ_organiz=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":2");
//						map_curr_SEQ_locat=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":3");
//						map_curr_SEQ_numbers=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":4");
//						map_curr_SEQ_nouns=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":5");
//						map_curr_SEQ_sentiment=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":6");
//						map_curr_SEQ_verbs=map_lineNoNID_person_organiz_locati_numbers_nouns.get(curr_DocID+":7");
//						map_2_arr_curr_SEQ_numbers_CLEANED  <-numbers
//						map_currDocsNUMBER=mapTemp.get(1);   // NUMBER
//						map_currDocsMONEY =mapTemp.get(2);   // MONEY
//						map_currDocsYEAR  =mapTemp.get(3);   // YEAR
						 					
						
						if(map_curr_SEQ_sentiment!=null && map_curr_SEQ_verbs_CSVwithSpace!=null
							&& map_curr_SEQ_nouns_CSVwithSpace!=null &&  map_curr_SEQ_locat!=null &&map_curr_SEQ_numbers!=null
							&& map_curr_SEQ_person!=null && map_curr_SEQ_organiz!=null
							){
							//print all attributes loaded
							System.out.println(  "docID: all attrib size:"+" person:"+map_curr_SEQ_person.size()+" organiz:"+map_curr_SEQ_organiz.size()
												 +" loca:"+map_curr_SEQ_locat.size()+" numbers:"+map_curr_SEQ_numbers.size()
												 +" nouns:"+map_curr_SEQ_nouns_CSVwithSpace.size()
												 +" sentim:"+map_curr_SEQ_sentiment.size()+" verb:"+map_curr_SEQ_verbs_CSVwithSpace.size()
												 //+" map_VerbWord_N_arr_lineNo:"+map_VerbWord_N_arr_lineNo
												 );
						}
						//
						if(map_2_arr_curr_SEQ_numbers_CLEANED!=null){
							//System.out.println(" numb:"+map_2_arr_curr_SEQ_numbers_CLEANED.length);
							//writer_debug.append("\n docId:"+ curr_DocID+" numb:"+map_2_arr_curr_SEQ_numbers_CLEANED.length);
						}
						if(map_2_arr_curr_SEQ_RelatedWords4Numbers!=null){
							System.out.println(" relatedWords:"+map_2_arr_curr_SEQ_RelatedWords4Numbers.length);
							//writer_debug.append("\n docId:"+ curr_DocID+" relatedWords:"+map_2_arr_curr_SEQ_RelatedWords4Numbers.length);
						}
						
						// N PRECEDING DOCUMENT
						TreeMap<Integer, TreeMap<String,String>> map_DocID_PRECED_Verb_Filtered_as_KEY=
																			new TreeMap<Integer, TreeMap<String,String>>();
						//compare
						double currDoc_Verb_Filtered_as_KEY_size=0.0;
						double avg_currDoc_N_PRECED_Verb_Filtered_as_KEY_size=0.0;
						//System.out.println("N PRECED STARTS verb");
						//how strong verb (actions) of currDocID related to neighbor PRECED (random) ?
						{
							// curr document filtered verb
							TreeMap<String, String>  currDoc_Verb_Filtered_as_KEY=
												get_currDos_Verb_Filtered_as_KEY(  map_curr_SEQ_verbs_CSVwithSpace,
																				   stemmer,
																				   map_VerbWord_N_arr_lineNo,
																				   map_NounWord_N_arr_lineNo);
							
							currDoc_Verb_Filtered_as_KEY_size=currDoc_Verb_Filtered_as_KEY.size();
							//preceding documents
							for(int docID_preced:map_preceding_N_document_docIDs_AS_KEY.keySet()){
								
								TreeMap<String, String > map_PREC_DOC_curr_SEQ_verbs_CSVwithSpace=
														map_lineNoNID_person_organiz_locati_numbers_nouns.get(docID_preced+":7");
								
								TreeMap<String, String>  currDoc_PRECED_Verb_Filtered_as_KEY=
														get_currDos_Verb_Filtered_as_KEY(  map_PREC_DOC_curr_SEQ_verbs_CSVwithSpace,
																						   stemmer,
																						   map_VerbWord_N_arr_lineNo,
																						   map_NounWord_N_arr_lineNo);
								
								// 
								map_DocID_PRECED_Verb_Filtered_as_KEY.put(docID_preced, currDoc_PRECED_Verb_Filtered_as_KEY);
								
							}
							double sum_map_DocID_PRECED_Verb_Filtered_as_KEY=0.0;
							// preceding
							for(int PREC_DOC_ID:map_DocID_PRECED_Verb_Filtered_as_KEY.keySet()){
								sum_map_DocID_PRECED_Verb_Filtered_as_KEY=sum_map_DocID_PRECED_Verb_Filtered_as_KEY+
																		  map_DocID_PRECED_Verb_Filtered_as_KEY.get(PREC_DOC_ID).size();
							}
							avg_currDoc_N_PRECED_Verb_Filtered_as_KEY_size=sum_map_DocID_PRECED_Verb_Filtered_as_KEY/(double)map_DocID_PRECED_Verb_Filtered_as_KEY.size();
						}
						// 
						boolean is_true_numbers_with_relatedWords=false;
						if(map_2_arr_curr_SEQ_RelatedWords4Numbers!=null){
							if(map_2_arr_curr_SEQ_RelatedWords4Numbers.length>0)
								is_true_numbers_with_relatedWords=true;
						}
						
						//organization
						// N PRECEDING DOCUMENT
						TreeMap<Integer, TreeMap<String,String>> map_DocID_PRECED_Organiz_Filtered_as_KEY=
																			new TreeMap<Integer, TreeMap<String,String>>();
						//compare
						double currDoc_Organiz_Filtered_as_KEY_size=0.0;
						double avg_currDoc_N_PRECED_Organiz_Filtered_as_KEY_size=0.0;
						int sum_on_map_N_PREC_currDocsNUMBER=0;double avg_on_sum_on_map_N_PREC_currDocsNUMBER=0.0;
						int sum_on_map_N_PREC_currDocsMONEY=0;double avg_on_sum_on_map_N_PREC_currDocsMONEY=0.;
						int sum_on_map_N_PREC_currDocsYEAR=0;double avg_on_sum_on_map_N_PREC_currDocsYEAR=0;
						//System.out.println("N PRECED STARTS organization");
						// ORGANIZATION -- how strong ORGANIZA ( ) of currDocID related to neighbor PRECED (random) ?
						{	
							// curr document filtered verb
							TreeMap<String, String>  currDoc_Organiz_Filtered_as_KEY=
												get_currDos_Verb_Filtered_as_KEY(  map_curr_SEQ_organiz,
																				   stemmer,
																				   map_VerbWord_N_arr_lineNo,
																				   map_NounWord_N_arr_lineNo);
							
							currDoc_Organiz_Filtered_as_KEY_size=currDoc_Organiz_Filtered_as_KEY.size();
							//preceding documents
							for(int docID_preced:map_preceding_N_document_docIDs_AS_KEY.keySet()){
								
								TreeMap<String, String > map_PREC_DOC_curr_SEQ_Organiz_CSVwithSpace=
														map_lineNoNID_person_organiz_locati_numbers_nouns.get(docID_preced+":2");
								 
								TreeMap<String, String>  currDoc_PRECED_Organiz_Filtered_as_KEY=
														get_currDos_Verb_Filtered_as_KEY(  map_PREC_DOC_curr_SEQ_Organiz_CSVwithSpace,
																						   stemmer,
																						   map_VerbWord_N_arr_lineNo,
																						   map_NounWord_N_arr_lineNo);
								
								// 
								map_DocID_PRECED_Organiz_Filtered_as_KEY.put(docID_preced, currDoc_PRECED_Organiz_Filtered_as_KEY);
								
							}
							double sum_map_DocID_PRECED_Organiz_Filtered_as_KEY=0.0;
							// preceding
							for(int PREC_DOC_ID:map_DocID_PRECED_Organiz_Filtered_as_KEY.keySet()){
								sum_map_DocID_PRECED_Organiz_Filtered_as_KEY=sum_map_DocID_PRECED_Organiz_Filtered_as_KEY+
																		  map_DocID_PRECED_Organiz_Filtered_as_KEY.get(PREC_DOC_ID).size();
							}
							avg_currDoc_N_PRECED_Organiz_Filtered_as_KEY_size=sum_map_DocID_PRECED_Organiz_Filtered_as_KEY/(double)map_DocID_PRECED_Organiz_Filtered_as_KEY.size();
							
						}
						
						
						
						//BEGIN SENTIMENT 
						//(1) 
						try{
							//  total negative sentiment (sentence) / total no of sentences
							for(String i:map_curr_SEQ_sentiment.keySet()){
								if(map_curr_SEQ_sentiment.containsKey("1.0") ){
									total_neg_sentiment=total_neg_sentiment+ Integer.valueOf( map_curr_SEQ_sentiment.get("1.0"));
									
								}
								else{
									total_neg_sentiment=0; //NOT AVAILABLE
								}
//								if(map_curr_SEQ_sentiment.containsKey("2.0") ){
//									total_neg_sentiment=total_neg_sentiment+ Integer.valueOf( map_curr_SEQ_sentiment.get("2.0"));
//								}
	//							if(map_curr_SEQ_sentiment.containsKey("3.0") ){
	//								total_neg_sentiment=total_neg_sentiment+ Integer.valueOf( map_curr_SEQ_sentiment.get("3.0"));
	//							}
								// total sentiment of all sentences
								for(String senti:map_curr_SEQ_sentiment.keySet())
									total_no_sentence_curr_DocID=total_no_sentence_curr_DocID+Integer.valueOf( map_curr_SEQ_sentiment.get(senti) );
							}
							// ratio of negative sentiment sentence
							ratio_neg_senti_currDocID_per_sentence=total_neg_sentiment/total_no_sentence_curr_DocID;
							//System.out.println("map_curr_SEQ_sentiment.get(1.0):"+map_curr_SEQ_sentiment.get("1.0"));
							ratio_neg_senti_currDocID_per_document=Double.valueOf( total_neg_sentiment);
						}
						catch(Exception e){
							System.out.println("error on curr_DocID:"+curr_DocID);
							e.printStackTrace();
						}
						//reset 
						total_neg_sentiment=0;
						total_no_sentence_preced_N_docs=0;
						int cnt_cnt=0;
						double sum_TF_IDF_on_verbs_FOR_N_PREC_DocID=0.0;
						double avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID=0.;
						denom_count_number_of_verb=0;
						//System.out.println("N PRECED STARTS sentiment");
						//(2) preceding documents (SENTIMENT)
						for(int docID_preced:map_preceding_N_document_docIDs_AS_KEY.keySet()){
							//**sentiment
							map_curr_SEQ_sentiment=map_lineNoNID_person_organiz_locati_numbers_nouns.get(docID_preced+":6");
							//System.out.println("N PRECED STARTS sentiment 1.0 ");
							// 
							if(map_curr_SEQ_sentiment!=null){
								int size_map_curr_SEQ_sentiment=0;
								size_map_curr_SEQ_sentiment=map_curr_SEQ_sentiment.size();
	//							if(map_curr_SEQ_sentiment!=null){
	//								try{
	//									size_map_curr_SEQ_sentiment=map_curr_SEQ_sentiment.size();
	//								}
	//								catch(Exception e){
	//									size_map_curr_SEQ_sentiment=0;
	//									continue;
	//								}
	//							}
								
								writer_debug.append("\n ()()map_curr_SEQ_sentiment:"+map_curr_SEQ_sentiment
												  +" gt:"+map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)
												  +" curr_DocID:"+curr_DocID);
								writer_debug.flush();
								
								//DISTINCT sentiments
								total_count_number_of_distinct_sentiment_N_preced_docs=total_count_number_of_distinct_sentiment_N_preced_docs
																							+size_map_curr_SEQ_sentiment;
								//  total negative sentiment (sentence) / total no of sentences
								for(String i:map_curr_SEQ_sentiment.keySet()){
									if(map_curr_SEQ_sentiment.containsKey("1.0") ){
										total_neg_sentiment=total_neg_sentiment+ Integer.valueOf( map_curr_SEQ_sentiment.get("1.0"));
									}
//									if(map_curr_SEQ_sentiment.containsKey("2.0") ){
//										total_neg_sentiment=total_neg_sentiment+ Integer.valueOf( map_curr_SEQ_sentiment.get("2.0"));
//									}
	//								if(map_curr_SEQ_sentiment.containsKey("3.0") ){
	//									total_neg_sentiment=total_neg_sentiment+ Integer.valueOf( map_curr_SEQ_sentiment.get("3.0"));
	//								}
									// total sentiment of all sentences
									for(String senti:map_curr_SEQ_sentiment.keySet())
										total_no_sentence_preced_N_docs=total_no_sentence_preced_N_docs+Integer.valueOf( map_curr_SEQ_sentiment.get(senti) );
								}
								//** END sentiment
								// ** start 
							 
								// START (NUMBER , MONEY, YEAR)
								//CLEANED NUMBER
								map_2_arr_curr_SEQ_numbers_CLEANED=map_docIDrlineNo_numbersCSV.get(docID_preced);
								map_2_arr_curr_SEQ_RelatedWords4Numbers=map_docIDrlineNo_arrelatedWordsForNumbersCSV.get(docID_preced);
								
								TreeMap<String, Double> map_N_PREC_currDocsNUMBER=new TreeMap<String, Double>();
								TreeMap<String, Double> map_N_PREC_currDocsMONEY=new TreeMap<String, Double>();
								TreeMap<String, Double> map_N_PREC_currDocsYEAR=new TreeMap<String, Double>();
								// convert String array to Integer array (remove money part)
								mapTemp=convert_arrString_to_arrInteger_only_remove_money(map_2_arr_curr_SEQ_numbers_CLEANED, 
																						  writer_debug);
								//
								
								map_N_PREC_currDocsNUMBER=mapTemp.get(1); // NUMBER
								map_N_PREC_currDocsMONEY =mapTemp.get(2);  // MONEY
								map_N_PREC_currDocsYEAR  =mapTemp.get(3);   // YEAR
								
								sum_on_map_N_PREC_currDocsNUMBER=sum_on_map_N_PREC_currDocsNUMBER+map_N_PREC_currDocsNUMBER.size();
								sum_on_map_N_PREC_currDocsMONEY=sum_on_map_N_PREC_currDocsMONEY+map_N_PREC_currDocsMONEY.size();
								sum_on_map_N_PREC_currDocsYEAR=sum_on_map_N_PREC_currDocsYEAR+map_N_PREC_currDocsYEAR.size();
								 
								// START - FOCUS WORDS - N PRECED
//								{
//									currDocID_bodyText=map_DocID_bodytext.get(docID_preced);
//									//String []words=currDocID_bodyText.split(" ");
//									int cnt22=0;
//									for(String word:map_focusWords_AS_key.keySet()){
//										// 
//										if(currDocID_bodyText.toLowerCase().indexOf(word)>=0 
//												&& currDocID_bodyText.toLowerCase().indexOf(map_focusWords_AS_key.get(word))>=0 ){
//											sum_of_FocusWord_Count_N_PRECED_DocID_=sum_of_FocusWord_Count_N_PRECED_DocID_+1;
//										}
//										cnt++;
//									}
//								}
								
								
							}	
							// ** end
						
							//System.out.println("N PRECED STARTS max TFIDF value ");
							//BEGIN - AVERAGE TFIDF OF VERB (N PRECED DOCS) 
							{
								max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs=0; //max
								//PRECEDING VERB
								map_curr_SEQ_verbs_CSVwithSpace=map_lineNoNID_person_organiz_locati_numbers_nouns.get(docID_preced+":7");
								// BEGIN AVERAGE OF tfidf for VERB (N PRECED DOCS)
								arr_verb=map_curr_SEQ_verbs_CSVwithSpace.get("1").split(" ");
								c20=0;
								// 
								while(c20<arr_verb.length){
									String word=arr_verb[c20];
									
									double wordID=-1.;
									String stem_word=stemmer.stem(word);
									if(map_VocWord_WordID.containsKey(word))
										wordID=map_VocWord_WordID.get(word);
									
									// System.out.println(" word:"+word+" stem:"+stem_word);
									if(wordID<0){
										if(map_VocWord_WordID.containsKey(stem_word)){
											wordID=map_VocWord_WordID.get(stem_word);
										}
									}
									// 	
									if(wordID>0){ // AVERAGE TFIDF OF VERB (N PRECED DOCS)
										//int convert
										int wordID_int= (int) wordID;
										if(map_DocIDWordID_TFIDF.get(docID_preced +":" +  wordID_int )!=null){
											//sum TFIDF 
											sum_TF_IDF_on_verbs_FOR_N_PREC_DocID=sum_TF_IDF_on_verbs_FOR_N_PREC_DocID+
																			      Double.valueOf(map_DocIDWordID_TFIDF.get(docID_preced +":" +  wordID_int ));
											denom_count_number_of_verb++;
											 
											// maxvalue
											if(max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs==0)
												max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs=Double.valueOf(map_DocIDWordID_TFIDF.get(docID_preced +":" +  wordID_int ) );
											else if(Double.valueOf(map_DocIDWordID_TFIDF.get(docID_preced +":" +  wordID_int ) )>max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs  )
												max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs=Double.valueOf(map_DocIDWordID_TFIDF.get(docID_preced +":" +  wordID_int ) );
											
										}
										//	
									}
									c20++;
								}

							}
							// END - AVERAGE TF-IDF OF VERB (N PRECED DOCS)

							//System.out.println("N PRECED STARTS WIKIPEDIA -"+cnt_cnt);
							// BEGIN : WIKIPEDIA FOCUS WORD ( N PRECEDING DOC
							// bodyText
							String currDocID_bodyText_docID_preced=map_DocID_bodytext.get(docID_preced);
							currDocID_bodyText_docID_preced=clean_String( currDocID_bodyText_docID_preced);//cleaning 
							
							sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA=0;
							avg_sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA=0;
							
							map_Temp=new TreeMap<String, String>();
							map_Temp2=new TreeMap<String, String>();
							map_Temp3=new TreeMap<Integer, String>();
							// PRECED DOC BODYTEXTS
							arr_currDocID_word=currDocID_bodyText_docID_preced.toLowerCase().split(" ") ;
							cnt300=0;
							
							//each word
							while(cnt300<arr_currDocID_word.length){
								String word =arr_currDocID_word[cnt300];
								String stem_word= stemmer.stem(word);
								
								if(Stopwords.is_stopword(word)){ cnt300++; continue; };
								
								// 
								if( (map_Word_StemmedWord_WIKIPEDIA.containsKey(word)
									|| map_Word_StemmedWord_WIKIPEDIA.containsValue(stem_word) )
										&& !IsNumeric.isNumeric(word) //NOT NUMERIC
									){
									map_Temp.put(stem_word , "");
									sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA++;
									TOTAL_FocusWord_Count_currDocID_from_WIKIPEDIA++;
								}
								
								cnt300++;
							}
							
							cnt_cnt++;
							// END - FOCUS WORDS (WIKIPEDIA) - N PRECEDING DOC		
							//*** ONLY 1 
							if(cnt_cnt>1){ break; } // "1" means 2 document used ..NOTE cnt_cnt starts with 0
						} //END for(int docID_preced:map_preceding_N_document_docIDs_AS_KEY.keySet()){
						// ratio of negative sentiment sentence
						try{
							ratio_neg_senti_N_preced_docs_per_sentence=total_neg_sentiment/total_no_sentence_preced_N_docs;
						}catch(Exception e){}
						
						ratio_neg_senti_N_preced_docs_per_document=total_neg_sentiment / (double) cnt_cnt;
						avg_on_sum_of_FocusWord_Count_N_PRECED_DocID_=sum_of_FocusWord_Count_N_PRECED_DocID_ / (double) cnt_cnt;
						avg_sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA=sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA / (double) cnt_cnt;
						//VERB (AVERAGE) TFIDF
						avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID=sum_TF_IDF_on_verbs_FOR_N_PREC_DocID/(double)denom_count_number_of_verb;
						
						System.out.println("total_neg_sentiment:"+total_neg_sentiment +" cnt_cnt:"+cnt_cnt
											+" ratio_neg_senti_N_preced_docs_per_document:"+ratio_neg_senti_N_preced_docs_per_document
											 +" map_preceding_N_document_docIDs_AS_KEY:"+map_preceding_N_document_docIDs_AS_KEY.size()
											);
						//cur document
						double currDocID_Number_size=map_currDocsNUMBER.size();
						double currDocID_Money_size=map_currDocsMONEY.size();
						double currDocID_Year_size=map_currDocsYEAR.size();
						
						//average ( N preced )
						avg_on_sum_on_map_N_PREC_currDocsNUMBER=sum_on_map_N_PREC_currDocsNUMBER/(double)map_preceding_N_document_docIDs_AS_KEY.size();
						avg_on_sum_on_map_N_PREC_currDocsMONEY=sum_on_map_N_PREC_currDocsMONEY/(double)map_preceding_N_document_docIDs_AS_KEY.size();
						avg_on_sum_on_map_N_PREC_currDocsYEAR=sum_on_map_N_PREC_currDocsYEAR/(double)map_preceding_N_document_docIDs_AS_KEY.size();
						 
						System.out.println(   "1:"+currDocID_Number_size+" "+currDocID_Money_size+" "+currDocID_Year_size
											+" 2:"+avg_on_sum_on_map_N_PREC_currDocsNUMBER+" "+avg_on_sum_on_map_N_PREC_currDocsMONEY
											+" "+avg_on_sum_on_map_N_PREC_currDocsYEAR
											+" 3:focus->"+TOTAL_FocusWord_Count_currDocID_+" "+ avg_on_sum_of_FocusWord_Count_N_PRECED_DocID_
											);
						
						// OFFSET
						offset_on_average_on_total_COUNT_of_numbers_N_preceding_Docs=0.8;
						comp1=ratio_neg_senti_currDocID_per_document ;
						comp2=ratio_neg_senti_N_preced_docs_per_document;
						 
						double a1= 1/ (double) (1+currDoc_Verb_Filtered_as_KEY_size*currDoc_Verb_Filtered_as_KEY_size);
						double a2= 1/ (double) (1+avg_currDoc_N_PRECED_Verb_Filtered_as_KEY_size*avg_currDoc_N_PRECED_Verb_Filtered_as_KEY_size);
						 
						double b1= 1/ (double) (1+currDoc_Organiz_Filtered_as_KEY_size*currDoc_Organiz_Filtered_as_KEY_size);
						double b2= 1/ (double) (1+avg_currDoc_N_PRECED_Organiz_Filtered_as_KEY_size*avg_currDoc_N_PRECED_Organiz_Filtered_as_KEY_size);
						 
						System.out.println("comp1,comp2:"+comp1+" "+comp2+ " "+avg_organiz_currDocID_TFIDF+" "+avg_avg_organiz_N_prec_DocID_TFIDF);
						
						writer_debug.append("\ncomp1,comp2:"+comp1+" "+comp2+ " "+avg_organiz_currDocID_TFIDF+" "+avg_avg_organiz_N_prec_DocID_TFIDF
												+" 2.gt:"+map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)
												+" -------wikipedia.currdocid.map_Temp:"+map_Temp);
						writer_debug.flush();
						
						boolean cond_1_=false; boolean cond_1_a=false;
						
						boolean m3=(java.lang.Math.min(a1, b1)<offset_a1_b1*java.lang.Math.min(a2, b2));
						System.out.println("Predict");
						 // 
						if(( java.lang.Math.min(a1, b1)  < offset_a1_b1*java.lang.Math.min(a2, b2) )
							&&
							( ((currDocID_Number_size+currDocID_Money_size+currDocID_Year_size))
								< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER+avg_on_sum_on_map_N_PREC_currDocsMONEY+avg_on_sum_on_map_N_PREC_currDocsYEAR)))
								&& comp1>10)
							cond_1_=true;
						if (( java.lang.Math.min(a1, b1) < offset_a1_b1*java.lang.Math.min(a2, b2) )
							&&
							( ((currDocID_Number_size+currDocID_Money_size+currDocID_Year_size))
								< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER+avg_on_sum_on_map_N_PREC_currDocsMONEY+avg_on_sum_on_map_N_PREC_currDocsYEAR))
								))
							cond_1_a=true;
						
						// **** predict 
						if( // ( map_currDocsNUMBER.size() > 0||  map_currDocsYEAR.size()>0 || is_true_numbers_with_relatedWords==true )
								//&& (currDoc_Verb_Filtered_as_KEY_size > 1.2*avg_currDoc_N_PRECED_Verb_Filtered_as_KEY_size
								//&& 
							( java.lang.Math.min(a1, b1) < offset_a1_b1*java.lang.Math.min(a2, b2) )
							&&
							( ((currDocID_Number_size+currDocID_Money_size+currDocID_Year_size))
								< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER+avg_on_sum_on_map_N_PREC_currDocsMONEY+avg_on_sum_on_map_N_PREC_currDocsYEAR)))
								&& comp1>10
								//&& avg_organiz_currDocID_TFIDF  > avg_avg_organiz_N_prec_DocID_TFIDF
								//&& (a1 > a2)
								 //currDoc_Organiz_Filtered_as_KEY_size > 1.2*avg_currDoc_N_PRECED_Organiz_Filtered_as_KEY_size
								//&& (b1 > b2)
								//|| map_2_arr_curr_SEQ_RelatedWords4Numbers!=null
								){
							predict_for_curr_DocID=1;
							
							//relabel (tfidf)  (NOT RELIABLE) 
							if(avg_on_sum_TF_IDF_on_verbs_FOR_currDocID>avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID){ // <---> 
								total_count_relabel_1_verb_tfidf++;
								// relabel
								//predict_for_curr_DocID=0;
								if(map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)){
									total_count_relabel_1_verb_tfidf_in_GT_TP++;
								}
								else{
									total_count_relabel_1_verb_tfidf_NOT_in_GT_TP++;
								}
							}
							bool_reset_total_count_relabel_1=false;
							//relabel
//							if( ( java.lang.Math.min(a1, b1) < offset_a1_b1*java.lang.Math.min(a2, b2) ) &&
//								((currDocID_Number_size+currDocID_Money_size+currDocID_Year_size))
//								< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER+avg_on_sum_on_map_N_PREC_currDocsMONEY+avg_on_sum_on_map_N_PREC_currDocsYEAR))
//								&& comp1>20 &&comp1<45 ){
							if(currDocID_Number_size==0){ //for ebola this increases fP by 150
								total_count_relabel_1++;
								//relabel
								//predict_for_curr_DocID=0;
								bool_reset_total_count_relabel_1=true;
								
								// 
								if(map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)){
									total_count_relabel_1_in_GT_TP++;
								}
								else{
									total_count_relabel_1_NOT_in_GT_TP++;
								}
								
							}
							
							//
							if(!map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)){
								//
								if( java.lang.Math.min(a1, b1)  < offset_a1_b1*java.lang.Math.min(a2, b2) ){
									cond_11_fp++;
								}
								if(( ((currDocID_Number_size+currDocID_Money_size+currDocID_Year_size))
									< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER+avg_on_sum_on_map_N_PREC_currDocsMONEY+avg_on_sum_on_map_N_PREC_currDocsYEAR)))){
									cond_12_fp++;
								}
								if(comp1>10){
									cond_13_fp++;
								}
								//
								if(TOTAL_FocusWord_Count_currDocID_>FocusWord_Count_currDocID_OFFSET)//avg_on_sum_of_FocusWord_Count_N_PRECED_DocID_)
									cond_14_fp++;
								else{
								}
								
								String printStrin="\n 7.FP:"+FP+"--"
										+cond_11_fp+" "+cond_12_fp+" " +cond_13_fp+" focus:"+cond_14_fp
										+" currDocID:"+curr_DocID
										+" a1:"+a1+" a2:"+a2+" b1:"+b1+" b2:"+b2
										+" m1:"+java.lang.Math.min(a1, b1)+" m2:"+offset_a1_b1*java.lang.Math.min(a2, b2)
										+" m3:"+(java.lang.Math.min(a1, b1)<offset_a1_b1*java.lang.Math.min(a2, b2))
										+" m4:"+ ( ((currDocID_Number_size+currDocID_Money_size+currDocID_Year_size))
												< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER+avg_on_sum_on_map_N_PREC_currDocsMONEY+avg_on_sum_on_map_N_PREC_currDocsYEAR)))
										+" m5:"+(comp1>10)
										+" a1,a2:"+(a1>a2)
										+" b1,b2:"+(b1>b2)
										+" comp1:"+comp1
										+" c.num:"+currDocID_Number_size+" c.mon:"+currDocID_Money_size+" c.yr:"+currDocID_Year_size
										+" as.nprec.num:"+avg_on_sum_on_map_N_PREC_currDocsNUMBER
										+" as.nprec.mon:"+avg_on_sum_on_map_N_PREC_currDocsMONEY
										+" as.nprec.yr:"+ avg_on_sum_on_map_N_PREC_currDocsYEAR
										+" f.off:"+(TOTAL_FocusWord_Count_currDocID_>FocusWord_Count_currDocID_OFFSET)
										+" f.off5:"+(TOTAL_FocusWord_Count_currDocID_>5)
										+" f.off10:"+(TOTAL_FocusWord_Count_currDocID_>10)
										+" f.off15:"+(TOTAL_FocusWord_Count_currDocID_>15)
										+" f.off20:"+(TOTAL_FocusWord_Count_currDocID_>20)
										+" f.off25:"+(TOTAL_FocusWord_Count_currDocID_>25)
										+" f.off30:"+(TOTAL_FocusWord_Count_currDocID_>30)
										+" f.off40:"+(TOTAL_FocusWord_Count_currDocID_>40)
										+" f.off50:"+(TOTAL_FocusWord_Count_currDocID_>50)
										+" fcnt:" +cond_14_fn
										+" focus:"+TOTAL_FocusWord_Count_currDocID_
										+" offset:"+FocusWord_Count_currDocID_OFFSET
										+" avg_on_sum_TF_IDF_on_verbs_FOR_currDocID:"+avg_on_sum_TF_IDF_on_verbs_FOR_currDocID
										+" avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID:"+avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID
										+" condVtfidf:"+(avg_on_sum_TF_IDF_on_verbs_FOR_currDocID-avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID)
										+" condVtfidfbool:"+(avg_on_sum_TF_IDF_on_verbs_FOR_currDocID>avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID)
										+" max1:"+max_TF_IDF_on_verbs_FOR_currDocID 
										+" max2:"+max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs
										+" maxtfidfbool:"+(max_TF_IDF_on_verbs_FOR_currDocID > max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs)
										+" gtTP:"+map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)
										+" istfidf:"+istfidf
										+" b1_b2:"+b1_b2
										+" a1_a2:"+a1_a2
										+" cond_1_:"+cond_1_
										+" cond_1_a:"+cond_1_a
										+" bool_reset_total_count_relabel_1:"+bool_reset_total_count_relabel_1
										+" predict_for_curr_DocID:"+predict_for_curr_DocID
										+" Fword.wiki:"+sum_of_FocusWord_Count_curr_DocID_FROM_WIKIPEDIA
										+" Fword.wikiNPREC:"+sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA
										+" Fword.wikiNPREC(avg):"+avg_sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA
										+" Fword.bool:"+(sum_of_FocusWord_Count_curr_DocID_FROM_WIKIPEDIA>avg_sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA);
								
								// 
								writer_debug_currRUN_false_POSITIVE.append(printStrin);
								writer_debug.flush();
								
								writer_debug_currRUN_all.append(printStrin);
								writer_debug_currRUN_all.flush();
								
							}else{
								if( java.lang.Math.min(a1, b1)  < offset_a1_b1*java.lang.Math.min(a2, b2) ){
									cond_11_tp++;
								}
								if(( ((currDocID_Number_size+currDocID_Money_size+currDocID_Year_size))
									< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER+avg_on_sum_on_map_N_PREC_currDocsMONEY+avg_on_sum_on_map_N_PREC_currDocsYEAR)))){
									cond_12_tp++;
								}
								if(comp1>10){
									cond_13_tp++;
								}
								if(TOTAL_FocusWord_Count_currDocID_>FocusWord_Count_currDocID_OFFSET) //avg_on_sum_of_FocusWord_Count_N_PRECED_DocID_)
									cond_14_tp++;
								
								String printStrin="\n 7.TP:"+TP+"--"+cond_11_tp+" "+cond_12_tp+" " +cond_13_tp+" focus:"+cond_14_tp
										+" currDocID:"+curr_DocID
										+" a1:"+a1+" a2:"+a2+" b1:"+b1+" b2:"+b2
										+" m1:"+java.lang.Math.min(a1, b1)+" m2:"+offset_a1_b1*java.lang.Math.min(a2, b2)
										+" m3:"+(java.lang.Math.min(a1, b1)<offset_a1_b1*java.lang.Math.min(a2, b2))
										+" m4:"+ ( ((currDocID_Number_size+currDocID_Money_size+currDocID_Year_size))
												< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER+avg_on_sum_on_map_N_PREC_currDocsMONEY+avg_on_sum_on_map_N_PREC_currDocsYEAR)))
										+" m5:"+(comp1>10)
										+" a1,a2:"+(a1>a2)
										+" b1,b2:"+(b1>b2)
										+" comp1:"+comp1
										+" c.num:"+currDocID_Number_size+" c.mon:"+currDocID_Money_size+" c.yr:"+currDocID_Year_size
										+" as.nprec.num:"+avg_on_sum_on_map_N_PREC_currDocsNUMBER
										+" as.nprec.mon:"+avg_on_sum_on_map_N_PREC_currDocsMONEY
										+" as.nprec.yr:"+ avg_on_sum_on_map_N_PREC_currDocsYEAR
										+" f.off:"+(TOTAL_FocusWord_Count_currDocID_>FocusWord_Count_currDocID_OFFSET)
										+" f.off5:"+(TOTAL_FocusWord_Count_currDocID_>5)
										+" f.off10:"+(TOTAL_FocusWord_Count_currDocID_>10)
										+" f.off15:"+(TOTAL_FocusWord_Count_currDocID_>15)
										+" f.off20:"+(TOTAL_FocusWord_Count_currDocID_>20)
										+" f.off25:"+(TOTAL_FocusWord_Count_currDocID_>25)
										+" f.off30:"+(TOTAL_FocusWord_Count_currDocID_>30)
										+" f.off40:"+(TOTAL_FocusWord_Count_currDocID_>40)
										+" f.off50:"+(TOTAL_FocusWord_Count_currDocID_>50)
										+" fcnt:" +cond_14_fn
										+" focus:"+TOTAL_FocusWord_Count_currDocID_
										+" offset:"+FocusWord_Count_currDocID_OFFSET
										+" avg_on_sum_TF_IDF_on_verbs_FOR_currDocID:"+avg_on_sum_TF_IDF_on_verbs_FOR_currDocID
										+" avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID:"+avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID
										+" condVtfidf:"+(avg_on_sum_TF_IDF_on_verbs_FOR_currDocID-avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID)
										+" condVtfidfbool:"+(avg_on_sum_TF_IDF_on_verbs_FOR_currDocID>avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID)
										+" max1:"+max_TF_IDF_on_verbs_FOR_currDocID 
										+" max2:"+max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs
										+" maxtfidfbool:"+(max_TF_IDF_on_verbs_FOR_currDocID > max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs)
										+" gtTP:"+map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)
										+" istfidf:"+istfidf
										+" b1_b2:"+b1_b2
										+" a1_a2:"+a1_a2
										+" cond_1_:"+cond_1_
										+" cond_1_a:"+cond_1_a
										+" bool_reset_total_count_relabel_1:"+bool_reset_total_count_relabel_1
										+" predict_for_curr_DocID:"+predict_for_curr_DocID
										+" Fword.wiki:"+sum_of_FocusWord_Count_curr_DocID_FROM_WIKIPEDIA
										+" Fword.wikiNPREC:"+sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA
										+" Fword.wikiNPREC(avg):"+avg_sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA
										+" Fword.bool:"+(sum_of_FocusWord_Count_curr_DocID_FROM_WIKIPEDIA>avg_sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA)
										+" currDocID:"+curr_DocID;
								// 
								writer_debug_currRUN_true_POSITIVE.append(printStrin);
								writer_debug.flush();
								//
								writer_debug_currRUN_all.append(printStrin );
								writer_debug_currRUN_all.flush();
								
							}
								
							 
						}
						else{
							predict_for_curr_DocID=0;
							
							
 
//		Lenins-MacBook-Pro:fire.3 lenin$  cat currRUN_false_NEGATIVE.txt |grep 7.FN: |grep   idfbool:false |grep -v c.num:0.0 |grep a1,a2:true |wc -l
//		Lenins-MacBook-Pro:fire.3 lenin$ cat currRUN_false_NEGATIVE.txt |grep 7.FN: |grep   idfbool:false |grep -v c.num:0.0 |grep b1,b2:false |wc -l

							istfidf=(avg_on_sum_TF_IDF_on_verbs_FOR_currDocID>avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID);
							
							if(b1>b2) b1_b2=true;
							else 	  b1_b2=false;							
							if(a1>a2) a1_a2=true;
							else 	  a1_a2=false;

							//relabel (tfidf)
							//if(cond_1_a==false && avg_on_sum_TF_IDF_on_verbs_FOR_currDocID>avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID){
							if(   cond_1_==false // m3==false 
								&& istfidf==false && currDocID_Number_size>0.0  &&
								(b1_b2 ==false 
								&& a1_a2 ==true 
								)
									){
								//relabel
								//predict_for_curr_DocID=1;
								
								total_count_relabel_0_verb_tfidf++;
								
								writer_debug.append("\n avg_on_sum_TF_IDF_on_verbs_FOR_currDocID:"+avg_on_sum_TF_IDF_on_verbs_FOR_currDocID
													+" avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID:"+avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID
													+" gtTP:"+map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)
													);
								writer_debug.flush();
								
								if(map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)){
									total_count_relabel_0_verb_tfidf_in_GT_TP++;
								}
								else{
									total_count_relabel_0_verb_tfidf_NOT_in_GT_TP++;
								
								}
							}
							
							//relabel
							if(cond_1_a==false && comp1<=10){ 
								//predict_for_curr_DocID=1;
								total_count_relabel_0++;
								
								if(map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)){
									total_count_relabel_0_in_GT_TP++;
								}
								else{
									total_count_relabel_0_NOT_in_GT_TP++;
								}
								
							}
							

//							if(	
////									(  
////								( java.lang.Math.min(a1, b1)  > offset_a1_b1*java.lang.Math.min(a2, b2) ) &&
////									( ((currDocID_Number_size+currDocID_Money_size+currDocID_Year_size))
////									> ((avg_on_sum_on_map_N_PREC_currDocsNUMBER+avg_on_sum_on_map_N_PREC_currDocsMONEY+avg_on_sum_on_map_N_PREC_currDocsYEAR))
////									) )
////									&&
////									comp1<=10
////									){
//									currDocID_Number_size <avg_on_sum_on_map_N_PREC_currDocsNUMBER
//									){
//								//predict_for_curr_DocID=1;
//							}
//							else 
//								predict_for_curr_DocID=0;
							//
							if(map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)){
								
								// 
								if( java.lang.Math.min(a1, b1)  < java.lang.Math.min(a2, b2) )
									cond_11_fn++;
								if(( ((currDocID_Number_size))
									< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER))))
									cond_12_1_fn++;
								if((currDocID_Money_size<avg_on_sum_on_map_N_PREC_currDocsMONEY))
									cond_12_2_fn++;
								if((currDocID_Year_size<avg_on_sum_on_map_N_PREC_currDocsYEAR))
									cond_12_3_fn++;

								if(comp1>10){
									cond_13_fn++;
								}
								if(TOTAL_FocusWord_Count_currDocID_>FocusWord_Count_currDocID_OFFSET) //avg_on_sum_of_FocusWord_Count_N_PRECED_DocID_)
									cond_14_fn++;
								
								String printStrin="\n 7.FN:"+FN +"--"+ cond_11_fn
													+" "+ cond_12_1_fn +" out of(fn_tn)"+(cond_12_1_fn+cond_12_1_tn)
													+" "+cond_12_2_fn  +" out of(fn_tn)"+(cond_12_2_fn+cond_12_2_tn)
													+" "+cond_12_3_fn +" out of(fn_tn)"+(cond_12_3_fn+cond_12_3_tn)
													+" " +cond_13_fn +" out of (fn+tn)"+(cond_13_fn+cond_13_tn)
													+" currDocID:"+curr_DocID
													+" a1:"+a1+" a2:"+a2+" b1:"+b1+" b2:"+b2
													+" a1,a2:"+(a1>a2)
													+" b1,b2:"+(b1>b2)
													+" m1:"+java.lang.Math.min(a1, b1)+" m2:"+offset_a1_b1*java.lang.Math.min(a2, b2)
													+" m3:"+(java.lang.Math.min(a1, b1)<offset_a1_b1*java.lang.Math.min(a2, b2))
													+" m4:"+ ( ((currDocID_Number_size+currDocID_Money_size+currDocID_Year_size))
															< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER+avg_on_sum_on_map_N_PREC_currDocsMONEY+avg_on_sum_on_map_N_PREC_currDocsYEAR)))
													+" m5:"+(comp1>10)
													+" comp1:"+comp1
													+" c.num:"+currDocID_Number_size+" c.mon:"+currDocID_Money_size+" c.yr:"+currDocID_Year_size
													+" as.nprec.num:"+avg_on_sum_on_map_N_PREC_currDocsNUMBER
													+" as.nprec.mon:"+avg_on_sum_on_map_N_PREC_currDocsMONEY
													+" as.nprec.yr:"+ avg_on_sum_on_map_N_PREC_currDocsYEAR
													+" f.off:"+(TOTAL_FocusWord_Count_currDocID_>FocusWord_Count_currDocID_OFFSET)
													+" f.off5:"+(TOTAL_FocusWord_Count_currDocID_>5)
													+" f.off10:"+(TOTAL_FocusWord_Count_currDocID_>10)
													+" f.off15:"+(TOTAL_FocusWord_Count_currDocID_>15)
													+" f.off20:"+(TOTAL_FocusWord_Count_currDocID_>20)
													+" f.off25:"+(TOTAL_FocusWord_Count_currDocID_>25)
													+" f.off30:"+(TOTAL_FocusWord_Count_currDocID_>30)
													+" f.off40:"+(TOTAL_FocusWord_Count_currDocID_>40)
													+" f.off50:"+(TOTAL_FocusWord_Count_currDocID_>50)
													+" fcnt:" +cond_14_fn
													+" focus:"+TOTAL_FocusWord_Count_currDocID_
													+" offset:"+FocusWord_Count_currDocID_OFFSET
													+" avg_on_sum_TF_IDF_on_verbs_FOR_currDocID:"+avg_on_sum_TF_IDF_on_verbs_FOR_currDocID
													+" avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID:"+avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID
													+" condVtfidf:"+(avg_on_sum_TF_IDF_on_verbs_FOR_currDocID-avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID)
													+" condVtfidfbool:"+(avg_on_sum_TF_IDF_on_verbs_FOR_currDocID>avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID)
													+" max1:"+max_TF_IDF_on_verbs_FOR_currDocID 
													+" max2:"+max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs
													+" maxtfidfbool:"+(max_TF_IDF_on_verbs_FOR_currDocID > max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs)
													+" gtTP:"+map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)
													+" istfidf:"+istfidf
													+" b1_b2:"+b1_b2
													+" a1_a2:"+a1_a2
													+" cond_1_:"+cond_1_
													+" cond_1_a:"+cond_1_a
													+" bool_reset_total_count_relabel_1:"+bool_reset_total_count_relabel_1
													+" predict_for_curr_DocID:"+predict_for_curr_DocID
													+" Fword.wiki:"+sum_of_FocusWord_Count_curr_DocID_FROM_WIKIPEDIA
													+" Fword.wikiNPREC:"+sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA
													+" Fword.wikiNPREC(avg):"+avg_sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA
													+" Fword.bool:"+(sum_of_FocusWord_Count_curr_DocID_FROM_WIKIPEDIA>avg_sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA)
													+" currDocID:"+curr_DocID;
								
								writer_debug_currRUN_false_NEGATIVE.append( printStrin );
								writer_debug.flush();
								
								writer_debug_currRUN_all.append(printStrin);
								writer_debug_currRUN_all.flush();
								
							}else{

								if( java.lang.Math.min(a1, b1)  < offset_a1_b1*java.lang.Math.min(a2, b2) )
									cond_11_tn++;
//								if(( ((currDocID_Number_size+currDocID_Money_size+currDocID_Year_size))
//									< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER+avg_on_sum_on_map_N_PREC_currDocsMONEY+avg_on_sum_on_map_N_PREC_currDocsYEAR))))
//									cond_12_tn++;
								if(( ((currDocID_Number_size))
										< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER))))
										cond_12_1_tn++;
									if((currDocID_Money_size<avg_on_sum_on_map_N_PREC_currDocsMONEY))
										cond_12_2_tn++;
									if((currDocID_Year_size<avg_on_sum_on_map_N_PREC_currDocsYEAR))
										cond_12_3_tn++;								
									if(comp1>10)
										cond_13_tn++;
									if(TOTAL_FocusWord_Count_currDocID_> FocusWord_Count_currDocID_OFFSET ) //avg_on_sum_of_FocusWord_Count_N_PRECED_DocID_)
										cond_14_tn++;
								
								String printStrin="\n 7.TN:"+TN+"--"+cond_11_tn+" "+cond_12_1_tn+" "+cond_12_2_tn+" " 
										+cond_12_3_tn+" "+cond_13_tn+" focus:"+cond_14_tn
										+" currDocID:"+curr_DocID
										+" a1:"+a1+" a2:"+a2+" b1:"+b1+" b2:"+b2
										+" a1,a2:"+(a1>a2)
										+" b1,b2:"+(b1>b2)
										+" m1:"+java.lang.Math.min(a1, b1)+" m2:"+offset_a1_b1*java.lang.Math.min(a2, b2)
										+" m3:"+(java.lang.Math.min(a1, b1)<offset_a1_b1*java.lang.Math.min(a2, b2))
										+" m4:"+ ( ((currDocID_Number_size+currDocID_Money_size+currDocID_Year_size))
												< ((avg_on_sum_on_map_N_PREC_currDocsNUMBER+avg_on_sum_on_map_N_PREC_currDocsMONEY+avg_on_sum_on_map_N_PREC_currDocsYEAR)))
										+" m5:"+(comp1>10)
										+" comp1:"+comp1
										+" c.num:"+currDocID_Number_size+" c.mon:"+currDocID_Money_size+" c.yr:"+currDocID_Year_size
										+" as.nprec.num:"+avg_on_sum_on_map_N_PREC_currDocsNUMBER
										+" as.nprec.mon:"+avg_on_sum_on_map_N_PREC_currDocsMONEY
										+" as.nprec.yr:"+ avg_on_sum_on_map_N_PREC_currDocsYEAR
										+" f.off:"+(TOTAL_FocusWord_Count_currDocID_>FocusWord_Count_currDocID_OFFSET)
										+" f.off5:"+(TOTAL_FocusWord_Count_currDocID_>5)
										+" f.off10:"+(TOTAL_FocusWord_Count_currDocID_>10)
										+" f.off15:"+(TOTAL_FocusWord_Count_currDocID_>15)
										+" f.off20:"+(TOTAL_FocusWord_Count_currDocID_>20)
										+" f.off25:"+(TOTAL_FocusWord_Count_currDocID_>25)
										+" f.off30:"+(TOTAL_FocusWord_Count_currDocID_>30)
										+" f.off40:"+(TOTAL_FocusWord_Count_currDocID_>40)
										+" f.off50:"+(TOTAL_FocusWord_Count_currDocID_>50)
										+" fcnt:" +cond_14_fn
										+" focus:"+TOTAL_FocusWord_Count_currDocID_
										+" offset:"+FocusWord_Count_currDocID_OFFSET
										+" avg_on_sum_TF_IDF_on_verbs_FOR_currDocID:"+avg_on_sum_TF_IDF_on_verbs_FOR_currDocID
										+" avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID:"+avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID
										+" condVtfidf:"+(avg_on_sum_TF_IDF_on_verbs_FOR_currDocID-avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID)
										+" condVtfidfbool:"+(avg_on_sum_TF_IDF_on_verbs_FOR_currDocID>avg_on_sum_TF_IDF_on_verbs_FOR_N_PREC_DocID)
										+" max1:"+max_TF_IDF_on_verbs_FOR_currDocID 
										+" max2:"+max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs
										+" maxtfidfbool:"+(max_TF_IDF_on_verbs_FOR_currDocID > max_TF_IDF_on_verbs_FOR_N_PRECED_DocIDs)
										+" gtTP:"+map_NodeName_N_DocID_groundTruthTP.containsValue( curr_DocID)
										+" istfidf:"+istfidf
										+" b1_b2:"+b1_b2
										+" a1_a2:"+a1_a2
										+" cond_1_:"+cond_1_
										+" cond_1_a:"+cond_1_a
										+" bool_reset_total_count_relabel_1:"+bool_reset_total_count_relabel_1
										+" predict_for_curr_DocID:"+predict_for_curr_DocID
										+" Fword.wiki:"+sum_of_FocusWord_Count_curr_DocID_FROM_WIKIPEDIA
										+" Fword.wikiNPREC:"+sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA
										+" Fword.wikiNPREC(avg):"+avg_sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA
										+" Fword.bool:"+(sum_of_FocusWord_Count_curr_DocID_FROM_WIKIPEDIA>avg_sum_of_FocusWord_Count_N_PRECED_DocID_FROM_WIKIPEDIA)
										+" currDocID:"+curr_DocID;
								//
								writer_debug_currRUN_true_NEGATIVE.append(printStrin);
								writer_debug.flush();
								
								writer_debug_currRUN_all.append(printStrin);
								writer_debug_currRUN_all.flush();
							}
							
						}
						 
						
						// each token
//						while(cnt<arr_word.length){
//							//SKIP NULL
//							if(arr_word[cnt]==null){cnt++; continue; } 
//							if(arr_word[cnt]!=null){
//								if(arr_word[cnt].length()==0) {cnt++; continue; } 
//							}
//							int cnt2=0;
//							 
//							//
//							while(cnt2<arr_word2.length){
//								//skip NULL
//								if(arr_word2[cnt2]==null){cnt2++; continue; } 
//								if(arr_word2[cnt2]!=null){
//									if(arr_word2[cnt2].length()==0) {cnt2++; continue; } 
//								}
//								//change lower
//								arr_word2[cnt2]=arr_word2[cnt2].toLowerCase();
//								//remove end . such as Monday.
//								if(arr_word2[cnt2].lastIndexOf(".")==arr_word2[cnt2].length()-1){
//									arr_word2[cnt2]=arr_word2[cnt2].substring(0, arr_word2[cnt2].lastIndexOf("."));
//								}
//								// stopwords
//								if(stopwords.is_stopword(arr_word2[cnt2])){cnt2++; continue;}
//								//STEM
//								String curr_stem_word=stemmer2.stem(arr_word2[cnt2]).toLowerCase();
//
//								System.out.println("arr_word2[cnt2]:"+arr_word2[cnt2] + " stemmed:" +curr_stem_word);
//								
//								if(map_NounWord_N_arr_lineNo.containsKey(curr_stem_word)){
//									System.out.println("found in noun->"+curr_stem_word);
//									writer_debug.append("\nfound in noun->"+curr_stem_word+" for "+curr_DocID);
//								}
//								
//								if(map_VerbWord_N_arr_lineNo.containsKey(curr_stem_word)){
//									System.out.println("found in verb->"+curr_stem_word);
//									writer_debug.append("\nfound in verb->"+curr_stem_word+" for "+curr_DocID);
//								}
//								
//								if(map_OrganizWord_N_arr_lineNo.containsKey(curr_stem_word)){
//									System.out.println("found in organiz->"+curr_stem_word);
//									writer_debug.append("\nfound in organiz->"+curr_stem_word+" for "+curr_DocID);
//								}
//								
//								if(map_LocationWord_N_arr_lineNo.containsKey(curr_stem_word)){
//									System.out.println("found in location->"+curr_stem_word);
//									writer_debug.append("\nfound in location->"+curr_stem_word+" for "+curr_DocID);
//								}
//								
//								if(map_PersonWord_N_arr_lineNo.containsKey(curr_stem_word)){
//									System.out.println("found in person->"+curr_stem_word);
//									writer_debug.append("\nfound in person->"+curr_stem_word+" for "+curr_DocID);
//								}
//								//
//								if(map_NumberWord_N_arr_lineNo.containsKey(curr_stem_word)){
//									System.out.println("found in number->"+curr_stem_word);
//									writer_debug.append("\nfound in number->"+curr_stem_word+" for "+curr_DocID+" size:"+map_stat_no_DocID_found_as_key.size());
//									map_stat_no_DocID_found_as_key.put(curr_DocID, "");
//								}
//								//
//								writer_debug.flush();
//								cnt2++;
//							}
//							cnt++;
//						} // END while(cnt<arr_word.length){
						
						
					} // END else if(classify_method_type.equalsIgnoreCase("7")){
					
					if(isSOPprint)
						System.out.println("------1.4-out 20>11");
//				}
//				else{
//					writer_debug.append("\n ERROR: cant find DocId->"+(curr_DocID-1)+" inside "+map_DocID_GraphPropValue_from_NetworkX_prop1);
//					writer_debug.flush();
//				}
//				}
					if(isSOPprint)
						System.out.println("------1.4-out 20> 0000 21211");
					String print4="";
					
					//  PRINT conditions
					if(  	count_number_of_organization_FOR_currDocID >  offset_average_on_total_COUNT_of_organization_N_preceding_Docs
							  *average_on_total_COUNT_of_organization_N_preceding_Docs ){
						 rule_4_a_GLOBAL++;
					}
					else{
						 rule_4_b_GLOBAL++;
					}
					//AND
					if(  	count_number_of_organization_FOR_currDocID >  offset_average_on_total_COUNT_of_organization_N_preceding_Docs
							  *average_on_total_COUNT_of_organization_N_preceding_Docs 
							  && comp1 > comp2 ){
						rule_4_a_AND_a1_GLOBAL++;
					}
					print4=print4+"\n rule_4_a_AND_a1_GLOBAL:"+rule_4_a_AND_a1_GLOBAL;
					//
					if(comp1 > comp2 ){
						rule_4_a1_GLOBAL++;
					}
					else{
						rule_4_b1_GLOBAL++;
					}
					//AND
					if(  	  comp1 > comp2  &&
							average_on_count_number_of_verbs_FOR_currDocID > 1.3*  
							 average_on_count_number_of_verbs_FOR_curr_preceding_DocID ){
						rule_4_a1_AND_a3_GLOBAL++;
					}
					print4=print4+"\n rule_4_a1_AND_a3_GLOBAL:"+rule_4_a1_AND_a3_GLOBAL;
					//  
					if( average_on_count_number_of_verbs_FOR_currDocID > 1.3*  
							 average_on_count_number_of_verbs_FOR_curr_preceding_DocID ){
						 rule_4_a3_GLOBAL++;
					}
					else{
						rule_4_b3_GLOBAL++;
					}
					// AND
					if(average_on_count_number_of_verbs_FOR_currDocID > 1.3* average_on_count_number_of_verbs_FOR_curr_preceding_DocID &&
							count_number_of_distinct_sentiment_for_curr_DocID>average_on_total_count_number_of_distinct_sentiment_N_preced_docs){
						rule_4_a3_AND_a2_GLOBAL++;
					}
					print4=print4+"\n rule_4_a3_AND_a2_GLOBAL:"+rule_4_a3_AND_a2_GLOBAL;
					//
					if(count_number_of_distinct_sentiment_for_curr_DocID>average_on_total_count_number_of_distinct_sentiment_N_preced_docs){
						rule_4_a2_GLOBAL++;
					}
					else{
						rule_4_b2_GLOBAL++;
					}
					
				// confusion matrix
				if(map_NodeName_N_DocID_groundTruthTP.containsKey(currNodeName) && predict_for_curr_DocID==1
					&& !map_NodeNames_DocID_OUT_ofContext.containsValue(curr_DocID ) ){
					
					try{
						//:1 -> person, :2 -> organization, :3 -> location, :4 -> numbers, :6 -> sentiments, :5 -> nouns, :7 -> Verbs
						writer_debug_currRUN_true_POSITIVE.append("\n DocID->"+ curr_DocID
																	+"!!!NodeName:"+currNodeName
																	+"!!!person:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":1"))
																	+"!!!organization:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":2"))
																	+"!!!location:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":3"))
																	+"!!!numbers:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":4"))
																	+"!!!nouns:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":5"))
																	+"!!!sentiments:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":6"))
																	+"!!!Verbs:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":7"))
																	+"!!!"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":8"))
																	+"!!!URL:"+ map_DocID_URL.get(curr_DocID));
						
						writer_debug_currRUN_true_POSITIVE.append("\n compare:"+comp1+" "+comp2);
						writer_debug_currRUN_true_POSITIVE.append(print1);
						writer_debug_currRUN_true_POSITIVE.append(print2+"\n------------ true positive -------\n");
						
						writer_debug_currRUN_true_POSITIVE.flush();
					}
					catch(Exception e){
					}
					TP++;
				}
				else if(!map_NodeName_N_DocID_groundTruthTP.containsKey(currNodeName) && predict_for_curr_DocID==0 
						&& !map_NodeNames_DocID_OUT_ofContext.containsValue(curr_DocID )){
					TN++;
					try{
						writer_debug_currRUN_true_NEGATIVE.append("\n DocID->"+ curr_DocID
																+"!!!NodeName:"+currNodeName
																+"!!!person:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":1"))
																+"!!!organization:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":2"))
																+"!!!location:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":3"))
																+"!!!numbers:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":4"))
																+"!!!nouns:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":5"))
																+"!!!sentiments:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":6"))
																+"!!!Verbs:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":7"))
																+"!!!"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":8"))
																+"!!!URL:"+ map_DocID_URL.get(curr_DocID));
						writer_debug_currRUN_true_NEGATIVE.append("\n compare:"+comp1+" "+comp2);
						writer_debug_currRUN_true_NEGATIVE.append(print1);
						writer_debug_currRUN_true_NEGATIVE.append(print2+"\n-------------------\n");
						writer_debug_currRUN_true_NEGATIVE.append(" print3:"+print3);
						
						
						//  PRINT
						if(  	count_number_of_organization_FOR_currDocID >  offset_average_on_total_COUNT_of_organization_N_preceding_Docs
								  *average_on_total_COUNT_of_organization_N_preceding_Docs 
								
								){
							rule_4_a_tn++;
						}
						else{
							rule_4_b_tn++;
						}
						//
						if(comp1 > comp2 ){
							rule_4_a1_tn++;
						}
						else{
							rule_4_b1_tn++;
						}
						//
						if( average_on_count_number_of_verbs_FOR_currDocID > offset_average_on_count_number_of_verbs_FOR_curr_preceding_DocID 
								* average_on_count_number_of_verbs_FOR_curr_preceding_DocID )
							rule_4_a3_tn++;
						else
							rule_4_b3_tn++;
						//
						if(count_number_of_distinct_sentiment_for_curr_DocID>average_on_total_count_number_of_distinct_sentiment_N_preced_docs)
							rule_4_a2_tn++;
						else
							rule_4_b2_tn++;
						// 
						writer_debug_currRUN_true_NEGATIVE.append("\n rule_4_a_tn:(TN)"+rule_4_a_tn+" rule_4_b_tn:"+rule_4_b_tn);
						writer_debug_currRUN_true_NEGATIVE.append("\n rule_4_a1_tn:"+rule_4_a1_tn+" rule_4_b1_tn:"+rule_4_b1_tn);
						writer_debug_currRUN_true_NEGATIVE.append("\n rule_4_a2_tn:"+rule_4_a2_tn+" rule_4_b2_tn:"+rule_4_b2_tn);
						writer_debug_currRUN_true_NEGATIVE.append("\n rule_4_a3_tn:"+rule_4_a3_tn+" rule_4_b3_tn:"+rule_4_b3_tn);
						writer_debug_currRUN_true_NEGATIVE.append("\n cond_1:"+cond_1+" cond_2:"+cond_2+" cond_3:"+cond_3);
						writer_debug_currRUN_true_NEGATIVE.flush();
						writer_debug_currRUN_true_NEGATIVE.append("\n------------true negative------------\n");
						writer_debug_currRUN_true_NEGATIVE.flush();
					}
					catch(Exception e){
						
					}
				}
				else if(map_NodeName_N_DocID_groundTruthTP.containsKey(currNodeName) && predict_for_curr_DocID==0 
						&& !map_NodeNames_DocID_OUT_ofContext.containsValue(curr_DocID )){
					FN++;
					try{
						writer_debug_currRUN_false_NEGATIVE.append("\n DocID->"+ curr_DocID
																    +"!!!NodeName:"+currNodeName
																	+"!!!person:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":1"))
																	+"!!!organization:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":2"))
																	+"!!!location:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":3"))
																	+"!!!numbers:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":4"))
																	+"!!!nouns:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":5"))
																	+"!!!sentiments:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":6"))
																	+"!!!Verbs:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":7"))
																	+"!!!"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":8"))
																	+"!!!URL:"+ map_DocID_URL.get(curr_DocID));
						writer_debug_currRUN_false_NEGATIVE.append("\n compare:"+comp1+" "+comp2);
						writer_debug_currRUN_false_NEGATIVE.append(print1);
						writer_debug_currRUN_false_NEGATIVE.append(print2+"\n %%%%%%%%%%%%%%%%%%%\n");
						writer_debug_currRUN_false_NEGATIVE.append(" print3:"+print3);
						writer_debug_currRUN_true_NEGATIVE.append("\n print4:"+print4);
						//  PRINT
						if(  	count_number_of_organization_FOR_currDocID >  offset_average_on_total_COUNT_of_organization_N_preceding_Docs
								  *average_on_total_COUNT_of_organization_N_preceding_Docs 
								
								){
							rule_4_a++; 
						}
						else{
							rule_4_b++; 
						}

						//
						if(comp1 > comp2 ){
							rule_4_a1++; 
						}
						else{
							rule_4_b1++; 
						}
												
						//
						if( average_on_count_number_of_verbs_FOR_currDocID > offset_average_on_count_number_of_verbs_FOR_curr_preceding_DocID 
								* average_on_count_number_of_verbs_FOR_curr_preceding_DocID ){
							rule_4_a3++; 
						}
						else{
							rule_4_b3++; 
						}
						//
						if(count_number_of_distinct_sentiment_for_curr_DocID>average_on_total_count_number_of_distinct_sentiment_N_preced_docs){
							rule_4_a2++;  
						}
						else{
							rule_4_b2++; 
						}
						 
						writer_debug_currRUN_false_NEGATIVE.append("\n rule_4_a:(organiz)(FN)"+rule_4_a+" rule_4_b:"+rule_4_b
																	+" rule_4_a_GLOBAL:"+rule_4_a_GLOBAL+" rule_4_b_GLOBAL:"+rule_4_b_GLOBAL);
						writer_debug_currRUN_false_NEGATIVE.append("\n rule_4_a1:(comp):"+rule_4_a1+" rule_4_b1:"+rule_4_b1
																	+" rule_4_a1_GLOBAL:"+rule_4_a1_GLOBAL+" rule_4_b1_GLOBAL:"+rule_4_b1_GLOBAL);
						writer_debug_currRUN_false_NEGATIVE.append("\n rule_4_a2(sentim):"+rule_4_a2+" rule_4_b2:"+rule_4_b2
																		+" rule_4_a2_GLOBAL:"+rule_4_a2_GLOBAL+" rule_4_b2_GLOBAL:"+rule_4_b2_GLOBAL);
						writer_debug_currRUN_false_NEGATIVE.append("\n rule_4_a3(verb):"+rule_4_a3+" rule_4_b3:"+rule_4_b3
																		+" rule_4_a3_GLOBAL:"+rule_4_a3_GLOBAL+" rule_4_b3_GLOBAL:"+rule_4_b3_GLOBAL);
						writer_debug_currRUN_false_NEGATIVE.append("\n cond_1:"+cond_1+" cond_2:"+cond_2+" cond_3:"+cond_3);
						writer_debug_currRUN_false_NEGATIVE.append("\n print4:"+print4);
						
						writer_debug_currRUN_false_NEGATIVE.flush();
						writer_debug_currRUN_false_NEGATIVE.append("\n--------False negative-----------\n");
						writer_debug_currRUN_false_NEGATIVE.flush();
						
					}
					catch(Exception e){
					}
					
				}
				else if(!map_NodeName_N_DocID_groundTruthTP.containsKey(currNodeName) && predict_for_curr_DocID==1 
						&& !map_NodeNames_DocID_OUT_ofContext.containsValue(curr_DocID )){
					
					try{
						writer_debug_currRUN_false_POSITIVE.append("\n DocID->"+ curr_DocID
																	+"!!!NodeName:"+currNodeName
																	+"!!!person:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":1"))
																	+"!!!organization:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":2"))
																	+"!!!location:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":3"))
																	+"!!!numbers:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":4"))
																	+"!!!nouns:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":5"))
																	+"!!!sentiments:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":6"))
																	+"!!!Verbs:"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":7"))
																	+"!!!"+map_lineNoNID_person_organiz_locati_numbers_nouns.get(String.valueOf(curr_DocID+":8"))
																	+"!!!URL:"+ map_DocID_URL.get(curr_DocID)
																	+"!!!:" +map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_GLOBAL.get(currNodeName)
																	);
						writer_debug_currRUN_false_POSITIVE.append("\n compare:"+comp1+" "+comp2);
						writer_debug_currRUN_false_POSITIVE.append(print1);
						writer_debug_currRUN_false_POSITIVE.append(print2+"\n---------false positive----------\n");
						
						writer_debug_currRUN_false_POSITIVE.flush();
					}
					catch(Exception e){
					}
					FP++;
				}
				//
				System.out.println("TP:"+TP+" TN:"+TN+" FP:"+FP+" FN:"+FN+" total mathed doc:"+total_no_documents_curr_query);
				
				if(isSOPprint)
					System.out.println("------1.4-out 20>121211");
				
				// ground truth TP
				if(map_NodeName_N_DocID_groundTruthTP.containsKey(currNodeName)){
					if(map_curr_person_WordNtfidf != null){
						stat_groundTruth_TP_docs_having_person_Count++;
						stat_groundTruth_TP_docs_having_person_Count_TOTAL=stat_groundTruth_TP_docs_having_person_Count_TOTAL+map_curr_person_WordNtfidf.size();
					}
					if(map_curr_organiz_WordIDNtfidf!= null){
						stat_groundTruth_TP_docs_having_organiz_Count++;
						stat_groundTruth_TP_docs_having_organiz_Count_TOTAL=stat_groundTruth_TP_docs_having_organiz_Count_TOTAL+map_curr_organiz_WordIDNtfidf.size();
					}
					if(map_curr_location_WordIDNtfidf!= null){
						stat_groundTruth_TP_docs_having_location_Count++;
						stat_groundTruth_TP_docs_having_location_Count_TOTAL=stat_groundTruth_TP_docs_having_location_Count_TOTAL+map_curr_location_WordIDNtfidf.size();
					}
					if(map_curr_numbers_WordIDNtfidf!= null){
						stat_groundTruth_TP_docs_having_numbers_Count++;
						stat_groundTruth_TP_docs_having_numbers_Count_TOTAL=stat_groundTruth_TP_docs_having_numbers_Count_TOTAL+map_curr_numbers_WordIDNtfidf.size();
					}
					if(isSOPprint)
						System.out.println("------1.4-out 20> 0000 111");
				}
				// NOT in ground truth TP
				if(!map_NodeName_N_DocID_groundTruthTP.containsKey(currNodeName)){
					if(map_curr_person_WordNtfidf!=null){
						stat_NOT_groundTruth_TP_docs_having_person_Count++;
						stat_NOT_groundTruth_TP_docs_having_person_Count_TOTAL=stat_NOT_groundTruth_TP_docs_having_person_Count_TOTAL+map_curr_person_WordNtfidf.size();
					}
					if(map_curr_organiz_WordIDNtfidf!=null){
						stat_NOT_groundTruth_TP_docs_having_organiz_Count++;
						stat_NOT_groundTruth_TP_docs_having_organiz_Count_TOTAL=stat_NOT_groundTruth_TP_docs_having_organiz_Count_TOTAL+map_curr_organiz_WordIDNtfidf.size();
					}
					if(map_curr_location_WordIDNtfidf!=null){
						stat_NOT_groundTruth_TP_docs_having_location_Count++;
						stat_NOT_groundTruth_TP_docs_having_location_Count_TOTAL=stat_NOT_groundTruth_TP_docs_having_location_Count_TOTAL+map_curr_location_WordIDNtfidf.size();
					}
					if(map_curr_numbers_WordIDNtfidf!=null){
						stat_NOT_groundTruth_TP_docs_having_numbers_Count++;
						stat_NOT_groundTruth_TP_docs_having_numbers_Count_TOTAL=stat_NOT_groundTruth_TP_docs_having_numbers_Count_TOTAL+map_curr_numbers_WordIDNtfidf.size();
					}
					if(isSOPprint)
						System.out.println("------1.4-out 20> 0000 11121");
				}
				if(isSOPprint)
					System.out.println("------1.4-out 20> 0000 HHHH");
	
			} //END for(String currNodeName:map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.keySet()){
			
			  System.out.println("------------------ for curr query ->"+curr_query+" no docs procssed for curr query:"
					  				+map_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP.size());
			  total=TP+TN+FP+FN;
			  String print_metric="TP:"+TP+" TN:"+TN+" FP:"+FP+" FN:"+FN;
			  System.out.println();
			  // debug
			  map_currQuery_TNTPFNFP.put(curr_query, print_metric);
			  
			  precision=TP/(double)(TP+FP);
			  recall=TP/(double)(TP+FN);
			  F1=2*(precision*recall)/(precision+recall);
			  System.out.println("precision:"+precision+" recall:"+recall+ " F1:"+ F1  +" accuracy:"+(TP+TN)/(double) total);
			  System.out.println("------------------ --------");
			  
			  writer_debug.append("\n\n relabel: total_count_relabel_1_in_GT_TP:"+total_count_relabel_1_in_GT_TP+" total_count_relabel_1_NOT_in_GT_TP:"
					  				+total_count_relabel_1_NOT_in_GT_TP+" OUT OF TOTAL="+total_count_relabel_1);
			  writer_debug.append("\n "+"relabel: total_count_relabel_0_in_GT_TP:"+total_count_relabel_0_in_GT_TP+" total_count_relabel_0_NOT_in_GT_TP:"
			  						 +total_count_relabel_0_NOT_in_GT_TP+" OUT OF TOTAL="+total_count_relabel_0+"\n");
			  
//			  writer_debug.append("\n verb TFIDF \n\n"+"total_count_relabel_0_verb_tfidf:"+total_count_relabel_0_verb_tfidf
//					  					+" total_count_relabel_1_verb_tfidf:"+total_count_relabel_1_verb_tfidf+"\n");
			  
			  writer_debug.append("\n\n verb TFIDF \n\n total_count_relabel_1_verb_tfidf_in_GT_TP:"+total_count_relabel_1_verb_tfidf_in_GT_TP
					  				+" total_count_relabel_1_verb_tfidf_NOT_in_GT_TP:"+total_count_relabel_1_verb_tfidf_NOT_in_GT_TP
					  				+" OUT OF TOTAL="+total_count_relabel_1_verb_tfidf+
					  				"\n");
				
				writer_debug.append("\n\n total_count_relabel_0_verb_tfidf_in_GT_TP:"+total_count_relabel_0_verb_tfidf_in_GT_TP
										+" total_count_relabel_0_verb_tfidf_NOT_in_GT_TP:"+total_count_relabel_0_verb_tfidf_NOT_in_GT_TP
										+" OUT OF TOTAL="+total_count_relabel_0_verb_tfidf
										+"\n");
			  
			  writer_debug.append("\n query(metric):"+curr_query+" "+ 
					  				"precision:"+precision+" recall:"+recall+ " F1:"+ F1  +" accuracy:"+(TP+TN)/(double) total
					  				+" for offset_a1_b1:"+offset_a1_b1);
			  writer_debug.append("\n TP:"+TP+" TN:"+TN+" FP:"+FP+" FN:"+FN);
			  writer_debug.flush();
			  
			  writer_debug_currRUN_true_POSITIVE.append("\n TP:"+TP);writer_debug_currRUN_true_POSITIVE.flush();
			  writer_debug_currRUN_true_NEGATIVE.append("\n TN:"+TN);writer_debug_currRUN_true_NEGATIVE.flush();
			  writer_debug_currRUN_false_POSITIVE.append("\n FP:"+FP);writer_debug_currRUN_false_POSITIVE.flush();
			  writer_debug_currRUN_false_NEGATIVE.append("\n FN:"+FN);writer_debug_currRUN_false_NEGATIVE.flush();
			  
			  map_ID_N_Metric.put( curr_query+":prec" , precision);
			  map_ID_N_Metric.put( curr_query+":recall" , recall);
			  map_ID_N_Metric.put( curr_query+":F1" , F1);
			  
			  map_Query_ID_N_Metric.put(curr_query, map_ID_N_Metric);
			  map_ID_N_Metric=new TreeMap<String, Double>(); //reset

			  total_precision=total_precision+precision;
			  total_recall=total_recall+recall;
		      total_F1=total_F1+F1;
		      //
		      total_TP=total_TP+TP;
		      total_TN=total_TN+TN;
		      total_FP=total_FP+FP;
		      total_FN=total_FN+FN;
		      
		      TP=0;TN=0; FP=0;FN=0;
		      
		      denom_total_count_query_for_metric++;
		} //END for(String curr_query: map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.keySet()){
			 
			  average_on_total_precision=total_precision/map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.size();
			  average_on_total_recall=total_recall/map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.size();
			  average_on_total_F1=total_F1/map_currQury_AND_NodeName_N_DocID_bothgroundTruthTPAND_NOTgroundtruthTP_only_curr_QUERY.size();
			  
			  //debug print
			  for(Integer docID:map_docID_N_FocusWord_TFIDF.keySet()){
				  writer_debug.append("\ndocID:"+docID
						  				+" word,tfidf:"+map_docID_N_FocusWord_TFIDF.get(docID)+"(size="+map_docID_N_FocusWord_TFIDF.get(docID).size()+") "
						  				+" dummy(no value):"+map_docID_N_FocusWord_dummy.get(docID)+"(size="+map_docID_N_FocusWord_dummy.get(docID).size()+")"
						  				+" wordid,tfidf:"+map_docID_N_FocusWordID_TFIDF.get(docID)+"(size="+map_docID_N_FocusWordID_TFIDF.get(docID).size()+") "
						  				+" gtTP:"+map_NodeName_N_DocID_groundTruthTP.containsValue(docID)
						  				//+" currbodyText:"+ map_DocID_bodytext.get(docID)
						  				//+" map_docID_N_tempbodyText.get():"+map_docID_N_tempbodyText.get(docID)
						  				);
				  writer_debug.flush();
			  
			  }
			   
			  
			  //debug to see if correctly loaded
			  if(map_docID_N_tempbodyText.get(100)!=null){
				  if(map_docID_N_tempbodyText.get(100).length()>1001)
					  writer_debug.append("debug 100 print:"+map_docID_N_tempbodyText.get(100).substring(0, 1000));
				  else
					  writer_debug.append("debug 100 print:"+map_docID_N_tempbodyText.get(100));
			  }
			  else if(map_docID_N_tempbodyText.get(10000)!=null){
				   
				  //debug print
				  if(map_docID_N_tempbodyText.get(10000).length()>1001)
					  writer_debug.append("debug 10000 print:"+map_docID_N_tempbodyText.get(10000).substring(0, 1000));
				  else
					  writer_debug.append("debug 10000 print:"+map_docID_N_tempbodyText.get(10000));
			  }
			  else{
				  writer_debug.append("debug 10000 print: map_docID_N_tempbodyText  is NULL");
			  }
				
			  writer_debug.append("\n map_DocID_bodytext:"+map_DocID_bodytext);
			  writer_debug.flush();
			
			System.out.println("----------Average  Precision, Recall, F1 -----start---");
			System.out.println("average_on_total_precision:"+average_on_total_precision+" average_on_total_recall:"+average_on_total_recall
								+ " average_on_total_F1:"+ average_on_total_F1);
			
			System.out.println("----------Average  Precision, Recall, F1 -----end---");
			System.out.println("----------statistics -----start---ground truth TP documents having person, organizaiton, ");
			System.out.println("Below person, organiz, location, numbers (in ground truth TP)");
			System.out.println(stat_groundTruth_TP_docs_having_person_Count+"--"+stat_groundTruth_TP_docs_having_organiz_Count+"--"+stat_groundTruth_TP_docs_having_location_Count
								+"--"+stat_groundTruth_TP_docs_having_numbers_Count);
			System.out.println("Below person, organiz, location, numbers (NOT in ground truth TP)");
			System.out.println(stat_NOT_groundTruth_TP_docs_having_person_Count+"--"+stat_NOT_groundTruth_TP_docs_having_organiz_Count
					            +"--"+stat_NOT_groundTruth_TP_docs_having_location_Count
								+"--"+stat_NOT_groundTruth_TP_docs_having_numbers_Count);
			System.out.println("Below person, organiz, location, numbers <- averaging (in ground truth TP)");
			System.out.println(stat_groundTruth_TP_docs_having_person_Count_TOTAL/(double)stat_groundTruth_TP_docs_having_person_Count 
						+"--"+stat_groundTruth_TP_docs_having_organiz_Count_TOTAL/(double)stat_groundTruth_TP_docs_having_organiz_Count
						+"--"+stat_groundTruth_TP_docs_having_location_Count_TOTAL/(double)stat_groundTruth_TP_docs_having_location_Count
					    +"--"+stat_groundTruth_TP_docs_having_numbers_Count_TOTAL/(double)stat_groundTruth_TP_docs_having_numbers_Count);
			System.out.println("Below person, organiz, location, numbers <- averaging (NOT in ground truth TP)");
			System.out.println(stat_NOT_groundTruth_TP_docs_having_person_Count_TOTAL/(double)stat_NOT_groundTruth_TP_docs_having_person_Count
							+"--"+stat_NOT_groundTruth_TP_docs_having_organiz_Count_TOTAL/(double)stat_NOT_groundTruth_TP_docs_having_organiz_Count
							+"--"+stat_NOT_groundTruth_TP_docs_having_location_Count_TOTAL/(double)stat_NOT_groundTruth_TP_docs_having_location_Count
							+"--"+stat_NOT_groundTruth_TP_docs_having_numbers_Count_TOTAL/(double)stat_NOT_groundTruth_TP_docs_having_numbers_Count);
			
			System.out.println("----------statistics -----end");

			writer_debug_currRUN_false_POSITIVE.append("\n ----- offset ------\n");
			writer_debug_currRUN_false_POSITIVE.append( "\n offset_on_average_on_total_COUNT_of_numbers_N_preceding_Docs:"+
														offset_on_average_on_total_COUNT_of_numbers_N_preceding_Docs);
			writer_debug_currRUN_false_POSITIVE.append( "\n offset_average_on_total_COUNT_of_organization_N_preceding_Docs:"+
														offset_average_on_total_COUNT_of_organization_N_preceding_Docs);
			writer_debug_currRUN_false_POSITIVE.append( "\n offset_average_on_count_number_of_verbs_FOR_curr_preceding_DocID:"+
														offset_average_on_count_number_of_verbs_FOR_curr_preceding_DocID);
			
			writer_debug.append(" \n (map_Query_ID_N_Metric) precision, recall, F1 - > \n"+map_Query_ID_N_Metric);
			
			
			writer_debug.append("\n\n ***Average on all queries: TP:"+total_TP/(double)denom_total_count_query_for_metric +" TN:"+total_TN/(double)denom_total_count_query_for_metric
								+" FP:"+total_FP/(double)denom_total_count_query_for_metric+" FN:"+total_FN/(double)denom_total_count_query_for_metric
									);
			writer_debug.append("\n ***Total on all queries: total_TP:"+total_TP+" total_TN:"+total_TN+" total_FP:"+total_FP+" total_FN:"+total_FN +"\n");
			
			writer_debug_currRUN_false_NEGATIVE.append("\n ***Total on all queries: total_TP:"+total_TP+" total_TN:"+total_TN+" total_FP:"+total_FP+" total_FN:"+total_FN +"\n");
			writer_debug_currRUN_false_NEGATIVE.flush();
			
			// debug (each query, metric print)	
			for(String currQuery:map_currQuery_TNTPFNFP.keySet()){
				writer_debug.append("\n For Query="+currQuery+"-->"+map_currQuery_TNTPFNFP.get(currQuery));
				System.out.println(" For Query="+currQuery+"-->"+map_currQuery_TNTPFNFP.get(currQuery));
				//false negative
				writer_debug_currRUN_false_NEGATIVE.append("\n For Query="+currQuery+"-->"+map_currQuery_TNTPFNFP.get(currQuery));
				writer_debug_currRUN_false_NEGATIVE.flush();
			}
			writer_debug.flush();
			System.out.println("\n ***Total on all queries: total_TP:"+total_TP+" total_TN:"+total_TN+" total_FP:"+total_FP+" total_FN:"+total_FN +"\n");
			 
			System.out.println("sum_curr_DocID_organiz_TFIDF_Top_N_:"+sum_curr_DocID_location_TopN_TFIDF+" "+sum_curr_DocID_numbers_TopN_TFIDF
					     		+" "+sum_curr_DocID_organiz_TopN_TFIDF+" "+sum_curr_DocID_person_TopN_TFIDF);
			
			if(is_YES_skip_edge_normalization){
				System.out.println("*** U skipped is_YES_skip_edge_normalization->"+is_YES_skip_edge_normalization);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		finally{
			
				try {
					if(writer_debug!=null)
						writer_debug.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	 
	//
	private static TreeMap<Integer, Double> sort_AND_get_TOP_N_(
												TreeMap<String, Double> map_curr_numbers_WordIDNtfidf,
												int top_N_TFIDF_only,
												double offset_for_TFIDF,
												String debug_Label,
												String  debugFile
												) {
		// TODO Auto-generated method stub
		HashMap<String, Double> map_curr_numbers_WordNtfidf_SORTED=new HashMap<String, Double>();
		TreeMap<Integer, Double> mapOut=new TreeMap<Integer,Double>();
		
		double sum_numbers_curr_DocID_TFIDF_Top_N_=0;
		double average_on_sum_numbers_curr_DocID_TFIDF_Top_N_=0;
		FileWriter writer_debug=null;
		try{
			writer_debug=new FileWriter(new File(debugFile), true);
			// sort
			map_curr_numbers_WordNtfidf_SORTED=Sort_given_treemap.sortByValue_SD_method4_ascending(map_curr_numbers_WordIDNtfidf);
			
			int cnt2=0; int count_sum=0; 
			// sum of top N TF-IDF
			for(String wordNumbers:map_curr_numbers_WordNtfidf_SORTED.keySet()){
				// 
				if(map_curr_numbers_WordNtfidf_SORTED.get(wordNumbers) <= offset_for_TFIDF){
					cnt2++;
					//System.out.println("sorting:"+wordNumbers+" "+sum_numbers_curr_DocID_TFIDF_Top_N_+" "+debug_Label);
					if(cnt2>top_N_TFIDF_only)
						break;
					else{
						sum_numbers_curr_DocID_TFIDF_Top_N_=sum_numbers_curr_DocID_TFIDF_Top_N_+map_curr_numbers_WordNtfidf_SORTED.get(wordNumbers);
						count_sum++;
					}
					writer_debug.append("\n passed offset_for_TFIDF:"+wordNumbers+" debug_Label="+debug_Label);
				}
				else{
					writer_debug.append("\n **NOT passed offset_for_TFIDF:"+wordNumbers+" actual offset:"+map_curr_numbers_WordNtfidf_SORTED.get(wordNumbers)
												+" debug_Label="+debug_Label);
				}
				
				writer_debug.flush();
			}
			// 
			average_on_sum_numbers_curr_DocID_TFIDF_Top_N_=sum_numbers_curr_DocID_TFIDF_Top_N_/ (double) count_sum;
			
			if(String.valueOf(average_on_sum_numbers_curr_DocID_TFIDF_Top_N_).indexOf("NaN") >=0){
				average_on_sum_numbers_curr_DocID_TFIDF_Top_N_=0.0;
			}
			
			mapOut.put( 1,  sum_numbers_curr_DocID_TFIDF_Top_N_ );
			mapOut.put( 2,  average_on_sum_numbers_curr_DocID_TFIDF_Top_N_ );
			
		}
		catch(Exception e){
		}
		finally{
			try {
				if(writer_debug!=null)
					writer_debug.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mapOut;
	}

	// count_numbers_for_currDocID
	private static int count_numbers_for_currDocID(TreeMap<String, String> map_curr_SEQ_numbers) {
		// TODO Auto-generated method stub
		int count=0;
		boolean istrue=true;
		//int b=-1;
		try{
			
			for(String seq:map_curr_SEQ_numbers.keySet()){
				String curr_num=map_curr_SEQ_numbers.get(seq);
				istrue=true;
						
				if( curr_num.indexOf(":")>=0  || curr_num.indexOf("-")>=0){
					istrue=false;
					
				}
				char a= curr_num.charAt(0);
				String h=String.valueOf(a);
				try{
					//b=Integer.valueOf(a);
					istrue=IsNumeric.isNumeric( h );
				}
				catch(Exception e){
					istrue=false;
				}
				
				if(istrue  ) {
					//System.out.println("numbers:"+curr_num +" b="+h);
					count++;
				}
				
			}
			
		}
		catch(Exception e){
			
		}
		return count;
	}

	//wrapper_to_get_only_root_news_nodes
	public static TreeMap<Integer, String> wrapper_to_get_only_root_news_nodes_R_allNodes(	String baseFolder,
																							String inFile_GBAD,
																							int    Flag // {1=return all nodes, 2=return only root news nodes, 3 = only ground truth}
																						 ){
				BufferedReader reader=null; String line="";
				TreeMap<Integer, String> map=new TreeMap<Integer, String>();
				TreeMap<Integer, String> mapNodeID_NodeName=new TreeMap<Integer, String>();
				TreeMap<Integer, String> mapNodeID_NodeName_only_groundTruth=new TreeMap<Integer, String>();
				TreeMap<Integer, String> map_NewsRootNodeID_NodeName=get_rootNodes_of_all_news_articles(mapNodeID_NodeName);
				
				FileWriter writer_debug=null;
				try{
					reader = new BufferedReader(new FileReader(inFile_GBAD));
					writer_debug = new FileWriter(new File(inFile_GBAD+"_debug.txt"));

					while ((line = reader.readLine()) != null) {
						//
						if(line.indexOf("v ") >=0 ){
							
							String s[] = line.split(" ");
							//System.out.println("node="+line+" "+ s.length);
							
							if(s.length==3){
								
								mapNodeID_NodeName.put(Integer.valueOf(s[1]) , s[2]);
								 
							}
							else{
								int cnt=0; String concLine="";
								//
								while(cnt<s.length){
									if(cnt>1){ /// 
										if(concLine.length()==0)
											concLine=s[cnt];
										else
											concLine=concLine+" "+ s[cnt];
									}
									cnt++;
								}
								mapNodeID_NodeName.put(Integer.valueOf(s[1]) , concLine);
							}
						}
					}
					int no_root_node=0;
					for(int nodeID:mapNodeID_NodeName.keySet()){
						int count_=Find_occurance_Count_Substring.find_occurance_Count_Substring("aa", mapNodeID_NodeName.get(nodeID));
						if(count_==2){
							no_root_node++;
//							// root node for Article (each)
//							System.out.println("node:"+ mapNodeID_NodeName.get(nodeID)
//										+" IN:"+mapEdge_NodeID1_NodeID2_IN.get(nodeID)
//										+" OUT:"+mapEdge_NodeID1_NodeID2_OUT.get(nodeID)
		//
//										);
							map_NewsRootNodeID_NodeName.put(nodeID, mapNodeID_NodeName.get(nodeID));
						}
						
					}
					if(Flag==1)
						map=mapNodeID_NodeName;
					else if(Flag==2)
						map=map_NewsRootNodeID_NodeName;
				
					System.out.println("number root news nodes:"+no_root_node+" Flag:"+Flag
										+" map.size="+map.size());
				}
		    catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		   }
		   return map;
	}
	
	//  convert_GBAD_to_getIN_R_Out_Edge_Also_expand_to_neighours (main) for loading root News Nodes.
	//map.get(1) <- map_SS_NewsRootNodeID_NodeName (only root node)
	//map.get(2) <- map_SS_NodeID_NodeName (all)
	//map.get(3) <- map_SS_NodePair_EdgeValue (all)
	public static TreeMap<Integer, TreeMap<String, String>> convert_GBAD_to_getIN_R_Out_Edge_Also_expand_to_neighours( 	String  baseFolder,
																			 											String  inFile,
																			 											int     run_for_TOP_N_article,
																			 											int     MAX_no_hops,
																			 											boolean isSOPprint
																														){
		TreeMap<Integer, TreeMap<String, String>> mapOut2=new TreeMap<Integer, TreeMap<String,String>>();
		BufferedReader reader=null; String line="";
		TreeMap<Integer, String> map=new TreeMap<Integer, String>();
		TreeMap<Integer, String> mapNodeID_NodeName=new TreeMap<Integer, String>();
		
		TreeMap<Integer, List<Integer>> mapEdge_global_NodeID1_NodeID2_IN=new TreeMap<Integer, List<Integer>>();
		TreeMap<Integer, List<Integer>> mapEdge_global_NodeID1_NodeID2_OUT=new TreeMap<Integer, List<Integer>>();
		TreeMap<String, Float > NodePair_N_Score=new TreeMap<String, Float>();
		TreeMap<Integer, String> map_NewsRootNodeID_NodeName=get_rootNodes_of_all_news_articles(mapNodeID_NodeName);
		//fr output
		TreeMap<String,String> map_SS_NewsRootNodeID_NodeName=new TreeMap<String, String>();
		TreeMap<String, String> map_SS_NodeID_NodeName=new TreeMap<String, String>();
		TreeMap<String, String> map_SS_NodePair_EdgeValue=new TreeMap<String, String>();
		
		FileWriter writer_debug=null;
		FileWriter writer_NodeID_outnodes_innodes=null;
		try{
			reader = new BufferedReader(new FileReader(inFile));
			writer_debug = new FileWriter(new File(inFile+"_debug.txt"));
			writer_NodeID_outnodes_innodes = new FileWriter(new File(inFile+"_NodeID_corresp_OUTnodes_INnodes.txt"));

			while ((line = reader.readLine()) != null) {
			//
			if(line.indexOf("v ") >=0 ){
				
				String s[] = line.split(" ");
				//System.out.println("node="+line+" "+ s.length);
				
				if(s.length==3){
					mapNodeID_NodeName.put(Integer.valueOf(s[1]) , s[2]);
				}
				else{
					int cnt=0; String concLine="";
					//
					while(cnt<s.length){
						if(cnt>1){ /// 
							if(concLine.length()==0)
								concLine=s[cnt];
							else
								concLine=concLine+" "+ s[cnt];
						}
						cnt++;
					}
					mapNodeID_NodeName.put(Integer.valueOf(s[1]) , concLine);
				}
			}
			//edge
			if(line.indexOf("d ") ==0 ){
				
				String s[] = line.split(" ");
				 
				//System.out.println("edge="+line+" "+ s.length);
				if(s.length>=3){
					
					
					// OUT degree
					if(mapEdge_global_NodeID1_NodeID2_IN.containsKey(Integer.valueOf(s[1]))){
						List ls=mapEdge_global_NodeID1_NodeID2_IN.get(Integer.valueOf(s[1]));
						ls.add( Integer.valueOf( Integer.valueOf(s[2])  ) );
						//source to dest
						mapEdge_global_NodeID1_NodeID2_IN.put(Integer.valueOf(s[1]), ls );
					}
					else{
						 List ls=new ArrayList<Integer>();
						 ls.add(Integer.valueOf(s[2]));
						 mapEdge_global_NodeID1_NodeID2_IN.put(Integer.valueOf(s[1]), ls ); 
					}
					// IN degree
					if(mapEdge_global_NodeID1_NodeID2_OUT.containsKey(Integer.valueOf(s[2]))){
						List ls=mapEdge_global_NodeID1_NodeID2_OUT.get(Integer.valueOf(s[2]));
						ls.add( Integer.valueOf( Integer.valueOf(s[1])  ) );
						//source to dest
						mapEdge_global_NodeID1_NodeID2_OUT.put(Integer.valueOf(s[2]), ls );
					}
					else{
						 List ls=new ArrayList<Integer>();
						 ls.add(Integer.valueOf(s[1]));
						 mapEdge_global_NodeID1_NodeID2_OUT.put(Integer.valueOf(s[2]), ls ); 
					}
					
					
					//System.out.println(s[3].replace("\"", "") );
					
					NodePair_N_Score.put(Integer.valueOf(s[1]) +":" +Integer.valueOf(s[2]),
										 Float.valueOf(s[3].replace("\"", "")  ) );
					NodePair_N_Score.put(Integer.valueOf(s[2]) +":" +Integer.valueOf(s[1]), //REVERSE 
							 			Float.valueOf(s[3].replace("\"", "")  ) );
					
				}
				else{
					
//					int cnt=0; String concLine="";
//					//
//					while(cnt<s.length){
//						if(cnt>1){ /// 
//							if(concLine.length()==0)
//								concLine=s[cnt];
//							else
//								concLine=concLine+" "+ s[cnt];
//						}
//						cnt++;
//					}
//					Edge_NodeID1_NodeID2.put(Integer.valueOf(s[1]) , concLine);
					
				}
				
			} //end edge 
			
		}
			//System.out.println("map1.:"+mapNodeID_NodeName+" \nmap2:"+mapEdge_NodeID1_NodeID2_IN);
			
			if(isSOPprint)
			System.out.println("\nmap1.:NodeID_NodeName:"+mapNodeID_NodeName.size()
							+" \nmap2:Edge_NodeID1_NodeID2_OUT:"+mapEdge_global_NodeID1_NodeID2_OUT.size()
							+" \nmap2:Edge_NodeID1_NodeID2_IN:"+mapEdge_global_NodeID1_NodeID2_IN.size());
			
			writer_NodeID_outnodes_innodes.append("nodeID!!!nodeIDNodeNAME!!!Corresp_OUTnodes!!!Corresp_INnodes!!!Corresp_OUTnodesNames!!!Corresp_INnodesNames\n");
			writer_NodeID_outnodes_innodes.flush();
			//WRITING OUT NODE
			for(int a:mapEdge_global_NodeID1_NodeID2_IN.keySet()){
				//System.out.println("\n OUT of node "+a+" -> "+mapEdge_NodeID1_NodeID2_IN.get(a) 
				//+" IN->"+mapEdge_NodeID1_NodeID2_OUT.get(a));
				writer_debug.append( "\n OUT nodes of node "+a+" -> "+mapEdge_global_NodeID1_NodeID2_IN.get(a) 
									+" IN nodes of node->"+a+"-->"+mapEdge_global_NodeID1_NodeID2_OUT.get(a));
				writer_debug.flush();
				String tmp_IN="", tmp_OUT="",
				tmp_IN_NodeName="",tmp_OUT_NodeName="";
				//
				if(mapEdge_global_NodeID1_NodeID2_IN.get(a)==null){
					tmp_IN="";tmp_IN_NodeName="";
				}else {
					tmp_IN=mapEdge_global_NodeID1_NodeID2_IN.get(a).toString().replace("[", "").replace("]", "").replace(", ", ",");
					tmp_IN_NodeName="";
					
					List<Integer> list_in=mapEdge_global_NodeID1_NodeID2_IN.get(a);
					Iterator itr=list_in.iterator();
					//
					while(itr.hasNext()){
						int j=(Integer) itr.next();
						
						if(tmp_IN_NodeName==null) tmp_IN_NodeName="";
						if(tmp_IN_NodeName.length()==0){
							tmp_IN_NodeName=mapNodeID_NodeName.get(j);	
						}
						else
							tmp_IN_NodeName=tmp_IN_NodeName+","+mapNodeID_NodeName.get(j);
						
					}
					
				}
				//tmp_OUT
				if(mapEdge_global_NodeID1_NodeID2_OUT.get(a)==null){
					tmp_OUT="";tmp_OUT_NodeName="";
				}else {
					tmp_OUT=mapEdge_global_NodeID1_NodeID2_OUT.get(a).toString().replace("[", "").replace("]", "").replace(", ", ",");
					
					tmp_OUT_NodeName="";
					
					List<Integer> list_in=mapEdge_global_NodeID1_NodeID2_OUT.get(a);
					Iterator itr=list_in.iterator();
					//
					while(itr.hasNext()){
						int j=(Integer) itr.next();
						
						if(tmp_OUT_NodeName.length()==0){
							tmp_OUT_NodeName=mapNodeID_NodeName.get(j);	
						}
						else
							tmp_OUT_NodeName=tmp_OUT_NodeName+","+mapNodeID_NodeName.get(j);
						
					}	
				}
				//naming convention is reverse , not confus
				writer_NodeID_outnodes_innodes.append(a+"!!!"+mapNodeID_NodeName.get(a)+"!!!"+tmp_IN+"!!!"+tmp_OUT+"!!!"+tmp_IN_NodeName+"!!!"+tmp_OUT_NodeName+"\n");
				writer_NodeID_outnodes_innodes.flush();
				
				tmp_OUT="";tmp_OUT_NodeName="";
				tmp_IN="";tmp_IN_NodeName="";
			}
			int no_root_node=0;
			
			//get all root nodes of all news articels
			map_NewsRootNodeID_NodeName=get_rootNodes_of_all_news_articles(mapNodeID_NodeName);
			
			//out
			map_SS_NewsRootNodeID_NodeName=convert_map_IS_SS_(map_NewsRootNodeID_NodeName);
			mapOut2.put(1, map_SS_NewsRootNodeID_NodeName);
			map_SS_NodeID_NodeName=convert_map_IS_SS_(mapNodeID_NodeName);
			mapOut2.put(2, map_SS_NodeID_NodeName);
			map_SS_NodePair_EdgeValue=convert_map_SF_SS_(NodePair_N_Score);
			mapOut2.put(3, map_SS_NodePair_EdgeValue);

	
			System.out.println("-----------------------------------");
			
			TreeMap<Integer, String> map_interestedNode=new TreeMap<Integer, String>();
			//s=map_NewsRootNodeID_NodeName;
			 TreeMap<Integer, TreeMap<String,Integer>> mapOut=new TreeMap<Integer, TreeMap<String,Integer>>();
			 //hop to HOP jump
			 int cnt_article_count=0;
			 String [] s=new String[0];
			 TreeMap<Integer, TreeMap<Integer, String>> map_curr_hop_N_interestedNodes_IN=new TreeMap<Integer,TreeMap<Integer, String>>();
			 TreeMap<Integer, TreeMap<Integer, String>> map_curr_hop_N_interestedNodes_OUT=new TreeMap<Integer,TreeMap<Integer, String>>();
			 //run for each root node (each article root node)
			 for(int root_node_ID:map_NewsRootNodeID_NodeName.keySet()){
				 cnt_article_count++;
				 map_interestedNode=new TreeMap<Integer, String>();
			//	 map_interestedNode.put(i, -1);
				 map_interestedNode.put(root_node_ID,  mapNodeID_NodeName.get(root_node_ID)  );
				 TreeMap<Integer, String> map_interestedNode_new_IN=new TreeMap<Integer, String>();
				 TreeMap<Integer, String> map_interestedNode_new_OUT=new TreeMap<Integer, String>();
				 	 // 
					 if(cnt_article_count>run_for_TOP_N_article){
						 System.out.println(" top N break ");
						 break;
					 }
				    writer_debug.append("\n----------------------------cnt_article_count-"+cnt_article_count);
				    writer_debug.flush();
					 //int MAX_no_hops=2; 
					 int curr_hop=1;
					// MAX_no_hops
					while(curr_hop<=MAX_no_hops){
						writer_debug.append("\n----------------------------curr_hop:"+curr_hop);
						//HOP
						if(curr_hop==1){
							// do N hops and get IN or OUT nodes
							mapOut=DO_n_hops_get_IN(map_interestedNode,
												    mapEdge_global_NodeID1_NodeID2_OUT,
												    isSOPprint
													);
						}
						else{
							mapOut=DO_n_hops_get_IN( map_interestedNode_new_IN,
													 mapEdge_global_NodeID1_NodeID2_OUT,
													 isSOPprint
													 );
							
						}
						if(isSOPprint)
						System.out.println("curr_hop:"+curr_hop+" IN:"+mapOut.get(1));
						if(isSOPprint)
						System.out.println("curr_hop:"+ curr_hop +" OUT:"+mapOut.get(2));
						//map_interestedNode= mapOut.get(1);
						//NEW (curr_hop>=1) comes here
						if(mapOut.get(1).size()>0){
							map_interestedNode_new_IN=new TreeMap<Integer, String>();
							for(String i:mapOut.get(1).keySet()){
								map_interestedNode_new_IN.put( mapOut.get(1).get(i), mapNodeID_NodeName.get(mapOut.get(1).get(i)) );
							}
							map_curr_hop_N_interestedNodes_IN.put(curr_hop, map_interestedNode_new_IN);
						}
						else if(curr_hop==1){ // 
							map_curr_hop_N_interestedNodes_IN.put(curr_hop, map_interestedNode);
						}
						writer_debug.append("\ncurr_hop(IN):"+curr_hop+" mapOut.get(1).SIZE:"+mapOut.get(1).size()+" mapOut.1.:"+mapOut.get(1)+" s.len:"+//s.length+
											"\n\n"+" map_interestedNode_new_IN:"+map_interestedNode_new_IN+"\nmap_interestedNode:"+map_interestedNode
											+"\n");
						writer_debug.flush();
						//
						if(curr_hop==1){
							// 
							mapOut=DO_n_hops_get_OUT(map_interestedNode,
									 				mapEdge_global_NodeID1_NodeID2_IN
									 			   );
						}
						else{
							mapOut=DO_n_hops_get_OUT(map_interestedNode_new_OUT,
					 								mapEdge_global_NodeID1_NodeID2_IN
					 			   					);
							
						}
						//NEW (curr_hop>=1) comes here 
						if(mapOut.get(1).size()>0){
							map_interestedNode_new_IN=new TreeMap<Integer, String>();
							for(String i:mapOut.get(1).keySet()){
								map_interestedNode_new_OUT.put( mapOut.get(1).get(i) , 
																mapNodeID_NodeName.get(mapOut.get(1).get(i)) );
							}
							map_curr_hop_N_interestedNodes_OUT.put(curr_hop,
																   map_interestedNode_new_IN);
						}
						else if(curr_hop==1){
							map_curr_hop_N_interestedNodes_OUT.put(curr_hop, map_interestedNode);
						}
						
						// writer to debug
						writer_debug.append("\ncurr_hop(OUT):"+curr_hop+"mapOut.get(1).SIZE:"+mapOut.get(1).size()+" mapOut.1.:"+mapOut.get(1)
											+" for root_node_ID="+root_node_ID
											+"\n\n");
						writer_debug.flush();
						
						curr_hop++;
					}//WHILE while(curr_hop<=MAX_no_hops){
			 } // end for(int root_node_ID:map_NewsRootNodeID_NodeName.keySet()){ 
			 
			 //at each hop, how many outward nodes we got.
			 for(int curr_hop:map_curr_hop_N_interestedNodes_IN.keySet()){
				 //for(int ss:map_curr_hop_N_interestedNodes.get(curr_hop).keySet()){
					 System.out.println("curr_hop->"+ curr_hop+" "+ map_curr_hop_N_interestedNodes_IN.get(curr_hop).size() );	 
				 //}
			 }
			
			System.out.println("no_root_node:"+no_root_node+"--"+map_NewsRootNodeID_NodeName);
//			System.out.println("\nmapEdge_global_NodeID1_NodeID2_IN:"+mapOut.get(1));
//			System.out.println("\nmapEdge_global_NodeID1_NodeID2_OUT:"+mapOut.get(2));
			System.out.println("\n--------------------------------");
			writer_debug.append("\nmapEdge_global_NodeID1_NodeID2_IN<OUTWARD nodes of node_1 is FROM node_2>:"+mapEdge_global_NodeID1_NodeID2_IN.size()+"<--->"
								+mapEdge_global_NodeID1_NodeID2_IN);
			writer_debug.append("\nmapEdge_global_NodeID1_NodeID2_OUT<INWARD nodes of node_1 is FROM node_2>:"+ mapEdge_global_NodeID1_NodeID2_OUT.size()+ "<--->"
								+mapEdge_global_NodeID1_NodeID2_OUT);
			writer_debug.flush();
			
			if(isSOPprint)
			System.out.println("all nodes: "+mapNodeID_NodeName.size() +" map_NewsRootNodeID_NodeName:"+map_NewsRootNodeID_NodeName.size()
								+" news node(total):"+map_NewsRootNodeID_NodeName.size()
								+" DO_n_hops_get_IN_OR_OUT_nodes(1,2):"+mapOut.get(1).size()+" , "+mapOut.get(2).size());
		
			
			System.out.println("Output of in nodes and out nodes written to:"+inFile+"_NodeID_corresp_OUTnodes_INnodes.txt");
			

			// output the out file name 
			TreeMap<String, String> tmp=new TreeMap<String, String>();
			tmp.put("999",  inFile+"_NodeID_corresp_OUTnodes_INnodes.txt");
			mapOut2.put( 999, tmp);
		}
		catch(Exception e ){
			e.printStackTrace();
		}
		finally {
			if(writer_debug!=null)
				try {
					writer_debug.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return mapOut2;
	}
	// readFile_convert_token_to_a_map  (convert2.py python code uses networkx graph output which is input to this method)
	public static TreeMap<Integer,TreeMap<String,String>> readFile_convert_token_to_a_map(String inFile,
																		 				  int 	 startLineNumber,
																		 				  int 	  endLineNumber
																						 ){
		
		TreeMap<Integer,TreeMap<String,String>> mapLineNo_ConvertedTreeMap=new TreeMap<Integer,TreeMap<String,String>>();
		TreeMap<Integer,String> map_inFile_eachLine=null;
		try {
			  //
			  map_inFile_eachLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(inFile, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 "thehindu", //debug_label
																									 false //isPrintSOP
																									 );
			  int lineNumber=0;
			  TreeMap<String,String> map_mapString_to_treemap=new TreeMap<String, String>();
			  for(int seq:map_inFile_eachLine.keySet()){
				  lineNumber++;
				  String currLine=map_inFile_eachLine.get(seq);
				  if(currLine.length()<1) continue; //blank
				  //
				  if(endLineNumber>0 && startLineNumber>0 ){
					  //
					  if(lineNumber>=startLineNumber && lineNumber<=endLineNumber ){
						  if(currLine.indexOf("!!!")==-1){
							  
							  map_mapString_to_treemap=Convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(currLine,
									  																				":", //dictionary_name_value_splitter
									  																				true);
							  System.out.println("processing "+lineNumber +" :"+map_mapString_to_treemap.size());
						  }
						  //
						  else{
							  // 3rd token has the map string
							  String []s =currLine.split("!!!");
							  System.out.println("processing(2) "+lineNumber);
							  map_mapString_to_treemap=Convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(s[2],
									  																				":", //dictionary_name_value_splitter
									  																				true);
							  
						  }
					  }
					  mapLineNo_ConvertedTreeMap.put(lineNumber , map_mapString_to_treemap);
					  
				  }  
			  }
			  
			  System.out.println("mapLineNo_ConvertedTreeMap.size="+mapLineNo_ConvertedTreeMap.size());
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mapLineNo_ConvertedTreeMap;
	}
	
	// This takes input of file that has   things (1) NodeName..usually it also has bodyText(not needed in this method)
	// uses lineNo as docID
	//mapOut.get(1)-> map_NodeName_LineNo
	//mapOut.get(2)-> map_NodeName_LineNo_NOT_ground_Truth
	//mapOut.get(3)-> Both ground truth and NOT GROUND truth
	public static TreeMap<Integer,TreeMap<String,Integer>> readFile_get_only_gTruth_NodeNames_N_DocID_N_NOTgTruth_NodeNames(
																								 String inFile,
																								 int    token_index_having_nodeNam,
																								 boolean is_having_header
																								 ){
		
		TreeMap<String,Integer> map_NodeName_LineNo=new TreeMap<String,Integer>();
		TreeMap<String,Integer> map_NodeName_LineNo_NOT_ground_Truth=new TreeMap<String,Integer>();
		TreeMap<String,Integer> map_NodeName_LineNo_ALL=new TreeMap<String,Integer>();
		TreeMap<Integer,String> map_inFile_eachLine=null;
		TreeMap<Integer,TreeMap<String,Integer>> mapOut=new TreeMap<Integer, TreeMap<String,Integer>>();
		try {
			FileWriter writer_only_GT_NodeName_docID=new FileWriter(new File(inFile+"only_GT_NodeName_docID_DEBUG.txt"));
			FileWriter writer_only_NOT_GT_NodeName_docID=new FileWriter(new File(inFile+"only_NOT_GT_NodeName_docID_DEBUG.txt"));
			  //
			  map_inFile_eachLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(inFile, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 "thehindu", //debug_label
																									 false //isPrintSOP
																									 );
			  int lineNumber=0;
			  TreeMap<String,String> map_mapString_to_treemap=new TreeMap<String, String>();
			  for(int seq:map_inFile_eachLine.keySet()){
				  lineNumber++;
				  // 
				  if(is_having_header==true && seq==1) continue;
				  
				  String currLine=map_inFile_eachLine.get(seq);
				  
				  if(currLine.indexOf("ground_truth")>=0){
					  String []s=currLine.split("!!!");
					  String nodeName=s[token_index_having_nodeNam-1];
					  map_NodeName_LineNo.put(nodeName, lineNumber);
					  map_NodeName_LineNo_ALL.put(nodeName, lineNumber);
					  
					  writer_only_GT_NodeName_docID.append(nodeName+"!!!"+lineNumber+"\n");
					  writer_only_GT_NodeName_docID.flush();
				  }
				  else{
					  String []s=currLine.split("!!!");
					  String nodeName=s[token_index_having_nodeNam-1];
					  map_NodeName_LineNo_NOT_ground_Truth.put(nodeName, lineNumber);
					  map_NodeName_LineNo_ALL.put(nodeName, lineNumber);
					  
					  writer_only_NOT_GT_NodeName_docID.append(nodeName+"!!!"+lineNumber+"\n");
					  writer_only_NOT_GT_NodeName_docID.flush();
					  
				  }
				  
			  }
			  
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		mapOut.put(1, map_NodeName_LineNo);
		mapOut.put(2, map_NodeName_LineNo_NOT_ground_Truth);
		mapOut.put(3, map_NodeName_LineNo_ALL);
		return mapOut;
	}
	//print_eachLine_stringToMapConverted
	public static TreeMap<Integer,TreeMap<String, String>> print_eachLine_stringToMapConverted(
																					String baseFolder,
																					String outFile_for_debug,
																					TreeMap<Integer,TreeMap<String,String>> mapLineNumber_String2MapConverted,
																					int 	StartLine,
																					int 	EndLines,
																					TreeMap<String,Integer> mapNodeName_DocID_ground_truth,
																					TreeMap<Integer, TreeMap<String,String>> mapOut3_networkX_mapString_prop1,
																					boolean 				is_print_NOT_in_ground_truth,
																					boolean is_write_to_debugFile
																					){
		TreeMap<Integer,TreeMap<String, String>> mapID_nodeID_N_Value=new TreeMap<Integer, TreeMap<String,String>>();
		int cnt_hit=0;
		TreeMap<String, String> map_nodeID_value_for_Ground_truth=new TreeMap<String, String>();
		TreeMap<String, String> map_nodeID_value_for_NOT_Ground_truth=new TreeMap<String, String>();
		int cnt_hit_not_in_ground_truth=0;
		FileWriter writer=null;
		try {
			writer=new FileWriter(new File(outFile_for_debug));
			if(is_write_to_debugFile)
				writer.append("\n --------BELOW are nodes in ground truth---------------\n");
	    	// ground truth
	    	for(String node_id_minus_one:mapLineNumber_String2MapConverted.get(1).keySet()){ //consider only first line
	    		String curr_nodeName_orig=mapLineNumber_String2MapConverted.get(1).get(node_id_minus_one).replace("\"", "");
	    		String curr_nodeName=curr_nodeName_orig.split(" ")[0]; //get only first element in  "997aa 997aa"
	    		
	    		
	    		if( mapNodeName_DocID_ground_truth.containsKey(curr_nodeName)  ){
	    			cnt_hit++;
//	    			System.out.println((node_id_minus_one+1)+" "+mapLineNumber_String2MapConverted.get(1).get(node_id_minus_one)
//	    														+" "+mapOut3_networkX_mapString_prop1.get(1).get(node_id_minus_one) );
	    			
	    			if(is_write_to_debugFile)
		    			writer.append("\n"+(node_id_minus_one+1)+" "+mapLineNumber_String2MapConverted.get(1).get(node_id_minus_one)
		    														+" "+mapOut3_networkX_mapString_prop1.get(1).get(node_id_minus_one));
		    			writer.flush();
	    			map_nodeID_value_for_Ground_truth.put(String.valueOf(node_id_minus_one+1),
	    												  mapLineNumber_String2MapConverted.get(1).get(node_id_minus_one) );
	    		}
	    	}
	    	if(is_write_to_debugFile)
	    		writer.append("\n --------BELOW are nodes NOT in ground truth---------------\n");
	    	writer.flush();
	    	//NOT in ground truth
	    	for(String node_id_minus_one:mapLineNumber_String2MapConverted.get(1).keySet()){//consider only first line
	    		String curr_nodeName_orig=mapLineNumber_String2MapConverted.get(1).get(node_id_minus_one).replace("\"", "");
	    		String curr_nodeName=curr_nodeName_orig.split(" ")[0]; //get only first element in  "997aa 997aa"
	    		
	    		if( !mapNodeName_DocID_ground_truth.containsKey(curr_nodeName)  ){
	    			cnt_hit_not_in_ground_truth++;
	    			//System.out.println((node_id_minus_one+1)+" "+mapLineNumber_String2MapConverted.get(1).get(node_id_minus_one));
	    			if(is_write_to_debugFile)
		    			writer.append("\n "+(node_id_minus_one+1)+" "+mapLineNumber_String2MapConverted.get(1).get(node_id_minus_one)
		    								+" "+" "+mapOut3_networkX_mapString_prop1.get(1).get(node_id_minus_one)+" <- NOT IN");
	    			writer.flush();
	    			map_nodeID_value_for_NOT_Ground_truth.put(String.valueOf(node_id_minus_one+1),
							  								  mapLineNumber_String2MapConverted.get(1).get(node_id_minus_one) );
	    		}
	    	}

	    	System.out.println("cnt_hit="+cnt_hit+" cnt_hit_not_in_ground_truth:"+cnt_hit_not_in_ground_truth);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		mapID_nodeID_N_Value.put(1, map_nodeID_value_for_Ground_truth );
		mapID_nodeID_N_Value.put(2, map_nodeID_value_for_NOT_Ground_truth );
		
		return mapID_nodeID_N_Value;
	}
	
	// (1) loads vocabulary of format <Word!!!WordI> <---> <output map.get(1) -> Word as KEY>
	// (2) load a typical TFDIDF output file having <DocID!!!WordID!!!TF-IDF> <---> <output map.get(2) -> DocID+WordID as KEY>
	public static TreeMap<Integer, TreeMap<String, Double>> TFIDF_load_docIDNWordID_TF_IDFScore(
																								String baseFolder,
																								String inFile_VocabularyWord_wordID,
																								String inFile_DocID_WordID_TFIDF
																								){
		TreeMap<Integer, TreeMap<String, Double>> map_vocabularyFile_N_TFIDFfile=new TreeMap<Integer, TreeMap<String,Double>>();
		TreeMap<String, Double> map_Word_WordID=new TreeMap<String, Double>();
		TreeMap<String, Double> map_DocIDWordID_TFIDF=new TreeMap<String, Double>();
		
		try {
			TreeMap<Integer, String> map_inFile_eachLine_word_wordID=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 											.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(inFile_VocabularyWord_wordID, 
																																			 	 -1, //startline, 
																																				 -1, //endline,
																																				 "thehindu", //debug_label
																																				 false //isPrintSOP
																																				 );
			
			TreeMap<Integer, String> map_inFile_eachLine_DocIDWordIDTFIDF=
															ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
															.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(inFile_DocID_WordID_TFIDF, 
																																 	 	-1, //startline, 
																																 	 	-1, //endline,
																																 	 	"thehindu", //debug_label
																																 	 	false //isPrintSOP
																																	 	);
			
				// Word AND WordID	 	  
				for(int lineNo:map_inFile_eachLine_word_wordID.keySet()){
					//System.out.println("Vocab.lineNo:"+lineNo);
					String line=map_inFile_eachLine_word_wordID.get(lineNo).replace("!!!!!!", "!!!#!!!").replace("!!!!!", "!!!").replace("!!!!", "!!!");
					String [] s=line.split("!!!");
					
					if(s[0].length()<1) continue;
					map_Word_WordID.put(s[0] , Double.valueOf(s[1]));
				}
				
				// DocID+WordID AND TFIDF
				for(int lineNo:map_inFile_eachLine_DocIDWordIDTFIDF.keySet()){
					//System.out.println("docIDWordID.lineNo:"+lineNo);
					String line=map_inFile_eachLine_DocIDWordIDTFIDF.get(lineNo).replace("!!!!!!", "!!!#!!!").replace("!!!!!", "!!!").replace("!!!!", "!!!");
					//System.out.println("line->"+line);
					String [] s2=line.split("!!!");
					//blank
					if((s2[0]+":"+s2[1]).length()<1) continue;
					
					map_DocIDWordID_TFIDF.put(s2[0]+":"+s2[1] , Double.valueOf(s2[2]));
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		map_vocabularyFile_N_TFIDFfile.put(1, map_Word_WordID);
		map_vocabularyFile_N_TFIDFfile.put(2, map_DocIDWordID_TFIDF);
		return map_vocabularyFile_N_TFIDFfile;
	}
	
	//load_DocID_AND_person_organi_locati
	// mapOut( <lineNo>:1  ) -> person  <<----here lineNo = docID
	// mapOut( <lineNo>:2  ) -> Organiz
	// mapOut( <lineNo>:3  ) -> Location
	// mapOut( <lineNo>:4  ) -> Numbers
	// mapOut( <lineNo>:5  ) -> Nouns
	// mapOut( <lineNo>:6  ) -> Sentiment
	// mapOut( <lineNo>:7  ) -> Verb
	// mapOut( <lineNo>:8  ).get("1") -> Ground Truth label
	public static TreeMap<String, TreeMap<String,String>> load_DocID_AND_person_organi_locati_numbers( 
																								String baseFolder, 
																								String inFile,
																								String delimiter,
																								int	   token_having_docID_OR_lineNo_as_weCall,
																								int    token_having_person_mapString,
																								int    token_having_organiz_mapString,
																								int    token_having_location_mapString,
																								int    token_having_numbers_mapString,
																								int    token_having_nouns_mapString,
																								int    token_having_sentiment_mapString,
																								int    token_having_verb_mapString,
																								int    token_having_GroundTruth_label,
																								boolean is_having_header
																								) {
		
		TreeMap<Integer,String> map_inFile_eachLine=new TreeMap<Integer, String>();
		TreeMap<String, TreeMap<String,String>> mapOut=new TreeMap<String, TreeMap<String,String>>();
		String header="";
		// TODO Auto-generated method stub
		try{
			
			  map_inFile_eachLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(inFile, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 " ", //debug_label
																									 false //isPrintSOP
																									 );
			  int last_seq_used=-1;
			  String currPersonMapString="";
			  String currOrganizMapString="";
			  String currLocationMapString="";
			  String currNumbersMapString="";
			  String currNounsMapString="";
			  String curr_ground_Truth_label="";
			  int	 curr_docID_or_LINEno_as_weCall=-1;
			  //
			  for(int seq:map_inFile_eachLine.keySet()){
				  
				  if(is_having_header && seq==1)
					  continue;
				  
				  String currLine=map_inFile_eachLine.get(seq) ;
				  
				  if(currLine.length()<=1) continue;
				  String [] s= currLine.split(delimiter);
				  // another delimiter
				  String [] s2= currLine.split("!!!");
				  
				  String currVerbsMapString="";
				  //System.out.println("lineNo:"+seq +" "+s.length);
				  
				  try{
					   currPersonMapString=s[token_having_person_mapString-1];
					   currOrganizMapString=s[token_having_organiz_mapString-1];
					   currLocationMapString=s[token_having_location_mapString-1];
					   currNumbersMapString=s[token_having_numbers_mapString -1];
					   currNounsMapString=s[token_having_nouns_mapString -1];
					   curr_ground_Truth_label=s[token_having_GroundTruth_label-1];  
					   //delimter !!! to get docID
					   curr_docID_or_LINEno_as_weCall=Integer.valueOf(s2[token_having_docID_OR_lineNo_as_weCall-1]);
				  }
				  catch(Exception e){
					  System.out.println("line:"+seq+" error:"+e.getMessage()+" token.len:"+s.length);
				  }
				   
				  
//				  System.out.println("2::"+currPersonMapString);
//				  System.out.println("3::"+currOrganizMapString);
//				  System.out.println("4::"+currLocationMapString);
//				  System.out.println("5::"+currNumbersMapString);
//				  System.out.println("6::"+currNounsMapString);
				  
				  String currSentimentMapString=s[token_having_sentiment_mapString -1];
//				  System.out.println("7::"+currSentimentMapString);
				  
				  if(s.length>=token_having_verb_mapString)
					  currVerbsMapString=s[token_having_verb_mapString -1];
				  
//				  System.out.println("8::"+currVerbsMapString);
				  
				  //System.out.println("currPersonMapString:"+currPersonMapString+" currLine:"+currLine);
				  
				  TreeMap<String, String>  map_Person=new TreeMap<String, String>();
				  TreeMap<String, String>  map_Organiz=new TreeMap<String, String>();
				  TreeMap<String, String>  map_Location=new TreeMap<String, String>();
				  TreeMap<String, String>  map_Numbers=new TreeMap<String, String>();
				  TreeMap<String, String>  map_Nouns=new TreeMap<String, String>();
				  TreeMap<String, String>  map_Sentiments=new TreeMap<String, String>();
				  TreeMap<String, String>  map_Verbs=new TreeMap<String, String>();
				  TreeMap<String, String>  map_GroundTruth_label=new TreeMap<String, String>();
				  
				  if(currPersonMapString.indexOf("{")>=0)
					  map_Person=Convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(currPersonMapString, 
													  										 "=",//dictionary_name_value_splitter, 
													  										 false//is_print_SOP
													  										 );
				  
				  //System.out.println("currOrganizMapString:"+currOrganizMapString);
				  if(currOrganizMapString.indexOf("{")>=0)
					  map_Organiz=Convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(currOrganizMapString, 
									  														   "=",//dictionary_name_value_splitter, 
									  														   false//is_print_SOP
									  														   );
				  
				  if(currLocationMapString.indexOf("{")>=0)
					  map_Location=Convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(currLocationMapString, 
										  														"=",//dictionary_name_value_splitter, 
										  														false//is_print_SOP
										  														);
				  if(currNumbersMapString.indexOf("{")>=0)
					  map_Numbers=Convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(currNumbersMapString, 
									  															"=",//dictionary_name_value_splitter, 
									  															false//is_print_SOP
									  															);
				  
				  if(currSentimentMapString.indexOf("{")>=0){
					  map_Sentiments=Convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(currSentimentMapString, 
																								"=",//dictionary_name_value_splitter, 
																								false//is_print_SOP
																								);
					  
				  }
				  
				  //
				  map_GroundTruth_label.put("1", curr_ground_Truth_label );
				  
				  // seq is not docID
				  
//				  mapOut.put( seq +":1", map_Person);
//				  mapOut.put( seq +":2", map_Organiz);
//				  mapOut.put( seq +":3", map_Location);
//				  mapOut.put( seq +":4", map_Numbers);
//				  mapOut.put( seq +":6", map_Sentiments);
//				  mapOut.put( seq +":8", map_GroundTruth_label);
				  
				  mapOut.put( curr_docID_or_LINEno_as_weCall +":1", map_Person);
				  mapOut.put( curr_docID_or_LINEno_as_weCall +":2", map_Organiz);
				  mapOut.put( curr_docID_or_LINEno_as_weCall +":3", map_Location);
				  mapOut.put( curr_docID_or_LINEno_as_weCall +":4", map_Numbers);
				  mapOut.put( curr_docID_or_LINEno_as_weCall +":6", map_Sentiments);
				  mapOut.put( curr_docID_or_LINEno_as_weCall +":8", map_GroundTruth_label);
				  
				  TreeMap<String,String> temp=new TreeMap<String, String>();
				  temp.put("1", currNounsMapString );
				  mapOut.put( curr_docID_or_LINEno_as_weCall +":5", temp);
//				  mapOut.put( seq +":5", temp);
				 
				  temp=new TreeMap<String, String>();
				  temp.put("1", currVerbsMapString );
				  mapOut.put( curr_docID_or_LINEno_as_weCall +":7", temp);
//				  mapOut.put( seq +":7", temp);
				  
			 
				  System.out.println("docID:"+curr_docID_or_LINEno_as_weCall+" "+map_Person+" "+map_Organiz
						  			+" "+map_Location+" "+map_Numbers+" "+map_Sentiments+" "+map_GroundTruth_label+" "+temp
						  			);
				  
				  last_seq_used=curr_docID_or_LINEno_as_weCall;
			  }
			  System.out.println("load_DocID_AND_person_organi_locati()-> mapOut.size: "+mapOut.size()
					  			+ " ** map_inFile_eachLine->"+map_inFile_eachLine.size() +" last_seq_used:"+last_seq_used);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	
	//
	public static TreeMap<String, String> get_currDos_Verb_Filtered_as_KEY(  TreeMap<String,String>  map_curr_SEQ_verbs_CSVwithSpace,
											   								Stemmer stemmer,
											   								TreeMap<String, String []> map_VerbWord_N_arr_lineNo,
											   								TreeMap<String, String []> map_NounWord_N_arr_lineNo
											  							){
		TreeMap<String, String> map_currDocID_Filtered_stemmedverb_asKEY=new TreeMap<String, String>();
		try {
			
			for(String seq:map_curr_SEQ_verbs_CSVwithSpace.keySet() ){
				//
				String currVerbsWithSpace=map_curr_SEQ_verbs_CSVwithSpace.get(seq);
				String []arr_currVerbsWithSpace=currVerbsWithSpace.split(" ");
				int cnt111=0;
				map_currDocID_Filtered_stemmedverb_asKEY=new TreeMap<String, String>();
				//each verb
				while(cnt111<arr_currVerbsWithSpace.length){
					String curr_verb=arr_currVerbsWithSpace[cnt111].replace(".", "").replace(",", "");
					String stem_verb=stemmer.stem(curr_verb);
					
					//
					if(map_VerbWord_N_arr_lineNo.containsKey(stem_verb)
						&& !map_NounWord_N_arr_lineNo.containsKey(stem_verb) ){
						//System.out.println("pass VERB: "+curr_verb);
						map_currDocID_Filtered_stemmedverb_asKEY.put(stem_verb, "");
						
					}
					else{
						//System.out.println("not NOUN VERB: "+curr_verb);
					}
					
					cnt111++;
				}
				
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return map_currDocID_Filtered_stemmedverb_asKEY;
	}
	
	// main
    public static void main(String[] args) throws IOException {
    	 
    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	/// PRE-REQUISITE:
    	// (1) Run wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati.graphOUTfile_4_tag_for_person_organiz_locati() 
    	//		to get person, location, organizt
    	// (2) Caution: Number of line Numbers of two files should be EQUAL (1) O/P "tag_for_person_organiz_locati" (2) I/P file of "tag_for_person_organiz_locati"
    	//   	run "readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String" for this mismatch to find.
    	// (3) Be sure if this number is rightly loaded map_DocID_bodytext
    	// (4) Produces output file "debug_myAlgo2.txt"
    	// (5) Set "prefix_for_problem=<>" . Example. prefix_for_problem=p18
    	// (6) Use "readFile_eachWord_of_given_Token_apperance_get_as_CSV_LineNumber()" to create CSV files for output of step (1)
     	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	
    	/// speeding up 
    	//is_YES_skip_run_for_outFile_having_givenNode_OutNodes_INnodes <-after first run, set this to true
    	// 
    	Stemmer stemmer=new Stemmer();
    	String prefix_for_problem="p8";
    	TreeMap<String, String> map_Word_StemmedWord=new TreeMap<String, String>();
    	long t0 = System.nanoTime();
    	String baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/";
    	// SKIPPING ****
    	boolean is_YES_skip_edge_normalization=false; // ******
    	boolean is_YES_skip_run_for_outFile_having_givenNode_OutNodes_INnodes=true;
    	
    	// <>,LINENO
    	String inFile_nounWord_LineNoCSV = baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_NOUN_LINENO.txtSTEMMED_NODUP.txt";
    	String inFile_verbWord_LineNoCSV = baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_VERB_LINENO.txtSTEMMED_NODUP.txt";
    	String inFile_personWord_LineNoCSV = baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_PERSON_LINENO.txtSTEMMED_NODUP.txt";
    	String inFile_organizWord_LineNoCSV = baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_ORGANIZ_LINENO.txtSTEMMED_NODUP.txt";
    	String inFile_locationWord_LineNoCSV = baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_LOCATION_LINENO.txtSTEMMED_NODUP.txt";
//    	String inFile_numberWord_LineNoCSV = baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_NUMBER_LINENO.txtSTEMMED_NODUP.txt";
    	String inFile_numberWord_LineNoCSV = baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_NUMBER_LINENO.txt_NumberToken_LINENOCSV.txt";
    	// <lineNo(docID)!!!numberCSV!!!relatedWordsCSV_WITH@@@>
    	String inFile_lineNoRdocID_N_numberCSV_relatedWordsCSV=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_NUMBER_LINENO.txt_LINENO_NumberCSV_RelatedWordCSV.txt";
    	    	
    	String inFile_GBAD=baseFolder+"output_gbad_clean.g";
    	//this input comes from??
    	String inFile_person_origz_loc=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb.txt";
    	String outFile_having_givenNode_OutNodes_INnodes=baseFolder+"output_gbad_clean.g_NodeID_corresp_OUTnodes_INnodes.txt";
    	//after first run, set this to true

    	String inFile="";
    	String debugFile=baseFolder+"debug_myAlgo2.txt";
 
    	FileWriter writer_debug=new FileWriter(new File(debugFile));
    	//CAN RUN ONLY ONCE TO TAG <ORGANIZATION, PERSON, LOCATION>
    	baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/";
    	//baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds2/final/ds2_30000/";
    	// this file has bodyText and taggedText also
    	inFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt";
//    	inFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_URLs_NOT_exists_in_sample_ds2_AND_ground_truth_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt";
    	
    	 
    	 // System.out.println("Loadin stemmed word");
		 // Loading Word and Stemmed_word  (below input file created when running tf-idf method  calc_tf_idf() )
    	 // && (NOT NEEDED HERE) convert_DocID_WordID_Freq_To_another_format.convert_DocID_WordID_Freq_To_another_format_2()
         // check (NOT NEEDED HERE) P18_get_dupURL_N_domainNames() flag==3 for example
    	///***NOTE: TECHNICALLY THIS HAS TO BE FROM WIKIPEDIA(ONTOLOGY), CHECK YOUR BACKGROUND KNOWLEDGE ON THIS.
		  String infile2="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/tf-idf/tf-idf.txt_Word_stemmedWord.txt";
		  map_Word_StemmedWord=LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE(
				  																							infile2,
														  													"!!!",
														  													"1,2",
			  																								"", //append_suffix_at_each_line
														  													false,
														  													false //isPrintSOP
														  													 );
	    	System.out.println("RUNNING Word and Stemmed_word (WIKIPEDIA)");
	    	///THIS HAS ACTUALLY DONE TO PICK FROM WIKIPEDIA
			String infile_wiki="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/tf-idf/tf-idf_wikipedia.txt_Word_stemmedWord.txt";
			//  Loading Word and Stemmed_word (WIKIPEDIA) 
			  TreeMap<String, String> map_Word_StemmedWord_from_WIKIPEDIA=
					  						LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE(
					  																								infile2,
					  																								"!!!",
					  																								"1,2",
					  																								"", //append_suffix_at_each_line
					  																								false,
					  																								false //isPrintSOP
					  																								);		  
			   
			  
    	System.out.println("RUNNING tag_for_person_organiz_locati");
    	String inFolder_semantic_REPOSITORY="/Users/lenin/Downloads/#repository/p8.sentiment/";
	    // Flag_type={1=works as usual(past),2="add extra Organization (without token breaking) "}
    	int Flag_type=2;
    	//ONE-TIME**** comment/uncomment as required
    	Wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati.
    	graphOUTfile_4_tag_for_person_organiz_locati( baseFolder,
				    								  inFile,
				    								  "!#!#",
				    								  3, //TOKEN without NLP tagging
				    								  2, //TOKEN with NLP tagging
				    								  true, // is_having_header
													  map_Word_StemmedWord,
													  inFolder_semantic_REPOSITORY, //REPOSITORY
													  prefix_for_problem,
													  Flag_type
				    								 );
    	
    	System.out.println("END RUNNING tag_for_person_organiz_locati");
    	//CHANGEHERE
    	String first_File="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt";
    	String second_File=inFile+"_added_perso_origz_loc_verb.txt";;
// 		BELOW very ***important (2)Caution: Number of line Numbers of two files should be EQUAL (1) O/P "tag_for_person_organiz_locati" (2) I/P file of "tag_for_person_organiz_locati"
//    	readFile_2_EachLineORtoken_from_First_File_exists_in_Second_File_as_String.
//    	readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String(
//    																			first_File, 
//    																			"", //delimiter_to_be_added_to_first_File, 
//    																			1, 	//token_interested_in_first_File, 
//    																			second_File, 
//    																			"!!!", //delimiter_for_splitting
//    																			second_File+"_EXIST.txt", 
//    																			false, //is_Append_outFile_exists, 
//    																			second_File+"_NON_EXIST.txt", 
//    																			false, //is_Append_outFile_not_exists, 
//    																			true  //isSOPprint
//    																			);
//    	 
    	inFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb.txt";
  //  	inFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_URLs_NOT_exists_in_sample_ds2_AND_ground_truth_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc.txt";
    	// verify no tokens in above created file / delimiter @@@@ 
//    	readFile_verify_and_Correct_No_Of_Tokens_In_EachLine.readFile_only_verify_No_Of_Tokens_In_EachLine( 5		 , //noTokensNeeded, 
//																											"@@@@", //delimiter, 
//																											inFile, 
//																											inFile+"_NOT_MATCHED_token_debug.txt", 
//																											inFile+"_MATCHED_token_debug.txt", 
//																											inFile+"_MATCHED_debug.txt",
//																											false //isSOPprint
//																											);
//    	
    	
    	
    	// (1) read GBAd file (2) TESTED-writes to OUTPUT file->gets in-edge and out-edge of each node 
    	// (3) (NOT TESTED) writes to OUTPUT file->expand to neighbors and get in-edge and out-edge
		//map.get(1) <- map_SS_NewsRootNodeID_NodeName (only root node)
		//map.get(2) <- map_SS_NodeID_NodeName (all)
		//map.get(3) <- map_SS_NodePair_EdgeValue (all)
    	TreeMap<Integer, TreeMap<String, String>> mapOut=new TreeMap<Integer, TreeMap<String,String>>();
    	if(is_YES_skip_run_for_outFile_having_givenNode_OutNodes_INnodes==false){
    		System.out.println("RUNNIN LOADING.(NEW RUN).convert_GBAD_to_getIN_R_Out_Edge_Also_expand_to_neighours...");
    		// this method produces (TWO) output files
	    	mapOut=convert_GBAD_to_getIN_R_Out_Edge_Also_expand_to_neighours(	baseFolder,
	    																		inFile_GBAD,
	    																		10250 , //run_for_TOP_N_article, // run top N documens for hop loop iteration.
	    																		1, //MAX_no_hops
	    																		false //isSOPprint
	    															  			);
	    	if(mapOut.containsKey(999)){
	    		//	output file
	    		outFile_having_givenNode_OutNodes_INnodes=mapOut.get(999).get("999");
	    	}
	    	//write to output file.
	    	FileWriter writer_outNode=new FileWriter(new File(inFile_GBAD+"_outNode.txt"));
	    	//
	    	for(Integer i:mapOut.keySet()){
	    		writer_outNode.append(i+"!!!"+mapOut.get(i) +"\n");
	    		writer_outNode.flush();
	    	}
	    	
    	}
    	//skipping wont give the output file name, so manually set it
    	else{
//    		System.out.println("RUNNIN RE-LOADING.(LAST RUN).convert_GBAD_to_getIN_R_Out_Edge_Also_expand_to_neighours...");
//    		//FILE 1: SET FILE NAME 
//    		outFile_having_givenNode_OutNodes_INnodes=inFile_GBAD+"_NodeID_corresp_OUTnodes_INnodes.txt";
//    		
//    	  // FILE 2: LOAD
//    	  // load from last ran OUTPUT file
//  		  HashMap<String, String> map_TEMP=LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE( 
//  				  																								  inFile_GBAD+"_outNode.txt",
//																												 "!!!",
//																												  false //isPrintSOP
//																												 );
//  		  
//  		  
//   		   //loading last ran output
//  		  for(String id:map_TEMP.keySet()){
//  			  TreeMap<String,String> maptemp=convert_mapString_to_TreeMap.convert_mapString_to_SS_TreeMap(map_TEMP.get(id), "=", false);
//  			  mapOut.put( Integer.valueOf(id), maptemp);
//  		  }
  		  
    	}
    	
    	TreeMap<String,String> map_SS_NodePair_EdgeValue=mapOut.get(3);
        
    	//System.out.println( "root nodes:"+mapOut.get(1).size()+" all size:"+mapOut.get(2).size()
    	//					+" nodepair for edge value:"+mapOut.get(3).size()+" outFile_having_givenNode_OutNodes_INnodes:"+outFile_having_givenNode_OutNodes_INnodes);
    	
    	///////////////////////////Start loading graph properties/////////////////////////
    	boolean is_1_neworkx_loading=true;
    	boolean is_2_neworkx_loading=true;
    	baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/graph/";
    	//this file produced by NetworkX such as output of katz_centrality;
    	String in_from_networkX_outFile="eigenvector_centrality_numpy.txt";
    	inFile=baseFolder+in_from_networkX_outFile;
//makes sense->directed_closeness_centrality.txt,undirected_output_eigenvector_centrality_numpy_weight_1,directed_output_eigenvector_centrality_numpy_weight_1,
//undirected_output_node_average_degree_connectivity_nodup,directed_katz_centrality*,directed_k_nearest_neighbors*,
//undirected_output_average_degree_connectivity_all_weight_1_in_out-both-onsourceNtarget.txt,undirected_output_average_degree_connectivity_all_weight_2_in_out-both-onsourceNtarget
//undirected_output_average_degree_connectivity_all_weight_1_source-out_target-in,undirected_output_average_degree_connectivity_all_weight_2_source-out_target-in.txt

    	System.out.println("RUNNIN LOADING..readFile_convert_token_to_a_map...");
    	TreeMap<Integer,TreeMap<String,String>> mapOut3_networkX_mapString_prop1=new TreeMap<Integer, TreeMap<String,String>>();
    	TreeMap<Integer,TreeMap<String,String>> mapOut3_networkX_mapString_prop2=new TreeMap<Integer, TreeMap<String,String>>();
    	//**************below are zeros and lesser distinct one
    	//directed_eigenvector_centrality(lot zero),directed_load_centrality_1_default(zeros),directed_in_degree_weight_1 & directed_out_degree_weight_1(less distinct value)
    	//directed_dispersion(zeros),
    	//NO sense->directed_output_node_average_neighbor_degree
    	// load the output file of NetworkX graph properties into a map
//    	try{
//	    	mapOut3_networkX_mapString_prop1=readFile_convert_token_to_a_map( inFile,
//			    															  1, //start line 
//			    															  1 //end line 
//	    																	  );
//    	}
//    	catch(Exception e ){
//    		is_1_neworkx_loading=false;
//    	}
////    	System.out.println("size(no lines):"+mapOut3_networkX_mapString_prop1.size()+ ";first line:"+mapOut3_networkX_mapString_prop1.get(1));
////    	//graph prop2 file 
//    	inFile=baseFolder+"directed_closeness_centrality.txt";
//    	try{
//	    	mapOut3_networkX_mapString_prop2=readFile_convert_token_to_a_map( inFile,
//																			  1, //start line 
//																			  1 //end line 
//																			  );
//		}
//		catch(Exception e ){
//			is_2_neworkx_loading=false;
//		}
    	
    	///////////////////////////End loading graph properties/////////////////////////
    	

    	System.out.println("RUNNIN LOADING..readFile_get_only_gTruth_NodeNames_N_DocID_N_NOTgTruth_NodeNames...");
    	baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/";
    	inFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt";
    	//mapOut.get(1)-> map_NodeName_LineNo
    	//mapOut.get(2)-> map_NodeName_LineNo_NOT_ground_Truth
    	//mapOut.get(3)-> Both ground truth and NOT GROUND truth
    	// read data file (which has bodyText etc) . get the DocID that is ground Truth - True positive
    	TreeMap<Integer,TreeMap<String,Integer>> mapOut4=readFile_get_only_gTruth_NodeNames_N_DocID_N_NOTgTruth_NodeNames(
			    																										inFile,
										    																			3, //token index having node name
										    																			true //is_having_header // 
										    																			);
    	TreeMap<String,Integer> mapNodeName_DocID_for_ground_truth=mapOut4.get(1);
    	TreeMap<String,Integer> mapNodeName_DocID_for_NOT_ground_truth=mapOut4.get(2);
    	TreeMap<String,Integer> mapNodeName_DocID_for_BOTH_ground_truth_and_NOT=mapOut4.get(3);
    	
    	///DEBUG
    	for(int i_:mapOut4.keySet()){
    		writer_debug.append("\n mapOut4 i:"+i_+"<--->"+mapOut4.get(i_));
    		writer_debug.flush();
    	}
    	

    	System.out.println("map ( ground truth and DocID loading-from file that usually has bodyText):"
    												+  mapNodeName_DocID_for_ground_truth.size()
						    						+" "+mapNodeName_DocID_for_NOT_ground_truth.size()
						    						+" "+mapNodeName_DocID_for_BOTH_ground_truth_and_NOT.size());
    	
//    	int cnt_hit=0;
//    	//
//    	for(String node_id_minus_one:mapOut.get(1).keySet()){
//    		if(mapNodeName_DocID_ground_truth.containsValue(Integer.valueOf(node_id_minus_one)  +1)){
//    			cnt_hit++;
//    			System.out.println((node_id_minus_one+1)+" "+mapOut.get(1).get(node_id_minus_one));
//    		}
//    	}
//    	System.out.println("cnt_hit:"+cnt_hit);
    	
    	System.out.println("RUNNIN LOADING..print_eachLine_stringToMapConverted...");
    	baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/graph/";
    	String outFile_for_debug=baseFolder+"debug_"+in_from_networkX_outFile;
    	
    	TreeMap<Integer,TreeMap<String, String>> map_Nodes_GT_N_NOT_GT=new TreeMap<Integer, TreeMap<String,String>>();
    	TreeMap<String, String> map_nodeID_Value_for_GT =new TreeMap<String, String>();
    	TreeMap<String, String> map_nodeID_Value_for_NO_GT =new TreeMap<String, String>();
    	// NetworkX (convert2.py) products output of graph properties such as centrality which is a dictionary. now this has to be converted to MAP. ONE such file read. 
    	// DEBUG - map to read and debug (WRITES TO output file for debug)
//    	TreeMap<Integer,TreeMap<String, String>> map_Nodes_GT_N_NOT_GT=
//										    	print_eachLine_stringToMapConverted(baseFolder,
//										    										outFile_for_debug,
//										    										mapOut,
//										    										1, //startLine
//										    										1, //endLine
//										    										mapNodeName_DocID_for_ground_truth,
//										    										mapOut3_networkX_mapString_prop1,
//										    										false, //is_print_NOT_in_ground_truth
//										    										true  //is_write_to_outputFile
//										    										);
//    	
//    	map_nodeID_Value_for_GT =  map_Nodes_GT_N_NOT_GT.get(1);
//    	map_nodeID_Value_for_NO_GT =  map_Nodes_GT_N_NOT_GT.get(2);
    	
    	System.out.println("RUNNIN LOADING..map_VocabFile_TFIDFfile...");
    	baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/tf-idf/";
    	String inFile_VocabularyWord_wordID=baseFolder+"unique_word_vocabulary.txt";
    	String inFile_DocID_WordID_TFIDF=baseFolder+"tf-idf.txt_noStopWord_WORD_ID.txt";
    	// (1) loads vocabulary of format <Word!!!WordI> <---> <output map.get(1) -> Word as KEY>
    	// (2) load a typical TFDIDF output file having <DocID!!!WordID!!!TF-IDF> <---> <output map.get(2) -> DocID+WordID as KEY>
    	TreeMap<Integer, TreeMap<String, Double>> map_VocabFile_TFIDFfile=TFIDF_load_docIDNWordID_TF_IDFScore(
																											baseFolder,
																											inFile_VocabularyWord_wordID,
																											inFile_DocID_WordID_TFIDF
																											);
    	
    	
    	//writer_debug=new FileWriter(baseFolder+"temp.txt");
    	
    	// 
//    	writer_debug.append("\n 1:map_VocabFile_TFIDFfile.get(1):"+map_VocabFile_TFIDFfile.get(1) );
//    	writer_debug.append("\n 2:map_VocabFile_TFIDFfile.get(2):"+map_VocabFile_TFIDFfile.get(2) );
//      writer_debug.flush();
    	
    	System.out.println("map( ground truth and DocID loading):"+mapNodeName_DocID_for_ground_truth.size()
																+" "+mapNodeName_DocID_for_NOT_ground_truth.size()
																+" "+mapNodeName_DocID_for_BOTH_ground_truth_and_NOT.size());
    	
    	//****config
    	System.out.println("RUNNIN LOADING..load_DocID_AND_person_organi_locati_numbers");
    	baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/";

    	boolean is_having_header=true;
    	//loading DocID (lineNo) and person , organiz, locaiton
    	// mapOut( <lineNo>:1  ) -> person  <<----here lineNo = docID
    	// mapOut( <lineNo>:2  ) -> Organiz
    	// mapOut( <lineNo>:3  ) -> Location
    	// mapOut( <lineNo>:4  ) -> Numbers
    	TreeMap<String, TreeMap<String,String>> map_lineNoNID_person_organiz_locati_numbers=
											    	load_DocID_AND_person_organi_locati_numbers(
											    										baseFolder,
											    										inFile_person_origz_loc,
											    										"@@@@",
											    										 1,// 	 token_having_docID_OR_lineNo_as_weCall
																						 2,//    token_having_person_mapString,
																						 3,//    token_having_organiz_mapString,
																						 4,//    token_having_location_mapString,
																						 5,//    token_having_numbers_mapString,
																						 6,//    token_having_nouns
																						 7,//    token_having_sentiment
																						 8,//    token_having_verbs
																						 9, //token_having_GroundTruth_label
											    										 is_having_header
											    										);
    	
    	System.out.println("RUNNIN OUTnodes..map_inFile_seqID_N_givenNode_OutNodes_INnodes");
    	TreeMap<Integer,String >map_inFile_seqID_N_givenNode_OutNodes_INnodes=new TreeMap<Integer, String>();
    	//
    	if(is_YES_skip_run_for_outFile_having_givenNode_OutNodes_INnodes==false){
    	 //load given Node and OutNodes and INnodes
		  map_inFile_seqID_N_givenNode_OutNodes_INnodes=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 									.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
					 																			 outFile_having_givenNode_OutNodes_INnodes, 
																							 	 -1, //startline, 
																								 -1, //endline,
																								 "thehindu", //debug_label
																								 false //isPrintSOP
																								 );
    	}
    	
		  //iterate above and get treemap with <DocID,Line>
		  TreeMap<Integer,String >map_inFile_DocID_N_givenNode_OutNodes_INnodes=new TreeMap<Integer, String>();
		  for(int seq:map_inFile_seqID_N_givenNode_OutNodes_INnodes.keySet()){
			  if(seq==1) continue; //header
			  String currLine=map_inFile_seqID_N_givenNode_OutNodes_INnodes.get(seq);
			  String []s=currLine.split("!!!");
			  map_inFile_DocID_N_givenNode_OutNodes_INnodes.put(Integer.valueOf(s[0]), currLine );
		  }
		  
		  System.out.println("RUNNIN NLP..readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile");
		  
    	//load tagging NLP for each line (doc ID)
    	inFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt";
		  TreeMap<Integer,String >map_inFile_DocID_N_CurrLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
															 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																	 						 inFile, 
																						 	 -1, //startline, 
																							 -1, //endline,
																							 "thehindu", //debug_label
																							 false //isPrintSOP
																							 );
		 TreeMap<Integer, String> map_DocID_NLPtext=new TreeMap<Integer, String>();
		 TreeMap<Integer, String> map_DocID_bodytext=new TreeMap<Integer, String>();
		 TreeMap<Integer,  String> map_DocID_URL=new TreeMap<Integer, String>();
		 int token_having_bodyText_withNLPtag=2;
		 int token_having_bodyText=3;
		 //
		 for(int seq:map_inFile_DocID_N_CurrLine.keySet()){
			 if(seq==1) continue; //header
			 String [] s=map_inFile_DocID_N_CurrLine.get(seq).split("!#!#");
			 map_DocID_NLPtext.put( seq, s[token_having_bodyText_withNLPtag-1] );
			 map_DocID_bodytext.put(seq, s[token_having_bodyText-1]  );
			 map_DocID_URL.put( seq, s[0] );
		 }
		 
		 writer_debug.append("\n map_inFile_DocID_N_CurrLine.size:"+map_inFile_DocID_N_CurrLine.size());
		 writer_debug.append("\n map_DocID_NLPtext.size:"+map_DocID_NLPtext.size());
		 writer_debug.append("\n map_DocID_bodytext.size:"+map_DocID_bodytext.size());
		 writer_debug.append("\n map_DocID_URL.size:"+map_DocID_URL.size());
		 writer_debug.flush();
		 
		 String outOFcontextURL="/Users/lenin/Downloads/#problems/p8/work-FullArticle/ground truth/merging/OUT-of-context.txt";
    	 //load given Node and OutNodes and INnodes
		  TreeMap<Integer,String> map_inFile_seqID_N_URL_outOFcontext=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 															.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
					 																			 outOFcontextURL, 
																							 	 -1, //startline, 
																								 -1, //endline,
																								 " loading", //debug_label
																								 false //isPrintSOP
																								 );
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
		  ///////////////////////////////////
		  ///////////////////////////////////
		  System.out.println("Loadin Number and LineNoCSV ( docID_CSV ) ");
		  // Loadin LINENO (docID ) and NUMBER_CSV
		  
		  TreeMap<String, String> map_number_lineNorDocIDCSV=LoadMultipleValueToMap2_AS_KEY_VALUE.LoadMultipleValueToMap2_AS_KEY_VALUE( 
				  																								  inFile_numberWord_LineNoCSV,
																												 "!!!",
																												 "1,2",
																												 "", //suffix to append
																												 false,
																												  false //isPrintSOP
																												 );
		  
		  TreeMap<String, String []> map_number_arrlineNorDocIDCSV=new TreeMap<String, String[]>();
		  for(String number:map_number_lineNorDocIDCSV.keySet()){
			  String [] s=map_number_lineNorDocIDCSV.get(number).split(",");
			  //System.out.println("hre:"+docID+" len:"+s.length);
			  map_number_arrlineNorDocIDCSV.put(number, s);
		  }
		  map_number_lineNorDocIDCSV.clear();
		  ///////////////////////////////////
		   
		//START  - Loading <LineNo!!!NumerCSV!!!RelatedWordsToNumber>      	
    	TreeMap<Integer, TreeMap<String,String>> map=LoadMultipleValueToKeyMap3.LoadMultipleValueToKeyMap3(  
    																										 inFile_lineNoRdocID_N_numberCSV_relatedWordsCSV, 
			       																							 "!!!");
    	
	    	TreeMap<String,String> map_docIDrlineNo_numberCSV=map.get(1);
	    	TreeMap<String,String> map_docIDrlineNo_RelatedWordsForNumbersCSV=map.get(2);

			     //(1) convert to array String (related words for number CSV)
			    	TreeMap<Integer, String []> map_docIDrlineNo_numbersCSV=new TreeMap<Integer, String[]>();
					  for(String docID:map_docIDrlineNo_numberCSV.keySet()){
						  String [] s=map_docIDrlineNo_numberCSV.get(docID).split(" ");
						  //System.out.println("hre:"+docID+" len:"+s.length);
						  map_docIDrlineNo_numbersCSV.put( Integer.valueOf(docID) , s);
					  }
					  map_docIDrlineNo_numberCSV.clear();
			    	//(2) convert to array String (related words for number CSV)
			    	TreeMap<Integer, String []> map_docIDrlineNo_arrelatedWordsForNumbersCSV=new TreeMap<Integer, String[]>();
					  for(String docID:map_docIDrlineNo_RelatedWordsForNumbersCSV.keySet()){
						  String [] s=map_docIDrlineNo_RelatedWordsForNumbersCSV.get(docID).split("@@@");
						  //System.out.println("hre:"+docID+" len:"+s.length);
						  map_docIDrlineNo_arrelatedWordsForNumbersCSV.put( Integer.valueOf(docID) , s);
					  }
					  map_docIDrlineNo_RelatedWordsForNumbersCSV.clear();
					  
	   //END Loading <LineNo!!!NumerCSV!!!RelatedWordsToNumber>
	    	
		 		System.out.println("RUNNIN myAlgo..");
		 		
		 		writer_debug.append("\nmap_VocabFile_TFIDFfile:"+map_VocabFile_TFIDFfile);
		 		writer_debug.append("\nmap_Word_StemmedWord:"+map_Word_StemmedWord);
		 		writer_debug.flush();
		 		
		 		boolean is_debug_more=false;
		    	// implementing my algo
		    	myAlgo(
		    			baseFolder,
		    			inFile_GBAD,
		    			map_SS_NodePair_EdgeValue,
		    			map_Word_StemmedWord,
		    			map_Word_StemmedWord_from_WIKIPEDIA,
		    			mapOut3_networkX_mapString_prop1,
		    			mapOut3_networkX_mapString_prop2,
		    			map_VocabFile_TFIDFfile,
		    			map_Nodes_GT_N_NOT_GT,
		    			map_lineNoNID_person_organiz_locati_numbers,
		    			map_inFile_seqID_N_URL_outOFcontext,
		    			mapOut4.get(1), // DocID and groundTruth NodeName
		    			mapOut4.get(2), // DocID and NOT groundTruth NodeName
		    			mapOut4.get(3), // DocID and both groundTruth and NOT groundTruth NodeName
		    			map_inFile_DocID_N_givenNode_OutNodes_INnodes,
		    			map_DocID_NLPtext,
		    			map_DocID_bodytext,
		    			map_DocID_URL,
		    			map_NounWord_N_arr_lineNo,
		    			map_VerbWord_N_arr_lineNo,
				    	map_PersonWord_N_arr_lineNo,
				    	map_OrganizWord_N_arr_lineNo,
				    	map_LocationWord_N_arr_lineNo,
				    	map_number_arrlineNorDocIDCSV,
				    	map_docIDrlineNo_numbersCSV,
				    	map_docIDrlineNo_arrelatedWordsForNumbersCSV,
		    			debugFile,
		    			false,  //isSOPprint
		    			"fire,collapse,migrant,kidnap,road acciden,senior citize,ebola,mining,juvenile,swine,slavery,traffick", // curr_query_CSV {migrant,kidnap,road acciden,senior citize,ebola,mining,juvenile,swine,collapse,slavery,traffick,fire}
		    			"collapse#ceasefire,ceasefire#fire,,,,,,,,,,dummy",  // filter_NO_apply_for_inputFile (example: collapse for fire )
		    			2, //offset_for_TFIDF
		    			1, //N_preceding_docs
		    			is_YES_skip_edge_normalization, //is_YES_skip_edge_normalizationf
		    			stemmer,
		    			is_debug_more
		    			);
		    	
//curr_query_CSV->{fire,collapse,migrant,kidnap,road acciden,senior citize,ebola,mining,juvenile,swine,slavery,traffick}
//filter_NO_apply_for_inputFile->{collapse#ceasefire,ceasefire#fire,,,,,,,,,,}
		    	
		    	
//migrant,kidnap(16),road acciden(2xx),senior citizen(1022),ebola(1598),mining(300),juvenile(98),swine(129),collapse(exclude ceasefire,fire)(4234)
// ,slavery(11),traffick(53),fire(exclude collapse,ceasefire) (2062)
//selective->collapse,ebola,fire,road,senior citize
//all->collapse(exclude ceasefire,fire),ebola,fire(exclude collapse,ceasefire)),juvenile,kidnap,migrant,mining,road accide,senior citize,slavery,swine,traffick" ////curr_query_CSV
 
		    	
    	System.out.println("(debug)loaded file from NetworkX output analysis:"+outFile_for_debug);
    	System.out.println("map_VocabFile_TFIDFfile.get(1)-Vocab->"+map_VocabFile_TFIDFfile.get(1).size());//+"<->"+map_VocabFile_TFIDFfile.get(1));
    	System.out.println("map_VocabFile_TFIDFfile.get(2)-TFIDF->"+map_VocabFile_TFIDFfile.get(2).size());//+"<->"+map_VocabFile_TFIDFfile.get(2));
    	System.out.println("Ground Truth nodeID only:"+map_nodeID_Value_for_GT.size() +" from outFile_for_debug:"+outFile_for_debug);
    	System.out.println("NOT Ground Truth nodeID only:"+map_nodeID_Value_for_NO_GT.size()+" from outFile_for_debug:"+outFile_for_debug);
    	System.out.println("outFile_having_givenNode_OutNodes_INnodes:"+outFile_having_givenNode_OutNodes_INnodes);
    	
    	
    	System.out.println("Time Taken:"
							+ NANOSECONDS.toSeconds(System.nanoTime() - t0) + " seconds; "
							+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
							+ " minutes");
    	
    	baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/";
    	inFile_GBAD=baseFolder+"output_gbad_.g";
    	// load all nodes or news only nodes. //************convert_GBAD_to_getIN_R_Out_Edge_Also_expand_to_neighours d	oes this already
//    	TreeMap<Integer,String> map = wrapper_to_get_only_root_news_nodes_R_allNodes( baseFolder,
//    																				  inFile_GBAD,
//    																				  2 // {1=return all nodes, 2=return only root news nodes, }
//    																				  );
//    	System.out.println("map( all root nodes):"+map.size());
 
    	
    	if(is_YES_skip_run_for_outFile_having_givenNode_OutNodes_INnodes)
    		System.out.println("****skipped->is_skip_run_for_outFile_having_givenNode_OutNodes_INnodes->"+is_YES_skip_run_for_outFile_having_givenNode_OutNodes_INnodes);
    	if(is_YES_skip_edge_normalization){
			System.out.println("*** U skipped is_YES_skip_edge_normalization->"+is_YES_skip_edge_normalization);
		if(is_1_neworkx_loading==true ||is_2_neworkx_loading==true)
			System.out.println("*** FAILED : Loading ....networx files");
		
		
		System.out.println("-----------------------------------");
		System.out.println("Be sure if this number is rightly loaded map_DocID_bodytext.size ->"+map_DocID_bodytext.size());
		System.out.println("output file : "+debugFile);	
		}
    	
    } //end main

}
