package crawler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

//read top N lines
public class ReadFile_ouput_concLine {

	// read top N lines
	public static String readFile_ouput_concLine(String inputFile
											   ){
		System.out.println("readTopNline.1");
		String concLine="";
		try{
			//FileWriter writer  = new FileWriter(outputFile);
			String line="";
			System.out.println("readTopNline");
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			
			System.out.println("input:"+inputFile+"\noutput:");
				int cnt=0;
				//read each line of given file
	            while ((line = reader.readLine()) != null) {
	            	cnt++;
	            	concLine=concLine+line.toLowerCase();
	            	
	            	if(cnt%3000==0)
	            		System.out.println("3k next:"+cnt);
	            	
	            }
	            System.out.println("readTopNline.end");
		}
		catch(Exception e){
			System.out.println("readTopNline.catch");
			e.printStackTrace();
		}
		 
		return concLine;
	}
	 
	// main() 
	public static void main(String[] args) throws IOException {
		
		String WSbaseFolder="/Users/lenin/Downloads/Feeds-5-Jun-.dummy1/";
		 
		String fileName="";
		
		WSbaseFolder="/Users/lenin/Downloads/output/";
		fileName="dum.uptominute.output1.txt";
		
		System.out.println("reading input here..");
		// step 1
		String concLine=ReadFile_ouput_concLine.readFile_ouput_concLine(
								WSbaseFolder+fileName
								);
		System.out.println("input done");
		
		// step 2		
		TreeMap<Integer, String> mapOut=
			Find_Start_n_End_Index_Of_GivenString.getMatchedLines(concLine, 
												 "http:", ".html", false);
		//
		for(int i:mapOut.keySet()){
			System.out.println( mapOut.get(i) );
		}
		System.out.println("map.size:"+mapOut.size());
		
	}	
}