package crawler;

import java.io.IOException;

public class Find_clean_get_last_pattern_from_a_string_having_multiple_pattern {
	
	//example:
	//multi_url_string="http://www.thehindu.com/thehindu/2000/01/07/http://www.thehindu.com/thehindu/2000/01/07/stories/02070001.htm",
	//pattern="http"
	public static String find_clean_get_last_pattern_from_a_string_having_multiple_pattern(String multi_url_string,
																					  String pattern //pattern (ex: http)
																					  ){
		String orig_multi_url_string=multi_url_string;
		System.out.println("clean_get_last_pattern_from_a_string_having_multiple_pattern");
		try{
			 
			//
			while(Find_occurance_Count_Substring.find_occurance_Count_Substring("http" , multi_url_string ) > 1 ){
				  
				int first=multi_url_string.indexOf("http");
				
				multi_url_string=multi_url_string.substring(multi_url_string.indexOf("http", first+1 ), 
															multi_url_string.length());
				System.out.println("multi_url_string:"+multi_url_string);
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return multi_url_string;
	}
	
	//main
	public static void main(String[] args) throws IOException {
		
				String t=
					find_clean_get_last_pattern_from_a_string_having_multiple_pattern(
														"http://www.thehindu.com/thehindu/2000/01/07/http://www.thehindu.com/thehindu/2000/01/07/stories/02070001.htm",
														"http"
														);
	
					System.out.println("clean:"+t);
	
		
	}

}
