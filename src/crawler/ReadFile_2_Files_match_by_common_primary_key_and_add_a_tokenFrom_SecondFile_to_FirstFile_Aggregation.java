package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.TreeMap;
import crawler.Get_Aggregate_average_R_sum_from_Map;
 
//readFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom2File_to_1File 
public class ReadFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom_SecondFile_to_FirstFile_Aggregation {
		
		// read File
		public static void readFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom_SecondFile_to_FirstFile_Aggregation(
																					String  first_File,
																					String  delimiter_to_be_added_to_first_File,
																					// token_interested_in_first_File=-1 means it will use line of firstFile instead a token from first File
																					String 	token_in_first_file_having_primary_Key_CSV, //primary key created in same given order
																					String 	second_File,
																					String 	token_in_second_file_having_primary_Key_CSV,//primary key created in same given order
																					int     line_range_N_4_second_File_2_process_START,
																					int     line_range_N_4_second_File_2_process_END,
																					String	token_interested_in_second_File_for_aggregation_N_to_be_appended_to_first,
																					String   out_File_exists,
																					boolean  is_Append_outFile_exists,
																					String   out_File_not_exists,
																					boolean  is_Append_outFile_not_exists,
																					boolean  isSOPprint
																					){
			TreeMap<String, String> mapFirstFile_pKey_line=new TreeMap<String, String>();
			TreeMap<String, TreeMap<Integer, Double> > mapSecondFile_pKey_InterestedToken=new TreeMap<String, TreeMap<Integer, Double>>();
			Runtime rs =  Runtime.getRuntime(); String line="";
		
			try {
				FileWriter writer_ExistsFile=new FileWriter(new File(out_File_exists)
																	, is_Append_outFile_exists);
				FileWriter writer_Not_ExistsFile=new FileWriter(new File(out_File_not_exists)
																	, is_Append_outFile_not_exists);
			

				BufferedReader reader = new BufferedReader(new FileReader(first_File));
				System.out.println("first file processing..");
				// FIRST FILE - read each line of given file
				while ((line = reader.readLine()) != null) {
					String f1_primarykey="";
					//replace , with !!! for delimiter
					if(line.indexOf("!!!")==-1) line=line.replace(",", "!!!");
					
					String []s =line.replace("!!!!!!","!!!"+"#"+"!!!").split("!!!");
					String [] f1_token_atom=token_in_first_file_having_primary_Key_CSV.split(",");
					int cnt=0;
					//f1 - create composite primary key
					while(cnt<f1_token_atom.length){
						
						if(f1_primarykey.length()==0)
							f1_primarykey=s[ Integer.valueOf( f1_token_atom[cnt]) -1];
						else 
							f1_primarykey=f1_primarykey+"!!!"+ s[ Integer.valueOf( f1_token_atom[cnt]) -1];
						cnt++;
					}
					
					// CLEANING URL (if primary key given is an url)
					if(f1_primarykey.indexOf("url=")>=0){
						f1_primarykey=Clean_embeddedURLs.clean_get_embeddedURL(f1_primarykey);
					}
					System.out.println("f1_primarykey:"+f1_primarykey);
					mapFirstFile_pKey_line.put(f1_primarykey, line);
				}
				
				System.out.println("second file processing..");
				BufferedReader reader2 = new BufferedReader(new FileReader(second_File));
				int lineNumber2=0;
				// SECOND FILE
				while ((line = reader2.readLine()) != null) {
					lineNumber2++;
					//range of lines
					if( lineNumber2 >=line_range_N_4_second_File_2_process_START  && 
					    lineNumber2 <=line_range_N_4_second_File_2_process_END &&
					    line_range_N_4_second_File_2_process_START>0 &&
					    line_range_N_4_second_File_2_process_END>0
					  ){  System.out.println("pass"); }
					else if(lineNumber2 >line_range_N_4_second_File_2_process_END &&
							line_range_N_4_second_File_2_process_START>0 &&
						    line_range_N_4_second_File_2_process_END>0
							) { System.out.println("break");
								break;}
					else if(line_range_N_4_second_File_2_process_START>0 &&
						    line_range_N_4_second_File_2_process_END>0) {
						 System.out.println("continue...");
						 continue;
						}
						 
					
					//replace , with !!! for delimiter
					if(line.indexOf("!!!")==-1) line=line.replace(",", "!!!");
					
					String []s =line.replace("!!!!!!","!!!"+"#"+"!!!").split("!!!");
					String f2_primarykey="";
					String [] f2_token_atom=token_in_second_file_having_primary_Key_CSV.split(",");
					int cnt=0;
					//f1 - create composite primary key
					while(cnt<f2_token_atom.length){
						
						if(f2_primarykey.length()==0)
							f2_primarykey=s[ Integer.valueOf( f2_token_atom[cnt]) -1];
						else 
							f2_primarykey=f2_primarykey+"!!!"+ s[ Integer.valueOf( f2_token_atom[cnt]) -1];
						cnt++;
						
					}
					//System.out.println("f2_primarykey:"+f2_primarykey);
					// CLEANING URL (if primary key given is an url)
					if(f2_primarykey.indexOf("url=")>=0){
						f2_primarykey=Clean_embeddedURLs.clean_get_embeddedURL(f2_primarykey);
					}
					 
					
					 //interested (a) token for aggregation from second file..
					double temp_Token= Double.valueOf( s[ Integer.valueOf(token_interested_in_second_File_for_aggregation_N_to_be_appended_to_first) - 1]);
					 
					
					// Interested Token in second file that has to be appended to first file
					if( !mapSecondFile_pKey_InterestedToken.containsKey(f2_primarykey) ){
						TreeMap<Integer,Double> tempMap=new TreeMap<Integer, Double>();
						tempMap.put(1, temp_Token);
						mapSecondFile_pKey_InterestedToken.put(f2_primarykey,  tempMap );
					}
					else{
						TreeMap<Integer,Double> tempMap=mapSecondFile_pKey_InterestedToken.get(f2_primarykey);
						int cnt22=tempMap.size()+1;
						tempMap.put(cnt22, temp_Token);
						mapSecondFile_pKey_InterestedToken.put(f2_primarykey,  tempMap );
					}
					
					
					if(isSOPprint)
						System.out.println("size:"+mapSecondFile_pKey_InterestedToken.size());
				}
				 
				 
				String s_orig="";
				int found=0, not_found=0; int lineNo=0;
				System.out.println("aggregating and writing file processing..");
				//
				for(String s:mapFirstFile_pKey_line.keySet()){
					s_orig="";
					lineNo++;
					//System.out.println("writings ..lineNo:"+lineNo);
					
					//if primary key exists
					if( mapSecondFile_pKey_InterestedToken.containsKey(s) ){
						//String []s2=s.split("!!!");//obselete
						
						//calculate the aggregate..
						//get the map of current, to get all data points..
						TreeMap<Integer,Double> tempMap=mapSecondFile_pKey_InterestedToken.get(s);
						//calling a method with input map that does the aggregation
						double avg_R_sum_aggregate= Get_Aggregate_average_R_sum_from_Map.get_ID_Aggregate_average_R_sum_from_Map(
																					  tempMap
																					, 0 //flag
																					); 
						
						s_orig=mapFirstFile_pKey_line.get(s)+"!!!"+avg_R_sum_aggregate;						
						
						//.replace("-", "#!#").replace("/", "#!#") <datetime normalization
						writer_ExistsFile.append("\n"+s_orig);
						writer_ExistsFile.flush();
						found++;
					//	System.out.println("picked token "+s);
					}
					else{
						s_orig=mapFirstFile_pKey_line.get(s);
						writer_Not_ExistsFile.append("\n"+s_orig);
						writer_Not_ExistsFile.flush();
						not_found++;
					}
					System.out.println("lineNo:"+lineNo);
				}
				
				System.out.println("mapFirstFile_pKey_line.size:"+mapFirstFile_pKey_line.size() );
				System.out.println("mapSecondFile_pKey_InterestedToken.size:"+mapSecondFile_pKey_InterestedToken.size());
				System.out.println("Exists Count:"+found);
				System.out.println("NOT Exists Count:"+not_found);
				System.out.println("First line:"+first_File);
				System.out.println("Second line:"+second_File);
				writer_ExistsFile.close();
				writer_Not_ExistsFile.close();
				rs.gc();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
		}
		
		// main
		public static void main(String[] args) {
			
			String baseFolder="/Users/lenin/Downloads/#problems/p8/work-Full Article/samples/sample.on.keywords_ds1/";
			//
			// ***** merging vertical 
			baseFolder="/Users/lenin/Downloads/#problems/p19/";
			//redteam.txt - "time,user@domain,source computer,destination computer"
			String first_File="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_isRSS_true.SET.1.copy.txt";
			//firstFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/file.us.50.thinktank.txt";
			first_File=baseFolder+"redteam.txt";

			//flows.txt - "time,duration,source computer,source port,destination computer,destination port,protocol,packet count,byte count"
			String second_File="/Users/lenin/Downloads/MyThunderbirdFeeds-Blogs & News Feeds.29.opml";

			//secondFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_isRSS_true.SET.1.copy.txt";
			second_File= baseFolder+"C17693_only_flow.txt";
			
			String outFile_Exists=baseFolder+"__exists2.txt";
			String outFile_Not_Exists=baseFolder+"__NOexists2.txt";
			String delimiter_to_be_added_to_first_File="";
			//primary key created in same given order
			String token_in_first_file_having_primary_Key_CSV="1,3,4";
			//primary key created in same given order
			String token_in_second_file_having_primary_Key_CSV="1,3,5";
			int line_range_N_4_second_File_2_process_START=-1;
			int line_range_N_4_second_File_2_process_END=-1;
			String token_interested_in_second_File_for_aggregation_N_to_be_appended_to_first="9";
			
			//readFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom_SecondFile_to_FirstFile
			readFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom_SecondFile_to_FirstFile_Aggregation(
																								first_File,
																								delimiter_to_be_added_to_first_File,
																								token_in_first_file_having_primary_Key_CSV,//-1
																								second_File,
																								token_in_second_file_having_primary_Key_CSV,
																								line_range_N_4_second_File_2_process_START,
																								line_range_N_4_second_File_2_process_END,
																								token_interested_in_second_File_for_aggregation_N_to_be_appended_to_first,
																								outFile_Exists,
																								false, //is_Append_outFile_exists
																								outFile_Not_Exists,
																								false, //is_Append_outFile_not_exists
																								false //isSOPprint
																								);
		
		}

}
