package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

public class ReadFile_reading_2_OPML_files_output_MisMatch {
	
		// compare TWO OPML files (exists in inFile_1 and not in inFile_2)
		public static TreeMap<String, String> readFile_reading_2_OPML_files_output_MisMatch(
																						 String 	inFile_1,
																						 String 	inFile_2,
																						 String 	outFile
																						 ){
			
			TreeMap<String, String> mapOut = new TreeMap<String, String>();
			TreeMap<String, String> map_inFile_1 = new TreeMap<String, String>();
			TreeMap<String, String> map_inFile_2 = new TreeMap<String, String>();
			String line="";
			try{
				System.out.println("100.outFile:"+outFile);
				FileWriter writer= new FileWriter(new File(outFile) );
				System.out.println("inFile_1:"+inFile_1);
				BufferedReader reader_1 = new BufferedReader(new FileReader(inFile_1));
				BufferedReader reader_2 = new BufferedReader(new FileReader(inFile_2));
				// 	read each line of given file
				while ((line = reader_1.readLine()) != null) {
					int begin_index=line.indexOf("xmlUrl=\""); //xmlUrl // htmlUrl cant be used
					int end_index=line.indexOf(" ", begin_index+"xmlUrl=\"".length()+3);
					if(begin_index>0 && end_index>0){
						System.out.println("good:" +begin_index+" "+ end_index+
											" "+ line.substring(begin_index, end_index));
						map_inFile_1.put(line.substring(begin_index, end_index),"");
					}
					else{
						System.out.println("err:"+begin_index+" "+end_index+" "+line);
					}
						
				}
				//
				while ((line = reader_2.readLine()) != null) {
					int begin_index=line.indexOf("xmlUrl=\"");
					int end_index=line.indexOf(" ", begin_index+"xmlUrl=\"".length()+3);
					if(begin_index>0 && end_index>0){
						map_inFile_2.put(line.substring(begin_index, end_index),"");
						System.out.println("good:" +begin_index+" "+ end_index+
										" "+ line.substring(begin_index, end_index));
					}
					else{
						System.out.println("err:"+begin_index);//+" "+end_index+"---->line:"+line);
					}
				}
				
				//not exists
				for(String xmlURL:map_inFile_1.keySet()){
					if(!map_inFile_2.containsKey(xmlURL)){
						xmlURL=xmlURL.replace("xmlUrl=", "").replace("\"", "");
						System.out.println("not exists:"+xmlURL);
						mapOut.put(xmlURL, "");
						writer.append( xmlURL +"\n");
						writer.flush();
					}
				}
				
				System.out.println("MISMATCH RSS feed - mapOut.size:"+mapOut.size());
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return mapOut;
		}
		
		//main
		public static void main(String[] args) throws IOException {
			String baseFolder="";
			baseFolder="/Users/lenin/Downloads/OPML(2-May)-thunderbird/tmp/";
			//
			String inFile_2=baseFolder+"thunderbird.opml";
			String inFile_1=baseFolder+"1.opml";
			
			String outFile=baseFolder+"out_mismatch.txt";
			// mismatch
			TreeMap<String, String> mapOut=
			readFile_reading_2_OPML_files_output_MisMatch(inFile_1,
														  inFile_2,
														  outFile
														 );
			
			// 
//			readFile_convert_from_url_text_to_opml_format.readFile_convert_from_url_text_to_opml_format(
//																 outFile, 
//																 outFile +"_opml.opml"
//																,false // is_Append_outFile
//																,-1 // startline
//																,-1 // endline
//																);
			System.out.println("mapOut.size:"+mapOut.size());
			
		}

}
