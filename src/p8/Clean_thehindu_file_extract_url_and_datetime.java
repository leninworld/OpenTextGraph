package p8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class Clean_thehindu_file_extract_url_and_datetime {
	
	//clean_thehindu_file
	public static void clean_thehindu_file_extract_url_and_datetime(String baseFolder,
																	String inputFile,
										   							String outputFile){
		String line="";
		try {

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			FileWriter writer=new FileWriter(outputFile);
			int lineNo=0;
			
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				lineNo++;
				String []s =line.replace("!!!!!!","!!!"+"#"+"!!!").split("!!!");
				int beginIndex=line.indexOf("<meta name=\"DC.date.issued\"");
				int endIndex=line.indexOf(">", beginIndex+1);
				//
				if(beginIndex>=0 && endIndex >=0 && endIndex>=beginIndex){
					String dateTime=line.substring(beginIndex, endIndex).replace("<meta name=\"DC.date.issued\"", "").replace(">", "").replace(" content=\"", "").replace("\"", "").replace("/", "");
					
					
					System.out.println("lineNo:"+lineNo);
					writer.append(dateTime.substring(0, 7).replace("-", "#!#") +"!!!"+ s[0] +"!!!"+dateTime+"!!!"+dateTime.replace("-", "#!#")+"\n" );
					writer.flush();
				}
				else{
					System.out.println("NO MATCH lineNo:"+lineNo+" "+beginIndex+" "+endIndex);
				}
				 
			}// end while
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}	
	// main
	public static void main(String[] args) {
		
		String baseFolder="/Users/lenin/Downloads/#problems/p8/complete corpus/thehindu/";
		String inFile=baseFolder+"thehindu.only.full.article.4.txt";
		String outFile=baseFolder+"thehindu.only.full.article.4_URLnDateTime.txt";
		// clean_thehindu_file_extract_url_and_datetime
		clean_thehindu_file_extract_url_and_datetime(baseFolder,
													 inFile, 
													 outFile);
		
	}

}