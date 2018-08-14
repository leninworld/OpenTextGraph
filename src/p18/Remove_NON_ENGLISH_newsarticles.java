package p18;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

import crawler.Find_domain_name_from_Given_URL;
import crawler.Load_domainname_pattern4crawledHTML;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

//
public class Remove_NON_ENGLISH_newsarticles {
	
	// remove_NON_ENGLISH_newsarticles 
	// file1 -> file having URL and other column such as bodyText
	public static void remove_NON_ENGLISH_newsarticles(
														String  baseFolder,
														String  file1,
														int 	token_having_URL,
														String  file2_DomainName_NewsStartPattern,
														String  OUTPUT_file3, //out
														boolean isSOPprint
													   ){
		//LOADING
		TreeMap<String, String> map_domainName_NewsStartPattern=Load_domainname_pattern4crawledHTML.load_domainname_pattern4crawledHTML(file2_DomainName_NewsStartPattern);
		FileWriter writer_ENGLISH=null;
		FileWriter writer_NOT_ENGLISH=null;
		FileWriter writerDebug=null;
		try {
			writer_ENGLISH=new FileWriter(new File(OUTPUT_file3+"_ENGLISH.txt"));
			writer_NOT_ENGLISH=new FileWriter(new File(OUTPUT_file3+"_NOT_ENGLISH.txt"));
			writerDebug=new FileWriter(new File(OUTPUT_file3+"_DEBUG.txt"));
					
			
			// map_file1
			TreeMap<Integer,String> map_file1=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								  file1,
																	 							  -1, //startline, 
																	 							  -1, //endline,
																	 							  "f1 ", //debug_label
																	 							  false //isPrintSOP
																							 	  );
			String url=""; int hit_english=0; int hit_non_english=0;
			//
			for(int seq:map_file1.keySet()){
				String eachLine=map_file1.get(seq);
				//
				String []s = eachLine.split("!!!");
				if(s.length>=token_having_URL){
					//clean URL
					if(s[token_having_URL-1].indexOf("url=")>=0)
						url = Clean_embeddedURLs.clean_get_embeddedURL( s[1].replace("new():", ""));
					else
						url = s[token_having_URL-1].replace("new():", "");
				
					//domain_name
					String domain_name=Find_domain_name_from_Given_URL.find_domain_name_from_Given_URL(url, false);
					boolean is_english=false;
					//check if this domain name is NON ENGLISH
					if(map_domainName_NewsStartPattern.containsKey(domain_name)){
						String startPattern=map_domainName_NewsStartPattern.get(domain_name);
						//NOT ENGLISH
						if(startPattern.toLowerCase().indexOf("not")>=0 && startPattern.toLowerCase().indexOf("english")>=0){
							is_english=false;
						}
						else{
							is_english=true;
						}
						
					}
					else{
						//domain name not avialable, mark it as english
						is_english=true; 	
					}
					
					if(is_english){
						writer_ENGLISH.append(eachLine+"\n");
						writer_ENGLISH.flush();
						hit_english++;
					}
					else{
						writer_NOT_ENGLISH.append(eachLine+"\n");
						writer_NOT_ENGLISH.flush();
						hit_non_english++;
					}
					
				}
			} //
			
			System.out.println("map_file1.size:"+map_file1.size() +" hit_english:"+hit_english
								+" hit_non_english:"+hit_non_english);
			System.out.println("english->"+OUTPUT_file3+"_ENGLISH.txt" 
								+"\nnon english->"+OUTPUT_file3+"_NOT_ENGLISH.txt");
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
		String  baseFolder="/Users/lenin/Downloads/#problems/p18/ds1/17feb_set1/";
				baseFolder="/Users/lenin/Downloads/#problems/p18/ds1/before_17Feb_set2/";
		
		String  file1=baseFolder+"bfr_17feb_set2_OUTPUT_merged_full_bodyText.txt";
		int 	token_having_URL=1;
		String  file2_DomainName_NewsStartPattern="/Users/lenin/Downloads/#problems/p18/ds1/global_domainNAMES_patternFORNEWSBODYSTART.txt";
		String  OUTPUT_file3=baseFolder+"bfr_17feb_set2_OUTPUT_merged_full_bodyText_filter_"; //out
		boolean isSOPprint=false;
		
		//remove_NON_ENGLISH_newsarticles
		remove_NON_ENGLISH_newsarticles(
								   		baseFolder,
								   		file1,
								   		token_having_URL,
								   		file2_DomainName_NewsStartPattern,
								   		OUTPUT_file3, //out
								   		isSOPprint);
		 
		
	}

}
