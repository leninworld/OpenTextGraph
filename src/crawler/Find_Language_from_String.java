package crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

import me.champeau.ld.UberLanguageDetector;

public class Find_Language_from_String {
	
	// find if it is english language or not (NOT SURE IT IS WORKING TO MARK)
	public static boolean find_Language_from_String_2(String text1,
													  String debugFile){
		
		TreeMap<Boolean, Integer> mapCnt=new TreeMap();
		  
		try {
			FileWriter writerDebug=new FileWriter(debugFile, true);
	
			System.out.println("\n----text1:\n-------------------");
			//System.out.println(text1);
			System.out.println("text1:\n-------------------");
			String start_tag="####*";
			String end_tag="####*";
			
			
			TreeMap<Integer, String> mapOut2=
			Find_Start_n_End_Index_Of_GivenString.getMatchedLines
						(text1, start_tag, end_tag, false);
		
			  System.out.println(mapOut2.get(1).replace("#", " ") );
			  
			  int cnt=1;
			  int max=8;
		
			  try{
				  while(cnt<=max){
					  
					  if(!mapCnt.containsKey(Find_charsetdecoder.isPureAscii(mapOut2.get(cnt).replace("#", " "))))
					  {
					  mapCnt.put( 
							  Find_charsetdecoder.isPureAscii(mapOut2.get(cnt).replace("#", " ")),
							  1);
					  }
					  else{
						  int cnt2=mapCnt.get(Find_charsetdecoder.isPureAscii(mapOut2.get(cnt).replace("#", " ")));
						  mapCnt.put(Find_charsetdecoder.isPureAscii(mapOut2.get(cnt).replace("#", " ")), 
								  	 cnt2+1);
						  
					  }
						  
					  cnt++;
				  }
			  }
			  catch(Exception e){
				  
			  }
			  System.out.println("mapCnt:"+mapCnt+"; mapOut2.size:"+mapOut2.size());
			  writerDebug.append("mapCnt:"+mapCnt+"; mapOut2.size:"+mapOut2.size());
			  writerDebug.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
		int true_cnt=0;
		int false_cnt=0;
		if(mapCnt.containsKey(true))
			true_cnt=mapCnt.get(true);
		if(mapCnt.containsKey(false))
			false_cnt=mapCnt.get(false);
		
		if(true_cnt>false_cnt){
			System.out.println("yes english");
			return true;
		}
		System.out.println("NOT english");
		return false;
	}
	
	// find language from string  (GOOD)
	public static String find_Language_from_String(String inputText,
												  boolean isSOPprint
													){
		UberLanguageDetector detector = UberLanguageDetector.getInstance();
		String language="";
 
		try {
			// ..
			 
			language = detector.detectLang(inputText);
			
			if(isSOPprint)
				System.out.println("language:"+language);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return language;
	}
	//main
	public static void main(String[] args) throws IOException {
		String url="";
		url="http://carnegieendowment.org/experts/?fa=437";
		url="http://carnegie-mec.org/experts/?fa=915";
		url="https://www.deutschland.de/de";
		url="http://www.epa.gov/espanol/";
		url="http://www.usip.org/publications/2015/06/19/help-afghanistan-survive-narrow-the-focus";
		url="http://www.transparency.org/country/";
		url="http://www.sipri.org/newsletter/june15";
		url="http://www.wilsoncenter.org/experts-list/25";
		url="http://carnegie-mec.org/experts/?fa=1029";
		
		url="";
		String debugFile="";
		
		System.out.println(Find_Language_from_String.find_Language_from_String_2("hello",
																				  debugFile
																				  )
																				  );
	}

}
