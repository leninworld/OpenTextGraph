package crawler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.TreeMap;
// class
public class Http_preWiki_PageProcess_Append_SubTitle {

	// When extract a wikipedia page, append the subtitle with the following lines. 
	// Example: Copy this link http://en.wikipedia.org/wiki/List_of_hospitals_in_Massachusetts to "inFile" input
	// Variable "state" -> getting appended to alert string prepared
	public static TreeMap<Integer,String> preProcAppendSubTitle(String inFile, String subTitle, String state) {
		TreeMap<Integer,String> mapOut = new TreeMap();
		String line = ""; int counter=0; String lastSubTitle="";
		// Read the mapping to drill-down
		HashMap<String, String> mapDrillDown = new HashMap();
		String cTitle = "";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				counter++;
				line=line.toLowerCase();
				if(line.length()<=1) continue;
				cTitle=line+" "+lastSubTitle;
				//
				if(line.indexOf(subTitle) == -1 || line.indexOf("hospital") >=0 ){
					if(cTitle.replace(lastSubTitle, "").length() >15) 
							System.out.println(line+" "+lastSubTitle+" "+state);
					else{
//						System.out.println(" not right ---->"+cTitle+"#"+lastSubTitle+"#"+cTitle.replace(lastSubTitle, "")+
//											"#"+cTitle.replace(lastSubTitle, "").length());
					}
				}
				//
				if(line.indexOf(subTitle) >= 0 && line.indexOf("hospital") == -1 ){
					lastSubTitle=line;
				}
				mapOut.put(counter, line);
			} //
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	// When extract a wikipedia page, append the subtitle with the following lines. 
	// Example: Copy this link http://en.wikipedia.org/wiki/List_of_beaches to "inFile" input
	public static TreeMap<Integer,String> preProcWikiBeachAppendSubTitle(String inFile, 
												String subTitle, 
												String state,
												String outFile,
												boolean isIncludeEmptySubTitle) {
		TreeMap<Integer,String> mapOut = new TreeMap();
		String line = ""; int counter=0; String lastSubTitle="";
		// Read the mapping to drill-down
		HashMap<String, String> mapDrillDown = new HashMap();
		String cTitle = "";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			FileWriter writer = new FileWriter(outFile);
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				counter++;
				line=line.toLowerCase();
				if(line.length()<=1) continue;
				cTitle=line+" "+lastSubTitle;
				//
				if(line.indexOf(subTitle) == -1 || line.indexOf("hospital") >=0 ){
					if(cTitle.replace(lastSubTitle, "").length() >15) {
							System.out.println(line+" "+lastSubTitle+" "+state);
							writer.append(line+" "+lastSubTitle+" "+state+"\n"); writer.flush();
					}
					else{
						if(isIncludeEmptySubTitle==true){
							writer.append(line+" "+lastSubTitle+" "+state+"\n"); writer.flush();
						}
//						System.out.println(" not right ---->"+cTitle+"#"+lastSubTitle+"#"+cTitle.replace(lastSubTitle, "")+
//											"#"+cTitle.replace(lastSubTitle, "").length());
					}
				}
				//
				if(line.indexOf(subTitle) >= 0 && line.indexOf("hospital") == -1 ){
					lastSubTitle=line;
				}
				mapOut.put(counter, line);
			
			} //
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return mapOut;
	}
	// main
	public static void main(String[] args) {
//		preProcAppendSubTitle("/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE/crawler/data/file.us.massachusetts.txt",
//								"#", // county or city
//								"massachusetts"); // name of province or state
		
		preProcWikiBeachAppendSubTitle(
				"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data/listofbeach.all.country.txt",
				"#", // county or city
				"",
				"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/out.listofbeach.all.country.txt",
				false
				); // name of province or state (getting appended to alert string prepared)
		
	}

}