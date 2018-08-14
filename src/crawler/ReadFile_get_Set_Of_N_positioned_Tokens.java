package crawler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

//
public class ReadFile_get_Set_Of_N_positioned_Tokens {
	//Read input file and for each line of input file, 
	//get only set of interested Tokens and write to output file.
	public static void readFile_get_Set_Of_N_positioned_Tokens(String 	inFile,
															   String 	outFile,
															   String 	delimiter_OF_inFile,
															   String 	interested_TokenNumbers_In_CSV,//starts with ONE
															   boolean 	is_remove_duplicate,
															   boolean 	isSOPprint
															   ){
		String line=""; 
		TreeMap<Integer, Integer> mapInterestedTokens=new TreeMap<Integer, Integer>();
		TreeMap<String, String> mapDuplicateCheck=new TreeMap();
		System.out.println("readFile_get_Set_Of_N_positioned_Tokens");
		try{
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			FileWriter writer = new FileWriter(outFile);
			FileWriter writer_DEBUG = new FileWriter(outFile+"_DEBUG.txt");
			int TokenCount=1;
			String[] arrVertics = interested_TokenNumbers_In_CSV.split(",");
			//read interested tokens
			while(TokenCount<=arrVertics.length){
				mapInterestedTokens.put(new Integer(arrVertics[TokenCount-1]), 
										new Integer(arrVertics[TokenCount-1]));
				TokenCount++;
			}
			String concLine="";int lineNumber=0;
			System.out.println("mapInterestedTokens:"+mapInterestedTokens);
			// read each line
			while ((line = reader.readLine()) != null) {
					lineNumber++;
					if(isSOPprint)
						System.out.println("line:"+line);
					
					concLine="";
					TokenCount=0;
					if(line.indexOf(delimiter_OF_inFile) ==-1)
						continue;
					System.out.println("lineNumber:"+lineNumber);
					//vertices
					arrVertics = line.split(delimiter_OF_inFile);
					while(TokenCount<=arrVertics.length){
						TokenCount++;
						//System.out.println("TokenCount:"+TokenCount);
						//is interested token
						if(mapInterestedTokens.containsKey(TokenCount)){
							if(concLine.length()==0)
								concLine=line.split(delimiter_OF_inFile)[TokenCount-1];
							else{
								concLine=concLine+delimiter_OF_inFile
									+line.split(delimiter_OF_inFile)[TokenCount-1];
							}
						}
						else{
							continue;
						}
						String [] s=line.split(delimiter_OF_inFile);
						
						if(isSOPprint)
							System.out.println("concLine:"+concLine+";len:"+s.length);	
					}
					//duplicate 
					if(is_remove_duplicate){
						if(!mapDuplicateCheck.containsKey(concLine)){
							writer.append(concLine+"\n");
						}
						else{
							writer_DEBUG.append(concLine+"\n");
							writer_DEBUG.flush();
						}
					}
					else{
						writer.append(concLine+"\n");
					}
					mapDuplicateCheck.put(concLine, "dummy");
					writer.flush();
			}//end while
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//readFile_WRAPPER_get_Set_Of_N_positioned_Tokens
	public static void readFile_WRAPPER_get_Set_Of_N_positioned_Tokens(String inFolder,
																	   String delimiter,
																	   String interested_TokenNumbers_In_CSV, //starts with 1
																	   boolean is_remove_duplicate,
																	   boolean isSOPprint
																	   ){
		int cnt=0;
		try {

			File f=new File(inFolder);
			String [] filesOfgivenFolder=f.list();
			
			
			while(cnt<filesOfgivenFolder.length){ // while start
				
				if(filesOfgivenFolder[cnt].indexOf("Store")>=0) {cnt++;continue;}
				//skip subfolder
				if(new File(filesOfgivenFolder[cnt]).isDirectory()) {cnt++;continue;}
				String currFile=inFolder+filesOfgivenFolder[cnt];
 
				//do process for curr file 
				readFile_get_Set_Of_N_positioned_Tokens(currFile,
														currFile +"_out.txt",
														delimiter,
														interested_TokenNumbers_In_CSV,
														is_remove_duplicate,//is_remove_duplicate
														isSOPprint//isSOPprint  
														 );
				
				cnt++;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	
	//main
	public static void main(String[] args) throws IOException {
		 
		 String baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Feeds-Newscopy/Feeds-Trad-17-Feb-2015-Set1-XML2CSV/tmp/";
		 	baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/";
		 	baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/";
		 	baseFolder="/Users/lenin/Dropbox/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds10/debugMALLETfiles/";
		 	baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/";
		 	baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/daily_try4/2014Only-numeric/categorical/categoricaltry3_clean/";
		 	baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/2014Only/";
		 	baseFolder="/Users/lenin/Dropbox/#problems/p18/FromMaitrayi/";
		 	baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/tmp/";
		 	baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/tmp/";
		 	baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/"; 
	    	baseFolder="/Users/lenin/Dropbox/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/_temp3/";

    	 String inputFileName=baseFolder+"all_issues_id37151552_NODUP.txt";
    	 		inputFileName=baseFolder+"all.txt";
    	 		inputFileName=baseFolder+"mergedall-2016-T9-10-Topics-all-tokens.txt_ONLY_ENGLISH.txt_removed_NOISE.txt_REMOVE_STOPWORDS.txt_MATCHED.txt_bTextNLPtagged.txt_titleNLP.txt";
    	 		inputFileName=baseFolder
    	 				+"mergedall-2016-T9-10-Topics-all-tokens.txt_ONLY_ENGLISH.txt_removed_NOISE.txt_REMOVE_STOPWORDS.txt_MATCHED.txt_4_mallet_DEBUG_newDELIMITER.txt";
    	 		inputFileName=baseFolder
    	 				+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb.txt";
    	 		inputFileName=baseFolder+"single_all.txt";
    	 		inputFileName=baseFolder+"all_2014.csv";
    	 		inputFileName=baseFolder+"all.txt";
    	 		inputFileName=baseFolder+"out_docID_URL.txt";
    	 		inputFileName=baseFolder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt.format2.SET.2..txt";
    	 		inputFileName=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt";
    	 		inputFileName=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all.txt_added_perso_origz_loc_verb2.txt";
		    	
    	 //NOT USED
		 String outputFileName=baseFolder+"all_issues_id37151552_NODUP_ONLYURL.txt";
		 		outputFileName=baseFolder+"all_only_dataid.txt";
		 		outputFileName=baseFolder+"mergedall-2016-T9-10-Topics-all-tokens-token4OnlyPERSON.txt";
		 		outputFileName=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all.txt";
		 		outputFileName=baseFolder+"out_single_all_dataIDs.txt";
		 		outputFileName=baseFolder+"2014_only_all_dataIDs_ONLY.txt";
		 		outputFileName=baseFolder+"all_NODUP.txt";
		 		outputFileName=baseFolder+"out_docID_URL_NODUP.txt";
		 		outputFileName=baseFolder+"OUTPUT_tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt.format2.SET.2..txt";
		 		outputFileName=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18_primarykey.txt";
		 		outputFileName=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt_url_tagline_untagline.dummy.all_new.txt";
		 		
		 String interested_TokenNumbers_In_CSV="1,2,3"; /// example-> "1,2,3"
		 //********** for only 1 file , run this
		 readFile_get_Set_Of_N_positioned_Tokens(inputFileName,
				 								 outputFileName,
				 								 "!#!#", //"/t","!!!","!#!#",","
				 								 interested_TokenNumbers_In_CSV, //starts with 1
				 								 true, //is_remove_duplicate
				 								 false //isSOPprint
				 								 );
		 
		 //********** for only 1 folder , run this
		 //baseFolder="/Users/lenin/OneDrive/Maitrayi/Full Article/ndtv/cleaned/";
		 //interested_TokenNumbers_In_CSV="1";
//		 readFile_WRAPPER_get_Set_Of_N_positioned_Tokens(baseFolder,
//														 "!!!",
//														 interested_TokenNumbers_In_CSV, //starts with 1
//														 true, //is_remove_duplicate
//														 false //isSOPprint
//														 );
		 
		 
		 
	}
	
}
