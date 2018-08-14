package crawler;

import java.io.IOException;

public class IsInteger {
	
	public static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	//
	public static boolean isInteger(String s) {
		
	    return isInteger(s,10);
	}
	//main
	public static void main(String[] args) throws IOException {
		   System.out.println( isInteger("100",10));
		
	}
}
