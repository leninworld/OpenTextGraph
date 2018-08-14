package crawler;

import java.util.Properties;


import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreNLPProtos.Sentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.*;
import edu.stanford.nlp.ling.CoreAnnotations;

import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceChunker;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.*;
import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.JointClassification;
import com.aliasi.classify.LMClassifier;


/**
 * 
 * @author lenin
 * http://rahular.com/twitter-sentiment-analysis/ 
 *https://blog.openshift.com/day-20-stanford-corenlp-performing-sentiment-analysis-of-twitter-using-java/
 */


import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.io.IOException;

/**
 * Created by mridul.v on 8/11/2014.
 */
public class Find_sentiments_of_a_sentence {
	
    static String[] categories;
    static LMClassifier lmClassifier;
    public static void SentimentClassifier() throws IOException, ClassNotFoundException {

    	String file = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/src/crawler/classifier-model.txt";
    	 	   file = "/Users/lenin/Dropbox/dataDONTDELETE/crawler/classifier-model.txt";

        lmClassifier = (LMClassifier) AbstractExternalizable.readObject(new File(file));
        categories = lmClassifier.categories();
    }

    public static String find_sentiments_classify1(String text) {
    	try {

            String file = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/src/crawler/classifier-model.txt";
                   file = "/Users/lenin/Dropbox/dataDONTDELETE/crawler/classifier-model.txt";

			lmClassifier = (LMClassifier) AbstractExternalizable.readObject(new File(file));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        categories = lmClassifier.categories();
        //System.out.println("categories.len:"+categories.length+" "+categories[0]+" "+categories[1]);
        ConditionalClassification classification = lmClassifier.classify(text);
        
        //System.out.println(classification.score(1));
        return classification.bestCategory();
    }
	
	// main
	public static void main(String[] args) {
		try{
			//find_sentiment_of_given_String("lucy is happy.");
			String s="mom is unhappy. but i love it.";
			
			s="an 18-year-old girl was shot dead by her brother in the kahna area on tuesday";
			s="I am very sad.";
			//NLP.findSentiment(s);
			String t=Find_sentiments_of_a_sentence.find_sentiments_classify1(s);

			
			System.out.println(t);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

    
}

