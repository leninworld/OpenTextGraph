package crawler;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.io.FileWriter;
import java.net.URL;
import java.util.List;
import java.util.TreeMap;
import crawler.Http_getWikiPageLinks;
//
public class Get_NewsPaper_ArchiveURL {
	private final static int MINMONTH=1;
	private final static int MAXMONTH=12;
	private final static int MINYEAR=1999;
	private static int 		 MAXYEAR=2015;
	private final static int MINDATE=1;
	private final static int MAXDATE=31;
	int i=1;

	//cleaning output file of thehindu.
	//cleaning allnewsArticles
	public static String thehindu_clean_split_multiple_news_to_single(String inFile){

		TreeMap<String, String> mapTreeNews=
				ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
				readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
													inFile, -1, -1,
													"",  //outFile,
													false, //is_Append_outFile,
													false, // is_Write_To_OutputFile
													"", //debug_label
													-1, // token_to_be_used_as_primarykey
													true //boolean isSOPprint
													);


		try{

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}

	//thehindu print
	//2000-2005
	public static TreeMap<Integer, String> thehindu_print(
														String outFile,
														String baseURL,
														int min_year,
														int max_year){
		//String baseURL="http://www.thehindu.com/thehindu/"; // change and this will be passed as input param

		int i =2000; int j=1; int k=1;
		MAXYEAR=2005;

		if(max_year>0)
			MAXYEAR=max_year;

		if(min_year>0)
			i=min_year;

		String mm=""; String yy=""; String dd="";
		int counter=0;
		//
		TreeMap<Integer, String> mapAllURL=new TreeMap();
		try{
		FileWriter writer = new FileWriter(outFile);
		System.out.println("i:"+i+";MAXYEAR:"+MAXYEAR);
		while(i<=MAXYEAR){
			yy=Integer.toString(i);
			j=1;
			System.out.println("i:"+i);
			while(j<=MAXMONTH){
				mm= Integer.toString(j);
				k=1;
				System.out.println("j:"+j+";MAXMONTH:"+MAXMONTH);
				while(k<=MAXDATE){
					dd= Integer.toString(k);
					if( mm.length() ==1 ){
						mm="0"+mm;
					}
					if( dd.length() ==1 ){
						dd="0"+dd;
					}

					String t = baseURL+ yy +"/"+mm+"/"+dd ;
					System.out.println("t:"+t);

					counter++;
					writer.append(counter+"!!!"+t+"\n");
					writer.flush();
					mapAllURL.put(counter, t);
					k++;
				}
				j++;
			}
			i++;
		}
		//System.out.println("output ->"+outFile);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return mapAllURL;
	}
	//thehindu
	//ex: http://www.thehindu.com/archive/print/2014/09/04/
	public static TreeMap<Integer, String> thehinduweb(String  outFile,
													   int min_year,
													   int max_year,
													   boolean is_Append_outFile
														){
		String baseURL="http://www.thehindu.com/archive/web/";
		int i =2009; int j=1; int k=1;
		String mm=""; String yy=""; String dd="";
		int counter=0;

		if(max_year>0){
			MAXYEAR=max_year;
		}
		if(min_year>0){
			i=min_year;
		}

		TreeMap<Integer, String> mapAllURL =new TreeMap();
		try{
		FileWriter writer = new FileWriter(outFile, is_Append_outFile);
		System.out.println("i:"+i+";MAXYEAR:"+MAXYEAR);
		while(i<=MAXYEAR){
			yy=Integer.toString(i);
			j=1;
			System.out.println("i:"+i);
			while(j<=MAXMONTH){
				mm= Integer.toString(j);
				k=1;
				System.out.println("j:"+j+";MAXMONTH:"+MAXMONTH);
				while(k<=MAXDATE){
					dd= Integer.toString(k);
					if( mm.length() ==1 ){
						mm="0"+mm;
					}
					if( dd.length() ==1 ){
						dd="0"+dd;
					}

					String t = baseURL+ yy +"/"+mm+"/"+dd ;
					System.out.println("t:"+t);

					counter++;
					writer.append(counter+"!!!"+t+"\n");
					writer.flush();
					mapAllURL.put(counter, t);
					k++;
				}
				j++;
			}
			i++;
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}

	return mapAllURL;
}

	//Read the year-wise page url and pull all the URLs from each page (each page has monthly URL of particular year).
	public static void URLOFthehindu_web( String outFileForURLextractedFromMainMonthly,
										 boolean is_Append_outFile,
										 String outFolder,
										 String outFileForSetOFnewsURLextractedFromEachMonthly,// allnewsArticles_thehindu_web_URL
										 String failed_File,
										 boolean is_Append_Failed_File,
										 String success_File,
										 boolean is_Append_Success_File,
										 boolean skip_is_english,
										 String stop_pattern_for_url,
										 int 	min_year,
										 int 	max_year,
										 String  past_ran_success_file_for_skipping_url,
										 boolean is_ignore_past_ran_success_file_for_skipping_url,
										 String	 kick_start_pattern_url,
										 boolean is_kick_start_pattern_url_true,
										 boolean isDebug){

		Http_getWikiPageLinks gwp = new Http_getWikiPageLinks();
		try{
			FileWriter writer_success=new FileWriter(outFolder+success_File, is_Append_Success_File );
			FileWriter writer_failed=new FileWriter(outFolder+failed_File, is_Append_Failed_File);
			FileWriter writerDebug=new FileWriter(outFolder+"debug.txt", true);

			FileWriter writer = new FileWriter(outFileForSetOFnewsURLextractedFromEachMonthly, is_Append_outFile);
			TreeMap<Integer, String> mapOut = thehinduweb(outFileForURLextractedFromMainMonthly,
														  min_year, max_year,
														  is_Append_outFile);

			TreeMap<String, String> map_Past_Ran_URL=
													 ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
													 readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
																					past_ran_success_file_for_skipping_url
																					,-1 //startline
																					,-1// endline
																					,"" //outFile,
																					,false //is_Append_outFile,
																					,false //is_Write_To_OutputFile
																					,"" //debug_label
																					, -1 // token to be used as prim key
																					,true //boolean isSOPprint
																					);

			boolean is_kick_start_set=false;
			//
			if(is_kick_start_pattern_url_true==false)
				is_kick_start_set=true;

			// for each found url (from monthly corpus of particular year) get all the news links for that month.
			for(int i:mapOut.keySet()){
				String url=mapOut.get(i);
				//
				if(is_kick_start_set==false){
					if(    is_kick_start_pattern_url_true==true
						&& url.indexOf(kick_start_pattern_url)==-1  ){
						System.out.println("skipping(kick start):"+url);
						continue;
					}
					if(    is_kick_start_pattern_url_true==true
							&& url.indexOf(kick_start_pattern_url)>=0  ){
						is_kick_start_set=true;
					}
				}

				TreeMap<Integer, String> monthYear=Find_Start_n_End_Index_Of_GivenString.
												   getMatchedLines(url, ".com", "archive",false);
				//if(monthYear.get(1).equalsIgnoreCase("-1")) continue; //cant find the pattern in the path

				TreeMap<Integer, TreeMap<Integer,String>> mapLinksFromMonthlyCorpus =new TreeMap();

				System.out.println("-----------------------being http_getlinks---->"+url+"-------"+monthYear.get(1).replace(".com", ""));
				writerDebug.append("\n-----------------------being http_getlinks---->"+url+"-------"+monthYear.get(1).replace(".com", ""));
				writerDebug.flush();

				try{
				//dummy stop pattern

				if(stop_pattern_for_url.length()>1){
					if(url.indexOf(stop_pattern_for_url)>=0){
						System.out.println("breaking..stop_pattern_for_url :"+stop_pattern_for_url);
						break;
					}
				}

				if(is_ignore_past_ran_success_file_for_skipping_url==false &&
						map_Past_Ran_URL.containsKey(url)){
					System.out.println("skipping..already ran past:"+url);
					continue;
				}

				//
				mapLinksFromMonthlyCorpus 			= 	gwp.http_getLinks(
																		 1,
																		 url, //url
																		 "",  //html_input_File
																		 "", //output_to_store_list_of_URLs_already_crawled_file
																		 outFolder+"thehindu.1.outfoundURLfromWikiPage_MainMonthly_thehinduweb.txt",
																		 outFolder+"thehindu.2.outfoundURLfromWikiPage_MainMonthly_thehinduweb.txt",
																		 true, //outFile_1
																		 true,//outFile_2
																		 "/wiki/",
																		 "http://en.wikipedia.org/wiki/",
																		 monthYear.get(1).replace(".com", ""), //prefixCounter
																		 ".ece", //filter on matched
																		 "",//FilterURL_Omit_OnMatchedString
																		 "", //Stop_Word_for_ExtractedText
																		 "http",
																		 ">",
																		 outFolder+"debug.txt",
																		 skip_is_english,
																		 "", //type
																		 false //isDebug
																		 );

				System.out.println("\n mapLinksFromMonthlyCorpus.get(1).size:"+mapLinksFromMonthlyCorpus.get(1).size());
				System.out.println("\n mapLinksFromMonthlyCorpus.size():"+mapLinksFromMonthlyCorpus.size());
				System.out.println("-------------------------------------------");

				writerDebug.append("\n mapLinksFromMonthlyCorpus.get(1).size:"+mapLinksFromMonthlyCorpus.get(1).size());

				if(mapLinksFromMonthlyCorpus.containsKey(2))
					writerDebug.append("\n mapLinksFromMonthlyCorpus.get(2).size:"+mapLinksFromMonthlyCorpus.get(2).size());

				writerDebug.append("\n mapLinksFromMonthlyCorpu.size():"+mapLinksFromMonthlyCorpus.size());
				writerDebug.append("\n-------------------------------------------");
				writerDebug.flush();

				writer_success.append("\n"+url);
				writer_success.flush();

				}
				catch(Exception e){
					writer_failed.append("\n"+url);
					writer_failed.append("\n"+url+"!!!"+e.getMessage());
					writer_failed.flush();

				}
				if(!mapLinksFromMonthlyCorpus.containsKey(-9)
					&& mapLinksFromMonthlyCorpus.containsKey(1)){
					//all news articles from current monthly corpus write to out file.
					for(int k:mapLinksFromMonthlyCorpus.get(1).keySet() ){
						String url2=mapLinksFromMonthlyCorpus.get(1).get(k).toLowerCase().replace("//", "/");
						//
						if( url2.indexOf(".ece" ) == -1 ){

							System.out.println("   noise (not require pattern):"+url2);
							writerDebug.append("\n noise (not require pattern):"+url2);
							writerDebug.flush();
							continue;
						}else{
							System.out.println("right one:"+url2);
							writerDebug.append("\nright one:"+url2);
							writerDebug.flush();
							}

						writer.append(url.replace("http://www.thehindu.com/archive/web/", "")+"!!!"
									 +url2+"\n");
						writer.flush();
					}
				}
				//writer
				System.out.println(mapLinksFromMonthlyCorpus.size());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	//Read the year-wise page url and pull all the URLs from each page (each page has monthly URL of particular year).
	public static void URLOFthehindu_print( String outFileForURLextractedFromMainMonthly,
										 boolean is_Append_outFile,
										 String outFolder,
										 String outFileForSetOFnewsURLextractedFromEachMonthly,//file: thehindu.allnewsArticles_thehindu_web_URL.txt
										 String failed_File,
										 boolean is_Append_Failed_File,
										 String success_File,
										 boolean is_Append_Success_File,
										 boolean skip_is_english,
										 String stop_pattern_for_url,
										 int 	min_year,
										 int 	max_year,
										 String  past_ran_success_file_for_skipping_url,
										 boolean is_ignore_past_ran_success_file_for_skipping_url,
										 String	 kick_start_pattern_url,
										 boolean is_kick_start_pattern_url_true,
										 boolean isDebug){

		Http_getWikiPageLinks gwp = new Http_getWikiPageLinks();
		try{

			Check_for_existence_of_a_file_N_create_if_not_exist.check_for_existence_of_a_file_N_create_if_not_exist( outFolder+success_File);
			Check_for_existence_of_a_file_N_create_if_not_exist.check_for_existence_of_a_file_N_create_if_not_exist( outFolder+failed_File);
			Check_for_existence_of_a_file_N_create_if_not_exist.check_for_existence_of_a_file_N_create_if_not_exist( outFolder+"debug.txt");

			FileWriter writer_success=new FileWriter(outFolder+success_File, is_Append_Success_File );
			FileWriter writer_failed=new FileWriter(outFolder+failed_File, is_Append_Failed_File);
			FileWriter writerDebug=new FileWriter(outFolder+"debug.txt", true);

			FileWriter writer = new FileWriter(outFileForSetOFnewsURLextractedFromEachMonthly, is_Append_outFile);
			FileWriter writer_label = new FileWriter(outFileForSetOFnewsURLextractedFromEachMonthly +"_for_labeling.txt"
												, is_Append_outFile);
			TreeMap<Integer, String> mapOut = thehinduweb(outFileForURLextractedFromMainMonthly,
														  min_year, max_year,
														  is_Append_outFile);

			TreeMap<Integer, String> mapOut_Print =new TreeMap<Integer, String>();

			TreeMap<String, String> map_Past_Ran_URL=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
													 readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
													 (
																					past_ran_success_file_for_skipping_url
																					,-1 //startline
																					,-1// endline
																					,"" //outFile,
																					,false //is_Append_outFile,
																					,false //is_Write_To_OutputFile
																					,""// debug_label
																					, -1 //token_to_be_used_as_primarykey
																					,true //boolean isSOPprint
																					);

			boolean is_kick_start_set=false;
			//
			if(is_kick_start_pattern_url_true==false)
				is_kick_start_set=true;

			for(int i:mapOut.keySet()){
				mapOut_Print.put(i, mapOut.get(i).replace("web", "print"));
			}

			// for each found url (from monthly corpus of particular year) get all the news links for that month.
			for(int i:mapOut_Print.keySet()){
				String url=mapOut_Print.get(i);
				//
				if(is_kick_start_set==false){
					if(    is_kick_start_pattern_url_true==true
						&& url.indexOf(kick_start_pattern_url)==-1  ){
						System.out.println("skipping(kick start):"+url);
						continue;
					}
					if(    is_kick_start_pattern_url_true==true
							&& url.indexOf(kick_start_pattern_url)>=0  ){
						is_kick_start_set=true;
					}
				}

				TreeMap<Integer, String> monthYear=Find_Start_n_End_Index_Of_GivenString.
												   getMatchedLines(url, ".com", "archive",false);
				//if(monthYear.get(1).equalsIgnoreCase("-1")) continue; //cant find the pattern in the path

				TreeMap<Integer, TreeMap<Integer,String>> mapLinksFromMonthlyCorpus =new TreeMap();

				System.out.println("-----------------------being http_getlinks---->"+url+"-------"+monthYear.get(1).replace(".com", ""));
				writerDebug.append("\n-----------------------being http_getlinks---->"+url+"-------"+monthYear.get(1).replace(".com", ""));
				writerDebug.flush();

				try{
				//dummy stop pattern

				if(stop_pattern_for_url.length()>1){
					if(url.indexOf(stop_pattern_for_url)>=0){
						System.out.println("breaking..stop_pattern_for_url :"+stop_pattern_for_url);
						break;
					}
				}

				if(is_ignore_past_ran_success_file_for_skipping_url==false &&
						map_Past_Ran_URL.containsKey(url)){
					System.out.println("skipping..already ran past:"+url);
					continue;
				}

				//crawling
				Source source=new Source(new URL(url));
				List<Element> linkElements=source.getAllElements(HTMLElementName.A);
				for (Element linkElement : linkElements) {
					String href=linkElement.getAttributeValue("href");
					if (href==null) continue;
					// A element can contain other tags so need to extract the text from it:
					String label=linkElement.getContent().getTextExtractor().toString();

					if(href.indexOf(".ece")>=0){
						System.out.println("label:"+ label+"!!!href:<"+href+'>');
						writer.append("\n"+"root:"+url+"!!!title:"+ label+"!!!href:<"+href+'>');
						writer.flush();

						writer_label.append("\n"+"title:"+ label+"!!!href:"+href );
						writer_label.flush();

					}
				}

				System.out.println("\n mapLinksFromMonthlyCorpus.get(1).size:"+mapLinksFromMonthlyCorpus.get(1).size());
				System.out.println("\n mapLinksFromMonthlyCorpus.size():"+mapLinksFromMonthlyCorpus.size());
				System.out.println("-------------------------------------------");

				writerDebug.append("\n mapLinksFromMonthlyCorpus.get(1).size:"+mapLinksFromMonthlyCorpus.get(1).size());

				if(mapLinksFromMonthlyCorpus.containsKey(2))
					writerDebug.append("\n mapLinksFromMonthlyCorpus.get(2).size:"+mapLinksFromMonthlyCorpus.get(2).size());

				writerDebug.append("\n mapLinksFromMonthlyCorpu.size():"+mapLinksFromMonthlyCorpus.size());
				writerDebug.append("\n-------------------------------------------");
				writerDebug.flush();

				writer_success.append("\n"+url);
				writer_success.flush();

				}
				catch(Exception e){
					writer_failed.append("\n"+url);
					writer_failed.append("\n"+url+"!!!"+e.getMessage());
					writer_failed.flush();

				}

				//writer
				System.out.println(mapLinksFromMonthlyCorpus.size());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	//Read the year-wise page url and pull all the URLs from each page (each page has monthly URL of particular year).
		public static void URLOFthehinduprint06ToTill(String outFileForURLextractedFromMainMonthly,
													 boolean is_Append_outFile,
													 String outFolder,
													 String outFileForSetOFnewsURLextractedFromEachMonthly,
													 String failed_File,
													 boolean is_Append_Failed_File,
													 String success_File,
													 boolean is_Append_Success_File,
													 boolean skip_is_english,
													 boolean isDebug){

			Http_getWikiPageLinks gwp=new Http_getWikiPageLinks();
			TreeMap<Integer, String > mapSetOfMainLinks = new TreeMap<Integer, String>();
			try{
				FileWriter writer_success=new FileWriter(outFolder+success_File, is_Append_Success_File );
				FileWriter writer_failed=new FileWriter(outFolder+failed_File, is_Append_Failed_File);
				FileWriter writerDebug=new FileWriter(outFolder+"debug.txt", true);

				//http://www.thehindu.com/thehindu/2000/01/01/ <front page>
				//http://www.thehindu.com/thehindu/2000/01/01/02hdline.htm
				//http://www.thehindu.com/thehindu/2000/01/01/03hdline.htm
				//http://www.thehindu.com/thehindu/2000/01/01/04hdline.htm

				FileWriter writer = new FileWriter(outFileForSetOFnewsURLextractedFromEachMonthly, is_Append_outFile);
				//thehinduprint get "range of years".
				TreeMap<Integer, String> mapOut = thehindu_print(outFileForURLextractedFromMainMonthly,
																"http://www.thehindu.com/thehindu/",
																2006,
																2015);
				// for each found url (from monthly corpus of particular year) get all the news links for that month.
				for(int i:mapOut.keySet()){
					String url=mapOut.get(i); //curr base url

					mapSetOfMainLinks.put(1, url);
//					mapSetOfMainLinks.put(2, url+"/02hdline.htm");
//					mapSetOfMainLinks.put(3, url+"/03hdline.htm");
//					mapSetOfMainLinks.put(4, url+"/04hdline.htm");
					for(int h:mapSetOfMainLinks.keySet()){  //iterate front page, national, international etc.
					url=mapSetOfMainLinks.get(h);
					if(isDebug==true)
						System.out.println(".url only!!!!!!!!!!!!!!!!!!!!!!!!!!!!->"+url);
					TreeMap<Integer, String> monthYear=Find_Start_n_End_Index_Of_GivenString.getMatchedLines(url, ".com", "archive",false);

					//if(monthYear.get(1).equalsIgnoreCase("-1")) continue; //cant find the pattern in the path

					if(monthYear!=null && monthYear.containsKey(1))
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!->"+url+"-------"+monthYear.get(1).replace(".com", ""));

					TreeMap<Integer, TreeMap<Integer,String>> mapLinksFromMonthlyCorpus =new TreeMap();
					try{
								mapLinksFromMonthlyCorpus = gwp.http_getLinks(
																						 1,
																						 url,
																						 "",  //html_input_File
																						 "", //output_to_store_list_of_URLs_already_crawled_file
																						 outFolder+"thehindu.1.outfoundURLfromWikiPage_main_thehinduprint06ToTill.txt",
																						 outFolder+"thehindu.2.outfoundURLfromWikiPage_main_thehinduprint06ToTill.txt",
																						 true, //outFile_1
																						 true,//outFile_2
																						 "/wiki/",
																						 "http://en.wikipedia.org/wiki/",
																						 monthYear.get(1).replace(".com", ""),
																						 ".ece",
																						 "",//FilterURL_Omit_OnMatchedString
																						 "", //Stop_Word_for_ExtractedText
																						 "http",
																						 ">",
																						 outFolder+"debug.txt",
																						 skip_is_english,
																						 "",//type
																						 false);

								writerDebug.append("\nmapLinksFromMonthlyCorpus.size:"+mapLinksFromMonthlyCorpus.size());
								writerDebug.append("\nmapLinksFromMonthlyCorpus:"+mapLinksFromMonthlyCorpus);
								writerDebug.flush();

								writer_success.append("\n"+url);
								writer_success.flush();

					}
					catch(Exception e){
						writer_failed.append("\n"+url);
						writer_failed.append("\n"+url+"!!!"+e.getMessage());
						writer_failed.flush();
					}
					if(!mapLinksFromMonthlyCorpus.containsKey(-9)){
						//all news articles from current monthly corpus write to out file.
						for(int k:mapLinksFromMonthlyCorpus.get(1).keySet() ){
							String url2=mapLinksFromMonthlyCorpus.get(1).get(k).toLowerCase().replace("//", "/");
							//
							if(!url2.contains(".ece" )){
								System.out.println("noise (not require pattern):"+url2);
								continue;
							}else{System.out.println("right one:"+url2);}

							writer.append(url.replace("http://www.thehindu.com/archive/print/", "")+"!!!"
										 +url2+"\n");
							writer.flush();
						} //end of for k.
					}
					System.out.println(mapLinksFromMonthlyCorpus.size());
					} //end of list of main url page ( front page, national, international etc.)
					//writer
				} //end of for
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		//Read the year-wise page url and pull all the URLs from each page (each page has monthly URL of particular year).
		// Output of this file "option1" and "option2" can be cleaned by method "".
		public static void URLOFthehinduprint_any_year_range( String  outFileForURLextractedFromMainMonthly,
														 	  String  outFolder,
														 	  String  outFileForSetOFnewsURLextractedFromEachMonthly,
														 	  String  outFileForSetOFnewsURLextractedFromEachMonthly_2,
														 	  String  failed_File,
														 	  boolean is_Append_Failed_File,
														 	  String  success_File,
														 	  boolean is_Append_Success_File,
														 	  boolean isAppendoutFileForSetOFnewsURLextractedFromEachMonthly,
														 	  boolean skip_is_english,
														 	  String  past_ran_success_file_for_skipping_url,
														 	  boolean is_ignore_past_ran_success_file_for_skipping_url,
														 	  String  which_set_of_url, // {option1, option2}
														 	  int 	 start_year_range,
														 	  int 	 end_year_range,
														 	  String  kick_start_pattern,
														 	  String  stop_pattern_for_url,//stop_pattern_for_url
														 	  String  which_crawler, // {"crawler_send_http_request","dummy"}
														 	  boolean isDebug
														 	  ){

			TreeMap<Integer, TreeMap<Integer,String>> mapLinksFromMonthlyCorpus =new TreeMap<Integer, TreeMap<Integer,String>>();
					//getWikiPageLinks gwp = new getWikiPageLinks();
					Http_getWikiPageLinks gwp = new Http_getWikiPageLinks();

					TreeMap<Integer, String > mapSetOfMainLinks = new TreeMap<Integer, String>();
					try{
						FileWriter writer_success=new FileWriter(outFolder+success_File, is_Append_Success_File );
						FileWriter writer_failed=new FileWriter(outFolder+failed_File, is_Append_Failed_File);

						FileWriter writerDebug =new FileWriter(outFolder+"debug.txt", true);

						//http://www.thehindu.com/thehindu/2000/01/01/ <front page>
						//http://www.thehindu.com/thehindu/2000/01/01/02hdline.htm
						//http://www.thehindu.com/thehindu/2000/01/01/03hdline.htm
						//http://www.thehindu.com/thehindu/2000/01/01/04hdline.htm

						FileWriter writer=new FileWriter(outFileForSetOFnewsURLextractedFromEachMonthly,
														 isAppendoutFileForSetOFnewsURLextractedFromEachMonthly);
						FileWriter writer2=new FileWriter(outFileForSetOFnewsURLextractedFromEachMonthly_2,
								 						  isAppendoutFileForSetOFnewsURLextractedFromEachMonthly);


						// past run
						TreeMap<String, String> map_Past_Ran_URL=
																 ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
																 readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
																				past_ran_success_file_for_skipping_url
																				,-1 //startline
																				,-1// endline
																				,"" //outFile,
																				,false //is_Append_outFile,
																				,false //is_Write_To_OutputFile
																				,""//debug_label
																				,-1 //token_to_be_used_as_primarykey
																				,true //boolean isSOPprint
																				);

						//thehinduprint get "range of years".
						TreeMap<Integer, String> mapOut = thehindu_print(outFileForURLextractedFromMainMonthly,
																		"http://www.thehindu.com/thehindu/",
																		start_year_range,
																		end_year_range
																		);
						boolean is_kick_start_pattern = false ;

						// for each found url (from monthly corpus of particular year) get all the news links for that month.
						for(int i:mapOut.keySet()){

							String url=mapOut.get(i); //curr base url


							//
							if(stop_pattern_for_url.length()>1){
								if(url.indexOf(stop_pattern_for_url)>=0){
									System.out.println(".thehindu.breaking..stop_pattern_for_url :"+stop_pattern_for_url);
									break;
								}
							}

							if(kick_start_pattern.length()>0 &&is_kick_start_pattern==false){
								if(url.indexOf(kick_start_pattern)>=0){
									System.out.println("SET ..kick_start_pattern :"+kick_start_pattern);
									is_kick_start_pattern=true;
								}
								else{
									continue;
								}
							}
							//
							if(is_kick_start_pattern==false){
								continue;
							}

							if(which_set_of_url.equalsIgnoreCase("option1")){
								//set 1 (option 1)
								mapSetOfMainLinks.put(1, url); //front page
	 							mapSetOfMainLinks.put(2, url+"/02hdline.htm"); //national
	 							mapSetOfMainLinks.put(3, url+"/03hdline.htm"); //international
	 							mapSetOfMainLinks.put(4, url+"/04hdline.htm"); //southern state
	 							System.out.println("****option:"+which_set_of_url);
							}

							if(which_set_of_url.equalsIgnoreCase("option2")){
	 							//set 2 (option 2)
								// 2004 and 2005 has 21hdline...23hdline...25hdline; 2000-2003-> some have
	 							mapSetOfMainLinks.put(1, url+"/14hdline.htm"); //exists since 2000 (other states)
								mapSetOfMainLinks.put(2, url+"/21hdline.htm");
	 							mapSetOfMainLinks.put(3, url+"/22hdline.htm");
	 							mapSetOfMainLinks.put(4, url+"/23hdline.htm");
	 							mapSetOfMainLinks.put(5, url+"/24hdline.htm");
	 							mapSetOfMainLinks.put(6, url+"/25hdline.htm");
	 							System.out.println("****option:"+which_set_of_url);
							}

 							  //iterate front page, national, international etc.
							for(int h:mapSetOfMainLinks.keySet()){

							url=mapSetOfMainLinks.get(h);


							if(is_ignore_past_ran_success_file_for_skipping_url==false &&
									map_Past_Ran_URL.containsKey(url)){
								System.out.println("skipping..already ran past:"+url);
								continue;
							}

							if(isDebug==true)
								System.out.println(".url only!!!!!!!!!!!!!!!!!!!!!!!!!!!!->"+url);
//							TreeMap<Integer, String> monthYear=
//								    findGivenStartnEndIndexOfString.getMatchedLines(url, ".com", "archive",false);
							//if(monthYear.get(1).equalsIgnoreCase("-1")) continue; //cant find the pattern in the path



							System.out.println("----------------calling http_getLinks for--------->"+url+"-------"  );


							//which crawler to call.

							mapLinksFromMonthlyCorpus=new TreeMap();

							if(which_crawler.equalsIgnoreCase("")){
							try{

							mapLinksFromMonthlyCorpus = 			gwp.http_getLinks(
																					 1,
																					 url,
																					 "",  //html_input_File
																					 "", //output_to_store_list_of_URLs_already_crawled_file
																					 outFolder+"thehindu.1.outfoundURLfromWikiPage_main_thehinduprint2000To2005-opt-"+which_set_of_url+".txt",
																					 outFolder+"thehindu.2.outfoundURLfromWikiPage_main_thehinduprint2000To2005-opt-"+which_set_of_url+".txt",
																					 true, //outFile_1
																					 true,//outFile_2
																					 "stories/", // replace from (for more urls from based "url"
																					 mapSetOfMainLinks.get(1)
																					 	+"/stories/",
																					 "", //prefix counter
																					 "stories/",
																					 "",//FilterURL_Omit_OnMatchedString
																					 "", //Stop_Word_for_ExtractedText
																					 "#<stories",	//"<a ",
																					 "#<stories",	//">",
																					 outFolder+"debug.txt",
																					 skip_is_english,
																					 "URLOFthehinduprint2000To2005", //type={"URLOFthehinduprint2000To2005"}
																					 isDebug
																					 );

							System.out.println("\nmapLinksFromMonthlyCorpus.size:"+mapLinksFromMonthlyCorpus.size());
							System.out.println("\nmapLinksFromMonthlyCorpus:"+mapLinksFromMonthlyCorpus);
							System.out.println("--------------------------------------------------------------------");

							if(isDebug){
								writerDebug.append("\nmapLinksFromMonthlyCorpus.size:"+mapLinksFromMonthlyCorpus.size());
								writerDebug.append("\nmapLinksFromMonthlyCorpus:"+mapLinksFromMonthlyCorpus);
								writerDebug.flush();
							}
								writer_success.append("\n"+url);
								writer_success.flush();

							}
							catch(Exception e){
								writer_failed.append("\n"+url);
								writer_failed.append("\n"+url+"!!!"+e.getMessage());
								writer_failed.flush();
							}
						//failed
						if(!mapLinksFromMonthlyCorpus.containsKey(-9)){

							//all news articles from current monthly corpus write to out file.
							for(int k:mapLinksFromMonthlyCorpus.get(1).keySet() ){
								String url2=mapLinksFromMonthlyCorpus.get(1).get(k).toLowerCase().replace("//", "/");
								//
								if(!url2.toLowerCase().contains("stories" )){
									System.out.println("noise (not require pattern):"+url2);
									continue;
								}else{System.out.println("right one:"+url2);}


								url2=url2.replace("#", " ");
								url2=url2.replace("\n", "").replace("\r", "").replace("\t", "")
											.replaceAll("\\p{Cntrl}", "");
								// not null
								if(url2!=null && url2!=null){
									writer.append(url.replace("http://www.thehindu.com/archive/print/", "")+"!!!"
												 +url2.replace("#", " ") +"\n");
									writer.flush();
								}


							} //end of for k.

							System.out.println("mapLinksFromMonthlyCorpus.size():"+mapLinksFromMonthlyCorpus.size()
													+";"+mapLinksFromMonthlyCorpus);
							//write to outFile_2
							writer2.append("\n"+url+"!!!$$$"+
											mapLinksFromMonthlyCorpus.get(2).get(2).replace("#", " ")+"!!!$$$"+
											mapLinksFromMonthlyCorpus.get(2).get(2));
							writer2.flush();


						}
							System.out.println(mapLinksFromMonthlyCorpus.size());


							} //if end - if(which_crawler.equalsIgnoreCase("")){
							else if(which_crawler.equalsIgnoreCase("crawler_send_http_request")){

								try{
									String htmlWithTag=Crawler_send_http_request.crawler_send_http_get(url,
																										""
																										).get(1);

									writer2.append("\n"+url+"!!!$$$"+
													htmlWithTag.replace("\n", "")
													.replace("\r", "").replace("\t", "")
													.replaceAll("\\p{Cntrl}", ""));
									writer2.flush();
									writer_success.append("\n"+url);
									writer_success.flush();
								}
								catch(Exception e){
									writer_failed.append("\n"+url);
									writer_failed.append("\n"+url+"!!!"+e.getMessage());
									writer_failed.flush();
								}

							}

							} //end FOR of list of main url page ( front page, national, international etc.)
							//writer
						} //end of for
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				//Read the year-wise page url and pull all the URLs from each page (detailed national/souther states/states-TN,karnataka etc)
				//(each page has monthly URL of particular year).
				//http://www.thehindu.com/2002/12/31/14hdline.htm <and>
				// from 21hdline.htm to  /22hdline.htm
				public static void URLOFthehinduprint2000To2005detailedNational(
															String outFileForURLextractedFromMainMonthly,
															String outFolder,
															String outFileForSetOFnewsURLextractedFromEachMonthly,
															boolean is_Append_outFileForSetOFnewsURLextractedFromEachMonthly,
															String failed_File,
															boolean is_Append_Failed_File,
															String success_File,
															boolean is_Append_Success_File,
															boolean skip_is_english,
															String  past_ran_success_file_for_skipping_url,
															boolean is_ignore_past_ran_success_file_for_skipping_url,
															boolean isDebug){

							Http_getWikiPageLinks gwp = new Http_getWikiPageLinks();
							TreeMap<Integer, String > mapSetOfMainLinks = new TreeMap<Integer, String>();
							try{
								FileWriter writer_success=new FileWriter(outFolder+success_File, is_Append_Success_File );
								FileWriter writer_failed=new FileWriter(outFolder+failed_File, is_Append_Failed_File);
								FileWriter writerDebug=new FileWriter(outFolder+"debug.txt", true);
								//http://www.thehindu.com/thehindu/2000/01/01/ <front page>
								//http://www.thehindu.com/thehindu/2000/01/01/02hdline.htm
								//http://www.thehindu.com/thehindu/2000/01/01/03hdline.htm
								//http://www.thehindu.com/thehindu/2000/01/01/04hdline.htm

								FileWriter writer=new FileWriter(outFileForSetOFnewsURLextractedFromEachMonthly,
																is_Append_outFileForSetOFnewsURLextractedFromEachMonthly);

								// past run
								TreeMap<String, String> map_Past_Ran_URL=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
																		 readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
																									past_ran_success_file_for_skipping_url
																									,-1 //startline
																									,-1// endline
																									,"" //outFile,
																									,false //is_Append_outFile,
																									,false //is_Write_To_OutputFile
																									,"" //debug_label
																									, -1 //token_to_be_used_as_primarykey
																									,true //boolean isSOPprint
																									);

								//thehindu_print get "range of years".
								TreeMap<Integer, String> mapOut = thehindu_print(outFileForURLextractedFromMainMonthly,
																				"http://www.thehindu.com/thehindu/",
																				2000,
																				2005);
								// for each found url (from monthly corpus of particular year) get all the news links for that month.
								for(int i:mapOut.keySet()){
									String url=mapOut.get(i); //curr base url

									System.out.println("\n main url:"+url);

									if(isDebug==true){

										writerDebug.append("\n main url:"+url);
										writerDebug.flush();
									}

									mapSetOfMainLinks.put(0, url);
									mapSetOfMainLinks.put(1, url+"/14hdline.htm");
									mapSetOfMainLinks.put(2, url+"/21hdline.htm");
		 							mapSetOfMainLinks.put(3, url+"/22hdline.htm");
		 							mapSetOfMainLinks.put(4, url+"/23hdline.htm");
		 							mapSetOfMainLinks.put(5, url+"/24hdline.htm");
		 							mapSetOfMainLinks.put(6, url+"/25hdline.htm");
		 							// another set
		 							mapSetOfMainLinks.put(7, url+"/02hdline.htm");
		 							mapSetOfMainLinks.put(8, url+"/03hdline.htm");
		 							mapSetOfMainLinks.put(9, url+"/04hdline.htm");

		 							//iterate front page, national, international etc.
									for(int h:mapSetOfMainLinks.keySet()){
										url=mapSetOfMainLinks.get(h);

										// skip past ran found
										if(is_ignore_past_ran_success_file_for_skipping_url==false &&
												map_Past_Ran_URL.containsKey(url)){
											System.out.println("skipping..already ran past:"+url);
											continue;
										}

	//									TreeMap<Integer, String> monthYear=
	//										    findGivenStartnEndIndexOfString.getMatchedLines(url, ".com", "archive",false);
										//if(monthYear.get(1).equalsIgnoreCase("-1")) continue; //cant find the pattern in the path

										TreeMap<Integer, TreeMap<Integer,String>> mapLinksFromMonthlyCorpus =new TreeMap();

										try{
														mapLinksFromMonthlyCorpus = gwp.http_getLinks(
																					 1,
																					 url,
																					 "",  //html_input_File
																					 "", //output_to_store_list_of_URLs_already_crawled_file
																					 outFolder+"1.outfoundURLfromWikiPage_thehinduprint2000To2005detailedNational.txt",
																					 outFolder+"2.outfoundURLfromWikiPage_thehinduprint2000To2005detailedNational.txt",
																					 true, //outFile_1
																					 true,//outFile_2
																					 "stories/", // replace from (for more urls from based "url"
																					 mapSetOfMainLinks.get(0)+"/stories/",
																					 "", //prefix counter
																					 "", //  "stories/",   <filter on matched
																					 "",//FilterURL_Omit_OnMatchedString
																					 "", //Stop_Word_for_ExtractedText
																					 "#<stories", // #<stories OR "href",    //start_tag
																					 "#<stories", // #<stories OR "/a>",     //end_tag
																					 outFolder+"debug.txt",
																					 skip_is_english,
																					 "",//type
																					 false);
													//
													if(mapLinksFromMonthlyCorpus.containsKey(-9)){

														System.out.println("***error:"+mapLinksFromMonthlyCorpus.get(-9));
														writerDebug.append(  "\n***error:after http_getlinks()"+ mapLinksFromMonthlyCorpus.get(-9));
														writerDebug.flush();

														writer_failed.append("\n"+mapLinksFromMonthlyCorpus.get(-9));
														writer_failed.flush();
													}
													else{
														System.out.println("success:"+url);

														writerDebug.append(  "\n ##success:after http_getlinks()"+ url
																				+"!!!mapLinksFromMonthlyCorpus:"+mapLinksFromMonthlyCorpus.size());
														writerDebug.flush();

														writer_success.append("\n"+url);
														writer_success.flush();



													} //else end


													if(!mapLinksFromMonthlyCorpus.containsKey(-9)){
													//all news articles from current monthly corpus write to out file.
													for(int k:mapLinksFromMonthlyCorpus.get(1).keySet() ){
														String url2=mapLinksFromMonthlyCorpus.get(1).get(k).toLowerCase().replace("//", "/");
														//url2 to be written to output file
														if(!url2.toLowerCase().contains("stories" )){
															System.out.println(" noise (not require pattern):"+url2);

															if(isDebug){
															writerDebug.append("\n noise (not require pattern):"+url2);
															writerDebug.flush();
															}

															continue;
														}
														else{
														System.out.println("right one:"+k);}
														if(isDebug){
															writerDebug.append("\n right one:"+k);
															writerDebug.flush();
														}

														String url_write=url.replace("http://www.thehindu.com/archive/print/", "");
														String url2_write=url2.replace("/14hdline", "")+url2.replace("/21hdline", "")
																		+url2.replace("/22hdline", "")+url2.replace("/23hdline", "")
																		+url2.replace("/24hdline", "")+url2.replace("/25hdline", "");


														if(Find_occurance_Count_Substring.find_occurance_Count_Substring("http", url2_write)>1
																){
															url2_write=
																	 Find_clean_get_last_pattern_from_a_string_having_multiple_pattern.
																	 find_clean_get_last_pattern_from_a_string_having_multiple_pattern(url2_write, "http");
															}


														writer.append(url_write+"!!!"+
																	  url2_write
																	  +"\n");
														writer.flush();
													} //end of for k.

													}

													System.out.println(mapLinksFromMonthlyCorpus.size());

										}
										catch(Exception e){
												writer_failed.append("\n"+url);
												writer_failed.append("\n"+url+"!!!"+e.getMessage());
												writer_failed.flush();
												if(isDebug){
													writerDebug.append("\n catch on calling get_links "+url+"!!!"+e.getMessage());
												}
												writerDebug.flush();
												System.out.println("catch---->"+ url+"!!!"+e.getMessage());
										}

									} //end of list of main url page ( front page, national, international etc.)
								} //end of for
							}
							catch(Exception e){
								e.printStackTrace();
							}
		}
		//is leap year
	   public static boolean isLeapYear(int year) {
			if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
				return true;
			} else {
				return false;
			}
		    }
	//getNoDaysInaMonth (out -> string, integer)
	public static TreeMap<String,Integer> getSNoDaysInaMonth(int year){
		TreeMap<String,Integer> outMapMonthDays = new TreeMap<String, Integer>();
		try{
			outMapMonthDays.put("january",31);
			if(isLeapYear(year)==true)
				outMapMonthDays.put("february",29);
			else
				outMapMonthDays.put("february",28);
			outMapMonthDays.put("march",31);
			outMapMonthDays.put("april",30);
			outMapMonthDays.put("may",31);
			outMapMonthDays.put("june",30);
			outMapMonthDays.put("july",31);
			outMapMonthDays.put("august",31);
			outMapMonthDays.put("september",30);
			outMapMonthDays.put("october",31);
			outMapMonthDays.put("november",30);
			outMapMonthDays.put("december",31);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return outMapMonthDays;
	}
	//getNoDaysInaMonth (out -> integer, integer)
	public static TreeMap<Integer,Integer> getINoDaysInaMonth(int year){
		TreeMap<Integer, Integer> outMapMonthDays = new TreeMap<Integer, Integer>();
		try{

			outMapMonthDays.put(1,31);
			if(isLeapYear(year)==true)
				outMapMonthDays.put(2,29);
			else
				outMapMonthDays.put(2,28);
			outMapMonthDays.put(3,31);
			outMapMonthDays.put(4,30);
			outMapMonthDays.put(5,31);
			outMapMonthDays.put(6,30);
			outMapMonthDays.put(7,31);
			outMapMonthDays.put(8,31);
			outMapMonthDays.put(9,30);
			outMapMonthDays.put(10,31);
			outMapMonthDays.put(11,30);
			outMapMonthDays.put(12,31);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return outMapMonthDays;
	}

	//timesofindia
	//ex:http://timesofindia.indiatimes.com/2014/1/1/archivelist/year-2014,month-1,starttime-41640.cms
	//   http://timesofindia.indiatimes.com/archive/year-2014,month-5.cms   <--monthly
	public static TreeMap<Integer, String> timesofindia(String outFile,
														int start_year,
														int end_year,
														boolean is_Append_outFile){
		Crawler c=new Crawler();
		String bodyText="";
		TreeMap<String,Integer> mapStartNumber = new TreeMap();
		TreeMap<String,Integer> mapEndNumber = new TreeMap();

		mapStartNumber.put("20011",36892); //since 2001 only available
		mapStartNumber.put("20021",37257);
		mapStartNumber.put("20031",37622);
		mapStartNumber.put("20041",37987);
		mapStartNumber.put("20051",38353);
		mapStartNumber.put("20061",38718);
		mapStartNumber.put("20071",39083);
		mapStartNumber.put("20081",39448);
		mapStartNumber.put("20091",39814);
		mapStartNumber.put("20101",40179);
		mapStartNumber.put("20111",40544);
		mapStartNumber.put("20121",40909);
		mapStartNumber.put("20131",41275);
		mapStartNumber.put("20141",41640);
		mapStartNumber.put("20151",42005);
//		mapStartNumber.put("20142", 41671); mapStartNumber.put("20143", 41699); mapStartNumber.put("20144", 41730);
//		mapStartNumber.put("20145", 41760);mapStartNumber.put("20146", 41791);mapStartNumber.put("20147", 41821);mapStartNumber.put("20148", 41852);
//		mapStartNumber.put("20149", 41883);mapStartNumber.put("201410", 41913); mapStartNumber.put("201411", 41944);mapStartNumber.put("201412", 41974);

		//mapStartNumber.put("20132", 41306); mapStartNumber.put("20133", 41334);
		String baseURL="http://timesofindia.indiatimes.com/";
		int i =2001; int j=1; int k=1;

		if(start_year>0){
			i=start_year;
		}
		if(end_year>0){
			MAXYEAR=end_year;
		}

		String mm=""; String yy=""; String dd="";
		int counter=0;
		TreeMap<Integer, String> mapAllURL =new TreeMap();
		// crawl monthly and
		try{
			FileWriter writer = new FileWriter(outFile, is_Append_outFile);
			 int lMAXDATE=2;
				// <p class=""> </p>
				//int startIndex = bodyText.indexOf(start_tag);
				//int endIndex = bodyText.indexOf(end_tag, startIndex) + 1;
				System.out.println("i:"+i+";MAXYEAR:"+MAXYEAR);
				int f=0; int lastf=0;
				while(i<=MAXYEAR){
					yy=Integer.toString(i);
					j=1; f=0;
					System.out.println("i:"+i);
					while(j<=MAXMONTH){
						mm= Integer.toString(j);
						k=0;
						System.out.println("j:"+j+";MAXMONTH:"+MAXMONTH);
						TreeMap<Integer,Integer> mapMonthDays=getINoDaysInaMonth(i);
						lMAXDATE= mapMonthDays.get(j);
						while(k<lMAXDATE){
							dd= Integer.toString(k);

							System.out.println("mapStartNumber:"+mapStartNumber);

							if(!mapStartNumber.containsKey(Integer.toString(i)+"1")){
								System.out.println("not:"+Integer.toString(i)+"1");
								break;
							}

							System.out.println(mapStartNumber.get(Integer.toString(i)+"1"));
							if(j==1){ //first month
								f=Integer.valueOf( mapStartNumber.get(Integer.toString(i)+"1")) + k;
							}
							else{
								f=lastf+1;
							}

							//ex: 2013/3/1/archivelist/year-2013,month-3,starttime-41334.cms
							String t = baseURL+ yy +"/"+mm+"/"+(k+1)+"/"+"archivelist/"+"year-"+i+",month-"+j+",starttime-"
										+ Integer.toString(f)
										+  ".cms";

							System.out.println("new generated url:"+t);

							writer.flush();
							counter++;
							writer.append(counter+"!!!"+t+"\n");
							mapAllURL.put(counter, t);
							k++;
							lastf=f;
						}
						j++;
					}
					i++;
				}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapAllURL;
	}
	//ndtv
	//ex:http://archives.ndtv.com/articles/2014-04.html <- monthly available along with each day subtitle
	public static TreeMap<Integer, String> ndtv(String outFile,
												boolean is_Append_outFile){
		String baseURL="http://archives.ndtv.com/articles/";
		int i =2005; int j=1; int k=1;
		String mm=""; String yy=""; String dd="";
		int counter=0;
		TreeMap<Integer, String> mapAllURL=new TreeMap();
		System.out.println("i:"+i+";MAXYEAR:"+MAXYEAR);
		try{
		FileWriter writer=new FileWriter(outFile, is_Append_outFile);
		while(i<=MAXYEAR){
			yy=Integer.toString(i);
			j=1;
			System.out.println("i:"+i);
			//
			while(j<=MAXMONTH){
				mm= Integer.toString(j);
				k=1;
				System.out.println("j:"+j+";MAXMONTH:"+MAXMONTH);

					if( mm.length() ==1 ){
						mm="0"+mm;
					}

					String t = baseURL+ yy +"-"+mm+".html";
					System.out.println("ndtv().t:"+t);
					counter++;
					writer.append(counter+"!!!"+t+"\n");
					writer.flush();
					mapAllURL.put(counter, t);
				j++;
			}
			i++;
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("out map:"+mapAllURL.size());
		return mapAllURL;
	}
	//Read the year-wise page url and pull all the URLs from each page (each page has monthly URL of particular year).
	public static void URLOFndtv(		 String outFileForURLextractedFromMainMonthly,
										 String outFolder,
										 String outFileForSetOFnewsURLextractedFromEachMonthly,
										 boolean is_Append_outFile,
										 String failed_File,
										 boolean is_Append_Failed_File,
										 String success_File,
										 boolean is_Append_Success_File,
										 boolean skip_is_english,
										 boolean isDebug){
		Http_getWikiPageLinks gwp = new Http_getWikiPageLinks();
		int debugCounter=0;
		try{
			FileWriter writer_success=new FileWriter(outFolder+success_File, is_Append_Success_File );
			FileWriter writer_failed=new FileWriter(outFolder+failed_File, is_Append_Failed_File);
			FileWriter writerDebug =new FileWriter(outFolder+"debug.txt", true);
			FileWriter writer = new FileWriter(outFileForSetOFnewsURLextractedFromEachMonthly, is_Append_outFile);
			//get set of news articles from base archive url
			TreeMap<Integer, String> mapOut = ndtv(outFileForURLextractedFromMainMonthly, is_Append_outFile);
			if(isDebug) System.out.println("mapOut.size:"+mapOut.size());
			// for each found url (from monthly corpus of particular year) get all the news links for that month.
			for(int i:mapOut.keySet()){
				String url=mapOut.get(i);
				if(isDebug==true)
					System.out.println(".url only!!!!!!!!!!!!!!!!!!!!!!!!!!!!->"+url);
				TreeMap<Integer, String> monthYear=	Find_Start_n_End_Index_Of_GivenString
												 	.getMatchedLines(url, "href", ">",false);
				//if(monthYear.get(1).equalsIgnoreCase("-1")) continue; //cant find the pattern in the path
				TreeMap<Integer, TreeMap<Integer,String>> mapLinksFromMonthlyCorpus =new TreeMap();
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!->"+url+"-------"+monthYear.get(1).replace(".com", ""));
				try{
										mapLinksFromMonthlyCorpus = gwp.http_getLinks(
																					 1,
																					 url,
																					 "",  //html_input_File
																					 "", //output_to_store_list_of_URLs_already_crawled_file
																					 outFolder+"ndtv.1.outfoundURLfromWikiPage_MainMonthly_ndtv.txt",
																					 outFolder+"ndtv.2.outfoundURLfromWikiPage_MainMonthly_ndtv.txt",
																					 true, //outFile_1
																					 true,//outFile_2
																					 "/wiki/",
																					 "http://en.wikipedia.org/wiki/",
																					 monthYear.get(1).replace(".com", ""),
																					 "",//FilterURLOnMatchedString
																					 "sports,", //FilterURL_Omit_OnMatchedString
																					 "", //Stop_Word_for_ExtractedText
																					 "http://www.ndtv.com/",
																					 ">",
																					 outFolder+"debug.txt",
																					 skip_is_english,
																					 "", //type
																					 true
																					 );

							writerDebug.append("\nmapLinksFromMonthlyCorpus.size:"+mapLinksFromMonthlyCorpus.size());
							writerDebug.append("\nmapLinksFromMonthlyCorpus:"+mapLinksFromMonthlyCorpus);
							writerDebug.flush();

							writer_success.append("\n"+url);
							writer_success.flush();

				}
				catch(Exception e){
					writer_failed.append("\n"+url);
					writer_failed.append("\n"+url+"!!!"+e.getMessage());
					writer_failed.flush();

				}


				if(!mapLinksFromMonthlyCorpus.containsKey(-9)){
					//all news articles from current monthly corpus write to out file.
					for(int k:mapLinksFromMonthlyCorpus.get(1).keySet() ){
						String url2=mapLinksFromMonthlyCorpus.get(1).get(k).toLowerCase().replace("//", "/");
						//does it article
						if(url2.contains("http://www.ndtv.com/article")){
							if(isDebug){
								System.out.println("filtered word found ..so skipping.."+url2+"#"+url2.indexOf("http://www.ndtv.com/article"));
							}
							continue;
						}
						else{System.out.println("required article:"+url2);}
						System.out.println("writing:"+monthYear.get(1).replace(".com", "")+" "+url2+"\n");
						int k2=0;
						while(k2<5000000) {
							k2++;
						}
						writer.append(url.replace("http://archives.ndtv.com/articles/", "").replace(".html", "")+"!!!"
									  +url2+"\n");
						writer.flush();
					}
				}

				//writer
				System.out.println("size of current corpus set:"+mapLinksFromMonthlyCorpus.size());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//Read the year-wise page url and pull all the URLs from each page (each page has monthly URL of particular year).
	public static void URLOFtimesofindia(String outFileForURLextractedFromMainMonthly,
										 boolean is_Append_outFile,
										 String  outFolder,
										 String  outFileForSetOFnewsURLextractedFromEachMonthly,
										 String  failed_File,
										 boolean is_Append_Failed_File,
										 String  success_File,
										 boolean is_Append_Success_File,
										 boolean skip_is_english,
										 String  stop_pattern_for_url,
										 String  past_ran_success_file_for_skipping_url,
										 boolean is_ignore_past_ran_success_file_for_skipping_url,
										 boolean isDebug
										 ){
		Http_getWikiPageLinks gwp=new Http_getWikiPageLinks();
		TreeMap<String, String> map_Past_Ran_URL=new TreeMap<String, String>();
		try{
			String url="";
			FileWriter writer_success=new FileWriter(outFolder+success_File, is_Append_Success_File );
			FileWriter writer_failed=new FileWriter(outFolder+failed_File, is_Append_Failed_File);

			FileWriter writer = new FileWriter(outFileForSetOFnewsURLextractedFromEachMonthly, is_Append_outFile);
			FileWriter writerDebug= new FileWriter(outFolder+"debug.timesofindia.txt", true);
			TreeMap<Integer, String> mapOut = timesofindia(outFileForURLextractedFromMainMonthly,
															2000, //start_year
															2015, //end_year
															is_Append_outFile);

			if(isDebug){
				for(int i:mapOut.keySet()){
					writerDebug.append("\nurl generated:"+i+"!!!"+mapOut.get(i));
					writerDebug.flush();
				}

			}

			System.out.println("mapOut.size:"+mapOut.size());

			try{


				try{

				// past ran file
				map_Past_Ran_URL=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
						readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
																										past_ran_success_file_for_skipping_url
																										,-1 //startline
																										,-1// endline
																										,"" //outFile,
																										,false //is_Append_outFile,
																										,false //is_Write_To_OutputFile
																										,"" //debug_label
																										, -1 //token_to_be_used_as_primarykey
																										,true //boolean isSOPprint
																										);
				}
				catch(Exception e){
					writerDebug.append("\n past_ran_success_file_for_skipping_url:"+past_ran_success_file_for_skipping_url);
					writerDebug.append("\n error on reading past_ran_success_file_for_skipping_url: "+e.getMessage());
					writerDebug.flush();
				}


			// for each found url (from monthly corpus of particular year) get all the news links for that month.
			for(int i:mapOut.keySet()){
				url=mapOut.get(i);


				if(stop_pattern_for_url.length()>1){
					if(url.indexOf(stop_pattern_for_url)>=0){
						System.out.println("TOI..breaking..stop_pattern_for_url :"+stop_pattern_for_url);

						if(isDebug){
							writerDebug.append("\nTOI..breaking..stop_pattern_for_url :"+stop_pattern_for_url);
							writerDebug.flush();
						}

						break;
					}
				}

				if(is_ignore_past_ran_success_file_for_skipping_url==false &&
						map_Past_Ran_URL.containsKey(url)){
					System.out.println("skipping..already ran past:"+url);

					continue;
				}


				TreeMap<Integer, String> monthYear=
													Find_Start_n_End_Index_Of_GivenString.
													getMatchedLines(url, ".com", "archivelist",false);

				if(monthYear.get(1).equalsIgnoreCase("-1")) {
					if(isDebug){
						writerDebug.append("\n (monthYear) cant find the pattern in the path..continue :"+url);
						writerDebug.flush();
					}
					continue; //cant find the pattern in the path
				}

				System.out.println("!!!!!curr url->"+url+"-------"+monthYear.get(1).replace(".com", ""));
				TreeMap<Integer, TreeMap<Integer,String>> mapLinksFromMonthlyCorpus =new TreeMap();
				try{

										if(isDebug){
											writerDebug.append("\n befor calling getLinks :"+url);
											writerDebug.flush();
										}
										//
										mapLinksFromMonthlyCorpus = gwp.http_getLinks
																		(
																		 1,
																		 url, //url
																		 "", //html_input_File
																		 "", //output_to_store_list_of_URLs_already_crawled_file
																		 outFolder+"timesofindia.1.outfoundURLfromWikiPage_MainMonthly_ToI.txt",
																		 outFolder+"timesofindia.2.outfoundURLfromWikiPage_MainMonthly_ToI.txt",
																		 true, //outFile_1
																		 true,//outFile_2
																		 "/wiki/",
																		 "http://en.wikipedia.org/wiki/",
																		 monthYear.get(1).replace(".com", ""), //prefixCounter
																		 ".cms",
																		 "",//FilterURL_Omit_OnMatchedString
																		 "", //Stop_Word_for_ExtractedText
																		 "http", //start_tag
																		 ">", //end_tag
																		 outFolder+"debug.txt",
																		 skip_is_english,
																		 "", //type
																		 isDebug //isDebug
																		 );

										if(isDebug){

											writerDebug.append("\n success after calling getLinks :"+url
																+" fetched url.size:"+mapLinksFromMonthlyCorpus.get(1).size());

											writerDebug.flush();

										}
										writer_success.append("\n"+url);
										writer_success.flush();

				}
							catch(Exception e){
								writer_failed.append("\n"+url);
								writer_failed.append("\n"+url+"!!!"+e.getMessage());
								writer_failed.flush();

				}
				//debug
				if(!mapLinksFromMonthlyCorpus.containsKey(-9)){
					writerDebug.append("\n ---------fetch url start----------");
					writerDebug.flush();
					//all news articles from current monthly corpus write to out file.
					for(int k:mapLinksFromMonthlyCorpus.get(1).keySet() ){
						String url2=mapLinksFromMonthlyCorpus.get(1).get(k).toLowerCase().replace("//", "/");
						writerDebug.append("\n"+url2);
						writerDebug.flush();
					}
					writerDebug.append("\n ----------fetch url end---------");
					writerDebug.flush();
				}

				//
				if(!mapLinksFromMonthlyCorpus.containsKey(-9)){
					//all news articles from current monthly corpus write to out file.
					for(int k:mapLinksFromMonthlyCorpus.get(1).keySet() ){
						String url2=mapLinksFromMonthlyCorpus.get(1).get(k).toLowerCase().replace("//", "/");

						//
						if(url2.indexOf("bollywood.")>=0
						  || url2.indexOf("videos/")>=0||url2.indexOf("entertainment/")>=0||url2.indexOf("bollywood/")>=0
						  || url2.indexOf("sports/")>=0||url2.indexOf("business/")>=0||url2.indexOf("hollywood/")>=0
						  || url2.indexOf("topic/")>=0||url2.indexOf("tech/")>=0
						  || url2.indexOf("rss.cms")>=0||url2.indexOf("tech/")>=0
						  || url2.indexOf("photostory/")>=0||url2.indexOf("eternalplat_home")>=0
						  ||url2.indexOf("newslettersubscription.cms")>=0||url2.indexOf("feedback.cms")>=0||url2.indexOf("zigwheels.cms")>=0
						  ||url2.indexOf("magicbricks.cms")>=0||url2.indexOf("budgetspecial.cms")>=0||url2.indexOf("zigwheels.cms")>=0
						  ||url2.indexOf("javascript:populatediv")>=0
						  ||url2.indexOf("javascript:")>=0 ||url2.indexOf(".shopping.")>=0
						  ||url2.indexOf(".mobile.")>=0 ||url2.indexOf("/entertainment")>=0
						  ||url2.indexOf(".amazon.")>=0
						  ||url2.indexOf(".happytrips.")>=0
						  ||url2.indexOf("idiva.")>=0
						  ||url2.indexOf("beautypageants.")>=0
						  ||url2.indexOf("photogallery.")>=0
						  ||url2.indexOf("ads.indiatimes.com")>=0
						  ||url2.indexOf(".gocricket.")>=0
						  ){

							if(isDebug){
								if(url.indexOf("http")==-1){
								writerDebug.append("\n NOT a http :"+url);
								writerDebug.flush();
								}
								writerDebug.append("\n NOT a http2 :"+url);
							}
							continue;
						}

						writer.append(monthYear.get(1).replace(".com", "")+"!!!"+url2+"\n");
						writer.flush();
					}
				} //end of
				//writer
				System.out.println(mapLinksFromMonthlyCorpus.size());
			}

			}
			catch(Exception e){
				writerDebug.append("\n errror:"+e.getMessage()+"!!!url:"+url );
				writerDebug.flush();
			}

		}
		catch(Exception e){

			e.printStackTrace();
		}
	}
	// main
	public static void main(String[] args){
		String Folder="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/";
		TreeMap<Integer, String> mapOut=null;
		String failed_File="";
		boolean is_Append_Failed_File=true;
		String success_File="";
		boolean is_Append_Success_File=true;
		boolean skip_is_english=true;
		//TreeMap<Integer, String> mapOut = thehindu("");
		//TreeMap<Integer, String> mapOut = ndtv("");
		//TreeMap<Integer, String> mapOut = timesofindia(Folder+"timesofIndia.txt");
		String  stop_pattern_for_url="";
		String  past_ran_success_file_for_skipping_url=Folder+"timesofindia.success.time_of_india.txt";
		boolean is_ignore_past_ran_success_file_for_skipping_url=false;

		// pull all the required news articles from base archives
//		URLOFtimesofindia(	Folder+"timesofindia-main.txt",
//							true, //is_Append_outFile
//							Folder,
//							Folder+"timesofindia.allnewsArticles-times-of-india-URL.txt",
//							"timesofindia.failed.time_of_india.txt", //failed_file
//							true, //is_Append_Failed_File
//							"timesofindia.success.time_of_india.txt", //success_file
//							true, //is_Append_Success_File
//							true, //skip_is_english
//							stop_pattern_for_url,
//							past_ran_success_file_for_skipping_url,
//							is_ignore_past_ran_success_file_for_skipping_url,
//							true);


		// pull thehinduweb
//		URLOFthehinduweb(	Folder+"thehindu-web.txt",
//							true,
//							Folder,
//							Folder+"thehindu.allnewsArticles_thehindu_web_URL.txt",
//							"thehindu.failed.thehindu_web.txt", //failed_file
//							true, //is_Append_Failed_File
//							"thehindu.success.thehindu_web.txt", //success_file
//							true, //is_Append_Success_File
//							skip_is_english, //skip_is_english
//							"2015/07",//stop_pattern_for_url
//							2009, //min_year
//							2015, //max_year
//							true
//						);

		//pull thehinduprint
		//********** available from http://www.thehindu.com/archive/print/2006/01/01/
//		URLOFthehinduprint06ToTill(	Folder+"thehindu-06-to-till.txt",
//									Folder,
//									Folder+"thehindu.allnewsArticles-thehindu-URL-Print-06-to-till.txt",
//									"thehindu.failed.thehindu-print-06-to-till.txt", //failed_file
//									true, //is_Append_Failed_File
//									"thehindu.success.thehindu-print-06-to-till.txt", //success_file
//									true, //is_Append_Success_File
//									true);

		//http://www.thehindu.com/thehindu/2000/01/01/ <front page>
		//http://www.thehindu.com/thehindu/2000/01/01/02hdline.htm
		//http://www.thehindu.com/thehindu/2000/01/01/03hdline.htm
		//http://www.thehindu.com/thehindu/2000/01/01/04hdline.htm
//		URLOFthehinduprint2000To2005(	Folder+"thehindu.main.monthly.txt",
//										Folder,
//										Folder+"thehindu.allnewsArticles-thehindu-URL-print-2000-05.txt",
//										"thehindu.failed.thehindu-URL-print-2000-05.txt", //failed_file
//										true, //is_Append_Failed_File
//										"thehindu.success.thehindu-URL-print-2000-05.txt", //success_file
//										true, //is_Append_Success_File
//										true, //isAppendoutFileForSetOFnewsURLextractedFromEachMonthly
//										true  //isDebug
//										);

//		// from 23hdline to start
//		URLOFthehinduprint2000To2005detailedNational(	Folder+"thehindu.txt",
//														Folder,
//														Folder+"thehindu.print.state-wise-00-05-detailed-national.txt",
//														true,
//														"thehindu.failed.thehindu00-05-detailed-national.txt", //failed_file
//														true, //is_Append_Failed_File
//														"thehindu.success.thehindu00-05.txt", //success_file
//														true, //is_Append_Success_File
//														true);
		//ndtv
//		URLOFndtv(	Folder+"ndtv_main_monthly.txt",
//					Folder,
//					Folder+"ndtv.allnewsArticles-ndtv-.URL.txt",
//					true, //is_Append_outFile
//					"ndtv.failed.txt", //failed_file
//					true, //is_Append_Failed_File
//					"ndtv.success.txt", //success_file
//					true, //is_Append_Success_File
//					true, // skip_is_english
//					true);
		//System.out.println("map size:"+mapOut.size()+":"+mapOut.get(1));


	}
}