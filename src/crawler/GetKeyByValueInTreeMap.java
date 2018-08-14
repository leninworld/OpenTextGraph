package crawler;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GetKeyByValueInTreeMap {
	
	// key by value
	public static String getKeyByValueInTreeMap(String value, 
												TreeMap<Integer,String> hmap,
												boolean isDebug){
		String key="";
		try{
		   //Map hmap = new HashMap();
			if(isDebug)
				System.out.println("getKeyByValueInTreeMap():"+value+" : "+hmap);
		   TreeMap<String, String> map=new  TreeMap();
		   //Set<Map.Entry<String, Double>> set = hmap.entrySet();
		   //
		   //for(Map.Entry entry : set ){
		   for(int key2:hmap.keySet()){
			   String value2= hmap.get(key2);
			   //System.out.println("tmp:"+tmp);
			   // it can be CSV  
			   //String [] s = tmp.split(",");
//			   int cnt=0;
//			   while(cnt<s.length){
//				   map.put(s[cnt], s[cnt]);
//				   cnt++;
//			   }
			   
	            if(  value.equalsIgnoreCase(value2) ){
	                key = String.valueOf(key2);
	                break; //breaking because its one to one map
	            }
	        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return key;
	}
	

}
