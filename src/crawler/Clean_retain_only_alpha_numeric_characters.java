package crawler;

public class Clean_retain_only_alpha_numeric_characters {
	
	//clean_retain_only_alpha_numeric_characters
	public static String clean_retain_only_alpha_numeric_characters(
														String 	inString,
														boolean 	is_Convert_to_lowercase
														){
		
		//
		if(is_Convert_to_lowercase)
			return inString.replaceAll("[^a-zA-Z0-9]+","").toLowerCase();
		else
			return inString.replaceAll("[^a-zA-Z0-9]+","");
	 
	}
	
	// main
    public static void main (String[] args) throws java.lang.Exception{
    	String s=" A third officer received a two-year sentence for extorting money from the woman's fiance";
    	
    	s="\"Inspectors have uncovered widespread irregularities and suspected corruption among military units based around Beijing, China's Defense Ministry said Tuesday, a sign that a widening anti-graft campaign that is turning to the sprawling 2";
    	System.out.println(clean_retain_only_alpha_numeric_characters(s, true));
    	
    	
    }

}
