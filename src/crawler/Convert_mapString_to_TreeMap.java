package crawler;

import java.util.TreeMap;

public class Convert_mapString_to_TreeMap {
	
	// convert map String to TreeMap
	public static TreeMap<String, String> convert_mapString_to_SS_TreeMap(String mapString,
			  															  String  dictionary_name_value_splitter, // {": ",":","="} 
																		  boolean is_print_SOP
																		  ){
		
		TreeMap<String, String> map_each_q_topic_freq=new TreeMap<String, String>();
		mapString=mapString.replace(", ", ",").replace("{", "").replace("}", "");
		String [] s=mapString.split(",");
		int c=0;
		//
		while(c<s.length){
			if(s[c].indexOf("null")>=0) {c++; continue;}
			//System.out.println("s[c]:"+s[c]);
			String s2[]=s[c].split(dictionary_name_value_splitter);
			// second token might be blank, that also throws error
			if(s2.length>=2)
				map_each_q_topic_freq.put(s2[0] , s2[1] );
			c++;
		}
		
		if(is_print_SOP)
			System.out.println("converted:"+map_each_q_topic_freq);
		return map_each_q_topic_freq;
	}

}
