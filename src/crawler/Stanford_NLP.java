package crawler;

import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Stanford_NLP {
	
	public static String Stanford_NLP_POS_tagger(
									  String path_of_stanford_tagger_input_1, //input_1
									  MaxentTagger tagger_input_2, // input_1 or input_2 needed 
									   String sentence_to_POS){
		
		
		MaxentTagger tagger = null;  
				
//		if(path_of_stanford_tagger_input_1.length()>0)
//			try {
//				tagger=new MaxentTagger(path_of_stanford_tagger_input_1);
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		else
//			tagger=tagger_input_2;
		
		String tagged = tagger.tagString(sentence_to_POS);
		System.out.println(tagged);
		
		return "";
	}
	
	// main 
    public static void main(String[] args) throws Exception {
    	
    	String path_of_stanford_tagger="/Users/lenin/OneDrive/jar/stanford-tagger/models/english-bidirectional-distsim.tagger";
    	String a = "I like watching movies";
    	
    	//
	    Stanford_NLP_POS_tagger(path_of_stanford_tagger, null, a );
    	
    	
    }

}
