package crawler;

import java.io.File;
import java.io.FileWriter;

public class Wrapper_for_readFile_eachLine_removeBlackSlashUnicode_write_to_another_File {
	
	// wrapper_for_readFile_eachLine_removeBlackSlashUnicode_write_to_another_File
	public static void wrapper_for_readFile_eachLine_removeBlackSlashUnicode_write_to_another_File ( 
																String inputFolderName,
																String outputFolder_for_removedSlashesFiles
											    ){
		
		
		try{
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
						String outFile=outputFolder_for_removedSlashesFiles +"/"+ Filenames[i]+".txt";
						 
						
						//
						ReadFile_eachLine_removeBlackSlashUnicode_write_to_another_File.readFile_eachLine_removeBlackSlashUnicode_write_to_another_File
										(inFile, outFile, false);	

						
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
		catch(Exception e){
			
		}
	}
	
public static void main(String[] args) {
		
	

	
		String inputFolderName="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/thehindu.backup.success./2006-2009/dup";
 
		String outputFolder_for_removedSlashesFiles="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup.txt";
		
		inputFolderName="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/StagingSplitFiles_output/";
		outputFolder_for_removedSlashesFiles=
				"/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/StagingSplitFiles.removedSlashes/";
	 
		//
		Wrapper_for_readFile_eachLine_removeBlackSlashUnicode_write_to_another_File.
		wrapper_for_readFile_eachLine_removeBlackSlashUnicode_write_to_another_File
																(
																inputFolderName,
																outputFolder_for_removedSlashesFiles
																);
		
	}

}
