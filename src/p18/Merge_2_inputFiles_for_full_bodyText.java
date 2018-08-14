package p18;

import java.io.File;

import crawler.ReadFile_2_EachLineORtoken_from_First_File_exists_in_Second_File_as_String;
import java.io.FileWriter;
import java.util.TreeMap;
import crawler.Find_occurance_Count_Substring;
import crawler.Load_domainname_pattern4crawledHTML;
import crawler.Find_domain_name_from_Given_URL;
//import p8.co
import crawler.Clean_retain_only_alpha_numeric_characters;
import crawler.Crawler;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

public class Merge_2_inputFiles_for_full_bodyText {
	
	//get_approx_index_of_DATETIME
	public static int get_approx_index_of_DATETIME(String crawledHTMLpage, String YYYY){
		int index=-1; boolean found=false; int max_cnt=15; int cnt2=0;
		try {
			String found_string="";
			//while(found==false){
			while(cnt2<max_cnt){
				
				int found_index=crawledHTMLpage.indexOf(YYYY);
				System.out.println("idx:"+found_index ); //+" crawledHTMLpage:"+crawledHTMLpage);
				found_string=crawledHTMLpage.substring(found_index-15, found_index+17);
				System.out.println("trying:"+found_string);
				
				int cnt=Find_occurance_Count_Substring.find_occurance_Count_Substring(":",found_string);
				
				if(cnt>=2 || found_string.indexOf(" AM ")>=0|| found_string.indexOf(" PM ")>=0){
					//found=true;
					index=found_index;
					System.out.println("****found_string:"+found_string);
					break;
				}
				else{
					
				}
				// YYYY = 4 length
				crawledHTMLpage=crawledHTMLpage.substring(found_index+4, crawledHTMLpage.length());
				cnt2++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return index;
	}
	 
	public static void merge_2_inputFiles_for_full_bodyText(
															String baseFolder,
															String file1_from_rssFeed,
															String file2_from_crawled,
															String OUTPUT_file3, //out
															String file4_DomainName_NewsStartPattern,
															boolean isSOPprint
															){
		int count_half_match=0;
		TreeMap<String, String> map_file1_keyword_bodytext=new TreeMap<String, String>();
		TreeMap<String, String> map_file1_keyword_Subject=new TreeMap<String, String>();
		TreeMap<String, String> map_file2_keyword_bodytext=new TreeMap<String, String>();
		TreeMap<String, String> map_file1_keyword_year=new TreeMap<String, String>();
		TreeMap<String, String> map_file1_keyword_DOMAINname=new TreeMap<String, String>();
		//load
		TreeMap<String, String> map_domainName_NewsStartPattern=Load_domainname_pattern4crawledHTML.load_domainname_pattern4crawledHTML(file4_DomainName_NewsStartPattern);
		FileWriter writer=null;
		FileWriter writerDebug=null;
		try {
			writer=new FileWriter(new File(OUTPUT_file3));
			writerDebug=new FileWriter(new File(OUTPUT_file3+"_DEBUG.txt"));
			
			// 
			TreeMap<Integer,String> map_file1=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								  file1_from_rssFeed,
																	 							  -1, //startline, 
																	 							  -1, //endline,
																	 							  "f1 ", //debug_label
																	 							  false //isPrintSOP
																							 	  );
			// 
			TreeMap<Integer,String> map_file2=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								 file2_from_crawled, 
																							 	 -1, //startline, 
																								 -1, //endline,
																								 "f1 ", //debug_label
																								 false //isPrintSOP
																								 );
			int cnt1=0,cnt2=0;
			String url =  "";
			//rss feed
			for(int seq:map_file1.keySet()){
				//body!!!URL!!!datetime
				String []s = map_file1.get(seq).split("!!!");
				if(s.length>=2){
					//clean URL
					if(s[1].indexOf("url=")>=0)
						url = Clean_embeddedURLs.clean_get_embeddedURL( s[1].replace("new():", ""));
					else
						url = s[1].replace("new():", "");
					
					map_file1_keyword_bodytext.put(url, //URL 
												   s[0]);
					
					map_file1_keyword_Subject.put(  url, //URL 
													s[3] //4th token is Subject
													);
					
					//example s[2]:from - fri, 16 oct 2015 20:24:58 mdt x-mozilla-status: 
					s[2]=s[2].substring(s[2].indexOf(",")+2, s[2].length());
					//System.out.println("1:"+s[2]);
					String [] s2=s[2].split(" ");
					
					//CALLING domain name
					String domain_name=Find_domain_name_from_Given_URL.find_domain_name_from_Given_URL(url, false);
					// <URL,domainNAME>
					map_file1_keyword_DOMAINname.put(url, domain_name );
					
					//System.out.println("map_file1.get(seq):"+map_file1.get(seq));
					if(s2.length>=3){

						//YYYY
						if(s2[2].length()==4){//only length 4
							map_file1_keyword_year.put(	 url , //URL 
														s2[2]); // s2[2]=YYYY <--YEAR
							if(isSOPprint)
								System.out.println("2:"+s2[2].replace("new():", "")+" lline:"+seq);
						}
					}
					else{
						writerDebug.append("\n token <3: lineNo:"+seq+" line:"+ map_file1.get(seq));
						writerDebug.flush();
					}
					
				}
				else{
					if(isSOPprint)
						System.out.println("error token:"+ map_file1.get(seq));
				}
				cnt1++;
			}
			// crawled from URL
			for(int seq:map_file2.keySet()){
				// URL!!!crawledHTMLtext
				String []s = map_file2.get(seq).split("!!!");

				if(s.length>=2){
					//clean URL
					if(s[0].indexOf("url=")>=0)
						url =  Clean_embeddedURLs.clean_get_embeddedURL( s[0].replace("new():", ""));
					else 
						url=s[0].replace("new():", "");
					//System.out.println(s[0]);
					map_file2_keyword_bodytext.put( url,
													s[1]);
					//debug 
					if(seq<5){
						writerDebug.append("\n map_file2_keyword_bodytext->"+s[0].replace("new():", "")+"---"+s[1]);
						writerDebug.flush();
					}
				}
				cnt2++;
			}
			
			String currBodyText_Crawled="";
			int cnt=0;
			String currBodyText_rssFeeed_100="";
			int hit_cnt=0; int no_hit_cnt=0;
			 int cantFind_url_from_file1_in_file2=0;
			 int cnt_NULL_4_curr_pattern_4_crawledHTML=0;
			// for each RSS feed
			for(String currURL:map_file1_keyword_bodytext.keySet()){
				cnt++;
				//
				String currBodyText_rssFeeed=map_file1_keyword_bodytext.get(currURL);
				String currDomainName=map_file1_keyword_DOMAINname.get(currURL);
				String curr_pattern_4_crawledHTML=map_domainName_NewsStartPattern.get(currDomainName);
				
//				try{
//					currBodyText_rssFeeed_100=currBodyText_rssFeeed.substring(15, 100);
//				}
//				catch(Exception e){
//					try{
//						currBodyText_rssFeeed_100=currBodyText_rssFeeed.substring(15, 55);
//					}
//					catch(Exception e2){
//						currBodyText_rssFeeed_100=currBodyText_rssFeeed;
//					}
//				}
//				
//				String currBodyText_rssFeeed_compressed=
//						clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters(currBodyText_rssFeeed, true);
//				//half
//				String currBodyText_rssFeeed_compressed_HALF=currBodyText_rssFeeed_compressed.substring(0, 
//																										currBodyText_rssFeeed_compressed.length()/2);

				// file1 URL find in file2
				if(map_file2_keyword_bodytext.containsKey(currURL)){
					currBodyText_Crawled=map_file2_keyword_bodytext.get(currURL);
				}
				else {
					currBodyText_Crawled="";
					cantFind_url_from_file1_in_file2++;
					if(isSOPprint){
						System.out.println("continue:"+cnt);
					}
					// Write_debug
					//writerDebug.append("\n currURL:"+currURL);
					writerDebug.flush();
					
					continue;
				}
				
				String tmp="";
				int begin=-1;
				if(isSOPprint){
					System.out.println("year :"+ map_file1_keyword_year.get(currURL)+" url:"+currURL+" pattern:"+curr_pattern_4_crawledHTML);
				}
				//
				if(curr_pattern_4_crawledHTML==null){
					curr_pattern_4_crawledHTML="dummydummy";
					//writerDebug.append("\n NOT FOUND - currDomainName="+currDomainName +" for NULL");
					cnt_NULL_4_curr_pattern_4_crawledHTML++;
				}
				else{
//					writerDebug.append("\n NOW** FOUND - currDomainName="+currDomainName 
//										+" curr_pattern_4_crawledHTML="+curr_pattern_4_crawledHTML);
				}
				
				//int begin=currBodyText_Crawled.indexOf("<p ");
				// METHOD 1 APPROXIMATELY try to get DATETIME begin to find START OF ARTICLE (NOT VERY RIGHT)
				//begin=get_approx_index_of_DATETIME(currBodyText_Crawled, map_file1_keyword_year.get(currURL));
				// METHOD 2  (NOT VERY RIGHT)
				//begin= currBodyText_Crawled.toLowerCase().indexOf(currBodyText_rssFeeed_100)-15; 
				// METHOD 3 (USING manually collected pattern for start of news in CRAWLED HTML)
				
				//example: if given curr_pattern_4_crawledHTML-->class="article-body";<p> , first search for class, then followed by <p>
				if(curr_pattern_4_crawledHTML.indexOf(";")==-1){
					begin=currBodyText_Crawled.indexOf(curr_pattern_4_crawledHTML);
				}
				else{
					String [] pattern=curr_pattern_4_crawledHTML.split(";");
					begin=currBodyText_Crawled.indexOf(pattern[0]);
					begin=currBodyText_Crawled.indexOf(pattern[1],begin+2);
				}
				
				
				writerDebug.append("\n NOW** FOUND - currDomainName="+currDomainName 
											+" begin="+begin+" "+currBodyText_Crawled.contains( curr_pattern_4_crawledHTML) );
				
				if(currDomainName.equalsIgnoreCase("armenianow.com")){
					writerDebug.append("\n 	currBodyText_Crawled:"+currBodyText_Crawled);
				}
				
				if(begin>=0){hit_cnt++;}
				else{ no_hit_cnt++;}
				
				
				//
//				if(currBodyText_Crawled.lastIndexOf(currBodyText_rssFeeed_compressed_HALF)>=0){
//					count_half_match++;
//					begin=currBodyText_Crawled.lastIndexOf(currBodyText_rssFeeed_compressed_HALF);
//				}
				
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

//				System.out.println("curr_pattern:"+curr_pattern_4_crawledHTML
//									+" tmp:"+tmp
//									);

				/// remove HTML TAGS
				tmp=Crawler.SubRemoveTagsFromHTML(tmp);
				/// remove junk words
				tmp=Clean_htmlcrawled_remove_htmlTagsETC.remove_htmlTagsETC(tmp);
				tmp=tmp.replaceAll("\\s+", " ");
				
				tmp=tmp+" beginINDEXis"+begin;
				
//				if(currURL.equalsIgnoreCase("http://www.ibtimes.com/indian-american-sikh-man-chicago-beaten-alleged-hate-crime-called-bin-laden-terrorist-2090442")){
//					tmp=tmp+currBodyText_Crawled;
//				}
				
				// PRINT 
				String print =  currURL+"!!!"+
								map_file1_keyword_bodytext.get(currURL)+"!!!"+
								map_file1_keyword_Subject.get(currURL)+"!!!"+
								tmp+" #!!!"
								+currDomainName;
								//	+curr_pattern_4_crawledHTML;
				
				if(curr_pattern_4_crawledHTML.toLowerCase().indexOf("note")>=0){
					print=print+"#!!!"+curr_pattern_4_crawledHTML;
				}
				else
					print=print+"#!!!"+" ";
				
				// write to OUTPUT
				writer.append(print+"\n");
				writer.flush();
				
			}
			
			System.out.println("----------------------------");
			System.out.println("file1_from_rssFeed:"+file1_from_rssFeed +"\n file2_from_crawled:"+file2_from_crawled);
			System.out.println("size:file1:"+map_file1_keyword_bodytext.size()+" size:file2:"+map_file2_keyword_bodytext.size());
			System.out.println("cantFind_url_from_file1_in_file2:"+cantFind_url_from_file1_in_file2
								+" cnt_NULL_4_curr_pattern_4_crawledHTML:"+cnt_NULL_4_curr_pattern_4_crawledHTML);
			System.out.println("map_file1(cnt):"+cnt1+" map_file2(cnt2):"+cnt2
								+ " hit_cnt:"+hit_cnt+" no_hit_cnt:"+no_hit_cnt);
			
			System.out.println("map_file1_keyword_bodytext.size:"+map_file1_keyword_bodytext.size() 
								+" map_file2_keyword_bodytext.size:"+map_file2_keyword_bodytext.size());
			
			//writerDebug.append("currBodyText_CrawledMAP:"+map_file2_keyword_bodytext);
			writerDebug.flush();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		finally {
			
		}
		
	}
	
	// main
	public static void main(String[] args) throws Exception{
		//
		String baseFolder="/Users/lenin/Downloads/#problems/p18/ds1/bfr_17Feb_set2/";
		String fileprefix="bfr_17Feb_set2"; //"17feb";
		//RSS FEED ( <body!!!URL!!!dateTime!!!Subject> )
		String file1=baseFolder+fileprefix+"_all_issue_body_URL_datetime_RSSFEED.txt";
		//CRAWLED
		String file2=baseFolder+fileprefix+"_CRAWLED_output.txt";
		//OUTPUT
		String out_file3=baseFolder+fileprefix+"_OUTPUT_merged_full_bodyText.txt";
		// 
		String file4_DomainName_NewsStartPatterns="/Users/lenin/Downloads/#problems/p18/ds1/global_domainNAMES_patternFORNEWSBODYSTART.txt";
		
		//background: (1) Filter out based on keywords (mental illness etc) from news feed say "file1".
		//			  (2) Some news articles have only partial body of news. Hence, need to crawl the full body. call that "file2"
		//			  
		//STEP 1: MERGING RSSFEED + CRAWLED HTML raw  file
		//MERGE
		merge_2_inputFiles_for_full_bodyText(
											 baseFolder,
											 file1, //FROM RSS FEED (INPUT)
											 file2, //FROM CRAWLED HTMLPAGE (INPUT)
											 out_file3,//OUTPUT
											 file4_DomainName_NewsStartPatterns,
											 false //	isSOPprint
						 					);
		
		String file2_EXISTS=file2+"_EXISTS_FROM_RSSFEED.txt";
		String file2_NOTEXISTS=file2+"_NOT_EXISTS_FROM_RSSFEED.txt";
		String _File1_addedComment=file2+"__FROM_RSSFEED_File1_addedComment.txt";
		// STEP 2 : mismatch between RSSFEED (file1) and file3 above
		ReadFile_2_EachLineORtoken_from_First_File_exists_in_Second_File_as_String.
		readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String( file1  , //first_File, 
																					"", //delimiter_to_be_added_to_first_File, 
																					2, //token_interested_in_first_File, 
																					out_file3, // second_File, (input here)
																					"!!!", //delimiter_for_splitting, 
																					file2_EXISTS, //out_File_exists, 
																					false, //is_Append_outFile_exists, 
																					file2_NOTEXISTS, //out_File_not_exists, 
																					false, //  is_Append_outFile_not_exists,
																					_File1_addedComment, //outFile_File1_addedComment
																					false  // isSOPprint
																					);
		
 
		//STEP 3: (ENGLISH ONLY)
		int 	token_having_URL_for_file1=2;
		String  file2_DomainName_NewsStartPattern="/Users/lenin/Downloads/#problems/p18/ds1/global_domainNAMES_patternFORNEWSBODYSTART.txt";
		String  OUTPUT_file3=out_file3+"_"; //out

		 
		//remove_NON_ENGLISH_newsarticles
		Remove_NON_ENGLISH_newsarticles.
		remove_NON_ENGLISH_newsarticles(
								   		baseFolder,
								   		out_file3, //STEP 1 (from)..INPUT here
								   		token_having_URL_for_file1,
								   		file2_DomainName_NewsStartPattern,
								   		OUTPUT_file3, //out
								   		false //isSOPprint
								   		);
		System.out.println("STEP 3: INPUT:"+file2_EXISTS);
		System.out.println("STEP 3: OUTPUT:"+OUTPUT_file3);
		
		//STEP 4: LENGTH LIMIT
		int 	token_1_for_applying_length_FILTER=2;
		int 	token_2_for_applying_length_FILTER=4;
		OUTPUT_file3=OUTPUT_file3+"_ENGLISH.txt"; //ENGLISH
		boolean isSOPprint=false;
		int filter_length=600;
		
		// file1 -> (1) REMOVE note:news articles (2) remove NOT FOUND startPattern for news article
		//          (3) REMOVE short message on given length
		//readFile_read_a_token_FILTER_on_length
		ReadFile_read_a_token_FILTER_on_length.
		readFile_read_a_token_FILTER_on_length(
								   		baseFolder,
								   		OUTPUT_file3,
								   		token_1_for_applying_length_FILTER,//can be -1
								   		token_2_for_applying_length_FILTER, //can be -1
								   		filter_length,
								   		OUTPUT_file3+"_LEN_50_FILTER.txt", //out
								   		isSOPprint);
		
		System.out.println("STEP 4: INPUT:"+OUTPUT_file3);
		System.out.println("STEP 4: OUTPUT:"+OUTPUT_file3+"_LEN_50_FILTER.txt");
		
	}

}
