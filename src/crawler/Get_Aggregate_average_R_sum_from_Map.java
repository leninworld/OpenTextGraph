package crawler;

import java.util.Random;
import java.util.TreeMap;

public class Get_Aggregate_average_R_sum_from_Map {

	//get_ID_Aggregate_average_R_sum_from_Map
	public static double get_ID_Aggregate_average_R_sum_from_Map(
															TreeMap<Integer,Double> mapIn,
															int flag
															){
 
		double out_double =0.;
		int cnt=0;
		try {
			//
			for(int i:mapIn.keySet()){
				out_double=out_double+mapIn.get(i);
				cnt++;
			}
			 
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	 
	    return out_double/(double) cnt;
	}
	public static void main(String[] args) {
		 
	}
    
}
