package LinearPerceptron;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/* The class DataProcessing processes the data in a String format */
public class DataProcessing {
	
	protected String dataProcessed; //this is the string that represents the processed data issued from the 4 sets of documents
	protected String[] setAddresses; //address of the 4 sets of documents used for the training
	protected String mainFileAddress;
	protected ArrayList<Document> documents; //this ArrayList contains all the documents (File format) and for each document, the word counting
	
	public DataProcessing(int set1, int set2, int set3, int set4)
	{
		setAddresses = new String[4];
		mainFileAddress = "data 1";
		setAddresses[0] = mainFileAddress + "/s" + set1; //Set of documents 1
		if(set2>0) //#l
			setAddresses[1] = mainFileAddress + "/s" + set2; //Set of documents 2
		if(set3>0) //#l
			setAddresses[2] = mainFileAddress + "/s" + set3; //Set of documents 3
		if(set4>0) //#l
			setAddresses[3] = mainFileAddress + "/s" + set4; //Set of documents 4
		
		dataProcessed = "";
		documents = new ArrayList<Document>();
		
		System.out.println(setAddresses[0]+" "+setAddresses[1]+" "+setAddresses[2]+" "+setAddresses[3]);
		
		processData();
	}
	
	public DataProcessing(int set)
	{
		setAddresses = new String[1];
		mainFileAddress = "data 1";
		setAddresses[0] = mainFileAddress + "/s" + set; //Set of documents
		
		dataProcessed = "";
		documents = new ArrayList<Document>();
		processData();
	}
	
	public String getDataProcessed()
	{
		return dataProcessed;
	}
	
	/* The function processData processes the data into a String format */
	public void processData()
	{
		File file;
		File directory;
		String[] filesList;
		Document document;
		HashMap<String, WordCounting> wordCountingMap;
		String[] sports = {"baseball", "hockey"};
		int[] classifications = {1, -1};
		int counter = 0;
		int counterWords;
		WordCounting wordCounting;
		String key;
		Set<String> keySet;
		Iterator<String> iterKey;
		Iterator<Document> iterDoc;
		double tf = 0;
		double idf = 0;
		double tfidf = 0;
		double[] tfidf_array; //this array contains all the tfidf of a document
		double[] top_tfidf_array; //this array contains the 50-top tfidf of a document
		int numberWords;
		int incr = 0;
		int decr = 0;
		
		System.out.println("Processing the data...");
		/* ------------------- Step 1 : Store each document in an array (calculate tf (term frequency)) ------------------- */
		/* For each document : for each directory, for each sport */
		for(int i = 0; i < setAddresses.length; i++)
		{
			for(int j = 0; j < 2; j++)
			{
				directory = new File(setAddresses[i] + "/" + sports[j]);
				filesList = directory.list();
				for(int k = 0; k < filesList.length; k++)
				{
					/* Open the document */
					file = new File(directory + "/" + filesList[k]);
					
					/* Read the document and get the wordCountingMap of this document */ 
					wordCountingMap = countWords(file);
					
					/* Get the number of words of the document using wordCountingMap */
					keySet = wordCountingMap.keySet();
					iterKey = keySet.iterator();
					counterWords = 0;
					while(iterKey.hasNext())
					{
						key = iterKey.next();
						counterWords++;
					}
					
					/* Add the document to the ArrayList of documents */
					document = new Document(file, classifications[j], wordCountingMap, counterWords);
					documents.add(document);
					counter++;
					System.out.println("File " + counter + " added : " + file + " (" + "class : " + classifications[j] + " - words : " + counterWords + ")");
				}
			}
		}
		
		System.out.println(counter + " files have been added.");
		
		/* ------------------- Step 2 : Calculate the idf of each term in each document ------------------- */
		iterDoc = documents.iterator();	
		counter = 0;
		while(iterDoc.hasNext())
		{
			 document = iterDoc.next();
			 numberWords = document.getNumberWords();
			 tfidf_array = new double[numberWords];
			 top_tfidf_array = new double[50];
			 counterWords = 0;
			 wordCountingMap = document.getWordCountingMap();
			 keySet = wordCountingMap.keySet();
			 iterKey = keySet.iterator();
			 
			 while(iterKey.hasNext())
			 {
				 key = iterKey.next(); //this is the word that we want to calculate the idf
				 /* Calculation of the idf */
				 idf = calculateIdf(key);
				 
				 //Update of the WordCountingMap for that word (idf and tfidf)
				 wordCounting = wordCountingMap.get(key);
				 tf = wordCounting.getTf();
				 tfidf = tf*idf;
				 
			     wordCounting.setIdf(idf);
			     wordCounting.setTfidf(tfidf);
			     wordCountingMap.put(key, wordCounting);
				 
			     tfidf_array[counterWords] = tfidf;
			     
				 counterWords++;
			 }
			 
			 /* Take the 50 words with best tfidf from the document 
			  * Calculate which are the top tfidf */
			 Arrays.sort(tfidf_array); //Sorting using quicksort
			 decr = tfidf_array.length - 1;
			 incr = 0;
			 while(incr < 50 && decr >= 0)
			 {
				 top_tfidf_array[incr] = tfidf_array[decr]; 
				 decr--;
				 incr++;
			 }
					 
			 iterKey = keySet.iterator();
			 
			 while(iterKey.hasNext())  //Remove all the not-best tfidf words
			 {
				 key = iterKey.next();
				 wordCounting = wordCountingMap.get(key);
				 tfidf = wordCounting.getTfidf();
				 
				 boolean bool = false; //equals true if the tfidf was found among the 50 top ones
				 for(int i = 0; i < top_tfidf_array.length; i++)
				 {
					 if(top_tfidf_array[i] == tfidf)
					 {
						 bool = true;
						 break;
					 }
				 }
				 
				 if(! (tfidf != 0 && bool)) //if the tfidf was not found, remove the associated word
					 iterKey.remove();		
			 }
			 
			 counter++;
			 System.out.println("Calculation of idf and tfidf for document " + counter + " : " + document.getDocument() + " done");
		}
		
		 /* ------------------- Step 3 : Update "dataProcessed" ------------------- */ 
		 iterDoc = documents.iterator();
		 counter = 0;
		 while(iterDoc.hasNext()) //for each document
		 {
			 document = iterDoc.next();
			 if(document.getClassification() == 1)
				 dataProcessed += "+1 ";
			 else
				 dataProcessed += "-1 ";
			 wordCountingMap = document.getWordCountingMap();
			 keySet = wordCountingMap.keySet();
			 iterKey = keySet.iterator();
			 while(iterKey.hasNext()) //for each word, print the tfidf
			 {
				 key = iterKey.next();
				 wordCounting = wordCountingMap.get(key);
				 tfidf = wordCounting.getTfidf();
				 tfidf = (double)Math.round(tfidf * 1000) / 1000;
				 dataProcessed += key + ":" + tfidf + " ";
			 }
			 
			 dataProcessed += "\n";
			 counter++;
			 System.out.println("Update for output for document " + counter + " : " + document.getDocument() + " done");
		 }
	}
	
