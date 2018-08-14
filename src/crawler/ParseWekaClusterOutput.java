package crawler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

//
public class ParseWekaClusterOutput {

	// parseOutClusterFromWeka
	public static void parseOutClusterFromWeka(String inFile,
												String outFile,
												String debug
												){
		try{
		FileWriter writer = new FileWriter(outFile);
		FileWriter writerDebug = new FileWriter(debug);
		BufferedReader br = new BufferedReader(new FileReader(inFile)); 
		String line=""; 
			//	
			while ((line = br.readLine()) != null) {
				if(line.indexOf(",")==-1) 
					continue;
				String [] s = line.split(",");
				writerDebug.append(s.length+"\n");
				writerDebug.flush();
				int c=0; String concLine="";
				//
				while(c<s.length){
					c++;
					if(c<=2 || c==s.length){
						concLine=concLine+","+s[c-1];
					}
				}
				writer.append(concLine+"\n");
				writer.flush();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//main
	public static void main(String[] args){
		String inFile = "";
		parseOutClusterFromWeka("/Users/lenin/Dropbox/#Data/m.txt",
						 		"/Users/lenin/Dropbox/#Data/dummy.mSelected.txt",
						 		"/Users/lenin/Dropbox/#Data/debug.mSelected.txt");
	}
	
}
