package crawler;
import edu.stanford.nlp.hcoref.CorefCoreAnnotations;
import edu.stanford.nlp.hcoref.data.CorefChain;
import edu.stanford.nlp.hcoref.data.Mention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.*;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


//http://stackoverflow.com/questions/16523067/how-to-use-stanford-parser

public class Find_sentiment_stanford_OpenNLP {
	
	//find_sentiment_inputDependencyTreeSentenceString
	public static String find_sentiment_inputDependencyTreeSentenceString(String In_sentence){
		String str_tree="";int sentiment_score =0;
		try{
			 
			Tree parse =  Tree.valueOf(In_sentence);
			str_tree =parse.pennString();
			  
			  //System.out.println("--------------");
			  //parse.pennPrint();
			  //System.out.println("--------------");
			
			sentiment_score = RNNCoreAnnotations.getPredictedClass(parse);
			//System.out.println("input tree, score: "+sentiment_score);	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return String.valueOf(sentiment_score);
	}
	
	
	
	
	//find_sentiment_stanford_OpenNLP_returnClass
	public static String find_sentiment_stanford_OpenNLP_returnClass(String text){
		String class_sentiment="";
        Annotation document = new Annotation(text);
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.annotate(document);
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
//            System.out.println("---");
//            System.out.println(sentence);
//            System.out.println(sentence.get(SentimentAnnotatedTree.class));
            //System.out.println(sentence.get(SentimentClass.class));
            class_sentiment=sentence.get(SentimentClass.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(sentence.get(SentimentAnnotatedTree.class));
            //System.out.println(sentiment);
            
        }
		return class_sentiment;
	}
	//find_sentiment_stanford_OpenNLP_returnScore
	public static float find_sentiment_stanford_OpenNLP_returnScore(String text){
		float  sentiment_score=0.0f;
        Annotation document = new Annotation(text);
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        System.out.println("---------");
        pipeline.annotate(document);
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
        	System.out.println("one time");
//            System.out.println("---");
//            System.out.println(sentence);
//            System.out.println(sentence.get(SentimentAnnotatedTree.class));
            //System.out.println(sentence.get(SentimentClass.class));
            //class_sentiment=sentence.get(SentimentClass.class);
        	Tree tree=sentence.get(SentimentAnnotatedTree.class);
        	//tree.pennPrint();
        	//System.out.println(tree.score());
            sentiment_score = RNNCoreAnnotations.getPredictedClass(tree);
            
        }
        System.out.println("-----------");
        
		return sentiment_score;
	}
	
//	public static String test(){
//		String str_tree="";
//		try{
//			 // LexicalizedParser lp = new LexicalizedParser("//Users//lenin//OneDrive//jar//stanford-tagger//englishParser//englishPCFG.ser.gz");
//			String grammer="/Users/lenin/OneDrive/jar/stanfordparser-master/stanford-parser/models/englishPCFG.ser.gz";
//			  
//			// set up grammar and options as appropriate
//			LexicalizedParser lp = LexicalizedParser.loadModel(grammer);
//		 
//			String[] sent3 = { "movie", "was","very", "good","." };
//			// Parser gets tag of second "can" wrong without help                    
//			String[] tag3 = { "PRP", "VBD", "RB", "JJ","." };                             
//			List sentence3 = new ArrayList();
//			for (int i = 0; i < sent3.length; i++) {
//			  sentence3.add(new TaggedWord(sent3[i], tag3[i]));
//			}
//			Tree parse = lp.parse(sentence3);
//			  str_tree =parse.pennString();
//			  
//			  System.out.println("--------------");
//			  parse.pennPrint();
//			  System.out.println("--------------");
//			
//			int sentiment_score = RNNCoreAnnotations.getPredictedClass(parse);
//			System.out.println("score: "+sentiment_score);
//
//			
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		return str_tree;
//	}
	
	
	
	//
    public static void main(String[] args) throws Exception {
    	String text="\"queen elizabeth met pope francis for the first time on thursday and gave a bemused pontiff culinary delights from the royal estates, including a dozen eggs and a bottle of whisky";
    	System.out.println(	//find_sentiment_stanford_OpenNLP_returnClass(text)+" score:"
    			 			//+ ""+
    			 		find_sentiment_stanford_OpenNLP_returnScore("I am so happy.")+" test:"
    						);
    	
//    	 find_sentiment_stanford_OpenNLP.find_sentiment_stanford_OpenNLP_returnScore(text);
//    	
//    	String s=test();
//    	System.out.println("s -> "+s);
//    	
//    	Tree newT=Tree.valueOf(s);
//    	System.out.println("test:"+newT.pennString());
//    	String t="(ROOT  (S    (NP (PRP movie))    (VP (VBD was) (ADJP (RB very) (JJ good)))(. .)))";
//    	t="(ROOT  (S    (NP (PRP$ My) (NN dog))    (ADVP (RB also))    (VP (VBZ likes)      (S        (VP (VBG eating)          (NP (NN sausage)))))    (. .)))";
//    	t="(ROOT  (S    (NP (PRP I))    (VP (VBP am)      (ADJP (RB very) (RB much) (JJ happy)))    (. .)))";
//    	
//    	
//    	String str_tree="";
//    	String sentence="(ROOT (S (S (NP (NNP china)) (VP (VBD experimented) (PP (IN in) (NP (DT the) (NN past))) (PP (IN with) (NP (NP (JJ various) (JJ political) (NNS systems,)) (VP (VBG including) (NP (JJ multi-party) (NN democracy,))))))) (CC but) (S (NP (PRP it)) (VP (VBD did) (RB not) (VP (VB work,) (S (NP (NN president) (NN xi) (NN jinping)) (VP (VBD said) (PP (IN during) (NP (NP (DT a) (NN visit)) (S (VP (TO to) (VP (VB europe,) (VP (VBG warning) (SBAR (IN that) (S (S (VP (VBG copying) (NP (JJ foreign) (JJ political) (CC or) (NN development) (NNS models)))) (VP (MD could) (VP (VB be) (ADJP (JJ catastrophic)))))))))))))))))))";
//    	sentence="(ROOT  (S    (NP (PRP I))    (VP (VBP am)      (ADJP (RB very) (RB much) (JJ happy)))    (. .)))";
//    	int sentiment_score =0;
//		try{
//			 
//			Tree parse =  Tree.valueOf(sentence);
//			parse.pennPrint();
//			  
//			  //System.out.println("--------------");
//			  //parse.pennPrint();
//			  //System.out.println("--------------");
//			
//			sentiment_score = RNNCoreAnnotations.getPredictedClass(parse);
//			
//			//System.out.println("input tree, score: "+sentiment_score +"  "+RNNCoreAnnotations.getPredictions(parse));	
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		 
//    	
//    	
    	
    	//System.out.println(t);
    	//find_sentiment_inputDependencyTreeSentenceString(t);
    	
    }
}