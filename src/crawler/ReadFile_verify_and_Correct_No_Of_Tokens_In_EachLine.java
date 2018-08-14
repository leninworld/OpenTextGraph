package crawler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

 	// class to verify the format (number of tokens in each line) of a given file.
 	public class ReadFile_verify_and_Correct_No_Of_Tokens_In_EachLine{
		// Correct Lines with Wrong Number of Token
 		// Clean crawled files (blank lines in between). There are number of mismatch in expected number of tokens in each line.
 		// This method corrects them.
		public static void readFile_verify_and_Correct_No_Of_Tokens_In_EachLine
											 (int noTokensNeeded, String delimiter, 
											  String inputFile, String outputFile,
											  String debugFile){
		String line=""; BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(inputFile));
			FileWriter writer = new FileWriter(outputFile); 
			FileWriter writerDebug = new FileWriter(debugFile); 
			boolean isRightFormat = false;
			String [] s = null; String concLine=""; String lastLine=""; int lineNo=0;
			int realLine =0;
			writer.append("filename\turl\ttitle\tbody\n");
			writer.flush();
					//
					while ((line = reader.readLine()) != null) {
						lineNo++;
						line=line+".";
							//System.out.println(";lineno:"+lineNo+";isRightFormat:"+isRightFormat+";lastLine:"+lastLine);
							// 
							if( lineNo == 1 ){
							   lastLine=line;
							}
							s=lastLine.split(delimiter);
							//
							if( s.length == noTokensNeeded ) {
								isRightFormat=true;
							}
							else
								isRightFormat=false;
							
							//
							if(s != null ){
//								System.out.println("lineNo:"+lineNo+";s.length:"+s.length
//													+";isRightFormat:"+isRightFormat+";line:"+line);
//								System.out.println(";lastline:"+lastLine);
							}
							writer.flush();
						//
						if(  lineNo > 1 ){
							if(s.length<noTokensNeeded){
								lastLine=lastLine+line;
								//writer.append(lastLine+"\n");
								//writer.flush();
								//System.out.println("#### <noTokens:"+noTokensNeeded);
								//continue;
							}
							else if(s.length == noTokensNeeded) {
								realLine++;
								System.out.println(realLine+";"+lastLine);
								writer.append(lastLine.toLowerCase().replace("!!!","\t") +"\n"); // so far accumulated
								writer.flush();
								writerDebug.append(realLine+"--"+lastLine.split("!!!").length  +"\n");
								writerDebug.flush();
								//System.out.println("==:"+noTokensNeeded);
								lastLine=line; //
							}
							else
								lastLine=line;
						}
						else if(  lineNo == 1  ){
							//System.out.println("@@@@@@@@@@@@lineNo==1:"+line);
							lastLine=line;
						}
						writer.flush();
						System.out.println("lNo:"+lineNo+";s.len:"+s.length+";l:"+line+";#######ll:"+lastLine);
					} //end of while
					System.out.println("in:"+inputFile +";out:"+outputFile);
					
					writer.append(lastLine+"\n"); // so far accumulated
					writer.flush();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	// Verify the number of tokens in each line of given file. 
	public static int readFile_only_verify_No_Of_Tokens_In_EachLine(  
															  int 	  noTokensNeeded, 
															  String  delimiter, 
															  String  inputFile, 
															  String  outputFile_not_matched,
															  String  outputFile_matched,
															  String  debugFile,
															  boolean is_flag_strictly_lesserORequal_noTokensNeeded,
															  boolean is_add_lineNo_at_end,
															  boolean isSOPprint
															  ){
		String line=""; BufferedReader reader = null;
		int no_mismatch_lines=0;
		try{
			System.out.println("inputFile:"+inputFile);
			reader = new BufferedReader(new FileReader(inputFile));
			FileWriter writer_not_matched = new FileWriter(outputFile_not_matched);
			FileWriter writer_matched = new FileWriter(outputFile_matched);
			FileWriter writerDebug = new FileWriter(debugFile); 
			boolean isRightFormat = false;
			String [] s = null; String concLine=""; String lastLine=""; int lineNo=0;
			
			int realLine =0;
			
			String mismatch_line_nos="";
					//
					while ((line = reader.readLine()) != null) {
						
							//avoid troublig twice occuring blank tokens
							lineNo++;
							//System.out.println(";lineno:"+lineNo+";isRightFormat:"+isRightFormat+";lastLine:"+lastLine);
							
							s=line.replace(delimiter+delimiter, delimiter+" "+delimiter) .split(delimiter);
		 
							//is_flag_strictly_lesserORequal_noTokensNeeded==TRUE
							if(is_flag_strictly_lesserORequal_noTokensNeeded==false && ( s.length >= noTokensNeeded)  ){
								//
								if(is_add_lineNo_at_end==false ){
									writer_matched.append(line+"\n");
								}
								else{
									writer_matched.append(line+"!!!"+lineNo+"\n");
								}
								writer_matched.flush();
							}
							//not matched
							else if(s.length < noTokensNeeded || s.length > noTokensNeeded){
								
								no_mismatch_lines++;
								
								//debug output
								if(mismatch_line_nos.length()==0)
									mismatch_line_nos=String.valueOf(lineNo)+"("+s.length+")";
								else
									mismatch_line_nos+=String.valueOf(lineNo)+"("+s.length+")";
								
								//
								if(isSOPprint==true){
									if(lineNo<50)
										System.out.println("lNo:"+lineNo +" (actual=expect):"+s.length+"="+noTokensNeeded
														+";inputFile:"+inputFile);
											  		 	//+";not matched:ln:"+line);
								}
								else{
									if(lineNo<50)
										System.out.println("lNo:"+lineNo +" (actual=expect):"+s.length+"="+noTokensNeeded+";inputFile:"+inputFile);
									
								}
								
								if(is_add_lineNo_at_end==false ){
									writer_not_matched.append("lineNo:"+lineNo+" actual="+s.length+" expected:"+noTokensNeeded+"!!!line:"+line+"\n");
								}
								else{
									writer_not_matched.append("lineNo:"+lineNo+" actual="+s.length+" expected:"+noTokensNeeded+"!!!line:"+line+"!!!"+lineNo+"\n");
								}
								writer_not_matched.flush();
								
								//System.out.println("#### <noTokens:"+noTokensNeeded);
								//continue;
							}
							else if(s.length == noTokensNeeded) {
								
								if(!isSOPprint){
									if(lineNo<50)
										System.out.println("lNo:"+lineNo+" (actual=expect):"+s.length+"="+noTokensNeeded
															+";inputFile:"+inputFile
															+";matched:ln:"+line );
								}
								else{
									if(lineNo<50)
										System.out.println("lNo:"+lineNo+" (actual=expect):"+s.length+"="+noTokensNeeded);
								}
								
								if(is_add_lineNo_at_end==false ){
									writer_matched.append(line+"\n");
								}
								else{
									writer_matched.append(line+"!!!"+lineNo+"\n");
								}
								writer_matched.flush();
								//writer.append(lastLine.toLowerCase().replace("!!!","\t") +"\n"); // so far accumulated
								//writer.flush();
								//writerDebug.append(realLine+"--"+lastLine.split("!!!").length  +"\n");
								//writerDebug.flush();
								//System.out.println("==:"+noTokensNeeded);
							}
							writer_not_matched.flush();
						//System.out.println(".lNo:"+lineNo+";s.len:"+s.length+";l:"+line);
					} //end of while
					//System.out.println("in:"+inputFile +";out:"+outputFile);
					
					System.out.println("no_mismatch_lines:"+no_mismatch_lines+" mismatch_line_nos->"+mismatch_line_nos);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return no_mismatch_lines;
	}
	// main 
	public static void main(String[] args) throws IOException {
		//crawler c = new crawler();
		String baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/tmp/";
			   baseFolder="/Users/lenin/Dropbox/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds10/";
		
		String inputFile=baseFolder+"tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent.txt_HEADER4_WEKA.csv";
			   inputFile=baseFolder+"20000_merge_add2MOREfeature_crawledBODYTEXT.txt";
		
		String outputFile_not_matched=baseFolder+"20000_merge_add2MOREfeature_crawledBODYTEXT_nomatch.txt";
		String outputFile_matched=baseFolder+"20000_merge_add2MOREfeature_crawledBODYTEXT_match.txt";
		String debugFile=baseFolder+"debug.txt";
		 //clean and correct them.
//		readFile_verify_and_Correct_No_Of_Tokens_In_EachLine
//								( 2,
//								 "!!!",
//				  	 			 inputFile,
//				  	 			 outputFile_matched,
//				  	 			 debugFile);
		
		//verification if all lines have same number of tokens.
		readFile_only_verify_No_Of_Tokens_In_EachLine
												 ( 2, //noTokensNeeded
												 "!!!", // delimiter  
								 	 			 inputFile,  
								 	 			 outputFile_not_matched,
								  	 			 outputFile_matched,
								 	 			 debugFile,
								 	 			 true, //is_flag_strictly_lesserORequal_noTokensNeeded
								 	 			 true, //is_add_lineNo_at_end
								 	 			 false //isSOPprint
								 	 			 );
		
		
	}	
}