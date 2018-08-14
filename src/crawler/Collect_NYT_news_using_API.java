package crawler;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.TreeMap;

//NYT
public class Collect_NYT_news_using_API {

	
	//http://api.nytimes.com/svc/search/v2/articlesearch.json?begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328

	//for range of dates 
	public static TreeMap<String, String> collect_NYT_news_using_API(
																	String outFile,
																	int fromDate,
																	int toDate
																	//String api_key
												 					){
		TreeMap<Integer, String> mapYearRange=new TreeMap<Integer, String>();
		TreeMap<String, String> map_NYT_YearRange=new TreeMap<String, String>();
		
		
		try{

			mapYearRange=Get_NewsPaper_ArchiveURL.thehindu_print(outFile,
																"", //baseURL to append
															    fromDate, 
															    toDate 
															    );
			
			//
			for(int i:mapYearRange.keySet()){
				
				String h=mapYearRange.get(i)
						 .replace("http://www.thehindu.com/thehindu/", "");
				
//				System.out.println( h+ "!!!"+ 
//									h.substring(0, 4) + 
//									h.substring(5, 7) +
//									h.substring(8, 10)
//									);
				map_NYT_YearRange.put(h.substring(0, 4) +
								      h.substring(5, 7) +
								      h.substring(8, 10) ,
								      h
									  );
						 
			}
			System.out.println("size:"+mapYearRange.size());
			
//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&type_of_material=News&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&type_of_material=Article&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&type_of_material.contains={News Analysis}&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&type_of_material.contains={Front Page}&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&type_of_material.contains={Newsletter}&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328

//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&news_desk=U.S.&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&news_desk=World&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&news_desk=Regionals&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&news_desk=Foreign&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328

//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&section_name=U.S.&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&section_name=World&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&section_name=National&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
//http://api.nytimes.com/svc/search/v2/articlesearch.json?page=20&section_name={Front Page}&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
					
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return map_NYT_YearRange;
	}
	
	// returns list of interested URL with input page_no and start_date and end_date
	public static TreeMap<String, String> getCustomized_List_Of_url_of_NYT(
																		int page,
																		String start_date,
																		String end_date,
																		String api_key
																	){
		
		TreeMap<String, String> map_Customized_List_Of_url_of_NYT=new TreeMap<String, String>();
		try {
			//type of materials
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&type_of_material=News&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
			"d");
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&type_of_material=Article&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
			"d");
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&type_of_material.contains={News%Analysis}&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
			"d");
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&type_of_material.contains={Front%Page}&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
			"d");
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&type_of_material.contains={Newsletter}&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
			"d");
			
			//news Desk
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&news_desk=U.S.&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
			"d");
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&news_desk=World&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
			"d");
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&news_desk=Regionals&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
			"");
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&news_desk=Foreign&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
			"");

			//section
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&section_name=U.S.&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
            "");
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&section_name=World&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
			"");
			
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&section_name=National&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
			"");
			
			map_Customized_List_Of_url_of_NYT.put(
			"http://api.nytimes.com/svc/search/v2/articlesearch.json?page="+page+"&section_name={Front%Page}&begin_date="+start_date+"&end_date="+end_date+"&api-key="+api_key,
			"");

		
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return map_Customized_List_Of_url_of_NYT;
	}
	
	//main_to_run_nyt_date_range_and_custom_url
	public static void main_to_run_nyt_date_range_and_custom_url(String outFile,
																 String startPattern,
																 int startYear,
																 int endYear,
																 String outFile_Already_Crawled_list_of_URL,
																 String outFile_writing_crawled_URL_list,
																 String base_Folder,
																 TreeMap<String, String> map_api_key
																 ){
		TreeMap<String, String>  map_Customized_List_Of_url_of_NYT=new TreeMap<String, String>();
		TreeMap<String, String>  map_Already_Crawled_URL_load_from_file_raw=new TreeMap<String, String>();
		TreeMap<String, String>  map_Already_Crawled_URL_load_from_file=new TreeMap<String, String>();
		TreeMap<Integer, String> mapOut = new TreeMap<Integer, String>();
		String failed_File = "/Users/lenin/Dropbox/dataDONTDELETE/NYT-2014-09-TO-2014-12/failed.txt";
		String success_File = "/Users/lenin/Dropbox/dataDONTDELETE/NYT-2014-09-TO-2014-12/success.txt";
		String caught_already_crawled_url=base_Folder+"caught_already_crawled_url.txt";
		
		FileWriter writer=null;
		
		Date date = new Date();
	    String StartDate=date.toString();  

		 
		map_Already_Crawled_URL_load_from_file_raw=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
														  outFile_Already_Crawled_list_of_URL
														, 0 //startline, 
														, 100000000   //endline
														, "" //outFile_Already_Crawled 
														, false //is_Append_outFile 
														, false //is_Write_To_OutputFile
														, "" //debug_label
														, -1 //token to be used as primary key
														,true //boolean isSOPprint
														);
		
		for(String i:map_Already_Crawled_URL_load_from_file_raw.keySet()){
			System.out.println("i.before substring:"+i);
			if(i.length()<=4) continue;
			String s=i.substring(0, i.indexOf("&api"));
			System.out.println("i.after substring:"+s);
			map_Already_Crawled_URL_load_from_file.put(s,
													   s);
		}
		
		
		TreeMap<String, String> map_collect_nyt_date_range_in_using_of_api=  
								Collect_NYT_news_using_API.collect_NYT_news_using_API(
																					  outFile,
																					  startYear,
																					  endYear
																				//	  api_key
																					  );
		 // for each of API key
		for(String api_key:map_api_key.keySet()){
		
		int max_page_no=100; int page_no=1;
		String ExtractedText="";
		try {
			FileWriter writerSuccess=new FileWriter(new File(success_File), true);
			FileWriter writerFailed=new FileWriter(new File(failed_File), true);
			FileWriter writerCaught_already_crawled_url=new FileWriter(new File(caught_already_crawled_url), true);
			
			writer=new FileWriter(new File(outFile_writing_crawled_URL_list), true);
			boolean is_continue_after_found_startpattern=false;
			
			if(startPattern.equalsIgnoreCase("")){
				is_continue_after_found_startpattern=true;
			}
			int continued_failure_403=0;
			//each YYYYMMDD
			for(String curr_YYYYMMDD:map_collect_nyt_date_range_in_using_of_api.keySet()){
				int number_of_empty_docs=0;
				continued_failure_403=0;
				page_no=1;
				if(is_continue_after_found_startpattern==false){
					//startpattern found?
					if(curr_YYYYMMDD.indexOf(startPattern)>=0){
						is_continue_after_found_startpattern=true;
						System.out.println("found startpattern:"+startPattern);
					}
					//
					if(is_continue_after_found_startpattern==false){
						System.out.println("NOT found startpattern:"+startPattern);
						continue;
					}
				}
				
				//
				while(page_no<max_page_no){
					//
					
					
					map_Customized_List_Of_url_of_NYT=getCustomized_List_Of_url_of_NYT(page_no,
																						 curr_YYYYMMDD,
																						 curr_YYYYMMDD,
																						 api_key
																						);
					//crawling
					for(String i:map_Customized_List_Of_url_of_NYT.keySet()){
						System.out.println("------------------------");
						System.out.println("map_Already_Crawled_URL_load_from_file.size:"+map_Already_Crawled_URL_load_from_file.size()+";curr_YYYYMMDD:"+curr_YYYYMMDD);
						System.out.println("continued_failure:"+continued_failure_403+" url:"+i);
						System.out.println("api_key:"+api_key);
						if(map_Already_Crawled_URL_load_from_file.containsKey(i.substring(0, i.indexOf("&api")) )){
							System.out.println("Already exists.."+i);
							writerCaught_already_crawled_url.append("\n"+i);
							writerCaught_already_crawled_url.flush();
							//continue;
							break;
						}
						
						mapOut=Crawler_send_http_request.crawler_send_http_get(i, "");
						
						ExtractedText= mapOut.get(1);
						//
						if(!mapOut.get(2).equalsIgnoreCase("200")){
							System.out.println("FAILED:" );
							writerFailed.append("\n"+i);
							writerFailed.flush();
							if(mapOut.get(2).equalsIgnoreCase("403")){
								continued_failure_403++;
							}
						}
						else{
							//only success write
							writer.append("\n"+i+"!!!"+ExtractedText+"!!!"+ExtractedText.length());
							writer.flush();
							
							if(ExtractedText.indexOf("\"docs\":[]}")>=0){
								number_of_empty_docs++;
							}
							
						}
						//
					
						writerSuccess.append("\n"+i);
						writerSuccess.flush();
						//
						if(number_of_empty_docs>=2){
							System.out.println("Empty document: "+number_of_empty_docs
												+";curr_YYYYMMDD:"+curr_YYYYMMDD);
							number_of_empty_docs=0;
							break;
						}
						 
					}
					page_no++;
					if(continued_failure_403>=22){//OFFSET
						break;
					}
				}
				System.out.println("*******************end of page***********curr_YYYYMMDD:"+curr_YYYYMMDD);
				if(continued_failure_403>=22){//OFFSET
					break;
				}
				
			} 
			
		}
	      
		 catch (Exception e) {
			// TODO: handle exception
		}
	   } // for each of map_api_key
	   
		 date = new Date();
		 String endDate=date.toString();
		 System.out.println("start datetime:"+StartDate);
		 System.out.println("end   datetime:"+endDate);

	}
	
	//main
	public static void main(String[] args) throws IOException {
		int startYear=2014;
		int endYear=2015;
		long t0 = System.nanoTime();
		String baseFolder="/Users/lenin/Downloads/#crawleroutput/nyt/attempt.1/";
		String outFile=baseFolder+"outRange.txt";
		TreeMap<String, String> map_Customized_List_Of_url_of_NYT=new TreeMap<String, String>();
		
		String outFile_Already_Crawled=baseFolder+"success.txt";
		String outFile_writing_crawled_URL_list=baseFolder+"NYT_out_crawled.SET.1.txt";
		String startPattern="201404";
		String api_key_CSV="23c38a9337878b2ca1c10fca0afda77b:15:72969328";
				api_key_CSV= "23c38a9337878b2ca1c10fca0afda77b:15:72969328,"
							+"532b9a6915ec648bb56636be3809b77b:15:73018277,"
			 	  			+"ee46096862bd43a1c8d0fa3c20167758:4:73018177";
		
	 	//23c38a9337878b2ca1c10fca0afda77b:15:72969328
		//532b9a6915ec648bb56636be3809b77b:15:73018277
	    //ee46096862bd43a1c8d0fa3c20167758:4:73018177
		TreeMap<String, String> map_api_key=new TreeMap<String, String>();
		String [] api_array=api_key_CSV.split(",");
		int cnt=0;
		System.out.println("api_array:"+api_array.length);
		//
		while(cnt<api_array.length){
			map_api_key.put(api_array[cnt], api_array[cnt]);
			cnt++;
		}
		 System.out.println("map api:"+map_api_key);
  		 // collect NYT 
		 Collect_NYT_news_using_API.main_to_run_nyt_date_range_and_custom_url(
																		  outFile,
																		  startPattern,
																		  startYear,
																		  endYear,
																		  outFile_Already_Crawled,
																		  outFile_writing_crawled_URL_list,
																		  baseFolder,
																		  map_api_key
																		  );
		 //
		 //getCustomized_List_Of_url_of_NYT(10, "20140801", "20140801");

			//list of API keys
		 	//23c38a9337878b2ca1c10fca0afda77b:15:72969328
			//532b9a6915ec648bb56636be3809b77b:15:73018277
		    //ee46096862bd43a1c8d0fa3c20167758:4:73018177
		 
		 
		 System.out.println("Time Taken (FINAL ENDED):"
					+ NANOSECONDS.toSeconds(System.nanoTime() - t0)
					+ " seconds; "
					+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
					+ " minutes");
	}
	
}