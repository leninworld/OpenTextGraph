package crawler;
import java.io.IOException;


public class GetMaxFromArray {
	// get max value from given Array (generic) 14Aug2014
	public static double getMaxFromSingleArray(String s[], boolean debug){
		double max=0;
		try{
			if(debug==true)
				System.out.println( "s:"+s.length+";s:"+s.toString());
			int cnt=0; double last_no = 0;
			// 
			while(cnt<s.length){
				if(debug==true)
					System.out.println("d:-->"+s[cnt]);
				if(cnt>1 && Double.valueOf(s[cnt]) > last_no ){
					max=Double.valueOf(s[cnt]);
				}
				try{
					last_no= Double.valueOf(s[cnt]);
				}
				catch(Exception e){ //do nothing
					cnt++;
					continue;
				}
				cnt++;
			} //end
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return max;
	}
	//
	public static void main(String[] args) throws IOException {
		
		
	}
	
}
