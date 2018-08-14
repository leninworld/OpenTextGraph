package LinearPerceptron;
import java.io.File;
import java.util.HashMap;

/* This class represents a document and the statistical informations of each word it contains (HashMap of WordCounting object) */
public class Document 
{	
	protected File document;
	protected int classification;
	protected HashMap<String, WordCounting> wordCountingMap;
	protected HashMap<String, Double> wordCountingMapTraining; //used for the training
	protected int numberWords;
	
	public Document(File document, int classification, HashMap<String, WordCounting> wordCountingMap, int numberWords) 
	{
		this.document = document;
		this.classification = classification;
		this.wordCountingMap = wordCountingMap;
		this.numberWords = numberWords;
	}
	
	public Document(int classification, HashMap<String, Double> wordCountingMapTraining) 
	{
		this.classification = classification;
		this.wordCountingMapTraining = wordCountingMapTraining;
	}

	public File getDocument()
	{
		return document;
	}

	public void setDocument(File document) 
	{
		this.document = document;
	}

	public int getClassification() 
	{
		return classification;
	}

	public void setClassification(int classification) 
	{
		this.classification = classification;
	}

	public HashMap<String, WordCounting> getWordCountingMap() 
	{
		return wordCountingMap;
	}

	public void setWordCountingMap(HashMap<String, WordCounting> wordCountingMap) 
	{
		this.wordCountingMap = wordCountingMap;
	}
	
	public HashMap<String, Double> getWordCountingMapTraining() 
	{
		return wordCountingMapTraining;
	}

	public void setWordCountingMapTraining(HashMap<String, Double> wordCountingMapTraining) 
	{
		this.wordCountingMapTraining = wordCountingMapTraining;
	}
	
	public int getNumberWords() 
	{
		return numberWords;
	}

	public void setNumberWords(int numberWords) 
	{
		this.numberWords = numberWords;
	}

	
}
