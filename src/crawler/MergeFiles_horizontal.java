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
	//
	public class MergeFiles_horizontal {

		//merge Files
		public static void mergeFiles_horizontal(
									  String 	inputFolderName_input_1_OR_2, //input 1 (OR)
									  String    [] list_of_files_input_1_OR_2,//input 2
									  String 	outputFileName1, 
									  boolean 	isMac,
									  boolean   is_add_file_name_of_source_as_last_column_of_eachLine_in_mergedFile
									  ){
			File file = null;String[] Filenames =null;
			String absolutePathOfInputFile ="";String Parent ="";
			
			// folder given
			if(inputFolderName_input_1_OR_2.length()>0){
				file=new File(inputFolderName_input_1_OR_2);
				//
				if(file.isDirectory() == false){
					System.out.println(" Given file is not a directory");
				//	return;
				}
				
		        absolutePathOfInputFile = file.getAbsolutePath();
		        Parent =  file.getParent(); 
		        
		        Filenames = file.list();
	        
			}
			//lisst of files given
			else{
				Filenames=list_of_files_input_1_OR_2;
			}

	        System.out.println("absolutePathOfInputFile:"+absolutePathOfInputFile);
	        System.out.println("Parent:"+Parent);
	        System.out.println("inputFolderName:"+inputFolderName_input_1_OR_2);

	  	  try{
			
	        //String outputFileName1 = "C:\\Documents and Settings\\lmookiah\\Desktop\\custom work\\output.txt";
	        FileWriter writer = new FileWriter(outputFileName1);

	        String pathslash="";
			//While loop to read each file and write into.
			for (int i=0; i < Filenames.length; i++)
			{
				
			  try{
				  if(isMac)
					  pathslash="//";
				  else
					  pathslash="\\";
				  
				  //File fileinput = new File(absolutePathOfInputFile+pathslash + Filenames[i]);
				  
				  File fileinput = new File(inputFolderName_input_1_OR_2+ Filenames[i]);
				  System.out.println ("File reading::"+absolutePathOfInputFile+pathslash + Filenames[i]);
				  System.out.println (fileinput.getAbsoluteFile());
				  
				  if(  (fileinput.getAbsoluteFile().toString().indexOf("Store")>=0
						 && fileinput.getAbsoluteFile().toString().indexOf("DS")>=0) ||
						  fileinput.isDirectory()==true
						 ){
					  continue;
				  }
				  
				  Write( fileinput , fileinput , absolutePathOfInputFile,writer,
						  is_add_file_name_of_source_as_last_column_of_eachLine_in_mergedFile
						  );
		
			  }
			  catch(Exception e)
			  {
				  System.out.println("An exception occurred while calling Write method ..!!");	
				  e.printStackTrace();
				  
			  }
			  //writer.close();
			}
	  	   } //end try
			 catch(Exception e){
				 System.out.println("error.");
			 }
			
		}
				// WRITE
				public static void Write(File outputFileName, 
							File fileinput, String absolutePathOfInputFile , FileWriter writer,
							boolean is_add_file_name_of_source_as_last_column_of_eachLine_in_mergedFile){
				String line = null ;	
				//String outputFileName1 =  absolutePathOfInputFile  + "\\output.txt";
				try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(
											new FileInputStream(fileinput.getAbsoluteFile())));
				System.out.println ("---------- ");
				System.out.println ("Inside write method input file :"+ fileinput );
				System.out.println ("Inside write method "+ outputFileName +" : "+ reader);
					
					System.out.println ("outputFileName :"+outputFileName);
					
					  while ((line = reader.readLine()) != null) {
						  //System.out.println(reader.readLine());
						  //System.out.println("############# BEFORE APPENDING #############");
						  if(is_add_file_name_of_source_as_last_column_of_eachLine_in_mergedFile==false){
							  writer.append(line.toString()+ '\n');
						  }
						  else{
							  writer.append(line.toString()+"!!!inFile:"+fileinput +'\n');
						  }
						  
						  writer.flush(); 
					  }
					  System.out.println ("----------flushing ");
					  
				}
				  
				  //reader.close();
				catch(Exception e)
				  {
					  System.out.println("An exception occurred while writing the files..!!");	
					  e.printStackTrace();
				  }
		  
				}
				// main
				public static void main(String[] args) {
					
//					if (args.length != 1) {
//						System.out.println("Incorrect input provided..!! Please provide proper folder name");
//						System.out.println("Correct Usage is: java MergeFiles <input folder name>");
//						return;
//					}
				  try{
					String inputFolderName_input_1_OR_2 = "";
					String baseFolder="/Users/lenin/Dropbox/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds11/pick20000articles/merge/";
					
					if(args.length>0)
						inputFolderName_input_1_OR_2=args[0];
					
					String outputFileName = "";
					inputFolderName_input_1_OR_2="/Users/lenin/Downloads/crawleroutput/output/all.think.tank.from.world.aug.20.2015/merge/";
					outputFileName="/Users/lenin/Downloads/crawleroutput/output/all.think.tank.from.world.aug.20.2015/merged.txt";
					
					inputFolderName_input_1_OR_2=baseFolder;
					outputFileName=baseFolder+"20000.txt";
					String pathslash="//";
					//
					//String [] arr_list_of_files_input2=new String([]);
					String [] arr_list_of_files_input2=null;
					
					boolean is_add_file_name_of_source_as_last_column_of_eachLine_in_mergedFile
																		= false;
					
					//mergeFiles
					mergeFiles_horizontal( 
								inputFolderName_input_1_OR_2,
								arr_list_of_files_input2,
								outputFileName,
								true, //isMac
								is_add_file_name_of_source_as_last_column_of_eachLine_in_mergedFile
								);
					
				  }
				  catch(Exception e)
				  {
					  System.out.println("An exception occurred while calling Write method ..!!");	
					  e.printStackTrace();
				  }
							
				}
				 
}
