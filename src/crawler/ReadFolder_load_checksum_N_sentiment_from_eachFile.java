package crawler;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

public class ReadFolder_load_checksum_N_sentiment_from_eachFile {
	
	
	//readFolder_load_checksum_N_sentiment_from_eachFile
	public static TreeMap<Integer,TreeMap<String,String>> readFolder_load_checksum_N_sentiment_from_eachFile(String inFolder, String debugFile, boolean isSOPprint){
		
		 TreeMap<Integer,TreeMap<String,String>> mapOut=new TreeMap<Integer, TreeMap<String,String>>();
		
		 TreeMap<String,String> mapOut_checkSum_Score = new TreeMap<String,String>();
		 TreeMap<String,String> mapOut_CompressedSentence_Score = new TreeMap<String,String>();
		
		 String [] arr_files=(new File(inFolder)).list();
		 int len_arr_files=arr_files.length;
		 int cnt=0;
		 TreeMap<Integer,String> map_inFile_eachLine=new TreeMap<Integer, String>();
		 
		try {
			FileWriter writer_debug=new FileWriter(new File(debugFile),true);
			//
			while(cnt<len_arr_files){
				//SKIP if its a sub-folder
				 if(new File(arr_files[cnt]).isDirectory() || arr_files[cnt].indexOf("Store")>=0 ){
					 System.out.println("skip subfolder");
					 cnt++ ; continue;
				 }
				System.out.println("LineNo:"+cnt+1);
				 //get interested token number values from file
				map_inFile_eachLine=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
								 															 inFolder+arr_files[cnt], 
																						 	 -1, 
																							 -1,
																							 " createGBADgraphFromTextUsingNLP ", //debug_label
																							 false //isPrintSOP
																							 );
				
				for(int lno:map_inFile_eachLine.keySet()){
					String line=map_inFile_eachLine.get(lno).replace("!!!!!!", "!!!a!!!");
					String [] token=line.split("!!!");
					
					if(isSOPprint)
						System.out.println("line:"+lno +"-->"+map_inFile_eachLine.get(lno));
					
					if(token.length<2) {
						continue;
					}
					// score numeric
					if(IsNumeric.isNumeric(token[1]) ){
						mapOut_checkSum_Score.put( token[0],  token[1] );
						if(isSOPprint)
							System.out.println("line:tokens:"+map_inFile_eachLine.get(lno));
						if(token[2].length()>1) //sentence not blank
							mapOut_CompressedSentence_Score.put( token[2],  token[1] );
					}
				}
				cnt++;
				
			}
			writer_debug.append( "\n size of map_inFile_eachLine.size:"+map_inFile_eachLine.size() );
			writer_debug.flush();
			
			mapOut.put( 1, mapOut_checkSum_Score);
			mapOut.put( 2, mapOut_CompressedSentence_Score);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mapOut;
	}

	// main
    public static void main (String[] args) throws java.lang.Exception{
    	
    }
	
}
