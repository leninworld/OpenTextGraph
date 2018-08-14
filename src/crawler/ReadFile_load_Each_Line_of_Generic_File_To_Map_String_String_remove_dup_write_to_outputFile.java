package crawler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile  {


		//output -> <eachline,eachline>
		public static TreeMap<String,String> readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
																													(String inFile,
																													int startline, 
																													int endline,
																													String outFile,
																													boolean is_Append_outFile,
																													boolean is_Write_To_OutputFile,
																													String debug_label,
																													int    token_to_be_used_as_primarykey,
																													boolean isSOPprint 
																													) {
			TreeMap<String,String> mapOut = new TreeMap();
			String line = ""; int counter=0;
			FileWriter writer= null;

			// Read the mapping to drill-down
			HashMap<String, String> mapDrillDown = new HashMap();
			int newcounter=0;
			try {
				if(is_Write_To_OutputFile){
					writer= new FileWriter(new File(outFile), is_Append_outFile);
				}
				BufferedReader reader = new BufferedReader(new FileReader(inFile));
				
				// read each line of given file
				while ((line = reader.readLine()) != null) {
					if(isSOPprint)
						System.out.println(" debug_label:"+debug_label +";line:"+line );
//					if(line.indexOf("http")==-1)
//						continue;
//					
//					line=line.replace("2..r,", "").replace("rss_,", "")
//							.replace(" ", "").replace("!.!href=", "")
//							.replace("!!!", "").replace("\"", "")
//							.replace("'", "")
//							.replace("!.!", "")
//							.replace("“", "")
//							.replace("“", "");
					
					counter++;
					
					//debug
//					if(counter>3)
//						 break;
					
					
					if(endline>0){
						//
						if(counter>=startline && counter<=endline )
						    newcounter++;
						else{
							if(isSOPprint)
								System.out.println("line no :"+counter+"!!!skip.line:"+line);
							continue;
						}
					}
					else{
						if(isSOPprint)
							System.out.println("line no :"+counter+"!!!loading...:"+line);
						newcounter++;
					}
					String primaryKey="";
					if(token_to_be_used_as_primarykey>0){
						primaryKey=line.split("!!!")[token_to_be_used_as_primarykey-1];
					}
					else{
						primaryKey=line;
					}
					
					mapOut.put(primaryKey, line);
					
				}
				//
				if(is_Write_To_OutputFile){
					//write to output file
					for(String s:mapOut.keySet()){
						writer.append(mapOut.get(s)+"\n");
						writer.flush();
					}
				}
				System.out.println("readFile_load_Each_Line_of_Generic_File_To_Map_String_String.inFile:"+inFile  +" number of lines:"+mapOut.size()+"\n");
				reader.close();
				//writer.close();
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return mapOut;
		}
		
	// output -> map key,value -> <counter,eachline>
	// endline-> <0 possible
	public static TreeMap<Integer,String> readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
																							(String  inFile, 
																							 int 	 startline, 
																							 int 	 endline,
																							 String  debug_label,
																							 boolean isPrintSOP
																							) {
		TreeMap<Integer,String> mapOut = new TreeMap();
		String line = ""; int counter=0;
		// Read the mapping to drill-down
		HashMap<String, String> mapDrillDown = new HashMap();
		int newcounter=0;
		try {
			System.out.println("inFile:"+inFile);
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				counter++;
				if(isPrintSOP){ 
					System.out.println("20.line->"+" debug_label:"+debug_label+" "+counter );
											//+" " +line);
					if(counter==1) System.out.println("1.line->"+counter+" " +line);
				}
				if(endline>0){
					//
					if(counter>=startline && counter<=endline )
					    newcounter++;
					else
						continue;
					}
				else{
					newcounter++;
				}
				
				mapOut.put(newcounter, line);
			}
			
			System.out.println("END counter, line->"+counter+" " +line);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	 
	// main
	public static void main(String[] args) {
		
		
		String inputFile_with_dup="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.dup.txt";
		String outputFile_with_nodup="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup.txt";
		inputFile_with_dup="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/thehindu.backup.success./2006-2009/test2.txt";
		outputFile_with_nodup="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/thehindu.backup.success./2006-2009/test2_nodup.txt";
		
		inputFile_with_dup="/Users/lenin/Downloads/#problems/p8/Full Article/samples/sample.on.keywords_ds1/_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup.txt";
		outputFile_with_nodup="/Users/lenin/Downloads/#problems/p8/Full Article/samples/sample.on.keywords_ds1/_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup.txt";
		
		inputFile_with_dup="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/tmp/tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL.txt";
		outputFile_with_nodup="";
	
		//loadEachLineToMap("");
		//loadEachGoogleAlertLineToMap("/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE/crawler/conf/googlealert.txt");
		ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
		readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
																							inputFile_with_dup,//inFile, 
																							-1, -1,
																							outputFile_with_nodup,//outFile, 
																							false, //is_Append_outFile, 
																							false, //is_Write_To_OutputFile
																							"", //debug_label
																							1,   //token_to_be_used_as_primarykey
																							true //isSOPprint 
																							);
		
	}
	
}