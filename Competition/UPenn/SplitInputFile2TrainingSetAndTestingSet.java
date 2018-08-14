package UPenn;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import crawler.Crawler;

// split
public class SplitInputFile2TrainingSetAndTestingSet {
	
		//SplitInputFile2TrainingSetAndTestingSet
		public static void SplitInputFile2TrainingSetAndTestingSet(String inputFile, String trainingSetOutFile, String testSetOutFile){
			try{
				String line=null; BufferedReader reader = null;
				reader = new BufferedReader(new FileReader(inputFile));
				FileWriter writer1 = null, writer2 = null;
				writer1=new FileWriter(trainingSetOutFile);
				writer2=new FileWriter(testSetOutFile);
				//each line
				while ((line = reader.readLine()) != null) {
					if(line.indexOf("1 1:")>=0 || line.indexOf("0 1:")>=0){
						writer1.append(line+"\n"); writer1.flush();
					}
					
					if(line.indexOf("? 1:")>=0){
						writer2.append(line+"\n"); writer2.flush();
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

	
	public static void main(String[] args) throws IOException {
		Crawler c = new Crawler();
		//c.getSingleURLCrawler("http://www.fbi.gov/collections/terrorism?b_start:int=60");
		
	}
	
}
