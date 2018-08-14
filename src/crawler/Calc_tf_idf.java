package crawler;

import crawler.Stopwords;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

//authro lenin 
public class Calc_tf_idf {
	
	//input file has a token (having text) and do tf-idf calculationg (via word-freq)
	public static TreeMap<Integer,String> calc_tf_idf(  String 	baseFolder,
													    String baseFolder_out,
														String 	inputFile, 
														String delimiter_for_inputFile,
														int 	token_index_having_text,
														String 	outFile,
														boolean is_do_stemming_on_word,
														boolean is_ignore_stop_words_all_together,
														boolean isSOPprint
														){
		TreeMap<Integer,String> mapOut=new TreeMap<Integer, String>();
		try{
			String line="";
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream( inputFile) ));
			//boolean is_do_stemming_on_word=true;
			Stemmer stemmer=new Stemmer(); String token="";
		   TreeMap<String, Integer> mapVoc_Freq=new TreeMap<String, Integer>();
		   TreeMap<String, List<Integer>> map_Word_UniqueDocumentIdHavingIT=new TreeMap<String, List<Integer>>();
		   TreeMap<Integer, TreeMap<String, Integer > > map_DocID_wordFrequency=new TreeMap<Integer, TreeMap<String,Integer>>();
				int lineNumber=0; int last_doc_id=-1;
			
			FileWriter writer_voc=new FileWriter(new File(baseFolder_out+"unique_word_vocabulary.txt"));
				
			FileWriter writer=new FileWriter(new File(outFile));
			// files for docID wordID tf-idf output
			FileWriter writer_no_stopword=new FileWriter(new File(outFile+"_noStopWord.txt"));			
			FileWriter writer_wordID=new FileWriter(new File(outFile+"_WORD_ID.txt"));
			FileWriter writer_wordID_no_stopword=new FileWriter(new File(outFile+"_noStopWord_WORD_ID.txt"));
			FileWriter writer_word_stemmedWord=new FileWriter(new File(outFile+"_Word_stemmedWord.txt"));
			
			// files for docID wordID frequency output
			FileWriter writer2=new FileWriter(new File(outFile+"_docWordID_freq2.txt"));
			FileWriter writer2_no_stopword=new FileWriter(new File(outFile+"_noStopWord2.txt"));
			FileWriter writer2_wordID=new FileWriter(new File(outFile+"_WORD_ID2.txt"));
			FileWriter writer2_wordID_no_stopword=new FileWriter(new File(outFile+"_noStopWord_WORD_ID2.txt"));
			FileWriter writer2_word_stemmedWord=new FileWriter(new File(outFile+"_Word_stemmedWord2.txt"));
			

			
			FileWriter writer_debug=new FileWriter(new File(outFile+"_DEBUG.txt"));
			FileWriter writer_debug_2=new FileWriter(new File(outFile+"_DEBUG_2.txt"));
			
