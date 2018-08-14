package convert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class ReadConfig {

			// load multiple values to maps (with key in first column)
			public static TreeMap<String, String> readConfig( 
										String filename, String delimiter
										) {
				String line = "";
				String key = "";
				String value = ""; int lineNo=0;
				TreeMap<String, String> mapOut=new TreeMap();
				int count = 0;
				try {
					BufferedReader reader = new BufferedReader(new FileReader(filename));
					String name="";
					while ((line = reader.readLine()) != null) {
						lineNo++;
						line = line.replaceAll("\\p{Cntrl}", "");
						StringTokenizer stk = new StringTokenizer(line, delimiter);
						count = 1;
						while (stk.hasMoreTokens()) {
							value = stk.nextToken();
							 
							if (count == 1  ) {
								// writerDebug.append(" hp map insertion "+
								// key+";"+value+"\n");
								name=value;
							} else if (count == 2  ) {
								// writerDebug.append(" hp map insertion "+
								// key+";"+value+"\n");
								mapOut.put(name, value);
								System.out.println("read config:"+name+":"+value);
							} 
							count++;
						} // while loop for string tokenization
					
					} // while loop for line read of given file
				} catch (Exception e) {
					System.out.println("readConfig():");
					e.printStackTrace();
				}
				return mapOut;
			}
			//main
			public static void main(String[] args) throws IOException {
				
				String WSbaseFolder="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/Streaming/data/";
				String baseFolder="/Users/lenin/Downloads/Data/vast2013/nf/";
				String fileName="config.txt";	
				readConfig(WSbaseFolder+fileName,"=");
				System.out.println(WSbaseFolder+fileName);
				
			}
	
}
