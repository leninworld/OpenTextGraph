package crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

public class ReadFile_load_Each_GoogleAlertLine_To_Map {
	
	// output -> <counter,eachline>
	public static TreeMap<Integer,String> readFile_load_Each_GoogleAlertLine_To_Map(String inFile) {
		TreeMap<Integer,String> mapOut = new TreeMap();
		String line = ""; int counter=0;
		// Read the mapping to drill-down
		HashMap<String, String> mapDrillDown = new HashMap();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				counter++;
				line=line.replace("#","").replace(",", " ").replace(" - ", " ").replace("(", " ").replace(")", "").replace(" county", " ")
						.replace("[1]", " ").replace("[2]", " ").replace("[3]", " ").replace("[4]", " ").replace("[5]", " ")
						.replace("[6]", " ").replace("[7]", " ").replace("[8]", " ").replace("[9]", " ").replace("[10]", " ")
						.replace(" – ", " ").replace("  ", " ").toLowerCase().replace("  ", " ")
						.replace(" "," AND ").replace("part AND of AND ", " ");
				System.out.println(line);
				mapOut.put(counter, line );
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return mapOut;
	}
	
	//main
	public static void main(String[] args) throws IOException {
				readFile_load_Each_GoogleAlertLine_To_Map("");
	}
	 
	

}
