package crawler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class ReadFile_find_String_replaceWith_String_Write_to_Another_File  {


		//output -> <eachline,eachline>
		public static TreeMap<String,String> readFile_find_String_replaceWith_String_Write_to_Another_File
																			(String inFile,
																			String find_string,
																			String replace_string,
																			int startline, 
																			int endline,
																			String outFile,
																			boolean is_Append_outFile,
																			boolean is_Write_To_OutputFile
																			) {
			TreeMap<String,String> mapOut = new TreeMap();
			String line = ""; int counter=0;
			FileWriter writer= null;
			System.out.println("readFile_load_Each_Line_of_Generic_File_To_Map_String_String.inFile:"+inFile);
			// Read the mapping to drill-down
			HashMap<String, String> mapDrillDown = new HashMap();
			int newcounter=0;
			try {
				
				if(is_Write_To_OutputFile){
					writer= new FileWriter(new File(outFile), is_Append_outFile);
				}
				BufferedReader reader = new BufferedReader(new FileReader(inFile));
				
				
				// read each line of given file
				while ((line = reader.readLine()) != null) {
					System.out.println("line:"+line);
//					if(line.indexOf("http")==-1)
//						continue;
//					
//					line=line.replace("2..r,", "").replace("rss_,", "")
//							.replace(" ", "").replace("!.!href=", "")
//							.replace("!!!", "").replace("\"", "")
//							.replace("'", "")
//							.replace("!.!", "")
//							.replace("“", "")
//							.replace("“", "");
					
					counter++;
					if(endline>0){
						//
						if(counter>=startline && counter<=endline )
						    newcounter++;
						else{
							System.out.println("line no :"+counter+"!!!skip.line:"+line);
							continue;
						}
						}
					else{
						System.out.println("line no :"+counter+"!!!loading...:"+line);
						newcounter++;
					}
					
					line=line.replace(find_string, replace_string);
					
					mapOut.put(line, line);
					
		
					
				} //while end
			
				
				//write to output file
				if(is_Write_To_OutputFile){

					for(String s:mapOut.keySet()){
						writer.append(s+"\n");
						writer.flush();
					}
				}
				
				reader.close();
				//writer.close();
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return mapOut;
		}
	 
		//
		public static void readFile_wrapper_for_find_String_replaceWith_String_Write_to_Another_File(
																							String inputFolderName,
																							String find_string,
																							String replace_string,
																							int startline, 
																							int endline,
																							String outputFolder,
																							boolean is_Append_outFile,
																							boolean is_Write_To_OutputFile
																							){
			
 
				File file = new File(inputFolderName);
				
				if(file.isDirectory() == false)
				{
					System.out.println(" Given file is not a directory");
				//	return;
				}
				
		        String absolutePathOfInputFile = file.getAbsolutePath();
		        String Parent =  file.getParent(); 
		        
		        String[] Filenames = file.list();

		        System.out.println("absolutePathOfInputFile:"+absolutePathOfInputFile);
		        System.out.println("Parent:"+Parent);
		        System.out.println("inputFolderName:"+inputFolderName);

		  	  try{
				
		        //String outputFileName1 = "C:\\Documents and Settings\\lmookiah\\Desktop\\custom work\\output.txt";
		         

				//While loop to read each file and write into.
				for (int i=0; i < Filenames.length; i++)
				{
					
				  try{
					  File fileinput = new File(absolutePathOfInputFile +"/"+ Filenames[i]);
					  System.out.println ("File reading::"+absolutePathOfInputFile +"/"+ Filenames[i]);
					  System.out.println ("full path fileinput:"+fileinput.getAbsoluteFile());
					  
					  
					  
					  //
					  readFile_find_String_replaceWith_String_Write_to_Another_File(fileinput.getAbsolutePath(), 
							  														find_string, replace_string,
							  														startline, endline, 
							  														outputFolder+fileinput.getName()+".tsv", 
							  														is_Append_outFile, 
							  														is_Write_To_OutputFile);
			
				  }
				  catch(Exception e)
				  {
					  System.out.println("An exception occurred while calling Write method ..!!");	
					  e.printStackTrace();
					  
				  }
				  
				}
				
		  	   } //end try
				 catch(Exception e){
					 System.out.println("error.");
				 }
		}
			 
	// main
	public static void main(String[] args) {
	
		String inFile="/Users/lenin/GoogleDrive/uaw.test.input/emailFeed_out_cool2022.txt";
		inFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/us.tt.isRSS.all/merge.pastResults.isRSS.all.txt";
		String outFile="/Users/lenin/GoogleDrive/uaw.test.input/emailFeed_out_cool2022_tsv.tsv";
		outFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/us.tt.isRSS.all/merge.pastResults.isRSS.all.nodup.txt";
		String find_string="";
		String replac_with_string="";
		
		//loadEachLineToMap("");
		//loadEachGoogleAlertLineToMap("/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE/crawler/conf/googlealert.txt");
		ReadFile_find_String_replaceWith_String_Write_to_Another_File.
		readFile_find_String_replaceWith_String_Write_to_Another_File(
																		 inFile,//inFile,
																		find_string, //find String
																		replac_with_string, //replace string
																		-1, -1,
																		outFile,//outFile, 
																		true, //is_Append_outFile, 
																		true //is_Write_To_OutputFile
																		);
		
		
		String 	inFolder="/Users/lenin/GoogleDrive/uaw.test.input/email.feed.all/";
		String 	outFolder="/Users/lenin/GoogleDrive/uaw.test.input/email.feed.all.tsv.out/";
		int 	start_line=-1;
		int 	end_line=-1;
		  		find_string="!!!";
		  		replac_with_string="\t";
		
		//
//		readFile_find_String_replaceWith_String_Write_to_Another_File.
//		readFile_wrapper_for_find_String_replaceWith_String_Write_to_Another_File(
//																		 inFolder,//inFile,
//																		find_string, //find String
//																		replac_with_string, //replace string
//																		start_line, end_line,
//																		outFolder,//outFile, 
//																		true, //is_Append_outFile, 
//																		true //is_Write_To_OutputFile
//																		);
		
		
	}
	
}