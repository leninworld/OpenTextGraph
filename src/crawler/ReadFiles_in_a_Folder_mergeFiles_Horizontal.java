package crawler;
/*
 * (1) Read a folder name 
 * (2) Read all the text files in it.
 * (3) Merge the files into a single file "output.txt"
 * 
 * Note: Merge here is only horizontal merge
 * 
 * */ 

	import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

	public class ReadFiles_in_a_Folder_mergeFiles_Horizontal {

		// merge files
		public static void readFiles_in_a_Folder_mergeFiles_Horizontal(String inputFolderName_input_1, 
																	   String outputFileName1,
																	   String only_inputFiles_match_on_pattern,
																	   boolean is_append_primary_FileName_at_end,
																	   String [] inputFiles_input_2,
																	   boolean  isSOPprint,
																	   String 	delimiter
																	   ){
			File file = new File(inputFolderName_input_1);
			String[] Filenames =null;
			int total_files_processed=0;
			if(file.isDirectory() == false)
			{
				System.out.println(" Given file is not a directory");
			//	return;
			}
			String absolutePathOfInputFile="",Parent="";
	        
	        if(inputFolderName_input_1.length()>0){
	        	Filenames = file.list();	
	        	absolutePathOfInputFile = file.getAbsolutePath();
		        Parent =  file.getParent();
		        	
	        }
	        else{
	        	// input string array given
	        	Filenames = inputFiles_input_2;
	        }
	        

	        System.out.println("absolutePathOfInputFile:"+absolutePathOfInputFile);
	        System.out.println("Parent:"+Parent);
	        System.out.println("inputFolderName:"+inputFolderName_input_1);

	  	  try{
	  		FileWriter writer_debug=new FileWriter(new File(outputFileName1+"_debug.txt")); 
	  		
	        //String outputFileName1 = "C:\\Documents and Settings\\lmookiah\\Desktop\\custom work\\output.txt";
	        FileWriter writer = new FileWriter(outputFileName1);
	        File fileinput = null;

			//While loop to read each file and write into.
			for (int i=0; i < Filenames.length; i++)
			{
				
			  try{
				  
				  if(inputFolderName_input_1.length()>0){
					  fileinput = new File(absolutePathOfInputFile +"/"+ Filenames[i]);
					  System.out.println ("INPUT 1 -File reading::"+absolutePathOfInputFile +"/"+ Filenames[i]);
					  System.out.println ("INPUT 1 -full path fileinput:"+fileinput.getAbsoluteFile());
				  }
				  
				  if(inputFolderName_input_1.length()==0){
					  absolutePathOfInputFile=new File(Filenames[i]).getAbsolutePath();
					  fileinput = new File( Filenames[i]);
					  System.out.println ("INPUT 2 -File reading::"+absolutePathOfInputFile +"/"+ Filenames[i]);
					  System.out.println ("INPUT 2 -full path fileinput:"+fileinput.getAbsoluteFile());
				  }
				  
				  boolean is_dir=new File(absolutePathOfInputFile +"/"+ Filenames[i]).isDirectory();
				  
				  if(Filenames[i].indexOf("DS_Store")>=0 ||   is_dir==true   
						  || Filenames[i].toLowerCase().indexOf("readme")>=0 || Filenames[i].toLowerCase().indexOf("debug")>=0 ){
					  System.out.println(" ***skipping for file "+Filenames[i]
							  			+" "+is_dir
							  			+" "+Filenames[i].toLowerCase().indexOf("readme")+" "+Filenames[i].indexOf("DS_Store")
							  			+" "+Filenames[i].indexOf(".txt")
							  			+" "+absolutePathOfInputFile +"/"+ Filenames[i]);
					  writer_debug.append("\n ***skipping for file "+Filenames[i]
							  			+" "+is_dir
							  			+" "+Filenames[i].toLowerCase().indexOf("readme")+" "+Filenames[i].indexOf("DS_Store")
							  			+" "+Filenames[i].indexOf(".txt")
							  			+" "+absolutePathOfInputFile +"/"+ Filenames[i]);
					  continue;
				  }
				  else{
					  
				  }

				  System.out.println("only_inputFiles_match_on_pattern:"+only_inputFiles_match_on_pattern);
				  writer_debug.append("\n-----only_inputFiles_match_on_pattern:"+only_inputFiles_match_on_pattern
					  	+" "+only_inputFiles_match_on_pattern.length()+" "+Filenames[i].indexOf(only_inputFiles_match_on_pattern) +" Filenames[i]:"+Filenames[i]);
				  writer_debug.flush();
				  // 
				  if( Filenames[i].toLowerCase().indexOf(only_inputFiles_match_on_pattern.toLowerCase()) >=0){
					  System.out.println("------FILTER MATCHED-Processing file ->"+Filenames[i]+" i="+i+" out of total files="+Filenames.length );
					  writer_debug.append("\n-------Processing file ->"+Filenames[i]+" i="+i+" out of total files="+Filenames.length );
					  writer_debug.flush();
					  //
					  Write( fileinput , fileinput , absolutePathOfInputFile,writer, i, Filenames[i] , is_append_primary_FileName_at_end, delimiter);
					  total_files_processed++;
				  }	
				  else{
					  System.out.println("-------- FILTER NOT MATCHED ----NO PROCSSING file ->"+Filenames[i] +" i="+i+" out of total files="+Filenames.length );
					  writer_debug.append("\n-------FILTER NOT MATCHED ----NO PROCSSING  file ->"+Filenames[i] +" i="+i+" out of total files="+Filenames.length );
					  writer_debug.flush();
//					  
//					  Write( fileinput , fileinput , absolutePathOfInputFile,writer, i, Filenames[i] , is_append_primary_FileName_at_end);
//					  total_files_processed++;
				  }
				  writer_debug.append("\n--- total_files_processed:"+total_files_processed);
				  writer_debug.flush();
				   
		
			  }
			  catch(Exception e)
			  {
				  System.out.println("An exception occurred while calling Write method ..!!");	
				  e.printStackTrace();
				  
			  }
			  
			}
			writer.close();
			writer_debug.close();
			  System.out.println("total_files_processed:"+total_files_processed);
	  	   } //end try
			 catch(Exception e){
				 System.out.println("error.");
			 }
		}
				// write
				public static void Write(File outputFileName,  File fileinput, String absolutePathOfInputFile 
										, FileWriter writer, int i, String primaryFileName,
										boolean is_append_primary_FileName_at_end,
										String delimiter
										){
				String line = null ; int lineNo = 0;
				//String primaryFileName = absolutePathOfInputFile.substring( , endIndex)
				//String outputFileName1 =  absolutePathOfInputFile  + "\\output.txt";
				try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileinput.getAbsoluteFile())));
				
				System.out.println ("Inside write method input file :"+ fileinput );
				System.out.println ("Inside write method "+ outputFileName +" : "+ reader);
				//System.out.println ("---------- ");
				
					System.out.println ("outputFileName :"+outputFileName);
					  //
					  while ((line = reader.readLine()) != null) {
						  line=line.replace(delimiter+delimiter, delimiter+"#"+delimiter);
						  lineNo++;
						  if(i>0 && lineNo==1) continue;
						  //System.out.println(reader.readLine());
						  //System.out.println("############# BEFORE APPENDING #############");
						  
						  
						  if(is_append_primary_FileName_at_end){
							  writer.append( line.replace("\t", "").replace(", ",",").replace("      ", "").replace("     ", "").replace("    ", "")
									  .replace("   ", "").replace("  ", "").replace("  ", "").replace(", ", ",")
									  .toString() + "!!!"+primaryFileName+ '\n');
						  }
						  else{
							  writer.append( line.replace("\t", "").replace(", ",",").replace("      ", "").replace("     ", "").replace("    ", "")
									  .replace("   ", "").replace("  ", "").replace("  ", "").replace(", ", ",")
									  .toString()+ '\n');
						  }
						  
						  writer.flush(); 
					  }
					  System.out.println ("----------flushing ");
					  reader.close();

				}
				catch(Exception e)
				  {
					  System.out.println("An exception occurred while writing the files..!!");	
					  e.printStackTrace();
				  }
		  
				} //END OF write
				
				// main
				public static void main(String[] args) {
					
//					if (args.length != 1) {
//						System.out.println("Incorrect input provided..!! Please provide proper folder name");
//						System.out.println("Correct Usage is: java MergeFiles <input folder name>");
//						return;
//					}
				  try{
					String baseFolder="/Users/lenin/Dropbox/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds10/pick20000articles/merge/";
						  
					String inputFolderName = "/Users/lenin/Dropbox/#Data/group-by-age-related-out/";
					inputFolderName="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/us.tt.isRSS.all/";
					
					inputFolderName="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/May/";
					inputFolderName=baseFolder;
								
					String outputFileName = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/merge.pastResults/";
					outputFileName="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/us.tt.isRSS.all/merge.pastResults.isRSS.all.txt";
					
					outputFileName="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/May/NYT.May.merged.txt";
					outputFileName=baseFolder+"merged_all.txt";
					boolean is_append_primary_FileName_at_end=false;
					String [] inputFiles_input_2=new String[1];
					
					//
					readFiles_in_a_Folder_mergeFiles_Horizontal(
																inputFolderName, 
																outputFileName,
																"",//only_inputFiles_match_on_pattern
																is_append_primary_FileName_at_end,
																inputFiles_input_2,
																true, //isSOPprint
																"!!!"// delimiter
															   );
					
				  }
				  catch(Exception e)
				  {
					  System.out.println("An exception occurred while calling Write method ..!!");	
					  e.printStackTrace();
				  }
							
				}
				
				 
}
