package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;




//import javax.json.Json;
//import javax.json.JsonObject;
//import javax.json.JsonReader;
//import javax.json.JsonValue;
//import javax.json.stream.JsonParser;
//import javax.json.stream.JsonParser.Event;
//
//import org.json.simple.parser.ParseException;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;













import com.google.gson.*;

public class Convert_givenString_to_Json {
	
	// convert a Token to JSON (THIS IS NOT GENERIC ONE ). It is for NYT, LATER make GENERIC ONE
	public static String readFile_eachLine_convert_a_Token_to_JSON_FOR_NYT_N_write2CSVfile(
																			   String baseFolder, 
																		       String input_File_containing_JSONtoken,
																			   int    token_index_that_contains_JSON_String, //starts from 1
																			   String YES_filter_for_eachLine_of_inputFile,
																			   String NO_filter_for_eachLine_of_inputFile,
																			   TreeMap<String, String> mapSS_interested_SectionNames
																			   ){
		System.out.println("input_jsonFile:"+input_File_containing_JSONtoken);
		TreeMap<String,TreeMap<String, String>> mapOut=new TreeMap<String,TreeMap<String, String>>();
		TreeMap<Integer, String> mapEachLines=new TreeMap<Integer, String>();
		int cumm_count_world=0;
		int cumm_count_us=0;
		int cumm_count_national=0;
		int cumm_count_foreign=0;
		int ERR_JSON_NULL=0; int ERR_TOKEN_NUMBERS_INSUFFICIENT=0;
		try{
			FileWriter writer_debug=new FileWriter(new File(baseFolder+"debug.txt"));
//			mapEachLines=
//			readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
//			readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(input_jsonFile, 
//																					   -1, -1);
			
			BufferedReader reader=new BufferedReader(new FileReader(new File(input_File_containing_JSONtoken)));
			String currLine="";
			// each line
			//for(int i:mapEachLines.keySet()){
			int lineNo=0;
			FileWriter writer=new FileWriter(new File(input_File_containing_JSONtoken+"_CSV.txt"));
			TreeMap<String, String> map_already_proc_line  = new TreeMap<String, String>();
			TreeMap<String, String> mapSS_Distinct_SectionNames_ALL=new TreeMap<String, String>(); 
			//mapSS_Distinct_SectionNames_ALL:{"Arts"=, "Blogs"=, "Business Day"=, "Multimedia"=, "Multimedia/Photos"=, "N.Y. / Region"=, "Opinion"=, "Paid Death Notices"=, "Science"=, "T:Style"=, "Technology"=, "Travel"=, "U.S."=, "World"=, "arts"=, "blogs"=, "business day"=, "false"=, "multimedia"=, "multimedia/photos"=, "n.y. / region"=, "opinion"=, "paid death notices"=, "science"=, "t:style"=, "technology"=, "travel"=, "u.s."=, "world"=, null=}
			
			
			//each line 
			while ((currLine = reader.readLine()) != null) {
				try{
				// skip
				if(map_already_proc_line.containsKey(Calc_checksum.calc_checksum_for_string(currLine))){
						System.out.println(" duplicate line" );
						continue;
				}
				//yes filter 
				if(	  YES_filter_for_eachLine_of_inputFile.length()>0 
				   && currLine.indexOf(YES_filter_for_eachLine_of_inputFile)==-1){
					System.out.println("continue as YES filter not matched ");
					continue;
				}
				if(	  NO_filter_for_eachLine_of_inputFile.length()>0 
						   && currLine.indexOf(NO_filter_for_eachLine_of_inputFile)>=0){
							System.out.println("continue as NO filter  matched ");
							continue;
					}
					
				System.out.println("------");
				lineNo++;
				//System.out.println("currLine:"+currLine);
				//currLine=mapEachLines.get(i);
				String []s=currLine.split("!!!");
				if(s.length<token_index_that_contains_JSON_String){
					System.out.println("continue on len: "+s.length);
					ERR_TOKEN_NUMBERS_INSUFFICIENT++;
					continue;
				}
				
				String jsonString=s[token_index_that_contains_JSON_String-1]; //token_index_that_contains_JSON_String
				JsonObject json=convert_givenString_to_JSON(jsonString);
				//
				if(json==null){
					System.out.println("continue on NULL: "+s.length+" string:"+jsonString);
					writer_debug.append("continue on NULL: "+s.length+" \njsonString:"+jsonString+"\n");
					
					ERR_JSON_NULL++;
					// try to correct broken JSON
					//String new_jsonString=jsonString.substring(0 , jsonString.lastIndexOf(",") ) +"}]}}";
					String new_jsonString=jsonString.substring(0 , jsonString.lastIndexOf(",") ) +
							"]},\"status\":\"ok\",\"copyright\":\"copyright (c) 2013 the new york times company.  all rights reserved.\"}";
					writer_debug.append("continue on NULL: "+s.length+" \nnew_jsonString:"+new_jsonString+"\n");
					writer_debug.flush();
					
					json=convert_givenString_to_JSON(new_jsonString);
					
					if(json==null){
						writer_debug.append("  NOTE: ...still invalid \n");writer_debug.flush();	
						continue;
					}
				}
				//convert
				mapOut=convert_givenString_to_JSON_For_NYT(baseFolder, json, jsonString ,  lineNo, mapSS_interested_SectionNames,  writer);
				int curr_count_world=0;
				int curr_count_us=0;
				int curr_count_national=0;
				int curr_count_foreign=0;
				TreeMap<String,String> mapCurr_Statistics= mapOut.get("3");
				TreeMap<String,String> mapSS_Distinct_SectionNames= mapOut.get("4");
				//add to global section name
				for(String sec_name:mapSS_Distinct_SectionNames.keySet()){
					mapSS_Distinct_SectionNames_ALL.put(sec_name, "");
				}
				
				if(mapCurr_Statistics.containsKey("3"))
					curr_count_world=Integer.valueOf(mapCurr_Statistics.get("3")); //world
				if(mapCurr_Statistics.containsKey("4"))
					curr_count_us=Integer.valueOf(mapCurr_Statistics.get("4")); //us
				if(mapCurr_Statistics.containsKey("5"))
					curr_count_national=Integer.valueOf(mapCurr_Statistics.get("5")); //national
				if(mapCurr_Statistics.containsKey("6"))
					curr_count_foreign=Integer.valueOf(mapCurr_Statistics.get("6")); //foreign
				cumm_count_foreign  =cumm_count_foreign+curr_count_foreign;
				cumm_count_national =cumm_count_national+curr_count_national;
				cumm_count_us		=cumm_count_us+curr_count_us;
				cumm_count_world	=cumm_count_world+curr_count_world;
				System.out.println("cumm_count_foreign:"+cumm_count_foreign);
				System.out.println("cumm_count_national:"+cumm_count_national);
				System.out.println("cumm_count_us:"+cumm_count_us);
				System.out.println("cumm_count_world:"+cumm_count_world);
				
				//get distinct keywords
				}
				catch(Exception e){
					System.out.println("dont catch error");
				}
				
				map_already_proc_line.put(Calc_checksum.calc_checksum_for_string(currLine), "");
			}
			//}
			System.out.println("FINAL cumm_count_foreign:"+cumm_count_foreign +" lineNo:"+lineNo);
			System.out.println("FINAL cumm_count_national:"+cumm_count_national);
			System.out.println("FINAL cumm_count_us:"+cumm_count_us);
			System.out.println("FINAL cumm_count_world:"+cumm_count_world);
		    System.out.println("mapSS_Distinct_SectionNames_ALL:"+mapSS_Distinct_SectionNames_ALL);
		}
		catch(Exception e ){
			System.out.println("ERROR input_jsonFile:"+input_File_containing_JSONtoken+" e.msg:"+e.getMessage());
			e.printStackTrace();
		}
		System.out.println("ERR_JSON_NULL:"+ERR_JSON_NULL+"<->ERR_TOKEN_NUMBERS_INSUFFICIENT:"+ERR_TOKEN_NUMBERS_INSUFFICIENT);
		return "";
	}
	
