package crawler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

// wrapper
public class ReadFile_load_wrapper_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile  {

	// wrapper taking inFolder and outFolder
	public static void readFile_wrapper_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
																							(String inputFolderName 
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
	
		//	String outputFileName1 = "C:\\Documents and Settings\\lmookiah\\Desktop\\custom work\\output.txt";
		FileWriter writer = null;
	
		//While loop to read each file and write into.
		for (int i=0; i < Filenames.length; i++)
		{
		
		try{
			File fileinput = new File(absolutePathOfInputFile +"/"+ Filenames[i]);
			System.out.println ("File reading::"+absolutePathOfInputFile +"/"+ Filenames[i]);
			System.out.println ("full path fileinput:"+fileinput.getAbsoluteFile());
			String inFile=absolutePathOfInputFile +"/"+ Filenames[i];
			String outFile=absolutePathOfInputFile +"/"+ Filenames[i]+"_nodup.txt";
			 
			
			//
			ReadFile_load_wrapper_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
			readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
									inFile,//inFile, 
									-1, -1,
									outFile,//outFile, 
									true, //is_Append_outFile, 
									true //is_Write_To_OutputFile
									);
			
			}
			catch(Exception e)
			{
				System.out.println("An exception occurred while calling Write method ..!!");	
				e.printStackTrace();
			}
		
		}
		writer.close();
	} //end try
	catch(Exception e){
		System.out.println("error.");
	}
}
		//output -> <eachline,eachline>
		public static TreeMap<String,String> readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
																			(String inFile,
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
					
					mapOut.put(line, line);
				}
				//
				if(is_Write_To_OutputFile){
					//write to output file
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
	// output -> map key,value -> <counter,eachline>
	public static TreeMap<Integer,String> readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
																							(String inFile, 
																							 int 	startline, 
																							 int 	endline) {
		TreeMap<Integer,String> mapOut = new TreeMap();
		String line = ""; int counter=0;
		// Read the mapping to drill-down
		HashMap<String, String> mapDrillDown = new HashMap();
		int newcounter=0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				counter++;
				if(endline>0){
					//
					if(counter>=startline && counter<=endline )
					    newcounter++;
					else
						continue;
					}
				else{
					newcounter++;
				}
				
				mapOut.put(newcounter, line);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	 
	// main
	public static void main(String[] args) {
		
		
		String inputFile_with_dup="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.dup.txt";
		String outputFile_with_nodup="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup.txt";
		inputFile_with_dup="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/thehindu.backup.success./2006-2009/test2.txt";
		outputFile_with_nodup="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/thehindu.backup.success./2006-2009/test2_nodup.txt";
	
		//loadEachLineToMap("");
		//loadEachGoogleAlertLineToMap("/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE/crawler/conf/googlealert.txt");
//		readFile_load_wrapper_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
//		readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
//								inputFile_with_dup,//inFile, 
//								-1, -1,
//								outputFile_with_nodup,//outFile, 
//								true, //is_Append_outFile, 
//								true //is_Write_To_OutputFile
//								);
		
		String inputFolderName="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/thehindu.backup.success./2006-2009/dup";
		
		inputFolderName= "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/toi.backup.success.22.july.2015.one/dup/all/";
		
		// 
		ReadFile_load_wrapper_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
		readFile_wrapper_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
																						(
																						inputFolderName
																						);
		
	}
	
}