package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
public class Find_links_from_Google_Search_Engine {
 
	//find links from google search engine
	public static TreeMap<String,String> find_links_from_Google_Search_Engine(
														    String searchTerm,
														    int num,
														    String proxy_ip_and_port
														    ){
	       //Taking search term input from console
		 
		 final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
		 TreeMap<String,String> mapResults=new TreeMap<String, String>();
		 System.out.println("proxy:"+proxy_ip_and_port);
	     String []s=proxy_ip_and_port.split("!!!");
	    
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please enter the search term.");
//        String searchTerm = scanner.nextLine();
//        System.out.println("Please enter the number of results. Example: 5 10 20");
//        int num = scanner.nextInt();
//        scanner.close();
         
        String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm+"&num="+num;
        //without proper User-Agent, we will get 403 error
        Document doc; int random_number=1;
		try {
			Properties systemProperties = System.getProperties();
			//  NOT null proxy_ip_and_port
			if(proxy_ip_and_port.length()>4){
				systemProperties.setProperty("http.proxyHost",s[0]);//proxy_ip);
				systemProperties.setProperty("http.proxyPort",s[1]);//proxy_port);
			}
			
			System.out.println("searchURL:"+searchURL);
			doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();

	        //below will print HTML data, save it to a file and open in browser to compare
	        //System.out.println(doc.html());
	         
	        //If google search results HTML change the <h3 class="r" to <h3 class="r1"
	        //we need to change below accordingly
	        Elements results = doc.select("h3.r > a");
	 
	        for (Element result : results) {
	            String linkHref = result.attr("href");
	            String linkText = result.text();
	            System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(6, linkHref.indexOf("&")));
	            mapResults.put(linkText, linkHref.substring(6, linkHref.indexOf("&")));
	        }
        
		} catch (IOException e) {
			mapResults.put("status", "failed");
			System.out.println("Catch: Failed *************"+e.getMessage());
			return mapResults;
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		mapResults.put("status", "success");
		return mapResults;
    }
	//wrapper
	public static void find_wrapper_for_find_links_from_Google_Search_Engine_for_keywords_from_file(
																			String input_Keyword_File,
																			String output_File_1,
																			String output_already_searched_keywords_File,
																			boolean is_Append_output_File_1,
																			String output_File_2,
																			boolean is_Append_output_File_2,
																			int 	num_results_required, 
																			String add_Pattern_to_each_line_of_input_file,
																			String input_proxy_ip_address_file  //input_proxy_ip_and_port_file={"",<file>}
																			){
		 TreeMap<String,String> mapResults=new TreeMap<String, String>();
		 TreeMap<Integer, String> mapProxyList=new TreeMap<Integer, String>();
		//not NULL = input PROXY FILE given
		if(input_proxy_ip_address_file.length()>2){
					 mapProxyList=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
				 							  readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
												 (input_proxy_ip_address_file
														 ,-1
														 ,-1
														 ,"" //debug_label
		        										 , false // isSOPprint
														 );
		 }
		
		 //already searched keywords (so to skip)
		 TreeMap<Integer, String> map_Already_Searched_Keywords=
				 	ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
				  							   readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
				  							   (output_already_searched_keywords_File, 
				  								-1, 
				  								-1,
				  								"" //debug_label
       										    , false // isSOPprint
				  								);
		 
		 int size_of_proxy_list=mapProxyList.size();
		 int random_number=1;
		 String proxy_ip_and_port="";
		 if(size_of_proxy_list>1){
			 Generate_Random_Number_Range.generate_Random_Number_Range(1, size_of_proxy_list);
		 }
		  //
		  if(mapProxyList.containsKey(random_number)){
			  proxy_ip_and_port=mapProxyList.get(random_number);
		  }
		try {
			FileWriter writer_1=new FileWriter(new File(output_File_1), 
											    is_Append_output_File_1);
			FileWriter writer_2=new FileWriter(new File(output_File_2+".only.link.txt"), 
												is_Append_output_File_2);
			FileWriter writer_3_already_done_keywords=new FileWriter(new File(output_already_searched_keywords_File), 
																		true);
			
			//load keywords
			TreeMap<String,String> mapKeyWords=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
											   readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
											   (input_Keyword_File, 
												-1,-1, //startline, endline,
												"",//outFile, 
												false,//is_Append_outFile, 
												false,//is_Write_To_OutputFile
												"" //debug_label
												,-1 //token to be used as primary key
												,true //boolean isSOPprint
												);
			int curr_try_count=1; int max_try_count=5;
			int number_of_keywords_processed_so_far_count=1;
			//each keyword
			for(String keyword_currLine:mapKeyWords.keySet()){
				System.out.println("number_of_keywords_processed_so_far_count:"+number_of_keywords_processed_so_far_count);
				keyword_currLine= keyword_currLine.replace(" ","+")
								 +add_Pattern_to_each_line_of_input_file.replace(" ","+");
				//already done?
				if(map_Already_Searched_Keywords.containsValue(keyword_currLine)){
					System.out.println("Already done:keyword_currLine->"+keyword_currLine);
					continue;
				}
				 
				//try max times
				while(curr_try_count<=max_try_count){
					System.out.println("-------------curr_try_count:"+curr_try_count);
					// if NO proxy file given
					if(mapProxyList.size()==0){
							proxy_ip_and_port="";
					}
					// find links from google search
					mapResults=find_links_from_Google_Search_Engine(keyword_currLine, 
															        num_results_required,
															        proxy_ip_and_port);
					//mapResults.put("status", "success");
					if(mapResults.get("status").equalsIgnoreCase("success")){
						System.out.println("mapResults.get(\"status\")->"+mapResults.get("status"));
						curr_try_count=1;
						writer_3_already_done_keywords.append("\n"+keyword_currLine);
						writer_3_already_done_keywords.flush();
						break;
					}
					else{
						System.out.println("mapResults.get(\"status\")->"+mapResults.get("status"));
					}
					curr_try_count++;
					if(mapProxyList.size()>0){
						random_number=Generate_Random_Number_Range.generate_Random_Number_Range(1, size_of_proxy_list);
						proxy_ip_and_port=mapProxyList.get(random_number);
					}
					System.out.println("---------------------------");
				}
					
				
				//write to output file
				for(String link:mapResults.keySet()){
					if(!link.equalsIgnoreCase("status")){
						writer_1.append("\n"+link+"!!!"+mapResults.get(link)+"!!!keyword:"+keyword_currLine);
						writer_1.flush();
						writer_2.append("\n"+mapResults.get(link));
						writer_2.flush();
					}
				}
				
					number_of_keywords_processed_so_far_count++;
					curr_try_count=1;
			}
			
			writer_1.close();
			writer_2.close();
			writer_3_already_done_keywords.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	// main
    public static void main(String[] args) throws IOException {
    	
    	// approach 1 
    	//find_links_from_Google_Search_Engine("lenin+mookiah",10);
    	
    	String input_Keyword_File="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data/15.find_links_from_Google_Search_Engine/keywords.txt";
    	input_Keyword_File="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data/list-of-all-think-tank-from-world-from-wiki-list";
    	
    	String input_proxy_ip_and_port_file="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/proxy-ip-address-usa.txt";
    	
		String output_File_1="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/out_google_search-list-of-all-think-tank-from-world-from-wiki-list_1.txt";
		boolean is_Append_output_File_1=true;
		String output_File_2="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/out_google_search-list-of-all-think-tank-from-world-from-wiki-list_2.txt";
		//
		String output_already_searched_keywords_File="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/out_google_already_searched_success_keywords.txt";
		
		boolean is_Append_output_File_2=true;
		int num_results_required=20;
		
		String add_Pattern_to_each_line_of_input_file=" rss ";
		//approach 2 
		find_wrapper_for_find_links_from_Google_Search_Engine_for_keywords_from_file(
										  input_Keyword_File
										, output_File_1
										, output_already_searched_keywords_File
										, is_Append_output_File_1, output_File_2
										, is_Append_output_File_2, num_results_required
										, add_Pattern_to_each_line_of_input_file
										, "" //input_proxy_ip_and_port_file={"",<file>}
										);		
    	
    }
}