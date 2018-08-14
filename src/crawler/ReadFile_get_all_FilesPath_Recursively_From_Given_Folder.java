package crawler;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.TreeMap;

import sun.reflect.generics.tree.Tree;

public class ReadFile_get_all_FilesPath_Recursively_From_Given_Folder {

	
	//readFile -> get_all_FilesPath_Recursively_From_Given_Folder
	public static TreeMap readFile_get_all_FilesPath_Recursively_From_Given_Folder(
									String inputGivenTopFolder,
									String outputFile_For_FullPath_generated){
		TreeMap<String, String> mapFiles=new TreeMap();
		
		try {
			FileWriter writer=new FileWriter(new File(outputFile_For_FullPath_generated));	
			//String inputGivenTopFolder="/Users/lenin/Library/Mail/V2/IMAP-coolsun2020@imap.gmail.com/[Gmail].mbox/All Mail.mbox/47E0C302-248D-474E-94D3-453E762B3BC1";
			String decodedPath = URLDecoder.decode(inputGivenTopFolder);
			
			System.out.println("\nreadFile_get_all_FilesPath_Recursively_From_Given_Folder.inputGivenTopFolder:"
							  +inputGivenTopFolder);
			
			File currentDir = new File(decodedPath); // current directory

			mapFiles=readFile_Directory_and_return_all_files_as_map(currentDir, mapFiles);
			System.out.println("mapFiles.size-"+mapFiles.size());
			
			for(String i:mapFiles.keySet()){
				writer.append("\n"+i);
				writer.flush();
			}
			
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		 
		return mapFiles;
	}
	
	//
	public static TreeMap readFile_Directory_and_return_all_files_as_map
												   (File dir,  
												   TreeMap mapFiles) {
		
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					System.out.println("directory:" + file.getCanonicalPath());
					readFile_Directory_and_return_all_files_as_map(file, mapFiles);
				} else {
					System.out.println("file:" + file.getCanonicalPath());
					mapFiles.put(file.getCanonicalPath(), 
								 file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapFiles;
	}
	// main
	public static void main(String[] args) {

		readFile_get_all_FilesPath_Recursively_From_Given_Folder(
				"/Users/lenin/Library/Mail/V2/IMAP-coolsun2020@imap.gmail.com/[Gmail].mbox/All Mail.mbox/47E0C302-248D-474E-94D3-453E762B3BC1",
				"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/fullPath.txt"
				);
		
	}

}
