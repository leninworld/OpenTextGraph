package p18;

import LinearPerceptron.Main;
import crawler.*;
import iitb.MaxentClassifier.MaxentClassifier;
import iitb.Utils.Options;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import org.apache.commons.io.FileUtils;
 

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

//import Perceptron.src.Main.*;
  

//
public class P18_MAIN_get_dupURL_N_domainNames {
	
	//get_only_topN_TF_IDF //   string_all_featureID_featureValue
	public static Map<Integer, Double>  get_only_topN_TF_IDF(String string_all_featureID_featureValue){
		Map<Integer,Double> mapOut_sorted_asc_top_N=new TreeMap<Integer, Double>();
		String [] s1=string_all_featureID_featureValue.split(" ");
		int c=0; int featureIdx=-1; TreeMap<Integer, Double> mapOut=new TreeMap<Integer, Double>();
		try {
			while(c<s1.length){
				//
				if( s1[c].indexOf(":") >=0){
					featureIdx=Integer.valueOf(s1[c].substring(0, s1[c].indexOf(":")));
					double frequency_OR_Tfidf=Double.valueOf(s1[c].substring( s1[c].indexOf(":")+1, s1[c].length()) );
					mapOut.put(featureIdx, frequency_OR_Tfidf);
				}
				c++;
			}
			// sortByValue_ID_method4_ascending
			Map<Integer,Double> mapOut_sorted_asc=
						Sort_given_treemap.sortByValue_ID_method4_ascending(mapOut);
			//sortByValue_ID_method4_ascending_reverse_TopN (take only highest top N=10 tf-idf values)
							mapOut_sorted_asc_top_N=Sort_given_treemap.
														sortByValue_ID_method4_ascending_reverse_TopN(mapOut_sorted_asc, 10);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mapOut_sorted_asc_top_N;
	}
	////// calc_N_return_max_feature_IDX
	static int calc_N_return_max_feature_IDX(String inputFile){
		int max_feature_IDX=-1;
		try{
			String line="";
			//first read to get MAXIMUM number of features in the file
			BufferedReader reader1   = new BufferedReader(new FileReader(inputFile));
			int featureIdx=0;
			while ((line = reader1.readLine()) != null) {
				String []s1=line.split(" ");
				//label does not have :, other features have : 
				int c=0;  
				int curr_label = Integer.valueOf(s1[s1.length-1]);
				
				//
				while(c<s1.length){
					//
					if( s1[c].indexOf(":") >=0){
						featureIdx=Integer.valueOf(s1[c].substring(0, s1[c].indexOf(":")));
						//
						if(featureIdx>max_feature_IDX)
							max_feature_IDX=featureIdx;
				
					}
					c++;
				
				}
			}

			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return max_feature_IDX;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//is_flag->1 = does first 4 steps (upto crawling)
	//is_flag->2 = does WORK OF merge_2_inputFiles_for_full_bodyText() - MERGES RSSFEED and OUTPUT_CRAWLED based on common URL match
	//is_flag->3 = TF-IDF creation
	//is_flag->4 = (Prerequisition: manually create "automated ground truth(true positive)" file ). 
	//			   Both  TF-IDF output from (flag==3) AND "automated ground truth(true positive)" merging as "single file".
	//				i.e., Create final file with format <label> <feat1:value1> <feat2:value2>
	//								  (0) Generate file1 using grep command on file2***
	//							  	  (1) get automated groundTruth truePOSITIVEdocID as KEY in a map/
	//								  (2) create final file with format <label> <feat1:value1> <feat2:value2>
	//is_flag->5 =   CREATING SPARSE FILE without HEADER and WITH header (WEKA). 
	//				convert (a) word-frequency based file to sparse (zero) file for maxent classification run.
	//				 calling method convert_featureFile_to_sparseFeatureFile()
	//is_flag->6 =  2-fold cross-validation- split one into two
	//is_flag->7 =  Run (1) Linear Perceptron (2) iitb.MaxentClassifier
	//is_flag->8 =  Prepare a k-means input for weka from <feature:frequency> file.  
	public static TreeMap<Integer,String> P18_get_dupURL_N_domainNames(String[] 	args
																		, int 		flag
																		, int 		top_N_idf_flag4 //can be -1 ( only if is_only_top_N_idf_flag3=true)
																		, boolean 	is_only_top_N_idf_flag4
																		){
		TreeMap<Integer,String> mapOut=new TreeMap<Integer, String>();
		// need manual change
		String problem_prefix="p18";
		// need manual change
	    String baseFolder_for_perceptron="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini1/Perceptron/";
		String intermediate_folder="tmp/";
		//need manual change
		String baseFolder="";
			   baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/";
			   baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/";
		String baseFolder_out="";
		   	   baseFolder_out=baseFolder;
		String outFile=baseFolder_out+"tf-idf.txt";
		String outFile_tf_idf_format2=baseFolder_out+intermediate_folder+"tf-idf.txt_format2.txt";
		String outFile_tf_idf_NOSTOPWORDS_format2=baseFolder_out+intermediate_folder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent.txt";
		// HUMAN ANNOTATORS (Flag==3) and (Flag==4) 
	  	String file1_human_annotator_LABELED="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL.txt";
	  																																								   
	  	// manual change
	 	int	 	token_for_human_annotator_having_url = 4;
	 	int	 	token_for_human_annotator_having_label = 3;
																					  
		FileWriter writeDebug=null;
		String outFile_tfidf_pair=baseFolder_out+"tf-idf.txt";
		try {
			writeDebug=new FileWriter(new File(baseFolder+"debug.txt"));
			// step 0 : REMOVE DUPLICATE LINES
			String InputFile="";String OutputFile="";
			
			String workspace_basefolder="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/";
			 
		    InputFile =baseFolder+"all_issues_id37151552.txt";
			OutputFile=baseFolder+"all_issues_id37151552_NODUP.txt"; //this is again set "String inputFileName_only=OutputFile;" inside Flag==1
			String RSSFEED_file_1_NODUP=OutputFile;
			   
			  //DONT change this..same way used inside FLAG==1 and also again used in Flag==4 
			 String CRAWLED_outputFile_backup=baseFolder+"CRAWLED_4_"+OutputFile; //OUTPUT
			//boolean is_Append_out_File=true;
			int token_index_to_consider_for_duplicate=2;
			System.out.println("entered...");
			TreeMap<String, String> mapConfig = new TreeMap<String, String>();
			if(flag==1){
			// REMOVE DUPLICATES
			ReadFile_readEachLine_removeDuplicates_WriteToAnotherFile.readFile_readEachLine_removeDuplicates_WriteToAnotherFile(
																							InputFile, 
																							OutputFile, 
																							String.valueOf(token_index_to_consider_for_duplicate),
																							true,  //is_token_index_to_consider_for_duplicate_a_URL
																							false, //is_Append_out_File, 
																							false  //isSOPprint
																							);
			
//			String InputFile,
//			String OutputFile,
//			String token_index_to_consider_for_duplicateCSV,
//			boolean is_token_index_to_consider_for_duplicate_a_URL,
//			boolean is_Append_out_File,
//			boolean isSOPprint
			
			  // step 1 : get specific token -URL
				
		 	  //baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/";
		 	
			 //manual need change
		 	 String inputFileName_only=OutputFile; // "all_issues_id37151552_NODUP.txt";
	    	 String inputFileName=baseFolder+inputFileName_only;
	    	 String inputFileName_ORIG=inputFileName;
	    	 //NOT USED
			 String outputFileName=baseFolder+"all_issues_id37151552_NODUP_ONLYURL.txt";

			 
			 String interested_TokenNumbers_In_CSV="2";
			 //********** for only 1 file , run this (GET URL)
			 // 
//			if(flag==1){
			 
			 ReadFile_get_Set_Of_N_positioned_Tokens.readFile_get_Set_Of_N_positioned_Tokens(
					 								 inputFileName,
					 								 outputFileName,
					 								 "!!!",
					 								 interested_TokenNumbers_In_CSV, //starts with 1
					 								 true, //is_remove_duplicate
					 								 false //isSOPprint
					 								 );
		 
			
			// step 2 : get distinct DOMAIN NAME from each URL 
			//baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/";
			//
			String inFile=baseFolder+"all_issues_id37151552_NODUP_ONLYURL.txt";	 
				  outFile =baseFolder+"all_issues_id37151552_NODUP_DOMAINNAMES.txt";
			  
				// find domain name
				Find_distinct_domain_name_from_Given_FILE.
				find_distinct_domain_name_from_Given_FILE(  baseFolder,
															inFile,
															1, //index_having_url_inFile, //can be -1
															outFile, // output
															2, //Flag_Approach
															true
														 );
				
			 
			//step 3 : Compare the output of Step 2 with GLOBAL DOMAIN NAME
			String first_file=baseFolder+"all_issues_id37151552_NODUP_DOMAINNAMES.txt";
			//below is the global domain name(DONT CHANGE) ..backup saved in google drive.
			String second_file  ="/Users/lenin/Downloads/#problems/p18/ds1/global_domainNAMES_patternFORNEWSBODYSTART.txt";
			
			String outFile_Exists=baseFolder+"all_issues_id37151552_NODUP_DOMAINNAMES.txt_URLs_exists.txt";
			String outFile_Not_Exists=baseFolder+"all_issues_id37151552_NODUP_DOMAINNAMES.txt_URLs_notExists.txt";
			String outFile_File1_addedComment=first_file+"_addedComment.txt";
			
			String delimiter_to_be_added_to_first_File="";
			int token_interested_in_first_File=1;
			 
				//CALLING - readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String
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
			// step 4: CRAWLING
			
			// System.out.println("mapCrawled->" + mapAlreadyCrawledURL);
			// Run from command prompt
			if (args.length >= 1) {
				String run_type = args[0].toLowerCase(); // first argument from command line
				String confFile = args[1]; // first argument from command line
			}
			// dummy no input parameters..
			else if (args.length == 0) {
				// pre-defined config file

			}


			String confFile="";

			Crawler crawler=new Crawler();
			String option="n";
						if (args.length >= 1) {
							confFile = args[1]; // second argument from command line
							System.out.println("\n Got config File from command prompt:"+ confFile);
						} else if (args.length == 0) {
							confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/1.crawler/config.txt";
						}
						// config
						mapConfig =  GetConfig.getConfig(confFile);
						mapConfig.put("run_type","dummy");
						// getCrawler()
						// crawler t2 = new crawler();
						// t2.mapConfig = mapConfig;
						String folder = mapConfig.get("folder"); // base folder
						//OVERWRITE FOLDER
						folder=baseFolder;
						mapConfig.put("folder", folder);
						
						String  NLPfolder = mapConfig.get("NLPfolder"); // base folder
						boolean is_NLP_run = Boolean.valueOf(mapConfig.get("is_NLP_run"));
						String  inputSingleURL = mapConfig.get("inputSingleURL");
						String	outputFile     = folder+ mapConfig.get("outputFile");
						boolean isAppend = Boolean.valueOf(mapConfig.get("isAppend"));
						String  delimiter = mapConfig.get("delimiter"); // "!!!"
						String  inputType = mapConfig.get("inputType"); // {"file",""} input
																		// is a file type
																		// (or) single url
						String outputType = mapConfig.get("outputType");// {"writeCrawledText2OutFile","mongodb","outNewUnCrawledURLFile"}
						String outAlreadyCrawledURLsINaFile =folder+ mapConfig.get("outAlreadyCrawledURLsINaFile"); // "outAlreadyCrawledURLsINaFile.txt";
					
						String debugFileName = folder + mapConfig.get("debugFileName");

						int fromLine = Integer.valueOf(mapConfig.get("fromLine"));
						int toLine = Integer.valueOf(mapConfig.get("toLine"));
						String outdebugErrorAndURLfile = folder+ mapConfig.get("outdebugErrorAndURLfile");
						String outNewUnCrawledURLFile = folder+mapConfig.get("outNewUnCrawledURLFile");
						String parseType = mapConfig.get("parseType");
						String method = mapConfig.get("method");
						String flag2 = mapConfig.get("flag2");
						boolean isSOPdebug = Boolean.valueOf(mapConfig.get("isSOPdebug"));
						System.out.println("url present:"+ mapConfig.get("URL_present_in_which_column_token"));
						int URL_present_in_which_column_token = Integer.valueOf(mapConfig.get("URL_present_in_which_column_token"));
						String crawlerType = mapConfig.get("crawlerType");
						int in_Already_Crawled_File_URL_present_in_which_column_token = Integer.valueOf(mapConfig.get("in_Already_Crawled_File_URL_present_in_which_column_token"));
						isSOPdebug = Boolean.valueOf(mapConfig.get("isSOPdebug"));
						boolean is_overwrite_flag_with_flag_model=Boolean.valueOf(mapConfig.get("is_overwrite_flag_with_flag_model"));
						//model for tagging
						POSModel inflag_model2 = new POSModelLoader().load(new File(NLPfolder+ "en-pos-maxent.bin"));
						//model for chunker
						InputStream is = new FileInputStream(NLPfolder+"en-parser-chunking.bin");
						ParserModel inflag_model2_chunking =null;
						// load only flag==1
						if(flag==1)
							inflag_model2_chunking = new ParserModel(is);
						
						//OVERWRITING
						String inputListOf_URLfileCSV =folder+ mapConfig.get("inputListOf_URLfileCSV");
							   inputListOf_URLfileCSV=inputFileName_ORIG;
							   outputFile=folder+"CRAWLED_4_"+inputFileName_only; //OUTPUT
							   URL_present_in_which_column_token=2;
						

							   
					    //Overwriting
						mapConfig.put("inputListOf_URLfileCSV" , inputListOf_URLfileCSV);
						mapConfig.put("URL_present_in_which_column_token" , String.valueOf(URL_present_in_which_column_token));
						mapConfig.put("outputFile", outputFile);
							   
						// CONFIGURATION
						System.out.println("\nConfiguration:");
						for (String i : mapConfig.keySet()) {
							System.out.println(i + "->" + mapConfig.get(i));
						}
						// option="n";
						// // continue
						// option=read_input_from_command_line_to_continue_Yes_No(br);
						//
						// if(!option.equalsIgnoreCase("y"))
						// System.exit(1);
						//
						// t0 = System.nanoTime();
						//
						// if(outAlreadyCrawledURLsINaFile.equals("")){
						// outAlreadyCrawledURLsINaFile=inputFile;
						// }
						
						// not EXISTS , create empty
						if(!new File(outAlreadyCrawledURLsINaFile).exists())
							new File(outAlreadyCrawledURLsINaFile).createNewFile();
						TreeMap<String, String> mapAlreadyCrawledURL=new TreeMap<String, String>();
						if(flag==1){
							// N th token is assumed to have already crawled URL
							// This much be the output file (format:
							// <email.file.name!!!URL!!!crawled text>) <- N=2 here
							// mapAlreadyCrawledURL <--> <lineNo,URL>
													mapAlreadyCrawledURL =  crawler.
																			LoadNthTokenFromWithORWithoutGivenPatternFromGivenFileListandLoadToMap(
																						outAlreadyCrawledURLsINaFile,
																						in_Already_Crawled_File_URL_present_in_which_column_token,
																						"!!!", "http", // pattern1
																						"www", // pattern2
																						"http://", // replace
																						"www.", //
																						false //isSOPprint
																	       );
						}

						System.out.println("folder:" + folder + "\nNLPfolder:" + NLPfolder
											+ "\nis_NLP_run:" + is_NLP_run
											+ "\ninputListOf_URLfileCSV:" + inputListOf_URLfileCSV
											+ "\ninputSingleURL:" + inputSingleURL + "\noutputFile"
											+ outputFile + "\nisAppend" + isAppend + "\ndelimiter:"
											+ delimiter + "\ninputType:" + inputType + "\noutputType:"
											+ outputType + "\noutAlreadyCrawledURLsINaFile:"
											+ outAlreadyCrawledURLsINaFile + "\ninputURLfileListCSV:"
											+ inputListOf_URLfileCSV + "\nfromLine:" + fromLine
											+ "\ntoLine:" + toLine + "\noutdebugErrorAndURLfile:"
											+ outdebugErrorAndURLfile + "\noutNewUnCrawledURLFile:"
											+ outNewUnCrawledURLFile + "\nparseType:" + parseType
											+ "\nmethod:" + method);

						System.out.println("mapAlreadyCrawledURL.size:"
								+ mapAlreadyCrawledURL.size());
						System.out.print("\n Enter Y (or) N to continue..");
						BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
						// read the username from the command-line; need to use try/catch
						// with the
						// readLine() method
						try {
							 option = br.readLine();
						} catch (IOException ioe) {
							System.out.println("IO error trying to read your option!");
							System.exit(1);
						}
						if (!option.equalsIgnoreCase("y"))
							System.exit(1);
						long t0 = System.nanoTime();
						
						if(flag==1){

						// run the main crawler function
						crawler.
						getCrawler(
								folder,
								NLPfolder,
								is_NLP_run,
								inputListOf_URLfileCSV, // input file
														// <email.name!!!URL!!!header text
														// from email> <- at least two
														// column
								inputSingleURL,
								outputFile,
								isAppend,
								delimiter,
								inputType, // {"file","mongodbURLonly","mongodburlandbody}
											// input is a file type (or) single url
								outputType, // {"outfile","mongodb",""}
								flag2, //
								outAlreadyCrawledURLsINaFile, mapAlreadyCrawledURL,
								fromLine,
								toLine,
								outNewUnCrawledURLFile, // for
														// {outputType="outNewUnCrawledURLFile"}
								outdebugErrorAndURLfile, parseType, isSOPdebug,
								URL_present_in_which_column_token, crawlerType,
								is_overwrite_flag_with_flag_model,
								inflag_model2,
								inflag_model2_chunking,
								debugFileName);
						
						System.out.println("end:mapAlreadyCrawledURL:"+ mapAlreadyCrawledURL.size());
						
						}
			
						System.out.println("Entering flag 2..");
			     
						// write to output map	
						mapOut.put(1, mapConfig.get("outputFile"));
				}

//****begin***********Step 5: merge 2 files vertically (1) RSSFEED static file (2) CRAWLED output -NOT CLEANED (has HTML TAGS)**************  
				//
				if(flag==2){

					//String fileprefix="bfr_17Feb_set2"; //"17feb"; OBSELETE
					//RSS FEED ( <body!!!URL!!!dateTime!!!Subject> )
					String file1=RSSFEED_file_1_NODUP;
					//CRAWLED OUTPUT from above "Step 5: Clean the crawled output file and get only bodyText from NEWS." 
					String file2=mapConfig.get("outputFile"); //baseFolder+fileprefix+"_CRAWLED_output.txt";
					//OUTPUT
					String out_file3=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt";
					// 
					String file4_DomainName_NewsStartPatterns="/Users/lenin/Downloads/#problems/p18/ds1/global_domainNAMES_patternFORNEWSBODYSTART.txt";
					
					//background: (1) Filter out based on keywords (mental illness etc) from news feed say "file1".
					//			  (2) Some news articles have only partial body of news. Hence, need to crawl the full body. call that "file2"
					//			  
					//STEP 1: MERGING RSSFEED + CRAWLED HTML raw  file
					//MERGE
					Merge_2_inputFiles_for_full_bodyText.
					merge_2_inputFiles_for_full_bodyText(
														 baseFolder,
														 file1, //FROM RSS FEED (INPUT)
														 file2, //FROM CRAWLED HTMLPAGE (INPUT)
														 out_file3,//OUTPUT
														 file4_DomainName_NewsStartPatterns,
														 false //	isSOPprint
									 					);
					
					String file2_EXISTS=file2+"_EXISTS_FROM_RSSFEED.txt";
					String file2_NOTEXISTS=file2+"_NOT_EXISTS_FROM_RSSFEED.txt";
					String _File1_addedComment=file2+"__FROM_RSSFEED_File1_addedComment.txt";
					// STEP 2 : mismatch between RSSFEED (file1) and file3 above
					ReadFile_2_EachLineORtoken_from_First_File_exists_in_Second_File_as_String.
					readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String( file1  , //first_File, 
																								"", //delimiter_to_be_added_to_first_File, 
																								2, //token_interested_in_first_File, 
																								out_file3, // second_File, (input here)
																								"!!!", //delimiter_for_splitting, 
																								file2_EXISTS, //out_File_exists, 
																								false, //is_Append_outFile_exists, 
																								file2_NOTEXISTS, //out_File_not_exists, 
																								false, //  is_Append_outFile_not_exists,
																								_File1_addedComment, //outFile_File1_addedComment
																								false  // isSOPprint
																								);
					
			 
					//STEP 3: (ENGLISH ONLY)
					int 	token_having_URL_for_file1=2;
					String  file2_DomainName_NewsStartPattern="/Users/lenin/Downloads/#problems/p18/ds1/global_domainNAMES_patternFORNEWSBODYSTART.txt";
					String  OUTPUT_file3=out_file3+"_"; //out
					
					//remove_NON_ENGLISH_newsarticles
					Remove_NON_ENGLISH_newsarticles.
					remove_NON_ENGLISH_newsarticles(
											   		baseFolder,
											   		out_file3, //STEP 1 (from)..INPUT here
											   		token_having_URL_for_file1,
											   		file2_DomainName_NewsStartPattern,
											   		OUTPUT_file3, //out
											   		false //isSOPprint
											   		);
					System.out.println("STEP 3: INPUT:"+file2_EXISTS);
					System.out.println("STEP 3: OUTPUT:"+OUTPUT_file3);
					
					//STEP 4: LENGTH LIMIT
					int 	token_1_for_applying_length_FILTER=2;
					int 	token_2_for_applying_length_FILTER=4;
					OUTPUT_file3=OUTPUT_file3+"_ENGLISH.txt"; //ENGLISH
					boolean isSOPprint=false;
					int filter_length=600;
					
					// file1 -> (1) REMOVE note:news articles (2) remove NOT FOUND startPattern for news article
					//          (3) REMOVE short message on given length
					//readFile_read_a_token_FILTER_on_length
					ReadFile_read_a_token_FILTER_on_length.
					readFile_read_a_token_FILTER_on_length(
											   		baseFolder,
											   		OUTPUT_file3,
											   		token_1_for_applying_length_FILTER,//can be -1
											   		token_2_for_applying_length_FILTER, //can be -1
											   		filter_length,
											   		OUTPUT_file3+"_LEN_50_FILTER.txt", //out
											   		isSOPprint);
					
					System.out.println("STEP 4: INPUT:"+OUTPUT_file3);
					System.out.println("STEP 4: OUTPUT:"+OUTPUT_file3+"_LEN_50_FILTER.txt");
					
				}

//****END***********Step 5: merge 2 files vertically (1) RSSFEED static file (2) CRAWLED output**************
				System.out.println("BEFORE Entering flag 3..");
			String inputFile_noStopWord_WORD_ID_format2=""; int maxwordid=-1;
			String file_for_writer_maxwordid=baseFolder+"maxwordid.txt";
			FileWriter writer_maxwordid =null;
			String file_TFIDF_based_with_LABEL=inputFile_noStopWord_WORD_ID_format2+"_added_LABEL_4WEKA.txt";
//***BEGIN ***Step 6:******** TF-IDF creation/
			if(flag==3){
				System.out.println("Entering flag 3..");
				//need manual change
				baseFolder=baseFolder+"tmp/"; 
				String inputFile=baseFolder+"";
				// below file (maybe created by "readFile_read_a_token_FILTER_on_length" above)
				// FILE that has a token to be tagged for NLP
				//need manual change (baseFolder added to "tmp/" actually ---THIS FILE VERY IMPORTANT 
				inputFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt";
				inputFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txtNEW.txt_bTextNLPtagged_NODUP.txt";
				inputFile=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txtNEW_1000labelled_confCorrected.txt_1st1000ln_removeIrrelevaLABEL.txt_bTextNLPtagged.txt";
				inputFile=baseFolder+"O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged.txt";
					//only African-American
				inputFile=baseFolder+"O.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txt_removeIrrelevaLABEL_NLPtagged_onlyAfricanAmerican.txt";
				
				// output docID and URL
				String output_docID_URL=baseFolder+"out_docID_URL.txt";
				//output for maxwordid
				writer_maxwordid = new FileWriter(file_for_writer_maxwordid);
				String delimiter_for_inputFile="!!!"; //{"!!!","!#!#"}
				//need manual change
				int token_index_having_text=4;
					token_index_having_text=9; // bodyTEXT
				int token_having_primaryKEY_url=5; //URL
				//
				outFile=baseFolder_out+"tf-idf.txt";
				// writing docID!!!URL
				FileWriter writer_docID_URL=new FileWriter(new File(output_docID_URL));
				
				System.out.println("START OF calc_tf_idf..");
				//Step 1: calculate TF-IDF
				// calc tf-idf
				TreeMap<Integer,String> mapOut_for_tfidf=   Calc_tf_idf.
															calc_tf_idf( baseFolder,
																		 baseFolder_out,
																		 inputFile,
																		 delimiter_for_inputFile, // delimiter_for_inputFile	 
																		 token_index_having_text, //token_index_having_text
																		 outFile,
																		 true, //is_do_stemming_on_word
																		 true, //is_ignore_stop_words_all_together
																		 false //isSOPprint
																		);
				System.out.println("END OF calc_tf_idf..");
				/*************some convertion call below can be temporarily commented to speed up..uncomment after****/
				//Step 2: convert into format_2 ("Convert docID_wordID_freq to another format 
				//  <docId!!!word_id_1:freq_1 word_id_2:freq_2 word_id_3:freq_3>")
				//word tfidf conversion to format2
				// below comment/uncomment as needed
				TreeMap<String,Integer> map_out_from_word_tfidf=
				Convert_DocID_WordID_Freq_To_another_format.convert_DocID_WordID_Freq_To_another_format_2(
																	baseFolder,
																	outFile,  //in 
																	outFile_tf_idf_format2, //out
																	"!!!",
																	false //isSOPprint
																	);
				
//				   String  baseFolder,
//				   String  inputFile,
//				   String  outFile,
//				   String  delimiter_4_inputFile,
				
				
				
				maxwordid =Integer.valueOf( mapOut_for_tfidf.get(1) );
				// Write maxwordid to output file
				writer_maxwordid.append(String.valueOf(maxwordid));
				writer_maxwordid.flush();
// 				below comment/uncomment as needed
				//word tfidf conversion to format2
				Convert_DocID_WordID_Freq_To_another_format.convert_DocID_WordID_Freq_To_another_format_2(
																	baseFolder,
																	outFile+"_noStopWord_WORD_ID.txt",  //in 
																	outFile+"_noStopWord_WORD_ID.txt_format2.txt", //out <-TF-IDF
																	"!!!",
																	false //isSOPprint
																	);
				
// 				*****below comment/uncomment as needed
//				//word frequency conversion to format2
				Convert_DocID_WordID_Freq_To_another_format.convert_DocID_WordID_Freq_To_another_format_2(
																	baseFolder,
																	outFile+"_noStopWord_WORD_ID2.txt",  //in
																	outFile+"_noStopWord_WORD_ID2.txt_format2.txt", //out
																	"!!!",
																	false //isSOPprint
																	);
				
				inputFile_noStopWord_WORD_ID_format2=outFile+"_noStopWord_WORD_ID.txt_format2.txt";

				TreeMap<Integer,String> map_inFile_seq_eachLine=
				ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								 inputFile, 
																							 	 -1, //startline, 
																								 -1, //endline,
																								 "f1 ", //debug_label
																								 false //isPrintSOP
																								 );
				// create DocID and URL
				 for(int docID:map_inFile_seq_eachLine.keySet() ){
					 String [] arr_ = map_inFile_seq_eachLine.get(docID).split("!!!");
					 if(arr_.length>0)
					 writer_docID_URL.append(docID+"!!!"+arr_[token_having_primaryKEY_url-1]+"\n");
					 writer_docID_URL.flush();
				 }
				 writer_docID_URL.close();
				System.out.println("*****note down max word id:"+mapOut_for_tfidf.get(1));
				
				// add label to TF-IDF file
				//  lineNo in input1 (a token before!!!) == actual lineNo in input2
				merge_change_format_tfidf_add_label(inputFile_noStopWord_WORD_ID_format2, // input1  
													file1_human_annotator_LABELED, //input2
													token_for_human_annotator_having_label, //index_having_LABEL_in_inputFile2 (SET at the starting)
													true, //is_HEADER_present_in_input1File
													true, //is_HEADER_present_in_input2File
													file_TFIDF_based_with_LABEL //OUTPUT
													);
				
				System.out.println("NOTE: TF-IDF and added LABEL file->"+file_TFIDF_based_with_LABEL);
			} //if(flag==3){
			
			
			
			
//******END ****Step 6:******** TF-IDF creation/
			System.out.println("BEFORE Entering flag 4..");
			BufferedReader reader2 = null;
			BufferedReader reader3 = null;
			//******START ****Step 7:******** (0) Generate file1 using grep command on file2***
			//							  	  (1) get automated groundTruth truePOSITIVEdocID as KEY in a map/
			//								  (2) (tf-idf based) create final file with format <label> <feat1:value1> <feat2:value2>
			//							      (3) (word-frequency based) create final file with format <label> <feat1:value1> <feat2:value2>
			//							      (4) (word-frequency based) create final file with format maxentropy crf.iitb format <feat1_value1>,<feat2_value2>,<label>
			//need manual change for intermediate "tmp"
			intermediate_folder="tmp/";
			//create directory if not exists
			if(!new File(baseFolder+intermediate_folder).exists())
				new File(baseFolder+intermediate_folder).mkdir();
			//tf-idf based; lineno==docID--> THIS FILE <lineno!!!feat1:tfidf feat2:tfidf feat3:tfidf>
			inputFile_noStopWord_WORD_ID_format2=baseFolder+intermediate_folder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt";
			//word-frequency based
			String inputFile_noStopWord_WORD_ID_format2_2=baseFolder+intermediate_folder+"tf-idf.txt"+"_noStopWord_WORD_ID2.txt_format2.txt";
			//need manual change***generate file1 using grep command on file2***
			String file1_automated_ground_truth_file_byGREP_from_file2fullCRAWLED_OUTPUT
									=baseFolder+"OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER_AUTO_GT_TP.txt";
			//----------------------- or
			//need manual change***  HUMAN ANNOTATOR ***
			// this is set at the starting
//			String 	file1_human_annotator_LABELED="/Users/lenin/Dropbox/#problems/p18/FromMaitrayi/all.txt";
//				  	file1_human_annotator_LABELED="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/experiment/labelled/OUTPUT_merged_full_bodyText_RSSFEED_CRAWLED.txt__ENGLISH.txt_LEN_50_FILTER.txt_PASSED_FILTER.txt_url_tagline_untagline.3all_p18.txtNEW_1000labelled_confCorrected.txt_1st1000ln_removeIrrelevaLABEL.txt";
				  	
			
			// THIS below file is output from Flag==3 ,... DONT change the FILE NAME..
			String inputFile_docID_URL=baseFolder+intermediate_folder+"out_docID_URL.txt";
			
			// input
			//this is word-freq based --> <feat1:freq feat2:freq feat3:freq> <LABEL>
			String fileName_maxent=inputFile_noStopWord_WORD_ID_format2+"2_added_LABEL_crfiitb_maxent.txt";
			// output
			String file_of_inputFile_noStopWord_WORD_ID_format2=inputFile_noStopWord_WORD_ID_format2+"_added_LABEL.txt";

			
			//one of two below MAP has the label (AUTOMATED or HUMAN LABEL) 
			TreeMap<Integer,Integer> map_automated_groundTruth_truePOSITIVEdocID_as_KEY=new TreeMap<Integer, Integer>();
			TreeMap<String, String> map_HumanAnnotator_URL_N_label=new TreeMap<String, String>();
			TreeMap<String, String> map_docID_URL=new TreeMap<String, String>();
			
			if(flag==4){
				System.out.println("Entering flag 4..");
				// manual change
				int 	token_for_file1_having_url=1;
				String 	file2_full_OUTPUT_CRAWLED_file=CRAWLED_outputFile_backup; //lineNO=docID for this file
				int	 	token_for_file2_having_url =1; //only for BELOW METHOD (automated)
				String 	delimiter_for_file1_file2="!!!";
				//*******(1a) ------- AUTOMATED GROUND TRUTH
				//*****(1) get automated groundTruth truePOSITIVEdocID as KEY in a map/
				// COMMENT/ UNCOMMENT--- see the HEADER of BELOW method FOR MORE DETAILS....
//				map_automated_groundTruth_truePOSITIVEdocID_as_KEY=get_automated_groundTruth_truePOSITIVEdocID_as_KEY(
//																			  	file1_automated_ground_truth_file_byGREP_from_file2fullCRAWLED_OUTPUT,
//																			  	token_for_file1_having_url,
//																			  	file2_full_OUTPUT_CRAWLED_file,//lineNO=docID for this file
//																			 	token_for_file2_having_url,
//																			  	delimiter_for_file1_file2);
				
				//*******(1b) ------- GROUND TRUTH given by HUMAN ANNOTATORS
				//
				if(file1_human_annotator_LABELED.length()>10){ //if file name given ??
					
					   //
				  	  TreeMap<Integer, String>  map_seq_eachLine=
							  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
												.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																												 file1_human_annotator_LABELED, 
																											 	 -1, //startline, 
																												 -1, //endline,
																												 " loading", //debug_label
																												 false //isPrintSOP
																												 );
					 // get URL, LABEL
				  	  for(int seq:map_seq_eachLine.keySet()){
				  		  String [] arr_=map_seq_eachLine.get(seq).replace("^", "").split("!!!");
				  		  if(arr_.length>=3){
				  			  map_HumanAnnotator_URL_N_label.put(arr_[token_for_human_annotator_having_url-1], arr_[token_for_human_annotator_having_label-1].replace(" ", "") );
				  		  }
				  	  }

				}
				// GET the DOCID and URL
			  	  TreeMap<Integer, String>  map_seq_eachLine_forDOClabel=
						  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
											.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																											 inputFile_docID_URL, 
																										 	 -1, //startline, 
																											 -1, //endline,
																											 " loading", //debug_label
																											 false //isPrintSOP
																											 );
			  	  // DOCID, URL
				for(int seq:map_seq_eachLine_forDOClabel.keySet()){
					String [] arr_ = map_seq_eachLine_forDOClabel.get(seq).split("!!!");
					if(arr_.length>=2){
						map_docID_URL.put(arr_[0], arr_[1]); ///its always index 0 and 1s
					}
				}
				//***** (2) (tf-idf based) create final file with format <label> <feat1:value1> <feat2:value2>
				//variable "inputFile_noStopWord_WORD_ID_format2" set in step 6.
				try{
					reader2 = new BufferedReader(new FileReader(inputFile_noStopWord_WORD_ID_format2));//comes from Step 6 output
					reader3 = new BufferedReader(new FileReader(inputFile_noStopWord_WORD_ID_format2_2));//comes from Step 6 output
				}
				catch(Exception e){
					System.out.println("file:"+inputFile_noStopWord_WORD_ID_format2);
					e.printStackTrace();
				}
				
				//TF-IDF BASED
				FileWriter writer = new FileWriter(file_of_inputFile_noStopWord_WORD_ID_format2);
				String line=""; int lineNumber=0;
				System.out.println("map_HumanAnnotator_URL_N_label.size:"+map_HumanAnnotator_URL_N_label.size());
				//each line<====> <docID>!!!<feat1:value1> <feat2:value2> <====> 1!!!1:0.0 89:2.694605198933569 222:6.020599913279624  
				while ((line = reader2.readLine()) != null) {
					String []s2=line.split("!!!");
					int currDocID=Integer.valueOf(s2[0]); // docid
					// GET currURL
					String currURL=map_docID_URL.get(String.valueOf(currDocID));
					
					String curr_set_of_featureID_N_tfidfvalue=s2[1];
					
					//is_only_top_N_idf_flag4::: Pick only top 10 TF-IDF wordid for a document. All other eliminate.
					if(is_only_top_N_idf_flag4){
						//get_only_topN_TF_IDF
						 Map<Integer, Double>  map_sorted_topN_tfidf_currDoc=
								 					get_only_topN_TF_IDF(curr_set_of_featureID_N_tfidfvalue);
						 //overwrite
						 curr_set_of_featureID_N_tfidfvalue=
								 map_sorted_topN_tfidf_currDoc.toString().replace("{", "").replace("}", "").replace("=", ":").replace(", ", " ");
					}
					
					if(currURL==null){
						System.out.println("currURL IS NULL:"+currURL+" "+map_docID_URL.containsKey(String.valueOf(currDocID))+"<-->"+currDocID+"<-->");continue;
					}
					 
//					System.out.println("currURL:"+currURL);
//					System.out.println("map_HumanAnnotator_URL_N_label:"+map_HumanAnnotator_URL_N_label+" currURL:"+currURL);
					
					if(currURL!=null){
						// URL PRESENT IN HUMAN ANNOTATOR
						if(map_HumanAnnotator_URL_N_label.containsKey(currURL)){
							//automated ground truth "true positive"  OR HUMAN ANNOTATOR
							if(map_automated_groundTruth_truePOSITIVEdocID_as_KEY.containsKey(currDocID) ||
								map_HumanAnnotator_URL_N_label.get(currURL).equals("1")
									){
								writer.append( "+1 "+curr_set_of_featureID_N_tfidfvalue+"\n");
							}
							//automated ground truth "true negative"  OR HUMAN ANNOTATOR
							else if(!map_automated_groundTruth_truePOSITIVEdocID_as_KEY.containsKey(currDocID) ||
									 map_HumanAnnotator_URL_N_label.get(currURL).equals("0")) { 
								writer.append( "-1 "+curr_set_of_featureID_N_tfidfvalue+"\n");
							}
						}
					}
					writer.flush();
				}
				
//				System.out.println("map_docID_URL:"+map_docID_URL);
				
				//
				//***** (3) (word-frequency based) create final file with format <label> <feat1:value1> <feat2:value2>
				//WORD-FREQUENCY BASED
				FileWriter writer2 = new FileWriter(inputFile_noStopWord_WORD_ID_format2+"2_added_LABEL.txt");
				FileWriter writer_NB_CPP_wfreq = new FileWriter(inputFile_noStopWord_WORD_ID_format2+"2_added_LABEL_NB_CPP.txt");

				FileWriter writer3_maxent = new FileWriter(fileName_maxent);
				
				 line="";   lineNumber=0;
					//each line<====> <docID>!!!<feat1:value1> <feat2:value2> <====> 1!!!1:0.0 89:2.694605198933569 222:6.020599913279624  
					while ((line = reader3.readLine()) != null) {
						String []s2=line.split("!!!");
						int currDocID=Integer.valueOf(s2[0]); // docid 
						// GET currURL
						String currURL=map_docID_URL.get(String.valueOf(currDocID));
						if(currURL==null) continue;
						//word-frequency
						String curr_set_of_featureID_N_wordFreqvalue=s2[1];
						// URL PRESENT IN HUMAN ANNOTATOR ONLY to be CONSIDERED , other URLS not annotated by human (i.e., present in the file) is NOT CONSIDERED
						if(map_HumanAnnotator_URL_N_label.containsKey(currURL)){
							//automated ground truth "true positive"
							if(map_automated_groundTruth_truePOSITIVEdocID_as_KEY.containsKey(currDocID) ||
								map_HumanAnnotator_URL_N_label.get(currURL).equals("1")
									){
								writer2.append( "+1 "+curr_set_of_featureID_N_wordFreqvalue+"\n");
								//preprocess for maxent/weka
								writer3_maxent.append(curr_set_of_featureID_N_wordFreqvalue+" 1"+"\n");
								//NAIVE BAYES CPP (TP=1)
								writer_NB_CPP_wfreq.append( "1 "+curr_set_of_featureID_N_wordFreqvalue+"\n");
							}
							//automated ground truth "true negative" OR HUMAN ANNOTATOR
							else if(!map_automated_groundTruth_truePOSITIVEdocID_as_KEY.containsKey(currDocID) ||
									map_HumanAnnotator_URL_N_label.get(currURL).equals("0")){
								writer2.append( "-1 "+curr_set_of_featureID_N_wordFreqvalue+"\n");
								//preprocess for maxent/weka
								writer3_maxent.append(curr_set_of_featureID_N_wordFreqvalue+" 0"+"\n");
								//NAIVE BAYES CPP (TN=0)							
								writer_NB_CPP_wfreq.append( "0 "+curr_set_of_featureID_N_wordFreqvalue+"\n");
							}
						}
						writer2.flush();
						writer3_maxent.flush();
						writer_NB_CPP_wfreq.flush();
					}
				
			} //if(flag==4){
			//******END   ****Step 7:******** TF-IDF creation/
			
			//need manual change (NOW automated, DOES NOT NEED manual change)
			int max_feature_index=47739;//NUMBER of features only excluding label
			//read max_feature_index from file.
			BufferedReader reader5 = new BufferedReader(new FileReader(file_for_writer_maxwordid));
			String line="";
			//read the only line and set it for max feature index
			while ((line = reader5.readLine()) != null) {
				max_feature_index=Integer.valueOf(line);
			}
			
			line="";
			//WORD-FREQ_BASED
			String output_WEKA1=fileName_maxent+"_HEADER4_WEKA.txt";
			String output_WEKA2=fileName_maxent+"_HEADER4_WEKA2.txt";
			//TF-IDF BASED
			String output_WEKA1_TFIDF=fileName_maxent+"_HEADER4_WEKA_TFIDF.txt";
			String output_WEKA2_TFIDF=fileName_maxent+"_HEADER4_WEKA2_TFIDF.txt";
			
			//****Begin************Step 8 - Prepare sparse (zero) data file *****/
			if(flag==5){
				// COMMENT/UNCOMMENT WORD-FREQUENCY as required
				// START --- WORD-FREQUENCY BASED  -- WEKA FILE
				FileWriter writer_header = new FileWriter(output_WEKA1);
				FileWriter writer_header2 = new FileWriter(output_WEKA2);
				// get MAX FEATURE ID
				int max_feature_IDX=calc_N_return_max_feature_IDX(fileName_maxent);				
//				boolean is_having_header=true;
//
//				//prepare sparse file, label is the last column. can be used for iitb.MaxentClassifier
//				convert_featureFile_to_sparseFeatureFile(
//														fileName_maxent,  // input
//														baseFolder+intermediate_folder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt", //OUTPUT
//														max_feature_IDX, //max_feature_index, //NUMBER of features only excluding label
//														true, //is_include_quotes_4_label
//												 		false //isSOPprint
//													);
//				
//				/// BEING TEMPORARY COMMENTED - LENIN
//				// create header for WEKA file.
//				int cnt20=1; String header="";
//				while(cnt20<=max_feature_IDX){
//					if(header.length()==0){
//						header=String.valueOf(cnt20);
//					}
//					else{
//						header=header+","+String.valueOf(cnt20);
//					}
//					cnt20++;
//				}
//				//add label header at the end. 
//				header=header+","+"label";
//				//write header to output file.
//				writer_header.append(header+"\n");
//				
//				writer_header2.append(header+"\n");
//				//read the file just created by convert_featureFile_to_sparseFeatureFile() above 
//				BufferedReader reader4 = new BufferedReader(new FileReader(baseFolder+intermediate_folder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt"));
//				//each line
//				while ((line = reader4.readLine()) != null) {
//					writer_header.append(line+"\n");
//					writer_header.flush();
//					// another format, label at the end is NOT numeric, instaead categorical for the WEKA
//					String AllFeatures_excludeLabel=line.substring(0, line.lastIndexOf(","));
//					String [] arr=line.split(",");
//					int label=Integer.valueOf(  arr[arr.length-1].replace("\"", "") );
//					if(label==1)
//						writer_header2.append(AllFeatures_excludeLabel+",yes"+"\n");
//					else if(label==0)
//						writer_header2.append(AllFeatures_excludeLabel+",no"+"\n");
//					
//					writer_header2.flush();
//				}
//			 
//				/// END TEMPORARY COMMENTED - LENIN
//				
//				//VERIFICATION if all lines have same number of tokens for that INPUT FILE TO WEKA. (temporary commented
//				readFile_verify_and_Correct_No_Of_Tokens_In_EachLine.
//				readFile_only_verify_No_Of_Tokens_In_EachLine
//														 (max_feature_IDX+1, //noTokensNeeded
//														 ",", // delimiter  
//														 output_WEKA1,  
//														 output_WEKA1+"_notMATCHED_debug.txt",
//														 output_WEKA1+"_MATCHED_debug.txt",
//														 output_WEKA1+"_DEBUG.txt",
//										 	 			 true, //is_flag_strictly_lesserORequal_noTokensNeeded
//										 	 			 true, //is_add_lineNo_at_end
//										 	 			 false //isSOPprint
//										 	 			 );
//				
//				System.out.println("-->check which one right max_feature_IDX:"+max_feature_IDX+" max_feature_index:"+max_feature_index);
				
				
				// END --- WORD-FREQUENCY BASED -- WEKA FILE
				
				
				// START --- TF-IDF BASED -- WEKA FILE
				FileWriter writer_header_2 = new FileWriter(output_WEKA1_TFIDF);
				FileWriter writer_header2_2 = new FileWriter(output_WEKA2_TFIDF);
				// get MAX FEATURE ID
				max_feature_IDX=calc_N_return_max_feature_IDX(file_TFIDF_based_with_LABEL);				
				boolean is_having_header_2=true;

				//prepare sparse file, label is the last column. can be used for iitb.MaxentClassifier
				convert_featureFile_to_sparseFeatureFile(
														file_TFIDF_based_with_LABEL,  // input
														baseFolder+intermediate_folder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt_added_LABEL_TFIDF.txt", //OUTPUT
														max_feature_IDX, //max_feature_index, //NUMBER of features only excluding label
														true, // is_include_quotes_4_label
												 		false //isSOPprint
													);
				
				/// BEING TEMPORARY COMMENTED - LENIN
				// create header for WEKA file.
				int cnt20_2=1; String header_2="";
				while(cnt20_2<=max_feature_IDX){
					if(header_2.length()==0){
						header_2=String.valueOf(cnt20_2);
					}
					else{
						header_2=header_2+","+String.valueOf(cnt20_2);
					}
					cnt20_2++;
				}
				//add label header at the end. 
				header_2=header_2+","+"label";
				//write header to output file.
				writer_header_2.append(header_2+"\n");
				
				writer_header2_2.append(header_2+"\n");
				//read the file just created by convert_featureFile_to_sparseFeatureFile() above 
				BufferedReader reader4_2 = new BufferedReader(new FileReader(baseFolder+intermediate_folder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt_added_LABEL_TFIDF.txt"));
				int num_lines_=0;int error_on_label_expected=0;
				//each line
				while ((line = reader4_2.readLine()) != null) {
					num_lines_++;
					writer_header_2.append(line+"\n");
					writer_header_2.flush();
					// another format, label at the end is NOT numeric, instead categorical for the WEKA
					String AllFeatures_excludeLabel=line.substring(0, line.lastIndexOf(","));
					String [] arr=line.split(",");
					int label=Integer.valueOf(  arr[arr.length-1].replace("\"", "") );
					// -->1= biased; 0= not biased; 
					if(label==1)
						writer_header2_2.append(AllFeatures_excludeLabel+",no"+"\n");
					else if(label==0 || label==-1)
						writer_header2_2.append(AllFeatures_excludeLabel+",yes"+"\n"); // <--for "p18" , yes=NOT biased
					else{
						System.out.println("ERROR: UNEXPECTED label: "+label);
						error_on_label_expected++;
					}
					
					writer_header2_2.flush();
				}
				//verify number of tokens
				ReadFile_verify_and_Correct_No_Of_Tokens_In_EachLine.
				readFile_only_verify_No_Of_Tokens_In_EachLine
														 (max_feature_IDX+1, //noTokensNeeded
														 ",", // delimiter  
														 output_WEKA1_TFIDF,  
														 output_WEKA1_TFIDF+"_notMATCHED_debug.txt",
														 output_WEKA1_TFIDF+"_MATCHED_debug.txt",
														 output_WEKA1_TFIDF+"_DEBUG.txt",
										 	 			 true, //is_flag_strictly_lesserORequal_noTokensNeeded
										 	 			 true, //is_add_lineNo_at_end
										 	 			 false //isSOPprint
										 	 			 );
				System.out.println("num_lines_ processed from input file before changing label to varchar(yes/no)->"+num_lines_
										+" error_on_label_expected:"+error_on_label_expected);
				
				// END 	 --- TF-IDF BASED -- WEKA FILE
				
			}
			
			//****End**************Step 8 - Prepare sparse (zero) data file *****/
			

			//****Begin************Step 9 - 2-fold cross-validation- split one into two******************************//
			if(flag==6){
				
			//String 	inFile, int  number_of_output_File,int 	index_having_label_4_inFile,String	delimiter_4_inFile,String  label_for_TP,String  label_for_TN
				
				//split WORD-FREQUENCY based file into 2 files(sets) - must work for SVM LIGHT
				ReadFile_Split_One_into_Many_by_Size.
				split_by_line_Count_2FoldcrossValidation(inputFile_noStopWord_WORD_ID_format2+"2_added_LABEL.txt", 2, 
															1," ", "+1", "-1" );
				
				//NAIVE BAYES(REM)- MAKE A COPY 2 SPLIT FILE FROM ABOVE METHOD , AND RENAME THE 2 FILES FOR NAIVE BAYES INPUT
				//Fold 1 - pair 
				FileUtils.copyFile(new File(inputFile_noStopWord_WORD_ID_format2+"2_added_LABEL.txt.SET.1..txt"),
								   new File(baseFolder_for_perceptron+ "preprocessed_training_data_1.txt"));				
				FileUtils.copyFile(new File(inputFile_noStopWord_WORD_ID_format2+"2_added_LABEL.txt.SET.2..txt"),
						   		   new File(baseFolder_for_perceptron+ "preprocessed_test_data_1.txt"));
				//Fold 2 - pair
				FileUtils.copyFile(new File(inputFile_noStopWord_WORD_ID_format2+"2_added_LABEL.txt.SET.1..txt"),
								   new File(baseFolder_for_perceptron+ "preprocessed_training_data_2.txt"));				
				FileUtils.copyFile(new File(inputFile_noStopWord_WORD_ID_format2+"2_added_LABEL.txt.SET.2..txt"),
						   		   new File(baseFolder_for_perceptron+ "preprocessed_test_data_2.txt"));				
				
				//split TF-IDF based file into 2 files (sets) - must work for SVM LIGHT
				ReadFile_Split_One_into_Many_by_Size.
				split_by_line_Count_2FoldcrossValidation(inputFile_noStopWord_WORD_ID_format2+"_added_LABEL.txt", 2
														 ,1," ", "+1", "-1" );
				
				// split word-frequency based file into 2 files (sets) NAVIE BAYES CPP program
				// NOTE: TP=1 and TN=2
				ReadFile_Split_One_into_Many_by_Size.
				split_by_line_Count_2FoldcrossValidation(inputFile_noStopWord_WORD_ID_format2+"2_added_LABEL_NB_CPP.txt", 2
														 ,1," ", "1", "0" );
				
				int index_for_label=(max_feature_index+1);
				//split word-frequency base file into 2 files (set) for MAXENT input
				ReadFile_Split_One_into_Many_by_Size.
				split_by_line_Count_2FoldcrossValidation(baseFolder+intermediate_folder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt", 2
														,index_for_label,",", "1", "0" );
				/// new format:<label> <feat1>\t<feat2>
				//format2
				String above_split_SET1=baseFolder+intermediate_folder
										+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt.SET.1..txt";
				String above_split_SET2=baseFolder+intermediate_folder
										+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt.SET.2..txt";
				String OUT_above_split_SET1=baseFolder+intermediate_folder
										+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt.format2.SET.1..txt";
				String OUT_above_split_SET2=baseFolder+intermediate_folder
										+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt.format2.SET.2..txt";
				// convert_to_formate2_TABdelimiter_labelFirst (SET1)
				convert_to_formate2_TABdelimiter_labelFirst(above_split_SET1,
															OUT_above_split_SET1
															);
				// convert_to_formate2_TABdelimiter_labelFirst (SET2)
				convert_to_formate2_TABdelimiter_labelFirst(above_split_SET2,
															OUT_above_split_SET2
															);
				
				//// BELOW is just JUNK..
//				//merge two formate files, so that it can be used for 	WEKA.. generate header also
//				generate_header_for_WEKA_file.
//				merge_two_files(	baseFolder,
//									above_split_SET1,
//									above_split_SET2,
//									baseFolder+"WEKA_input1.txt"
//								);
//				
//				//merge two formate2 files, so that it can be used for 	WEKA.. generate header also
//				generate_header_for_WEKA_file.
//				merge_two_files(	baseFolder,
//									OUT_above_split_SET1,
//									OUT_above_split_SET2,
//									baseFolder+"WEKA_input2.txt"
//								);
//				
//				System.out.println("**INPUT file for WEKA is located in "+baseFolder+"WEKA_input.txt");
				
			} // if(flag==6){
			
			//****End************Step 9 - 2-fold cross-validation- split one into two******************************//
			System.out.println("(to be noted)flag=3, maxwordid:"+maxwordid);
			
			FileWriter writer_iitb_maxent_config_fold1=null; 
			//****Begin************Step 10 - Run (1) iitb.MaxentClassifier (2) Linear Perceptron ******************************//
			if(flag==7){
				// Step 1: START run MAXENT classifier run
				//comment and uncomment as required.
				args=new String[1];
				// need manual change 
				args[0]=workspace_basefolder+"crf.iitb/samples/"+problem_prefix+".1.conf";
				String workspace_basefolder_for_crf_iitb=workspace_basefolder+"crf.iitb/";
				//prepare config file for maxent classifier run.
				writer_iitb_maxent_config_fold1 = new FileWriter(args[0]);
				
				String train_set=baseFolder+intermediate_folder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt.SET.1..txt";
				String test_set=baseFolder+intermediate_folder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt.SET.2..txt";
				//copy the above two files into folder under samples/
				// training and test copy
				FileUtils.copyFile(new File(train_set),
								   new File(workspace_basefolder+"crf.iitb/samples/data/"+problem_prefix+"/"+problem_prefix+".train.tagged"));				
				FileUtils.copyFile(new File(test_set),
						   		   new File(workspace_basefolder+"crf.iitb/samples/data/"+problem_prefix+"/"+problem_prefix+".test.tagged"));
				
				//2-FOLD CROSS VALIDAITON, FOLD-1 conf create .
				writer_iitb_maxent_config_fold1.append("basedir=samples/"+"\n");
				writer_iitb_maxent_config_fold1.append("numlabels=2"+"\n");
				writer_iitb_maxent_config_fold1.append("inname="+problem_prefix+"\n");
				writer_iitb_maxent_config_fold1.append("outdir="+problem_prefix+"\n");
				writer_iitb_maxent_config_fold1.append("delimit=,"+"\n");
				writer_iitb_maxent_config_fold1.append("impdelimit=,"+"\n");
			    writer_iitb_maxent_config_fold1.append("numColumns="+max_feature_index+"\n"); //number of features.
			    writer_iitb_maxent_config_fold1.append("trainFile=samples/data/"+problem_prefix+"/"+problem_prefix+".train.tagged"+"\n");
			    writer_iitb_maxent_config_fold1.append("testFile=samples/data/"+problem_prefix+"/"+problem_prefix+".test.tagged"+"\n");
			    writer_iitb_maxent_config_fold1.append("modelGraph=noEdge"+"\n");
			    writer_iitb_maxent_config_fold1.append("model-args=bcrf"+"\n");
			    writer_iitb_maxent_config_fold1.append("maxMemory=2"+"\n");
			    writer_iitb_maxent_config_fold1.append("iflag=2 %may or may not be needed%"+"\n");
			    writer_iitb_maxent_config_fold1.append("trainer=ll"+"\n");
			    writer_iitb_maxent_config_fold1.flush();
				//RUN FOR FOLD-1
			    Options opts = new Options(args);
			    MaxentClassifier maxent = new MaxentClassifier(opts);
			    System.out.println(args.length+" "+opts.getMandatoryProperty("trainFile")
			    				+" "+ new File(workspace_basefolder_for_crf_iitb+opts.getMandatoryProperty("trainFile")).getAbsolutePath());
			    
			    //train and test are public 2 be accessed here.
			    //**** comment and uncommentworkspace_basefolder_for_crf_iitb as required.
			    maxent.train(workspace_basefolder_for_crf_iitb + opts.getMandatoryProperty("trainFile"));

			    System.out.println("Finished training...Starting test");
			    maxent.test(workspace_basefolder_for_crf_iitb+opts.getMandatoryProperty("testFile"));
				
				//2-FOLD CROSS VALIDAITON, FOLD-2 conf create .
				args[0]=workspace_basefolder+"crf.iitb/samples/"+problem_prefix+".2.conf";
				//prepare config file for maxent classifier run.
				writer_iitb_maxent_config_fold1 = new FileWriter(args[0]);
				//2-FOLD CROSS VALIDAITON, FOLD-2 conf create.
				writer_iitb_maxent_config_fold1.append("basedir=samples/"+"\n");
				writer_iitb_maxent_config_fold1.append("numlabels=2"+"\n");
				writer_iitb_maxent_config_fold1.append("inname="+problem_prefix+"\n");
				writer_iitb_maxent_config_fold1.append("outdir="+problem_prefix+"\n");
				writer_iitb_maxent_config_fold1.append("delimit=,"+"\n");
				writer_iitb_maxent_config_fold1.append("impdelimit=,"+"\n");
			    writer_iitb_maxent_config_fold1.append("numColumns="+max_feature_index+"\n"); //number of features.
			    writer_iitb_maxent_config_fold1.append("trainFile=samples/data/"+problem_prefix+"/"+problem_prefix+".test.tagged"+"\n");
			    writer_iitb_maxent_config_fold1.append("testFile=samples/data/"+problem_prefix+"/"+problem_prefix+".train.tagged"+"\n");
			    writer_iitb_maxent_config_fold1.append("modelGraph=noEdge"+"\n");
			    writer_iitb_maxent_config_fold1.append("model-args=bcrf"+"\n");
			    writer_iitb_maxent_config_fold1.append("maxMemory=2"+"\n");
			    writer_iitb_maxent_config_fold1.append("iflag=2 %may or may not be needed%"+"\n");
			    writer_iitb_maxent_config_fold1.append("trainer=ll"+"\n");
			    writer_iitb_maxent_config_fold1.flush();
			    
				//RUN FOR FOLD-2
			    opts = new Options(args);
			    maxent = new MaxentClassifier(opts);
			    System.out.println(args.length+" "+opts.getMandatoryProperty("trainFile")
			    					+" "+ new File(workspace_basefolder_for_crf_iitb+opts.getMandatoryProperty("trainFile")).getAbsolutePath());
			    
			    //train and test are public 2 be accessed here.
			    //**** comment and uncomment as required.
			    maxent.train(workspace_basefolder_for_crf_iitb + opts.getMandatoryProperty("trainFile"));
			    System.out.println("Finished training...Starting test");
			    maxent.test(workspace_basefolder_for_crf_iitb+opts.getMandatoryProperty("testFile"));

			    System.out.println("OUTPUT OF maxent in the Downloads Folder ***************");
			    }
				// Step 1: END run MaxEnt classifier run

				// Step 2: START run Linear Perceptron classifier ( need manual change )
			    System.out.println("baseFolder_for_perceptron:"+baseFolder_for_perceptron);
			    System.out.println("-------------Running LINEAR PERCEPTRON----------------");
			    /// 
			    if(flag==8){
			    	
			    	Main main=new Main();
					//comment and uncomment as required.
				    //running LINEAR PERCEPTRON (pass input files)
			    	main.wrapper_to_run_(baseFolder_for_perceptron);
			    	 
			    }
				// Step 2: END run Linear Perceptron classifier ( need manual change )
			    
			    // Step 3: run SVM classifier (SVM light)
			    // generate debug_ReadME.txt file for run command
				//2-fold input for SVM located in below files
				System.out.println( " ****************************************** ");
				System.out.println( " SVM 2-FOLDER FILES BELOW **start");
				System.out.println( " SVM FOLD1(train):" +baseFolder_for_perceptron+ "preprocessed_training_data_1.txt");
				System.out.println( " SVM FOLD1(test):" +baseFolder_for_perceptron+ "preprocessed_test_data_1.txt");
				System.out.println( " SVM FOLD2(train):" +baseFolder_for_perceptron+ "preprocessed_training_data_2.txt");
				System.out.println( " SVM FOLD2(test):" +baseFolder_for_perceptron+ "preprocessed_test_data_2.txt");
				System.out.println( " SVM 2-FOLDER FILES BELOW **end");
				System.out.println( "-------------------------------------");
				System.out.println( " SVM - Run BELOW Commands FOR fold-1...");
				System.out.println( "-------------------------------------");
				System.out.println("1) cd "+"/Users/lenin/Downloads/Google\\ Drive/\\#\\#\\ \\*\\*\\*\\*\\ Machine\\ Learning\\ Tools/SVM/svm_light/");
//				System.out.println("2) ./svm_learn  -e 0.0001 -t 1 ../../\\#problems/p18/tf-idf.txt_noStopWord_WORD_ID2.txt_format2.txt2_added_LABEL.txt.SET.1..txt ../../#problems/p18/model2.txt" );
//				System.out.println("3) ./svm_classify  ../../\\#problems/p18/tf-idf.txt_noStopWord_WORD_ID2.txt_format2.txt2_added_LABEL.txt.SET.2..txt ../../\\#problems/p18/model2.txt ../../\\#problems/p18/output.txt");
				System.out.println("2) ./svm_learn  -e 0.0001 -t 0 "+baseFolder_for_perceptron+"preprocessed_training_data_1.txt"+" ../../#problems/p18/modelf1.txt" );
				System.out.println("3) ./svm_classify  "+baseFolder_for_perceptron+"preprocessed_test_data_1.txt"+" ../../\\#problems/p18/modelf1.txt ../../\\#problems/p18/outputf1SVM.txt");
				System.out.println( "-------------------------------------");
				System.out.println( " SVM - Run BELOW Commands FOR fold-2...");
				System.out.println( "-------------------------------------");
				System.out.println("4) ./svm_learn  -e 0.0001 -t 0 "+baseFolder_for_perceptron+"preprocessed_training_data_2.txt"+" ../../#problems/p18/modelf2.txt" );
				System.out.println("5) ./svm_classify  "+baseFolder_for_perceptron+"preprocessed_test_data_2.txt"+" ../../\\#problems/p18/modelf2.txt ../../\\#problems/p18/outputf2SVM.txt");
				
				///Users/lenin/Downloads/Google\ Drive/\\#\\#\\ \\*\\*\\*\\*\\ Machine\\ Learning\\ Tools/SVM/svm_light/
			    
				// Step 4: run Naive Bayes (CPP) classifier - input similar to SVM LIGHT 
			    // generate debug_ReadME.txt file for run command
				System.out.println( " ****************************************** ");
				System.out.println( " NB CPP 2-FOLDER FILES BELOW **start");
				System.out.println( " NB CPP FOLD1:" +inputFile_noStopWord_WORD_ID_format2+"2_added_LABEL_NB_CPP.txt.SET.1..txt");
				System.out.println( " NB CPP FOLD2:" +inputFile_noStopWord_WORD_ID_format2+"2_added_LABEL_NB_CPP.txt.SET.2..txt");
				System.out.println( " NB CPP 2-FOLDER FILES BELOW **end");
				System.out.println( "-------------------------------------");
				System.out.println( " NB - Run BELOW Commands FOR fold-1...");
				System.out.println( "-------------------------------------");
				System.out.println( "1) cd /Users/lenin/Downloads/Google\\ Drive/\\#\\#\\ \\*\\*\\*\\*\\ Machine\\ Learning\\ Tools/openpr-nb_v1.12/");
				System.out.println( "2) ./nb_learn -e 1 "+baseFolder_for_perceptron+"preprocessed_training_data_1.txt"+"  ../#problems/p18/nbmodelFOLD1.txt");
				System.out.println( "3) ./nb_classify -e 1 "+baseFolder_for_perceptron+"preprocessed_test_data_2.txt"+"  ../#problems/p18/nbmodelFOLD1.txt"
														 +" ../#problems/p18/nb_output_fold1.txt >> ../#problems/p18/nb_FOLD1_output_output.txt");
				System.out.println( "-------------------------------------");
				System.out.println( " NB - Run BELOW Commands FOR fold-2...");
				System.out.println( "-------------------------------------");
				System.out.println( "4) ./nb_learn -e 1 "+baseFolder_for_perceptron+"preprocessed_training_data_2.txt"+"  ../#problems/p18/nbmodelFOLD2.txt");
				System.out.println( "5) ./nb_classify -e 1 "+baseFolder_for_perceptron+"preprocessed_test_data_1.txt"+"  ../#problems/p18/nbmodelFOLD2.txt"
//													+"  >> ../#problems/p18/nb_FOLD2_output_output.txt");
														 +" ../#problems/p18/nb_output_fold2.txt >> ../#problems/p18/nb_FOLD2_output_output.txt");
				
				System.out.println( "-------------------------------------");
				System.out.println( " MaxentClassifier - ...");
				System.out.println( "-------------------------------------");
				System.out.println( " MaxentClassifier already RAN as part of Flag==7 ");
				
				System.out.println( "-------------------------------------");
				System.out.println( "Input file for WEKA (Flag==5)");
				System.out.println( "-------------------------------------");
				System.out.println("first file has label 1 or 0; second file is either yes or no");
				System.out.println("Below files can be input to WEKA  (Flag==5)->");
				System.out.println( "FREQUENCY-BASED WEKA FILES");
				System.out.println("output_WEKA1:"+output_WEKA1);
				System.out.println("output_WEKA2:"+output_WEKA2);
				System.out.println( "TF-IDF-BASED WEKA FILES");
				System.out.println("output_WEKA1_TFIDF:"+output_WEKA1_TFIDF);
				System.out.println("output_WEKA2_TFIDF:"+output_WEKA2_TFIDF);
				System.out.println( "-------------------------------------");
				
				System.out.println( "This the TF-IDF file ---> tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt");
				System.out.println( " ****************************************** ");

			//****Begin************Step 10 - Run (1) Linear Perceptron (2) iitb.MaxentClassifier ******************************//
			

			//****Begin************Step 11 - convert <feature:frequency> file to k-means input file for weka ******************************//
			if(flag==8){
				 
				// outFile+"_format2.txt" //tf-idf file 
				
//				 //NOT NEEDED: OBSOLETE converting  <feature:freq> to <feature:tf-idf>
//				convert_doc_word_FREQ_file_to_doc_word_TFIDF.convert_doc_word_FREQ_file_to_doc_word_TFIDF(
//																						baseFolder+intermediate_folder, 
//																						fileName_maxent,
//																						fileName_maxent+"_#KMEANS.txt"
//																						);
				
				
					//convert_featureFreq_2_kmeansINPUT4weka (commented as not important for p18
//					convert_featureFreq_2_kmeansINPUT4weka(
//														    fileName_maxent,
//														    outFile_tf_idf_NOSTOPWORDS_format2,
//															baseFolder+intermediate_folder+"_#KMEANS.csv"
//															);
					
					writeDebug.append("\n K-MEANS file for weka->"+baseFolder+intermediate_folder+"_#KMEANS.csv");
					writeDebug.flush();
			}
			//****End************Step 11 - convert <feature:frequency> file to k-means input file for weka ******************************//
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mapOut;
	}
	
	// merge_change_format_tfidf_add_label
	private static void merge_change_format_tfidf_add_label(	String inputFile_noStopWord_WORD_ID_format2,//input_1
																String file1_human_annotator_LABELED,  //input_2
																int    index_having_LABEL_in_inputFile2,
																boolean is_HEADER_present_in_input1File,
																boolean is_HEADER_present_in_input2File,
																String output //output
															) {
		// TODO Auto-generated method stub
		try{
			
			FileWriter writer=new FileWriter(output);
			
			  TreeMap<Integer, String>  map_seq_eachLine_input1=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
												inputFile_noStopWord_WORD_ID_format2, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  TreeMap<Integer, String>  map_seq_eachLine_input2=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
										.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																										 file1_human_annotator_LABELED, 
																									 	 -1, //startline, 
																										 -1, //endline,
																										 " loading", //debug_label
																										 false //isPrintSOP
																										 );
			  ////////// input1
			  //lineNo here is a token from inputFile1
			  TreeMap<Integer,String> map_lineNo_featIDnVALUE_from_INPUT1=new TreeMap<Integer, String>();
			  //read inputfile1
			  for(int seq:map_seq_eachLine_input1.keySet()){
				  if(is_HEADER_present_in_input1File && seq==1){
					  continue;
				  }
				  
				  String [] arr_file1=map_seq_eachLine_input1.get(seq).split("!!!");
				  map_lineNo_featIDnVALUE_from_INPUT1.put(Integer.valueOf(arr_file1[0]), arr_file1[1]);
			  }
			  ////////// input2
			  //lineNo here is actual lineNO from inputFile2
			  TreeMap<Integer, Integer> map_lineNo_LABEL_from_INPUT2=new TreeMap<Integer, Integer>();
			  int currLABEL=-1;
			  //read inputfile2
			  for(int seq:map_seq_eachLine_input2.keySet()){
				  if(is_HEADER_present_in_input2File && seq==1){
					  continue;
				  }
				  try{
				   currLABEL=Integer.valueOf(map_seq_eachLine_input2.get(seq).split("!!!")[index_having_LABEL_in_inputFile2-1]);
				  }
				  catch(Exception e){
					  e.printStackTrace();
				  }
				  map_lineNo_LABEL_from_INPUT2.put(seq, currLABEL);
			  }			  
			  //
			  //iterate and add label
			  for(int currLineNo:map_lineNo_featIDnVALUE_from_INPUT1.keySet()){
				  writer.append(map_lineNo_featIDnVALUE_from_INPUT1.get(currLineNo) +" "+map_lineNo_LABEL_from_INPUT2.get(currLineNo)+"\n");
				  writer.flush();
			  }
			  
			  
			
		}
		catch(Exception e){
			
		}
		
	}
	//convert_to_formate2_TABdelimiter_labelFirst
	private static void convert_to_formate2_TABdelimiter_labelFirst(
																	String above_split_SET1, //input
																	String oUT_above_split_SET1 //out
																	){
		// TODO Auto-generated method stub
		try{
			String line="";
			BufferedReader reader1 = new BufferedReader(new FileReader(above_split_SET1));
			FileWriter writer=new FileWriter(new File(oUT_above_split_SET1));
//			TreeMap<String, Integer> map_file2_URL_docID=new TreeMap<String, Integer>();
			
			//read file2 first
			while ((line = reader1.readLine()) != null) {
				String []_arr=line.split(",");
				int last_comma_index=line.lastIndexOf(",");
				String all_featureS_excludeLabel=line.substring(0, last_comma_index);
//				System.out.println("-label string:"+all_featureS_excludeLabel);
				String new_label="";
				//
				if(Integer.valueOf( _arr[_arr.length-1] ) == 1){
					new_label="+1";
					new_label="yes";
				}
				else if(Integer.valueOf( _arr[_arr.length-1] ) == 0){
					new_label="-1";
					new_label="no";
				}
				
//				String temp=all_featureS_excludeLabel.replace(",", "\t").replace("\t"+"50"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"49"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"48"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"47"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"46"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"45"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"44"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"43"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"42"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"41"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"39"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"38"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"37"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"36"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"35"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"34"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"33"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"32"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"31"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"30"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"29"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"28"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"27"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"26"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"25"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"24"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"23"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"22"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"21"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"20"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"19"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"18"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"17"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"16"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"15"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"14"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"13"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"12"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"11"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"10"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"9"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"8"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"7"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"6"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"5"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"4"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"3"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"2"+"\t", "\t"+"1"+"\t")
//						.replace("\t"+"1"+"\t", "\t"+"1"+"\t");
//				
//				temp=temp.replace("\t"+"50", "\t"+"1")
//						.replace("\t"+"49", "\t"+"1")
//						.replace("\t"+"48", "\t"+"1")
//						.replace("\t"+"47", "\t"+"1")
//						.replace("\t"+"46", "\t"+"1")
//						.replace("\t"+"45", "\t"+"1")
//						.replace("\t"+"44", "\t"+"1")
//						.replace("\t"+"43", "\t"+"1")
//						.replace("\t"+"42", "\t"+"1")
//						.replace("\t"+"41", "\t"+"1")
//						.replace("\t"+"39", "\t"+"1")
//						.replace("\t"+"38", "\t"+"1")
//						.replace("\t"+"37", "\t"+"1")
//						.replace("\t"+"36", "\t"+"1")
//						.replace("\t"+"35", "\t"+"1")
//						.replace("\t"+"34", "\t"+"1")
//						.replace("\t"+"33", "\t"+"1")
//						.replace("\t"+"32", "\t"+"1")
//						.replace("\t"+"31", "\t"+"1")
//						.replace("\t"+"30", "\t"+"1")
//						.replace("\t"+"29", "\t"+"1")
//						.replace("\t"+"28", "\t"+"1")
//						.replace("\t"+"27", "\t"+"1")
//						.replace("\t"+"26", "\t"+"1")
//						.replace("\t"+"25", "\t"+"1")
//						.replace("\t"+"24", "\t"+"1")
//						.replace("\t"+"23", "\t"+"1")
//						.replace("\t"+"22", "\t"+"1")
//						.replace("\t"+"21", "\t"+"1")
//						.replace("\t"+"20", "\t"+"1")
//						.replace("\t"+"19", "\t"+"1")
//						.replace("\t"+"18", "\t"+"1")
//						.replace("\t"+"17", "\t"+"1")
//						.replace("\t"+"16", "\t"+"1")
//						.replace("\t"+"15", "\t"+"1")
//						.replace("\t"+"14", "\t"+"1")
//						.replace("\t"+"13", "\t"+"1")
//						.replace("\t"+"12", "\t"+"1")
//						.replace("\t"+"11", "\t"+"1")
//						.replace("\t"+"10", "\t"+"1")
//						.replace("\t"+"9", "\t"+"1")
//						.replace("\t"+"8", "\t"+"1")
//						.replace("\t"+"7", "\t"+"1")
//						.replace("\t"+"6", "\t"+"1")
//						.replace("\t"+"5", "\t"+"1")
//						.replace("\t"+"4", "\t"+"1")
//						.replace("\t"+"3", "\t"+"1")
//						.replace("\t"+"2", "\t"+"1")
//						.replace("\t"+"1", "\t"+"1") ; 
				
				//
//				writer.append(new_label+"\t"+all_featureS_excludeLabel+"\n");
				writer.append(new_label+","+all_featureS_excludeLabel+"\n");
				writer.flush();
				
			} //while ((line = reader1.readLine()) != null) {
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//convert_featureFreq_2_kmeansINPUT4weka
	private static void convert_featureFreq_2_kmeansINPUT4weka(String inFile_maxent_feature_frequency,
															   String inFile_feature_tfidf,
															   String outFile
															   ) {
		
		TreeMap<Integer, Double> map_lineNoAKAdocID_Frequency = new TreeMap<Integer, Double>();
		TreeMap<Integer, Integer> map_lineNoAKAdocID_Label = new TreeMap<Integer, Integer>();
		TreeMap<Integer, Double> map_lineNoAKAdocID_TFIDF = new TreeMap<Integer, Double>();
		// TODO Auto-generated method stub
		try {
				
			TreeMap<Integer,String> map_firstfile_lineNo_primarykey =new TreeMap();
			TreeMap<Integer,String> map_secondfile_lineNo_primarykey =new TreeMap();
			FileWriter writer=new FileWriter(new File(outFile));
			 
			TreeMap<Integer, String> map_lineNo_Line_featureFrequency=
				ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
				readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
																(  inFile_maxent_feature_frequency, 
																   -1, // 	startline, 
																   -1, //  	endline,
																   " debug_label",
																   false // isPrintSOP
																);
			
			TreeMap<Integer, String> map_lineNo_Line_featureTFIDF=
					ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
					readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
																	(  inFile_feature_tfidf, 
																	   -1, // 	startline, 
																	   -1, //  	endline,
																	   "2 debug_label",
																	   false // isPrintSOP
																	);
		
			System.out.println("PROCESSING for TF-IDF....");
			// <label>!!!feat1:tfidfvalue feat2:tfidfvalue
			for(int lineNo:map_lineNo_Line_featureTFIDF.keySet()){
				String currLine=map_lineNo_Line_featureTFIDF.get(lineNo);
				//System.out.println("l20:"+currLine);
				
				String [] s =currLine.split("!!!");
				//
				String currLabel="";
				System.out.println("tfidf: lineno"+lineNo+" label:"+currLabel+" line:"+currLine);
				
				String [] s2 =s[0].split(" ");
				int cnt=0; 
				double avg_TFIDF=0.;double denominator_TFIDF=0.;
				int cnt2=0;
				//each feature:TFIDFvalue
				while(cnt < s2.length ){
					
					//if label , no : , hence skip
					if(s2[cnt].indexOf(":")==-1) {
						currLabel=s2[cnt];
						cnt++;continue;}
					
					String [] s3 =s2[cnt].split(":");
					
					double currFeature_tfidf_Value= Double.valueOf(s3[1]);
					  cnt2=0;
					// 
					if(currFeature_tfidf_Value>0.0){
						denominator_TFIDF=denominator_TFIDF+currFeature_tfidf_Value;
						cnt2++;
					}
					
					cnt++;
				}
				//average
				avg_TFIDF=denominator_TFIDF / (double) cnt2;
				map_lineNoAKAdocID_TFIDF.put(lineNo , avg_TFIDF);
				//LABEL
				map_lineNoAKAdocID_Label.put(lineNo, Integer.valueOf(currLabel)  );
			} //end for
			
			System.out.println("PROCESSING for WORD-FREQUENCY...."+map_lineNo_Line_featureFrequency.size());
			///// WORD FREQUENCY
			// <label>!!!feat1:FREQ feat2:FREQ
			for(int lineNo:map_lineNo_Line_featureFrequency.keySet()){
				System.out.println("wfreq:"+lineNo);
				String currLine=map_lineNo_Line_featureFrequency.get(lineNo);

				//NO !!! actually
				String [] s =currLine.split("!!!");
				//String currLabel=s[0];
				

				
				String [] s2 =s[0].split(" ");
				int cnt=0; double avg_frequency=0.;
				double denominator_freq=0.;
				int cnt2=0;
				//System.out.println("l22: 2.token.size:"+s.length +"--1.token.size:"+s2.length+ "--"+currLine);
				
				//each feature:TFIDFvalue
				while(cnt < s2.length ){
					//if label , no : , hence skip
					if(s2[cnt].indexOf(":")==-1)
						{ cnt++;continue;
						}
					
					String [] s3 =s2[cnt].split(":");
					
					double currFeature_freq_Value= Double.valueOf(s3[1]);
					  cnt2=0;
					// 
					if(currFeature_freq_Value>0.0){
						denominator_freq=denominator_freq+currFeature_freq_Value;
						cnt2++;
					}
					
					cnt++;
				}
				//System.out.println("l221.avgg");
				//average
				avg_frequency=denominator_freq / (double) cnt2;
				map_lineNoAKAdocID_Frequency.put(lineNo , avg_frequency);
 
			} //end for
			
			System.out.println("#####################################");
			System.out.println("GIVEN INPUT word-freq file:"+inFile_maxent_feature_frequency);
			System.out.println("GIVEN INPUT tfidf file:"+inFile_feature_tfidf);
			
			System.out.println("both should be equal-."+map_lineNo_Line_featureTFIDF.size()
												  +" "+map_lineNo_Line_featureFrequency.size());
			System.out.println("#####################################");
			
			writer.append("freqAvg,tfidfAvg,label\n"); writer.flush();
			//writing
			for(int lineNo:map_lineNo_Line_featureFrequency.keySet()){
				writer.append(
							  map_lineNoAKAdocID_Frequency.get(lineNo)+","+
							  map_lineNoAKAdocID_TFIDF.get(lineNo)+","+
							  "\""+
							  map_lineNoAKAdocID_Label.get(lineNo) +"\"" +"\n"
							 );
				writer.flush();
				
			}
			
			System.out.println("#####################################");
			System.out.println(" This file can be imported to weka (for kmeans:)"+outFile);
			System.out.println("#####################################");
			
			
		} catch (Exception e) {
			e.printStackTrace( );
			// TODO: handle exception
		}
		
	}

	// get_automated_groundTruth;
	// 2 INPUTS ->(1) file1->it is created from file2. We use some keyword filters with grep on file2.
	//Example: if any of "grep -i -E 'court|assembl|congres|colleg|univers|Institute|commissi|board'" present in file2 for a news article
	// or document (each line=one document in file2), then we write it to file1.
	// (2) file2 is complete (full) CRAWLED output file for a set of keywords (for issues such as abortion etc.)
	// file2-> comes from output file of readFile_readEachLine_removeDuplicates_WriteToAnotherFile() in P18_get_dupURL_N_domainNames()
	// OUTPUT=>automated Ground Truth "True Positive" Doc ID in each line.
	public static TreeMap<Integer, Integer> get_automated_groundTruth_truePOSITIVEdocID_as_KEY(
															String 	file1_automated_ground_truth_file_byGREP_from_file2fullCRAWLED_OUTPUT,
															int 	token_for_file1_having_url,
															String 	file2_full_OUTPUT_CRAWLED_file,//lineNO=docID for this file
															int	 	token_for_file2_having_url,
															String 	delimiter_for_file1_file2
															){
		String line="";int lineNumber=0;

		TreeMap<Integer, Integer>  map_truePositiveDocID_as_KEY=new TreeMap<Integer, Integer>();
		try {
			BufferedReader reader1 = new BufferedReader(new FileReader(file1_automated_ground_truth_file_byGREP_from_file2fullCRAWLED_OUTPUT));	
			BufferedReader reader2 = new BufferedReader(new FileReader(file2_full_OUTPUT_CRAWLED_file));
			TreeMap<String, Integer> map_file2_URL_docID=new TreeMap<String, Integer>();
			
			//read file2 first
			while ((line = reader2.readLine()) != null) {
				lineNumber++;//docID
				String []s2=line.split("!!!");
				String currURL=s2[token_for_file2_having_url-1];
				map_file2_URL_docID.put(currURL , lineNumber);
			}
			//read file1
			while ((line = reader1.readLine()) != null) {
				//lineNumber++;//docID
				String []s1=line.split("!!!");
				String currURL=s1[token_for_file1_having_url-1];
				//OUTPUT MAP
				map_truePositiveDocID_as_KEY.put(map_file2_URL_docID.get(currURL), -1); //-1 is dummy
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return map_truePositiveDocID_as_KEY;
	}
	// convert_featureFile_to_sparseFeatureFile
	private static void convert_featureFile_to_sparseFeatureFile(String  inputFile,
																 String  outputFile,
																 int     max_feature_index, // maximum no of index
																 boolean is_include_quotes_4_label,
																 boolean isSOPprint
																 ) {
		BufferedReader reader1 = null; String line=""; String concline_new="";
		FileWriter writer= null; FileWriter writer_debug= null;
		int lineNumber=0;
		int diff2=-1;
		// TODO Auto-generated method stub
		try {
			reader1 = new BufferedReader(new FileReader(inputFile));
			writer = new FileWriter(new File(outputFile));
			writer_debug = new FileWriter(new File(outputFile+"_DEBUG.txt"));
			int featureIdx=-1; int num_of_error=0;
			
			//
			while ((line = reader1.readLine()) != null) {
				lineNumber++;//docID
//				System.out.println("lineNumber:"+lineNumber);
				String []s1=line.split(" ");
				//label does not have :, other features have : 
				int c=0; int last_featureIdx=-1;
				concline_new="";
//				int last_frequency_OR_Tfidf=0;
				String last_frequency_OR_Tfidf="0";
				int curr_label = Integer.valueOf(s1[s1.length-1]);
				
				//
				while(c<s1.length){
					
					if( s1[c].indexOf(":") >=0){
						featureIdx=Integer.valueOf(s1[c].substring(0, s1[c].indexOf(":")));
						//this works for word-frequency, but not for double value in tf-idf
//						int frequency_OR_Tfidf=Integer.valueOf(s1[c].substring( s1[c].indexOf(":")+1, s1[c].length()) );
						
						String frequency_OR_Tfidf=s1[c].substring( s1[c].indexOf(":")+1, s1[c].length()) ;
						
						//after 1st token 
						if(c>0){
							int difference_btw_last_featureIdx_N_featureIdx=featureIdx-last_featureIdx;
							// DIFFERENCE in ONE
							if(difference_btw_last_featureIdx_N_featureIdx==1){
								//only one difference, straight forward case
								if(concline_new.length()==0){
									concline_new=String.valueOf(last_frequency_OR_Tfidf)+","+String.valueOf(frequency_OR_Tfidf);
								}
								else{
									concline_new=concline_new+","+
												 String.valueOf(frequency_OR_Tfidf);
								}
							}
							// more than 1 gap, so we have to fill it with zeros
							else{
								int cnt=1; String fill_with_zeros="";
								// filling with zero with ,
								while(cnt<difference_btw_last_featureIdx_N_featureIdx){
									
									if(cnt==1) 
										{ fill_with_zeros="0,";}
									else{ fill_with_zeros=fill_with_zeros+"0,"; }

									cnt++;
								}
								//only one difference, straight forward case
								if(concline_new.length()==0){
									concline_new=String.valueOf(last_frequency_OR_Tfidf)+","+ fill_with_zeros+
												 String.valueOf(frequency_OR_Tfidf);
								}
								else{
									concline_new=concline_new+","+
												  fill_with_zeros+
												 String.valueOf(frequency_OR_Tfidf);
									
								}
							}
							
						}
						else if(c==0){ //first token
							
							
						}
						if(isSOPprint)
							System.out.println("last featureID:"+last_featureIdx+"<->curr featureID:"+ featureIdx);
						
						last_frequency_OR_Tfidf=frequency_OR_Tfidf;
						last_featureIdx=featureIdx;
						
						if(isSOPprint)
							System.out.println("concline_new:"+concline_new);	
					}
					//label doesnt have :
					else {
						 
						if(isSOPprint)
							System.out.println("label:"+" "+concline_new.split(",").length);
					
					}
					c++;
				} // while(c<s1.length){
				// 
				if(isSOPprint)
					System.out.println("concline_new:"+concline_new +" featureIdx:"+featureIdx);
				
				//at the end , the maximum available feature id of a line (a document) might be 
				// lesser than the maximum feature globally. Example: a corpus might have 47738 unique words, each document may not contain
				// all of them. the last feature index will be lesser than 47738. we have to fill that with zero.
				diff2=max_feature_index-featureIdx;
				int cnt=1; String fill_with_zeros2="";
				// 
				if(diff2>1){
					// filling with zero with ,
					while(cnt<=diff2){
						
						if(cnt==1) 
							{ fill_with_zeros2="0,";}
						else{ fill_with_zeros2=fill_with_zeros2+"0,"; }
	
						cnt++;
					}
					//concline_new=concline_new.substring(0, concline_new.length());
				}
				if(fill_with_zeros2.length()>0){
					//remove last ,
					fill_with_zeros2=fill_with_zeros2.substring(0, fill_with_zeros2.length()-1);
					concline_new=concline_new +","+fill_with_zeros2;
					
					if(isSOPprint)
						System.out.println("fill_with_zeros2:"+fill_with_zeros2);
				}
				// LABEL
				if(concline_new.length()==0){
					if(is_include_quotes_4_label)
						concline_new="\""+String.valueOf(curr_label)+"\"";
					else
						concline_new=String.valueOf(curr_label);	
				}
				else{
					if(is_include_quotes_4_label)
						concline_new=concline_new+","+"\""+String.valueOf(curr_label)+"\"";	
					else
						concline_new=concline_new+","+String.valueOf(curr_label);
				}
				// error ON not matching 
//				if(concline_new.split(",").length != max_feature_index+1) {
				if(concline_new.split(",").length != max_feature_index+1) {
					System.out.println(" error:"+" lineNumber:"+lineNumber+" (actual)len:"+concline_new.split(",").length+
																	" expected:"+ ( max_feature_index+1)+" curr_label:"+curr_label+" concline_new:"+concline_new);
					//
					writer_debug.append(" error:"+" len:"+concline_new.split(",").length+ " expected:"+ ( max_feature_index+1)+
									" lineNumber:"+lineNumber +" concline_new:"+concline_new+"\n");
					writer_debug.flush();
					
					if(is_include_quotes_4_label)
						concline_new=concline_new+","+"\""+curr_label+"\"" ;// added last column - label as zero (manually verify this)
					else
						concline_new=concline_new+","+curr_label ;// added last column - label as zero (manually verify this)
					
					//WRITING to OUTPUT file
//					writer.append( concline_new +"\n");
//					writer.flush();
					// ?? still not fixed
					if(concline_new.split(",").length != max_feature_index+1) {
						System.out.println("AGAIN error:"+" len:"+concline_new.split(",").length+" lineNumber:"+lineNumber+"\n");
						writer_debug.append("AGAIN error:"+" len:"+concline_new.split(",").length+" lineNumber:"+lineNumber+"\n");
						writer_debug.flush();	
					}
					else{
						System.out.println("AGAIN NOerror:"+" len:"+concline_new.split(",").length+" lineNumber:"+lineNumber+"\n");
						writer_debug.append("AGAIN NOerror:"+" len:"+concline_new.split(",").length+" lineNumber:"+lineNumber+"\n");
					}
					num_of_error++;
				}
//				else{
//				System.out.println("concline_new:"+concline_new);
					//WRITING to OUTPUT file
					writer.append( concline_new +"\n");
					writer.flush();
//				}
				
			} //while ((line = reader1.readLine()) != null) {
			
			System.out.println("******NOTE:: num_of_error:"+num_of_error);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	// main
	public static void main(String[] args) {
		
		// NOTES:
		// (0) NO CONFIG FILE , search for "need manual change" in all the above methods to set right value
		// (1) search for "need manual change" where needed change
		// (2) After running this method, run wrapper_2_graphOUTfile_4_tag_for_person_organiz_locati()
		// (3) 

		//is_flag->1 = does first 4 steps (upto CRAWLING)
		//is_flag->2 = does WORK OF merge_2_inputFiles_for_full_bodyText() 
		//				- MERGES RSSFEED and OUTPUT_CRAWLED based on common URL match
		//is_flag->3 = TF-IDF creation
		//is_flag->4 = (Prerequisition: manually create "automated ground truth(true positive)" file ). 
		//			   Both  TF-IDF output from (flag==3) AND "automated ground truth(true positive)" merging as "single file".
		//				i.e., Create final file with format <label> <feat1:value1> <feat2:value2>
		//								  (0) Generate file1 using grep command on file2***
		//							  	  (1) get automated groundTruth truePOSITIVEdocID as KEY in a map/
		//								  (2) create final file with format <label> <feat1:value1> <feat2:value2>
		/// NOTE: CHECK debug FILE to see if numbers of features if ANY LINE there, correct it MANUALLY..
		//is_flag->5 =   CREATING SPARSE FILE without HEADER and WITH header (WEKA).  <-------this produces input file for WEKA 
		//				convert (a) word-frequency based file to sparse (zero) file for maxent classification run.
		//				 calling method "convert_featureFile_to_sparseFeatureFile()"
		//is_flag->6 =   split one label file into 2 files (2 fold cross validation) 
		//				 make sure variable "baseFolder_for_perceptron" is correctly set.
		//is_flag->7 = Run "maxent Classification" for 2-fold cross validation
		//is_flag->8 = Run "Linear Perceptron" for 2-fold cross validation
		int top_N_idf_flag4=3000;
		int is_flag=5;
		boolean is_only_top_N_idf_flag4=true;
		// P18_get_dupURL_N_domainNames
		TreeMap<Integer,String>  mapOut=P18_get_dupURL_N_domainNames(
																		 // (1) arg_1 -> run_type (2) arg_2 -> config_file
																		args, //either configure file given from args or its manually set (default:manually set)
																		is_flag, //is_flag
																		top_N_idf_flag4, //can be -1 ( only if is_only_top_N_idf_flag3=true) 
																		is_only_top_N_idf_flag4
																	);
		
		int max_feature_index=47739;
		//max_feature_index=30;
		//convert featureFile
//		convert_featureFile_to_sparseFeatureFile("/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/tmp/tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent.txt", 
//												 "/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/tmp/tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt",
//												 max_feature_index,
//												 false //isSOPprint
//												 );
		
		
		
	}

}
