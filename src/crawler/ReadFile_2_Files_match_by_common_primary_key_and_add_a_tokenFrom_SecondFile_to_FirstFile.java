package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.TreeMap;
 
//readFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom2File_to_1File 
public class ReadFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom_SecondFile_to_FirstFile {
		
		// read File
		public static void readFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom_SecondFile_to_FirstFile(
																					String  first_File,
																					String  delimiter_to_be_added_to_first_File,
																					// token_interested_in_first_File=-1 means it will use line of firstFile instead a token from first File
																					String 	token_in_first_file_having_primary_Key_CSV, //primary key created in same given order
																					String 	second_File,
																					String 	token_in_second_file_having_primary_Key_CSV,//primary key created in same given order
																					String	token_interested_in_second_File_to_be_appended_to_first_CSV, // -1 <--if NO interested token
																					String  token_in_first_file_WHERE_APPENDING_token_interested_in_second_File_to_be_appended_to_first_CSV,//

																					String   out_File_exists,
																					boolean  is_Append_outFile_exists,
																					String   out_File_not_exists,
																					boolean  is_Append_outFile_not_exists,
																					boolean  is_add_dummy_tokens_for_NOT_exists
																					){
			 
			 
			
			TreeMap<String, String> mapFirstFile_pKey_line=new TreeMap<String, String>();
			TreeMap<String, String> mapSecondFile_pKey_InterestedToken=new TreeMap<String, String>();
			Runtime rs =  Runtime.getRuntime(); String line="";
			int token_in_first_file_WHERE_APPENDING_token_interested_in_second_File_to_be_appended_to_first_CSV_INT=
									Integer.valueOf(token_in_first_file_WHERE_APPENDING_token_interested_in_second_File_to_be_appended_to_first_CSV);
			
			//create dummy tokens 
			String [] s10= token_interested_in_second_File_to_be_appended_to_first_CSV.split(",");
			int cnt10=0;String dummy_tokens_CSV_4_NOT_MATCHED="";
			//
			while(cnt10<s10.length){
				dummy_tokens_CSV_4_NOT_MATCHED=dummy_tokens_CSV_4_NOT_MATCHED+"!!!"+"dummy";
				cnt10++;
			}
			
			try {
				FileWriter writer_ExistsFile=new FileWriter(new File(out_File_exists)
																	, is_Append_outFile_exists);
				FileWriter writer_Not_ExistsFile=new FileWriter(new File(out_File_not_exists)
																	, is_Append_outFile_not_exists);
				
				FileWriter writer_both =new FileWriter(new File(out_File_exists+"_BOTH.txt")
														, is_Append_outFile_not_exists);
			

				BufferedReader reader = new BufferedReader(new FileReader(first_File));
				int first_lineNo=0;
				// FIRST FILE - read each line of given file
				while ((line = reader.readLine()) != null) {
					first_lineNo++;
					String f1_primarykey="";
					//replace , with !!! for delimiter
					if(line.indexOf("!!!")==-1) line=line.replace(",", "!!!");
					
					//cleaning dirty delimiter
					line=line.replace("!!!!!!","!!!"+"#"+"!!!").replace("!!!!!","!!!").replace("!!!!","!!!");
					
					if(line.length()<=2) continue; //empty line skip
					
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
					mapFirstFile_pKey_line.put(f1_primarykey, line);
					
					//
					if(first_lineNo <=4)
						System.out.println("f1_primarykey:"+f1_primarykey+" line:"+line);
					
				}
				int second_lineNo=0;
				BufferedReader reader2 = new BufferedReader(new FileReader(second_File));
				// SECOND FILE
				while ((line = reader2.readLine()) != null) {
					second_lineNo++;
					 
					//replace , with !!! for delimiter
					if(line.indexOf("!!!")==-1) line=line.replace(",", "!!!");
					//cleaning dirty delimiter
					line=line.replace("!!!!!!","!!!"+"#"+"!!!").replace("!!!!!","!!!").replace("!!!!","!!!");
					
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
					
					// CLEANING URL (if primary key given is an url)
					if(f2_primarykey.indexOf("url=")>=0){
						f2_primarykey=Clean_embeddedURLs.clean_get_embeddedURL(f2_primarykey);
					}
					
					//concatenated interested token from second file to be appended to first
					String [] f2_interested_token_=token_interested_in_second_File_to_be_appended_to_first_CSV.split(",");
					cnt=0; String conc_f2_interested_token="";
					
					//There is NO need for any token to be appended..
					if(token_interested_in_second_File_to_be_appended_to_first_CSV.equals("-1")){
						// Interested Token in second file that has to be appended to first file 
						mapSecondFile_pKey_InterestedToken.put(f2_primarykey, "" //conc_f2_interested_token 
																);
					}
					else{
						while (cnt<f2_interested_token_.length) {
							
							if(conc_f2_interested_token.length()==0)
								conc_f2_interested_token= s[ Integer.valueOf(f2_interested_token_[cnt]) - 1]; //interested token
							else
								conc_f2_interested_token=conc_f2_interested_token+"!!!"+s[ Integer.valueOf(f2_interested_token_[cnt]) - 1];
							
							cnt++;
						}
						
						// Interested Token in second file that has to be appended to first file 
						mapSecondFile_pKey_InterestedToken.put(f2_primarykey, conc_f2_interested_token );
					
					}
					
					//
					if(second_lineNo <=4)
						System.out.println("f2_primarykey:"+f2_primarykey+" line:"+line);
				}
				 
				// 
				String s_orig="";
				int found=0, not_found=0; int lineNo=0;
				//
				for(String s:mapFirstFile_pKey_line.keySet()){
					s_orig="";
					lineNo++;
					//if primary key exists
					if( mapSecondFile_pKey_InterestedToken.containsKey(s) ){
						//String []s2=s.split("!!!");//obselete

						// NOT inserting the interested token from second file to a specific token position in first file 
						if(token_in_first_file_WHERE_APPENDING_token_interested_in_second_File_to_be_appended_to_first_CSV.equals("-1")){
							
							if(token_interested_in_second_File_to_be_appended_to_first_CSV.equals("-1"))
								s_orig=mapFirstFile_pKey_line.get(s);
							else
								s_orig=mapFirstFile_pKey_line.get(s)+"!!!"+mapSecondFile_pKey_InterestedToken.get(s).replace("-", "#!#").replace("/", "#!#") ;
							
							//.replace("-", "#!#").replace("/", "#!#") <datetime normalization
						
							writer_ExistsFile.append(s_orig+"\n");
							writer_ExistsFile.flush();
							//BOTH
							writer_both.append(s_orig+"\n");
							writer_both.flush();
						}
						else{
							String [] arr_first=mapFirstFile_pKey_line.get(s).split("!!!");
							String [] arr_second=new String[1];
							arr_second[0]=mapSecondFile_pKey_InterestedToken.get(s);
							
							int arr_first_len=arr_first.length;
							int arr_second_len=arr_second.length; 
							int c1=0; int c2=0;
							String concLine_secondFile="";String concLine_firstFile="";
							//second file
							while(c2<arr_second_len){
								if(concLine_secondFile.length()==0)
									concLine_secondFile=arr_second[c2];
								else
									concLine_secondFile=concLine_secondFile+" "+arr_second[c2];
								c2++;
							}
							//first file
							while(c1<arr_first_len){
								//
								if(concLine_firstFile.length()==0){
									
									if(token_in_first_file_WHERE_APPENDING_token_interested_in_second_File_to_be_appended_to_first_CSV_INT-1==(c1))
										concLine_firstFile=arr_first[c1]+" <--> "+concLine_secondFile;
									else
										concLine_firstFile=arr_first[c1];
								}
								else{
								
									
									if(token_in_first_file_WHERE_APPENDING_token_interested_in_second_File_to_be_appended_to_first_CSV_INT-1==(c1)){
//										System.out.println("exists concLine_secondFile:"+concLine_secondFile+" arr_first[c1]:"+arr_first[c1]
//												+" [c1]:" +c1
//												+" first_file_line->"+mapFirstFile_pKey_line.get(s)+" arr_first_len:"+arr_first_len);
										
										concLine_firstFile=concLine_firstFile+"!!!"+arr_first[c1]+" <-->"+concLine_secondFile;
									}
									else
										concLine_firstFile=concLine_firstFile+"!!!"+arr_first[c1];
								}
								 
								c1++;
							}
						
							writer_ExistsFile.append(concLine_firstFile+"\n");
							writer_ExistsFile.flush();
							//BOTH
							writer_both.append(concLine_firstFile+"\n");
							writer_both.flush();
							
						}
						
						found++;
					//	System.out.println("picked token "+s);
					}
					else{
						// NOT inserting the interested token from second file to a specific token position in first file
						if(token_in_first_file_WHERE_APPENDING_token_interested_in_second_File_to_be_appended_to_first_CSV.equals("-1")){
							s_orig=mapFirstFile_pKey_line.get(s);
							//  add dummy tokens 
							if(is_add_dummy_tokens_for_NOT_exists){
								
								if(token_interested_in_second_File_to_be_appended_to_first_CSV.equals("-1")){
									writer_Not_ExistsFile.append(s_orig+"\n");
									writer_Not_ExistsFile.flush();
									// BOTH
									writer_both.append(s_orig+"\n");
									writer_both.flush();
								}
								else{
								
									writer_Not_ExistsFile.append(s_orig+dummy_tokens_CSV_4_NOT_MATCHED+"\n");
									writer_Not_ExistsFile.flush();
									// BOTH
									writer_both.append(s_orig+dummy_tokens_CSV_4_NOT_MATCHED+"\n");
									writer_both.flush();
								}
							}
							else {
								if(token_interested_in_second_File_to_be_appended_to_first_CSV.equals("-1")){
									writer_Not_ExistsFile.append(s_orig+"\n");
									writer_Not_ExistsFile.flush();
									// BOTH
									writer_both.append(s_orig+"\n");
									writer_both.flush();
								}
								else{
									writer_Not_ExistsFile.append(s_orig+"\n");
									writer_Not_ExistsFile.flush();
									// BOTH
									writer_both.append(s_orig+"\n");
									writer_both.flush();
								}
							}
						}
						else{
							
							String [] arr_first=mapFirstFile_pKey_line.get(s).split("!!!");
							String [] arr_second=new String[1];
							arr_second[0]=mapSecondFile_pKey_InterestedToken.get(s);
							
							int arr_first_len=arr_first.length;
							int arr_second_len=arr_second.length; 
							int c1=0; int c2=0;
							String concLine_secondFile="";String concLine_firstFile="";
							//second file
							while(c2<arr_second_len){
								if(concLine_secondFile.length()==0)
									concLine_secondFile=arr_second[c2];
								else
									concLine_secondFile=concLine_secondFile+" "+arr_second[c2];
								c2++;
							}
							//first file
							while(c1<arr_first_len){
								//
								if(concLine_firstFile.length()==0){
									
									if(token_in_first_file_WHERE_APPENDING_token_interested_in_second_File_to_be_appended_to_first_CSV_INT-1==(c1))
										concLine_firstFile=arr_first[c1]+" <--> "+concLine_secondFile;
									else
										concLine_firstFile=arr_first[c1];
								}
								else{
									if(token_in_first_file_WHERE_APPENDING_token_interested_in_second_File_to_be_appended_to_first_CSV_INT-1==(c1)){
										 
//											System.out.println("concLine_secondFile:"+concLine_secondFile+" arr_first[c1]:"+arr_first[c1]);
										
										concLine_firstFile=concLine_firstFile+"!!!"+arr_first[c1]+" <--> "+concLine_secondFile;
										
									}
									else
										concLine_firstFile=concLine_firstFile+"!!!"+arr_first[c1];
								}
								 
								c1++;
							}
//							System.out.println("concLine_firstFile:"+concLine_firstFile);
							writer_Not_ExistsFile.append(concLine_firstFile+"\n");
							writer_Not_ExistsFile.flush();
							// BOTH
							writer_both.append(concLine_firstFile+"\n");
							writer_both.flush();
						}
						
						not_found++;
					}
//					System.out.println("lineNo:"+lineNo);
				}
				
				System.out.println("mapFirstFile_pKey_line.size:"+mapFirstFile_pKey_line.size() );
				System.out.println("mapSecondFile_pKey_InterestedToken.size:"+mapSecondFile_pKey_InterestedToken.size());
				System.out.println("Exists Count:"+found);
				System.out.println("NOT Exists Count:"+not_found);
				System.out.println("Both EXIST AND not EXIST->"+out_File_exists+"_BOTH.txt");
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
			String first_File="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_isRSS_true.SET.1.copy.txt";
			//firstFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/file.us.50.thinktank.txt";
			first_File="/Users/lenin/Downloads/#problems/p8/Full Article/thehindu/attemp.2./merged_all.txt";
			first_File=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup.txt";
			
			String second_File="/Users/lenin/Downloads/MyThunderbirdFeeds-Blogs & News Feeds.29.opml";
			//secondFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_isRSS_true.SET.1.copy.txt";
			second_File=baseFolder+"merged_all.txt";
			second_File="/Users/lenin/Downloads/#problems/p8/work-Full Article/samples/URLnDateTime/all_dateTime_URL.txt";

			
			String outFile_Exists=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists.txt";
			String outFile_Not_Exists=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_NOexists.txt";
			String delimiter_to_be_added_to_first_File="";
			//primary key created in same given order
			String token_in_first_file_having_primary_Key_CSV="1";
			//primary key created in same given order
			String token_in_second_file_having_primary_Key_CSV="2";
			String token_interested_in_second_File_to_be_appended_to_first_CSV="1";
			String token_in_first_file_WHERE_APPENDING_token_interested_in_second_File_to_be_appended_to_first_CSV="";
			
			//readFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom_SecondFile_to_FirstFile
			readFile_2_Files_match_by_common_primary_key_and_add_a_tokenFrom_SecondFile_to_FirstFile(
																								first_File,
																								delimiter_to_be_added_to_first_File,
																								token_in_first_file_having_primary_Key_CSV,//-1
																								second_File,
																								token_in_second_file_having_primary_Key_CSV,
																								token_interested_in_second_File_to_be_appended_to_first_CSV,
																								token_in_first_file_WHERE_APPENDING_token_interested_in_second_File_to_be_appended_to_first_CSV,
																								outFile_Exists,
																								false, //is_Append_outFile_exists
																								outFile_Not_Exists,
																								false, //is_Append_outFile_not_exists
																								true //add_dummy_tokens_for_NOT_exists
																								);
		
		}

}
