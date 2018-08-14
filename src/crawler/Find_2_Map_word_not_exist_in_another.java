package crawler;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


public class Find_2_Map_word_not_exist_in_another {
	
	//returns firstFeature not exists in second one.
	public static TreeMap<Integer,TreeMap<String,Double>> find_2_Map_word_not_exist_in_another(TreeMap<String, Double> firstFeatures, 
															TreeMap<String, Double> secondFeatures) {

			TreeMap<String,Double> exists_string=new TreeMap<String, Double>();
			TreeMap<String,Double> not_exists_string=new TreeMap<String, Double>();
			TreeMap<Integer,TreeMap<String,Double>> mapOut=new TreeMap<Integer, TreeMap<String,Double>>();
		try {
			Set<String> fkeys = firstFeatures.keySet();
			Iterator<String> fit = fkeys.iterator();
			//
			while (fit.hasNext()) {
				String featurename = fit.next();
				boolean containKey = secondFeatures.containsKey(featurename);
				if (containKey) {
					exists_string.put(featurename , 0.0);
				}
				else {
					not_exists_string.put(featurename , 0.0);
				}
			}
			mapOut.put(1, exists_string);
			mapOut.put(2, not_exists_string );
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mapOut;
	}

	
	
	public static void main(String[] args) {
		 
		 
	}
}
