package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class Cosine_similarity {

	 // APPROACH 1: PASSING DOUBLE[]
	 /**
     * Method to calculate cosine similarity between two documents.
     * @param docVector1 : document vector 1 (a)
     * @param docVector2 : document vector 2 (b)
     * @return 
     */
	 // APPROACH 1: PASSING DOUBLE[]
    public double cosineSimilarity_approach_2(double[] docVector1, double[] docVector2) {
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        double cosineSimilarity = 0.0;
        System.out.println("===================");
        for (int i = 0; i < docVector1.length; i++) //docVector1 and docVector2 must be of same length
        {
        	//System.out.println("i="+docVector1[i] +" "+docVector2[i]);
            dotProduct += docVector1[i] * docVector2[i];  //a.b
            magnitude1 += Math.pow(docVector1[i], 2);  //(a^2)
            magnitude2 += Math.pow(docVector2[i], 2); //(b^2)
        }
        System.out.println("===================");
        magnitude1 = Math.sqrt(magnitude1);//sqrt(a^2)
        magnitude2 = Math.sqrt(magnitude2);//sqrt(b^2)

        if (magnitude1 != 0.0 | magnitude2 != 0.0) {
            cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
        } else {
            return 0.0;
        }
        return cosineSimilarity;
    }
	
    //APPROACH 3 = APPROACH 1 : BNUT INPUT IS STRING ARRAY
    public double cosineSimilarity_approach_2(String[] docVector1, String[] docVector2, String debug_label) {
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        double cosineSimilarity = 0.0;
        System.out.println("=======cosine !====="+debug_label);
        for (int i = 0; i < docVector1.length; i++) //docVector1 and docVector2 must be of same length
        {
        	//System.out.println("i="+docVector1[i] +" "+docVector2[i]);
            dotProduct += Double.valueOf(docVector1[i]) *Double.valueOf(docVector2[i]);  //a.b
            magnitude1 += Math.pow(Double.valueOf(docVector1[i]), 2);  //(a^2)
            magnitude2 += Math.pow(Double.valueOf(docVector2[i]), 2); //(b^2)
        }
        //System.out.println("===================");
        magnitude1 = Math.sqrt(magnitude1);//sqrt(a^2)
        magnitude2 = Math.sqrt(magnitude2);//sqrt(b^2)

        if (magnitude1 != 0.0 | magnitude2 != 0.0) {
            cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
        } else {
            return 0.0;
        }
        return cosineSimilarity;
    }
	
    
    //APPROACH 2: PASSING HashMap<>
	/**
	 * calculate the cosine similarity between feature vectors of two clusters
	 * 
	 * The feature vector is represented as HashMap<String, Double>. 
	 * 
	 * @param firstFeatures The feature vector of the first cluster
	 * @param secondFeatures The feature vector of the second cluster 
	 * @return the similarity measure
	 */
    //APPROACH 2: PASSING HashMap<>
	public static Double calculateCosineSimilarity_approach_1(TreeMap<String, Double> firstFeatures, 
												   			  TreeMap<String, Double> secondFeatures) {
		Double similarity = 0.0;
		Double sum = 0.0;	// the numerator of the cosine similarity
		Double fnorm = 0.0;	// the first part of the denominator of the cosine similarity
		Double snorm = 0.0;	// the second part of the denominator of the cosine similarity
		Set<String> fkeys = firstFeatures.keySet();
		Iterator<String> fit = fkeys.iterator();
		while (fit.hasNext()) {
			String featurename = fit.next();
			boolean containKey = secondFeatures.containsKey(featurename);
			if (containKey && (firstFeatures.get(featurename)>0 || secondFeatures.get(featurename)>0) ) {
				sum = sum + firstFeatures.get(featurename) * secondFeatures.get(featurename);
			}
		}
		fnorm = calculateNorm(firstFeatures);
		snorm = calculateNorm(secondFeatures);
		similarity = sum / (fnorm * snorm);
		return similarity;
	}
	
	/**
	 * calculate the norm of one feature vector
	 * 
	 * @param feature of one cluster
	 * @return
	 */
	public static Double calculateNorm(TreeMap<String, Double> feature) {
		Double norm = 0.0;
		Set<String> keys = feature.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String featurename = it.next();
			norm = norm + Math.pow(feature.get(featurename), 2);
		}
		return Math.sqrt(norm);
	}
	
	//calc_Cosine_Similarity_for_doc_pair
	public static void calc_Cosine_Similarity_for_doc_pair(
														  String inFile,
														  String outFile
														  ){
		String line="";
		
		try{
			BufferedReader reader=new BufferedReader(new FileReader(new File(inFile)));
			//
			while((line=reader.readLine())!=null){
				
				
				
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	// main
	public static void main(String args[]) throws FileNotFoundException, IOException{
		
		TreeMap<String, Double> firstFeatures=new TreeMap();
		TreeMap<String, Double> secondFeatures=new TreeMap();
		
		firstFeatures.put("shark", 1.);
		firstFeatures.put("attack", 2.);
		
		secondFeatures.put("shark", 2.);
		
		System.out.println( calculateCosineSimilarity_approach_1(firstFeatures, secondFeatures));
		
    }
	
	
}