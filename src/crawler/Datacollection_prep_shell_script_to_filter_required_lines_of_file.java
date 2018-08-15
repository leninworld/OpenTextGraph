package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Datacollection_prep_shell_script_to_filter_required_lines_of_file {
	
	//datacollection_prep_shell_script_to_filter_required_lines_of_file
	 public static void datacollection_prep_shell_script_to_filter_required_lines_of_file(
			 															String baseFolder,
				 														String sourceFileName,
				 														String CSV_Tokens_to_filter_for,
				 														String Yes_Token_to_filter_for,
				 														String NO_Token_to_filter_for, // ex: grep -v sport
				 														String prefix_for_output_file,
				 														boolean isSOPprint
			 														){
		 try{
			 Stemmer stemmer = new Stemmer();
			 FileWriter writer=new FileWriter(baseFolder+"shell_output_cat_command.sh", true);
			 
			 String [] s =CSV_Tokens_to_filter_for.split(",");
			 int len_s=s.length;
			 int cnt=0; String temp=""; String output_writeLine="";
			 String string_all_files="";
			 File inFile=new File(sourceFileName);
			 String inFile_primaryFileName=inFile.getName();
			 // cnt 
			 while(cnt<len_s){
				 
//example: cat thehindu.allnewsArticles_thehindu_web_URL.txt_for_labeling.txt | grep fire | grep -v world  >> thehindu.mai.label.fire.india.txt
				 
				 //if single word, apply stemming
				 if(s[cnt].indexOf(" ")==-1 && s[cnt].indexOf("#")==-1 )
					 s[cnt]=stemmer.stem(s[cnt]);
				 
				 temp=" cat "+sourceFileName+" | grep -i "+s[cnt].replace("#", "");
				 
				 if(Yes_Token_to_filter_for.length()>0)
					 temp=temp+ "| grep -i "+Yes_Token_to_filter_for;
				 
				 
				 if(NO_Token_to_filter_for.length()>0)
					 temp=temp+ "| grep -v "+NO_Token_to_filter_for;
				 
				 //
				 temp=temp+" >> "+prefix_for_output_file+"_"+inFile_primaryFileName
						 					+ "_"+ s[cnt]+".txt";
				 
				 System.out.println(temp);
				 writer.append(temp+"\n");
				 writer.flush();
				 
				 string_all_files=string_all_files+" "+prefix_for_output_file+ s[cnt]+".txt";
				 //
				 output_writeLine=prefix_for_output_file+ s[cnt]+".txt";
				 
				 cnt++;
			 }
			 //output file
			 writer.append("cat "+string_all_files+" >> "+"all.txt");
			 writer.flush();
			  
			 System.out.println("cat "+string_all_files+" >> "+"all.txt");
			 System.out.println("Output file:"+baseFolder+"shell_output_cat_command.sh");
			 
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
	}
	 //wrapper
	 public static void WRAPPER_datacollection_prep_shell_script_to_filter_required_lines_of_file(
																			String baseFolder,
																			String CSV_Tokens_to_filter_for,
																			String Yes_Token_to_filter_for,
																			String NO_Token_to_filter_for, // ex: grep -v sport
																			String prefix_for_output_file,
																			boolean isSOPprint
			 																){
	 
		 try {
			 String sourceFileName="";
			 File f=new File(baseFolder);
			 String [] arr_file=(f.list());
			 int cnt=0;
			 //
			 while(cnt<arr_file.length){
				 	sourceFileName=baseFolder + arr_file[cnt];
				 	//readme.txt skill
				 	if(arr_file[cnt].toLowerCase().indexOf("readme.")>=0){
				 		cnt++; continue;
				 	}
				 	if(isSOPprint) //
				 		System.out.println("file:"+sourceFileName);
				 	//each file , call
				 	datacollection_prep_shell_script_to_filter_required_lines_of_file(
											 CSV_Tokens_to_filter_for,
											 sourceFileName,
											 CSV_Tokens_to_filter_for,
											 Yes_Token_to_filter_for,
											 NO_Token_to_filter_for, // ex: grep -v sport
											 prefix_for_output_file,
											 false //isSOPprint
							 				 );
				 cnt++;
			 }
			 
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		 
	 }
	  // main
	  public static void main(String[] args) throws IOException {
		     
		    String sourceFileName= "_merged_all_tokens_normalized_matched_merged_with_gTruth_TP_nodup_nodup_DATETIME_exists_AddLineNo.txt";
		    
		    // if put # in front of token, then it wont do stemming. example: #gas  
		    
			String CSV_Tokens_to_filter_for="2000\\!\\!\\!1\\!\\!\\!,2000\\!\\!\\!2\\!\\!\\!,2000\\!\\!\\!3\\!\\!\\!,2000\\!\\!\\!4\\!\\!\\!,2000\\!\\!\\!5\\!\\!\\!,2000\\!\\!\\!6\\!\\!\\!,2000\\!\\!\\!7\\!\\!\\!,2000\\!\\!\\!8\\!\\!\\!,2000\\!\\!\\!9\\!\\!\\!,2000\\!\\!\\!10\\!\\!\\!,2000\\!\\!\\!11\\!\\!\\!,2000\\!\\!\\!12\\!\\!\\!,2001\\!\\!\\!1\\!\\!\\!,2001\\!\\!\\!2\\!\\!\\!,2001\\!\\!\\!3\\!\\!\\!,2001\\!\\!\\!4\\!\\!\\!,2001\\!\\!\\!5\\!\\!\\!,2001\\!\\!\\!6\\!\\!\\!,2001\\!\\!\\!7\\!\\!\\!,2001\\!\\!\\!8\\!\\!\\!,2001\\!\\!\\!9\\!\\!\\!,2001\\!\\!\\!10\\!\\!\\!,2001\\!\\!\\!11\\!\\!\\!,2001\\!\\!\\!12\\!\\!\\!,2002\\!\\!\\!1\\!\\!\\!,2002\\!\\!\\!2\\!\\!\\!,2002\\!\\!\\!3\\!\\!\\!,2002\\!\\!\\!4\\!\\!\\!,2002\\!\\!\\!5\\!\\!\\!,2002\\!\\!\\!6\\!\\!\\!,2002\\!\\!\\!7\\!\\!\\!,2002\\!\\!\\!8\\!\\!\\!,2002\\!\\!\\!9\\!\\!\\!,2002\\!\\!\\!10\\!\\!\\!,2002\\!\\!\\!11\\!\\!\\!,2002\\!\\!\\!12\\!\\!\\!,2003\\!\\!\\!1\\!\\!\\!,2003\\!\\!\\!2\\!\\!\\!,2003\\!\\!\\!3\\!\\!\\!,2003\\!\\!\\!4\\!\\!\\!,2003\\!\\!\\!5\\!\\!\\!,2003\\!\\!\\!6\\!\\!\\!,2003\\!\\!\\!7\\!\\!\\!,2003\\!\\!\\!8\\!\\!\\!,2003\\!\\!\\!9\\!\\!\\!,2003\\!\\!\\!10\\!\\!\\!,2003\\!\\!\\!11\\!\\!\\!,2003\\!\\!\\!12\\!\\!\\!,2004\\!\\!\\!1\\!\\!\\!,2004\\!\\!\\!2\\!\\!\\!,2004\\!\\!\\!3\\!\\!\\!,2004\\!\\!\\!4\\!\\!\\!,2004\\!\\!\\!5\\!\\!\\!,2004\\!\\!\\!6\\!\\!\\!,2004\\!\\!\\!7\\!\\!\\!,2004\\!\\!\\!8\\!\\!\\!,2004\\!\\!\\!9\\!\\!\\!,2004\\!\\!\\!10\\!\\!\\!,2004\\!\\!\\!11\\!\\!\\!,2004\\!\\!\\!12\\!\\!\\!,2005\\!\\!\\!1\\!\\!\\!,2005\\!\\!\\!2\\!\\!\\!,2005\\!\\!\\!3\\!\\!\\!,2005\\!\\!\\!4\\!\\!\\!,2005\\!\\!\\!5\\!\\!\\!,2005\\!\\!\\!6\\!\\!\\!,2005\\!\\!\\!7\\!\\!\\!,2005\\!\\!\\!8\\!\\!\\!,2005\\!\\!\\!9\\!\\!\\!,2005\\!\\!\\!10\\!\\!\\!,2005\\!\\!\\!11\\!\\!\\!,2005\\!\\!\\!12\\!\\!\\!,2006\\!\\!\\!1\\!\\!\\!,2006\\!\\!\\!2\\!\\!\\!,2006\\!\\!\\!3\\!\\!\\!,2006\\!\\!\\!4\\!\\!\\!,2006\\!\\!\\!5\\!\\!\\!,2006\\!\\!\\!6\\!\\!\\!,2006\\!\\!\\!7\\!\\!\\!,2006\\!\\!\\!8\\!\\!\\!,2006\\!\\!\\!9\\!\\!\\!,2006\\!\\!\\!10\\!\\!\\!,2006\\!\\!\\!11\\!\\!\\!,2006\\!\\!\\!12\\!\\!\\!,2007\\!\\!\\!1\\!\\!\\!,2007\\!\\!\\!2\\!\\!\\!,2007\\!\\!\\!3\\!\\!\\!,2007\\!\\!\\!4\\!\\!\\!,2007\\!\\!\\!5\\!\\!\\!,2007\\!\\!\\!6\\!\\!\\!,2007\\!\\!\\!7\\!\\!\\!,2007\\!\\!\\!8\\!\\!\\!,2007\\!\\!\\!9\\!\\!\\!,2007\\!\\!\\!10\\!\\!\\!,2007\\!\\!\\!11\\!\\!\\!,2007\\!\\!\\!12\\!\\!\\!,2008\\!\\!\\!1\\!\\!\\!,2008\\!\\!\\!2\\!\\!\\!,2008\\!\\!\\!3\\!\\!\\!,2008\\!\\!\\!4\\!\\!\\!,2008\\!\\!\\!5\\!\\!\\!,2008\\!\\!\\!6\\!\\!\\!,2008\\!\\!\\!7\\!\\!\\!,2008\\!\\!\\!8\\!\\!\\!,2008\\!\\!\\!9\\!\\!\\!,2008\\!\\!\\!10\\!\\!\\!,2008\\!\\!\\!11\\!\\!\\!,2008\\!\\!\\!12\\!\\!\\!,2009\\!\\!\\!1\\!\\!\\!,2009\\!\\!\\!2\\!\\!\\!,2009\\!\\!\\!3\\!\\!\\!,2009\\!\\!\\!4\\!\\!\\!,2009\\!\\!\\!5\\!\\!\\!,2009\\!\\!\\!6\\!\\!\\!,2009\\!\\!\\!7\\!\\!\\!,2009\\!\\!\\!8\\!\\!\\!,2009\\!\\!\\!9\\!\\!\\!,2009\\!\\!\\!10\\!\\!\\!,2009\\!\\!\\!11\\!\\!\\!,2009\\!\\!\\!12\\!\\!\\!,2010\\!\\!\\!1\\!\\!\\!,2010\\!\\!\\!2\\!\\!\\!,2010\\!\\!\\!3\\!\\!\\!,2010\\!\\!\\!4\\!\\!\\!,2010\\!\\!\\!5\\!\\!\\!,2010\\!\\!\\!6\\!\\!\\!,2010\\!\\!\\!7\\!\\!\\!,2010\\!\\!\\!8\\!\\!\\!,2010\\!\\!\\!9\\!\\!\\!,2010\\!\\!\\!10\\!\\!\\!,2010\\!\\!\\!11\\!\\!\\!,2010\\!\\!\\!12\\!\\!\\!,2011\\!\\!\\!1\\!\\!\\!,2011\\!\\!\\!2\\!\\!\\!,2011\\!\\!\\!3\\!\\!\\!,2011\\!\\!\\!4\\!\\!\\!,2011\\!\\!\\!5\\!\\!\\!,2011\\!\\!\\!6\\!\\!\\!,2011\\!\\!\\!7\\!\\!\\!,2011\\!\\!\\!8\\!\\!\\!,2011\\!\\!\\!9\\!\\!\\!,2011\\!\\!\\!10\\!\\!\\!,2011\\!\\!\\!11\\!\\!\\!,2011\\!\\!\\!12\\!\\!\\!,2012\\!\\!\\!1\\!\\!\\!,2012\\!\\!\\!2\\!\\!\\!,2012\\!\\!\\!3\\!\\!\\!,2012\\!\\!\\!4\\!\\!\\!,2012\\!\\!\\!5\\!\\!\\!,2012\\!\\!\\!6\\!\\!\\!,2012\\!\\!\\!7\\!\\!\\!,2012\\!\\!\\!8\\!\\!\\!,2012\\!\\!\\!9\\!\\!\\!,2012\\!\\!\\!10\\!\\!\\!,2012\\!\\!\\!11\\!\\!\\!,2012\\!\\!\\!12\\!\\!\\!,2013\\!\\!\\!1\\!\\!\\!,2013\\!\\!\\!2\\!\\!\\!,2013\\!\\!\\!3\\!\\!\\!,2013\\!\\!\\!4\\!\\!\\!,2013\\!\\!\\!5\\!\\!\\!,2013\\!\\!\\!6\\!\\!\\!,2013\\!\\!\\!7\\!\\!\\!,2013\\!\\!\\!8\\!\\!\\!,2013\\!\\!\\!9\\!\\!\\!,2013\\!\\!\\!10\\!\\!\\!,2013\\!\\!\\!11\\!\\!\\!,2013\\!\\!\\!12\\!\\!\\!,2014\\!\\!\\!1\\!\\!\\!,2014\\!\\!\\!2\\!\\!\\!,2014\\!\\!\\!3\\!\\!\\!,2014\\!\\!\\!4\\!\\!\\!,2014\\!\\!\\!5\\!\\!\\!,2014\\!\\!\\!6\\!\\!\\!,2014\\!\\!\\!7\\!\\!\\!,2014\\!\\!\\!8\\!\\!\\!,2014\\!\\!\\!9\\!\\!\\!,2014\\!\\!\\!10\\!\\!\\!,2014\\!\\!\\!11\\!\\!\\!,2014\\!\\!\\!12\\!\\!\\!,2015\\!\\!\\!1\\!\\!\\!,2015\\!\\!\\!2\\!\\!\\!,2015\\!\\!\\!3\\!\\!\\!,2015\\!\\!\\!4\\!\\!\\!,2015\\!\\!\\!5\\!\\!\\!,2015\\!\\!\\!6\\!\\!\\!,2015\\!\\!\\!7\\!\\!\\!,2015\\!\\!\\!8\\!\\!\\!,2015\\!\\!\\!9\\!\\!\\!,2015\\!\\!\\!10\\!\\!\\!,2015\\!\\!\\!11\\!\\!\\!,2015\\!\\!\\!12\\!\\!\\!";
				   CSV_Tokens_to_filter_for="nepal";
			String NO_Token_to_filter_for=""; // ex: grep -v sport
			String Yes_Token_to_filter_for="";
			String prefix_for_output_file="out_";
			String baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/prepare.cronological.ds1/";
			
			// prepare shell script
//			 datacollection_prep_shell_script_to_filter_required_lines_of_file(
//					 												  baseFolder,
//																	  sourceFileName,
//																	  CSV_Tokens_to_filter_for,
//																	  Yes_Token_to_filter_for,
//																	  NO_Token_to_filter_for, // ex: grep -v sport
//																	  prefix_for_output_file
//																	  );
			 
			 //p16-> "#instabi,constitution,economy,employment,#gas,electricity,fuel,protests,strikes,bandhs,corruption,black marketing,profiteering,shortage,blockade,vandalism,accidents,traffic,commuting";
			 
			 //IMPORTANT: BEGIN wrapper for "datacollection_prep_shell_script_to_filter_required_lines_of_file"
			 WRAPPER_datacollection_prep_shell_script_to_filter_required_lines_of_file(  
					 												  baseFolder,
																	  CSV_Tokens_to_filter_for,
																	  Yes_Token_to_filter_for,
																	  NO_Token_to_filter_for, // ex: grep -v sport
																	  prefix_for_output_file,
																	  false //isSOPprint
																	  );
			 
			 // END wrapper for "datacollection_prep_shell_script_to_filter_required_lines_of_file"
			 
			//p8 - BEGIN CODE To run a date 
//			TreeMap<Integer,String> mapOut=get_NewsPaper_ArchiveURL.
//										 thehindu_print(
//												 		"//Users//lenin//Downloads//p8_ds1.txt", //outFile
//												 		"", //baseurl
//												 		2000,
//												 		2015
//												 		);
//			TreeMap<Integer,String> mapOut2=new TreeMap<Integer, String>();
//			//
//			for(int i:mapOut.keySet()){
//				String t=mapOut.get(i).replace("/01/", "/1/").replace("/02/", "/2/").replace("/03/", "/3/").replace("/04/", "/4/").replace("/05/", "/5/").replace("/06/", "/6/")
//						.replace("/07/", "/7/").replace("/08/", "/8/").replace("/09/", "/9/");
//				t=t.substring(0, t.lastIndexOf("/"));
//				t=t.replace("/", "\\!\\!\\!")+"\\!\\!\\!";
//				if(!mapOut2.containsValue(t))
//					mapOut2.put( i,  t);
//			}
//			System.out.println("mapOut2:"+mapOut2);
//			String t2="";
//			//prepare CSV string for method datacollection_prep_shell_script_to_filter_required_lines_of_file
//			for(Integer h:mapOut2.keySet()){
//				t2=t2+","+mapOut2.get(h);
//			}
//			System.out.println(t2);
			//p8 - END CODE To run a date 
		     
	  }
	
}
