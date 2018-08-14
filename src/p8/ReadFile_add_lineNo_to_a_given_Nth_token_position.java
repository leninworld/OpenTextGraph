package p8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

public class ReadFile_add_lineNo_to_a_given_Nth_token_position {
	
	//readFile_add_lineNo_to_a_given_Nth_token_position
	public static  void readFile_add_lineNo_to_a_given_Nth_token_position(
																		String baseFolder,
																		int token_position_to_insert_lineNumber,
																		String inFile,
																		String outFile,
																		int    default_start_lineNo
																		){
		BufferedReader reader = null;
		String line=""; int lineNo=0;
		
		if(default_start_lineNo>0)
			lineNo=default_start_lineNo;
		
		try{
			reader=new BufferedReader(new FileReader(inFile));
			FileWriter writer=new FileWriter(new File(outFile));
			 
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				lineNo++;
				String [] s= line.split("!!!");
				int cnt=0;
				String concLine="";
				System.out.println("lineNo:"+lineNo);
				// each token
				while(cnt < s.length){
					//
					if(cnt==token_position_to_insert_lineNumber-1){
						//
						if(concLine.length()==0){
							concLine=String.valueOf(lineNo)+"aa"; //suffix is used so that GBdAD creation graph dont get this mixed to other nodes with same names
						}
						else{
							concLine=concLine+"!!!"+lineNo+"aa";
						}
					}
					
					//
					if(concLine.length()==0){
						concLine=s[cnt];
					}
					else{
						concLine=concLine+"!!!"+s[cnt];
					}
					
					cnt++;
				}
				
				writer.append(concLine+"\n");
				writer.flush();
			}
			
			writer.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	// main
    public static void main(String[] args) throws IOException {
    	
    	String baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds2/";
    	String inFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_URLs_NOT_exists_in_sample_ds2_AND_ground_truth_DATETIME_exists.txt";
    	String outFile=baseFolder+"_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_URLs_NOT_exists_in_sample_ds2_AND_ground_truth_DATETIME_exists_AddLineNo.txt";
    	int token_position_to_insert_lineNumber=3;
    	
    	// readFile_add_lineNo_to_a_given_Nth_token_position
    	readFile_add_lineNo_to_a_given_Nth_token_position(
    														baseFolder,
    														token_position_to_insert_lineNumber,
    														inFile,
    														outFile,
    														500 //default_start_lineNo
    													);
    	
    	
    }
	

}
