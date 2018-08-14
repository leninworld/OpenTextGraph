package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

import jdk.jfr.events.FileWriteEvent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

// read given file for pattern match on each line
public class ReadFile_readEachLine_For_a_Set_of_Start_String_End_String_WriteToAnotherFile {
	
	// read each line of a file to match for a pattern
	public static TreeMap<Integer, String> readFile_readEachLine_For_a_Set_of_Start_String_End_String_WriteToAnotherFile(
																		String patterString,
																		String InputFile,
																		String OutputFile,
																		boolean is_Append_out_File,
																		boolean is_remove_html_tags,
																		boolean is_preserve_original_CASE
																		){
		TreeMap<Integer, String> mapOut = new TreeMap<Integer, String>();
		try {
			
			TreeMap<String, String> mapStartPatternEndPattern=new TreeMap(); 
			
			String s1[] = patterString.split(";");
			int cnt1=0;
			//
			while(cnt1<s1.length){
				String s2[] =s1[cnt1].split(",");
				mapStartPatternEndPattern.put(s2[0], s2[1]);
				cnt1++;
			}
			
			 FileWriter writer=new FileWriter(new File(OutputFile), is_Append_out_File);
			 LineIterator it = IOUtils.lineIterator(
				       			new BufferedReader(new FileReader(InputFile)));
				 for (int lineNumber = 0; it.hasNext(); lineNumber++) {
				    String line = (String) it.next();
				    //convert 2 LOWERCASE
				    if(is_preserve_original_CASE==false)
				    	   line=line.toLowerCase();
				    
				    String concLine="";
				    int counterPattern=0;
				    //
				    for(String startPattern:mapStartPatternEndPattern.keySet()){
				    	String endPattern=mapStartPatternEndPattern.get(startPattern);
				    	int startIndex=line.indexOf(startPattern);
				    	int endIndex=line.indexOf(endPattern,startIndex + startPattern.length()+2);
				    	System.out.println("line:"+lineNumber+" start:"+startIndex+" end:"+endIndex);
				    	//
				    	if(startIndex>0 && endIndex>0 && endIndex>startIndex){
				    		//
				    		if(counterPattern==0){
				    			concLine=line.substring(startIndex, endIndex);
				    		}
				    		else{
				    			concLine=concLine+"!!!"+line.substring(startIndex, endIndex);
				    		}
				    		counterPattern++;
				    	}
				    	else{ //nothing matched. say "dummy"
				    		
				    		if(counterPattern==0){
				    			concLine=startPattern+" dummy";
				    		}
				    		else{
				    			concLine=concLine+"!!!"+startPattern+" dummy";
				    		}
				    		counterPattern++;
				    	}
				    }
				    System.out.println("matched line:"+concLine);
				    if(concLine.length()>1){
				    	 // remove <> </> etc.
				    	if(is_remove_html_tags){
				    		concLine=Crawler.SubRemoveTagsFromHTML(concLine);
				    	}
					    mapOut.put(lineNumber, concLine);
					    writer.append(concLine.replaceAll("\\s+", " ").replace("Content-Base: ", "")  +"\n");
				    }
				    writer.flush();
//				    if (lineNumber == expectedLineNumber) {
//				        return line;
//				    }
				 }
				 writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mapOut;
	}
	
	//
	public static void readFileSearchForAPatternInSpecificLineOrder(String inputFolder){
		File directoryF = new File(inputFolder);
		try {
			String line = (String) FileUtils.readLines(directoryF).get(100);
			System.out.println(""+line);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	//
	public static void r(){
		try {
			 String text =
				        "Line1 blah blah\n" +
				        "Line2 more blah blah\n" +
				        "Line3 let's try something new \r\n" +
				        "Line4 meh\n" + 
				        "Line5 bleh\n" + 
				        "Line6 bloop\n";
				    //Scanner sc = new Scanner(text).skip("(?:.*\\r?\\n|\\r){4}");
			 		String skipText="Line4";
			 		Scanner sc = new Scanner(text).skip("(?:.*\\r?\\n|\\r){4}");
			 
				    while (sc.hasNextLine()) {
				        System.out.println("t:"+sc.nextLine());
				    }
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
     	// read each line of a file to match for a pattern
		public static TreeMap<Integer, String> readFile_readEachLine_For_a_Set_of_Start_String_End_String_WriteToAnotherFile_Recursive(
																			String patterString,
																			String InputFile,
																			String OutputFile,
																			String Yes_Filter_Pattern_for_each_line,
																			boolean is_Append_out_File,
																			String debug_label
																			){
			TreeMap<Integer, String> mapOut=new TreeMap<Integer, String>();
			try {
				
				TreeMap<String, String> mapStartPatternEndPattern=new TreeMap(); 
				
				String s1[] = patterString.split(";");
				int cnt1=0;
				//
				while(cnt1<s1.length){
					String s2[] =s1[cnt1].split(",");
					mapStartPatternEndPattern.put(s2[0], s2[1]);
					cnt1++;
				}
				
				 FileWriter writer=new FileWriter(new File(OutputFile), is_Append_out_File);
				 System.out.println("InputFile:"+InputFile);
				 LineIterator it = IOUtils.lineIterator(
					       			new BufferedReader(new FileReader(InputFile)));
				 int startIndex=0;
				 int endIndex=0;
				 int counterFound=0;
				 TreeMap<String,String> mapAlreadyGot=new TreeMap<String, String>();
					 for (int lineNumber = 0; it.hasNext(); lineNumber++) {
					    String line = (String) it.next();
					    line=line.toLowerCase();
					    //
					    if(Yes_Filter_Pattern_for_each_line.length()>0
					    	&& line.toLowerCase().indexOf(Yes_Filter_Pattern_for_each_line.toLowerCase())==-1 ){
					    	continue;
					    }
					    
					    String concLine="";
					    int counterPattern=0;
					    boolean isEnteringFirstTime=true;
					    String tmp="";
					    //iterate each pattern start-end pair
					    for(String startPattern:mapStartPatternEndPattern.keySet()){
					    	//isEnteringFirstTime=true;
					    	String endPattern=mapStartPatternEndPattern.get(startPattern);
					    	
					    	while(isEnteringFirstTime==true || (startIndex>=0 && endIndex>=0)  ){
					    	
						    	startIndex=line.indexOf(startPattern);
						    	endIndex=line.indexOf(endPattern,startIndex + startPattern.length()+2);
						    	System.out.println("FN():;line:"+lineNumber+";line.len:"+line.length()+" start:"+startIndex
						    			+" end:"+endIndex+" debug_label:"+debug_label);
						    	//
						    	if(startIndex>0 && endIndex>0 && endIndex>startIndex){
						    		tmp=line.substring(startIndex, endIndex);
						    		//
						    		if(counterPattern==0 &&
						    		  !mapAlreadyGot.containsKey((line.substring(startIndex, endIndex) ) )){
						    			concLine=tmp;
						    			mapAlreadyGot.put(tmp ,"dum");
						    		}
						    		else if(!mapAlreadyGot.containsKey((line.substring(startIndex, endIndex) ) )) {
						    			concLine=concLine+"!!!"+tmp;
						    			mapAlreadyGot.put(tmp ,"dum"); 
						    		}
						    		counterPattern++;
						    	}
						    	if(endIndex==-1) 
						    		break;
						    	
						    	line=line.substring(endIndex, line.length());
						    	
						    	isEnteringFirstTime=false;
					    	}//end while
					    	
					    }
					    System.out.println("fn():Inside function()!!!length:"+concLine.length()
					    					+";matched line:"+concLine);
					    if(concLine.length()>1  && !mapAlreadyGot.containsKey(concLine) ){
						    mapOut.put(lineNumber, concLine);
						    writer.append("\n"+concLine);
						    mapAlreadyGot.put(concLine, "dum");
						    concLine="";
					    }
					    writer.flush();
					    
//					    if (lineNumber == expectedLineNumber) {
//					        return line;
//					    }
					 }
					 writer.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			return mapOut;
		}

		

	// main
	public static void main(String[] args) throws IOException {
		//
		String [] arr_inputFile=new String[1000];
		
		String 	baseFolder="";
				baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Feeds-Newscopy/Trad_dummy_for_test/";
				//TRAD--> /Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Feeds-Newscopy/Trad/
				//TRAD--> /Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Feeds-Newscopy/Trad_dummy_for_test/  <--DUMMY
				//POLI--> /Users/lenin/Downloads/#Data2/Feed-Backup-Poli/US-TT-Feed-02-May-2016/
				//POLI--> /Users/lenin/Downloads/#Data2/Feed-Backup-Poli/US-TT-Feed-17-Feb-2016/
				//POLI--> /Users/lenin/Downloads/#Data2/Feed-Backup-Poli/US-TT-Feed-26-Oct/
				//POLI--> /Users/lenin/Downloads/#Data2/Feed-Backup-Poli/US-TT-Feed-3-Sep/
				//POLI--> /Users/lenin/Downloads/#Data2/Feed-Backup-Poli/US-TT-Feed-12-Dec/
		
		String 	InputFile_OR_Folder=baseFolder; //given is FOLDER
				//InputFile_OR_Folder=baseFolder+"out__static.txt_eachLineNEWSstring.txt"; //given is a FILE
			
		String outFile_prefix="all_all_body_URL_datetime_auth_keywor_subjec";
		// 
		String 	OutputFile="";
				OutputFile=baseFolder+outFile_prefix+".txt"; //OVERWRITTEN BELOW if "InputFile_OR_Folder" is a folder
				
		String  pattern="!!!true";
		boolean is_Append_out_File=false;
		String subFolder="";
		//each pair is separated by ;   Example: "place_cont,class;url\",\""
		// "LOWER CASE"
		String patterString="place_cont,class;url\",\"";
			   patterString="<body,</body;Content-Base:, ;From -,0000;Subject:, Content;From:,MIME-Version;Keywords:,Content-";  //Subject:,Keywor;Subject:,Content
		int number_of_folders_processed=0;
		// 
		File file_=new File(InputFile_OR_Folder);
		// directory
		if(file_.isDirectory() ){ // baseFolder = "/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Feeds-Newscopy/Trad"
			arr_inputFile=file_.list();	
			//
			int max_=arr_inputFile.length;
			int cnt2=0; number_of_folders_processed=0;
			// 
			while(cnt2 < max_ ){
				//
				if(arr_inputFile[cnt2].indexOf("DS_Store") > 0 || arr_inputFile[cnt2].indexOf("XML2CSV_LOWERCASE") >=0
						|| arr_inputFile[cnt2].indexOf("XML2CSV") ==-1
						|| arr_inputFile[cnt2].indexOf(".space") > 0    ){
					cnt2++; 
					continue;
				}
//				else if(  ){
//					
//				}
				
				number_of_folders_processed++;
				//
				String curr_SUB_folder = baseFolder + arr_inputFile[cnt2]+"/";

				
				String [] s2_file=  (new File(curr_SUB_folder)).list();
				int cnt10=0;

				System.out.println("curr_File:" +curr_SUB_folder+"---arr_inputFile[cnt2]:"+arr_inputFile[cnt2]+" files of subfolder:"+s2_file.length);
				
				int max_2=s2_file.length;						
				// 
				while(cnt10 < max_2 ){
					String curr_curr_File=s2_file[cnt10];   
					//System.out.println("curr_curr_File:"+curr_curr_File);
					// 
					if(curr_curr_File.indexOf("DS_Store") > 0 ){
						cnt10++; continue;
					}
					// 
					if(curr_curr_File.toLowerCase().indexOf("eachlinenewsstring") >= 0){
						System.out.println("PASS for eachLineNEWSstring*******:"+curr_curr_File);
						
						// output
						OutputFile=curr_SUB_folder+   outFile_prefix+"_OF_"+curr_curr_File.toLowerCase().replace("eachlinenewsstring" ,"");
						
						//input
						curr_curr_File=curr_SUB_folder+curr_curr_File;
						
						//readFile_readEachLine_For_a_Set_of_Start_String_End_String_WriteToAnotherFile
						readFile_readEachLine_For_a_Set_of_Start_String_End_String_WriteToAnotherFile(
																										patterString, //LOWER CASE
																										curr_curr_File,
																										OutputFile,
																										is_Append_out_File,
																										true, //is_remove_html_tags
																										true // is_preserve_original_CASE
																									 );
						
					}
					else{
						//System.out.println("FAIL for eachLineNEWSstring:"+curr_curr_File);
					}
					
					cnt10++;
				}
				

				
				
				cnt2++;
			}
			
		}
		else{ //only one file
			arr_inputFile[0]=InputFile_OR_Folder;
			subFolder=baseFolder;
			
			//readFile_readEachLine_For_a_Set_of_Start_String_End_String_WriteToAnotherFile
			readFile_readEachLine_For_a_Set_of_Start_String_End_String_WriteToAnotherFile(
																							patterString, //LOWER CASE
																							arr_inputFile[0],
																							OutputFile,
																							is_Append_out_File,
																							true, //is_remove_html_tags
																							true // is_preserve_original_CASE
																						 );
			
		}
		

			   
		 

		System.out.println("mapPattern::" +" number_of_folders_processed:"+number_of_folders_processed
							+" is_Append_out_File:"+is_Append_out_File);
		
		
	}

}
