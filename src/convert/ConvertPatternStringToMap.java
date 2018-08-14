package convert;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class ConvertPatternStringToMap {


	public static String removeBraces(String inputString){
		try{

			return inputString.replace("(","").replace(")","" );

		}
		catch(Exception e){
				e.printStackTrace();
		}
		return inputString;
	}

	//convertSubStringString2StartEndIndexMap
	public static TreeMap<Integer, Integer> convertPatternSubStringString2StartEndIndexMap(String substringstring){
		TreeMap<Integer, Integer> mapOut = new TreeMap<Integer, Integer>();
		try{
			substringstring=substringstring.replace("substr(", "").replace(")", "").replace("_to_", ",");
			String[] s=substringstring.split(",");
			mapOut.put(1,  Integer.valueOf( removeBraces(s[0]) ) );
			mapOut.put(2,  Integer.valueOf( removeBraces(s[1]) ) );
			
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
				String[] sValue=value.split("_and_");
				
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
	
	// convert string to map
	public static TreeMap<Integer, TreeMap<Integer, String>> convertStringToMap(
														String graphTopologyString, 
														String delimiter1, //default ,
														String delimiter2 // default ->
														){
		TreeMap<Integer, TreeMap<Integer, String>> mapOut = new TreeMap();
		int count=1; String line=""; String key=""; int lineNumber=0;
		TreeMap<Integer, String> mapSource = new TreeMap();
		TreeMap<Integer, String> mapDest = new TreeMap();
        TreeMap<Integer, String> mapID = new TreeMap();
        TreeMap<Integer, String> mapSTART = new TreeMap();
        TreeMap<Integer, String> mapEND = new TreeMap();
        TreeMap<Integer, String> mapOTHER = new TreeMap();

        TreeMap<Integer, String> map_CHAIN_edge = new TreeMap();
        TreeMap<Integer, String> map_CHAIN_dest2 = new TreeMap();

		TreeMap<Integer, String> mapEdgeLabel = new TreeMap();
		TreeMap<Integer, String> mapDistinct_NewVertexID_OldVertexID = new TreeMap();
		TreeMap<String, Integer> mapDistinct_OldVertexID_NewVertexID = new TreeMap();
		try{
			
			String[] s1= graphTopologyString.split(delimiter1);
			int s1Count=0; String value=""; String dest="";
			System.out.println("---------------------------------");
			System.out.println("s1.len:"+s1.length);
			boolean is_having_currlyBracket_for_ChainNodes=false;
			TreeMap<String,String> map_destID_NOT_to_included=new TreeMap<String, String>();
			//
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
						mapSource.put(lineNumber, value.replace("_and_", ","));
						//if(value.indexOf("[") ==-1){
							int sz=mapDistinct_NewVertexID_OldVertexID.size()+1;
							if(!mapDistinct_NewVertexID_OldVertexID.containsValue(value.replace("_and_", ","))){
								mapDistinct_NewVertexID_OldVertexID.put( sz, value.replace("_and_", ",") );
								mapDistinct_OldVertexID_NewVertexID.put(value.replace("_and_", ","), sz);
							}
						//}
						
					} else if (count == 2 ) {
						// writerDebug.append(" hp map insertion "+
						// key+";"+value+"\n");
						//mapName.put(key, value);

                        ////////  EDGE
						String edgeLabel="";
						if(value.indexOf("(") >= 0 && value.indexOf(")") >= 0){
							edgeLabel=value.substring( value.indexOf("("), value.indexOf(")")+1 );
						}
						
						mapEdgeLabel.put(lineNumber, edgeLabel);

						////////  DESTINATION
						if(value.indexOf("(") >= 0 && value.indexOf(")") >= 0){
                            System.out.println(" value=== (dest)-->"+value);

                            if(value.indexOf("{")>0){
                                int begin_index=value.indexOf(")")+1;
                                dest=value.substring(begin_index, value.indexOf("{"));
                                map_destID_NOT_to_included.put(dest, ""); //NOT include
                            }
                            else{
                                dest=value.substring(value.indexOf(")")+1 , value.length() ).replace("_and_", ",");
                            }
                            // get the chain nodes
                            if(value.indexOf("{") >= 0 && value.indexOf("}") >= 0) {
                            	map_destID_NOT_to_included.put(dest, "");
                                // example: {[ID.1],[START.edgelabel],[OTHER.edgeLabel],4*[has]*1**4*[has]*2,[END.edgeLabel]}
                                System.out.println(" value2,new=== (dest)-->" + value);
                                String[] arr_value = value.split("#");
                                String  ID_ = arr_value[0];
                                        ID_=ID_.substring(ID_.indexOf("{"),ID_.length());
                                        ID_=ID_.replace("{","");

                                String START_ = arr_value[1];
                                String OTHER_ = arr_value[2];
                                String NODES_CHAIN_ = arr_value[3];

                                String[] _ChainNode_edgeLabel_auxillaryNode = NODES_CHAIN_.split("\\*\\*");
                                int i=0;
                                // from the CHAIN , WE have edgeLabel and Destination2 which will appear in each of the CHAIN..
                                while(i<_ChainNode_edgeLabel_auxillaryNode.length){
                                    String [] edge_dest2_pair=_ChainNode_edgeLabel_auxillaryNode[i].split("\\.");

                                    // edgeLabel
                                    if(!map_CHAIN_edge.containsKey(lineNumber))
                                    	if(edge_dest2_pair.length>=1)
                                    		map_CHAIN_edge.put(lineNumber, edge_dest2_pair[0]); // edgeLabel
                                    else {
                                        String t = "";
                                        if(edge_dest2_pair.length>=1){
                                        	t=map_CHAIN_edge.get(lineNumber) + "**" + edge_dest2_pair[0];
                                        	map_CHAIN_edge.put(lineNumber, t);
                                        }
                                    }

                                    if(!map_CHAIN_dest2.containsKey(lineNumber))
                                    	if(edge_dest2_pair.length>=2)
                                    		map_CHAIN_dest2.put(lineNumber, edge_dest2_pair[1]); // destination 2
                                    else{
                                        String t= "";
                                        if(edge_dest2_pair.length>=2){
                                        	t=map_CHAIN_dest2.get(lineNumber)+"**"+edge_dest2_pair[1];
                                        	map_CHAIN_dest2.put(lineNumber, t); // destination 2
                                        }
                                    }

                                    i++;
                                }

                                String  END_ = arr_value[4];
                                        END_ = END_.replace("}","");

                                int begin_index = value.indexOf(")") + 1;
                                dest = value.substring(begin_index, value.indexOf("{"));
                                System.out.println("1..ID_:" + ID_ + " START_:" + START_ + " OTHER_:" + OTHER_ + " NODES_CHAIN_:" + NODES_CHAIN_
                                        + " END_:" + END_);

                                // ID must be a number
                                mapID.put(lineNumber, ID_.replace("ID.","").replace("[","").replace("]","") );

                                mapSTART.put(lineNumber, START_.replace(".edgeLabel","").replace("[","").replace("]",""));
                                mapEND.put(lineNumber, END_.replace(".edgeLabel","").replace("[","").replace("]",""));
                                mapOTHER.put(lineNumber, OTHER_.replace(".edgeLabel","").replace("[","").replace("]",""));
                            }

						}
                        else if(value.indexOf("{") >= 0 && value.indexOf("}") >= 0){
                        	
						    // example: {[ID.1],[START.edgelabel],[OTHER.edgeLabel],4*[has]*1**4*[has]*2,[END.edgeLabel]}
                            System.out.println(" value2=== (dest)-->"+value);
                            String [] arr_value= value.split("#");
                            String ID_ = arr_value[0];
                            String START_ = arr_value[1];
                            String OTHER_ = arr_value[2];
                            String NODES_CHAIN_ = arr_value[3];

                            String [] _ChainNode_edgeLabel_auxillaryNode = NODES_CHAIN_.split("\\*\\*");

                            String END_ = arr_value[4];

                            int begin_index=value.indexOf(")")+1;
                            dest=value.substring(begin_index, value.indexOf("{"));
                            System.out.println("ID_:"+ID_+" START_:"+START_+" OTHER_:"+OTHER_+" NODES_CHAIN_:"+NODES_CHAIN_
                                                    +" END_:"+END_);

                            mapID.put(lineNumber, ID_.replace("ID.","").replace("[","").replace("]","") );
                            mapSTART.put(lineNumber, START_.replace(".edgeLabel","").replace("[","").replace("]",""));
                            mapEND.put(lineNumber, END_.replace(".edgeLabel","").replace("[","").replace("]",""));
                            mapOTHER.put(lineNumber, OTHER_.replace(".edgeLabel","").replace("[","").replace("]",""));
                        }
						else{
							System.out.println("***error()");
						}
						
						mapDest.put(lineNumber, dest);
						
						//if(value.indexOf("[") ==-1){
						
						if(!map_destID_NOT_to_included.containsKey(dest)){
						
							int sz=mapDistinct_NewVertexID_OldVertexID.size()+1;
							if(!mapDistinct_NewVertexID_OldVertexID.containsValue(dest)){
								mapDistinct_NewVertexID_OldVertexID.put( sz,
																		 dest.replace("_and_", ","));
								mapDistinct_OldVertexID_NewVertexID.put(dest.replace("_and_", ","), 
																		 sz);
							}
						}
						System.out.println("------>"+s1Count+" <k,v,edgeLabel>:"+ key+" "+dest+" "+edgeLabel); 
					}
					count++;
				} // while loop for string tokenization

				s1Count++;
			} // token 1 read
			System.out.println("s:"+mapSource.size()); 
			mapOut.put(1, mapSource);
			mapOut.put(2, mapDest);
			mapOut.put(3, mapDistinct_NewVertexID_OldVertexID);
			mapOut.put(4, mapEdgeLabel);
            mapOut.put(5, map_CHAIN_edge);
            mapOut.put(6, map_CHAIN_dest2);
            //
            mapOut.put(7, mapID);
            mapOut.put(8, mapSTART);
            mapOut.put(9, mapEND);
            mapOut.put(10, mapOTHER);

			//mapOut.put(4, mapDistinct_OldVertexID_NewVertexID);
			System.out.println("---------------------------------");
			System.out.println("1:<lineNo,Source>:"+mapOut.get(1));
			System.out.println("2:<lineNo,Destination>:"+mapOut.get(2));
			System.out.println("4:(lineNo,edge):"+mapOut.get(4));
			System.out.println("3:(distinct New_VertexID-Old_VertexID):"+mapOut.get(3));
            System.out.println("5:<lineNo,edgeLabel_4_ChainNode>:"+mapOut.get(5));
            System.out.println("6:<lineNo,dest2_4_ChainNode>:"+mapOut.get(6));
            System.out.println("7:<lineNo,mapID(KEY(7), ID as VALUE)>:"+mapOut.get(7));
            System.out.println("8:<lineNo,mapSTART(KEY(8), START as VALUE)>:"+mapOut.get(8));
            System.out.println("9:<lineNo,mapEND(KEY(9), END as VALUE)>:"+mapOut.get(9));
            System.out.println("10:<lineNo,mapOTHER(KEY(10), OTHER as VALUE)>:"+mapOut.get(10));
			System.out.println("---------------------------------");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	// main
	public static void main(String[] args) {
		//dont DELETE these samples.
		String graph_topology="[person]->(with)[ID],[ID]->(is)1.substr(1_to_6)_and_2.substr(0_to_2),[person]->(xy)[XY],[XY]->(3)3.substr(1_to_3)_and_3.substr(1_to_4)";
		
		convertStringToMap("8->9,8->13,9->14",",","->");
		convertStringToMap(graph_topology,",","->");
		
		
	}
	
}