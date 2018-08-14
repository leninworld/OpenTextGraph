package crawler;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Class to read documents
 *
 * @author Mubin Shrestha
 */
public class DocumentParser_AND_Cosine {

    //This variable will hold all terms of each document in an array.
    private List<String[]> termsDocsArray = new ArrayList<String[]>();
    private List<String> allTerms = new ArrayList<String>(); //to hold all terms
    private List<double[]> tfidfDocsVector = new ArrayList<double[]>();

    //parse_doc_each_pair_lines_calc_cosine
    public static void parse_doc_each_pair_lines_calc_cosine(
    														 String baseFolder,
    														 String inFile,
    														 String outFile,
    														 boolean isAppend_outFile,
    														 int    last_i, //obsolete
    														 int    last_j, //obsolete
    														 boolean yes_check_on_past_ran
    														 ){
    
    	   //lineNo and <docid!!!word_id:freq,word_id2:freq>
    	   TreeMap<Integer,String> mapIS_tf_idf_each_doc_orig=new TreeMap<Integer, String>();
    	   // doc_id and !!!word_id:freq,word_id2:freq
    	   TreeMap<Integer,String> mapIS_tf_idf_each_doc=new TreeMap<Integer, String>();
           
           	// load inFile TF-IDF as only cosine to be calculated  
   	        mapIS_tf_idf_each_doc_orig=
   	        ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
   	        .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
   									            					inFile, -1, -1
   									            				  , " load tf-idf external"
   									            				  , true //isPrintSOP
   									            				  );
   	        
   	        System.out.println("from file:"+inFile+ " mapIS_tf_idf_each_doc_orig.size="+mapIS_tf_idf_each_doc_orig.size());
   	         
   	        //i is lineNumber, but we want it as doc_id
   	        for(int i:mapIS_tf_idf_each_doc_orig.keySet()){
   	        	String []s=mapIS_tf_idf_each_doc_orig.get(i).split("!!!");
   	        	int doc_id=new Integer(s[0]);
   	        	mapIS_tf_idf_each_doc.put(doc_id, "!!!"+s[1]);
   	        	
   	        }
   	        
   	        
   	        if(!new File(outFile).exists()){
				try {
					new File(outFile).createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
   	        }
   	        
   	        TreeMap<Integer, String>
		   	        mapIS_AlreadyRANFile= new TreeMap<Integer, String>();
   	        
   	        // if true, it will skip this process
   	        if(yes_check_on_past_ran==true){
   	        	mapIS_AlreadyRANFile=
			   	   	        ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
			   	   	        .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
			   	   	        													    outFile, -1, -1
			   	   									            				  , " load tf-idf external"
			   	   									            				  , true //isPrintSOP
			   	   									            				  );
   	        }
   	        
   	     TreeMap<String, String> mapAlreadyRan_i_j_asKEY=new TreeMap<String, String>(); 
   	     
   	        // get => i+","+j
   	        for(int hh:mapIS_AlreadyRANFile.keySet()){
   	        	System.out.println("process:"+mapIS_AlreadyRANFile.get(hh));
   	        	String[] curr_=mapIS_AlreadyRANFile.get(hh).split(",");
   	        	mapAlreadyRan_i_j_asKEY.put(curr_[0] +":"+curr_[1] , "");  // i+":"+j
   	        	System.out.println( "alre:"+curr_[0] +":"+curr_[1]);
   	        }
   	         
   	      
           FileWriter writer2=null; String line=""; int lineNumber=0;
           FileWriter writerDebug=null;
           TreeMap<String, String> mapAlreadyPair_of_cosine=new TreeMap<String, String>();
           TreeMap<Integer, TreeMap<String,Double>> mapMAINEachLine_as_Doc_word_id_tf_idf
           											=new TreeMap<Integer, TreeMap<String,Double>>();
           TreeMap<Integer,String> map_each_doc_WordTFIDF_of_inFile=new TreeMap<Integer, String>();
           
