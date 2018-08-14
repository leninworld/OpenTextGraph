package crawler;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

// first find can be a file with list of url (one url in each line)
// second file could be opml file. 
public class ReadFile_2_EachLineORtoken_from_First_File_exists_in_Second_File_as_String {
		// read File
		public static void readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String(
																					String first_File,
																					String delimiter_to_be_added_to_first_File,
													// token_interested_in_first_File=-1 means it will use line of firstFile instead a token from first File
																					int	   token_interested_in_first_File,
																					String second_File,
																					String delimiter_for_splitting,
																					String out_File_exists,
																					boolean is_Append_outFile_exists,
																					String out_File_not_exists,
																					boolean is_Append_outFile_not_exists,
																					String  outFile_File1_addedComment,
																					boolean isSOPprint
																					){
			TreeMap<String, String> mapFirstFileLines=new TreeMap<String, String>();
			String secondFile_CompleteString="";
			int existsCount=0; int notExistsCount=0;
			Runtime rs =  Runtime.getRuntime();
			int cnt=0;
			try {
				FileWriter writer_ExistsFile=new FileWriter(new File(out_File_exists)
																	, is_Append_outFile_exists);
				FileWriter writer_Not_ExistsFile=new FileWriter(new File(out_File_not_exists)
																	, is_Append_outFile_not_exists);
				
				FileWriter writer_File1_addedComment=new FileWriter(new File(outFile_File1_addedComment));
				
				
				System.out.println("Loading first file");
				mapFirstFileLines=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
						readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
																	  first_File
																	, -1, -1
																	, "" //outfile
																	, false //is_Append_outFile
																	, false //is_Write_To_OutputFile
																	, "" //debug_label
																	, -1 //token_to_be_used_as_primarykey
																	,false //boolean isSOPprint
																	);
				
				
				int firstFile_count=mapFirstFileLines.size();
				
				System.out.println("Loading second file");
				secondFile_CompleteString=
										 ReadFile_CompleteFile_to_a_Single_String
										.readFile_CompleteFile_to_a_Single_String(second_File);
				String s_orig="";
				String secondFileName=new File(second_File).getName();
				//
				for(String s:mapFirstFileLines.keySet()){
					cnt++;
					if(isSOPprint)
						System.out.println("(match 2 files)line no :"+cnt);
					
					if(token_interested_in_first_File>0){
						String []s2=s.split(delimiter_for_splitting);
						s_orig=s;
						try{
							// pick token of interest 
							s=s2[token_interested_in_first_File-1];
						}
						catch(Exception e){
						}
						
					//	System.out.println("picked token "+s);
					}
					else{
						s_orig=s;
					}
					
					if(secondFile_CompleteString.indexOf( s+delimiter_to_be_added_to_first_File)>=0 ){
						existsCount++;
						if(isSOPprint)
							System.out.println(cnt +" (out of "+firstFile_count +") exists:"+s);
						writer_ExistsFile.append("\n"+s_orig);
						writer_ExistsFile.flush();
						//
						writer_File1_addedComment.append("\n"+s_orig+"!!!<<<<<< Exists in "+ 
																		secondFileName
																		+ ">>>>");
						writer_File1_addedComment.flush();
						
					}
					else{
						notExistsCount++;
						if(isSOPprint)
							System.out.println(cnt +" (out of "+ firstFile_count+") NOT exists:"+s);
						writer_Not_ExistsFile.append("\n"+s_orig);
						writer_Not_ExistsFile.flush();
						
						writer_File1_addedComment.append("\n"+s_orig);
						writer_File1_addedComment.flush();
					}
					
				}
				
				System.out.println("mapFirstFileLines.size:"+mapFirstFileLines.size());
				System.out.println("secondFile_CompleteString.len:"+secondFile_CompleteString.length());
				System.out.println("Exists Count:"+existsCount);
				System.out.println("NOT Exists Count:"+notExistsCount);
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
			
			String baseFolder="/Users/lenin/Downloads/#problems/p18/ds1/Labeling/";
				   baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/tmp/";
			//
			String firstFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_isRSS_true.SET.1.copy.txt";
			//firstFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/file.us.50.thinktank.txt";
			firstFile="/Users/lenin/Downloads/#problems/p8/Full Article/thehindu/attemp.2./merged_all.txt";
			firstFile=baseFolder+"minorit_S_L.txt";
			firstFile=baseFolder+"O___ENGLISH__LEN_50_FILTER__PASSED_FILTER__url_tagline_untagline.3all_p18__removeIrrelevaLABEL_NLPtagged_onlyAfricanAmerican__added_perso_origz_loc_verb2__added##GroundTruth.txt";
			
			String secondFile="/Users/lenin/Downloads/MyThunderbirdFeeds-Blogs & News Feeds.29.opml";
			//secondFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_isRSS_true.SET.1.copy.txt";
			secondFile=baseFolder+"mentaly_ill_all_NODUP.txt";
			secondFile="/Users/lenin/Dropbox/Maitrayi/p18 -/Labeling/1500labeled_marked_DOUBTS/O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_onlyAfricanAmerican_removeIrrelevaLABEL.txt";
					
			
			String outFile_Exists=baseFolder+"URLs_exists.txt";
			String outFile_Not_Exists=baseFolder+"URLs_notExists.txt";
			String outFile_File1_addedComment=firstFile+"_addedComment.txt";
			
			String delimiter_to_be_added_to_first_File="";
			int token_interested_in_first_File=5;
			
			//readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String
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
		
		}

}
