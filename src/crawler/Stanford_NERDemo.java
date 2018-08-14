package crawler;

import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.util.StringUtils;

import java.util.List;
import java.io.IOException;
public class Stanford_NERDemo {

    public static void main(String[] args) throws IOException {

      String serializedClassifier = "/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.all.3class.distsim.crf.ser.gz";

      if (args.length > 0) {
        serializedClassifier = args[0];
      }

      AbstractSequenceClassifier classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);

      /* For either a file to annotate or for the hardcoded text example,
         this demo file shows two ways to process the output, for teaching
         purposes.  For the file, it shows both how to run NER on a String
         and how to run it on a whole file.  For the hard-coded String,
         it shows how to run it on a single sentence, and how to do this
         and produce an inline XML output format.
      */
      if (args.length > 1) {
        String fileContents =  args[1];
        
        List<List<CoreLabel>> out = classifier.classify(fileContents);
        for (List<CoreLabel> sentence : out) {
          for (CoreLabel word : sentence) {
            System.out.print(word.word() + '/' + word.get(AnswerAnnotation.class) + ' ');
          }
          System.out.println();
        }
        out = classifier.classifyFile(args[1]);
        for (List<CoreLabel> sentence : out) {
          for (CoreLabel word : sentence) {
            System.out.print(word.word() + '/' + word.get(AnswerAnnotation.class) + ' ');
          }
          System.out.println();
        }

      } else {
        String s1 = "Good afternoon Rajat Raina, how are you today?";
        String s2 = "I go to school at Stanford University, which is located in California.";
        s2="Good afternoon Rajat Raina, how are you today?";
        System.out.println(classifier.classifyToString(s1));
        System.out.println(classifier.classifyWithInlineXML(s2));
        System.out.println(classifier.classifyToString(s2, "xml", true));
      }
    }

}