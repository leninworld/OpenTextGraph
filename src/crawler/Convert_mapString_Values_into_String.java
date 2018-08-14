package crawler;
 
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

public class Convert_mapString_Values_into_String {

	//convert_mapString_Values_into_String
    public static String convert_mapString_Values_into_String(String  mapString,
    														  boolean is_remove_Duplicates_on_mapValues
    														  ){
    	
    		String out="";
    		TreeMap<String,String> map_already_Values=new TreeMap<String, String>();
	    	try {
	    		
	    		String []s1 =mapString.replace("{", "").replace("}", "").replace(" , ", ",").split(",");
	    		int cnt1=0;int cnt2=0;
	    		
	    		//
	    		while(cnt1<s1.length){
	    			 
	    			String []s2=s1[cnt1].split("=");
	    			//System.out.println(s2[0]+" "+s2[1]);
	    			//
	    			if(out.length()==0){
	    				out=s2[1];
	    				map_already_Values.put(s2[1], "");
	    			}
	    			else{
	    				//
	    				
	    				//remove duplicates
	    				if(is_remove_Duplicates_on_mapValues==true){
	    					if(!map_already_Values.containsKey(s2[1])){
		    					out=out+"!!!"+s2[1];
		    				}	
	    				}
	    				else{ //
	    					out=out+"!!!"+s2[1];
	    				}
	    				map_already_Values.put(s2[1], "");
	    			}
	    			cnt1++;
	    		}
	    		 
	    		
			} catch (Exception e) {
				// TODO: handle exception
			}
 
	    	return out;
        }
	
	//
    public static void main(String args[]) throws Exception {
 
    	String inputFPfile="{1=DELHI , 2=Taking , 3=note , 4=high , 5=number , 19=deaths , 6=deaths , 7=Indian , 8=roads , 9=1.41 , 10=lakh , 11=2014 , 12=Prime , 13=Minister , 14=France , 15=United , 16=Kingdom , 17=Denmark , 18=Sweden , 19=bring , 20=deaths , 21=significantly , 22=Sources , 23=Prime , 24=Minister , 25=Office , 26=India , 27=scores , 28=10 , 29=points , 30=enforcement , 31=speed , 32=limits , 33=scores , 34=10 , 35=enforcement , 36=helmet , 37=seatbelt , 38=laws , 39=50 , 40=% , 41=wheeler , 42=drivers , 43=wear , 44=helmets , 45=27 , 46=% , 47=wear , 48=seatbelts , 49=Welcoming }";
    	
    	//
    	System.out.println(convert_mapString_Values_into_String(inputFPfile, 
    															false ));
    	

    }

}