package crawler;

import java.util.TreeMap;

public class Verify_input_createGBADfromTextUsing{

	public class WrongInputException extends Exception {
		   WrongInputException(String s) {
		      super(s);
		   }
	}
	//
	public static boolean verify_input_createGBADfromTextUsing(
											 TreeMap<Integer, String> map_interested_idx_SourceFeature_mapping,
											 String[] arr_header) throws WrongInputException{
		// TODO Auto-generated method stub
		System.out.println("verify: input map:"+map_interested_idx_SourceFeature_mapping);
		for(int idx:map_interested_idx_SourceFeature_mapping.keySet()){
			System.out.println( "verify: "+idx+"<->"+map_interested_idx_SourceFeature_mapping.get(idx));
			//verify
			if(map_interested_idx_SourceFeature_mapping.get(idx).indexOf("$_")==-1 && // not _nn or _vbc
			  map_interested_idx_SourceFeature_mapping.get(idx).indexOf("$")>0){ 
			  
			if(!map_interested_idx_SourceFeature_mapping.get(idx).replace("$", "").equalsIgnoreCase(arr_header[idx-1] ) ){
// 				 throw new WrongInputException("");
				return false;
			}
		}
	
	}
		return true;
 }
}