package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

public class ReadFile_convert_from_url_text_to_opml_format {
	
	// convert text to multiple opml format
	public static TreeMap<String, String> readFile_convert_from_url_text_to_multiple_opml_format(
																 String 	inFile,
																 String 	outFile,
																 boolean 	is_Append_outFile,
																 int 	 	startline,
																 int 	 	endline,
																 long 		param_chunkSize
																 ) {
		TreeMap<String, String> mapOut = new TreeMap();
		long chunkSize2 = 2000000*5;
	 	//2mb = 2000000 
		
		if(param_chunkSize>0)
			chunkSize2=param_chunkSize;
		 
		System.out.println("inFile:"+inFile);
		System.out.println("outFile:"+outFile);
		try {
			// step 1  split chunk
			mapOut=
			ReadFile_Split_One_into_Many_by_Size.split_by_size(inFile,
																chunkSize2);
			int cnt=0;
			for(String currFile:mapOut.keySet()){
				cnt++;
				System.out.println("chunk writing:"+currFile+"_"+cnt+"_.opml");
				//step 2
				readFile_convert_from_url_text_to_opml_format(
														 	 	currFile,
														 	 	currFile+"_"+cnt+"_.opml",
														 	    is_Append_outFile,
														 	 	startline,
														     	endline
														 		);
			}
			System.out.println("size:"+mapOut.size());
			System.out.println("mapOut:"+mapOut);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mapOut;
	}
	
	//  convert from Text file to OPML
	public static TreeMap<String, String> readFile_convert_from_url_text_to_opml_format(
																					 String 	inFile,
																					 String 	outFile,
																					 boolean is_Append_outFile,
																					 int 	 startline,
																					 int 	 endline
																					 ) {
	TreeMap<String, String> mapOut = new TreeMap();
	String line = ""; int counter=0;
	FileWriter writer= null;
	// Read the mapping to drill-down
	HashMap<String, String> mapDrillDown = new HashMap();
	int newcounter=0;
	try {
	System.out.println("outFile:"+outFile);
	
	writer= new FileWriter(new File(outFile), is_Append_outFile);
 
	BufferedReader reader = new BufferedReader(new FileReader(inFile));

	// 	read each line of given file
	while ((line = reader.readLine()) != null) {
			counter++;
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

			line=line.replace("https", "http");
			
			//  & <- skip (it causes opml format not working)
			if(line.indexOf("&")>=0
				|| line.indexOf("</")>=0){
				continue;
			}
			
			if(line.indexOf("http")==-1
					|| line.length()<=2
					|| line.indexOf("http")>3
					|| line.indexOf("http:")==-1){
					System.out.println("skip:" +line+":"+line.indexOf("http"));
					continue;
			}
			 
			
			if(		line.indexOf("http:\\\\")==-1 
				&& 	line.indexOf("http:\\")  >=0 ){
					line=line.replace("http:\\", "http:\\\\");
					 
				}
		 
			
			mapOut.put(line, line); //remove duplicates
	}
	
	
			System.out.println("url map count:"+mapOut.size());
	
//		writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+"\n");
//		writer.append("<opml xmlns:rssowl=\"http://www.rssowl.org\" version=\"1.1\">"+"\n");
//		writer.append("<head>"+"\n");
//		writer.append("<title>RSSOwl Subscriptions</title>"+"\n");
//		writer.append("<dateModified>Sun, 18 Jan 2015 00:39:01 CST</dateModified>"+"\n");
//		writer.append("</head>"+"\n");
//		writer.append("<body>"+"\n");
		
		
		writer.append("<opml version=\"1.0\" xmlns:fz=\"urn:forumzilla:\">"+"\n");
		writer.append("<head>"+"\n");
		writer.append("<title>Thunderbird OPML Export - US TT</title>"+"\n");
		writer.append("<dateCreated>Mon, 29 Jun 2015 20:39:57 GMT</dateCreated>"+"\n");
		writer.append("</head>"+"\n");
		writer.append("<body>"+"\n");
	
    	// <outline text= xmlUrl= rssowl:id= >
		int text_id=0;
 
	
		//write to output file
		for(String url:mapOut.keySet()){
  
				text_id++;
				String domain=Find_domain_name_from_Given_URL.find_domain_name_from_Given_URL(url, false);
				String tmp="<outline title=\""+ domain +"\">"
						+ "<outline type=\"rss\" title=\""+domain+
									"-rnd:"+ Generate_Random_Number_Range.generate_Random_Number_Range(1,1000000)+"-"+  "\""
						+ " text=\""+domain+
						 		    "-rnd:"+Generate_Random_Number_Range.generate_Random_Number_Range(1,1000000)+"-"
						 		    +"\" version=\"RSS\""
						+ " fz:quickMode=\"true\"  fz:options=\"{&quot;version&quot;:1,&quot;category&quot;:{&quot;enabled&quot;:false,&quot;prefixEnabled&quot;:true,&quot;prefix&quot;:&quot;t&quot;}}"
							+"\""
						+" xmlUrl=\""+url+"\""
						+" htmlUrl=\""+url+"\""+"/>"
						+"</outline>"

						;
				
				//<outline title="Zee News South asia">
			    //<outline type="rss" title="Zee News :South asia" text="Zee News :South asia" version="RSS"
			    //fz:quickMode="true" fz:options="{&quot;version&quot;:1,&quot;category&quot;:{&quot;enabled&quot;:false,&quot;prefixEnabled&quot;:true,&quot;prefix&quot;:&quot;t&quot;}}" 
				//xmlUrl="http://zeenews.india.com/rss/south-asia-news.xml" htmlUrl="http://zeenews.india.com"/>
				//</outline>
				
				System.out.println( Find_domain_name_from_Given_URL.find_domain_name_from_Given_URL(url, true)						
									+"!!!" + Generate_Random_Number_Range.generate_Random_Number_Range(1, 100)
									+"!!!"+tmp );
				
				
				
				writer.append(tmp+"\n");
				writer.flush();
		}
			
		writer.append("</body>" +"\n");
		writer.append("</opml>"+"\n");
		writer.flush();

		System.out.println("url map count:"+mapOut.size());
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	
	
		//main
		public static void main(String[] args) throws IOException {
			String baseFolder="";
			TreeMap<String,String> mapout= new TreeMap<String, String>();
			long chunkSize2 = 2000000;
		 	//2mb = 2000000 
			
			//test();
//			 readFile_convert_from_url_text_to_opml_format(
//					 				"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/us.tt.isRSS.all/notExists.merge.pastResults.isRSS_true_.all.nodup.txt.SET.4..txt",
//					 			    "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/us.tt.isRSS.all/notExists.merge.pastResults.isRSS_true_.all.nodup.SET.4..txt.opml",
//									true, //is_Append_outFile
//									-1, 
//									-1
//									);
			 
//			 readFile_convert_from_url_text_to_multiple_opml_format(
//						"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/us.tt.isRSS.all/merge.pastResults.isRSS_true_.all.nodup.txt",
//						"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/us.tt.isRSS.all/merge.pastResults.isRSS_true.opml",
//						true, //is_Append_outFile
//						-1, 
//						-1,
//						-1 //chunkSize2
//						);
			 
			//
			for(String k:mapout.keySet()){
			  	System.out.println(k);
			}
			baseFolder="/Users/lenin/Downloads/OPML (2-May)-thunderbird/tmp/";
			String inFile_1=baseFolder+"thunderbird.opml";
			String inFile_2=baseFolder+"1.opml";
			String outFile=baseFolder+"out_mismatch.txt";
//
			ReadFile_reading_2_OPML_files_output_MisMatch.
			readFile_reading_2_OPML_files_output_MisMatch(inFile_1,
														  inFile_2,
														  outFile
														);
			
			System.out.println(mapout.size());
			System.out.println("http:\\");
			System.out.println(	 "http:\\".replace("http:\\", "http:\\\\")  );
			
			
		}

	

}
