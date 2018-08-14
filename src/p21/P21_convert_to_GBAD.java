package p21;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import crawler.LoadGoogleAlert_FileToMap;
import crawler.Crawler;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

public class P21_convert_to_GBAD {

    //First slide has Topology->https://docs.google.com/presentation/d/1NloLOxxDyLSm_F3HFAgokEG3lSUHbIsPdsf8jV5YNoU/edit#slide=id.g1897bc53b5_0_5
	//p21_convert_to_GBAD_topology1
//	public static void p21_convert_to_GBAD_topology1(	
//											String baseFolderINPUT,
//											String baseFolderOUTPUT, 
//											TreeMap<Integer, String> map_SEQ_headerTokenOFall, 
//											TreeMap<String, Integer> map_headerTokenOFall_SEQ,
//											int OFFSET_dont_write_lesserTHAN_TOTALvertex, 
//											boolean isSOPprint
//											){
//		
//		String [] arr_list_of_files=new String[200000];
//		String NA_string="\"na\"";
//		FileWriter writer=null; FileWriter writerDebug=null;
//		int is_run_for_GBAD_or_SUBDUE=1;  //{1=GBAD, 2 =SUBDUE}
//		try{
//			
//			int c=0;
//			System.out.println("baseFolderINPUT:"+baseFolderINPUT);
//			arr_list_of_files=new File(baseFolderINPUT).list();
//			System.out.println("arr_list_of_files:"+arr_list_of_files);
//			int maxfile=arr_list_of_files.length;
//			TreeMap<Integer, String> mapLines=new TreeMap<Integer, String>();
//			
//			TreeMap<Integer, String> map_TokenIndexNOTmissing_TokenName =new TreeMap<Integer, String>();
//			TreeMap<String, Integer> map_TokenName_TokenIndexNOTmissing =new TreeMap<String, Integer>();
//			TreeMap<String, Integer> map_headerTokenOFall_SEQvertex=new TreeMap<String, Integer>();
//			//continous SEQ ID for vertex id needed
//			TreeMap<Integer, String> map_SEQ4vertexID__TokenName =new TreeMap<Integer, String>();
//			TreeMap<String,Integer> map__TokenName_SEQ4vertexID=new TreeMap<String, Integer>();
//			writerDebug=new FileWriter(new File(baseFolderINPUT +"debug2.txt"  ) );
//			int curr_xp_num=0; int last_lineNo_used=0;
//			// 
//			while(c<maxfile){
//				 String curr_File=baseFolderINPUT+arr_list_of_files[c];
//				 if(curr_File.indexOf("Store")>=0 || curr_File.indexOf(".g")>=0 || curr_File.indexOf("debug")>=0
//						|| new File(curr_File).isDirectory()==true 	)
//				 	{c++; continue;}
//				 System.out.println("loading :"+curr_File);
//				 
//				 writer=new FileWriter(new File(curr_File.replace(".txt", "").replace(".csv","") +".g"  ) );
//				 
//				 // load 
//				 mapLines=LoadGoogleAlert_FileToMap.loadEachGoogleAlertLineToMap(curr_File , ",");
////				 //BEGIN read and create VERTEXID
////				 //read first line only and find NON-missing features. .feature!="na"
////				 String [] arr_first_line=mapLines.get(1).split(" AND ");
////				int c2=0;
////				while(c2<arr_first_line.length){
////					//
////					if(!arr_first_line[c2].equalsIgnoreCase("na") ){
////						map_TokenIndexNOTmissing_TokenName.put(c2, map_SEQ_headerTokenOFall.get(c2));
////						map_TokenName_TokenIndexNOTmissing.put(map_SEQ_headerTokenOFall.get(c2), c2);
////						//continous SEQ ID for vertex id needed
////						if(c2>1 ) {  //skip dataid and local_15min
////							int sz=map_SEQ4vertexID__TokenName.size()+1;
////							map_SEQ4vertexID__TokenName.put(sz, map_SEQ_headerTokenOFall.get(c2));
////							map__TokenName_SEQ4vertexID.put(map_SEQ_headerTokenOFall.get(c2), sz);
////						}
////					}
////					c2++;
////				}
////				//add "home" node 
////				int sz=map_SEQ4vertexID__TokenName.size()+1;
////				map_SEQ4vertexID__TokenName.put(sz, "home");
////				map__TokenName_SEQ4vertexID.put("home", sz);
////				//END read and create VERTEXID
//				
//				curr_xp_num=0;
//				int past_dataID=-1;int curr_dataID=-1;
//				
//				try{
//				 //
//				 for(int lineNo:mapLines.keySet()){
//					 String eachLine=mapLines.get(lineNo);
//					 String [] arr_eachFeature=eachLine.split(" AND ");
//					 String dateID_N_dateTime="dataID:"+arr_eachFeature[0]+",dt:"+arr_eachFeature[1];
//					 
//					 try{
//						 curr_dataID=Integer.valueOf(arr_eachFeature[0]);
//					 }
//					 catch(Exception e){
//						 System.out.println("eachLine:"+eachLine+" file:"+curr_File);
//						 e.printStackTrace();
//					 }
//					 // ---------------------------------------
//					 // BEGIN read and create VERTEXID
//					 if( past_dataID==-1  || (past_dataID>0 &&  past_dataID!=curr_dataID )){
//						// clear 
//						map_SEQ4vertexID__TokenName=new TreeMap<Integer, String>();
//						map__TokenName_SEQ4vertexID=new TreeMap<String, Integer>();
//						int c2=0;
//						while(c2<arr_eachFeature.length){
//							//
//							if(!arr_eachFeature[c2].equalsIgnoreCase("na") && 
//									!arr_eachFeature[c2].equalsIgnoreCase(NA_string)){
//								map_TokenIndexNOTmissing_TokenName.put(c2, map_SEQ_headerTokenOFall.get(c2));
//								map_TokenName_TokenIndexNOTmissing.put(map_SEQ_headerTokenOFall.get(c2), c2);
//								//continous SEQ ID for vertex id needed
//								if(c2>2 ){  //skip dataid and local_15min,use
//									// (debug) c2==0
//									int sz=map_SEQ4vertexID__TokenName.size()+1;
//									map_SEQ4vertexID__TokenName.put(sz, map_SEQ_headerTokenOFall.get(c2));
//									map__TokenName_SEQ4vertexID.put(map_SEQ_headerTokenOFall.get(c2), sz);
//								}
//								//(debug) --- below for debug purpose only (comment later)
////								if(c2==1){
////									int sz=map_SEQ4vertexID__TokenName.size()+1;
////									map_SEQ4vertexID__TokenName.put(sz, map_SEQ_headerTokenOFall.get(c2));
////									map__TokenName_SEQ4vertexID.put(map_SEQ_headerTokenOFall.get(c2), sz);
////								}
//							}
//							c2++;
//						}
//						//add "home" node 
//						int sz=map_SEQ4vertexID__TokenName.size()+1;
//						map_SEQ4vertexID__TokenName.put(sz, "home");
//						map__TokenName_SEQ4vertexID.put("home", sz);
//						
//						//add "dataID" node 
////						sz=map_SEQ4vertexID__TokenName.size()+1;
////						map_SEQ4vertexID__TokenName.put(sz, "dataID");
////						map__TokenName_SEQ4vertexID.put("dataID", sz);
//						//
//						
//						
////						System.out.println("dataid:"+curr_dataID+ "\n map_TokenName_TokenIndexNOTmissing:"+map_TokenName_TokenIndexNOTmissing
////											+"\n map__TokenName_SEQ4vertexID:"+map__TokenName_SEQ4vertexID);
//					 }
//					 ////END read and create VERTEXID
//					 // ---------------------------------------
//
////					 if(lineNo>33) break;
//					 
//
//					 // Number of vertex in this XP less than offset, DONT write 
//					 if(map_SEQ4vertexID__TokenName.size()<=OFFSET_dont_write_lesserTHAN_TOTALvertex  
//					 		&& OFFSET_dont_write_lesserTHAN_TOTALvertex>0){
//						 continue;
//					 }
//					 curr_xp_num++;
//					 if(isSOPprint)
//						 System.out.println("XP # "+curr_xp_num);
//					 
////					 if(last_lineNo_used-lineNo==1){
//					 
//					//BELOW is for GBAD
//					 if(is_run_for_GBAD_or_SUBDUE==1){
//						writer.append("XP # "+curr_xp_num+"\n"); //  lineNo+"\n");
//						
//					 }
//					 else{
//						//BELOW is for SUBDUE 
//						writer.append("XP"+"\n"); //  lineNo+"\n");
//					 }
//						
////					 	last_lineNo_used=lineNo;
////					 }
////					 else{
////						 last_lineNo_used++;
////						 writer.append("XP # "+last_lineNo_used+"\n"); //  lineNo+"\n"); 
////					 }
//					 //BELOW is for GBAD
//					 if(is_run_for_GBAD_or_SUBDUE==1){
//						 writer.append("//"+dateID_N_dateTime+"\n");
//					 }
//					 else{
//						 writer.append("%"+dateID_N_dateTime+"\n");
//					 }
//					 
//					 //(create VERTEX) iterate <continous SEQ ID for vertex id, tokenName> 
//					 for(int vertexID:map_SEQ4vertexID__TokenName.keySet()){
//						 if(isSOPprint){
//							 System.out.println("v "+vertexID+" \""+map_SEQ4vertexID__TokenName.get(vertexID)+"\"");
//						 }
//						 writer.append("v "+vertexID+" \""+map_SEQ4vertexID__TokenName.get(vertexID)+"\""+"\n");
//					 }
//					 
//					 // ------------ create EDGE start
//					 //edge "grid" and "home"
//					 if(map__TokenName_SEQ4vertexID.containsKey("grid")){
//						 String edge= "\""+ arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("grid") ] +"\"" ;
//						 //overwrite
//						 edge= "\""+ "with-usage-from-a" +"\"" ;
//						 
//						 if(isSOPprint){
//							 System.out.println("d "+ map__TokenName_SEQ4vertexID.get("grid")+" "+map__TokenName_SEQ4vertexID.get("home")+" "
//									 					+ arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("grid") ] );
//						 }
//						 
//						 if(!edge.equalsIgnoreCase(NA_string) && !edge.equalsIgnoreCase("na") )
//							 writer.append("d "+ map__TokenName_SEQ4vertexID.get("grid")+" "+map__TokenName_SEQ4vertexID.get("home")
//							 					+" "+  edge
//	//								 			+ "use2.0" 
//									 			+ "\n");
//						 
//						 
//					 }
//					 	
//					    //dataID
//					    //(Debug)
//					    String edge="\""+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("dataid") ]+"\"";
//						 if(isSOPprint){
//							 System.out.println("d "+ map__TokenName_SEQ4vertexID.get("dataid")+" "+map__TokenName_SEQ4vertexID.get("home")+" "
//									 					+ edge );
//						 }
////						 if(!edge.equalsIgnoreCase(NA_string))
////							 writer.append("d "+ map__TokenName_SEQ4vertexID.get("dataid")+" "+map__TokenName_SEQ4vertexID.get("home")+" "
////					 					       				+ edge +"\n");
//
//						 // local_15min
//						 edge="\""+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("local_15min") ]+"\"";
//						 //(DEBUG) local_15min 
////						 if(edge.equalsIgnoreCase(NA_string))
////							 writer.append("d "+ map__TokenName_SEQ4vertexID.get("local_15min")+" "+map__TokenName_SEQ4vertexID.get("home")+" "
////					       							+ edge +"\n");
//						
//					 //gen
//					 if(map__TokenName_SEQ4vertexID.containsKey("gen") && map__TokenName_SEQ4vertexID.containsKey("grid") ){
//						 edge="\""+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("gen") ]+"\"";
//						 if(isSOPprint){
//						 System.out.println("d "+ map__TokenName_SEQ4vertexID.get("gen")+" "+map__TokenName_SEQ4vertexID.get("grid")+" "
//				 					       + arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("gen") ] );
//						 }
//						 
//						 if(!edge.equalsIgnoreCase("na") && !edge.equalsIgnoreCase(NA_string) )
//							 writer.append("d "+ map__TokenName_SEQ4vertexID.get("gen")+" "+map__TokenName_SEQ4vertexID.get("grid")+" "+  edge 
////									 		+"use100"
//					 						+ "\n");
//						 
//					 }
//					 // gen present and grid not
//					 else if(map__TokenName_SEQ4vertexID.containsKey("gen") && !map__TokenName_SEQ4vertexID.containsKey("grid") ){
//						 edge="\""+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("gen") ] +"\"";
//						 
//						 if(!edge.equalsIgnoreCase("na") && !edge.equalsIgnoreCase(NA_string) )
//							 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("gen")+" "+edge
//	//								 						+"use3.0"
//									 						+"\n");
//						 
//					 }
//					 //use
////					 if(map__TokenName_SEQ4vertexID.containsKey("use") && map__TokenName_SEQ4vertexID.containsKey("grid") ){
////						 
////						 edge="\""+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("use") ] +"\"";
////						 if(isSOPprint){
////							 System.out.println("d "+ map__TokenName_SEQ4vertexID.get("grid")+" "+map__TokenName_SEQ4vertexID.get("use")+" "
////					 					+ arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("use") ] );
////						 }
////						 if(!edge.equalsIgnoreCase("na") && !edge.equalsIgnoreCase(NA_string) )
////							 writer.append("d "+ map__TokenName_SEQ4vertexID.get("grid")+" "+map__TokenName_SEQ4vertexID.get("use")+" "+  edge
////	//				 								+"use2"
////					 								+"\n");
////					 }
////					 //use present, but not grid
////					 else if(map__TokenName_SEQ4vertexID.containsKey("use") && !map__TokenName_SEQ4vertexID.containsKey("grid")){
////						 edge="\""+ arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("use") ] +"\"";
////						 
////						 if(!edge.equalsIgnoreCase("na") && !edge.equalsIgnoreCase(NA_string) )
////							 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("use")+" "
////					 						+  edge
////	//				 						+"use1"
////					 						+"\n");
////					 }
//						writer.flush();
//						 // all other vertex which to be attached to "use" except "home","gen","grid"
//						 for(String tokenName:map__TokenName_SEQ4vertexID.keySet()){
//							 
//							 if(tokenName.equalsIgnoreCase("home") || tokenName.equalsIgnoreCase("grid") || tokenName.equalsIgnoreCase("gen") 
//									 		|| tokenName.equalsIgnoreCase("use")){
//								 continue;
//							 }
//							 //
//							 if(map_TokenName_TokenIndexNOTmissing.containsKey(tokenName)){
//								 
//								 try{
//									 edge="\""+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get(tokenName) ]+"\"";
//								 }catch(Exception e){
//									 System.out.println("ERROR:" +tokenName+" arr_eachFeature.len:"+arr_eachFeature.length);
//									 //(debug) uncomment this later when run list of files
////									 e.printStackTrace();
//								 }
//								 
//								 //
//								 if(!edge.equalsIgnoreCase("na") && !edge.equalsIgnoreCase(NA_string) ){
//									 if(isSOPprint){
//										 System.out.println("d "+ map__TokenName_SEQ4vertexID.get("use")+" "+map__TokenName_SEQ4vertexID.get(tokenName)+" "
//								 							+ edge );
//									 }
//									 // 
////									 if(map__TokenName_SEQ4vertexID.containsKey("use")){
////										 writer.append("d "+ map__TokenName_SEQ4vertexID.get("use")+" "+map__TokenName_SEQ4vertexID.get(tokenName)+" "+ edge
//////								 							+"use5" 
////								 							+"\n");
////									 }
////									 else 
//									 if(map__TokenName_SEQ4vertexID.containsKey("grid")){
//										 writer.append("d "+ map__TokenName_SEQ4vertexID.get("grid")+" "+map__TokenName_SEQ4vertexID.get(tokenName)+" "+ edge 
////												 	+"use10"
//												 	+"\n");
//									 }
//									 else if(map__TokenName_SEQ4vertexID.containsKey("home")){
//										 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get(tokenName)+" "+ edge
////												 	+"use11"
//						 							+"\n");
//									 }
//								 }
//							 }
//						 } //END for(String tokenName:map__TokenName_SEQ4vertexID.keySet()){
//					 // ------------ create EDGE end					 
//						 
//					past_dataID=curr_dataID;
//					writer.flush();
//				 } //for(int lineNo:mapLines.keySet()){
//				 
//				}
//				catch(Exception e){
//					e.printStackTrace();
//				}
//				 
//				 c++;
//			} // while(c<maxfile){
//			
//			
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	 	
	
