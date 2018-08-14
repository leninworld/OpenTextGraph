package crawler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

	//node
	class myNode{
		int testFts;
		int predictedLabel=-1;
		int AllFeaturesCnt=0;
		List<defOneInstance> listInstances;
		myNode Children[];
		myNode parent;
		//
		myNode(myNode parent, List<defOneInstance> lstInstances){
			testFts=-1;
			int cnt[] = { 0, 0 };
			Children = new myNode[lstInstances.size()]; //re-check on the limit
			this.parent = parent;
			this.listInstances = lstInstances;
			AllFeaturesCnt = lstInstances.get(0).gNoFeatures.length;
			//update statistics for each label count in this set.
			for (defOneInstance currInstances: this.listInstances)
				//
				cnt[Integer.valueOf(currInstances.label)]++;
			if( cnt[0] > cnt[1])
				predictedLabel =  0; 
			else
				predictedLabel =  1;
		}

		//classify
		public int classify(defOneInstance t) {
			// System.out.println("test\t" + testFts);
			if (testFts == -1) {
				return predictedLabel;
			} else {
				if (Children[t.gNoFeatures[testFts]] != null) {
					return Children[t.gNoFeatures[testFts]].classify(t);
				} else {
					//out parent lab
					return -1;
				}
			}
		}
	
} //end of node class


	//define one instance
	class defOneInstance{
		int ID;
		static int FTSVALUERANGE;
		String instanceLine;
		//String[] tokenAttributes = line.split(" ");
		String label;
		int[] gNoFeatures;
		
		// TODO Auto-generated constructor stub
		public defOneInstance(int ID, String instanceLine) {
			String[] attributes=instanceLine.split(",");
			
			char[] lChar = attributes[0].toCharArray();
			int NoFeatures=lChar.length;
			this.gNoFeatures = new int[NoFeatures];
			//
			label=attributes[attributes.length-1];
			this.ID=ID;
			// Store each of feature value for this instance
			for (int i = 0; i < lChar.length; i++) {
				gNoFeatures[i]= Integer.valueOf(attributes[i]);
			}
		}
	}//end class
		


