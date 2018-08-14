package p19;

import java.io.FileWriter;
import java.nio.channels.WritableByteChannel;

import crawler.ReadFile_2_EachLineORtoken_from_First_File_exists_in_Second_File_as_String;
import crawler.Generate_Random_Number_Range;
import java.util.TreeMap;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.File;
 

import crawler.IsFile_a_Directory;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeFactory;

public class P19_p21_convert_token_with_numbers_to_AggregateNumbers {
	 
	// convert_token_with_numbers_to_AggregateNumbers (convert to  )
	public static void p19_p21_convert_token_with_numbers_to_AggregateNumbers(
																	String baseFolder,
																	String input_Folder_or_file1, //output of convert_epoch_datetime
																	String delimiter_for_file1,
																	int    token_containing_PrimaryKEY,
																	String tokens_integer_interested_CSV,
																	int    Flag_to_calc_MEAN_n_StdDEV, 
																	String[] arr_all_header_CSV,
																	String[] arr_high_energy_device_CSV, 
																	int flag_method_type_for_DATETIME_matching_on_seeding,
																	TreeMap<String, String> map_COMBINATION_dataID_N_datetime_higherEnergyDeviceINDEX_asKEY,
																	FileWriter writer_dataID_date_mean_SD, 
																	TreeMap<String, TreeMap<String, Double>> map_Date_PrimaryKeyToken_Mean, //mean
																	TreeMap<String, TreeMap<String, Double>> map_Date_PrimaryKeyToken_StandardDeviation, //SD
																	TreeMap<Integer, TreeMap<Integer, Integer>> map_personaID_DataIDasKEY, //persona and is DataIDs
																	TreeMap<String, TreeMap<Integer, Integer> > map_HousingType_DataIDasKEY_I_YYYY
																	){
		String found_housingType_for_currDataID="";
		TreeMap<String,  Double> map_PrimaryKeyToken_Mean=new TreeMap<String, Double>();
		TreeMap<String,  Double> map_PrimaryKeyToken_StandardDeviation=new TreeMap<String, Double>();
		
		FileWriter writer=null;
		FileWriter writerDebug=null;
		FileWriter writer_Apartment=null;
		FileWriter writer_singleFamilyHomes=null;
				
		FileWriter writer_SEEDEDonly=null; //FileWriter writer_dataID_date_mean_SD=null;
		long t0 = System.nanoTime();
		TreeMap<String, String> map_tokenNAME_asKEY =new TreeMap<String, String>();
		//take 
		String date="";
		// 
		String temp_input_Folder_or_file1=input_Folder_or_file1.substring(input_Folder_or_file1.lastIndexOf("/")+1, input_Folder_or_file1.length());
		int begin=-1;
		if(temp_input_Folder_or_file1.indexOf("2012")>=0)
			begin=temp_input_Folder_or_file1.indexOf("2012");
		else if(temp_input_Folder_or_file1.indexOf("2013")>=0)
			begin=temp_input_Folder_or_file1.indexOf("2013");
		else if(temp_input_Folder_or_file1.indexOf("2014")>=0)
			begin=temp_input_Folder_or_file1.indexOf("2014");
		else if(temp_input_Folder_or_file1.indexOf("2015")>=0)
			begin=temp_input_Folder_or_file1.indexOf("2015");
		else if(temp_input_Folder_or_file1.indexOf("2016")>=0)
			begin=temp_input_Folder_or_file1.indexOf("2016");
		//
		if(begin>0){
			date=temp_input_Folder_or_file1.substring(begin, begin+10);
		}
		
		try {
			/// 
			TreeMap<Integer,String> map_file1=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																							 input_Folder_or_file1, 
																						 	 -1, //startline, 
																							 -1, //endline,
																							 "f1 ", //debug_label
																							 false //isPrintSOP
																		 					);
			
			writer_Apartment=new FileWriter((input_Folder_or_file1.replace(".txt", "").replace(".csv", "")+"_apartment_categorical.csv"));
			writer_singleFamilyHomes=new FileWriter((input_Folder_or_file1.replace(".txt", "").replace(".csv", "")+"_singleFamilyHomes_categorical.csv"));
			
			// temp comment
//			writer=new FileWriter((input_Folder_or_file1.replace(".txt", "").replace(".csv", "")+"_categorical.csv"));
//			writer_SEEDEDonly=new FileWriter((input_Folder_or_file1.replace(".txt", "").replace(".csv", "")+"_categorical_SEEDEDonly.csv"));
			writerDebug=new FileWriter(baseFolder+"debug_categorical.txt");
			
			TreeMap<Integer, TreeMap<Integer,String>> map_tokenSEQ_lineNoNline=
														new TreeMap<Integer, TreeMap<Integer,String>>();
			TreeMap<Integer, TreeMap<Integer,String>> map_tokenSEQ_lineNoPrimaryKEY=
														new TreeMap<Integer, TreeMap<Integer,String>>();
			
			TreeMap<String, TreeMap<Integer,String>> map_PrimaryKeyANDtokenSEQ_lineNoNline=
													new TreeMap<String, TreeMap<Integer,String>>();
			
			TreeMap<String, TreeMap<Integer,String>> map_PrimaryKey_NsetOFlinesFORit_as_KEY=
													new TreeMap<String, TreeMap<Integer,String>>();
			
			TreeMap<String, TreeMap<Integer,String>> map_PrimaryKeyTokenANDtokenSEQ_N_lineNoNline=
														new TreeMap<String, TreeMap<Integer, String>>();
			
			TreeMap<Integer,  Double> map_tokenSEQ_StandardDeviation=new TreeMap<Integer, Double>();
			TreeMap<Integer,  Double> map_tokenSEQ_Mean=new TreeMap<Integer, Double>();
			
			TreeMap<Integer,  Integer> map_token_interested_as_KEY=new TreeMap<Integer, Integer>();
			String [] arr_tokens_interested_CSV=tokens_integer_interested_CSV.split(",");
			//
			int j=0;
			while(j<arr_tokens_interested_CSV.length){
				map_token_interested_as_KEY.put( Integer.valueOf(arr_tokens_interested_CSV[j])-1 , -1);
				j++;
			}
			String curr_primaryKey="";
			// 
			for(int lineNo:map_file1.keySet()){
				String eachLine=map_file1.get(lineNo);
				eachLine=eachLine.replace(",,", ",0,").replace(",,", ",0,");
				// last column
				if(eachLine.substring(eachLine.length()-1, eachLine.length()).equals(",") ){
					eachLine=eachLine+"0";
				}
				
				String [] arr_tokens=eachLine.split(delimiter_for_file1);
				
//				System.out.println("arr_tokens.len:"+arr_tokens.length+" <->2.lineNo:"+lineNo +" eachLine:"+eachLine
//													+"<--->"+ eachLine.substring(eachLine.length()-1, eachLine.length()));
				
				
//				if(lineNo>1) break; // debug
				
				// Dataid
				curr_primaryKey=arr_tokens[token_containing_PrimaryKEY-1];
				found_housingType_for_currDataID="";boolean bool_found_housingType_for_currDataID=false;
				/// 
				for(String eachHousingType:map_HousingType_DataIDasKEY_I_YYYY.keySet()){
						// check if this dataID present in Austin TX and in year YYYY
						if(map_HousingType_DataIDasKEY_I_YYYY.get(eachHousingType).containsKey(Integer.valueOf(curr_primaryKey))){
							found_housingType_for_currDataID=eachHousingType;
							bool_found_housingType_for_currDataID=true;
							break;
						}
				}
				
				//get the housing type where this dataID belongs..
				//only process if belong to Austin, TX  and also in year YYYY
				if(bool_found_housingType_for_currDataID==false || found_housingType_for_currDataID.length()==0){
						continue;
				}
				
				
				String curr_primaryKeyANDtokenSEQ="";
				
				int c=0;
				while(c<arr_tokens.length){ //
					curr_primaryKeyANDtokenSEQ=curr_primaryKey+","+c;
					String  tokenNAME= arr_all_header_CSV[c];
					//fill empty with ZERO
					if(arr_tokens[c].equalsIgnoreCase("")) arr_tokens[c]="0";
					// 
					if(!map_tokenSEQ_lineNoNline.containsKey(c)){
						TreeMap<Integer,String> temp=new TreeMap<Integer, String>();
						temp.put(lineNo, arr_tokens[c]);
						map_tokenSEQ_lineNoNline.put(c, temp);
						//lineno, primarykey
						TreeMap<Integer,String> temp2=new TreeMap<Integer, String>();
						temp2.put(lineNo, arr_tokens[token_containing_PrimaryKEY-1]);
						map_tokenSEQ_lineNoPrimaryKEY.put(c , temp2);
						// PrimaryKEY+tokenSEQ
						map_PrimaryKeyANDtokenSEQ_lineNoNline.put(curr_primaryKeyANDtokenSEQ, temp);
						 
						map_tokenNAME_asKEY.put(tokenNAME, ""); //name
					}
					else{
						TreeMap<Integer,String> temp=map_tokenSEQ_lineNoNline.get(c);
						temp.put(lineNo, arr_tokens[c]);
						map_tokenSEQ_lineNoNline.put(c, temp);
						//lineno, primarykey
						TreeMap<Integer,String> temp2=map_tokenSEQ_lineNoPrimaryKEY.get(c);
						temp2.put(lineNo, arr_tokens[token_containing_PrimaryKEY-1]);
						map_tokenSEQ_lineNoPrimaryKEY.put(c , temp2);
						// PrimaryKEY+tokenSEQ
						map_PrimaryKeyANDtokenSEQ_lineNoNline.put(curr_primaryKeyANDtokenSEQ, temp);
						map_tokenNAME_asKEY.put(tokenNAME, ""); //name
					}
					// primaryKEY and its set of line NO
					if(!map_PrimaryKey_NsetOFlinesFORit_as_KEY.containsKey(curr_primaryKey)){
						//primarykey AND lineNO of the primaryKEY
						TreeMap<Integer,String> temp3=new TreeMap<Integer, String>();
						temp3.put(lineNo, "-1");
						map_PrimaryKey_NsetOFlinesFORit_as_KEY.put(curr_primaryKey, temp3);
					}
					else{
						//primarykey AND lineNO of the primaryKEY
						TreeMap<Integer,String> temp3=map_PrimaryKey_NsetOFlinesFORit_as_KEY.get(curr_primaryKey);
						temp3.put(lineNo, "-1");
						map_PrimaryKey_NsetOFlinesFORit_as_KEY.put(curr_primaryKey, temp3);
					}
					
//					System.out.println("arr_tokens[c]:"+arr_tokens[c]);
					c++;
				} //while(c<arr_tokens.length){
				
			} // for(int lineNo:map_file1.keySet()){
			
			System.out.println("----------Entering calc for mean and STD--------------------");
			//for each token, calculate the statistics = Mean and Standard Deviation
			// Flag (aggregate calculated throughout file)
			if(Flag_to_calc_MEAN_n_StdDEV==1){
				for(int curr_tokenSEQ:map_tokenSEQ_lineNoNline.keySet()){
					TreeMap<Integer,String> map_curr_lineNoNToken= map_tokenSEQ_lineNoNline.get(curr_tokenSEQ);
					//
					if(!map_token_interested_as_KEY.containsKey(curr_tokenSEQ)) continue;
					
						double sum_curr_token_sum_4_currFile=0.0;
						double SQUARE_sum_curr_token_sum_4_currFile=0.0;
					
						//read token value of each specific token and get its SUM
						for(int currLineNo:map_curr_lineNoNToken.keySet()){
		//					System.out.println("token value:"+map_curr_lineNoNToken.get(currLineNo)+" currLineNo:"+currLineNo
		//												+" size:"+map_curr_lineNoNToken.size());
							sum_curr_token_sum_4_currFile+=Double.valueOf(map_curr_lineNoNToken.get(currLineNo));
							SQUARE_sum_curr_token_sum_4_currFile+=Double.valueOf(map_curr_lineNoNToken.get(currLineNo))
																  *Double.valueOf(map_curr_lineNoNToken.get(currLineNo));
						}
						// mean & Std deviation
						map_tokenSEQ_Mean.put(curr_tokenSEQ, 
											  (sum_curr_token_sum_4_currFile/ (double) map_curr_lineNoNToken.size() ) );
						map_tokenSEQ_StandardDeviation.put(curr_tokenSEQ,
														   Math.sqrt((SQUARE_sum_curr_token_sum_4_currFile/(double) map_curr_lineNoNToken.size())) );
						double mean=(double)(sum_curr_token_sum_4_currFile/ (double) map_curr_lineNoNToken.size() );
						double sd=(double)Math.sqrt((SQUARE_sum_curr_token_sum_4_currFile/(double) map_curr_lineNoNToken.size()));
						//  
			  }
			}
			// Flag (aggregate calculated dataid-wise)
			else if(Flag_to_calc_MEAN_n_StdDEV==2){
				int total_unique_pkey=map_PrimaryKeyANDtokenSEQ_lineNoNline.size(); int count=0;
				// 
				for(String curr_PrimaryKeyANDtokenSEQ:map_PrimaryKeyANDtokenSEQ_lineNoNline.keySet()){
					count++;
//					TreeMap<Integer,String> map_lineNoNline= map_PrimaryKeyANDtokenSEQ_lineNoNline.get(curr_PrimaryKeyANDtokenSEQ);
					String []arr=curr_PrimaryKeyANDtokenSEQ.split(",");
					String curr_PrimaryKEY=arr[0]; //dataID
					String curr_tokenSEQ=arr[1]; double numeric=0.;
					
					if(!map_token_interested_as_KEY.containsKey(Integer.valueOf(curr_tokenSEQ) )) {continue;}
					
//					System.out.println("count:"+count+" of total="+total_unique_pkey);
					// is already exists
					boolean is_already_calculatedEXISTS=false;
					if(map_Date_PrimaryKeyToken_Mean.containsKey(date)){
						if(map_Date_PrimaryKeyToken_Mean.get(date).containsKey(curr_PrimaryKeyANDtokenSEQ)
							&& map_Date_PrimaryKeyToken_StandardDeviation.get(date).containsKey(curr_PrimaryKeyANDtokenSEQ)
								  ){
							is_already_calculatedEXISTS=true;
						}
					}
					// 
					if( is_already_calculatedEXISTS==true && map_token_interested_as_KEY.containsKey(Integer.valueOf(curr_tokenSEQ)) ){
					// if  exists in past run of mean and SD 
						    //get from past run - mean and SD
							map_PrimaryKeyToken_Mean.put( curr_PrimaryKeyANDtokenSEQ, map_Date_PrimaryKeyToken_Mean.get(date).get(curr_PrimaryKeyANDtokenSEQ));
							map_PrimaryKeyToken_StandardDeviation.put( curr_PrimaryKeyANDtokenSEQ, map_Date_PrimaryKeyToken_StandardDeviation.get(date).get(curr_PrimaryKeyANDtokenSEQ));
							continue;
					}
					else{
						double sum_curr_token_sum_4_currFile=0.0;
						double SQUARE_sum_curr_token_sum_4_currFile=0.0;
						TreeMap<Integer,String> map_curr_lineNoNToken= map_tokenSEQ_lineNoNline.get(Integer.valueOf(curr_tokenSEQ));
						TreeMap<Integer,String> map_curr_lineNoNprimaryKEY= map_tokenSEQ_lineNoPrimaryKEY.get(Integer.valueOf(curr_tokenSEQ));

						boolean is_numeric=true;
						// calculate mean + SD freshly
						
						//read token value of each specific token OF A PRIMARY KEY and get its SUM )  
						TreeMap<Integer,String> curr_NsetOFlinesFORit_as_KEY= map_PrimaryKey_NsetOFlinesFORit_as_KEY.get(curr_PrimaryKEY);
						for(int currLineNo:curr_NsetOFlinesFORit_as_KEY.keySet()){
							if(currLineNo==1) continue; //header
							
							try{
								numeric=Double.valueOf(map_curr_lineNoNToken.get(currLineNo));	
							}
							catch(Exception e){
								is_numeric=false;
								break;
							}
							sum_curr_token_sum_4_currFile+=Double.valueOf(map_curr_lineNoNToken.get(currLineNo));
							SQUARE_sum_curr_token_sum_4_currFile+= Double.valueOf(map_curr_lineNoNToken.get(currLineNo))
																  *Double.valueOf(map_curr_lineNoNToken.get(currLineNo));
						}
						
						if(is_numeric==false) //its a NOT a number
							continue;
						
						//read token value of each specific token and get its SUM )( VERY SLOW)
//						for(int currLineNo:map_curr_lineNoNToken.keySet()){
//		//					System.out.println("token value:"+map_curr_lineNoNToken.get(currLineNo)+" currLineNo:"+currLineNo
//		//												+" size:"+map_curr_lineNoNToken.size());
//							String curr_PrimaryKEY2=map_curr_lineNoNprimaryKEY.get(currLineNo);
//							
//							//check on PRIMARY KEY match
//							if(curr_PrimaryKEY2.equalsIgnoreCase(curr_PrimaryKEY)) {
//								sum_curr_token_sum_4_currFile+=Double.valueOf(map_curr_lineNoNToken.get(currLineNo));
//								SQUARE_sum_curr_token_sum_4_currFile+=Double.valueOf(map_curr_lineNoNToken.get(currLineNo))
//																	  *Double.valueOf(map_curr_lineNoNToken.get(currLineNo));
//							}
//						}
						double mean=(double)(sum_curr_token_sum_4_currFile/ (double) map_curr_lineNoNToken.size() );
						double sd=(double)Math.sqrt( (SQUARE_sum_curr_token_sum_4_currFile/(double) map_curr_lineNoNToken.size()));
						// mean & Std deviation
						map_PrimaryKeyToken_Mean.put(curr_PrimaryKeyANDtokenSEQ, 
											  		 mean);
						map_PrimaryKeyToken_StandardDeviation.put(curr_PrimaryKeyANDtokenSEQ,
														   		   sd
														   		 );
 						
//						System.out.println("date: "+date+" "+curr_PrimaryKeyANDtokenSEQ+" "+mean+" "+sd);
						
						//save SD and mean <DATE!!!dataID!!!tokenSequence!!!mean!!!standardDeviation>
						writer_dataID_date_mean_SD.append(date+"!!!"+curr_PrimaryKeyANDtokenSEQ.replace(",", "!!!") +"!!!"+mean+"!!!"+sd+"\n");
						writer_dataID_date_mean_SD.flush();
						
					 }
						
			  } // END for(String curr_PrimaryKeyANDtokenSEQ:map_PrimaryKeyANDtokenSEQ_lineNoNline.keySet()){
			} // else if(Flag_to_calc_MEAN_n_StdDEV==2){
			 // debug
			if(Flag_to_calc_MEAN_n_StdDEV==1){
				System.out.println("(uncommentHERE)map_tokenSEQ_Mean.size:"+map_tokenSEQ_Mean.size()
				  									//+" \n"+map_tokenSEQ_StandardDeviation+" \n"+map_tokenSEQ_Mean
													);

			}
			else if(Flag_to_calc_MEAN_n_StdDEV==2){
				System.out.println("(uncommentHERE)map_PrimaryKeyToken_Mean.size:"+map_PrimaryKeyToken_Mean.size()
												//+" \n"+map_PrimaryKeyToken_Mean
										);					
			}
			TreeMap<String,String> map_primaryKEY_for_vveryLOW=new TreeMap<String,String>();
//			map_primaryKEY_for_vveryLOW.put("26","");
//			map_primaryKEY_for_vveryLOW.put("59","");
//			map_primaryKEY_for_vveryLOW.put("77","");
//			map_primaryKEY_for_vveryLOW.put("86","");
//			map_primaryKEY_for_vveryLOW.put("93","");
//			map_primaryKEY_for_vveryLOW.put("94","");
//			map_primaryKEY_for_vveryLOW.put("101","");
//			map_primaryKEY_for_vveryLOW.put("114","");
//			map_primaryKEY_for_vveryLOW.put("115","");
//			map_primaryKEY_for_vveryLOW.put("121","");
			
			System.out.println("--------creating new line--------------------");
			//create new-line with number tokens to <categories>
			for(int lineNo:map_file1.keySet()){
				
				// if(lineNo==1) continue; < --commented as NO HEADER
				
				String eachLine=map_file1.get(lineNo);
//				System.out.println("eachLine;"+eachLine);
				eachLine=eachLine.replace(",,", ",0,").replace(",,", ",0,");
				// last column
				if(eachLine.substring(eachLine.length()-1, eachLine.length()).equals(",") ){
					eachLine=eachLine+"0";
				}
				
				String [] arr_tokens=eachLine.split(delimiter_for_file1);
				
//				System.out.println("eachLine:"+eachLine+" arr.len:"+arr_tokens.length);
				
//				System.out.println("arr_tokens.len:"+arr_tokens.length+" lineNo:"+lineNo); //+" eachLine:"+eachLine);
//				if(lineNo>1) break; //debugging
				int c=0; double mean_stdDev_offset=0.; double mean_stdDev_offset2=0.;
				curr_primaryKey=arr_tokens[token_containing_PrimaryKEY-1]; //dataID
				String curr_dateTime_eachLine=arr_tokens[1]; //datetime
				String newLine=""; String new_token_value_categorical="";
				boolean isFound_for_seeded=false;
				String combined_dataID_datetime_tokenINDEX="";
				double new_token_value=0.;
				
				
				found_housingType_for_currDataID="";boolean bool_found_housingType_for_currDataID=false;
				/// 
				for(String eachHousingType:map_HousingType_DataIDasKEY_I_YYYY.keySet()){
						// check if this dataID present in Austin TX and in year YYYY
						if(map_HousingType_DataIDasKEY_I_YYYY.get(eachHousingType).containsKey(Integer.valueOf(curr_primaryKey))){
							found_housingType_for_currDataID=eachHousingType;
							bool_found_housingType_for_currDataID=true;
							break;
						}
				}
				 
//				if(lineNo%10000==0) 
//					System.out.println("lineNo:"+lineNo);
				
//				System.out.println("arr_tokens.length:"+arr_tokens.length);
				// 
				while(c<arr_tokens.length){
					
					isFound_for_seeded=false;
					String curr_primaryKEY_tokenSEQ=curr_primaryKey+","+ String.valueOf(c)  ;

					// safety ...again put it 
					if(!map_PrimaryKeyToken_Mean.containsKey(curr_primaryKEY_tokenSEQ) 
							&& map_Date_PrimaryKeyToken_Mean.containsKey(date)){
							
							if( map_Date_PrimaryKeyToken_Mean.get(date).containsKey(curr_primaryKEY_tokenSEQ) ) {
								map_PrimaryKeyToken_Mean.put(curr_primaryKEY_tokenSEQ, map_Date_PrimaryKeyToken_Mean.get(date).get(curr_primaryKEY_tokenSEQ));
								map_PrimaryKeyToken_StandardDeviation.put(curr_primaryKEY_tokenSEQ, map_Date_PrimaryKeyToken_StandardDeviation.get(date).get(curr_primaryKEY_tokenSEQ));								
							}

					}
					 
					//combined primary key for (EACHLINE) TO BE MATCHED n inserting SEEDED
					if(flag_method_type_for_DATETIME_matching_on_seeding==1){
						combined_dataID_datetime_tokenINDEX=curr_primaryKey+"!!!"
											+curr_dateTime_eachLine.substring(0, curr_dateTime_eachLine.indexOf(" ")).replace("\"", "")+"!!!"+String.valueOf(c);
					}
					else if(flag_method_type_for_DATETIME_matching_on_seeding==2){ //primaryKEY!!!datetime!!!indexOFtoken
						combined_dataID_datetime_tokenINDEX=curr_primaryKey+"!!!"+curr_dateTime_eachLine.replace("\"", "")
																				  +"!!!"+String.valueOf(c);
//						System.out.println("22:"+combined_dataID_datetime_tokenINDEX);
					}
					else if(flag_method_type_for_DATETIME_matching_on_seeding==3){ // primaryKEY!!!datetime
						combined_dataID_datetime_tokenINDEX=curr_primaryKey+"!!!"+curr_dateTime_eachLine.replace("\"", "");
					}
						
					//fill empty with ZERO
					if(arr_tokens[c].equalsIgnoreCase("")) arr_tokens[c]="na"; //not available
					//
					if(!map_token_interested_as_KEY.containsKey(c)){
						if(newLine.length()==0){
							newLine=arr_tokens[c];
						}
						else{
							newLine+=","+arr_tokens[c];
						}
						c++;
						continue;
					}
					// 
					if(!arr_tokens[c].equalsIgnoreCase("na")){
						// 
						if(Flag_to_calc_MEAN_n_StdDEV==1){
							mean_stdDev_offset=	 Double.valueOf(map_tokenSEQ_Mean.get(c))
												+ Double.valueOf(map_tokenSEQ_StandardDeviation.get(c));
						}
						else if(Flag_to_calc_MEAN_n_StdDEV==2){
	//						System.out.println("curr_primaryKEY_tokenSEQ:"+curr_primaryKEY_tokenSEQ
	//											+" is:"+map_PrimaryKeyToken_Mean.containsKey(curr_primaryKEY_tokenSEQ)
	//											+" is:"+map_PrimaryKeyToken_StandardDeviation.containsKey(curr_primaryKEY_tokenSEQ));
							
//							if(map_PrimaryKeyToken_Mean.containsKey(curr_primaryKEY_tokenSEQ)==false)
//								System.out.println("curr_primaryKEY_tokenSEQ:"+
//												date+"!!!"+curr_primaryKEY_tokenSEQ+"!!!"+map_PrimaryKeyToken_Mean.containsKey(curr_primaryKEY_tokenSEQ)
//												+"<-->"+map_Date_PrimaryKeyToken_Mean.get(date).containsKey(curr_primaryKEY_tokenSEQ));
							
							// safety ...again put it 
//							map_PrimaryKeyToken_Mean.put(curr_primaryKEY_tokenSEQ, map_Date_PrimaryKeyToken_Mean.get(date).get(curr_primaryKEY_tokenSEQ));
//							map_PrimaryKeyToken_StandardDeviation.put(curr_primaryKEY_tokenSEQ, map_Date_PrimaryKeyToken_StandardDeviation.get(date).get(curr_primaryKEY_tokenSEQ));
														
								try{
									//
									mean_stdDev_offset=
												   Double.valueOf(map_PrimaryKeyToken_Mean.get(curr_primaryKEY_tokenSEQ))
												 + Double.valueOf(map_PrimaryKeyToken_StandardDeviation.get(curr_primaryKEY_tokenSEQ));
									
									mean_stdDev_offset2=
											   		Double.valueOf(map_PrimaryKeyToken_Mean.get(curr_primaryKEY_tokenSEQ))
											   	 + (2*Double.valueOf(map_PrimaryKeyToken_StandardDeviation.get(curr_primaryKEY_tokenSEQ)));
							
								}
								catch(Exception e){
//									System.out.println("ERROR :map_PrimaryKeyToken_Mean.get(curr_primaryKEY_tokenSEQ):"+map_PrimaryKeyToken_Mean.get(curr_primaryKEY_tokenSEQ)
//															+"curr_primaryKEY_tokenSEQ:"+
//															date+"!!!"+curr_primaryKEY_tokenSEQ+"!!!"+map_PrimaryKeyToken_Mean.containsKey(curr_primaryKEY_tokenSEQ));
															
								}
						}
					}
					
//					double curr_token_Mean=map_tokenSEQ_Mean.get(c);
//					double curr_token_StandardDeviation=map_tokenSEQ_StandardDeviation.get(c);
					
					try{
						new_token_value=  Double.valueOf(arr_tokens[c]);
					}
					catch(Exception e){
						e.printStackTrace();
						System.out.println("error :c="+c+" lineNo="+lineNo);
					}
					if(combined_dataID_datetime_tokenINDEX.indexOf("-04-28")>=0){ //debug
//						writerDebug.append("map_comb.size:"+map_COMBINATION_dataID_N_datetime_higherEnergyDeviceINDEX_asKEY.size()
//											+" "+combined_dataID_datetime_tokenINDEX+"<---->"
//											+map_COMBINATION_dataID_N_datetime_higherEnergyDeviceINDEX_asKEY.containsKey(combined_dataID_datetime_tokenINDEX)+"\n");
//						writerDebug.flush();
//						
//						System.out.println("map_comb.size:"+map_COMBINATION_dataID_N_datetime_higherEnergyDeviceINDEX_asKEY.size()
//											+" "+combined_dataID_datetime_tokenINDEX+"<---->"
//											+map_COMBINATION_dataID_N_datetime_higherEnergyDeviceINDEX_asKEY.containsKey(combined_dataID_datetime_tokenINDEX)
//								);
					}
					//seeded (insert on matching combined primary keys)
					if(new_token_value == 0.00){ //negative means the feature is missing values.
						new_token_value_categorical="na";
					}
					else if(map_COMBINATION_dataID_N_datetime_higherEnergyDeviceINDEX_asKEY.containsKey(
														combined_dataID_datetime_tokenINDEX)){
						
						//for these 10 primaryKEY (p21- dataid), make it vveryLow
						if(map_primaryKEY_for_vveryLOW.containsKey(curr_primaryKey)){
							new_token_value_categorical="vveryLow";
						}
						else
							new_token_value_categorical="vveryHigh";
						
						isFound_for_seeded=true;
					}
					else if(new_token_value > mean_stdDev_offset2)
						new_token_value_categorical="veryHigh";
					else if(new_token_value > mean_stdDev_offset)
						new_token_value_categorical="high";
					else if(new_token_value < mean_stdDev_offset)
						new_token_value_categorical="low";
					else
						new_token_value_categorical="normal";
					
					// concatenate 
					if(newLine.length()==0){
						newLine=new_token_value_categorical;
					}
					else{
						newLine+=","+new_token_value_categorical;
					}
					c++;
				} //while(c<arr_tokens.length){
				
//				System.out.println("newline.leng:"+newLine.split(",").length+"<--->"+newLine);
				
//				System.out.println(newLine);
				// This was writing all the housing type in one File
//				writer.append(newLine+"\n");
//				writer.flush();
				
//				System.out.println("found_housingType_for_currDataID:"+found_housingType_for_currDataID);
				// housetype-wise file writing
				if(found_housingType_for_currDataID.equalsIgnoreCase("apartment")){
					writer_Apartment.append(newLine.replace(",0", ",na")+"\n");
					writer_Apartment.flush();
				}
				else if(found_housingType_for_currDataID.equalsIgnoreCase("Single-Family Home")){
					writer_singleFamilyHomes.append(newLine.replace(",0", ",na")+"\n");
					writer_singleFamilyHomes.flush();
				}
				
				// SEEDED only
				if(isFound_for_seeded){
					writer_SEEDEDonly.append(newLine+"\n");
					writer_SEEDEDonly.flush();
				}
				
			} // END for(int lineNo:map_file1.keySet()){
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		System.out.println("Time Taken (FINAL ENDED):"
							+ NANOSECONDS.toSeconds(System.nanoTime() - t0)
							+ " seconds; "
							+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
							+ " minutes");
	}
	// generate_seeded_pattern 
	private static void generate_seeded_pattern(String inFile_UNIQUE_dataIDs, 
												String inFile_dataIDs_to_be_seeded, //input 1
												String input_file_seededPATTERN_4_datetime_CSV,  //input 2 
												String[] arr_all_header_CSV,
												String[] arr_high_energy_device_CSV, 
												TreeMap<Integer, String> map_SEQ_higherEnergyDevice, //input 3
												TreeMap<String, Integer> map_headerTokenOFall_SEQ, 
												String outputFile_generate_SEEDED_COMBINATION_dateID_N_datetime_higherEnergyDevice
												) {
		
		
		// TODO Auto-generated method stub
		try{
			FileWriter writer=new FileWriter(new File(outputFile_generate_SEEDED_COMBINATION_dateID_N_datetime_higherEnergyDevice));
			TreeMap<Integer,String> map_inFile_dataIDs_to_be_seeded=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																		 inFile_dataIDs_to_be_seeded, 
																	 	 -1, //startline, 
																		 -1, //endline,
																		 "f1 ", //debug_label
																		 false //isPrintSOP
																		 );
			TreeMap<Integer,String> map_input_file_seededPATTERN_4_datetime_CSV=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																		 input_file_seededPATTERN_4_datetime_CSV, 
																	 	 -1, //startline, 
																		 -1, //endline,
																		 "f1 ", //debug_label
																		 false //isPrintSOP
																		 );
			