			TreeMap<String,Integer> map_already_wrote=new TreeMap<String, Integer>();
			//
			while ((line = reader.readLine()) != null) {
				lineNumber++;
				System.out.println("lineNumber:"+lineNumber);
				
				//DEBUG BREAK
//				if(lineNumber>100) break;
				
				//mapVoc_Freq=new TreeMap<String, Integer>();
				int curr_doc_id=lineNumber;
				String [] token_= line.split(delimiter_for_inputFile);
				
				 //clean
				 token=token_[token_index_having_text-1].replace("\"", " ").replace("'", " ").replace(",", " ").replace(";"," ").replace(".", " ")
								    .replace("‘", " ").replace("“", " ").replace("–", " ").replace(")", " ").replace("(", " ")
								    .replace("-", " ").replace(":", " ").replace("’", " ").replace("”", " ").replace("—", " ")
								    .replace("#", " ").replace("%", " ")//.replace("$", " ")
								    .replace("/"," ")
								    .replace("  ", " ");
				 
				 token=token.toLowerCase();

				 
				 // 
				 String [] arr_token=token.split(" ");
				 int cnt=0;
				 // 
				 while(cnt < arr_token.length){
					 String temp= Ltrim.ltrim( arr_token[cnt]);
					 
					 // ignore stop words all together
					 if(is_ignore_stop_words_all_together==true && Stopwords.is_stopword(temp)){
						 cnt++;
						 continue;
					 }
					 
					 // do stemming
					 if(is_do_stemming_on_word){
						 String orig_temp=temp;
						 //ltrim.ltrim(
						 temp=  stemmer.stem(temp);
						 
						 if(!map_already_wrote.containsKey(orig_temp)){
							 writer_word_stemmedWord.append(orig_temp+"!!!"+temp +"\n");
							 writer_word_stemmedWord.flush();
						 }
						 
						 map_already_wrote.put(orig_temp, -1);
						 
					 }
					 else 
						 temp=temp;

						 // 
						 if(!mapVoc_Freq.containsKey(temp)){
							  //vocabulary  term-freq
							 mapVoc_Freq.put(temp, 1);												  
							  //word-document freq- unique number of documents this word occurs.
							  List l=new ArrayList<Integer>();
							  l.add(curr_doc_id);
							  map_Word_UniqueDocumentIdHavingIT.put(temp, l);
							  
							  //doc-wise frequency
							  if(!map_DocID_wordFrequency.containsKey(curr_doc_id)){
								  TreeMap<String,Integer> t2=new TreeMap<String, Integer>();
								  t2.put( temp, 1 ) ;
								  map_DocID_wordFrequency.put(curr_doc_id, t2);
							  }
							  else{
								  TreeMap<String,Integer> t2=map_DocID_wordFrequency.get(curr_doc_id);
								  
								  if(!t2.containsKey(temp)){
									  TreeMap<String,Integer> t2_2=map_DocID_wordFrequency.get(curr_doc_id);
									  t2_2.put( temp, 1 ) ;
									  map_DocID_wordFrequency.put(curr_doc_id, t2_2);
								  }
								  else{
									  TreeMap<String,Integer> t2_2=map_DocID_wordFrequency.get(curr_doc_id);
									  int sz=t2.get(temp)+1;
									  t2_2.put( temp, sz ) ;
									  map_DocID_wordFrequency.put(curr_doc_id, t2_2);
								  }
							  }
							  
						 }
						 else{
							  int freq=mapVoc_Freq.get(temp)+1;
							  //System.out.println("last_doc_id!=curr_doc_id->"+last_doc_id+"<-->"+curr_doc_id );
							  //vocabulary term-freq
							  mapVoc_Freq.put(temp, freq);
							    // word-document freq- unique number of documents this word occurs.
							  
								  List<Integer> uniq_doc=map_Word_UniqueDocumentIdHavingIT.get(temp);
								  // if this docID not contained before in list
								  if(!uniq_doc.contains(curr_doc_id)){
									  uniq_doc.add(curr_doc_id );
									  map_Word_UniqueDocumentIdHavingIT.put(temp, uniq_doc);
								  }
								  
								  //doc-wise frequency
								  if(!map_DocID_wordFrequency.containsKey(curr_doc_id)){
									  TreeMap<String,Integer> t2=new TreeMap<String, Integer>();
									  t2.put( temp, 1 ) ;
									  map_DocID_wordFrequency.put(curr_doc_id, t2);
								  }
								  else{
									  TreeMap<String,Integer> t2=map_DocID_wordFrequency.get(curr_doc_id);
									  
									  if(!t2.containsKey(temp)){
										  TreeMap<String,Integer> t2_2=map_DocID_wordFrequency.get(curr_doc_id);
										  t2_2.put( temp, 1 ) ;
										  map_DocID_wordFrequency.put(curr_doc_id, t2_2);
									  }
									  else{
										  TreeMap<String,Integer> t2_2=map_DocID_wordFrequency.get(curr_doc_id);
										  int sz=t2.get(temp)+1;
										  t2_2.put( temp, sz ) ;
										  map_DocID_wordFrequency.put(curr_doc_id, t2_2);
									  }
									  
								  }
								  
						 }
					cnt++;
				 }
				 
				 if(isSOPprint){
					 System.out.println("doc:"+curr_doc_id+" token-freq:"+mapVoc_Freq.size()+" token-doc.freq:"+map_Word_UniqueDocumentIdHavingIT.size());
				 }
				 //
				 last_doc_id=curr_doc_id;
			}
			System.out.println(" token-freq:"+mapVoc_Freq.size()+" token-doc.freq:"+map_Word_UniqueDocumentIdHavingIT.size());
			int uniq_word_id=1;
			TreeMap<String, Integer> mapVocabulary_word_wordID= new TreeMap<String, Integer>();
			// create uniq_word_id for each word in vocabulary
			for(String word:mapVoc_Freq.keySet()){
				mapVocabulary_word_wordID.put(word, uniq_word_id);
				
				writer_voc.append(word+"!!!"+uniq_word_id+"\n");
				writer_voc.flush();
				 
				uniq_word_id++;
			}
			System.out.println("step 1: running docid-wordid-tf-IDF");
			//step 1: calculate tf-idf  (DONT COMMENT BELOW)
			for(int docID:map_DocID_wordFrequency.keySet()){
				//System.out.println("docID:"+docID+ " size:" +map_DocID_wordFrequency.get(docID).size()); //	+" word-freq:"+map_DocID_wordFrequency.get(docID));
				writer_debug.append("docID:"+docID+ " size:" +map_DocID_wordFrequency.get(docID).size()+" word-freq:"+map_DocID_wordFrequency.get(docID)+"\n");
				writer_debug.flush();
				double tf_idf=0.0;
				//Iterate each word in current document
				for(String word:map_DocID_wordFrequency.get(docID).keySet() ){
					//
					if(mapVoc_Freq.containsKey(word )){
						
						//System.out.println("param1,2:"+map_DocID_wordFrequency.get(docID).get(word) +"  "+map_Word_UniqueDocumentIdHavingIT.get(word).size() );
						// tf-idf
						tf_idf=map_DocID_wordFrequency.get(docID).get(word) * Math.log10( map_DocID_wordFrequency.size() / map_Word_UniqueDocumentIdHavingIT.get(word).size()  );
						//System.out.println();
						writer.append( docID+"!!!"+word+"!!!"+tf_idf+"\n");
						writer.flush();
						
						writer_wordID.append( docID+"!!!"+mapVocabulary_word_wordID.get(word)+"!!!"+tf_idf+"\n");
						writer_wordID.flush();
						
						// 
						if(!Stopwords.is_stopword(word.toLowerCase())){
							writer_no_stopword.append(docID+"!!!"+word+"!!!"+tf_idf+"\n");
							writer_no_stopword.flush();
							
							writer_wordID_no_stopword.append(docID+"!!!"+ mapVocabulary_word_wordID.get(word) +"!!!"+tf_idf+"\n");
							writer_wordID_no_stopword.flush();
							
						}
					}
					else{
						writer_debug_2.append( "(not exist in vocab) word->"+word+" for doc_id->"+docID+"\n");
						writer_debug_2.flush();
					}
				}
			} //for(int docID:map_DocID_wordFrequency.keySet()){
			
