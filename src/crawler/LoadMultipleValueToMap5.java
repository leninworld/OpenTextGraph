package crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class LoadMultipleValueToMap5 {
	
	// load multiple values to maps (with key in first column)
		public static void LoadMultipleValueToMap4WithKeyINFirstCol(final Map mapName,
				final Map mapHomepage, final Map mapEmail, final Map mapPosition,
				String filename, String delimiter, FileWriter writerDebug) {
			String line = "";
			String key = "";
			String value = "";
			int count = 0;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(filename));
				while ((line = reader.readLine()) != null) {
					line = line.replaceAll("\\p{Cntrl}", "");
					StringTokenizer stk = new StringTokenizer(line, delimiter);
					count = 1;
					while (stk.hasMoreTokens()) {
						value = stk.nextToken();
						if (count == 1) {
							key = value;
						} else if (count == 2 && !(value.equalsIgnoreCase("na"))) {
							// writerDebug.append(" hp map insertion "+
							// key+";"+value+"\n");
							mapName.put(key, value);
						} else if (count == 3 && !(value.equalsIgnoreCase("na"))) {
							// writerDebug.append(" hp map insertion "+
							// key+";"+value+"\n");
							mapHomepage.put(key, value);
						} else if (count == 4 && !(value.equalsIgnoreCase("na"))) {
							// writerDebug.append(" email map insertion "+
							// key+";"+value+"\n");
							mapEmail.put(key, value);
						} else if (count == 5 && !(value.equalsIgnoreCase("na"))) {
							// writerDebug.append("  Postion map insertion "+
							// key+";"+value+"\n");
							mapPosition.put(key, value);
						}
						count++;
					} // while loop for string tokenization
					writerDebug.flush();
				} // while loop for line read of given file
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		// load multiple values to maps 
		public static TreeMap<Integer, TreeMap<Integer,String> > LoadMultipleValueToMap5(
				String filename, String delimiter, 
				FileWriter writerDebug) {
			String line = "";
			String key = "";
			String value = ""; int lineNo=0;
			 TreeMap<Integer, TreeMap<Integer,String> > mapOut=
					 new TreeMap<Integer, TreeMap<Integer,String>>();
			  TreeMap mapOne=new TreeMap();
			  TreeMap mapTwo=new TreeMap();
			  TreeMap mapThree=new TreeMap();
			  TreeMap mapFour=new TreeMap();
			  TreeMap mapFive=new TreeMap();
			
			int count = 0;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(filename));
				while ((line = reader.readLine()) != null) {
					lineNo++;
					System.out.println("lineNo:"+lineNo);
					line = line.replaceAll("\\p{Cntrl}", "");
					StringTokenizer stk = new StringTokenizer(line, delimiter);
					count = 1;
					while (stk.hasMoreTokens()) {
						value = stk.nextToken();
						 
						if (count == 1  ) {
							// writerDebug.append(" hp map insertion "+
							// key+";"+value+"\n");
							mapOne.put(lineNo, value);
							System.out.println("1-Col(key)="+value);
						} else if (count == 2  ) {
							// writerDebug.append(" hp map insertion "+
							// key+";"+value+"\n");
							mapTwo.put(lineNo, value);
						} else if (count == 3  ) {
							// writerDebug.append(" email map insertion "+
							// key+";"+value+"\n");
							mapThree.put(lineNo, value);
						} else if (count == 4  ) {
							// writerDebug.append("  Postion map insertion "+
							// key+";"+value+"\n");
							mapFour.put(lineNo, value);
						} else if (count == 5  ) {
							mapFive.put(lineNo, value);
						}
						
						count++;
					} // while loop for string tokenization
					writerDebug.flush();
				} // while loop for line read of given file
				
				mapOut.put(1, mapOne);
				mapOut.put(2, mapTwo);
				mapOut.put(3, mapThree);
				mapOut.put(4, mapFour);
				mapOut.put(5, mapFive);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mapOut;
		}
	

}
