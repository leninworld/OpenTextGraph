package crawler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class PrepAlertStringsForCountryRStatesRCity {
 
	//Generate alert string for given set of country. Each country, each state, produce the key word
	 //see ReadME.txt
	public static void generateAlertStringListForEachCityRStateRCountry(
																		String inFile,
																		String inFileForAlertKeywords,
																		String ouFile
																		){
		String line = ""; String line2="";
		try{
			//read input files
			BufferedReader readerInAlertFile = new BufferedReader(new FileReader(inFileForAlertKeywords));
			BufferedReader readerCityRCountryRStateRAllTogether = null;
			FileWriter writer = new FileWriter(ouFile);
			// read each line of given file
			while ((line = readerInAlertFile.readLine()) != null) {
				readerCityRCountryRStateRAllTogether=new BufferedReader(new FileReader(inFile));
				//
				while ((line2 = readerCityRCountryRStateRAllTogether.readLine()) != null) {
					writer.append(line2+" "+line+"\n");
					writer.flush();
				}
			}
			writer.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//
	public static void main(String[] args) throws IOException {
		//crawler c = new crawler();
		//c.getSingleURLCrawler("http://www.fbi.gov/collections/terrorism?b_start:int=60");
		String folder = "/Users/lenin/Google Drive/#Data/";
		generateAlertStringListForEachCityRStateRCountry(
									  folder+"testInputCityList.txt",
									  folder+"testlistcrime.txt",
									  folder+"outList.txt"
									  );
	}
	
}
