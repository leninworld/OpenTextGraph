package crawler;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;

public class ReadFile_pick_a_Token_doNLPtagging_writeOUT {

	//readFile_pick_a_Token_doNLPtagging_writeOUT
	public static  void readFile_pick_a_Token_doNLPtagging_writeOUT(
																		String 		baseFolder,
																		int 		token_index_of_interest,
																		String 		inFile,
																		String 		outFile,
																		String      inFile_alreadyRan_pastTagging,
																		int    		default_start_lineNo,
																		String  	NLPfolder,
																		POSModel 	inflag_model2,
																		boolean 	is_overwrite_flag_with_flag_model,
																		boolean 	isSOPdebug
																		){
		BufferedReader reader = null;
		String line=""; int lineNo=0;
		
		if(default_start_lineNo>0)
			lineNo=default_start_lineNo;
		
		try{
			reader=new BufferedReader(new FileReader(inFile));
			FileWriter writer=new FileWriter(new File(outFile));
			
			FileWriter writer_debug=new FileWriter(new File(baseFolder+"debug__pick_a_Token_doNLPtagging_.txt"));
			 
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				lineNo++;
				String [] s= line.split("!!!");
				int cnt=0;
				String concLine="";
				System.out.println("lineNo:"+lineNo);
				// each token
				while(cnt < s.length){
					// interested token (from given input)
					if(cnt==token_index_of_interest-1){
						String tmp=s[cnt];
						
//						tmp=tmp.replace("Subject:", "");
						
						// calling NLP tagging for each line , interested token
						TreeMap<String, String> maptaggedNLP = Crawler.wrapper_getNLPTrained_en_pos_maxent(
																				"", //inputSingleURL_OR_input1, // give input_1 or input_2
																				tmp, //input_ExtractedText_OR_input2, 
																				NLPfolder,
																				"en-pos-maxent.bin", // Flag
																									// ={en-pos-maxent.bin,en-ner-person.bin,en-ner-organization.bin,en-ner-location.bin,en-ner-time.bin,en-ner-money.bin}
																				"", // flag2
																				isSOPdebug,
																				is_overwrite_flag_with_flag_model,
																				inflag_model2,
																				null, // inflag_model2_chunking,
																				baseFolder+"debugFileName.txt"
																				);
						//tagged NLP
						String tmp_NEW = maptaggedNLP.get("taggedNLP");
						
						// 
						if(concLine.length()==0){
							concLine=tmp+"!!!"+tmp_NEW;
						}
						else{
							concLine=concLine+"!!!"+tmp+"!!!"+tmp_NEW;
						}
						//
//						System.out.println("token for NLP:"+tmp+"-------tmp_NEW:"+tmp_NEW+"\n\n concLine:"+concLine);
						
//						writer_debug.append(lineNo+" :"+tmp+"-------tmp_NEW:"+tmp_NEW );
						writer_debug.flush();
					}
					else{
						if(concLine.length()==0){
							concLine=s[cnt];
						}
						else{
							concLine=concLine+"!!!"+s[cnt];
						}
					}
					
					//
				
					cnt++;
				}
				
				writer.append(concLine+"\n");
				writer.flush();
				
				// writing to debug
//				writer_debug.append("\n\n"+concLine+"\n");
				writer_debug.flush();
			}
			
			writer.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//
		System.out.println("\n input file ->"+inFile);
		System.out.println("output file ->"+outFile);
	}
	// main
    public static void main(String[] args) throws IOException {
    	
    	String baseFolder="/Users/lenin/Downloads/#problems/p18/ds1/Labeling/To_Maitrayi_set1/";
    	String inFile=baseFolder+"sikh_NO_us.txt";
    	String outFile=baseFolder+"OUT_sikh_NO_us.txt";
    	int token_position_to_insert_lineNumber=4;
    	String NLPfolder="/Users/lenin/OneDrive/jar/NLP/";
    	//ParserModel inflag_model2 
//		InputStream is = new FileInputStream(NLPfolder+"en-pos-maxent.bin");
//		ParserModel inflag_model2 = new ParserModel(is);
    	
		POSModel inflag_model2 = new POSModelLoader().load(new File(NLPfolder+ "en-pos-maxent.bin"));
    	boolean is_overwrite_flag_with_flag_model=true;
    	boolean isSOPdebug=false;
    	
    	// readFile_add_lineNo_to_a_given_Nth_token_position
    	readFile_pick_a_Token_doNLPtagging_writeOUT(
    														baseFolder,
    														token_position_to_insert_lineNumber,
    														inFile,
    														outFile,
    														"", //inFile_alreadyRan_pastTagging
    														0, //default_start_lineNo
    														NLPfolder,
    														inflag_model2,
    														is_overwrite_flag_with_flag_model,
    														isSOPdebug
    													);
	  
    	
    	 //wrapper 4 find specific tags
    	 Find_specific_tags_in_given_taggedNLP_string.
    	 wrapper_4_find_specific_tags_in_given_taggedNLP_string(
    			 												baseFolder,
																5, //token_position_to_insert_lineNumber,
																outFile,
														 		outFile+"__N.txt",
														 		"_NNP",// interested_NLP_tag,
														    	-1 //default_start_lineNo
														    	);
    	 //
    	 
    	 
    	
//    	String 		baseFolder,
//		int 		token_position_to_insert_lineNumber,
//		String 		inFile,
//		String 		outFile,
//		int    		default_start_lineNo,
//		String  	NLPfolder,
//		ParserModel inflag_model2,
//		boolean 	is_overwrite_flag_with_flag_model,
//		boolean 	isSOPdebug
    	
    }
	

}
