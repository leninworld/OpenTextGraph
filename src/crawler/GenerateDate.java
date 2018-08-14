package crawler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GenerateDate {
	
	
	public static String generate_return_EEE_for_input_Day_of_Week(int day_of_week_int){
		String out_day_of_week_string="";
		try {
			if(day_of_week_int==1)
				out_day_of_week_string="Mon";
			if(day_of_week_int==2)
				out_day_of_week_string="Tue";
			if(day_of_week_int==3)
				out_day_of_week_string="Wed";
			if(day_of_week_int==4)
				out_day_of_week_string="Thu";
			if(day_of_week_int==5)
				out_day_of_week_string="Fri";
			if(day_of_week_int==6)
				out_day_of_week_string="Sat";
			if(day_of_week_int==0)
				out_day_of_week_string="Sun";
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return out_day_of_week_string;
	}
	//generate 
	public static String generate_Date_Pattern_for_RSS_XML_Parser_for_date_DD_MMM_YYYY(
																	String DD_MMM_YYYY
																	){
		String out="";
		try {
			
			out= "From - "+
				 find_Day_of_Week_EEE_for_input_date_DD_MMM_YYYY(DD_MMM_YYYY)
				 +", "+DD_MMM_YYYY;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return out;
	}
	
	//
	public static String find_Day_of_Week_EEE_for_input_date_DD_MMM_YYYY(
														String DD_MMM_YYYY
														){
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy"); //dd-MM-yyyy
		String dateInString = DD_MMM_YYYY;
       	Calendar cal=Calendar.getInstance();
    	Date date=null;
		try {
			date = sdf.parse(dateInString);   	
 
        	 System.out.println("date2::"
        			 			+","+date.getDay()
        			 			+","+GenerateDate.generate_return_EEE_for_input_Day_of_Week(date.getDay())
        			 			+","
        			 			+dateInString);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return GenerateDate.generate_return_EEE_for_input_Day_of_Week(date.getDay());
	}
	//main
	public static void main(String[] args) throws IOException {
		
		System.out.println(
		generate_Date_Pattern_for_RSS_XML_Parser_for_date_DD_MMM_YYYY("14 July 2015")
		);
		
	}	

}