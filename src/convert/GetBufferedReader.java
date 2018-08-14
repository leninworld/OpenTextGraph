package convert;

import java.io.BufferedReader;
import java.io.FileReader;

public class GetBufferedReader {
	//
	public static BufferedReader getBufferedReader(String inFile){
		BufferedReader reader=null;
		try{
			reader = new BufferedReader(new FileReader(inFile));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return reader;
	}

}
