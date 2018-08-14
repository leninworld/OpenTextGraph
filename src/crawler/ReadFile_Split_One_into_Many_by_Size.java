package crawler;

import java.io.*;
import java.util.TreeMap;

 /**
   * A class to take a file and split it into smaller files 
   * (for example, for fitting a file on floppy disks).
   * <P>To execute the program to split files apart, pass the -split
   * option and specify a filename:<BR>
   * <I>java FileSplitter -split bigFile.zip</I>
   * <P>To join a file back together, specify the -join flag and
   * give the base filename:<BR>
   * <I>java FileSplitter -join bigFile.zip</I><BR>
   * @author Keith Trnka
   */
 public class ReadFile_Split_One_into_Many_by_Size
 	{
 	/** 
 	  * a constant representing the size of a chunk that would go on a floppy disk.
 	  * 1.4 MB is used instead of 1.44 MB so that it isn't too close to the limit.
 	  */
 	public static final long floppySize = (long)(1.4 * 1024 * 1024);
 	
 	/** the maximum size of each file "chunk" generated, in bytes */
 	public static long chunkSize = 2000000*5;
 	//2mb = 2000000 
 	
 	// split by line number (2 fold cross validation)
 	public static TreeMap<String,String> split_by_line_Count_2FoldcrossValidation(
									 											String 	inFile,
									 											int 	number_of_output_File,
									 											int 	index_having_label_4_inFile,
									 											String	delimiter_4_inFile,
									 											String  label_for_TP,
									 											String  label_for_TN
 																				){
 		TreeMap<String,String> mapOut=new TreeMap<String, String>();
 		int lineNo=0; String line="";
 		try {
 			TreeMap<Integer, String> map_seq_eachLine=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
															readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
															(  inFile, 
															   -1, // 	startline, 
															   -1, //  	endline,
															   " debug_label",
															   false // isPrintSOP
															);
 			
 			System.out.println("--------------------------------------------------------");
 			int total_no_lines=map_seq_eachLine.size();
			
 			int no_line_for_each_out_split_file=total_no_lines/number_of_output_File;
 			int start_line=1; int end_line=no_line_for_each_out_split_file;
 			
 			FileWriter writer=null;
// 			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));

 			int c=1;
 			TreeMap<Integer, Integer> map_FileID_StartLine=new TreeMap<Integer, Integer>();
 			TreeMap<Integer, Integer> map_FileID_EndLine=new TreeMap<Integer, Integer>();
 			//
 			while(c	<= number_of_output_File){
 				writer=new FileWriter(new File(inFile+".SET."+c+"."+".txt"));
 				
 				map_FileID_StartLine.put(c, start_line);
 				map_FileID_EndLine.put(c, end_line);
 				
 				start_line=end_line+1;
 				end_line=end_line+end_line;
 				c++;
 			}
 			System.out.println("total_no_lines:"+total_no_lines);
 			System.out.println("Input File:"+inFile);
 			System.out.println("number_of_output_File:"+number_of_output_File);
 			
 			////////////////////////////////
 			TreeMap<Integer, Integer> map_TP_lineNo_as_KEY=new TreeMap<Integer, Integer>();
 			TreeMap<Integer, Integer> map_TN_lineNo_as_KEY=new TreeMap<Integer, Integer>();
 			
			  lineNo=0;
			  BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
			  //
			  while ((line = reader.readLine()) != null) {
				  lineNo++;
				  String [] arr_ = line.split(delimiter_4_inFile);
//				  System.out.println("line:"+lineNo+" arr_.len:"+arr_.length);
				  //true positive
				  if(arr_[index_having_label_4_inFile-1].equals(label_for_TP)){
					  map_TP_lineNo_as_KEY.put(lineNo, -1);
				  }
				  else if(arr_[index_having_label_4_inFile-1].equals(label_for_TN)){
					  map_TN_lineNo_as_KEY.put(lineNo, -1);
				  }
			  } // while ((line = reader.readLine()) != null) {
 			//////////////////////////
			  
			  int half_lineNo_for_TP=map_TP_lineNo_as_KEY.size()/2;
			  int half_lineNo_for_TN=map_TN_lineNo_as_KEY.size()/2;
			  
			  //write FIRST half of the file
			  writer=new FileWriter(new File(inFile+".SET."+"1"+"."+".txt"));
			  int cnt=0;
			  //TP
			  for(int lineNo2:map_TP_lineNo_as_KEY.keySet()){
				  cnt++;
				  if(cnt<=half_lineNo_for_TP){
					  writer.append(map_seq_eachLine.get(lineNo2)+"\n");
					  writer.flush();
				  }
			  }
			  cnt=0;
			  //TN
			  for(int lineNo3:map_TN_lineNo_as_KEY.keySet()){
				  cnt++;
				  if(cnt<=half_lineNo_for_TN){
					  writer.append(map_seq_eachLine.get(lineNo3)+"\n");
					  writer.flush();
				  }
			  }
			  
			  //write SECOND half of the file
			  writer=new FileWriter(new File(inFile+".SET."+"2"+"."+".txt"));
			  cnt=0;
			  //TP
			  for(int lineNo2:map_TP_lineNo_as_KEY.keySet()){
				  cnt++;
				  if(cnt>half_lineNo_for_TP){
					  writer.append(map_seq_eachLine.get(lineNo2)+"\n");
					  writer.flush();
				  }
			  }
			  cnt=0;
			  //TN
			  for(int lineNo3:map_TN_lineNo_as_KEY.keySet()){
				  cnt++;
				  if(cnt>half_lineNo_for_TN){
					  writer.append(map_seq_eachLine.get(lineNo3)+"\n");
					  writer.flush();
				  }
			  }
			  
 			c=1;
 			// process "number_of_output_File" times
// 			while(c	<= number_of_output_File){
// 				//
// 				writer=new FileWriter(new File(inFile+".SET."+c+"."+".txt"));
// 				
// 				int lcl_start_line=-1;
// 				int lcl_end_line=-1;
// 				lcl_start_line=map_FileID_StartLine.get(c);
// 				lcl_end_line=map_FileID_EndLine.get(c);
// 				System.out.println("processing.. c=="+c +" out of "+number_of_output_File+" start line:"+lcl_start_line
// 									+" end_line:"+lcl_end_line
// 									+" map_TP_lineNo_as_KEY.size:"+map_TP_lineNo_as_KEY.size()
// 									+" map_TN_lineNo_as_KEY.size:"+map_TN_lineNo_as_KEY.size()
// 									+" split file:"+inFile+".SET."+c+"."+".txt");
// 				lineNo=0;
// 				reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
//				  //
//				  while ((line = reader.readLine()) != null) {
//					  lineNo++;
//					  // 
//					  if( lineNo>=lcl_start_line &&
//						  lineNo<=lcl_end_line ){
//						  writer.append(line+"\n");
//						  writer.flush();
//					  }
//					  
//				  }
//				  c++;
//				  writer.close();
// 			}
				  line=null;
				  writer.close();
//				  reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
 		return mapOut;
 	}
 	
 	// split by line number
 	public static TreeMap<String,String> split_by_line_Count(
 											String 	inFile,
 											int 	number_of_output_File
 											){
 		TreeMap<String,String> mapOut=new TreeMap<String, String>();
 		int lineNo=0; String line="";
 		try {
 			System.out.println("--------------------------------------------------------");
 			int total_no_lines= ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
								readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
																				(  inFile, 
																				   -1, // 	startline, 
																				   -1, //  	endline,
																				   " debug_label",
																				   false // isPrintSOP
																				).size();
				
 			
 			int no_line_for_each_out_split_file=total_no_lines/number_of_output_File;
 			int start_line=1; int end_line=no_line_for_each_out_split_file;
 			
 			FileWriter writer=null;
// 			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));

 			int c=1;
 			TreeMap<Integer, Integer> map_FileID_StartLine=new TreeMap<Integer, Integer>();
 			TreeMap<Integer, Integer> map_FileID_EndLine=new TreeMap<Integer, Integer>();
 			//
 			while(c	<= number_of_output_File){
 				writer=new FileWriter(new File(inFile+".SET."+c+"."+".txt"));
 				
 				map_FileID_StartLine.put(c, start_line);
 				map_FileID_EndLine.put(c, end_line);
 				
 				start_line=end_line+1;
 				end_line=end_line+end_line;
 				c++;
 			}
 			System.out.println("total_no_lines:"+total_no_lines);
 			System.out.println("Input File:"+inFile);
 			System.out.println("number_of_output_File:"+number_of_output_File);
 			
 			c=1;
 			// processs "number_of_output_File" times
 			while(c	<= number_of_output_File){
 				//
 				writer=new FileWriter(new File(inFile+".SET."+c+"."+".txt"));
 				
 				int lcl_start_line=-1;
 				int lcl_end_line=-1;
 				lcl_start_line=map_FileID_StartLine.get(c);
 				lcl_end_line=map_FileID_EndLine.get(c);
 				
 				System.out.println("processing.. c=="+c +" out of "+number_of_output_File+" start line:"+lcl_start_line
 									+" end_line:"+lcl_end_line+" split file:"+inFile+".SET."+c+"."+".txt");
 				lineNo=0;
 				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
				  //
				  while ((line = reader.readLine()) != null) {
					  lineNo++;
					  
					  if(lineNo>=lcl_start_line &&
						 lineNo<=lcl_end_line){
						  writer.append(line+"\n");
						  writer.flush();
					  }
//					  else if(lineNo > end_line 
//							  && cnt<number_of_output_File
//							  ){
//						  start_line=lineNo;
//						  end_line=end_line+no_line_for_each_out_split_file;
//						  cnt++;
//						  writer=new FileWriter(new File(inFile+".SET."+cnt+"."+".txt"));
//						  writer.append(line+"\n");
//						  writer.flush();
//						  System.out.println("2:"+start_line+"!!!"+end_line+"!!!cnt:"+cnt);
//					  }
//					  else if(cnt==number_of_output_File){ //last file , write everything
//						  writer.append(line+"\n");
//						  writer.flush();
//					  }
					  
				  }
				  c++;
				  writer.close();
 			}
				  line=null;
				  writer.close();
//				  reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
 		return mapOut;
 	}
 	
 	/**
 	  * split the file specified by filename into pieces, each of size
 	  * chunkSize except for the last one, which may be smaller
 	  */
 	public static TreeMap<String,String> split_by_size(String filename, 
 													   long param_chunkSize) 
 								throws FileNotFoundException, IOException
 		{
 		TreeMap<String,String> mapOut=new TreeMap<String,String>();
 		// open the file
 		BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename));
 		
 		if(param_chunkSize<0)
 			param_chunkSize=chunkSize;
 		 
 		
 		// get the file length
 		File f = new File(filename);
 		long fileSize = f.length();
 		
 		// loop for each full chunk
 		int subfile;
 		for (subfile = 0; subfile < fileSize / param_chunkSize; subfile++)
 			{
 			// open the output file
 			BufferedOutputStream out = new BufferedOutputStream(
 								new FileOutputStream(filename + "." + subfile+".txt"));
 			
 			mapOut.put(filename + "." + subfile+".txt", filename + "." + subfile+".txt");
 			
 			String t="";
 			
 			int count_Char=0;
 			// write the right amount of bytes
 			for (int currentByte = 0; currentByte < param_chunkSize; currentByte++)
 				{
 				
 				//String t= (char) content;
 				//System.out.println((char) in.read());
 				// load one byte from the input file and write it to the output file
 				out.write(in.read());
 				count_Char++;
 				
 				//
 				if(count_Char%10000==0)
 					System.out.println(t);
 					count_Char=0;
 				}
 				
 			// close the file
 			out.close();
 			}
 		
 		// loop for the last chunk (which may be smaller than the chunk size)
 		if (fileSize != param_chunkSize * (subfile - 1))
 			{
 			// open the output file
 			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filename + "." + subfile));
 			
 			// write the rest of the file
 			int b;
 			while ((b = in.read()) != -1)
 				out.write(b);
 				
 			// close the file
 			out.close();			
 			}
 		
 		// close the file
 		in.close();
 		return mapOut;
 		}
 		
 	/**
 	  * list all files in the directory specified by the baseFilename
 	  * , find out how many parts there are, and then concatenate them
 	  * together to create a file with the filename <I>baseFilename</I>.
 	  */
 	public static void join(String baseFilename) throws IOException
 		{
 		int numberParts = getNumberParts(baseFilename);

 		// now, assume that the files are correctly numbered in order (that some joker didn't delete any part)
 		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(baseFilename));
 		for (int part = 0; part < numberParts; part++)
 			{
 			BufferedInputStream in = new BufferedInputStream(new FileInputStream(baseFilename + "." + part));

 			int b;
 			while ( (b = in.read()) != -1 )
 				out.write(b);

 			in.close();
 			}
 		out.close();
 		}
 	
 	/**
 	  * find out how many chunks there are to the base filename
 	  */
 	private static int getNumberParts(String baseFilename) throws IOException
 		{
 		// list all files in the same directory
 		File directory = new File(baseFilename).getAbsoluteFile().getParentFile();
 		final String justFilename = new File(baseFilename).getName();
 		String[] matchingFiles = directory.list(new FilenameFilter()
 			{
 			public boolean accept(File dir, String name)
 				{
 				return name.startsWith(justFilename) && name.substring(justFilename.length()).matches("^\\.\\d+$");
 				}
 			});
 		return matchingFiles.length;
 		}
 	//main
 	public static void main(String[] args) throws Exception
		{
		if (args.length != 2)
			{
				System.out.println("Must specify a flag -split or -join and a file argument. The file argument for splitting is the file to split and for joining is the base filename to join on.");
				//System.exit(0);
			}
		
		//inputFile="/Users/lenin/Downloads/Feeds-5-Jun-.dummy1/Breaking";
		
			//file name input
//			args[0]="-split";
//			args[1]="/Users/lenin/Downloads/Feeds-5-Jun-.dummy1/Breaking";
			
			String inputFile="/Users/lenin/Downloads/Feeds-5-Jun-.dummy1/EMM Alert India";
			inputFile="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/tmp/tf-idf.txt_noStopWord_WORD_ID.txt_format2.txt2_added_LABEL_crfiitb_maxent_output.txt";
			inputFile="/Users/lenin/Downloads/reu-cdean/RawData/2014_only_all_dataIDs_ONLY.txt";
			
			long chunkSize2 = 2000000*3000;
		 	//2mb = 2000000 
			////// Flag////// ////// ////// 
			// -split - split by size
			// -join - join
			// -split2 - split by line
			String flag="-split2";
		try
			{
			//split by size of file
			if (flag.equalsIgnoreCase("-split")){
				System.out.println("split");
				split_by_size(inputFile, chunkSize2);
			}
			else if (flag.equalsIgnoreCase("-join")){
				System.out.println("join");
				join(inputFile);
			}
			//split by size of line count
			else
				{
				//
				split_by_line_Count(  inputFile
									, 2 );
//				System.out.println("The first argument must be an option:");
//				System.out.println("\t-split: split the specified file");
//				System.out.println("\t-join: join all splitter outfiles with the specified base filename");
				//System.exit(0);
				}
			}
		catch (FileNotFoundException e)
			{
			System.out.println("File not found: " );
			}
		catch (IOException e)
			{
			System.out.println("IO Error");
			}
		}
 	
 	}