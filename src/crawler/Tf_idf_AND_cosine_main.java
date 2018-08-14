package crawler;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Mubin Shrestha
 */
public class Tf_idf_AND_cosine_main {
	
	//calc_tf_idf_for_one_Folder
	public static void calc_tf_idf_for_one_Folder(String inFolder,
												  String outFile,
												  boolean only_run_tf_idf,
												  boolean only_run_cosine
												  ){
		try{
		   	
	        DocumentParser_AND_Cosine dp = new DocumentParser_AND_Cosine();
	        //dp.parseFiles(inFolder);
	        System.out.println("Parsing each line...");
	        if(only_run_tf_idf){
	        	dp.parse_each_line_as_Files_From_a_folder(inFolder);
	        }
	        // OR CALL 
	        System.out.println("Calculate TF-IDF...");
	        //tfIdfCalculator -> calculates tfidf & cosine similarity (cosine calc NOT GOOD)
	        
	        if(only_run_cosine){
	        	dp.tfIdfCalculator(
	        						inFolder,
	        						outFile,
	    							only_run_tf_idf,
	    							only_run_cosine
	        						);
	        }
	        //dp.getCosineSimilarity(); //calculated cosine similarity
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//calculate tf-idf and/or cosine simlarity
	public static void calc_tf_idf_for_one_File(
										String inFile,
										String outFile,
										boolean only_run_tf_idf,
										boolean only_run_cosine
										//boolean for_doc_only_load_lines_dont_calc_tokens
										){
		try{
			DocumentParser_AND_Cosine dp = new DocumentParser_AND_Cosine();
	        //dp.parseFiles(inFolder);
	        System.out.println("Parsing each line...");
	        if(only_run_tf_idf)
	        	dp.parse_each_line_as_Files_from_single_file(inFile);
	        // OR CALL 
	        System.out.println("Calculate TF-IDF...");
	        
	        if(only_run_cosine){
		        //tfIdfCalculator -> calculates tfidf & cosine similarity (cosine calc NOT GOOD)
		        dp.tfIdfCalculator(
					        		inFile,
					        		outFile,
									only_run_tf_idf,
									only_run_cosine
		        					);
	        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
    
    /**
     * Main method
     * @param args
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void main(String args[]) throws FileNotFoundException, IOException{
    	String baseFolder="";
    	String inFolder="/Users/lenin/Downloads/p6/merged/dummy.mergeall/dummy1";
    	String outFile="/Users/lenin/Downloads/p6/merged/dummy.mergeall/out_tfidf.txt";
    	
    	//BEGIN - 
    	// (1) PARSE EACH LINE IN A FILE (FROM GIVEN FOLDER) AND ADD TO DOCUMENT
    	// (2) TF-IDF FEATURE VECTOR for Each doc 
    	// (3) method "tfIdfCalculator"->Cosine similarity for each pair of documents to second output file suffix "_cosine.txt".
    	// 
//    	calc_tf_idf_for_one_Folder(inFolder,
//    							   outFile);
    	
    	baseFolder="/Users/lenin/Downloads/p6/merged/dummy.mergeall/ds7/";
    	String inFile=baseFolder+"mergedall-2015-T7-10-Topics-only-bodyText.txt";
    		   inFile=baseFolder+"out_tfidf_my.txt";	
    	 	   outFile=baseFolder+"out_cosine.txt";
    	boolean only_run_cosine=true;
    	boolean only_run_tfidf=true;
    	// calculate tf-idf and/or cosine simlarity
    	calc_tf_idf_for_one_File(inFile, 
    							outFile,
    							only_run_tfidf,
    							only_run_cosine
    							);
        
    }
}

