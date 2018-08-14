package p21;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

// 
public class P21_scp_copyMISMATCH_fileName_diff_withONEFISH {
	
	//fileName_diff_withONEFISH
	public static void fileName_diff_withONEFISH(String []  arr_files_from_Onefish,
												 String FolderOfFiles_local){
		TreeMap<String,String> map_fileNameOnefish_asKEY=new TreeMap<String, String>();
		
		
		String new_Folder_for_diffFiles=FolderOfFiles_local+"diffFile/";
		//new folder 
		if(new File(new_Folder_for_diffFiles).exists())
			new File(new_Folder_for_diffFiles).mkdir();	
		
		try {
			
			int max_files=arr_files_from_Onefish.length;
			int c=0;
			while(c<max_files){
				map_fileNameOnefish_asKEY.put( arr_files_from_Onefish[c],"");
				c++;
			}
			File files=new File(FolderOfFiles_local);
			int num_notFOUND=0;
			//local
			String [] arr_files_local=files.list();
			 c=0;max_files=arr_files_local.length;
			 //
			 while(c<max_files){
				if(!map_fileNameOnefish_asKEY.containsKey(arr_files_local[c])){
					System.out.println(""+arr_files_local[c]);
					String source=FolderOfFiles_local + arr_files_local[c];
					String target=new_Folder_for_diffFiles + arr_files_local[c];
					
					if(!(new File(source).isDirectory())  && !(new File(target).isDirectory()) ) 
						//copy 2 new folder
						FileUtils.copyFile( new File(source), 
											new File(target));
					
					num_notFOUND++;
				}
				c++;
			 }
			
			 System.out.println("NUMBER of files in local:"+arr_files_local.length);
			 System.out.println("NUMBER of files in ONEFISH:"+arr_files_from_Onefish.length);
			 System.out.println("NOT FOUND:"+num_notFOUND);
			 System.out.println("new_Folder_for_diffFiles:"+new_Folder_for_diffFiles);
			 System.out.println(map_fileNameOnefish_asKEY);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}
	
	// main
	public static void main(String[] args) throws IOException {
		
		int Flag=2; //{1,2}
		String filesinOnefish="";
		//Flag 1-> set var "filesinOnefish"
		//Flag 2-> set var "input_file_1_LST_LTR_command_output_from_ONEFISH"
		String [] arr_onefish_files=null;
		 //Flag 2-
		String input_file_1_LST_LTR_command_output_from_ONEFISH
							="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data/p21/onefishuploadedfile.txt"; //Flag 2-
		TreeMap<String,String> mapTempLoad=new TreeMap<String, String>();
	
		if(Flag==1){
			filesinOnefish="Nov14_1week_categorical.g    March14_4week_categorical.g  June12_3week_categorical.g  Jan14_3week_categorical.g  Feb12_2week_categorical.g  Aug13_1week_categorical.g"+
							"Nov13_4week_categorical.g    March14_3week_categorical.g  June12_2week_categorical.g  Jan14_2week_categorical.g  Feb12_1week_categorical.g  Aug12_4week_categorical.g"+
							"Nov13_3week_categorical.g    March14_2week_categorical.g  June12_1week_categorical.g  Jan14_1week_categorical.g  Dec15_4week_categorical.g  Aug12_3week_categorical.g"+
							"Nov13_2week_categorical.g    March14_1week_categorical.g  July16_1week_categorical.g  Jan13_4week_categorical.g  Dec15_3week_categorical.g  Aug12_2week_categorical.g"+
							"Nov13_1week_categorical.g    March13_4week_categorical.g  July15_4week_categorical.g  Jan13_3week_categorical.g  Dec15_2week_categorical.g  Aug12_1week_categorical.g"+
							"Nov12_4week_categorical.g    March13_3week_categorical.g  July15_3week_categorical.g  Jan13_2week_categorical.g  Dec15_1week_categorical.g  April16_4week_categorical.g"+
							"Nov12_3week_categorical.g    March13_2week_categorical.g  July15_2week_categorical.g  Jan13_1week_categorical.g  Dec14_4week_categorical.g  April16_3week_categorical.g"+
							"Nov12_2week_categorical.g    March13_1week_categorical.g  July15_1week_categorical.g  Jan12_4week_categorical.g  Dec14_3week_categorical.g  April16_2week_categorical.g"+
							"Nov12_1week_categorical.g    March12_4week_categorical.g  July14_4week_categorical.g  Jan12_3week_categorical.g  Dec14_2week_categorical.g  April16_1week_categorical.g"+
							"May14_4week_categorical.g    March12_3week_categorical.g  July14_3week_categorical.g  Jan12_2week_categorical.g  Dec14_1week_categorical.g  April15_4week_categorical.g"+
							"May14_3week_categorical.g    March12_2week_categorical.g  July14_2week_categorical.g  Jan12_1week_categorical.g  Dec13_4week_categorical.g  April15_3week_categorical.g"+
							"May14_2week_categorical.g    March12_1week_categorical.g  July14_1week_categorical.g  Feb16_4week_categorical.g  Dec13_3week_categorical.g  April15_2week_categorical.g"+
							"May14_1week_categorical.g    June16_4week_categorical.g   July13_4week_categorical.g  Feb16_3week_categorical.g  Dec13_2week_categorical.g  April15_1week_categorical.g"+
							"May13_4week_categorical.g    June16_3week_categorical.g   July13_3week_categorical.g  Feb16_2week_categorical.g  Dec13_1week_categorical.g  April14_4week_categorical.g"+
							"May13_3week_categorical.g    June16_2week_categorical.g   July13_2week_categorical.g  Feb16_1week_categorical.g  Dec12_4week_categorical.g  April14_3week_categorical.g"+
							"May13_2week_categorical.g    June16_1week_categorical.g   July13_1week_categorical.g  Feb15_4week_categorical.g  Dec12_3week_categorical.g  April14_2week_categorical.g"+
							"May13_1week_categorical.g    June15_4week_categorical.g   July12_4week_categorical.g  Feb15_3week_categorical.g  Dec12_2week_categorical.g  April14_1week_categorical.g"+
							"May12_4week_categorical.g    June15_3week_categorical.g   July12_3week_categorical.g  Feb15_2week_categorical.g  Dec12_1week_categorical.g  April13_4week_categorical.g"+
							"May12_3week_categorical.g    June15_2week_categorical.g   July12_2week_categorical.g  Feb15_1week_categorical.g  Aug15_4week_categorical.g  April13_3week_categorical.g"+
							"May12_2week_categorical.g    June15_1week_categorical.g   July12_1week_categorical.g  Feb14_4week_categorical.g  Aug15_3week_categorical.g  April13_2week_categorical.g"+
							"May12_1week_categorical.g    June14_4week_categorical.g   Jan16_4week_categorical.g   Feb14_3week_categorical.g  Aug15_2week_categorical.g  April13_1week_categorical.g"+
							"March16_4week_categorical.g  June14_3week_categorical.g   Jan16_3week_categorical.g   Feb14_2week_categorical.g  Aug15_1week_categorical.g  April12_4week_categorical.g"+
							"March16_3week_categorical.g  June14_2week_categorical.g   Jan16_2week_categorical.g   Feb14_1week_categorical.g  Aug14_4week_categorical.g  April12_3week_categorical.g"+
							"March16_2week_categorical.g  June14_1week_categorical.g   Jan16_1week_categorical.g   Feb13_4week_categorical.g  Aug14_3week_categorical.g  April12_2week_categorical.g"+
							"March16_1week_categorical.g  June13_4week_categorical.g   Jan15_4week_categorical.g   Feb13_3week_categorical.g  Aug14_2week_categorical.g  April12_1week_categorical.g"+
							"March15_4week_categorical.g  June13_3week_categorical.g   Jan15_3week_categorical.g   Feb13_2week_categorical.g  Aug14_1week_categorical.g"+
							"March15_3week_categorical.g  June13_2week_categorical.g   Jan15_2week_categorical.g   Feb13_1week_categorical.g  Aug13_4week_categorical.g"+
							"March15_2week_categorical.g  June13_1week_categorical.g   Jan15_1week_categorical.g   Feb12_4week_categorical.g  Aug13_3week_categorical.g"+
							"March15_1week_categorical.g  June12_4week_categorical.g   Jan14_4week_categorical.g   Feb12_3week_categorical.g  Aug13_2week_categorical.g";
						// 
						filesinOnefish=filesinOnefish.replace(" ", "").replace(".g", ".g,");
						arr_onefish_files=filesinOnefish.split(",");
			}
		else if(Flag==2){
			//File_1
//			arr_onefish_files=(new File(input_file_1_LST_LTR_command_output_from_ONEFISH)).list();
			
			mapTempLoad = ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 .readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
																	 input_file_1_LST_LTR_command_output_from_ONEFISH, -1, -1, "",
																	 false, false, " already load"// debug_label
																	 , -1 //token_to_be_used_as_primarykey
																	 , false  // isSOPprint
																	 );
			arr_onefish_files=new String[mapTempLoad.size()];
			int c=0;
			for(String s:mapTempLoad.keySet()){
				arr_onefish_files[c]=s.substring(s.lastIndexOf(" ")+1,s.length()); //ls ltr last has the filename
				c++;
			}
		}
			
			System.out.println(" "+filesinOnefish);
			//File_2
			String FolderOfFiles_local="/Users/lenin/Downloads/reu-cdean/RawData/daily_DONE/daily-split-SEEDED/gbad/"; 
			// fileName_diff_withONEFISH
			fileName_diff_withONEFISH( arr_onefish_files,
									   FolderOfFiles_local
									   );
			
	}
}
