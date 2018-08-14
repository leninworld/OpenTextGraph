package crawler;
/*
 
 * 
 * */ 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.TreeMap;
	//find a set of pattern (use*** getWikiPageLinks.getLinks() )
	public class Find_domain_name_from_Given_URL {
 
		public static String find_domain_name_from_Given_URL(
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
		// main()
		public static void main(String[] args) {
			
			if (args.length >=10) {
				System.out.println("Incorrect input provided..!! Please provide proper folder name");
				System.out.println("Correct Usage is: java MergeFiles <input folder name>");
				return;
			}
		  try{
				String url=
						"http://www.bruegel.org/nc/blog/detail/article/1669-in-bad-faith/pages/rssfeedscareers/";
				url="http://www.ceps.be/research-areas/energy-and-climate-change/content/ceps-rss-links/research-areas/rights";
				url="http://www.opensocietyfoundations.org/feeds";
				url="http://www.opensocietyfoundations.org/topics/black-male-achievement/feed";
				// find domain name
				Find_domain_name_from_Given_URL.
				find_domain_name_from_Given_URL(url, true); 
			
		  }
		  catch(Exception e)
		  {
			  System.out.println("An exception occurred while calling Write method ..!!");	
			  e.printStackTrace();
		  }
					
		}
			 
}
