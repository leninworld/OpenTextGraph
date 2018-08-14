package crawler;
import java.io.IOException;
//reverse string
public class ReverseString {
	// reverse a string
	public static String reverseString(String source) {
	    int i, len = source.length();
	    StringBuffer dest = new StringBuffer(len);

	    for (i = (len - 1); i >= 0; i--)
	      dest.append(source.charAt(i));
	    return dest.toString();
	  }
	
	public static void main(String[] args) throws IOException {
		
		
	}
	
}