	    //Second slide has Topology->https://docs.google.com/presentation/d/1NloLOxxDyLSm_F3HFAgokEG3lSUHbIsPdsf8jV5YNoU/edit#slide=id.g1897bc53b5_0_5
		//p21_convert_to_GBAD_topology2
		public static void p21_convert_to_GBAD_topology2(	
												String 						baseFolderINPUT,
												String 						baseFolderOUTPUT,
												TreeMap<Integer, String> 	map_SEQ_headerTokenOFall, 
												TreeMap<String, Integer> 	map_headerTokenOFall_SEQ,
												int 					 	OFFSET_dont_write_lesserTHAN_TOTALvertex, 
												TreeMap<String, String> 	map_subGroupType_N_listOFdevicesCSV,
												TreeMap<Integer, String> 	map_interestedDataIDs_2014_AustinTexas_asVALUES,
												TreeMap<Integer, String> 	map_inFile_seq_eachLine1_dataID_ALL_2014_ANYCITY_asVALUES,
												boolean 					isSOPprint					
												){
			
			String [] arr_list_of_files=new String[200000];
			String NA_string="\"na\"";
			FileWriter writer=null; FileWriter writerDebug=null;
			TreeMap<String, TreeMap<String, String>> map_subGroupType_N_devicesASkey =new TreeMap<String, TreeMap<String,String>>();
			
			int is_run_for_GBAD_or_SUBDUE=1;  //{1=GBAD, 2 =SUBDUE}
			try{
				// split each device from CSV corresponds to each subGroupType
				for(String currSubGroupType:map_subGroupType_N_listOFdevicesCSV.keySet()){
					String [] arr_devices_of_currSubGroupType=map_subGroupType_N_listOFdevicesCSV.get(currSubGroupType).split(",");
					
					int c=0;
					while(c<arr_devices_of_currSubGroupType.length){
						String currDevice= arr_devices_of_currSubGroupType[c];
						// 
						if(!map_subGroupType_N_devicesASkey.containsKey(currSubGroupType)){
							TreeMap<String, String> temp=new TreeMap<String, String>();
							temp.put(currDevice, "");
							map_subGroupType_N_devicesASkey.put(currSubGroupType, temp);
						}
						else{
							TreeMap<String, String> temp=map_subGroupType_N_devicesASkey.get(currSubGroupType);
							temp.put(currDevice, "");
							map_subGroupType_N_devicesASkey.put(currSubGroupType, temp);
						}
						c++;
					}
					
				}
				
				
				int c=0;
				System.out.println("baseFolderINPUT:"+baseFolderINPUT);
				arr_list_of_files=new File(baseFolderINPUT).list();
				System.out.println("arr_list_of_files:"+arr_list_of_files);
				int maxfile=arr_list_of_files.length;
				TreeMap<Integer, String> mapLines=new TreeMap<Integer, String>();
				
				TreeMap<Integer, String> map_TokenIndexNOTmissing_TokenName =new TreeMap<Integer, String>();
				TreeMap<String, Integer> map_TokenName_TokenIndexNOTmissing =new TreeMap<String, Integer>();
				TreeMap<String, Integer> map_headerTokenOFall_SEQvertex=new TreeMap<String, Integer>();
				//continous SEQ ID for vertex id needed
				TreeMap<Integer, String> map_SEQ4vertexID__TokenName =new TreeMap<Integer, String>();
				TreeMap<String,Integer> map__TokenName_SEQ4vertexID=new TreeMap<String, Integer>();
				writerDebug=new FileWriter(new File(baseFolderINPUT +"debug2.txt"  ) );
				int curr_xp_num=0; int last_lineNo_used=0;
				// 
				while(c<maxfile){
					 String curr_File=baseFolderINPUT+arr_list_of_files[c];
					 //
					 if(curr_File.indexOf("Store")>=0 || curr_File.indexOf(".g")>=0 || curr_File.indexOf("debug")>=0
							|| new File(curr_File).isDirectory()==true 	)
					 	{c++; continue;}
					 System.out.println("loading :"+curr_File);
					 
					 writer=new FileWriter(new File(curr_File.replace(".txt", "").replace(".csv","") +".g"  ) );
					 
					 // load 
					 mapLines=LoadGoogleAlert_FileToMap.loadEachGoogleAlertLineToMap(curr_File ,",", true);
//					 //BEGIN read and create VERTEXID
//					 //read first line only and find NON-missing features. .feature!="na"
//					 String [] arr_first_line=mapLines.get(1).split(" AND ");
//					int c2=0;
//					while(c2<arr_first_line.length){
//						//
//						if(!arr_first_line[c2].equalsIgnoreCase("na") ){
//							map_TokenIndexNOTmissing_TokenName.put(c2, map_SEQ_headerTokenOFall.get(c2));
//							map_TokenName_TokenIndexNOTmissing.put(map_SEQ_headerTokenOFall.get(c2), c2);
//							//continous SEQ ID for vertex id needed
//							if(c2>1 ) {  //skip dataid and local_15min
//								int sz=map_SEQ4vertexID__TokenName.size()+1;
//								map_SEQ4vertexID__TokenName.put(sz, map_SEQ_headerTokenOFall.get(c2));
//								map__TokenName_SEQ4vertexID.put(map_SEQ_headerTokenOFall.get(c2), sz);
//							}
//						}
//						c2++;
//					}
//					//add "home" node 
//					int sz=map_SEQ4vertexID__TokenName.size()+1;
//					map_SEQ4vertexID__TokenName.put(sz, "home");
//					map__TokenName_SEQ4vertexID.put("home", sz);
//					//END read and create VERTEXID
					
					curr_xp_num=0;
					int past_dataID=-1;int curr_dataID=-1;
					
					try{
					 //
					 for(int lineNo:mapLines.keySet()){
						 String eachLine=mapLines.get(lineNo);
						 String [] arr_eachFeature=eachLine.split(" AND ");
						 String dateID_N_dateTime="dataID:"+arr_eachFeature[0]+",dt:"+arr_eachFeature[1];
						 
						 try{
							 curr_dataID=Integer.valueOf(arr_eachFeature[0]);
							 
							 // is present in 2014 Austing Texas ? (DEBUG)
							 if(!map_interestedDataIDs_2014_AustinTexas_asVALUES.containsValue(String.valueOf(curr_dataID))
									&& map_inFile_seq_eachLine1_dataID_ALL_2014_ANYCITY_asVALUES.containsValue(String.valueOf(curr_dataID))
									 ){
								 writerDebug.append("\n NOT in 2014 Austin Texas:"+curr_dataID);
								 writerDebug.flush();
								 continue;
							 }
							 
						 }
						 catch(Exception e){
							 System.out.println("eachLine:"+eachLine+" file:"+curr_File);
							 e.printStackTrace();
						 }
						 // ---------------------------------------
						 // BEGIN read and create VERTEXID
						 if( past_dataID==-1  || (past_dataID>0 &&  past_dataID!=curr_dataID )
//							|| (eachLine.toLowerCase().indexOf("vvery") >=0 // <---Structural Anomalies (OR it can be statistical also)
							) {
							// clear 
							map_SEQ4vertexID__TokenName=new TreeMap<Integer, String>();
							map__TokenName_SEQ4vertexID=new TreeMap<String, Integer>();
							int c2=0;
							while(c2<arr_eachFeature.length){
								// NOT empty
								if(!arr_eachFeature[c2].equalsIgnoreCase("na") && 
										!arr_eachFeature[c2].equalsIgnoreCase(NA_string)){
									map_TokenIndexNOTmissing_TokenName.put(c2, map_SEQ_headerTokenOFall.get(c2));
									map_TokenName_TokenIndexNOTmissing.put(map_SEQ_headerTokenOFall.get(c2), c2);
									//continous SEQ ID for vertex id needed
									if(c2>2 ){  //skip dataid and local_15min, use
										// (debug) c2==0
										int sz=map_SEQ4vertexID__TokenName.size()+1;
										map_SEQ4vertexID__TokenName.put(sz, map_SEQ_headerTokenOFall.get(c2));
										map__TokenName_SEQ4vertexID.put(map_SEQ_headerTokenOFall.get(c2), sz);
									}
									//(debug) --- below for debug purpose only (comment later)
//									if(c2==1){
//										int sz=map_SEQ4vertexID__TokenName.size()+1;
//										map_SEQ4vertexID__TokenName.put(sz, map_SEQ_headerTokenOFall.get(c2));
//										map__TokenName_SEQ4vertexID.put(map_SEQ_headerTokenOFall.get(c2), sz);
//									}
								}
								c2++;
							}
							//add "home" node 
							int sz=map_SEQ4vertexID__TokenName.size()+1;
							map_SEQ4vertexID__TokenName.put(sz, "home");
							map__TokenName_SEQ4vertexID.put("home", sz);
							//add set of nodes below->		Kitchen,LivingRoom,BedRoom,RestRoom,Utility,Outdoor, smallAppliance,bigAppliance
							//add "Kitchen" node
//							sz=map_SEQ4vertexID__TokenName.size()+1;
//							map_SEQ4vertexID__TokenName.put(sz, "Kitchen");
//							map__TokenName_SEQ4vertexID.put("Kitchen", sz);
							// add "smallAppliance" and "bigAppliance" nodes-- which will be under "Kitchen" node..
							// add "smallAppliance"
							sz=map_SEQ4vertexID__TokenName.size()+1;
							map_SEQ4vertexID__TokenName.put(sz, "smallAppliance");
							map__TokenName_SEQ4vertexID.put("smallAppliance", sz);
							// add "bigAppliance"
							sz=map_SEQ4vertexID__TokenName.size()+1;
							map_SEQ4vertexID__TokenName.put(sz, "bigAppliance");
							map__TokenName_SEQ4vertexID.put("bigAppliance", sz);
							// add "Utility" node
							sz=map_SEQ4vertexID__TokenName.size()+1;
							map_SEQ4vertexID__TokenName.put(sz, "UtilityRoom");
							map__TokenName_SEQ4vertexID.put("UtilityRoom", sz);
							// add "BedRoom" node
							sz=map_SEQ4vertexID__TokenName.size()+1;
							map_SEQ4vertexID__TokenName.put(sz, "BedRoom");
							map__TokenName_SEQ4vertexID.put("BedRoom", sz);
//							//add "RestRoom" node (removed as per professor discuss)
//							sz=map_SEQ4vertexID__TokenName.size()+1;
//							map_SEQ4vertexID__TokenName.put(sz, "RestRoom");
//							map__TokenName_SEQ4vertexID.put("RestRoom", sz);
							
							//add "LivingRoom" node
							sz=map_SEQ4vertexID__TokenName.size()+1;
							map_SEQ4vertexID__TokenName.put(sz, "LivingRoom");
							map__TokenName_SEQ4vertexID.put("LivingRoom", sz);
							
							//add "Outdoor" node
							sz=map_SEQ4vertexID__TokenName.size()+1;
							map_SEQ4vertexID__TokenName.put(sz, "OutdoorSpace");
							map__TokenName_SEQ4vertexID.put("OutdoorSpace", sz);
							
							//add "dataID" node 
//							sz=map_SEQ4vertexID__TokenName.size()+1;
//							map_SEQ4vertexID__TokenName.put(sz, "dataID");
//							map__TokenName_SEQ4vertexID.put("dataID", sz);
							
//							System.out.println("dataid:"+curr_dataID+ "\n map_TokenName_TokenIndexNOTmissing:"+map_TokenName_TokenIndexNOTmissing
//												+"\n map__TokenName_SEQ4vertexID:"+map__TokenName_SEQ4vertexID);
							
						 } //if( past_dataID==-1  || (past_dataID>0 &&  past_dataID!=curr_dataID )){
						 ////END read and create VERTEXID
						 // ---------------------------------------

//						 if(lineNo>33) break;
						 

						 // Number of vertex in this XP less than offset, DONT write 
						 if(map_SEQ4vertexID__TokenName.size()<=OFFSET_dont_write_lesserTHAN_TOTALvertex  
						 		&& OFFSET_dont_write_lesserTHAN_TOTALvertex>0){
							 continue;
						 }
						 curr_xp_num++;
						 if(isSOPprint)
							 System.out.println("XP # "+curr_xp_num);
						 
//						 if(last_lineNo_used-lineNo==1){
						 
						//BELOW is for GBAD
						 if(is_run_for_GBAD_or_SUBDUE==1){
							writer.append("XP # "+curr_xp_num+"\n"); //  lineNo+"\n");
							
						 }
						 else{
							//BELOW is for SUBDUE 
							writer.append("XP"+"\n"); //  lineNo+"\n");
						 }
							
//						 	last_lineNo_used=lineNo;
//						 }
//						 else{
//							 last_lineNo_used++;
//							 writer.append("XP # "+last_lineNo_used+"\n"); //  lineNo+"\n"); 
//						 }
						 //BELOW is for GBAD
						 if(is_run_for_GBAD_or_SUBDUE==1){
							 writer.append("//"+dateID_N_dateTime+"\n");
						 }
						 else{
							 writer.append("%"+dateID_N_dateTime+"\n");
						 }
						 
						 //(create VERTEX) iterate <continous SEQ ID for vertex id, tokenName> 
						 for(int vertexID:map_SEQ4vertexID__TokenName.keySet()){
							 if(isSOPprint){
								 System.out.println("v "+vertexID+" \""+map_SEQ4vertexID__TokenName.get(vertexID)+"\"");
							 }
							 writer.append("v "+vertexID+" \""+map_SEQ4vertexID__TokenName.get(vertexID)+"\""+"\n");
						 }
						 
						 // ################################ create EDGE start ################################

						 // ---------------
						 // edge for "grid" and "home"
						 if(map__TokenName_SEQ4vertexID.containsKey("grid")){
							 String edge= "\""+ arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("grid") ] +"\"" ;
							 //overwrite
							 edge= "\""+ "with-usage-from-a" +"\"" ;
							 if(isSOPprint){
								 System.out.println("d "+ map__TokenName_SEQ4vertexID.get("grid")+" "+map__TokenName_SEQ4vertexID.get("home")+" "
										 					+ arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("grid") ] );
							 }
							 
							 if(!edge.equalsIgnoreCase(NA_string) && !edge.equalsIgnoreCase("na") )
								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("grid")+" "+map__TokenName_SEQ4vertexID.get("home")
								 					+" "+  edge
		//								 			+ "use2.0" 
										 			+ "\n");
							 
							 
						 }
						 // ---------------create edges for home and smallAppliance,bigAppliance,LivingRoom,BedRoom,RestRoom,Utility,Outdoor
						 // edge for "home" and "Kitchen"
//						 if(map__TokenName_SEQ4vertexID.containsKey("Kitchen")){
//							 String edge= "\""+  "that-has-a"  +"\"" ;
//							 if(isSOPprint){
//								 System.out.println("d "+  map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("Kitchen")+" "+  edge);
//							 }
//							 
//							 if(!edge.equalsIgnoreCase(NA_string) && !edge.equalsIgnoreCase("na") )
//								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("Kitchen")
//								 					+" "+  edge
//		//								 			+ "use2.0" 
//										 			+ "\n");
//							 
//							 
//						}
						 // ---------------
						 // edge for "home" and "smallAppliance"
						 if(map__TokenName_SEQ4vertexID.containsKey("smallAppliance")){
							 String edge= "\""+  "that-has-a"  +"\"" ;
							 if(isSOPprint){
								 System.out.println("d "+  map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("smallAppliance")+" "+  edge);
							 }
							 
							 if(!edge.equalsIgnoreCase(NA_string) && !edge.equalsIgnoreCase("na") )
								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("smallAppliance")
								 					+" "+  edge
		//								 			+ "use2.0" 
										 			+ "\n");
							 
							 
						}
						 	// ---------------
							// edge for "home" and "bigAppliance"
						 if(map__TokenName_SEQ4vertexID.containsKey("bigAppliance")){
							 String edge= "\""+  "that-has-a"  +"\"" ;
							 if(isSOPprint){
								 System.out.println("d "+  map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("bigAppliance")+" "+  edge);
							 }
							 
							 if(!edge.equalsIgnoreCase(NA_string) && !edge.equalsIgnoreCase("na") )
								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("bigAppliance")
								 					+" "+  edge
		//								 			+ "use2.0" 
										 			+ "\n");
							 
							 
						}
						 	// ---------------
							// edge for "home" and "RestRoom" (RestRoom not needed as per Prof)
//						 if(map__TokenName_SEQ4vertexID.containsKey("RestRoom")){
//							 String edge= "\""+  "has"  +"\"" ;
//							 if(isSOPprint){
//								 System.out.println("d "+  map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("RestRoom")+" "+  edge);
//							 }
//							 
//							 if(!edge.equalsIgnoreCase(NA_string) && !edge.equalsIgnoreCase("na") )
//								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("RestRoom")
//								 					+" "+  edge
//		//								 			+ "use2.0" 
//										 			+ "\n");
//							 
//							 
//						}
						 	// ---------------
							// edge for "home" and "LivingRoom"
						 if(map__TokenName_SEQ4vertexID.containsKey("LivingRoom")){
							 String edge= "\""+  "that-has-a"  +"\"" ;
							 if(isSOPprint){
								 System.out.println("d "+  map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("LivingRoom")+" "+  edge);
							 }
							 
							 if(!edge.equalsIgnoreCase(NA_string) && !edge.equalsIgnoreCase("na") )
								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("LivingRoom")
								 					+" "+  edge
		//								 			+ "use2.0" 
										 			+ "\n");
							 
							 
						}
						 	// ---------------
							// edge for "home" and "Outdoor"
						 if(map__TokenName_SEQ4vertexID.containsKey("OutdoorSpace")){
							 String edge= "\""+  "that-has-a"  +"\"" ;
							 if(isSOPprint){
								 System.out.println("d "+  map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("OutdoorSpace")+" "+  edge);
							 }
							 
							 if(!edge.equalsIgnoreCase(NA_string) && !edge.equalsIgnoreCase("na") )
								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("OutdoorSpace")
								 					+" "+  edge
		//								 			+ "use2.0" 
										 			+ "\n");
							 
						}
						 	// ---------------
							// edge for "home" and "BedRoom"
						 if(map__TokenName_SEQ4vertexID.containsKey("BedRoom")){
							 String edge= "\""+  "that-has-a"  +"\"" ;
							 if(isSOPprint){
								 System.out.println("d "+  map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("BedRoom")+" "+  edge);
							 }
							 
							 if(!edge.equalsIgnoreCase(NA_string) && !edge.equalsIgnoreCase("na") )
								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("BedRoom")
								 					+" "+  edge
		//								 			+ "use2.0" 
										 			+ "\n");
							 
						}
						 	// ---------------
							// edge for "home" and "Utility"
						 if(map__TokenName_SEQ4vertexID.containsKey("UtilityRoom")){
							 String edge= "\""+  "that-has-a"  +"\"" ;
							 if(isSOPprint){
								 System.out.println("d "+  map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("UtilityRoom")+" "+  edge);
							 }
							 
							 if(!edge.equalsIgnoreCase(NA_string) && !edge.equalsIgnoreCase("na") )
								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("UtilityRoom")
								 					+" "+  edge
		//								 			+ "use2.0" 
										 			+ "\n");
							 
						}
						  
						 
						    //-------BEGIN comment for "dataID"
						    //(Debug)
						    String edge="\""+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("dataid") ]+"\"";
							 if(isSOPprint){
								 System.out.println("d "+ map__TokenName_SEQ4vertexID.get("dataid")+" "+map__TokenName_SEQ4vertexID.get("home")+" "
										 					+ edge );
							 }