			int max_rand=map_SEQ_higherEnergyDevice.size();
			//
			for(int seq1:map_inFile_dataIDs_to_be_seeded.keySet()){
				String curr_dataID=map_inFile_dataIDs_to_be_seeded.get(seq1);
				//
				for(int seq2:map_input_file_seededPATTERN_4_datetime_CSV.keySet()){
					String curr_datetime_pattern=map_input_file_seededPATTERN_4_datetime_CSV.get(seq2);
					System.out.println("curr_dataID:"+curr_dataID+" curr_datetime_pattern:"+curr_datetime_pattern+" "+max_rand);
					int random_SEQ_4_higherEnergyDevice = Generate_Random_Number_Range.generate_Random_Number_Range(1, max_rand);
					//
//					for(int seq3:map_SEQ_higherEnergyDevice.keySet()){
						
						String curr_random_higherEnergyDevice=map_SEQ_higherEnergyDevice.get(random_SEQ_4_higherEnergyDevice);
//						System.out.println(curr_dataID+"!!!"+curr_datetime_pattern+"!!!"+curr_random_higherEnergyDevice+"\n");
						// 
						writer.append(curr_dataID+"!!!"+curr_datetime_pattern+"!!!"+map_headerTokenOFall_SEQ.get(curr_random_higherEnergyDevice)
										+"!!!"+curr_random_higherEnergyDevice+"!!!"+"\n");
						writer.flush();
//					}
					
				}
				
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
		// p21_get_list_of_validDevices_for_Each_dataID (output "dataID_n_validDevices.txt")
		private static void p21_get_list_of_validDevices_for_Each_dataID(String baseFolder, 
																		 String input_Folder_or_file1,
																		 String delimiter_for_file1, 
																		 int 	token_containing_PrimaryKEY, 
																		 String tokens_integer_interested_CSV,
																		 String[] arr_all_header_CSV
																		) {
			String curr_primaryKey="";

			TreeMap<String, TreeMap<Integer,String>> map_tokenNAME_asKEY=
												new TreeMap<String, TreeMap<Integer,String>>();
			
			// TODO Auto-generated method stub
			try {
				String output=baseFolder+"dataID_n_validDevices.txt";
				String input=baseFolder+"alreadyRanGotDataIDs.txt";
				
				TreeMap<Integer, Integer> map_alreadyRan_DataIDs = new TreeMap<Integer, Integer>();
				// already got the numeric devices for DATAids
				if(!new File(input).exists()){
					new File(input).createNewFile();
				}
				else{
					TreeMap<Integer,String> map_file2=
							ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
								.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										input, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 "f1 ", //debug_label
																										 false //isPrintSOP
																										 );
					for(int seq:map_file2.keySet()){
						String [] arr_=map_file2.get(seq).split("!!!");
						map_alreadyRan_DataIDs.put( Integer.valueOf(arr_[0]), -1 );	
					}
					
				}
				
				FileWriter writer=null;
				FileWriter writerDebug=null;
				long t0 = System.nanoTime();
				 	
					TreeMap<Integer,String> map_file1=
							ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
								.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 input_Folder_or_file1, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 "f1 ", //debug_label
																										 false //isPrintSOP
																				 );
				
						writer=new FileWriter(output, true);
				
						TreeMap<Integer, TreeMap<Integer,String>> map_tokenSEQ_lineNoNline=
								new TreeMap<Integer, TreeMap<Integer,String>>();
						TreeMap<Integer, TreeMap<Integer,String>> map_tokenSEQ_lineNoPrimaryKEY=
												new TreeMap<Integer, TreeMap<Integer,String>>();
						
						
						TreeMap<String, TreeMap<Integer,String>> map_PrimaryKeyANDtokenSEQ_lineNoNline=
															new TreeMap<String, TreeMap<Integer,String>>();
						
						TreeMap<String, TreeMap<Integer,String>> map_PrimaryKey_NsetOFlinesFORit_as_KEY=
											new TreeMap<String, TreeMap<Integer,String>>();
						
						TreeMap<String, TreeMap<Integer,String>> map_PrimaryKeyTokenANDtokenSEQ_N_lineNoNline=
												new TreeMap<String, TreeMap<Integer,String>>();

						TreeMap<Integer,  Double> map_tokenSEQ_StandardDeviation=new TreeMap<Integer, Double>();
						TreeMap<Integer,  Double> map_tokenSEQ_Mean=new TreeMap<Integer, Double>();
						
						TreeMap<String,  Double> map_PrimaryKeyToken_Mean=new TreeMap<String, Double>();
						TreeMap<String,  Double> map_PrimaryKeyToken_StandardDeviation=new TreeMap<String, Double>();
						
						TreeMap<Integer,  Integer> map_token_interested_as_KEY=new TreeMap<Integer, Integer>();
						String [] arr_tokens_interested_CSV=tokens_integer_interested_CSV.split(",");
						//
						int j=0;
						while(j<arr_tokens_interested_CSV.length){
							map_token_interested_as_KEY.put( Integer.valueOf(arr_tokens_interested_CSV[j])-1 , -1);
							j++;
						}
						curr_primaryKey="";
						TreeMap<String, Integer> map_alreadyRan_primaryKEY_asKEY=new TreeMap<String, Integer>();
						// 
						for(int lineNo:map_file1.keySet()){
							String eachLine=map_file1.get(lineNo);
							eachLine=eachLine.replace(",,", ",0,").replace(",,", ",0,");
							String [] arr_tokens=eachLine.split(delimiter_for_file1);
							
//							System.out.println("arr_tokens.len:"+arr_tokens.length+" <->2.lineNo:"+lineNo+"<-->"+eachLine); //+" eachLine:"+eachLine);
							//if(lineNo>1) break; // debug
							
							curr_primaryKey=arr_tokens[token_containing_PrimaryKEY-1];
							//already go the dataID
							if(map_alreadyRan_primaryKEY_asKEY.containsKey(curr_primaryKey) ||
									map_alreadyRan_DataIDs.containsKey(Integer.valueOf(curr_primaryKey))
								) {
								continue;
							}
							
							String curr_primaryKeyANDtokenSEQ="";
							
							int c=0;
							// each token
							while(c<arr_tokens.length){ //
								curr_primaryKeyANDtokenSEQ=curr_primaryKey+","+c;
								String  tokenNAME= arr_all_header_CSV[c];
								//fill empty with ZERO
								if(arr_tokens[c].equalsIgnoreCase("")) arr_tokens[c]="0";
								// 
								if(!map_tokenSEQ_lineNoNline.containsKey(c)){
									TreeMap<Integer,String> temp=new TreeMap<Integer, String>();
//									temp.put(lineNo, arr_tokens[c]);
//									map_tokenSEQ_lineNoNline.put(c, temp);
									//lineno, primarykey
//									TreeMap<Integer,String> temp2=new TreeMap<Integer, String>();
//									temp2.put(lineNo, arr_tokens[token_containing_PrimaryKEY-1]);
//									map_tokenSEQ_lineNoPrimaryKEY.put(c , temp2);
									// PrimaryKEY+tokenSEQ
//									map_PrimaryKeyANDtokenSEQ_lineNoNline.put(curr_primaryKeyANDtokenSEQ, temp);
									//PrimaryKEY+tokenNAME
									map_tokenNAME_asKEY.put(tokenNAME, temp);
								}
								else{
									TreeMap<Integer,String> temp=map_tokenSEQ_lineNoNline.get(c);
//									temp.put(lineNo, arr_tokens[c]);
//									map_tokenSEQ_lineNoNline.put(c, temp);
									//lineno, primarykey
//									TreeMap<Integer,String> temp2=map_tokenSEQ_lineNoPrimaryKEY.get(c);
//									temp2.put(lineNo, arr_tokens[token_containing_PrimaryKEY-1]);
//									map_tokenSEQ_lineNoPrimaryKEY.put(c , temp2);
									// PrimaryKEY+tokenSEQ
//									map_PrimaryKeyANDtokenSEQ_lineNoNline.put(curr_primaryKeyANDtokenSEQ, temp);
									//PrimaryKEY+tokenNAME
									map_tokenNAME_asKEY.put(tokenNAME, temp);
								}
							 
								
								//System.out.println("arr_tokens[c]:"+arr_tokens[c]);
								c++;
							} //while(c<arr_tokens.length){
							
							String conc_numerictokennames="";
							//iterate only the tokenNAME having numeric values
							for(String name:map_tokenNAME_asKEY.keySet()){
								if(conc_numerictokennames.length()==0) conc_numerictokennames=name;
								else conc_numerictokennames+=","+name;
								
							}
							writer.append(curr_primaryKey+"!!!"+conc_numerictokennames+"\n");
							writer.flush();
							
							conc_numerictokennames ="";
							map_tokenNAME_asKEY=new TreeMap<String, TreeMap<Integer,String>>();
							// 
							map_alreadyRan_primaryKEY_asKEY.put(curr_primaryKey, -1);
						} //END for(int lineNo:map_file1.keySet()){
				
			
						System.out.println("***output is stored in "+output);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		// p21_get_set_of_DocIDs_for_eachPersona
		private static TreeMap<Integer, TreeMap<Integer, Integer>> p21_get_set_of_DocIDs_for_eachPersona(
																	TreeMap<Integer, String> map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly, 
																	String 					 inFile_dataIDs_to_be_seeded
																	) {
			TreeMap<Integer, TreeMap<Integer, Integer>> map_personaID_DataIDasKEY =new TreeMap<Integer, TreeMap<Integer,Integer>>();
			
			
			System.out.println("inside p21_get_set_of_DocIDs_for_eachPersona-->map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly->"+map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly);
			System.out.println("inside p21_get_set_of_DocIDs_for_eachPersona-->inFile_dataIDs_to_be_seeded-->"+inFile_dataIDs_to_be_seeded);
			
			// TODO Auto-generated method stub
			try {
				// 
				TreeMap<Integer,String> map_file1=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
							.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																			  inFile_dataIDs_to_be_seeded, 
																		 	 -1, //startline, 
																			 -1, //endline,
																			 "f1 ", //debug_label
																			 false //isPrintSOP
																			 );
				
				System.out.println("inside p21_get_set_of_DocIDs_for_eachPersona-->map_file1.size-->"+map_file1.size());
				
				String[] arr_persona1_start_end_lineno_range=map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly.get(1).split(",");
				String[] arr_persona2_start_end_lineno_range=map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly.get(2).split(",");
				String[] arr_persona3_start_end_lineno_range=map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly.get(3).split(",");
				// 
				for(int seq:map_file1.keySet()){
					String eachLine=map_file1.get(seq);
					
					arr_persona1_start_end_lineno_range[0]=arr_persona1_start_end_lineno_range[0].replace(" ", "");
					arr_persona1_start_end_lineno_range[1]=arr_persona1_start_end_lineno_range[1].replace(" ", "");
							
					if(	seq >= Integer.valueOf(arr_persona1_start_end_lineno_range[0]) 
						&& seq <= Integer.valueOf(arr_persona1_start_end_lineno_range[1]) ){
						// 
						if(map_personaID_DataIDasKEY.containsKey(1)){
							TreeMap<Integer, Integer> tmp=map_personaID_DataIDasKEY.get(1);
							tmp.put( Integer.valueOf(eachLine), -1);
							map_personaID_DataIDasKEY.put(1, tmp);
						}
						else{
							TreeMap<Integer, Integer> tmp=new TreeMap<Integer, Integer>();
							tmp.put( Integer.valueOf(eachLine), -1);
							map_personaID_DataIDasKEY.put(1, tmp);	
						}
						
						
					}
					else if(	seq >= Integer.valueOf(arr_persona2_start_end_lineno_range[0]) 
							&& seq <= Integer.valueOf(arr_persona2_start_end_lineno_range[1]) ){
						
						// 
						if(map_personaID_DataIDasKEY.containsKey(2)){
							TreeMap<Integer, Integer> tmp=map_personaID_DataIDasKEY.get(2);
							tmp.put( Integer.valueOf(eachLine), -1);
							map_personaID_DataIDasKEY.put(2, tmp);
						}
						else{
							TreeMap<Integer, Integer> tmp=new TreeMap<Integer, Integer>();
							tmp.put( Integer.valueOf(eachLine), -1);
							map_personaID_DataIDasKEY.put(2, tmp);	
						}
						
					}
					else if(	seq >= Integer.valueOf(arr_persona3_start_end_lineno_range[0]) 
							&& seq <= Integer.valueOf(arr_persona3_start_end_lineno_range[1]) ){
						
						// 
						if(map_personaID_DataIDasKEY.containsKey(3)){
							TreeMap<Integer, Integer> tmp=map_personaID_DataIDasKEY.get(3);
							tmp.put( Integer.valueOf(eachLine), -1);
							map_personaID_DataIDasKEY.put(3, tmp);
						}
						else{
							TreeMap<Integer, Integer> tmp=new TreeMap<Integer, Integer>();
							tmp.put( Integer.valueOf(eachLine), -1);
							map_personaID_DataIDasKEY.put(3, tmp);
						}
						
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return map_personaID_DataIDasKEY;
		}
		//return the person
		public static TreeMap<Integer, TreeMap<Integer, Integer>>  return_map(
																		TreeMap<Integer, String> map_persona_setOFdevices,
																		TreeMap<String, Integer> map_headerTokenOFall_SEQ
																			){

			TreeMap<Integer, TreeMap<Integer, Integer>> map_personaID_IndexOFdeviceasKEY=new TreeMap<Integer, TreeMap<Integer,Integer>>();
			try {
				
				System.out.println("---->return_map: map_persona_setOFdevices:"+map_persona_setOFdevices
										+"<--->map_headerTokenOFall_SEQ:"+map_headerTokenOFall_SEQ);
				
				//read set of devices for each Persona
				for(int curr_personaID:map_persona_setOFdevices.keySet()){
					String [] arr_setOFDeviceName=map_persona_setOFdevices.get(curr_personaID).split(",");
					int c2=0;
					TreeMap<Integer, Integer> temp=new TreeMap<Integer, Integer>();
					// 
					while(c2<arr_setOFDeviceName.length){
						String currDeviceName=arr_setOFDeviceName[c2];
						int indexOFDevice=map_headerTokenOFall_SEQ.get(currDeviceName);
						
						temp.put(indexOFDevice, -1);
						c2++;
					}
					map_personaID_IndexOFdeviceasKEY.put(curr_personaID, temp);
					System.out.println("map_personaID_IndexOFdeviceasKEY:"+map_personaID_IndexOFdeviceasKEY +" dataID:");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
			System.out.println("---->return_map: map_personaID_IndexOFdeviceasKEY:"+map_personaID_IndexOFdeviceasKEY);
			return  map_personaID_IndexOFdeviceasKEY;
		}
		
		//inject_STRUCTURALanomaly_categoricalFiles
		private static void inject_STRUCTURALanomaly_categoricalFiles(	String 		baseFolder,
																		String 		input_Folder_or_file1,
																		String 		delimiter_for_file1,
																		int 		token_containing_PrimaryKEY,
																		String 		tokens_integer_interested_CSV, 
																		String[] 	arr_all_header_CSV,
																		String 		inFile_dataIDs_to_be_seeded_STATISTICAL_DATE,
																		TreeMap<Integer, String> map_SEQ_headerTokenOFall, 
																		TreeMap<String, Integer> map_headerTokenOFall_SEQ,
																		TreeMap<Integer, String> map_persona_setOFdevices,
																		TreeMap<Integer, TreeMap<Integer, Integer>> map_personaID_DataIDasKEY,
																		TreeMap<Integer, TreeMap<Integer, Integer>> map_personaID_IndexOFdeviceasKEY,
																		TreeMap<Integer, TreeMap<String, String>> map_dataID_DATETIMEasKEY,
																		String 		output_File_dataID_N_dateTime_N_deviceName_deviceIndex_N_OLDvalue_N_NEWvalue,
																		boolean		isAppend_output_File_dataID_N_dateTime_N_device_N_OLDvalue_N_NEWvalue
																	 ) {
			FileWriter writer_output=null;
			// TODO Auto-generated method stub
			try {
				
				writer_output=new FileWriter(new File(output_File_dataID_N_dateTime_N_deviceName_deviceIndex_N_OLDvalue_N_NEWvalue),
											  	isAppend_output_File_dataID_N_dateTime_N_device_N_OLDvalue_N_NEWvalue);
				
				String outFile=input_Folder_or_file1.replace(".csv", "").replace(".txt", "") +"_struct.csv";
				System.out.println("output file="+outFile);
				
				TreeMap<Integer,  Integer> map_token_INTEGERinterestedINDEX_as_KEY=new TreeMap<Integer, Integer>();
				String [] arr_tokens_interested_CSV=tokens_integer_interested_CSV.split(",");
				//
				int j=0;
				while(j<arr_tokens_interested_CSV.length){
					map_token_INTEGERinterestedINDEX_as_KEY.put( Integer.valueOf(arr_tokens_interested_CSV[j])-1 , -1);
					j++;
				}
				//remove clean suffix
				FileWriter writer = new FileWriter(new File(outFile));
				FileWriter writer_debug = new FileWriter(new File(baseFolder+"debug_.txt") , true);
				//
//				writer_debug.append("\n map_token_INTEGERinterestedINDEX_as_KEY: "+map_token_INTEGERinterestedINDEX_as_KEY);
//				writer_debug.append("\n map_dataID_DATETIMEasKEY: "+map_dataID_DATETIMEasKEY);
				writer_debug.flush();
				
//				TreeMap<Integer, TreeMap<Integer, Integer>> map_personaID_IndexOFdeviceasKEY=new TreeMap<Integer, TreeMap<Integer,Integer>>();

				//read set of devices for each Persona
//				for(int curr_personaID:map_persona_setOFdevices.keySet()){
//					String [] arr_setOFDeviceName=map_persona_setOFdevices.get(curr_personaID).split(",");
//					int c2=0;
//					TreeMap<Integer, Integer> temp=new TreeMap<Integer, Integer>();
//					// 
//					while(c2<arr_setOFDeviceName.length){
//						String currDeviceName=arr_setOFDeviceName[c2];
//						int indexOFDevice=map_headerTokenOFall_SEQ.get(currDeviceName);
//						
//						temp.put(indexOFDevice, -1);
//						c2++;
//					}
//					map_personaID_IndexOFdeviceasKEY.put(curr_personaID, temp);
//					System.out.println("persona:"+map_personaID_IndexOFdeviceasKEY +" dataID:");
//				}
				// 
				TreeMap<Integer,String> map_file1=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
							.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																									 input_Folder_or_file1, 
																								 	 -1, //startline, 
																									 -1, //endline,
																									 "f1 ", //debug_label
																									 false //isPrintSOP
																			 						 );
				int found_personaID_for_currPrimaryKEY=-1;
				TreeMap<Integer, Integer> map_temp_FOUND_personaID_IndexOFdeviceasKEY=new TreeMap<Integer, Integer>();
				// 
				for(int seq:map_file1.keySet()){
					String eachLine=map_file1.get(seq);
					String[] arr_=eachLine.split(delimiter_for_file1);
					String primarKey_dataID =arr_[token_containing_PrimaryKEY-1];
					String curr_datatime =arr_[1].replace("\"", "");
					found_personaID_for_currPrimaryKEY=-1;
					boolean isFound_currDataID_dateTime=false;
					
					//
//					System.out.println( "primarKey_dataID:"+primarKey_dataID+" datetime:"+curr_datatime
//											+" map_dataID_DATETIMEasKEY:"+map_dataID_DATETIMEasKEY);
					//  get list of DATETIME to inject struct anomalies
					if(map_dataID_DATETIMEasKEY.containsKey(Integer.valueOf(primarKey_dataID)) ){
						// 
						if(map_dataID_DATETIMEasKEY.get(Integer.valueOf(primarKey_dataID)).containsKey(curr_datatime)){
							isFound_currDataID_dateTime=true;
						}
					}
					
					//check if any personaID exists for this primary key (dataID)
					//read set of devices for each Persona (Which personaID this dataID (pKEY) belongs to??? )
					for(int curr_personaID:map_personaID_DataIDasKEY.keySet()){
						// is found ?
						if(map_personaID_DataIDasKEY.get(curr_personaID).containsKey(Integer.valueOf(primarKey_dataID) )){
							found_personaID_for_currPrimaryKEY=curr_personaID;
							break;
						}
					}
					// get the set of Device for the found PersonaID above <----> _FOUND_personaID_IndexOFdeviceasKEY
					if(map_personaID_IndexOFdeviceasKEY.containsKey(found_personaID_for_currPrimaryKEY)
						&& found_personaID_for_currPrimaryKEY>0){
						//
						map_temp_FOUND_personaID_IndexOFdeviceasKEY = map_personaID_IndexOFdeviceasKEY.get(found_personaID_for_currPrimaryKEY);
					}
						int c=0;
						String concLine="";
						String concLineDEBUG="";
						// 
						while(c<arr_.length){
							// 
							if(concLine.length()==0){
								// 
								if(map_temp_FOUND_personaID_IndexOFdeviceasKEY.containsKey(c)){
									
									if( Integer.valueOf(primarKey_dataID) == 9931){
//										writer_output.append("Found->"+primarKey_dataID+"!!!"+curr_datatime+"!!!"+map_SEQ_headerTokenOFall.get(c)
//															+"!!!"+isFound_currDataID_dateTime
//															+"!!!"+c
//															+"!!!found_personaID_for_currPrimaryKEY:"+found_personaID_for_currPrimaryKEY
//															+"!!!"+map_temp_FOUND_personaID_IndexOFdeviceasKEY
//															+"!!!"+map_temp_FOUND_personaID_IndexOFdeviceasKEY.containsKey(c)
//															+"!!!"+map_persona_setOFdevices
//															+"!!!map_personaID_DataIDasKEY:"+map_personaID_DataIDasKEY
//															+"\n");
										writer_output.flush();
									}
									//
									if(map_token_INTEGERinterestedINDEX_as_KEY.containsKey(c) && isFound_currDataID_dateTime==true ){
										concLine="vveryHigh";
										//record out (_dataID_N_dateTime_N_deviceName_deviceIndex_N_OLDvalue_N_NEWvalue)
										writer_output.append(primarKey_dataID+"!!!"+curr_datatime+"!!!"+map_SEQ_headerTokenOFall.get(c)
															+"!!!"+c+"!!!"+arr_[c]+"!!!"+"vveryHigh"+"\n");
										writer_output.flush();
										
										concLineDEBUG="vveryHigh" +"("+ map_SEQ_headerTokenOFall.get(c) + ")" ;
									}
									// an empty "na" token, and can still be used to replace with STRUCTURAL INJECTION
									else if(!map_token_INTEGERinterestedINDEX_as_KEY.containsKey(c) && isFound_currDataID_dateTime==true){						
										concLine="vveryHigh";
										//record out (_dataID_N_dateTime_N_deviceName_deviceIndex_N_OLDvalue_N_NEWvalue)
										writer_output.append(primarKey_dataID+"!!!"+curr_datatime+"!!!"+map_SEQ_headerTokenOFall.get(c)
															+"!!!"+c+"!!!"+arr_[c]+"!!!"+"vveryHigh"+"\n");
										writer_output.flush();
									}
									else{
										concLine=arr_[c];
										
										concLineDEBUG=arr_[c]+"("+ map_SEQ_headerTokenOFall.get(c) + ")";
									}
								}
								else{
									concLine=arr_[c];
									
									concLineDEBUG=arr_[c]+"("+ map_SEQ_headerTokenOFall.get(c) + ")";
								}
							}
							else{
//								if( Integer.valueOf(primarKey_dataID) == 9931){
//									writer_output.append("2.Found->"+primarKey_dataID+"!!!"+curr_datatime+"!!!"+map_SEQ_headerTokenOFall.get(c)
//															+"!!!"+isFound_currDataID_dateTime
//															+"!!!"+map_temp_FOUND_personaID_IndexOFdeviceasKEY.containsKey(c)
//															+"!!!found_personaID_for_currPrimaryKEY:"+found_personaID_for_currPrimaryKEY
//															+"!!!"+c
//															+"!!!"+map_temp_FOUND_personaID_IndexOFdeviceasKEY
//															+"!!!"+map_persona_setOFdevices
//															+"!!!map_personaID_DataIDasKEY:"+map_personaID_DataIDasKEY
//															+"\n");
//									writer_output.flush();
//								}
								
								//
								if(map_temp_FOUND_personaID_IndexOFdeviceasKEY.containsKey(c)){
									
									if(map_token_INTEGERinterestedINDEX_as_KEY.containsKey(c) && isFound_currDataID_dateTime==true){
										concLine+=","+"vveryHigh";

										//record out (_dataID_N_dateTime_N_deviceName_deviceIndex_N_OLDvalue_N_NEWvalue)
										writer_output.append(primarKey_dataID+"!!!"+curr_datatime+"!!!"+map_SEQ_headerTokenOFall.get(c)
															+"!!!"+c+"!!!"+arr_[c]+"!!!"+"vveryHigh"+"\n");
										writer_output.flush();
										
										concLineDEBUG+=","+arr_[c]+"("+ map_SEQ_headerTokenOFall.get(c) + ")";
								
									}
									// an empty "na" token, and can still be used to replace with STRUCTURAL INJECTION
									else if(!map_token_INTEGERinterestedINDEX_as_KEY.containsKey(c) && isFound_currDataID_dateTime==true){
										concLine+=","+"vveryHigh";
										
										//record out (_dataID_N_dateTime_N_deviceName_deviceIndex_N_OLDvalue_N_NEWvalue)
										writer_output.append(primarKey_dataID+"!!!"+curr_datatime+"!!!"+map_SEQ_headerTokenOFall.get(c)
															+"!!!"+c+"!!!"+arr_[c]+"!!!"+"vveryHigh"+"\n");
										writer_output.flush();
										
									}
									else{
										concLine+=","+arr_[c];
										
										concLineDEBUG+=","+arr_[c]+"("+ map_SEQ_headerTokenOFall.get(c) + ")";
									}
								}
								else{
										concLine+=","+arr_[c];
										
										concLineDEBUG+=","+arr_[c]+"("+ map_SEQ_headerTokenOFall.get(c) + ")";
								}
							} //if(concLine.length()==0){
							
							// 
							if(isFound_currDataID_dateTime==true && c==arr_.length-1 ){
								writer_debug.append("found.."+concLineDEBUG  // + "--"+map_SEQ_headerTokenOFall
																+  "\n");
								writer_debug.flush();
							}
						
							writer_output.flush();
							c++;
						} //while(c<arr_.length){
						
						writer.append(concLine+"\n");
						writer.flush();	
					} // for(int seq:map_file1.keySet()){
				System.out.println("injected FILE output_File_dataID_N_dateTime_N_deviceName_deviceIndex_N_OLDvalue_N_NEWvalue:"+output_File_dataID_N_dateTime_N_deviceName_deviceIndex_N_OLDvalue_N_NEWvalue);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}	
		}
		
		//clean_last_column_from_categoricalFiles
				private static void clean_last_column_from_categoricalFiles(String baseFolder, 
																			String input_Folder_or_file1,
																			String delimiter_for_file1) {
					
					// TODO Auto-generated method stub
					try {
						FileWriter writer = new FileWriter(new File(input_Folder_or_file1.replace(".csv", "").replace(".txt", "") +"_clean.csv"));
						FileWriter writer_debug = new FileWriter(new File(baseFolder+"debug_.txt"));
						
						TreeMap<Integer,String> map_file1=
								ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
									.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																											 input_Folder_or_file1, 
																										 	 -1, //startline, 
																											 -1, //endline,
																											 "f1 ", //debug_label
																											 false //isPrintSOP
																					 						 );
						// 
						for(int seq:map_file1.keySet()){
							String eachLine=map_file1.get(seq);
							String[] arr_=eachLine.split(delimiter_for_file1);
							/// 
							if(arr_[arr_.length-1].indexOf("000")>=0){
								int c=0;
								String concLine="";
								//
								while(c<arr_.length){
									// 
									if(concLine.length()==0){
										concLine=arr_[c];
									}
									else{
										// 
										if(c == arr_.length-1){
											if(arr_[c].indexOf("000")>=0 && arr_[c].indexOf(".")>=0){
												// 
												if(arr_[c].indexOf(".")>=0){
													
													concLine+=","+arr_[c].substring(0 , arr_[c].indexOf("."));
													System.out.println("match lastcolumn concLine:"+concLine +" <-->"+eachLine);
												
												}
											}
											// . exists, but not 0? 
											if(arr_[c].indexOf("0")>=0  && arr_[c].indexOf(".")==-1){
												writer_debug.append(eachLine+ " from file="+input_Folder_or_file1+"\n");
												writer_debug.flush();
											}
											
											
										}
										else{
											concLine+=","+arr_[c];
										}
									}
									c++;
								} //while(c<arr_.length){
								
								writer.append(concLine+"\n");
								writer.flush();
								
							}
							else{
								writer.append(eachLine+"\n");
								writer.flush();
							}
							
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
				}
		
	// main
	public static void main(String[] args) throws Exception{
		 
		 /**** p21 steps notes
		 * (1) split_inputFile_2_multipleEachFile1_N_minutes() <- split weekly or monthly or seconds
		 * (2) Class file p19_p21_convert_token_with_numbers_to_AggregateNumbers() <- convert to categorical data
		 * 			(	Flag==1  --> generate_seeded_pattern()
		 * 				Flag==11 --> p21_get_list_of_validDevices_for_Each_dataID() <--- output "dataID_n_validDevices.txt"<---PERSONA
		 * 				Flag==2  --> p19_p21_convert_token_with_numbers_to_AggregateNumbers() )
		 * (3) p21_convert_to_GBAD ()   ////OR may be crawler() "prob" "p5" <- create GBAD files
		 */
		 
		// manual change 
		// p21 
		String all_tokens_HEADERS_CSV="dataid,local_15min,use,air1,air2,air3,airwindowunit1,aquarium1,bathroom1,bathroom2,bedroom1,bedroom2,bedroom3,bedroom4"
									+ ",bedroom5,car1,clotheswasher1,clotheswasher_dryg1,diningroom1,diningroom2,dishwasher1,disposal1,drye1,dryg1,freezer1,furnace1,furnace2,garage1,garage2,gen,grid,heater1,housefan1,icemaker1,jacuzzi1,kitchen1,kitchen2,kitchenapp1,kitchenapp2,lights_plugs1,lights_plugs2,lights_plugs3,lights_plugs4,lights_plugs5,lights_plugs6,livingroom1,livingroom2,microwave1,office1,outsidelights_plugs1,outsidelights_plugs2,oven1,oven2,pool1,pool2,poollight1,poolpump1,pump1,range1,refrigerator1,refrigerator2,security1,shed1"
									+ ",sprinkler1,utilityroom1,venthood1,waterheater1,waterheater2,winecooler1";
		String high_energy_device_CSV="heater1,clotheswasher_dryg1,air1,air2,air3,airwindowunit1,waterheater1,waterheater2,jacuzzi1,drye1,dryg1";
		//only integer tokens to be considered to calculated mean and standard deviation..
		String tokens_integer_interested_CSV="3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,"
											+ "31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,"
											+ "63,64,65,66,67,68,69"; //p21 problem //just added 69 lately
		/*******
		 *  "run_Flag"
		 * 	1   = generate_seeded_pattern()
		 *  11  = p21_get_list_of_validDevices_for_Each_dataID() <--- output "dataID_n_validDevices.txt"<---PERSONA
		 *  2   = convert_token_with_numbers_to_AggregateNumbers()
		 *  3   = Read and Clean the last column of files from Flag=2 (having .0000 or .xx0000 - remove values after .) --cleaning categorical files
		 *  4   = Inject Structural anomalies
		 */
		int run_Flag=2;
		// "Flag_method_type_for_DATETIME_matching_on_seeding"
		// BELOW Flag_method_type_for_DATETIME_matching_on_seeding
		// 1->seeded based on match of date* only from var "seeded_dataid_datetime_CSV" <dataid!!!date>
		// 2->seeded based on match of datetime* only from var <dataid!!!datetime!!!tokenIndex>
		int Flag_method_type_for_DATETIME_matching_on_seeding=2;
		//
		String  baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/daily/";
				baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/daily_try1/daily-split-NOseeding/"; //not seeded
				baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/missingMonthlyMay20152016/daily_missingMonthlyMay20152016/";
				baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/daily_try1/daily-split-NOseeding/daily_missingMonthlyMay20152016/temp/";
				baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2012Only-numeric/";
				baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2014Only-numeric/categorical/";
				baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/daily_try1/daily-split-SEEDED/2016OnlyCategorical/";
				baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2013Only-numeric/";
				baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2014Only-numeric/categorical/categoricaltry2_clean_injectStructural/";
				baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2014Only-numeric_try2/";
				
		String input_Folder_or_file1=baseFolder;//+"all_2013.txt"; // Flag==2 can be folder
		String inFile_UNIQUE_dataIDs="/Users/lenin/Downloads/reu-cdean/RawData/all_only_UNIQUE_dataid.txt"; 
		// input_1 (dataid choices FOR "statistical" anomaly)
		String inFile_dataIDs_to_be_seeded_STATISTICAL="/Users/lenin/Downloads/reu-cdean/RawData/all_only_UNIQUE_dataid_for_SEEDINGpattern_STATISTICAL.txt";
		
		// input_2 (datetime choices) < --this is only for STATISTICAL anomaly
		String input_file_seededPATTERN_4_datetime_CSV="/Users/lenin/Downloads/reu-cdean/RawData/all_only_UNIQUE_datetime_for_SEEDINGpattern.txt";
		///NOTE : below file has the pattern to INSERT anomalies
		String outputFile_generate_SEEDED_COMBINATION_dataID_N_datetime_higherEnergyDevice=
								"/Users/lenin/Downloads/reu-cdean/RawData/"+"SEEDED_dataID_N_datetime_higherEnergyDevice.txt"; // output
		String outputFile_generate_housingTYPE_setOFdataIDs=
														"/Users/lenin/Downloads/reu-cdean/RawData/"+"out_housingType_setOFdataIDs.txt";
		String delimiter_for_file1=",";
		int token_containing_PrimaryKEY=1;
		// input_4 ( Data IDs from only Austing TX, ANY YEAR - taken from database )
		String inFile_DataID_Austin="/Users/lenin/Downloads/reu-cdean/buildingType_dataID_austinTX.txt";
		// input_5
		String inFile_DataID_Texas_I_2013=
		"/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2013Only-numeric/categorical/categoricaltry1/dataIDS_existsin2013_N_inTexas.txt";
		inFile_DataID_Texas_I_2013="/Users/lenin/Downloads/reu-cdean/RawData/daily_try1/daily-split-SEEDED/2016OnlyCategorical/dataIDS_existsin2013_N_inTexas.txt";
		// 2014 only "2014Only-numeric/categorical/categoricaltry1/dataIDS_existsin2013_N_inTexas.txt"
		inFile_DataID_Texas_I_2013="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2014Only-numeric/categorical/categoricaltry1/dataIDS_existsin2013_N_inTexas.txt";
		// 2014 only (TAKEN from RAW DATA of 2014)
		inFile_DataID_Texas_I_2013="/Users/lenin/Downloads/reu-cdean/RawData/2014_only_all_dataIDs_ONLY.txt";
		
		////// Flag==1 //////////// 
		// These are used to create STRUCTURAL ANOMALIES
		// line no range for set of dataIDs for each of person - persona1, persona2, persona3 <- 3 different persona
		TreeMap<Integer, String> map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly=new TreeMap<Integer, String>();
		map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly.put(1, "1,4" ); map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly.put(2, "5,7" );
		map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly.put(3, "8,10");
		// Decided Set of Devices for each persona*  /////// SETTING 1
			// 	---> also for Flag==4
		TreeMap<Integer, String> map_persona_setOFdevices=new TreeMap<Integer, String>();
		map_persona_setOFdevices.put(1, "kitchen1,kitchen2,kitchenapp1,kitchenapp2,oven1,oven2,microwave1,freezer1,winecooler1" ); 
		map_persona_setOFdevices.put(2, "waterheater1,waterheater2,jacuzzi1,bathroom1,bathroom2,clotheswasher1,clotheswasher_dryg1,drye1,dryg1" );
		map_persona_setOFdevices.put(3, "livingroom1,livingroom2,pool1,poollight1,outsidelights_plugs1,outsidelights_plugs2" );
		///////// SETTING 2
		map_persona_setOFdevices.put(1, "winecooler1" ); 
		map_persona_setOFdevices.put(2, "jacuzzi1" );
		map_persona_setOFdevices.put(3, "poollight1" );
		
		////// Flag==1 ////////////
		
		//// Flag==2 ////////////
		String output_dataID_date_mean_SD=baseFolder+"out_dataID_mean_SD.txt";
		//// Flag==2 ////////////
		
		////// Flag==4 ////////////  
		/// BELOW 2 files should have same number of lines.. each line in first file is dataID, and each in 2nd is corresponding datetime we interested
		// 			to insert structural anomalies....  
		// input_3 (dataID choices FOR "structure" anomaly)
		String inFile_dataIDs_to_be_seeded_STUCTURAL="/Users/lenin/Downloads/reu-cdean/RawData/all_only_UNIQUE_dataid_for_SEEDINGpattern_STRUCTURE.txt";
			// input_1 (2)
		String inFile_dataIDs_to_be_seeded_STUCTURAL_DATEcsv="/Users/lenin/Downloads/reu-cdean/RawData/all_only_UNIQUE_dataid_for_SEEDINGpattern_STRUCTURE_DATEScsv.txt";
		//OUTPUT
		String output_File_dataID_N_dateTime_N_device_N_OLDvalue_N_NEWvalue_4structural
												="/Users/lenin/Downloads/reu-cdean/RawData/OUTPUT__dataID_N_dateTime_N_device_N_OLDvalue_N_NEWvalue_4structural.txt";
		
		////// Flag==4 //////////// 
		
		// seeded anomalies
		// high energy - heater1,clotheswasher_dryg1,air1,air2,air3,airwindowunit1,waterheater1,waterheater2,jacuzzi1,drye1,dryg1
		long t0 = System.nanoTime();
		boolean is_inputFolder_or_File=false;
		
		// 1= calculate mean and standard.deviation for the whole file.
		// 2= calculate mean and standard.deviation primary-key wise
		int Flag_to_calc_MEAN_n_StdDEV =2; 
		String [] arr_all_header_CSV=all_tokens_HEADERS_CSV.split(",");
		String [] arr_high_energy_device_CSV=high_energy_device_CSV.split(",");
		int c2=0; TreeMap<Integer,String> map_SEQ_higherEnergyDevice=new TreeMap<Integer, String>();
		TreeMap<Integer,String> map_SEQ_headerTokenOFall=new TreeMap<Integer, String>();
		TreeMap<String, Integer> map_headerTokenOFall_SEQ=new TreeMap<String, Integer>();
		//each token (from header)
		while(c2<arr_all_header_CSV.length){
//			System.out.println("arr_tokens[]:"+arr_all_header_CSV[c2]+"--- token:"+(c2+1));
			map_SEQ_headerTokenOFall.put(c2, arr_all_header_CSV[c2]);
			map_headerTokenOFall_SEQ.put(arr_all_header_CSV[c2], c2);
			c2++;
		}
		c2=0;
		//interested each token for SEEDING (subset of all headers)
		while(c2<arr_high_energy_device_CSV.length){
			map_SEQ_higherEnergyDevice.put((c2+1), arr_high_energy_device_CSV[c2]); 
			c2++;
		}
		///--------- Load DataIDS that belong to only Austin, TEXAS (ANY YEAR)
		TreeMap<Integer,String> map_inFile_seq_eachLine1_dataID_housingType=
				ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																	inFile_DataID_Austin, 
																 	 -1, //startline, 
																	 -1, //endline,
																	 "f1 ", //debug_label
																	 false //isPrintSOP
																	 );
		TreeMap<Integer, String> map_dataID_housingType_=new TreeMap<Integer, String>();
		for(int seq:map_inFile_seq_eachLine1_dataID_housingType.keySet()){
			if(seq==1) continue; //header
			String arr_housingType_dataID_[]= map_inFile_seq_eachLine1_dataID_housingType.get(seq).split(",");
			
			map_dataID_housingType_.put(Integer.valueOf(arr_housingType_dataID_[1]), arr_housingType_dataID_[0]);
		}
		///--------- Load DataIDS that belong to 2013 and also in Austin, Texas 
		TreeMap<Integer,String> map_inFile_seq_eachLine2_dataID_SetOFDevicesINcsv=
				ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																	 inFile_DataID_Texas_I_2013, 
																 	 -1, //startline, 
																	 -1, //endline,
																	 "f1 ", //debug_label
																	 false //isPrintSOP
																	 );
		TreeMap<Integer, String> map_dataID_SetOFDevicesINcsv=new TreeMap<Integer, String>();
		for(int seq:map_inFile_seq_eachLine2_dataID_SetOFDevicesINcsv.keySet()){
			String arr_housingType_dataID_[]= map_inFile_seq_eachLine2_dataID_SetOFDevicesINcsv.get(seq).split("!!!");
			map_dataID_SetOFDevicesINcsv.put(Integer.valueOf(arr_housingType_dataID_[0]), "") ; // arr_housingType_dataID_[1]);
		}
		TreeMap<String, TreeMap<Integer, Integer> > map_HousingType_DataIDasKEY=new TreeMap<String, TreeMap<Integer,Integer>>();
		TreeMap<String, TreeMap<Integer, Integer> > map_HousingType_DataIDasKEY_I_YYYY=new TreeMap<String, TreeMap<Integer,Integer>>();
		
		FileWriter writer_10 = new FileWriter(new File(outputFile_generate_housingTYPE_setOFdataIDs));
		//// match and get list of dataID for each of the "housing_type" --- "map_dataID_SetOFDevicesINcsv" and "map_dataID_housingType_"
		for(int DataID:map_dataID_housingType_.keySet()){
			String curr_housingType=map_dataID_housingType_.get(DataID);
			if(map_HousingType_DataIDasKEY.containsKey(curr_housingType)){
				TreeMap<Integer, Integer> tmp=map_HousingType_DataIDasKEY.get(curr_housingType);
				tmp.put(DataID, -1);
				map_HousingType_DataIDasKEY.put(curr_housingType, tmp);
			}
			else{
				TreeMap<Integer, Integer> tmp=new TreeMap<Integer, Integer>();
				tmp.put(DataID, -1);
				map_HousingType_DataIDasKEY.put(curr_housingType, tmp);
			}
			 // also in YYYY 
			if(map_dataID_SetOFDevicesINcsv.containsKey(DataID)){
				//
				writer_10.append("\nfound DataID:"+DataID);
				writer_10.flush();
				//
				if(map_HousingType_DataIDasKEY_I_YYYY.containsKey(curr_housingType)){
					TreeMap<Integer, Integer> tmp=map_HousingType_DataIDasKEY_I_YYYY.get(curr_housingType);
					tmp.put(DataID, -1);
					map_HousingType_DataIDasKEY_I_YYYY.put(curr_housingType, tmp);
				}
				else{
					TreeMap<Integer, Integer> tmp=new TreeMap<Integer, Integer>();
					tmp.put(DataID, -1);
					map_HousingType_DataIDasKEY_I_YYYY.put(curr_housingType, tmp);
				}
			}
			
		}

		writer_10.append("\n File 1->"+inFile_DataID_Austin+" "+map_dataID_housingType_.size());
		writer_10.append("\n File 2->"+inFile_DataID_Texas_I_2013+  " "+map_inFile_seq_eachLine2_dataID_SetOFDevicesINcsv.size()+"\n");
		for(String curr:map_HousingType_DataIDasKEY.keySet()){
			writer_10.append("htype:"+curr+"<--->"+map_HousingType_DataIDasKEY.get(curr)
											 +"<---->found in YYYY"+map_HousingType_DataIDasKEY_I_YYYY.get(curr) +"\n");
			writer_10.flush();
		}
		
		//////////////////////////
		if(run_Flag==1){///<---------------------------
			// generating file for var "file_seededPATTERN_dataid_datetime_CSV"  (STATISTICAL ANOMALIES)
			// this generates for a set of dataID(unique file) and "arr_high_energy_device_CSV" to generate 
			generate_seeded_pattern(
									inFile_UNIQUE_dataIDs, //input
									inFile_dataIDs_to_be_seeded_STATISTICAL, // input 1 (<dataID>)
									input_file_seededPATTERN_4_datetime_CSV, //input 2 (<datetime>)
									arr_all_header_CSV,
									arr_high_energy_device_CSV,
									map_SEQ_higherEnergyDevice,  //input 3 <higherEnergeyDevice>
									map_headerTokenOFall_SEQ, 
									outputFile_generate_SEEDED_COMBINATION_dataID_N_datetime_higherEnergyDevice //OUTPUT
									);
		}
		//read for combined primary key
		TreeMap<Integer,String> map_inFile_seq_eachLine1=
				ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																	 outputFile_generate_SEEDED_COMBINATION_dataID_N_datetime_higherEnergyDevice, 
																 	 -1, //startline, 
																	 -1, //endline,
																	 "f1 ", //debug_label
																	 false //isPrintSOP
																	 );
		TreeMap<String,String> map_COMBINATION_dataID_N_datetime_higherEnergyDeviceINDEX_asKEY=new TreeMap<String, String>();
		// LOADING combination->dataID_N_datetime_higherEnergyDeviceINDEX_asKEY
		for(int seq:map_inFile_seq_eachLine1.keySet()){
			String []arr_=map_inFile_seq_eachLine1.get(seq).split("!!!");
			String combinedPrimaryKEY="";
			if(Flag_method_type_for_DATETIME_matching_on_seeding==2)
				combinedPrimaryKEY=arr_[0]+"!!!"+arr_[1]+"!!!"+arr_[2]; //assume may have date or datetime 
			else if(Flag_method_type_for_DATETIME_matching_on_seeding==3){
				combinedPrimaryKEY=arr_[0]+"!!!"+arr_[1];
			}
//			System.out.println("11:"+combinedPrimaryKEY);
			map_COMBINATION_dataID_N_datetime_higherEnergyDeviceINDEX_asKEY.put(combinedPrimaryKEY, "");
		}
		
		TreeMap<Integer, TreeMap<Integer, Integer>> map_personaID_DataIDasKEY=new TreeMap<Integer, TreeMap<Integer,Integer>>();
		
		// For each persona, get set of DataIDs under them as per lineNo range as per map "map_persona_lineRange_forRangeOFdataIDs"
		// For each dataID, read and get the list of devices that has numerical values (i.e., valid devices for each dataID) 
		if(run_Flag==11){ ///<---------------------------
			
			////////////////////// p21_get_set_of_DocIDs_for_eachPersona			
			map_personaID_DataIDasKEY= p21_get_set_of_DocIDs_for_eachPersona(
																			 map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly,
																			 inFile_dataIDs_to_be_seeded_STUCTURAL
																			);
//			System.out.println("map_personaID_DataIDasKEY:"+map_personaID_DataIDasKEY);
			
			////////////////////// START get list of validDevices for Each_dataID (devices that has NUMERICAL VALUES)
			int max=-1;
			String [] arr_list_files=new String[10000];
			if(new File(input_Folder_or_file1).isDirectory()){
				arr_list_files=new File(input_Folder_or_file1).list();
				max=arr_list_files.length;
			}
			else{
				arr_list_files[0]=input_Folder_or_file1;
				max=1;
			}
			
			int c=0;
			//each file run..
			while(c<max){
				String curr_file=baseFolder+arr_list_files[c];
				 // gives output file "dataID_n_validDevices.txt"
				// convert_token_with_numbers_to_AggregateNumbers
				p21_get_list_of_validDevices_for_Each_dataID(
															  baseFolder,
															  curr_file, //input_Folder_or_file1,
															  delimiter_for_file1,
															  token_containing_PrimaryKEY,
															  tokens_integer_interested_CSV,
															  arr_all_header_CSV
															  );
				c++;
			} // END while(c<max){
			/////////////////////////// compare between DataIDs of Austin, Texas and year 2012 or 2013 and so on...
			boolean isDir=new File(input_Folder_or_file1).isDirectory();
//			if(isDir==false){
				//
				String firstFile=baseFolder+"dataID_n_validDevices.txt";
				String secondFile="/Users/lenin/Downloads/reu-cdean/RawData/all_only_UNIQUE_dataid_from_AUSTIN_TX.txt";
	 
				String outFile_Exists=baseFolder+"dataIDS_existsin2013_N_inTexas.txt";
				String outFile_Not_Exists=baseFolder+"dataIDS_existsin2013_N_NOTinTexas.txt";
				String outFile_File1_addedComment=firstFile+"_addedComment.txt";
				
				String delimiter_to_be_added_to_first_File="";
				int token_interested_in_first_File=1;
				
				//readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String
				ReadFile_2_EachLineORtoken_from_First_File_exists_in_Second_File_as_String.
				readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String(
																					firstFile,
																					delimiter_to_be_added_to_first_File,
																					token_interested_in_first_File,//-1
																					secondFile,
																					"!!!", // delimiter_for_splitting
																					outFile_Exists,
																					false, //is_Append_outFile_exists
																					outFile_Not_Exists,
																					false, //is_Append_outFile_not_exists
																					outFile_File1_addedComment,
																					false //if(isSOPprint)
																					);
				
//			}
			
		} // END if(run_Flag==11){
		
		// 
		try{
		// is dir
		is_inputFolder_or_File=IsFile_a_Directory.isFile_a_Directory(input_Folder_or_file1);

		FileWriter writer_dataID_date_mean_SD=null;
		if(run_Flag==2){ ///<---------------------------
			long t2 = System.nanoTime();
			TreeMap<String, TreeMap<String,  Double>> map_Date_PrimaryKeyToken_Mean=new TreeMap<String, TreeMap<String, Double>>();
			TreeMap<String, TreeMap<String,  Double>> map_Date_PrimaryKeyToken_StandardDeviation=new TreeMap<String, TreeMap<String, Double>>();
			// get the past saved mean and Standard Deviation
			if(!new File(output_dataID_date_mean_SD).exists()){
				writer_dataID_date_mean_SD=new FileWriter(output_dataID_date_mean_SD);
			}
			else{
				//append
				writer_dataID_date_mean_SD=new FileWriter(output_dataID_date_mean_SD, true);
				// <DATE!!!dataID!!!tokenSequence!!!mean!!!standardDeviation>
				TreeMap<Integer,String> map_eachLine_already_saved_mean_SD=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
							.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																						  output_dataID_date_mean_SD, 
																					 	 -1, //startline, 
																						 -1, //endline,
																						 "f1 ", //debug_label
																						 false //isPrintSOP
																						 );
				
				
				System.out.println("loaded map_eachLine_already_saved_mean_SD.size:"+map_eachLine_already_saved_mean_SD.size());
				// 
				for(int seq:map_eachLine_already_saved_mean_SD.keySet()){
					String eachLine=map_eachLine_already_saved_mean_SD.get(seq);
					String [] arr_ =eachLine.split("!!!");
					String curr_date=arr_[0];
//					System.out.println("loaded 1:"+arr_.length);
					if(arr_.length<4) continue;
					
					int currDataID=Integer.valueOf(arr_[1]);
					boolean is_available_dataID_for_yearNAustin=false;
					//// is this dataid corresponds to Austin and year YYYY
					for(String currHousingType:map_HousingType_DataIDasKEY_I_YYYY.keySet()){
						if(map_HousingType_DataIDasKEY_I_YYYY.get(currHousingType).containsKey(currDataID)){
							is_available_dataID_for_yearNAustin=true;
							break;
						}
					}
					if(is_available_dataID_for_yearNAustin==false) 
						continue;
					//  MEAN
					if( map_Date_PrimaryKeyToken_Mean.containsKey(curr_date) ){
						TreeMap<String, Double> tmp=map_Date_PrimaryKeyToken_Mean.get(curr_date);
						tmp.put(arr_[1]+","+arr_[2], Double.valueOf(arr_[3])); //mean
						map_Date_PrimaryKeyToken_Mean.put(curr_date, tmp);	
					}
					else{
						TreeMap<String, Double> tmp=new TreeMap<String, Double>();
						tmp.put(arr_[1]+","+arr_[2], Double.valueOf(arr_[3])); //
						map_Date_PrimaryKeyToken_Mean.put(curr_date, tmp);
					}
//					System.out.println("loaded 2.1:"+arr_.length);
					// STANDARD DEVIATION
					if( map_Date_PrimaryKeyToken_StandardDeviation.containsKey(curr_date) ){
						TreeMap<String, Double> tmp=map_Date_PrimaryKeyToken_StandardDeviation.get(curr_date);
						tmp.put(arr_[1]+","+arr_[2], Double.valueOf(arr_[4]));
						map_Date_PrimaryKeyToken_StandardDeviation.put(curr_date, tmp);	
					}
					else{
						TreeMap<String, Double> tmp=new TreeMap<String, Double>();
						tmp.put(arr_[1]+","+arr_[2], Double.valueOf(arr_[4])); //
						map_Date_PrimaryKeyToken_StandardDeviation.put(curr_date, tmp);
					}
				}
//				System.out.println("loaded 3:");
			} // END if(!new File(output_dataID_date_mean_SD).exists()){
			
			System.out.println("Time Taken (FINAL ENDED):"
									+ NANOSECONDS.toSeconds(System.nanoTime() - t2)
									+ " seconds; "
									+ (NANOSECONDS.toSeconds(System.nanoTime() - t2)) / 60
									+ " minutes");
			
			System.out.println("loaded from past run map_Date_PrimaryKeyToken_StandardDeviation:"+map_Date_PrimaryKeyToken_StandardDeviation.size());
			System.out.println("loaded from past run map_Date_PrimaryKeyToken_Mean:"+map_Date_PrimaryKeyToken_Mean.size());
			map_personaID_DataIDasKEY = new TreeMap<Integer, TreeMap<Integer,Integer>>();
			//  get set of DocIDs for each Persona	( STRUCTURAL anomalies )
			map_personaID_DataIDasKEY = p21_get_set_of_DocIDs_for_eachPersona(
																				map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly,
																				inFile_dataIDs_to_be_seeded_STUCTURAL 
																			 );
//			System.out.println("map_personaID_DataIDasKEY:"+map_personaID_DataIDasKEY);
			
			//INPUT is ONLY a file
			if(is_inputFolder_or_File==false){
				System.out.println("#### Only one file to run....");
				// convert_token_with_numbers_to_AggregateNumbers
				p19_p21_convert_token_with_numbers_to_AggregateNumbers(
															  baseFolder,
															  input_Folder_or_file1,
															  delimiter_for_file1,
															  token_containing_PrimaryKEY,
															  tokens_integer_interested_CSV,
															  Flag_to_calc_MEAN_n_StdDEV,
															  arr_all_header_CSV,
															  arr_high_energy_device_CSV,
															  Flag_method_type_for_DATETIME_matching_on_seeding,
															  map_COMBINATION_dataID_N_datetime_higherEnergyDeviceINDEX_asKEY,
															  writer_dataID_date_mean_SD,
															  map_Date_PrimaryKeyToken_Mean,
															  map_Date_PrimaryKeyToken_StandardDeviation,
															  map_personaID_DataIDasKEY,
															  map_HousingType_DataIDasKEY
															 );
			}
			else{ // INPUT is ONLY a FOLDER
				System.out.println("#### Running list of files to run...");
				 String [] list_of_Files=(new File(baseFolder)).list();
				 int max_files=list_of_Files.length;
				 int c=0;
				 //
				 while(c<max_files){
					 	String curr_inFile=baseFolder+list_of_Files[c];
					 	if(curr_inFile.indexOf("Store")>=0 || (new File(curr_inFile).isDirectory()==true)  ){
					 		c++;continue;
					 	}
					 	// process CSV only
					 	if(curr_inFile.toLowerCase().indexOf(".csv")==-1){c++;continue;}
					 	System.out.println("--------------processing..."+curr_inFile +" current counter:"+ (c+1) +" "+max_files);
						// convert_token_with_numbers_to_AggregateNumbers
					 	p19_p21_convert_token_with_numbers_to_AggregateNumbers(
																	  baseFolder,
																	  curr_inFile, //curr file
																	  delimiter_for_file1,
																	  token_containing_PrimaryKEY,
																	  tokens_integer_interested_CSV,
																	  Flag_to_calc_MEAN_n_StdDEV,
																	  arr_all_header_CSV,
																	  arr_high_energy_device_CSV,
																	  Flag_method_type_for_DATETIME_matching_on_seeding,
																	  map_COMBINATION_dataID_N_datetime_higherEnergyDeviceINDEX_asKEY,
																	  writer_dataID_date_mean_SD,
																	  map_Date_PrimaryKeyToken_Mean,
																	  map_Date_PrimaryKeyToken_StandardDeviation,
																	  map_personaID_DataIDasKEY,
																	  map_HousingType_DataIDasKEY
																	 );
					   c++;
				 } // while 
				
			}
			System.out.println("Consider deleting file "+output_dataID_date_mean_SD + " to freshly calc mean and SD...");
		} //if(run_Flag==2){
		
		is_inputFolder_or_File=IsFile_a_Directory.isFile_a_Directory(input_Folder_or_file1);
		//clean the last column..
		if(run_Flag==3){ ///<---------------------------
			//INPUT is ONLY a file
			if(is_inputFolder_or_File==false){
				System.out.println(" single file to clean");
				// clean the last column
				clean_last_column_from_categoricalFiles(
														baseFolder,
														input_Folder_or_file1,
														delimiter_for_file1
														);
			}
			else{
				System.out.println("#### CLEANING - Running list of files to run...");
				 String [] list_of_Files=(new File(baseFolder)).list();
				 int max_files=list_of_Files.length;
				 int c=0;
				 //
				 while(c<max_files){
					 	String curr_inFile=baseFolder+list_of_Files[c];
					 	if(curr_inFile.indexOf("Store")>=0 || (new File(curr_inFile).isDirectory()==true)  ){
					 		c++;continue;
					 	}
				  
					// process CSV only
				 	if(curr_inFile.toLowerCase().indexOf(".csv")==-1){c++;continue;}
				 	System.out.println(" processing :" +curr_inFile);
					// clean the last column
					clean_last_column_from_categoricalFiles(
															baseFolder,
															curr_inFile,
															delimiter_for_file1
															);
					c++;
				 }
			}
		}
		
		TreeMap<Integer, TreeMap<String, String>> map_dataID_DATETIMEasKEY_Flag4=new TreeMap<Integer, TreeMap<String,String>>();
		// INJECTING STRUCTURAL ANOMALIES
		//clean the last column..
		if(run_Flag==4){ ///<---------------------------
			System.out.println(" completed Flag == 4 ; loaded 0.2");
			//read for DATE CSV for each dataID of structural 
			TreeMap<Integer,String> map_inFile_seq_DataID_seeded_STUCTURAL_Flag4=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																		 inFile_dataIDs_to_be_seeded_STUCTURAL, 
																	 	 -1, //startline, 
																		 -1, //endline,
																		 "f1 ", //debug_label
																		 false //isPrintSOP
																		 );
			// read for list of dataID of structural
			TreeMap<Integer,String> map_inFile_seq_DATATIMEcsv_Flag4=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																		 inFile_dataIDs_to_be_seeded_STUCTURAL_DATEcsv, 
																	 	 -1, //startline, 
																		 -1, //endline,
																		 "f1 ", //debug_label
																		 false //isPrintSOP
																		 );
			System.out.println(" completed Flag == 4 ; loaded 0.1 : map_inFile_seq_DATATIMEcsv.sz:"+map_inFile_seq_DATATIMEcsv_Flag4.size()
														+" map_inFile_seq_DataID.sz:"+map_inFile_seq_DataID_seeded_STUCTURAL_Flag4.size());
			

			////////////////////// p21_get_set_of_DocIDs_for_eachPersona			
			map_personaID_DataIDasKEY= p21_get_set_of_DocIDs_for_eachPersona(
																			 map_persona_lineRange_forRangeOFdataIDs_STRUCTURALanomaly,
																			 inFile_dataIDs_to_be_seeded_STUCTURAL
																			);
			//
			for(int seq:map_inFile_seq_DataID_seeded_STUCTURAL_Flag4.keySet()){
				
				int curr_dataID=Integer.valueOf(map_inFile_seq_DataID_seeded_STUCTURAL_Flag4.get(seq));
				String []  arr_curr_DATETIME=map_inFile_seq_DATATIMEcsv_Flag4.get(seq).split(",");
				System.out.println("1..2: seq:"+seq +" "+map_inFile_seq_DataID_seeded_STUCTURAL_Flag4.get(seq)
										+"<---->"+ arr_curr_DATETIME.length+" curr_dataID:"+curr_dataID);
				int c3=0;
				//
				while(c3<arr_curr_DATETIME.length){
					String currDateTIME=arr_curr_DATETIME[c3];
					if(!map_dataID_DATETIMEasKEY_Flag4.containsKey(curr_dataID)){
						TreeMap<String, String> temp=new TreeMap<String, String>();
						temp.put(currDateTIME, "");
						map_dataID_DATETIMEasKEY_Flag4.put(curr_dataID, temp);
					}
					else{
						TreeMap<String, String> temp=map_dataID_DATETIMEasKEY_Flag4.get(curr_dataID);
						temp.put(currDateTIME, "");
						map_dataID_DATETIMEasKEY_Flag4.put(curr_dataID, temp);
					}
					c3++;
				}
			} //for(int seq:map_inFile_seq_DataID.keySet()){
			System.out.println(" completed Flag==4 ; loaded 1 : ");			
			// read and get as map (person,index of device as key) <----------
			TreeMap<Integer, TreeMap<Integer, Integer>> map_personaID_IndexOFdeviceasKEY_Flag4
																=return_map(map_persona_setOFdevices, map_headerTokenOFall_SEQ);
			
			System.out.println(" completed Flag==4 ; loaded 2");
			// INPUT is ONLY a file
			if(is_inputFolder_or_File==false){
				System.out.println(" single file to clean...");
				// clean the last column
				inject_STRUCTURALanomaly_categoricalFiles(
														baseFolder,
														input_Folder_or_file1,
														delimiter_for_file1,
														token_containing_PrimaryKEY,
														tokens_integer_interested_CSV,
														arr_all_header_CSV,
														inFile_dataIDs_to_be_seeded_STUCTURAL_DATEcsv,
														map_SEQ_headerTokenOFall,
														map_headerTokenOFall_SEQ,
														map_persona_setOFdevices,
														map_personaID_DataIDasKEY,
														map_personaID_IndexOFdeviceasKEY_Flag4,
														map_dataID_DATETIMEasKEY_Flag4,
														output_File_dataID_N_dateTime_N_device_N_OLDvalue_N_NEWvalue_4structural,
														false // isAppend_output_File_dataID_N_dateTime_N_device_N_OLDvalue_N_NEWvalue //
														);
			}
			else{
				System.out.println("#### CLEANING - Running list of files to run...");
				 String [] list_of_Files=(new File(baseFolder)).list();
				 int max_files=list_of_Files.length;
				 int c=0;
				 //
				 while(c<max_files){
					 	String curr_inFile=baseFolder+list_of_Files[c];
					 	if(curr_inFile.indexOf("Store")>=0 || (new File(curr_inFile).isDirectory()==true)  ){
					 		c++;continue;
					 	}
				  
					// process CSV only
				 	if(curr_inFile.toLowerCase().indexOf(".csv")==-1){c++;continue;}
				 	System.out.println(" processing :" +curr_inFile);
					// clean the last column
				 	inject_STRUCTURALanomaly_categoricalFiles(
															baseFolder,
															curr_inFile,
															delimiter_for_file1,
															token_containing_PrimaryKEY,
															tokens_integer_interested_CSV,
															arr_all_header_CSV,
															inFile_dataIDs_to_be_seeded_STUCTURAL_DATEcsv,
															map_SEQ_headerTokenOFall,
															map_headerTokenOFall_SEQ,
															map_persona_setOFdevices,
															map_personaID_DataIDasKEY,
															map_personaID_IndexOFdeviceasKEY_Flag4,
															map_dataID_DATETIMEasKEY_Flag4,
															output_File_dataID_N_dateTime_N_device_N_OLDvalue_N_NEWvalue_4structural,
															true // isAppend_output_File_dataID_N_dateTime_N_device_N_OLDvalue_N_NEWvalue //
															);
					c++;
				 } // while(c<max_files){
			} // if(is_inputFolder_or_File==false){
			
			System.out.println(" completed Flag ==4");
		}
		// 
		System.out.println("Time Taken (FINAL ENDED):"
								+ NANOSECONDS.toSeconds(System.nanoTime() - t0)
								+ " seconds; "
								+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
								+ " minutes");
		
		}
		catch(Exception e){
			
		}
	}

	
}
