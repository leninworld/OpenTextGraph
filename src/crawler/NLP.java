package crawler;

import java.util.Properties;
import org.ejml.simple.SimpleMatrix;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.util.List;
import java.util.Properties;
import edu.stanford.*;

//NLP
public class NLP {
	
	public static void main(String[] args) {
	    findSentiment("life is good.");
	}
	//findSentiment
	public static void findSentiment(String line) {

	    Properties props = new Properties();
	    props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
//	    props.setProperty("parse.maxlen", "20");
//	    props.setProperty("tokenize.options", "untokenizable=noneDelete");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    int mainSentiment = 0;
	    
	    if (line != null && line.length() > 0) {
	    	System.out.println("line:"+line);
	        int longest = 0;
	        Annotation annotation = pipeline.process(line);
	        //
	        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
	        	Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
	        	//if(tree!=null){
		            int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
		            String partText = sentence.toString();
		            if (partText.length() > longest) {
		                mainSentiment = sentiment;
		                longest = partText.length();
		            }
//	            }
//	        	else{
//	        		System.out.println("NULL");
//	        	}
	        }
	    }
	    if (mainSentiment == 2 || mainSentiment > 4 || mainSentiment < 0) {
	        System.out.println("Neutral " + line);
	    }
	    else{
	    }
	    /*
	     * TweetWithSentiment tweetWithSentiment = new TweetWithSentiment(line,
	     * toCss(mainSentiment)); return tweetWithSentiment;
	     */

	}
}	

    
    
    
 