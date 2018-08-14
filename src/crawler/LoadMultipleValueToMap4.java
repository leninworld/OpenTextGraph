package crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class LoadMultipleValueToMap4 {
	
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
	
		// load multiple values to maps (with key in first column)
	public static TreeMap<Integer,TreeMap<Integer, String>> LoadMultipleValueToMap4(
			String filename, String delimiter,
			FileWriter writerDebug) {

		TreeMap mapOne = new TreeMap();
		TreeMap mapTwo = new TreeMap();
		TreeMap mapThree = new TreeMap();
		TreeMap mapFour = new TreeMap();

		TreeMap<Integer,TreeMap<Integer, String>> out = new TreeMap<>();

		String line = "";
		String key = "";
		String value = ""; int lineNo=0;
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				lineNo++;
//				line = line.replaceAll("\\p{Cntrl}", "");
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				count = 1;
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();

					if (count == 1  ) {
						// writerDebug.append(" hp map insertion "+
						// key+";"+value+"\n");
						mapOne.put(lineNo, value);
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
					}
					count++;
				} // while loop for string tokenization
				writerDebug.flush();
			} // while loop for line read of given file

			out.put(1, mapOne);
			out.put(2, mapTwo);
			out.put(3, mapThree);
			out.put(4, mapFour);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	// load multiple values to maps (with key in first column)
	public static TreeMap<Integer,TreeMap<Integer, String>> LoadMultipleValueToMap3(
			String filename, String delimiter,
			FileWriter writerDebug) {

		TreeMap mapOne = new TreeMap();
		TreeMap mapTwo = new TreeMap();
		TreeMap mapThree = new TreeMap();


		TreeMap<Integer,TreeMap<Integer,String>> out = new TreeMap<>();

		String line = "";
		String key = "";
		String value = ""; int lineNo=0;
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
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
						mapOne.put(lineNo, value);
					} else if (count == 2  ) {
						// writerDebug.append(" hp map insertion "+
						// key+";"+value+"\n");
						mapTwo.put(lineNo, value);
					} else if (count == 3  ) {
						// writerDebug.append(" email map insertion "+
						// key+";"+value+"\n");
						mapThree.put(lineNo, value);
					}
					count++;
				} // while loop for string tokenization
				writerDebug.flush();
			} // while loop for line read of given file

			out.put(1, mapOne);
			out.put(2, mapTwo);
			out.put(3, mapThree);


		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	

}
