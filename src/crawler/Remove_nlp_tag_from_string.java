package crawler;

public class Remove_nlp_tag_from_string {
	
	//
	public static String remove_nlp_tag_from_string(String inString){
		
		return inString.substring(0, inString.indexOf("_"));
	}
	
	// main
		public static void main(String[] args) {
			System.out.println(remove_nlp_tag_from_string("test_nn"));
		}
	
	

}
