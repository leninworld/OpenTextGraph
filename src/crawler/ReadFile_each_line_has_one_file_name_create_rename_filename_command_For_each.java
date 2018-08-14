package crawler;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.util.TreeMap;

import  me.champeau.ld.UberLanguageDetector;

public class ReadFile_each_line_has_one_file_name_create_rename_filename_command_For_each {
 
	 
	// readFile_each_line_has_one_file_name_create_rename_filename_command_For_each
	public static void readFile_each_line_has_one_file_name_create_rename_filename_command_For_each(
																							String inputFile, 
																							String outFile ){
		BufferedReader br=null;
		try{
			br = new BufferedReader(new FileReader(inputFile));
			FileWriter writer= new FileWriter(new File(outFile));
			String line="";
			while( (line = br.readLine()) !=null){
				System.out.println("l:"+line);
				if(line.length()>=1)
				//writer.append( line.substring(1, line.length()) +"\n");
				writer.append( "mv "+line+" " +line.replace(".g.txt", ".g").replace("world","all")
							+"\n");
				writer.flush();
			}
			
			System.out.println("tes");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	 
	 
	//main
	public static void main(String[] args) throws IOException {
		TreeMap<String,String> mapout= new TreeMap<String, String>();
		
		//rss.us.tt.merged.nodup_pastResults.SET.3.._true_only
		//rss.us.tt.merged.nodup_isRSS_true.SET.3..txt
//		mapout=
//				readFile_load_File_To_Map
//				.readFile_load_Each_Line_of_Generic_File_To_Map_String_String(
//										"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.dup.SET.5.txt", 
//										-1, 
//										-1,
//										"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup.SET.5..txt",
//										false,
//										true);
		
		
		
				//System.out.println(mapout.size());
				
		//System.out.println("yahoo.com/2015/07".indexOf("2015/07"));
				
		readFile_each_line_has_one_file_name_create_rename_filename_command_For_each
							("/Users/lenin/Downloads/names.txt",
							 "/Users/lenin/Downloads/names_1.txt");
 
		System.out.println("clean:" );
		
		
/*

 http://api.nytimes.com/svc/search/v2/articlesearch.json?fq=news_desk:("National")&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
 http://api.nytimes.com/svc/search/v2/articlesearch.json?fq=news_desk:("U.S")&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
 http://api.nytimes.com/svc/search/v2/articlesearch.json?facet_field=day_of_week&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
http://api.nytimes.com/svc/search/v2/articlesearch.json?section_name="U.S."&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328

http://api.nytimes.com/svc/search/v2/articlesearch.json?news_desk="National Desk"&section_name="U.S."&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328
http://api.nytimes.com/svc/search/v2/articlesearch.json?type_of_material=News&begin_date=20140801&end_date=20140801&api-key=23c38a9337878b2ca1c10fca0afda77b:15:72969328


"type_of_material": "News",

  "section_name": "Front Page; U.S.",
  "news_desk": "National Desk",

*/		
				
//		String InputFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_pastResults.SET.3..txt";
//		String OutputFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_pastResults.SET.3.._true_only.txt";
//		readFile_readEachLine_For_a_pattern_IFmatched_WriteToAnotherFile.
//		readFile_readEachLine_For_a_pattern_IFmatched_WriteToAnotherFile(InputFile, 
//																		OutputFile, 
//																		"!!!true",
//																		true);
		
//		t(  	"/Users/lenin/Downloads/crawleroutput/output/all think tank from world  (aug 20 2015)/out_google_search-list-of-all-think-tank-from-world-from-wiki-list_2.txt.only.link.txt",
//	        	 "/Users/lenin/Downloads/crawleroutput/output/all think tank from world  (aug 20 2015)/out_google_search-list-of-all-think-tank-from-world-from-wiki-list_2.txt.only.link_clean.txt"
//	        	 );

		
		String a= "jer\u00e9 \\/longman";
		//a="body\/head (music group)";
		System.out.println(a.replace("/\\", "").replace("//", "").replaceAll("(\\r|\\n)", "") )	;
		String str= a.replaceAll("[\u0000-\u001f]", "").replaceAll("\\p{Cntrl}", "").replaceAll("\\\\", "")
					 .replaceAll("////", "").replaceAll("[^\\x00-\\x7F]", "");;
 
		System.out.println(":"+a.replaceAll("\\\\", "").replace("\\", "")+"::"+a.indexOf("/"));
		int s=a.indexOf("/");
		//
		//if(){
			
		//}
		
	}
}