	//convert given string to JSON
	public static JsonObject convert_givenString_to_JSON(String json_String){
		 //JSONParser parser = new JSONParser();
		System.out.println(json_String);
		//json = "{\"Success\":true,\"Message\":\"Invalid access token.\"}";
		JsonParser jsonParser = new JsonParser();
		JsonObject jo=null;
		try{
			jo = (JsonObject)jsonParser.parse(json_String);
		}
		catch(Exception e){
			
		}
		return jo;
	}
	
	// convert given string to JSON for nytimes and get features ( keywords, byline etc.)
	// Calculates statistics
	public static TreeMap<String,TreeMap<String, String>> convert_givenString_to_JSON_For_NYT(
																							  String        baseFolder,
																							  JsonObject 	jo,
																							  String   		jo_String_equivalent, //debuggin
																							  int			lineNo, //debuggin
																							  TreeMap<String, String> mapSS_interested_SectionNames,
																							  FileWriter    writer_outFile){
		TreeMap<String, String> mapSS_Curr_KeywordNameValue=new TreeMap<String, String>();
		TreeMap<String, String> mapSS_Counter_ByLine=new TreeMap<String, String>();
		TreeMap<String, String> mapSS_Statisics=new TreeMap<String, String>();
		TreeMap<String, String> mapSS_Distinct_SectionNames=new TreeMap<String, String>();
		
		TreeMap<String,TreeMap<String, String>> mapOut=
												new TreeMap<String,TreeMap<String, String>>();
		String str_keywords="";
	    String str_byline="";
		int count_num_of_national=0;int count_num_of_foreign=0;
		int count_num_of_us=0;int count_num_of_world=0;
		
		try{
			
		
		 //JSONParser parser = new JSONParser();
		//System.out.println(json_String);
		//json = "{\"Success\":true,\"Message\":\"Invalid access token.\"}";
//		JsonParser jsonParser = new JsonParser();
//		JsonObject jo = (JsonObject)jsonParser.parse(json_String);
		String result="";
		int count_available_no_articles_with_byline=0;
		int count_available_no_articles_with_keywords=0;
		
		if(lineNo==1){ //add header
		    writer_outFile.append("url!!!snip!!!lpara!!!sect!!!pdate!!!keyw!!!organiz!!!original!!!MAPkeyw!!!MAPbline\n");
		    writer_outFile.flush();
		}
	    
//		System.out.println(jo.get(param).getAsString());
		Gson gson = new Gson();
		//Response response = gson.fromJson(json, Response.class);
		JsonObject  jobject = jo.getAsJsonObject();
	    jobject = jobject.getAsJsonObject("response");
	     
	    JsonArray jarray = jobject.getAsJsonArray("docs");
//	    jobject = jarray.get(0).getAsJsonObject();
//	    String result = jobject.get("web_url").toString();
//	    System.out.println(result +" "+jarray.size());
	    int cnt=0;
	    // each feed has 10 articles (sometimes may be lesser)
	    while(cnt<10){
	    	System.out.println("-----------------"+cnt);
	    	try{
	    		jarray.get(cnt);
	    	}
	    	catch(Exception e){
	    		cnt++;
	    		System.out.println("error: lineNo:"+lineNo+" cnt:"+cnt+" string(line)->"+jo_String_equivalent);
	    		continue;
	    	}
	    		
	    	  //jarray = jobject.getAsJsonArray("docs");
		    jobject = jarray.get(cnt).getAsJsonObject();
		    result = jobject.get("web_url").toString();
		    String keywords = jobject.get("keywords").toString();
		    String pub_date = jobject.get("pub_date").toString();
		    String snippet = jobject.get("snippet").toString();
		    String lead_paragraph = jobject.get("lead_paragraph").toString();
		    String news_desk= jobject.get("news_desk").toString();
		    String section_name = jobject.get("section_name").toString();
		    //is interested section 
		    if(!mapSS_interested_SectionNames.containsKey(section_name.replace("\"","") .toLowerCase()) ){
		    	System.out.println("NOT interested section ->"+section_name);
		    	cnt++;
		    	continue;
		    }
		    
		    //JsonArray BYLINE = jobject.getAsJsonArray("byline");
		    String BYLINE = jobject.get("byline").toString();
		    mapSS_Distinct_SectionNames.put( section_name, "");
		    //
		    if(news_desk.toLowerCase().indexOf("u.s")>=0 ||
		       section_name.toLowerCase().indexOf("u.s")>=0
		    		){
		    	count_num_of_us++;
		    }
		    if(news_desk.toLowerCase().indexOf("world")>=0 ||
				       section_name.toLowerCase().indexOf("world")>=0
				    		){
				count_num_of_world++;
			}
		    if(news_desk.toLowerCase().indexOf("foreign")>=0 ||
				       section_name.toLowerCase().indexOf("foreign")>=0
				    		){
				count_num_of_foreign++;
			}
		    if(news_desk.toLowerCase().indexOf("national")>=0 ||
				       section_name.toLowerCase().indexOf("national")>=0
				    		){
				count_num_of_national++;
		    }
		    
		    try{
		    JsonObject obj =  jobject.get("byline").getAsJsonObject();
		    //Iterator iterator = obj.entrySet();

				for (Entry<String, JsonElement> entry : obj.entrySet())
				{
					String key=entry.getKey() ; String value=entry.getValue().toString();
				    System.out.println("byline:"+key+ "/" + value);
				    mapSS_Counter_ByLine.put(key, value);
				    count_available_no_articles_with_byline++;
				}
		    }
		    catch(Exception e){
		    	System.out.println("error: ****"+e.getMessage());
		    }
		    try{
			    JsonArray obj =  jobject.getAsJsonArray("keywords");
			    //Iterator iterator = obj.entrySet();
			    	System.out.println("keyword.size: "+obj.size());
			    	
			    	int cnt10=0;
			   	    while(cnt10<obj.size()){
			   	    	System.out.println(cnt10+" "+obj.get(cnt10));
			   	    	JsonObject obj2 =obj.get(cnt10).getAsJsonObject();
				   	 	for (Entry<String, JsonElement> entry : obj2.entrySet())
						{
				   	 		String key=entry.getKey() ; String value=entry.getValue().toString();
						    System.out.println("keyword<N,V> =>"+key + " <-> "
						    					+ value);
						    count_available_no_articles_with_keywords++;
						    mapSS_Curr_KeywordNameValue.put(key, value);
						}
			   	    	cnt10++;
			   	    }

//					for (Entry<String, JsonElement> entry : obj.entrySet())
//					{
//					    System.out.println("keyword N,V:"+entry.getKey() + "/" + entry.getValue());
//					}
			    }
			    catch(Exception e){
			    	//System.out.println("error:keyword ****"+e.getMessage());
			    	System.out.println();
			    	e.printStackTrace();
			    }

		    mapSS_Statisics.put("1", String.valueOf(count_available_no_articles_with_byline));
		    mapSS_Statisics.put("2", String.valueOf(count_available_no_articles_with_keywords));
		    mapSS_Statisics.put("3", String.valueOf(count_num_of_world)); //world
		    mapSS_Statisics.put("4", String.valueOf(count_num_of_us)); //us
		    mapSS_Statisics.put("5", String.valueOf(count_num_of_national)); //national
		    mapSS_Statisics.put("6", String.valueOf(count_num_of_foreign)); //foreign
		    
		    mapOut.put("1", mapSS_Counter_ByLine);
		    mapOut.put("2", mapSS_Curr_KeywordNameValue);
		    mapOut.put("3", mapSS_Statisics);
		    mapOut.put("4", mapSS_Distinct_SectionNames);
		    
		    System.out.println(  "\n url:"+result 
		    					+"\n keywords:"+ mapSS_Curr_KeywordNameValue
		    					+"\n section_name:"+section_name
		    					+"\n BYLINE:"+mapSS_Counter_ByLine
		    					+"\n pub_date:"+pub_date
		    					+"\n "+jarray.size());


		    writer_outFile.append(result
		    					  +"!!!"+snippet
		    					  +"!!!"+lead_paragraph
		    					  +"!!!"+section_name
		    					  +"!!!"+pub_date
		    					  +"!!!"+mapSS_Curr_KeywordNameValue.get("value")
		    					  +"!!!"+mapSS_Counter_ByLine.get("organization")
		    					  +"!!!"+mapSS_Counter_ByLine.get("original")
		    					  +"!!!"+mapSS_Curr_KeywordNameValue //MAPkeyword
		    					  +"!!!"+mapSS_Counter_ByLine // MAPbyline
		    					  +"\n");
		    
		    
		    //writer_outFile.append("url!!!snip!!!lpara!!!+sect!!!pdate!!!keyw!!!organiz!!!original!!!MAPkeyw!!!MAPbline\n");
		    
		    // writer_outFile.flush();
		    
	//	    int bylinesize=BYLINE.size();
//		    int cnt2=0;
//		    while(cnt2<bylinesize){
////		    	JsonObject jobject2 = BYLINE.get(cnt2).getAsJsonObject();
////		    	System.out.println("\n person->person:"+jobject2.get("person")
////		    						+" organization->"+jobject2.get("organization")
////		    						);
//		    	cnt2++;
//		    }
		    
		    
		    cnt++;
	    }
	    
		}//
		catch(Exception e){
			e.printStackTrace();
		}
	    
	    return mapOut;
	}
	//
	public static String  readJSON(String inputString){
		//create JsonReader object
         
        /**
         * We can create JsonReader from Factory also
        JsonReaderFactory factory = Json.createReaderFactory(null);
        jsonReader = factory.createReader(fis);
        */
          
         
        return "";
	}
	
