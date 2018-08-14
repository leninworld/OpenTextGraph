package crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class LoadMultipleValueToMap2_AS_KEY_VALUE {
	

	// load inputFile <KEY!!!VALUE> to a TreeMap<KEY,VALUE>
	public static TreeMap<String, String> LoadMultipleValueToMap2_AS_KEY_VALUE( 
															String filename,
															String delimiter,
															String index_of_key_AND_value_as_CSV,
															String append_suffix_at_each_line,
															boolean is_have_only_alphanumeric,
															boolean isSOPprint
															) {
		String line = "";
		String key = "", correctAbbr = "";
		String value = "";
		TreeMap<String, String> map_key_value=new TreeMap<String, String>();
		int count = 0;
		String [] index=new String[2];
		try {
			System.out.println("inside LoadMultipleValueToMap2_AS_KEY_VALUE->" +filename);
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			int lineNumber=0;
			
			index=index_of_key_AND_value_as_CSV.split(",");
			int index_key= Integer.valueOf(index[0])-1;
			int index_value=Integer.valueOf(index[1])-1;
			
			int max_index =0;
			if(index_key >=index_value) 
					max_index=index_key;
			else
					max_index=index_value;
			
			while ((line = reader.readLine()) != null) {
					
					line=line+append_suffix_at_each_line;
				
					lineNumber++;
					///debug
					//if(lineNumber>1) break;
					
					String s[] =line.split(delimiter);
					
					if(lineNumber <=10 )
						System.out.println("insidekeywvalue: line:"+line+" s.len:"+s.length +" max_index:"+max_index 
											+" index_of_key_AND_value_as_CSV:"+index_of_key_AND_value_as_CSV);
					count = 1; 
					//System.out.println("lineNumber->"+lineNumber);
					if(s.length<2 ||  s.length < max_index+1  ) {
						System.out.println("NO sufficient tokens; given file:"+filename);
						continue;
					}
					
					if(s[0].length()<1 && s[1].length()<1)
						continue;
					
					if(isSOPprint)
						System.out.println("LoadMultipleValueToMap2_AS_KEY_VALUE:key,value-> "+s[index_key]+" " +s[index_value]);
					
					if(is_have_only_alphanumeric){
						
						map_key_value.put(
								Clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters(s[index_key],false),
								Clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters(s[index_value],false)
						);
					}
					else{
						map_key_value.put(s[index_key].trim(), s[index_value].trim());
					}
				    
					count++;
					// writerDebug.flush();
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map_key_value;
	}
	
	///
	// load inputFile <KEY!!!VALUE> to a TreeMap<KEY,VALUE>
	public static TreeMap<String, String> LoadMultipleValueToMap2_AS_KEY_VALUE_REVERSE( 
															String filename,
															String delimiter,
															boolean is_have_only_alphanumeric,
															boolean isSOPprint
															) {
		String line = "";
		String key = "", correctAbbr = "";
		String value = "";
		TreeMap<String, String> map_key_value=new TreeMap<String, String>();
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			int lineNumber=0;
			while ((line = reader.readLine()) != null) {
					lineNumber++;
					///debug
					//if(lineNumber>1) break;
					
					String s[] =line.split(delimiter);
					count = 1; 
					//System.out.println("lineNumber->"+lineNumber);
					if(s.length<2) continue;
					if(s[0].length()<1 && s[1].length()<1)
						continue;
					
					if(isSOPprint)
						System.out.println("LoadMultipleValueToMap2_AS_KEY_VALUE:key,value-> "+s[0]+" " +s[1]);
							
					
					if(is_have_only_alphanumeric){
						
						map_key_value.put(
								Clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters(s[1],false),
								Clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters(s[0],false)
						);
					}
					else{
						map_key_value.put(s[1].trim() , s[0].trim());
					}
					
					count++;
					// writerDebug.flush();
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map_key_value;
	}
	
	
	
	public static void main(String[] args) {
		String baseFolder_out="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/tf-idf/";
		String outFile=baseFolder_out+"tf-idf.txt_Word_stemmedWord.txt";
		
		
		TreeMap<String, String> map=
									LoadMultipleValueToMap2_AS_KEY_VALUE(
																		outFile,
																		"!!!",
																		"1,2",
																		 "", //
																		true , //is_have_only_alphanumeric
																		true //isSOPprint
																		);
		
		
		System.out.println(map.get("financially"));
		
		
	}

}
