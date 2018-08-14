package crawler;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class ConvertPatternStringToMap {
	
	// is Function
	public static boolean isFunction(String inStringToCheck){
		
		if(inStringToCheck.indexOf("substr")  >=0 
				|| inStringToCheck.indexOf("replace") >=0 
				||	inStringToCheck.indexOf("removetag") >=0 )
			return true;
		
		return false;
	}
	// Convert to Target String using Function with Arguments
	public static String convert_to_targetStringForFunctionWithArguments(   String targetString,
																		    String stringPattern, 
																			String arg1,
																			String arg2){
		// 
		if(stringPattern.indexOf("replace")>=0){
			arg1=arg1.replace("replace(", "");
			System.out.println("args:"+arg1+" "+arg2+" target:"+targetString);
			
			return targetString.replace(arg1, arg2);
		}
		if(stringPattern.indexOf("substr")>=0){
			return targetString.substring( Integer.valueOf(arg1), Integer.valueOf(arg2) );
		}
		if(stringPattern.indexOf("removetag")>=0){
			return Crawler.removeTagsFromHTML(targetString, false);
		}
		
		return "";
	}
	
	// convert to pattern to string array
	public static String[] convert_to_Pattern_into_arrayString(String toString){
		
		String [] s=toString.split("to");
 
		return s;
	}
	
	//convertSubStringString2StartEndIndexMap
	public static TreeMap<Integer, Integer> convertPatternSubStringString2StartEndIndexMap(String substringstring){
		TreeMap<Integer, Integer> mapOut = new TreeMap<Integer, Integer>();
		try{
			substringstring=substringstring.replace("substr(", "").replace(")", "")
										   .replace("to", ",");
			String[] s=substringstring.split(",");
			mapOut.put(1, Integer.valueOf(s[0]));
			mapOut.put(2, Integer.valueOf(s[1]));
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	// convert pattern substring to start index and end index array*
	public static String [] convertPattern_SubStringString2_StartEndIndexArrayString(String substringstring){
		
		TreeMap<Integer, Integer> mapOut = new TreeMap<Integer, Integer>();
		String[] s=null;
		try{
			substringstring=substringstring.replace("substr(", "").replace(")", "")
										   .replace("to", ",");
			s=substringstring.split(",");
	 
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return s;
	}
	
	public static TreeMap<Integer, Integer> convertPatternReplaceString2StartEndIndexMap(String replacestringstring){
		TreeMap<Integer, Integer> mapOut = new TreeMap<Integer, Integer>();
		try{
			replacestringstring=replacestringstring.replace("replace(", "").replace(")", "")
										   .replace("to", ",");
			String[] s=replacestringstring.split(",");
			mapOut.put(1, Integer.valueOf(s[0]) );
			mapOut.put(2, Integer.valueOf(s[1]) );
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	
	
	//attributefunction
	public static TreeMap<Integer, TreeMap<Integer, String>> attributefunction(String attributefunction ){
		TreeMap<Integer, TreeMap<Integer, String>> mapOut = new TreeMap();
		
		TreeMap<Integer, String> valueMap=new TreeMap(); 
		String [] s = attributefunction.split(",");
		int len=s.length; int cnt=0; String currStr="";
		System.out.println("attributefunction:"+attributefunction);
		try{
			while(cnt<len){
				currStr=s[cnt];
				valueMap=new TreeMap();
				String key=currStr.substring(0, currStr.indexOf("->") );
				String value=currStr.substring( currStr.indexOf("->")+2 ,currStr.length()  );
				String[] sValue=value.split("AND");
				
				System.out.println("#<k,v>:"+key+" , "+value+ " : "+sValue[0]);
				int cnt2=0;
				while(cnt2<sValue.length){
					valueMap.put(cnt2+1 , sValue[cnt2]);
					System.out.println("substringstring2map:"+convertPatternSubStringString2StartEndIndexMap(sValue[cnt2]));
					cnt2++;
				}
				mapOut.put( Integer.valueOf(key) , valueMap);
				
				cnt++;
			} //end of while
			System.out.println("attributefunction--------mapOut:"+mapOut);	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	// convertXMLParserStringToMap -> XML parser String to Map
	public static TreeMap<String, String> convertXMLParserStringToMap(
																	 String xmlParser,
																	 boolean isSOPprint){
		
		TreeMap<String, String> mapOut=new TreeMap();
		String [] csvElement=null;
		//mainTagStart->From,mainTagEnd->html,csvElements-><body>to</body>.replace();<head>to</head>.replace()
		try{
			String[] s = xmlParser.split(",");
			int len=s.length;
			int c=0;
			while(c<len){
				
				if(isSOPprint)
					System.out.println("s:"+s[c]);
				
				String[] s2=s[c].split("->");
				
				if(isSOPprint)
					System.out.println("s2:"+s2[0]+":"+s2[1]);
				
				mapOut.put(s2[0], s2[1]);
//				if(s2[0].equalsIgnoreCase("csvelements")){
//					csvElement=s2[1].split(";");
//				}
				
				c++;
			}
			
			//System.out.println("csv:"+csvElement[0]+" "+csvElement[1]);
			if(isSOPprint)
				System.out.println("mapOut:"+mapOut);
			
		}
		catch(Exception e){
			
		}
		return mapOut;
	}
	// graphTopologyStringToMap
	public static TreeMap<Integer, TreeMap<Integer, String>> convertStringToMap(
																	String graphTopologyString, 
																	String delimiter1,
																	String delimiter2
																	){
		TreeMap<Integer, TreeMap<Integer, String>> mapOut = new TreeMap();
		int count=1; String line=""; String key=""; int lineNumber=0;
		TreeMap<Integer, String> mapSource = new TreeMap();
		TreeMap<Integer, String> mapDest = new TreeMap();
		TreeMap<Integer, String> mapEdgeLabel = new TreeMap();
		TreeMap<Integer, String> mapDistinct_NewVertexID_OldVertexID = new TreeMap();
		TreeMap<String, Integer> mapDistinct_OldVertexID_NewVertexID = new TreeMap();
		try{
			
			String[] s1= graphTopologyString.split(delimiter1);
			int s1Count=0; String value=""; String dest="";
//			System.out.println("s1.len:"+s1.length);
			while (s1Count<s1.length) {
				lineNumber++;
				line=s1[s1Count];
				line = line.replaceAll("\\p{Cntrl}", "");
				StringTokenizer stk = new StringTokenizer(line, delimiter2);
				count = 1;
				while (stk.hasMoreTokens()) {
					value = (String) stk.nextToken();
					if (count == 1) {
						key = value;
						mapSource.put(lineNumber, value.replace("AND", ","));
						//if(value.indexOf("[") ==-1){
							int sz=mapDistinct_NewVertexID_OldVertexID.size()+1;
							if(!mapDistinct_NewVertexID_OldVertexID.containsValue(value.replace("AND", ","))){
								mapDistinct_NewVertexID_OldVertexID.put( sz, value.replace("AND", ",") );
								mapDistinct_OldVertexID_NewVertexID.put(value.replace("AND", ","), sz);
							}
						//}
						
					} else if (count == 2 ) {
						// writerDebug.append(" hp map insertion "+
						// key+";"+value+"\n");
						//mapName.put(key, value);
						String edgeLabel="";
						if(value.indexOf("(") >= 0 && value.indexOf(")") >= 0){
							edgeLabel=value.substring( value.indexOf("("), value.indexOf(")")+1 );
						}
						
						mapEdgeLabel.put(lineNumber, edgeLabel);
						if(value.indexOf("(") >= 0 && value.indexOf(")") >= 0){
							dest=value.substring(  value.indexOf(")")+1 , value.length() )
									.replace("AND", ",");
						}
						else{
//							System.out.println("***error()");
						}
						
						mapDest.put(lineNumber, dest);
						
						//if(value.indexOf("[") ==-1){
							int sz=mapDistinct_NewVertexID_OldVertexID.size()+1;
							if(!mapDistinct_NewVertexID_OldVertexID.containsValue(dest)){
								mapDistinct_NewVertexID_OldVertexID.put( sz,
																		 dest.replace("AND", ","));
								mapDistinct_OldVertexID_NewVertexID.put(dest.replace("AND", ","), 
																		 sz);
							}
						//}
//						System.out.println("<k,v,edgeLabel>:"+ key+" "+dest+" "+edgeLabel); 
					}
					count++;
				} // while loop for string tokenization
//				System.out.println("s:"+mapSource.size()); 
				s1Count++;
			} // token 1 read
			mapOut.put(1, mapSource);
			mapOut.put(2, mapDest);
			mapOut.put(3, mapDistinct_NewVertexID_OldVertexID);
			mapOut.put(4, mapEdgeLabel);
			//mapOut.put(4, mapDistinct_OldVertexID_NewVertexID);
//			System.out.println("---------------------------------");
//			System.out.println("1:<lineNo,Source>		:"+mapOut.get(1));
//			System.out.println("2:<lineNo,Destination>	:"+mapOut.get(2));
//			System.out.println("3:(distinct New_VertexID-Old_VertexID)		:"+mapOut.get(3));
//			System.out.println("---------------------------------");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	//main
	public static void main(String[] args) {
//		convertStringToMap("8->9,8->13,9->14",
//								",","->");
		
		System.out.println("test".indexOf("")+   " "+""+" ".length());
		
	}
	
}