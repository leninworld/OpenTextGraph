package crawler;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

// first find can be a file with list of url (one url in each line)
// second file could be opml file. 
public class ReadFile_eachLine_removeBlackSlashUnicode_write_to_another_File {
		// read File
		public static void readFile_eachLine_removeBlackSlashUnicode_write_to_another_File(
																			String first_File,
																			String out_File,
																			boolean is_Append_outFile_exists
																			){
			TreeMap<Integer, String> mapFirstFileLines=new TreeMap<Integer, String>();
			String secondFile_CompleteString="";
			int existsCount=0; int notExistsCount=0;
			Runtime rs =  Runtime.getRuntime();
			int cnt=0;
			try {
				FileWriter writer_ExistsFile=new FileWriter(new File(out_File)
																	, is_Append_outFile_exists);
				
				mapFirstFileLines=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
						.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																	  	  first_File
																		, -1
																		, -1
																		, ""//debug_label
					        											, false // isSOPprint
																		);
 
				
				//
				for(int s:mapFirstFileLines.keySet()){
					
				    writer_ExistsFile.append(RemoveBackSlash.removeBackSlash((RemoveUnicodeChar.removeUnicodeChar(
				    						 mapFirstFileLines.get(s)))
				    						+"\n"
				    						));
				    writer_ExistsFile.flush();
					cnt++;
				}
				
				System.out.println("mapFirstFileLines.size:"+mapFirstFileLines.size());
				System.out.println("secondFile_CompleteString.len:"+secondFile_CompleteString.length());
				System.out.println("Exists Count:"+existsCount);
				System.out.println("NOT Exists Count:"+notExistsCount);
				writer_ExistsFile.close();
 
				rs.gc();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
		}
		// main
		public static void main(String[] args) {
			//
			String firstFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_isRSS_true.SET.1.copy.txt";
			//firstFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/file.us.50.thinktank.txt";
			firstFile="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/StagingSplitFiles_output/";

			//secondFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_isRSS_true.SET.1.copy.txt";
			String outFile="/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/dummy.output2/nytimes_world_2014-04-03-2014-05-02.txt.3_1.txt";
			
			readFile_eachLine_removeBlackSlashUnicode_write_to_another_File(
								firstFile,
								outFile,
								true //is_Append_outFile_exists
								);
		
		}

}
