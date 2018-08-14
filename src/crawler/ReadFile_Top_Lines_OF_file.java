package crawler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//read top N lines
public class ReadFile_Top_Lines_OF_file {

	// read top N lines
	public static void read_Top_N_line(String inputFile, String outputFile, int N){
		System.out.println("readTopNline.1");
		try{
			//FileWriter writer  = new FileWriter(outputFile);
			String line="";
			System.out.println("readTopNline");
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			
			System.out.println("input:"+inputFile+"\noutput:"+outputFile);
				int cnt=0;
				//read each line of given file
	            while ((line = reader.readLine()) != null) {
	            	cnt++;
	            	if(cnt<=N){
	            		System.out.println(line);
	            	}
	        
	            }
	            System.out.println("readTopNline.end");
		}
		catch(Exception e){
			System.out.println("readTopNline.catch");
			e.printStackTrace();
		}
		System.out.println("readTopNline.2");
	}
	// read top N lines
	public static void readFile_Top_N2M_line(String inputFile, String outputFile
											 , int N, int M){
		try{
			//FileWriter writer  = new FileWriter(outputFile);
			String line="";
			System.out.println("readTopN2Mline");
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			System.out.println("input:"+inputFile+"\noutput:"+outputFile);
				  int lineNo=0;
				//read each line of given file
	            while ((line = reader.readLine()) != null) {
	            	lineNo++;
	            	if(lineNo>=N  && lineNo<=M){
	            		System.out.println(line);
	            	}
	        
	            }
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//
	public static void main(String[] args) throws IOException {
		
		String WSbaseFolder="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/Streaming/data/";
		String baseFolder="/Users/lenin/Downloads/Data/vast2013/nf/";
		String fileName="nf-chunk1.csv";
		
		WSbaseFolder="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/thehindu.backup.success./2006-2009/";
		baseFolder="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/thehindu.backup.success./2006-2009/";
		fileName="thehindu.allnewsArticles-thehindu-URL-print-2006-2009-2-opt-option2.txt"; 
		
		//
		ReadFile_Top_Lines_OF_file.read_Top_N_line(
													baseFolder+fileName,
													WSbaseFolder+"output.csv",
													10
													);
		
	}
}