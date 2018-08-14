package crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

//
public   class LoadGoogleAlert_FileToMap {

		// output -> <counter,eachline>
		public  static TreeMap<Integer,String> loadEachGoogleAlertLineToMap(String inFile,
																			String delimiter_4_inFile,
																			boolean is_header_present
																		    ) {
			TreeMap<Integer,String> mapOut = new TreeMap();
			String line = ""; int counter=0;
			// Read the mapping to drill-down
			HashMap<String, String> mapDrillDown = new HashMap();
			try {
				BufferedReader reader = new BufferedReader(new FileReader(inFile));
				// read each line of given file
				while ((line = reader.readLine()) != null) {
					counter++;
					
					if(is_header_present==true && counter==1) continue;
					
					line=line.replace("#","").replace(delimiter_4_inFile, "###") // DELIMITER
							.replace(" - ", " ").replace("(", " ").replace(")", "").replace(" county", " ")
							.replace("[1]", " ").replace("[2]", " ").replace("[3]", " ").replace("[4]", " ").replace("[5]", " ")
							.replace("[6]", " ").replace("[7]", " ").replace("[8]", " ").replace("[9]", " ").replace("[10]", " ")
							.replace(" – ", " ").replace("  ", " ").toLowerCase().replace("  ", " ")
							.replace("###"," AND ") // DELIMITER
							.replace("part AND of AND ", " ");
					
					if(counter<10)
						System.out.println("loadEachGoogleAlertLineToMap():"+line+" counter:"+counter);
					
					if(is_header_present==true && counter==1){}
					else{
						mapOut.put(counter, line );
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return mapOut;
		}
	// output -> <counter,eachline>
	public static TreeMap<Integer,String> loadEachLineToMap(String inFile,
															int startline, int endline) {
		TreeMap<Integer,String> mapOut = new TreeMap();
		String line = ""; int counter=0;
		// Read the mapping to drill-down
		HashMap<String, String> mapDrillDown = new HashMap();
		int newcounter=0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				counter++;
				if(endline>0){
					//
					if(counter>=startline && counter<=endline )
					    newcounter++;
					else
						continue;
					}
				else{
					newcounter++;
				}
				
				mapOut.put(newcounter, line);
			}
		}
		catch(Exception e){
			System.out.println("loadFileToMap():error 1101");
			e.printStackTrace();
		}
		return mapOut;
	}
	 
	// main
	public static void main(String[] args) {
	
		//loadEachLineToMap("");
		//loadEachGoogleAlertLineToMap("/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE/crawler/conf/googlealert.txt");
	}
	
}