//							 if(!edge.equalsIgnoreCase(NA_string))
//								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("dataid")+" "+map__TokenName_SEQ4vertexID.get("home")+" "
//						 					       				+ edge +"\n");

							 // local_15min
							 edge="\""+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("local_15min") ]+"\"";
							 //(DEBUG) local_15min 
//							 if(edge.equalsIgnoreCase(NA_string))
//								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("local_15min")+" "+map__TokenName_SEQ4vertexID.get("home")+" "
//						       							+ edge +"\n");
							
							//-------END comment for "dataID"
						  ///---------------------------------------------------------------------
						 //edge between "gen" and "grid"
						 if(map__TokenName_SEQ4vertexID.containsKey("gen") && map__TokenName_SEQ4vertexID.containsKey("grid") ){
							 edge="\""+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("gen") ]+"\"";
							 //overwrite
							 edge= "\""+ "with-usage-from-a" +"\"" ;
							 if(isSOPprint){
								 System.out.println("d "+ map__TokenName_SEQ4vertexID.get("gen")+" "+map__TokenName_SEQ4vertexID.get("grid")+" "
						 					       + arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("gen") ] );
							 }
							 
							 if(!edge.equalsIgnoreCase("na") && !edge.equalsIgnoreCase(NA_string) )
								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("gen")+" "+map__TokenName_SEQ4vertexID.get("grid")+" "+  edge 
