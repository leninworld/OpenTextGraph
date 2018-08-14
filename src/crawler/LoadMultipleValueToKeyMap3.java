package crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class LoadMultipleValueToKeyMap3 {
	
	
	// method will take <key|||value1|||value2> output-> map<key,value1> , map<key,value2)
	public static TreeMap<Integer, TreeMap<String,String>> LoadMultipleValueToKeyMap3(
														String filename,
														String delimiter
														) {
		
		TreeMap<Integer, TreeMap<String,String>> mapOut=new TreeMap<Integer, TreeMap<String,String>>();
		String line = "";
		String key = "", url = "";
		String value = "";
		int count = 0;
		
		TreeMap<String, String> urlMap=new TreeMap<String, String>();
		TreeMap<String, String> truthMap=new TreeMap<String, String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			//
			while ((line = reader.readLine()) != null) {
				 
				String [] s= line.split(delimiter);
				
				//System.out.println(s.length + "--"+line);
				
				if(s.length>=2)
					urlMap.put( s[0] , s[1]);
				
				if(s.length>=3)
					truthMap.put(s[0], s[2]);
				 
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mapOut.put(1, urlMap);
		mapOut.put(2, truthMap);
		
		return mapOut;
	}

	public static void main(String[] args) {
		String baseFolder_out="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/";
		String outFile=baseFolder_out+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb_NUMBER_LINENO.txt_LINENO_NumberCSV_RelatedWordCSV.txt";

		System.out.println(outFile);
		
		TreeMap<Integer, TreeMap<String,String>> h=LoadMultipleValueToKeyMap3(outFile,
																			 "!!!"
																			 );
		
		System.out.println(h.get(1).size());
		System.out.println(h.get(2).size());
		
	}
	

}
