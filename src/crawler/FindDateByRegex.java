package crawler;

import java.util.TreeMap;
import java.util.regex.*;

public class FindDateByRegex {
	
	// find month integer for given month string
	public static int findMonthIntegerFor_GivenMonthString(String monthString){
		monthString=monthString.toLowerCase();
		int monthInteger=0;
		try {
			if(monthString.indexOf("jan")>=0){
				monthInteger=1;
			}
			else if(monthString.indexOf("feb")>=0){
				monthInteger=2;
			}
			else if(monthString.indexOf("mar")>=0){
				monthInteger=3;
			}
			else if(monthString.indexOf("apr")>=0){
				monthInteger=4;
			}
			else if(monthString.indexOf("may")>=0){
				monthInteger=5;
			}
			else if(monthString.indexOf("jun")>=0){
				monthInteger=6;
			}
			else if(monthString.indexOf("jul")>=0){
				monthInteger=7;
			}
			else if(monthString.indexOf("aug")>=0){
				monthInteger=8;
			}
			else if(monthString.indexOf("sep")>=0){
				monthInteger=9;
			}
			else if(monthString.indexOf("oct")>=0){
				monthInteger=10;
			}
			else if(monthString.indexOf("nov")>=0){
				monthInteger=11;
			}
			else if(monthString.indexOf("dec")>=0){
				monthInteger=12;
			}
			
				
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return monthInteger;
	}
	
	// 
	public static TreeMap<Integer, String> findDateByRegexPatternForGivenInputTextandReturnSpecificGroup
																	(String inputText,
			   												   		 String stringPattern,
			   												   		 int GroupSequenceID){
		TreeMap<Integer, String>  mapOut= new TreeMap<Integer, String>();
		try {

				Pattern p = Pattern.compile(stringPattern); 
				Matcher m = p.matcher(inputText);
				
				StringBuffer result = new StringBuffer();
				int id=0;
				while (m.find()) {
				id++;
//				System.out.println("Masking: " + m.group(1)+" "+m.group(2)+" "+m.group(3)+" "+m.group(4)
//							+" "+m.group(5)+" "+m.group(6));
				mapOut.put(id, m.group(GroupSequenceID));
				m.appendReplacement(result, m.group(1) + "***masked***");
				}
				m.appendTail(result);
				//System.out.println(result);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mapOut;
	}
	
	// 
	public static void findDateByRegexPatternForGivenInputText(String inputText,
							    							   String stringPattern){
		
		try {

			Pattern p = Pattern.compile(stringPattern); 
			Matcher m = p.matcher(inputText);

			StringBuffer result = new StringBuffer();
			while (m.find()) {
				System.out.println("Masking: " + m.group(1)+" "+m.group(2)
									+" "+m.group(3)+" "+m.group(4)
									+" "+m.group(5)+" "+m.group(6));
				m.appendReplacement(result, m.group(1) + "***masked***");
			}
			m.appendTail(result);
			System.out.println(result);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	public static void main(String[] args) {
		
		
		String input = 
                  "User  Sun, 01 Mar 2015 17:41:19 EST clientId=23421. Fri, 27 Feb 2015 18:54:14 EST Some more text clientId=33432. This clientNum=100";
		
		// works for DD MMM YYYY
		Pattern p = Pattern.compile("(\\d{2}).([a-zA-Z][a-zA-Z][a-zA-Z]).(\\d{4})"); 
		// works for DD MMM YYYY HH:MM:SS
		String stringpattern="(\\d{2}).([a-zA-Z][a-zA-Z][a-zA-Z]).(\\d{4}).(\\d{2}):(\\d{2}):(\\d{2})";
		
		
		findDateByRegexPatternForGivenInputText(input, stringpattern);
		
		System.out.println(findDateByRegexPatternForGivenInputTextandReturnSpecificGroup(input, stringpattern,2));
		System.out.println(findMonthIntegerFor_GivenMonthString("apr"));
		
	}
	
}