	//main
	public static void main(String[] args) throws IOException {
		System.out.println("here");
		TreeMap<String,String> mapout= new TreeMap<String, String>();
		String s="[{"+
					"\"key\":\"value\","+
					"\"key1\":[234234.32,adfasdf],"+
					"\"key2\":[arrayvalue,arrayvalue1,arrayvalue2]"+
					"}]";
		
		String t="{\"phonetype\":\"N95\",\"cat\":\"WP\"}";
//		//readJSON(s);
//		//convert_givenString_to_JSON(t,"cat");

		try {
//			String temp=
//			readFile_CompleteFile_to_a_Single_String_onefile.readFile_CompleteFile_to_a_Single_String(
//					"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data/samplejson.json");

			String baseFolder="/Users/lenin/Downloads/#crawleroutput/nyt/attempt.1/";
			String inputBigFile_Having_JSONToken=baseFolder+"20140403.txt";
			String YES_filter_for_eachLine_of_inputFile=""; //"\"section_name\":\"world\"";
			String NO_filter_for_eachLine_of_inputFile="";
			System.out.println("inputBigFile_Having_JSONToken:"+inputBigFile_Having_JSONToken);
			
			TreeMap<String, String> mapSS_interested_SectionNames=new TreeMap<String, String>();
			//NOTE: LOWER CASE GIVE
			mapSS_interested_SectionNames.put("n.y. \\/ region", "");mapSS_interested_SectionNames.put("N.Y. \\/ Region", "");
			mapSS_interested_SectionNames.put("n.y. / region", "");mapSS_interested_SectionNames.put("N.Y. / Region", "");
			mapSS_interested_SectionNames.put("u.s.", ""); mapSS_interested_SectionNames.put("U.S.", "");
			mapSS_interested_SectionNames.put("world", "");mapSS_interested_SectionNames.put("World", "");

			//	NOTE: FILTER which section with mapSS_interested_SectionNames
			//convert_givenString_to_JSON(temp);
			readFile_eachLine_convert_a_Token_to_JSON_FOR_NYT_N_write2CSVfile
																		( baseFolder,
																		  inputBigFile_Having_JSONToken,
																		  2, //token_index_that_contains_JSON_String
																		  YES_filter_for_eachLine_of_inputFile,
																		  NO_filter_for_eachLine_of_inputFile, 
																		  mapSS_interested_SectionNames
																		);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//crawler.readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
		//readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
		
	}
}