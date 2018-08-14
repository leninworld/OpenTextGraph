package crawler;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
 
public class Sort_given_treemap {
	
	public static <K,V extends Comparable<? super V>> 
    List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

				List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
				
				Collections.sort(sortedEntries, 
				    new Comparator<Entry<K,V>>() {
				        @Override
				        public int compare(Entry<K,V> e1, Entry<K,V> e2) {
				            return e2.getValue().compareTo(e1.getValue());
				        }
				    }
				);
				
				return sortedEntries;
	}
	
	//sortByValue_ID_method4_ascending_reverse_TopN (note: final return will have sort by key values and not by values)
	public static TreeMap<Integer, Double> sortByValue_ID_method4_ascending_reverse_TopN(  
																		  Map<Integer, Double>  sortedMap,
																		  int top_N){
		  TreeMap<Integer, Double>  sortedMap_topN=new TreeMap<Integer, Double>();
		try {
				//reverse a map and get top N
		 
				 int cnt=0;
			     ArrayList<Integer> keys = new ArrayList<Integer>(sortedMap.keySet());
			     //debug below
			     for(int i=keys.size()-1; i>=0;i--){

			            cnt++;
			            //
			            if(cnt>top_N)
			            	break;
			            //System.out.println("reversing:"+sortedMap.get(keys.get(i)));
			            // reverse (descending top N)
			            sortedMap_topN.put(keys.get(i), sortedMap.get(keys.get(i)) );
			     }

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sortedMap_topN;
	
	}
 
	public static TreeMap<String, Integer> sortByValue_SI(Map unsortedMap) {
		TreeMap sortedMap = new TreeMap(new ValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}
	
	public static TreeMap<Integer, Double> sortByValue(TreeMap<Integer, Double> map_lineNo_score) {
		TreeMap<Integer, Double> sortedMap = new TreeMap<Integer, Double>(new ValueComparator(map_lineNo_score));
		sortedMap.putAll(map_lineNo_score);
		return sortedMap;
	}
	
	public static TreeMap<Integer, Integer> sortByValue_II(TreeMap<Integer, Integer> map_lineNo_score) {
		TreeMap<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>(new ValueComparator(map_lineNo_score));
		sortedMap.putAll(map_lineNo_score);
		return sortedMap;
	}
	
	
	//bigger number to smaller number sorting
	public static TreeMap<String,Integer> sortByValue_SI_method2_descending(TreeMap<String, Integer> hm){
		  TreeMap<String, Integer> mapOut=new TreeMap<String, Integer>();
	        Set<Entry<String, Integer>> set = hm.entrySet();
	        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(
	                set);
	        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
												            public int compare(Map.Entry<String, Integer> o1,
												                    Map.Entry<String, Integer> o2) {
												                return o2.getValue().compareTo(o1.getValue());
												            }
												        });
	        int counter=0;
	        for (Entry<String, Integer> entry : list) {
	        	Integer value=entry.getValue(); String key=entry.getKey();
	            System.out.println("inside:"+key+" "+value );
	            mapOut.put(key , value);
	            counter++;
	        }
	        System.out.println("------ total count:"+counter);
		return mapOut;
	}
	//smaller to bigger
	static Map<String, Integer> sortByValue_SI_method4_ascending(Map<String, Integer> unsortMap) {
		// Convert Map to List
		List<Map.Entry<String, Integer>> list = 
			new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
                                           Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
		int counter=0;
		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			
			String key=entry.getKey(); int value=entry.getValue();
			sortedMap.put(key, value);
			counter++;
		}
		System.out.println("--------------------------- counter:"+counter);
		return sortedMap;
	}
	//smaller to bigger
	public static HashMap<String, Double> sortByValue_SD_method4_ascending(Map<String, Double> unsortMap) {
		// Convert Map to List
		List<Map.Entry<String, Double>> list = 
			new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
                                           Map.Entry<String, Double> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
		int counter=0;
		// Convert sorted map back to a Map
		HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Iterator<Map.Entry<String, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Double> entry = it.next();
			String key=entry.getKey(); double value=entry.getValue();
			
			sortedMap.put(key, value);
			counter++;
		}
		System.out.println("--------------------------- counter:"+counter);
		return sortedMap;
	}
	
	//
	public static Map<Integer, Double> sortByValue_ID_method4_ascending(Map<Integer, Double> unsortMap
																		) {

		// Convert Map to List
		List<Map.Entry<Integer, Double>> list = 
			new LinkedList<Map.Entry<Integer, Double>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1,
                                           Map.Entry<Integer, Double> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
		for (Iterator<Map.Entry<Integer, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		

		
		return sortedMap;
	}
	
 
	
	//sortByValue_II_method4_ascending
	public static Map<Integer, Integer> sortByValue_II_method4_ascending(Map<Integer, Integer> unsortMap) {

		// Convert Map to List
		List<Map.Entry<Integer, Integer>> list = 
			new LinkedList<Map.Entry<Integer, Integer>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
			public int compare(Map.Entry<Integer, Integer> o1,
                                           Map.Entry<Integer, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
		int counter=0;
		
		// Convert sorted map back to a Map
		Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
		for (Iterator<Map.Entry<Integer, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Integer> entry = it.next();
			
			int key=entry.getKey(); int value=entry.getValue();
			sortedMap.put(key, value);
//			System.out.println("key,value:"+key +" "+value);
			counter++;
		}
		System.out.println("--------------counter:"+counter);
		return sortedMap;
	}
	
	public static void printMap(Map<String, Integer> map) {
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println("[Key] : " + entry.getKey() 
                                      + " [Value] : " + entry.getValue());
		}
	}

	
 
static class ValueComparator implements Comparator {
 
	Map map;
 
	public ValueComparator(Map map) {
		this.map = map;
	}
 
	public int compare(Object keyA, Object keyB) {
		Comparable valueA = (Comparable) map.get(keyA);
		Comparable valueB = (Comparable) map.get(keyB);
		return valueB.compareTo(valueA);
	}
	
	
	
	
	public static void main(String[] args) {
		TreeMap<Integer, Double> map = new TreeMap<Integer, Double>();
		map.put(1, 10.);
		map.put(2, 30.);
		map.put(3, 50.);
		map.put(4, 40.);
		map.put(5, 20.);
		System.out.println(map);
 
		Map sortedMap = sortByValue(map);
		System.out.println("out:"+ sortedMap);
//		System.out.println( sortByValue_SI_method4_ascending(map) );
//		
		TreeMap<Integer, Integer> map2 = new TreeMap<Integer, Integer>();
		map2.put(1, 10);
		map2.put(2, 30);
		map2.put(3, 50);
		map2.put(4, 40);
		map2.put(5, 20);
		 
		
		
		TreeMap<Integer, Double> map3 = new TreeMap<Integer, Double>();
		map3.put(1, 10.1);
		map3.put(2, 30.2);
		map3.put(3, 50.3);
		map3.put(7, 40.5);
		map3.put(9, 20.1);
		map3.put(22, 12.1);
		Map<Integer, Double> map3_sorted_asc=sortByValue_ID_method4_ascending(map3);
		
		Map<String, Integer> map4 = new TreeMap<String, Integer>();
		map4.put("1", 39);
		map4.put("10", 20);
		map4.put("2", 37);
//		{1=39, 10=20, 11=14, 12=11, 13=10, 14=2, 2=37, 3=35, 4=31, 5=28, 6=27, 7=24, 8=22, 9=21}
		List<Entry<String, Integer>> map4_sorted_desc= entriesSortedByValues(map4);
		
		 System.out.println("ascending:"+map3_sorted_asc);
		 System.out.println("map4_sorted_desc:"+map4_sorted_desc);
		 
		 	System.out.println("#1 iterator");
			Iterator<Entry<String, Integer>> iterator = map4_sorted_desc.iterator();
			while (iterator.hasNext()) {
				Entry<String, Integer> NOW=iterator.next();
				System.out.println("NEXT:" + NOW.getKey() +" "+NOW.getValue());
			}
		 
	     ArrayList<Integer> keys = new ArrayList<Integer>(map3_sorted_asc.keySet());
	     //
	     for(int i=keys.size()-1; i>=0;i--){
	            System.out.println(keys.get(i)+"<->"+ map3_sorted_asc.get(keys.get(i)));
	     }
	     System.out.println("sorted:"+sortByValue_ID_method4_ascending_reverse_TopN(map3_sorted_asc, 5));
		
	}
	


	}

}