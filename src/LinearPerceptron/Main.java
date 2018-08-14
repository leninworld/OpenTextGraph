package LinearPerceptron;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Main 
{
	
	public static void main(String[] args)
	{
		//**********wrapper_to_run_() does what this main method does**********
		//******manually change here ********/
		int number_of_cross_validation=2;
		String baseFolder="";
		//**************/
		
		ArrayList<Document> trainingDocuments;
		ArrayList<Document> testDocuments;
		HashMap<String, Double> w;
		Statistics statistics;
		double sumPrecision = 0;
		double sumRecall = 0;
		double sumF1 = 0;
		double sumAccuracy = 0;
		double averagePrecision = 0;
		double averageRecall = 0;
		double averageF1=0;
		double averageAccuracy = 0;
		boolean isSOPprint=false;

		System.out.println("Machine Learning - Homework 1 - Perceptron");
		//Measure of time
		long start = System.currentTimeMillis();
		
		for(int testSet = 1; testSet <= number_of_cross_validation; testSet++) //CROSS-VALIDATION
		{
			System.out.println("Computation for testSet = " + testSet);
			
			File f = new File("preprocessed_training_data_" + testSet + ".txt");
			System.out.println( "f.file->"+f.getAbsolutePath() );

			/* --------------------------------- Step 1 : Preprocessing the data --------------------------------- */
			//Process the data (training and test) and save the processed data in a file
			//We only do that if the data is not already preprocessed, hence if the text files are not already created
			if( !(new File("preprocessed_training_data_" + testSet + ".txt")).exists() || !(new File("preprocessed_test_data_" + testSet + ".txt")).exists())
				processData(testSet);

			//Get the processed data from the file into the ArrayLists trainingDocuments and testDocuments
			trainingDocuments = getProcessData(baseFolder, true, testSet, number_of_cross_validation, isSOPprint);
			testDocuments = getProcessData(baseFolder, false, testSet, number_of_cross_validation, isSOPprint);
			//printData(trainingDocuments);
			//printData(testDocuments);

			/* --------------------------------- Step 2 : Learning from the training data (training set) --------------------------------- */
			w = learningData(trainingDocuments, isSOPprint);
			//printCoefficients(w);

			/* --------------------------------- Step 3 : Apply the learnt coefficients to the test data (test set) --------------------------------- */
			statistics = testData(testDocuments, w, isSOPprint);
			System.out.println("Statistics");
			System.out.println("Precision : " + statistics.getPrecision());
			sumPrecision += statistics.getPrecision();
			System.out.println("Recall : " + statistics.getRecall());
			sumRecall += statistics.getRecall();
			System.out.println("F1 : " + statistics.getF1());
			sumF1 += statistics.getF1();
			sumAccuracy+=statistics.getAccuracy();
		}
		
		long end = System.currentTimeMillis();
		long timeNeeded = end-start;
		timeNeeded /= 1000;
		
		averagePrecision = sumPrecision / (double)number_of_cross_validation;
		averageRecall = sumRecall / number_of_cross_validation;
		averageF1 = sumF1 / number_of_cross_validation;
		averageAccuracy=sumAccuracy/number_of_cross_validation;
		//average 
		averagePrecision = (double) Math.round(averagePrecision * 1000) / 1000;
		averageRecall = (double) Math.round(averageRecall * 1000) / 1000;
		averageF1 = (double) Math.round(averageF1 * 1000) / 1000;		
		System.out.println("**************************************");
		System.out.println("FINAL STATISTICS (time needed : " + timeNeeded + " seconds)");
		System.out.println("Average precision = " + averagePrecision);
		System.out.println("Average recall = " + averageRecall);
		System.out.println("Average f1 = " + averageF1);
		System.out.println("Average accuracy = " + averageAccuracy);
		System.out.println("**************************************");
	}
	
	// wrapper_to_run_
	public static void wrapper_to_run_(String baseFolder){
		//******manually change here ********/
		int number_of_cross_validation=2;
		//**************/
		
		ArrayList<Document> trainingDocuments;
		ArrayList<Document> testDocuments;
		HashMap<String, Double> w;
		Statistics statistics;
		double sumPrecision = 0;
		double sumRecall = 0;
		double sumF1 = 0;
		double sumAccuracy = 0;
		double averagePrecision = 0;
		double averageRecall = 0;
		double averageF1=0;
		double averageAccuracy = 0;
		boolean isSOPprint=false;

		System.out.println("Machine Learning - Homework 1 - Perceptron");
		//Measure of time
		long start = System.currentTimeMillis();
		
		for(int testSet = 1; testSet <= number_of_cross_validation; testSet++) //CROSS-VALIDATION
		{
			System.out.println("Computation for testSet = " + testSet);
			
			File f = new File("preprocessed_training_data_" + testSet + ".txt");
			System.out.println( "f.file->"+f.getAbsolutePath() );

			/* --------------------------------- Step 1 : Preprocessing the data --------------------------------- */
			//Process the data (training and test) and save the processed data in a file
			//We only do that if the data is not already preprocessed, hence if the text files are not already created
			if( !(new File(baseFolder+"preprocessed_training_data_" + testSet + ".txt")).exists() 
				|| !(new File(baseFolder+"preprocessed_test_data_" + testSet + ".txt")).exists())
				processData(testSet);

			//Get the processed data from the file into the ArrayLists trainingDocuments and testDocuments
			trainingDocuments = getProcessData(baseFolder, true, testSet, number_of_cross_validation, isSOPprint);
			testDocuments = getProcessData(baseFolder, false, testSet, number_of_cross_validation, isSOPprint);
			//printData(trainingDocuments);
			//printData(testDocuments);

			/* --------------------------------- Step 2 : Learning from the training data (training set) --------------------------------- */
			w = learningData(trainingDocuments, isSOPprint);
			//printCoefficients(w);

			/* --------------------------------- Step 3 : Apply the learnt coefficients to the test data (test set) --------------------------------- */
			statistics = testData(testDocuments, w, isSOPprint);
			System.out.println("Statistics");
			System.out.println("Precision : " + statistics.getPrecision());
			sumPrecision += statistics.getPrecision();
			System.out.println("Recall : " + statistics.getRecall());
			sumRecall += statistics.getRecall();
			System.out.println("F1 : " + statistics.getF1());
			sumF1 += statistics.getF1();
			sumAccuracy+=statistics.getAccuracy();
		}
		
		long end = System.currentTimeMillis();
		long timeNeeded = end-start;
		timeNeeded /= 1000;
		
		averagePrecision = sumPrecision / (double)number_of_cross_validation;
		averageRecall = sumRecall / number_of_cross_validation;
		averageF1 = sumF1 / number_of_cross_validation;
		averageAccuracy=sumAccuracy/number_of_cross_validation;
		//average 
		averagePrecision = (double) Math.round(averagePrecision * 1000) / 1000;
		averageRecall = (double) Math.round(averageRecall * 1000) / 1000;
		averageF1 = (double) Math.round(averageF1 * 1000) / 1000;		
		System.out.println("**************************************");
		System.out.println("FINAL STATISTICS (time needed : " + timeNeeded + " seconds)");
		System.out.println("Average precision = " + averagePrecision);
		System.out.println("Average recall = " + averageRecall);
		System.out.println("Average f1 = " + averageF1);
		System.out.println("Average accuracy = " + averageAccuracy);
		System.out.println("**************************************");
	}
	
	//Process the data (training and test set)
	public static void processData(int testSet)
	{
		DataProcessing dataProcessing;
		String dataProcessed = "";
		File file;
		
		int trainingSet1;
		int trainingSet2=0;
		int trainingSet3=0;
		int trainingSet4=0;
		
		switch(testSet)
		{
			case 1:
				trainingSet1 = 2;
				trainingSet2 = 3;
				trainingSet3 = 4;
				trainingSet4 = 5;
				break;
			
			case 2:
				trainingSet1 = 1;
				trainingSet2 = 3;
				trainingSet3 = 4;
				trainingSet4 = 5;
				break;
				
			case 3:
				trainingSet1 = 1;
				trainingSet2 = 2;
				trainingSet3 = 4;
				trainingSet4 = 5;
				break;
			
			case 4:
				trainingSet1 = 1;
				trainingSet2 = 2;
				trainingSet3 = 3;
				trainingSet4 = 5;
				break;
				
			case 5:
				trainingSet1 = 1;
				trainingSet2 = 2;
				trainingSet3 = 3;
				trainingSet4 = 4;
				break;
			
			default:
				trainingSet1 = 1;
				trainingSet2 = 2;
				trainingSet3 = 3;
				trainingSet4 = 4;
				System.out.println("Error : the test set should be equal to 1, 2, 3, 4 or 5");
				System.exit(0);
				break;
		}
		
		/* ---------- Process the training data ------------ */
		dataProcessing = new DataProcessing(trainingSet1, trainingSet2, trainingSet3, trainingSet4);
		dataProcessed = dataProcessing.getDataProcessed();
		
		System.out.println("TRAINING - Preprocessed data : ");
		System.out.println(dataProcessed);
		
		/* ---------- Save the processed training data in a file ------------ */
		file = new File("preprocessed_training_data_" + testSet + ".txt");
		try {
			FileOutputStream os = new FileOutputStream(file);
			OutputStreamWriter or = new OutputStreamWriter(os);
			BufferedWriter on = new BufferedWriter(or);
			
			on.write(dataProcessed);
			
			on.close();
			or.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/* ---------- Process the test data ------------ */
		dataProcessing = new DataProcessing(testSet);
		dataProcessed = dataProcessing.getDataProcessed();
		
		System.out.println("TEST - Preprocessed data : ");
		System.out.println(dataProcessed);
		
		/* ---------- Save the processed test data in a file ------------ */
		file = new File("preprocessed_test_data_" + testSet + ".txt");
		try {
			FileOutputStream os = new FileOutputStream(file);
			OutputStreamWriter or = new OutputStreamWriter(os);
			BufferedWriter on = new BufferedWriter(or);
			
			on.write(dataProcessed);
			
			on.close();
			or.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Get the processed data from the file "preprocessed_training_data.txt" if training = true
	//Get the processed data from the file "preprocessed_test_data.txt" if training = false
	public static ArrayList<Document> getProcessData(
													 String baseFolder,
													 boolean training, int testSet,
													 int number_of_cross_validation,
													 boolean isSOPprint)
	{
		String debug_file_var="";
		File file;
		
		
		if(training){
			file = new File(baseFolder+"preprocessed_training_data_"+ testSet +".txt");
			debug_file_var=baseFolder+"preprocessed_training_data_"+ testSet +".txt";
		}
		else{
			file = new File(baseFolder+"preprocessed_test_data_"+ testSet +".txt");
			debug_file_var=baseFolder+"preprocessed_test_data_"+ testSet +".txt";
		}
		
		ArrayList<Document> documents = new ArrayList<Document>();
		String[] parts;
		String[] parts2;
		String word;
		double tfidf;
		String s;
		int classification;
		HashMap<String, Double> wordCountingMap;
		Document document;
		int counter = 0;
		
		try {
			FileInputStream is = new FileInputStream(file);
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader in = new BufferedReader(ir);
			
			//For one document
			while(( s = in.readLine()) != null)
			{
				wordCountingMap = new HashMap<String, Double>();
				parts = s.split(" ");
				classification = Integer.valueOf(parts[0]); //classification (1 or -1)
				//Construct the wordCountingMap of the document
				for(int i = 1; i < parts.length; i++)
				{
					parts2= parts[i].split(":");
					word = parts2[0];
					tfidf = Double.valueOf(parts2[1].replace("+", "").replace("-", ""));
					wordCountingMap.put(word, tfidf);
				}
				//Create the document
				document = new Document(classification, wordCountingMap);
				
				//Add the document to the ArrayList
				documents.add(document);
				counter++;
				if(isSOPprint){
					if(training)
						System.out.println("TRAINING - Constructing the data from the string of file " + counter+" on file="+debug_file_var);
					else
						System.out.println("TEST - Constructing the data from the string of file " + counter+" on file="+debug_file_var);
				
				}
				
			}
			//print once
			if(training)
				System.out.println("TRAINING - Constructing the data from the string of file " + counter+" on file="+debug_file_var);
			else
				System.out.println("TEST - Constructing the data from the string of file " + counter+" on file="+debug_file_var);
			
			in.close();
			ir.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return documents;
	}
	
	//Print the data from the ArrayList documents
	public static void printData(ArrayList<Document> documents)
	{
		String dataProcessed = "";
		Iterator<Document> iterDoc = documents.iterator();
		while(iterDoc.hasNext()) //for each document
		{
			Document document = iterDoc.next();
			if(document.getClassification() == 1)
				dataProcessed += "+1 ";
			else
				dataProcessed += "-1 ";
			
			HashMap<String, Double> wordCountingMap = document.getWordCountingMapTraining();
			Set<String> keySet = wordCountingMap.keySet();
			Iterator<String> iterKey = keySet.iterator();
			while(iterKey.hasNext()) //for each word, print the tfidf
			{
				String key = iterKey.next();
				double tfidf = wordCountingMap.get(key);
				tfidf = (double)Math.round(tfidf * 1000) / 1000;
				dataProcessed += key + ":" + tfidf + " ";
			}

			dataProcessed += "\n";
		}
		
		System.out.println("Preprocessed data : ");
		System.out.println(dataProcessed);
	}
	
	//Learning from the training set
	public static HashMap<String, Double> learningData(ArrayList<Document> documents, boolean isSOPprint)
	{
		double alpha = 0.10; //IMPORTANT : this is the learning rate of the learning algorithm
		int number_iterations = 1000; //IMPORTANT
		
		int p;
		Document document;
		HashMap<String, Double> wordCountingMap;
		Set<String> keySet;
		Iterator<String> iterKey;
		double tfidf;
		String key;
		int classification;
		double value;
		
		/* Variable HashMap that is modified now (through the learning), and that will be used to compute the classification later */
		HashMap<String, Double> w = new HashMap<String, Double>(); 
		
		/* Initialization of this variable : put all the reference words (50 per document) in the HashMap */
		Iterator<Document> iterDoc = documents.iterator();
		while(iterDoc.hasNext())
		{
			document = iterDoc.next();
			wordCountingMap = document.getWordCountingMapTraining();
			keySet = wordCountingMap.keySet();
			iterKey = keySet.iterator();
			while(iterKey.hasNext())
			{
				key = iterKey.next();
				if(!w.containsKey(key))
					w.put(key, 0.0);
			}
		}
		System.out.println("Learning...(iteration going).....");
		for(int i = 1; i <= number_iterations; i++)
		{
			if(isSOPprint)
				System.out.println("Learning...(iteration " + i + ")");
			iterDoc = documents.iterator();
			while(iterDoc.hasNext()) //for each document
			{
				document = iterDoc.next();
				classification = document.getClassification();
				wordCountingMap = document.getWordCountingMapTraining();
				keySet = wordCountingMap.keySet();

				/* Evaluation */
				p = 0;
				iterKey = keySet.iterator();
				while(iterKey.hasNext()) //for each word of the document
				{
					key = iterKey.next();
					tfidf = wordCountingMap.get(key);
					p += w.get(key) * tfidf; 
				}

				/* Correction */
				if(classification * p <= 0)
				{
					iterKey = keySet.iterator();
					while(iterKey.hasNext()) //for each word of the document
					{
						key = iterKey.next();
						tfidf = wordCountingMap.get(key);
						value = w.get(key);
						value += alpha * classification * tfidf;
						w.put(key, value);
					}
				}
			}
		}
		
		return w;
	}
	
	//Print the coefficients that are represented by HashMap w
	public static void printCoefficients(HashMap<String, Double> w)
	{
		System.out.println("Coefficients");
		
		String key;
		double coefficient;
		Set<String> keySet = w.keySet();
		Iterator<String> iterKey = keySet.iterator();
		while(iterKey.hasNext()) 
		{
			key = iterKey.next();
			coefficient = w.get(key);
			coefficient = (double)Math.round(coefficient * 1000) / 1000;
			System.out.println(key + " : " + coefficient);
		}
	}
	
	//Apply the learnt coefficients to the test data (test set)
	public static Statistics testData(ArrayList<Document> testDocuments, HashMap<String, Double> w,
			 								boolean isSOPprint)
	{
		Statistics statistics;
		Document document;
		int p; //prediction
		String key;
		double tfidf;
		int classification;
		int predicted_classification;
		int counter = 0;
		Set<String> keySet;
		Iterator<String> iterKey;
		HashMap<String, Double> wordCountingMap;
		
		/* Statistics */
		int true_positive = 0;
		int true_negative = 0;
		int false_positive = 0;
		int false_negative = 0;
		double precision;
		double recall;
		double f1;
		double accuracy;
		
		Iterator<Document> iterDoc = testDocuments.iterator();
		while(iterDoc.hasNext()) //for each document
		{
			counter++;
			document = iterDoc.next();
			wordCountingMap = document.getWordCountingMapTraining();
			classification = document.getClassification();
			p = 0;
			keySet = wordCountingMap.keySet();
			iterKey = keySet.iterator();
			while(iterKey.hasNext()) 
			{
				key = iterKey.next();
				tfidf = wordCountingMap.get(key);
				if(w.containsKey(key))
					p += w.get(key) * tfidf; 
			}
			
			if(p <= 0 ) 
				predicted_classification = -1; //Predicted class : hockey
			else 
				predicted_classification = 1; //Predicted class : basket
			
			if(classification == predicted_classification){
				if(isSOPprint)
				System.out.println("For document " + counter + ", the prediction is CORRECT");
			}
			else{
				if(isSOPprint)
				System.out.println("For document " + counter + ", the prediction is INCORRECT");
			}
			
			if( (classification == 1) && (predicted_classification == 1) )
				true_positive++;
			if( (classification == -1) && (predicted_classification == -1) )
				true_negative++;
			if( (classification == -1) && (predicted_classification == 1) )
				false_positive++;
			if( (classification == 1) && (predicted_classification == -1) )
				false_negative++;
		}
		
		System.out.println("Among the " + counter + " documents");
		System.out.println(true_positive + " \"basket\" documents were correctly classified");
		System.out.println(true_negative + " \"hockey\" documents were correctly classified");
		System.out.println(false_positive + " \"basket\" documents were incorrectly classified");
		System.out.println(false_negative + " \"hockey\" documents were incorrectly classified");
		
		precision = (double) true_positive / ((double) true_positive + (double) false_positive);
		recall = (double) true_positive / ((double) true_positive + (double) false_negative);
		f1 = 2 * precision * recall / (precision + recall);
		accuracy = (double)( true_positive + true_negative) / (double) (true_positive + true_negative +false_positive+false_negative);
		precision = (double) Math.round(precision * 1000) / 1000;
		recall = (double) Math.round(recall * 1000) / 1000;
		f1 = (double) Math.round(f1 * 1000) / 1000;
		
		statistics = new Statistics(precision, recall, f1, accuracy);
		return statistics;
	}
}
