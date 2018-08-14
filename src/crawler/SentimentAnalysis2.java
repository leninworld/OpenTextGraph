package crawler;


import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.google.api.client.util.Lists;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class SentimentAnalysis2 {
    private final String document;

    public SentimentAnalysis2(String document) {
        this.document = document;
    }

    public SentimentAnalysis2(Collection<String> sentences) {
        this.document = Joiner.on(". ").join(sentences);
    }

    public List<Integer> annotateAndScore(){
        List<Integer> scores = Lists.newArrayList();
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation document = new Annotation(this.document);
        pipeline.annotate(document);
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree annotatedTree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int score = RNNCoreAnnotations.getPredictedClass(annotatedTree);
            scores.add(score);
        }
        return scores;
    }
    
    public static void main(String[] args) {
    	
    	String line = "Great item! HDMI and decent wifi required as with all streaming devices.\n" +
                "The flow on the homepage is very good and responsive. Watching a series is a doddle, flow is great, no action required.\n" +
                "The remote and controller app both work a treat.\n" +
                "I really like this device.\n" +
                "I'd like to see an Amazon-written mirroring app available for non-Amazon products but no-one likes talking to each other in this field!";

        Long textLength = 0L;
        int sumOfValues = 0;

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        int mainSentiment = 0;
        if (line != null && line.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(line);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                //tree= sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
                if(tree!=null){
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
	                if (partText.length() > longest) {
	                    textLength += partText.length();
	                    sumOfValues = sumOfValues + sentiment * partText.length();
	
	                    System.out.println(sentiment + " " + partText);
	                }
                }
                else{
                	System.out.println("Tree NULL");
                }
                
            }
        }

        System.out.println("Overall: " + (double)sumOfValues/textLength);
    }
    
}