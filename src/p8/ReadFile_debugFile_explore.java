package p8;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.TreeMap;

import crawler.DocumentParser_AND_Cosine;
import crawler.Calc_tf_idf;
import crawler.Convert_DocID_WordID_Freq_To_another_format;
import crawler.Cosine_similarity;
import crawler.Find_2_Map_word_not_exist_in_another;
import crawler.Generate_Random_Number_Range;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import crawler.Sort_given_treemap;

// this takes input 
public class ReadFile_debugFile_explore {
	 
		//readFile_debugFile_explore  (extract one feature)
		public static void readFile_debugFile_explore(
													String baseFolder,
													String file_1,
													String file_2,
													String startPattern,
													String endPattern
													){
			TreeMap<Integer,Integer> map_BucketIndex_Count=new TreeMap<Integer, Integer>();
			try{
				FileWriter writer=new FileWriter(new File(file_1+"#"+startPattern+"_out_debug_explore.txt"));

				TreeMap<Integer, String> map_inFile_EachLine_of_document_f1=new TreeMap<Integer, String>();
				TreeMap<Integer, String> map_inFile_EachLine_of_document_f2=new TreeMap<Integer, String>();
				
				
				
				// loading
				map_inFile_EachLine_of_document_f1=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 							.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line( 
						 																									 file_1, 
						 																									 -1, //start_line, //startline, 
																															 -1, //end_line, //endline,
																															 " map_inFile_EachLine_of_document ", //debug_label
																															 false //isPrintSOP
																															 );
				
				System.out.println("map_inFile_EachLine_of_document_f1.size:"+map_inFile_EachLine_of_document_f1.size());
				double sum=0.; int denomi_hit_count=0;
				//
				for(Integer seq: map_inFile_EachLine_of_document_f1.keySet()){
					//System.out.println(   seq );
					String currLine=map_inFile_EachLine_of_document_f1.get(seq).toLowerCase();
					
					int beginIndex=currLine.indexOf(startPattern);
					int endIndex=currLine.indexOf(endPattern,beginIndex+1);
					
					//System.out.println( beginIndex +" "+endIndex +" "+currLine);
					if(beginIndex>0 &&endIndex>0){
						
						String extracted=currLine.substring(beginIndex, endIndex ).replace(startPattern, " ").trim().replace("\"", "");
						System.out.println("extracted:"+extracted+" lineNo:"+seq);
						writer.append("\n " + extracted);
						writer.flush();
						
						sum=sum+Double.valueOf(extracted);
						
						//convert 55.6 to 50 
						int bucketindex =-1; 
						
						if( Double.valueOf(extracted)>=10){
							 bucketindex=(int) (Math.floor( Double.valueOf(extracted) /10) * 10);
						}
						else{ 
							
							bucketindex= Integer.valueOf(extracted.replace(".0", ""));
						}
						//
						if(!map_BucketIndex_Count.containsKey(bucketindex)){
							map_BucketIndex_Count.put(bucketindex, 1);
						}
						else{
							int sz=map_BucketIndex_Count.get(bucketindex)+1;
							map_BucketIndex_Count.put(bucketindex, sz);
						}
						denomi_hit_count++;
					}	
				}
				 
				System.out.println("map_BucketIndex_Count:"+map_BucketIndex_Count +" avg:"+sum/ (double) denomi_hit_count  );
				writer.append("\n"+"map_BucketIndex_Count:"+map_BucketIndex_Count +"\navg:"+sum/ (double) denomi_hit_count );
				writer.flush();
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}

	// main
	public static void main(String[] args) throws Exception {
		
		
		// picking a feature and exploring it
		
		String topic_CSV = "ebola.5" ; // ex: ebola, 
		
		String [] arr_topic_CSV=topic_CSV.split(",");
		int cnt=0;
		
		// 
		while(cnt<arr_topic_CSV.length){
			String curr_query=arr_topic_CSV[cnt];
			//
			String baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/"+curr_query+"/";
			
			
			String list_of_files_CSV="currRUN_false_POSITIVE.txt,currRUN_false_NEGATIVE.txt,currRUN_true_POSITIVE.txt,currRUN_true_NEGATIVE.txt";
			String [] arr_list_of_files_CSV=list_of_files_CSV.split(",");
			int cnt2=0;
			// each file 
			while(cnt2<arr_list_of_files_CSV.length){
				
				String inputFile=baseFolder+arr_list_of_files_CSV[cnt2];
				// EXPLORE 
				readFile_debugFile_explore(
										baseFolder,
										inputFile, //file_1,
										"", //file_2,
										"focus:", //startPattern,
										" " //end
										);
				cnt2++;
			
			}
			
			cnt++;
			
		}
		 	
	}
	
}
