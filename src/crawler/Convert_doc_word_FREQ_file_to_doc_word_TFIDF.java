package crawler;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

//
public class Convert_doc_word_FREQ_file_to_doc_word_TFIDF {
	
	//convert_doc_word_FREQ_file_to_doc_word_TFIDF
	public static void convert_doc_word_FREQ_file_to_doc_word_TFIDF(
																		String baseFolder,
																	   	String inputFile,
																	   	String outFile
																	   ){
		 String line="";
			BufferedReader br=null;
			int max_word_id=-1;
			try{
				System.out.println("convert_doc_word_FREQ_file_to_doc_word_TFIDF(): inputFile->"+inputFile);
				
				br = new BufferedReader(new FileReader(inputFile));
				FileWriter writer= new FileWriter(new File(outFile));
				FileWriter writerDebug= new FileWriter(new File(baseFolder+"debug130.txt"));
				int doc_ID=0;
				TreeMap<String, Integer> map_each_Word_UniqueDoc=new TreeMap<String, Integer>();
				TreeMap<Integer, String> map_each_doc_WordFreq_of_inFile=new TreeMap<Integer, String>();
				int lineNumber=0;
				while( (line = br.readLine()) !=null){
					System.out.println("lineNumber:"+lineNumber++);
					if(line.length()>=1){
						
						String [] arr_tokens=line.split("!!!"); //first token has doc_ID
						
						if(arr_tokens.length<2){
							writerDebug.append("ERRROR: <2 tokens"+ lineNumber+" line:"+line);
							writerDebug.flush();
							continue;
						}
						
						doc_ID=Integer.valueOf(arr_tokens[0]);
						String [] arr_wordID_delimitr_freq= arr_tokens[1].split(" ");
						int cnt=0; 
						// 
						map_each_doc_WordFreq_of_inFile.put(doc_ID, arr_tokens[1]);
						
						//
						while(cnt<arr_wordID_delimitr_freq.length){
							
							String curr_wordID_delimitr_freq =arr_wordID_delimitr_freq[cnt]; //it has "233:1" 
							
							String[] name_value=curr_wordID_delimitr_freq.split(":");
							
							if(cnt<100){
								writerDebug.append(doc_ID+","+
											 name_value[0]+"," //word_id
											 +name_value[1]+"\n"); //frequency
								writerDebug.flush();
							}
							
							// present of curr_word in how many documents?
							if(!map_each_Word_UniqueDoc.containsKey(name_value[0])){
								map_each_Word_UniqueDoc.put(name_value[0], 1);
							}
							else{
								int new_freq=map_each_Word_UniqueDoc.get(name_value[0])+1;
								map_each_Word_UniqueDoc.put(name_value[0], new_freq);
							}
							
							cnt++;
						}
					}
					 
				} //while line-read
				
				int D =map_each_Word_UniqueDoc.size();
				String conc_word_tfIDF="";				
				//re-read each line of input file
				for(int doc_id:map_each_doc_WordFreq_of_inFile.keySet()){
					System.out.println("tf.idf: doc-id->"+doc_id);
					//   IDF  = log (N/ n_present_doc_count)
					String curr_word_freq_line=map_each_doc_WordFreq_of_inFile.get(doc_id);
					String [] arr_wordID_delimitr_freq=curr_word_freq_line.split(" ");
					int cnt=0;
					while(cnt<arr_wordID_delimitr_freq.length){
						String curr_wordID_delimitr_freq =arr_wordID_delimitr_freq[cnt]; //it has "233:1" 
						String[] name_value=curr_wordID_delimitr_freq.split(":");
						Double curr_word_freq=Double.valueOf(name_value[1]);
						
						double curr_tf_idf=curr_word_freq 
											* Math.log10(D/map_each_Word_UniqueDoc.get(name_value[0]));
						
						writerDebug.append("\n"+curr_tf_idf+" D:"+D+" dist.cont.doc:"
												//			+map_each_Word_UniqueDoc.get(name_value[0])
												+" curr_word_freq:"+curr_word_freq);
						writerDebug.flush();
						
						//
						if(conc_word_tfIDF.length()==0){
							conc_word_tfIDF=name_value[0]+":"+curr_tf_idf;
						}
						else{
							conc_word_tfIDF=conc_word_tfIDF+" "+name_value[0]+":"+curr_tf_idf;
						}
						cnt++;
					}
					String tmp=doc_id+ "!!!"+conc_word_tfIDF+"\n";
					writer.append(tmp);
					writer.flush();
					//System.out.println("out:"+tmp);
					conc_word_tfIDF="";
				} //each line re-read inFile 
				
				
				//writer.append("max word id:"+max_word_id+"\n");
				writer.flush();
				
				System.out.println("tes");
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		
	}

	//main
	public static void main(String args[]) throws FileNotFoundException, IOException{
		String baseFolder="/Users/lenin/Downloads/p6/merged/dummy.mergeall/ds8/";
		String inputFile=baseFolder+"doc_id_word_id_AND_freq_d.txt";
		String outFile=baseFolder+"doc_id_word_id_AND_tfidf.txt";
 		long t0 = System.nanoTime();
		t0 = System.nanoTime();
		//convert_doc_word_FREQ_file_to_doc_word_TFIDF
		convert_doc_word_FREQ_file_to_doc_word_TFIDF(
														baseFolder,
														inputFile,
														outFile
														);
		
		 System.out.println("Time Taken:" + NANOSECONDS.toSeconds(System.nanoTime() - t0) +" seconds; "
	       	   		+ (NANOSECONDS.toSeconds(System.nanoTime() - t0))/60 +" minutes" );
		
	}
	

}