			System.out.println("step 2: running docid-wordid-frquency");
			//step 2: (DOC-WORDID AND FREQUENCY)another output - writing <docID!!!wordID!!!frequency>
			for(int docID:map_DocID_wordFrequency.keySet()){
				
				System.out.println("2.docID:"+docID+ " size:" +map_DocID_wordFrequency.get(docID).size()); //	+" word-freq:"+map_DocID_wordFrequency.get(docID));
				//writer_debug.append("docID:"+docID+ " size:" +map_DocID_wordFrequency.get(docID).size()+" word-freq:"+map_DocID_wordFrequency.get(docID)+"\n");
				//writer_debug.flush();
				int frequency=0;
				//Iterate each word in current document
				for(String word:map_DocID_wordFrequency.get(docID).keySet() ){
					//
					if(mapVoc_Freq.containsKey(word )){
						
						// System.out.println("param1,2:"+map_DocID_wordFrequency.get(docID).get(word) +"  "+map_Word_UniqueDocumentIdHavingIT.get(word).size() );
						// frequency
						frequency=map_DocID_wordFrequency.get(docID).get(word);
						//System.out.println();
						writer2.append( docID+"!!!"+word+"!!!"+frequency+"\n");
						writer2.flush();
						
						writer2_wordID.append( docID+"!!!"+mapVocabulary_word_wordID.get(word)+"!!!"+frequency+"\n");
						writer2_wordID.flush();
						 
						// 
						if(!Stopwords.is_stopword(word.toLowerCase())){
							writer2_no_stopword.append(docID+"!!!"+word+"!!!"+frequency+"\n");
							writer2_no_stopword.flush();
							//
							writer2_wordID_no_stopword.append(docID+"!!!"+ mapVocabulary_word_wordID.get(word) +"!!!"+frequency+"\n");
							writer2_wordID_no_stopword.flush();
							
						}
					}
					else{
						writer_debug_2.append( "(not exist in vocab) word->"+word+" for doc_id->"+docID+"\n");
						writer_debug_2.flush();
					}
				}
			} //for(int docID:map_DocID_wordFrequency.keySet()){

