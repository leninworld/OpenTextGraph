package p18;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

import crawler.Find_domain_name_from_Given_URL;
import crawler.Load_domainname_pattern4crawledHTML;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

//
public class Correct_labels_in_WEKA_file_using_groundTruthFile {
	
	// merge two files
	public static void correct_labels_in_WEKA_file_using_groundTruthFile(
														String  baseFolder,
														String  input_file1,
														String  input_file2,
														String  outputFile
													   ){
	
		FileWriter writer=null;
		
		FileWriter writerDebug=null;
		try {
			writer =new FileWriter(new File(outputFile.replace(".txt", "")+"_correctedLabels.txt"));
			 
			writerDebug =new FileWriter(new File(outputFile+"debug_WEKA_input_NOtokens_in_EACHLINE.txt"));
			  
			// map_file1 (WEKA) --token 10097 has label
			TreeMap<Integer,String> map_inFile1_eachLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(input_file1, 
																							 	 -1, //startline, 
																								 -1, //endline,
																								 " ", //debug_label
																								 false //isPrintSOP
																								 );
			
			
			// map_file2 (GROUND TRUTH ) 
			TreeMap<Integer,String> map_inFile2_eachLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(input_file2, 
																							 	 -1, //startline, 
																								 -1, //endline,
																								 " ", //debug_label
																								 false //isPrintSOP
																								 );
			 
			String weka_header=map_inFile1_eachLine.get(1);
 
			TreeMap<Integer,String> map_lineNo_allFeaturesCSVExcludeLabel_fromWEKAfile
									=new TreeMap<Integer, String>();
			// weka
			for(int seq:map_inFile1_eachLine.keySet()){
				if(seq==1) continue; //header
				
				String eachLine=map_inFile1_eachLine.get(seq);
				String [] arr1=eachLine.split(",");
				//not required
				String labelWEKA=arr1[10097];
			
//				System.out.println(" "+eachLine.substring(0, eachLine.lastIndexOf(",")));
				map_lineNo_allFeaturesCSVExcludeLabel_fromWEKAfile.put(seq, eachLine.substring(0, eachLine.lastIndexOf(",")));
//				if(seq>2) break;
				
			} //
			
			TreeMap<Integer,Integer> map_lineNo_label4groundTruth=new TreeMap<Integer, Integer>();
			
			//keyword!!!title!!!label!!!URL!!!bodyTEXT
			for(int seq:map_inFile2_eachLine.keySet()){
				if(seq==1) continue; //header
				
				String eachLine=map_inFile2_eachLine.get(seq);
				String [] arr1=eachLine.split("!!!");
				map_lineNo_label4groundTruth.put(seq, Integer.valueOf(arr1[2]));
			} //
			
			writer.append(weka_header+"\n");
			
			int max=map_lineNo_allFeaturesCSVExcludeLabel_fromWEKAfile.size()+100;
			int c=1;
			int count_lineswrote=0;
			//
			while(c<max){
				//
				
				if(map_lineNo_allFeaturesCSVExcludeLabel_fromWEKAfile.containsKey(c)){
					
					if(map_lineNo_label4groundTruth.get(c)==1)
						writer.append(map_lineNo_allFeaturesCSVExcludeLabel_fromWEKAfile.get(c)+","+
											"no"+"\n");
					else if(map_lineNo_label4groundTruth.get(c)==0)
						writer.append(map_lineNo_allFeaturesCSVExcludeLabel_fromWEKAfile.get(c)+","+
											"yes"+"\n");
						
					writer.flush();
				}
				
				c++;
			}
			
			System.out.println("map_file1.size:"+map_inFile1_eachLine.size() +" map_inFile2_eachLine:"+map_inFile2_eachLine.size() );
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			// TODO: handle finally clause
			
		}
	}
	

	// main
	public static void main(String[] args) throws Exception{
		String  baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/tmp/";
		
		String  file1=baseFolder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent.txt_HEADER4_WEKA2_TFIDF.txt";
		String  file2="/Users/lenin/Dropbox/Maitrayi/p18 -/Labeling/1500labeled_marked_DOUBTS/O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_onlyAfricanAmerican_removeIrrelevaLABEL.txt";
	    String  outputFile=baseFolder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent.txt_HEADER4_WEKA2_TFIDF.txt";
		
		
		//correct labeling....
		correct_labels_in_WEKA_file_using_groundTruthFile(
												baseFolder,
												file1,
												file2,
												outputFile
											);
		 
		
	}

}
