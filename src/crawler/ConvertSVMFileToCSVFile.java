package crawler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConvertSVMFileToCSVFile {
	// (generic 19 Aug 2014) / Assume no missing data in between, but can be missing data after the end in each line.
	public static void convertSVMFileToCSVFile(String inputFile, String outputFile, int maxFeature){
		try{
			String line=null; BufferedReader reader = null;
			reader = new BufferedReader(new FileReader(inputFile));
			FileWriter writer1 = null, writer2 = null;
			writer1=new FileWriter(outputFile);
		 
			int FeatCount=1; String origLine="";
			//each line
			while ((line = reader.readLine()) != null) {
				   FeatCount=1;
				   line= line.replace("1 1:", "1,").replace("0 1:", "0,");
				   origLine=line;
				   // find each feature and replace <feature_no>:<value> with ,
				   while(FeatCount<=maxFeature){
					   line=line.replace(" "+FeatCount+":", ",");
					   FeatCount++;
				   }
				   line=line.replace(",,", "");
				   // 
				   if(origLine.indexOf("17:")==-1){
					  line=line+",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"; 
				   }
				   
					writer1.append(line+"\n"); writer1.flush();
			}//end of each line read
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//main
	public static void main(String[] args) throws IOException {
		//crawler c = new crawler();
		//c.getSingleURLCrawler("http://www.fbi.gov/collections/terrorism?b_start:int=60");
		
	}
	
}

	