//										 		+"use100"
						 						+ "\n");
							 
						 }
						 // "gen" present and "grid" not
						 else if(map__TokenName_SEQ4vertexID.containsKey("gen") && !map__TokenName_SEQ4vertexID.containsKey("grid") ){
							 edge="\""+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("gen") ] +"\"";
							 //overwrite
							 edge= "\""+ "with-usage-from-a" +"\"" ;
							 if(!edge.equalsIgnoreCase("na") && !edge.equalsIgnoreCase(NA_string) )
								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("gen")+" "
										 			+map__TokenName_SEQ4vertexID.get("home")+" "+edge
		//								 						+"use3.0"
										 						+"\n");
							 
						 }
						 ///---------------------------------------------------------------------
						 // "use" node
//						 if(map__TokenName_SEQ4vertexID.containsKey("use") && map__TokenName_SEQ4vertexID.containsKey("grid") ){
//							 
//							 edge="\""+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("use") ] +"\"";
//							 //overwrite
//							 edge= "\""+ "with-usage-from-a" +"\"" ;
//							 if(isSOPprint){
//								 System.out.println("d "+ map__TokenName_SEQ4vertexID.get("grid")+" "+map__TokenName_SEQ4vertexID.get("use")+" "
//						 					+ arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("use") ] );
//							 }
//							 if(!edge.equalsIgnoreCase("na") && !edge.equalsIgnoreCase(NA_string) )
//								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("grid")+" "+map__TokenName_SEQ4vertexID.get("use")+" "+  edge
//		//				 								+"use2"
//						 								+"\n");
//						 }
						 //use present, but not grid
