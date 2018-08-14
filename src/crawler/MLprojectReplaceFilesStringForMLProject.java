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
import java.util.TreeMap;

	public class MLprojectReplaceFilesStringForMLProject {

		// merge files
		public static void mergeFiles(String inputFolderName, String outputFileName1, String slashtype){
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
	        FileWriter writer = new FileWriter(outputFileName1);

			//While loop to read each file and write into.
			for (int i=0; i < Filenames.length; i++)
			{
				
			  try{
				  File fileinput = new File(absolutePathOfInputFile+ slashtype + Filenames[i]);
				  System.out.println ("File reading::"+absolutePathOfInputFile+ slashtype + Filenames[i]);
				  System.out.println ("full path fileinput:"+fileinput.getAbsoluteFile());
				  //
				  Write( fileinput , fileinput , absolutePathOfInputFile,writer, i, Filenames[i]);
		
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
				// write
				public static void Write(File outputFileName,  File fileinput, String absolutePathOfInputFile 
										, FileWriter writer, int i, String primaryFileName ){
				String line = null ; int lineNo = 0;
				//String primaryFileName = absolutePathOfInputFile.substring( , endIndex)
				//String outputFileName1 =  absolutePathOfInputFile  + "\\output.txt";
				TreeMap< String, Integer> map=new TreeMap();
				try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileinput.getAbsoluteFile())));
				
				System.out.println ("Inside write method input file :"+ fileinput );
				System.out.println ("Inside write method "+ outputFileName +" : "+ reader);
				//System.out.println ("---------- ");
				
					System.out.println ("outputFileName :"+outputFileName);
					  //
					  while ((line = reader.readLine()) != null) {
						  
						  if(line.indexOf("?")>=0) {
							  lineNo++; continue;  
						  }
						  else if(line.indexOf("@")>=0) {
							  writer.append(line+ '\n');
							  writer.flush();
							  continue;
						  }
						  else if(line.indexOf("%")>=0) {
							  writer.append(line+ '\n');
							  writer.flush(); 
							  continue;
						  }
						  
						  lineNo++;
						   
						  line=line.replace(";12:", " 12:").replace(";13:", " 13:").replace(";14:", " 14:")
								   .replace(";15:", " 15:").replace(";16:", " 16:").replace(";17:", " 17:")
								   .replace(";18:", " 18:").replace(";19:", " 19:").replace(";20:", " 20:")
								   .replace(";21:", " 21:").replace(";22:", " 22:").replace(";23:", " 23:")
								   .replace(";00:", " 00:").replace(";01:", " 01:").replace(";02:", " 02:")
								   .replace(";03:", " 03:").replace(";04:", " 04:").replace(";05:", " 05:")
								   .replace(";06:", " 06:").replace(";07:", " 07:").replace(";08:", " 08:")
								   .replace(";09:", " 09:").replace(";10:", " 10:").replace(";11:", " 11:");
						  line=line.replace(";", ",");
						   
						  
						  String []s=line.split(",");
						  s[0]=s[0].replace("2006", "").replace("2007", "").replace("2008", "")
								   .replace("2009", "").replace("2010", "");
						  int k=s[0].indexOf("/");
						  System.out.println("1:"+s[0]);
						  s[0]=s[0].substring(0, k  );
						  System.out.println("2:"+s[0]);
						  	  int cnt=0;
						      //
							  int id=map.size();
							  
							  if(!map.containsKey(s[0]) ){
								  System.out.println(" not present "+s[0]+"<=>"+id);
								  map.put(s[0], id+1);
							  }
							  else{
								  System.out.println(" present "+s[0]);
							  }
						  String newline=""; cnt=0;
						  while(cnt<s.length){
								  
							  if(cnt==0){
								  newline=  String.valueOf(map.get(s[0]));
							  }
							  else{
								  newline=newline+","+s[cnt];
							  }
							  cnt++;
							  System.out.println(cnt+"<=>"+s.length+"<=>"+s[0]+"<=>"+id);
						  }
						  
						  
						  //System.out.println(reader.readLine());
						  //System.out.println("############# BEFORE APPENDING #############");
						  writer.append(newline+ '\n');
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
					String inputFolderName = "/Users/lenin/OneDrive/##TTU/*Course-MachineLearning/project/household";
					String outputFileName = "/Users/lenin/OneDrive/##TTU/*Course-MachineLearning/project/household_output.txt";
					mergeFiles(inputFolderName, outputFileName,"/");
				  }
				  catch(Exception e)
				  {
					  System.out.println("An exception occurred while calling Write method ..!!");	
					  e.printStackTrace();
				  }
							
				}
				
				 
}
