package crawler;

public class Find_occurance_Count_Substring {
	 
		// fidn occurance 
		public static int find_occurance_Count_Substring(String subStr, String str){
			return (str.length() - str.replace(subStr, "").length()) / subStr.length();
		}
  
	
	public static void main(String[] args){
		System.out.println(find_occurance_Count_Substring("^", "{-64fed3:^80 {(k^98:c!!!9)680:m } 64fed3 }"));
		System.out.println(find_occurance_Count_Substring("abab", "ababababab"));
		System.out.println(find_occurance_Count_Substring("a*b", "abaabba*bbaba*bbab"));
	}
}