//						 else if(map__TokenName_SEQ4vertexID.containsKey("use") && !map__TokenName_SEQ4vertexID.containsKey("grid")){
//							 edge="\""+ arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get("use") ] +"\"";
//							 //overwrite
//							 edge= "\""+ "with-usage-from-a" +"\"" ;
//							 if(!edge.equalsIgnoreCase("na") && !edge.equalsIgnoreCase(NA_string) )
//								 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get("use")+" "
//						 						+  edge
//		//				 						+"use1"
//						 						+"\n");
//						 }
							writer.flush();
							
					   ///---------------------------------------------------------------------
						
							 ///---------------------------------------------------------------------
							 // all "OTHER" vertex which has to be attached to subgroup nodes such as "Outdoor","LivingRoom","RestRoom",
							// 																		  "bigAppliance","smallAppliance"
							// all OTHER vertex to be attached to "home"
							 for(String tokenName:map__TokenName_SEQ4vertexID.keySet()){
								 //
								 if(tokenName.equalsIgnoreCase("home") || tokenName.equalsIgnoreCase("grid") || tokenName.equalsIgnoreCase("gen") 
										 		|| tokenName.equalsIgnoreCase("use")
										 		|| tokenName.equalsIgnoreCase("OutdoorSpace")
										 		|| tokenName.equalsIgnoreCase("LivingRoom")
										 		|| tokenName.equalsIgnoreCase("RestRoom")
										 		|| tokenName.equalsIgnoreCase("BedRoom")
										 		|| tokenName.equalsIgnoreCase("UtilityRoom")
										 		|| tokenName.equalsIgnoreCase("smallAppliance")
										 		|| tokenName.equalsIgnoreCase("bigAppliance")
										 		){
									 continue;
								 }
//								 System.out.println("tokenName:"+tokenName+" arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get(tokenName) ]:"+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get(tokenName) ]
//								 		+ "	<--->map_TokenName_TokenIndexNOTmissing:"+map_TokenName_TokenIndexNOTmissing
//								 		+"\n ----map__TokenName_SEQ4vertexID:"+map__TokenName_SEQ4vertexID);
								 
								 // NOT MISSING <=> NOT "na"
								 if(map_TokenName_TokenIndexNOTmissing.containsKey(tokenName)){
									 
									 try{
										 edge="\""+arr_eachFeature[ map_TokenName_TokenIndexNOTmissing.get(tokenName) ]+"\"";
										 
									 }catch(Exception e){
										 System.out.println("ERROR:" +tokenName+" arr_eachFeature.len:"+arr_eachFeature.length);
										 //(debug) uncomment this later when run list of files
//										 e.printStackTrace();
									 }
									 // NOT MISSING <=> NOT "na"
									 if(!edge.equalsIgnoreCase("na") && !edge.equalsIgnoreCase(NA_string) ){
										 //overwrite with fixed value
										 edge= "\""+  "with-usage-from-a"  +"\"" ;
										 
										 if(isSOPprint){
											 System.out.println("d "+ map__TokenName_SEQ4vertexID.get("use")+" "+map__TokenName_SEQ4vertexID.get(tokenName)+" "
									 							+ edge );
										 }
										 String curr_FOUND_subGroupType_4_currDevice="";
										 // CHECK which subgroupType, this current tokenName belongs to?
										 for(String currSubGRoupType:map_subGroupType_N_devicesASkey.keySet()){
											 //
											 if(map_subGroupType_N_devicesASkey.get(currSubGRoupType).containsKey(tokenName)){
												 curr_FOUND_subGroupType_4_currDevice=currSubGRoupType;
//												 System.out.println("2:::curr_FOUND_subGroupType_4_currDevice:"+curr_FOUND_subGroupType_4_currDevice+" tokenName:"+tokenName
//														 		+" found?="+map__TokenName_SEQ4vertexID.containsKey(curr_FOUND_subGroupType_4_currDevice)
//														 		+" map__TokenName_SEQ4vertexID:"+map__TokenName_SEQ4vertexID);
												 break;
											 }
										 }
										 
										 // if others, those devices has to be attached to "home" with edge "with-usage-from-a"
										 if(curr_FOUND_subGroupType_4_currDevice.equalsIgnoreCase("Others")){
											 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")
											 				   +" "+map__TokenName_SEQ4vertexID.get(tokenName)
											 				   +" "+ "\"" + "with-usage-from-a" + "\""
				//								 			   +"use5" 
									 						   +"\n");
										 }
										 // edge between "curr_FOUND_subGroupType_4_currDevice" and current Token
										 else if(map__TokenName_SEQ4vertexID.containsKey(curr_FOUND_subGroupType_4_currDevice)
												 ){
//											 System.out.println("--INSIDE edge create-->"+ tokenName);
											 writer.append("d "+ map__TokenName_SEQ4vertexID.get(curr_FOUND_subGroupType_4_currDevice)
											 				  +" "+map__TokenName_SEQ4vertexID.get(tokenName)
											 				  +" "+ edge
	//								 							+"use5" 
									 							+"\n");
										 }
										 
										 // only if "use" node exists
//										 if(map__TokenName_SEQ4vertexID.containsKey("use")){
//											 writer.append("d "+ map__TokenName_SEQ4vertexID.get("use")+" "+map__TokenName_SEQ4vertexID.get(tokenName)+" "+ edge
////									 							+"use5" 
//									 							+"\n");
//										 }// only if "grid" node exists
//										 else if(map__TokenName_SEQ4vertexID.containsKey("grid")){
//											 writer.append("d "+ map__TokenName_SEQ4vertexID.get("grid")+" "+map__TokenName_SEQ4vertexID.get(tokenName)+" "+ edge 
////													 	+"use10"
//													 	+"\n");
//										 }// only if "home" node exists
//										 else if(map__TokenName_SEQ4vertexID.containsKey("home")){
//											 writer.append("d "+ map__TokenName_SEQ4vertexID.get("home")+" "+map__TokenName_SEQ4vertexID.get(tokenName)+" "+ edge
////													 	+"use11"
//							 							+"\n");
//										 }
									 }// END if(!edge.equalsIgnoreCase("na") && !edge.equalsIgnoreCase(NA_string) ){
								 } // END if(map_TokenName_TokenIndexNOTmissing.containsKey(tokenName)){
							 } // END for(String tokenName:map__TokenName_SEQ4vertexID.keySet()){
							 
