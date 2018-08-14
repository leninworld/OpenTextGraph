package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Sort_by_datetime_for_extracted_CSV_news_articles {

	
	// sort_by_datetime_for_extracted_CSV_news_articles
	public static void sort_by_datetime_for_extracted_CSV_news_articles(
																		String baseFolder,
																		String inputFile,
																		String outFile,
																		int index_of_token_datetime_in_inputFile
																		){
		
		FileWriter writer=null;
		TreeMap<Integer,String> map_inputFile_lineNo_datetimeTokenString=new TreeMap<Integer, String>();
		TreeMap<Integer,Integer> map_inputFile_lineNo_datetimeTokenInteger=new TreeMap<Integer, Integer>();
		TreeMap<Integer,Integer> map_inputFile_lineNo_DD=new TreeMap<Integer, Integer>();
		TreeMap<Integer,Integer> map_inputFile_lineNo_MM=new TreeMap<Integer, Integer>();
		TreeMap<Integer,Integer> map_inputFile_lineNo_YYYY=new TreeMap<Integer, Integer>();
		FileWriter writerDebug=null;
		String DD="",MM="",YYYY="";
		try {
			writer=new FileWriter(new File(outFile));
			writerDebug=new FileWriter(new File(baseFolder+"debug_datetime.txt"));
			//map_inputFile_only
			TreeMap<Integer,String> map_inputFile_only=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
								 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
										 												 inputFile, 
																					 	 -1, //startline, 
																						 -1, //endline,
																						 " reloading..", //debug_label
																						 false //isPrintSOP
																						 );
			String 	curr_datetime_new="";
			// eachLine 
			for( int seq:map_inputFile_only.keySet()){
				String orig_eachLine=map_inputFile_only.get(seq);
				
				String []s=orig_eachLine.split("!!!");
				String curr_datetime=s[index_of_token_datetime_in_inputFile-1];
				
				//add "0" ,when "4" change to 04
				curr_datetime=curr_datetime.replace(" 1 ", " 01 ").replace(" 2 ", " 02 ").replace(" 3 ", " 03 ").replace(" 4 ", " 04 ").replace(" 5 ", " 05 ").replace(" 6 ", " 06 ")
						.replace(" 7 ", " 07 ").replace(" 8 ", " 08 ").replace(" 9 ", " 09 ");
				
				curr_datetime=curr_datetime.replace("From", "").replace("-", "").replace(",", "");
				
				
//				System.out.println("curr_datetime:"+curr_datetime);
				// 
				int begin_=curr_datetime.indexOf(" Jan");
				
				if(begin_==-1) begin_=curr_datetime.indexOf(" Feb");if(begin_==-1) begin_=curr_datetime.indexOf(" Mar");
				if(begin_==-1) begin_=curr_datetime.indexOf(" Apr");if(begin_==-1) begin_=curr_datetime.indexOf(" May");
				if(begin_==-1) begin_=curr_datetime.indexOf(" Jun");if(begin_==-1) begin_=curr_datetime.indexOf(" Jul");
				if(begin_==-1) begin_=curr_datetime.indexOf(" Aug");if(begin_==-1) begin_=curr_datetime.indexOf(" Sep");
				if(begin_==-1) begin_=curr_datetime.indexOf(" Oct");if(begin_==-1) begin_=curr_datetime.indexOf(" Nov");
				if(begin_==-1) begin_=curr_datetime.indexOf(" Dec");
				
				begin_=begin_-3;
				
				if(begin_==-1)
					begin_=curr_datetime.toLowerCase().indexOf("om - ")+5; //from
				
				int end_=curr_datetime.indexOf(":",begin_);
				
				try{
				curr_datetime_new=curr_datetime.substring(begin_, end_);
				}
				catch(Exception e){
					System.out.println("curr_datetime:"+curr_datetime+" begin_:"+begin_+" end_:"+end_);
					continue;
//					e.printStackTrace();
				}
				 
				String orig_curr_datetime_new=curr_datetime_new;
	
//				System.out.println("orig_curr_datetime_new:"+orig_curr_datetime_new);
				 
				curr_datetime_new=curr_datetime_new.replace("-", "");
				
				curr_datetime_new=curr_datetime_new.substring(0, 12);
				
				map_inputFile_lineNo_datetimeTokenString.put(seq, curr_datetime_new);
				
				String
				curr_datetime_new2=curr_datetime_new.replace(" Jan ", "01").replace(" Feb ", "02").replace(" Mar ", "03").replace(" Apr ", "04").replace(" May ", "05")
						.replace(" Jun ", "06").replace(" Jul ", "07").replace(" Aug ", "08").replace(" Sep ", "09").replace(" Oct ", "10").replace(" Nov ", "11")
						.replace(" Dec ", "12"); 
				
				curr_datetime_new2=curr_datetime_new2.replace(" ", "");
//				System.out.println("curr_datetime_new2:"+curr_datetime_new2);
				 
				DD=curr_datetime_new2.substring(0, 2);  MM=curr_datetime_new2.substring(2, 4); 
				YYYY=curr_datetime_new2.substring(4, curr_datetime_new2.length());
				
				System.out.println("seq:"+seq+" date:"+curr_datetime_new+" new2:"+curr_datetime_new2+" orig:"+orig_curr_datetime_new
										+" DD:"+DD+" MM:"+MM+" YYYY:"+YYYY );
				
				//
				try{
					map_inputFile_lineNo_datetimeTokenInteger.put(seq, Integer.valueOf(curr_datetime_new2));
					map_inputFile_lineNo_YYYY.put(seq, Integer.valueOf(YYYY) );
					map_inputFile_lineNo_DD.put(seq, Integer.valueOf(DD));
					map_inputFile_lineNo_MM.put(seq, Integer.valueOf(MM));
				}
				catch(Exception e){
					System.out.println(" erorr in map_inputFile_lineNo_datetimeTokenInteger : ");
				}
				
				
			}
			
			TreeMap<Integer, TreeMap<Integer,Integer>> map_yyyy_map_lineNo_mmyyyy=new TreeMap<Integer, TreeMap<Integer,Integer>>();
			 
			//EACH YEAR, CREATE A NEW TREEMAP 
			for(int lineNo:map_inputFile_lineNo_datetimeTokenInteger.keySet()){
				System.out.println("lineNo:"+lineNo + " "+map_inputFile_lineNo_YYYY.containsKey(lineNo));
				
//				System.out.println("map_inputFile_lineNo_YYYY:"+map_inputFile_lineNo_YYYY);
				 
				int yyyy=map_inputFile_lineNo_YYYY.get(lineNo);
				if(!map_yyyy_map_lineNo_mmyyyy.containsKey(yyyy)){
					TreeMap<Integer,Integer> temp=new TreeMap<Integer, Integer>();
					temp.put(lineNo, map_inputFile_lineNo_MM.get(lineNo) + map_inputFile_lineNo_YYYY.get(lineNo) );
					map_yyyy_map_lineNo_mmyyyy.put(yyyy, temp);
				}
				else{
					TreeMap<Integer,Integer> temp=map_yyyy_map_lineNo_mmyyyy.get(yyyy);
					temp.put(lineNo, map_inputFile_lineNo_MM.get(lineNo) + map_inputFile_lineNo_YYYY.get(lineNo)  );
					map_yyyy_map_lineNo_mmyyyy.put(yyyy, temp);
				}
				
				
			}
			
			//iterate each year..
			int begin_year=1920;
			int end_year=2100;
			
			// 
			while(begin_year<=end_year){

				System.out.println("begin_year:"+begin_year);
				if(map_yyyy_map_lineNo_mmyyyy.containsKey(begin_year)){
					
					TreeMap<Integer,Integer> temp_map_yyyy_map_lineNo_mmyyyy=map_yyyy_map_lineNo_mmyyyy.get(begin_year);
					
					// 
					Map<Integer,Integer> temp_map_yyyy_map_lineNo_mmyyyy_SORTED=
										Sort_given_treemap.sortByValue_II_method4_ascending(temp_map_yyyy_map_lineNo_mmyyyy);
					
					//DEBUG 
					writerDebug.append("temp_map_yyyy_map_lineNo_mmyyyy_SORTED:"
										+temp_map_yyyy_map_lineNo_mmyyyy_SORTED+"\n");
					writerDebug.flush();
					
					// WRITING
					for(int lineno:temp_map_yyyy_map_lineNo_mmyyyy_SORTED.keySet()){
						writer.append(map_inputFile_only.get(lineno)+"\n");
						writer.flush();
					}
				
				}
				
				begin_year++;
			}
			
			



			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	
	// main
    public static void main (String[] args) throws IOException {
        
        String  baseFolder="/Users/lenin/Dropbox/#problems/p6/merged_used_for_INTELLIGENCE_course_7xxx/dummy.mergeall/ds11/";
        String inputFile=baseFolder+"mergedall-2016-T10-10-Topics-all-tokens.txt_ONLY_ENGLISH.txt_removed_NOISE.txt_REMOVE_STOPWORDS_FINAL.txt_MATCHED.txt";
        int index_of_token_datetime_in_inputFile=3;
        
        String outFile=inputFile+"_date_SORTED.txt";
        
        //sort_by_datetime_for_extracted_CSV_news_articles
        sort_by_datetime_for_extracted_CSV_news_articles(
        													baseFolder,
        													inputFile,
        													outFile,
        													index_of_token_datetime_in_inputFile
        													);
        
    }
}
