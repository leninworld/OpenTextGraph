package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class ReadFile_verify_is_RSS {

	//NOTE: INPUT is second file (2.xxxxx) from http_getLinks() or http_getWikiPageLinks.http_getLinks()
	public static void readFile_verify_is_RSS(String inFile,
											  int start_line,
											  int end_line,
											  String outFile,
											  boolean is_Append_outFile,
											  String pastRunResultsFile,
											  boolean is_Append_pastRunResultsFile,
											  boolean isSOPprint
											  ){
		TreeMap<String, String> mapURL_eachLine=new TreeMap<String, String>();
		Runtime rs =  Runtime.getRuntime();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			FileWriter writer=new FileWriter(new File(outFile), is_Append_outFile);
			FileWriter writer_pastRunResultsFile=new FileWriter(new File(pastRunResultsFile), is_Append_pastRunResultsFile);
			TreeMap<String, String> mapPastRunResults=new TreeMap<String, String>();
			// given 
			if(     pastRunResultsFile.length()>2
				|| !pastRunResultsFile.equalsIgnoreCase("dummy")){
					mapPastRunResults=
							ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
							readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					    			(pastRunResultsFile, 
					    			-1,-1,
						    		"", //outFile,
						    		false, //is_Append_outFile, 
						    		false, //is_Write_To_OutputFile
						    		"", //debug_label
						    		-1, // token to be used for primary key
						    		true //boolean isSOPprint 
						    		);
				    		
			}
			TreeMap<String, Boolean> mapPast_EachURL_isRSS_Result=new TreeMap();
			String option="n";
			//
			if(mapPastRunResults.size()>0){
				for(String currURL:mapPastRunResults.keySet()){
					System.out.println("currURL:"+currURL);
					currURL=currURL.replace("error:!!!","");  //replace first column error
					
					if(currURL.length()<2) //empty
						continue;
					String[] s = currURL.split("!!!");
					System.out.println("pastRunResults:"+s[0]+"!!!"+s[1]);
					mapPast_EachURL_isRSS_Result.put(s[0], Boolean.valueOf(s[1]));
				}
			}
			//
			mapURL_eachLine=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
					readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
																		(inFile
																		,start_line
														    			,end_line 
																		,""//outFile
																		,false // is_Append_outFile
																		,false // is_Write_To_OutputFile
																		,"" //debug_label
																		,-1 // token for primary key 
																		,true //boolean isSOPprint
																		);
			
			int total=mapURL_eachLine.size();
			System.out.println("Map of PastRunResults.size:"+mapPastRunResults.size());
			System.out.println("Map of input file (total URL given):"+total);
			option="n";
			// continue
 			option=Crawler.read_input_from_command_line_to_continue_Yes_No(br);
 			
 			if(!option.equalsIgnoreCase("y"))
 			   	  System.exit(1);
			
			int cnt=0; String url2="";
			//
			for(String currline:mapURL_eachLine.keySet()){
				currline=currline.replace("errror:!!!", "");
				System.out.println("currline:"+currline);
				String []token=currline.split("!!!");
				if(token.length<2) continue;
				
				String url=token[0];
				 //
				 if(url.indexOf("#")>=0){
					url=url.substring(0, url.indexOf("#")); 
				 }
				
				url2=token[1];
				
				//second url
				//url2=mapURLs.get(url);
				url2=url2.toLowerCase();
				// has year
				if(url2.indexOf("2016")>=0 ||url2.indexOf("2017")>=0 ||
						url.indexOf("2015")>=0 || url.indexOf("2014")>=0|| url.indexOf("2013")>=0
						  || url.indexOf("2012")>=0|| url.indexOf("2011")>=0|| url.indexOf("2010")>=0
						  || url.indexOf("2009")>=0|| url.indexOf("2008")>=0|| url.indexOf("2007")>=0
						  || url.indexOf("2006")>=0|| url.indexOf("2005")>=0|| url.indexOf("2004")>=0
						  || url.indexOf("2003")>=0|| url.indexOf("2002")>=0|| url.indexOf("2001")>=0
						  || url.indexOf("2000")>=0|| url.indexOf("1999")>=0|| url.indexOf("1998")>=0){
					System.out.println("url is an year ..url2:"+url2);
					continue;
				}
				
				//has year means its not a rss 
				if(url2.indexOf("2016")>=0 ||url2.indexOf("2017")>=0 ||
				   url2.indexOf("2015")>=0 || url2.indexOf("2014")>=0|| url2.indexOf("2013")>=0
				  || url2.indexOf("2012")>=0|| url2.indexOf("2011")>=0|| url2.indexOf("2010")>=0
				  || url2.indexOf("2009")>=0|| url2.indexOf("2008")>=0|| url2.indexOf("2007")>=0
				  || url2.indexOf("2006")>=0|| url2.indexOf("2005")>=0|| url2.indexOf("2004")>=0
				  || url2.indexOf("2003")>=0|| url2.indexOf("2002")>=0|| url2.indexOf("2001")>=0
				  || url2.indexOf("2000")>=0|| url2.indexOf("1999")>=0|| url2.indexOf("1998")>=0
				  || url2.indexOf("+")>=0|| url2.indexOf("wordpress")>=0
				  || url2.indexOf("sport")>=0|| url2.indexOf("cricket")>=0
				  || url2.indexOf("video")>=0 || url2.indexOf("utm_source")>=0
				  || url2.indexOf("finance-")>=0|| url2.indexOf("business-")>=0
						){
					System.out.println("continue on url2..");
					continue;
				}
				
				System.out.println("input: url:"+url+" url2:"+url2);
				//System.out.println("inFile:"+inFile+" pastRunResultsFile:"+pastRunResultsFile);
				
				// if url2 DONT have HTTP
				if(url2.indexOf("http") == -1 ){
					//get domain name
					String domain_name=Find_domain_name_from_Given_URL.find_domain_name_from_Given_URL(url, false)+"/";
					//
					url2=domain_name+url2;
					//
					url2=url2.replace("//", "/");
					// RSS usually has this pattern->.php, rss.php, .rss2, =rss, rss2.0.xml, ?rss, .ssf, .cms, rss. , .rss , .xml, feeds., ends with /feed /rss
				}
				// BEGIN URL2
				else if (url2.indexOf("http") >= 0 ){
				}
				 
				
				//BEGIN URL2
				   //url not tested for isRSS in past
				   if(!mapPast_EachURL_isRSS_Result.containsKey(url2)){
					   //if(is_RSS.isRSS(url2)){
					   if(ReadRSS_Document.readRSS_Document(url2)){
						   //
						   writer.append(url2+"\n");
						   writer.flush();
						   //
						   writer_pastRunResultsFile.append("\n"+url2+"!!!true");
						   writer_pastRunResultsFile.flush();
						   mapPast_EachURL_isRSS_Result.put(url2, true);
						   System.out.println( cnt+" true"+" (out of "+ total+")"+ url2);   
					   }
					   else{
						   writer_pastRunResultsFile.append("\n"+url2+"!!!false");
						   writer_pastRunResultsFile.flush();
						   mapPast_EachURL_isRSS_Result.put(url2, false);
						   System.out.println( cnt+" false"+" (out of "+ total+")"+ url2);
					   }
				   }
				   else if(mapPast_EachURL_isRSS_Result.containsKey(url2)){
					   //
					   if(mapPast_EachURL_isRSS_Result.get(url2)==true){
						   System.out.println( cnt+" true"+" (out of "+ total+")"+ url2 +" ALREADY FOUND");   
						   writer.append(url+"\n");
						   writer.flush();
					   }
					   else{
						   System.out.println("cnt:"+cnt+" Found from past** run that it is FALSE for "+url);
					   }
				   }
				 // END URL2	

				
				//
				if(isSOPprint)
					System.out.println("-----------start------verify--rss-----");
			   cnt++;
//			   if(url.indexOf("rss")==-1
//				 || url.indexOf("feed")==-1)
//				   continue;

			   
			   // BEGIN URL //
			   //url not tested for isRSS in past
//			   if(!mapPast_EachURL_isRSS_Result.containsKey(url)){
//				   //if(is_RSS.isRSS(url)){
//				   if(readRSS_Document.readRSS_Document(url)){
//					   //
//					   writer.append(url+"\n");
//					   writer.flush();
//					   
//					   writer_pastRunResultsFile.append("\n"+url+"!!!true");
//					   writer_pastRunResultsFile.flush();
//					   System.out.println( cnt+" true"+" (out of "+ total+")"+ url);   
//				   }
//				   else if(!readRSS_Document.readRSS_Document(url)){
//					   writer_pastRunResultsFile.append("\n"+url+"!!!false");
//					   writer_pastRunResultsFile.flush();
//					   System.out.println( cnt+" false"+" (out of "+ total+")"+ url);
//				   }
//			   }
//			   else if(mapPast_EachURL_isRSS_Result.containsKey(url)){
//				   //
//				   if(mapPast_EachURL_isRSS_Result.get(url)==true){
//					   writer.append(url+"\n");
//					   writer.flush();
//				   }
//				   else{
//					   System.out.println("cnt:"+cnt+" Found from past** run that it is FALSE for "+url);
//				   }
//			   }
			   // END URL
			   
			   rs.gc();//garbage
			   
			   if(isSOPprint)
				   System.out.println("---------------end---------");
			} // END for(String url:mapURLs.keySet()){
			writer.close();
			writer_pastRunResultsFile.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}
	
	
	// main
	public static void main(String[] args) throws Exception{
		String url="http://mf.feeds.reuters.com/reuters/UKSportsNews";
		url="http://www.thehindu.com/news/cities/chennai/?service=rss";
		url="http://mf.feeds.reuters.com/reuters/UKMotorSportsNews";
		url="http://mf.feeds.reuters.com/reuters/UKSportsNews";
		url="http://docs.oracle.com/javase/7/docs/api/javax/xml/parsers/DocumentBuilder.html";
		url="http://timesofindia.feedsportal.com/c/33039/f/533916/index.rss";
		url="http://www.opensocietyfoundations.org/topics/black-male-achievement/feed";
		//isAtom(url);
		
		//isRSS(url);
		
		Find_is_RSS.find_is_RSS(url);
		
		String inFile=
				"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup.txt";
		
		String outFile=
				"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_isRSS.txt";
		
		String outPastResultsFile=
				"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged._pastResults.txt";
		int start_line=20000;
		int end_line  =60000;
		 
		
		
		
		//NOTE: INPUT is second file (2.xxxxx) from http_getLinks() or http_getWikiPageLinks.http_getLinks()
//		readFile_verify_is_RSS.
//		readFile_verify_is_RSS(inFile,
//							   start_line,
//							   end_line,
//							   outFile,
//							   false,//is_Append_outFile
//							   outPastResultsFile,
//							   true //is_Append_pastRunResultsFile
//							   );
		
		
		
		
	}
	

}
