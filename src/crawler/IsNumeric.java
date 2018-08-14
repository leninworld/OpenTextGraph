package crawler;

import java.util.HashMap;

public class IsNumeric {
	

	// check numeric
	public static boolean isNumeric(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	  // is_numeric
		public static HashMap<Integer,Boolean> is_numeric_2(String inString){
				HashMap<Integer,Boolean> mapOut=new HashMap<Integer, Boolean>();
				char b='\0'; 
				try{
					
					char a= inString.charAt(0);
					
					try{
					  b= inString.charAt(1);
					}catch(Exception e){
						
					}
					
					boolean b_=false;
					boolean a_=IsNumeric.isNumeric(String.valueOf(a));
					
					try{
						b_=IsNumeric.isNumeric(String.valueOf(b));
					}
					catch(Exception e){
						b_=false;
					}
					// 
					if(a_==true || b_==true){										
					}
					
					mapOut.put(1, a_);
					mapOut.put(2, b_);
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return mapOut;
			}
			
		 // input: sundar,32 -> false  / input 22.2 -> true
		public static boolean is_Numeric_3(String a){
			try {
 
				String [] h=a.split(",");
//				System.out.println("::"+h[0].substring(h[0].length()-1, h[0].length())  +" "+h[1].substring(0, 1) );
				
				String before_comma=h[0].substring(h[0].length()-1, h[0].length()) ;
				String after_comma=h[1].substring(0, 1);
				
				if( Crawler.isNumeric(before_comma)==false && Crawler.isNumeric(after_comma) ==true ){
					//System.out.println("true");
					return false;
				}
				else{
					//System.out.println("NOT TRUE");
					return true;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return false;
		}


	// main
	public static void main(String[] args) {
		System.out.println("main of isNumeric..");
	}


}
