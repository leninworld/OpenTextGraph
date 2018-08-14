package crawler;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

//
public class ReadFiles_From_Directory_AND_delete_IF_size_NOT_within_Range {
	
	//read files
	public static void readFiles_From_Directory_AND_delete_IF_size_NOT_within_Range(String Folder,
																			float startRange,
																			float endRange
																		  ){
		 try {
			 	File directoryF = new File(Folder);
				File[] files = directoryF.listFiles();
				
				for (File file : files) {
					if(file.getAbsolutePath().indexOf("Store")>=0
							&& file.getAbsolutePath().indexOf("DS")>=0
							)
						continue;
					float sizeMB=(float) (file.length()/(1024.0*1024.0));
					 
					if(sizeMB>=startRange && sizeMB<=endRange){
						System.out.printf("%-20s Size in MB:" +sizeMB + "\n", file.getName());
					}
					else{
						file.delete();
						System.out.printf("%-20s Size in MB(deleted):" +sizeMB + "\n", file.getName());
					}
					
					
				}
				
				System.out.println("Finished: deleted for given range:"+startRange
											+";"+endRange+" for folder:"+Folder);
				
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	//main
	public static void main(String[] args) {
		
		
		try {
		// 
		String inputFolder="/Users/lenin/Downloads/Feeds-5-Jun-SplitBase/1(0.0-1.0)/";
		// read and delete file NOT in given range
		readFiles_From_Directory_AND_delete_IF_size_NOT_within_Range(inputFolder,
														    		 0.0f,
														    		 1.0f);
		
		File directoryF = new File("/Users/lenin/Downloads/Feeds-5-Jun.near1/Well");
	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
