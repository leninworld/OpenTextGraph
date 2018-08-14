package p25;


import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

//import org.apache.xmlbeans.impl.xb.xsdschema.Public;
//import org.json.*;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
  

public class Get_rssFeed_fromNewsAPI {
	
	
	public static TreeMap<String, String> p25_load_domainname_pattern4crawledHTML(String inFile){
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
	
	public static String P25_remove_htmlTagsETC(String inputString){
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
	
	
public static String P25_find_domain_name_from_Given_URL(
								String in_URL_String, 
								boolean isDebug){

String out="";
try {
			if(isDebug)
			System.out.println("1.in_URL_String:"+in_URL_String);
			
			in_URL_String=in_URL_String.replace("https:", "")
						.replace("http:", "").replace("//", "")
						.replace("www.", "")
						.replace("www", "");
			
			if(isDebug)
			System.out.println("find:"+out);
			if(isDebug)
			System.out.println("in_URL_String:"+in_URL_String);
			
			if(in_URL_String.indexOf("/",2)>=0){
			out=in_URL_String.substring(0,
										in_URL_String.indexOf("/",2) );
			}
			else if(in_URL_String.indexOf(".",2)>=0){
			out=in_URL_String.substring(0,
					in_URL_String.indexOf(".",2) );
			}



} catch (Exception e) {
// TODO: handle exception
}

if(isDebug)
System.out.println("find2:"+out);

return out;
}
	
	public static String P25_clean_get_embeddedURL(String inputStringURL){
		try {
 				 // 
				 if(inputStringURL.indexOf("url=")>=0  ){
					//outString=outString+" ";
					inputStringURL=inputStringURL.substring(inputStringURL.indexOf("url=")+"url=".length() ,  inputStringURL.length());
				}
				else if(inputStringURL.indexOf("http:")>=0  ){
					inputStringURL=inputStringURL.substring(inputStringURL.indexOf("http:"), inputStringURL.length());
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
	
	// output -> map key,value -> <counter,eachline>
	// endline-> <0 possible
	public static TreeMap<Integer,String> P25_readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
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
	
	// crawl_the_given_list_ofURL 
	public static void crawl_the_given_list_ofURL(
												String inputFile_with_listOFurl,
												String outputFolder,
												String ouputFile_crawled
												){
		try{
			
			String out__alreadyCRAWLED_from_pastRUN= outputFolder +"alreadyCRAWLED_pastRUN.txt";
			
			FileWriter write_CRAWLED=new FileWriter(ouputFile_crawled, true);
			FileWriter write_alreadyCRAWLED_from_pastRUN=new FileWriter(out__alreadyCRAWLED_from_pastRUN, true);
			
			TreeMap<String, String>  map_uniqueURL_asKEY=new TreeMap<String, String>();
			
	    	  // Loading seq and Line 
			  TreeMap<Integer, String>  map_seq_URL_TEMP=
//					  readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
										P25_readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile_with_listOFurl, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  //
			  for(int seq:map_seq_URL_TEMP.keySet()){
				  map_uniqueURL_asKEY.put(map_seq_URL_TEMP.get(seq).toLowerCase() , "");
			  }
			  System.out.println("GIVEN unique set of URLS:"+map_uniqueURL_asKEY.size());
			  
	    	  // Loading alreadyCRAWLED_from_pastRUN
			  TreeMap<Integer, String>  map_seq_inFile_alreadyCRAWLED_from_pastRUN=
//					  readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
					  P25_readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 out__alreadyCRAWLED_from_pastRUN, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  String curr_crawledTEXT="";
			  int c=0;
			  // crawl each url 
			  for(String currURL:map_uniqueURL_asKEY.keySet()){
				  c++;
				  
				  if(currURL.indexOf("youtu")>=0 || currURL.indexOf("wired\\.de")>=0  ) { c++; continue; }
				  System.out.println("Processing "+c+" out of total="+map_uniqueURL_asKEY.size());
				  try{
					  //if not already crawled
					  if(!map_seq_inFile_alreadyCRAWLED_from_pastRUN.containsValue(currURL)){
						  curr_crawledTEXT=send_Get_HTTP(currURL);
						  write_CRAWLED.append(currURL+"!!!"+curr_crawledTEXT+"\n");
						  write_CRAWLED.flush();
						  //already 
						  write_alreadyCRAWLED_from_pastRUN.append(currURL+"\n");
						  write_alreadyCRAWLED_from_pastRUN.flush();
					  }
					  else{
						  System.out.println("Already crawled URL->"+currURL);
					  }
				  }
				  catch(Exception e){
					  
				  }
			  }
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	// HTTP GET request
		private static String send_Get_HTTP(String url) throws Exception {

//			String url = "http://www.google.com/search?q=mkyong";
			String USER_AGENT = "Mozilla/5.0";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
//			System.out.println("\nSending 'GET' request to URL : " + url);
//			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			return response.toString();
		}
	
	
	// crawl collect RSS feed
	public static void get_rssFeed_fromNewsAPI(	
													String 		baseFolder,
													String  	inFile,
													String 		sourceList_inFile,
													String 		output_File
													){
		try{
			

	    	  // Loading seq and Line 
			  TreeMap<Integer, String>  map_seq_N_SOURCE=
//					  readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
					  P25_readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										  sourceList_inFile, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  String currSource="";
			  String mainHTTPurl="https://newsapi.org/v1/articles?source="+currSource+"&sortBy=top&apiKey=97a66e0e479749589ae0fe6b902463a2";
			  
			  FileWriter writer=new FileWriter(output_File);
			  
			  TreeMap<Integer,String> map_seq_RSSFeed=new TreeMap<Integer, String>();
			  
			  //
			  for(int seq:map_seq_N_SOURCE.keySet()){
				  currSource=map_seq_N_SOURCE.get(seq);
				  
				  try{
				  
				  //
				  mainHTTPurl="https://newsapi.org/v1/articles?source="+currSource+"&sortBy=latest&apiKey=97a66e0e479749589ae0fe6b902463a2";
				  // strategy 1 
//				  map_seq_RSSFeed=crawler.crawler_getSingleURLCrawler.crawler_getSingleURLCrawler(mainHTTPurl, false, false);
//				  System.out.println("map_seq_RSSFeed:"+map_seq_RSSFeed);
//				  //
//				  for(int seq2:map_seq_RSSFeed.keySet()){
//					  writer.append(map_seq_RSSFeed.get(seq2)+"\n");
//					  writer.flush();
//				  }
				  
				  // strategy 2
				  String currExtractedPage=send_Get_HTTP(mainHTTPurl);
				  writer.append(currExtractedPage+"\n");
				  writer.flush();
				  
				  }
				  catch(Exception e){
					  try{
						  //
						  mainHTTPurl="https://newsapi.org/v1/articles?source="+currSource+"&sortBy=top&apiKey=97a66e0e479749589ae0fe6b902463a2";
						  
						  // strategy 2
						  String currExtractedPage=send_Get_HTTP(mainHTTPurl);
						  writer.append(currExtractedPage+"\n");
						  writer.flush();
					  }
					  catch(Exception e2){
						  try{
							  //
							  mainHTTPurl="https://newsapi.org/v1/articles?source="+currSource+"&apiKey=97a66e0e479749589ae0fe6b902463a2";
							  
							  // strategy 2
							  String currExtractedPage=send_Get_HTTP(mainHTTPurl);
							  writer.append(currExtractedPage+"\n");
							  writer.flush();
						  }
						  catch(Exception e3){
							  
							  try{
								  //
								  mainHTTPurl="https://newsapi.org/v1/articles?source="+currSource+"&sortBy=popular&apiKey=97a66e0e479749589ae0fe6b902463a2";
								  
								  // strategy 2
								  String currExtractedPage=send_Get_HTTP(mainHTTPurl);
								  writer.append(currExtractedPage+"\n");
								  writer.flush();
							  }
							  catch(Exception e4){
								  
							  }
							  
						  }
						  
						  
					  }
					  
				  }
				  
//				  if(seq>2)
//					  break;
				  
			  }
			  
			
			  
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("output_File-->"+output_File);
	}
	
	//get_unique_URL_from_RSSfeed
	public static void get_unique_URL_from_RSSfeed(	
											String 			baseFolder,
											FileWriter 		writer,
											FileWriter		writer2_output_url_date_source_author,
											String          inputFile_CLEANED // from "cleaning_and_convert.do_clean_crawling_docs_using_domainNAMES_patternFORNEWSBODYSTART"
											){
		try{
			File h = new File(baseFolder);
			String [] files = h.list();

			int cnt2=0;
			TreeMap<String, String> map_uniqueFile_askey=new TreeMap<String, String>();
			// 
			TreeMap<Integer, String>  map_CRAWLED_n_CLEANED_eachLine =
//					  readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
								P25_readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 inputFile_CLEANED, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			TreeMap<String, String> map_=new TreeMap<String, String>();
			//
			for(int seq:map_CRAWLED_n_CLEANED_eachLine.keySet()){
				String currLine=map_CRAWLED_n_CLEANED_eachLine.get(seq);
				//
				String [] _arr2=currLine.split("!!!");
//				System.out.println("_arr2[0]:"+_arr2[0]);
				map_.put(_arr2[0], currLine);
			}
			//
			while(cnt2 < files.length){
				//
				if(files[cnt2].indexOf("Store")>=0  || new File(baseFolder+files[cnt2]).isDirectory()==true  ) {cnt2++;continue;}
				System.out.println("curr file:"+baseFolder+files[cnt2]);
 
				// loading
				TreeMap<Integer, String>  map_currFile_eachLine=
//						  readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//											.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
								P25_readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																											 baseFolder+files[cnt2], 
																										 	 -1, //startline, 
																											 -1, //endline,
																											 " loading", //debug_label
																											 false //isPrintSOP
																											 );
				
				map_uniqueFile_askey.put(baseFolder+files[cnt2], "");
				// get unique URL
				for(int seq:map_currFile_eachLine.keySet()){
					
					String currLine=map_currFile_eachLine.get(seq);
					//
//					JsonParserFactory factory=JsonParserFactory.getInstance();
//					JSONParser parser=factory.newJsonParser();
//					System.out.println("currLine:"+currLine);
//					Map jsonMap=(Map) parser.parseJson(currLine);
					
//					System.out.println("Map:"+jsonMap);
//					System.out.println("articles:"+jsonMap);
					
//					String concat_URL=jsonMap.toString();
					String concat_URL=currLine;
					//
					while(concat_URL.indexOf("\"url\":\"")>=0){
						int begin=concat_URL.indexOf("\"url\":\"");
						int end=concat_URL.indexOf("\",",begin+8);
						//
						if(begin>=0 && end>=0){
							String currURL=concat_URL.substring(begin+7, end);
							// output
							writer.append(currURL+"\n");
							writer.flush();
							//
							concat_URL=concat_URL.substring(end, concat_URL.length());
						}
						 
					}
					
				}
				
				//Step 2: GET url!!!source!!!author!!!
				for(int seq:map_currFile_eachLine.keySet()){
					String currLine=map_currFile_eachLine.get(seq);
					int begin=currLine.indexOf("\"source\":");
					int end = currLine.indexOf(",", begin+1);
//					System.out.println("curr:"+currLine+" begin:"+begin +" end:"+end);
					
					if(begin<0 || end<0) continue;
					
					//source
					String source=currLine.substring(begin, end );
					
					String main = currLine.substring(currLine.indexOf("\"articles\":"), currLine.length());
					
//					main=main.replace("},{", "####");
					
					String [] _arr=main.split("\\},\\{");
					System.out.println("_arr.len:"+_arr.length+" files[cnt2]:"+files[cnt2]);
					int c2=0;
					//
					while(c2<_arr.length){
						String atom_news=_arr[c2];
//						System.out.println("atom_news:"+atom_news);
						//author
						begin=atom_news.indexOf("\"author\":");
						end = atom_news.indexOf(",", begin+1);
//						System.out.println("1->"+begin+" "+end);
						String author=atom_news.substring(begin, end );
						//date
						begin=atom_news.indexOf("\"publishedAt\":");
						end = atom_news.indexOf(",", begin+1);
//						System.out.println("2->"+begin);
						String date=atom_news.substring(begin ,atom_news.length() ); //last token in the line and no ,
						date=date.replace("}]}", "");
						//url
						begin=atom_news.indexOf("\"url\":");
						end = atom_news.indexOf(",", begin+1);
//						System.out.println("4->"+begin+" "+end);
						String url=atom_news.substring(begin, end );
						url=url.replace("\"url\":\"", "").replace("\"", "");
						//curr domain
						String currDomain=
//									find_domain_name_from_Given_URL.find_domain_name_from_Given_URL
									P25_find_domain_name_from_Given_URL
									(url, false);
						
//						String temp=url+"!!!"+date +"!!!"+author+"!!!"+source+"!!!"+currDomain;
						String temp=date +"!!!"+author+"!!!"+source+"!!!"+currDomain;
						
						temp=temp.replace("\"url\":", "").replace("\"author\":", "").replace("\"source\":", "")
								 .replace("\"publishedAt\":", "").replace("\"", ""); //.replace("}]}", "");
						//
						if(author.indexOf("null")>=0 || author.indexOf("http")>=0){
							c2++;
							continue;
						}
						
//						writer2_output_url_date_source_author.append(String.valueOf(map_.size()));
						System.out.println(map_.containsKey(url)+" "+url);
						if(map_.containsKey(url)){
							temp=temp+"!!!"+ map_.get(url); //+"!!!"+map_.size();
							writer2_output_url_date_source_author.append(temp+"\n");
						}
						//
						writer2_output_url_date_source_author.flush();
						c2++;
					}
				}
				cnt2++;
			}
			
			System.out.println("map_uniqueFile_askey:"+map_uniqueFile_askey);
//			System.out.println("map_:"+map_.keySet());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// remove the tag
	public static String P25_SubRemoveTagsFromHTML(String bodyText) {
		try {
			while (bodyText.indexOf("<") >= 0) {
				// <p class=""> </p>
				int startIndex = bodyText.indexOf("<");
				int endIndex = bodyText.indexOf(">", startIndex) + 1;
				if (endIndex > startIndex)
					bodyText = bodyText.replace(
							bodyText.substring(startIndex, endIndex), " ");
				else
					return bodyText;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bodyText;
	}
	
	//  do_clean_crawling_docs_using_domainNAMES_patternFORNEWSBODYSTART
	public static void P25_do_clean_crawling_docs_using_domainNAMES_patternFORNEWSBODYSTART(
															  		String input_crawled_file, // IN 
															  		String file4_DomainName_NewsStartPatterns, //IN
															  		String output_crawled_file //OUT
																	) {
		// TODO Auto-generated method stub
		try { 
			
				String line = "";
				BufferedReader reader = null;
				reader = new BufferedReader(new FileReader(input_crawled_file));
				FileWriter writer = new FileWriter(output_crawled_file);
				HashMap<String, String> map_uniqueDomainFromINPUTCrawledFile=new HashMap<String, String>();
				
				FileWriter writer_domainName = new FileWriter(output_crawled_file+"_only_DOMAINname_FROM_INPUTcrawledFILE_2_DEBUG.txt");
				
				FileWriter writer_NOglobalPATTERNavailable = new FileWriter(output_crawled_file+"_NOglobalPATTERNavailable .txt");
				// LOADING
				TreeMap<String, String> map_domainName_NewsStartPattern=
										p25_load_domainname_pattern4crawledHTML(file4_DomainName_NewsStartPatterns);
				int begin=-1,no_hit_cnt=-1, hit_cnt=-1; String tmp="";
				int lineNo=0;
				//
				while ((line = reader.readLine()) != null) {
					lineNo++;
					
					String [] arr_line=line.split("!!!");
					if(arr_line.length<2){ continue;}
					
					System.out.println("lineNo:"+lineNo+" arr_line[0]:"+arr_line[0]);
					
					String curr_url = arr_line[0];
					try{
//					 if(curr_url.indexOf("http") >=0 ) 
//						 curr_url=curr_url.substring(curr_url.indexOf("orig_sourceUrlString:") +"orig_sourceUrlString:".length(),
//								 						curr_url.length());
					}
					catch(Exception e){
						continue;
					}
					//CALLING domain name
					String currDomainName=
//									find_domain_name_from_Given_URL.find_domain_name_from_Given_URL
									P25_find_domain_name_from_Given_URL
									(curr_url, false);
					 
					String curr_pattern_4_crawledHTML=map_domainName_NewsStartPattern.get(currDomainName);
					// only domain name
					if(map_uniqueDomainFromINPUTCrawledFile.containsKey(currDomainName)){
						writer_domainName.append(currDomainName+"\n");
						writer_domainName.flush();
					}
					map_uniqueDomainFromINPUTCrawledFile.put(currDomainName, "");
					
					String currBodyText_Crawled=arr_line[1];
					//
					if(curr_pattern_4_crawledHTML==null){
						 writer_NOglobalPATTERNavailable.append(line+"\n" );
						 writer_NOglobalPATTERNavailable.flush();
						 continue;
					}
					
					///////
					//example: if given curr_pattern_4_crawledHTML-->class="article-body";<p> , first search for class, then followed by <p>
					if(curr_pattern_4_crawledHTML.indexOf(";")==-1){
						begin=currBodyText_Crawled.indexOf(curr_pattern_4_crawledHTML);
					}
					else{
						String [] pattern=curr_pattern_4_crawledHTML.split(";");
						begin=currBodyText_Crawled.indexOf(pattern[0]);
						begin=currBodyText_Crawled.indexOf(pattern[1],begin+2);
					}
					
					
//					writerDebug.append("\n NOW** FOUND - currDomainName="+currDomainName 
//												+" begin="+begin+" "+currBodyText_Crawled.contains( curr_pattern_4_crawledHTML) );
					
//					if(currDomainName.equalsIgnoreCase("armenianow.com")){
//						writerDebug.append("\n 	currBodyText_Crawled:"+currBodyText_Crawled);
//					}
					
					if(begin>=0){hit_cnt++;}
					else{ no_hit_cnt++;}
					
					
					
					//
//					if(currBodyText_Crawled.lastIndexOf(currBodyText_rssFeeed_compressed_HALF)>=0){
//						count_half_match++;
//						begin=currBodyText_Crawled.lastIndexOf(currBodyText_rssFeeed_compressed_HALF);
//					}
					
					//
	 
						try{
							tmp=currBodyText_Crawled.substring(begin, begin+9000);
						}
						catch(Exception e){
							 try{
								 tmp=currBodyText_Crawled.substring(begin, begin+7500);
								 System.out.println("ERROR ON >2500");

							 }
							 catch(Exception e2){
							
								try{
									tmp=currBodyText_Crawled.substring(begin,begin+4500);
								}
								catch(Exception e22){
										try{
											tmp=currBodyText_Crawled.substring(begin,begin+3500);
										}
										catch(Exception e3){
											try {
												tmp=currBodyText_Crawled.substring(begin,begin+2500);	
											} catch (Exception e4) {
												// TODO: handle exception
												try {
													tmp=currBodyText_Crawled.substring(begin,begin+1500);	
												} catch (Exception e5) {
													try {
														// TODO: handle exception
														tmp=currBodyText_Crawled.substring(begin,begin+500);											
													} catch (Exception e6) {
														// TODO: handle exception
														tmp="(*(ERROR()*";
													}
			
												}
											}
											
										}
									}	 
							 }
						} //END CATCH

//					System.out.println("curr_pattern:"+curr_pattern_4_crawledHTML
//										+" tmp:"+tmp
//										);

					/// remove HTML TAGS
					tmp=P25_SubRemoveTagsFromHTML(tmp);
					/// remove junk words
					tmp=P25_remove_htmlTagsETC(tmp);
					tmp=tmp.replaceAll("\\s+", " ");
					
					tmp=tmp+" beginINDEXis"+begin;
					//
					if(tmp.indexOf("beginINDEXis-1")==-1){					
						// WRITING
						writer.append(curr_url+"!!!"+tmp+"\n");
						writer.flush();
					}
			    
				} // END while ((line = reader.readLine()) != null) {
//			TreeMap<Integer,String> map_inputFile_all_unique_matched_lines_on_allQUERIES_merge_add2MOREfeature=
//					  readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//					 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
//							 							 all_unique_matched_lines_on_allQUERIES_merge_add2MOREfeature, 
//													 	 -1, //startline, 
//														 -1, //endline,
//														 " reloading..", //debug_label
//														 false //isPrintSOP
//														 );
			 
//			for(int seq:map_inputFile_all_unique_matched_lines_on_allQUERIES_merge_add2MOREfeature.keySet()){
//				String currLine=map_inputFile_all_unique_matched_lines_on_allQUERIES_merge_add2MOREfeature.get(seq);
//				
//				
//			}
			

			//bodyText!!!url!!!dateTime!!!author!!!keywords!!!Subject!!!from File!!!bodyText no stopwords!!!docID!!!missing_keywords
			System.out.println("map_domainName_NewsStartPattern:"+map_domainName_NewsStartPattern);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	public static String P25_find_distinct_domain_name_from_Given_FILE(
			String  baseFolder,
			String  inFile,
			int     index_having_url_inFile, //can be -1
			String  outFile,
			int		Flag_Approach,
			boolean isDebug
			){

TreeMap<String,String> map_unique_domainName_as_KEY=new TreeMap<String, String>();
//TreeMap<Integer,String> map_seq_domainName_as_KEY=new TreeMap<Integer, String>();
String out="";
try {
FileWriter writer=new FileWriter(new File(outFile));


if(Flag_Approach==1){

// APPROACH 1 - BIGGER FILES DONT WORK ...
TreeMap<Integer,String> map_file1=
//readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
P25_readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																inFile,
								 							  -1, //startline, 
								 							  -1, //endline,
								 							  " ", //debug_label
								 							  false //isPrintSOP
														 	  );

String url="";
for(int seq:map_file1.keySet()){
String eachLine=map_file1.get(seq);
/// 
if(index_having_url_inFile>0){
String [] arr_eachLine=eachLine.split("!!!");
///
if( arr_eachLine.length>=index_having_url_inFile ){
url=arr_eachLine[index_having_url_inFile-1];
}

}
else{
url= eachLine;
}
// CLEANING URL
if(url.indexOf("url=")>=0){
url=P25_clean_get_embeddedURL(url);
}

//DOMAIN NAME
String currDomain=
//				find_domain_name_from_Given_URL.find_domain_name_from_Given_URL
				P25_find_domain_name_from_Given_URL
				(url, false);
//blank domains should not be added
if(currDomain.length()>1){
//
map_unique_domainName_as_KEY.put(currDomain, "");
}
}

} // Flag_Approach==1

// APPROACH 2
String line2="";
BufferedReader reader = new BufferedReader(new FileReader(inFile));
if(Flag_Approach==2){
String eachLine="";String url="";
// read each line of given file
while ((eachLine = reader.readLine()) != null) {

/// 
if(index_having_url_inFile>0){
String [] arr_eachLine=eachLine.split("!!!");
///
if( arr_eachLine.length>=index_having_url_inFile ){
url=arr_eachLine[index_having_url_inFile-1];
}

}
else{
url= eachLine;
}
// CLEANING URL
if(url.indexOf("url=")>=0){
url=P25_clean_get_embeddedURL(url);
}

//DOMAIN NAME
String currDomain=
//					find_domain_name_from_Given_URL.find_domain_name_from_Given_URL
					P25_find_domain_name_from_Given_URL
					(url, false);
//
map_unique_domainName_as_KEY.put(currDomain, "");

}
} //if(Flag_Approach==2){


//WRITE
for(String url2:map_unique_domainName_as_KEY.keySet()){
writer.append(url2+"\n");
writer.flush();
}

System.out.println("distinct domain:"+map_unique_domainName_as_KEY.size()+" "+map_unique_domainName_as_KEY);


} catch (Exception e) {
// TODO: handle exception
}

System.out.println(out);
return out;
}

	
	
	//
	public static void main(String[] args) throws IOException {
		
		DateFormat dateFormat = new SimpleDateFormat("ddMMMyy");
		Date today = new Date();
		String todayDate=dateFormat.format(today);
		System.out.println("todayDate:"+todayDate);
		//
		String baseFolder="/Users/lenin/Dropbox/Ramesh/Data.p25/";
		String inFile=baseFolder+"";
		String outputFile=baseFolder+"crawled_newsapi_"+todayDate+".txt";
		
		String sourceList_inFile=baseFolder+"source.txt";
		
		// 1) In this method, run Flag==1
		// 3) run cleaning_and_convert.java (OR) instead run Flag==5 here		
		// 2) In this method, run Flag==2
		
		// change file name in "outputFile"
		// 1...3...5....2...... we have to run Flag=1, 3, 5 and 2
		int Flag=5;
		
		int count=1;
		
		///  WHILE LOOP
		while(count<=4){
	
			if(count==1)
				Flag=1;
			else if(count==2){
				Flag=3;	
				//count++; continue; // skip crawling part
			}
			else if(count==3)
				Flag=5;
			else if(count==4)
				Flag=2;
		
		//
		if(Flag==1){//
				//get_rssFeed_fromNewsAPI
				get_rssFeed_fromNewsAPI(
										baseFolder,
										inFile,
										sourceList_inFile,
										outputFile
										);
				System.out.println("outputFile:"+outputFile);
		}
		String outputFolder=baseFolder+"outputFolderForUNIQUE_url/";
		// 1) get UNIQUE URL
		// 2) get url!!!date!!!authors!!!source
		if(Flag==2){
			
			String output_unique_URL=outputFolder+"output_unique_URL.txt";
			String output_url_date_source_author=outputFolder+"output_url_date_source_author.txt";
			
			if(new File(output_unique_URL).exists())
				new File(output_unique_URL).delete();
			
			FileWriter writer=new FileWriter(output_unique_URL,true);
			FileWriter writer2_output_url_date_source_author=new FileWriter(output_url_date_source_author);
			String inputFile_CLEANED=baseFolder+"outputFolderForUNIQUE_url/crawled_GLOBAL.txt_CLEANED.txt";
			
			writer2_output_url_date_source_author.append("date!!!author!!!source!!!domainName!!!url!!!bodyTEXT\n");
			
			// get_unique_URL_from_RSSfeed
			get_unique_URL_from_RSSfeed(baseFolder, 	// input 1
										writer,// output 1
										writer2_output_url_date_source_author, // output 2
										inputFile_CLEANED // input 2 <-------
										);
			
		}
		String ouputFile_crawled=outputFolder+"crawled_GLOBAL.txt";
		// get UNIQUE URL
		if(Flag==3){
			String inputFile_with_listOFurl=outputFolder+"output_unique_URL.txt"; //this is output from flag==2
			//
			crawl_the_given_list_ofURL(
										inputFile_with_listOFurl,
										outputFolder,
										ouputFile_crawled
										);
		}
		
		//get distinct domain names 
		if(Flag==4){
			
//			 find_distinct_domain_name_from_Given_FILE.find_distinct_domain_name_from_Given_FILE(
			P25_find_distinct_domain_name_from_Given_FILE(
					 							baseFolder,
					 							ouputFile_crawled, //sourceList_inFile, 
					 							1, // index_having_url_inFile,
					 							ouputFile_crawled+"_uniqueDOMAIN.txt", 
					 							2, //Flag_Approach, 
					 							false);
			
		}
		
 		 String crawled_matched_file=baseFolder+"outputFolderForUNIQUE_url/crawled_GLOBAL.txt";
	     String file4_DomainName_NewsStartPatterns="/Users/lenin/Dropbox/#problems/p18/ds1/global_domainNAMES_patternFORNEWSBODYSTART.txt";
	     String OUTPUT_crawled_file_CLEANED=crawled_matched_file+"_CLEANED.txt";
	      
		//NOTE: previously this method is called from cleaning_and_convert.java and now directly moved here
		if(Flag==5){
			
	    	   // CLEANING CRAWLING DOCUMENTS
			  P25_do_clean_crawling_docs_using_domainNAMES_patternFORNEWSBODYSTART(
	    			  													crawled_matched_file, // IN 
	    			  													file4_DomainName_NewsStartPatterns, //IN
	    			  													OUTPUT_crawled_file_CLEANED //OUT
	    			  	 												);
			
		}
		
		
		 count++;
	  }//while(count<=4){
		
	} // main
}