package crawler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

//
class Wordfrequency{
	
		// get word frequency;
		// intype: {'crawlerfile','arff'} <- input file is crawler output (3 columns with tab delimited).
		// outype: {'arff','csv'} <-> arff is weka software file type. "csv" is generic comma separated.
		public static void getWordFrequency(String inFolder,
											int	   token_having_text_4_files_inFolder,
											String inStopWordFiles,
											String outFile,
											String debugFile,
											boolean boolStopWords,
											String inType,
											String outType,
											boolean isSOPprint
											){
			String t="";
			try{
				 System.out.println(inFolder);
				 FileWriter writer = new FileWriter(outFile);
				 FileWriter writerDebug = new FileWriter(debugFile);
				 File directory = new File(inFolder);
				 String[] listOfFiles = directory.list() ;//To get the list of file-names found at the directory
				 System.out.println("........" +listOfFiles.length);
				 HashMap<String, String> mapStopWords = new HashMap();
				 TreeMap<Integer, String> mapUniqueIDWords = new TreeMap();
				 TreeMap<String, Integer> mapUniqueWordsID = new TreeMap();
				 BufferedReader br = null;
				 String words[] = null; String temp2[] = null;
				 String line;
				 String files;
				 Map<String, Integer> wordCount = new HashMap<String, Integer>(); //Creates an Hash Map for storing the words and its count
				 BufferedReader brstopwords = new BufferedReader(new FileReader(inStopWordFiles)); 
				 //read stop words
				 while ((line = brstopwords.readLine()) != null) {
					 mapStopWords.put(line, "");
				 }
				 //for each file @ given folder
				 for (String currFile : listOfFiles) {
					 
					 if(isSOPprint)
						 System.out.println("2:"+inFolder+currFile);
					 
					 if(currFile.toLowerCase().indexOf("Store")>=0) continue;
					 File currFilePtr = new File(inFolder+currFile);
					 if (currFilePtr.isFile()) {
						 //files = currFilePtr.getName();
						 files=inFolder+currFile;
						 if(isSOPprint)
							 System.out.print("in files:"+files);
					 try {
						 	 if(isSOPprint)
							 	System.out.print("3.in files:"+files);
							 br = new BufferedReader(new FileReader(files)); //creates an Buffered Reader to read the contents of the file
							 if(isSOPprint)
								 System.out.print("4.in files:"+files);
							 //read each line
							 while ((line = br.readLine()) != null) {
								 String temp=line; 
								 //what type of input file
								 if(inType.toLowerCase().indexOf("crawlerfile")>=0){
									 temp2 = temp.split("\\s");
									 temp = temp2[token_having_text_4_files_inFolder-1]; //third column holding the text
								 }
								 else if(inType.indexOf("arff")>=0){ //weka input file (has @attribute_names)
									 temp=line.substring(1, line.length());
									 temp = line.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "").replaceAll("\\p{Cntrl}", "")
										 .replaceAll("\\p{C}", "?").replaceAll("[^\\x00-\\x7F]", "")
										   .toLowerCase().replace("%", "").replace("'", "").replace(".", " ").replace("-", " ")
										 	.replace("'", " ").replace(","," ").replace(":"," ")
										 	.replace(";"," ").replace("  ", " ");
								 }
								 words = temp.split("\\s"); //Splits the words with "space" as an delimiter
								 if(isSOPprint)
									 System.out.println("\nl:"+line+";w:"+words.length);
								 
								 //System.out.println("\nword:"+words.toString());
								 for (String read : words) {
									 if(read.length()==0) continue;
									 if(!mapUniqueWordsID.containsKey(read)
										&& !mapStopWords.containsKey(read)
										&& read.indexOf("@") == -1
										&& read.indexOf("Ä") == -1
										&& read.indexOf("£") == -1
										&& read.indexOf("‚") == -1
										){
										 int cnt = mapUniqueIDWords.size();
										 mapUniqueIDWords.put(cnt+1, read);
										 mapUniqueWordsID.put(read,cnt+1);
									 }
									 Integer freq = wordCount.get(read);
									 wordCount.put(read, (freq == null) ? 1 : freq + 1); //For Each word the count will be incremented in the Hashmap
								 }
							 } //end of current read
							 br.close();
							
							if(isSOPprint){
								 System.out.println("\n"+wordCount);
							}
					 } catch (Exception e) {
						 System.out.println("\nerror:"+inFolder);
						 e.printStackTrace();
					 }
				  } //end if
			} //end for //read all files in folder and got unique words
				 System.out.println("#################mapUniqueIDWords:"+mapUniqueIDWords.size());
			//re-read 
				 int nonZeroWords=0; 
				 String cluster="";double start = 0.001;
				 double end   = 0.0012;
				 for (String currFile : listOfFiles) {
					 System.out.println("2:"+inFolder+currFile);
					 File currFilePtr = new File(inFolder+currFile);
					 if (currFilePtr.isFile()) {
						 //files = currFilePtr.getName();
						 files=inFolder+currFile;
						 System.out.print("in files:"+files); int LineNo=0;
						 try {
							 br = new BufferedReader(new FileReader(files)); //creates an Buffered Reader to read the contents of the file
							 //read each line
							 while ((line = br.readLine()) != null) {
								 if(line.indexOf("@") >=0) continue; //for arff files only applied
								 LineNo++;
								 if(LineNo>2) break;//debug
								 nonZeroWords=0;
								 wordCount = new HashMap<String, Integer>(); // reset 
								 cluster=line.substring(0, 1);
								 String temp=line; 
								 //what type of input file
								 if(inType.indexOf("crawlerfile")>=0){
									 temp2 = temp.split("\\s");
									 temp = temp2[2]; //third column holding the text
								 }
								 else if(inType.indexOf("arff")>=0){ //weka input file (has @attribute_names)
									 temp=line.substring(1, line.length());
									 temp = line.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "").replaceAll("\\p{Cntrl}", "")
										 .replaceAll("\\p{C}", "?").replaceAll("[^\\x00-\\x7F]", "")
										   .toLowerCase().replace("%", "").replace("'", "").replace(".", " ").replace("-", " ")
										 	.replace("'", " ").replace(","," ").replace(":"," ")
										 	.replace(";"," ").replace("  ", " ");
								 }
								 words = temp.split("\\s"); //Splits the words with "space" as an delimeter
								 System.out.println("\nl:"+line+";w:"+words.length);
								 //System.out.println("\nword:"+words.toString());
								 for (String read : words) {
									 if(read.length()==0) continue;
									 Integer freq = wordCount.get(read);
									 wordCount.put(read, (freq == null) ? 1 : freq + 1); //For Each word the count will be incremented in the Hashmap
								 }
								 String concLine=""; int cnt =0; String comma="";
								 double random = new Random().nextDouble();
								 double result = start + (random * (end - start));
								 System.out.println(result);
								 //READ through all unique value
								 for(String f: mapUniqueWordsID.keySet() ){
									 cnt++;
									 if(cnt>1) {comma=",";}
									 if(!wordCount.containsKey(f)  ){
										 
										 //concLine=concLine+comma+"0";
										 concLine=concLine+comma+result;
									 }
									 else{
										 concLine=concLine+comma+ wordCount.get(f)+ "";
										 if(outType.toLowerCase().indexOf("arff")>=0){// weka software
											 writer.append("#"+wordCount.get(f)+","+nonZeroWords+"\n");
										 }
										 else if(outType.toLowerCase().indexOf("csv")>=0){
											 writer.append("#"+wordCount.get(f)+","+nonZeroWords+"\n");
										 }
										 nonZeroWords=new Integer( wordCount.get(f)) +nonZeroWords;
									 }
								 }
								 if(outType.indexOf("arff")>=0){// weka software
									 writer.append(cluster+","+concLine+"\n");
								 }
								 else if(outType.indexOf("csv")>=0){
									 System.out.println("temp2:"+temp2.length);
									 writer.append("->"+temp2[0]+" "+temp2[1]+ " " +concLine+"\n");
								 }
								 writer.flush();
								 if(outType.indexOf("arff")>=0){
									 t= cluster+concLine;
									 writerDebug.append(cluster+","+ t.split(",").length+","+nonZeroWords +"\n"  );
								 }
								 writerDebug.flush();
								 
							 } //end of current read
							 br.close();
							 System.out.println("uniq:"+mapUniqueWordsID.size()+";sz:"+wordCount.size()+"\n;wcount:"+wordCount);
							 
						 } catch (Exception e) {
							 System.out.println("\nerror:"+inFolder);
							 System.out.println("\nI could'nt read your files:" + e);
						 }
				  } //end if
			} //end for ..each line
				 // weka software
				 if(inType.indexOf("arff")>=0 && outType.indexOf("arff") >=0){
					 writer.append("@RELATION   ncr2\n");
					 for(String f: mapUniqueWordsID.keySet() ){
						 writer.append("@ATTRIBUTE\t"+f+"\tNUMERIC\n");
						 writer.flush();
					 }
					 writer.append("@DATA\n");
				 }
				 writer.flush();
				 
				 
			System.out.println("sz:"+mapUniqueIDWords.size()+";mapUniqueIDWords:"+mapUniqueIDWords);
			writer.close();
			writerDebug.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}//end of
		// main
        public static void main(String[] args){
        	// setting variable
        	String baseFolder="/Users/lenin/Downloads/#Data2/Feed-Backup-Trad/Filter_N_Merge/Trad/p18-filtered-1-attempt-id37151552/tmp/";
			String inFolder = baseFolder+"";
			String outFile = baseFolder+"out.csv";
			int token_having_text_4_files_inFolder=4;
			//step 1: create word-frequency (CSV) file from text (token in input File) 
			//getWordFrequency
			getWordFrequency(inFolder,
							 token_having_text_4_files_inFolder,//token_having_text_4_files_inFolder
							 "/Users/lenin/Downloads/#Data2/StopWords.txt",
							 outFile,
							 baseFolder+"debug.txt",
							 true, //boolStopWords
							 "crawlerfile", // {'crawlerfile','arff'}
							 "csv", //{'arff','csv'}
							 false //isSOPprint
							 );
			
			//Step 2 convert word-frequency file to weka arff file.
//			weka.convert_csv_to_weka_ARFF(outFile, 
//										  outFile+".arff"
//										  );
			
        }
}