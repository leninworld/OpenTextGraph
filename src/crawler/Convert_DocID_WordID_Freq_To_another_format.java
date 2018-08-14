package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

public class Convert_DocID_WordID_Freq_To_another_format {
	
	// input   -> convert to format <Doc_ID!!!Word_ID!!!Freq>  <- Freq can instead be TF-IDF
	// output  -> <docId!!!word_id_1:freq_1 word_id_2:freq_2 word_id_3:freq_3> 
	// This assumes  
	public static TreeMap<String,Integer> convert_DocID_WordID_Freq_To_another_format_2(
																	   String  baseFolder,
																	   String  inputFile,
																	   String  outFile,
																	   String  delimiter_4_inputFile,
																	   boolean isSOPprint
																	){
		BufferedReader br=null;
		 TreeMap<String,Integer>  mapOut=new TreeMap<String, Integer>();
		int max_word_id=-1;
		TreeMap<String, String> mapDocID_WordIDFreqCSV=new TreeMap<String, String>();
		try{
			br = new BufferedReader(new FileReader(inputFile));
			FileWriter writer= new FileWriter(new File(outFile));
			FileWriter writerDebug= new FileWriter(new File(baseFolder+"debug.txt"));
			String line=""; int doc_ID=0;
			 int maxs=-1;
			//eachLine
			while( (line = br.readLine()) !=null){
				
				if(isSOPprint)
					System.out.println("l:"+line);
				
				// Doc_ID!!!Word_ID!!!Freq  <- Freq can instead be TF-IDF
				String []arr_token=line.toLowerCase().split(delimiter_4_inputFile); //"!!!");
				String curr_docID=arr_token[0];
				String curr_WordID=arr_token[1];
				String curr_Freq=arr_token[2];
				// 
				if(Integer.valueOf(curr_docID) > maxs){
					maxs=Integer.valueOf(curr_docID);
				}
				
				// 
				if(line.length()>=1){
					// already docID exists
					if(mapDocID_WordIDFreqCSV.containsKey(curr_docID)){
						String t=mapDocID_WordIDFreqCSV.get(curr_docID);
						t=t+" "+curr_WordID+":"+curr_Freq;
						mapDocID_WordIDFreqCSV.put(curr_docID, t);
					}
					else{
						String t=curr_WordID+":"+curr_Freq;
						mapDocID_WordIDFreqCSV.put(curr_docID, t);
					}
	 
					
				}
				
				
			} // END while
			
			//UNORDERED
//			for(String docID:mapDocID_WordIDFreqCSV.keySet()){
//				writer.append(docID+"!!!"+mapDocID_WordIDFreqCSV.get(docID) +"\n");
//				writer.flush();
//			}
			//ORDERED (commented above)
			int cnt2=1;
			while(cnt2<=maxs){
				if(mapDocID_WordIDFreqCSV.containsKey( String.valueOf(cnt2)))
					writer.append(cnt2+"!!!"+mapDocID_WordIDFreqCSV.get(String.valueOf(cnt2)) +"\n");
				else{
					System.out.println("NOT contains..."+String.valueOf(cnt2));
				}
				writer.flush();
				cnt2++;
			}
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		mapOut.put("maxwordid", max_word_id);
		return mapOut;
	}
	
	// input  -> <docId!!!word_id_1:freq_1 word_id_2:freq_2 word_id_3:freq_3>
	// output -> convert to format <doc_id, word_id, frequency>
	public static void convert_DocID_WordID_Freq_To_another_format(
																   String baseFolder,
																   String inputFile,
																   String  outFile){
			
			BufferedReader br=null;
			int max_word_id=-1;
			try{
				br = new BufferedReader(new FileReader(inputFile));
				FileWriter writer= new FileWriter(new File(outFile));
				FileWriter writerDebug= new FileWriter(new File(baseFolder+"debug.txt"));
				String line=""; int doc_ID=0;
				while( (line = br.readLine()) !=null){
					System.out.println("l:"+line);
					if(line.length()>=1){
						
						String [] arr_tokens=line.split("!!!"); //first token has doc_ID
						doc_ID=Integer.valueOf(arr_tokens[0]);
						String [] arr_wordID_delimitr_freq= arr_tokens[1].split(" ");
						int cnt=0; 
						//
						while(cnt<arr_wordID_delimitr_freq.length){
							
							String curr_wordID_delimitr_freq =arr_wordID_delimitr_freq[cnt]; //it has "233:1" 
							
							String[] name_value=curr_wordID_delimitr_freq.split(":");
							
							writer.append(doc_ID+","+
										 name_value[0]+","+name_value[1]+"\n");
							writer.flush();
							//
							if(max_word_id==-1) {
								max_word_id=Integer.valueOf(name_value[0]);
								writerDebug.append("cnt=0,"+max_word_id+"\n");
								writerDebug.flush();
							}
							else{
								if( Integer.valueOf(name_value[0]) > max_word_id){
									writerDebug.append("cnt>0,"+Integer.valueOf(name_value[0])+","+max_word_id+"\n");
									writerDebug.flush();
									max_word_id=Integer.valueOf(name_value[0]);
									
								}
							}
							
							cnt++;
						}
						
						
						//writer.append( line.substring(1, line.length()) +"\n");
 
					}
					
					writer.flush();
				}
				
				writer.append("max word id:"+max_word_id+"\n");
				writer.flush();
				
				System.out.println("tes");
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	//main
	public static void main(String[] args) throws IOException {
		
		String  baseFolder="/Users/lenin/Downloads/p6/merged/third_time/";
				baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/";
		String inFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo_CHRONO.txt_TF_IDF.txt_noStopWord_WORD_ID.txt";
		String outFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo_CHRONO.txt_TF_IDF.txt_noStopWord_WORD_ID_format2.txt";
		
		//comment/uncomment
		//**************** THIS IS THE STANDARD WAY of running- for one file
//		convert_DocID_WordID_Freq_To_another_format_2( baseFolder, 
//													 inFile, 
//													 outFile,
//													 false //isSOPprint
//													 );


		//comment/uncomment
		//**************** THIS IS THE STANDARD WAY of running- for all files in a folder
		String inputFolder_having_SETofINPUTfiles="/Users/lenin/Dropbox/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds10/tempFolder4Conversion/";
		String [] arr_files=new File(inputFolder_having_SETofINPUTfiles).list();
		
//			inFile =baseFolder+"";
//			outFile=baseFolder+"";
		
		int c=0;
		//
		while(c<arr_files.length){
			if(arr_files[c].toLowerCase().indexOf("store")>=0){c++;continue;}
			System.out.println("Currently running file:"+inputFolder_having_SETofINPUTfiles+arr_files[c]);
			String curr_inFile=inputFolder_having_SETofINPUTfiles+arr_files[c];
			// convert_DocID_WordID_Freq_To_another_format_2
			convert_DocID_WordID_Freq_To_another_format_2( 	 inputFolder_having_SETofINPUTfiles, 
															 curr_inFile,  //INPUT
															 curr_inFile.replace(".txt", "_OUT.txt"), //OUTPUT
															 ",", //delimiter_4_inputFile
															 false //isSOPprint
															 );
			
			c++;
		}
		
		
		
		//<docId!!!word_id_1:freq_1 word_id_2:freq_2 word_id_3:freq_3>
//		convert_DocID_WordID_Freq_To_another_format( baseFolder, 
//													 inFile, 
//													 outFile);

		
	}

}
