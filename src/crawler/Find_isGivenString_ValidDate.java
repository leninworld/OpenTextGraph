package crawler;

import java.io.IOException;
import java.util.TreeMap;
import java.util.Date;
import java.util.Calendar;
import java.text.ParseException;

import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.Locale;

public class Find_isGivenString_ValidDate {
	public static boolean isValidDate(String inputDate, 
									  String format//example"yyyy/MM/dd"
									  ) {
		boolean valid = false;
		Date date2=null;
		try {
			date2=new Date(inputDate);
			//System.out.println("  date:"+date2);
			SimpleDateFormat format2 = new SimpleDateFormat(format);
			 
			  String DateToStr = format2.format(date2);
			  System.out.println(DateToStr);
			  
			  if(!DateToStr.equalsIgnoreCase(inputDate)){
				  //System.out.println("NOT VALID DATE");
				  return false;
			  }
			  else{
				  //System.out.println("VALID DATE");
				  return true;
			  }
			  
			  
		} catch (Exception ignore) {
			System.out.println(" error date:"+date2);
		}

		return valid;
	}

	// main
	public static void main(String[] args) throws IOException {
		TreeMap<String, String> mapout = new TreeMap<String, String>();
		isValidDate( "2015/04/31","yyyy/MM/dd");
	}

}