//							 System.out.println("map__TokenName_SEQ4vertexID:"+map__TokenName_SEQ4vertexID);
							 
						 // ------------ create EDGE end
						 ///---------------------------------------------------------------------
							 
						past_dataID=curr_dataID;
						writer.flush();
					 } //for(int lineNo:mapLines.keySet()){
					 
					}
					catch(Exception e){
						e.printStackTrace();
					}
					 
					 c++;
				} // while(c<maxfile){
				
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	// main
	public static void main(String[] args) {
		long t0 = System.nanoTime();
		
		// NOTE:
		 /**** p21 steps notes
		 * (1) split_inputFile_2_multipleEachFile1_N_minutes() <- split weekly or monthly or seconds
		 * (2) p19_p21_convert_token_with_numbers_to_AggregateNumbers() <- convert to categorical data
		 * (3) p21_convert_to_GBAD ()   ////OR may be crawler() "prob" "p5" <- create GBAD files
		 */
		
		// This will have weekly data with injected 
		/////////////////// INPUT
		String baseFolderINPUT ="/Users/lenin/Downloads/reu-cdean/RawData/daily_DONE/daily-split-SEEDED/";
		String baseFolderOUTPUT="/Users/lenin/Downloads/reu-cdean/RawData/daily_DONE/daily-split-SEEDED/";
		baseFolderINPUT="/Users/lenin/Downloads/reu-cdean/RawData/daily_try1/daily-split-NOseeding/daily_missingMonthlyMay20152016/temp/gbad/";
		baseFolderINPUT="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2014Only-numeric/categorical/categoricaltry3_clean/";
				// 	{Apartment_CSV, SingleFamilyHomes_CSV}
		baseFolderINPUT="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2014Only-numeric/categorical/categoricaltry3_clean/SingleFamilyHomes_CSV/";
		baseFolderINPUT="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2014Only-numeric_try2/categorical/Apartment/";
		/////////////////// OUTPUT
		baseFolderOUTPUT="/Users/lenin/Downloads/reu-cdean/RawData/daily_try1/daily-split-NOseeding/daily_missingMonthlyMay20152016/temp/gbad/";
				// 	{Apartment_CSV, SingleFamilyHomes_CSV}
		baseFolderOUTPUT="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2014Only-numeric/categorical/categoricaltry3_clean/SingleFamilyHomes_CSV/";
		baseFolderOUTPUT="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2014Only-numeric_try2/categorical/Apartment/";
		//manually change - {"Apartment","Single-Family Home"}
		String housingType="";
		//chose right hosingType
		if(baseFolderOUTPUT.toLowerCase().indexOf("singlefamily")>=0)
			housingType= "Single-Family Home";
		else
			housingType= "Apartment"; 
		
		// FILTER YES 
		//2014 only "2014Only-numeric/categorical/categoricaltry1/dataIDS_existsin2013_N_inTexas.txt"
//		String inFile_DataID_Texas_I_2013="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2014Only-numeric/categorical/categoricaltry1/dataIDS_existsin2013_N_inTexas.txt";
//		String inFile_DataID_Austin="/Users/lenin/Downloads/reu-cdean/RawData/all_only_UNIQUE_dataid_from_AUSTIN_TX.txt";
//		//<building_type,dataid> --< header present
//		String inFile_buildingType_dataID_austinTX="/Users/lenin/Downloads/reu-cdean/buildingType_dataID_austinTX.txt";
				
		// <2014 only DataIDs for Austin_Texas - SingleHomeFamily> 
		String inFile_DataID_Austin_2014_SingleHomeFamily="/Users/lenin/Downloads/reu-cdean/RawData/out_housingType_SingleHomeFamily_setOFdataIDs_2014.txt";
		String inFile_DataID_Austin_2014_Apartment="/Users/lenin/Downloads/reu-cdean/RawData/out_housingType_Apartment_setOFdataIDs_2014.txt";
		// all 2014 dataIDs
		String inFile_buildingType_dataID_2014_ONLY="/Users/lenin/Downloads/reu-cdean/RawData/2014_only_all_dataIDs_ONLY.txt";
		
		//Sept13_2week_categorical-first line has NOT na for grid, but na for that is there is else where
		
		String all_tokens_HEADERS_CSV=    "dataid,local_15min,use,air1,air2,air3,airwindowunit1,aquarium1,bathroom1,bathroom2,bedroom1,bedroom2,bedroom3,bedroom4"
										+ ",bedroom5,car1,clotheswasher1,clotheswasher_dryg1,diningroom1,diningroom2,dishwasher1,disposal1,drye1,dryg1,freezer1,"
										+ "furnace1,furnace2,garage1,garage2,gen,grid,heater1,housefan1,icemaker1,jacuzzi1,kitchen1,kitchen2,kitchenapp1,kitchenapp2,"
										+ "lights_plugs1,lights_plugs2,lights_plugs3,lights_plugs4,lights_plugs5,lights_plugs6,livingroom1,livingroom2,microwave1,office1,"
										+ "outsidelights_plugs1,outsidelights_plugs2,oven1,oven2,pool1,pool2,poollight1,poolpump1,pump1,range1,refrigerator1,refrigerator2,"
										+ "security1,shed1,sprinkler1,utilityroom1,venthood1,waterheater1,waterheater2,winecooler1";
		String [] arr_all_header_CSV=all_tokens_HEADERS_CSV.split(",");
		int c2=0;
		int OFFSET_dont_write_lesserTHAN_TOTALvertex=-1; // -1 or >0 value like 6;
		
	    ///////Second slide has Topology->https://docs.google.com/presentation/d/1NloLOxxDyLSm_F3HFAgokEG3lSUHbIsPdsf8jV5YNoU/edit#slide=id.g1897bc53b5_0_5
		// for each different subGroupType like kitchen, livingroom, restroom, outdoors
//	    "dataid,local_15min,use,gen,grid,air1*,air2*,air3*,airwindowunit1*,aquarium1*,bathroom1*,bathroom2*,bedroom1*,bedroom2*,bedroom3*,bedroom4*"
//		+ ",bedroom5*,car1*,clotheswasher1*,clotheswasher_dryg1*,diningroom1*,diningroom2*,dishwasher1*,disposal1*,drye1*,dryg1*,freezer1*,"
//		+ "furnace1*,furnace2*,garage1*,garage2*,heater1*,housefan1*,icemaker1*,jacuzzi1*,kitchen1*,kitchen2*,kitchenapp1*,kitchenapp2*,"
//		+ "lights_plugs1*,lights_plugs2*,lights_plugs3*,lights_plugs4*,lights_plugs5*,lights_plugs6*,livingroom1*,livingroom2*,microwave1*,office1*,"
//		+ "outsidelights_plugs1*,outsidelights_plugs2*,oven1*,oven2*,pool1*,pool2*,poollight1*,poolpump1*,pump1*,range1*,refrigerator1*,refrigerator2*,"
//		+ "security1*,shed1*,sprinkler1*,utilityroom1*,venthood1*,waterheater1*,waterheater2*,winecooler1*";
		TreeMap<String, String> map_subGroupType_N_listOFdevicesCSV=new TreeMap<String, String>();
		
		//-----BELOW two group goes to small and big appliances under node "Kitchen"
		map_subGroupType_N_listOFdevicesCSV.put( "smallAppliance", "venthood1,range1,diningroom1,diningroom2"
																  + ",kitchen1,kitchen2,kitchenapp1,kitchenapp2");
		map_subGroupType_N_listOFdevicesCSV.put( "bigAppliance", "refrigerator1,refrigerator2,winecooler1,oven1,oven2,icemaker1"
																  + ",freezer1,microwave1,dishwasher1,disposal1");
		map_subGroupType_N_listOFdevicesCSV.put( "LivingRoom", "airwindowunit1,livingroom1,livingroom2,housefan1,furnace2"
																+",aquarium1,security1,office1,heater1");
		map_subGroupType_N_listOFdevicesCSV.put( "BedRoom","bedroom1,bedroom2,bedroom3,bedroom4,bedroom5,furnace1");
		map_subGroupType_N_listOFdevicesCSV.put( "UtilityRoom", "utilityroom1,clotheswasher1,clotheswasher_dryg1,waterheater1,waterheater2,drye1,dryg1");
		map_subGroupType_N_listOFdevicesCSV.put( "OutdoorSpace", "car1,sprinkler1,outsidelights_plugs1,outsidelights_plugs2,pool1,pool2,poollight1,poolpump1"
																+",garage1,garage2,pump1,shed1,jacuzzi1");
		//devices belong to others will go to attached to "home" node
		map_subGroupType_N_listOFdevicesCSV.put( "Others", "bathroom1,bathroom2,lights_plugs5,lights_plugs3,lights_plugs4,lights_plugs2,lights_plugs1,lights_plugs6"
																+ ",air1,air2,air3");
//		map_subGroupType_N_listOFdevices.put( "others", "");
		
		int tot=0;
		// Totally add up the devie in each subGRoupType
		for(String eachSubGroup:map_subGroupType_N_listOFdevicesCSV.keySet()){
			tot+=map_subGroupType_N_listOFdevicesCSV.get(eachSubGroup).split(",").length;
		}
		System.out.println("tot-->"+tot + " all_header.size:"+all_tokens_HEADERS_CSV.split(",").length);
		
		TreeMap<Integer,String> map_SEQ_headerTokenOFall=new TreeMap<Integer, String>();
		TreeMap<String, Integer> map_headerTokenOFall_SEQ=new TreeMap<String, Integer>();
		
		//each token (from header)
		while(c2<arr_all_header_CSV.length){
//					System.out.println("arr_tokens[]:"+arr_all_header_CSV[c2]+"--- token:"+(c2+1));
					map_SEQ_headerTokenOFall.put(c2, arr_all_header_CSV[c2]);
					map_headerTokenOFall_SEQ.put(arr_all_header_CSV[c2], c2);
					c2++;
		}
		// DataID_Austin_2014_SingleHomeFamily
		TreeMap<Integer,String> map_inFile_seq_eachLine1_dataID_housingTypeSingleHomeFamily_2014_AustinTX=
		ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
			.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
															inFile_DataID_Austin_2014_SingleHomeFamily, 
														 	 -1, //startline, 
															 -1, //endline,
															 "f1 ", //debug_label
															 false //isPrintSOP
															 );
		// _DataID_Austin_2014_Apartment
		TreeMap<Integer,String> map_inFile_seq_eachLine1_dataID_housingTypeAPARTMENT_2014_AustinTX=
		ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
			.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
															inFile_DataID_Austin_2014_Apartment, 
														 	 -1, //startline, 
															 -1, //endline,
															 "f1 ", //debug_label
															 false //isPrintSOP
															 );
		//inFile_buildingType_dataID_2014_ONLY
		TreeMap<Integer,String> map_inFile_seq_eachLine1_dataID_ALL_2014_ANYCITY=
											ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
												.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								 inFile_buildingType_dataID_2014_ONLY, 
																							 	 -1, //startline, 
																								 -1, //endline,
																								 "f1 ", //debug_label
																								 false //isPrintSOP
																								 );
		
		///
		///--------- Load DataIDS that belong to only Austin, TEXAS
