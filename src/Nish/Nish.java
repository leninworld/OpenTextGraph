package Nish;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

public class Nish {
	
	
	public static void Nish(
						String inputFile
						
											){
		try {
			
			//white - 8,17
			//alaskan - 2,11
			//asian 3,12
			//hispanic - 5,14
			//black - 4,13
			//native - 6, 15
			//mixed - 7, 16
			//white - 8, 17
			FileWriter writer=new FileWriter(new File(inputFile+"OUT.txt"));

			TreeMap<Integer, String> map_inFile_EachLine_of_document_f1=new TreeMap<Integer, String>();
			TreeMap<Integer, String> map_inFile_EachLine_of_document_f2=new TreeMap<Integer, String>();
			
			// loading
			map_inFile_EachLine_of_document_f1=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 							.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line( 
					 																									inputFile, 
					 																									 -1, //start_line, //startline, 
																														 -1, //end_line, //endline,
																														 " map_inFile_EachLine_of_document ", //debug_label
																														 false //isPrintSOP
																														 );

			TreeMap<String, String> mapping_of_Race=new TreeMap<String, String>();
			mapping_of_Race.put("white", "8,17");
			mapping_of_Race.put("alaskan", "2,11");
			mapping_of_Race.put("asian", "3,12");
			mapping_of_Race.put("hispanic", "5,14");
			mapping_of_Race.put("black", "4,13");
			mapping_of_Race.put("native", "6,15");
			mapping_of_Race.put("mixed", "7,16");
			 
			
			//
			for(int seq:map_inFile_EachLine_of_document_f1.keySet()){
				//System.out.println("lineno:"+seq);
				if(seq<=2) {continue;}
				
				String eachLine=map_inFile_EachLine_of_document_f1.get(seq);
				String [] arr_eachLine=eachLine.split(",");
				//
				String currState=arr_eachLine[0];
				
			   //if(seq>20) break;
				
				//each race
				for(String eachRace:mapping_of_Race.keySet()){
					String []s2=mapping_of_Race.get(eachRace).split(",");
					int f1_intr_token_posit = Integer.valueOf(s2[0]);
					int f2_intr_token_posit =Integer.valueOf(s2[1]); 
					String t="";
					
					if(arr_eachLine[f1_intr_token_posit-1].indexOf(".")>=0)
						t=arr_eachLine[f1_intr_token_posit-1].substring(0 , arr_eachLine[f1_intr_token_posit-1].indexOf("."));
					else
						t=arr_eachLine[f1_intr_token_posit-1];
					
					System.out.println( "['"+eachRace+"','"+currState+"',"+
											 t  +","
											+  arr_eachLine[f2_intr_token_posit-1].substring(0 , arr_eachLine[f2_intr_token_posit-1].indexOf(".")) 
											+"]," );
					
					
				}
				
			}
		 
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	// Nish2
	public static void Nish2(
							String inputFile
			 				){
	try {
		

		TreeMap<Integer, String> map_inFile_EachLine_of_document_f1=new TreeMap<Integer, String>();
		TreeMap<Integer, String> map_inFile_EachLine_of_document_f2=new TreeMap<Integer, String>();
		
		// loading
		map_inFile_EachLine_of_document_f1=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
				 							.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line( 
				 																									inputFile, 
				 																									 -1, //start_line, //startline, 
																													 -1, //end_line, //endline,
																													 " map_inFile_EachLine_of_document ", //debug_label
																													 false //isPrintSOP
																													 );

		

		
		String header =map_inFile_EachLine_of_document_f1.get(1);
		
		String [] arr_header=header.split(",");
		
		String root="root.";
		System.out.println("id,value");
		System.out.println("root");
		// map
		for(int seq:map_inFile_EachLine_of_document_f1.keySet()){
			 
			
			//System.out.println("lineno:"+seq);
			if(seq<=2) {continue;}
			
			String eachLine=map_inFile_EachLine_of_document_f1.get(seq);
			String [] arr_eachLine=eachLine.split(",");
			
			String currState=arr_eachLine[0];
			String _1=arr_eachLine[1].substring( 0 , arr_eachLine[1].indexOf("."));
			String _2=arr_eachLine[2].substring( 0 , arr_eachLine[2].indexOf("."));
			String _3=arr_eachLine[3].substring( 0 , arr_eachLine[3].indexOf("."));
			String _4=arr_eachLine[4].substring( 0 , arr_eachLine[4].indexOf("."));
			String _5=arr_eachLine[5].substring( 0 , arr_eachLine[5].indexOf("."));
			String _6=arr_eachLine[6].substring( 0 , arr_eachLine[6].indexOf("."));
			String _7=arr_eachLine[7].substring( 0 , arr_eachLine[7].indexOf("."));
			String _8=arr_eachLine[8].substring( 0 , arr_eachLine[8].indexOf("."));
			String _9=arr_eachLine[9].substring( 0 , arr_eachLine[9].indexOf("."));
			String _10=arr_eachLine[10].substring( 0 , arr_eachLine[10].indexOf("."));
			 
			
			
			currState=root+currState;
			//
			System.out.println(  currState+",");
			System.out.println(  currState+"."+arr_header[1] +","+_1);
			System.out.println(  currState+"."+arr_header[2]+","+_2);
			System.out.println(  currState+"."+arr_header[3]+","+_3);
			System.out.println(  currState+"."+arr_header[4]+","+_4);
			System.out.println(  currState+"."+arr_header[5]+","+_5);
			System.out.println(  currState+"."+arr_header[6]+","+_6);
			System.out.println(  currState+"."+arr_header[7]+","+_7);
			System.out.println(  currState+"."+arr_header[8]+","+_8);
			System.out.println(  currState+"."+arr_header[9]+","+_9);
			System.out.println(  currState+"."+arr_header[10]+","+_10);
			
			
		}
		
		
	}
	catch(Exception e){
		e.printStackTrace();
	}
	
	}
	

	// main
    public static void main(String[] args) throws IOException {
    	
    	Nish("/Users/lenin/Downloads/Racial BuildupF.csv");
    	
    	//Nish2("/Users/lenin/Downloads/fundingdata.csv");
    	 
    }

}
