package crawler;
import java.util.TreeMap;

public class Splitting_given_1_uniqueKeys_SplitMap_to_multiple_each_having_keys_Start_with_first_letter {

//
public static TreeMap<String, TreeMap<String,String>> 
splitting_given_1_uniqueKeys_SplitMap_to_multiple_each_having_keys_Start_with_first_letter(
									TreeMap<String,String> mapUniqueKeys,
									int Flag //{0=keys;1=values}
									){

TreeMap<String, TreeMap<String,String> > mapString_Array=new TreeMap();
TreeMap<String,String> mapCurr_out_multi_keys =new TreeMap();

try{

		//
		if(Flag==0){
		
			for(String key:mapUniqueKeys.keySet()){
					if(key==null)
						continue;
					else if(key.length()==0)
						continue;
					String firstLetter=key.substring(0, 1);
					
					// exists
					
					if(mapString_Array.containsKey(firstLetter) ){
					
							mapCurr_out_multi_keys= mapString_Array.get(firstLetter);
							mapCurr_out_multi_keys.put(key ,"dummy");
							mapString_Array.put(firstLetter, mapCurr_out_multi_keys);
					}
					
					else{ //not exists
					
						//	int size=curr_arr_out_multi_keys.length;
					
						int cnt=0;
						mapCurr_out_multi_keys.put(key, ""); 
						mapString_Array.put(firstLetter, mapCurr_out_multi_keys);
					
					}
					
					mapCurr_out_multi_keys=new TreeMap();
			
			}
		}

	//	
	if(Flag==1){
	
	}

}
catch(Exception e){
	e.printStackTrace();
}

return mapString_Array;

}

//

    public static void main(String[] arg){

    TreeMap<String,String> mapUniqueKeys=new TreeMap();

    mapUniqueKeys.put("lenin","");
    mapUniqueKeys.put("kishore","");
    mapUniqueKeys.put("ganesh t","");
    mapUniqueKeys.put("vb","");

    System.out.println(
    		splitting_given_1_uniqueKeys_SplitMap_to_multiple_each_having_keys_Start_with_first_letter
    		(mapUniqueKeys, 0)
    );

    

    }



}