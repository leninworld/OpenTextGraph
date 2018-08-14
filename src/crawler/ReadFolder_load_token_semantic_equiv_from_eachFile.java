package crawler;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

public class ReadFolder_load_token_semantic_equiv_from_eachFile {
	
	
	//readFolder_load_token_semantic_equiv_from_eachFile
	public static TreeMap<String,TreeMap<Integer,String> > readFolder_load_token_semantic_equiv_from_eachFile(String inFolder,
																											  String debugFile,
																											  boolean isSOPprint
																											  ){
		 TreeMap<String,TreeMap<Integer,String> > mapOut_token_semantic_equiv = new TreeMap<String,TreeMap<Integer,String>>();
		 TreeMap<Integer,String> mapTemp=new TreeMap<Integer, String>();
		 
		 String [] arr_files=(new File(inFolder)).list();
		 int len_arr_files=arr_files.length;
		 int cnt=0;
		 
		try {
			FileWriter writer_debug=new FileWriter(new File(debugFile),true);
			//
			while(cnt<len_arr_files){
				//SKIP if its a sub-folder
				 if(new File(arr_files[cnt]).isDirectory() || arr_files[cnt].indexOf("Store")>=0 ){
					 System.out.println("skip subfolder");
					 cnt++ ; continue;
				 }
				
				 //get interested token number values from file
				TreeMap<Integer,String> map_inFile_eachLine=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
								 															 inFolder+arr_files[cnt], 
																						 	 -1, 
																							 -1,
																							 " createGBADgraphFromTextUsingNLP ", //debug_label
																							 false //isPrintSOP
																							 );
				
				
				for(int lno:map_inFile_eachLine.keySet()){
					String [] token=map_inFile_eachLine.get(lno).split("!!!");
					
					
					if(!mapOut_token_semantic_equiv.containsKey( token[0])){
						mapTemp.put(1, token[1] );
						mapOut_token_semantic_equiv.put( token[0], mapTemp) ;
					}
					else{
						mapTemp= mapOut_token_semantic_equiv.get( token[0]);
						int cnt2=mapTemp.size()+1;
						mapTemp.put(cnt2, token[1] );
						mapOut_token_semantic_equiv.put( token[0], mapTemp) ;
						
					}
					mapTemp=new TreeMap<Integer, String>();
				}
				cnt++;	
			}
			writer_debug.append("size of mapOut_token_semantic_equiv:"+mapOut_token_semantic_equiv.size());
			writer_debug.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mapOut_token_semantic_equiv;
	}

	// main
    public static void main (String[] args) throws java.lang.Exception{
    	
    }
	
}