           try{
        	   System.out.println("2.inFile:"+inFile);
        	   
        	    BufferedReader br=new BufferedReader(new FileReader(new File(inFile)));
        	   	writer2=new FileWriter(new File(outFile), isAppend_outFile);
        	   	writerDebug=new FileWriter(new File(baseFolder+"debug2020.txt"), true);
        	   	
        	   	writerDebug.append("\n pagerank in file:"+inFile+" \n outFile:"+outFile
        	   						+"\n baseFolder:"+baseFolder+" \n isAppend_outFile:"+isAppend_outFile);
        	   	
        	   	writerDebug.flush();
        	   	
        	   	int doc_ID=0;
           		Cosine_similarity cs=new Cosine_similarity();
           		TreeMap<String,Double> map_each_line_asDoc_wordID_tfidf_pairs=new TreeMap<String, Double>(); 
           		
           		
	           	// Convert mapIS_tf_idf_each_doc.keySet())
        		while( (line = br.readLine()) !=null){
        			map_each_line_asDoc_wordID_tfidf_pairs= new TreeMap<String, Double>();
        			
					System.out.println("l:"+lineNumber++);
					if(line.length()>=1){
						//writerDebug.append("\nline:"+line);
						writerDebug.flush();
						String [] arr_tokens=line.split("!!!"); //first token has doc_ID
						if(arr_tokens[0].length() ==0 || arr_tokens.length==0 )
							continue;
						
						doc_ID=Integer.valueOf(arr_tokens[0]);
						String [] arr_wordID_delimitr_TFIDF= arr_tokens[1].split(" ");
						int cnt=0; 
						// 
						map_each_doc_WordTFIDF_of_inFile.put(doc_ID, arr_tokens[1]);
						
						//
						while(cnt<arr_wordID_delimitr_TFIDF.length){
							
							String curr_wordID_delimitr_TFIDF =arr_wordID_delimitr_TFIDF[cnt]; //it has "233:1"
							String[] name_value=curr_wordID_delimitr_TFIDF.split(":");
							try{
								map_each_line_asDoc_wordID_tfidf_pairs.put(name_value[0], new Double(name_value[1]));
							}
							catch(Exception e){
								System.out.println("err .curr_wordID_delimitr_TFIDF:"+curr_wordID_delimitr_TFIDF);
								e.printStackTrace();
							}
						    
							cnt++;
						}
					}
					
					mapMAINEachLine_as_Doc_word_id_tf_idf.put(doc_ID, map_each_line_asDoc_wordID_tfidf_pairs);
					System.out.println("docID,tf-idf vec:"+doc_ID+"<->"
									 +map_each_line_asDoc_wordID_tfidf_pairs
										);
					
				} //while line-read
	            
        		writerDebug.append("\n mapMAINEachLine_as_Doc_word_id_tf_idf:"+mapMAINEachLine_as_Doc_word_id_tf_idf);
        		writerDebug.flush();
        		System.out.println("mapIS_tf_idf_each_doc.size:"+mapIS_tf_idf_each_doc.size());
        		
        		//System.out.println("mapMAINEachLine_as_Doc_word_id_tf_idf:"+mapMAINEachLine_as_Doc_word_id_tf_idf);
        		boolean isalreadyran=false;
               // calculate Cosine similarity
               for(int i:mapIS_tf_idf_each_doc.keySet()){
            	   //System.out.println("i->"+i);
            	   
//             		if(last_i>0 && last_j>0
//               				&& i < last_i  
//               				//&& isalreadyran==false
//               				){
//               			System.out.println("i,j->"+i+ " last(i,j)->" +last_i+" "+last_j);
//               			i=last_i;
////               			j=last_j;
//               			isalreadyran=true;
//               			continue;
//               			
//               		}
               	//
               	for(int j:mapIS_tf_idf_each_doc.keySet()){
               	    String t=i+":"+j;
//               	    System.out.println("curr pair :" + t+" i:"+mapMAINEachLine_as_Doc_word_id_tf_idf.get(i)
//               	    				+" j:"+mapMAINEachLine_as_Doc_word_id_tf_idf.get(j)
//               	    				);
               	    if(yes_check_on_past_ran==true){
	               		if( mapAlreadyRan_i_j_asKEY.containsKey(t) ||
	               			mapAlreadyRan_i_j_asKEY.containsKey(j+":"+i)
	               		  ){
	               			System.out.println("already ran:" + t);
	               			continue;
	               		}
               	    }
               		
               		TreeMap<String, Double> converted_to_map_feat_vect_1=new TreeMap<String, Double>();
               		TreeMap<String, Double> converted_to_map_feat_vect_2=new TreeMap<String, Double>();
               		//System.out.println("vec 1:"+mapIS_tf_idf_each_doc.get(i));
               		//System.out.println("vec 2:"+mapIS_tf_idf_each_doc.get(j));
               		
               		String [] i_bfr=mapIS_tf_idf_each_doc.get(i).split("!!!");
               		String [] j_bfr=mapIS_tf_idf_each_doc.get(j).split("!!!");
               		//System.out.println("i,j->"+i+" "+j+" tokens.len:"+i_bfr.length+" "+j_bfr.length);
               		
               		if(i_bfr.length<2 || j_bfr.length<2){
               			System.out.println(" not right token");
               			continue;
               		}
               		
               		String [] i_th=i_bfr[1].split(",");
               		String [] j_th=j_bfr[1].split(",");
               		
               		// convert string array to map
               		int c10=0;
               		
               		converted_to_map_feat_vect_1=mapMAINEachLine_as_Doc_word_id_tf_idf.get(i);
               		converted_to_map_feat_vect_2=mapMAINEachLine_as_Doc_word_id_tf_idf.get(j);
               		
               		if(converted_to_map_feat_vect_1==null || converted_to_map_feat_vect_2==null){
               			//System.out.println("** feature vector NULL.."+converted_to_map_feat_vect_1+" "+converted_to_map_feat_vect_2);
               			continue;
               		}
               		
//               		System.out.println("converted_to_map_feat_vect_1:"+converted_to_map_feat_vect_1);
//               		System.out.println("converted_to_map_feat_vect_2:"+converted_to_map_feat_vect_2);
              
               		//
               		if(!mapAlreadyPair_of_cosine.containsKey(i+":"+j)){
               			double curr_cosine=cs.calculateCosineSimilarity_approach_1( converted_to_map_feat_vect_1,
																					converted_to_map_feat_vect_2 );
               			
               			System.out.println("Writing i , j -> "+i +" "+j+" cosine:"+curr_cosine);
   	            		writer2.append(i+","+j+","+
//   			            				cs.cosineSimilarity_approach_2(   i_th 
//   						            									, j_th
//   						            									,  i+" "+j
//   						            									)+"!!!"
											curr_cosine
   						            		//							+"!!!"
   						            		//+cosine_sim.calculateSimilarity(i_th, j_th, 0)
   						            									+"\n");
   	            		writer2.flush();
   	            		
   	            		//if(i<=0){
   	            			writerDebug.append("\ni:"+i+
   	            								"\n pair1:"+converted_to_map_feat_vect_1+
   	            								"\n pair2:"+converted_to_map_feat_vect_2
   	            								);
   	            			writerDebug.flush();
   	            		//}
   	            		
   	            		mapAlreadyPair_of_cosine.put(i+":"+j, "");
   	            		mapAlreadyPair_of_cosine.put(j+":"+i, "");
               		}
               		
               	}

               } //end for 
               System.out.println("loaded mapIS_tf_idf_each_doc.size:"+mapIS_tf_idf_each_doc.size());
               //System.out.println("mapIS_tf_idf_each_doc:"+mapIS_tf_idf_each_doc);
               System.out.println("mapMAINEachLine_as_Doc_word_id_tf_idf.size:"+mapMAINEachLine_as_Doc_word_id_tf_idf.size());
               //System.out.println("map:"+mapMAINEachLine_as_Doc_word_id_tf_idf);
           }
           catch(Exception e){
        	   e.printStackTrace();
           }
    }
    
    
    /** Each file added to docsArrays
     * Method to read files and store in array.
     * @param filePath : source file path
     * @throws FileNotFoundException
     * @throws IOException
     */
    public List<String[]> parseFiles(String filePath) throws FileNotFoundException, IOException {
        File[] allfiles = new File(filePath).listFiles();
        BufferedReader in = null;
        for (File f : allfiles) {
        	System.out.println("DocumentParser.parseFiles:"+f.getAbsolutePath());
            if (f.getName().endsWith(".txt")) {
                in = new BufferedReader(new FileReader(f));
                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }
                String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
                for (String term : tokenizedTerms) {
                    if (!allTerms.contains(term)) {  //avoid duplicate entry
                        allTerms.add(term);
                    }
                }
                termsDocsArray.add(tokenizedTerms);
            }
        }
        System.out.println(termsDocsArray.size());
        return termsDocsArray;
    }
    //parse_each_line_as_Files_from_single_file
    public void parse_each_line_as_Files_from_single_file
    						(
    								String filePath
    						) throws FileNotFoundException, IOException {
        File onefile = new File(filePath);
        System.out.println("parse_each_line_as_Files....");
        BufferedReader in = null; int lineNumber=0;
         try{
        	System.out.println("DocumentParser.parseFiles:"+onefile.getAbsolutePath());
            if (onefile.getName().endsWith(".txt")) {
                in = new BufferedReader(new FileReader(onefile));
                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = in.readLine()) != null) {
                	lineNumber++;
                	System.out.println("tf-ifd.lineno:"+lineNumber);
	                String[] tokenizedTerms = s.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
	                for (String term : tokenizedTerms) {
	                    if (!allTerms.contains(term)) {  //avoid duplicate entry
	                        allTerms.add(term);
	                    }
	                }
	                termsDocsArray.add(tokenizedTerms);
	                
                }
                
            }
         } 
         catch(Exception e){
        	 System.out.println(" errr:"+e.getLocalizedMessage());
         }
        System.out.println("parse_each_line_as_Files:"+termsDocsArray.size());
    }
    
    // parse_each_line_as_Files
    public void parse_each_line_as_Files_From_a_folder(String filePath) throws FileNotFoundException, IOException {
        File[] allfiles = new File(filePath).listFiles();
        System.out.println("parse_each_line_as_Files....");
        BufferedReader in = null;
        for (File f : allfiles) {
        	System.out.println("DocumentParser.parseFiles:"+f.getAbsolutePath());
            if (f.getName().endsWith(".txt")) {
                in = new BufferedReader(new FileReader(f));
                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = in.readLine()) != null) {
                 
	                String[] tokenizedTerms = s.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
	                for (String term : tokenizedTerms) {
	                    if (!allTerms.contains(term)) {  //avoid duplicate entry
	                        allTerms.add(term);
	                    }
	                }
	                termsDocsArray.add(tokenizedTerms);
                }
                
            }
        }
        System.out.println("parse_each_line_as_Files:"+termsDocsArray.size());
    }
    
    
    /**
     * Method to create termVector according to its tfidf score.
     */
    public void tfIdfCalculator(
    							String inFile,
    							String outFile,
    							boolean only_tf_idf,
    							boolean only_cosinesimilarity //this needs inFile
    								) {
    	System.out.println("Inside tf-idf Calc..");
    	FileWriter writer=null;FileWriter writer2=null;FileWriter writer3=null;
    	try{
    		writer=new FileWriter(new File(outFile));
    		writer2=new FileWriter(new File(outFile+"_cosine.txt"));
    		writer3=new FileWriter(new File(outFile+"_only_tf_idf_feature_vec.txt"));
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term requency inverse document frequency
        int cnt=0;
        if(only_tf_idf==true){
        
        for (String[] docTermsArray : termsDocsArray) {
        	cnt++;
        	System.out.println("doc number i="+cnt);
            double[] tfidfvectors = new double[allTerms.size()];
            int count = 0;
            for (String terms : allTerms) {
                tf = new Tf_idf().tfCalculator(docTermsArray, terms);
                idf = new Tf_idf().idfCalculator(termsDocsArray, terms);
                tfidf = tf * idf;
                tfidfvectors[count] = tfidf;
                count++;
                System.out.println("tfidf="+tfidf+" terms="+terms );
            }
            tfidfDocsVector.add(tfidfvectors);  //storing document vectors;
            
            try{
        
            	int c2=0; String tf_idf_CSV_curr_doc="";
            	// 
            	while(c2<tfidfvectors.length){
            		if(tf_idf_CSV_curr_doc.length()>0)
            			tf_idf_CSV_curr_doc=tf_idf_CSV_curr_doc+","+tfidfvectors[c2];
            		else
            			tf_idf_CSV_curr_doc=String.valueOf(tfidfvectors[c2]);
            		c2++;
            	}
            	
            	writer.append(cnt+"!!!"+tf_idf_CSV_curr_doc.replace("NaN", "0.00")
            						+"!!!tokens.size:"+c2+"\n");
            	writer.flush();
            	
            	writer3.append(tf_idf_CSV_curr_doc.replace("NaN", "0.00")+"\n");
            	writer3.flush();
            	
            }
            catch(Exception e){
            	e.printStackTrace();
            }
        
        } //each doc process END termsDocsArray
        
        }
        TreeMap<Integer,String> mapIS_tf_idf_each_doc=new TreeMap<Integer, String>();
        if(only_tf_idf){
        
	        //load tf-idf
	        mapIS_tf_idf_each_doc=
	        ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
	        .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
									            					outFile, -1, -1
									            				  , " load tf-idf just ran"
									            				  ,  true //isPrintSOP
									            				  );
        }
        else if(only_cosinesimilarity){
        	// load inFile TF-IDF as only cosine to be calculated  
	        mapIS_tf_idf_each_doc=
	        ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
	        .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
									            					inFile, -1, -1
									            				  , " load tf-idf external"
									            				  ,  true //isPrintSOP
									            				  );
        }
        
        
        TreeMap<String, String> mapAlreadyPair_of_cosine=new TreeMap<String, String>();
        try{
        	Cosine_similarity cs=new Cosine_similarity();
        	System.out.println("mapIS_tf_idf_each_doc.size:"+mapIS_tf_idf_each_doc.size());
            // calculate Cosine similarity
            for(int i:mapIS_tf_idf_each_doc.keySet()){
            	//System.out.println("enter i->"+i);
            	//
            	for(int j:mapIS_tf_idf_each_doc.keySet()){
            		//System.out.println("enter i,j->"+i+" "+j);
            		TreeMap<String, Double> converted_to_map_feat_vect_1=new TreeMap<String, Double>();
            		TreeMap<String, Double> converted_to_map_feat_vect_2=new TreeMap<String, Double>();
            		
            		String [] i_th=mapIS_tf_idf_each_doc.get(i).split("!!!")[1].split(",");
            		String [] j_th=mapIS_tf_idf_each_doc.get(i).split("!!!")[1].split(",");
            		
            		// convert string array to map
            		int c10=0;
            		// 
            		while(c10<i_th.length){
            			converted_to_map_feat_vect_1.put(String.valueOf(c10) ,new Double(i_th[c10])) ;
            			converted_to_map_feat_vect_2.put(String.valueOf(c10), new Double(j_th[c10]));
            			c10++;
            		}
            		
            		if(!mapAlreadyPair_of_cosine.containsKey(i+"!"+j)){
	            		writer2.append(i+"!!!"+j+"!!!"+
			            				cs.cosineSimilarity_approach_2(   i_th 
						            									, j_th
						            									,  i+" "+j
						            									)+"!!!"+
						            	cs.calculateCosineSimilarity_approach_1(converted_to_map_feat_vect_1,
						            											converted_to_map_feat_vect_2 )
						            									+"!!!"
						            		+Cosine_sim.calculateSimilarity(i_th, j_th, 0)
						            									+"\n");
	            		writer2.flush();
	            		mapAlreadyPair_of_cosine.put(i+"!"+j, "");
	            		mapAlreadyPair_of_cosine.put(j+"!"+i, "");
            		}
            		
            	}
            } //END  calculate Cosine similarity
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        
        
        
    }

    /**
     * Method to calculate cosine similarity between all the documents.
     */
    public void getCosineSimilarity() {
    	System.out.println("Inside getCosineSimilarity Calc..");
        for (int i = 0; i < tfidfDocsVector.size(); i++) {
            for (int j = 0; j < tfidfDocsVector.size(); j++) {
                System.out.println("between " + i + " and " + j + "  =  "
                                   + new Cosine_similarity().cosineSimilarity_approach_2
                                       (
                                         tfidfDocsVector.get(i), 
                                         tfidfDocsVector.get(j)
                                       )
                                  );
            }
        }
    }
    // main
    public static void main(String args[]) throws FileNotFoundException, IOException{
    	String baseFolder="/Users/lenin/Downloads/p6/merged/dummy.mergeall/ds7/"; 
    	
    	String inFile=baseFolder+"trafficking_inFile_for_PageRank_calc.txt";
    	String outFile=baseFolder+"trafficking_inFile_for_PageRank_calc.txt_PAGERANK.txt";
    	int last_i=1017;
    	int last_j=7192;
    	//parse_doc_each_pair_lines_calc_cosine
    	parse_doc_each_pair_lines_calc_cosine(
    										  baseFolder,
    										  inFile,
    										  outFile,
    										  true, // is_Append_outFile
    										  last_i,
    										  last_j,
    										  false //yes_check_on_past_ran
    										  );
    	
    }
    
}
