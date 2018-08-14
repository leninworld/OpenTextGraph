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
	public class Find_Start_n_End_Index_Of_GivenString {
		 //mapOut = new TreeMap<Integer, String>();
		
		// Extracts set of lines that matches a pattern from start_tag and end_tag
		// getWikiPageLinks.getLinks does similar things (but few custom modification done to suit for removing junk links)
		public static TreeMap<Integer, String>  getMatchedLines(String inString, 
										String start_tag, String end_tag,
										boolean isDebug){
			int cnt=0;
			TreeMap<Integer, String> mapOut = new TreeMap<Integer, String>();
			System.out.println("getMatchedLines() running..");
			System.out.println("start_tag:"+start_tag+";inString.indexOf(start_tag):"+inString.indexOf(start_tag));
			//
			if(inString.indexOf(start_tag)==-1){
				System.out.println("not found");
				mapOut.put(1, "-1");
			}
			else{
				while (inString.indexOf(start_tag) >= 0) {
					if(isDebug)
					System.out.println("getmatchedlines.cnt:"+cnt);
					cnt++;
					// <p class=""> </p>
					int startIndex = inString.indexOf(start_tag);
					int endIndex = inString.indexOf(end_tag, startIndex+start_tag.length());
					if(startIndex==-1 || endIndex==-1) break;
					if(isDebug){
						//System.out.println(startIndex+" "+endIndex+";s.tag:"+start_tag+";inString:"+inString);
					}
					
					//
					if(!mapOut.containsValue(inString.substring(startIndex , endIndex) ))
						mapOut.put(cnt, inString.substring(startIndex , endIndex) );
					
					inString=inString.substring(startIndex+start_tag.length(), inString.length());
				}
			} // end
			return mapOut;
		}
		// main()
		public static void main(String[] args) {
			
			if (args.length >=10) {
				System.out.println("Incorrect input provided..!! Please provide proper folder name");
				System.out.println("Correct Usage is: java MergeFiles <input folder name>");
				return;
			}
		  try{
				String WSbaseFolder="/Users/lenin/Downloads/Feeds-5-Jun-.dummy1/";
				 
				String fileName="Breaking.10k";
				
				WSbaseFolder="/Users/lenin/Downloads/output/";
				fileName="dum.uptominute.output1.txt";
				//step 1: //read a file
				String concLine=ReadFile_ouput_concLine.readFile_ouput_concLine(
								WSbaseFolder+fileName
						);
			  
			  
			//String inputFolderName = args[0];
			String outputFileName = "";
			// extract a pattern
			TreeMap<Integer, String> getMap=
					getMatchedLines("http://timesofindia.indiatimes.com/2014/12/29/archivelist/year-2014,month-12,starttime-42002.cms"
									+ "@!3232http://timesofindia.indiatimes.com/2014/12/29/archivelist/year-2012.cms",
									"http",".cms", false);
			System.out.println(getMap.size());
			// read the map
			for(int i:getMap.keySet()){
				System.out.println(i+" "+getMap.get(i));
			}
			
		  }
		  catch(Exception e)
		  {
			  System.out.println("An exception occurred while calling Write method ..!!");	
			  e.printStackTrace();
		  }
					
		}
			 
}