			System.out.println("********************************************");
			System.out.println("File holding the not found words in vocabulary: "+"outFile+\"_DEBUG_2.txt\"");
			System.out.println("Doc-wise word freq: "+"outFile+\"_DEBUG.txt\"");
			System.out.println("******Below files are docId-wordID-tfIDF");
			System.out.println("with Word->"+outFile);
			System.out.println("with Word(no stopword)->"+outFile+"_noStopWord.txt");
			System.out.println("with WordID->"+outFile+"_WORD_ID.txt");
			System.out.println("with WordID(no stopword)->"+outFile+"_noStopWord_WORD_ID.txt");
			System.out.println("VOC FILE ->"+baseFolder+"unique_word_vocabulary.txt");
			//**********/
			System.out.println("***********Below files are docId-wordID-frequency (file ends with \"format2.txt\"");
			System.out.println( ""+outFile+"_docWordID_freq2.txt");
			System.out.println( ""+outFile+"_noStopWord2.txt");
			System.out.println( ""+outFile+"_WORD_ID2.txt");
			System.out.println( ""+outFile+"_noStopWord_WORD_ID2.txt");
			System.out.println( ""+outFile+"_Word_stemmedWord2.txt");
			 
			mapOut.put(-99, outFile+"_noStopWord_WORD_ID.txt");
			//maximum word id
			mapOut.put(1, String.valueOf( uniq_word_id) );
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	
	// main
	public static void main(String[] args) {
		String baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/tmp/";
		
		//baseFolder="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data_prob/P8/p8_wikipedia_government_of_india";
		String inputFile=baseFolder+"";
		inputFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt";
		inputFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data_prob/P8/policy";
		inputFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt";
		
		String delimiter_for_inputFile="!!!"; //{"!!!","!#!#"}
		int token_index_having_text=4;
		//
		String baseFolder_out="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/tf-idf/";
			   baseFolder_out=baseFolder;
		String outFile=baseFolder_out+"tf-idf.txt";
		//Step 1: calculate TF-IDF
		// calc tf-idf
		calc_tf_idf( baseFolder,
					 baseFolder_out,
					 inputFile,
					 delimiter_for_inputFile, // delimiter_for_inputFile	 
					 token_index_having_text, //token_index_having_text
					 outFile,
					 true, //is_do_stemming_on_word
					 true, //is_ignore_stop_words_all_together
					 false //isSOPprint
					);
		
	 
		//Step 2: convert into format_2 ("Convert docID_wordID_freq to another format 
		//  <docId!!!word_id_1:freq_1 word_id_2:freq_2 word_id_3:freq_3>")
//		convert_DocID_WordID_Freq_To_another_format.convert_DocID_WordID_Freq_To_another_format_2(
//															baseFolder,
//															outFile, 
//															outFile+"_format2.txt", 
//															false //isSOPprint
//															);
		
//		convert_DocID_WordID_Freq_To_another_format.convert_DocID_WordID_Freq_To_another_format_2(
//														baseFolder,
//														outFile+"_noStopWord_WORD_ID.txt", 
//														outFile+"_noStopWord_WORD_ID.txt_format2.txt", 
//														false //isSOPprint
//														);
		
		    
		
	}

}
