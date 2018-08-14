package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;
import crawler.*;

public class ReadFile_pickAtoken_removeHTMLtags_removeCtrlChar_write2AnotherFile {
	
	//readFile_pickAtoken_removeHTMLtags_removeCtrlChar_write2AnotherFile
	public static boolean readFile_pickAtoken_removeHTMLtags_removeCtrlChar_write2AnotherFile(
																							String inFile,
																							int    token_index_of_interest
																						    ){
		try {
			int lineno=0;
			FileWriter writer=new FileWriter(new File(inFile+"_cleaned.txt"));
			
			//load to map
			TreeMap<Integer,String> map_eachLine=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																							  inFile
																							, -1
																							, -1
																							, "" //debug_label
																							, false// isPrintSOP
																							);
			
			//
			for(int seq:map_eachLine.keySet()){
				String concLine="";int cnt2=0;
				String[] s=map_eachLine.get(seq).split("!!!");
				//
				while(cnt2<s.length){
					
					//interested token.
					if(cnt2==token_index_of_interest-1){
						// remove tag and control character
						String tmp=	 RemoveUnicodeChar.removeUnicodeChar(
								Crawler.removeTagsFromHTML(
																					s[token_index_of_interest-1]+">"
																							,false
																					 )
								);
						if(concLine.length()==0)
							concLine=tmp;
						else
							concLine=concLine+"!!!"+tmp;
					}
					else{
						if(concLine.length()==0)
							concLine=s[cnt2];
						else
							concLine=concLine+"!!!"+s[cnt2];
					}
					
					cnt2++;
				}
				writer.append(concLine+"\n");
				writer.flush();
				lineno= seq;
			}
			
			System.out.println("\n ** Total no lines ->"+lineno+ " for file "+inFile+ "\n");
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return true;
	}
	    
		// readFile_pickAtoken_removeHTMLtags_removeCtrlChar_write2AnotherFile
		public static boolean readFile_WRAPPER_pickAtoken_removeHTMLtags_removeCtrlChar_write2AnotherFile(
																								String inFolder,
																								int    token_index_of_interest
																							  ){
			int cnt=0;
			try {
				File f=new File(inFolder);
				String [] filesOfgivenFolder=f.list();
				// each file 
				while(cnt<filesOfgivenFolder.length){ // while start
					if(filesOfgivenFolder[cnt].indexOf("Store")>=0) {cnt++;continue;}
					//subdirectory skip
					if(new File(filesOfgivenFolder[cnt]).isDirectory() ) {cnt++;continue;}
					
					String currFile=inFolder+filesOfgivenFolder[cnt];
					// 
					readFile_pickAtoken_removeHTMLtags_removeCtrlChar_write2AnotherFile(currFile, token_index_of_interest);
					cnt++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			return true;
		}
	
	// main 
	public static void main(String[] args) throws IOException {
		String baseFolder="/Users/lenin/OneDrive/Maitrayi/Full Article/ndtv/";
		String inFile=baseFolder+"toi.only.full.article.0.o1.txt";
		int token_index_of_interest=2;
		
		// remove html tag n ctrl
//		readFile_pickAtoken_removeHTMLtags_removeCtrlChar_write2AnotherFile(inFile,
//																		    token_index_of_interest
//																			);
		
		
		//wrapper for input as folder
		readFile_WRAPPER_pickAtoken_removeHTMLtags_removeCtrlChar_write2AnotherFile(baseFolder,
																					token_index_of_interest
																					);
		
		
	}

}
