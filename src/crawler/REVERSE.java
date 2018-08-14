package crawler;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class REVERSE {
    public static void main(String args[] ) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
    String thisLine = null;
    String line="";
//    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//    while ((thisLine = br.readLine()) != null) {
//            System.out.println(thisLine);
//             
//         }
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    while ((line = stdin.readLine()) != null && line.length()!= 0) {
        String[] input = line.split(" ");
        if (input.length == 2) {
//            System.out.println( "test ");
            
            char [] first=input[0].toCharArray();
            char [] second=input[1].toCharArray();
            char [] first_reverse=new char[first.length];
            char [] second_reverse=new char[second.length];
            
            int max=first.length-1;
            String concFirstString="";
            String concSecondString="";
            boolean last_value_is_zero=false;
            
            for(max=first.length-1;max>=0;max--){
            	first_reverse[max]=first[max];
            }
            
            for(max=second.length-1;max>=0;max--){
            	second_reverse[max]=second[max];
            }
            
            // remove leading and trailing zeros.
            for(max=first_reverse.length-1;max>=0;max--){
//            	System.out.println(first_reverse[max]);
            		if(concFirstString.equals(""))
            			concFirstString=String.valueOf(first_reverse[max]);
            		else
            			concFirstString+=String.valueOf(first_reverse[max]);
            					
            }
         // remove leading and trailing zeros.
            for(max=second_reverse.length-1;max>=0;max--){
//            	System.out.println(second_reverse[max]);
            		if(concSecondString.equals(""))
            			concSecondString=String.valueOf(second_reverse[max]);
            		else
            			concSecondString+=String.valueOf(second_reverse[max]);
            					
            }
            
//            System.out.println("max:"+max+" "+" "+concFirstString.replaceFirst ("0*$", ""));
            concFirstString=concFirstString.replaceFirst ("0*$", "");
            concSecondString=concSecondString.replaceFirst ("0*$", "");
            
//            System.out.println(concFirstString+"---"+concSecondString);
 
            int firstInt=Integer.valueOf(concFirstString);
            int secondInt=Integer.valueOf(concSecondString);
            int out=firstInt+secondInt;
            String outSS="";
            char[] outS=String.valueOf(out).toCharArray();
            char[] outS_reverse=new char[outS.length];
            for(max=outS.length-1;max>=0;max--){
            	outS_reverse[max]=outS[max];
            	if(outSS.equals(""))
            			outSS=String.valueOf(outS[max]);
            	else
            		    outSS+=String.valueOf(outS[max]);
            }
            int final_v=Integer.valueOf(outSS);
            System.out.println("pairs input: "+firstInt+" "+secondInt+" sum:"+final_v);
            
        }
    }
    
  }
    
}

/*
3
24 1
4358 754
305 794
*/
