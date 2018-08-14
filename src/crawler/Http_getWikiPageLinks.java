package crawler;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.TreeMap;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

// This method input is a wikipedia page link (can be any link)
// Output gives list of URLs mentioned in the given input link.
public class Http_getWikiPageLinks {
	
	//http recursive call for wrapper for http get links
	public static TreeMap<Integer,String> http_recursive_call_for_wrapper_for_http_getLinks
									(  int max_depth_level, 
									   int curr_depth_level,
									   String inFile_with_list_of_url,
									   String output_to_store_list_of_URLs_already_crawled_file,
									   String outFile_1,
									   String outFile_2,
									   boolean isOutFile_1_Append,
									   boolean isOutFile_2_Append,
									   String ToFind,
									   String ToReplaceWith,
									   String prefixCounter,
									   String FilterURLOnMatchedString,
									   String FilterURL_Omit_OnMatchedString,
									   String Stop_Word_for_ExtractedText,
									   String start_tag,
									   String end_tag,
									   String debugFile,
									   boolean skip_is_english, //false means all URL given considered as ENGLISH
									   boolean isDebug,
									   boolean is_consider_only_uniqueURLdomain,
									   int 	   top_N_lines_to_skip_from_input_URL_file
									   ){
		TreeMap<Integer,String> mapOut=new TreeMap<Integer, String>();
		String option="";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Runtime rs =  Runtime.getRuntime();
		
		try {
			FileWriter writerDebug=new FileWriter(new File(debugFile.replace(".txt", ".2.txt") )
												  , true);
			// create one 
			if(! new File(output_to_store_list_of_URLs_already_crawled_file).exists()){
				//new File(output_to_store_list_of_URLs_already_crawled_file).createNewFile();
			}
			
			//load to map
			TreeMap mapAlreadyCrawledURL=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
												output_to_store_list_of_URLs_already_crawled_file
												, -1
												, -1
												, "" //debug_label
												, false// isPrintSOP
												);
			
			// max depth
			while(curr_depth_level<=max_depth_level){
				
				System.out.println("curr, max:"+curr_depth_level+";"+max_depth_level);
				System.out.println("new files:"
									+ "\ninFile_with_list_of_url:"+inFile_with_list_of_url
									+ "\noutFile_1:"+outFile_1
									+ "\noutFile_2:"+outFile_2
									);
				
				
				writerDebug.append("\nnew files:"
									+ "\ninFile_with_list_of_url:"+inFile_with_list_of_url
									+ "\noutFile_1:"+outFile_1
									+ "\noutFile_2:"+outFile_2
									+ "\ncurr, max:"+curr_depth_level+";"+max_depth_level);
				writerDebug.flush();
				
				System.out.println("-------entering.."+curr_depth_level
									+"\n;option:"+option);
			
				option="n";
				
				System.out.println("\n enter option:"+option);
				// continue
//	 			option= (new crawler()).read_input_from_command_line_to_continue_Yes_No(br);
//	 			
//	 			if(!option.equalsIgnoreCase("y"))
//	 			   	  System.exit(1);
	 			
				try{
				// call recursive
				http_wrapper_for_http_getLinks( 
												 curr_depth_level,
												 inFile_with_list_of_url,
												 mapAlreadyCrawledURL, //can be empty
												 output_to_store_list_of_URLs_already_crawled_file,
												 outFile_1,
												 outFile_2,
												 isOutFile_1_Append,
												 isOutFile_2_Append,
												 ToFind,
												 ToReplaceWith,
												 prefixCounter,
												 FilterURLOnMatchedString,
												 FilterURL_Omit_OnMatchedString,
												 Stop_Word_for_ExtractedText,
												 start_tag,
												 end_tag,
												 debugFile,
												 skip_is_english, //false means all URL given considered as ENGLISH
												 isDebug,
												 is_consider_only_uniqueURLdomain, //TRUE means only first one considered. Duplicate URLs from same domain will be ignore,
												 top_N_lines_to_skip_from_input_URL_file // top_N_lines_to_skip_from_input_URL_file
												 );
				
				}
				catch(Exception e){
					System.out.println("error:"+e.getMessage());
					writerDebug.append("error:"+e.getMessage());
					writerDebug.flush();
				}
				
				String orig_outFile_2=outFile_2;
				inFile_with_list_of_url=orig_outFile_2;
				outFile_2=orig_outFile_2.replace(".txt", ".curr."+curr_depth_level+".txt")
									  .replace(".csv", ".curr."+curr_depth_level+".csv");
				
				outFile_1=outFile_1.replace(".txt",".curr."+curr_depth_level+".txt")
								   .replace(".csv", ".curr."+curr_depth_level+".csv");
				
				option="n";
				
				System.out.println("new files:"
								+ "	\ninFile_with_list_of_url:"+inFile_with_list_of_url
									+"\noutFile_2:"+outFile_2
									+"\noutFile_1:"+outFile_1);
				System.out.println("2.curr, max:"+curr_depth_level+";"+max_depth_level);
				curr_depth_level++;
			}
			writerDebug.close();
			
			rs.gc();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mapOut;
	}
	//method to get links
	public static TreeMap<Integer, TreeMap<Integer, String>> http_wrapper_for_http_getLinks(
												   int 	  curr_iteration_level,
												   String inFile_with_list_of_url,
												   TreeMap mapAlreadyCrawledURL, //value contains URL can be empty
												   String output_to_store_list_of_URLs_already_crawled_file,
												   String outFile_1,
												   String outFile_2,
												   boolean isOutFileAppend_1,
												   boolean isOutFileAppend_2,
												   String ToFind,
												   String ToReplaceWith,
												   String prefixCounter,
												   String FilterURLOnMatchedString,
												   String FilterURL_Omit_OnMatchedString,
												   String Stop_Word_for_ExtractedText,
												   String start_tag,
												   String end_tag,
												   String debugFile,
												   boolean skip_is_english,//TRUE means all URL given considered as ENGLISH
												   boolean isDebug,
												   boolean is_consider_only_uniqueURLdomain, //TRUE means only first one considered. Duplicate URLs from same domain will be ignore, 
												   int     top_N_lines_to_skip_from_input_URL_file
												   ){
		TreeMap<Integer, TreeMap<Integer, String>> mapOut=new TreeMap<Integer, TreeMap<Integer, String>>();
		Runtime rs =  Runtime.getRuntime();
		int counter_for_lineNo_currURL=0;
		TreeMap<String, String> mapCurrRUN_AlreadyCrawled_URL=new TreeMap<String, String>();
		try {


			//
			TreeMap<String, String> mapURL=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
					readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
																		(inFile_with_list_of_url, 
																		-1, -1,
																		"", //outFile
																		true, // is_outFile_append
																		false, //is write to output file
																		"", //debug_label
																		-1, //token for primary key
																		true //boolean isSOPprint
																		);
			boolean is_valid=false;
			//read and output to file
			for(String curr_url:mapURL.keySet()){
				counter_for_lineNo_currURL++;
				//
				if(counter_for_lineNo_currURL<=top_N_lines_to_skip_from_input_URL_file){
					counter_for_lineNo_currURL++;
					continue;
				}
				
				//NO_FILTER (these NO FILTERS are FOR RSS NEWS FEED COLLECTION SEARCH). FOR OTHERS JUST COMMENT THEM
				if(curr_url.toLowerCase().indexOf(".rss")>=0 ||
				   curr_url.toLowerCase().indexOf("music")>=0 ||
				   curr_url.toLowerCase().indexOf("shopping")>=0 ||
				   curr_url.toLowerCase().indexOf("twitter")>=0 ||
				   curr_url.toLowerCase().indexOf("facebook")>=0||
				   curr_url.toLowerCase().indexOf("advertisement")>=0||
				   curr_url.toLowerCase().indexOf("newsletter")>=0||
				   curr_url.toLowerCase().indexOf("contact-us")>=0 ||
				   curr_url.toLowerCase().indexOf("articles")>=0 ||
				   curr_url.toLowerCase().indexOf("staff")>=0 ||
				   curr_url.toLowerCase().indexOf("event/")>=0 ||
				   curr_url.toLowerCase().indexOf("masters-degrees")>=0 ||
				   curr_url.toLowerCase().indexOf("weather")>=0 ||
				   curr_url.toLowerCase().indexOf("login-regist")>=0 ||
				   curr_url.toLowerCase().indexOf("cookies-policy")>=0 ||
				   (curr_url.toLowerCase().indexOf("random")>=0 &&
				   curr_url.toLowerCase().indexOf("number")>=0 ||
				   curr_url.toLowerCase().indexOf("#comments")>=0||
				   curr_url.toLowerCase().indexOf("#")>=0 ||
				   curr_url.toLowerCase().indexOf(".mov")>=0 ||
				   curr_url.toLowerCase().indexOf(".mp3")>=0 ||
				   curr_url.toLowerCase().indexOf(".mp4")>=0 ||
				   curr_url.toLowerCase().indexOf(".flv")>=0 ||
				   curr_url.toLowerCase().indexOf("movie")>=0
				   )
//				   ||
//				   curr_url.split("-").length>=3
//				   || curr_url.split("&").length>=2
					){
					System.out.println("NO_filter****** : ");
					continue;
				}
				
				
				if(mapAlreadyCrawledURL.containsValue(curr_url)){
					System.out.println("Already crawled ->"+curr_url);
					continue;
				}
				

				
				if(curr_url.length()>5){ //not empty
					is_valid=true;
					System.out.println("CURR counter_for_lineNo_currURL:"+counter_for_lineNo_currURL+
							   " (out of TOTAL:"+mapURL.size()+")"+
								" inFile_with_list_of_url:"+inFile_with_list_of_url+
								 " ;"+curr_url);
					
					try{
					String curr_DomainName=Find_domain_name_from_Given_URL.find_domain_name_from_Given_URL(curr_url, false);
					
					// if that domain is not processed
					if(  is_consider_only_uniqueURLdomain== true
					  && mapCurrRUN_AlreadyCrawled_URL.containsValue(curr_DomainName)){
						is_valid=false;
						System.out.println("Duplicate domain name***");
					}
					//
					if(!mapCurrRUN_AlreadyCrawled_URL.containsKey(curr_url)
						&& is_valid==true){
						
						mapOut=
						http_getLinks(
										curr_iteration_level,
										curr_url,
										"",//html_input_File(empty)
										output_to_store_list_of_URLs_already_crawled_file, //already crawled
								   	 	outFile_1,
								   	 	outFile_2,
								   	 	isOutFileAppend_1,
								   	 	isOutFileAppend_2,
								   	 	ToFind,
								   	 	ToReplaceWith,
								   	 	curr_url, //prefixCounter
								   	 	FilterURLOnMatchedString,
								   	 	FilterURL_Omit_OnMatchedString,
								   	 	Stop_Word_for_ExtractedText,
								   	 	start_tag,
								   	 	end_tag,
								   	 	debugFile,
								   	 	skip_is_english,//false means all URL given considered as ENGLISH
								   	 	"", //type
								   	 	isDebug);
					}
					
					mapCurrRUN_AlreadyCrawled_URL.put( curr_url,  
													   curr_DomainName);
					
					
					}
					catch(Exception e){
						 e.getMessage();
					}
				} //end if
				
			} //end for 
			rs.gc();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mapOut;
	}
	//method to get links (input is URL or html_input_file)
	@SuppressWarnings("null")
	public static TreeMap<Integer, TreeMap<Integer,String>> http_getLinks(   
														   int     curr_iteration_level,// 1 for onetime run
														   String  url, // input option 1 
														   String  html_input_File, // OR input option 2
														   String  output_to_store_list_of_URLs_already_crawled_file,
														   String  outFile_1,
														   String  outFile_2,
														   boolean isOutFile_1_Append,
														   boolean isOutFile_2_Append,
														   String ToFind,
														   String ToReplaceWith,
														   String prefixCounter,
														   String FilterURLOnMatchedString, //delimiter  !!!
														   String FilterURL_Omit_OnMatchedString, //delimiter  !!!
														   String Stop_Word_for_ExtractedText,
														   String start_tag,
														   String end_tag,
														   String debugFile,
														   boolean skip_is_English,//TRUE means all URL given considered as ENGLISH
														   String 	type,
														   boolean isDebug
														   ){
		//crawler c = new crawler();
		int strlen=0;
		boolean is_English=false;
		String  concLine_outFile_2="";
		TreeMap<Integer, TreeMap<Integer,String>> main_treeOut = new TreeMap();
		
		TreeMap<Integer,String> treeOut = new TreeMap();
		TreeMap<Integer,String> treeOut_2 = new TreeMap();
		TreeMap<String, String> is_Already_Wrote=new TreeMap<String, String>();
		TreeMap<Integer, String> mapOut = new TreeMap();
		String [] arrayFilterURLOnMatchedString=FilterURLOnMatchedString.split("!!!");
		String [] arrayFilterURL_Omit_OnMatchedString=FilterURL_Omit_OnMatchedString.split("!!!");
		int debugCounter=0;
		InputStream is = null;
		String renderedText="";
		String renderText2="";
		 
		Source source = null;
		File 	   file_outFile_1=null;
		File 	   file_outFile_2=null;
		
		try{
			FileWriter writerAlreadyCrawledFile=null;
			System.out.println("out file:"+output_to_store_list_of_URLs_already_crawled_file);
			writerAlreadyCrawledFile=new FileWriter(output_to_store_list_of_URLs_already_crawled_file, true);
			
			file_outFile_1=new File(outFile_1);
			file_outFile_2=new File(outFile_2);
			 
			// if file doesnt exists, then create it
						if (!file_outFile_1.exists()) {
							file_outFile_1.createNewFile();
						}
						if (!file_outFile_2.exists()) {
							file_outFile_2.createNewFile();
						}
//						File n=new File(outFile_1);
//						if (!n.exists()) {
//							System.out.println("NOT EXISTS file");
//						}
//						else{
//							System.out.println("EXISTS file");
//						}
						
			
			 
			FileWriter writerDebug = new FileWriter(debugFile, true);
			int i=0;
			
			if(isDebug){
			writerDebug.append(  "\n :arrayFilterURLOnMatchedString:"+arrayFilterURLOnMatchedString.length
								+"\n ;arrayFilterURL_Omit_OnMatchedString:"+arrayFilterURL_Omit_OnMatchedString.length
								);
			
			writerDebug.append("\n -----------------");
			writerDebug.append("\n printed arrayFilterURLOnMatchedString and arrayFilterURL_Omit_OnMatchedString");
			while(i<arrayFilterURLOnMatchedString.length){
				writerDebug.append("\n"+arrayFilterURLOnMatchedString[i]);
				i++;
			}
			i=0;
			writerDebug.append("\n -------%%%%%%----------");
			while(i<arrayFilterURL_Omit_OnMatchedString.length){
				writerDebug.append("\n"+arrayFilterURL_Omit_OnMatchedString[i]);	
				i++;
			}
			writerDebug.append("\n -----------------");
			writerDebug.append("\nurl:"+url);
			writerDebug.append("\noutFile_1:"+outFile_1);
			writerDebug.append("\noutFile_2:"+outFile_2);
			writerDebug.append("\ncurr_iteration_level:"+curr_iteration_level);
			}
			writerDebug.flush();
			
		if(isDebug)
			System.out.println("getWikiPageLinks.getLinks()->"+url);
		
		try{
			//source = new Source(new URL(url));
		}
		catch(Exception e){
			System.out.println("error->http_getLinks:error->url:"+url +" msg:"+e.getMessage());
			writerDebug.append("error->url:"+url +" msg:"+e.getMessage());
			writerDebug.flush();

			treeOut.put(-9, "failed!!!"+e.getMessage());
			main_treeOut.put(-9, treeOut);
		 
			return main_treeOut;
			
		}

		FileWriter writer = new FileWriter(outFile_1, isOutFile_1_Append);
		FileWriter writer2 = new FileWriter(outFile_2, isOutFile_2_Append);
		
		
		URLConnection openConnection=null;
		// url or html_input_file given
		//url not empty
		if(url.length()>4){
		 
				try{
				//approach 1:
				openConnection = new URL(url).openConnection();
				//openConnection.addRequestProperty("User-Agent", "Firefox/25.0");
				//conn.setRequestProperty("Cookie", cookies);
				openConnection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
				//openConnection.addRequestProperty("User-Agent", "Mozilla");
				openConnection.addRequestProperty("Referer", "google.com");
				openConnection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				openConnection.setRequestProperty("Accept-Language","en-us,en;q=0.5");
				openConnection.setRequestProperty("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
				
				openConnection.setRequestProperty("User-Agent","Mozilla/5.0 (X11; U; Linux i686; en-US)");
				
				openConnection.setReadTimeout(3000);//timeout
				 
				System.out.println("url-> "+url);
				is = openConnection.getInputStream();
				
				source = new Source(is);

				renderedText = source.getRenderer().toString().replace(" ", "#");
				renderText2  = source.getTextExtractor().setIncludeAttributes(true).toString();
				
				}
				catch(Exception e){
					writer.append("\nerror:!!!"+url+"!!!"+e.getMessage());
					writer.flush();
					treeOut.put(-9, "failed!!!"+e.getMessage());
					
					main_treeOut.put(-9, treeOut);
					writer.close();
					writer2.close();
					writerDebug.close();
					return main_treeOut;
				}
				
				mapOut.put(1, renderedText);
				mapOut.put(2, renderText2);
			
				// get Single URLCrawler
//				mapOut = getSingleURLCrawler.getSingleURLCrawler(url,
//																 false, //is_Custom_News_All_Element_Parsing
//																 false);
				
			
		}
		else if(html_input_File.length()>4){ //not empty
			String ss =ReadFile_CompleteFile_to_a_Single_String
					  .readFile_CompleteFile_to_a_Single_String(html_input_File);
			mapOut.put(1, ss);
			//is_English=true;
		}
		
		if(isDebug==true) 
			System.out.println("mapout:"+mapOut.get(1));
		
		String str=mapOut.get(1);
		
		if(skip_is_English==true){
			is_English=true;
		}
		else{
			//
			is_English=Find_Language_from_String.find_Language_from_String_2(str, debugFile);
		}
		System.out.println("is English:"+is_English);
		
		
		if(isDebug){
			writerDebug.append("\n----------part start--------------------");
			//
			if(str != null && str.length()>1500){
				writerDebug.append("\nstr(partial.substr-1499):"+str.substring(0, 1499));
			}
			else{
				writerDebug.append("\nstr(full):"+str);
			}
			writerDebug.append("\n----------part end--------------------");
			writerDebug.append("\n-----------full start-------------------");
			writerDebug.append("\nstr(full):"+str);
			writerDebug.append("\n-----------full end-------------------");
		}
		writerDebug.flush();
		
		// NOT English
		if(! is_English){
			System.out.println("failed (is_English).."+is_English);
			writerDebug.append("\nfailed (is_English).."+is_English);
			writerDebug.flush();
			mapOut.put(-9, "failed");
			main_treeOut.put(-9, treeOut);
			return main_treeOut;
		}
		
		//Stop_Word_for_ExtractedText
		if(Stop_Word_for_ExtractedText.length()>1){
			int idx=str.toLowerCase().indexOf(Stop_Word_for_ExtractedText.toLowerCase());
			System.out.println("system:idx:"+idx);
			writerDebug.append("\nsystem:idx:"+idx);
			writerDebug.flush();
			if(idx>0){
				str=str.substring(0, idx);
			}
			 
		}
		if(isDebug){
			writerDebug.append("\nafter stop word"+Stop_Word_for_ExtractedText);
		}
		
		writerDebug.flush();
		System.out.println("$$$$$$$");
		int beginIdx  = 0; int endIdx = 0;
		int beginIdx1 = 0; int beginIdx2 = 0;
		int cnt=0;
		if(isDebug==true){
			System.out.println("$$$$$$$$$$$$$$$$$$$" );
			System.out.println(str.length());
		}
		boolean isTrue= true;
		String conc_complete_url="";
		debugCounter=0;
		String orig_start_tag=start_tag;
		String orig_end_tag=end_tag;
		//
//		if(type.equalsIgnoreCase("URLOFthehinduprint2000To2005")){
//				if(str.indexOf("####*#")>0  ){
//					start_tag	="####*#";
//					end_tag		="####*#";
//				}
//				if(str.indexOf("#...")>=0){
//					end_tag="#...";
//					start_tag=orig_start_tag;
//				}
//		}
		
		
		
		//
		while(isTrue==true ){
			System.out.println("$$$$$$2$ : url2:"+cnt);
			cnt++; debugCounter++;
			beginIdx1 =str.indexOf(start_tag); //begin tag index
			beginIdx2 =str.indexOf("href");
			if(beginIdx1>=0 && beginIdx2==-1 )
				beginIdx=beginIdx1;
			else if(beginIdx2>=0 && beginIdx1==-1 )
				beginIdx=beginIdx2;
			else if(beginIdx1<=beginIdx2)
				beginIdx=beginIdx1;
			else
				beginIdx=beginIdx2;
			
			endIdx =str.indexOf(end_tag, beginIdx+start_tag.length() ); //end tag index
			
			//
//			if(type.equalsIgnoreCase("URLOFthehinduprint2000To2005")){
//				//
//				if(start_tag.equalsIgnoreCase("####*#") &&  beginIdx>=0 && endIdx==-1){
//					end_tag=">";
//					endIdx =str.indexOf( end_tag, beginIdx+start_tag.length() );
//				}
//				if(start_tag.equalsIgnoreCase("#<stories") &&  beginIdx>=0 && endIdx==-1){
//					end_tag="#<";
//					endIdx =str.indexOf( end_tag, beginIdx+start_tag.length() );
//				}
//			}
			
			if(isDebug){
				writerDebug.append("\n breaking..:cnt:"+cnt+";str:"+str);
				writerDebug.flush();
			}
			
			if(isDebug==true)
				System.out.println("url begin end:"+beginIdx+" "+endIdx);
			//
			if(type.equalsIgnoreCase("URLOFthehinduprint2000To2005")){
				if((beginIdx==-1 || endIdx==-1) && cnt==1){
					if(isDebug){
						writerDebug.append("\n<------\n error:error:"+str+"\n<----------");
						writerDebug.flush();
					}
				}
			}
			
			//error , not found any begin_tag or end_tag
			if(beginIdx==-1 || endIdx==-1){
				
				//
				if(   beginIdx>=0 
				   && endIdx==-1){
					
					//
					if(str.length()>=501){
						concLine_outFile_2=concLine_outFile_2+str.substring(0, 500);
					}
					else{
						concLine_outFile_2=concLine_outFile_2+str;
					}
					//URLOFthehinduprint2000To2005
					if(type.equalsIgnoreCase("URLOFthehinduprint2000To2005")){
						 int front_page_idx=str.toLowerCase().indexOf("front#page");
						 writerDebug.append("\nfront_page_idx:"+front_page_idx);
						 writerDebug.flush();
						  // front page end
						  if(front_page_idx>=0){
							  concLine_outFile_2=concLine_outFile_2+str.substring(0, front_page_idx);
						  }
								  
					}
					
					concLine_outFile_2=concLine_outFile_2.replace("\n", "").replace("\r", "").replace("\t", "")
							 							 .replaceAll("\\p{Cntrl}", "");
					
					treeOut_2.put(2, concLine_outFile_2);
					//return 
					main_treeOut.put(2, treeOut_2);
				}
				
				isTrue=false;
				if(isDebug){
					writerDebug.append("\n breaking..:cnt:"+cnt+";begin:"+beginIdx+";end:"+endIdx
										+";start_tag:"+start_tag+";end_tag:"+end_tag
										+";url:"+url);
					writerDebug.flush();
				}
				break;
				//strlen = str.length();
				//str=str.substring(endIdx, strlen);
			}
			
			String url2 = str.substring(beginIdx, endIdx).toLowerCase();
			String url2_approx_extra = "";
			//
			if(type.equalsIgnoreCase("URLOFthehinduprint2000To2005")){
				if(cnt>1){
					url2_approx_extra=str.substring(beginIdx, endIdx).toLowerCase();
				}
				else if(cnt==1){
					if(beginIdx>=201){
						url2_approx_extra=str.substring(beginIdx-200, endIdx).toLowerCase();
					}
					else{
						url2_approx_extra=str.substring(beginIdx, endIdx).toLowerCase();//regular one
					}
				}
				
			}
			
			if(isDebug){
				writerDebug.append("\n got url2.."+url2+" beginIdx:"+beginIdx+" endIdx:"+endIdx);
				writerDebug.flush();
			}
			
			//skip noise
			if(url2.indexOf("profit.ndtv.com")>=0||url2.indexOf("movies.")>=0
					||url2.indexOf("gadgets.")>=0|| url2.indexOf("sports.")>=0
			  )
				{
				
				System.out.println("****noise in url:"+url2);
				writerDebug.append("\n****noise in url:"+url2);
				writerDebug.flush();
				System.out.println("index:"+url2.indexOf("profit.ndtv.com")+"!!!"+url2.indexOf("movies.")
								  +"!!!"+url2.indexOf("gadgets.") +"!!!"+ url2.indexOf(".sports."));
				System.out.println("\nbase url:"+url);
				
				str=str.substring(endIdx, str.length());
				continue;
				}
			else{//not-noise add to tree
				System.out.println("NO noise in url:"+url2+";base url:"+url);
				//writerDebug.append("\nNO noise in url:"+url2+";base url:"+url);
				writerDebug.flush();
				treeOut.put(cnt, url2);
				
				concLine_outFile_2=concLine_outFile_2+" "+url2_approx_extra;
				
				if(isDebug){
					writerDebug.append("\n----------conc start---------------url:"+url);
					writerDebug.append("\nconcLine_outFile_2:"+concLine_outFile_2);
					writerDebug.append("\n----------conc end----------");
					writerDebug.flush();
				}
			}
			
			//
			if(!is_Already_Wrote.containsKey(url2)){
				writer.append(prefixCounter+"#"+cnt+"!!!"+url2+"\n");
				writer.flush();
				
				// complete url
				if(url2.indexOf("http")==-1){
					 
					if(prefixCounter.indexOf("#")>=0){
						conc_complete_url= prefixCounter.substring(0, prefixCounter.indexOf("#")-1) 
										   + url2;
					}
					else{
						conc_complete_url= prefixCounter + url2;
					}
				}
				else{
					conc_complete_url= url2;
				}
				conc_complete_url=	conc_complete_url.substring(0, 11)+
									conc_complete_url.substring(11, conc_complete_url.length())
									.replace("//", "/");
				// both FilterURL_Omit_OnMatchedString and FilterURLOnMatchedString
				if(FilterURL_Omit_OnMatchedString.length()>2){
					if(isDebug){
						writerDebug.append("\n if.condition. both expected match and omit");
						writerDebug.flush();
					}
					int cnt2=0;
					boolean isMatchedOmit=false;
					//
					while(cnt2<arrayFilterURL_Omit_OnMatchedString.length){
						if(isDebug){
							writerDebug.append("\n match?:"+arrayFilterURL_Omit_OnMatchedString[cnt2]
											+"!!!"+conc_complete_url
											+"!!!"+conc_complete_url.indexOf(arrayFilterURL_Omit_OnMatchedString[cnt2]
										));
							writerDebug.flush();
						}
						if(conc_complete_url.indexOf(arrayFilterURL_Omit_OnMatchedString[cnt2])>=0){		
							isMatchedOmit=true;
							break;
						}
						cnt2++;
					}
					//matched on omit
					if(isMatchedOmit){
						System.out.println("cont");
						strlen = str.length();
						str=str.substring(endIdx, strlen);
						continue;
					}
					else{
						// check on FilterURLOnMatchedString
					
						if(FilterURLOnMatchedString.length()>1){
							if(isDebug){
								writerDebug.append("\n if.condition. FilterURLOnMatchedString.length()>1 )");
								writerDebug.flush();
							}
							cnt2=0;
							boolean isMatchedExpectedString=false;
							//
							while(cnt2<arrayFilterURLOnMatchedString.length){
								if(isDebug){
									writerDebug.append("\n if.condition. 2.match?:"+arrayFilterURLOnMatchedString[cnt2]
													+"!!!"+conc_complete_url
													+"!!!"+conc_complete_url.indexOf(arrayFilterURLOnMatchedString[cnt2]
													));
									writerDebug.flush();
								}
								//
								if(conc_complete_url.indexOf(arrayFilterURLOnMatchedString[cnt2])>=0){
									isMatchedExpectedString=true;
									break;
								}
								cnt2++;
							}
							if(isDebug){
								writerDebug.append("\n isMatchedExpectedString:"+arrayFilterURLOnMatchedString[cnt2]);
								writerDebug.flush();
							}
							
							// isMatchedExpectedString
							if(isMatchedExpectedString){
								conc_complete_url=conc_complete_url.replace(ToFind, ToReplaceWith)
												  .replace("\"", "");
								
								if(Find_occurance_Count_Substring.find_occurance_Count_Substring("http", conc_complete_url)>1
									){
									conc_complete_url=Find_clean_get_last_pattern_from_a_string_having_multiple_pattern.
													  find_clean_get_last_pattern_from_a_string_having_multiple_pattern(conc_complete_url, "http");
								}
								
								conc_complete_url=conc_complete_url.replace("\n", "")
													.replace("\r", "").replace("\t", "")
													.replaceAll("\\p{Cntrl}", "");
								
								writer2.append(conc_complete_url+"!!!"+FilterURL_Omit_OnMatchedString+"\n");
								writer2.flush();
								
								treeOut.put( cnt, conc_complete_url);
							}
						}
						else{
							if(isDebug){
								writerDebug.append("\n if.condition. FilterURLOnMatchedString.length()<=1 )");
								writerDebug.flush();
							}
							
							conc_complete_url=conc_complete_url.replace(ToFind, ToReplaceWith).replace("\"", "");
							
							if(Find_occurance_Count_Substring.find_occurance_Count_Substring("http", conc_complete_url)>1
									){
									conc_complete_url=Find_clean_get_last_pattern_from_a_string_having_multiple_pattern.
													  find_clean_get_last_pattern_from_a_string_having_multiple_pattern(conc_complete_url, "http");
							}
							
							conc_complete_url=conc_complete_url.replace("\n", "").replace("\r", "").replace("\t", "")
															   .replaceAll("\\p{Cntrl}", "");
							
							writer2.append(conc_complete_url+"!!!"+FilterURL_Omit_OnMatchedString+"\n");
							writer2.flush();
							
							treeOut.put( cnt, conc_complete_url);
						}
						
						
					}
				}
				else{    //only FilterURLOnMatchedString
					if(isDebug){
						writerDebug.append("\n if.condition. only FilterURLOnMatchedString; FilterURLOnMatchedString->"+FilterURLOnMatchedString);
						writerDebug.flush();
					}
					if(FilterURLOnMatchedString.length()>2){
						int cnt2=0;
						boolean isMatchedExpectedString=false;
						//
						while(cnt2<arrayFilterURLOnMatchedString.length){
							if(isDebug){
								writerDebug.append(". if.condition. while inside if.condition. 3.match?:"+arrayFilterURLOnMatchedString[cnt2]
												  +"!!!"+conc_complete_url
												  +"!!!"+conc_complete_url.indexOf(arrayFilterURLOnMatchedString[cnt2]
												  +"!!!"
													));
								writerDebug.flush();
							}
							if(conc_complete_url.indexOf(arrayFilterURLOnMatchedString[cnt2])>=0){
								isMatchedExpectedString=true;
								break;
							}
							cnt2++;
						}
						// isMatchedExpectedString
						if(isMatchedExpectedString){
							if(isDebug){
								writerDebug.append(" if.condition. matched: match.");
								writerDebug.flush();
							}
							conc_complete_url=conc_complete_url.replace(ToFind, ToReplaceWith)
											  .replace("\"", "");
							
							if(Find_occurance_Count_Substring.find_occurance_Count_Substring("http", conc_complete_url)>1
									){
									conc_complete_url=Find_clean_get_last_pattern_from_a_string_having_multiple_pattern.
													  find_clean_get_last_pattern_from_a_string_having_multiple_pattern(conc_complete_url, "http");
								}
							
							conc_complete_url=conc_complete_url.replace("\n", "").replace("\r", "").replace("\t", "")
															   .replaceAll("\\p{Cntrl}", "");
							
							writer2.append(conc_complete_url+"!!!"+FilterURL_Omit_OnMatchedString+"\n");		
							writer2.flush();
							
							treeOut.put(cnt, conc_complete_url );// url2);
						}
					}
					else{
						if(isDebug){
							writerDebug.append("\n if.condition. No matched : url->"+url);
							writerDebug.flush();
						}
						conc_complete_url=conc_complete_url.replace(ToFind, ToReplaceWith)
									      				   .replace("\"", "");
						
						if(Find_occurance_Count_Substring.find_occurance_Count_Substring("http", conc_complete_url)>1
								){
								conc_complete_url=Find_clean_get_last_pattern_from_a_string_having_multiple_pattern.
												  find_clean_get_last_pattern_from_a_string_having_multiple_pattern(conc_complete_url, "http");
						}
						
						conc_complete_url=conc_complete_url.replace("\n", "").replace("\r", "").replace("\t", "")
													       .replaceAll("\\p{Cntrl}", "");
						
						writer2.append(conc_complete_url+"!!!"+FilterURL_Omit_OnMatchedString+"\n");
						writer2.flush();
						
						treeOut.put(cnt, conc_complete_url); // url2);
					}	
				}
				
				writer2.flush();
			}
			
			is_Already_Wrote.put(url2, url2);
			
			writer.flush();
			
			
			if(isDebug==true)
				System.out.println("extract all links:"+cnt+" "+url2);
			strlen = str.length();
			str=str.substring(endIdx, strlen);
			
			
			conc_complete_url="";
			if(str.indexOf("http")>=0  ){
				isTrue = true;
			}
			else{
				isTrue = false;
			}
			//if(debugCounter>10){System.out.println("debugCounter:"+debugCounter); isTrue=false;}
		}
		
		writerDebug.append("\n getting link:" +"Stop_Word_for_ExtractedText:"+
							Stop_Word_for_ExtractedText +";FilterURL_Omit_OnMatchedString:"+FilterURL_Omit_OnMatchedString);
		writerDebug.flush();
		// pull the link elements
		List<Element> linkElements=source.getAllElements(HTMLElementName.A);
		for (Element linkElement : linkElements) {
			cnt++;
			String href=linkElement.getAttributeValue("href");
			writerDebug.append("\n link:"+href);
			writerDebug.flush();
			if(isDebug){
				writerDebug.append("\n  if.condition. *for. linkElement:"+href );
				writerDebug.flush();
			}
			
			//Stop_Word_for_ExtractedText
			if(Stop_Word_for_ExtractedText!=null && href!=null && !Stop_Word_for_ExtractedText.equalsIgnoreCase("") ){
				if(Stop_Word_for_ExtractedText.length()>1){
					int idx=href.toLowerCase().indexOf(Stop_Word_for_ExtractedText.toLowerCase());
					System.out.println("stop2.:idx (match.):"+idx);
					if(idx>0){
						writerDebug.append("\n.. breaking..");
						writerDebug.flush();
						break;
					}
				}
			}
			//FilterURL_Omit_OnMatchedString
			if(FilterURL_Omit_OnMatchedString!=null
				&& !FilterURL_Omit_OnMatchedString.equalsIgnoreCase("") 
				&& href!=null){
				if(FilterURL_Omit_OnMatchedString.length()>1){
					if(isDebug){
					writerDebug.append("\n if.condition. FilterURL_Omit_OnMatchedString.length()>1");
					writerDebug.flush();
					}
					
					int cnt2=0;
					boolean isMatchedOmit=false;
					//
					while(cnt2<arrayFilterURL_Omit_OnMatchedString.length){
						if(isDebug){
							writerDebug.append("\n if.condition. match.1.?:"+arrayFilterURL_Omit_OnMatchedString[cnt2]
												+"!!!"+conc_complete_url
												+"!!!"+conc_complete_url.indexOf(arrayFilterURL_Omit_OnMatchedString[cnt2]
											));
							writerDebug.flush();
						}
						if(conc_complete_url.indexOf(arrayFilterURL_Omit_OnMatchedString[cnt2])>=0){		
							isMatchedOmit=true;
							break;
						}
						cnt2++;
					}
					//int idx=href.toLowerCase().indexOf(FilterURL_Omit_OnMatchedString.toLowerCase());
					//System.out.println("filter.:idx:"+idx);
					
					//if(idx>=0){
					if(isMatchedOmit){
						continue;
					}
				}
			}
			
			if(isDebug==true) 
				System.out.println("extract linkElements:"+href);
			if(href==null || href.equalsIgnoreCase("")) {
				System.out.println("#continue");
				continue;
			}
			if (href==null  || href.toLowerCase().indexOf("file:")>=0){ 
				continue;
			}
			if(href.toLowerCase().indexOf("terms_of_use")>=0 ) 
				{System.out.println("exit on end of wiki end page");break;}
			
			// A element can contain other tags so need to extract the text from it:
			String label=linkElement.getContent().getTextExtractor().toString();
			if(isDebug==true)
				System.out.println("label+href:"+label+" <"+href+'>');
			
			System.out.println("conc_complete_url:"+conc_complete_url);
			//Is any matching pattern for output URL (or String) given.
			if(FilterURLOnMatchedString!=null && FilterURLOnMatchedString.length()>1){
				if(isDebug){
					writerDebug.append("\n if.condition. FilterURLOnMatchedString.length()>1");
					writerDebug.flush();
				}
				
				int cnt2=0;
				boolean isMatchedExpectedString=false;
				 
					//
					while(cnt2<arrayFilterURLOnMatchedString.length){
						if(isDebug){
							writerDebug.append("\n while inside if.condition 4.match?:"+arrayFilterURLOnMatchedString[cnt2]
												+"!!!"+conc_complete_url
												+"!!!"+conc_complete_url.indexOf(arrayFilterURLOnMatchedString[cnt2]
												));
							writerDebug.flush();
						}
						//
						if(href.replace(ToFind, ToReplaceWith).indexOf(arrayFilterURLOnMatchedString[cnt2])>=0){
							isMatchedExpectedString=true;
							break;
						}
						cnt2++;
					}
				 
				
				if(isDebug){
					writerDebug.append("\nisMatchedExpectedString:"+isMatchedExpectedString);
					writerDebug.flush();
				}
				
				//only matched with pattern of FilterURLOnMatchedString then write. otherwise ignore
				//if(href.replace(ToFind, ToReplaceWith).indexOf(FilterURLOnMatchedString)>=0){
				if(isMatchedExpectedString){
					//
					if(!is_Already_Wrote.containsKey(href.replace(ToFind, ToReplaceWith))){
						writer.append(prefixCounter+"#"+cnt+"!!!"+href.replace(ToFind, ToReplaceWith) 
										+"\n");
						writer.flush();
						
						// complete url
						if(href.replace(ToFind, ToReplaceWith).indexOf("http")==-1){
							 
							if(prefixCounter.indexOf("#")>=0){
								conc_complete_url=prefixCounter.substring(0, prefixCounter.indexOf("#")-1) 
												   + href.replace(ToFind, ToReplaceWith);
							}
							else{
								conc_complete_url=prefixCounter+ href.replace(ToFind, ToReplaceWith);
							}
						}
						else{
							conc_complete_url= href.replace(ToFind, ToReplaceWith);
						}
						if(conc_complete_url.length()<=11)
							continue;
							
						conc_complete_url=conc_complete_url.substring(0, 11)+
												conc_complete_url.substring(11, conc_complete_url.length())
												.replace("//", "/");
						//omit
						if(FilterURL_Omit_OnMatchedString.length()>1){
							if(conc_complete_url.indexOf(FilterURL_Omit_OnMatchedString)>=0){
								continue;
							}
							else{
								conc_complete_url=conc_complete_url.replace(ToFind, ToReplaceWith).replace("\"", "");
								
								if(Find_occurance_Count_Substring.find_occurance_Count_Substring("http", conc_complete_url)>1
										){
										conc_complete_url=Find_clean_get_last_pattern_from_a_string_having_multiple_pattern.
														  find_clean_get_last_pattern_from_a_string_having_multiple_pattern(conc_complete_url, "http");
								}
								
								conc_complete_url=conc_complete_url.replace("\n", "").replace("\r", "").replace("\t", "")
																   .replaceAll("\\p{Cntrl}", "");
								
								writer2.append(conc_complete_url+"\n");
								writer2.flush();
								
								treeOut.put( cnt, conc_complete_url);
							}
						}
						else{
							conc_complete_url=conc_complete_url.replace(ToFind, ToReplaceWith).replace("\"", "");
							
							if(Find_occurance_Count_Substring.find_occurance_Count_Substring("http", conc_complete_url)>1
									){
									conc_complete_url=Find_clean_get_last_pattern_from_a_string_having_multiple_pattern.
													  find_clean_get_last_pattern_from_a_string_having_multiple_pattern(conc_complete_url, "http");
							}
							
							conc_complete_url=conc_complete_url.replace("\n", "").replace("\r", "").replace("\t", "")
															   .replaceAll("\\p{Cntrl}", "");
							
							writer2.append(conc_complete_url+"\n");
							writer2.flush();
							
							treeOut.put( cnt, conc_complete_url);
						} 
						
						writer2.flush();
						
					}
					
					is_Already_Wrote.put(href.replace(ToFind, ToReplaceWith), 
							 			 href.replace(ToFind, ToReplaceWith));
					
					writer.flush();
					System.out.println(" .match: filter:"+FilterURLOnMatchedString
										+";prefixCounter:"
										+prefixCounter+"#"+cnt+" "+href.replace(ToFind, ToReplaceWith) 
										+";outFile_2:"+file_outFile_2.getName() +"\n");
					if(isDebug){
					writerDebug.append("\n if.condition .match: filter:"+FilterURLOnMatchedString
										+";prefixCounter:"
										+prefixCounter+"#"+cnt+" "+href.replace(ToFind, ToReplaceWith) 
										+";outFile_2:"+ file_outFile_2.getName() +"\n");
					writerDebug.flush();
					}
					
				}
				else{
					System.out.println(".continue:..filter *:"+FilterURLOnMatchedString
										+" not match in found URL..found:"+href);
					if(isDebug){
						writerDebug.append("\n if.condition (else) .continue:..filter *:"+FilterURLOnMatchedString
											+" not match in found URL..found:"+href);
						writerDebug.flush();
					}
					continue;
				}
			}
			else{
				if(isDebug){
					writerDebug.append("\n if.condition. FilterURLOnMatchedString.length()<=1");
					writerDebug.flush();
				}
				//
				if(!is_Already_Wrote.containsKey(href.replace(ToFind, ToReplaceWith))){
					writer.append(prefixCounter+"#"+cnt+"!!!"
								 +href.replace(ToFind, ToReplaceWith) +"\n");
					writer.flush();
					
					// complete url
					if(href.replace(ToFind, ToReplaceWith).indexOf("http")==-1){
						
						if(prefixCounter.indexOf("#")>=0){
							conc_complete_url= prefixCounter
											   .substring(0, prefixCounter.indexOf("#")-1) 
											   +href.replace(ToFind, ToReplaceWith);
						}
						else{
							conc_complete_url=    prefixCounter
									   			+ href.replace(ToFind, ToReplaceWith);
						}
						
					}
					else{ //convert partial to full url
						conc_complete_url= href.replace(ToFind, ToReplaceWith);
					}
					if(conc_complete_url.length()<=11){
						continue;
					}
					conc_complete_url=  conc_complete_url.substring(0, 11)+
										conc_complete_url.substring(11, conc_complete_url.length())
										.replace("//", "/");
					
					if(FilterURL_Omit_OnMatchedString.length()>1){
						if(conc_complete_url.indexOf(FilterURL_Omit_OnMatchedString)>=0){
							continue;
						}
						else{
							conc_complete_url=conc_complete_url.replace(ToFind, ToReplaceWith)
											  				   .replace("\"", "");
							
							if(Find_occurance_Count_Substring.find_occurance_Count_Substring("http", conc_complete_url)>1
									){
									conc_complete_url=Find_clean_get_last_pattern_from_a_string_having_multiple_pattern.
													  find_clean_get_last_pattern_from_a_string_having_multiple_pattern(conc_complete_url, "http");
							}
							
							conc_complete_url=conc_complete_url.replace("\n", "").replace("\r", "").replace("\t", "")
									  											.replaceAll("\\p{Cntrl}", "");
							
							writer2.append(conc_complete_url+"\n");
							writer2.flush();
							
							
							
							treeOut.put( cnt, conc_complete_url);
						}
					}
					else{
						conc_complete_url=conc_complete_url.replace(ToFind, ToReplaceWith)
								  		  .replace("\"", "");
						
						if(Find_occurance_Count_Substring.find_occurance_Count_Substring("http", conc_complete_url)>1
								){
								conc_complete_url=Find_clean_get_last_pattern_from_a_string_having_multiple_pattern.
												  find_clean_get_last_pattern_from_a_string_having_multiple_pattern(conc_complete_url, "http");
						}
						
						if(Find_occurance_Count_Substring.find_occurance_Count_Substring("http", conc_complete_url)>1
								){
								conc_complete_url=Find_clean_get_last_pattern_from_a_string_having_multiple_pattern.
												  find_clean_get_last_pattern_from_a_string_having_multiple_pattern(conc_complete_url, "http");
							}
						
						conc_complete_url=conc_complete_url.replace("\n", "").replace("\r", "").replace("\t", "")
								  										     .replaceAll("\\p{Cntrl}", "");
						
						writer2.append(conc_complete_url+"\n");	
						writer2.flush();
						
						treeOut.put( cnt, conc_complete_url);
					}
					
					writer2.flush();
					if(isDebug){
					writerDebug.append(" already wrote? match.");
					writerDebug.flush();
					}
				}
				is_Already_Wrote.put(href.replace(ToFind, ToReplaceWith), 
									 href.replace(ToFind, ToReplaceWith));
				
				writer.flush();
				System.out.println("writing->"+prefixCounter+"#"+cnt
								 +" "+href.replace(ToFind, ToReplaceWith) +"\n");
			}
			conc_complete_url="";
			writer.flush();
			writer2.flush();
			
			//writerAlreadyCrawledFile.close();
		}
			writerAlreadyCrawledFile.append(url+"\n");
			writerAlreadyCrawledFile.flush();
			writer.close();
			writer2.close();
			writerDebug.flush();
			writerDebug.close();

		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("getWikiPageLinks.getLinks():"+treeOut.size());
		
		main_treeOut.put(1, treeOut);
		
		return main_treeOut;
	}
	// main args
	public static void main(String[] args){
		String folder = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/";
		long t0 = System.nanoTime();
		String url="http://guides.library.upenn.edu/content.php?pid=323076&sid=2644795";
		//url="https://en.wikipedia.org/wiki/International_Yoga_Day";
		String FilterURLOnMatchedString="wikipedia";
		String FilterURL_Omit_OnMatchedString="Category:";
		String Stop_Word_for_ExtractedText="Help:";
		
		FilterURLOnMatchedString="";
		FilterURL_Omit_OnMatchedString="";
		Stop_Word_for_ExtractedText="";
		String html_input_file="/Users/lenin/Downloads/feeds.htm";
		String writerAlreadyCrawledFile="";
		
		// get links
		TreeMap<Integer, TreeMap<Integer,String>> mapLinks=
																http_getLinks(
																				 1, //curr_iteration_level
																				 "", //url (single)
																				 html_input_file, // html_input_file
																				 writerAlreadyCrawledFile,
																				 folder+"h.outfoundURLfromWikiPage.txt",
																				 folder+"h2.outfoundURLfromWikiPage.txt",
																				 false,
																				 true,
																				 "href=\"", //ToFind
																				 "", // ToReplaceWith
																				 "http://www.opensocietyfoundations.org", //prefix counter
																				 FilterURLOnMatchedString, //FilterURLOnMatchedString
																				 FilterURL_Omit_OnMatchedString, //FilterURL_Omit_OnMatchedString
																				 Stop_Word_for_ExtractedText, //Stop_Word_for_ExtractedText
																				 "href", //http or 
																				 ">", //> or " or 
																				 folder+"debug.txt",
																				 true, // skip_is_english
																				 "", //type
																				 false);
		System.out.println("size:"+mapLinks.size());
		
		// wrapper
		 String input_list_of_URL_in_a_file=
			"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/file.us.50.thinktank.dummy.txt";
		 input_list_of_URL_in_a_file="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/";
		  
		 input_list_of_URL_in_a_file="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/file.us.50.thinktank.dummy.txt";
		 String start_tag="href";
		 String end_tag=">";
		 FilterURLOnMatchedString="rss!!!publication!!!expert!!!.xml";
		 FilterURLOnMatchedString="";
		 FilterURL_Omit_OnMatchedString="";
		 Stop_Word_for_ExtractedText="";
		 
//			http_getWikiPageLinks.
//			http_wrapper_for_http_getLinks(
//									 input_list_of_URL_in_a_file,
//									 folder+"1."+"outfoundURLfromWikiPage.txt",
//									 folder+"2."+"outfoundURLfromWikiPage.txt",
//									 true, //isOutFileAppend
//									 true,
//									 "/wiki/", //ToFind
//									 "http://en.wikipedia.org/wiki/", // ToReplaceWith
//									 "2014.DEC", //(dummy) prefix counter <- dummy in this place
//									 FilterURLOnMatchedString, //FilterURLOnMatchedString
//									 start_tag, //http or   <start tag>
//									 end_tag, //> or " or    <end tag>
//									 folder+"debug.txt",
//									 false
//								 	);
		
//			http_getWikiPageLinks.
//			http_recursive_call_for_wrapper_for_http_getLinks(
//								 1,  //max_depth
//								 1,  //curr_depth
//								 input_list_of_URL_in_a_file,
//								 folder+"1."+"dummy.outfoundURLfromWikiPage.txt",
//								 folder+"2."+"dummy.outfoundURLfromWikiPage.txt",
//								 true, //isOutFileAppend
//								 true,
//								 "/wiki/", //ToFind
//								 "http://en.wikipedia.org/wiki/", // ToReplaceWith
//								 "2014.DEC", //(dummy) prefix counter <- dummy in this place
//								 FilterURLOnMatchedString, //FilterURLOnMatchedString
//								 FilterURL_Omit_OnMatchedString, //FilterURL_Omit_OnMatchedString
//								 Stop_Word_for_ExtractedText,
//								 start_tag, //http or   <start tag>
//								 end_tag, //> or " or    <end tag>
//								 folder+"debug.txt",
//								 false
//							 	);
			
			System.out.println("Time Taken:" + NANOSECONDS.toSeconds(System.nanoTime() - t0) +" seconds; "
	       	   		+ (NANOSECONDS.toSeconds(System.nanoTime() - t0))/60 +" minutes");
		
	}
}