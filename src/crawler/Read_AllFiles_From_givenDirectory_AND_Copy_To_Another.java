package crawler;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class Read_AllFiles_From_givenDirectory_AND_Copy_To_Another {
	
	//read all files
	public static void read_AllFiles_From_givenDirectory_AND_Copy_To_Another(
													String sourceDirectory,
													String destinationDirectory,
													int No_times_to_repeat_event
													){
		File destDir =null;
        File srcDir = new File(sourceDirectory);
        int c=1;

		try {
			//while 
			while(c<=No_times_to_repeat_event){
				System.out.println("Times-------------------");
	            System.out.println("Source:"+srcDir.list().length);
	            
				destDir= new File(destinationDirectory+c);
				FileUtils.copyDirectory(srcDir, destDir);
				System.out.println("Dest:"+destDir.list().length);
	            c++;
			}
            
            
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}
		//	read all files
		public static int read_AllFiles_From_givenDirectory_AND_Copy_To_Another_Then_apply_Filter_for_Size_of_File_in_eachFolder(
																					String sourceDirectory,
																					String destinationDirectory,
																					TreeMap<Integer, TreeMap<Integer,Float>> mapFilterForEachFolder
																					){
			File destDir =null;
	        File srcDir = new File(sourceDirectory);
	        int c=1;
	        int totalFilesInDestinationFolder=0;

			try {
				//while 
				while(c<=mapFilterForEachFolder.size()){
					System.out.println("Times-------------------");
		            System.out.println("Source:"+srcDir.list().length);
		            String dest_directoryName=destinationDirectory+c+"("
		            		+mapFilterForEachFolder.get(c).get(1)
		            		+"-"+mapFilterForEachFolder.get(c).get(2)
		            		+")";
		            
		            System.out.println("Curr Destination:"+dest_directoryName);
		            
					destDir= new File(dest_directoryName);
					FileUtils.copyDirectory(srcDir, destDir);
					System.out.println("Dest:"+destDir.list().length);
					
					// delete files that do not follow range
					ReadFiles_From_Directory_AND_delete_IF_size_NOT_within_Range.
					readFiles_From_Directory_AND_delete_IF_size_NOT_within_Range
									(dest_directoryName,
									 mapFilterForEachFolder.get(c).get(1),
									 mapFilterForEachFolder.get(c).get(2)
									 );
					
					System.out.println("(after delete):destination.length:"+destDir.list().length+" for folder "+dest_directoryName);
					
					totalFilesInDestinationFolder=   totalFilesInDestinationFolder
												   + destDir.list().length;
					
					System.out.println("totalFilesInDestinationFolder:"+totalFilesInDestinationFolder);
					
		            c++;
				}
	        
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			return totalFilesInDestinationFolder;
		}
	// main
    public static void main(String[] args) {
        //
        // An existing directory to copy.
        //
        String source = "";
        String destination = "";

        //
        // The destination directory to copy to. This directory
        // doesn't exists and will be created during the copy
        // directory process.
        //
 
        String input_Float_Size_Range_For_Each_Folder= "";
        
        try {
            //
            // Copy source directory into destination directory
            // including its child directories and files. When
            // the destination directory is not exists it will
            // be created. This copy process also preserve the
            // date information of the file.
            //
            //FileUtils.copyDirectory(srcDir, destDir);
            
//        	read_AllFiles_From_givenDirectory_AND_Copy_To_Another(source,
//        												   destination,
//        												   1
//        												   );
        	
        	final long t0 = System.nanoTime();
        	
        	TreeMap<Integer, TreeMap<Integer,Float>> mapFilterForEachFolder=new TreeMap();
        	TreeMap<Integer,Float> mapRange=new TreeMap<Integer, Float>();
 
        	
        	int totalFilesInDestinationFolder=0;
        	input_Float_Size_Range_For_Each_Folder="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data/input_Float_Size_Range_For_Each_Folder.txt";
            
            source	   ="/Users/lenin/Downloads/Feeds-29-Jun-2015/";
            destination="/Users/lenin/Downloads/Feeds-29-Jun-SplitBase/";
            //load all lines
        	TreeMap<Integer,String> mapLoadEachLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
        											readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
        											(input_Float_Size_Range_For_Each_Folder, 
        											-1, 
        											-1,
        											"", //debug_label
        											false // isSOPprint
        											);
        	//read each line
        	for(int lineNo:mapLoadEachLine.keySet()){
        		String [] sRange=mapLoadEachLine.get(lineNo).split(",");
        		mapRange=new TreeMap<Integer, Float>();
        		mapRange.put(1, Float.valueOf(sRange[0])); 
        		mapRange.put(2, Float.valueOf(sRange[1]));
        		mapFilterForEachFolder.put(lineNo, mapRange);
        	}
        	
        	System.out.println("mapFilterForEachFolder:"+mapFilterForEachFolder);
        	
        	// copy files  
        	 totalFilesInDestinationFolder
        		=read_AllFiles_From_givenDirectory_AND_Copy_To_Another_Then_apply_Filter_for_Size_of_File_in_eachFolder(
			        			source,
			        			destination,
			        			mapFilterForEachFolder
			   );
        	
        	//921 +183 + 96+49+62+39+14+17+6  <- sum of 9 folders
        	
        	System.out.println("mapFilterForEachFolder:"+mapFilterForEachFolder);
        	
		    System.out.println("Time Taken:" + NANOSECONDS.toSeconds(System.nanoTime() - t0) +" seconds; "
			       	   + (NANOSECONDS.toSeconds(System.nanoTime() - t0))/60 +" minutes");
		    System.out.println("totalFilesInDestinationFolder:"+totalFilesInDestinationFolder);
        	
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}