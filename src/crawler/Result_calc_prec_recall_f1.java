package crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;

public class Result_calc_prec_recall_f1 {
	
	// input two files. each file has first column as label
	public static TreeMap<Integer, Double> result_calc_prec_recall_f1(
												String file1_GT, //ground truth (actual)
												String file2_pred, //predicted
												int    token_for_file1_having_label,
												int    token_for_file2_having_label,
												String delimiter_4_file1,
												String delimiter_4_file2,
												String label_for_TP,
												String label_for_TN,
												boolean isSOPprint
												){
		
		TreeMap<Integer, Double> map_Out=new TreeMap<Integer, Double>();
		int lineNumber=0; String line=""; int TP=0,TN=0, FP=0, FN=0;
		double precision=0.0; double recall=0.0, f1=0., accuracy=0. ; 
		try {
			BufferedReader reader1 = new BufferedReader(new FileReader(file1_GT));	
			BufferedReader reader2 = new BufferedReader(new FileReader(file2_pred));
			TreeMap<Integer, String> map_file1_LineNo_Label=new TreeMap<Integer, String>();
			TreeMap<Integer, String> map_file2_LineNo_Label=new TreeMap<Integer, String>();
			
			//read file1
			while ((line = reader1.readLine()) != null) {
				lineNumber++;//can be seen as docID
				String []s1=line.split(delimiter_4_file1);
				String currLabel=s1[token_for_file1_having_label-1];
				//OUTPUT MAP
				map_file1_LineNo_Label.put(lineNumber, currLabel);
			}
			lineNumber=0;
			//read file2  
			while ((line = reader2.readLine()) != null) {
				lineNumber++;//can be seen as docID	
				String []s2=line.split(delimiter_4_file2);
				String currLabel=s2[token_for_file2_having_label-1];
				map_file2_LineNo_Label.put(lineNumber, currLabel);
			}
			 
			//same line number check
			if(map_file1_LineNo_Label.size() != map_file2_LineNo_Label.size()){
				System.out.println("ERROR: no equal no lines:"+map_file1_LineNo_Label.size()+" "+map_file2_LineNo_Label.size());
				return map_Out;
			}
			
			// each line 
			for(int curr_lineNo:map_file1_LineNo_Label.keySet()){
				//
				String l_file1_GT=map_file1_LineNo_Label.get(curr_lineNo).replaceAll(" ", "").replaceAll("\t", "");
				String l_file2_pred=map_file2_LineNo_Label.get(curr_lineNo).replaceAll(" ", "").replaceAll("\t", "");
				// 
				if(isSOPprint)
					System.out.println("labels:"+l_file1_GT+"<->"+l_file2_pred+"<");
				
				if(l_file1_GT.equalsIgnoreCase(l_file2_pred) &&l_file1_GT.equalsIgnoreCase(label_for_TP) ){
					TP++;
				}
				else if (l_file1_GT.equalsIgnoreCase(l_file2_pred) &&l_file1_GT.equalsIgnoreCase(label_for_TN) ){
					TN++;
				}
				else if (l_file1_GT.equalsIgnoreCase(label_for_TN) &&l_file2_pred.equalsIgnoreCase(label_for_TP) ){
					FP++;
				}
				else if (l_file1_GT.equalsIgnoreCase(label_for_TP) &&l_file2_pred.equalsIgnoreCase(label_for_TN) ){
					FN++;
				}
			}
			
			//
			precision= TP/ (double) (TP+FP);
			recall= TP/ (double) (TP+FN);
			f1= 2*(precision*recall) / (precision+recall);
			accuracy = (TP + TN) / (double) (TP+TN+FP+FN);
			
			System.out.println("************************************");
			System.out.println("TP:"+TP+" TN:"+TN+" FP:"+FP+" FN:"+FN);
			System.out.println("precision:"+precision+" recall:"+recall+" f1:"+f1+" accuracy:"+accuracy);
			System.out.println("************************************");
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return map_Out;
	}
	
	//wrapper_for_2_fold_cross_Validation
	public static void wrapper_for_2_fold_cross_Validation(
														   ){
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	public static void main(String args[]) {
		
		String baseFolder="/Users/lenin/Downloads/Google Drive/## **** Machine Learning Tools/#problems/p18/";
		
		String file1_GT=baseFolder+"nb_output_SET.2_as_set.txt"; //ground truth (actual)
		String file2_pred=baseFolder+"tf-idf.txt_noStopWord_WORD_ID2.txt_format2.txt2_added_LABEL.txt.SET.2_label_mod..txt"; //predicted
		
		int    token_for_file1_having_label=1;
		int    token_for_file2_having_label=1;
		//
		String delimiter_4_file1=" ";
		String delimiter_4_file2=" ";
		//
		String label_for_TP="1";
		String label_for_TN="2";
		
		//
		result_calc_prec_recall_f1(
								 file1_GT, //ground truth (actual)
							     file2_pred, //predicted
							     token_for_file1_having_label,
							     token_for_file2_having_label,
							     delimiter_4_file1,
							     delimiter_4_file2,
							     label_for_TP,
							     label_for_TN,
							     false //isSOPprint
								 );
		
		
	}
	

}
