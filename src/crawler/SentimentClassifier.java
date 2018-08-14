package crawler;

import java.io.File;
import java.io.IOException;

import com.aliasi.classify.Classification;
import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;

public class SentimentClassifier {  
    String[] categories;  
    static LMClassifier class2;  
    public SentimentClassifier() {  
    try {  
       //class2= (LMClassifier) AbstractExternalizable.readObject(new File("abc.txt"));  
       //categories = class2.categories();  
    }  
    catch (Exception e) {  
       e.printStackTrace();  
    }  
    }  
    public String classify(String text) {  
    	System.out.println("t:"+text);
    	Classification classification = class2.classify(text);  
    	return classification.bestCategory();  
    }  
 }  