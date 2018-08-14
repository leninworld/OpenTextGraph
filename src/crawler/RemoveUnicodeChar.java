package crawler;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.util.TreeMap;
 
// http://www.ascii.cl/htmlcodes.htm
public class RemoveUnicodeChar {
	// 
	public static String removeUnicodeChar(String input){
		try{
			
			// http://www.ascii.cl/htmlcodes.htm
			input=input.replaceAll("[\u0000-\u001f]", "").replaceAll("\\p{Cntrl}", "").replace("&#", "&")
						.replace("\\", "").replace("/","")
                                .replace("&8211;", " ")
                                .replace("&8212;", " ")
                                .replace("&#8212", " ")
                                .replace("&8216;", " ")
                                .replace("&#8212", " ")
                                .replace("&8217;", " ")
                                .replace("&8218;", " ")
                                .replace("&8220;", " ")
                                .replace("&8221;", " ")
                                .replace("&8222;", " ")
                                .replace("&8224;", " ")
                                .replace("&8225;", " ")
                                .replace("&8226;", " ")
                                .replace("&8230;", " ")
                                .replace("&8240;", " ")
                                .replace("&8364;", " ")
                                .replace("&8482;", " ")
                                .replace("&8243;", " ")
                                .replace("o&039", " ")
                                .replace("&x85;", " ")
                                .replace("&x91;", " ")
                                .replace("&x92;", " ")                                
                                .replace("&x93;", " ")
                                .replace("&x94;", " ")
                                .replace("&x95;", " ")
                                .replace("&x96;", " ")
                                .replace("&x97;", " ")
                                .replace("&x98;", " ")
                                .replace("&x99;", " ")
                                .replace("&xa0;", " ")
                                .replace("&xe0;", " ")
                                .replace("&xe1;", " ")
                                .replace("&xe2;", " ")
                                .replace("&xe3;", " ")
                                .replace("&xe4;", " ")
                                .replace("&xe5;", " ")
                                .replace("&xe6;", " ")
                                .replace("&xe7;", " ")
                                .replace("&xe8;", " ")
                                .replace("&xe9;", " ")
                                .replace("&xea;", " ")
                                .replace("&xeb;", " ")
                                .replace("&xec;", " ")
                                .replace("&xed;", " ")
                                .replace("&xee;", " ")
                                .replace("&xef;", " ")
                                .replace("&xf1;", " ")
                                .replace("&xf2;", " ")
                                .replace("&xf3;", " ")
                                .replace("&xf4;", " ")
                                .replace("&xf5;", " ")
                                .replace("&xf6;", " ")
                                .replace("&xf7;", " ")
                                .replace("&xfc;", " ")
                                .replace("&xdf;", " ")
                                .replace("&xa7;", " ")
                                .replace("&lt;br&gt;", " ")
                                .replace("&160 ", " ")
                                .replace("&rdquo", " ")
                                .replace("rdquo", " ")
                                .replace("&ldquo", " ")
                                .replace("ldquo", " ")
                                .replace("&nbsp", " ")
                                .replace("&rsquo;", " ")
                                .replace("&mdash", " ")
                                .replace("*", " ")
                                .replace("-", " ")
                                .replace("  ", " ")
                                ;
	   
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return input;
	}
	 
	 
	
	//main
	public static void main(String[] args) throws IOException {
		TreeMap<String,String> mapout= new TreeMap<String, String>();
 
		String a= "jer\u00e9 \\/longman";
		//a="body\/head (music group)";
		System.out.println(a.replace("/\\", "").replace("//", "").replaceAll("(\\r|\\n)", "") )	;
		String str= a.replaceAll("[\u0000-\u001f]", "").replaceAll("\\p{Cntrl}", "").replaceAll("\\\\", "")
					 .replaceAll("////", "").replaceAll("[^\\x00-\\x7F]", "");;
 
		System.out.println(":"+a.replaceAll("\\\\", "").replace("\\", "")+"::"+a.indexOf("/"));
		int s=a.indexOf("/");
		//
		//if(){
			
		//}
		
	}
}