//		TreeMap<Integer,String> map_inFile_seq_eachLine1_dataID_housingType=
//				readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
//																	  inFile_buildingType_dataID_austinTX, 
//																 	 -1, //startline, 
//																	 -1, //endline,
//																	 "f1 ", //debug_label
//																	 false //isPrintSOP
//																	 );
//		System.out.println("loaded.."+map_inFile_seq_eachLine1_dataID_housingType.size());
//		TreeMap<Integer, String> map_dataID_housingType_=new TreeMap<Integer, String>();
//		for(int seq:map_inFile_seq_eachLine1_dataID_housingType.keySet()){
//			if(seq==1) continue; //header
//			String arr_housingType_dataID_[]= map_inFile_seq_eachLine1_dataID_housingType.get(seq).split(",");
//			System.out.println("line:"+map_inFile_seq_eachLine1_dataID_housingType.get(seq));
//			map_dataID_housingType_.put(Integer.valueOf(arr_housingType_dataID_[1]), arr_housingType_dataID_[0]);
//		}
//		/// GET <HousingType,<DataID,>>
//		TreeMap<String, TreeMap<Integer, Integer> > map_HousingType_DataIDasKEY=new TreeMap<String, TreeMap<Integer,Integer>>();
//		for(int DataID:map_dataID_housingType_.keySet()){
//			String curr_housingType=map_dataID_housingType_.get(DataID);
//			if(map_HousingType_DataIDasKEY.containsKey(curr_housingType)){
//				TreeMap<Integer, Integer> tmp=map_HousingType_DataIDasKEY.get(curr_housingType);
//				tmp.put(DataID, -1);
//				map_HousingType_DataIDasKEY.put(curr_housingType, tmp);
//			}
//			else{
//				TreeMap<Integer, Integer> tmp=new TreeMap<Integer, Integer>();
//				tmp.put(DataID, -1);
//				map_HousingType_DataIDasKEY.put(curr_housingType, tmp);
//			}
//		}
//		System.out.println("map_HousingType_DataIDasKEY:"+map_HousingType_DataIDasKEY);
//		TreeMap<Integer, Integer> map_interestedDataIDs_as_KEY=new TreeMap<Integer, Integer>();
//		// one  of {apartment,singlehomeFamily}
//		if(housingType.equalsIgnoreCase("apartment")){
//			map_interestedDataIDs_as_KEY=map_HousingType_DataIDasKEY.get(housingType.toLowerCase());
//		}
//		else{
//			map_interestedDataIDs_as_KEY=map_HousingType_DataIDasKEY.get(housingType);
//		}
		//////////////////////////////////////////////////////////////////////////////////////
		// topology 1---> p21_convert_to_GBAD_topology1
