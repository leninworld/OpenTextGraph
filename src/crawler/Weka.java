package crawler;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
 
import java.io.File;

public class Weka {

	// convert CSV to WEKA ARFF file
	public static void convert_csv_to_weka_ARFF(String inFile,
										   		String outFile){
		 try {
			    // load CSV
			    CSVLoader loader = new CSVLoader();
			    loader.setSource(new File(inFile));
			    Instances data = loader.getDataSet();
			 
			    // save ARFF
			    ArffSaver saver = new ArffSaver();
			    saver.setInstances(data);
			    saver.setFile(new File(outFile));
			    saver.setDestination(new File(outFile));
			    saver.writeBatch();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	 
	}
	
	// main
    public static void main(String[] args){
    	// setting variable
    	String baseFolder="";
		String inFile = "";
		String outFile = baseFolder+"";
		//convert
		convert_csv_to_weka_ARFF( inFile,
								  outFile
								);
		
		
    }
	
}
