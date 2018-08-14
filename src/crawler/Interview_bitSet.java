package crawler;
 
import java.io.IOException;
import java.math.BigInteger;

//import com.google.common.primitives.UnsignedInts;

public class Interview_bitSet {
	
	
	public static int interview_bitSet(int n){
		int c=0;
	    int i=1; int t_count=0;
		try{
				
			    while(i<=n){
				    BigInteger BigNum = new BigInteger(String.valueOf(i));
				    c = BigNum.bitCount();
//				    System.out.println("now.count:"+c+" i="+i+" t_count:"+t_count);
				    t_count+=c;
				    i++;
			    
			    }

			    System.out.println("Answer= " + t_count +" for input="+n);
			    
			return t_count;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return t_count;
	}
	
	
	public static void main(String[] args) throws IOException {
		interview_bitSet(6);
		
		interview_bitSet(8);
	 
	}

 
}