	/* Read the document and get the wordCounting (only update the tf) of this document */ 
	public HashMap<String, WordCounting> countWords(File file)
	{
		HashMap<String, WordCounting> wordCounting = new HashMap<String, WordCounting>();
		String s; 
		FileInputStream is;
		InputStreamReader ir;
		BufferedReader in;
		String[] words;
		String word;
		WordCounting value;
		double tf;
		
		try {
			is = new FileInputStream(file);
			ir = new InputStreamReader(is);
			in = new BufferedReader(ir);
			
			while(( s = in.readLine()) != null)
			{
				words = s.split(" ");
				for(int i = 0; i < words.length; i++)
				{
					word = words[i];
					word = word.toLowerCase(); //everything in lower case
					word = word.replaceAll("[^a-z]", ""); //only keep the letters
					if(word.length() >= 4) //only keep the words that have more than 4 characters
					{
						if(wordCounting.containsKey(word))
						{
							value =  wordCounting.get(word);
							tf = value.getTf();
							tf++;
							value.setTf(tf);
						}
						else
						{
							tf = 1;
							value = new WordCounting(tf);
						}
						
						wordCounting.put(word, value);
					}
				}
			}
				
			in.close();
			ir.close();
			is.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return wordCounting;
	}
	
	/* Calculate the idf of the word (the tf of the "documents" variable should have been calculated) */
	public double calculateIdf(String word)
	{
		double idf = 0;
		int counterDocumentWithWord = 0;
		int counterDocuments = 0;
		Iterator<Document> iterDoc = documents.iterator();
		Document document;
		HashMap<String, WordCounting> wordCountingMap;
		
		//We go through the list of documents to check if the word belongs to the document
		while(iterDoc.hasNext())
		{
			 document = iterDoc.next();
			 wordCountingMap = document.getWordCountingMap();
			 if(wordCountingMap.containsKey(word)) //that means the word is present in the document
				 counterDocumentWithWord++; //we increment the number of documents that contain the word
			 counterDocuments++;
		}
		
		idf = Math.log(counterDocuments / (counterDocumentWithWord + 1)); //calculation of the idf
		
		return idf;
	}
}
