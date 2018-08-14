package crawler;

import java.util.TreeMap;


public class Load_domainname_pattern4crawledHTML {
	
	
	// load 
	public static TreeMap<String, String> load_domainname_pattern4crawledHTML(String inFile){
		TreeMap<String, String> mapOut_new=new TreeMap<String, String>();
		 
		try{
			TreeMap<Integer,String> map_file1=
						 ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																									inFile,
																									-1, //startline, 
																									-1, //endline,
																									" ", //debug_label
																									false //isPrintSOP
																							 	  	);
			// 
			for(int seq:map_file1.keySet()){
				String []s=map_file1.get(seq).split("!!!");
				
				if(s.length>=2)
					mapOut_new.put(s[0], s[1]);
				else{
					System.out.println("error no tokens:"+map_file1.get(seq));
				}
			}
		 
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut_new;
		
	}
	
	 
 
 

	// main
	public static void main(String[] args) {
		
		String baseFolder="/Users/lenin/Downloads/#problems/p18/ds1/";
		String inFile=baseFolder+"all_all_URL_DISTINCT_domainNAMES_PATTERNforNEWSBODYStart.txt";
		
		
		
		
		//
		TreeMap<String, String> map=load_domainname_pattern4crawledHTML(inFile);
		
		//testing
		String test = map.get("abcnews.go.com");
		System.out.println("test pattern "+test);
		String mainstring="here it is . <div class=\"article-copy\">. whtat is good.";
		System.out.println(mainstring.indexOf(test));
		
		
	}
	
	
}
