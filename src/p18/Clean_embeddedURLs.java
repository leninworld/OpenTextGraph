package p18;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

// (1) REMOVE GOOGLE NEWS
public class Clean_embeddedURLs {
	
	//http://news.google.com/news/url?sa=t&fd=r&ct2=in&usg=afqjcnghy5ge-bavds19nghq8c1br8vwlg&clid=c3a7d30bb8a4878e06b80cf16b898331&cid=52779019702858&ei=xwwevqdvd8yqhah594vwaq&url=http://www.hindustantimes.com/punjab/dog-delays-ai-dubai-flight-by-over-3-hours/story-hbfesddioocodjlfqa0ymi.html
	public static String clean_get_embeddedURL(String inputStringURL){
 
		 
		try {
			 
 				 // 
				 if(inputStringURL.indexOf("url=")>=0  ){
					//outString=outString+" ";
					
					inputStringURL=inputStringURL.substring(inputStringURL.indexOf("url=")+"url=".length() ,  inputStringURL.length());
				}
				else{
					return inputStringURL;
				}
			 
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return inputStringURL;
	}
	
	//
	public static void clean_embeddedURL(
										String baseFolder,
										String file1,
										int    token_having_URL,
										String OUTPUT_file3
										){
		
		int count_half_match=0;
		TreeMap<String, String> map_file1_keyword_bodytext=new TreeMap<String, String>();
		TreeMap<String, String> map_file2_keyword_bodytext=new TreeMap<String, String>();
		FileWriter writer=null;
		String curr_URL=""; 
		try {
			writer=new FileWriter(new File(OUTPUT_file3));
			 
			// 
			TreeMap<Integer,String> map_file1=
						 ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								  file1,
																	 							  -1, //startline, 
																	 							  -1, //endline,
																	 							  "f1 ", //debug_label
																	 							  false //isPrintSOP
																							 	  );
			
			String eachLine_reconstruct="";
			//
			for(int seq:map_file1.keySet()){
				String []s=map_file1.get(seq).split("!!!");
				
				if(s.length<token_having_URL) {continue;}
				
				curr_URL=s[token_having_URL-1];
				//remove GOOGLE NEWS TAG
				curr_URL=clean_get_embeddedURL(curr_URL);
				int c=0;
				eachLine_reconstruct="";
				//
				while(c<s.length){
				
					if(eachLine_reconstruct.length()==0){
						if(c==(token_having_URL-1)){
							eachLine_reconstruct=curr_URL;
						}
						else{
							eachLine_reconstruct=s[c];
						}
						 
					}
					else{
						if(c==(token_having_URL-1)){
							eachLine_reconstruct=eachLine_reconstruct+"!!!"+curr_URL;
						}
						else{
							eachLine_reconstruct=eachLine_reconstruct+"!!!"+s[c];
						}
						
					}
					
					c++;
				}
				// WRITE 
				writer.append(eachLine_reconstruct+"\n");
				writer.flush();
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	// main
	public static void main(String[] args) throws Exception{
		
		//
		String baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Feeds-Newscopy/Feeds-Trad-17-Feb-2015-Set1-XML2CSV/tmp/";
		baseFolder="/Users/lenin/Downloads/#problems/p18/ds1/before_17Feb_set2/";
		
		String file1_=baseFolder+"crawled/CRAWLED_output.txt";
			   file1_=baseFolder+"bfr_17feb_set2_all_issue_body_URL_datetime_RSSFEED.txt";
		String OUTPUT_file3=baseFolder+"bfr_17feb_set2_all_issue_body_URL_datetime_RSSFEED_CLEANEDurl.txt";
		
		int token_having_URL=2;

		// main
		clean_embeddedURL(
							  baseFolder,
							  file1_,
						      token_having_URL,
							  OUTPUT_file3
							);
		
		
	}

}
