package crawler;
/*
 
 * 
 * */ 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.TreeMap;
	//find a set of pattern (use*** getWikiPageLinks.getLinks() )
	public class Find_distinct_domain_name_from_Given_FILE {
 
		public static String find_distinct_domain_name_from_Given_FILE(
													String  baseFolder,
													String  inFile,
													int     index_having_url_inFile, //can be -1
													String  outFile,
													int		Flag_Approach,
													boolean isDebug
													){
			 
			TreeMap<String,String> map_unique_domainName_as_KEY=new TreeMap<String, String>();
//			TreeMap<Integer,String> map_seq_domainName_as_KEY=new TreeMap<Integer, String>();
			String out="";
			try {
				FileWriter writer=new FileWriter(new File(outFile));
				
				
				if(Flag_Approach==1){
				
				 // APPROACH 1 - BIGGER FILES DONT WORK ...
				TreeMap<Integer,String> map_file1=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
							.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										inFile,
																		 							  -1, //startline, 
																		 							  -1, //endline,
																		 							  " ", //debug_label
																		 							  false //isPrintSOP
																								 	  );
				
				String url="";
				for(int seq:map_file1.keySet()){
					String eachLine=map_file1.get(seq);
					/// 
					if(index_having_url_inFile>0){
						String [] arr_eachLine=eachLine.split("!!!");
						///
						if( arr_eachLine.length>=index_having_url_inFile ){
							url=arr_eachLine[index_having_url_inFile-1];
						}
						
					}
					else{
						url= eachLine;
					}
					// CLEANING URL
					if(url.indexOf("url=")>=0){
						url=Clean_embeddedURLs.clean_get_embeddedURL(url);
					}
					
					//DOMAIN NAME
					String currDomain=Find_domain_name_from_Given_URL.find_domain_name_from_Given_URL(url, false);
					//blank domains should not be added
					if(currDomain.length()>1){
						//
						map_unique_domainName_as_KEY.put(currDomain, "");
					}
				}
				
				} // Flag_Approach==1
				
				 // APPROACH 2
				String line2="";
				BufferedReader reader = new BufferedReader(new FileReader(inFile));
				if(Flag_Approach==2){
					String eachLine="";String url="";
					// read each line of given file
					while ((eachLine = reader.readLine()) != null) {
					
						/// 
						if(index_having_url_inFile>0){
							String [] arr_eachLine=eachLine.split("!!!");
							///
							if( arr_eachLine.length>=index_having_url_inFile ){
								url=arr_eachLine[index_having_url_inFile-1];
							}
							
						}
						else{
							url= eachLine;
						}
						// CLEANING URL
						if(url.indexOf("url=")>=0){
							url=Clean_embeddedURLs.clean_get_embeddedURL(url);
						}
						
						//DOMAIN NAME
						String currDomain=Find_domain_name_from_Given_URL.find_domain_name_from_Given_URL(url, false);
						//
						map_unique_domainName_as_KEY.put(currDomain, "");
						
					}
				} //if(Flag_Approach==2){
				
				
				//WRITE
				for(String url2:map_unique_domainName_as_KEY.keySet()){
					writer.append(url2+"\n");
					writer.flush();
				}
				
				System.out.println("distinct domain:"+map_unique_domainName_as_KEY.size()+" "+map_unique_domainName_as_KEY);
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			 
			System.out.println(out);
			return out;
		}
		// main()
		public static void main(String[] args) {
			
			//
			if (args.length >=10) {
				System.out.println("Incorrect input provided..!! Please provide proper folder name");
				System.out.println("Correct Usage is: java MergeFiles <input folder name>");
				return;
			}
			
		  try{
			    int index_having_url_inFile=-1; 
				String baseFolder="/Users/lenin/Downloads/#problems/p18/ds1/";
				
				baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Feeds-Newscopy/Feeds-Trad-17-Feb-2015-Set1-XML2CSV/tmp/";
				baseFolder="/Users/lenin/Dropbox/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds10/crawling/";
				baseFolder="/Users/lenin/Dropbox/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds10/findingUniqueDomainINeachNetwork/";
				baseFolder="/Users/lenin/Dropbox/#problems/p18/ds2_modified/";
				baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/tmp/";
				
				String inFile=baseFolder+"all_all_URL_cleaned.txt";
				String outFile=baseFolder+"all_all_URL_cleaned_DISTINCT_domainNAMES.txt";
				   
				inFile	= baseFolder+"poli.txt";
				inFile  = baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER_addKEYWORDS.txt";
				inFile  = baseFolder+"O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged_onlyAfricanAmerican.txt";
				
				
				outFile = 	inFile+"_DOMAINNAMES.txt";
				index_having_url_inFile=5;
				
				// STEP 1: find domain name (COMMENT/UNCOMMENT)
//				find_distinct_domain_name_from_Given_FILE.
//				find_distinct_domain_name_from_Given_FILE(  baseFolder,
//															inFile,
//															index_having_url_inFile,
//															outFile,
//															2, // Flag_Approach
//															true
//														 );
				 
				// STEP 2 : Compare the output of Step 2 with GLOBAL DOMAIN NAME
				String first_file=outFile;
					   first_file="/Users/lenin/Downloads/Google Drive/##***Now-Working/#Problems/Problem-25-NewsFeed-and-Twitter/Data.p25/outputFolderForUNIQUE_url/crawled_GLOBAL.txt_uniqueDOMAIN_backup.txt";				
				
				//below is the global domain name(DONT CHANGE) ..backup saved in google drive.
				String second_file  ="/Users/lenin/Dropbox/#problems/p18/ds1/global_domainNAMES_patternFORNEWSBODYSTART.txt";
				String outFile_Exists=first_file+"_URLs_exists.txt";
				String outFile_Not_Exists=first_file+"_URLs_notExists.txt";
				String outFile_File1_addedComment=first_file+"_addedComment.txt"; 
				
				String delimiter_to_be_added_to_first_File="";
				int token_interested_in_first_File=1;
					//	(COMMENT/UNCOMMENT)
					//CALLING - readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String (COMMENT/UNCOMMENT)
					ReadFile_2_EachLineORtoken_from_First_File_exists_in_Second_File_as_String.
					readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String
																						(
																						first_file,
																						delimiter_to_be_added_to_first_File,
																						token_interested_in_first_File,//-1
																						second_file,
																						"!!!", // delimiter_for_splitting
																						outFile_Exists,
																						false, //is_Append_outFile_exists
																						outFile_Not_Exists,
																						false, //is_Append_outFile_not_exists
																						outFile_File1_addedComment,
																						false //if(isSOPprint)
																						);
				 
			
				
		  }
		  catch(Exception e){
			  System.out.println("An exception occurred while calling Write method ..!!");	
			  e.printStackTrace();
		  }
					
		}
			 
}
