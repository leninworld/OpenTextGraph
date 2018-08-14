package crawler;

public class Remove_commas_etc {
	
    // remove_commas_etc
    public static String remove_commas_etc(String inStrin){
    	inStrin=inStrin.replace(";"," ").replace(","," ").replace(":"," ")
    			.replace("'"," ").replace("\"", " ").replace("’", " ")
    			.replace("-", " ").replace("”", " ").replace("“", " ")
    			.replace("—", " ").replace("(", " ").replace(")", " ")
    			.replace("?", " ").replace("]"," ").replace("["," ")
    			.replace("("," ").replace(")"," ").replace("-", " ")
    			.replace("  ", " ").replace("  ", " ");
    	return inStrin;
    }

}