//		p21_convert_to_GBAD_topology1(	baseFolderINPUT,
//										baseFolderOUTPUT,
//										map_SEQ_headerTokenOFall,
//										map_headerTokenOFall_SEQ,
//										OFFSET_dont_write_lesserTHAN_TOTALvertex,
//										false //isSOPprint
//										);
		
//		System.out.println("map_interestedDataIDs_as_KEY:"+map_interestedDataIDs_as_KEY);
//		System.out.println("map_interestedDataIDs_as_KEY.size:"+map_interestedDataIDs_as_KEY.size());
		 
		
		TreeMap<Integer,String> map_interestedDataIDs_2014_AustinTexas_asVALUES=new TreeMap<Integer, String>();
		
		// get  interested DataIDs and only for these dataIDs , we create .g graph..
		if(housingType.equalsIgnoreCase("Apartment")){
			map_interestedDataIDs_2014_AustinTexas_asVALUES=map_inFile_seq_eachLine1_dataID_housingTypeAPARTMENT_2014_AustinTX;
		}
		else if(housingType.equalsIgnoreCase("Single-Family Home")){//single home family
			map_interestedDataIDs_2014_AustinTexas_asVALUES=map_inFile_seq_eachLine1_dataID_housingTypeSingleHomeFamily_2014_AustinTX;
		}
				
				// topology 2---> p21_convert_to_GBAD_topology2
				p21_convert_to_GBAD_topology2(	baseFolderINPUT,
												baseFolderOUTPUT,
												map_SEQ_headerTokenOFall,
												map_headerTokenOFall_SEQ,
												OFFSET_dont_write_lesserTHAN_TOTALvertex,
												map_subGroupType_N_listOFdevicesCSV,
												map_interestedDataIDs_2014_AustinTexas_asVALUES,
												map_inFile_seq_eachLine1_dataID_ALL_2014_ANYCITY,
												false //isSOPprint
												);
				
				System.out.println("map_interestedDataIDs_2014_AustinTexas_asVALUES:"+map_interestedDataIDs_2014_AustinTexas_asVALUES);
				
		System.out.println("Time Taken (FINAL ENDED):"
				+ NANOSECONDS.toSeconds(System.nanoTime() - t0)
				+ " seconds; "
				+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
				+ " minutes");
	}

}
