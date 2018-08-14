package p18;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

//clean_htmlcrawled 
public class Clean_htmlcrawled_remove_htmlTagsETC {
	
	// NOTE: Check an input token and see if this token is a probably of html tag such as < or > etc rather than text
	// Only extract text (and NOT html tags or scripts)
	public static String remove_htmlTagsETC(String inputString){
		String outString="";
		inputString=inputString.replace("<", " <");
		try {
			String [] s=inputString.split(" ");
			int len=s.length; int cnt=0;
			// increase length
			while(cnt<len){
				String ctoken=s[cnt];
				//
//				if(ctoken.indexOf("<p ")>=0){
//					outString=outString+" "+ctoken;
//				}
				 
				 // 
				 if(ctoken.indexOf(">")>=0 ||ctoken.indexOf("<")>=0 ||ctoken.indexOf("=")>=0 ||ctoken.indexOf("return")>=0
						||ctoken.indexOf("function")>=0||ctoken.indexOf("{")>=0||ctoken.indexOf("}")>=0
						||ctoken.indexOf("]")>=0||ctoken.indexOf("[")>=0||ctoken.indexOf("-")>=0
						||ctoken.indexOf("fonts")>=0		||ctoken.indexOf("?")>=0
						||ctoken.indexOf(":")>=0||ctoken.indexOf("(")>=0||ctoken.indexOf(")")>=0
						||ctoken.toLowerCase().indexOf("().")>=0||ctoken.toLowerCase().indexOf("px ")>=0
						||ctoken.toLowerCase().indexOf("_")>=0
						||ctoken.toLowerCase().indexOf("div-")>=0
						||ctoken.toLowerCase().indexOf("div-")>=0
						||ctoken.toLowerCase().indexOf("button-")>=0 ||ctoken.toLowerCase().indexOf(" function -")>=0
						){
					cnt++;
					//outString=outString+" ";
					continue;	
				}
				else{
					outString=outString+" "+ctoken;
				}
				
				cnt++;
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return outString;
	}
	
	//
	public static void clean_htmlcrawled(
										String baseFolder,
										String file1_from_crawled,
										int    token_having_primarykey,
										int    token_having_crawledHTML,
										String OUTPUT_file3
										){
		
		int count_half_match=0;
		TreeMap<String, String> map_file1_keyword_bodytext=new TreeMap<String, String>();
		TreeMap<String, String> map_file2_keyword_bodytext=new TreeMap<String, String>();
		FileWriter writer=null;
		String curr_primarykey=""; String curr_crawledhtml="";
		try {
			writer=new FileWriter(new File(OUTPUT_file3));
			 
			// 
			TreeMap<Integer,String> map_file1=
						 ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								   file1_from_crawled,
																	 							  -1, //startline, 
																	 							  -1, //endline,
																	 							  "f1 ", //debug_label
																	 							  false //isPrintSOP
																							 	  );
			
			//
			for(int seq:map_file1.keySet()){
				String []s=map_file1.get(seq).split("!!!");
				
				if(s.length<token_having_crawledHTML) {continue;}
				
				curr_primarykey=s[token_having_primarykey-1];
				curr_crawledhtml=s[token_having_crawledHTML-1];
				//remove all tags and junks in the HTML CRAWLED
				curr_crawledhtml=remove_htmlTagsETC(curr_crawledhtml);
				
				writer.append(curr_primarykey+"!!!"+curr_crawledhtml+"\n");
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
		String baseFolder="/Users/lenin/Downloads/#problems/p18/ds1/before_17Feb_set2/";
		String file1_from_crawledHTML=baseFolder+"crawled/CRAWLED_output.txt";
		       file1_from_crawledHTML=baseFolder+"all_all_body_URL_datetime_set2.txt";
		
		String OUTPUT_file3=baseFolder+"OUTPUT_merged_full_bodyText.txt";
		int token_having_primarykey=1;
		int token_having_crawledHTML=2;

		//
		clean_htmlcrawled(
							  baseFolder,
							  file1_from_crawledHTML,
						      token_having_primarykey,
							  token_having_crawledHTML,
							  OUTPUT_file3
							);
		
		
	}

}
