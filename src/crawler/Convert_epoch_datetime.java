package crawler;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeMap;
import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

// converting EPOCH to DATETIME
public class Convert_epoch_datetime {
	
	// convert epoch 
	public static String convert_epoch_datetime(long inputEpoch){
		String formatted = "";
		try {
		        Date date = new Date(inputEpoch);
		        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		        format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		        formatted = format.format(date);
		        //System.out.println(formatted);
		        
		        //format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
		        //formatted = format.format(date);
		        //System.out.println(formatted);
			 
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return formatted;
	}
	
	//
	public static void convert_epoch_datetime_4_FILE(
												String baseFolder,
												String file1,
												boolean is_file1_has_header,
												String delimiter_for_file1,
												int    token_having_epoch,
												long    start_epoch_range,
												long    end_epoch_range,
												String OUTPUT_file3
											){
		
		int count_half_match=0;
		TreeMap<String, String> map_file1_keyword_bodytext=new TreeMap<String, String>();
		TreeMap<String, String> map_file2_keyword_bodytext=new TreeMap<String, String>();
		FileWriter writer=null;
		
		long curr_DATETIME=0L; 
		try {
			FileWriter writer_debug=new FileWriter(new File(baseFolder+"debug.txt"));
			writer=new FileWriter(new File(OUTPUT_file3));
			 
			// 
			TreeMap<Integer,String> map_file1=
						 ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																								  file1,
																	 							  -1, //startline, 
																	 							  -1, //endline,
																	 							  "f1 ", //debug_label
																	 							  false //isPrintSOP
																							 	  );
			
			//end of the file has HEADER
			int sz=map_file1.size();
			//HEADER at last line
			String header = map_file1.get(1);
			
			//
//			writer_debug.append("given file:"+file1+"\n");
//			writer_debug.append(map_file1.toString()+"\n");
//			writer_debug.append("header:"+header);
//			writer_debug.flush();
			
			//header 
			writer.append(header.replace("@", "!!!").replace("$", "")  +"\n");
			
			String conceachLine_OUTPUT="";
			
			//
			for(int seq:map_file1.keySet()){
				conceachLine_OUTPUT="";
				String []s=map_file1.get(seq).split(delimiter_for_file1);
				 
				System.out.println("len of tokens:"+s.length+" of lineno="+seq+" line:"+map_file1.get(seq));
				
				// 
				if(s.length<token_having_epoch || s.length==0 || (is_file1_has_header==true && seq==1)
						||s.length==1 || s.equals("")
				  ) {
					continue;
					}
				
				String curr_DATETIME_NEW="";
			
				curr_DATETIME= Long.valueOf(s[token_having_epoch-1]);
				///
				if(start_epoch_range>0 && end_epoch_range>0){
					//NOT in RANGE
					if(curr_DATETIME<start_epoch_range || curr_DATETIME >end_epoch_range){
						continue;
					}	
				}
				
				
				// convert EPOCH 2 DATETIME
				curr_DATETIME_NEW=convert_epoch_datetime(curr_DATETIME);
				System.out.println("epoch:"+curr_DATETIME+" "+curr_DATETIME_NEW);
				
				int c=0;
				//reconstruct to hold all tokens for OUTPUT
				while(c<s.length){
					//
					if(conceachLine_OUTPUT.length()==0){
						if(c==token_having_epoch-1){
							conceachLine_OUTPUT=curr_DATETIME_NEW; //DATETIME
						}
						else{
							conceachLine_OUTPUT=s[c];
						}
					}
					else{
						if(c==token_having_epoch-1){
							conceachLine_OUTPUT=conceachLine_OUTPUT+delimiter_for_file1+curr_DATETIME_NEW; //DATETIM
						}
						else{
							conceachLine_OUTPUT=conceachLine_OUTPUT+delimiter_for_file1+s[c];
						}
					}
					
					c++;
				}
				// OUTPUT
				writer.append(curr_DATETIME+delimiter_for_file1+conceachLine_OUTPUT.replace("@", "!!!").replace("$", "")  +"\n");
				writer.flush();
				
			}
			
		}
		catch(Exception e){
			
			e.printStackTrace();
		}
		
	}
	
	// main
	public static void main(String[] args) throws Exception{
		//
		String baseFolder="/Users/lenin/Downloads/#problems/p19/";
		String file1_=baseFolder+"crawled/CRAWLED_output.txt";
			   file1_=baseFolder+"C17693_C721_flows.txt";
		String OUTPUT_file3=baseFolder+"C17693_C721_flows_sample_CONVERED.txt";
		long start_epoch_range=459060;
		long end_epoch_range=605015;
		
		int token_having_epoch=1;
		String delimiter_for_file1=",";
		
		// main 
//		convert_epoch_datetime_4_FILE(
//									  baseFolder,
//									  file1_,
//									  true, //is_file1_has_header
//									  delimiter_for_file1,
//									  token_having_epoch,
//									  -1, // start_epoch_range,
//									  -1, //end_epoch_range,
//									  OUTPUT_file3
//									 );
		
		System.out.println(convert_epoch_datetime(871495));
		
	}

}