//id3
	public class MyID3 {

		
		
			private static final double threshold=.001;
			//abstract
			myNode root;
			
			//line2TokenMap
			public TreeMap<Integer, String> LineToTokenMap(String currLine){
				TreeMap<Integer, String> mapOut=new TreeMap<Integer, String>();
				try{
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return mapOut;
			}
			//
			public abstract static class baseImpure {
				public abstract double calc(int a, int b);

				public double SPurity(int parentPos, int parentNeg) {
					// TODO Auto-generated method stub
					return 0;
				}
			}
			//calculate prob of label1 or label2  
			//public static Impure2 extends Impure{
			public static baseImpure FnImpurity = new baseImpure() {
				public double  CalcOfImpure(double label1, double label2){
					//double label1=0.00; double label21=0.00;
					double problabel1=0.00 ; double problabel2=0.00;
					double out=0.00;
					try{
						problabel1=label1/(label1+label2);
						problabel2=label2/(label1+label2);
						if(problabel1>0)
							out += -problabel1 * Math.log(problabel1);
						if(problabel2>0)
							out += -problabel2 * Math.log(problabel2);
						//
						out=out/Math.log(2);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				return out;
				}

				@Override
				public double calc(int a, int b) {
					// TODO Auto-generated method stub
					return 0;
				}
	 
			};
			//read input file output list of instances
			public TreeMap<Integer,List<defOneInstance>> readFile2DS(String trainF, String testF,
																	 boolean FlagAtLast){
				File f=new File(trainF); String currLine="";
				int counter=0;
				List<defOneInstance> listTrainInstances = new ArrayList<defOneInstance>();
				List<defOneInstance> listTestInstances = new ArrayList<defOneInstance>();
				TreeMap<Integer,List<defOneInstance>> mapOut = new TreeMap<Integer,List<defOneInstance>>();
				BufferedReader inFile=null;
				if(!f.exists()){
					System.out.println("file not exists:"+trainF);
				}
				
				try {
					inFile=new BufferedReader(new FileReader(trainF));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				//ArrayList<String> l = new ArrayList<String>();
				
				try{
	
					// get only samples read
					while((currLine=inFile.readLine()) != null){
						//only samples get 
						counter++;
						defOneInstance currInstance=new defOneInstance(counter, currLine);
						listTrainInstances.add(currInstance);
					}
					inFile.close();
					mapOut.put(1, listTrainInstances);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				//read testing
				try {
					inFile=new BufferedReader(new FileReader(testF));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try{
				while((currLine=inFile.readLine()) != null){
					//only samples get 
					counter++;
					defOneInstance currInstance=new defOneInstance(counter, currLine);
					listTestInstances.add(currInstance);
				}
					inFile.close();
					mapOut.put(2, listTestInstances);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				return mapOut;
			}//end
		
	//		public static myNode buildTree(List<defOneInstance> listInstances, Impure  Impurefn) {
	//			myNode root = new myNode(listInstances);
	//			
	//		//	expand(root, Impurefn, ID3.chi_square_100, 0);
	//
	//			return root;
	//		}
			public static class ChiSquareTest {
				double threshold;
 
				ChiSquareTest(double threshold) {
					this.threshold = 0;//l:no pruning
				}
				// one type
				public boolean chiSquare(int[][] count) {
					double cSquare = 0;
					int lengthY = count[(int) cSquare].length;
					int lengthX = count.length;
					
					//margine
					double arraylengthY[] = new double[lengthY];
					double arraylengthX[] = new double[lengthX];
					
					double Total = 0;
					//initialized
					for (int i = 0; i < lengthX; i++) {
						for (int j = 0; j < lengthY; j++) {
							arraylengthY[i] += count[i][j];
							Total += count[i][j];
						}
					}
					for (int j = 0; j < lengthY; j++) {
						for (int i = 0; i < lengthX; i++) {
							arraylengthY[j] += count[i][j];
						}
					}
					//calc cSquare
					for (int i = 0; i < lengthX; i++) {
						for (int j = 0; j < lengthY; j++) {
							double E = 1.0 * arraylengthX[i] * arraylengthY[j] / Total;
							double O = count[i][j];
							if (E > 0) {
								cSquare += (E - O) * (E - O) / E;
							}
						}
					}
					return cSquare > threshold + 1e-8;
				}
			} //end
	
			//
			public myNode generate(List<defOneInstance> instances, baseImpure p) {
				myNode root = new myNode(null, instances);
				//l: no prune
				expand(root, p, 0);
				return root;
			}
			// more
			public void expand(myNode node, baseImpure p,
							   int depth) {
				int Positive=0; int Negative=0;
				double maxGain = -100000;
				int maxGainDecision = -1;
				int lengthFeat = node.listInstances.size();
				int ftsNum =  node.listInstances.get(0).gNoFeatures.length ;
				int mcount[][] = new int[defOneInstance.FTSVALUERANGE][2];

				int parentPos = 0, parentNeg = 0;
				//l:count number of positive and negative samples
				for (int i = 0; i < node.listInstances.size(); i++) {
					//l:
					if ( Integer.valueOf(node.listInstances.get(i).label) == 1) {
						Positive++;
					} else {
						Negative++;
					}
				}
				//l: iterate over each of all features
				for (int s = 0; s < node.AllFeaturesCnt; ++s) {

					int count[][] = new int[defOneInstance.FTSVALUERANGE][2];
					for (defOneInstance  t : node.listInstances) {
						if ( Integer.valueOf(t.label) == 1)
							count[t.gNoFeatures[s]][1]++;
						else
							count[t.gNoFeatures[s]][0]++;
					}
					double gain = p.SPurity(parentPos, parentNeg);
					//
					for (int i = 0; i < defOneInstance.FTSVALUERANGE; i++) {
						gain -= 1.0 * (count[i][0] + count[i][1])
									/ (parentPos + parentNeg)
								* p.SPurity(count[i][0], count[i][1]);
					}

					if (gain > maxGain) {
						maxGain = gain;
						maxGainDecision = s;
						for (int i = 0; i < defOneInstance.FTSVALUERANGE; i++) {
							mcount[i][0] = count[i][0];
							mcount[i][1] = count[i][1];
						}
					}

				}

				//l: stop building tree when maximum gain is zero
				if (maxGain > 1e-10 &&  chiSquare(mcount)) {
					//node. = maxGainDecision;

					//L:new array
					List<List<defOneInstance>> ts = new  ArrayList<List<defOneInstance>>();
					
					for (int i = 0; i < defOneInstance.FTSVALUERANGE; ++i) {
						ts.add(new ArrayList<defOneInstance>());
					}

					for (defOneInstance t : node.listInstances )
						ts.get(t.gNoFeatures[maxGainDecision]).add(t);

					/* Grow the tree recursively */
					for (int i = 0; i < defOneInstance.FTSVALUERANGE; i++) {
						if (maxGainDecision == 16 && i == 2) {
							int x = 0;
						}
						if (ts.get(i).size() > 0) {
							node.Children[i] = new myNode(node, ts.get(i));
							expand(node.Children[i], p, depth + 1);
						}
					}
				}
			}
			//
			private boolean chiSquare(int[][] mcount) {
				// TODO Auto-generated method stub
				return false;
			}
		 
			//
//			public void learn(List<defOneInstance> instances, baseImpure p) {
//				this.root = generate(instances, p );
//			}
			//
			public void learn(List<defOneInstance> instances) {
				this.root = generate(instances, MyID3.FnImpurity);
			}
			//classify
			public List<Integer> classify(List<defOneInstance> testInstances) {
				List<Integer> predictions = new ArrayList<Integer>();
				for (defOneInstance t : testInstances) {
		 
					int predictedCategory = root.classify(t);
					predictions.add(predictedCategory);
				}
				return predictions;
			}
			//computeAccuracy
			public static double computeAccuracy(List<Integer> ResultsPred,
												 List<defOneInstance> test) {
				double ans=0.00;
				  //
				  if(ResultsPred.size() == test.size()) {
					int Corr = 0, notCorr = 0;
					int i=0;
					while (i < ResultsPred.size()) {
						if (ResultsPred.get(i) == null) {
							notCorr++;
						} else if ( Integer.valueOf(test.get(i).label)//l:cast INT
								   	==	ResultsPred.get(i)
								  ) {
							Corr++;
						} else {
							notCorr++;
						}
						i++;
					}
					//l:calc answer
					ans= (double) Corr / ( (double) Corr + (double)notCorr);
					return ans;
				}
				else{ // l: not equal
					return 0;
				}
			}
		
		//l:random Forest
		public static void randomForest(){
			
		}
			
		//main
		public  void main(String[] args) {
			String Folder="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/ID3/";
			String trainFile=Folder+"soybean-small.data";
			String testFile=Folder+"soybean-small.data";
			try{
				int c=0; boolean FlagAtLast=true;
				//
				while(c < args.length ){
					System.out.println("input args[" +c+"]="+args[c]);
					c++;
				}
				if(args.length<3)
					System.out.println("No enough parameters run. Required:");
				else{
					
				}
				
				//read my input of train and test file
				TreeMap<Integer,List<defOneInstance>> mapOut=readFile2DS(trainFile,testFile,FlagAtLast);
				//train Instances
				List<defOneInstance> trainInstance=mapOut.get(1);
				List<defOneInstance> testInstance=mapOut.get(2);
				
				MyID3 myID= new MyID3();
				//train
				myID.learn(trainInstance);
				//test
				List<Integer> outPred =myID.classify(testInstance);
				myID.computeAccuracy(outPred, testInstance);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}

	/*
	Write a program that implements a Random Forest of unpruned ID3 trees. (You can assume categorical data and treat missing values 
	as separate value – e.g., congressional voting records, mushroom, and soybean datasets in UCI repository.)

	The program will take the number of trees and the data file name as an argument (runtime options) and 
	output a confusion matrix produced using 10-fold cross-validation. The Random Forest will use bootstrapping to select 
	the training data for the tree and will random select an attribute subset of size N, where N is the first integer less than 
	log2(M+1) and M is the number of available attributes. The out-of-bag accuracy estimate will be computed and output after 
	the addition of each tree.

	You will submit the source code along with compilation and execution instructions.
	
	http://www.cs.princeton.edu/courses/archive/spr07/cos424/assignments/boostbag/
	*/