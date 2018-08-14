package crawler;


import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.TreeMap;

import javax.servlet.jsp.tagext.TryCatchFinally;

//import org.slf4j.LoggerFactory;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class Stanford_ArticleNlpRunner {

	  //@Test
	  public static void basic(boolean useRegexner) {
//	    LOG.debug("Starting Stanford NLP");

	    // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and
	    Properties props = new Properties();
	    useRegexner = true;
	    if (useRegexner) {
	      props.put("annotators", "tokenize, ssplit, pos, lemma, ner, regexner");
	      props.put("regexner.mapping", "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf2/location.txt");
	    } else {
	      props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
	    }
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    // // We're interested in NER for these things (jt->loc->sal)
	    String[] tests =
	        {
	            "Partial invoice (€100,000, so roughly 40%) for the consignment C27655 we shipped on 15th August to London from the Make Believe Town depot. INV2345 is for the balance.. Customer contact (Sigourney) says they will pay this on the usual credit terms (30 days). "
	        	+"My name is lenin."
	        };
	    List tokens = new ArrayList();

	    for (String s : tests) {

	      // run all Annotators on the passed-in text
	      Annotation document = new Annotation(s);
	      pipeline.annotate(document);

	      // these are all the sentences in this document
	      // a CoreMap is essentially a Map that uses class objects as keys and has values with
	      // custom types
	      List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	      StringBuilder sb = new StringBuilder();
	      
	      //I don't know why I can't get this code out of the box from StanfordNLP, multi-token entities
	      //are far more interesting and useful..
	      //TODO make this code simpler..
	      for (CoreMap sentence : sentences) {
	        // traversing the words in the current sentence, "O" is a sensible default to initialise
	        // tokens to since we're not interested in unclassified / unknown things..
	        String prevNeToken = "O";
	        String currNeToken = "O";
	        boolean newToken = true;
	        for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
	          currNeToken = token.get(NamedEntityTagAnnotation.class);
	          String word = token.get(TextAnnotation.class);
	          //System.out.println("word:"+word);
	          // Strip out "O"s completely, makes code below easier to understand
	          if (currNeToken.equals("O")) {
	            // LOG.debug("Skipping '{}' classified as {}", word, currNeToken);
	            if (!prevNeToken.equals("O") && (sb.length() > 0)) {
	              handleEntity(prevNeToken, sb, tokens);
	              newToken = true;
	            }
	            continue;
	          }

	          if (newToken) {
	            prevNeToken = currNeToken;
	            newToken = false;
	            sb.append(word);
	            continue;
	          }

	          if (currNeToken.equals(prevNeToken)) {
	            sb.append(" " + word);
	            System.out.println("sb1:"+sb);
	          } else {
	            // We're done with the current entity - print it out and reset
	            // TODO save this token into an appropriate ADT to return for useful processing..
	        	  System.out.println("sb2:"+sb);
	            handleEntity(prevNeToken, sb, tokens);
	            
	            newToken = true;
	          }
	          prevNeToken = currNeToken;
	        }
	        
	      }
	      
	      //TODO - do some cool stuff with these tokens!
	      //LOG.debug("We extracted {} tokens of interest from the input text", tokens.size());
	      
	    }
	  }
	  private static void handleEntity(String inKey, StringBuilder inSb, List inTokens) {
	    //LOG.debug("'{}' is a {}", inSb, inKey);
	    inTokens.add(new EmbeddedToken(inKey, inSb.toString()));
	    inSb.setLength(0);
	  }


	}
	    class EmbeddedToken {

	  private String name;
	  private String value;

	  public String getName() {
	    return name;
	  }

	  public String getValue() {
	    return value;
	  }

	  public   EmbeddedToken(String name, String value) {
	    super();
	    this.name = name;
	    this.value = value;
	  }
	
	
	 /// main
	 public static void main(String args[])
	 {
	 String content="Japan's Renesas Electronics Corp has talked with several companies, including Apple Inc, on a possible sale of its display chip design unit, a source familiar with the situation said on Wednesday."
			 			+"This is 100  USD.";
	 //4class used before
	 String serializedClassifier="/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.muc.7class.distsim.crf.ser.gz";
	 																							
	 CRFClassifier<CoreLabel>  classifier=CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	  
	 
//	 //LinkedHashMap<String, LinkedHashSet<String>> 
//	 
//	 //
//	 TreeMap<Integer,TreeMap<Integer,String>>  map = StanfordNER.identifyNER(content,
//																			"/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.conll.3class.distsim.crf.ser.gz",
//																			classifier,
//																			true, // is_override_with_model2
//																			true //isSOPprint
//																			);
//	     System.out.println("========");
//	     //
//		 for(int s:map.keySet()) {
//			 TreeMap<Integer,String> tmp=map.get(s);
//			 System.out.println("&&&&&"+s+"<-->"+map.get(s));
//			 //
//			 for(int t:tmp.keySet()){	 
//				 System.out.println(s+"<-->"+t+"<-->"+map.get(s).get(t));
//			 }
//		 }

	 long t0 = System.nanoTime();
	 Stanford_ArticleNlpRunner n=new Stanford_ArticleNlpRunner();
	 
	 n.basic(true);
	 
	 System.out.println("Time Taken (FINAL ENDED):"
				+ NANOSECONDS.toSeconds(System.nanoTime() - t0)
				+ " seconds; "
				+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
				+ " minutes");
	  
	 }

			

}
