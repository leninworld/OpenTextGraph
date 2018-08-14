package p21;

import java.io.IOException;
import java.util.TreeMap;

import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

import java.io.File;
import java.io.FileWriter;

public class Read_output_from_GBAD {
	
	// reading gbad output and find the anomalies
	public static void read_GBAD_output_and_pick_fileWise_anomalies(	
													String 		baseFolderINPUT,
													String  	groundTruth_CSV,
													String 		output_baseFolder
													){
		
		TreeMap<String, TreeMap<String,Integer>> map_FileName_OriginalVertext_N_FreqCount=
												new TreeMap<String, TreeMap<String,Integer>>();
		TreeMap<String, TreeMap<String,Integer>> map_FileName_InjectedAnomalyDeviceas_KEY_4GT=
												new TreeMap<String, TreeMap<String,Integer>>();
		TreeMap<String, Integer> map_OriginalVertext_N_FreqCount=new TreeMap<String, Integer>();
		try {
			FileWriter writerTP=new FileWriter(new File(output_baseFolder+"TP.txt"));
			FileWriter writerFP=new FileWriter(new File(output_baseFolder+"FP.txt"));
			FileWriter writerTN=new FileWriter(new File(output_baseFolder+"TN.txt"));
			FileWriter writerFN=new FileWriter(new File(output_baseFolder+"FN.txt"));
			
			FileWriter writerEvaluation=new FileWriter(new File(output_baseFolder+"debug_p28_eval_out.txt"));
			

			
			File f=new File(baseFolderINPUT);
			
			String [] arr_files= (f).list();
			int c=0;
			 String currFileNameStringINSIDEfileITSELF="";
			 
			 //
			 TreeMap<String, Integer> map_readONLY_oneFilePerDate_from_Folder=new TreeMap<String, Integer>();
			 
			 //split the ground truth CSV
			 String [] arr_groundTruth_=groundTruth_CSV.split(",");
			 int c2=0;
			 //
			 while(c2 < arr_groundTruth_.length){
				 String curr_GT_N_fileName=arr_groundTruth_[c2].replace("grep ","");
				 String[] arr_ =curr_GT_N_fileName.split(" "); 
				 String fileName=arr_[1];
				 String InjectedAnomalyDevice=arr_[0];
				 // 
				 if(map_FileName_InjectedAnomalyDeviceas_KEY_4GT.containsKey(fileName)){
					 TreeMap<String,Integer> tmp=map_FileName_InjectedAnomalyDeviceas_KEY_4GT.get(fileName);
					 tmp.put(InjectedAnomalyDevice, -1);
					 map_FileName_InjectedAnomalyDeviceas_KEY_4GT.put(fileName, tmp);
				 }
				 else{
					 //
					 TreeMap<String,Integer> tmp=new TreeMap<String, Integer>();
					 tmp.put(InjectedAnomalyDevice, -1);
					 map_FileName_InjectedAnomalyDeviceas_KEY_4GT.put(fileName, tmp);
				 }
				 
				 c2++;
			 }
			 int currFile_num_of_iter=0;
			 
			// read each file
			while(c<arr_files.length){
				String curr_File=baseFolderINPUT+ arr_files[c];
				
				System.out.println("curr_File:"+curr_File);
				
				TreeMap<Integer,String> map_seq_eachLine_currFile=
						ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
							.readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
																			curr_File, 
																		 	 -1, //startline, 
																			 -1, //endline,
																			 "f1 ", //debug_label
																			 false //isPrintSOP
																			 );
				boolean is_enabled_for_lastIteration=false;
				//each line in curr file
				for(int seq:map_seq_eachLine_currFile.keySet()){
					String eachLine=map_seq_eachLine_currFile.get(seq);
					//get
					//Input file..................... /data/plads2/initial_files_for_anom_detection/Jan14_2014-01-26_apartment_categorical.g
					
					if(eachLine.indexOf("Input file")>=0){
						 currFileNameStringINSIDEfileITSELF=eachLine.substring(eachLine.lastIndexOf("/"), eachLine.length());
						 
						 currFileNameStringINSIDEfileITSELF=currFileNameStringINSIDEfileITSELF.replace("/", "");
					}
					// this file already read from folder.. its a duplicate
					if(map_readONLY_oneFilePerDate_from_Folder.containsKey(currFileNameStringINSIDEfileITSELF)){
						break;
					}
					// get the number of iterations.
					// Iterations..................... 3
					if(eachLine.indexOf("Iterations.....................")>=0){
						currFile_num_of_iter= Integer.valueOf(eachLine.replace("Iterations..................... ", "").replace(" ", "") );
					}
//					System.out.println("currFile_num_of_iter:"+currFile_num_of_iter);
					
					// only process the lines after it crossed last iteration
					if(eachLine.indexOf("----- Iteration "+currFile_num_of_iter+" -----")>=0){
						is_enabled_for_lastIteration=true;
					}
					// 
					if(is_enabled_for_lastIteration==false){ continue; }
					//get
					//v 46177 "clotheswasher1" <-- anomaly (original vertex: 1 , in original example 6913)
					if(eachLine.indexOf("original vertex")>=0){
						
						 int beginIndex=eachLine.indexOf("\"")+1;
						 int endIndex=eachLine.indexOf("\"",beginIndex+1);
						
						 System.out.println("orig.vertex:"+eachLine.substring(beginIndex,endIndex)+" for file->"+currFileNameStringINSIDEfileITSELF  );
						 String currOriginalVertex=eachLine.substring(beginIndex,endIndex);
						 
						 //filename already exists
						 if(map_FileName_OriginalVertext_N_FreqCount.containsKey(currFileNameStringINSIDEfileITSELF)){
							 TreeMap<String,Integer> tmp=map_FileName_OriginalVertext_N_FreqCount.get(currFileNameStringINSIDEfileITSELF);
							 
							 if(tmp.containsKey(currOriginalVertex)){
								 int newcnt=tmp.get(currOriginalVertex)+1; 
								 tmp.put(currOriginalVertex, newcnt);
							 }
							 else{
								 tmp.put(currOriginalVertex, 1);
							 }
							 map_FileName_OriginalVertext_N_FreqCount.put(currFileNameStringINSIDEfileITSELF, tmp);
						 }
						 else{
							 TreeMap<String,Integer> tmp=new TreeMap<String, Integer>();
							 tmp.put(currOriginalVertex, 1);
							 map_FileName_OriginalVertext_N_FreqCount.put(currFileNameStringINSIDEfileITSELF, tmp);
						 }
					}
										
				} //for(int seq:map_seq_eachLine_currFile.keySet()){
				
				
				map_readONLY_oneFilePerDate_from_Folder.put(currFileNameStringINSIDEfileITSELF, -1);
				currFileNameStringINSIDEfileITSELF="";
				c++;
			}
			
			System.out.println("map_FileName_OriginalVertext_N_FreqCount:"+map_FileName_OriginalVertext_N_FreqCount);
			System.out.println("map_FileName_InjectedAnomalyDeviceas_KEY_4GT:"+map_FileName_InjectedAnomalyDeviceas_KEY_4GT);
			//
			writerEvaluation.append("map_FileName_OriginalVertext_N_FreqCount:"+map_FileName_OriginalVertext_N_FreqCount+"\n\n");
			writerEvaluation.append("map_FileName_InjectedAnomalyDeviceas_KEY_4GT:"+map_FileName_InjectedAnomalyDeviceas_KEY_4GT+"\n");
			writerEvaluation.flush();
			
			int TP=0, TN=0 , FP=0, FN=0;
			double precision=0.0; double recall=0;double accuracy=0.;
			
			int count_deDuplication=0;
			//each file of GBAD output , check against ground truth
			for( String eachFileName:map_FileName_OriginalVertext_N_FreqCount.keySet() ){
				
				map_OriginalVertext_N_FreqCount=map_FileName_OriginalVertext_N_FreqCount.get(eachFileName);
				// 
				for(String each_originalVertex:map_OriginalVertext_N_FreqCount.keySet()){
					// take only count == 1 
//					if(map_OriginalVertext_N_FreqCount.get(each_originalVertex)==1){

						if( map_OriginalVertext_N_FreqCount.get(each_originalVertex)==1){
							//
							if(map_FileName_InjectedAnomalyDeviceas_KEY_4GT.containsKey(eachFileName)){
								// is it true positive? is that device exists in GT
								if(map_FileName_InjectedAnomalyDeviceas_KEY_4GT.get(eachFileName).containsKey(each_originalVertex)){
									TP++;
									
									writerTP.append(eachFileName+" "+each_originalVertex+"\n");
									writerTP.flush();
								}
								else{
									FP++;
									writerFP.append(eachFileName+" "+each_originalVertex+"\n");
									writerFP.flush();
								}
							}
//							else{
//								TN++;
//							}
							
						}
						else if ( map_OriginalVertext_N_FreqCount.get(each_originalVertex)>1){
							//
							if(!map_FileName_InjectedAnomalyDeviceas_KEY_4GT.containsKey(eachFileName)){
								TN++;
								
								writerTN.append(eachFileName+" "+each_originalVertex+"\n");
								writerTN.flush();
							}
							else if(map_FileName_InjectedAnomalyDeviceas_KEY_4GT.containsKey(eachFileName)){
								
								TreeMap<String, Integer> map_temp_InjectedAnomalyDeviceas_KEY_4GT=
															map_FileName_InjectedAnomalyDeviceas_KEY_4GT.get(eachFileName);
								//
								if(map_temp_InjectedAnomalyDeviceas_KEY_4GT.containsKey(each_originalVertex)){
									FN++;
									writerFN.append(eachFileName+" "+each_originalVertex+"\n");
									writerFN.flush();
								}
								else{
									TN++;
									writerTN.append(eachFileName+" "+each_originalVertex+"\n");
									writerTN.flush();
								}
							}
							//
							 if(map_OriginalVertext_N_FreqCount.get(each_originalVertex)>1)
									 count_deDuplication+=map_OriginalVertext_N_FreqCount.get(each_originalVertex);
						}
						
//					}
//					else{
//						
//						if(map_FileName_InjectedAnomalyDeviceas_KEY_4GT.get(eachFileName).containsKey(each_originalVertex)){
//							 
//						}
//						
//						TN++;
//						
//					}
					
				} // for(String each_originalVertex:map_OriginalVertext_N_FreqCount.keySet()){
			}
			
			precision= 100 *(  TP / ((double) TP + FP));
			recall= 100 *(TP /( (double) TP + FN));
			double F1= (2 * ( precision * recall)) / (precision+ recall);
			double accruacy = 100 * (TP+TN) / (double)(TP+TN +FP+FN);
		
			writerEvaluation.append("\n\n map_OriginalVertext_N_FreqCount:"+map_OriginalVertext_N_FreqCount+"\n");
			writerEvaluation.flush();
			
			System.out.println("output->"+output_baseFolder+"debug_p28_eval_out.txt");
			System.out.println("output->"+output_baseFolder+"TP.txt");
			System.out.println("output->"+output_baseFolder+"TN.txt");
			System.out.println("output->"+output_baseFolder+"FN.txt");
			System.out.println("output->"+output_baseFolder+"FP.txt");
			System.out.println("TP:"+TP+" TN:"+TN+" FP:"+FP+" FN:"+FN);
			
			System.out.println("precision:"+precision+" recall:"+recall+" F1:"+F1 +" accuracy:"+accruacy);
			System.out.println("count_deDuplication:"+count_deDuplication);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	// main
    public static void main(String[] args) throws IOException {

    	//merge the GBAD output from process running in onefish and also from my local
    	
    	// from ONEFISH
    	// /Users/lenin/Downloads/reu-cdean/RawData/dailytry4/2014Only-numerictry2/categorical/ApartmentGBADInjected/gbadoutputFromOneFish
    	//from local laptop
    	// /Users/lenin/Downloads/reu-cdean/RawData/dailytry4/2014Only-numerictry2/categorical/ApartmentGBADInjected/gbadoutput
    	
    	// files from above two folders are merged into one folder as below
    	// /Users/lenin/Downloads/reu-cdean/RawData/dailytry4/2014Only-numerictry2/categorical/ApartmentGBADInjected/merged
    	
    	//input
    	String baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/dailytry4/2014Only-numerictry2/categorical/ApartmentGBADInjected/merged/";
    	//output
    	String output_baseFolder="/Users/lenin/Downloads/reu-cdean/RawData/dailytry4/2014Only-numerictry2/categorical/ApartmentGBADInjected/";
    	
    	//ground truth (taken from ReadMe.txt)
    	String groundTruth_CSV="grep jacuzzi1 Jan14_2014-01-10_apartment_categorical.g,"+
						       "grep poolpump1 Jan14_2014-01-12_apartment_categorical.g,"+
						       "grep poolpump1 Feb14_2014-02-10_apartment_categorical.g,"+
						       "grep winecooler1 Feb14_2014-02-11_apartment_categorical.g,"+
						       "grep winecooler1 March14_2014-03-11_apartment_categorical.g,"+
						       "grep poolpump1 March14_2014-03-12_apartment_categorical.g,"+
						       "grep poolpump1 April14_2014-04-12_apartment_categorical.g,"+
						       "grep winecooler1 April14_2014-04-11_apartment_categorical.g,"+
						       "grep jacuzzi1 May14_2014-05-11_apartment_categorical.g,"+
						       "grep poollight1 May14_2014-05-10_apartment_categorical.g,"+
						       "grep poollight1 June14_2014-06-10_apartment_categorical.g,"+
						       "grep poolpump1 June14_2014-06-12_apartment_categorical.g,"+
						       "grep poolpump1 July14_2014-07-12_apartment_categorical.g,"+
						       "grep poolpump1 July14_2014-07-10_apartment_categorical.g,"+
						       "grep poolpump1 Aug14_2014-08-10_apartment_categorical.g,"+
						       "grep poollight1 Aug14_2014-08-11_apartment_categorical.g,"+
						       "grep poollight1 Sept14_2014-09-11_apartment_categorical.g,"+
						       "grep winecooler1 Sept14_2014-09-10_apartment_categorical.g,"+
						       "grep winecooler1 Oct14_2014-10-10_apartment_categorical.g,"+
						       "grep poollight1 Oct14_2014-10-11_apartment_categorical.g,"+
						       "grep poollight1 Nov14_2014-11-11_apartment_categorical.g,"+
						       "grep jacuzzi1 Nov14_2014-11-12_apartment_categorical.g,"+
						       "grep jacuzzi1 Dec14_2014-12-12_apartment_categorical.g,"+
						       "grep jacuzzi1 Dec14_2014-12-13_apartment_categorical.g";
    	
    	// read_GBAD_output_and_pick_fileWise_anomalies
    	read_GBAD_output_and_pick_fileWise_anomalies(
    												baseFolder,
    												groundTruth_CSV,
    												output_baseFolder
    												);
    	
    }

}
