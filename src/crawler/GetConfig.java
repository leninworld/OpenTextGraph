package crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;

public class GetConfig {

	// read configuration file
		public static TreeMap<String, String> getConfig(String inConfigFileName) {
			TreeMap<String, String> mapConfig = new TreeMap();
			String line = "";
			System.out.println("getConfig():inConfigFileName=");
			System.out.println(inConfigFileName);
			System.out.println("-----------------------");
			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						inConfigFileName));
				System.out.println("\nReading config file...\n");
				// read each line of given file
				while ((line = reader.readLine()) != null) {
					String[] s = line.split("=");
					System.out.println("line:" + line);
					try {
						if (s.length < 2)
							continue;
						mapConfig.put(s[0], s[1]);
					} catch (Exception e) {
						System.out
								.println("EACH LINE OF CONFIG FILE SHOULD HAVE TWO TOKENS");
						e.printStackTrace();
					}
				}
				System.out.println("getConfig:" + mapConfig);
				System.out.println("\n-----------getConfig end----------");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mapConfig;
		}

	
}
